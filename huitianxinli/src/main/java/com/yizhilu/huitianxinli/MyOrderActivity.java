package com.yizhilu.huitianxinli;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.CourseSortAdapter;
import com.yizhilu.adapter.MyOrderAdapter;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.baoli.BLCourseDetailsActivity;
import com.yizhilu.entity.OrderEntity;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

/**
 * @author ming 修改人: 时间:2015年10月19日 上午10:51:38 类说明:我的订单
 */
public class MyOrderActivity extends BaseActivity {
	private static Activity activity;
	private PullToRefreshScrollView refreshScrollView; // 上拉加载,下拉刷新
	private NoScrollListView order_listView,statucListView; // 订单的列表,订单状态的列表
	private LinearLayout back_layout,statucLayout,selectStatucLayout,kongLayout,order_null_layout; // 返回按钮,订单状态的布局,选择订单状态,显示订单状态的空白布局,没有订单显示的布局
	private MyOrderAdapter orderAdapter;// 订单的适配器
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private List<OrderEntity> orderList; // 订单的实体
	private int currentPage = 1, userId; // 当前的页数,用户Id
	private boolean isSelect;
	private String trxStatus; // 订单的状态
	private TextView order_status;  //标题订单状态
	private ImageView order_status_image;  //订单状态的图标
	private String[] statusList = new String[] { "全部订单", "待处理订单", "待支付订单", "已完成订单",
			"已取消订单", "已退款订单" };
	private String[] orderStatus = new String[]{"","APPROVE","INIT","SUCCESS","CANCEL","REFUND"};
	private CourseSortAdapter statucAdapter;  //订单状态的适配器
	private Animation animationIn,animationOut; 
	private Intent intent;  //意图对象
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_my_order);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		currentPage = 1;
		if(orderList!=null){
			orderList.clear();
		}
		// 联网获取我的订单的方法
		getOrderList(currentPage, userId, trxStatus);
	}

	/**
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		activity = MyOrderActivity.this;
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId",
				0); // 得到用户Id
		progressDialog = new ProgressDialog(MyOrderActivity.this); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		orderList = new ArrayList<OrderEntity>(); // 订单的实体
		intent = new Intent();  //意图对象
		order_status = (TextView) findViewById(R.id.order_status); //标题订单状态
		order_status_image = (ImageView) findViewById(R.id.order_status_image);  //订单状态图标
		order_status_image.setBackgroundResource(R.drawable.btn_open);
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView);
		refreshScrollView.setMode(Mode.BOTH);  //设置加载模式
		order_listView = (NoScrollListView) findViewById(R.id.order_listView); // 订单的列表
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回按钮
		statucLayout = (LinearLayout) findViewById(R.id.statucLayout);  //订单状态的布局
		selectStatucLayout = (LinearLayout) findViewById(R.id.selectStatucLayout);  //选择订单状态的布局
		statucListView = (NoScrollListView) findViewById(R.id.statucListView);  //订单状态的列表
		kongLayout = (LinearLayout) findViewById(R.id.kongLayout);  //显示订单状态的空白布局
		order_null_layout = (LinearLayout) findViewById(R.id.order_null_layout);
		statucAdapter = new CourseSortAdapter(MyOrderActivity.this, statusList);
		statucListView.setAdapter(statucAdapter);
//		statucAdapter.setPostion(0);
//		statucAdapter.notifyDataSetChanged();
	}
	
	/**
	 * @author bin 修改人: 时间:2015-10-24 下午6:59:21 方法说明:单例模式获取本实例
	 * @return
	 */
	public static Activity getInstence() {
		if (activity == null) {
			activity = new BLCourseDetailsActivity();
		}
		return activity;
	}

	/**
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this); // 返回按钮
		refreshScrollView.setOnRefreshListener(this);
		selectStatucLayout.setOnClickListener(this);  //选择订单状态的事件
		statucListView.setOnItemClickListener(this);  //订单状态的点击事件
		kongLayout.setOnClickListener(this);  //显示订单状态的空白布局
	}

	/**
	 * @author bin 修改人: 时间:2015-10-21 下午9:17:03 方法说明:联网获取我的订单的方法
	 */
	private void getOrderList(final int currentPage, int userId,
			String trxStatus) {
		RequestParams params = new RequestParams();
		params.put("page.currentPage", currentPage);
		params.put("queryTrxorder.userId", userId);
		if (!TextUtils.isEmpty(trxStatus)) {
			params.put("queryTrxorder.trxStatus", trxStatus);
		}
		Log.i("wulala", Address.MY_ORDER_LIST + params+"----------------------------------");
		httpClient.post(Address.MY_ORDER_LIST, params,
				new TextHttpResponseHandler() {
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
								PublicEntity publicEntity = JSON.parseObject(
										data, PublicEntity.class);
								String message = publicEntity.getMessage();
								boolean success = publicEntity.isSuccess();
								if (success) {
									PageEntity page = publicEntity.getEntity()
											.getPage();
									if (page.getTotalPageSize() <= currentPage) {
										refreshScrollView
												.setMode(Mode.PULL_FROM_START);
									}
									List<OrderEntity> trxorderList = publicEntity
											.getEntity().getOrderList();
									if (trxorderList != null
											&& trxorderList.size() > 0) {
										for (int i = 0; i < trxorderList.size(); i++) {
											orderList.add(trxorderList.get(i));
										}
									}
									orderAdapter = new MyOrderAdapter(
											MyOrderActivity.this, orderList);
									order_listView.setAdapter(orderAdapter);
									if(orderList.isEmpty()){
										order_null_layout.setVisibility(View.VISIBLE);
										refreshScrollView.setMode(Mode.DISABLED);
									}else{
										order_null_layout.setVisibility(View.GONE);
									}
									refreshScrollView.onRefreshComplete();
								} else {
									ConstantUtils.showMsg(MyOrderActivity.this,
											message);
								}
							} catch (Exception e) {
								refreshScrollView.onRefreshComplete();
							}

						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(progressDialog);
						refreshScrollView.onRefreshComplete();
					}
				});
	}

	/**
	 * 控件的点击事件
	 */
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.back_layout: // 返回按钮
			this.finish();
			break;
		case R.id.selectStatucLayout:  //选择订单状态
			if(isSelect){
				//隐藏订单状态的布局
				hintLayout();
				order_status_image.setBackgroundResource(R.drawable.btn_open);
			}else{
				//显示订单状态的布局
				showLayout();
				order_status_image.setBackgroundResource(R.drawable.btn_withdrawn);
			}
			break;
		case R.id.kongLayout:  //
			//隐藏订单状态布局
			hintLayout();
			order_status_image.setBackgroundResource(R.drawable.btn_withdrawn);
			break;
		default:
			break;
		}
	}

	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-10-24 下午4:34:15
	 * 方法说明:显示订单状态的布局
	 */
	private void showLayout() {
		animationIn = AnimationUtils.loadAnimation(MyOrderActivity.this, R.anim.push_up_in);
		statucLayout.setAnimation(animationIn);
		statucLayout.setVisibility(View.VISIBLE);
		isSelect = true;
	}

	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-10-24 下午4:33:29
	 * 方法说明:隐藏订单状态的布局
	 */
	private void hintLayout() {
		animationOut = AnimationUtils.loadAnimation(MyOrderActivity.this, R.anim.slide_down_out);
		statucLayout.setAnimation(animationOut);
		statucLayout.setVisibility(View.GONE);
		isSelect = false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		super.onItemClick(parent, view, position, id);
		switch (parent.getId()) {
		case R.id.statucListView:  //订单状态
//			if(position == 0){
//				trxStatus = "";
//			}else{
//				trxStatus = statusList[position];
//			}
			trxStatus = orderStatus[position];
			order_status.setText(statusList[position]);
			currentPage = 1;
			orderList.clear();
			refreshScrollView.setMode(Mode.BOTH);
			statucAdapter.setPostion(position);
			statucAdapter.notifyDataSetChanged();
			//隐藏订单状态的布局
			hintLayout();
			order_status_image.setBackgroundResource(R.drawable.btn_withdrawn);
			// 联网获取我的订单的方法
			getOrderList(currentPage, userId, trxStatus);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 下拉刷新的方法
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		currentPage = 1;
		orderList.clear();
		refreshScrollView.setMode(Mode.BOTH);
		// 联网获取我的订单的方法
		getOrderList(currentPage, userId, trxStatus);
	}

	/**
	 * 上拉加载的方法
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullUpToRefresh(refreshView);
		currentPage++;
		// 联网获取我的订单的方法
		getOrderList(currentPage, userId, trxStatus);
	}
}
