package com.yizhilu.application;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.LoginActivity;
import com.yizhilu.huitianxinli.MainActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @author 杨财宾 时间:2015-8-29 类说明:所有类的基类
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener, OnItemClickListener,
		OnFocusChangeListener, OnRefreshListener2<ScrollView>, OnGroupClickListener, OnChildClickListener {
	private Dialog dialog;
	private String phoneImei;
	private int userId;
	private String memTime;// 标记是否同时登录
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private boolean ispause = true;
	private ConnectivityManager mConnectivityManager;
	private NetworkInfo netInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 把继承改基类的类添加到集合中
		DemoApplication.getInstance().addActivity(BaseActivity.this);
		// 初始化控件的方法
		initView();
		// 添加点击事件的方法
		addOnClick();

		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0); // 获取用户Id
		if (userId != 0) {
			new Thread(new ThreadShow()).start();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		ispause = true;
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0); // 获取用户Id
		if (dialog != null && userId != 0 && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-9-21 上午10:10:49 方法说明:每个类中初始化控件的方法
	 */
	public abstract void initView();

	/**
	 * @author bin 修改人: 时间:2015-9-21 上午10:11:10 方法说明:每个类中添加点击事件的方法
	 */
	public abstract void addOnClick();

	/**
	 * 控件的点击事件
	 */
	@Override
	public void onClick(View v) {

	}

	/**
	 * listView的item的点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}

	/*
	 * 失去焦点或获取焦点的监听
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {

	}

	/**
	 * 下拉刷新的方法
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

	}

	/**
	 * 上拉加载的方法
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

	}

	/*
	 * 组的点击事件
	 */
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
		return false;
	}

	/*
	 * 子的点击事件
	 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		return false;
	}

	/**
	 * @author bin 修改人: 时间:2016-3-30 下午4:28:27 方法说明:获取登录状态
	 */
	public boolean getLoginStatus() {
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0); // 获取用户Id
		if (userId != 0) {
			return true;
		}
		return false;
	}

	// 检查用户是否在其他地方登录账号
	public void getCheckUserIsLogin(String cookieTime, int userId) {
		final RequestParams params = new RequestParams();
		params.put("cookieTime", cookieTime);
		params.put("userId", userId);
		httpClient.post(Address.CHECK_USERISLOGIN, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				try {
					if (!TextUtils.isEmpty(data)) {
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						boolean success = publicEntity.isSuccess();
						if (!success) {
							showQuitDiaLog();
						}
					}
				} catch (Exception e) {
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
			}
		});
	}

	// handler类接收数据
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 1) {
				userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0); // 获取用户Id
				memTime = getSharedPreferences("memTime", MODE_PRIVATE).getString("memTime", ""); // 标记是否同时登录
				if (ispause) {
					if (userId != 0) {
						if (!TextUtils.isEmpty(memTime)) {
							getCheckUserIsLogin(memTime, userId);
						}
					}
				}
			}
		};
	};

	// // 线程类
	class ThreadShow implements Runnable {

		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(10000);
					Message msg = new Message();
					msg.what = 1;
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 限制登錄顯示的diaLog
	public void showQuitDiaLog() {
		if (dialog != null) {
			dialog.show();
		} else {
			View view = getLayoutInflater().inflate(R.layout.dialog_show, null);
			WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
			@SuppressWarnings("deprecation")
			int width = manager.getDefaultDisplay().getWidth();
			int scree = (width / 3) * 2;
			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			layoutParams.width = scree;
			view.setLayoutParams(layoutParams);
			dialog = new Dialog(this, R.style.custom_dialog);
			dialog.setContentView(view);
			dialog.setCancelable(false);
			dialog.show();
			TextView titles = (TextView) view.findViewById(R.id.texttitles);
			titles.setText("您的账号已在其他设备登陆。");
			TextView btnsure = (TextView) view.findViewById(R.id.dialogbtnsure);
			btnsure.setText("退出");

			LinearLayout linbtnsure = (LinearLayout) view.findViewById(R.id.dialog_linear_sure);
			linbtnsure.setOnClickListener(new OnClickListener() {

				@SuppressWarnings("static-access")
				@Override
				public void onClick(View v) {
					DemoApplication.getInstance().SingleLoginExit(MainActivity.getIntence());
					getSharedPreferences("userId", MODE_PRIVATE).edit().putInt("userId", 0).commit();
					Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
					intent.putExtra("isSingle", true);
					startActivity(intent);
					dialog.dismiss();
				}
			});
			TextView btncancle = (TextView) view.findViewById(R.id.dialogbtncancle);
			btncancle.setText("重新登录");
			btncancle.setTextColor(getResources().getColor(R.color.Blue));
			LinearLayout linbtncancle = (LinearLayout) view.findViewById(R.id.dialog_linear_cancle);
			linbtncancle.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					DemoApplication.getInstance().SingleLoginExit(MainActivity.getIntence());
					getSharedPreferences("userId", MODE_PRIVATE).edit().putInt("userId", 0).commit();
					Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
					intent.putExtra("isSingle", true);
					startActivity(intent);
					dialog.dismiss();
				}
			});
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		ispause = false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
