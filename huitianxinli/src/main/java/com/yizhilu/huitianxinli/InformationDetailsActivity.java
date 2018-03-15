package com.yizhilu.huitianxinli;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.ShareSDK;

/**
 * @author bin 修改人: 时间:2015-10-16 下午2:19:52 类说明:资讯详情的类
 */
public class InformationDetailsActivity extends BaseActivity {
	private WebView webView; // 咨询详情的布局
	private int informationId; // 咨询的Id
	private String informationTitle; // 咨询的title
	private TextView titleText; //
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的方法
	private ImageView back; // 返回
	private LinearLayout share, back_layout; // 分享,返回
	private EntityCourse entityCourse; // 资讯的实体
	private ShareDialog shareDialog; // 分享的对话框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_information_details);
		// 获取传过来的信息
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}

	/**
	 * 获取传过来的信息
	 */
	private void getIntentMessage() {
		Intent intent = getIntent();
		// informationId = intent.getIntExtra("informationId", 0);
		informationTitle = intent.getStringExtra("informationTitle");
		entityCourse = (EntityCourse) intent.getSerializableExtra("entity");
		informationId = entityCourse.getId(); // 资讯的Id
	}

	@Override
	public void initView() {
		progressDialog = new ProgressDialog(InformationDetailsActivity.this); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的方法
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回
		share = (LinearLayout) findViewById(R.id.information_share); // 分享
		titleText = (TextView) findViewById(R.id.titleText);
		titleText.setText(informationTitle);
		webView = (WebView) findViewById(R.id.webView); // 咨询详情的布局
		webView.setWebViewClient(new WebViewClient());
		back = (ImageView) findViewById(R.id.details_back); // 返回
		webView.loadUrl(Address.INFORMATION_DETAILS + informationId);
		// 是否能分享
		getShare();
		// 联网获取咨询详情的方法
		// getInformationDetails(informationId);
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

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	/**
	 * @author ming 修改人: 时间:2015年10月13日 上午10:15:24 类说明:获取资讯详情的方法
	 */
	private void getInformationDetails(int id) {
		httpClient.get(Address.INFORMATION_DETAILS + id, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.showProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(data)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);

					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.showProgressDialog(progressDialog);
			}
		});
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this); // 返回
		back.setOnClickListener(this);
		share.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout: // 返回
			finish();
			break;
		case R.id.details_back: // 返回
			finish();
			break;
		case R.id.information_share: // 分享
			if (entityCourse == null) {
				return;
			}
			if (shareDialog == null) {
				shareDialog = new ShareDialog(this, R.style.custom_dialog);
				shareDialog.setCancelable(false);
				shareDialog.show();
				shareDialog.setEntityCourse(entityCourse, false, true, false);
			} else {
				shareDialog.show();
			}
			// showShare();
			break;
		default:
			break;
		}
	}

}
