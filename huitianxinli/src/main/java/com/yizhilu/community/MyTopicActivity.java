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
import com.yizhilu.application.BaseActivity;
import com.yizhilu.community.utils.Address;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.GroupImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//社区-我的话题
public class MyTopicActivity extends BaseActivity {

	private LinearLayout back_layout;// back...
	private TextView title_text;// title...
	private PullToRefreshListView my_topic_list;// 我的话题列表
	private HolderView holderView = null;
	private AsyncHttpClient httpClient;// 网络连接
	private ProgressDialog progressDialog;// loading...
	private int userId, currentPage = 1;// 用户Id，当前页
	private List<EntityPublic> myTopicList;// 我的话题列表集合
	private List<String> imagesList;// 图片数组大于9的时候代数据
	private ImageLoader imageLoader;
	private MyTopicAdapter adapter;// 我的话题列表适配器

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_my_topic);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
		back_layout = (LinearLayout) findViewById(R.id.back_layout);
		my_topic_list = (PullToRefreshListView) findViewById(R.id.my_topic_list);
		my_topic_list.setMode(Mode.BOTH);
		my_topic_list.setOnRefreshListener(new OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				myTopicList.clear();
				currentPage = 1;
				my_topic_list.setMode(Mode.BOTH);
				getMyTopic(userId, currentPage);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				currentPage++;
				getMyTopic(userId, currentPage);
			}
		});
		title_text = (TextView) findViewById(R.id.title_text);
		progressDialog = new ProgressDialog(this);
		title_text.setText("我的话题");// 设置标题
		httpClient = new AsyncHttpClient();
		imageLoader = ImageLoader.getInstance();
		myTopicList = new ArrayList<EntityPublic>();
		imagesList = new ArrayList<String>();
		getMyTopic(userId, currentPage);// 获取我的话题列表
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);
		my_topic_list.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:
			MyTopicActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onItemClick(arg0, arg1, arg2, arg3);
		Intent intent = new Intent(MyTopicActivity.this, TopicDetailsActivity.class);
		intent.putExtra("topicId", myTopicList.get(arg2 - 1).getId());
		intent.putExtra("isTop", myTopicList.get(arg2 - 1).getTop());
		intent.putExtra("isEssence", myTopicList.get(arg2 - 1).getEssence());
		intent.putExtra("isFiery", myTopicList.get(arg2 - 1).getFiery());
		startActivity(intent);
	}

	// 获取我的话题
	private void getMyTopic(int userId, final int currentPage) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("page.currentPage", currentPage);
		Log.i("xm", Address.MYTOPICLIST + "?" + params);
		httpClient.post(Address.MYTOPICLIST, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				my_topic_list.onRefreshComplete();
				HttpUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							PageEntity pageEntity = publicEntity.getEntity().getPage();
							if (pageEntity.getTotalPageSize() <= currentPage) {
								my_topic_list.setMode(Mode.PULL_FROM_START);
							}
							List<EntityPublic> tempMyTopicList = publicEntity.getEntity().getTopicList();
							if (tempMyTopicList != null && tempMyTopicList.size() > 0) {
								for (int i = 0; i < tempMyTopicList.size(); i++) {
									myTopicList.add(tempMyTopicList.get(i));
								}
								if (adapter == null) {
									adapter = new MyTopicAdapter();
									my_topic_list.setAdapter(adapter);
								} else {
									adapter.notifyDataSetChanged();
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
				my_topic_list.onRefreshComplete();
			}
		});
	}

	// 我的话题列表适配器
	class MyTopicAdapter extends BaseAdapter {

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
				convertView = LayoutInflater.from(MyTopicActivity.this).inflate(R.layout.item_my_topic_list, parent,
						false);
				holderView.my_topic_title = (TextView) convertView.findViewById(R.id.my_topic_title);
				holderView.my_topic_content = (TextView) convertView.findViewById(R.id.my_topic_content);
				holderView.my_topic_comment = (TextView) convertView.findViewById(R.id.my_topic_comment);
				holderView.my_topic_praise = (TextView) convertView.findViewById(R.id.my_topic_praise);
				holderView.my_topic_browse = (TextView) convertView.findViewById(R.id.my_topic_browse);
				holderView.my_topic_image_list = (GroupImageView) convertView.findViewById(R.id.my_topic_image_list);
				holderView.checking = (TextView) convertView.findViewById(R.id.checking);
				holderView.new_topic = (TextView) convertView.findViewById(R.id.new_topic);
				holderView.top = (TextView) convertView.findViewById(R.id.top);
				holderView.essence = (TextView) convertView.findViewById(R.id.essence);
				holderView.fiery = (TextView) convertView.findViewById(R.id.fiery);
				convertView.setTag(holderView);
			} else {
				holderView = (HolderView) convertView.getTag();
			}
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
				new_topic, top, essence, fiery;;
		private GroupImageView my_topic_image_list;
	}

}
