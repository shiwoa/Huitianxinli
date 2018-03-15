package com.yizhilu.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;

public class BitmapUtils {

	/**
	 * 从指定文件路径 加载位图对象
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap loadBitmap(String path) {
		return BitmapFactory.decodeFile(path);
	}

	/**
	 * 从图片的字节数组中 加载位图对象 并对加载图片的宽高进行限制
	 * 
	 * @param data
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap loadBitmap(byte[] data, int width, int height) {
		Bitmap bm = null;
		// 创建加载选项对象
		Options opts = new Options();
		// 设置仅加载边界信息
		opts.inJustDecodeBounds = true;
		// 加载为位图尺寸信息
		BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		// 计算并设置收缩比例
		int x = opts.outWidth / width;
		int y = opts.outHeight / height;
		opts.inSampleSize = x > y ? x : y;
		// 取消仅加载边界信息的设置
		opts.inJustDecodeBounds = false;
		// 加载位图
		bm = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		return bm;
	}

	/**
	 * 将位图对象保存到指定文件目录
	 * 
	 * @param bm
	 * @param file
	 * @throws IOException
	 */
	public static void save(Bitmap bm, File file) throws IOException {
		if (bm != null && file != null) {
			// 如果父目录不存在 则创建目录
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			// 如果文件不存在则创建文件
			if (!file.exists()) {
				file.createNewFile();
			}
			// 保存
			bm.compress(CompressFormat.JPEG, 100, new FileOutputStream(file));
		}
	}
}
