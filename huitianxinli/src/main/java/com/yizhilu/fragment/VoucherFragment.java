package com.yizhilu.fragment;

import org.apache.http.Header;

import android.app.ProgressDialog;
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
import com.yizhilu.utils.HttpUtils;

/**
 * @author bin
 * 修改人:
 * 时间:2016-1-14 上午11:56:29
 * 类说明:代金券的类
 */
public class VoucherFragment extends BaseFragment {
	private static VoucherFragment voucherFragment;  //本类对象
	private View inflate;  //总布局
	private LinearLayout available_voucher_layout,past_voucher_layout,used_voucher_layout;  //可用,不可用,已使用
	private TextView available_voucher_text,past_voucher_text,used_text;  //可用,不可用,已使用
	private View available_voucher_view,past_voucher_view,used_view;  //可用,不可用,已使用
	private boolean isAvailable,isUsed,isPast;  //是否可用
	private AvailableVoucherFragment availableVoucherFragment;  //可用代金券
	private UsedVoucherFragment usedVoucherFragment;  //已使用代金券
	private PastVoucherFragment pastVoucherFragment;  //不可用代金券
	private FragmentManager fragmentManager;  //fragment管理
	private AsyncHttpClient httpClient;  //联网获取数据的对象
	private ProgressDialog progressDialog;  //联网获取数据显示的dialog
	private int userId,page = 1;  //用户Id,页数
	private boolean fristUsed = true,fristPast = true;
	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_voucher, container,false);
		return inflate;
	}

	/* 
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		httpClient = new AsyncHttpClient();  //联网获取数据的对象
		progressDialog  = new ProgressDialog(getActivity());  //联网获取数据显示的dialog
		userId = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).getInt("userId", 0);
		available_voucher_layout = (LinearLayout) inflate.findViewById(R.id.available_voucher_layout);  //可用
		past_voucher_layout = (LinearLayout) inflate.findViewById(R.id.past_voucher_layout);  //不可用
		used_voucher_layout = (LinearLayout) inflate.findViewById(R.id.used_voucher_layout);  //已使用
		available_voucher_text = (TextView) inflate.findViewById(R.id.available_voucher_text);
		used_text = (TextView) inflate.findViewById(R.id.used_text);  //已使用
		used_view = inflate.findViewById(R.id.used_view);  //已使用
		past_voucher_text = (TextView) inflate.findViewById(R.id.past_voucher_text);
		available_voucher_view = inflate.findViewById(R.id.available_voucher_view);
		past_voucher_view = inflate.findViewById(R.id.past_voucher_view);
		//获取代金券的数据
		getCouponData(userId,page);
		fragmentManager = getActivity().getSupportFragmentManager(); // 得到fragment的管理器
		availableVoucherFragment = AvailableVoucherFragment.getInstentce();
		fragmentManager.beginTransaction()
				.add(R.id.available_past_voucher_layout, availableVoucherFragment)
				.show(availableVoucherFragment).commit();
	}
	
	/**
	 * 获取本类实例的方法
	 */
	public static VoucherFragment getInstance(){
		if(voucherFragment == null){
			voucherFragment = new VoucherFragment();
		}
		return voucherFragment;
	}

	/* 
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		available_voucher_layout.setOnClickListener(this);  //可用
		past_voucher_layout.setOnClickListener(this);  //不可用
		used_voucher_layout.setOnClickListener(this);  //已使用
	}
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-14 下午5:51:42
	 * 方法说明:联网获取优惠券的方法
	 */
	private void getCouponData(int userId, final int page) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("page.currentPage", page);
		params.put("type", 2);  //2为代金券
		Log.i("lala", Address.GET_USER_COUPON+"?"+params.toString());
		httpClient.post(Address.GET_USER_COUPON, params , new TextHttpResponseHandler() {
			@Override
			public void onStart() {
				super.onStart();
//				HttpUtils.showProgressDialog(progressDialog);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String data) {
				HttpUtils.exitProgressDialog(progressDialog);
				if(!TextUtils.isEmpty(data)){
					try {
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						String message = publicEntity.getMessage();
						if(publicEntity.isSuccess()){
							EntityPublic entityPublic = publicEntity.getEntity();
							available_voucher_text.setText("可使用("+entityPublic.getCount_1()+")");  //可使用
							used_text.setText("已使用("+entityPublic.getCount_2()+")"); //已使用
							int pastCount = entityPublic.getCount_3()+entityPublic.getCount_4();
							past_voucher_text.setText("已过期("+pastCount+")");  //已过期
						}
					} catch (Exception e) {
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
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
		case R.id.available_voucher_layout:  //可用
			isAvailable = true;
			isUsed = false;
			isPast = false;
			//设置可用的信息
			setAvailableOrPast();
			if (availableVoucherFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				availableVoucherFragment = AvailableVoucherFragment.getInstentce();
				transaction.add(R.id.available_past_voucher_layout, availableVoucherFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(availableVoucherFragment);
			}
			break;
		case R.id.used_voucher_layout:  //已使用
			isAvailable = false;
			isUsed = true;
			isPast = false;
			//设置可用的信息
			setAvailableOrPast();
			if(fristUsed){
				// 如果MessageFragment为空，则创建一个并添加到界面上
				usedVoucherFragment = UsedVoucherFragment.getInstentce();
				transaction.add(R.id.available_past_voucher_layout, usedVoucherFragment);
				fristUsed = false;
			}else{
				if (usedVoucherFragment == null) {
					// 如果MessageFragment为空，则创建一个并添加到界面上
					usedVoucherFragment = UsedVoucherFragment.getInstentce();
					transaction.add(R.id.available_past_voucher_layout, usedVoucherFragment);
				} else {
					// 如果MessageFragment不为空，则直接将它显示出来
					transaction.show(usedVoucherFragment);
				}
			}
			break;
		case R.id.past_voucher_layout:  //不可用
			isAvailable = false;
			isUsed = false;
			isPast = true;
			//设置可用的信息
			setAvailableOrPast();
			if(fristPast){
				// 如果MessageFragment为空，则创建一个并添加到界面上
				pastVoucherFragment = PastVoucherFragment.getInstentce();
				transaction.add(R.id.available_past_voucher_layout, pastVoucherFragment);
				fristPast = false;
			}else{
				if (pastVoucherFragment == null) {
					// 如果MessageFragment为空，则创建一个并添加到界面上
					pastVoucherFragment = PastVoucherFragment.getInstentce();
					transaction.add(R.id.available_past_voucher_layout, pastVoucherFragment);
				} else {
					// 如果MessageFragment不为空，则直接将它显示出来
					transaction.show(pastVoucherFragment);
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
		if (availableVoucherFragment != null) {
			transaction.hide(availableVoucherFragment);
		}
		if(!fristUsed){
			if (usedVoucherFragment != null) {
				transaction.hide(usedVoucherFragment);
			}
		}
		if(!fristPast){
			if (pastVoucherFragment != null) {
				transaction.hide(pastVoucherFragment);
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
		if(isAvailable){  //可使用
			available_voucher_text.setTextColor(getResources().getColor(R.color.Blue));
			available_voucher_view.setBackgroundColor(getResources().getColor(R.color.Blue));
			used_text.setTextColor(getResources().getColor(R.color.color_65));
			used_view.setBackgroundColor(getResources().getColor(R.color.White));
			past_voucher_text.setTextColor(getResources().getColor(R.color.color_65));
			past_voucher_view.setBackgroundColor(getResources().getColor(R.color.White));
		}else if(isUsed){  //已使用
			available_voucher_text.setTextColor(getResources().getColor(R.color.color_65));
			available_voucher_view.setBackgroundColor(getResources().getColor(R.color.White));
			used_text.setTextColor(getResources().getColor(R.color.Blue));
			used_view.setBackgroundColor(getResources().getColor(R.color.Blue));
			past_voucher_text.setTextColor(getResources().getColor(R.color.color_65));
			past_voucher_view.setBackgroundColor(getResources().getColor(R.color.White));
		}else if(isPast){  //已过期
			available_voucher_text.setTextColor(getResources().getColor(R.color.color_65));
			available_voucher_view.setBackgroundColor(getResources().getColor(R.color.White));
			used_text.setTextColor(getResources().getColor(R.color.color_65));
			used_view.setBackgroundColor(getResources().getColor(R.color.White));
			past_voucher_text.setTextColor(getResources().getColor(R.color.Blue));
			past_voucher_view.setBackgroundColor(getResources().getColor(R.color.Blue));
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		fristUsed = true;
		fristPast = true;
	}
}
