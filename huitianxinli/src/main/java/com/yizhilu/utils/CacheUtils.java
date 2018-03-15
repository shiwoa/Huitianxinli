package com.yizhilu.utils;

import android.content.Context;

public class CacheUtils {
	public static void setSharedPreference(Context context, String name,
			String data) {
		context.getSharedPreferences(name, context.MODE_PRIVATE).edit()
				.putString(name, data).commit();

	}

	public static String getSharedPreference(Context context, String name) {
		String data = context.getSharedPreferences(name, context.MODE_PRIVATE)
				.getString(name, "");
		return data;
	}

}
