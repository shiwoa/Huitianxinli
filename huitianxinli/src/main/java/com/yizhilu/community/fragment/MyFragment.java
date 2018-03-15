package com.yizhilu.community.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.community.MyGroupActivity;
import com.yizhilu.community.MyTopicActivity;
import com.yizhilu.community.TopicDetailsActivity;
import com.yizhilu.community.adapter.MyIntoGroupAdapter;
import com.yizhilu.community.utils.Address;
import com.yizhilu.community.utils.LoadImageUtil;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.view.CircleImageView;
import com.yizhilu.view.GroupImageView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

//社区-我的
public class MyFragment extends BaseGroupFragment implements OnItemClickListener, OnClickListener {

	private boolean isInited;// 组件是否初始化
	private boolean isLoaded;// 是否加载过
	private View mRootView;
	private AsyncHttpClient httpClient;// 网络连接
	private CircleImageView user_avatar_img;// 用户头像
	private TextView join_group_tv, all_topic_tv, empty_view;// 加入的小组,发表的话题，列表没有数据时显示的view
	private PullToRefreshListView my_topic_list;// 我的话题列表
	private GridView group_image_list;// 我的小组图片
	private int userId, currentPage = 1;// 用户ID，当前页
	private ImageLoader imageLoader;// 加载图片
	private MyTopicListAdapter topicListadapter;// 我的话题列表适配器
	private MyIntoGroupAdapter intoGroupAdapter;// 加入的小组的图片列表适配器
	private List<EntityPublic> myTopicList;// 我的话题集合
	private List<EntityPublic> intoGroupImageList;// 加入的小组的图片列表
	private HolderView holderView = null;
	boolean loadImg;// ??????
	private ImageView my_group_more;// 我的小组more
	private LinearLayout new_topic_layout, my_group_layout, my_topic_layout, login, unlogin;// 我的话题more,我发表的话题，我加入的小组,已登录的布局，未登录的布局
	private List<String> imagesList;// 图片数组大于9的时候代数据
	private ChangeBroadcastReceiver broadcastReceiver;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_group_my, container, false);
			initView();// 初始化组件
			isInited = true;
			Load();// 加载数据
		}
		return mRootView;
	}

	// 加载数据
	@Override
	protected void Load() {
		if (!isInited || !isVisible || isLoaded) {
			return;
		}
		getData(userId, currentPage);// 获取数据
		isLoaded = true;
	}

	// 初始化组件
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initView() {
		broadcastReceiver = new ChangeBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("Change");
		getActivity().registerReceiver(broadcastReceiver, intentFilter);
		userId = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).getInt("userId", 0);
		user_avatar_img = (CircleImageView) mRootView.findViewById(R.id.user_avatar_img);
		join_group_tv = (TextView) mRootView.findViewById(R.id.join_group_tv);
		all_topic_tv = (TextView) mRootView.findViewById(R.id.all_topic_tv);
		empty_view = (TextView) mRootView.findViewById(R.id.empty_view);
		my_topic_list = (PullToRefreshListView) mRootView.findViewById(R.id.my_topic_listView);
		group_image_list = (GridView) mRootView.findViewById(R.id.group_image_list);
		my_group_more = (ImageView) mRootView.findViewById(R.id.my_group_more);
		new_topic_layout = (LinearLayout) mRootView.findViewById(R.id.new_topic_layout);
		my_group_layout = (LinearLayout) mRootView.findViewById(R.id.my_group_layout);
		my_topic_layout = (LinearLayout) mRootView.findViewById(R.id.my_topic_layout);
		login = (LinearLayout) mRootView.findViewById(R.id.login);
		unlogin = (LinearLayout) mRootView.findViewById(R.id.unlogin);
		my_group_more.setOnClickListener(this);
		new_topic_layout.setOnClickListener(this);
		my_group_layout.setOnClickListener(this);
		my_topic_layout.setOnClickListener(this);
		my_topic_list.setOnItemClickListener(this);
		my_topic_list.setMode(Mode.BOTH);
		my_topic_list.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				currentPage = 1;
				myTopicList.clear();
				my_topic_list.setMode(Mode.BOTH);
				getData(userId, currentPage);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				currentPage++;
				getData(userId, currentPage);
			}
		});
		httpClient = new AsyncHttpClient();
		imageLoader = ImageLoader.getInstance();
		myTopicList = new ArrayList<EntityPublic>();
		intoGroupImageList = new ArrayList<EntityPublic>();
		imagesList = new ArrayList<String>();
		if (userId == 0) {
			login.setVisibility(View.GONE);
			unlogin.setVisibility(View.VISIBLE);
		} else {
			login.setVisibility(View.VISIBLE);
			unlogin.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.my_group_more:
			intent.setClass(getActivity(), MyGroupActivity.class);
			startActivity(intent);
			break;
		case R.id.my_group_layout:
			intent.setClass(getActivity(), MyGroupActivity.class);
			startActivity(intent);
			break;
		case R.id.new_topic_layout:
			intent.setClass(getActivity(), MyTopicActivity.class);
			startActivity(intent);
			break;
		case R.id.my_topic_layout:
			intent.setClass(getActivity(), MyTopicActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(getActivity(), TopicDetailsActivity.class);
		intent.putExtra("topicId", myTopicList.get(arg2 - 1).getId());
		intent.putExtra("isTop", myTopicList.get(arg2 - 1).getTop());
		intent.putExtra("isEssence", myTopicList.get(arg2 - 1).getEssence());
		intent.putExtra("isFiery", myTopicList.get(arg2 - 1).getFiery());
		startActivity(intent);
	}

	// 获取个人中心（我的）数据
	private void getData(int userId, final int currentPage) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("page.currentPage", currentPage);
		Log.i("xm", Address.SNSMY + "?" + params);
		httpClient.post(Address.SNSMY, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				my_topic_list.onRefreshComplete();
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							PageEntity pageEntity = publicEntity.getEntity().getPage();
							if (pageEntity.getTotalPageSize() <= currentPage) {
								my_topic_list.setMode(Mode.PULL_FROM_START);
							}
							List<EntityPublic> tempMyTopicList = publicEntity.getEntity().getTopicList();
							List<EntityPublic> tempIntoGroupImageList = publicEntity.getEntity().getGroupMembers();
							if (tempMyTopicList != null && tempMyTopicList.size() > 0) {
								for (int i = 0; i < tempMyTopicList.size(); i++) {
									myTopicList.add(tempMyTopicList.get(i));
								}
								if (topicListadapter == null) {
									topicListadapter = new MyTopicListAdapter();
									my_topic_list.setAdapter(topicListadapter);
								} else {
									topicListadapter.notifyDataSetChanged();
								}
							}
							my_topic_list.setEmptyView(empty_view);
							if (!loadImg) {
								imageLoader.displayImage(
										Address.IMAGE + publicEntity.getEntity().getUserExpandDto().getAvatar(),
										user_avatar_img, LoadImageUtil.loadImage());
								join_group_tv.setText(String.valueOf(publicEntity.getEntity().getGroupNo()));
								all_topic_tv.setText(String.valueOf(publicEntity.getEntity().getTopicNo()));
								if (tempIntoGroupImageList != null && tempIntoGroupImageList.size() > 0) {
									for (int i = 0; i < tempIntoGroupImageList.size(); i++) {
										intoGroupImageList.add(tempIntoGroupImageList.get(i));
									}
									if (intoGroupAdapter == null) {
										intoGroupAdapter = new MyIntoGroupAdapter(getActivity(), intoGroupImageList);
										group_image_list.setAdapter(intoGroupAdapter);
									} else {
										intoGroupAdapter.notifyDataSetChanged();
									}
								}
								loadImg = true;
							}
						}
					} catch (Exception e) {
						my_topic_list.setEmptyView(empty_view);
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				my_topic_list.onRefreshComplete();
				my_topic_list.setEmptyView(empty_view);
			}
		});
	}

	public class ChangeBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getBooleanExtra("group", false) && isLoaded) {
				updateMyGroup(userId, 1);
			}
			if (intent.getBooleanExtra("topic", false) && isLoaded) {
				updateMyTopic(userId, 1);
			}
		}
	}

	// 更新加入的小组
	private void updateMyGroup(int userId, final int currentPage) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("page.currentPage", currentPage);
		Log.i("xm", Address.SNSMY + "?" + params + "---update");
		httpClient.post(Address.SNSMY, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						List<EntityPublic> tempIntoGroupImageList = publicEntity.getEntity().getGroupMembers();
						join_group_tv.setText(String.valueOf(publicEntity.getEntity().getGroupNo()));
						intoGroupImageList.clear();
						if (tempIntoGroupImageList != null && tempIntoGroupImageList.size() > 0) {
							for (int i = 0; i < tempIntoGroupImageList.size(); i++) {
								intoGroupImageList.add(tempIntoGroupImageList.get(i));
							}
							if (intoGroupAdapter == null) {
								intoGroupAdapter = new MyIntoGroupAdapter(getActivity(), intoGroupImageList);
								group_image_list.setAdapter(intoGroupAdapter);
							} else {
								intoGroupAdapter.notifyDataSetChanged();
							}
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

	// 更新我的话题数据
	private void updateMyTopic(int userId, final int currentPage) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("page.currentPage", currentPage);
		httpClient.post(Address.SNSMY, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							List<EntityPublic> tempMyTopicList = publicEntity.getEntity().getTopicList();
							all_topic_tv.setText(String.valueOf(publicEntity.getEntity().getTopicNo()));
							if (tempMyTopicList != null && tempMyTopicList.size() > 0) {
								myTopicList.clear();
								for (int i = 0; i < tempMyTopicList.size(); i++) {
									myTopicList.add(tempMyTopicList.get(i));
								}
								topicListadapter.notifyDataSetChanged();
							}
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(broadcastReceiver);
	}

	// 我的话题列表适配器
	class MyTopicListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return myTopicList.size();
		}

		@Override
		public Object getItem(int position) {
			return myTopicList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holderView = new HolderView();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_my_topic_list, parent, false);
				holderView.my_topic_title = (TextView) convertView.findViewById(R.id.my_topic_title);
				holderView.my_topic_content = (TextView) convertView.findViewById(R.id.my_topic_content);
				holderView.my_topic_comment = (TextView) convertView.findViewById(R.id.my_topic_comment);
				holderView.my_topic_praise = (TextView) convertView.findViewById(R.id.my_topic_praise);
				holderView.my_topic_browse = (TextView) convertView.findViewById(R.id.my_topic_browse);
				holderView.checking = (TextView) convertView.findViewById(R.id.checking);
				holderView.new_topic = (TextView) convertView.findViewById(R.id.new_topic);
				holderView.top = (TextView) convertView.findViewById(R.id.top);
				holderView.essence = (TextView) convertView.findViewById(R.id.essence);
				holderView.fiery = (TextView) convertView.findViewById(R.id.fiery);
				holderView.my_topic_image_list = (GroupImageView) convertView.findViewById(R.id.my_topic_image_list);
				convertView.setTag(holderView);
			} else {
				holderView = (HolderView) convertView.getTag();
			}
			holderView.new_topic.setVisibility(View.VISIBLE);
			holderView.checking.setVisibility(View.VISIBLE);
			if (myTopicList.get(position).getIfAudit() == 1) {
				holderView.checking.setText("审");
				holderView.checking.setBackground(getResources().getDrawable(R.drawable.text_red_bg_solid_frame));
			} else if (myTopicList.get(position).getIfAudit() == 3) {
				holderView.checking.setText("驳回");
				holderView.checking.setBackground(getResources().getDrawable(R.drawable.text_gray_bg_solid_frame));
			} else if (myTopicList.get(position).getIfAudit() == 4) {
				holderView.checking.setText("冻结");
				holderView.checking.setBackground(getResources().getDrawable(R.drawable.text_gray_bg_solid_frame));
			}
			if (myTopicList.get(position).getTop() == 1) {
				holderView.top.setVisibility(View.VISIBLE);
			} else {
				holderView.top.setVisibility(View.GONE);
			}
			if (myTopicList.get(position).getEssence() == 1) {
				holderView.essence.setVisibility(View.VISIBLE);
			} else {
				holderView.essence.setVisibility(View.GONE);
			}
			if (myTopicList.get(position).getFiery() == 1) {
				holderView.fiery.setVisibility(View.VISIBLE);
			} else {
				holderView.fiery.setVisibility(View.GONE);
			}
			if (myTopicList.get(position).getHtmlImagesList() != null
					&& myTopicList.get(position).getHtmlImagesList().size() > 9) {
				for (int i = 0; i < myTopicList.get(position).getHtmlImagesList().size(); i++) {
					if (imagesList.size() < 9) {
						imagesList.add(myTopicList.get(position).getHtmlImagesList().get(i));
					}
				}
				holderView.my_topic_image_list.setPics(imagesList);
			} else {
				holderView.my_topic_image_list.setPics(myTopicList.get(position).getHtmlImagesList());
			}
			holderView.my_topic_title.setText(myTopicList.get(position).getTitle());
			holderView.my_topic_content.setText(Html.fromHtml(myTopicList.get(position).getContent()));
			holderView.my_topic_comment.setText(String.valueOf(myTopicList.get(position).getCommentCounts()));
			holderView.my_topic_praise.setText(String.valueOf(myTopicList.get(position).getPraiseCounts()));
			holderView.my_topic_browse.setText(String.valueOf(myTopicList.get(position).getBrowseCounts()));
			return convertView;
		}

	}

	class HolderView {
		private TextView my_topic_title, my_topic_content, my_topic_comment, my_topic_praise, my_topic_browse, checking,
				new_topic, top, essence, fiery;
		private GroupImageView my_topic_image_list;
	}

}
