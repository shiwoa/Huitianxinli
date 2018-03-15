package com.yizhilu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.ComboAdapter;
import com.yizhilu.adapter.CourseSortAdapter;
import com.yizhilu.adapter.CourseTeacherAdapter;
import com.yizhilu.adapter.SubjectAdapter;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.entity.CourseEntity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.ComboDetailsActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.EventReceive;
import com.yizhilu.utils.EventReceive.OnEventReceiveListener;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.FlowLayout;
import com.yizhilu.view.NoScrollListView;
import com.yizhilu.view.TagAdapter;
import com.yizhilu.view.TagFlowLayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @author bin 修改人: 时间:2015-10-15 下午2:03:50 类说明:套餐的类
 */
public class ComboFragment extends BaseFragment {
	private static ComboFragment comboFragment; // 本类对象
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的方法
	private View inflate; // 课程的总布局
	private boolean isFrist = true; // 是否是第一次加载
	// private MyBroadCast broadCast; // 广播的对象
	private LinearLayout newLayout, hotLayout, freeLayout;
	private TextView newText, hotText, freeText;
	private ImageView newImage, hotImage, freeImage;
	private PullToRefreshScrollView refreshScrollView; // 下拉刷新和上拉加载
	private NoScrollListView course_listView; // 課程列表
	private int currentPage = 1, subjectId, teacherId, sortId, subPosition, teacherPosition, sortPosition; // 当前页数,专业的Id,教师的Id,排序的Id
	private LinearLayout major_layout, teacher_layout, sort_layout; // 专业的总布局,教师的总布局,排序的总布局
	private LinearLayout majorKongLayout, teacherKongLayout, sortKongLayout; // 专业的空布局,教师的空布局,排序的空布局
	private LinearLayout courseLayout;
	private List<EntityCourse> courseList; // 课程的集合
	private boolean isSubjectShow, isTeacherShow, isSortShow, isSubject, isTeacher, isSort;
	private List<EntityCourse> subjectList, teacherList, sortList; // 专业的集合,教师的集合,排序的集合
	// private String[] sortList = new String[] { "全部", "最新", "热门", "免费"
	// };
	private SubjectAdapter subjectAdapter; // 专业的适配器
	private CourseTeacherAdapter teacherAdapter; // 教师的适配器
	private CourseSortAdapter sortAdapter; // 排序的适配器
	private Animation animationIn, animationOut;
	private TagFlowLayout stair_major, teacher_listView, sort_listView; // 专业(分类)的列表,教师的列表,排序的列表
	private TagAdapter<EntityCourse> tagAdapter;
	private EntityCourse entityCourse = new EntityCourse(); // 全部的实体
	protected ComboAdapter comboAdapter;
	private EventReceive eventReceive;
	protected String recev_id;
	protected TextView tv;

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.cobo_fragment, container, false);
		if (getArguments() != null) {
			Bundle bundle = getArguments();
			String temp = bundle.getString("subjectId");
			if (temp != null && !TextUtils.isEmpty(temp)) {
				subjectId = Integer.parseInt(temp);
			}
		}

		return inflate;
	}

	/**
	 * 获取本实例的对象
	 */
	public static ComboFragment getInstance() {
		if (comboFragment == null) {
			comboFragment = new ComboFragment();
		}
		return comboFragment;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	/**
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {

		progressDialog = new ProgressDialog(getActivity()); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		courseList = new ArrayList<EntityCourse>(); // 课程的集合
		subjectList = new ArrayList<EntityCourse>(); // 专业的集合
		teacherList = new ArrayList<EntityCourse>(); // 教师的集合
		sortList = new ArrayList<EntityCourse>(); // 排序的集合
		refreshScrollView = (PullToRefreshScrollView) inflate.findViewById(R.id.refreshScrollView); // 下拉刷新和上拉加载
		refreshScrollView.setMode(Mode.BOTH); // 设置加载模式
		course_listView = (NoScrollListView) inflate.findViewById(R.id.course_listView); // 課程列表的控件
		major_layout = (LinearLayout) inflate.findViewById(R.id.major_layout); // 专业的总布局
		teacher_layout = (LinearLayout) inflate.findViewById(R.id.teacher_layout); // 教师的总布局
		teacherKongLayout = (LinearLayout) inflate.findViewById(R.id.teacherKongLayout); // 教师的空布局
		teacher_listView = (TagFlowLayout) inflate.findViewById(R.id.teacher_listView); // 教师列表
		sort_layout = (LinearLayout) inflate.findViewById(R.id.sort_layout); // 排序的总布局
		sort_listView = (TagFlowLayout) inflate.findViewById(R.id.sort_listView); // 排序的列表
		newLayout = (LinearLayout) inflate.findViewById(R.id.newLayout);
		hotLayout = (LinearLayout) inflate.findViewById(R.id.hotLayout);
		freeLayout = (LinearLayout) inflate.findViewById(R.id.freeLayout);
		newText = (TextView) inflate.findViewById(R.id.newText);
		hotText = (TextView) inflate.findViewById(R.id.hotText);
		freeText = (TextView) inflate.findViewById(R.id.freeText);
		newImage = (ImageView) inflate.findViewById(R.id.newImage);
		hotImage = (ImageView) inflate.findViewById(R.id.hotImage);
		freeImage = (ImageView) inflate.findViewById(R.id.freeImage);
		majorKongLayout = (LinearLayout) inflate.findViewById(R.id.majorKongLayout); // 专业的空布局
		sortKongLayout = (LinearLayout) inflate.findViewById(R.id.sortKongLayout); // 排序的空布局
		stair_major = (TagFlowLayout) inflate.findViewById(R.id.stair_major); // 专业(分类)的列表
		courseLayout = (LinearLayout) inflate.findViewById(R.id.courseLayout);

		entityCourse.setSubjectName("全部");
		entityCourse.setSubjectId(0);
		entityCourse.setId(0);
		entityCourse.setName("全部");
		if (subjectId != 0) {
			newText.setText("心理咨询师考证");
		}
		getComboList(currentPage, subjectId);
	}

	/**
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		newLayout.setOnClickListener(this);
		hotLayout.setOnClickListener(this);
		freeLayout.setOnClickListener(this);
		course_listView.setOnItemClickListener(this); // 课程列表的
		refreshScrollView.setOnRefreshListener(this); // 上拉加载和下拉刷新
		majorKongLayout.setOnClickListener(this); // 专业的空布局
		teacherKongLayout.setOnClickListener(this); // 教师的空布局
		sortKongLayout.setOnClickListener(this); // 排序的空布局
		courseLayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		setAllSubjectBg();
		switch (v.getId()) {
		case R.id.newLayout: // 专业的点击事件
			if (isSubjectShow) {
				animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
				major_layout.startAnimation(animationOut);
				major_layout.setVisibility(View.GONE);
				isSubjectShow = false;
			} else {
				animationIn = AnimationUtils.loadAnimation(getActivity(), R.anim.push_out_show);
				major_layout.startAnimation(animationIn);
				major_layout.setVisibility(View.VISIBLE);
				isSubjectShow = true;
				newText.setTextColor(getResources().getColor(R.color.Blue));
				newImage.setBackgroundResource(R.drawable.close_select);
				if (!isSubject) {
					// 联网获取专业列表的方法
					getSubjectData(0);
				}
			}
			if (isTeacherShow) {
				animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
				teacher_layout.startAnimation(animationOut);
				teacher_layout.setVisibility(View.GONE);
				isTeacherShow = false;
			}
			if (isSortShow) {
				animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
				sort_layout.startAnimation(animationOut);
				sort_layout.setVisibility(View.GONE);
				isSort = false;
			}
			break;
		case R.id.hotLayout: // 教师的点击事件
			if (isTeacherShow) {
				animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
				teacher_layout.startAnimation(animationOut);
				teacher_layout.setVisibility(View.GONE);
				isTeacherShow = false;
			} else {
				animationIn = AnimationUtils.loadAnimation(getActivity(), R.anim.push_out_show);
				teacher_layout.startAnimation(animationIn);
				teacher_layout.setVisibility(View.VISIBLE);
				isTeacherShow = true;
				hotText.setTextColor(getResources().getColor(R.color.Blue));
				hotImage.setBackgroundResource(R.drawable.close_select);
				if (!isTeacher) {
					// 联网获取教师列表的方法
					getTeacherData();
				}
			}
			if (isSubjectShow) {
				animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
				major_layout.startAnimation(animationOut);
				major_layout.setVisibility(View.GONE);
				isSubjectShow = false;
			}
			if (isSortShow) {
				animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
				sort_layout.startAnimation(animationOut);
				sort_layout.setVisibility(View.GONE);
				isSortShow = false;
			}
			break;
		case R.id.freeLayout: // 排序的点击事件
			if (isSortShow) {
				animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
				sort_layout.startAnimation(animationOut);
				sort_layout.setVisibility(View.GONE);
				isSortShow = false;
			} else {
				animationIn = AnimationUtils.loadAnimation(getActivity(), R.anim.push_out_show);
				sort_layout.startAnimation(animationIn);
				sort_layout.setVisibility(View.VISIBLE);
				isSortShow = true;
				freeText.setTextColor(getResources().getColor(R.color.Blue));
				freeImage.setBackgroundResource(R.drawable.close_select);
				if (!isSort) {
					isSort = true;
					for (int i = 0; i < 4; i++) {
						EntityCourse entityCourse = new EntityCourse();
						switch (i) {
						case 0:
							entityCourse.setName("全部");
							entityCourse.setId(0);
							break;
						case 1:
							entityCourse.setName("最新");
							entityCourse.setId(2);
							break;
						case 2:
							entityCourse.setName("热门");
							entityCourse.setId(1);
							break;
						case 3:
							entityCourse.setName("免费");
							entityCourse.setId(3);
							break;
						default:
							break;
						}
						sortList.add(entityCourse);
					}
					// 点击排序列表
					getCouseList(sort_listView, sortList, sortPosition, "sort");
				}
			}
			if (isTeacherShow) {
				animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
				teacher_layout.startAnimation(animationOut);
				teacher_layout.setVisibility(View.GONE);
				isTeacherShow = false;
			}
			if (isSubjectShow) {
				animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
				major_layout.startAnimation(animationOut);
				major_layout.setVisibility(View.GONE);
				isSubjectShow = false;
			}
			break;
		case R.id.majorKongLayout: // 专业的空布局
			hintAllLayout();
			break;
		case R.id.teacherKongLayout: // 教师的空布局
			hintAllLayout();
			break;
		case R.id.sortKongLayout: // 排序的空布局
			hintAllLayout();
			break;
		default:
			break;
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-10-20 下午3:12:44 方法说明:获取专业列表数据的方法
	 */
	private void getSubjectData(int parentId) {
		RequestParams params = new RequestParams();
		params.put("parentId", parentId);
		Log.i("wulala", "" + Address.MAJOR_LIST + "?" + params);
		httpClient.post(Address.MAJOR_LIST, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				if (!TextUtils.isEmpty(data)) {
					try {
						CourseEntity courseEntity = JSON.parseObject(data, CourseEntity.class);
						if (courseEntity.isSuccess()) {
							isSubject = true;
							subjectList.clear();
							subjectList.add(entityCourse);
							List<EntityCourse> list = courseEntity.getEntity();
							if (list != null && list.size() > 0) {
								for (int i = 0; i < list.size(); i++) {
									subjectList.add(list.get(i));
									Log.i("wulala", subjectList.get(i).getSubjectId() + "_______");
								}
								// 点击专业列表或教师列表
								if (subjectId != 0) {
									getCouseList(stair_major, subjectList, 4, "major");
								} else {
									getCouseList(stair_major, subjectList, 0, "major");

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

	/**
	 * @author bin 修改人: 时间:2015-10-21 上午11:02:32 方法说明:获取教师列表的方法
	 */
	private void getTeacherData() {
		httpClient.get(Address.COURSE_TEACHER_LIST, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				if (!TextUtils.isEmpty(data)) {
					try {
						CourseEntity courseEntity = JSON.parseObject(data, CourseEntity.class);
						if (courseEntity.isSuccess()) {
							isTeacher = true;
							teacherList.add(entityCourse);
							List<EntityCourse> list = courseEntity.getEntity();
							if (list != null && list.size() > 0) {
								for (int i = 0; i < list.size(); i++) {
									teacherList.add(list.get(i));
								}
								// 点击教师列表
								getCouseList(teacher_listView, teacherList, teacherPosition, "teacher");
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

	/**
	 * @author bin 修改人: 时间:2015-12-5 下午5:46:26 方法说明:
	 */
	private void getCouseList(final TagFlowLayout tagFlowLayout, List<EntityCourse> list, final int selectPosition,
			final String type) {
		tagFlowLayout.setAdapter(tagAdapter = new TagAdapter<EntityCourse>(list) {
			@Override
			public View getView(FlowLayout parent, final int position, EntityCourse t) {
				tv = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.tv, tagFlowLayout, false);
				if ("major".equals(type)) {
					tv.setText(t.getSubjectName());
				} else if ("teacher".equals(type)) {
					tv.setText(t.getName());
				} else if ("sort".equals(type)) {
					tv.setText(t.getName());
				}
				if (position == selectPosition) {
					tv.setBackgroundResource(R.drawable.www);
					tv.setTextColor(getActivity().getResources().getColor(R.color.White));
				} else {
					tv.setBackgroundResource(R.drawable.qqq);
				}
				tv.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						refreshScrollView.setMode(Mode.BOTH); // 设置加载模式
						if (isSubjectShow) {
							hintAllLayout();
							setAllSubjectBg();
							currentPage = 1;
							courseList.clear();
							subjectId = subjectList.get(position).getSubjectId();
							if ("全部".equals(subjectList.get(position).getSubjectName())) {
								newText.setText("按专业");
							} else {
								newText.setText(subjectList.get(position).getSubjectName());
							}
							subPosition = position;
							// 点击专业列表或教师列表
							getCouseList(stair_major, subjectList, subPosition, "major");
							// getCourseList(currentPage, sortId,
							// subjectId, teacherId);
							getComboList(currentPage, subjectId);
						} else if (isTeacherShow) {
							hintAllLayout();
							setAllSubjectBg();
							currentPage = 1;
							courseList.clear();
							teacherId = teacherList.get(position).getId();
							if ("全部".equals(teacherList.get(position).getName())) {
								hotText.setText("按讲师");
							} else {
								hotText.setText(teacherList.get(position).getName());
							}
							teacherPosition = position;
							// 点击教师列表或教师列表
							getCouseList(teacher_listView, teacherList, teacherPosition, "teacher");
							// getCourseList(currentPage, sortId,
							// subjectId, teacherId);
							getComboList(currentPage, subjectId);
						} else if (isSortShow) {
							hintAllLayout();
							setAllSubjectBg();
							currentPage = 1;
							courseList.clear();
							sortId = sortList.get(position).getId();
							if ("全部".equals(sortList.get(position).getName())) {
								freeText.setText("按排序");
							} else {
								freeText.setText(sortList.get(position).getName());
							}
							sortPosition = position;
							// 点击排序列表
							getCouseList(sort_listView, sortList, sortPosition, "sort");
							// getCourseList(currentPage, sortId,
							// subjectId, teacherId);
							getComboList(currentPage, subjectId);
						}
					}
				});
				return tv;
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		switch (parent.getId()) {
		case R.id.course_listView: // 课程列表
			Intent intent = new Intent(getActivity(), ComboDetailsActivity.class);
			intent.putExtra("comboId", courseList.get(position).getId());

			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-10-21 下午1:31:18 方法说明:隐藏所有布局的方法
	 */
	public void hintAllLayout() {
		if (isSubjectShow) {
			animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
			major_layout.startAnimation(animationOut);
			major_layout.setVisibility(View.GONE);
			isSubjectShow = false;
		}
		if (isTeacherShow) {
			animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
			teacher_layout.startAnimation(animationOut);
			teacher_layout.setVisibility(View.GONE);
			isTeacherShow = false;
		}
		if (isSortShow) {
			animationOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_close);
			sort_layout.startAnimation(animationOut);
			sort_layout.setVisibility(View.GONE);
			isSortShow = false;
		}
	}

	public void setAllSubjectBg() {
		newText.setTextColor(getResources().getColor(R.color.color_67));
		newImage.setBackgroundResource(R.drawable.dropdown);
		freeText.setTextColor(getResources().getColor(R.color.color_67));
		freeImage.setBackgroundResource(R.drawable.dropdown);
		hotText.setTextColor(getResources().getColor(R.color.color_67));
		hotImage.setBackgroundResource(R.drawable.dropdown);
	}

	/**
	 * 下拉刷新的方法
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		hintAllLayout();
		currentPage = 1;
		courseList.clear();
		// getCourseList(currentPage, sortId, subjectId, teacherId);
		getComboList(currentPage, subjectId);
	}

	/**
	 * 上拉加载的方法
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullUpToRefresh(refreshView);
		hintAllLayout();
		currentPage++;
		// getCourseList(currentPage, sortId, subjectId, teacherId);
		getComboList(currentPage, subjectId);
	}

	/**
	 * @author bin 修改人: 时间:2016-7-16 下午3:12:34 方法说明:获取套餐列表的数据
	 */

	private void getComboList(final int page, int subjectId) {
		RequestParams params = new RequestParams();
		params.put("queryCourse.sellType", "PACKAGE");
		params.put("page.currentPage", page);
		params.put("queryCourse.subjectId", subjectId);
		Log.i("wulala", Address.COURSE_LIST + "?" + params.toString());
		httpClient.post(Address.COURSE_LIST, params, new TextHttpResponseHandler() {
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
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						String message = publicEntity.getMessage();
						if (publicEntity.isSuccess()) {
							int totalPageSize = publicEntity.getEntity().getPage().getTotalPageSize();
							if (currentPage >= totalPageSize) {
								refreshScrollView.setMode(Mode.PULL_FROM_START);
							} else {
								refreshScrollView.setMode(Mode.BOTH);
							}
							List<EntityCourse> courses = publicEntity.getEntity().getCourseList();
							if (courseList.size() > 0) {
								courseList.clear();
							}
							courseList.addAll(courses);
							comboAdapter = new ComboAdapter(getActivity(), courseList);
							course_listView.setAdapter(comboAdapter);
						} else {
							ConstantUtils.showMsg(getActivity(), message);
						}
						refreshScrollView.onRefreshComplete();
					} catch (Exception e) {
						refreshScrollView.onRefreshComplete();
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
				refreshScrollView.onRefreshComplete();
			}
		});
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
