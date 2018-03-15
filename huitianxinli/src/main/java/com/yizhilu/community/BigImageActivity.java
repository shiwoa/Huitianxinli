package com.yizhilu.community;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.community.view.ZoomOutPageTransformer;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.view.PhotoView;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//浏览大图
public class BigImageActivity extends BaseActivity {

	private ViewPager viewPager;
	private String[] picUrls;// 图片地址数组
	private int index;// 当前下标
	private int size;// 总size
	private static PhotoView photoView;
	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_big_image);
		super.onCreate(savedInstanceState);
		viewPager = (ViewPager) findViewById(R.id.photo_viewPager);
		title = (TextView) findViewById(R.id.title);
		index = getIntent().getIntExtra("index", 0);
		picUrls = getIntent().getStringArrayExtra("picUrls");
		size = picUrls.length;
		title.setText(index + 1 + "/" + size);
		viewPager.setAdapter(new CheckImageAdapter(picUrls));
		viewPager.setCurrentItem(index);
		viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				title.setText(position + 1 + "/" + size);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});
	}

	static class CheckImageAdapter extends PagerAdapter {

		private String[] picUrls;
		private int size;
		private DisplayImageOptions picOptions;

		public CheckImageAdapter(String[] picUrls) {
			this.picUrls = picUrls;
			this.size = picUrls.length;
			picOptions = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
					.showImageOnLoading(R.color.text_gray777).showImageForEmptyUri(R.color.text_gray777)
					.showImageOnFail(R.color.text_gray777).build();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			photoView = new PhotoView(container.getContext());
			ImageLoader.getInstance().displayImage(picUrls[position], photoView, picOptions);
			container.addView(photoView);
			return photoView;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return size;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOnClick() {
		// TODO Auto-generated method stub

	}

}
