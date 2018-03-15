package com.yizhilu.huitianxinli;

import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.CourseOrderAdapter;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

/**
 * @author bin 修改人: 时间:2015-10-21 下午7:28:09 类说明:确认订单的类
 */
public class PaymentConfirmOrderActivity extends BaseActivity {
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private NoScrollListView payment_course_listView; // 支付课程的列表
	private LinearLayout back_layout, alipay_layout, wxpay_layout,
			coupon_layout; // 返回的布局,支付宝支付,微信支付,优惠券布局
	private TextView title_text, submitOrder, zong_price, reality_price,
			coupon_text; // 标题,提交订单,订单总价,实付金额,优惠券说明
	private ImageView alipay_select, wxpay_select; // 课程图片,支付宝选中，微信选中
	private int userId,orderId; // 用户Id,订单Id
	private String payType = "ALIPAY"; // 支付类型(默认值支付宝)
	private EntityPublic entityPublic,trxorder; // 优惠券的实体,订单实体
	private Intent intent; // 意图对象
	private String couponCode = "";  //优惠券编码
	private boolean isCoupon; // 是否点击了优惠券布局
	private boolean alipayBoolean=false;
	private View view_pay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_payment_confirm_order);
		// 获取传过来的数据
		getIntentMessage();
		super.onCreate(savedInstanceState);

	}

	/**
	 * @author bin 修改人: 时间:2015-10-21 下午7:45:25 方法说明:获取传过来的数据
	 */
	private void getIntentMessage() {
		Intent intent = getIntent();
		orderId = intent.getIntExtra("orderId", 0); // 获取订单Id
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
	}

	@Override
	public void initView() {
		progressDialog = new ProgressDialog(PaymentConfirmOrderActivity.this); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的方法
		intent = new Intent(); // 意图对象
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回的布局
		payment_course_listView = (NoScrollListView) findViewById(R.id.payment_course_listView); // 支付课程的列表
		title_text = (TextView) findViewById(R.id.title_text); // 标题
		alipay_layout = (LinearLayout) findViewById(R.id.alipay_layout); // 支付宝支付
		wxpay_layout = (LinearLayout) findViewById(R.id.wxpay_layout); // 微信支付
		alipay_select = (ImageView) findViewById(R.id.alipay_select); // 支付宝选中
		wxpay_select = (ImageView) findViewById(R.id.wxpay_select); // 微信选中
		submitOrder = (TextView) findViewById(R.id.submitOrder); // 提交订单
		zong_price = (TextView) findViewById(R.id.zong_price); // 总金额
		reality_price = (TextView) findViewById(R.id.reality_price); // 实付金额
		coupon_layout = (LinearLayout) findViewById(R.id.coupon_layout); // 优惠券的布局
		coupon_text = (TextView) findViewById(R.id.coupon_text); // 优惠券类型
		title_text.setText(getResources().getString(R.string.confirm_order)); // 设置标题
		// 获取订单的数据
		getOrderData(orderId);
		view_pay=findViewById(R.id.view_pay);
		//是否可以使用微信或支付宝支付
		getShare();
	}
	// 是否能分享
	private void getShare() {
		Log.i("lala", Address.WEBSITE_VERIFY_LIST);
		httpClient.get(Address.WEBSITE_VERIFY_LIST,
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
								PublicEntity parseObject = JSON.parseObject(
										data, PublicEntity.class);
								boolean success = parseObject.isSuccess();
								if (success) {
									EntityPublic entity = parseObject.getEntity();
									String verifyAlipay = entity.getVerifyAlipay();
									String verifywx = entity.getVerifywx();
									if(verifyAlipay.equals("ON")){
										alipay_layout.setVisibility(View.VISIBLE);
									 }else{
										alipayBoolean=true;
										view_pay.setVisibility(View.GONE);
										alipay_layout.setVisibility(View.GONE);
									}
									if(verifywx.equals("ON")){
										if(alipayBoolean){
											payType = "WEIXIN";
											wxpay_select.setBackgroundResource(R.drawable.pay_selected);
											alipay_select.setBackgroundResource(R.drawable.pay_not_selected);	
										}
										wxpay_layout.setVisibility(View.VISIBLE);
									 }else{
										wxpay_layout.setVisibility(View.GONE);
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
	
	/**
	 * @author bin 修改人: 时间:2016-1-27 下午8:08:28 方法说明:获取订单的数据
	 */
	private void getOrderData(int orderId) {
		RequestParams params = new RequestParams();
		params.put("orderId", orderId);
		Log.i("wulala", Address.ORDER_NO_PAYMENT + "?" + params.toString()+"__________");
		httpClient.post(Address.ORDER_NO_PAYMENT, params,
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
								if (publicEntity.isSuccess()) {
									trxorder = publicEntity
											.getEntity().getTrxorder();
									payment_course_listView
											.setAdapter(new CourseOrderAdapter(
													PaymentConfirmOrderActivity.this,
													publicEntity));
									if(data.contains("couponCodeDTO")){
										EntityPublic codeDTO = publicEntity.getEntity().getCouponCodeDTO();
										couponCode = codeDTO.getCouponCode();  //优惠券编码
										String amount = codeDTO.getAmount();
										int type = codeDTO.getType();
										String retainOne = ConstantUtils.getRetainOneDecimal(1,amount);
										if(type == 1){
											coupon_text.setText("折扣券("+retainOne+"折)");
										}else if(type == 2){
											coupon_text.setText("立减("+retainOne+"元)");
										}else if(type == 3){
											
										}
									}else{
										coupon_text.setText("无可用购课券");
									}
									zong_price.setText("￥ "
											+ trxorder.getOrderAmount());  //订单总金额
									reality_price.setText("￥ "
											+ trxorder.getAmount());  //实付订单金额
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
		back_layout.setOnClickListener(this); // 返回的布局
		submitOrder.setOnClickListener(this); // 确认订单
		alipay_layout.setOnClickListener(this); // 支付宝支付
		wxpay_layout.setOnClickListener(this); // 微信支付
		coupon_layout.setOnClickListener(this); // 优惠券的布局
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:  //返回的布局
			this.finish();
			break;
		case R.id.submitOrder:  //确认订单订单
			orderId = trxorder.getId();  //获取订单Id
			//修改订单数据（用于重新支付前）接口
			getAgainPayVerificationOrder(orderId,couponCode);
			break;
		case R.id.alipay_layout:  //支付宝
			payType = "ALIPAY";
			alipay_select.setBackgroundResource(R.drawable.pay_selected);
			wxpay_select.setBackgroundResource(R.drawable.pay_not_selected);
			break;
		case R.id.wxpay_layout:  //微信支付
			payType = "WEIXIN";
			wxpay_select.setBackgroundResource(R.drawable.pay_selected);
			alipay_select.setBackgroundResource(R.drawable.pay_not_selected);
			break;
		case R.id.coupon_layout:  //优惠券的布局
			isCoupon = true;
			getApplyCoupon(userId, "COURSE", trxorder.getAmount());
			break;
		case R.id.coupon_cancel:  //取消优惠券
			reality_price.setText("￥ "
					+ trxorder.getAmount());
			coupon_text.setText("未选择");
			couponCode = "";
			break;
		default:
			break;
		}
	}

	/**
	 * @author bin 修改人: 时间:2016-1-25 下午4:39:44 方法说明:获取可用优惠券的方法
	 */
	private void getApplyCoupon(int userId, String type, String price) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("type", type);
		params.put("orderAmount", price);
		Log.i("lala", Address.GET_COUPON_LIST + "?" + params.toString());
		httpClient.post(Address.GET_COUPON_LIST, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						if (!TextUtils.isEmpty(data)) {
							try {
								entityPublic = JSON.parseObject(data,
										EntityPublic.class);
								String message = entityPublic.getMessage();
								if (entityPublic.isSuccess()) {
									List<EntityPublic> couponList = entityPublic
											.getEntity();
									if (couponList.isEmpty()) {
										if (isCoupon) {
											ConstantUtils.showMsg(
													PaymentConfirmOrderActivity.this,
													"无可用购课券");
										}else {
											coupon_text.setText("无可用优惠券");
										}
									} else {
										if (isCoupon) {
											intent.setClass(
													PaymentConfirmOrderActivity.this,
													AvailableCouponActivity.class);
											intent.putExtra("entity", entityPublic);
											startActivityForResult(intent, 1);
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

					}
				});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1&&resultCode == 1){
			int position = data.getIntExtra("position", -1);
			if(position!=-1){
				EntityPublic entity = entityPublic.getEntity().get(position);
				int type = entity.getType();
				String amount = entity.getAmount();
				couponCode = entity.getCouponCode();
				String orderAmount = trxorder.getOrderAmount();
				String retainOne = ConstantUtils.getRetainOneDecimal(1,amount);
				float orderFloat = Float.parseFloat(orderAmount);
				float amoutFloat = Float.parseFloat(amount);
				if(type == 1){
					coupon_text.setText("折扣券("+retainOne+"折)");
					reality_price.setText("￥ "+ orderFloat*(amoutFloat/10)); 
				}else if(type == 2){
					coupon_text.setText("立减("+retainOne+"元)");
					reality_price.setText("￥ "+ (orderFloat-amoutFloat)); 
				}
			}else {
				reality_price.setText("￥ "
						+ trxorder.getAmount());
				coupon_text.setText("未选择");
				couponCode = "";
//				coupon_cancel.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * @author bin 修改人: 时间:2016-1-26 下午4:18:17 方法说明:重新支付检验订单的接口
	 */
	private void getAgainPayVerificationOrder(int orderId,String couponCode) {
		RequestParams params = new RequestParams();
		params.put("orderId", orderId);
		params.put("couponcode", couponCode);
		Log.i("wulala", Address.AGAINPAYVERIFICATIONORDER+"?"+params.toString()+"***************");
		httpClient.post(Address.AGAINPAYVERIFICATIONORDER,params ,new TextHttpResponseHandler() {
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
								if (publicEntity.isSuccess()) {
									Intent intent = new Intent(
											PaymentConfirmOrderActivity.this,
											PayActivity.class);
									intent.putExtra("isPayment", true); // 待支付
									intent.putExtra("publicEntity",
											publicEntity);
									intent.putExtra("payType", payType);
									startActivity(intent);
									PaymentConfirmOrderActivity.this.finish();
								} else {
									ConstantUtils.showMsg(
											PaymentConfirmOrderActivity.this,
											message);
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
	protected void onDestroy() {
		super.onDestroy();
		getSharedPreferences("isbackPosition",MODE_PRIVATE)
		.edit().putBoolean("isbackPosition", false).commit();
	}
}
