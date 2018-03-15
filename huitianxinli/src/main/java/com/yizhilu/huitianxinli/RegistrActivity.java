package com.yizhilu.huitianxinli;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.ValidateUtil;

/**
 * @author bin 修改人: 时间:2015-10-19 上午11:03:32 类说明:手机注册的类
 */
public class RegistrActivity extends BaseActivity {
	private LinearLayout back_layout, goto_login, errorMessage_layout,
			email_regist, moblieCodeLayout; // 返回的布局,去登录,错误信息的布局,邮箱注册,验证码的布局
	private TextView title_text, agreementText, registerText, error_message,
			email_text, get_obtain_code; // 本類的標題,协议,注册的按钮,错误信息,邮箱,获取验证码
	private EditText userName_edit, verification_code, passWord_edit,
			confirm_passWord_edit; // 用户名,验证码,设置密码,确认密码
	private View userName_line, email_line, passWord_line,
			confirm_passWord_line;
	private CheckBox check_box; // 协议图标
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private Intent intent; // 意图对象
	private boolean isCountdown, isMobileCode, isBinDing; // 是否是发送验证码,手机验证码是否开发,是否是从绑定页面跳过来的
	private String cusName, appId, appType, photo; // 第三方的名称,id,类型,头像
	private HttpUtils httpUtils; // 工具类(主要用添加登陆的方法)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_register);
		// 获取传过来的信息
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}

	/**
	 * @author bin 修改人: 时间:2016-1-30 下午3:32:14 方法说明:获取传过来的信息
	 */
	private void getIntentMessage() {
		intent = getIntent();
		isBinDing = intent.getBooleanExtra("isBinDing", false);
		cusName = intent.getStringExtra("cusName");
		appId = intent.getStringExtra("appId");
		appType = intent.getStringExtra("appType");
		photo = intent.getStringExtra("photo");
	}

	@Override
	public void initView() {
		progressDialog = new ProgressDialog(RegistrActivity.this); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		if (intent == null) {
			intent = new Intent(); // 意图对象
		}
		httpUtils = new HttpUtils(RegistrActivity.this); // 工具类(主要用添加登陆的方法)
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回的布局
		title_text = (TextView) findViewById(R.id.title_text); // 本類的標題
		email_regist = (LinearLayout) findViewById(R.id.email_regist); // 邮箱注册
		email_text = (TextView) findViewById(R.id.email_text); // 邮箱注册
		email_text.setText(R.string.email_regist);
		goto_login = (LinearLayout) findViewById(R.id.goto_login); // 去登录
		userName_edit = (EditText) findViewById(R.id.userName_edit); // 用户名
		moblieCodeLayout = (LinearLayout) findViewById(R.id.moblieCodeLayout); // 验证码的布局
		verification_code = (EditText) findViewById(R.id.email_edit); // 验证码
		get_obtain_code = (TextView) findViewById(R.id.get_obtain_code); // 获取验证码
		confirm_passWord_edit = (EditText) findViewById(R.id.confirm_passWord_edit); // 确认密码
		passWord_edit = (EditText) findViewById(R.id.passWord_edit); // 设置密码
		userName_line = findViewById(R.id.userName_line);
		email_line = findViewById(R.id.email_line);
		passWord_line = findViewById(R.id.passWord_line);
		confirm_passWord_line = findViewById(R.id.confirm_passWord_line);
		check_box = (CheckBox) findViewById(R.id.check_box); // 协议图标
		agreementText = (TextView) findViewById(R.id.agreementText); // 协议
		errorMessage_layout = (LinearLayout) findViewById(R.id.errorMessage_layout); // 错误信息的布局
		error_message = (TextView) findViewById(R.id.error_message); // 错误信息
		registerText = (TextView) findViewById(R.id.registerText); // 注册的按钮
		title_text.setText(getResources().getString(R.string.register)); // 设置标题
		// 获取邮箱或手机号接受验证码开关的接口
		getCodeSwitchData();
	}

	/**
	 * @author bin 修改人: 时间:2016-1-28 下午3:13:09 方法说明:获取邮箱或手机号接受验证码开关的接口
	 */
	private void getCodeSwitchData() {
		Log.i("lala", Address.GETCODESWITCH);
		httpClient.get(Address.GETCODESWITCH, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				if (!TextUtils.isEmpty(data)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(data,
								PublicEntity.class);
						String message = publicEntity.getMessage();
						if (publicEntity.isSuccess()) {
							EntityPublic entityPublic = publicEntity
									.getEntity();
							String mobileCode = entityPublic
									.getVerifyRegMobileCode();
							if ("ON".equals(mobileCode)) {
								isMobileCode = true;
								moblieCodeLayout.setVisibility(View.VISIBLE);
								email_line.setVisibility(View.VISIBLE);
							} else {
								// 验证码没开放
								isMobileCode = false;
								moblieCodeLayout.setVisibility(View.GONE);
								email_line.setVisibility(View.GONE);
							}
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,
					Throwable arg3) {

			}
		});
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this); // 返回的布局
		email_regist.setOnClickListener(this); // 邮箱注册
		goto_login.setOnClickListener(this); // 去登录
		agreementText.setOnClickListener(this); // 协议
		registerText.setOnClickListener(this); // 注册的按钮
		get_obtain_code.setOnClickListener(this); // 获取验证码
		userName_edit.setOnFocusChangeListener(this); // 失去或获取焦点的监听
		passWord_edit.setOnFocusChangeListener(this); // 失去或获取焦点的监听
		verification_code.setOnFocusChangeListener(this); // 验证码
		confirm_passWord_edit.setOnFocusChangeListener(this); // 确认密码
	}

	/**
	 * 控件的点击事件
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		String mobile = userName_edit.getText().toString();
		switch (v.getId()) {
		case R.id.back_layout: // 返回的布局
			this.finish();
			break;
		case R.id.email_regist: // 邮箱注册
			intent.setClass(RegistrActivity.this, EmailRegistrActivity.class);
			if (isBinDing) {
				intent.putExtra("isBinDing", isBinDing);
				intent.putExtra("cusName", cusName); // 第三方昵称
				intent.putExtra("appId", appId); // 第三方的Id
				intent.putExtra("appType", appType); // 第三方登录的类型
				intent.putExtra("photo", photo); // 第三方的头像
			}
			startActivity(intent);
			this.finish();
			break;
		case R.id.goto_login: // 去登录
			intent.setClass(RegistrActivity.this, LoginActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.get_obtain_code: // 获取验证码

			if (!isCountdown) {
				if (TextUtils.isEmpty(mobile)) {
					ConstantUtils.showMsg(RegistrActivity.this, "请输入手机号");
					return;
				}
				if (!ValidateUtil.isMobile(mobile)) {
					ConstantUtils.showMsg(RegistrActivity.this, "请输入正确的手机号");
					return;
				}
				// 手机注册,获取sgin值
				getSginData(mobile);
			}
			break;
		case R.id.agreementText: // 协议
			intent.setClass(RegistrActivity.this, AgreementActivity.class);
			startActivity(intent);
			break;
		case R.id.registerText: // 注册的按钮
			errorMessage_layout.setVisibility(View.GONE);
			error_message.setText("");
			String code = verification_code.getText().toString();
			String passWord = passWord_edit.getText().toString();
			String confirm_passWord = confirm_passWord_edit.getText()
					.toString();
			if (TextUtils.isEmpty(mobile)) {
				errorMessage_layout.setVisibility(View.VISIBLE);
				error_message.setText("请输入手机号");
				return;
			}
			if (isMobileCode) { // 如果验证码开放的话
				if (TextUtils.isEmpty(code)) {
					errorMessage_layout.setVisibility(View.VISIBLE);
					error_message.setText("请输入验证码");
					return;
				}
			}
			if (TextUtils.isEmpty(passWord)) {
				errorMessage_layout.setVisibility(View.VISIBLE);
				error_message.setText("请输入密码");
				return;
			}
			if (TextUtils.isEmpty(confirm_passWord)) {
				errorMessage_layout.setVisibility(View.VISIBLE);
				error_message.setText("请确认密码");
				return;
			}
			if (!ValidateUtil.isMobile(mobile)) {
				errorMessage_layout.setVisibility(View.VISIBLE);
				error_message.setText("请输入正确的手机号");
				return;
			}
			if (!(passWord.length() >= 6 && passWord.length() <= 18)
					|| !ValidateUtil.isNumberOrLetter(passWord)) {
				errorMessage_layout.setVisibility(View.VISIBLE);
				error_message.setText("输入密码格式不正确");
				return;
			}
			if (!confirm_passWord.equals(passWord)) {
				errorMessage_layout.setVisibility(View.VISIBLE);
				error_message.setText("两次密码不对应");
				return;
			}
			boolean checked = check_box.isChecked();
			if (!checked) {
				errorMessage_layout.setVisibility(View.VISIBLE);
				error_message.setText("请阅读并遵守协议方可注册");
				return;
			}
			if (isBinDing) { // 在绑定页面跳过来的
				// 绑定第三方注册的方法
				getRegisterBinding(mobile, code, passWord, confirm_passWord);
			} else {
				// 联网注册的方法
				// getRegister(mobile, code, passWord, confirm_passWord);
				getRegisterNF(mobile, code, passWord, confirm_passWord);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * @author bin 修改人: 时间:2016-1-30 下午4:16:27 方法说明:绑定第三方注册的方法
	 */
	private void getRegisterBinding(final String mobile, String code,
			final String userPassword, String confirmPwd) {
		RequestParams params = new RequestParams();
		params.put("regType", "mobile");
		params.put("mobile", mobile);
		params.put("checkCode", code);
		params.put("userPassword", userPassword);
		params.put("confirmPwd", confirmPwd);
		params.put("cusName", cusName);
		params.put("appId", appId);
		params.put("appType", appType);
		params.put("photo", photo);
		httpClient.post(Address.REGISTERBINDING, params,
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
									ConstantUtils.showMsg(RegistrActivity.this,
											message);
									// 绑定成功的方法(绑定成功就相当于登录)
									LoginScuessMethod(publicEntity);
								} else {
									ConstantUtils.showMsg(RegistrActivity.this,
											message);
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
	 * @author bin 修改人: 时间:2016-1-30 上午10:37:24 方法说明:登陆成功的方法
	 */
	private void LoginScuessMethod(PublicEntity publicEntity) {
		int userId = publicEntity.getEntity().getId();
		// 添加登陆记录的方法
		httpUtils.addLoginRecord(userId);
		// 除了MainActivity把所有的Activity都finish掉
		DemoApplication.getInstance()
				.SingleLoginExit(MainActivity.getIntence());
		DemoApplication.getInstance().finishActivity(MainActivity.getIntence());
		getSharedPreferences("userId", MODE_PRIVATE).edit()
				.putInt("userId", userId).commit();
		Intent intent = new Intent(RegistrActivity.this, MainActivity.class);
		startActivity(intent);
		intent.setAction("login");
		sendBroadcast(intent);
		RegistrActivity.this.finish();
	}

	/**
	 * @author bin 修改人: 时间:2016-1-12 下午3:46:13 方法说明:获取sgin值的方法
	 */
	private void getSginData(final String mobile) {
		RequestParams params = new RequestParams();
		params.put("mobileType", "Android");
		params.put("mobile", mobile);
		httpClient.post(Address.GET_SGIN, params,
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
								JSONObject object = JSON.parseObject(data);
								String message = object.getString("message");
								if (object.getBoolean("success")) {
									String sgin = object.getString("entity");
									// 联网获取验证码的方法
									getVerificationCode(mobile, sgin);
								} else {
									ConstantUtils.showMsg(RegistrActivity.this,
											message);
								}
							} catch (Exception e) {
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {

					}
				});
	}

	/**
	 * @author bin 修改人: 时间:2015-12-4 上午11:42:18 方法说明:获取验证码的接口
	 */
	private void getVerificationCode(String mobile, String sgin) {
		RequestParams params = new RequestParams();
		params.put("sendType", "register");
		params.put("mobile", mobile);
		params.put("mobileType", "Android");
		params.put("sgin", sgin);
		Log.i("lala", Address.GET_PHONE_CODE + "?" + params.toString());
		httpClient.post(Address.GET_PHONE_CODE, params,
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
								JSONObject object = JSON.parseObject(data);
								String message = object.getString("message");
								if (object.getBoolean("success")) {
									ConstantUtils.showMsg(RegistrActivity.this,
											message);
									isCountdown = true;
									// 开启线程
									startTheard();
								} else {
									ConstantUtils.showMsg(RegistrActivity.this,
											message);
								}
							} catch (Exception e) {
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Log.i("lala", ".............");
						HttpUtils.exitProgressDialog(progressDialog);
					}
				});
	}

	/**
	 * @author bin 修改人: 时间:2016-1-12 下午4:14:39 方法说明:开启倒计时的线程
	 */
	private void startTheard() {
		new CountDownTimer(60000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {
				get_obtain_code.setText("重新获取" + millisUntilFinished / 1000
						+ "秒");
			}

			@Override
			public void onFinish() {
				get_obtain_code.setText("获取验证码");
				isCountdown = false;
			}
		}.start();
	}

	/**
	 * @author bishuang 修改人: 时间:2015-10-13 上午9:36:37 方法说明:注册的方法_手机验证码为ON
	 *         邮箱验证码为OF
	 */
	private void getRegisterNF(final String mobile, String code,
			final String userPassword, String confirmPwd) {
		final RequestParams params = new RequestParams();
		params.put("regType", "mobile");
		params.put("mobile", mobile);
		params.put("userPassword", userPassword);
		params.put("confirmPwd", confirmPwd);
		params.put("mobileCheckCode", code); // 手机验证码
		httpClient.post(Address.REGISTER, params,
				new TextHttpResponseHandler() {

					@Override
					public void onStart() {
						super.onStart();
						HttpUtils.showProgressDialog(progressDialog);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						Log.i("lala", Address.REGISTER + params.toString()
								+ "---手机验证码为ON 邮箱验证码为OFF");
						HttpUtils.exitProgressDialog(progressDialog);
						if (!TextUtils.isEmpty(data)) {
							try {
								PublicEntity publicEntity = JSON.parseObject(
										data, PublicEntity.class);
								String message = publicEntity.getMessage();
								if (publicEntity.isSuccess()) {
									intent.setClass(RegistrActivity.this,
											LoginActivity.class);
									intent.putExtra("userName", mobile);
									intent.putExtra("passWord", userPassword);
									startActivity(intent);
									RegistrActivity.this.finish();
								} else {
									ConstantUtils.showMsg(RegistrActivity.this,
											message);
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

	/*
	 * 失去或获取焦点的监听
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		super.onFocusChange(v, hasFocus);
		switch (v.getId()) {
		case R.id.userName_edit: // 用户名
			if (hasFocus) {
				// userName_line.setBackgroundResource(R.color.lanse);
				errorMessage_layout.setVisibility(View.GONE);
				error_message.setText("");
			} else {
				userName_line.setBackgroundResource(R.color.color_F6);
			}
			break;
		case R.id.email_edit: // 邮箱
			if (hasFocus) {
				// email_line.setBackgroundResource(R.color.lanse);
				errorMessage_layout.setVisibility(View.GONE);
				error_message.setText("");
			} else {
				email_line.setBackgroundResource(R.color.color_F6);
			}
			break;
		case R.id.passWord_edit: // 密码
			if (hasFocus) {
				// passWord_line.setBackgroundResource(R.color.lanse);
				errorMessage_layout.setVisibility(View.GONE);
				error_message.setText("");
			} else {
				passWord_line.setBackgroundResource(R.color.color_F6);
			}
			break;
		case R.id.confirm_passWord_edit: // 确认密码
			if (hasFocus) {
				// confirm_passWord_line.setBackgroundResource(R.color.lanse);
				errorMessage_layout.setVisibility(View.GONE);
				error_message.setText("");
			} else {
				confirm_passWord_line.setBackgroundResource(R.color.color_F6);
			}
			break;
		default:
			break;
		}
	}
}
