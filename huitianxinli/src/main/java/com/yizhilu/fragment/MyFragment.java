package com.yizhilu.fragment;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.baoli.PolyvDownloadListActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.DiscountCouponActivity;
import com.yizhilu.huitianxinli.LoginActivity;
import com.yizhilu.huitianxinli.MyAccountActivity;
import com.yizhilu.huitianxinli.MyCollectionActivity;
import com.yizhilu.huitianxinli.MyCourseActivity;
import com.yizhilu.huitianxinli.MyOrderActivity;
import com.yizhilu.huitianxinli.OpinionFeedbackActivity;
import com.yizhilu.huitianxinli.PersonalInformationActivity;
import com.yizhilu.huitianxinli.PersonalResume;
import com.yizhilu.huitianxinli.PlayHistoryActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.huitianxinli.SystemAcmActivity;
import com.yizhilu.huitianxinli.SystemSettingActivity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.CircleImageView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author bin 修改人: 时间:2015-10-15 下午2:05:17 类说明:我的类
 */
public class MyFragment extends BaseFragment {
	public static MyFragment myFragment; // 本类的对象
	private View inflate; // 我的总布局
	private boolean isFrist = true; // 是否是第一次加载
	// private MyBroadCast broadCast; // 广播的对象
	private LinearLayout personal_information, feedback, historyLayout; // 个人信息
																		// 意见反馈
																		// 播放记录
	private RelativeLayout layout_myorder, layout_myaccount; // 我的订单,我的账户
	private LinearLayout system_set_linear;// 系统设置
	private LinearLayout my_collection_linear, personal_resume;// 我的收藏,个人简历
	private LinearLayout layout_systemAcm, layout_discount_coupon;// 系统公告,优惠券
	private LinearLayout download_layiout; // 下载的布局
	private TextView exit_login, userName, courseNum, playJiluNum, download_num, signature; // 退出登錄的佈局,用户名,课程数,播放记录数,离线下载数,签名
	private int userId; // 用户的Id
	private CircleImageView head_iamge; // 用户头像
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private ImageLoader imageLoader; // 加载图片的对象
	private SharedPreferences preferences; // 获取用户Id的对象
	private OnSetUserMessageLisenner setUserMessageLisenner; // 设置用户信息的接口
	private Animation animation; // 退出登录的动画
	private LinearLayout myCourse; // 我的课程
	private ImageView setting_image, prompt_image; // 设置,小圆点
	private Dialog dialog; // 退出显示的对话框
	private TextView money; // 账户余额

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		imageLoader = ImageLoader.getInstance(); // 加载图片的对象
		inflate = inflater.inflate(R.layout.fragment_my, container, false);
		return inflate;
	}

	/**
	 * @author bin 修改人: 时间:2015-10-28 下午8:53:22 类说明:设置用户信息的接口
	 */
	public interface OnSetUserMessageLisenner {
		public void onSetUserMessage(String message);
	}

	/**
	 * @author bin 修改人: 时间:2015-10-28 下午8:54:48 方法说明:給接口付值的方法
	 */
	public void setOnSetUserMessageLisenner(OnSetUserMessageLisenner setUserMessageLisenner) {
		this.setUserMessageLisenner = setUserMessageLisenner;
	}

	/**
	 * 获取本类实例的方法
	 */
	public static MyFragment getInstance() {
		if (myFragment == null) {
			myFragment = new MyFragment();
		}
		return myFragment;
	}

	@Override
	public void onResume() {
		super.onResume();
		// if (broadCast == null) {
		// broadCast = new MyBroadCast();
		// IntentFilter filter = new IntentFilter();
		// filter.addAction("myFragment");
		// filter.addAction("login");
		// getActivity().registerReceiver(broadCast, filter);
		// }
		userId = preferences.getInt("userId", 0); // 得到用户的Id
		if (userId == 0) {
			exit_login.setVisibility(View.GONE);
			userName.setText("未登录");
			// signature.setVisibility(View.VISIBLE);
			// signature.setText(getResources().getString(R.string.signature));
		} else {
			// 获取用户信息的方法
			getUserMessage();
			exit_login.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-10-28 下午8:28:42 方法说明:联网获取用户信息的方法
	 */
	private void getUserMessage() {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		Log.i("lala", Address.MY_MESSAGE + "?" + params.toString());
		httpClient.post(Address.MY_MESSAGE, params, new TextHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				if (!TextUtils.isEmpty(data)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							if (publicEntity.getEntity().isNotPayOrder()) {
								prompt_image.setVisibility(View.VISIBLE);
							} else {
								prompt_image.setVisibility(View.GONE);
							}
							EntityPublic userExpandDto = publicEntity.getEntity().getUserExpandDto();
							String showname = userExpandDto.getShowname();
							String email = userExpandDto.getEmail();
							String mobile = userExpandDto.getMobile();
							if (!TextUtils.isEmpty(showname)) {
								userName.setText(showname);
							} else if (!TextUtils.isEmpty(email)) {
								userName.setText(email);
							} else {
								userName.setText(mobile);
							}
							String balance = publicEntity.getEntity().getBalance();
							if (TextUtils.isEmpty(balance)) {
								money.setText("余额 : ￥ 0.00");
							} else {
								// String amountString =
								// balance.split("\\.")[0];
								// String amouString = balance.split("\\.")[1];
								// String ammString = amouString.substring(0,
								// 1);
								money.setText("余额 : ￥ " + balance);
							}
							// signature.setVisibility(View.VISIBLE);
							String userInfo = userExpandDto.getUserInfo();
							if (TextUtils.isEmpty(userInfo)) {
								// signature.setText(getResources().getString(R.string.signature));
							} else {
								// signature.setText(userInfo);
							}
							imageLoader.displayImage(Address.IMAGE_NET + userExpandDto.getAvatar(), head_iamge,
									HttpUtils.getUserHeadDisPlay());
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
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		preferences = getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
		progressDialog = new ProgressDialog(getActivity()); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		userName = (TextView) inflate.findViewById(R.id.userName); // 用户名
		// signature = (TextView) inflate.findViewById(R.id.signature); //签名
		download_layiout = (LinearLayout) inflate.findViewById(R.id.download_layout);
		download_num = (TextView) inflate.findViewById(R.id.download_num); // 下载数据
		courseNum = (TextView) inflate.findViewById(R.id.courseNum); // 课程数
		playJiluNum = (TextView) inflate.findViewById(R.id.playJiluNum); // 播放记录数据
		head_iamge = (CircleImageView) inflate.findViewById(R.id.head_iamge); // 用户头像
		personal_information = (LinearLayout) inflate.findViewById(R.id.personal_information);// 个人信息
		personal_resume = (LinearLayout) inflate.findViewById(R.id.personal_resume); // 个人简历
		layout_myorder = (RelativeLayout) inflate.findViewById(R.id.layout_myorder);// 我的订单
		prompt_image = (ImageView) inflate.findViewById(R.id.prompt_image);
		layout_myaccount = (RelativeLayout) inflate.findViewById(R.id.layout_myaccount);// 我的账户
		money = (TextView) inflate.findViewById(R.id.money); // 账户余额
		layout_discount_coupon = (LinearLayout) inflate.findViewById(R.id.layout_discount_coupon); // 优惠券
		feedback = (LinearLayout) inflate.findViewById(R.id.opinion_feedback); // 意见反馈
		system_set_linear = (LinearLayout) inflate.findViewById(R.id.system_set_linear);// 系统设置
		my_collection_linear = (LinearLayout) inflate.findViewById(R.id.my_collection_linear);// 我的收藏
		historyLayout = (LinearLayout) inflate.findViewById(R.id.playHistory); // 播放记录
		exit_login = (TextView) inflate.findViewById(R.id.exit_login); // 退出登錄的佈局
		layout_systemAcm = (LinearLayout) inflate.findViewById(R.id.layout_systemAcm);
		myCourse = (LinearLayout) inflate.findViewById(R.id.my_course); // 我的课程
		setting_image = (ImageView) inflate.findViewById(R.id.setting_image); // 设置
	}

	/**
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		head_iamge.setOnClickListener(this); // 头像
		userName.setOnClickListener(this); // 登录
		personal_information.setOnClickListener(this); // 个人信息
		personal_resume.setOnClickListener(this); // 个人简历
		layout_myorder.setOnClickListener(this);// 我的订单
		layout_myaccount.setOnClickListener(this);
		feedback.setOnClickListener(this); // 意见反馈
		layout_discount_coupon.setOnClickListener(this); // 优惠券
		system_set_linear.setOnClickListener(this);// 系统设置
		my_collection_linear.setOnClickListener(this);
		historyLayout.setOnClickListener(this); // 播放记录
		exit_login.setOnClickListener(this); // 退出登录
		layout_systemAcm.setOnClickListener(this);
		download_layiout.setOnClickListener(this);
		myCourse.setOnClickListener(this); // 我的课程
		setting_image.setOnClickListener(this); // 设置的图标
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		userId = preferences.getInt("userId", 0); // 得到用户的Id
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.download_layout: // 离线下载
			// if (userId == 0) {
			// intent.setClass(getActivity(), LoginActivity.class);
			// startActivity(intent);
			// } else {
			intent.setClass(getActivity(), PolyvDownloadListActivity.class);
			startActivity(intent);
			// }
			break;
		case R.id.personal_information: // 个人信息
			if (userId == 0) {
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			} else {
				intent.setClass(getActivity(), PersonalInformationActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.personal_resume: // 学习统计
			if (userId == 0) {
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			} else {
				intent.setClass(getActivity(), PersonalResume.class);
				startActivity(intent);
			}
			break;
		case R.id.layout_myorder: // 我的订单
			if (userId == 0) {
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			} else {
				intent.setClass(getActivity(), MyOrderActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.layout_myaccount: // 我的账户
			if (userId == 0) {
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			} else {
				intent.setClass(getActivity(), MyAccountActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.layout_discount_coupon: // 优惠券
			if (userId == 0) {
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			} else {
				intent.setClass(getActivity(), DiscountCouponActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.opinion_feedback: // 意见反馈
			intent.setClass(getActivity(), OpinionFeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.system_set_linear: // 系统设置
			intent.setClass(getActivity(), SystemSettingActivity.class);
			startActivity(intent);
			break;
		case R.id.my_collection_linear: // 我的收藏
			if (userId == 0) {
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			} else {
				intent.setClass(getActivity(), MyCollectionActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.playHistory: // 播放记录
			if (userId == 0) {
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			} else {
				intent.setClass(getActivity(), PlayHistoryActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.exit_login: // 退出登录
			showQuitDiaLog();
			break;
		case R.id.layout_systemAcm: // 系统公告
			if (userId == 0) {
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			} else {
				intent.setClass(getActivity(), SystemAcmActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.head_iamge: // 用户头像
			if (userId == 0) {
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			} else {
				intent.setClass(getActivity(), PersonalInformationActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.userName: // 昵称
			if (userId == 0) {
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.my_course: // 我的课程
			if (userId == 0) {
				intent.setClass(getActivity(), LoginActivity.class);
				startActivity(intent);
			} else {
				intent.setClass(getActivity(), MyCourseActivity.class);
				startActivity(intent);
			}
			break;
		case R.id.setting_image: // 设置的图标
			intent.setClass(getActivity(), SystemSettingActivity.class);
			startActivity(intent);
			break;
		}
	}

	// 退出时显示的diaLog
	public void showQuitDiaLog() {
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_show, null);
		WindowManager manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
		@SuppressWarnings("deprecation")
		int width = manager.getDefaultDisplay().getWidth();
		int scree = (width / 3) * 2;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.width = scree;
		view.setLayoutParams(layoutParams);
		dialog = new Dialog(getActivity(), R.style.custom_dialog);
		dialog.setContentView(view);
		dialog.setCancelable(false);
		dialog.show();
		TextView textView = (TextView) view.findViewById(R.id.texttitles);
		textView.setText("确定退出 ?");
		TextView btnsure = (TextView) view.findViewById(R.id.dialogbtnsure);
		LinearLayout linbtnsure = (LinearLayout) view.findViewById(R.id.dialog_linear_sure);
		linbtnsure.setOnClickListener(new OnClickListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onClick(View v) {
				animation = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_out);
				exit_login.setAnimation(animation);
				exit_login.setVisibility(View.GONE);
				getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).edit().putInt("userId", 0)
						.commit();
				userId = 0;
				head_iamge.setImageResource(R.drawable.head_bg); // 修改用户头像
				money.setText("");
				prompt_image.setVisibility(View.GONE);
				userName.setText("未登陆");
				// signature.setText(getResources().getString(R.string.signature));
				// courseNum.setText("0"); //设置我的课程
				// download_num.setText("0"); //离线下载
				// playJiluNum.setText("0");
				// 通知侧滑栏改变头像
				if (setUserMessageLisenner != null) {
					setUserMessageLisenner.onSetUserMessage("exit_login");
				}
				dialog.cancel();
			}
		});
		TextView btncancle = (TextView) view.findViewById(R.id.dialogbtncancle);
		LinearLayout linbtncancle = (LinearLayout) view.findViewById(R.id.dialog_linear_cancle);
		linbtncancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}

	// @Override
	// public void onDestroy() {
	// super.onDestroy();
	// if (broadCast != null) {
	// getActivity().unregisterReceiver(broadCast);
	// }
	// }
}
