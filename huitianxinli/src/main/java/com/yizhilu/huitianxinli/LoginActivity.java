package com.yizhilu.huitianxinli;

import java.util.HashMap;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.CacheUtils;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.PhoneUtils;
import com.yizhilu.utils.ValidateUtil;

/**
 * @author bin 修改人: 时间:2015-10-19 上午11:04:10 类说明:登录的类
 */
public class LoginActivity extends BaseActivity implements PlatformActionListener {
	private LinearLayout back_layout, goto_register, errorMessage_layout; // 返回的布局,去注册,错误信息的布局
	private TextView title_text, loginText, error_message, forget_pass; // 本類的標題,注册的按钮,错误信息,忘记密码
	private EditText userName_edit, passWord_edit; // 用户名,设置密码
	private View userName_line, passWord_line;
	private String userName, passWord; // 用户名,密码(在注册界面传过来的)
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private Intent intent; // 意图对象
	private ImageView QQImage, sinaImage, weixinImage; // qq,新浪,微信
	private String thridType = ""; // 第三方登录的类型
	private boolean isSingle; // 是否是单点登录退出后跳到这个页面的
	private PhoneUtils phoneUtils; // 手机的工具类
	private Platform QQfd, Sinafd, Wechatfd; // 第三方授权对象(QQ,新浪,微信)
	public static LoginActivity loginActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_login);
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}

	/**
	 * @author bin 修改人: 时间:2015-10-20 上午10:40:30 方法说明:获取传过来的信息
	 */
	private void getIntentMessage() {
		Intent intent = getIntent();
		userName = intent.getStringExtra("userName");
		passWord = intent.getStringExtra("passWord");
		isSingle = intent.getBooleanExtra("isSingle", false);
	}

	public static LoginActivity getInstence() {
		if (loginActivity == null) {
			loginActivity = new LoginActivity();
		}
		return loginActivity;
	}

	/**
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		loginActivity = this;
		progressDialog = new ProgressDialog(LoginActivity.this); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		intent = new Intent(); // 意图对象
		phoneUtils = new PhoneUtils(LoginActivity.this); // 手机的工具类
		ShareSDK.initSDK(LoginActivity.this);
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回的布局
		title_text = (TextView) findViewById(R.id.title_text); // 本類的標題
		goto_register = (LinearLayout) findViewById(R.id.goto_register); // 去注册
		userName_edit = (EditText) findViewById(R.id.userName_edit); // 用户名
		if (!CacheUtils.getSharedPreference(LoginActivity.this, "lastname").equals("")) {
			userName_edit.setText(CacheUtils.getSharedPreference(LoginActivity.this, "lastname"));
			Editable etext = userName_edit.getText();
			Selection.setSelection(etext, etext.length());
		}

		passWord_edit = (EditText) findViewById(R.id.passWord_edit); // 设置密码
		userName_line = findViewById(R.id.userName_line);
		passWord_line = findViewById(R.id.passWord_line);
		errorMessage_layout = (LinearLayout) findViewById(R.id.errorMessage_layout); // 错误信息的布局
		forget_pass = (TextView) findViewById(R.id.forget_pass); // 忘记密码
		error_message = (TextView) findViewById(R.id.error_message); // 错误信息
		loginText = (TextView) findViewById(R.id.loginText); // 注册的按钮
		title_text.setText(getResources().getString(R.string.login)); // 设置标题
		if (!TextUtils.isEmpty(userName)) {
			userName_edit.setText(userName);
		}
		if (!TextUtils.isEmpty(passWord)) {
			passWord_edit.setText(passWord);
		}
		QQImage = (ImageView) findViewById(R.id.QQImage); // QQ
		sinaImage = (ImageView) findViewById(R.id.sinaImage); // 新浪
		weixinImage = (ImageView) findViewById(R.id.weixinImage); // 微信
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this); // 返回的布局
		goto_register.setOnClickListener(this); // 去注册
		loginText.setOnClickListener(this); // 注册的按钮
		forget_pass.setOnClickListener(this); // 忘记密码
		userName_edit.setOnFocusChangeListener(this); // 失去或获取焦点的监听
		passWord_edit.setOnFocusChangeListener(this); // 失去或获取焦点的监听
		QQImage.setOnClickListener(this); // QQ
		sinaImage.setOnClickListener(this); // 新浪
		weixinImage.setOnClickListener(this); // 微信
	}

	/**
	 * 控件的点击事件
	 */
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout: // 返回的布局
			if (isSingle) {
				DemoApplication.getInstance().finishActivity(MainActivity.getIntence());
				Intent intent = new Intent(LoginActivity.this, MainActivity.class);
				startActivity(intent);
				this.finish();
			} else {
				this.finish();
			}
			break;
		case R.id.goto_register: // 去注册
			// 获取注册类型的接口
			getRegistType();
			break;
		case R.id.loginText: // 登录的按钮
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
			if (!ValidateUtil.isEmail(userName) && !ValidateUtil.isMobile(userName)) {
				errorMessage_layout.setVisibility(View.VISIBLE);
				error_message.setText("请输入正确的用户名");
				return;
			}
			// 联网登录的方法
			getLogin(userName, passWord);
			break;
		case R.id.forget_pass: // 忘记密码
			intent.setClass(LoginActivity.this, PassWordRetrieveActivity.class);
			startActivity(intent);
			break;
		case R.id.QQImage: // QQ
			thridType = "QQ";
			QQfd = ShareSDK.getPlatform(QQ.NAME);
			QQfd.setPlatformActionListener(this);
			QQfd.authorize();
			// QQfd.showUser(null);
			break;
		case R.id.sinaImage: // 新浪
			thridType = "SINA";
			Sinafd = ShareSDK.getPlatform(SinaWeibo.NAME);
			Sinafd.setPlatformActionListener(this);
			Sinafd.SSOSetting(true);
			// Sinafd.showUser(null);
			Sinafd.authorize();
			break;
		case R.id.weixinImage: // 微信
			thridType = "WEIXIN";
			Wechatfd = ShareSDK.getPlatform(Wechat.NAME);
			Wechatfd.setPlatformActionListener(this);
			// Wechatfd.showUser(null);
			Wechatfd.authorize();
			break;
		default:
			break;
		}
	}

	/**
	 * @author bin 修改人: 时间:2016-3-9 下午5:24:44 方法说明:获取注册类型的接口
	 */
	private void getRegistType() {
		Log.i("lala", Address.REGIST_TYPE);
		httpClient.get(Address.REGIST_TYPE, new TextHttpResponseHandler() {
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
						if (publicEntity.isSuccess()) {
							String keyType = publicEntity.getEntity().getKeyType();

							if ("mobile".equals(keyType)) {
								intent.setClass(LoginActivity.this, RegistrActivity.class);
								startActivity(intent);
								LoginActivity.this.finish();
							} else if ("mobileAndEmail".equals(keyType)) {
								intent.setClass(LoginActivity.this, RegistrPhoneEmailActivity.class);
								startActivity(intent);
								LoginActivity.this.finish();
							} else {
								intent.setClass(LoginActivity.this, EmailRegistrActivity.class);
								startActivity(intent);
								LoginActivity.this.finish();
							}
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
	 * @author bin 修改人: 时间:2016-1-30 上午10:34:09 方法说明:登录的方法
	 */
	private void getLogin(final String account, String userPassword) {
		RequestParams params = new RequestParams();
		params.put("account", account);
		params.put("userPassword", userPassword);
		Log.i("lala", Address.LOGIN + "?" + params + "..........");
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
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						String message = publicEntity.getMessage();
						if (publicEntity.isSuccess()) {
							// 登陆成功的方法
							LoginScuessMethod(publicEntity);
							CacheUtils.setSharedPreference(LoginActivity.this, "lastname", account);
						} else {
							ConstantUtils.showMsg(LoginActivity.this, message);
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				Log.i("lala", "....登录失败");
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	/**
	 * @author bin 修改人: 时间:2016-1-30 上午10:37:24 方法说明:登陆成功的方法
	 */
	private void LoginScuessMethod(PublicEntity publicEntity) {
		int userId = publicEntity.getEntity().getUser().getId();
		// 判断用户是否存在其他地方登录标记
		String memTime = publicEntity.getEntity().getMemTime();
		String lastLoginTime = publicEntity.getEntity().getLastLoginTime();
		// 添加登陆记录的方法
		addLoginRecord(userId);
		// 除了MainActivity把所有的Activity都finish掉
		DemoApplication.getInstance().SingleLoginExit(MainActivity.getIntence());
		DemoApplication.getInstance().finishActivity(MainActivity.getIntence());

		getSharedPreferences("userId", MODE_PRIVATE).edit().putInt("userId", userId).commit();
		getSharedPreferences("memTime", MODE_PRIVATE).edit().putString("memTime", memTime).commit();
		getSharedPreferences("lastLoginTime", MODE_APPEND).edit().putString("lastLoginTime", lastLoginTime).commit();

		Intent intent = new Intent(LoginActivity.this, MainActivity.class);
		startActivity(intent);
		intent.setAction("login");
		sendBroadcast(intent);
		LoginActivity.this.finish();
	}

	/**
	 * @author bin 修改人: 时间:2016-1-25 下午1:43:41 方法说明:添加登陆记录的方法
	 */
	private void addLoginRecord(int userId) {
		RequestParams params = new RequestParams();
		params.put("websiteLogin.ip", PhoneUtils.GetHostIp());
		params.put("websiteLogin.brand", phoneUtils.getPhoneBrand());
		params.put("websiteLogin.modelNumber", phoneUtils.getPhoneModel());
		params.put("websiteLogin.size", phoneUtils.getPhoneSize());
		params.put("websiteLogin.userId", userId);
		params.put("websiteLogin.type", "android");

		Log.i("xiangyao", Address.ADD_LOGIN_RECORD + "?" + params.toString());

		httpClient.post(Address.ADD_LOGIN_RECORD, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

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
		case R.id.passWord_edit: // 密码
			if (hasFocus) {
				// passWord_line.setBackgroundResource(R.color.lanse);
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

	/*
	 * 第三方登陆取消登陆的方法
	 */
	@Override
	public void onCancel(Platform arg0, int arg1) {
		ConstantUtils.showMsg(LoginActivity.this, "您取消了登录");
	}

	/*
	 * 登陆第三方成功的回调
	 */
	@Override
	public void onComplete(Platform platform, int arg1, HashMap<String, Object> res) {
		// 获取资料
		final String userName = platform.getDb().getUserName();// 获取用户名字
		final String userIcon = platform.getDb().getUserIcon(); // 获取用户头像
		final String userId = platform.getDb().getUserId();
		String userGender = platform.getDb().getUserGender();
		final String unionid = platform.getDb().get("unionid");
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// 验证是否绑定第三方登录的方法
				thirdPartyLogin(userId, unionid, thridType, userName, userIcon);
			}
		});
	}

	/*
	 * 第三方登录出现错误时的回调
	 */
	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		ConstantUtils.showMsg(LoginActivity.this, "登录失败");
	}

	/**
	 * @author bin 修改人: 时间:2016-1-30 下午1:45:35 方法说明:验证第三方登录是否绑定账号的方法
	 */
	private void thirdPartyLogin(final String appId, final String unionid, final String type, final String name,
			final String userIcon) {
		RequestParams params = new RequestParams();
		if (type.equals("WEIXIN")) {
			params.put("appId", unionid);
			params.put("appType", type);
			params.put("cusName", name);
			Log.i("lala", "if" + params.toString());
		} else {
			params.put("appId", appId);
			params.put("appType", type);
			params.put("cusName", name);
			Log.i("lala", "eles" + params.toString());
		}
		Log.i("lala", Address.THIRDPARTYLOGIN_URL + "?" + params.toString());
		httpClient.post(Address.THIRDPARTYLOGIN_URL, params, new TextHttpResponseHandler() {
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
						JSONObject parseObject = JSON.parseObject(data);
						String message = parseObject.getString("message");
						Boolean success = parseObject.getBoolean("success");
						if (success) {
							PublicEntity publicEntity = JSONObject.parseObject(data, PublicEntity.class);
							// 登陆成功的方法
							LoginScuessMethod(publicEntity);
							// String userName =
							// getSharedPreferences("userName",
							// MODE_PRIVATE).getString("userName", "");
							// String passWord =
							// getSharedPreferences("passWord",
							// MODE_PRIVATE).getString("passWord", "");
							// getLogin(userName, passWord);
						} else {
							String string = parseObject.getString("entity");
							if ("NOT_USER".equals(string)) { // 没有绑定用户,跳到绑定界面
								intent.setClass(LoginActivity.this, BinDingActivity.class);
								intent.putExtra("cusName", name); // 第三方昵称
								intent.putExtra("appId", appId); // 第三方的Id
								intent.putExtra("appType", type); // 第三方登录的类型
								intent.putExtra("photo", userIcon); // 第三方的
								startActivity(intent);
							} else {
								ConstantUtils.showMsg(LoginActivity.this, message);
							}
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

	/*
	 * 返回键的监听(主要用于单点登录退出后跳到这个页面)
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (isSingle) {
			DemoApplication.getInstance().finishActivity(MainActivity.getIntence());
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
		} else {
			this.finish();
		}
	}
}
