package com.yizhilu.huitianxinli;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.application.BaseActivity;
import com.yizhilu.utils.Address;

/**
 * @author bin
 * 修改人:
 * 时间:2016-3-26 下午4:27:03
 * 类说明:协议的类
 */
public class AgreementActivity extends BaseActivity {
	private WebView webView;  //加载协议网页的控件
	private LinearLayout back_layout;  //返回的佈局
	private TextView title_text;  //標題
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_agreement);
		super.onCreate(savedInstanceState);
	}
	@Override
	public void initView() {
		back_layout = (LinearLayout) findViewById(R.id.back_layout);  //返回的佈局
		title_text = (TextView) findViewById(R.id.title_text);  //標題
		title_text.setText(getResources().getString(R.string.user_agreement));  //設置標題
		webView = (WebView) findViewById(R.id.webView);
		webView.getSettings().setLoadWithOverviewMode(true);
		webView.setWebViewClient(new WebViewClient());
		webView.loadUrl(Address.USER_AGREEMENT);
	}

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
