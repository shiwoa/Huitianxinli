package com.yizhilu.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ParamsUtil {
	
	public final static int INVALID = -1;

	public static int getInt(String str){
		int num = INVALID;
		
		try {
			num = Integer.parseInt(str);
		} catch (NumberFormatException e) {
			
		}
		
		return num;
	}
	
	public static long getLong(String str){
		long num = 0l;
		
		try {
			num = Long.parseLong(str);
		} catch (NumberFormatException e) {
		}
		
		return num;
	}
	
	public static String byteToM(long num){
		double m = (double) num / 1024 / 1024;
		return String.format("%.2f", m);
	}
	
	public static String millsecondsToStr(int seconds){
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
	
	
	public static String formatSecond(Object second) {
		/**
		 * 时间换算
		 * */
		String html = "0秒";
		if (second != null) {
			Double s = Double.parseDouble((String) second);
			String format;
			Object[] array;
			Integer hours = (int) (s / (60 * 60));
			Integer minutes = (int) (s / 60 - hours * 60);
			Integer seconds = (int) (s - minutes * 60 - hours * 60 * 60);
			if (hours > 0) {
				format = "%1$,d时%2$,d分%3$,d秒";
				array = new Object[] { hours, minutes, seconds };
			} else if (minutes > 0) {
				format = "%1$,d分%2$,d秒";
				array = new Object[] { minutes, seconds };
			} else {
				format = "%1$,d秒";
				array = new Object[] { seconds };
			}
			html = String.format(format, array);
		}

		return html;
	}
	
	public static int dpToPx(Context context, int height){
		float density = context.getResources().getDisplayMetrics().density;
		height = (int) (height * density + 0.5f);
		return height;
	}
	//---------------------------------------计算ListView的高度
		public static void setListViewHeightBasedOnChildren(ListView listView) {
			// 获取ListView对应的Adapter
			ListAdapter listAdapter = listView.getAdapter();
			if (listAdapter == null) {
				return;
			}
			int totalHeight = 0;
			for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
				View listItem = listAdapter.getView(i, null, listView);
				listItem.measure(0, 0); // 计算子项View 的宽高
				totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
			}
			ViewGroup.LayoutParams params = listView.getLayoutParams();
			params.height = totalHeight
					+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
			// listView.getDividerHeight()获取子项间分隔符占用的高度
			// params.height最后得到整个ListView完整显示需要的高度
			listView.setLayoutParams(params);

		}
	
}
