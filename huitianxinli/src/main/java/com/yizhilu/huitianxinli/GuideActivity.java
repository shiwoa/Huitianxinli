package com.yizhilu.huitianxinli;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.PhoneUtils;

/**
 * @author bin 修改人: 时间:2015-9-21 上午11:20:08 类说明:引导页的类
 */
public class GuideActivity extends BaseActivity{
	private ViewPager viewPager; // 切换引导页的控件
	private List<View> viewList; // 存放引导页的布局
	private ImageView imageOne, imageTwo; // 引导页的第一张和第二张图片
	private View viewThree; // 引导页第三张的布局
	private Button btnGo; // 立即体验的按钮
	private Intent intent; // 意图对象
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private LinearLayout layout_three;
	private PhoneUtils phoneUtils; // 手机的工具类

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_guide);
		super.onCreate(savedInstanceState);
	}

	/**
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		phoneUtils = new PhoneUtils(GuideActivity.this); // 手机的工具类
		intent = new Intent();
		viewList = new ArrayList<View>(); // 存放引导页的布局
		viewPager = (ViewPager) findViewById(R.id.viewPager); // 切换引导页的控件
		imageOne = new ImageView(GuideActivity.this);
		imageOne.setBackgroundResource(R.drawable.view_one);
		imageTwo = new ImageView(GuideActivity.this);
		imageTwo.setBackgroundResource(R.drawable.view_two);
		viewThree = LayoutInflater.from(GuideActivity.this).inflate(
				R.layout.layout_viewthree, null);
		layout_three = (LinearLayout) viewThree.findViewById(R.id.three_view);
		
		btnGo = (Button) viewThree.findViewById(R.id.btnGo); // 立即体验的按钮
		viewList.add(imageOne);
		viewList.add(imageTwo);
		viewList.add(viewThree);
		// 绑定适配器
		viewPager.setAdapter(new ViewPagerAdapter());
		// 添加安装记录的方法
		addInatallRecord();
	}

	/**
	 * @author bin 修改人: 时间:2016-1-25 下午2:23:55 方法说明:添加安装记录的方法
	 */
	private void addInatallRecord() {
		RequestParams params = new RequestParams();
		params.put("websiteInstall.ip", PhoneUtils.GetHostIp());
		params.put("websiteInstall.brand", phoneUtils.getPhoneBrand());
		params.put("websiteInstall.modelNumber", phoneUtils.getPhoneModel());
		params.put("websiteInstall.size", phoneUtils.getPhoneSize());
		params.put("websiteLogin.type", "android");
		Log.i("lala", Address.ADD_INSTALL_RECORD + "?" + params.toString());
		httpClient.post(Address.ADD_INSTALL_RECORD, params,
				new TextHttpResponseHandler() {

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {

					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {

					}
				});
	}

	/**
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		// btnGo.setOnClickListener(this); // 立即体验的按钮
		layout_three.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.three_view: // 立即体验的按钮
			getSharedPreferences("isFrist", MODE_PRIVATE).edit()
					.putBoolean("isFrist", false).commit();
			intent.setClass(GuideActivity.this, MainActivity.class);
			startActivity(intent);
			this.finish();
			break;

		default:
			break;
		}
	}

	/**
	 * @author bin 修改人: 时间:2015-9-21 下午1:54:58 类说明:引导页滑动的适配器
	 */
	class ViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return viewList.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(viewList.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(viewList.get(position));
			return viewList.get(position);
		}

	}
}
