package com.yizhilu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.PackageReviewsAdapter;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

/**
 * 
 * @author liuchangqi 修改人: 时间:2016-7-19 上午11:17:06 类说明:套餐评论
 */
public class PackageReviewsFragment extends BaseFragment {
	private View inflate;// 套餐评论的总布局
	private static PackageReviewsFragment packageReviewsFragement;
	private NoScrollListView discuss_listView;
	private List<EntityPublic> hotTopicList;// 热点话题集合
	private PackageReviewsAdapter adapter;// 评论的适配器
	private LinearLayout no_discuss_layout;// 没有数据的布局
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private PullToRefreshScrollView refreshScrollView;
	private int page = 1;
	private List<EntityPublic> entityPublics; // 课程评论列表的实体
	private int comboId;// 课程ID
	private int userId;// 用户ID
	private PublicEntity publicEntity;
	private int kpointId;
	private TextView send_message;// 发送按钮
	private EditText discuss_settext;

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_package_reviews, container, false);
		publicEntity = (PublicEntity) getArguments().getSerializable("publicEntity"); // 得到课程详情的实体
		getIntentMessage();
		return inflate;
	}

	// 传过来的数据
	private void getIntentMessage() {
		Intent intent = getActivity().getIntent();
		comboId = intent.getIntExtra("comboId", 0);// 套餐课程Id
		userId = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).getInt("userId", 0);// 用户ID
	}

	/**
	 * 获取本类实例的方法
	 */
	public static PackageReviewsFragment getInstance() {
		if (packageReviewsFragement == null) {
			packageReviewsFragement = new PackageReviewsFragment();
		}
		return packageReviewsFragement;
	}

	@Override
	public void initView() {
		if (publicEntity != null) {
			kpointId = publicEntity.getEntity().getDefaultKpointId(); // 得到课程节点的Id
		}
		send_message = (TextView) inflate.findViewById(R.id.send_message); // 发送的按钮
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		progressDialog = new ProgressDialog(getActivity()); // 联网获取数据显示的dialog
		discuss_settext = (EditText) inflate.findViewById(R.id.discuss_settext);
		discuss_listView = (NoScrollListView) inflate.findViewById(R.id.discuss_listView);
		no_discuss_layout = (LinearLayout) inflate.findViewById(R.id.no_discuss_layout); // 没有评论的布局
		refreshScrollView = (PullToRefreshScrollView) inflate.findViewById(R.id.refreshScrollView);
		entityPublics = new ArrayList<EntityPublic>(); // 课程评论列表的实体
		refreshScrollView = (PullToRefreshScrollView) inflate.findViewById(R.id.refreshScrollView);
		refreshScrollView.setMode(Mode.BOTH); // 設置加載模式
		getCourse_Comment_List(comboId, page);

	}

	@Override
	public void addOnClick() {
		// discuss_settext.setOnClickListener(this);
		refreshScrollView.setOnRefreshListener(this);
		send_message.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		// case R.id.discuss_settext:
		// Intent intent = new Intent();
		// intent.setClass(getActivity(), WriteCommentsActivity.class);
		// getActivity().startActivity(intent);
		// break;
		case R.id.send_message:
			String string = discuss_settext.getText().toString();
			discuss_settext.setText("");
			if (userId == 0) {
				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
			} else {
				if (!TextUtils.isEmpty(string)) {
					getAdd_Course_Comment(userId, comboId, kpointId, string);
				} else {
					ConstantUtils.showMsg(getActivity(), "请输入内容");
				}
			}
			break;
		}
	}

	/**
	 * @author bishuang 修改人: 时间:2015-10-23 上午11:25:12 方法说明:添加课程评论的方法
	 */
	private void getAdd_Course_Comment(int userId, final int courseId, int kpointId, String content) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("courseAssess.courseId", courseId);
		params.put("courseAssess.kpointId", kpointId);
		params.put("courseAssess.content", content);

		Log.i("lala", Address.ADD_COURSE_COMMENT + params.toString() + ".....");
		httpClient.post(Address.ADD_COURSE_COMMENT, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(data)) {
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
						String message = parseObject.getMessage();
						boolean success = parseObject.isSuccess();
						if (success) {
							entityPublics.clear();
							// HideKeyboard(setEdit);
							Toast.makeText(getActivity(), "评论发表成功！", Toast.LENGTH_SHORT).show();
							getCourse_Comment_List(comboId, 1);
						} else {
							ConstantUtils.showMsg(getActivity(), message);
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

	/**
	 * @author bishuang 修改人: 时间:2015-10-22 下午6:33:42 方法说明: 获取课程评论列表
	 */
	private void getCourse_Comment_List(int courseId, final int currentPage) {
		RequestParams params = new RequestParams();
		params.put("courseId", courseId);
		params.put("page.currentPage", currentPage);
		Log.i("wulala", Address.COURSE_COMMENT_LIST + "?" + params.toString() + "获取评论列表");
		httpClient.post(Address.COURSE_COMMENT_LIST, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(data)) {
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
						String message = parseObject.getMessage();
						if (parseObject.isSuccess()) {

							PageEntity pageEntity = parseObject.getEntity().getPage();
							if (pageEntity.getTotalPageSize() <= currentPage) {
								refreshScrollView.setMode(Mode.PULL_FROM_START);
							}
							List<EntityPublic> assessList = parseObject.getEntity().getAssessList();
							if (assessList != null&&assessList.size() > 0) {
								no_discuss_layout.setVisibility(View.GONE);
								for (int i = 0; i < assessList.size(); i++) {
									entityPublics.add(assessList.get(i));
								}
								adapter = new PackageReviewsAdapter(getActivity(), assessList);
								discuss_listView.setAdapter(adapter);
							} else {
								no_discuss_layout.setVisibility(View.VISIBLE);
							}
						} else {
							ConstantUtils.showMsg(getActivity(), message);
						}
						refreshScrollView.onRefreshComplete();
					} catch (Exception e) {
						refreshScrollView.onRefreshComplete();
						HttpUtils.exitProgressDialog(progressDialog);
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				refreshScrollView.onRefreshComplete();
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	/**
	 * 下拉刷新的方法
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		page = 1;
		entityPublics.clear();
		getCourse_Comment_List(comboId, page);
		refreshScrollView.setMode(Mode.BOTH); // 設置加載模式
	}

	/**
	 * 上拉加载的方法
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullUpToRefresh(refreshView);
		page++;
		getCourse_Comment_List(comboId, page);
	}
}
