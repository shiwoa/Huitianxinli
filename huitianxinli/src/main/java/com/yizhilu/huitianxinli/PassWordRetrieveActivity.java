package com.yizhilu.huitianxinli;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.ValidateUtil;

/**
 * @author bin 修改人: 时间:2015-12-2 下午8:24:42 类说明:找回密码的类
 */
public class PassWordRetrieveActivity extends BaseActivity {
	private ProgressDialog progressDialog; // 联网获取数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private LinearLayout back_layout,goto_login; // 返回的布局,显示密码,去登录
	private EditText regist_mobile_edit, verification_code_edit,
			regist_pass_edit,confirm_passWord_edit;// 输入手机号,输入验证码,输入密码,确认密码
	private TextView title_text, get_obtain_code, config_modify; // 标题,获取验证码,确认修改
	private Intent intent; // 意图对象
	private boolean isCountdown; // 是否是发送验证码
	private View userName_line,code_line,passWord_line,confirm_passWord_line;  //用户名线,验证码线,密码线,确认密码线
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_password_retrieve);
		super.onCreate(savedInstanceState);
	}

	/**
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		progressDialog = new ProgressDialog(PassWordRetrieveActivity.this); // 实例化dialog对象
		httpClient = new AsyncHttpClient(); // 实例化联网获取数据的对象
		intent = new Intent(); // 意图对象
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回的布局
		title_text = (TextView) findViewById(R.id.title_text); // 标题
		title_text.setText(R.string.pass_retrieve); // 设置标题
		goto_login = (LinearLayout) findViewById(R.id.goto_login); // 去登录
		regist_mobile_edit = (EditText) findViewById(R.id.userName_edit); // 输入手机号
		userName_line = findViewById(R.id.userName_line); //用户名线
		verification_code_edit = (EditText) findViewById(R.id.verification_code_edit); // 输入验证码
		code_line = findViewById(R.id.code_line);  //验证码线
		regist_pass_edit = (EditText) findViewById(R.id.passWord_edit); // 输入密码
		confirm_passWord_edit = (EditText) findViewById(R.id.confirm_passWord_edit);  //确认密码
		passWord_line = findViewById(R.id.passWord_line); //密码线
		confirm_passWord_line = findViewById(R.id.confirm_passWord_line);  //确认密码线
		get_obtain_code = (TextView) findViewById(R.id.get_obtain_code); // 获取验证码
		config_modify = (TextView) findViewById(R.id.config_modify); // 确认注册
	}

	/**
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this); // 返回
		goto_login.setOnClickListener(this); // 去登录
		get_obtain_code.setOnClickListener(this); // 获取验证码
		config_modify.setOnClickListener(this); // 确认注册
		regist_mobile_edit.setOnFocusChangeListener(this);
		verification_code_edit.setOnFocusChangeListener(this);
		regist_pass_edit.setOnFocusChangeListener(this);
		confirm_passWord_edit.setOnFocusChangeListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		String mobile = regist_mobile_edit.getText().toString();
		switch (v.getId()) {
		case R.id.back_layout: // 返回
			this.finish();
			break;
		case R.id.goto_login: // 去登录
			Intent intent = new Intent(PassWordRetrieveActivity.this,
					LoginActivity.class);
			startActivity(intent);
			this.finish();
			break;
		case R.id.get_obtain_code: // 获取验证码
			if (!isCountdown) {
				if (TextUtils.isEmpty(mobile)) {
					ConstantUtils.showMsg(PassWordRetrieveActivity.this,
							"请输入手机号/邮箱");
					return;
				}
				if (!ValidateUtil.isMobile(mobile)&&!ValidateUtil.isEmail(mobile)) {
					ConstantUtils.showMsg(PassWordRetrieveActivity.this,
							"请输入正确的手机号/邮箱");
					return;
				}else if(ValidateUtil.isMobile(mobile)){
					//手机找回密码,获取sgin值
					getSginData(mobile);
				}else if(ValidateUtil.isEmail(mobile)){
					//邮箱找回密码
					getEmailsCode(mobile);
				}
			}
			break;
		case R.id.config_modify: // 确认修改
			String code = verification_code_edit.getText().toString();
			String passWord = regist_pass_edit.getText().toString();
			String confirmPass = confirm_passWord_edit.getText().toString();
			if (TextUtils.isEmpty(mobile)) {
				ConstantUtils.showMsg(PassWordRetrieveActivity.this, "请输入手机号/邮箱");
				return;
			}
			if (TextUtils.isEmpty(code)) {
				ConstantUtils.showMsg(PassWordRetrieveActivity.this, "请输入验证码");
				return;
			}
			if (TextUtils.isEmpty(passWord)) {
				ConstantUtils.showMsg(PassWordRetrieveActivity.this, "请输入密码");
				return;
			}
			if(TextUtils.isEmpty(confirmPass)){
				ConstantUtils.showMsg(PassWordRetrieveActivity.this, "请输入确认密码");
				return;
			}
			if (!ValidateUtil.isMobile(mobile)&&!ValidateUtil.isEmail(mobile)) {
				ConstantUtils.showMsg(PassWordRetrieveActivity.this, "请输入正确的手机号/邮箱");
				return;
			}
			if (!(passWord.length() >= 6 && passWord.length() <= 18)) {
				ConstantUtils.showMsg(PassWordRetrieveActivity.this, "密码长度为6-18位");
				return;
			}
			if (!(passWord.length() >= 6 && passWord.length() <= 18)||!ValidateUtil.isNumberOrLetter(passWord)) {
				ConstantUtils.showMsg(PassWordRetrieveActivity.this, "请输入正确的密码格式");
				return;
			}
			if (!confirmPass.equals(passWord)) {
				ConstantUtils.showMsg(PassWordRetrieveActivity.this, "两次密码不对应");
				return;
			}
			if(ValidateUtil.isMobile(mobile)){
				//手机号找回密码的方法
				getPassWord(mobile,code, passWord,confirmPass,true);
			}else if(ValidateUtil.isEmail(mobile)){
				//邮箱找回密码的方法
				getPassWord(mobile,code, passWord,confirmPass,false);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-12 下午4:07:12
	 * 方法说明:获取邮箱的验证码
	 */
	private void getEmailsCode(String mobile) {
		RequestParams params = new RequestParams();
		params.put("sendType", "retrieve");
		params.put("email", mobile);
		httpClient.post(Address.GET_EMAIL_CODE, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						JSONObject object = JSON.parseObject(data);
						String message = object.getString("message");
						if (object.getBoolean("success")) {
							ConstantUtils.showMsg(PassWordRetrieveActivity.this,
									message);
							isCountdown = true;
							//开启线程
							startTheard();
						}else{
							ConstantUtils.showMsg(PassWordRetrieveActivity.this, message);
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
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-12 下午3:46:13
	 * 方法说明:获取sgin值的方法
	 */
	private void getSginData(final String mobile) {
		RequestParams params = new RequestParams();
		params.put("mobileType", "Android");
		params.put("mobile", mobile);
		Log.i("lala", Address.GET_SGIN+"?"+params.toString());
		httpClient.post(Address.GET_SGIN, params , new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						JSONObject object = JSON.parseObject(data);
						String message = object.getString("message");
						if(object.getBoolean("success")){
							String sgin = object.getString("entity");
							// 联网获取验证码的方法
							getVerificationCode(mobile,sgin);
						}else{
							ConstantUtils.showMsg(PassWordRetrieveActivity.this, message);
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
	 * @author bin 修改人: 时间:2015-12-4 上午11:42:18 方法说明:获取验证码的接口
	 */
	private void getVerificationCode(String mobile,String sgin) {
		RequestParams params = new RequestParams();
		params.put("sendType", "retrieve");
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
									ConstantUtils.showMsg(PassWordRetrieveActivity.this,
											message);
									isCountdown = true;
									//开启线程
									startTheard();
								}else{
									ConstantUtils.showMsg(PassWordRetrieveActivity.this, message);
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
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-12 下午4:14:39
	 * 方法说明:开启倒计时的线程
	 */
	private void startTheard() {
		new CountDownTimer(60000, 1000) {

			@Override
			public void onTick(
					long millisUntilFinished) {
				get_obtain_code.setText("重新获取"
						+ millisUntilFinished
						/ 1000 + "秒");
			}

			@Override
			public void onFinish() {
				get_obtain_code.setText("获取验证码");
				isCountdown = false;
			}
		}.start();
	}
	
	/**
	 * @author bin 修改人: 时间:2015-12-4 上午11:58:37 方法说明:找回密码的方法
	 */
	private void getPassWord(final String mobile, String code,
			final String userPassword,String confirmPass,boolean isMobile) {
		final RequestParams params = new RequestParams();
		if(isMobile){
			params.put("retrieveType", "mobile");
		}else{
			params.put("retrieveType", "email");
		}
		params.put("mobileOrEmail", mobile);
		params.put("code", code);
		params.put("newPwd", userPassword);
		params.put("confirmPwd", confirmPass);
		httpClient.post(Address.GET_PASSWORD, params,
				new TextHttpResponseHandler() {

					@Override
					public void onStart() {
						super.onStart();
						HttpUtils.showProgressDialog(progressDialog);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						Log.i("lala", Address.REGISTER + params.toString());
						HttpUtils.exitProgressDialog(progressDialog);
						if (!TextUtils.isEmpty(data)) {
							try {
								JSONObject object = JSON.parseObject(data);
								String message = object.getString("message");
								if (object.getBoolean("success")) {
									DemoApplication.getInstance().finishActivity(LoginActivity.getInstence());
									intent.setClass(PassWordRetrieveActivity.this,
											LoginActivity.class);
									intent.putExtra("userName", mobile);
									intent.putExtra("passWord", userPassword);
									startActivity(intent);
									PassWordRetrieveActivity.this.finish();
								} else {
									ConstantUtils.showMsg(PassWordRetrieveActivity.this,
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
		case R.id.userName_edit: //用户名
			if(hasFocus){
//				userName_line.setBackgroundResource(R.color.lanse);
			}else{
				userName_line.setBackgroundResource(R.color.color_F6);
			}
			break;
		case R.id.verification_code_edit:  //验证码
			if(hasFocus){
//				code_line.setBackgroundResource(R.color.lanse);
			}else{
				code_line.setBackgroundResource(R.color.color_F6);
			}
			break;
		case R.id.passWord_edit:  //密码
			if(hasFocus){
//				passWord_line.setBackgroundResource(R.color.lanse);
			}else{
				passWord_line.setBackgroundResource(R.color.color_F6);
			}
			break;
		case R.id.confirm_passWord_edit:  //确认密码
			if(hasFocus){
//				confirm_passWord_line.setBackgroundResource(R.color.lanse);
			}else{
				confirm_passWord_line.setBackgroundResource(R.color.color_F6);
			}
			break;
		default:
			break;
		}
	}
}
