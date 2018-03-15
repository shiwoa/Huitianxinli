package com.yizhilu.huitianxinli;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.ValidateUtil;

/**
 * @author bin
 * 修改人:
 * 时间:2016-1-30 上午11:28:19
 * 类说明:绑定第三方的类
 */
public class BinDingActivity extends BaseActivity {
	private LinearLayout back_layout,goto_register,errorMessage_layout;  //返回的布局,去注册,输入错误的布局
	private TextView title_text,error_message;  //标题,错误的信息
	private EditText userName_edit,passWord_edit;  //用户名,密码
	private View userName_line,passWord_line; //用户名的线,密码的线
	private TextView configBinding,forget_pass;  //确认绑定,忘记密码
	private String cusName,appId,appType,photo;  //第三方的名称,id,类型,头像
	private Intent intent;  //意图对象
	private AsyncHttpClient httpClient;  //联网获取数据的方法
	private ProgressDialog progressDialog;  //联网获取数据显示的dialog
	private HttpUtils httpUtils;  //工具类(主要用添加登陆的方法)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_binding);
		//获取传过来的信息
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-30 下午1:57:32
	 * 方法说明:获取传过来的信息
	 */
	private void getIntentMessage() {
		intent = getIntent();
		cusName = intent.getStringExtra("cusName");
		appId = intent.getStringExtra("appId");
		appType = intent.getStringExtra("appType");
		photo = intent.getStringExtra("photo");
	}
	/* 
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		httpClient = new AsyncHttpClient();  //联网获取数据的方法
		progressDialog = new ProgressDialog(BinDingActivity.this);  //联网获取数据显示的dialog
		httpUtils = new HttpUtils(BinDingActivity.this);  //工具类(主要用添加登陆的方法)
		back_layout = (LinearLayout) findViewById(R.id.back_layout);  //返回的布局
		title_text = (TextView) findViewById(R.id.title_text);  //标题
		if("QQ".equals(appType)){
			title_text.setText("QQ绑定");
		}else if("WEIXIN".equals(appType)){
			title_text.setText("微信绑定");
		}else if("SINA".equals(appType)){
			title_text.setText("微博绑定");
		}
		goto_register = (LinearLayout) findViewById(R.id.goto_register);  //去注册
		userName_edit = (EditText) findViewById(R.id.userName_edit);  //用户名
		userName_line = findViewById(R.id.userName_line);  //用户名的线
		passWord_edit = (EditText) findViewById(R.id.passWord_edit);  //密码
		passWord_line = findViewById(R.id.passWord_line);  //密码的线
		errorMessage_layout = (LinearLayout) findViewById(R.id.errorMessage_layout);  //输入错误的布局
		error_message = (TextView) findViewById(R.id.error_message);  //错误信息
		forget_pass = (TextView) findViewById(R.id.forget_pass);  //忘记密码
		configBinding = (TextView) findViewById(R.id.configBinding); //确认绑定
	}

	/* 
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);  //返回
		goto_register.setOnClickListener(this);  //去注册
		userName_edit.setOnFocusChangeListener(this); //用户名获取焦点
		passWord_edit.setOnFocusChangeListener(this);  //密码获取焦点
		forget_pass.setOnClickListener(this);  //忘记密码
		configBinding.setOnClickListener(this);  //确认绑定
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:  //返回
			this.finish();
			break;
		case R.id.goto_register:  //去注册
			//获取注册的方法
			getRegistType();
			break;
		case R.id.configBinding: //确认绑定
			errorMessage_layout.setVisibility(View.GONE);
			error_message.setText("");
			String userName = userName_edit.getText().toString();
			String passWord = passWord_edit.getText().toString();
			if (TextUtils.isEmpty(userName)) {
				errorMessage_layout.setVisibility(View.VISIBLE);
				error_message.setText("请输入用户名");
				return;
			}
			if (TextUtils.isEmpty(passWord)) {
				errorMessage_layout.setVisibility(View.VISIBLE);
				error_message.setText("请输入密码");
				return;
			}
			if (!ValidateUtil.isEmail(userName)
					&& !ValidateUtil.isMobile(userName)) {
				errorMessage_layout.setVisibility(View.VISIBLE);
				error_message.setText("请输入正确的用户名");
				return;
			}
			//确认绑定的方法
			configBinDingMethod(userName,passWord);
			break;
		case R.id.forget_pass:  //忘记密码
			intent.setClass(BinDingActivity.this, PassWordRetrieveActivity.class);
			intent.putExtra("isBinDing", true);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-3-9 下午5:24:44
	 * 方法说明:获取注册类型的接口
	 */
	private void getRegistType() {
		httpClient.get(Address.REGIST_TYPE, new TextHttpResponseHandler() {
			
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity publicEntity = JSON.parseObject(data,PublicEntity.class);
						if(publicEntity.isSuccess()){
							String keyType = publicEntity.getEntity().getKeyType();
							if("mobile".equals(keyType)){
								intent.setClass(BinDingActivity.this, RegistrActivity.class);
								intent.putExtra("isBinDing", true);
								intent.putExtra("cusName", cusName);  //第三方昵称
								intent.putExtra("appId", appId); //第三方的Id
								intent.putExtra("appType", appType);  //第三方登录的类型
								intent.putExtra("photo", photo);  //第三方的头像
								startActivity(intent);
							}else{
								intent.setClass(BinDingActivity.this, EmailRegistrActivity.class);
								intent.putExtra("isBinDing", true);
								intent.putExtra("cusName", cusName);  //第三方昵称
								intent.putExtra("appId", appId); //第三方的Id
								intent.putExtra("appType", appType);  //第三方登录的类型
								intent.putExtra("photo", photo);  //第三方的头像
								startActivity(intent);
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
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-30 下午2:18:07
	 * 方法说明:确认绑定的方法
	 * @param passWord 用户密码
	 * @param userName 用户名
	 */
	private void configBinDingMethod(final String userName, final String passWord) {
		RequestParams params = new RequestParams();
		params.put("account", userName);
		params.put("userPassword", passWord);
		params.put("cusName", cusName);
		params.put("appId", appId);
		params.put("appType", appType);
		params.put("photo", photo);
		Log.i("lala", Address.BINDINGEXISTACCOUNT+"?"+params.toString()+"_______________");
		httpClient.post(Address.BINDINGEXISTACCOUNT, params , new TextHttpResponseHandler() {
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
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						String message = publicEntity.getMessage();
						if(publicEntity.isSuccess()){
							ConstantUtils.showMsg(BinDingActivity.this, message);
							//绑定成功的方法(绑定成功就相当于登录)
//							LoginScuessMethod(publicEntity);
							getLogin(userName,passWord);
//							getSharedPreferences("userName", MODE_PRIVATE).edit()
//							 .putString("userName", userName).commit();
//							getSharedPreferences("passWord", MODE_PRIVATE).edit()
//							 .putString("passWord", passWord).commit();
						}else{
							ConstantUtils.showMsg(BinDingActivity.this, message);
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
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-30 上午10:34:09
	 * 方法说明:登录的方法
	 */
		RequestParams params = new RequestParams();
		private void getLogin(String account, String userPassword) {
		params.put("account", account);
		params.put("userPassword", userPassword);
		Log.i("lala", Address.LOGIN+"?"+params+"..........");
		httpClient.post(Address.LOGIN, params, new TextHttpResponseHandler() {
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
						PublicEntity publicEntity = JSON.parseObject(data,
								PublicEntity.class);
						String message = publicEntity.getMessage();
						if (publicEntity.isSuccess()) {
							//登陆成功的方法
							LoginScuessMethod(publicEntity);
						} else {
							ConstantUtils.showMsg(BinDingActivity.this, message);
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
	 * 时间:2016-1-30 上午10:37:24
	 * 方法说明:登陆成功的方法
	 */
	private void LoginScuessMethod(PublicEntity publicEntity) {
		int userId = publicEntity.getEntity().getId();
		//判断用户是否存在其他地方登录标记
		String memTime = publicEntity.getEntity().getMemTime();
		Log.i("lala", memTime+"-----memTime----");
		// 添加登陆记录的方法
		httpUtils.addLoginRecord(userId);
		//除了MainActivity把所有的Activity都finish掉
		DemoApplication.getInstance().SingleLoginExit(
				MainActivity.getIntence());
		DemoApplication.getInstance().finishActivity(
				MainActivity.getIntence());
		getSharedPreferences("userId", MODE_PRIVATE).edit()
				.putInt("userId", userId).commit();
		getSharedPreferences("memTime",MODE_PRIVATE).edit()
		.putString("memTime", memTime).commit();
		Intent intent = new Intent(BinDingActivity.this,
				MainActivity.class);
		startActivity(intent);
		intent.setAction("login");
		sendBroadcast(intent);
		BinDingActivity.this.finish();
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
				userName_line.setBackgroundResource(R.color.lanse);
				errorMessage_layout.setVisibility(View.GONE);
				error_message.setText("");
			} else {
				userName_line.setBackgroundResource(R.color.color_F6);
			}
			break;
		case R.id.passWord_edit: // 密码
			if (hasFocus) {
				passWord_line.setBackgroundResource(R.color.lanse);
				errorMessage_layout.setVisibility(View.GONE);
				error_message.setText("");
			} else {
				passWord_line.setBackgroundResource(R.color.color_F6);
			}
			break;
		default:
			break;
		}
	}
}
