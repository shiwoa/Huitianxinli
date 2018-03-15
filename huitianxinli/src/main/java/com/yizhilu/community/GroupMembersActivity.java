package com.yizhilu.community;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.community.adapter.MembersAdapter;
import com.yizhilu.community.utils.Address;
import com.yizhilu.community.utils.LoadImageUtil;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.CircleImageView;
import com.yizhilu.view.NoScrollListView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

//社区-小组成员
public class GroupMembersActivity extends BaseActivity {

	private LinearLayout back_layout;// back...
	private TextView title_text;// title...
	private AsyncHttpClient httpClient;// 网络连接
	private ProgressDialog progressDialog;// loading...
	private int groupId;// 小组Id
	private NoScrollListView smallGroupLeader_list, groupMembers_list;// 副社长列表，成员列表
	private List<EntityPublic> groupMembersList, smallGroupLeaderList;// 小组成员集合，副社长集合
	private CircleImageView groupLeader_avatar;// 社长头像
	private TextView groupLeader_name;// 社长名字
	private ImageLoader imageLoader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_group_members);
		super.onCreate(savedInstanceState);
	}

	@Override
	public void initView() {
		imageLoader=ImageLoader.getInstance();
		Intent intent = getIntent();
		groupId = intent.getIntExtra("GroupId", 0);
		back_layout = (LinearLayout) findViewById(R.id.back_layout);
		title_text = (TextView) findViewById(R.id.title_text);
		smallGroupLeader_list = (NoScrollListView) findViewById(R.id.smallGroupLeader_list);
		groupMembers_list = (NoScrollListView) findViewById(R.id.groupMembers_list);
		groupLeader_avatar = (CircleImageView) findViewById(R.id.groupLeader_avatar);
		groupLeader_name = (TextView) findViewById(R.id.groupLeader_name);
		title_text.setText("小组成员");// 设置标题
		httpClient = new AsyncHttpClient();
		progressDialog = new ProgressDialog(this);
		groupMembersList = new ArrayList<EntityPublic>();
		smallGroupLeaderList = new ArrayList<EntityPublic>();
		getMembersInfo(groupId);// 获取成员信息
	}

	@Override
	public void addOnClick() {
		back_layout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.back_layout:
			GroupMembersActivity.this.finish();
			break;

		default:
			break;
		}
	}

	// 获取成员信息
	private void getMembersInfo(int groupId) {
		RequestParams params = new RequestParams();
		params.put("groupId", groupId);
		Log.i("xm", Address.ALLGROUPMEMBER + "?" + params + "---allmembers");
		httpClient.post(Address.ALLGROUPMEMBER, params, new TextHttpResponseHandler() {

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
							imageLoader.displayImage(
									Address.IMAGE + publicEntity.getEntity().getGroupLeader().getAvatar(),
									groupLeader_avatar, LoadImageUtil.loadImage());
							groupLeader_name.setText(publicEntity.getEntity().getGroupLeader().getNickname());
							List<EntityPublic> tempMembersList = publicEntity.getEntity().getGroupMembers();
							List<EntityPublic> tempLeaderList = publicEntity.getEntity().getSmallGroupLeader();
							if (tempMembersList != null && tempLeaderList.size() > 0) {
								groupMembersList.addAll(tempMembersList);
								groupMembers_list
										.setAdapter(new MembersAdapter(groupMembersList, GroupMembersActivity.this));
							}
							if (tempLeaderList != null && tempLeaderList.size() > 0) {
								smallGroupLeaderList.addAll(tempLeaderList);
								smallGroupLeader_list.setAdapter(
										new MembersAdapter(smallGroupLeaderList, GroupMembersActivity.this));
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
