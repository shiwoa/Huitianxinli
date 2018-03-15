package com.yizhilu.community.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.community.GroupDetailActivity;
import com.yizhilu.community.SearchActivityCommuntiy;
import com.yizhilu.community.adapter.FindListAdapter;
import com.yizhilu.community.utils.Address;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PageEntity;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;

//社区-发现
public class FindFragment extends BaseGroupFragment implements OnItemClickListener, OnClickListener {

	private boolean isInited;// 组件是否初始化
	private boolean isLoaded;// 是否加载过
	private View mRootView;
	private PullToRefreshListView find_list;// 发现列表
	private AsyncHttpClient httpClient;// 网络连接
	private int currentPage = 1;// 当前页
	private FindListAdapter adapter;// 发现列表适配器
	private List<EntityPublic> groupList;// 发现小组集合
	private ImageLoader imageLoader;
	private int userId;// 用户Id
	private static int position, type;
	private ImageView course_search; // 小组搜索

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (mRootView == null) {
			mRootView = inflater.inflate(R.layout.fragment_find, container, false);
			initView();// 初始化组件
			isInited = true;
			Load();// 加载数据
		}
		return mRootView;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isLoaded) {
			Log.i("xm", "onResume");
			if (groupList.size() != 0) {
				groupList.get(position).setWhetherTheMembers(type);
				adapter.notifyDataSetChanged();
			}
		}
	}

	public static void setType(int index, int Type) {
		position = index;
		type = Type;
	}

	// 加载数据
	@Override
	protected void Load() {
		if (!isInited || !isVisible || isLoaded) {
			return;
		}
		getFindList(currentPage, userId);// 获取发现列表
		isLoaded = true;
	}

	// 初始化组件
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initView() {
		userId = getActivity().getSharedPreferences("userId", getActivity().MODE_PRIVATE).getInt("userId", 0);
		find_list = (PullToRefreshListView) mRootView.findViewById(R.id.find_list);
		find_list.setMode(Mode.BOTH);
		course_search = (ImageView) mRootView.findViewById(R.id.course_search); // 小组搜索
		course_search.setOnClickListener(this); // 小组搜索
		httpClient = new AsyncHttpClient();
		groupList = new ArrayList<EntityPublic>();
		imageLoader = ImageLoader.getInstance();
		find_list.setOnItemClickListener(this);
		find_list.setOnRefreshListener(new OnRefreshListener2() {

			@Override
			public void onPullDownToRefresh(PullToRefreshBase refreshView) {
				find_list.setMode(Mode.BOTH);
				groupList.clear();
				currentPage = 1;
				getFindList(currentPage, userId);
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase refreshView) {
				currentPage++;
				getFindList(currentPage, userId);
			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent intent = new Intent(getActivity(), GroupDetailActivity.class);
		intent.putExtra("GroupId", groupList.get(arg2 - 1).getId());
		intent.putExtra("position", arg2 - 1);
		startActivity(intent);
	}

	// 获取发现列表
	private void getFindList(final int currentPage, final int userId) {
		RequestParams params = new RequestParams();
		params.put("page.currentPage", currentPage);
		params.put("userId", userId);
		Log.i("xm", Address.FINDLIST + "?" + params);
		httpClient.post(Address.FINDLIST, params, new TextHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				find_list.onRefreshComplete();
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							PageEntity pageEntity = publicEntity.getEntity().getPage();
							if (pageEntity.getTotalPageSize() <= currentPage) {
								find_list.setMode(Mode.PULL_FROM_START);
							}
							List<EntityPublic> tempGroupList = publicEntity.getEntity().getGroupList();
							if (tempGroupList != null && tempGroupList.size() > 0) {
								for (int i = 0; i < tempGroupList.size(); i++) {
									groupList.add(tempGroupList.get(i));
								}
								if (adapter == null) {
									adapter = new FindListAdapter(groupList, getActivity(), userId);
									find_list.setAdapter(adapter);
								} else {
									adapter.notifyDataSetChanged();
								}
							}
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
				find_list.onRefreshComplete();
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.course_search: // 小组搜索
			Intent intent = new Intent();
			intent.setClass(getActivity(), SearchActivityCommuntiy.class);
			intent.putExtra("group", true);
			getActivity().startActivity(intent);
			break;

		default:
			break;
		}
	}
}
