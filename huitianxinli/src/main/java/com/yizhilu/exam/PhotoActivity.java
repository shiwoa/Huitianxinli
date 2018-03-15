package com.yizhilu.exam;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ExamAddress;

/**
 * @author bin
 * 时间: 2016/6/28 15:36
 * 类说明:图片缩小放大的类
 */
public class PhotoActivity extends BaseActivity {
	private PhotoView photoView;
	private ImageLoader imgeLoader;  //加载图片的对象
	private String url;  //图片的地址
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_photo);
		getIntentMessage();
		super.onCreate(savedInstanceState);
	}
	/**
	 * 获取传过来的信息
	 */
	private void getIntentMessage() {
		Intent intent = getIntent();
		url = intent.getStringExtra("url");  //图片路径
	}
	@Override
	public void initView() {
		imgeLoader = ImageLoader.getInstance();
		photoView = (PhotoView) findViewById(R.id.photoView);
		imgeLoader.loadImage(ExamAddress.IMAGE_HOST + url, new SimpleImageLoadingListener(){

			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				photoView.setImageBitmap(loadedImage);
			}
			
		});
	}

	@Override
	public void addOnClick() {
		photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
			
			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				PhotoActivity.this.finish();
			}
		});
	}

}
