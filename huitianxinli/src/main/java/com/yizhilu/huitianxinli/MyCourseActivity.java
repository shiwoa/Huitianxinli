package com.yizhilu.huitianxinli;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.MyCourseAdapater;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.baoli.BLCourseDetailsActivity;
import com.yizhilu.entity.CourseEntity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

/**
 * @author liuchangqi 修改人: 时间:2015年10月22日 下午3:47:28 类说明:我的课程
 */
public class MyCourseActivity extends BaseActivity {
	private TextView title; // 标题
	private PullToRefreshScrollView mScrollView; // 加载和刷新的布局
	private NoScrollListView mListView; // 我的课程的布局
	private int userId; // 用户Id
	private ProgressDialog mProgressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private MyCourseAdapater courseAdapter; // 课程的适配器
	private List<EntityCourse> datas; // 放置数据的总集合
	private LinearLayout back_layout, isShow; // 返回 显示默认图片
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.my_course);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void addOnClick() {
		mScrollView.setOnRefreshListener(this); // 加载和刷新的布局
		back_layout.setOnClickListener(this); // 返回
		mListView.setOnItemClickListener(this); // 我的课程的点击事件
	}

	@Override
	public void initView() {
		mProgressDialog = new ProgressDialog(MyCourseActivity.this); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		datas = new ArrayList<EntityCourse>(); // 放置数据的总集合
		title = (TextView) findViewById(R.id.title_text); // 标题
		title.setText(getResources().getString(R.string.sliding_course)); // 设置标题
		mScrollView = (PullToRefreshScrollView) findViewById(R.id.course_scrollview); // 加载和刷新的布局
		mScrollView.setMode(Mode.PULL_FROM_START); // 设置加载模式
		mListView = (NoScrollListView) findViewById(R.id.course_listview); // 我的课程的布局
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId",
				0); // 获取用户的Id
		isShow = (LinearLayout) findViewById(R.id.myCourse_isShow); // 显示默认图片
		// 聯網獲取我的課程
		getCourse(userId);
	}

	/**
	 * @author liuchangqi 修改人: 时间:2015年10月22日 下午4:11:25 方法说明:我的课程
	 */
	private void getCourse(int userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		Log.i("wulala", Address.MY_BUY_COURSE + "?" + params.toString());
		httpClient.post(Address.MY_BUY_COURSE, params,
				new TextHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						HttpUtils.showProgressDialog(mProgressDialog);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						HttpUtils.exitProgressDialog(mProgressDialog);
						if (!TextUtils.isEmpty(data)) {
							try {
								CourseEntity parseObject = JSON.parseObject(
										data, CourseEntity.class);
								boolean success = parseObject.isSuccess();
								if (success) {
									List<EntityCourse> entity = parseObject
											.getEntity();
									for (int i = 0; i < entity.size(); i++) {
										datas.add(entity.get(i));

									}
									if (entity.size() != 0) {
										courseAdapter = new MyCourseAdapater(
												MyCourseActivity.this, datas);
										mListView.setAdapter(courseAdapter);
									} else {
										mScrollView.setVisibility(View.GONE);
										isShow.setVisibility(View.VISIBLE);
									}
									mScrollView.onRefreshComplete();
								}
							} catch (Exception e) {
								mScrollView.onRefreshComplete();
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(mProgressDialog);
						mScrollView.onRefreshComplete();
					}
				});
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		datas.clear();
		getCourse(userId);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullUpToRefresh(refreshView);
		getCourse(userId);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout: // 返回
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
		type = datas.get(position).getSellType();
		switch (parent.getId()) {
		case R.id.course_listview: // 我的课程的点击事件
			if (type.equals("COURSE")) {
				Intent intent = new Intent(MyCourseActivity.this,
						BLCourseDetailsActivity.class);
				intent.putExtra("courseId", datas.get(position).getCourseId());
				startActivity(intent);
			} else if (type.equals("PACKAGE")) {
				Intent intent = new Intent(MyCourseActivity.this,
						ComboDetailsActivity.class);
				intent.putExtra("comboId", datas.get(position).getCourseId());
				startActivity(intent);
			} else if (type.equals("")) {

			}
			break;

		default:
			break;
		}
	}
}
