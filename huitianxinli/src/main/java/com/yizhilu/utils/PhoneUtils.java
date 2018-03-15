package com.yizhilu.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.WindowManager;

/**
 * @author bin 修改人: 时间:2015-12-17 下午3:36:33 类说明:手机信息的工具类
 */
public class PhoneUtils {
	private Context context;
	private TelephonyManager mTm;
	private WindowManager windowManager;  //窗口管理
	public PhoneUtils(Context context) {
		super();
		this.context = context;
		mTm = (TelephonyManager) this.context
				.getSystemService(Context.TELEPHONY_SERVICE);
		windowManager = (WindowManager) this.context
				.getSystemService(Context.WINDOW_SERVICE);
	}

	/**
	 * @author bin 修改人: 时间:2015-12-17 下午3:47:13 方法说明:获取手机型号
	 */
	public String getPhoneModel() {
		return android.os.Build.MODEL; // 手机型号
	}

	/**
	 * @author bin 修改人: 时间:2015-12-17 下午3:48:00 方法说明:获取手机型号
	 */
	public String getPhoneBrand() {
		return android.os.Build.BRAND;// 手机品牌
	}

	/**
	 * @author bin 修改人: 时间:2015-12-17 下午3:49:09 方法说明:获取手机Imei号
	 */
	public String getPhoneImei() {
		return mTm.getDeviceId();
	}

	/**
	 * @author bin 修改人: 时间:2015-12-17 下午3:50:02 方法说明:获取手机Imsi号
	 */
	public String getPhoneImsi() {
		return mTm.getSubscriberId();
	}

	/**
	 * @author bin 修改人: 时间:2015-12-17 下午3:51:15 方法说明:获取手机号的方法
	 */
	public String getPhoneNumber() {
		return mTm.getLine1Number();
	}

	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-12-28 下午5:30:48
	 * 方法说明:获取手机的IP
	 */
	public static String GetHostIp() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> ipAddr = intf.getInetAddresses(); ipAddr
						.hasMoreElements();) {
					InetAddress inetAddress = ipAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress();
					}
				}
			}
		} catch (SocketException ex) {
		} catch (Exception e) {
		}
		return null;
	}
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-25 下午2:02:13
	 * 方法说明:获取手机屏幕的宽度
	 */
	public int getPhoneWidth(){
		return windowManager.getDefaultDisplay().getWidth();
	}

	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-25 下午2:03:08
	 * 方法说明:获取屏幕的高度
	 */
	public int getPhoneHeight(){
		return windowManager.getDefaultDisplay().getHeight();
	}
	
	public String getPhoneSize(){
		return getPhoneWidth()+"*"+getPhoneHeight();
	}
}
