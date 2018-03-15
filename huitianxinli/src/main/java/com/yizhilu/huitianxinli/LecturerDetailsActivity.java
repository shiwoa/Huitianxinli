package com.yizhilu.huitianxinli;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.adapter.TeacherDetailsAdapter;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.baoli.BLCourseDetailsActivity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.entity.TeacherEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

/**
 * @author bishuang 修改人: 时间:2015-10-24 上午10:59:09 类说明:讲师详情的类
 */
public class LecturerDetailsActivity extends BaseActivity {
	private LinearLayout back_layout; // 返回的布局
	private TextView title_text, teacher_content_more; // 标题
	boolean imageMeasured = false;
	private TextView teacher_name, teacher_title, teacher_jianjie,
			teacher_content, lecturer_courseNumber; // 名字,级别,资历,简介,课程数量
	private ImageLoader imageLoader; // 加载头像的对象
	private ImageView teacher_image, open_content; // 头像,展开图标
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private TeacherEntity teacher; // 存放讲师的实体
	private NoScrollListView lecturer_courseList; // 所讲课程
	private PullToRefreshScrollView refreshScrollView; // 刷新和加载的对象
	private List<EntityCourse> EntityCourses; // 存放课程的实体
	private TeacherDetailsAdapter adapter; // 所讲课程的适配器
	private int teacherId, currentPage = 1; // 讲师Id,当前页
	private boolean open_more; //
	private Object type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_lecturer_details);
		// 获取传过来的信息
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}

	/**
	 * @author bin 修改人: 时间:2015-11-2 下午4:51:06 方法说明:获取传过来的信息
	 */
	private void getIntentMessage() {
		Intent intent = getIntent();
		teacherId = intent.getIntExtra("teacherId", 0); // 得到讲师Id
	}

	@Override
	public void initView() {
		imageLoader = ImageLoader.getInstance(); // 加载头像的对象
		httpClient = new AsyncHttpClient();// 联网获取数据的对象
		progressDialog = new ProgressDialog(LecturerDetailsActivity.this);// 加载数据显示的dialog
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回的布局
		title_text = (TextView) findViewById(R.id.title_text); // 标题
		title_text.setText(getResources().getString(R.string.teacher_details)); // 设置标题
		EntityCourses = new ArrayList<EntityCourse>(); // 存放课程的实体
		teacher_image = (ImageView) findViewById(R.id.teacher_image); // 教师头像
		teacher_name = (TextView) findViewById(R.id.teacher_name); // 教师的名字
		teacher_title = (TextView) findViewById(R.id.teacher_title); // 讲师等级
		open_content = (ImageView) findViewById(R.id.open_content); // 展开图标
		teacher_jianjie = (TextView) findViewById(R.id.teacher_jianjie); // 教师资历
		teacher_content = (TextView) findViewById(R.id.teacher_content); // 教师简介
		teacher_content_more = (TextView) findViewById(R.id.teacher_content_more);
		lecturer_courseNumber = (TextView) findViewById(R.id.lecturer_courseNumber); // 课程量
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView); // 刷新和加载的对象
		refreshScrollView.setMode(Mode.BOTH); // 设置加载模式
		lecturer_courseList = (NoScrollListView) findViewById(R.id.lecturer_courseList); // 所讲课程
		// 联网获取讲师详情的方法
		getTeacherInfo(teacherId);
		// 获取讲师课程方法
		getTeacherCourse(currentPage, teacherId);

	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this); // 返回的布局
		refreshScrollView.setOnRefreshListener(this); // 加载和刷新的
		lecturer_courseList.setOnItemClickListener(this); // 相关课程
		open_content.setOnClickListener(this);
	}

	/**
	 * @author bishuang 修改人: 时间:2015-10-21 上午11:51:56 方法说明: 获取讲师详情的方法
	 */
	private void getTeacherInfo(long teacherId) {
		RequestParams params = new RequestParams();
		params.put("teacherId", teacherId);
		httpClient.post(Address.TEACHER_DETAILS, params,
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
								boolean success = publicEntity.isSuccess();
								if (success) {
									teacher = publicEntity.getEntity()
											.getTeacher();
									teacher_name.setText(teacher.getName());
									if (teacher.getIsStar() == 0) {
										teacher_title.setText("高级讲师");
									} else {
										teacher_title.setText("首席讲师");
									}
									// Log.i("lala",
									// teacher.getCareer().length()+"...");
									// int length =
									// teacher.getCareer().length();
									// if(length>100){
									//
									// }
									teacher_jianjie.setText(teacher
											.getEducation());
									teacher_content.setText(teacher.getCareer());
									Layout layout = teacher_content.getLayout();
									if (layout != null) {
										int lineCount = layout.getLineCount();
										if (lineCount > 4) {
											open_more = true;
											open_content
													.setVisibility(View.VISIBLE);
											teacher_content_more
													.setText(teacher
															.getCareer());
										}
									}
									imageLoader.displayImage(Address.IMAGE_NET
											+ teacher.getPicPath(),
											teacher_image,
											HttpUtils.getDisPlay());
								}
							} catch (Exception e) {
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(progressDialog);
					}
				});
	}

	/**
	 * @author bishuang 修改人: 时间:2015-10-22 下午4:51:04 方法说明:获取所讲课程的类
	 */
	private void getTeacherCourse(final int currentPage, int teacherId) {
		RequestParams params = new RequestParams();
		params.put("page.currentPage", currentPage);
		params.put("teacherId", teacherId);
		Log.i("lala", Address.TEACHER_COURSE + "?" + params.toString());
		httpClient.post(Address.TEACHER_COURSE, params,
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
								boolean success = publicEntity.isSuccess();
								if (success) {
									List<EntityCourse> courseList = publicEntity
											.getEntity().getCourseList();
									PageEntity page = publicEntity.getEntity()
											.getPage();
									if (page.getTotalPageSize() <= currentPage) {
										refreshScrollView
												.setMode(Mode.PULL_FROM_START);
									}
									for (int i = 0; i < courseList.size(); i++) {
										EntityCourses.add(courseList.get(i));
									}
									lecturer_courseNumber.setText("("
											+ EntityCourses.size() + ")");
									adapter = new TeacherDetailsAdapter(
											LecturerDetailsActivity.this,
											EntityCourses);
									lecturer_courseList.setAdapter(adapter);
									refreshScrollView.onRefreshComplete();
								} else {
									HttpUtils
											.exitProgressDialog(progressDialog);
									refreshScrollView.onRefreshComplete();
									ConstantUtils.showMsg(
											LecturerDetailsActivity.this,
											message);
								}
							} catch (Exception e) {
								HttpUtils.exitProgressDialog(progressDialog);
								refreshScrollView.onRefreshComplete();
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(progressDialog);
						refreshScrollView.onRefreshComplete();
					}
				});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout: // 返回的布局
			this.finish();
			break;
		case R.id.open_content: // 展开图标
			if (open_more) {
				open_more = false;
				open_content.setBackgroundResource(R.drawable.close);
				teacher_content.setVisibility(View.GONE);
				teacher_content_more.setVisibility(View.VISIBLE);
			} else {
				open_more = true;
				open_content.setBackgroundResource(R.drawable.open);
				teacher_content.setVisibility(View.VISIBLE);
				teacher_content_more.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		currentPage = 1;
		EntityCourses.clear();
		refreshScrollView.setMode(Mode.BOTH);
		getTeacherCourse(currentPage, teacherId);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullUpToRefresh(refreshView);
		currentPage++;
		getTeacherCourse(currentPage, teacherId);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		super.onItemClick(parent, view, position, id);
		type = EntityCourses.get(position).getSellType();
		switch (parent.getId()) {
		case R.id.lecturer_courseList: // 相关课程
			int courseId = EntityCourses.get(position).getId();
			DemoApplication.getInstance().finishActivity(
					BLCourseDetailsActivity.getInstence());
			
			
			if (type.equals("COURSE")) {
				Intent intent = new Intent(LecturerDetailsActivity.this,
						BLCourseDetailsActivity.class);
				intent.putExtra("courseId", courseId);
				startActivity(intent);
			} else if (type.equals("PACKAGE")) {
				Intent intent = new Intent(LecturerDetailsActivity.this,
						ComboDetailsActivity.class);
				intent.putExtra("comboId", courseId);
				startActivity(intent);
			} else if (type.equals("")) {

			}
			
			this.finish();
			break;

		default:
			break;
		}
	}
}
