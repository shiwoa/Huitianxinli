package com.yizhilu.application;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ScrollView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;

/**
 * @author 杨财宾
 * 时间:2015-8-29
 * 类说明:所有fragment的基类
 */
public abstract class BaseFragment extends Fragment implements OnClickListener,OnItemClickListener,OnRefreshListener2<ScrollView>,
					OnGroupClickListener,OnChildClickListener,OnFocusChangeListener{
	private View layoutView;  //返回的当前类的布局
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//获取当前类的布局
		layoutView = getLayoutView(inflater,container,savedInstanceState);
		//初始化控件的方法
		initView();
		//添加点击事件的方法
		addOnClick();
		return layoutView;
	}
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-9-21 上午10:11:54
	 * 方法说明:加载当前类的布局文件的方法
	 */
	public abstract View getLayoutView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState);
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-9-21 上午10:12:05
	 * 方法说明:初始化控件的方法
	 */
	public abstract void initView();
	
	/**
	 * @author bin
	 * 修改人:
	 * 时间:2015-9-21 上午10:12:15
	 * 方法说明:添加点击事件的方法
	 */
	public abstract void addOnClick();
	
	/**
	 * 控件的点击事件
	 */
	@Override
	public void onClick(View v) {
		
	}
	
	/**
	 *	listView的点击事件
	 */
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
	}
	
	/**
	 *	下拉刷新的方法
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		
	}
	
	/**
	 *	上拉加载的方法
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		
	}
	
	/**
	 *	组的点击时间
	 */
	@Override
	public boolean onGroupClick(ExpandableListView parent, View v,
			int groupPosition, long id) {
		return false;
	}
	
	/**
	 *	子的点击事件
	 */
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		return false;
	}
	
	/**
	 * editText获取焦点的监听
	 */
	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		
	}
}
