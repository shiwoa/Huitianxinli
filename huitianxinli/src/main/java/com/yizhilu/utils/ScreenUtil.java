package com.yizhilu.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.DisplayMetrics;

public class ScreenUtil {

	private static DisplayMetrics mMetrics = new DisplayMetrics();
	private static ScreenUtil instance;
	private Context context;

	public static ScreenUtil getInstance(Context context) {
		if (instance == null)
			instance = new ScreenUtil(context);
		return instance;
	}

	private ScreenUtil(Context _context) {
		context = _context;
		mMetrics = context.getResources().getDisplayMetrics();
	}

	public int getScreenHeight() {
		return mMetrics.heightPixels;
	}

	public int getScreenWidth() {
		return mMetrics.widthPixels;
	}

	public int dip2px(float dpValue) {
		final float scale = mMetrics.density;
		return (int) (dpValue * scale + 0.5f);
	}

	public int px2dip(float pxValue) {
		final float scale = mMetrics.density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static void saveSDcard(Bitmap bitmap, String filename) {
		try {
			FileOutputStream outStream = new FileOutputStream(filename);
			if (outStream != null) {
				bitmap.compress(Bitmap.CompressFormat.PNG, 50, outStream);
				outStream.flush();
				outStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 获取圆角位图的方法
	 * 
	 * @param bitmap
	 *            需要转化成圆角的位图
	 * @param pixels
	 *            圆角的度数，数值越大，圆角越大
	 * @return 处理后的圆角位图
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
}
