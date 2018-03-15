package com.yizhilu.community;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.bugly.proguard.ad;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.community.adapter.FindListAdapter;
import com.yizhilu.community.adapter.GroupDetailTopicAdpater;
import com.yizhilu.community.adapter.GroupMembersAdapter;
import com.yizhilu.community.fragment.FindFragment;
import com.yizhilu.community.utils.Address;
import com.yizhilu.community.utils.LoadImageUtil;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.MessageEntity;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.HttpUtils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//社区-小组详情
public class GroupDetailActivity extends BaseActivity {

	private LinearLayout back_layout;// back...
	private TextView title_text;// title...
	private GridView group_detail_image_list;// 下组成员头像列表
	private AsyncHttpClient httpClient;// 网络连接
	private ProgressDialog progressDialog;// loading...
	private int groupId, userId;// 小组id,用户id
	private ImageLoader imageLoader;
	private GroupMembersAdapter membersAdpater;// 下组成员头像列表适配器
	private List<EntityPublic> groupMembersList;// 小组成员头像集合
	private List<EntityPublic> groupTopicList;// 小组详情的话题的集合
	private ImageView group_detail_avatar, add_topic;// 创建者头像，发表话题
	private PullToRefreshListView group_detail_topic_listView;// 小组详情的话题的列表
	private TextView group_detail_name, group_detail_members, group_detail_topic, group_detail_introduction;// 创建者姓名，小组成员，小组话题，小组介绍
	private GroupDetailTopicAdpater topicAdapter;// 小组详情话题列表的适配器
	private TextView isEmpty, join_group;// 当话题列表为空时,加入小组
	private ImageView more_members;// 更多成员
	private int currentPage = 1;// 当前页
	private Dialog dialog; // 退出显示的对话框
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_group_detail);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		Intent intent = getIntent();
		groupId = intent.getIntExtra("GroupId", 0);
		position = intent.getIntExtra("position", 0);
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
		imageLoader = ImageLoader.getInstance();
		back_layout = (LinearLayout) findViewById(R.id.back_layout);
		title_text = (TextView) findViewById(R.id.title_text);
		group_detail_avatar = (ImageView) findViewById(R.id.group_detail_avatar);
		group_detail_name = (TextView) findViewById(R.id.group_detail_name);
		group_detail_members = (TextView) findViewById(R.id.group_detail_members);
		group_detail_topic = (TextView) findViewById(R.id.group_detail_topic);
		join_group = (TextView) findViewById(R.id.join_group);
		group_detail_introduction = (TextView) findViewById(R.id.group_detail_introduction);
		add_topic = (ImageView) findViewById(R.id.add_topic);
		more_members = (ImageView) findViewById(R.id.more_members);
		isEmpty = (TextView) findViewById(R.id.isEmpty);
		group_detail_topic_listView = (PullToRefreshListView) findViewById(R.id.group_detail_topic_listView);
		group_detail_topic_listView.setMode(Mode.BOTH);
		title_text.setText("小组详情");// 设置标题
		group_detail_image_list = (GridView) findViewById(R.id.group_detail_image_list);
		httpClient = new AsyncHttpClient();
		progressDialog = new ProgressDialog(this);
		groupMembersList = new ArrayList<EntityPublic>();
		groupTopicList = new ArrayList<EntityPublic>();
		getGroupDetail(groupId, userId);// 获取小组详情
		getGroupDetailTopicList(groupId, currentPage);// 获取小组详情的话题列表
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);
		more_members.setOnClickListener(this);
		add_topic.setOnClickListener(this);
		join_group.setOnClickListener(this);
		group_detail_topic_listView.setOnItemClickListener(this);
		group_detail_topic_listView.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				groupTopicList.clear();
				currentPage = 1;
				group_detail_topic_listView.setMode(Mode.BOTH);
				getGroupDetailTopicList(groupId, currentPage);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				currentPage++;
				getGroupDetailTopicList(groupId, currentPage);
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back_layout:
			GroupDetailActivity.this.finish();
			break;
		case R.id.more_members:
			intent.setClass(GroupDetailActivity.this, GroupMembersActivity.class);
			intent.putExtra("GroupId", groupId);
			startActivity(intent);
			break;
		case R.id.add_topic:
			if (userId == 0) {
				Toast.makeText(GroupDetailActivity.this, "您还没有登录", 0).show();
				return;
			}
			checkIfCanAddTopic(groupId, userId);
			break;
		case R.id.join_group:
			if (userId == 0) {
				Toast.makeText(GroupDetailActivity.this, "您还没有登录", 0).show();
				return;
			}
			if (join_group.getText().equals("＋加入")) {
				joinGroup(groupId, userId);// 加入小组
			} else {
				exitDiaLog();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onItemClick(arg0, arg1, arg2, arg3);
		Intent intent = new Intent(GroupDetailActivity.this, TopicDetailsActivity.class);
		intent.putExtra("topicId", groupTopicList.get(arg2 - 1).getId());
		intent.putExtra("isTop", groupTopicList.get(arg2 - 1).getTop());
		intent.putExtra("isEssence", groupTopicList.get(arg2 - 1).getEssence());
		intent.putExtra("isFiery", groupTopicList.get(arg2 - 1).getFiery());
		startActivity(intent);
	}

	// 加入小组
	private void joinGroup(int groupId, int userId) {
		RequestParams params = new RequestParams();
		params.put("groupId", groupId);
		params.put("userId", userId);
		Log.i("xm", Address.JOINGROUP + "?" + params);
		httpClient.post(Address.JOINGROUP, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (!TextUtils.isEmpty(arg2)) {
					try {
						MessageEntity messageEntity = JSON.parseObject(arg2, MessageEntity.class);
						if (messageEntity.isSuccess()) {
							join_group.setText("－退出");
							Intent intent = new Intent();
							intent.setAction("Change");
							intent.putExtra("group", true);
							sendBroadcast(intent);
							Log.i("xm", "join");
							FindFragment.setType(position, 1);
							Toast.makeText(GroupDetailActivity.this, "加入成功!", 0).show();
						} else {
							Toast.makeText(GroupDetailActivity.this, "加入失败！", 0).show();
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

			}
		});
	}

	// 退出小组
	private void exitGroup(int groupId, int userId) {
		RequestParams params = new RequestParams();
		params.put("groupId", groupId);
		params.put("userId", userId);
		Log.i("xm", Address.EXITGROUP + "?" + params);
		httpClient.post(Address.EXITGROUP, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (!TextUtils.isEmpty(arg2)) {
					try {
						MessageEntity messageEntity = JSON.parseObject(arg2, MessageEntity.class);
						if (messageEntity.isSuccess()) {
							join_group.setText("＋加入");
							Intent intent = new Intent();
							intent.setAction("Change");
							intent.putExtra("group", true);
							sendBroadcast(intent);
							Log.i("xm", "exit");
							FindFragment.setType(position, 0);
							Toast.makeText(GroupDetailActivity.this, "退出成功!", 0).show();
						} else {
							Toast.makeText(GroupDetailActivity.this, "退出失败！", 0).show();
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

			}
		});
	}

	// 发表话题前验证
	private void checkIfCanAddTopic(final int groupId, int userId) {
		RequestParams params = new RequestParams();
		params.put("groupId", groupId);
		params.put("userId", userId);
		Log.i("xm", Address.CHECKIFCANADDTOPIC + "?" + params);
		httpClient.post(Address.CHECKIFCANADDTOPIC, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				HttpUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(arg2)) {
					try {
						MessageEntity messageEntity = JSON.parseObject(arg2, MessageEntity.class);
						if (messageEntity.isSuccess()) {
							Intent intent = new Intent();
							intent.setClass(GroupDetailActivity.this, AddTopicActivity.class);
							intent.putExtra("GroupId", groupId);
							startActivity(intent);
						} else {
							Toast.makeText(GroupDetailActivity.this, "你还不是该小组的成员，快加入他们发表话题吧！", 0).show();
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	// 获取小组详情
	private void getGroupDetail(int groupId, final int userId) {
		RequestParams params = new RequestParams();
		params.put("groupId", groupId);
		params.put("userId", userId);
		Log.i("xm", Address.GROUPINFO + "?" + params);
		httpClient.post(Address.GROUPINFO, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				HttpUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							if (publicEntity.getEntity().getGroup().getCusId() == userId) {
								join_group.setVisibility(View.GONE);
							} else {
								join_group.setVisibility(View.VISIBLE);
								if (publicEntity.getEntity().getJobType() != 0) {
									join_group.setText("－退出");
								} else {
									join_group.setText("＋加入");
								}
							}
							imageLoader.displayImage(Address.IMAGE + publicEntity.getEntity().getGroup().getImageUrl(),
									group_detail_avatar, LoadImageUtil.loadImageRound());
							group_detail_name.setText(publicEntity.getEntity().getGroup().getShowName());
							group_detail_members.setText("成员 " + publicEntity.getEntity().getGroup().getMemberNum());
							group_detail_topic.setText("话题 " + publicEntity.getEntity().getGroup().getTopicCounts());
							List<EntityPublic> tempGroupMembersList = publicEntity.getEntity().getGroupMembers();
							group_detail_introduction.setText(publicEntity.getEntity().getGroup().getIntroduction());
							if (tempGroupMembersList != null && tempGroupMembersList.size() > 0) {
								for (int i = 0; i < tempGroupMembersList.size(); i++) {
									groupMembersList.add(tempGroupMembersList.get(i));
								}
								if (membersAdpater == null) {
									membersAdpater = new GroupMembersAdapter(GroupDetailActivity.this,
											groupMembersList);
									group_detail_image_list.setAdapter(membersAdpater);
								} else {
									membersAdpater.notifyDataSetChanged();
								}
							}
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);

			}
		});
	}

	// 获取小组详情的话题列表
	private void getGroupDetailTopicList(int groupId, final int currentPage) {
		final RequestParams params = new RequestParams();
		params.put("groupId", groupId);
		params.put("page.currentPage", currentPage);
		Log.i("xm", Address.GROUPTOPICLIST + "?" + params + "---topiclist");
		httpClient.post(Address.GROUPTOPICLIST, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();

			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				group_detail_topic_listView.onRefreshComplete();
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							PageEntity pageEntity = publicEntity.getEntity().getPage();
							if (pageEntity.getTotalPageSize() <= currentPage) {
								group_detail_topic_listView.setMode(Mode.PULL_FROM_START);
							}
							List<EntityPublic> tempGroupTopicList = publicEntity.getEntity().getAllTopicList();
							if (tempGroupTopicList != null && tempGroupTopicList.size() > 0) {
								for (int i = 0; i < tempGroupTopicList.size(); i++) {
									groupTopicList.add(tempGroupTopicList.get(i));
								}
								if (topicAdapter == null) {
									topicAdapter = new GroupDetailTopicAdpater(GroupDetailActivity.this,
											groupTopicList);
									group_detail_topic_listView.setAdapter(topicAdapter);
								} else {
									topicAdapter.notifyDataSetChanged();
								}
							}
							group_detail_topic_listView.setEmptyView(isEmpty);
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				group_detail_topic_listView.onRefreshComplete();
				group_detail_topic_listView.setEmptyView(isEmpty);
			}
		});
	}

	private void exitDiaLog() {
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_show, null);
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = manager.getDefaultDisplay().getWidth();
		int scree = (width / 3) * 2;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.width = scree;
		view.setLayoutParams(layoutParams);
		dialog = new Dialog(this, R.style.custom_dialog);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
		TextView textView = (TextView) view.findViewById(R.id.texttitles);
		textView.setText("真的要退出小组吗 ?");
		TextView btnsure = (TextView) view.findViewById(R.id.dialogbtnsure);
		LinearLayout linbtnsure = (LinearLayout) view.findViewById(R.id.dialog_linear_sure);
		linbtnsure.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				exitGroup(groupId, userId);// 退出小组
				dialog.cancel();
			}
		});
		TextView btncancle = (TextView) view.findViewById(R.id.dialogbtncancle);
		LinearLayout linbtncancle = (LinearLayout) view.findViewById(R.id.dialog_linear_cancle);
		linbtncancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}

}
