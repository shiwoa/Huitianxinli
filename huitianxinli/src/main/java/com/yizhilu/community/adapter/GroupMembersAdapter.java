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

//小组详情成员列表适配器
public class GroupMembersAdapter extends BaseAdapter {

	private Context context;
	private List<EntityPublic> groupMembers;
	private HolderView holderView = null;
	private ImageLoader imageLoader;

	public GroupMembersAdapter(Context context, List<EntityPublic> groupMembers) {
		super();
		this.context = context;
		this.groupMembers = groupMembers;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		if (groupMembers.size() > 7) {
			return 7;
		}
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_group_members_list, parent, false);
			holderView.group_members_image = (CircleImageView) convertView.findViewById(R.id.group_members_image);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		imageLoader.displayImage(Address.IMAGE + groupMembers.get(position).getAvatar(), holderView.group_members_image,
				LoadImageUtil.loadImage());
		return convertView;
	}

	class HolderView {
		private CircleImageView group_members_image;
	}

}
