package com.yizhilu.community.fragment;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.community.GroupDetailActivity;
import com.yizhilu.community.GroupMainActivity;
import com.yizhilu.community.TopicDetailsActivity;
import com.yizhilu.community.adapter.HotGroupListAdapter;
import com.yizhilu.community.adapter.HotGroupListAdapter.OnItemClickLitener;
import com.yizhilu.community.fragment.MyFragment.ChangeBroadcastReceiver;
import com.yizhilu.community.utils.Address;
import com.yizhilu.community.utils.LoadImageUtil;
import com.yizhilu.community.view.XListView;
import com.yizhilu.community.view.XListView.IXListViewListener;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.LoginActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.CircleImageView;
import com.yizhilu.view.GroupImageView;
import com.yizhilu.view.MSwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

//社区-热点
public class GroupFragment extends BaseGroupFragment
		implements OnItemClickListener, OnRefreshListener, OnClickListener, IXListViewListener {

	private static final String ERRORINFO = "加载失败了(⊙o⊙)，点击重新加载";
	private boolean isInited;// 初始化是否完成
	private boolean isLoaded;// 是否已经加载过
	private View mRootView;
	private XListView hot_topic_listView;// 热点话题列表
	private AsyncHttpClient httpClient;// 网络连接
	private int currentPage = 1;// 当前页
	private List<EntityPublic> hotTopicList;// 热点话题集合
	private HolderView holderView = null;
	private HotTopicAdapter adapter;// 热点话题列表适配器
	private ImageLoader imageLoader;
	private List<String> imagesList;// 图片数组大于9的时候代数据
	private TextView goto_find, loading, goto_login, empty_view;// 去发现页，加载listView显示的loading,去登陆
	private RecyclerView hot_group_list;
	// private MSwipeRefreshLayout hot_refresh;// 下拉刷新
	private boolean isLoad, isGroup;// listView数据是否已加载过，热点小组是否已加载过
	private ProgressBar load_progressBar;// 加载listView显示的ProgressBarloading
	private HotGroupListAdapter hotGroupAdapter;// 热点小组适配器
	private List<EntityPublic> hotGroupList;// 热点小组集合
	private LinearLayout isLogin, heder_my_group;// 是否登陆,我的小组
	private int userId;// 用户Id
	private long refreshTime; // 刷新时间
	private ChangeBroadcastReceiver broadcastReceiver;
	private ProgressDialog progressDialog; // 获取数据显示dialog

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_group, container, false);
			initView();// 初始化组件
			isInited = true;
			Load();// 加载数据
		}
		return mRootView;
	}

	// 加载数据
	@Override
	protected void Load() {
		// 如果初始化没有完成或者当前页面不可见或者数据已经加载过，则return不加载数据
		if (!isInited || !isVisible || isLoaded) {
			return;
		}
		isJoinGroup(userId);// 是否已加入小组
		getHotTopicList(currentPage);// 获取热点列表
		hotGroup();// 热门小组
		isLoaded = true;// 数据已加载完成
	}

	// 初始化组件
	private void initView() {
		broadcastReceiver = new ChangeBroadcastReceiver();
		progressDialog = new ProgressDialog(getActivity());
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("Change");
		getActivity().registerReceiver(broadcastReceiver, intentFilter);
		userId = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).getInt("userId", 0);
		hot_topic_listView = (XListView) mRootView.findViewById(R.id.hot_topic_listView);
		// hot_refresh = (MSwipeRefreshLayout)
		// mRootView.findViewById(R.id.hot_refresh);
		isLogin = (LinearLayout) mRootView.findViewById(R.id.isLogin);
		goto_login = (TextView) mRootView.findViewById(R.id.goto_login);
		empty_view = (TextView) mRootView.findViewById(R.id.empty_view);
		if (userId == 0) {
			isLogin.setVisibility(View.VISIBLE);
		} else {
			isLogin.setVisibility(View.GONE);
		}
		// hot_refresh.setColorSchemeResources(R.color.Blue, R.color.YellowPlus,
		// R.color.GrennPlus, R.color.PinkPlus);
		// listView头布局
		View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.hot_listview_header_view, null);
		// listView底布局
		// View footView =
		// LayoutInflater.from(getActivity()).inflate(R.layout.hot_listview_foot_view,
		// null);
		// loading = (TextView) footView.findViewById(R.id.loading);
		// load_progressBar = (ProgressBar)
		// footView.findViewById(R.id.load_progressBar);
		goto_find = (TextView) headerView.findViewById(R.id.goto_find);
		hot_group_list = (RecyclerView) headerView.findViewById(R.id.hot_group_list);
		heder_my_group = (LinearLayout) headerView.findViewById(R.id.heder_my_group);
		hot_group_list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
		hot_topic_listView.addHeaderView(headerView);// 添加头布局
		// hot_topic_listView.addFooterView(footView);// 添加底布局
		httpClient = new AsyncHttpClient();
		hotTopicList = new ArrayList<EntityPublic>();
		imagesList = new ArrayList<String>();
		hotGroupList = new ArrayList<EntityPublic>();
		imageLoader = ImageLoader.getInstance();
		// loading.setOnClickListener(this);
		goto_login.setOnClickListener(this);
		goto_find.setOnClickListener(this);
		// hot_refresh.setOnRefreshListener(this);
		hot_topic_listView.setOnItemClickListener(this);
		hot_topic_listView.setPullLoadEnable(true);
		hot_topic_listView.setXListViewListener(this);
		empty_view.setOnClickListener(this);
		// hot_topic_listView.setOnScrollListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.goto_find:
			// 切换到发现页
			GroupMainActivity.gotoFind();
			break;
		// case R.id.loading:
		// // 如果加载失败，点击重新加载
		// if (loading.getText().equals(ERRORINFO)) {
		// load_progressBar.setVisibility(View.VISIBLE);
		// loading.setText("正在加载...");
		// getHotTopicList(currentPage);
		// }
		// break;
		case R.id.goto_login:
			Intent intent = new Intent();
			intent.setClass(getActivity(), LoginActivity.class);
			intent.putExtra("formSNS", true);
			startActivity(intent);
			break;
		case R.id.empty_view:
			HttpUtils.showProgressDialog(progressDialog);
			isJoinGroup(userId);// 是否已加入小组
			getHotTopicList(currentPage);// 获取热点列表
			hotGroup();// 热门小组
			break;
		default:
			break;
		}
	}

	private void refreshTime() {
		long currentTimeMillis = System.currentTimeMillis();
		Date date = new Date(currentTimeMillis);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String format2 = format.format(date);
		hot_topic_listView.setRefreshTime(format2);
		// }
		refreshTime = currentTimeMillis;
	}

	@Override
	public void onLoadMore() {
		currentPage++;
		getHotTopicList(currentPage);
	}

	@Override
	public void onRefresh() {
		// 下拉刷新
		hotTopicList.clear();
		currentPage = 1;
		getHotTopicList(currentPage);
		isJoinGroup(userId);
		// 如果初始化加载热点小组数据失败则重新加载
		if (!isGroup) {
			hotGroup();
		}
	}

	// @Override
	// public void onRefresh() {
	// // 下拉刷新
	// hotTopicList.clear();
	// currentPage = 1;
	// getHotTopicList(currentPage);
	// isJoinGroup(userId);
	// // 如果初始化加载热点小组数据失败则重新加载
	// if (!isGroup) {
	// hotGroup();
	// }
	// }

	// @Override
	// public void onScroll(AbsListView view, int firstVisibleItem, int
	// visibleItemCount, int totalItemCount) {
	//
	// }
	//
	// @Override
	// public void onScrollStateChanged(AbsListView view, int scrollState) {
	// switch (scrollState) {
	// // 当列表滚动停止
	// case OnScrollListener.SCROLL_STATE_IDLE:
	// // 最后一个view的下标
	// int lastVisibleItem = view.getLastVisiblePosition() - 1;
	// // 当前listView的总数量
	// int totalItemCount = adapter.getCount();
	// // 当用户滑动到最后一个View并且手指离开屏幕并且没有加载过则加载更多
	// if (lastVisibleItem == totalItemCount && !isLoad) {
	// // true 已经加载过了，防止之前请求的数据没有加载完成，用户再次滑动，导致多次请求
	// isLoad = true;
	// currentPage++;
	// getHotTopicList(currentPage);
	// }
	// break;
	// default:
	// break;
	// }
	// }

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.hot_topic_listView:
			if (arg2 == 1) {
				return;
			}
			intent.setClass(getActivity(), TopicDetailsActivity.class);
			intent.putExtra("topicId", hotTopicList.get(arg2 - 2).getId());
			intent.putExtra("isTop", hotTopicList.get(arg2 - 2).getTop());
			intent.putExtra("isEssence", hotTopicList.get(arg2 - 2).getEssence());
			intent.putExtra("isFiery", hotTopicList.get(arg2 - 2).getFiery());
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	// 获取热点话题列表
	private void getHotTopicList(final int currentPage) {
		RequestParams params = new RequestParams();
		params.put("page.currentPage", currentPage);
		Log.i("xm", Address.HOTTOPICLIST + "?" + params + "---hottopic");
		httpClient.post(Address.HOTTOPICLIST, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				HttpUtils.exitProgressDialog(progressDialog);
				hot_topic_listView.stopRefresh();
				hot_topic_listView.stopLoadMore();
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							PageEntity pageEntity = publicEntity.getEntity().getPage();
							if (pageEntity.getTotalPageSize() < currentPage) {
								// isLoad = true;
								// load_progressBar.setVisibility(View.GONE);
								// loading.setText("没有更多了╮(╯▽╰)╭");
								Toast.makeText(getActivity(), "没有更多了", 0).show();
							}
							// else {
							// load_progressBar.setVisibility(View.VISIBLE);
							// loading.setText("正在加载...");
							// isLoad = false;
							// }
							List<EntityPublic> tempHotTopicList = publicEntity.getEntity().getTopics();
							if (tempHotTopicList != null && tempHotTopicList.size() > 0) {
								for (int i = 0; i < tempHotTopicList.size(); i++) {
									hotTopicList.add(tempHotTopicList.get(i));
								}
								if (adapter == null) {
									adapter = new HotTopicAdapter();
									hot_topic_listView.setAdapter(adapter);
								} else {
									adapter.notifyDataSetChanged();
								}
							}
							hot_topic_listView.setEmptyView(empty_view);
						}
					} catch (Exception e) {
						// load_progressBar.setVisibility(View.GONE);
						// loading.setText(ERRORINFO);
						hot_topic_listView.setEmptyView(empty_view);
					}
				}
				refreshTime();
				// hot_refresh.setRefreshing(false);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// hot_refresh.setRefreshing(false);
				// load_progressBar.setVisibility(View.GONE);
				// loading.setText(ERRORINFO);
				hot_topic_listView.stopRefresh();
				hot_topic_listView.stopLoadMore();
				hot_topic_listView.setEmptyView(empty_view);
			}
		});
	}

	// 热门小组
	private void hotGroup() {
		Log.i("xm", Address.HOTGROUP + "---hotgroup");
		httpClient.get(Address.HOTGROUP, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							List<EntityPublic> tempHotGroupList = publicEntity.getEntity().getHotGroupList();
							if (tempHotGroupList != null && tempHotGroupList.size() > 0) {
								for (int i = 0; i < tempHotGroupList.size(); i++) {
									hotGroupList.add(tempHotGroupList.get(i));
								}
								if (hotGroupAdapter == null) {
									hotGroupAdapter = new HotGroupListAdapter(hotGroupList, getActivity());
									hot_group_list.setAdapter(hotGroupAdapter);
									isGroup = true;
									hotGroupAdapter.setOnItemClickLitener(new OnItemClickLitener() {

										@Override
										public void onItemClick(View view, int position) {
											Intent intent = new Intent();
											intent.setClass(getActivity(), GroupDetailActivity.class);
											intent.putExtra("GroupId", hotGroupList.get(position).getId());
											intent.putExtra("position", position);
											startActivity(intent);
										}
									});
								} else {
									hotGroupAdapter.notifyDataSetChanged();
								}
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

	// 是否已加入小组
	private void isJoinGroup(int Id) {
		RequestParams params = new RequestParams();
		params.put("userId", Id);
		Log.i("xm", Address.JOINGROUPED + "?" + params);
		httpClient.post(Address.JOINGROUPED, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							if (publicEntity.getEntity().isHaveGroup()) {
								heder_my_group.setVisibility(View.GONE);
							} else {
								heder_my_group.setVisibility(View.VISIBLE);
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

	public class ChangeBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getBooleanExtra("group", false) && isLoaded) {
				heder_my_group.setVisibility(View.GONE);
			}
		}
	}

	public static String replaceTagHTML(String src) {
		String regex = "\\<(.+?)\\>";
		if (!TextUtils.isEmpty(src)) {
			return src.replaceAll(regex, "");
		}
		return "";
	}

	class HotTopicAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return hotTopicList.size();
		}

		@Override
		public Object getItem(int position) {
			return hotTopicList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				holderView = new HolderView();
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.item_hot_topic_list, parent, false);
				holderView.hot_topic_avatar = (CircleImageView) convertView.findViewById(R.id.hot_topic_avatar);
				holderView.hot_topic_author_nickName = (TextView) convertView
						.findViewById(R.id.hot_topic_author_nickName);
				holderView.hot_topic_author_name = (TextView) convertView.findViewById(R.id.hot_topic_author_name);
				holderView.hot_topic_title = (TextView) convertView.findViewById(R.id.hot_topic_title);
				holderView.hot_topic_content = (TextView) convertView.findViewById(R.id.hot_topic_content);
				holderView.hot_topic_comment = (TextView) convertView.findViewById(R.id.hot_topic_comment);
				holderView.hot_topic_praise = (TextView) convertView.findViewById(R.id.hot_topic_praise);
				holderView.hot_topic_browse = (TextView) convertView.findViewById(R.id.hot_topic_browse);
				holderView.hot_topic_createTime = (TextView) convertView.findViewById(R.id.hot_topic_createTime);
				holderView.isTop = (TextView) convertView.findViewById(R.id.isTop);
				holderView.isEssence = (TextView) convertView.findViewById(R.id.isEssence);
				holderView.isFiery = (TextView) convertView.findViewById(R.id.isFiery);
				holderView.image_list = (GroupImageView) convertView.findViewById(R.id.image_list);
				convertView.setTag(holderView);
			} else {
				holderView = (HolderView) convertView.getTag();
			}
			if (hotTopicList.get(position).getTop() == 1) {
				holderView.isTop.setVisibility(View.VISIBLE);
			} else {
				holderView.isTop.setVisibility(View.GONE);
			}
			if (hotTopicList.get(position).getEssence() == 1) {
				holderView.isEssence.setVisibility(View.VISIBLE);
			} else {
				holderView.isEssence.setVisibility(View.GONE);
			}
			if (hotTopicList.get(position).getFiery() == 1) {
				holderView.isFiery.setVisibility(View.VISIBLE);
			} else {
				holderView.isFiery.setVisibility(View.GONE);
			}
			if (hotTopicList.get(position).getHtmlImagesList() != null
					&& hotTopicList.get(position).getHtmlImagesList().size() > 9) {
				for (int i = 0; i < hotTopicList.get(position).getHtmlImagesList().size(); i++) {
					if (imagesList.size() < 9) {
						imagesList.add(hotTopicList.get(position).getHtmlImagesList().get(i));
					}
				}
				holderView.image_list.setPics(imagesList);
			} else {
				holderView.image_list.setPics(hotTopicList.get(position).getHtmlImagesList());
			}
			imageLoader.displayImage(Address.IMAGE + hotTopicList.get(position).getAvatar(),
					holderView.hot_topic_avatar, LoadImageUtil.loadImage());
			holderView.hot_topic_author_nickName.setText(hotTopicList.get(position).getNickName());
			holderView.hot_topic_author_name.setText(hotTopicList.get(position).getGroupName());
			holderView.hot_topic_title.setText(hotTopicList.get(position).getTitle());
			holderView.hot_topic_content.setText(replaceTagHTML(hotTopicList.get(position).getContent()));// Html.fromHtml(hotTopicList.get(position).getContent())
			holderView.hot_topic_comment.setText(String.valueOf(hotTopicList.get(position).getCommentCounts()));
			holderView.hot_topic_praise.setText(String.valueOf(hotTopicList.get(position).getPraiseCounts()));
			holderView.hot_topic_browse.setText(String.valueOf(hotTopicList.get(position).getBrowseCounts()));
			holderView.hot_topic_createTime.setText(hotTopicList.get(position).getCreateTime());
			return convertView;
		}
	}

	class HolderView {
		private CircleImageView hot_topic_avatar;
		private TextView hot_topic_author_nickName, hot_topic_author_name, hot_topic_title, hot_topic_content,
				hot_topic_comment, hot_topic_praise, hot_topic_browse, hot_topic_createTime, isTop, isEssence, isFiery;
		private GroupImageView image_list;
	}

}
