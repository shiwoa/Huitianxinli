package com.yizhilu.huitianxinli;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yizhilu.application.BaseActivity;
import com.yizhilu.fragment.CouponFragment;
import com.yizhilu.fragment.MemberFragment;
import com.yizhilu.fragment.VoucherFragment;

/**
 * @author bin 修改人: 时间:2016-1-13 下午3:51:26 类说明:优惠券的类
 */
public class DiscountCouponActivity extends BaseActivity {
	private RelativeLayout coupon_layout, voucher_layout, member_layout; // 优惠券,代金券,会员券
	private LinearLayout back_layout; // 返回
	private TextView coupon_text, voucher_text, member_text; // 优惠券,代金券,会员券
	private View voucher_view, coupon_view, member_view; // 代金券,优惠券，会员券的线
	private boolean isCoupon = true, isVoucher = false, isMember = false; // 点击的是优惠券
	private CouponFragment couponFragment; // 优惠券的类
	private VoucherFragment voucherFragment; // 代金券的类
	private MemberFragment memberFragment; // 会员券的类
	private FragmentManager fragmentManager; // fragment管理类

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_discount_coupon);
		super.onCreate(savedInstanceState);
	}

	/*
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		back_layout = (LinearLayout) findViewById(R.id.back_layout); // 返回
		coupon_layout = (RelativeLayout) findViewById(R.id.coupon_layout); // 优惠券
		voucher_layout = (RelativeLayout) findViewById(R.id.voucher_layout); // 代金券
		member_layout = (RelativeLayout) findViewById(R.id.member_layout); // 会员券的布局
		coupon_text = (TextView) findViewById(R.id.coupon_text); // 优惠券
		voucher_text = (TextView) findViewById(R.id.voucher_text); // 代金券
		member_text = (TextView) findViewById(R.id.member_text); // 会员券
		voucher_view = findViewById(R.id.voucher_view); // 代金券的线
		coupon_view = findViewById(R.id.coupon_view); // 优惠券的线
		member_view = findViewById(R.id.member_view); // 会员券的线
//		voucherFragment = VoucherFragment.getInstance();
		couponFragment = CouponFragment.getInstance();
		fragmentManager = getSupportFragmentManager(); // 得到fragment的管理器
		fragmentManager.beginTransaction()
				.add(R.id.fragment_layout, couponFragment)
				.show(couponFragment).commit();
	}

	/*
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		}); // 返回
		coupon_layout.setOnClickListener(this); // 优惠券
		voucher_layout.setOnClickListener(this); // 代金券
		// member_layout.setOnClickListener(this); //会员券
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		// 开启一个Fragment的事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 隐藏掉所有的fragment
		hideFragments(transaction);
		switch (v.getId()) {
		case R.id.coupon_layout: // 代金券
			isCoupon = true;
			isVoucher = false;
			isMember = false;
			// 设置优惠券和代金券
			setCouponOrVoucher();
			
			if (voucherFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				voucherFragment = VoucherFragment.getInstance();
				transaction.add(R.id.fragment_layout, voucherFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(voucherFragment);
			}
			break;
		case R.id.voucher_layout: // 优惠券
			isVoucher = true;
			isCoupon = false;
			isMember = false;
			// 设置优惠券和代金券
			setCouponOrVoucher();
			if (couponFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				couponFragment = CouponFragment.getInstance();
				transaction.add(R.id.fragment_layout, couponFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(couponFragment);
			}
			break;
		case R.id.member_layout: // 会员券
			isMember = true;
			isVoucher = false;
			isCoupon = false;
			// 设置优惠券和代金券和会员券
			setCouponOrVoucher();
			if (memberFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				memberFragment = MemberFragment.getInstance();
				transaction.add(R.id.fragment_layout, memberFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(memberFragment);
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
		if (couponFragment != null) {
			transaction.hide(couponFragment);
		}
		if (voucherFragment != null) {
			transaction.hide(voucherFragment);
		}
		if (memberFragment != null) {
			transaction.hide(memberFragment);
		}
	}

	/**
	 * @author bin 修改人: 时间:2016-1-14 上午11:43:30 方法说明:设置优惠券和代金券
	 */
	private void setCouponOrVoucher() {
		if (isVoucher) { // 代金券
			voucher_layout.setBackgroundResource(R.drawable.left_yes);
			coupon_layout.setBackgroundResource(R.drawable.right_no);
			voucher_text.setTextColor(getResources().getColor(R.color.Blue));
			coupon_text.setTextColor(getResources().getColor(R.color.White));
			// coupon_view.setBackgroundColor(getResources().getColor(R.color.White));
			// voucher_view.setBackgroundColor(getResources().getColor(R.color.Blue));
			// member_view.setBackgroundColor(getResources().getColor(R.color.Blue));
		} else if (isCoupon) { // 优惠券
			coupon_layout.setBackgroundResource(R.drawable.right_yes);
			voucher_layout.setBackgroundResource(R.drawable.left_no);
			coupon_text.setTextColor(getResources().getColor(R.color.Blue));
			voucher_text.setTextColor(getResources().getColor(R.color.White));
			// coupon_view.setBackgroundColor(getResources().getColor(R.color.Blue));
			// voucher_view.setBackgroundColor(getResources().getColor(R.color.White));
			// member_view.setBackgroundColor(getResources().getColor(R.color.Blue));
		}
		// else if(isMember){
		// coupon_view.setBackgroundColor(getResources().getColor(R.color.Blue));
		// voucher_view.setBackgroundColor(getResources().getColor(R.color.Blue));
		// member_view.setBackgroundColor(getResources().getColor(R.color.White));
		// }
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
