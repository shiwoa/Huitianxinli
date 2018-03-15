package com.yizhilu.utils;

import java.io.IOException;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yizhilu.huitianxinli.R;

/**
 * @author bin 修改人: 时间:2015-10-12 上午11:18:51 类说明:联网的工具类
 */
public class HttpUtils {
	public static final int METHOD_GET = 1;
	public static final int METHOD_POST = 2;
	public static DisplayImageOptions options;
	public static DisplayImageOptions userOptions;
	public PhoneUtils phoneUtils; // 手机的工具类
	public AsyncHttpClient httpClient; // 联网获取数据的方法
	

	public HttpUtils(Context context) {
		super();
		phoneUtils = new PhoneUtils(context); // 手机的工具类
		httpClient = new AsyncHttpClient(); // 联网获取数据的方法
	}

	/**
	 * 加载图片
	 * */
	public static Bitmap getBitmap(String url) {
		try {
			HttpEntity entity = getEntity(url, null, METHOD_GET);
			byte[] data = EntityUtils.toByteArray(entity);
			Bitmap bitmap = BitmapUtils.loadBitmap(data, 400, 200);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 连接服务端指定的资源路径获取响应实体对象
	 * 
	 * @param uri
	 * @param params
	 * @param method
	 * @return
	 * @throws IOException
	 */
	public static HttpEntity getEntity(String uri, List<NameValuePair> params,
			int method) throws IOException {
		HttpEntity entity = null;
		// 创建客户端对象
		HttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 3000);
		// 创建请求对象
		HttpUriRequest request = null;
		switch (method) {
		case METHOD_GET:// get请求
			StringBuilder sb = new StringBuilder(uri);
			if (params != null && !params.isEmpty()) {
				sb.append('?');
				for (NameValuePair pair : params) {
					sb.append(pair.getName()).append('=')
							.append(pair.getValue()).append('&');
				}
				sb.deleteCharAt(sb.length() - 1);
			}
			request = new HttpGet(sb.toString());
			break;
		case METHOD_POST:// post请求
			request = new HttpPost(uri);
			if (params != null && !params.isEmpty()) {
				UrlEncodedFormEntity reqEntity = new UrlEncodedFormEntity(
						params);
				((HttpPost) request).setEntity(reqEntity);
			}
			break;
		}
		// 执行请求获得实体对象
		HttpResponse response = client.execute(request);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			entity = response.getEntity();
		}
		// 返回响应实体
		return entity;
	}

	/**
	 * @author bin 修改人: 时间:2015-10-12 上午11:32:40 方法说明:显示进度条
	 */
	public static void showProgressDialog(ProgressDialog progressDialog) {
		progressDialog.setMessage("努力加载中...");
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
	}

	/**
	 * @author bin 修改人: 时间:2015-10-12 上午11:33:10 方法说明:隐藏进度条
	 */
	public static void exitProgressDialog(ProgressDialog progressDialog) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-10-16 上午10:30:55 方法说明:加载图片
	 */
	public static DisplayImageOptions getDisPlay() {
		if (options == null) {
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.sprite)
					.showImageOnFail(R.drawable.sprite).cacheInMemory(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		}
		return options;
	}
	
	/**
	 * @author bin 修改人: 时间:2015-10-16 上午10:30:55 方法说明:头像图片加载
	 */
	public static DisplayImageOptions getUserHeadDisPlay() {
		if (userOptions == null) {
			userOptions = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.head_bg)
					.showImageOnFail(R.drawable.head_bg).cacheInMemory(true)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.imageScaleType(ImageScaleType.IN_SAMPLE_INT).build();
		}
		return userOptions;
	}

	/**
	 * @author bin 修改人: 时间:2016-1-25 下午1:43:41 方法说明:添加登陆记录的方法
	 */
	public void addLoginRecord(int userId) {
		RequestParams params = new RequestParams();
		params.put("websiteLogin.ip", PhoneUtils.GetHostIp());
		params.put("websiteLogin.brand", phoneUtils.getPhoneBrand());
		params.put("websiteLogin.modelNumber", phoneUtils.getPhoneModel());
		params.put("websiteLogin.size", phoneUtils.getPhoneSize());
		params.put("websiteLogin.userId", userId);
		params.put("websiteLogin.type", "android");
		httpClient.post(Address.ADD_LOGIN_RECORD, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						Log.i("lala", data+".....HttpUtils");
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {

					}
				});
	}
}
