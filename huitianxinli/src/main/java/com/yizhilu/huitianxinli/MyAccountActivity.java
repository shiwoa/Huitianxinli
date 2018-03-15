package com.yizhilu.huitianxinli;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.MyAccountAdapter;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.entity.EntityAccList;
import com.yizhilu.entity.EntityUserAccount;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

/**
 * @author ming 修改人: 时间:2015年10月20日 上午9:40:36 类说明:我的账户类
 */
public class MyAccountActivity extends BaseActivity {

	private PullToRefreshScrollView refreshScrollView;
	private NoScrollListView nolistview;
	private AsyncHttpClient httpClient; // 联网获取数据的对象
	private ProgressDialog progressDialog; // 加载数据显示的dialog
	private List<EntityAccList> accounList;// 账户List
	private List<EntityAccList> myAccLists;
	private TextView tv_accountcash;
	private TextView titleTextView; //标题
	private LinearLayout backLayout,defaultimg; //返回 默认图片
	private int userId,currentPage = 1;  //用户Id,当前页数
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_myaccount);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);  //得到用户Id
		progressDialog = new ProgressDialog(this);
		httpClient = new AsyncHttpClient();
		myAccLists = new ArrayList<EntityAccList>();
		backLayout = (LinearLayout) findViewById(R.id.back_layout); //返回
		refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pulltorefreshscrollview);
		nolistview = (NoScrollListView) findViewById(R.id.listview);
		tv_accountcash = (TextView) findViewById(R.id.tv_accountcash);
		titleTextView = (TextView) findViewById(R.id.title_text); //标题
		titleTextView.setText("个人账户");
		defaultimg = (LinearLayout) findViewById(R.id.show_img); //默认图片
		//获取账户信息
		getUser_Message(userId, currentPage);
	}

	@Override
	public void addOnClick() {
		backLayout.setOnClickListener(this); //返回
		refreshScrollView.setOnRefreshListener(this);
	}

	/**
	 * 方法说明:获取账户信息
	 */
	private void getUser_Message(final int userId, final int currentPage) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		params.put("page.currentPage", currentPage);
		httpClient.post(Address.USER_MESSAGE, params,
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
							PublicEntity publicEntity = JSON.parseObject(data,
									PublicEntity.class);
							if (publicEntity.isSuccess()) {
								PageEntity page = publicEntity.getEntity()
										.getPage();
								if (page.getCurrentPage() <= currentPage) {
									refreshScrollView
											.setMode(Mode.PULL_FROM_START);
								}
								EntityUserAccount cashAccount = publicEntity
										.getEntity().getUserAccount();
								tv_accountcash.setText(cashAccount.getBalance()+"");
								accounList = publicEntity.getEntity()
										.getAccList();
								if (accounList != null && accounList.size() > 0) {
									for (int i = 0; i < accounList.size(); i++) {
										myAccLists.add(accounList.get(i));
									}
									nolistview.setAdapter(new MyAccountAdapter(
											MyAccountActivity.this, myAccLists));
								}else {
									refreshScrollView.setVisibility(View.GONE);
									defaultimg.setVisibility(View.VISIBLE);
								}
								refreshScrollView.onRefreshComplete();
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
	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout: //返回
				finish();
			break;
		default:
			break;
		}
	}

	/* 
	 * 下拉刷新的方法
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		currentPage = 1;
		myAccLists.clear();
		//获取账户信息
		getUser_Message(userId, currentPage);
	}
	
	/* 
	 * 上拉加載的方法
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullUpToRefresh(refreshView);
		currentPage++;
		//获取账户信息
		getUser_Message(userId, currentPage);
	}
}
