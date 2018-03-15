package com.yizhilu.huitianxinli;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.application.BaseActivity;
import com.yizhilu.utils.Address;

/**
 * @author bin
 * 修改人:
 * 时间:2016-1-21 上午11:34:00
 * 类说明:播放如果是图文的类
 */
public class WebViewCoursePlay extends BaseActivity {
	private WebView webView;  //加载视图的对象
	private int userId,kpointId;  //用户Id,节点Id
	private String courseName;  //课程名字
	private LinearLayout back_layout;  //返回按钮
	private TextView title_text;  //标题
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_webview_course);
		//获取传过来的信息
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-21 上午11:37:08
	 * 方法说明:获取传过来的信息
	 */
	private void getIntentMessage() {
		Intent intent = getIntent();
		userId = intent.getIntExtra("userId", 0);
		kpointId = intent.getIntExtra("kpointId", 0);
		courseName = intent.getStringExtra("courseName");
	}
	/* 
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		back_layout = (LinearLayout) findViewById(R.id.back_layout);  //返回按钮
		title_text = (TextView) findViewById(R.id.title_text);  //标题
		webView = (WebView) findViewById(R.id.webView);  //加载视图的对象
		title_text.setText(courseName);  //设置标题
		webView.setWebViewClient(new WebViewClient());
		webView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
		});
		webView.getSettings().setJavaScriptEnabled(true);
		Log.i("lala", Address.GET_WEBVIEW_COURSE+"?userId="+userId+"&kpointId="+kpointId);
		webView.loadUrl(Address.GET_WEBVIEW_COURSE+"?userId="+userId+"&kpointId="+kpointId);
	}

	/* 
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);  //返回
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:  //返回
			this.finish();
			break;

		default:
			break;
		}
	}
}
