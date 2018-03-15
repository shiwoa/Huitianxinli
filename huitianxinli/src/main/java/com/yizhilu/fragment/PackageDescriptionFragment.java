package com.yizhilu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.adapter.PackageCourseAdapter;
import com.yizhilu.adapter.TeacherAdapter;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.baoli.BLCourseDetailsActivity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.OrderEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.ComboDetailsActivity;
import com.yizhilu.huitianxinli.ConfirmOrderActivity;
import com.yizhilu.huitianxinli.LecturerDetailsActivity;
import com.yizhilu.huitianxinli.LoginActivity;
import com.yizhilu.huitianxinli.PaymentConfirmOrderActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollGridView;
import com.yizhilu.view.NoScrollListView;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * @author liuchangqi 修改人: 时间:2016-7-19 上午11:14:37 类说明:套餐描述
 */
public class PackageDescriptionFragment extends BaseFragment implements OnItemClickListener, TextWatcher {
	private View inflate;// 套餐评论的总布局
	private static PackageDescriptionFragment packageDescriptionFragment;
	private PublicEntity publicEntity; // 课程介绍的总实体
	private TextView courseTitle, course_price, purchaseText; // 课程名字,课程的价格,立即购买按钮
	private WebView course_webView; // 课程介绍
	private NoScrollListView teacher_listview; // 教师的列表
	private TextView priceTwo; // 原价
	private ImageView open, free_view; // 显示关闭,免费
	private boolean flag = true;
	private EntityCourse course; // 课程介绍的实体
	private LinearLayout webView_layout; // webView的總佈局
	private int userId; // 用户Id
	private NoScrollGridView home_recommend_course_list;// 相关课程
	private List<EntityCourse> courseDtoList;// 相关课程数据源
	private PackageCourseAdapter course_adapter;// 课程套餐，课程推荐适配器
	private TextView related_recommendation;
	private ComboDetailsActivity comboDetailsActivity;
	private TextView degree_text, readyTime_text, component_text, cookTime_text;// 难易度,时间，分量,用时

	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private List<EntityPublic> EntityCourses;
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private Dialog dialog;
	private View view;
	private ImageView course_pic;
	private ImageLoader loader;
	private EditText editText;
	private TextView shang_name, lijidashang;
	private ImageView dashang;
	private boolean is_exit;
	protected OrderEntity orderEntity;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		comboDetailsActivity = (ComboDetailsActivity) activity;
	}

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_package_description, container, false);
		Bundle bundle = getArguments();
		publicEntity = (PublicEntity) bundle.getSerializable("publicEntity");
		return inflate;
	}

	/**
	 * 获取本类实例的方法
	 */
	public static PackageDescriptionFragment getInstance() {
		if (packageDescriptionFragment == null) {
			packageDescriptionFragment = new PackageDescriptionFragment();
		}
		return packageDescriptionFragment;
	}

	@Override
	public void initView() {
		httpClient = new AsyncHttpClient();// 联网获取数据的对象
		progressDialog = new ProgressDialog(getActivity());// 加载数据显示的dialog
		EntityCourses = new ArrayList<EntityPublic>();
		userId = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).getInt("userId", 0);
		courseTitle = (TextView) inflate.findViewById(R.id.courseTitle); // 课程名字
		course_price = (TextView) inflate.findViewById(R.id.course_price); // 课程的价格
		purchaseText = (TextView) inflate.findViewById(R.id.purchaseText); // 立即购买
		free_view = (ImageView) inflate.findViewById(R.id.free_view); // 免费
		course_webView = (WebView) inflate.findViewById(R.id.course_webView); // 课程介绍
		home_recommend_course_list = (NoScrollGridView) inflate.findViewById(R.id.home_recommend_course_list);
		loader = ImageLoader.getInstance();
		teacher_listview = (NoScrollListView) inflate.findViewById(R.id.teacher_listview); // 教师的列表
		priceTwo = (TextView) inflate.findViewById(R.id.price_two); // 原价
		priceTwo.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);// 添加中间横线
		open = (ImageView) inflate.findViewById(R.id.recommend_switch); // 展开关闭
		webView_layout = (LinearLayout) inflate.findViewById(R.id.webView_layout); // webView的總佈局
		degree_text = (TextView) inflate.findViewById(R.id.degree_text);// 难易度
		readyTime_text = (TextView) inflate.findViewById(R.id.readyTime_text);// 时间
		component_text = (TextView) inflate.findViewById(R.id.component_text);// 分量
		cookTime_text = (TextView) inflate.findViewById(R.id.cookTime_text);// 分量
		course = publicEntity.getEntity().getCourse();
		courseTitle.setText(course.getName());
		if (course.getIsPay() == 0) { // 免费
			// price_pay_layout.setVisibility(View.GONE);
			purchaseText.setVisibility(View.GONE);
			free_view.setVisibility(View.VISIBLE);
		} else if (course.getIsPay() == 1) { // 付费
			if (publicEntity.getEntity().isIsok()) { // 已经购买
				purchaseText.setVisibility(View.VISIBLE);
				purchaseText.setBackgroundResource(R.drawable.text_yellow_bg_solid_frame);
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

		home_recommend_course_list = (NoScrollGridView) inflate.findViewById(R.id.home_recommend_course_list);
		courseDtoList = publicEntity.getEntity().getCourseDtoList();
		if (courseDtoList != null && courseDtoList.size() > 0) {
			course_adapter = new PackageCourseAdapter(getActivity(), courseDtoList);
			home_recommend_course_list.setAdapter(course_adapter);
		}
		related_recommendation = (TextView) inflate.findViewById(R.id.related_recommendation);
	}

	@Override
	public void addOnClick() {
		purchaseText.setOnClickListener(this); // 立即购买的按钮
		teacher_listview.setOnItemClickListener(this); // 教师列表
		home_recommend_course_list.setOnItemClickListener(this);// 相关课程
		related_recommendation.setOnClickListener(this);
		home_recommend_course_list.setOnItemClickListener(this);
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
				intent.putExtra("courseId", publicEntity.getEntity().getCurrentCourseId());
				getActivity().startActivity(intent);
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
						intent.putExtra("publicEntity", publicEntity);
						startActivity(intent);
					} else {
						Intent intent = new Intent(getActivity(), PaymentConfirmOrderActivity.class);
						intent.putExtra("orderId", orderEntity.getId());
						startActivity(intent);
					}
				}
			}
			break;
		case R.id.related_recommendation:// 跳到课程列表
			// Intent intent = new Intent();
			// intent.setClass(getActivity(), CourseListActivity.class);
			// intent.putExtra("id",
			// publicEntity.getEntity().getDefaultKpointId());
			// getActivity().startActivity(intent);
			// comboDetailsActivity.finish();
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		Intent intent = new Intent();
		switch (parent.getId()) {
		case R.id.teacher_listview: // 教师列表
			comboDetailsActivity.finish();

			int teacherId = course.getTeacherList().get(position).getId();
			Intent intent2 = new Intent(getActivity(), LecturerDetailsActivity.class);
			intent2.putExtra("teacherId", teacherId);
			startActivity(intent2);
			break;
		case R.id.home_recommend_course_list: // 推荐列表
			intent.setClass(getActivity(), BLCourseDetailsActivity.class);
			intent.putExtra("courseId", EntityCourses.get(position).getCourseId());
			getActivity().startActivity(intent);
			comboDetailsActivity.finish();
			break;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.toString().contains(".")) {
			if (s.length() - 1 - s.toString().indexOf(".") > 2) {
				s = s.toString().subSequence(0, s.toString().indexOf(".") + 3);
				editText.setText(s);
				editText.setSelection(s.length());
			}
		}
		if (s.toString().trim().substring(0).equals(".")) {
			s = "0" + s;
			editText.setText(s);
			editText.setSelection(2);
		}
		if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
			if (!s.toString().substring(1, 2).equals(".")) {
				editText.setText(s.subSequence(0, 1));
				editText.setSelection(1);
				return;
			}
		}

	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
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
							List<OrderEntity> trxorderList = publicEntity.getEntity().getOrderList();

							if (trxorderList == null | trxorderList.size() == 0) {
								is_exit = false;
							} else {
								is_exit = true;
								orderEntity = trxorderList.get(0);
							}
						}
					} catch (Exception e) {
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
