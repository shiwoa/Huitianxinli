package com.yizhilu.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.application.BaseFragment;
import com.yizhilu.huitianxinli.R;

/**
 * @author bin 修改人: 时间:2016-2-23 上午11:15:30 类说明:会员券
 */
public class MemberFragment extends BaseFragment {
	private static MemberFragment memberFragment; // 本类对象
	private View inflate; // 总布局
	private LinearLayout available_member_layout, past_member_layout; // 可用,不可用
	private TextView available_member_text, past_member_text; // 可用,不可用
	private View available_member_view, past_member_view; // 可用,不可用
	private boolean isAvailable; // 是否可用
	private AvailableMemberFragment availableMemberFragment; // 可用会员券
	private PastMemberFragment pastMemberFragment; // 不可用会员券
	private FragmentManager fragmentManager; // fragment管理

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_member, container, false);
		return inflate;
	}

	/**
	 * 获取本类实例的方法
	 */
	public static MemberFragment getInstance() {
		if (memberFragment == null) {
			memberFragment = new MemberFragment();
		}
		return memberFragment;
	}

	/*
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		available_member_layout = (LinearLayout) inflate
				.findViewById(R.id.available_member_layout); // 可用
		past_member_layout = (LinearLayout) inflate
				.findViewById(R.id.past_member_layout); // 不可用
		available_member_text = (TextView) inflate
				.findViewById(R.id.available_member_text); // 可用
		past_member_text = (TextView) inflate
				.findViewById(R.id.past_member_text); // 不可用
		available_member_view = inflate
				.findViewById(R.id.available_member_view); // 可用
		past_member_view = inflate.findViewById(R.id.past_member_view); // 不可用
		availableMemberFragment = AvailableMemberFragment.getInstance(); // 获取可用会员券的对象
		fragmentManager = getActivity().getSupportFragmentManager(); // 得到fragment的管理器
		getActivity()
				.getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.available_past_member_layout, availableMemberFragment)
				.show(availableMemberFragment).commit();
	}

	/*
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		available_member_layout.setOnClickListener(this); // 可用
		past_member_layout.setOnClickListener(this); // 不可用
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		// 开启一个Fragment的事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		// 隐藏掉所有的fragment
		hideFragments(transaction);
		switch (v.getId()) {
		case R.id.available_member_layout: // 可用
			isAvailable = true;
			//设置可用的信息
			setAvailableOrPast();
			if (availableMemberFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				availableMemberFragment = AvailableMemberFragment.getInstance();
				transaction.add(R.id.available_past_member_layout, availableMemberFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(availableMemberFragment);
			}
			break;
		case R.id.past_member_layout: // 不可用
			isAvailable = false;
			//设置可用的信息
			setAvailableOrPast();
			if (pastMemberFragment == null) {
				// 如果MessageFragment为空，则创建一个并添加到界面上
				pastMemberFragment = PastMemberFragment.getInstance();
				transaction.add(R.id.available_past_member_layout, pastMemberFragment);
			} else {
				// 如果MessageFragment不为空，则直接将它显示出来
				transaction.show(pastMemberFragment);
			}
			break;
		default:
			break;
		}
		transaction.commit();
	}
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2016-1-14 下午2:30:59
	 * 方法说明:点击可用和不可用
	 */
	private void setAvailableOrPast() {
		if(isAvailable){
			available_member_text.setTextColor(getResources().getColor(R.color.Blue));
			available_member_view.setBackgroundColor(getResources().getColor(R.color.Blue));
			past_member_text.setTextColor(getResources().getColor(R.color.color_65));
			past_member_view.setBackgroundColor(getResources().getColor(R.color.White));
		}else{
			available_member_text.setTextColor(getResources().getColor(R.color.color_65));
			available_member_view.setBackgroundColor(getResources().getColor(R.color.White));
			past_member_text.setTextColor(getResources().getColor(R.color.Blue));
			past_member_view.setBackgroundColor(getResources().getColor(R.color.Blue));
		}
	}
	
	/**
	 * 将所有的Fragment都置为隐藏状态。
	 * 
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {
		if (availableMemberFragment != null) {
			transaction.hide(availableMemberFragment);
		}
		if (pastMemberFragment != null) {
			transaction.hide(pastMemberFragment);
		}
	}
}
