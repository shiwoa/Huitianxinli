package com.yizhilu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.CouponAdapter;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.entity.CouponEntity;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

/**
 * @author bin 修改人: 时间:2016-1-16 下午1:03:48 类说明:可用代金券的类
 */
public class UsedVoucherFragment extends BaseFragment {

	private View inflate; // 总布局
	private static UsedVoucherFragment usedVoucherFragment; // 本类的实体
	private PullToRefreshScrollView refreshScrollView; // 刷新和加載的對象
	private NoScrollListView listView; // 可用優惠券的的列表
	private ProgressDialog progressDialog; // 联网获取数据显示的dialog
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private int userId, page = 1;
	private List<CouponEntity> allCouponList; // 优惠券的总集合
	private LinearLayout null_layout; // 空布局
	private PublicEntity publicEntity; // 实体类

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_used_voucher, container,
				false);
		return inflate;
	}

	/*
	 * 初始化控件的方法
	 */
	@Override
	public void initView() {
		userId = getActivity().getSharedPreferences("userId",
				getActivity().MODE_PRIVATE).getInt("userId", 0);
		progressDialog = new ProgressDialog(getActivity()); // 联网获取数据显示的dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的对象
		allCouponList = new ArrayList<CouponEntity>(); // 优惠券总集合
		refreshScrollView = (PullToRefreshScrollView) inflate
				.findViewById(R.id.refreshScrollView); // 刷新的對象
		refreshScrollView.setMode(Mode.BOTH); // 設置加載模式
		listView = (NoScrollListView) inflate.findViewById(R.id.listView); // 可用優惠券的列表
		null_layout = (LinearLayout) inflate.findViewById(R.id.null_layout); // 空布局
		// 联网获取已用代金券的方法
		getUsedCouponData(userId, page);
	}

	/**
	 * @author bin 修改人: 时间:2016-4-1 上午9:12:33 方法说明:设置可以代金券
	 */
	private void setVoucherData(final int page, PublicEntity publicEntity) {
		if (publicEntity.isSuccess()) {
			EntityPublic entityPublic = publicEntity.getEntity();
			if (page >= entityPublic.getPage().getTotalPageSize()) {
				refreshScrollView.setMode(Mode.PULL_FROM_START);
			}
			List<CouponEntity> couponList = entityPublic.getCouponList();
			if (couponList == null || couponList.size() <= 0) {
				null_layout.setVisibility(View.VISIBLE);
				refreshScrollView.setVisibility(View.GONE);
			} else {
				null_layout.setVisibility(View.GONE);
				refreshScrollView.setVisibility(View.VISIBLE);
				for (int i = 0; i < couponList.size(); i++) {
					CouponEntity couponEntity = couponList.get(i);
					if (couponEntity.getStatus() == 2) {
						allCouponList.add(couponEntity);
					}
				}
			}
			if (allCouponList.isEmpty()) {
				null_layout.setVisibility(View.VISIBLE);
				refreshScrollView.setVisibility(View.GONE);
			}
			listView.setAdapter(new CouponAdapter(getActivity(), allCouponList));
		}
		refreshScrollView.onRefreshComplete();
	}

	/**
	 * @author bin 修改人: 时间:2016-1-14 下午5:51:42 方法说明:联网获取已用优惠券的方法
	 */
	private void getUsedCouponData(int userId, final int page) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("page.currentPage", page);
		params.put("type", 2); // 2为代金券
		Log.i("lala", Address.GET_USER_COUPON + "?" + params.toString());
		httpClient.post(Address.GET_USER_COUPON, params,
				new TextHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						HttpUtils.showProgressDialog(progressDialog);
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String data) {
						HttpUtils.exitProgressDialog(progressDialog);
						if (!TextUtils.isEmpty(data)) {
							try {
								PublicEntity publicEntity = JSON.parseObject(
										data, PublicEntity.class);
								String message = publicEntity.getMessage();
								// 设置可用代金券
								setVoucherData(page, publicEntity);
							} catch (Exception e) {
								refreshScrollView.onRefreshComplete();
								null_layout.setVisibility(View.VISIBLE);
								refreshScrollView.setVisibility(View.GONE);
							}
						}
					}

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						HttpUtils.exitProgressDialog(progressDialog);
						refreshScrollView.onRefreshComplete();
					}
				});
	}

	/**
	 * @author bin 修改人: 时间:2016-1-14 下午3:15:53 方法说明:获取本类对象
	 */
	public static UsedVoucherFragment getInstentce() {
		if (usedVoucherFragment == null) {
			usedVoucherFragment = new UsedVoucherFragment();
		}
		return usedVoucherFragment;
	}

	/*
	 * 添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		refreshScrollView.setOnRefreshListener(this);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		page = 1;
		allCouponList.clear();
		// 联网获取可用优惠券的方法
		getUsedCouponData(userId, page);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullUpToRefresh(refreshView);
		page++;
		// 联网获取可用优惠券的方法
		getUsedCouponData(userId, page);
	}
}
