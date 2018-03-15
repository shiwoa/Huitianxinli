package com.yizhilu.utils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.widget.Toast;

public class ConstantUtils {
	private static Context context;

	public ConstantUtils(Context context) {
		this.context = context;
	}

	/**
	 * 显示Toast的方法
	 */
	public static void showMsg(Context context, String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	// 获取当前app版本
	public static String getVersionName(Context context) {
		// 获取packagemanager的实例
		String version = null;
		PackageManager packageManager = context.getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(context.getPackageName(),
					0);
			version = packInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	public static String getDirPath(String name) {
		String path = "";
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			java.io.File file = new File(Environment
					.getExternalStorageDirectory().getPath() + "/268WX/");
			if (!file.exists()) {
				if (!file.mkdirs()) {
					return path;
				}
			}
			file = new File(Environment.getExternalStorageDirectory().getPath()
					+ "/268WX/" + name);
			if (!file.exists()) {
				if (file.mkdirs()) {
					path = file.getAbsolutePath();
				}
			} else {
				path = file.getAbsolutePath();
			}
		} else {
			File dir = context.getDir(name, Context.MODE_PRIVATE
					| Context.MODE_WORLD_READABLE
					| Context.MODE_WORLD_WRITEABLE);
			path = dir.getAbsolutePath();
		}
		return path;
	}

	/**
	 * 获取权限
	 * 
	 * @param permission
	 *            权限
	 * @param path
	 *            路径
	 */
	public static void chmod(String permission, String path) {
		try {
			String command = "chmod " + permission + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-28 下午2:03:04
	 * 方法说明:保留一位小数的方法
	 * position:保留的位数
	 * string:处理的支付串
	 */
	public static String getRetainOneDecimal(int position,String string){
		BigDecimal bigDecimal  = new BigDecimal(string);
		BigDecimal setScale = bigDecimal.setScale(position,BigDecimal.ROUND_HALF_UP);
		return setScale.toString();
	}
	
	/**
	 * 显示进度条
	 *
	 */
	public static void showProgressDialog(ProgressDialog progressDialog) {
		progressDialog.setMessage("努力加载中...");
		progressDialog.setCancelable(false);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.show();
	}

	/**
	 * 隐藏进度条
	 *
	 */
	public static void exitProgressDialog(ProgressDialog progressDialog) {
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}
}
