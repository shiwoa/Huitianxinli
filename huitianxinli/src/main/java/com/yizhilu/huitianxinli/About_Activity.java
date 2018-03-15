package com.yizhilu.huitianxinli;

import java.util.HashMap;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mob.tools.utils.UIHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.sina.weibo.SinaWeibo.ShareParams;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class About_Activity extends BaseActivity implements PlatformActionListener, Callback {
	private LinearLayout about_back; // 返回
	private TextView versionNumber; // 版本号
	private String name;
	private Button share; // 分享
	private TextView go_welcome; // 进入欢迎页
	private static final int MSG_TOAST = 1;
	private static final int MSG_ACTION_CCALLBACK = 2;
	private static final int MSG_CANCEL_NOTIFY = 3;
	private ShareDialog shareDialog; // 分享
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private ProgressDialog progressDialog; // 联网获取数据显示的dialog

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_about);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		ShareSDK.initSDK(this);
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		progressDialog = new ProgressDialog(this); // 联网获取数据显示的dialog
		about_back = (LinearLayout) findViewById(R.id.about_back); // 返回
		share = (Button) findViewById(R.id.password_preserve); // 分享
		versionNumber = (TextView) findViewById(R.id.about_version); // 版本号
		go_welcome = (TextView) findViewById(R.id.go_welcome); // 进入欢迎页
		try {
			name = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS).versionName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		versionNumber.setText("V " + name); // 获取当前版本号
		// 是否能分享
		getShare();
	}

	// 是否能分享
	private void getShare() {
		Log.i("lala", Address.WEBSITE_VERIFY_LIST);
		httpClient.get(Address.WEBSITE_VERIFY_LIST, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(data)) {
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
						boolean success = parseObject.isSuccess();
						EntityPublic entity = parseObject.getEntity();
						String verifyTranspond = entity.getVerifyTranspond();
						if (success) {
							Log.i("lala", verifyTranspond);
							if (verifyTranspond.equals("ON")) {
								share.setVisibility(View.VISIBLE);
							} else {
								share.setVisibility(View.GONE);
							}
						}
					} catch (Exception e) {
					}
				}
			}
		});
	}

	@Override
	public void addOnClick() {
		about_back.setOnClickListener(this); // 返回
		share.setOnClickListener(this); // 分享
		go_welcome.setOnClickListener(this); // 欢迎页
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.about_back: // 返回
			About_Activity.this.finish();
			break;
		case R.id.password_preserve: // 分享
			// showShare();
			if (shareDialog == null) {
				shareDialog = new ShareDialog(this, R.style.custom_dialog);
				shareDialog.setCancelable(false);
				shareDialog.show();
				shareDialog.setEntityCourse(null, false, false, true);
			} else {
				shareDialog.show();
			}
			break;
		case R.id.go_welcome: // 进入欢迎页
			DemoApplication.getInstance().exit();
			Intent intent = new Intent(About_Activity.this, GuideActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 == 0) { // qq好友
			ShareParams sp = new ShareParams();
			sp.setTitle("测试分享的标题");
			sp.setTitleUrl("http://www.baidu.com"); // 标题的超链接
			sp.setText("Text文本内容 http://www.baidu.com");
			Platform qzone = ShareSDK.getPlatform(QQ.NAME);
			qzone.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			qzone.share(sp);

		} else if (arg2 == 1) { // qq空间
			ShareParams sp = new ShareParams();
			sp.setTitle("测试分享的标题");
			sp.setTitleUrl("http://www.baidu.com"); // 标题的超链接
			sp.setText("Text文本内容 http://www.baidu.com");
			sp.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
			sp.setSite("sharesdk");
			sp.setSiteUrl("http://sharesdk.cn");
			Platform qzone = ShareSDK.getPlatform(QZone.NAME);
			qzone.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			qzone.share(sp);

		} else if (arg2 == 2) { // 腾讯微博
			ShareParams sp = new ShareParams();
			// sp.setTitle("测试分享的文本");
			sp.setText("测试分享的文本");
			// sp.setTitleUrl("http://www.baidu.com"); // 标题的超链接
			Platform weibo = ShareSDK.getPlatform(TencentWeibo.NAME);
			weibo.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			weibo.share(sp);

		} else if (arg2 == 3) { // 微信好友
			ShareParams sp = new ShareParams();
			sp.setText("测试分享的文本");
			Platform weibo = ShareSDK.getPlatform(Wechat.NAME);
			weibo.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			weibo.share(sp);

		} else if (arg2 == 4) { // 微信朋友圈
			ShareParams sp = new ShareParams();
			Platform weibo = ShareSDK.getPlatform(WechatMoments.NAME);
			sp.setText("Text文本内容 http://www.baidu.com");
			sp.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
			weibo.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			weibo.share(sp);

		} else if (arg2 == 5) { // 新浪微博
			ShareParams sp = new ShareParams();
			sp.setText("测试分享的文本");
			// sp.setImagePath("/mnt/sdcard/测试分享的图片.jpg");
			Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
			weibo.setPlatformActionListener(this); // 设置分享事件回调
			// 执行图文分享
			weibo.share(sp);
		}

	}

	@Override
	public void onCancel(Platform platform, int arg1) {
		// 取消
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 3;
		msg.arg2 = arg1;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
		Log.i("lala", "oncancel............");
	}

	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> arg2) {
		// 成功
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 1;
		msg.arg2 = action;
		msg.obj = platform;
		UIHandler.sendMessage(msg, this);
		Log.i("lala", "onComplete............");
	}

	@Override
	public void onError(Platform arg0, int action, Throwable t) {
		// 失敗
		// 打印错误信息,print the error msg
		t.printStackTrace();
		// 错误监听,handle the error msg
		Message msg = new Message();
		msg.what = MSG_ACTION_CCALLBACK;
		msg.arg1 = 2;
		msg.arg2 = action;
		msg.obj = t;
		UIHandler.sendMessage(msg, this);
		Log.i("lala", "onError............");
	}

	public boolean handleMessage(Message msg) {
		switch (msg.arg1) {
		case 1: {
			// 成功
			Toast.makeText(this, "分享成功", 10000).show();
			System.out.println("分享回调成功------------");
		}
			break;
		case 2: {
			// 失败
			Toast.makeText(this, "分享失败", 10000).show();
		}
			break;
		case 3: {
			// 取消
			Toast.makeText(this, "分享取消", 10000).show();
		}
			break;
		}

		return false;

	}

}
