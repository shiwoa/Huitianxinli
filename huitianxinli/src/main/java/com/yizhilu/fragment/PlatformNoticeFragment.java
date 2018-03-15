package com.yizhilu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.NewesDynamicAdapter;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.InformationDetailsActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

/**
 * @author bin
 * 修改人:
 * 时间:2015-10-15 下午2:47:35
 * 类说明:平台公告的类
 */
public class PlatformNoticeFragment extends BaseFragment {
	private static PlatformNoticeFragment platformNoticeFragment;  //本类对象
	private View inflate;  //平台公告总的布局
	private PullToRefreshScrollView refreshScrollView;  //上拉,下拉的布局
	private NoScrollListView newestDynamicList;  //行業資訊文章的列表
	private ProgressDialog progressDialog;  //获取数据显示dialog
	private AsyncHttpClient httpClient;  //联网获取数据的方法
	private int informationIndex = 1,type = 2;  //当前页数,公告为1
	private List<EntityCourse> informationList;  //文章的实体
	private Intent intent;
	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_newest_dynamic, container,false);
		return inflate;
	}
	/**
	 * 获取本类实例的方法
	 */
	public static PlatformNoticeFragment getInstance(){
		if(platformNoticeFragment == null){
			platformNoticeFragment = new PlatformNoticeFragment();
		}
		return platformNoticeFragment;
	}
	/**
	 *	初始化控件的方法
	 */
	@Override
	public void initView() {
		progressDialog = new ProgressDialog(getActivity());  //获取数据显示dialog
		httpClient = new AsyncHttpClient();  //联网获取数据的方法
		intent = new Intent();
		informationList = new ArrayList<EntityCourse>();  //文章的实体
		refreshScrollView = (PullToRefreshScrollView) inflate.findViewById(R.id.refreshScrollView);  //上拉,下拉的布局
		refreshScrollView.setMode(Mode.BOTH); //设置加载模式
		newestDynamicList  = (NoScrollListView) inflate.findViewById(R.id.newestDynamicList);  //最新动态文章的列表
		//聯網獲取行業資訊文章的方法
		getInformationList(informationIndex, type);
	}

	/**
	 *	添加点击事件的方法
	 */
	@Override
	public void addOnClick() {
		refreshScrollView.setOnRefreshListener(this);  //上拉加载和下拉刷新的事件
		newestDynamicList.setOnItemClickListener(this);
	}
	
	/**
	 * @author ming
	 * 修改人:
	 * 时间:2015年10月13日 上午9:59:00
	 * 类说明:获取资讯列表的方法
	 */
	private void getInformationList(final int currentPage,int type){
		RequestParams params=new RequestParams();
		params.put("page.currentPage", currentPage);
		params.put("type", type);
		httpClient.post(Address.INFORMATION_LIST, params, new TextHttpResponseHandler() {
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
						PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
						if(publicEntity.isSuccess()){
							if(currentPage >= publicEntity.getEntity().getPage().getTotalPageSize()){
								refreshScrollView.setMode(Mode.PULL_FROM_START);
							}else{
								refreshScrollView.setMode(Mode.BOTH);
							}
							List<EntityCourse> articleList = publicEntity.getEntity().getArticleList();
							if(articleList!=null&&articleList.size()>0){
								for(int i=0;i<articleList.size();i++){
									informationList.add(articleList.get(i));
								}
							}
							Log.i("lala", informationList.size()+".....");
							newestDynamicList.setAdapter(new NewesDynamicAdapter(getActivity(), informationList));
							refreshScrollView.onRefreshComplete();
						}
					} catch (Exception e) {
						refreshScrollView.onRefreshComplete();
					}
				}
			}
			
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				HttpUtils.exitProgressDialog(progressDialog);
				refreshScrollView.onRefreshComplete();
			}
		});
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		super.onItemClick(parent, view, position, id);
		EntityCourse entityCourse = informationList.get(position);
//		int informationId = informationList.get(position).getId();
		intent.setClass(getActivity(), InformationDetailsActivity.class);
//		intent.putExtra("informationId", informationId);
		intent.putExtra("informationTitle", "平台公告");
		intent.putExtra("entity", entityCourse);
		startActivity(intent);
	}

	/* 
	 * 下拉刷新的方法
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		informationIndex = 1;
		informationList.clear();
		getInformationList(informationIndex, type);
	}
	
	/* 
	 * 上拉加载的方法
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullUpToRefresh(refreshView);
		informationIndex++;
		getInformationList(informationIndex, type);
	}
}
