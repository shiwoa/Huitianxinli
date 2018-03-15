package com.yizhilu.fragment;

import org.apache.http.Header;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;

/**
 * @author bin
 * 修改人:
 * 时间:2016-1-14 上午11:55:32
 * 类说明:优惠券的类
 */
public class CouponFragment extends BaseFragment {

	private View inflate;  //优惠券的总布局
	private LinearLayout available_coupon_layout,used_coupon_layout,past_coupon_layout;  //可用,已使用不可用
	private TextView available_text,used_text,past_text;  //可用,已用,不可用
	private View available_view,used_view,past_view;  //可用,已使用,不可用
	private static CouponFragment couponFragment;  //本类对象
	private boolean isAvailable,isUsed,isPast;  //是否可用
	private AvailableCouponFragment availableCouponFragment;  //可用优惠券
	private UsedCouponFragment usedCouponFragment;  //已用优惠券
	private PastCouponFragment pastCouponFragment;  //不可用优惠券
	private FragmentManager fragmentManager;  //fragment管理
	private boolean fristUsed = true,fristPast = true;
	private int userId,page = 1;  //用户Id,页数
	private AsyncHttpClient httpClient;  //联网获取数据的方法
	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_coupon, container, false);
		return inflate;
	}

	/* 
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		httpClient = new AsyncHttpClient();  //联网获取优惠券的对象
		userId = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).getInt("userId", 0);
		available_coupon_layout = (LinearLayout) inflate.findViewById(R.id.available_coupon_layout);  //可用
		used_coupon_layout = (LinearLayout) inflate.findViewById(R.id.used_coupon_layout);  //已使用
		past_coupon_layout = (LinearLayout) inflate.findViewById(R.id.past_coupon_layout);  //不可用
		available_text = (TextView) inflate.findViewById(R.id.available_text);
		used_text = (TextView) inflate.findViewById(R.id.used_text);  //已用
		past_text = (TextView) inflate.findViewById(R.id.past_text);
		available_view = inflate.findViewById(R.id.available_view);  //可用线
		used_view = inflate.findViewById(R.id.used_view);  //已使用线
		past_view = inflate.findViewById(R.id.past_view);  //不可用线
		//联网获取可用优惠券的方法
		getAvailableCouponData(userId,page);
		availableCouponFragment = AvailableCouponFragment.getInstentce();
		fragmentManager = getActivity().getSupportFragmentManager(); // 得到fragment的管理器
		fragmentManager.beginTransaction()
				.add(R.id.available_past_coupon_layout, availableCouponFragment)
				.show(availableCouponFragment).commit();
	}
	
	/**
	 * 获取本类实例的方法
	 */
	public static CouponFragment getInstance(){
		if(couponFragment == null){
			couponFragment = new CouponFragment();
		}
		return couponFragment;
	}

	/* 
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		available_coupon_layout.setOnClickListener(this);  //可用
		used_coupon_layout.setOnClickListener(this);  //已用
		past_coupon_layout.setOnClickListener(this);  //不可用
	}
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-14 下午5:51:42
	 * 方法说明:联网获取优惠券的方法
	 */
	private void getAvailableCouponData(int userId, final int page) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("page.currentPage", page);
		params.put("type", 1);  //1为优惠券
		Log.i("lala", Address.GET_USER_COUPON+"?"+params.toString());
		httpClient.post(Address.GET_USER_COUPON, params , new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
//				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
//				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						String message = publicEntity.getMessage();
						if(publicEntity.isSuccess()){
							EntityPublic entityPublic = publicEntity.getEntity();
							available_text.setText("可使用("+entityPublic.getCount_1()+")");  //可使用
							used_text.setText("已使用("+entityPublic.getCount_2()+")"); //已使用
							int pastCount = entityPublic.getCount_3()+entityPublic.getCount_4();
							past_text.setText("已过期("+pastCount+")");  //已过期
						}
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
//				HttpUtils.exitProgressDialog(progressDialog);
			}
		});
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		// 开启一个Fragment的事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 隐藏掉所有的fragment
		hideFragments(transaction);
		switch (v.getId()) {
		case R.id.available_coupon_layout:  //可用
			isAvailable = true;
			isUsed = false;
			isPast = false;
			//设置可用的信息
			setAvailableOrPast();
			if (availableCouponFragment == null) {
				// 如果可用优惠券Fragment为空，则创建一个并添加到界面上
				availableCouponFragment = AvailableCouponFragment.getInstentce();
				transaction.add(R.id.available_past_coupon_layout, availableCouponFragment);
			} else {
				// 如果可用优惠券Fragment不为空，则直接将它显示出来
				transaction.show(availableCouponFragment);
			}
			break;
		case R.id.used_coupon_layout:  //已使用
			isAvailable = false;
			isUsed = true;
			isPast = false;
			//设置不可用的信息
			setAvailableOrPast();
			if(fristUsed){
				// 如果不可用优惠券Fragment为空，则创建一个并添加到界面上
				usedCouponFragment = UsedCouponFragment.getInstentce();
				transaction.add(R.id.available_past_coupon_layout, usedCouponFragment);
				fristUsed = false;
			}else{
				if (usedCouponFragment == null) {
					// 如果不可用优惠券Fragment为空，则创建一个并添加到界面上
					usedCouponFragment = UsedCouponFragment.getInstentce();
					transaction.add(R.id.available_past_coupon_layout, usedCouponFragment);
				} else {
					// 如果MessageFragment不为空，则直接将它显示出来
					transaction.show(usedCouponFragment);
				}
			}
			break;
		case R.id.past_coupon_layout:  //不可用
			isAvailable = false;
			isUsed = false;
			isPast = true;
			//设置不可用的信息
			setAvailableOrPast();
			if(fristPast){
				// 如果不可用优惠券Fragment为空，则创建一个并添加到界面上
				pastCouponFragment = PastCouponFragment.getInstentce();
				transaction.add(R.id.available_past_coupon_layout, pastCouponFragment);
				fristPast = false;
			}else{
				if (pastCouponFragment == null) {
					// 如果不可用优惠券Fragment为空，则创建一个并添加到界面上
					pastCouponFragment = PastCouponFragment.getInstentce();
					transaction.add(R.id.available_past_coupon_layout, pastCouponFragment);
				} else {
					// 如果不可用优惠券Fragment不为空，则直接将它显示出来
					transaction.show(pastCouponFragment);
				}
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}
	
	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (availableCouponFragment != null) {
			transaction.hide(availableCouponFragment);
		}
		if(!fristUsed){
			if(usedCouponFragment != null){
				transaction.hide(usedCouponFragment);
			}
		}
		if(!fristPast){
			if (pastCouponFragment != null) {
				transaction.hide(pastCouponFragment);
			}
		}
	}

	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-14 下午2:30:59
	 * 方法说明:点击可用和不可用
	 */
	private void setAvailableOrPast() {
		if(isAvailable){
			available_text.setTextColor(getResources().getColor(R.color.Blue));
			available_view.setBackgroundColor(getResources().getColor(R.color.Blue));
			used_text.setTextColor(getResources().getColor(R.color.color_65));
			used_view.setBackgroundColor(getResources().getColor(R.color.White));
			past_text.setTextColor(getResources().getColor(R.color.color_65));
			past_view.setBackgroundColor(getResources().getColor(R.color.White));
		}else if(isUsed){
			used_text.setTextColor(getResources().getColor(R.color.Blue));
			used_view.setBackgroundColor(getResources().getColor(R.color.Blue));
			available_text.setTextColor(getResources().getColor(R.color.color_65));
			available_view.setBackgroundColor(getResources().getColor(R.color.White));
			past_text.setTextColor(getResources().getColor(R.color.color_65));
			past_view.setBackgroundColor(getResources().getColor(R.color.White));
		}else if(isPast){
			past_text.setTextColor(getResources().getColor(R.color.Blue));
			past_view.setBackgroundColor(getResources().getColor(R.color.Blue));
			available_text.setTextColor(getResources().getColor(R.color.color_65));
			available_view.setBackgroundColor(getResources().getColor(R.color.White));
			used_text.setTextColor(getResources().getColor(R.color.color_65));
			used_view.setBackgroundColor(getResources().getColor(R.color.White));
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		fristUsed = true;
		fristPast = true;
	}
}
