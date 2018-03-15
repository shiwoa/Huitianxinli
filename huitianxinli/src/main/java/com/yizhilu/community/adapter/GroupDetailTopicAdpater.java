package com.yizhilu.community.adapter;

import java.util.ArrayList;
import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.community.utils.Address;
import com.yizhilu.community.utils.LoadImageUtil;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.view.CircleImageView;
import com.yizhilu.view.GroupImageView;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//小组详情话题列表适配器
public class GroupDetailTopicAdpater extends BaseAdapter {

	private Context context;
	private List<EntityPublic> groupTopicList;
	private HolderView holderView = null;
	private ImageLoader imageLoader;
	private List<String> imagesList;

	public GroupDetailTopicAdpater(Context context, List<EntityPublic> groupTopicList) {
		super();
		this.context = context;
		this.groupTopicList = groupTopicList;
		imageLoader = ImageLoader.getInstance();
		imagesList = new ArrayList<String>();
	}

	@Override
	public int getCount() {
		return groupTopicList.size();
	}

	@Override
	public Object getItem(int position) {
		return groupTopicList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_group_detail_topic_list, parent, false);
			holderView.group_detail_topic_avatar = (CircleImageView) convertView
					.findViewById(R.id.group_detail_topic_avatar);
			holderView.group_detail_topic_author_nickName = (TextView) convertView
					.findViewById(R.id.group_detail_topic_author_nickName);
			holderView.group_detail_topic_author_name = (TextView) convertView
					.findViewById(R.id.group_detail_topic_author_name);
			holderView.group_detail_topic_createTime = (TextView) convertView
					.findViewById(R.id.group_detail_topic_createTime);
			holderView.group_detail_topic_title = (TextView) convertView.findViewById(R.id.group_detail_topic_title);
			holderView.group_detail_topic_content = (TextView) convertView
					.findViewById(R.id.group_detail_topic_content);
			holderView.group_detail_topic_comment = (TextView) convertView
					.findViewById(R.id.group_detail_topic_comment);
			holderView.group_detail_topic_praise = (TextView) convertView.findViewById(R.id.group_detail_topic_praise);
			holderView.group_detail_topic_browse = (TextView) convertView.findViewById(R.id.group_detail_topic_browse);
			holderView.isDetailTop = (TextView) convertView.findViewById(R.id.isDetailTop);
			holderView.isDetailEssence = (TextView) convertView.findViewById(R.id.isDetailEssence);
			holderView.isDetailFiery = (TextView) convertView.findViewById(R.id.isDetailFiery);
			holderView.topic_detail_image_list = (GroupImageView) convertView
					.findViewById(R.id.topic_detail_image_list);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		if (groupTopicList.get(position).getTop() == 1) {
			holderView.isDetailTop.setVisibility(View.VISIBLE);
		} else {
			holderView.isDetailTop.setVisibility(View.GONE);
		}
		if (groupTopicList.get(position).getEssence() == 1) {
			holderView.isDetailEssence.setVisibility(View.VISIBLE);
		} else {
			holderView.isDetailEssence.setVisibility(View.GONE);
		}
		if (groupTopicList.get(position).getFiery() == 1) {
			holderView.isDetailFiery.setVisibility(View.VISIBLE);
		} else {
			holderView.isDetailFiery.setVisibility(View.GONE);
		}
		if (groupTopicList.get(position).getHtmlImagesList() != null
				&& groupTopicList.get(position).getHtmlImagesList().size() > 9) {
			for (int i = 0; i < groupTopicList.get(position).getHtmlImagesList().size(); i++) {
				if (imagesList.size() < 9) {
					imagesList.add(groupTopicList.get(position).getHtmlImagesList().get(i));
				}
			}
			holderView.topic_detail_image_list.setPics(imagesList);
		} else {
			holderView.topic_detail_image_list.setPics(groupTopicList.get(position).getHtmlImagesList());
		}
		imageLoader.displayImage(Address.IMAGE + groupTopicList.get(position).getAvatar(),
				holderView.group_detail_topic_avatar, LoadImageUtil.loadImage());
		holderView.group_detail_topic_author_nickName.setText(groupTopicList.get(position).getNickName());
		holderView.group_detail_topic_author_name.setText(groupTopicList.get(position).getGroupName());
		holderView.group_detail_topic_createTime.setText(groupTopicList.get(position).getCreateTime());
		holderView.group_detail_topic_title.setText(groupTopicList.get(position).getTitle());
		holderView.group_detail_topic_content.setText(Html.fromHtml(groupTopicList.get(position).getContent()));
		holderView.group_detail_topic_comment.setText(String.valueOf(groupTopicList.get(position).getCommentCounts()));
		holderView.group_detail_topic_praise.setText(String.valueOf(groupTopicList.get(position).getPraiseCounts()));
		holderView.group_detail_topic_browse.setText(String.valueOf(groupTopicList.get(position).getBrowseCounts()));
		return convertView;
	}

	class HolderView {
		private CircleImageView group_detail_topic_avatar;
		private TextView group_detail_topic_author_nickName, group_detail_topic_author_name,
				group_detail_topic_createTime, group_detail_topic_title, group_detail_topic_content,
				group_detail_topic_comment, group_detail_topic_praise, group_detail_topic_browse, isDetailTop,
				isDetailEssence, isDetailFiery;
		private GroupImageView topic_detail_image_list;
	}

}
