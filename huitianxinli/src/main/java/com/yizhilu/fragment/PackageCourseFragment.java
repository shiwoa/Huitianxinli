package com.yizhilu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.PackageCourseAdapter;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.baoli.BLCourseDetailsActivity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.ComboDetailsActivity;
import com.yizhilu.huitianxinli.ConfirmOrderActivity;
import com.yizhilu.huitianxinli.LoginActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollGridView;

/**
 * 
 * @author liuchangqi 修改人: 时间:2016-7-19 上午11:13:26 类说明:套餐课程
 */
public class PackageCourseFragment extends BaseFragment {
	private View inflate;// 套餐评论的总布局
	private static PackageCourseFragment packageCourseFragment;
	private NoScrollGridView recommendGridView, course_recommenView;// 套餐课程,课程推荐
	private PackageCourseAdapter adapter, course_adapter;// 课程套餐，课程推荐适配器
	private PublicEntity publicEntity;// 课程详情
	private List<EntityCourse> packageList;// 套餐课程数据
	private int userId; // 用户Id
	private TextView courseTitle;// 标题
	private EntityCourse course;// 课程
	private TextView course_price, price_two, purchaseText;// 现价,原价,购买
	private ImageView free_view;// 免费
	private ImageView small_recommend_more, course_recommen_image;// 更多套餐,相关课程
	private ComboDetailsActivity comboDetailsActivity;
	private List<EntityCourse> courseDtoList;// 相关课程
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private List<EntityPublic> EntityCourses;
	private ProgressDialog progressDialog; // 加载数据显示的dialog

	@Override
	@Deprecated
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		comboDetailsActivity = (ComboDetailsActivity) activity;
	}

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_package_course, container,
				false);
		Bundle bundle = getArguments();
		publicEntity = (PublicEntity) bundle.getSerializable("publicEntity");
		return inflate;
	}

	/**
	 * 获取本类实例的方法
	 */
	public static PackageCourseFragment getInstance() {
		if (packageCourseFragment == null) {
			packageCourseFragment = new PackageCourseFragment();
		}
		return packageCourseFragment;
	}

	@Override
	public void initView() {
		httpClient = new AsyncHttpClient();// 联网获取数据的对象
		progressDialog = new ProgressDialog(getActivity());// 加载数据显示的dialog
		EntityCourses = new ArrayList<EntityPublic>();
//		getHotRconmmonCourse();
		userId = getActivity().getSharedPreferences("userId",
				getActivity().MODE_PRIVATE).getInt("userId", 0);
		recommendGridView = (NoScrollGridView) inflate
				.findViewById(R.id.recommendGridView);// 套餐课程图片
		course_recommenView = (NoScrollGridView) inflate
				.findViewById(R.id.course_recommenView);
		packageList = publicEntity.getEntity().getCoursePackageList();// 套餐课程
		if (packageList != null && packageList.size() > 0) {
			adapter = new PackageCourseAdapter(getActivity(), packageList);
			recommendGridView.setAdapter(adapter);
		}
		course_recommenView = (NoScrollGridView) inflate
				.findViewById(R.id.course_recommenView);// 课程推荐
		courseDtoList = publicEntity.getEntity().getCourseDtoList();
		if (courseDtoList != null && courseDtoList.size() > 0) {
			course_adapter = new PackageCourseAdapter(getActivity(),
					courseDtoList);
			course_recommenView.setAdapter(course_adapter);
		}
		course_recommen_image = (ImageView) inflate
				.findViewById(R.id.course_recommen_image);// 课程推荐图片
		course = publicEntity.getEntity().getCourse();
		courseTitle = (TextView) inflate.findViewById(R.id.courseTitle);// 标题
		course_price = (TextView) inflate.findViewById(R.id.course_price);// 现价
		price_two = (TextView) inflate.findViewById(R.id.price_two);// 原价
		price_two.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 添加中间横线
		purchaseText = (TextView) inflate.findViewById(R.id.purchaseText);// 购买
		free_view = (ImageView) inflate.findViewById(R.id.free_view);
		small_recommend_more = (ImageView) inflate
				.findViewById(R.id.small_recommend_more);
		if (course != null) {
			courseTitle.setText(course.getName());
			course_price.setText(course.getCurrentprice() + "");
			price_two.setText(course.getSourceprice() + "");
			if (course.getIsPay() == 0) { // 免费
				purchaseText.setVisibility(View.GONE);
				free_view.setVisibility(View.VISIBLE);
			} else if (course.getIsPay() == 1) { // 付费
				if (publicEntity.getEntity().isIsok()) { // 已经购买
					purchaseText.setVisibility(View.VISIBLE);
					purchaseText
							.setBackgroundResource(R.drawable.text_yellow_bg_solid_frame);
					purchaseText.setText(getResources().getString(
							R.string.liji_look));
				} else {
					purchaseText.setVisibility(View.VISIBLE);
				}
			}
		}
	}

	@Override
	public void addOnClick() {
		recommendGridView.setOnItemClickListener(this);// 套餐课程点击事件
		purchaseText.setOnClickListener(this);// 支付，观看
		small_recommend_more.setOnClickListener(this);// 关闭activity
		course_recommenView.setOnItemClickListener(this);// 课程推荐点击事件
		course_recommen_image.setOnClickListener(this);// 跳到课程列表
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);

		switch (v.getId()) {
		case R.id.purchaseText: // 立即购买
			String purchase = purchaseText.getText().toString();
			if (getResources().getString(R.string.liji_look).equals(purchase)) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), BLCourseDetailsActivity.class);
				intent.putExtra("courseId", publicEntity.getEntity()
						.getCurrentCourseId());
				getActivity().startActivity(intent);
			} else {
				if (userId == 0) {
					Intent intent = new Intent(getActivity(),
							LoginActivity.class);
					startActivity(intent);
				} else {
					Intent intent = new Intent(getActivity(),
							ConfirmOrderActivity.class);
					intent.putExtra("publicEntity", publicEntity);
					startActivity(intent);
				}
			}
			break;
		case R.id.small_recommend_more: // 关闭activity
			comboDetailsActivity.finish();
			break;
		case R.id.course_recommen_image:// 跳到课程列表
//			Intent intent = new Intent();
//			intent.setClass(getActivity(), CourseListActivity.class);
//			intent.putExtra("id", publicEntity.getEntity().getDefaultKpointId());
//			getActivity().startActivity(intent);
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onItemClick(arg0, arg1, arg2, arg3);
		Intent intent = new Intent();

		switch (arg0.getId()) {
		case R.id.recommendGridView:
			intent.setClass(getActivity(), BLCourseDetailsActivity.class);
			intent.putExtra("courseId", packageList.get(arg2).getId());
			getActivity().startActivity(intent);
			break;
		case R.id.course_recommenView:
//			intent.setClass(getActivity(), BLCourseDetailsActivity.class);
//			intent.putExtra("courseId", EntityCourses.get(arg2).getCourseId());
//			getActivity().startActivity(intent);
			break;
		}
	}


}
