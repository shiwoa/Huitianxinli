package com.yizhilu.community;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.community.adapter.MyGroupAdapter;
import com.yizhilu.community.utils.Address;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.NoScrollListView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

//社区-我的小组
public class MyGroupActivity extends BaseActivity {

	private LinearLayout back_layout;// back...
	private TextView title_text;// title...
	private AsyncHttpClient httpClient;// 网络连接
	private ProgressDialog progressDialog;// loading...
	private int userId;// 用户Id
	private NoScrollListView my_create_list, my_join_list, my_manage_list;// 我创建的小组列表，我加入的小组列表，我管理的小组列表
	private List<EntityPublic> groupList, joinGroupList, manageGroupList;// 我创建的小组集合，我加入的小组集合，我管理的小组集合
	private LinearLayout no_create, no_join, no_manage;// 没有创建的小组，没有加入的小组，没有管理的小组

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_my_group);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
		back_layout = (LinearLayout) findViewById(R.id.back_layout);
		title_text = (TextView) findViewById(R.id.title_text);
		my_create_list = (NoScrollListView) findViewById(R.id.my_create_list);
		my_join_list = (NoScrollListView) findViewById(R.id.my_join_list);
		my_manage_list = (NoScrollListView) findViewById(R.id.my_manage_list);
		no_create = (LinearLayout) findViewById(R.id.no_create);
		no_join = (LinearLayout) findViewById(R.id.no_join);
		no_manage = (LinearLayout) findViewById(R.id.no_manage);
		title_text.setText("我的小组");// 设置标题
		httpClient = new AsyncHttpClient();
		progressDialog = new ProgressDialog(this);
		joinGroupList = new ArrayList<EntityPublic>();
		manageGroupList = new ArrayList<EntityPublic>();
		groupList = new ArrayList<EntityPublic>();
		getMyGroup(userId);// 获取我的小组
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);
		my_create_list.setOnItemClickListener(this);
		my_join_list.setOnItemClickListener(this);
		my_manage_list.setOnItemClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:
			MyGroupActivity.this.finish();
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		super.onItemClick(arg0, arg1, arg2, arg3);
		Intent intent = new Intent();
		switch (arg0.getId()) {
		case R.id.my_create_list:
			intent.setClass(MyGroupActivity.this, GroupDetailActivity.class);
			intent.putExtra("GroupId", groupList.get(arg2).getId());
			startActivity(intent);
			break;
		case R.id.my_join_list:
			intent.setClass(MyGroupActivity.this, GroupDetailActivity.class);
			intent.putExtra("GroupId", joinGroupList.get(arg2).getGroupId());
			startActivity(intent);
			break;
		case R.id.my_manage_list:
			intent.setClass(MyGroupActivity.this, GroupDetailActivity.class);
			intent.putExtra("GroupId", manageGroupList.get(arg2).getGroupId());
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	// 获取我的小组
	private void getMyGroup(int userId) {
		RequestParams params = new RequestParams();
		params.put("userId", userId);
		Log.i("xm", Address.MYGROUP + "?" + params);
		httpClient.post(Address.MYGROUP, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
				HttpUtils.showProgressDialog(progressDialog);
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				HttpUtils.exitProgressDialog(progressDialog);
				if (!TextUtils.isEmpty(arg2)) {
					try {
						PublicEntity publicEntity = JSON.parseObject(arg2, PublicEntity.class);
						if (publicEntity.isSuccess()) {
							List<EntityPublic> tempGroupList = publicEntity.getEntity().getGroupList();
							List<EntityPublic> tempJoinGroupList = publicEntity.getEntity().getJoinGroupList();
							List<EntityPublic> tempManageGroupList = publicEntity.getEntity().getManageGroupList();
							if (tempGroupList != null && tempGroupList.size() > 0) {
								no_create.setVisibility(View.VISIBLE);
								groupList.addAll(tempGroupList);
								my_create_list
										.setAdapter(new MyGroupAdapter(groupList, MyGroupActivity.this, "Create"));
							}
							if (tempJoinGroupList != null && tempJoinGroupList.size() > 0) {
								no_join.setVisibility(View.VISIBLE);
								joinGroupList.addAll(tempJoinGroupList);
								my_join_list.setAdapter(new MyGroupAdapter(joinGroupList, MyGroupActivity.this, null));
							}
							if (tempManageGroupList != null && tempManageGroupList.size() > 0) {
								no_manage.setVisibility(View.VISIBLE);
								manageGroupList.addAll(tempManageGroupList);
								my_manage_list
										.setAdapter(new MyGroupAdapter(manageGroupList, MyGroupActivity.this, null));
							}
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

}
