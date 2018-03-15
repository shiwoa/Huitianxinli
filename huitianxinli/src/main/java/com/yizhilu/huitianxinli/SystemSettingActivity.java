package com.yizhilu.huitianxinli;

import java.io.File;
import java.util.List;

import org.apache.http.Header;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.StringUtil;

/**
 * @author bin 修改人: 时间:2015-10-20 上午9:49:18 类说明:系统设置的类
 */

public class SystemSettingActivity extends BaseActivity implements
		OnCheckedChangeListener {
	private LinearLayout back_layout, system_eliminate, system_opinion,
			system_about;// 返回,清除软件缓存,意见反馈,关于
	private TextView title_text, system_eliminate_tv;// 标题 缓存
	private ImageView version_point;// 头像,检测新的版本图片
	private RelativeLayout system_Testing; // 检测新的版本
	private AsyncHttpClient httpClient; // 联网获取数据的方法
	private ProgressDialog progressDialog; // 获取数据显示dialog
	private CheckBox system_wifi_img; // wifi
	private boolean isWifi, isVersion; // 判断是否是wifi,判断是否点击了检测更新
	private File files;
	long cacheSize;
	private String formetFileSize;
	private LayoutInflater inflater;
	private Dialog dialog;
	private PackageManager packageManager; // 包管理对象
	private PackageInfo packageInfo; // 包的信息
	private String android_url, android_v, packageName; // apk下载地址,返回版本号,本地版本号

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_system_setting);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		httpClient = new AsyncHttpClient(); // 联网获取数据的方法
		progressDialog = new ProgressDialog(this); // 获取数据显示dialog
		packageManager = getPackageManager();
		back_layout = (LinearLayout) findViewById(R.id.back_layout);// 返回
		title_text = (TextView) findViewById(R.id.title_text);// 标题
		system_wifi_img = (CheckBox) findViewById(R.id.system_wifi_img);// wifi
		system_wifi_img.setChecked(false);
		system_eliminate = (LinearLayout) findViewById(R.id.system_eliminate);// 清除软件缓存
		system_opinion = (LinearLayout) findViewById(R.id.system_opinion);// 意见反馈
		system_Testing = (RelativeLayout) findViewById(R.id.system_Testing);// 检测新的版本
		version_point = (ImageView) findViewById(R.id.version_point); // 小圆点
		system_about = (LinearLayout) findViewById(R.id.system_about);
		system_eliminate_tv = (TextView) findViewById(R.id.system_eliminate_text2); // 缓存
		title_text.setText(R.string.system_set); // 设置标题
		isWifi = getSharedPreferences("wifi", MODE_PRIVATE).getBoolean("wifi",
				false);
		system_wifi_img.setChecked(isWifi);
		try {
			files = this.getCacheDir();
			cacheSize = StringUtil.getFolderSize(files);
			formetFileSize = StringUtil.FormetFileSize(cacheSize);
			system_eliminate_tv.setText(formetFileSize);
		} catch (Exception e) {
		}

		// 检测版本更新的方法
		detectionVersionsUpDate();
	}

	/**
	 * @author bin 修改人: 时间:2016-1-11 下午4:47:38 方法说明:检测版本更新的方法
	 */
	private void detectionVersionsUpDate(final boolean... b) {
		try {
			packageInfo = packageManager.getPackageInfo(getPackageName(),
					PackageManager.GET_CONFIGURATIONS);
			packageName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		Log.i("lala", Address.VERSIONUPDATE);
		httpClient.get(Address.VERSIONUPDATE, new TextHttpResponseHandler() {
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
						EntityPublic entityPublic = JSON.parseObject(data,
								EntityPublic.class);
						if (entityPublic.isSuccess()) {
							List<EntityPublic> entity = entityPublic
									.getEntity();
							for (int i = 0; i < entity.size(); i++) {
								String keyType = entity.get(i).getkType();
								if (keyType.equals("android")) {
									android_url = entity.get(i)
											.getDownloadUrl();
									android_v = entity.get(i).getVersionNo();
									Log.i("lala", packageName + "本地___线上"
											+ android_v);
									if (!TextUtils.isEmpty(android_v)
											&& !packageName.equals(android_v)) {
										version_point
												.setVisibility(View.VISIBLE);
										Log.i("lala", "是否显示远点");
										if (isVersion) {
											Log.i("lala", "需要更新");
											if (!TextUtils.isEmpty(android_url)) {
												Intent intent = new Intent();
												intent.setClass(
														SystemSettingActivity.this,
														UpdateEditionActivity.class);
												intent.putExtra("name",
														android_v);
												intent.putExtra("url",
														android_url);
												startActivity(intent);
											}
										}
									} else {
										if (b[0]) {
											Toast.makeText(
													SystemSettingActivity.this,
													"暂无新版本", 0).show();
										}
									}
								}
							}
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

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);
		system_about.setOnClickListener(this);
		system_opinion.setOnClickListener(this);
		system_wifi_img.setOnCheckedChangeListener(this);
		system_eliminate.setOnClickListener(this);
		system_Testing.setOnClickListener(this); // 检测版本更新
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout: // 返回
			SystemSettingActivity.this.finish();
			break;
		case R.id.system_about: // 关于
			intent.setClass(SystemSettingActivity.this, About_Activity.class);
			startActivity(intent);
			break;
		case R.id.system_opinion:
			intent.setClass(SystemSettingActivity.this,
					OpinionFeedbackActivity.class);
			startActivity(intent);
			break;
		case R.id.system_eliminate: // 清除缓存
			if (system_eliminate_tv.getText().equals("")) {
				Toast.makeText(this, "无缓存数据", Toast.LENGTH_SHORT).show();
				return;
			} else {
				showDialog();
			}
			break;
		case R.id.system_Testing: // 检测版本更新
			isVersion = true;
			// 检测版本更新的方法
			detectionVersionsUpDate(true);
			break;
		default:
			break;
		}
	}

	public void showDialog() {
		inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.dialog_show, null);
		WindowManager manager = (WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE);
		int width = manager.getDefaultDisplay().getWidth();
		int scree = (width / 3) * 2;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.width = scree;
		view.setLayoutParams(layoutParams);
		dialog = new Dialog(this, R.style.custom_dialog);
		dialog.setContentView(view);
		dialog.show();
		LinearLayout btnsure = (LinearLayout) view
				.findViewById(R.id.dialog_linear_sure);
		TextView textView = (TextView) view.findViewById(R.id.texttitles);
		textView.setText("确定清空 ?");
		btnsure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				StringUtil.deleteFolderFile(files.getAbsolutePath(), true);
				system_eliminate_tv.setText("");
				dialog.cancel();
			}
		});
		LinearLayout btncancle = (LinearLayout) view
				.findViewById(R.id.dialog_linear_cancle);
		btncancle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			getSharedPreferences("wifi", MODE_PRIVATE).edit()
					.putBoolean("wifi", true).commit();
		} else {
			getSharedPreferences("wifi", MODE_PRIVATE).edit()
					.putBoolean("wifi", false).commit();
		}
	}
}
