package com.yizhilu.community;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mob.tools.utils.UIHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.community.adapter.TopicCommentAdapter;
import com.yizhilu.community.utils.Address;
import com.yizhilu.community.utils.LoadImageUtil;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.MessageEntity;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.huitianxinli.ShareDialog;
import com.yizhilu.view.CircleImageView;
import com.yizhilu.view.NoScrollListView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Handler.Callback;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sina.weibo.SinaWeibo.ShareParams;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

//社区-话题详情
public class TopicDetailsActivity extends BaseActivity
		implements OnClickListener, PlatformActionListener, Callback, OnScrollListener {

	private static final String ERRORINFO = "加载失败了(⊙o⊙)，点击重新加载";
	private TextView title_text;// title
	private LinearLayout back_layout;// back
	private CircleImageView topic_details_avatar;// 用户头像
	private TextView topicTop, topicEssence, topicFiery, topic_praise, all_comment, topic_details_author,
			topic_group_name, topic_details_title, topic_details_createTime, topic_details_browse,
			topic_details_comment, topic_details_praise, loading; // 是否置顶,点赞数,全部评论,用户昵称，用户姓名，话题的标题，话题创建的时间，话题浏览数，话题评论，话题点赞
	private WebView topic_details_content;// 话题内容
	private LinearLayout comment_layout, praise_layout, share_layout;
	private AsyncHttpClient httpClient;// 网络连接
	private ProgressDialog progressDialog;// loading
	private int topicId;// 话题Id
	private int currentPage = 1;// 当前页
	private int userId;// 用户id
	private ListView topic_comment_listView;// 评论列表
	private TopicCommentAdapter adapter;// 评论列表适配器
	private List<EntityPublic> commentDtoList;// 评论集合
	private TextView topic_details_comment_empty;// 暂无评论
	private ImageLoader imageLoader;
	private int isTop, isEssence, isFiery;// 是否置顶，是否精华，是否火热
	private int counts;
	private boolean isCounts, isPraise, isCallBack;
	private ShareDialog shareDialog; // 分享
	private PublicEntity topicEntity;
	private ProgressBar load_progressBar;
	private boolean isLoad;
	private EntityPublic entityPublic;

	// 初始化组件
	@Override
	public void initView() {
		Intent intent = getIntent();
		topicId = intent.getIntExtra("topicId", 0);
		isTop = intent.getIntExtra("isTop", 0);
		isEssence = intent.getIntExtra("isEssence", 0);
		isFiery = intent.getIntExtra("isFiery", 0);
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
		imageLoader = ImageLoader.getInstance();
		title_text = (TextView) findViewById(R.id.title_text);
		title_text.setText("话题详情");// 设置标题
		back_layout = (LinearLayout) findViewById(R.id.back_layout);
		topic_details_comment = (TextView) findViewById(R.id.topic_details_comment);
		topic_details_praise = (TextView) findViewById(R.id.topic_details_praise);
		comment_layout = (LinearLayout) findViewById(R.id.comment_layout);
		praise_layout = (LinearLayout) findViewById(R.id.praise_layout);
		share_layout = (LinearLayout) findViewById(R.id.share_layout);
		topic_comment_listView = (ListView) findViewById(R.id.topic_comment_listView);
		httpClient = new AsyncHttpClient();
		commentDtoList = new ArrayList<EntityPublic>();
		progressDialog = new ProgressDialog(this);
		View headerView = LayoutInflater.from(TopicDetailsActivity.this).inflate(R.layout.topic_details_header, null);
		View footView = LayoutInflater.from(TopicDetailsActivity.this).inflate(R.layout.hot_listview_foot_view, null);
		loading = (TextView) footView.findViewById(R.id.loading);
		load_progressBar = (ProgressBar) footView.findViewById(R.id.load_progressBar);
		topicTop = (TextView) headerView.findViewById(R.id.topicTop);
		topicEssence = (TextView) headerView.findViewById(R.id.topicEssence);
		topicFiery = (TextView) headerView.findViewById(R.id.topicFiery);
		topic_details_avatar = (CircleImageView) headerView.findViewById(R.id.topic_details_avatar);
		topic_details_author = (TextView) headerView.findViewById(R.id.topic_details_author);
		topic_group_name = (TextView) headerView.findViewById(R.id.topic_group_name);
		topic_details_title = (TextView) headerView.findViewById(R.id.topic_details_title);
		topic_details_createTime = (TextView) headerView.findViewById(R.id.topic_details_createTime);
		topic_details_browse = (TextView) headerView.findViewById(R.id.topic_details_browse);
		topic_details_content = (WebView) headerView.findViewById(R.id.topic_details_content);
		topic_praise = (TextView) headerView.findViewById(R.id.topic_praise);
		all_comment = (TextView) headerView.findViewById(R.id.all_comment);
		topic_comment_listView.addHeaderView(headerView);
		topic_comment_listView.addFooterView(footView);
		// topic_details_author = (TextView)
		// findViewById(R.id.topic_details_author);
		// topic_group_name = (TextView) findViewById(R.id.topic_group_name);
		// topic_details_title = (TextView)
		// findViewById(R.id.topic_details_title);
		// topic_details_createTime = (TextView)
		// findViewById(R.id.topic_details_createTime);
		// topic_details_browse = (TextView)
		// findViewById(R.id.topic_details_browse);
		// topicTop = (TextView) findViewById(R.id.topicTop);
		// topicEssence = (TextView) findViewById(R.id.topicEssence);
		// topicFiery = (TextView) findViewById(R.id.topicFiery);
		// topic_praise = (TextView) findViewById(R.id.topic_praise);
		// all_comment = (TextView) findViewById(R.id.all_comment);
		// topic_details_avatar = (CircleImageView)
		// findViewById(R.id.topic_details_avatar);
		// topic_details_content = (WebView)
		// findViewById(R.id.topic_details_content);
		if (isTop == 1) {
			topicTop.setVisibility(View.VISIBLE);
		}
		if (isEssence == 1) {
			topicEssence.setVisibility(View.VISIBLE);
		}
		if (isFiery == 1) {
			topicFiery.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		currentPage = 1;
		commentDtoList.clear();
		getTopicCommentList(userId, topicId, currentPage);
		getTopicDetails(userId, topicId);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_topic_details);
		super.onCreate(savedInstanceState);
//		initView();// 初始化组件
//		addOnClick();// 添加点击事件
	}

	// 获取话题详情
	private void getTopicDetails(int userId, int topicId) {
		RequestParams params = new RequestParams();
		params.put("topicId", topicId);
		params.put("userId", userId);
		Log.i("xm", Address.TOPICINFO + "?" + params);
		httpClient.post(Address.TOPICINFO, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (!TextUtils.isEmpty(arg2)) {
					try {
						topicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (topicEntity.isSuccess()) {
							counts = topicEntity.getEntity().getTopic().getPraiseCounts();
							entityPublic = topicEntity.getEntity().getTopic();
							isCounts = true;
							if (isCallBack) {
								all_comment
										.setText(String.valueOf(topicEntity.getEntity().getTopic().getCommentCounts()));
								topic_details_comment
										.setText(String.valueOf(topicEntity.getEntity().getTopic().getCommentCounts()));
							} else {
								imageLoader.displayImage(
										Address.IMAGE + topicEntity.getEntity().getUserExpandDto().getAvatar(),
										topic_details_avatar, LoadImageUtil.loadImage());
								topic_details_author.setText(topicEntity.getEntity().getUserExpandDto().getNickname());
								topic_group_name.setText(topicEntity.getEntity().getGroup().getName());
								topic_praise
										.setText(String.valueOf(topicEntity.getEntity().getTopic().getPraiseCounts()));
								all_comment
										.setText(String.valueOf(topicEntity.getEntity().getTopic().getCommentCounts()));
								topic_details_title.setText(topicEntity.getEntity().getTopic().getTitle());
								topic_details_createTime.setText(topicEntity.getEntity().getTopic().getCreateTime());
								topic_details_browse
										.setText(String.valueOf(topicEntity.getEntity().getTopic().getBrowseCounts()));
								topic_details_content.loadDataWithBaseURL(null,
										topicEntity.getEntity().getTopic().getContent(), "text/html", "utf-8", null);
								topic_details_comment
										.setText(String.valueOf(topicEntity.getEntity().getTopic().getCommentCounts()));
								topic_details_praise
										.setText(String.valueOf(topicEntity.getEntity().getTopic().getPraiseCounts()));
								isCallBack = true;
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

	// 话题评论
	private void getTopicCommentList(int userId, int topicId, final int currentPage) {
		RequestParams params = new RequestParams();
		params.put("topicId", topicId);
		params.put("userId", userId);
		params.put("page.currentPage", currentPage);
		Log.i("xm", Address.TOPICCOMMENTLIST + "?" + params);
		httpClient.post(Address.TOPICCOMMENTLIST, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							PageEntity pageEntity = publicEntity.getEntity().getPage();
							if (pageEntity.getTotalPageSize() <= currentPage) {
								isLoad = true;
								load_progressBar.setVisibility(View.GONE);
								loading.setText("没有更多了╮(╯▽╰)╭");
							} else {
								load_progressBar.setVisibility(View.VISIBLE);
								loading.setText("正在加载...");
								isLoad = false;
							}
							List<EntityPublic> tempCommentDtoList = publicEntity.getEntity().getCommentDtoList();
							if (tempCommentDtoList != null && tempCommentDtoList.size() > 0) {
								for (int i = 0; i < tempCommentDtoList.size(); i++) {
									commentDtoList.add(tempCommentDtoList.get(i));
								}
							}
							if (adapter == null) {
								adapter = new TopicCommentAdapter(TopicDetailsActivity.this, commentDtoList);
								topic_comment_listView.setAdapter(adapter);
							} else {
								adapter.notifyDataSetChanged();
							}
						}
					} catch (Exception e) {
						load_progressBar.setVisibility(View.GONE);
						loading.setText(ERRORINFO);
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				load_progressBar.setVisibility(View.GONE);
				loading.setText(ERRORINFO);
			}
		});
	}

	// @Override
	// public void onScroll(AbsListView view, int firstVisibleItem, int
	// visibleItemCount, int totalItemCount) {
	//
	// }
	//
	// @Override
	// public void onScrollStateChanged(AbsListView view, int scrollState) {
	// Log.i("xm", "scrollState");
	// if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
	// int lastVisibleItem = view.getLastVisiblePosition();
	// int totalItemCount = adapter.getCount() - 1;
	// if (lastVisibleItem == totalItemCount) {
	// Toast.makeText(TopicDetailsActivity.this, "加载更多",
	// Toast.LENGTH_SHORT).show();
	// }
	// }
	// }

	// 话题点赞
	private void topicPraise(int topicId, int userId) {
		RequestParams params = new RequestParams();
		params.put("topicId", topicId);
		params.put("userId", userId);
		httpClient.post(Address.TOPICPRAISE, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (!TextUtils.isEmpty(arg2)) {
					MessageEntity messageEntity = JSON.parseObject(arg2, MessageEntity.class);
					if (messageEntity.isSuccess()) {
						int temp = counts++;
						isPraise = true;
						topic_praise.setText(String.valueOf(temp + 1));
						topic_details_praise.setText(String.valueOf(temp + 1));
						Toast.makeText(TopicDetailsActivity.this, "点赞成功", 0).show();
					} else {
						Toast.makeText(TopicDetailsActivity.this, "已经点过赞了", 0).show();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

			}
		});
	}

	// 设置点击事件
	@Override
	public void addOnClick() {
		loading.setOnClickListener(this);
		back_layout.setOnClickListener(this);
		topic_praise.setOnClickListener(this);
		praise_layout.setOnClickListener(this);
		comment_layout.setOnClickListener(this);
		share_layout.setOnClickListener(this);
		topic_group_name.setOnClickListener(this);
		topic_comment_listView.setOnScrollListener(this);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		// 当列表滚动停止
		case OnScrollListener.SCROLL_STATE_IDLE:
			// 最后一个view的下标
			int lastVisibleItem = view.getLastVisiblePosition() - 1;
			// 当前listView的总数量
			int totalItemCount = adapter.getCount();
			// 当用户滑动到最后一个View并且手指离开屏幕并且没有加载过则加载更多
			if (lastVisibleItem == totalItemCount && !isLoad) {
				// true 已经加载过了，防止之前请求的数据没有加载完成，用户再次滑动，导致多次请求
				isLoad = true;
				currentPage++;
				getTopicCommentList(userId, topicId, currentPage);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.back_layout:
			TopicDetailsActivity.this.finish();
			break;
		case R.id.topic_praise:
			if (userId == 0) {
				Toast.makeText(TopicDetailsActivity.this, "您还没有登录", 0).show();
			} else {
				if (isCounts) {
					if (!isPraise) {
						topicPraise(topicId, userId);
					} else {
						Toast.makeText(TopicDetailsActivity.this, "你已经点过赞了", 0).show();
					}
				}
			}
			break;
		case R.id.praise_layout:
			if (userId == 0) {
				Toast.makeText(TopicDetailsActivity.this, "您还没有登录", 0).show();
			} else {
				if (isCounts) {
					if (!isPraise) {
						topicPraise(topicId, userId);
					} else {
						Toast.makeText(TopicDetailsActivity.this, "你已经点过赞了", 0).show();
					}
				}
			}
			break;
		case R.id.comment_layout:
			if (userId == 0) {
				Toast.makeText(TopicDetailsActivity.this, "您还没有登录", 0).show();
			} else {
				intent.setClass(TopicDetailsActivity.this, AddCommentActivity.class);
				intent.putExtra("topicId", topicId);
				startActivity(intent);
			}
			break;
		case R.id.share_layout:
			if (userId == 0) {
				Toast.makeText(TopicDetailsActivity.this, "您还没有登录", 0).show();
			} else {
				if (shareDialog == null) {
					shareDialog = new ShareDialog(this, R.style.custom_dialog);
					shareDialog.setCancelable(false);
					shareDialog.show();
					shareDialog.setTopicCourse(entityPublic, false, false, false, true);
				} else {
					shareDialog.show();
				}
			}
			break;
		case R.id.topic_group_name:
			// intent.setClass(TopicDetailsActivity.this,
			// GroupDetailActivity.class);
			// intent.putExtra("GroupId",
			// topicEntity.getEntity().getGroup().getId());
			// startActivity(intent);
			break;
		case R.id.loading:
			// 如果加载失败，点击重新加载
			if (loading.getText().equals(ERRORINFO)) {
				load_progressBar.setVisibility(View.VISIBLE);
				loading.setText("正在加载...");
				getTopicCommentList(userId, topicId, currentPage);
			}
			break;
		default:
			break;
		}
	}

	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 == 0) { // qq好友
			ShareParams sp = new ShareParams();
			sp.setTitle("测试分享的标题");
			sp.setTitleUrl("http://www.baidu.com"); // 标题的超链接
			sp.setText("Text文本内容 http://www.baidu.com");
			Platform qzone = ShareSDK.getPlatform(QQ.NAME);
			qzone.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			qzone.share(sp);
		} else if (arg2 == 1) { // qq空间
			ShareParams sp = new ShareParams();
			sp.setTitle("测试分享的标题");
			sp.setTitleUrl("http://www.baidu.com"); // 标题的超链接
			sp.setText("Text文本内容 http://www.baidu.com");
			sp.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
			sp.setSite("sharesdk");
			sp.setSiteUrl("http://sharesdk.cn");
			Platform qzone = ShareSDK.getPlatform(QZone.NAME);
			qzone.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			qzone.share(sp);
		} else if (arg2 == 2) { // 腾讯微博
			ShareParams sp = new ShareParams();
			// sp.setTitle("测试分享的文本");
			sp.setText("测试分享的文本");
			// sp.setTitleUrl("http://www.baidu.com"); // 标题的超链接
			Platform weibo = ShareSDK.getPlatform(TencentWeibo.NAME);
			weibo.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			weibo.share(sp);
		} else if (arg2 == 3) { // 微信好友
			ShareParams sp = new ShareParams();
			sp.setText("测试分享的文本");
			Platform weibo = ShareSDK.getPlatform(Wechat.NAME);
			weibo.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			weibo.share(sp);
		} else if (arg2 == 4) { // 微信朋友圈
			ShareParams sp = new ShareParams();
			Platform weibo = ShareSDK.getPlatform(WechatMoments.NAME);
			sp.setText("Text文本内容 http://www.baidu.com");
			sp.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
			weibo.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			weibo.share(sp);
		} else if (arg2 == 5) { // 新浪微博
			ShareParams sp = new ShareParams();
			sp.setText("测试分享的文本");
			// sp.setImagePath("/mnt/sdcard/测试分享的图片.jpg");
			Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
			weibo.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			weibo.share(sp);
		}
	}

	@Override
	public void onCancel(Platform platform, int arg1) {
		// 取消
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = arg1;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
		Log.i("lala", "oncancel............");
	}

	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> arg2) {
		// 成功
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
		Log.i("lala", "onComplete............");
	}

	@Override
	public void onError(Platform arg0, int action, Throwable t) {
		// 失敗
		// 打印错误信息,print the error msg
		t.printStackTrace();
		// 错误监听,handle the error msg
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);
		Log.i("lala", "onError............");
	}

	public boolean handleMessage(Message msg) {
		switch (msg.arg1) {
		case 1: {
			// 成功
			Toast.makeText(this, "分享成功", 10000).show();
			System.out.println("分享回调成功------------");
		}
			break;
		case 2: {
			// 失败
			Toast.makeText(this, "分享失败", 10000).show();
		}
			break;
		case 3: {
			// 取消
			Toast.makeText(this, "分享取消", 10000).show();
		}
			break;
		}

		return false;

	}
}
