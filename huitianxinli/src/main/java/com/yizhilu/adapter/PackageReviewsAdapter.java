package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Picasso;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;
import com.yizhilu.view.CircleImageView;
import com.yizhilu.view.GroupImageView;

public class PackageReviewsAdapter extends BaseAdapter {
	private Context context;
	private List<EntityPublic> hotTopicList;
	private ImageLoader imageLoader;

	public PackageReviewsAdapter(Context context,
			List<EntityPublic> hotTopicList) {
		this.context = context;
		this.hotTopicList = hotTopicList;
		imageLoader = ImageLoader.getInstance();

	}

	@Override
	public int getCount() {
		Log.i("lala", hotTopicList.size() + "评论集合");
		return hotTopicList.size();

	}

	@Override
	public EntityPublic getItem(int position) {
		return hotTopicList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView = null;
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_package_reviews, parent, false);
			holderView.avatar = (CircleImageView) convertView
					.findViewById(R.id.avatar);
			holderView.nickName = (TextView) convertView
					.findViewById(R.id.nickName);
			holderView.date_text = (TextView) convertView
					.findViewById(R.id.date_text);
			holderView.content_text = (TextView) convertView
					.findViewById(R.id.content_text);
			holderView.image_list = (GroupImageView) convertView
					.findViewById(R.id.image_list);
			holderView.view = (WebView) convertView
					.findViewById(R.id.discuss_content_webview);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		String nickname = hotTopicList.get(position).getNickname();
		String email = hotTopicList.get(position).getEmail();
		String mobile = hotTopicList.get(position).getMobile();
		if (TextUtils.isEmpty(nickname)) {
			if (email.equals("")) {
				if (mobile.equals("")) {
					holderView.nickName.setText("");
				} else {
					holderView.nickName.setText(mobile);
				}
			} else {
				holderView.nickName.setText(email);
			}
		} else {
			holderView.nickName.setText(nickname);
		}
		holderView.date_text.setText(hotTopicList.get(position).getAddTime());
		holderView.view.loadDataWithBaseURL(null, hotTopicList.get(position)
				.getContent(), "text/html", "utf-8", null);

		imageLoader.displayImage(Address.IMAGE_NET
				+ hotTopicList.get(position).getAvatar(), holderView.avatar,
				HttpUtils.getDisPlay());

		return convertView;
	}

	class HolderView {
		private CircleImageView avatar;// 头像
		private TextView nickName, date_text, content_text;// 名字，日期,内容
		private GroupImageView image_list;// 图片
		private WebView view;
	}
}
