package com.yizhilu.huitianxinli;

import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.alibaba.fastjson.JSON;
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

/**
 * @author bin
 * 修改人:
 * 时间:2016-2-1 上午10:06:56
 * 类说明:解除第三方登录的类
 */
public class RemoveBindingActivity extends BaseActivity implements PlatformActionListener{
	private LinearLayout back_layout;  //返回的布局
	private TextView title_text,QQText,sinaText,weixinText,QQBinding,sinaBinding,weixinBinding;  //标题,QQ,新浪,微信
	private AsyncHttpClient httpClient;  //联网获取数据的方法
	private ProgressDialog progressDialog;  //联网获取数据显示的dialog
	private int userId;  //用户Id
	private EntityPublic QQEntity,sinaEntity,weiXinEntity;  //实体类
	private Intent intent;  //意图对象
	private EntityPublic userExpandDto;  //个人信息的实体
	private String thridType,binding;  //第三方的类型
	private Platform platform;  //第三方授权对象
	private LinearLayout qqLayout,sinLayout,weiLayout;
	private ImageView QQImage,sinaImage,weixinImage;
	private Dialog dialog;
	private EntityPublic entity;
	private RelativeLayout qq_relat,sina_relat,weixin_relat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_remove_binding);
		//获取传过来的信息
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-2-1 上午11:44:34
	 * 方法说明:获取传过来的信息
	 */
	private void getIntentMessage() {
		intent = getIntent();
		userExpandDto = (EntityPublic) intent.getSerializableExtra("EntityPublic");
		userId = userExpandDto.getId();
	}
	@Override
	public void initView() {
		httpClient = new AsyncHttpClient();  //联网获取数据的方法
		progressDialog = new ProgressDialog(RemoveBindingActivity.this);  //获取数据显示的dialog
		ShareSDK.initSDK(RemoveBindingActivity.this);
		back_layout = (LinearLayout) findViewById(R.id.back_layout);  //返回的布局
		title_text = (TextView) findViewById(R.id.title_text);  //标题
		title_text.setText(R.string.remove_binding); //设置标题
		QQText = (TextView) findViewById(R.id.QQText);  //QQ
		sinaText = (TextView) findViewById(R.id.sinaText);  //新浪
		weixinText = (TextView) findViewById(R.id.weixinText);  //微信\
		QQBinding = (TextView) findViewById(R.id.QQBinding);
		sinaBinding = (TextView) findViewById(R.id.sinaBinding);
		weixinBinding = (TextView) findViewById(R.id.weixinBinding);
		qqLayout = (LinearLayout) findViewById(R.id.QQBinding_linear);
		sinLayout = (LinearLayout) findViewById(R.id.sinaBinding_linear);
		weiLayout = (LinearLayout) findViewById(R.id.weixinText_linear);
		weixinImage = (ImageView) findViewById(R.id.weixinImage);
		QQImage = (ImageView) findViewById(R.id.QQImage);
		sinaImage = (ImageView) findViewById(R.id.sinaImage);
		qq_relat = (RelativeLayout) findViewById(R.id.qq_relat);
		sina_relat = (RelativeLayout) findViewById(R.id.sina_relat);
		weixin_relat = (RelativeLayout) findViewById(R.id.weixin_relat);
	}
	
	@Override
	protected void onResume() {	
		super.onResume();
		//获取与第三方绑定的方法
		getQueryUserBundling(userId);
	}

	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-2-1 上午11:37:00
	 * 方法说明:获取与第三方绑定的方法
	 */
	private void getQueryUserBundling(int userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		Log.i("lala", Address.QUERYUSERBUNDLING+"?"+params.toString());
		httpClient.post(Address.QUERYUSERBUNDLING, params , new TextHttpResponseHandler() {

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
						EntityPublic entityPublic = JSON.parseObject(data, EntityPublic.class);
						String message = entityPublic.getMessage();
						if(entityPublic.isSuccess()){
							List<EntityPublic> entityList = entityPublic.getEntity();
							if(!entityList.isEmpty()){
								for(int i=0;i<entityList.size();i++){
									entity = entityList.get(i);
									String profiletype = entity.getProfiletype();
									if("QQ".equals(profiletype)){
										QQEntity = entity;
										QQText.setText(QQEntity.getName());
//										QQText.setTextColor(getResources().getColor(R.color.Blue));
										QQImage.setBackgroundResource(R.drawable.qqimageyes);
										QQBinding.setText(R.string.relieve_binding);
										QQBinding.setTextColor(getResources().getColor(R.color.color_80));
//										QQBinding.setBackgroundColor(getResources().getColor(R.color.color_99a1a7));
										
										continue;
									}else{
//										QQText.setText("QQ");
//										QQText.setTextColor(getResources().getColor(R.color.Black));
										QQBinding.setText(R.string.immediately_binding);
//										QQBinding.setBackgroundColor(getResources().getColor(R.color.Blue));
										QQBinding.setTextColor(getResources().getColor(R.color.color_83));
									}
									if("WEIXIN".equals(profiletype)){
										weiXinEntity = entity;
										weixinText.setText(weiXinEntity.getName());
//										weixinText.setTextColor(getResources().getColor(R.color.Blue));
										weixinImage.setBackgroundResource(R.drawable.weixinimageyes);
										weixinBinding.setText(R.string.relieve_binding);
										weixinBinding.setTextColor(getResources().getColor(R.color.color_80));
//										weixinBinding.setBackgroundColor(getResources().getColor(R.color.color_99a1a7));
										continue;
									}else {
//										weixinText.setText("微信");
//										weixinText.setTextColor(getResources().getColor(R.color.Black));
										weixinBinding.setText(R.string.immediately_binding);
										weixinBinding.setTextColor(getResources().getColor(R.color.color_83));
//										weixinBinding.setBackgroundColor(getResources().getColor(R.color.Blue));
									}
									if("SINA".equals(profiletype)){
										sinaEntity = entity;
										sinaText.setText(sinaEntity.getName());
//										sinaText.setTextColor(getResources().getColor(R.color.Blue));
										sinaImage.setBackgroundResource(R.drawable.sinaimageyes);
										sinaBinding.setText(R.string.relieve_binding);
										sinaBinding.setTextColor(getResources().getColor(R.color.color_80));
//										sinaBinding.setBackgroundColor(getResources().getColor(R.color.color_99a1a7));
										continue;
									}else{
//										sinaText.setText("新浪");
//										sinaText.setTextColor(getResources().getColor(R.color.Black));
										sinaBinding.setText(R.string.immediately_binding);
										sinaBinding.setTextColor(getResources().getColor(R.color.color_83));
//										sinaBinding.setBackgroundColor(getResources().getColor(R.color.Blue));
									}
								}
							}else{
								//设置第三方信息
								setThridMessage();	
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
	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);  //返回的布局
		QQBinding.setOnClickListener(this);
		weixinBinding.setOnClickListener(this);
		sinaBinding.setOnClickListener(this);
		qqLayout.setOnClickListener(this);
		weiLayout.setOnClickListener(this);
		sinLayout.setOnClickListener(this);
		qq_relat.setOnClickListener(this);
		weixin_relat.setOnClickListener(this);
		sina_relat.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:  //返回
			this.finish();
			break;
		case R.id.qq_relat: 
			thridType = "QQ";
			binding = QQBinding.getText().toString();
			if(binding.equals(getResources().getString(R.string.relieve_binding))){
				//去解除绑定
				showQuitDiaLog();
				//设置第三方信息
//				setThridMessage();
			}else{
				//没有绑定,去绑定
				platform = ShareSDK.getPlatform(QQ.NAME);
				platform.setPlatformActionListener(this);
				platform.authorize();
			}
			break;
		case R.id.weixin_relat:
			thridType = "WEIXIN";
			binding = weixinBinding.getText().toString();
			if(binding.equals(getResources().getString(R.string.relieve_binding))){
				//去解除绑定
				showQuitDiaLog();
				//设置第三方信息
//				setThridMessage();
			}else{
				//没有绑定,去绑定
				platform = ShareSDK.getPlatform(Wechat.NAME);
				platform.setPlatformActionListener(this);
//				platform.showUser(null);
				platform.authorize();
			}
			break;
		case R.id.sina_relat:
			thridType = "SINA";
			binding = sinaBinding.getText().toString();
			if(binding.equals(getResources().getString(R.string.relieve_binding))){
				//去解除绑定
				showQuitDiaLog();
				//设置第三方信息
//				setThridMessage();
			}else{
				//没有绑定,去绑定
				platform = ShareSDK.getPlatform(SinaWeibo.NAME);
				platform.setPlatformActionListener(this);
//				platform.showUser(null);
				platform.authorize();
			}
			break;
		default:
			break;
		}
	}
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-2-1 下午5:23:51
	 * 方法说明:设置第三方显示的信息
	 */
	public void setThridMessage(){
//		QQText.setText("QQ");
//		QQText.setTextColor(getResources().getColor(R.color.Black));
		QQBinding.setText(R.string.immediately_binding);
		QQBinding.setTextColor(getResources().getColor(R.color.color_83));
		QQImage.setBackgroundResource(R.drawable.qqimagenot);
//		QQBinding.setBackgroundColor(getResources().getColor(R.color.Blue));
//		weixinText.setText("微信");
//		weixinText.setTextColor(getResources().getColor(R.color.Black));
		weixinBinding.setText(R.string.immediately_binding);
		weixinBinding.setTextColor(getResources().getColor(R.color.color_83));
		weixinImage.setBackgroundResource(R.drawable.weixinimagenot);
//		weixinBinding.setBackgroundColor(getResources().getColor(R.color.Blue));
//		sinaText.setText("新浪");
//		sinaText.setTextColor(getResources().getColor(R.color.Black));
		sinaBinding.setText(R.string.immediately_binding);
		sinaBinding.setTextColor(getResources().getColor(R.color.color_83));
		sinaImage.setBackgroundResource(R.drawable.sinaimagenot);
//		sinaBinding.setBackgroundColor(getResources().getColor(R.color.Blue));
	}
	
	/* 
	 * 第三方登陆取消登陆的方法
	 */
	@Override
	public void onCancel(Platform arg0, int arg1) {
		ConstantUtils.showMsg(RemoveBindingActivity.this, "您取消了登录");
	}

	/* 
	 * 登陆第三方成功的回调
	 */
	@Override
	public void onComplete(Platform platform, int arg1,
			HashMap<String, Object> res) {
		// 获取资料
		final String userName = platform.getDb().getUserName();// 获取用户名字
		final String userIcon = platform.getDb().getUserIcon(); // 获取用户头像
		final String appId = platform.getDb().getUserId();
		String userGender = platform.getDb().getUserGender();
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// 验证是否绑定第三方登录的方法
				addBundling(userId, userName,appId,thridType,userIcon);
			}

		});
	}

	/* 
	 * 第三方登录出现错误时的回调
	 */
	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		ConstantUtils.showMsg(RemoveBindingActivity.this, "登录失败");
	}
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-2-1 下午5:58:33
	 * 方法说明:绑定第三方的方法
	 */
	private void addBundling(final int userId, String userName, String appId,
			String thridType, String userIcon) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("cusName", userName);
		params.put("appId", appId);
		params.put("appType", thridType);
		params.put("photo", userIcon);
		httpClient.post(Address.ADDBUNDLING, params , new TextHttpResponseHandler() {
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
							ConstantUtils.showMsg(RemoveBindingActivity.this, message);
							//获取与第三方绑定的方法
							getQueryUserBundling(userId);
						}else{
							ConstantUtils.showMsg(RemoveBindingActivity.this, message);
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
	
	public void showQuitDiaLog() {
		if(dialog != null){
			dialog.show();
		}else{
			View view = getLayoutInflater().inflate(R.layout.dialog_show, null);
			WindowManager manager = (WindowManager) getSystemService(
					Context.WINDOW_SERVICE);
			@SuppressWarnings("deprecation")
			int width = manager.getDefaultDisplay().getWidth();
			int scree = (width / 3) * 2;
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.width = scree;
			view.setLayoutParams(layoutParams);
			dialog = new Dialog(this, R.style.custom_dialog);
			dialog.setContentView(view);
			dialog.setCancelable(false);
			dialog.show();
			TextView titles = (TextView) view.findViewById(R.id.texttitles);
			if("QQ".equals(thridType)){
				titles.setText("解除绑定后将不在使用"+thridType+"登录会天心理\n教育平台软件是否继续？");
			}else if("WEIXIN".equals(thridType)){
				titles.setText("解除绑定后将不在使用微信登录会天心理\n教育平台软件是否继续？");
			}else if("SINA".equals(thridType)){
				titles.setText("解除绑定后将不在使用新浪微博登录会天心理\n教育平台软件是否继续？");
			}
//			titles.setText("您的账号已在其他设备登陆。");
			TextView btnsure = (TextView) view.findViewById(R.id.dialogbtnsure);
			btnsure.setText("是");
			
			LinearLayout linbtnsure = (LinearLayout) view.findViewById(R.id.dialog_linear_sure);
			linbtnsure.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("static-access")
				@Override
				public void onClick(View v) {
					if (thridType.equals("QQ")) {
						intent.setClass(RemoveBindingActivity.this, RemoveBindingConfigActivity.class);
						intent.putExtra("EntityPublic", userExpandDto);
						intent.putExtra("entity", QQEntity);
						startActivity(intent);
					}else if (thridType.equals("WEIXIN")) {
						intent.setClass(RemoveBindingActivity.this, RemoveBindingConfigActivity.class);
						intent.putExtra("EntityPublic", userExpandDto);
						intent.putExtra("entity", weiXinEntity);
						startActivity(intent);
					}else if (thridType.equals("SINA")) {
						intent.setClass(RemoveBindingActivity.this, RemoveBindingConfigActivity.class);
						intent.putExtra("EntityPublic", userExpandDto);
						intent.putExtra("entity", sinaEntity);
						startActivity(intent);
					}
					dialog.dismiss();
				}
			});
			TextView btncancle = (TextView) view.findViewById(R.id.dialogbtncancle);
			btncancle.setText("否");
//			btncancle.setTextColor(getResources().getColor(R.color.Blue));
			LinearLayout linbtncancle = (LinearLayout) view.findViewById(R.id.dialog_linear_cancle);
			linbtncancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
		}
	}
}
