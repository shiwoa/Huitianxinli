package com.yizhilu.huitianxinli;

import android.R.integer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.yizhilu.adapter.AvailableCouponAdapter;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.view.NoScrollListView;

/**
 * @author bin
 * 修改人:
 * 时间:2016-1-27 上午9:46:30
 * 类说明:可用优惠券的类
 */
public class AvailableCouponActivity extends BaseActivity {
	private PullToRefreshScrollView refreshScrollView;  //刷新和加載的對象
	private NoScrollListView listView;  //可用優惠券的的列表
	private EntityPublic entityPublic;  //优惠券的实体
	private LinearLayout back_layout;  //返回的布局
	private TextView title_text;  //标题
	private LinearLayout top_layout;  //标题的布局
	private int posotionSelect; // 当前选中position
	private AvailableCouponAdapter adapter;
	private TextView collection_image_edit; // 取消选中
	private updateIsShow isShow; //接收显示取消选中的广播
	private boolean isSelect;
	private int position = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.copy_of_fragment_available_coupon);
		//获取传过来的信息
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-27 上午9:52:52
	 * 方法说明:获取传过来的信息
	 */
	private void getIntentMessage() {
		Intent intent = getIntent();
		entityPublic = (EntityPublic) intent.getSerializableExtra("entity");
	}
	@Override
	protected void onResume() {
		super.onResume();
		if (isShow == null) {
			isShow = new updateIsShow();
			IntentFilter filter = new IntentFilter();
			filter.addAction("inform");
			registerReceiver(isShow, filter);
		}
	}
	/* 
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
//		top_layout = (LinearLayout) findViewById(R.id.top_layout);  //标题的布局
//		top_layout.setVisibility(View.VISIBLE);  //显示
		back_layout = (LinearLayout) findViewById(R.id.back_layout);  //返回的布局
		title_text = (TextView) findViewById(R.id.collection_text);  //标题
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView);  //刷新的對象
		refreshScrollView.setMode(Mode.DISABLED);  //設置加載模式
		listView = (NoScrollListView) findViewById(R.id.listView);  //可用優惠券的列表
		collection_image_edit = (TextView) findViewById(R.id.collection_image_edit); // 取消选中
		title_text.setText(getResources().getString(R.string.discount_coupon));  //设置标题
		adapter = new AvailableCouponAdapter(AvailableCouponActivity.this, entityPublic.getEntity());
		listView.setAdapter(adapter);
		getSharedPreferences("selectPosition",MODE_PRIVATE).edit().putInt("selectPosition", -1).commit();
		boolean isposition = getSharedPreferences("isbackPosition", MODE_PRIVATE).getBoolean("isbackPosition", false);
		if (isposition) {
			int position  = getSharedPreferences("backPosition", MODE_PRIVATE).getInt("backPosition", 0);
			adapter.changeSelected(position);
			adapter.notifyDataSetChanged();
		}
	}

	/* 
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);  //返回
		listView.setOnItemClickListener(this);  //优惠券的点击事件
		collection_image_edit.setOnClickListener(this); // 取消选中
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:  //返回
			position = getSharedPreferences("selectPosition", MODE_PRIVATE).getInt("selectPosition", -1);
			Intent intent = new Intent();
			intent.putExtra("position", position);
			setResult(1, intent);
			this.finish();
			break;
		case R.id.collection_image_edit://点击取消选中
			getSharedPreferences("selectPosition",MODE_PRIVATE).edit().putInt("selectPosition", -1).commit();
			adapter = new AvailableCouponAdapter(AvailableCouponActivity.this, entityPublic.getEntity());
			listView.setAdapter(adapter);
			isSelect = false;
			break;
		default:
			break;
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		isSelect = true;
		posotionSelect = position;
		adapter.changeSelected(posotionSelect);
		adapter.notifyDataSetChanged();
//		Intent intent = new Intent();
//		intent.putExtra("position", position);
//		setResult(1, intent);
//		this.finish();
	}
	
//	接收显示通知的广播
	class updateIsShow extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			String action = arg1.getAction();
			if (action.equals("inform")) {
				collection_image_edit.setVisibility(View.VISIBLE);
			}
		}
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isShow != null) {
			AvailableCouponActivity.this.unregisterReceiver(isShow);
		}
		if (isSelect) {
			getSharedPreferences("backPosition",MODE_PRIVATE)
			.edit().putInt("backPosition", posotionSelect).commit();
			getSharedPreferences("isbackPosition",MODE_PRIVATE)
			.edit().putBoolean("isbackPosition", true).commit();
		}else {
			if (position == -1) {
				getSharedPreferences("isbackPosition",MODE_PRIVATE)
				.edit().putBoolean("isbackPosition", false).commit();
			}
		}
	}
	
}
