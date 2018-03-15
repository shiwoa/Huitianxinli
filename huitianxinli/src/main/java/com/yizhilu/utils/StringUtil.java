package com.yizhilu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.text.TextUtils;

public class StringUtil {

	public static SimpleDateFormat mDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");

	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}

	public static boolean isNotEmpty(String s) {
		return (s != null && s.trim().length() > 0);
	}

	public static String getRandom() {
		Random random = new Random();
		return String.valueOf(random.nextInt(20) + 40);
	}

	public static String readAssets(Context context, String fileName) {
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		if (fileName == null || fileName.equals("")) {
			return null;
		}
		try {
			is = context.getAssets().open(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));

			sb.append(br.readLine());

			String line;
			while ((line = br.readLine()) != null) {
				// temp+=line;
				sb.append(line);
			}
		} catch (Exception e) {
			return null;
		}
		return sb.toString();
	}

	public static String readFromFile(Context context, String fileName) {
		InputStream is = null;
		StringBuilder sb = new StringBuilder();

		// String temp = null;
		File file;
		if (fileName == null || fileName.equals("")) {
			return null;
		}
		try {
			file = context.getFileStreamPath(fileName);
			is = new FileInputStream(file);
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			int c;
			char[] charStr = new char[1024];
			while ((c = isr.read(charStr)) != -1) {
				sb.append(charStr, 0, c);
			}

			is.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return sb.toString();

	}

	public static int getChineseLength(String value) {
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 1;
			}
		}
		return valueLength;
	}

	public static int getLengthForUpdate(String value) {
		int cLength = 0;
		int eLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				cLength += 2;
			} else {
				eLength += 1;
			}
		}
		return cLength + eLength;
	}

	public static int getStrLength(String value) {
		int cLength = 0;
		int eLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		for (int i = 0; i < value.length(); i++) {
			String temp = value.substring(i, i + 1);
			if (temp.matches(chinese)) {
				cLength += 1;
			} else {
				eLength += 1;
			}
		}
		return cLength + eLength / 2 + eLength % 2;
	}

	/**
	 * 是否是合法的json字符串
	 * 
	 * @param str
	 * @param type
	 * @return
	 */

	public static String formatDistance(int meter) {
		if (meter < 0) {
			return "";
		} else if (meter < 100) {
			return meter + "米";
		} else if (meter < 50000) {
			return ToPrice(meter / 1000f, 1) + "公里";
		} else {
			return ">50公里";
		}
	}

	public static String ToPrice(float amount) {
		return String.format("%1$.2f", amount);
	}

	public static String ToPrice(float amount, int p) {
		return String.format("%1$." + p + "f", amount);
	}

	public static String ToNumber(double num, int p) {
		if (Double.isNaN(num)) {
			return "--";
		}
		BigDecimal bd = new BigDecimal(num);
		num = bd.setScale(p, BigDecimal.ROUND_HALF_UP).doubleValue();
		return String.format("%1$." + p + "f", num);
	}

	public static String ToDate(Date date) {
		return mDateFormat.format(date);
	}

	public static Date ParseDate(String date) {
		try {
			return mDateFormat.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getBeforeDot(String price) {
		if (StringUtil.isNotEmpty(price)) {
			if (-1 != price.indexOf(".")) {
				price = price.substring(0, price.indexOf("."));
				return price;
			}
		}
		return "";
	}

	private static String addZero(int str) {
		String tempStr = String.valueOf(str);
		if (1 == tempStr.length()) {
			return "0" + tempStr;
		} else {
			return tempStr;
		}
	}

	public static String formatLongToTimeStr(Long l) {
		String strtime = "";
		if (l > 0) {
			int hour = 0;
			int minute = 0;
			int second = 0;
			second = l.intValue() / 1000;
			if (second > 60) {
				minute = second / 60;
				second = second % 60;
			}

			if (minute > 60) {
				hour = minute / 60;
				minute = minute % 60;
			}

			strtime = addZero(hour) + ":" + addZero(minute) + ":"
					+ addZero(second);
		} else {
			strtime = "00:00:00";
		}

		return strtime;

	}

	public static String toUnicode(String s) {
		String s1 = "";
		for (int i = 0; i < s.length(); i++) {
			s1 += "\\u" + Integer.toHexString(s.charAt(i) & 0xffff);
		}
		return s1;
	}

	public static String replaceUrlWithPlus(String url) {
		if (url != null) {
			return url.replaceAll("http://(.)*?/", "")
					.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
		}
		return null;
	}

	public static String md5(String string) {

		byte[] hash;

		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			// throw new RuntimeException("Huh, MD5 should be supported?", e);
			return string;
		} catch (UnsupportedEncodingException e) {
			// throw new RuntimeException("Huh, UTF-8 should be supported?", e);
			return string;
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);

		for (byte b : hash) {
			int i = (b & 0xFF);
			if (i < 0x10)
				hex.append('0');
			hex.append(Integer.toHexString(i));
		}

		return hex.toString();
	}

	public static int getLength(String content) {
		String anotherString = "";
		try {
			anotherString = new String(content.getBytes("GBK"), "ISO8859_1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return anotherString.length();
	}

	public static String readAssetsCity(Context context, String fileName) {
		InputStream is = null;
		StringBuilder sb = new StringBuilder();
		if (fileName == null || fileName.equals("")) {
			return null;
		}
		try {
			is = context.getAssets().open(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(is,
					"UTF-8"));

			sb.append(br.readLine());

			String line;
			while ((line = br.readLine()) != null) {
				// temp+=line;
				sb.append(line);
			}
		} catch (Exception e) {
			return null;
		}
		return sb.toString();
	}

	public static String MobileMask(String mobile) {
		if (isNotEmpty(mobile) && mobile.length() > 3) {
			if (mobile.length() > 7)
				return mobile.substring(0, 3) + "****" + mobile.substring(7);
			return mobile.substring(0, 3) + "****";
		}
		return "****";
	}

	public static boolean isInstalled(Context context, String packageName) {
		PackageInfo packageInfo;

		try {
			packageInfo = context.getPackageManager().getPackageInfo(
					packageName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		return packageInfo != null ? true : false;
	}

	// 获取app缓存文件大小
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			java.io.File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	// 转换文件大小
	public static String FormetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS==0) {
			fileSizeString = "";
		}else if (fileS < 1024 && fileS > 0) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	/**
	 * 删除指定目录下文件及目录
	 * 
	 * @param deleteThisPath
	 * @param filepath
	 * @return
	 */
	public static void deleteFolderFile(String filePath, boolean deleteThisPath) {
		if (!TextUtils.isEmpty(filePath)) {
			try {
				File file = new File(filePath);
				if (file.isDirectory()) {// 如果下面还有文件
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) {
						deleteFolderFile(files[i].getAbsolutePath(), true);
					}
				}
				if (deleteThisPath) {
					if (!file.isDirectory()) {// 如果是文件，删除
						file.delete();
					} else {// 目录
						if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
//							file.delete();
						}
					}
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	/**
	 * @param field
	 * @return 判断字段是否为空  为空返回空字符串
	 */
	public static String isFieldEmpty(String field){
		if(!TextUtils.isEmpty(field)){
			return field;
		}
		return "";
	}
}
