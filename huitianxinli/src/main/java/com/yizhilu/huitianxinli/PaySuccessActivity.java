package com.yizhilu.huitianxinli;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.application.BaseActivity;

public class PaySuccessActivity extends BaseActivity{
	private LinearLayout back_layout;  //返回
	private TextView titleTextView,promptly_pay; //标题,马上去学习
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_pay_success);
		super.onCreate(savedInstanceState);
	}
	@Override
	public void initView() {
		back_layout = (LinearLayout) findViewById(R.id.back_layout);  //返回
		titleTextView = (TextView) findViewById(R.id.title_text); //标题
		titleTextView.setText("支付成功");
		promptly_pay = (TextView) findViewById(R.id.promptly_pay);  //马上去学习
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);  //返回
		promptly_pay.setOnClickListener(this);  //马上去学习
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:  //返回
			this.finish();
			break;
		case R.id.promptly_pay:  //马上去学习
			Intent intent = new Intent(PaySuccessActivity.this,MyCourseActivity.class);
			startActivity(intent);
			this.finish();
			break;
		default:
			break;
		}
	}
}
