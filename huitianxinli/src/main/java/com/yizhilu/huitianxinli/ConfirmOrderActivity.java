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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.Logs;

/**
 * @author bin 修改人: 时间:2015-10-21 下午7:28:09 类说明:确认订单的类
 */
public class ConfirmOrderActivity extends BaseActivity {
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private LinearLayout back_layout, alipay_layout, wxpay_layout,
			coupon_layout,coupon_cancel; // 返回的布局,支付宝支付,微信支付,优惠券布局,取消优惠券
	private TextView title_text, courseName, playNum, submitOrder, zong_price,
			reality_price, coupon_text; // 标题,课程名字,播放量,提交订单,订单总价,实付金额,优惠券
	private PublicEntity publicEntity; // 课程的实体
	private ImageLoader imageLoader; // 加载图片的对象
	private ImageView courseImage, alipay_select, wxpay_select; // 课程图片,支付宝选中，微信选中
	private int userId, courseId; // 用户Id,课程Id
	private String payType = "ALIPAY"; // 支付类型(默认值支付宝)
	private boolean isCoupon; // 是否点击了优惠券布局
	private Intent intent; // 意图对象
	private EntityPublic entityPublic; // 优惠券的实体
	private String couponCode = ""; // 优惠券编码
	private boolean alipayBoolean=false;
	private View view_pay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_confirm_order);
		// 获取传过来的数据
		getIntentMessage();
		super.onCreate(savedInstanceState);

	}

	/**
	 * @author bin 修改人: 时间:2015-10-21 下午7:45:25 方法说明:获取传过来的数据
	 */
	private void getIntentMessage() {
		Intent intent = getIntent();
		publicEntity = (PublicEntity) intent
				.getSerializableExtra("publicEntity");
		Logs.info("ConfirmOrderActivity price="+publicEntity.getEntity().getCourse().getCurrentprice());
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId",
				0);
	}

	@Override
	public void initView() {
		progressDialog = new ProgressDialog(ConfirmOrderActivity.this); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的方法
		intent = new Intent(); // 意图对象
		imageLoader = ImageLoader.getInstance(); // 实例化加载图片的对象
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回的布局
		title_text = (TextView) findViewById(R.id.title_text); // 标题
		courseImage = (ImageView) findViewById(R.id.courseImage); // 课程图片
		courseName = (TextView) findViewById(R.id.courseName); // 课程名字
		playNum = (TextView) findViewById(R.id.playNum); // 播放量
		alipay_layout = (LinearLayout) findViewById(R.id.alipay_layout); // 支付宝支付
		wxpay_layout = (LinearLayout) findViewById(R.id.wxpay_layout); // 微信支付
		alipay_select = (ImageView) findViewById(R.id.alipay_select); // 支付宝选中
		wxpay_select = (ImageView) findViewById(R.id.wxpay_select); // 微信选中
		submitOrder = (TextView) findViewById(R.id.submitOrder); // 提交订单
		zong_price = (TextView) findViewById(R.id.zong_price); // 总金额
		reality_price = (TextView) findViewById(R.id.reality_price); // 实付金额
		title_text.setText(getResources().getString(R.string.confirm_order)); // 设置标题
		coupon_layout = (LinearLayout) findViewById(R.id.coupon_layout); // 优惠券布局
		coupon_text = (TextView) findViewById(R.id.coupon_text); // 优惠券
//		coupon_cancel = (LinearLayout) findViewById(R.id.coupon_cancel);  //取消优惠券
		courseName.setText(publicEntity.getEntity().getCourse().getName());
		
		playNum.setText("播放量 : " + publicEntity.getEntity().getCourse().getPlayNum());
		zong_price.setText("￥ "
				+ publicEntity.getEntity().getCourse().getCurrentprice());
		reality_price.setText("￥ "
				+ publicEntity.getEntity().getCourse().getCurrentprice());
		imageLoader.displayImage(Address.IMAGE_NET
				+ publicEntity.getEntity().getCourse().getMobileLogo(),
				courseImage, HttpUtils.getDisPlay());
		// 获取可用优惠券的方法
		getApplyCoupon(userId, "COURSE", publicEntity.getEntity().getCourse()
				.getCurrentprice());
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
	 * @author bin 修改人: 时间:2016-1-25 下午4:39:44 方法说明:获取可用优惠券的方法
	 */
	private void getApplyCoupon(int userId, String type, float price) {
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
													ConfirmOrderActivity.this,
													"无可用购课券");
										} else {
											coupon_text.setText("无可用购课券");
										}
									} else {
										if (isCoupon) {
											intent.setClass(
													ConfirmOrderActivity.this,
													AvailableCouponActivity.class);
											intent.putExtra("entity",
													entityPublic);
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
		if (requestCode == 1 && resultCode == 1) {
			int position = data.getIntExtra("position", -1);
			if (position != -1) {
//				coupon_cancel.setVisibility(View.VISIBLE);
				EntityPublic entity = entityPublic.getEntity().get(position);
				int type = entity.getType();
				String amount = entity.getAmount();
				couponCode = entity.getCouponCode();
				String retainOne = ConstantUtils.getRetainOneDecimal(1,amount);
				float currentprice = publicEntity.getEntity().getCourse()
						.getCurrentprice();
				float amountFloat = Float.parseFloat(amount);
				if (type == 1) {
					coupon_text.setText("折扣券(" + retainOne + "折)");
					reality_price.setText("￥ " + currentprice
							* (amountFloat / 10));
				} else if (type == 2) {
					coupon_text.setText("立减(" + retainOne + "元)");
					reality_price.setText("￥ " + (currentprice - amountFloat));
				}
			}else {
				reality_price.setText("￥ "
						+ publicEntity.getEntity().getCourse().getCurrentprice());
				coupon_text.setText("未选择");
				couponCode = "";
//				coupon_cancel.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this); // 返回的布局
		submitOrder.setOnClickListener(this); // 提交订单
		alipay_layout.setOnClickListener(this); // 支付宝支付
		wxpay_layout.setOnClickListener(this); // 微信支付
		coupon_layout.setOnClickListener(this); // 优惠券布局
//		coupon_cancel.setOnClickListener(this);  //取消优惠券
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout: // 返回的布局
			this.finish();
			break;
		case R.id.submitOrder: // 提交订单
			courseId = publicEntity.getEntity().getCourse().getId();
			// 创建订单的方法
			getCreateOrder(userId, courseId, payType, couponCode);
			break;
		case R.id.alipay_layout: // 支付宝
			payType = "ALIPAY";
			alipay_select.setBackgroundResource(R.drawable.pay_selected);
			wxpay_select.setBackgroundResource(R.drawable.pay_not_selected);
			break;
		case R.id.wxpay_layout: // 微信支付
			payType = "WEIXIN";
			wxpay_select.setBackgroundResource(R.drawable.pay_selected);
			alipay_select.setBackgroundResource(R.drawable.pay_not_selected);
			break;
		case R.id.coupon_layout: // 优惠券布局
			isCoupon = true;
			// 获取可用优惠券的方法
			getApplyCoupon(userId, "COURSE", publicEntity.getEntity()
					.getCourse().getCurrentprice());
			break;
//		case R.id.coupon_cancel:  //取消优惠券
//			reality_price.setText("￥ "
//					+ publicEntity.getEntity().getCourse().getCurrentprice());
//			coupon_text.setText("未选择");
//			couponCode = "";
//			coupon_cancel.setVisibility(View.GONE);
//			break;
		default:
			break;
		}
	}

	/**
	 * @author 刘常启 修改人: 时间:2015-10-13 上午9:47:33 方法说明:连接网络获取创建订单数据
	 */
	private void getCreateOrder(int userId, int courseId, String payType,
			String couponCode) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("courseId", courseId);
		params.put("payType", payType);
		params.put("couponCode", couponCode);
		Log.i("lala", Address.CREATE_ORDER + "?" + params.toString());
		httpClient.post(Address.CREATE_ORDER, params,
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
								PublicEntity orderEntity = JSON.parseObject(
										data, PublicEntity.class);
								String message = orderEntity.getMessage();
								if (orderEntity.isSuccess()) {
									Intent intent = new Intent(
											ConfirmOrderActivity.this,
											PayActivity.class);
									intent.putExtra("publicEntity", orderEntity);
									intent.putExtra("currentPrice",
											publicEntity.getEntity()
													.getCourse()
													.getCurrentprice());
									startActivity(intent);
									ConfirmOrderActivity.this.finish();
								} else {
									ConstantUtils.showMsg(
											ConfirmOrderActivity.this, message);
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
