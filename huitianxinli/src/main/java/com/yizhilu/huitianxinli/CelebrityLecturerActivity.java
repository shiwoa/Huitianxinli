package com.yizhilu.huitianxinli;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.TeacherAdapter;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.TeacherEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

/**
 * @author bishuang 修改人: 时间:2015-10-22 下午5:07:43 类说明:名人讲师的类
 */
public class CelebrityLecturerActivity extends BaseActivity implements
		OnRefreshListener2<ScrollView> {
	private PullToRefreshScrollView pulltorefreshScrollView; // 上拉，下拉的布局
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private List<TeacherEntity> EntityList; // 存放讲师的实体
	private TeacherAdapter adapter; // 讲师的适配器
	private NoScrollListView listView; // 所讲课程列表
	private int currentPage = 1;// 初始的页数
	private LinearLayout backLayout; // 返回
	private TextView title_text; // 标题

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_celebrity_lecturer);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		progressDialog = new ProgressDialog(CelebrityLecturerActivity.this); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		EntityList = new ArrayList<TeacherEntity>(); // 存放讲师的实体
		listView = (NoScrollListView) findViewById(R.id.noScrollListView_lecturer); // 所讲课程列表
		pulltorefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.PullToRefreshScrollView_lecturer); // 上拉，下拉的布局
		pulltorefreshScrollView.setMode(Mode.BOTH);  //设置加载模式
		backLayout = (LinearLayout) findViewById(R.id.back_layout); // 返回
		title_text = (TextView) findViewById(R.id.title_text); // 标题
		title_text.setText(getResources().getString(R.string.sliding_teacher)); // 设置标题
		// 联网获取讲师的方法
		getTeacherList(currentPage);
	}

	/**
	 * @author bishuang 修改人: 时间:2015-10-20 下午5:08:20 方法说明:获取讲师列表的方法
	 */
	private void getTeacherList(final int currentPage) {
		RequestParams params = new RequestParams();
		params.put("page.currentPage", currentPage);
		httpClient.post(Address.TEACHER_LIST, params,
				new TextHttpResponseHandler() {
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
								PublicEntity publicEntity = JSON.parseObject(
										data, PublicEntity.class);
								String message = publicEntity.getMessage();
								if (publicEntity.isSuccess()) {
									PageEntity page = publicEntity.getEntity()
											.getPage();
									if(page.getTotalPageSize()<=currentPage){
										pulltorefreshScrollView.setMode(Mode.PULL_FROM_START);
									}
									List<TeacherEntity> list = publicEntity
											.getEntity().getTeacherList();
									for (int i = 0; i < list.size(); i++) {
										EntityList.add(list.get(i));
									}
									adapter = new TeacherAdapter(
											CelebrityLecturerActivity.this,
											EntityList);
									listView.setAdapter(adapter);
									pulltorefreshScrollView.onRefreshComplete();
								} else {
									HttpUtils
											.exitProgressDialog(progressDialog);
									ConstantUtils.showMsg(
											CelebrityLecturerActivity.this,
											message);
									pulltorefreshScrollView.onRefreshComplete();
								}
							} catch (Exception e) {
								HttpUtils.exitProgressDialog(progressDialog);
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						pulltorefreshScrollView.onRefreshComplete();
						HttpUtils.exitProgressDialog(progressDialog);
					}
				});
	}

	@Override
	public void addOnClick() {
		pulltorefreshScrollView.setOnRefreshListener(this); // 上拉，下拉的布局
		listView.setOnItemClickListener(this); // 所讲课程列表
		backLayout.setOnClickListener(this); // 返回
	}

	// 下拉刷新
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		EntityList.clear();
		currentPage = 1;
		pulltorefreshScrollView.setMode(Mode.BOTH);
		// 联网获取数据的方法
		getTeacherList(currentPage);
	}

	// 上拉加载
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		currentPage++;
		// 联网获取数据的方法
		getTeacherList(currentPage);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		switch (parent.getId()) {
		case R.id.noScrollListView_lecturer:  //教师列表
			int teacherId = EntityList.get(position).getId();
			Intent intent = new Intent(CelebrityLecturerActivity.this,
					LecturerDetailsActivity.class);
			intent.putExtra("teacherId", teacherId);
			startActivity(intent);
			break;

		default:
			break;
		}
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

}
