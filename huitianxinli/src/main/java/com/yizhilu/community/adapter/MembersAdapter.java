package com.yizhilu.community.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.community.utils.Address;
import com.yizhilu.community.utils.LoadImageUtil;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.view.CircleImageView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//成员列表适配器
public class MembersAdapter extends BaseAdapter {
	private List<EntityPublic> groupMembers;
	private Context context;
	private HolderView holderView = null;
	private ImageLoader imageLoader;

	public MembersAdapter(List<EntityPublic> groupMembers, Context context) {
		super();
		this.groupMembers = groupMembers;
		this.context = context;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return groupMembers.size();
	}

	@Override
	public Object getItem(int position) {
		return groupMembers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_members, parent, false);
			holderView.members_avatar = (CircleImageView) convertView.findViewById(R.id.members_avatar);
			holderView.members_name = (TextView) convertView.findViewById(R.id.members_name);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		holderView.members_name.setText(groupMembers.get(position).getShowName());
		imageLoader.displayImage(Address.IMAGE + groupMembers.get(position).getAvatar(), holderView.members_avatar,
				LoadImageUtil.loadImage());
		return convertView;
	}

	class HolderView {
		private CircleImageView members_avatar;
		private TextView members_name;
	}

}
