package com.yizhilu.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class Const {

	/**
	 * 
	 * @author lrannn 修改人: 时间:2015年10月23日 下午6:38:30 方法说明:创建文件
	 */
	public static File createFile(String title) {
		File file = null;
		// 判断sd卡是否存在
		if (android.os.Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File sdDir = Environment.getExternalStorageDirectory();// 获取根目录
			File dir = new File(sdDir + "/" + ConfigUtil.DOWNLOAD_DIR);
			if (!dir.exists()) {
				dir.mkdir();
			}
			String path = dir + "/" + title + ".pcm";
			file = new File(path);
		}

		return file;
	}

	public final static int INVALID = -1;

	public static int getInt(String str) {
		int num = INVALID;

		try {
			num = Integer.parseInt(str);
		} catch (NumberFormatException e) {

		}

		return num;
	}

	public static boolean findFile(String path) {
		File file = null;
		file = new File(path);
		if (file.exists()) {
			return true;
		}
		return false;
	}

	public static long getLong(String str) {
		long num = 0l;

		try {
			num = Long.parseLong(str);
		} catch (NumberFormatException e) {
		}

		return num;
	}

	// 字节转换成M
	public static String byteToM(long num) {
		double m = (double) num / 1024 / 1024;
		return String.format("%.2f", m);
	}

	public static String millsecondsToStr(int seconds) {
		seconds = seconds / 1000;
		String result = "";
		int hour = 0, min = 0, second = 0;
		hour = seconds / 3600;
		min = (seconds - hour * 3600) / 60;
		second = seconds - hour * 3600 - min * 60;
		if (hour < 10) {
			result += "0" + hour + ":";
		} else {
			result += hour + ":";
		}
		if (min < 10) {
			result += "0" + min + ":";
		} else {
			result += min + ":";
		}
		if (second < 10) {
			result += "0" + second;
		} else {
			result += second;
		}
		return result;
	}

	public static int dpToPx(Context context, int height) {
		float density = context.getResources().getDisplayMetrics().density;
		height = (int) (height * density + 0.5f);
		return height;
	}

}
