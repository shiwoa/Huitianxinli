package com.yizhilu.community.adapter;

import java.util.List;

import org.apache.http.Header;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.community.utils.Address;
import com.yizhilu.community.utils.LoadImageUtil;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.entity.MessageEntity;
import com.yizhilu.huitianxinli.R;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

//发现适配器
public class FindListAdapter extends BaseAdapter {

	private List<EntityPublic> groupList;
	private Context context;
	private HolderView holderView = null;
	private ImageLoader imageLoader;
	private AsyncHttpClient httpClient;
	private int userId;

	public FindListAdapter(List<EntityPublic> groupList, Context context, int userId) {
		super();
		this.groupList = groupList;
		this.context = context;
		this.userId = userId;
		httpClient = new AsyncHttpClient();
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return groupList.size();
	}

	@Override
	public Object getItem(int position) {
		return groupList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_find_group_list, parent, false);
			holderView.find_list_avatar = (ImageView) convertView.findViewById(R.id.find_list_avatar);
			holderView.find_list_name = (TextView) convertView.findViewById(R.id.find_list_name);
			holderView.find_list_member = (TextView) convertView.findViewById(R.id.find_list_member);
			holderView.find_list_topic_num = (TextView) convertView.findViewById(R.id.find_list_topic_num);
			holderView.join_btn = (TextView) convertView.findViewById(R.id.join_btn);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}

		if (groupList.get(position).getCusId() == userId || groupList.get(position).getWhetherTheMembers() == 1) {
			holderView.join_btn.setText("已加入");
			holderView.join_btn.setTextColor(context.getResources().getColor(R.color.text_grayB1B));
			holderView.join_btn.setEnabled(false);
		} else {
			holderView.join_btn.setText("加入");
			holderView.join_btn.setTextColor(context.getResources().getColor(R.color.text_blue3F8));
			holderView.join_btn.setEnabled(true);
		}
		imageLoader.displayImage(Address.IMAGE + groupList.get(position).getImageUrl(), holderView.find_list_avatar,
				LoadImageUtil.loadImageRound());
		holderView.find_list_name.setText(groupList.get(position).getName());
		holderView.find_list_member.setText("成员" + groupList.get(position).getMemberNum());
		holderView.find_list_topic_num.setText("话题" + groupList.get(position).getTopicCounts());
		holderView.join_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (userId == 0) {
					Toast.makeText(context, "您还没有登录", 0).show();
				} else {
					joinGroup(groupList.get(position).getId(), userId, position);
				}
			}
		});
		return convertView;
	}

	class HolderView {
		private ImageView find_list_avatar;
		private TextView find_list_name, find_list_member, find_list_topic_num, join_btn;
	}

	// 加入小组
	private void joinGroup(int groupId, int userId, final int position) {
		RequestParams params = new RequestParams();
		params.put("groupId", groupId);
		params.put("userId", userId);
		Log.i("xm", Address.JOINGROUP + "?" + params);
		httpClient.post(Address.JOINGROUP, params, new TextHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				if (!TextUtils.isEmpty(arg2)) {
					try {
						MessageEntity messageEntity = JSON.parseObject(arg2, MessageEntity.class);
						if (messageEntity.isSuccess()) {
							groupList.get(position).setWhetherTheMembers(1);
							notifyDataSetChanged();
							Intent intent = new Intent();
							intent.setAction("Change");
							intent.putExtra("group", true);
							context.sendBroadcast(intent);
							Toast.makeText(context, "加入成功!", 0).show();
						} else {
							Toast.makeText(context, "加入失败！", 0).show();
						}
					} catch (Exception e) {
					}
				}
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {

			}
		});
	}

}
