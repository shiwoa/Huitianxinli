package com.yizhilu.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @author bin 修改人: 时间:2015-9-28 下午2:22:55 类说明:网络的工具类
 */
public class NetWorkUtils {

	private static ConnectivityManager connectivityManager; // 判断网络的对象

	// 实例话判断网络的类
	public static void initConnectivityManager(Context context){
		if(connectivityManager == null){
			connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		}
	}
	// 判断网络是否可用
	public static boolean isNetworkAvailable(Context context) {
		initConnectivityManager(context);
		//得到网络的信息
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}
		return false;
	}
	// 判断是否是WIFI连接
	public static boolean isWIFI(Context context){
		initConnectivityManager(context);
		NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if(networkInfo!=null && networkInfo.isConnected()){
			return true;
		}
		return false;
	}
	//返回网络连接的类型
	public static String getNetWorkType(Context context){
		String type = "网络已断开";
		initConnectivityManager(context);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		if(activeNetworkInfo!=null){
			switch (activeNetworkInfo.getType()) {
			case ConnectivityManager.TYPE_WIFI:  //wifi连接
				type = "wifi网络连接";
				break;
			case ConnectivityManager.TYPE_MOBILE:
				type = "手机网络连接";
				break;
			default:
				break;
			}
		}
		return type;
	}
}
