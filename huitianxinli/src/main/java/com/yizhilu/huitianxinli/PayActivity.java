package com.yizhilu.huitianxinli;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alipay.sdk.app.PayTask;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.projectapp.alipay.PayResult;
import com.projectapp.alipay.SignUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.baoli.BLCourseDetailsActivity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.utils.Logs;
import com.yizhilu.wechatpayment.MD5;
import com.yizhilu.wechatpayment.Util;

/**
 * @author bin 修改人: 时间:2015-10-21 下午8:11:05 类说明:立即支付的类
 */
public class PayActivity extends BaseActivity {
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private LinearLayout back_layout; // 返回的布局
	private TextView title_text, promptly_pay, order_number, course_price, accout_money, need_pay; // 标题,立即支付按钮,订单号,价格,账户余额,还需支付
	private PublicEntity publicEntity; // 订单的实体
	// private OrderEntity orderEntity;
	private int orderId, userId; // 订单的Id,用户的Id
	private float currentPrice; // 订单总价
	private boolean isPayment; // 是从待支付订单跳过来的
	private String amount, bodyOrderNo; // 价格,传入支付宝参数的订单号
	private PublicEntity payEntity, payMessageEntity, weixinEntity; // 支付前验证的实体,支付宝信息的实体,微信信息的实体
	private String payType; // 支付类型
	private static final int SDK_PAY_FLAG = 1;
	private String APP_ID = "", MCH_ID = "", API_KEY = ""; // 微信支付的参数
	private MyBrodCast brodCast; // 广播的对象(用于微信支付成功)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_pay);
		// 获取传过来的信息
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}

	/**
	 * @author bin 修改人: 时间:2015-10-21 下午8:35:06 方法说明:获取传过来的信息
	 */
	private void getIntentMessage() {
		Intent intent = getIntent();
		isPayment = intent.getBooleanExtra("isPayment", false); // 判断是点立即购买还是点我的订单里的待支付跳过来的(true为:点待支付)
		publicEntity = (PublicEntity) intent.getSerializableExtra("publicEntity");
		if (isPayment) { // 点待支付进来的
			// orderEntity = (OrderEntity) intent
			// .getSerializableExtra("orderEntity");
			payType = intent.getStringExtra("payType"); // 待支付跳过来传的支付类型
		} else { // 点立即购买进来的
			currentPrice = intent.getFloatExtra("currentPrice", 0);
		}
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
	}

	@Override
	public void initView() {
		progressDialog = new ProgressDialog(PayActivity.this); // 加载数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的方法
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回的布局
		title_text = (TextView) findViewById(R.id.title_text); // 标题
		order_number = (TextView) findViewById(R.id.order_number); // 订单号
		course_price = (TextView) findViewById(R.id.course_price); // 价格
		promptly_pay = (TextView) findViewById(R.id.promptly_pay); // 立即支付按钮
		accout_money = (TextView) findViewById(R.id.accout_money); // 账户余额
		need_pay = (TextView) findViewById(R.id.need_pay); // 还需支付
		title_text.setText(getResources().getString(R.string.promptly_pay)); // 设置标题
		if (publicEntity.getEntity().getBankAmount() == null) {
			amount = "0.00"; // 待支付金额
		} else {
			amount = publicEntity.getEntity().getBankAmount();
		}
		if (isPayment) {
			order_number.setText(publicEntity.getEntity().getRequestId()); // 设置订单号
			course_price.setText("￥" + publicEntity.getEntity().getAmount()); // 订单总价
			String balance = publicEntity.getEntity().getBalance();
			accout_money.setText("￥" + publicEntity.getEntity().getBalance()); // 账户余额
			need_pay.setText("￥" + amount); // 还需支付
		} else {
			order_number.setText(publicEntity.getEntity().getOrderNo()); // 设置订单号
			amount = publicEntity.getEntity().getBankAmount();
			course_price.setText("￥" + currentPrice); // 订单总价格
			String balance = publicEntity.getEntity().getBalance();
			accout_money.setText("￥" + publicEntity.getEntity().getBalance()); // 账户余额
			need_pay.setText("￥" + amount); // 还需支付的价格
		}

		// 微信支付
		msgApi = WXAPIFactory.createWXAPI(this, null);
		sb = new StringBuffer();
		req = new PayReq();
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this); // 返回的布局
		promptly_pay.setOnClickListener(this); // 立即支付的事件
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout: // 返回的布局
			this.finish();
			break;
		case R.id.promptly_pay: // 立即支付
			orderId = publicEntity.getEntity().getOrderId(); // 获取订单Id
			if (!isPayment) {
				payType = publicEntity.getEntity().getPayType();
			}
			// 支付前验证接口
			getPaymentDetection(userId, orderId, payType);
			break;
		default:
			break;
		}
	}

	/**
	 * @author 刘常启 修改人: 时间:2015-10-13 上午9:55:46 方法说明:连接网络获取支付前检测数据
	 */
	private void getPaymentDetection(int userId, int orderId, final String payType) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("orderId", orderId);
		params.put("payType", payType);
		Log.i("lala", Address.PAYMENT_DETECTION + "?" + params.toString());
		httpClient.post(Address.PAYMENT_DETECTION, params, new TextHttpResponseHandler() {
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
						payEntity = JSON.parseObject(data, PublicEntity.class);
						String message = payEntity.getMessage();
						if (payEntity.isSuccess()) { // 代表已支付过
							ConstantUtils.showMsg(PayActivity.this, message);

							Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
							startActivity(intent);
							DemoApplication.getInstance().finishActivity(BLCourseDetailsActivity.getInstence());
							DemoApplication.getInstance().finishActivity(ComboDetailsActivity.getInstence());
							PayActivity.this.finish();
						} else {
							if ("用户账户余额不足！".equals(message)) {

								if ("ALIPAY".equals(payType)) {
									// 调用支付宝信息
									getAlipayInfo();
								} else if ("WEIXIN".equals(payType)) {
									// 调用微信信息
									getWeiXinInfo();
								}
							} else {
								ConstantUtils.showMsg(PayActivity.this, message);
							}
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	/**
	 * @author 刘常启 修改人: 时间:2015-10-13 上午10:50:43 方法说明:连接网络获取支付宝信息的方法
	 */
	private void getAlipayInfo() {
		httpClient.get(Address.ALIPAY_INFO, new TextHttpResponseHandler() {

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
						payMessageEntity = JSON.parseObject(data, PublicEntity.class);
						if (payMessageEntity.isSuccess()) {
							// 去支付
							if ("ALIPAY".equals(payType)) { // 支付宝
								// 调用支付的方法
								alipay();
							} else if ("WEIXIN".equals(payType)) { // 微信

							}
						} else {
							ConstantUtils.showMsg(PayActivity.this, "系统繁忙,请稍后在试！");
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

	// ----------------一下為支付寶支付所調用的方法以及所需要的信息----------------------------------------
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultInfo = payResult.getResult();
				Log.i("lala", resultInfo + "wo....");
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					String outTradeNo = resultInfo.split("out_trade_no=\"")[1].split("\"&subject")[0];
					String totalFee = resultInfo.split("total_fee=\"")[1].split("\"&notify_url")[0];
					Log.i("lala", outTradeNo + "..." + totalFee);
					// 向服务器提交订单信息
					getPaysuccessCall(userId, totalFee, outTradeNo, payType, payEntity.getEntity().getOrderNo());
				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(PayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(PayActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

					}
				}
				break;
			}
			default:
				break;
			}
		}

	};

	/**
	 * @author 刘常启 修改人: 时间:2015-10-13 上午10:09:40 方法说明:连接网络获取支付成功回调的数据
	 */
	private void getPaysuccessCall(int userId, String totalFee, String outTradeNo, String payType, String orderNo) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("totalFee", totalFee);
		params.put("outTradeNo", outTradeNo);
		params.put("payType", payType);
		params.put("orderNo", orderNo);
		Logs.info("连接网络获取支付成功回调的数据:"+Address.PAYSUCCESS_CALL+params);
		httpClient.post(Address.PAYSUCCESS_CALL, params, new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(data)) {
					try {
						PublicEntity parseObject = JSON.parseObject(data, PublicEntity.class);
						String message = parseObject.getMessage();
						ConstantUtils.showMsg(PayActivity.this, message);
						if (parseObject.isSuccess()) { // 支付成功跳到我购买的课程的界面
							Intent intent = new Intent(PayActivity.this, PaySuccessActivity.class);
							DemoApplication.getInstance().finishActivity(BLCourseDetailsActivity.getInstence());
							PayActivity.this.finish();
						} else {
							PayActivity.this.finish();
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				// TODO Auto-generated method stub
				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	/**
	 * @author 杨财宾 修改人: 时间:2015-9-9 下午4:49:30 方法说明:支付宝支付的方法
	 */
	private void alipay() {
		// 订单
		String orderInfo = getOrderInfo(payEntity.getEntity().getOut_trade_no(), payEntity.getEntity().getOrderNo(),
				amount);
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(PayActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo);
				Logs.info("调用支付接口，获取支付的结果:"+result);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * @author 杨财宾 修改人: 时间:2015-9-9 下午4:50:43 方法说明:创建订单信息
	 */
	public String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + payMessageEntity.getEntity().getAlipaypartnerID() + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + payMessageEntity.getEntity().getSellerEmail() + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + Address.HOST_PAY + "mobile/order/alipay/back/1" + "\"";
		// http://notify.msp.hk/notify.htm
		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * @author 杨财宾 修改人: 时间:2015-9-9 下午4:52:58 方法说明:生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 */
	public String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String key = format.format(date);

		Random r = new Random();
		key = key + r.nextInt();
		key = key.substring(0, 15);
		return key;
	}

	/**
	 * @author 杨财宾 修改人: 时间:2015-9-9 下午4:55:29 方法说明:对订单信息进行签名
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, payMessageEntity.getEntity().getPrivatekey());
	}

	/**
	 * @author 杨财宾 修改人: 时间:2015-9-9 下午5:00:10 方法说明:获取签名方式
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

	// --------------------------------微信支付所需要的信息---------------------------
	private IWXAPI msgApi; // 微信
	private StringBuffer sb;
	private Map<String, String> resultunifiedorder;
	private PayReq req;

	/**
	 * @author bin 修改人: 时间:2016-1-11 上午9:56:49 方法说明:获取微信信息的方法
	 */
	private void getWeiXinInfo() {
		Log.i("lala", Address.WEIXIN_INFO);
		httpClient.get(Address.WEIXIN_INFO, new TextHttpResponseHandler() {

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
						weixinEntity = JSON.parseObject(data, PublicEntity.class);
						if (weixinEntity.isSuccess()) {
							APP_ID = weixinEntity.getEntity().getMobileAppId();
							MCH_ID = weixinEntity.getEntity().getMobileMchId();
							API_KEY = weixinEntity.getEntity().getMobilePayKey();
							Log.i("lala", APP_ID + ".." + MCH_ID + ".." + API_KEY);
							// 微信支付生成预支付订单
							GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
							getPrepayId.execute();
						} else {
							ConstantUtils.showMsg(PayActivity.this, "系统繁忙,请稍后在试！");
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

	private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(PayActivity.this, getString(R.string.app_tip),
					getString(R.string.getting_prepayid));
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}
			sb.append("prepay_id\n" + result.get("prepay_id") + "\n\n");
			resultunifiedorder = result;
			// 生成app支付的参数
			genPayReq();
			// 调起微信支付
			sendPayReq();
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = genProductArgs();

			Log.e("orion", entity);

			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);
			Logs.info("微信支付:"+content);
			Map<String, String> xml = decodeXml(content);
			return xml;
		}
	}

	/**
	 * @author 杨财宾 修改人: 时间:2015-9-10 下午4:27:56 方法说明:生成微信支付的参数
	 */
	private void genPayReq() {
		req.appId = APP_ID;
		req.partnerId = MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());
		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genAppSign(signParams);
		sb.append("sign\n" + req.sign + "\n\n");
		Log.e("orion", signParams.toString());
	}

	/**
	 * @author 杨财宾 修改人: 时间:2015-9-10 下午4:31:40 方法说明:調起微信支付的方法
	 */
	private void sendPayReq() {
		msgApi.registerApp(APP_ID);
		msgApi.sendReq(req);
	}

	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();
			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams.add(new BasicNameValuePair("appid", APP_ID));
			packageParams.add(new BasicNameValuePair("attach", payEntity.getEntity().getOrderNo() + "," + userId));
			packageParams.add(new BasicNameValuePair("body", payEntity.getEntity().getOrderNo()));
			packageParams.add(new BasicNameValuePair("mch_id", MCH_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url", Address.HOST_PAY + "app/order/wxpaynotify"));
			packageParams.add(new BasicNameValuePair("out_trade_no", genOutTradNo()));
			packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", new BigDecimal(payEntity.getEntity().getBankAmount())
					.multiply(new BigDecimal(100)).setScale(0, BigDecimal.ROUND_HALF_UP).toString()));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));
			String sign = genPackageSign(packageParams);
			packageParams.add(new BasicNameValuePair("sign", sign));
			String xmlstring = toXml(packageParams);
			return xmlstring;
		} catch (Exception e) {
			return null;
		}
	}

	public Map<String, String> decodeXml(String content) {
		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {
				String nodeName = parser.getName();
				switch (event) {
				case XmlPullParser.START_DOCUMENT:
					break;
				case XmlPullParser.START_TAG:
					if ("xml".equals(nodeName) == false) {
						// 实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;
				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}
			return xml;
		} catch (Exception e) {
			Log.e("orion", e.toString());
		}
		return null;
	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(API_KEY);

		this.sb.append("sign str\n" + sb.toString() + "\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion", appSign);
		return appSign;
	}

	private String genOutTradNo() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	/**
	 * @author 杨财宾 修改人: 时间:2015-9-10 下午4:25:18 方法说明:生成签名
	 */
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion", packageSign);
		return packageSign;
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		Log.e("orion", sb.toString());
		return sb.toString();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (brodCast == null) {
			brodCast = new MyBrodCast();
			IntentFilter filter = new IntentFilter();
			filter.addAction("wxSuccess");
			registerReceiver(brodCast, filter);
		}
	}

	class MyBrodCast extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("wxSuccess".equals(action)) {
				// 向服务器提交订单信息
				// 向服务器提交订单信息
				getPaysuccessCall(userId, payEntity.getEntity().getBankAmount(), genOutTradNo(), payType,
						payEntity.getEntity().getOrderNo());
			}
		}
	}
}
