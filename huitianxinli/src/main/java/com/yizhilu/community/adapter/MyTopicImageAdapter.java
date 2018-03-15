package com.yizhilu.community.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.community.utils.Address;
import com.yizhilu.community.utils.LoadImageUtil;
import com.yizhilu.huitianxinli.R;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

//我的话题适配器
public class MyTopicImageAdapter extends BaseAdapter {

	private List<String> myTopicImagesList;
	private Context context;
	private HolderView holderView = null;
	private ImageLoader imageLoader;

	public MyTopicImageAdapter(List<String> myTopicImagesList, Context context) {
		super();
		this.myTopicImagesList = myTopicImagesList;
		this.context = context;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		if (myTopicImagesList.size() > 3) {
			return 3;
		}
		return myTopicImagesList.size();
	}

	@Override
	public Object getItem(int position) {
		return myTopicImagesList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_my_topic_list_image, parent, false);
			holderView.my_topic_image = (ImageView) convertView.findViewById(R.id.my_topic_image);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		Log.i("xm", myTopicImagesList.get(position) + "---url");
		imageLoader.displayImage(myTopicImagesList.get(position), holderView.my_topic_image, LoadImageUtil.loadImage());
		return convertView;
	}

	class HolderView {
		ImageView my_topic_image;
	}

}
