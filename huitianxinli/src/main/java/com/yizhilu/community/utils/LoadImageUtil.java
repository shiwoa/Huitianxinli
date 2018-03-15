package com.yizhilu.community.utils;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.yizhilu.huitianxinli.R;

import android.graphics.Bitmap;

public class LoadImageUtil {

	private static DisplayImageOptions options;
	private static DisplayImageOptions roundOptions;

	public static DisplayImageOptions loadImage() {

		if (options == null) {
			options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.weijiazai_header) // 设置图片下载期间显示的图片
					.showImageForEmptyUri(R.drawable.weijiazai_header) // 设置图片Uri为空或是错误的时候显示的图片
					.showImageOnFail(R.drawable.weijiazai_header) // 设置图片加载或解码过程中发生错误显示的图片
					.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
					.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
					.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
					.displayer(new FadeInBitmapDisplayer(1000))// 设置渐入动画时间
					// .displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
					.build(); // 构建完成
		}
		return options;
	}

	public static DisplayImageOptions loadImageRound() {

		if (roundOptions == null) {
			roundOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.weijiazai_header) // 设置图片下载期间显示的图片
					.showImageForEmptyUri(R.drawable.weijiazai_header) // 设置图片Uri为空或是错误的时候显示的图片
					.showImageOnFail(R.drawable.weijiazai_header) // 设置图片加载或解码过程中发生错误显示的图片
					.cacheInMemory(true) // 设置下载的图片是否缓存在内存中
					.cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
					.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
					.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
					// .displayer(new FadeInBitmapDisplayer(1000))// 设置渐入动画时间
					.displayer(new RoundedBitmapDisplayer(10)) // 设置成圆角图片
					.build(); // 构建完成
		}
		return roundOptions;
	}
}
