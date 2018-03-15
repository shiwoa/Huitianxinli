package com.yizhilu.huitianxinli;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.yizhilu.application.BaseActivity;

/**
 * @author bin
 * 修改人:
 * 时间:2015-9-21 上午10:39:33
 * 类说明:启动页
 */
public class StartActivity extends BaseActivity {
	private boolean isFrist;  //判断是否是第一次进入程序
	private Handler handler;  //延时跳转
	private Intent intent;  //意图对象
	//延时跳转的对象
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			if(isFrist){  //是第一次进入程序,跳入到导航页
				intent.setClass(StartActivity.this, GuideActivity.class);
			}else{  //不是第一次进入程序,跳入到主页
				intent.setClass(StartActivity.this, MainActivity.class);
			}
			startActivity(intent);
			StartActivity.this.finish();
		}
	};  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_start);
		super.onCreate(savedInstanceState);
	}
	
	/**
	 *	初始化控件的方法
	 */
	@Override
	public void initView() {
		isFrist = getSharedPreferences("isFrist", MODE_PRIVATE).getBoolean("isFrist", true);
		intent = new Intent();
		handler = new Handler();
		if(isFrist){  //是第一次进入程序
			handler.postDelayed(runnable, 1000);
		}else{  //不是第一次进入程序
			handler.postDelayed(runnable, 3000);
		}
	}

	/**
	 *	添加点击事件的方法
	 */
	@Override
	public void addOnClick() {

	}

}
