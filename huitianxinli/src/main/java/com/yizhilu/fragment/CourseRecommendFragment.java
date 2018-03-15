package com.yizhilu.fragment;

import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.TeacherAdapter;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.baoli.BLCourseDetailsActivity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.OrderEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.ConfirmOrderActivity;
import com.yizhilu.huitianxinli.LecturerDetailsActivity;
import com.yizhilu.huitianxinli.LoginActivity;
import com.yizhilu.huitianxinli.PaymentConfirmOrderActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.Logs;
import com.yizhilu.view.NoScrollListView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CourseRecommendFragment extends BaseFragment {
	private static CourseRecommendFragment courseRecommendFragment; // 課程介紹的對象
	private View inflate; // 課程介紹的總佈局
	private PublicEntity publicEntity; // 课程介绍的总实体
	private TextView courseTitle, course_price, purchaseText; // 课程名字,课程的价格,立即购买按钮
	private WebView course_webView; // 课程介绍
	private NoScrollListView teacher_listview; // 教师的列表
	private RelativeLayout price_pay_layout; // 价格和购买的布局
	private TextView priceTwo; // 原价
	private ImageView open, free_view; // 显示关闭,免费
	private boolean flag = true;
	private EntityCourse course; // 课程介绍的实体
	private LinearLayout webView_layout; // webView的總佈局
	private int userId; // 用户Id
	private BLCourseDetailsActivity courseDetailsActivity;
	private boolean is_exit;
	private OrderEntity orderEntity;
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private ProgressDialog progressDialog;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		courseDetailsActivity = (BLCourseDetailsActivity) activity;
	}

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_course_recommend, container, false);
		Bundle bundle = getArguments();
		publicEntity = (PublicEntity) bundle.getSerializable("publicEntity");
		Logs.info("CourseRecommendFragment price="+publicEntity.getEntity().getCourse().getCurrentprice());
		Log.i("intent", "从课程详情activity传递过来的实体对象");
		if (publicEntity == null) {
			Log.i("intent", "从课程详情activity传递过来的实体对象为null");
		} else {
			Log.i("intent", "从课程详情activity传递过来的实体对象值正常");
		}
		return inflate;
	}

	public static CourseRecommendFragment getInstence() {
		if (courseRecommendFragment == null) {
			courseRecommendFragment = new CourseRecommendFragment();
		}
		return courseRecommendFragment;
	}

	@Override
	public void initView() {
		httpClient=new AsyncHttpClient();
		progressDialog = new ProgressDialog(getActivity());// 加载数据显示的dialog
		userId = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).getInt("userId", 0);
		courseTitle = (TextView) inflate.findViewById(R.id.courseTitle); // 课程名字
		price_pay_layout = (RelativeLayout) inflate.findViewById(R.id.price_pay_layout); // 价格和购买的布局
		course_price = (TextView) inflate.findViewById(R.id.course_price); // 课程的价格
		purchaseText = (TextView) inflate.findViewById(R.id.purchaseText); // 立即购买
		free_view = (ImageView) inflate.findViewById(R.id.free_view); // 免费
		course_webView = (WebView) inflate.findViewById(R.id.course_webView); // 课程介绍
		teacher_listview = (NoScrollListView) inflate.findViewById(R.id.teacher_listview); // 教师的列表
		priceTwo = (TextView) inflate.findViewById(R.id.price_two); // 原价
		priceTwo.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 添加中间横线
		open = (ImageView) inflate.findViewById(R.id.recommend_switch); // 展开关闭
		webView_layout = (LinearLayout) inflate.findViewById(R.id.webView_layout); // webView的總佈局
		course = publicEntity.getEntity().getCourse();
		courseTitle.setText(course.getName());
		if (course.getIsPay() == 0) { // 免费
			purchaseText.setVisibility(View.GONE);
			free_view.setVisibility(View.VISIBLE);
		} else if (course.getIsPay() == 1) { // 付费
			if (publicEntity.getEntity().isIsok()) { // 已经购买
				purchaseText.setVisibility(View.VISIBLE);
				purchaseText.setBackgroundResource(R.drawable.look);
				purchaseText.setText(getResources().getString(R.string.liji_look));
			} else {
				purchaseText.setVisibility(View.VISIBLE);
			}
		}
		course_price.setText(course.getCurrentprice() + "");
		priceTwo.setText(course.getSourceprice() + "");
		course_webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		course_webView.getSettings().setLoadWithOverviewMode(true);
		course_webView.setWebViewClient(new WebViewClient());
		// course_webView.loadDataWithBaseURL(null, course.getContext(),
		// "text/html", "utf-8", null);
		course_webView.loadUrl(Address.COURSE_CONTENT + course.getId());
		if (course.getTeacherList() != null) {
			if (course.getTeacherList().size() != 0) {
				teacher_listview.setAdapter(new TeacherAdapter(getActivity(), course.getTeacherList()));
			}
		}
	}

	@Override
	public void addOnClick() {
		purchaseText.setOnClickListener(this); // 立即购买的按钮
		open.setOnClickListener(this); // 显示隐藏
		teacher_listview.setOnItemClickListener(this); // 教师列表
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.purchaseText: // 立即购买
			String purchase = purchaseText.getText().toString();
			if (getResources().getString(R.string.liji_look).equals(purchase)) {
				courseDetailsActivity.playVideo();
			} else {
				if (userId == 0) {
					Intent intent = new Intent(getActivity(), LoginActivity.class);
					startActivity(intent);
				} else {

					/**
					 * 支付之前检测是否存在历史订单未支付 如果存在直接跳转到重新支付
					 * 
					 */
					if (!is_exit) {
						Intent intent = new Intent(getActivity(), ConfirmOrderActivity.class);
						Logs.info("开始支付:price="+publicEntity.getEntity().getCourse().getCurrentprice());
						intent.putExtra("publicEntity", publicEntity);
						startActivity(intent);
					} else {
						Intent intent = new Intent(getActivity(), PaymentConfirmOrderActivity.class);
						intent.putExtra("orderId", orderEntity.getId());
						Logs.info("开始支付11111:price="+publicEntity.getEntity().getCourse().getCurrentprice()+"       "+orderEntity.getId());
						startActivity(intent);
					}

				}
			}
			break;
		case R.id.recommend_switch:
			if (flag) {
				webView_layout.setVisibility(View.GONE);
				open.setBackgroundResource(R.drawable.close);
				flag = false;
			} else {
				webView_layout.setVisibility(View.VISIBLE);
				open.setBackgroundResource(R.drawable.open);
				flag = true;
			}
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		switch (parent.getId()) {
		case R.id.teacher_listview: // 教师列表
			int teacherId = course.getTeacherList().get(position).getId();
			Intent intent = new Intent(getActivity(), LecturerDetailsActivity.class);
			intent.putExtra("teacherId", teacherId);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	public void onResume() {
		Log.i("lala", "onResume");
		super.onResume();
		is_exit = getOrderList(userId, publicEntity.getEntity().getCourse().getId());
	}
	/**
	 * @author bin 修改人: 时间:2015-10-21 下午9:17:03 方法说明:联网获取我的订单的方法
	 */
	private boolean getOrderList(int userId, int courseId) {

		RequestParams params = new RequestParams();
		params.put("queryTrxorder.userId", userId);
		params.put("queryTrxorder.courseId", courseId);
		params.put("queryTrxorder.trxStatus", "INIT");
		Log.i("wulala", Address.MY_ORDER_LIST + params + "查询是否有历史订单");
		httpClient.post(Address.MY_ORDER_LIST, params, new TextHttpResponseHandler() {
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
						boolean success = publicEntity.isSuccess();
						if (success) {
							Logs.info("publicEntity.getEntity()=="+publicEntity.getEntity());
							List<OrderEntity> trxorderList = publicEntity.getEntity().getOrderList();
							Logs.info("trxorderList=="+trxorderList);
							if (trxorderList==null || trxorderList.size() == 0) {
								is_exit = false;
							} else {
								is_exit = true;
								orderEntity = trxorderList.get(0);
							}
						}
					} catch (Exception e) {
						is_exit = false;
						Log.i("wulala", e.toString());
					}

				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
		return is_exit;
	}
}
