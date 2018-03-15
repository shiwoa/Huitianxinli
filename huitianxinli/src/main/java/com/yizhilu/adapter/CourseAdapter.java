package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yizhilu.entity.EntityCourse;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.HttpUtils;

public class CourseAdapter extends BaseAdapter {
	private Context context;
	private List<EntityCourse> courseList; // 课程的集合
	private ImageLoader imageLoader;

	public CourseAdapter(Context context, List<EntityCourse> courseList) {
		super();
		this.context = context;
		this.courseList = courseList;
		imageLoader = ImageLoader.getInstance();
	}

	@Override
	public int getCount() {
		return courseList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_course, null);
			holder.courseImage = (ImageView) convertView
					.findViewById(R.id.courseImage);
			holder.titleText = (TextView) convertView
					.findViewById(R.id.titleText);
			holder.playNum = (TextView) convertView.findViewById(R.id.playNum);
			holder.recommendMoney = (TextView) convertView
					.findViewById(R.id.Money);
			holder.recommend_freePrice = (LinearLayout) convertView
					.findViewById(R.id.freePrice);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.titleText.setText(courseList.get(position).getName());
		int ispay = courseList.get(position).getIsPay();
		float currentPrice = courseList.get(position).getCurrentprice();

		if (ispay == 0 || currentPrice <= 0) {

		/*	holder.recommend_currentPrice.setVisibility(View.GONE);*/
			holder.recommend_freePrice.setVisibility(View.VISIBLE);
		} else {
			holder.recommend_freePrice.setVisibility(View.GONE);
			holder.recommendMoney.setText(currentPrice + "");
		}

		holder.playNum.setText("播放量: "
				+ courseList.get(position).getPagePlaycount() + "");
		imageLoader.displayImage(Address.IMAGE_NET
				+ courseList.get(position).getMobileLogo(), holder.courseImage,
				HttpUtils.getDisPlay());
		return convertView;
	}

	class ViewHolder {
		private ImageView courseImage;
		private TextView titleText, playNum, recommendMoney;
		private LinearLayout recommend_currentPrice, recommend_freePrice; // 价格lin
																			// ,免费lin
	}
}
