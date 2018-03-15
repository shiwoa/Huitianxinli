package com.yizhilu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.adapter.HotGroupListAdapter;
import com.yizhilu.adapter.HotGroupListAdapter.OnItemClickLitener;
import com.yizhilu.adapter.NewesDynamicAdapter;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.InformationDetailsActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class NewArticleFragment extends BaseFragment {
	public static NewArticleFragment newArticleFragment;
	private View inflate; // 文章的总布局
	private int currentPage = 1, type = 0; // 当前页数，全部
	private AsyncHttpClient httpClient; // 联网获取数据的方法
	private ProgressDialog progressDialog; // 获取数据显示dialog
	private List<EntityPublic> title_list;// title的集合
	private List<EntityCourse> informationList; // 文章的实体
	private String informationTitle;// 选中的标题
	private RecyclerView title_layout;// 导航栏
	private int informationIndex = 1;
	private HotGroupListAdapter adapter;
	private PullToRefreshScrollView refreshScrollView; // 上拉,下拉的布局
	private NoScrollListView newestDynamicList; // 最新动态文章的列表
	private LinearLayout show_background;

	@Override
	public View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		inflate = inflater.inflate(R.layout.fragment_article_new, container,false);
		return inflate;
	}

	@Override
	public void initView() {
		newArticleFragment = this;
		title_list = new ArrayList<EntityPublic>();
		informationList = new ArrayList<EntityCourse>(); // 文章的实体
		progressDialog = new ProgressDialog(getActivity()); // 获取数据显示dialog
		httpClient = new AsyncHttpClient(); // 联网获取数据的方法
		title_layout = (RecyclerView) inflate.findViewById(R.id.horizontal_list);
		title_layout.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
		newestDynamicList = (NoScrollListView) inflate.findViewById(R.id.newestDynamicList);
		refreshScrollView = (PullToRefreshScrollView) inflate.findViewById(R.id.refreshScrollView);
		show_background = (LinearLayout) inflate.findViewById(R.id.show_background);
		getInformationList2(informationIndex, type);
	}

	@Override
	public void addOnClick() {
		refreshScrollView.setOnRefreshListener(this);
		newestDynamicList.setOnItemClickListener(this);

	}

	/**
	 * 获取本类对象
	 */
	public static NewArticleFragment getInstance() {
		if (newArticleFragment == null) {
			newArticleFragment = new NewArticleFragment();
		}
		return newArticleFragment;
	}

	/**
	 * @author ming 修改人: 时间:2015年10月13日 上午9:59:00 类说明:获取资讯列表的方法
	 */
	private void getInformationList2(final int currentPage, int type1) {
		RequestParams params = new RequestParams();
		params.put("page.currentPage", currentPage);
		params.put("type", type1);
		Log.i("wulala", Address.INFORMATION_LIST + "?" + params.toString());
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
						if (publicEntity.isSuccess()) {
							if (currentPage >= publicEntity.getEntity().getPage().getTotalPageSize()) {
								refreshScrollView.setMode(Mode.PULL_FROM_START);
							} else {
								refreshScrollView.setMode(Mode.BOTH);
							}
							List<EntityPublic> articleTypeList = publicEntity.getEntity().getArticleTypeList();
							title_list.clear();
							EntityPublic entity = new EntityPublic();
							entity.setName("全部");
							entity.setId(0);
							title_list.add(entity);
							if (articleTypeList != null && articleTypeList.size() > 0) {
								for (int i = 0; i < articleTypeList.size(); i++) {
									title_list.add(articleTypeList.get(i));
								}
							}
							if (adapter == null) {
								adapter = new HotGroupListAdapter(title_list, getActivity());
								title_layout.setAdapter(adapter);
								adapter.setOnItemClickLitener(new OnItemClickLitener() {

									@Override
									public void onItemClick(View view, int position) {
										Log.i("lala", "position=" + position);
										adapter.setPosition(position);
										adapter.notifyDataSetChanged();
										type = title_list.get(position).getId();
										informationTitle = title_list.get(position).getName();
										// 更新数据
										informationList.clear();
										informationIndex = 1;
										// 联网获取最新动态的文章的方法
										getInformationList2(informationIndex, type);
									}
								});
							} else {
								adapter.notifyDataSetChanged();
							}
							List<EntityCourse> articleList = publicEntity.getEntity().getArticleList();
							if (articleList != null && articleList.size() > 0) {
								refreshScrollView.setVisibility(View.VISIBLE);
								show_background.setVisibility(View.GONE);
								for (int i = 0; i < articleList.size(); i++) {
									informationList.add(articleList.get(i));
								}
							} else {
								show_background.setVisibility(View.VISIBLE);
								refreshScrollView.setVisibility(View.GONE);
							}
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
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullDownToRefresh(refreshView);
		informationIndex = 1;
		informationList.clear();
		getInformationList2(informationIndex, type);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		super.onPullUpToRefresh(refreshView);
		informationIndex++;
		getInformationList2(informationIndex, type);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onItemClick(arg0, arg1, arg2, arg3);
		Intent intent = new Intent();
		EntityCourse entityCourse = informationList.get(arg2);
		intent.setClass(getActivity(), InformationDetailsActivity.class);
		if (informationTitle == null || informationTitle.equals("")) {
			informationTitle = "全部资讯";
		}
		intent.putExtra("informationTitle", informationTitle);
		intent.putExtra("entity", entityCourse);
		startActivity(intent);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		newArticleFragment  = null;
	}
}
