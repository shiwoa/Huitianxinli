package com.yizhilu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.huitianxinli.R;

/**
 * @author bin 修改人: 时间:2015-10-21 上午11:35:04 类说明:排序的适配器
 */
public class CourseSortAdapter extends BaseAdapter {

	private Context context;
	private String[] sortList; // 排序的集合
	private int index = 0; // 選中的下標

	public CourseSortAdapter(Context context, String[] sortList) {
		super();
		this.context = context;
		this.sortList = sortList;
	}

	@Override
	public int getCount() {
		return sortList.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setPostion(int position) {
		this.index = position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Viewholder holder = null;
		if (convertView == null) {
			holder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_subject, null);
			holder.subjectText = (TextView) convertView
					.findViewById(R.id.subject_text);
			holder.select_image = (ImageView) convertView
					.findViewById(R.id.select_image);
			convertView.setTag(holder);
		} else {
			holder = (Viewholder) convertView.getTag();
		}
		holder.subjectText.setText(sortList[position]);
		if (index == position) {
			holder.subjectText.setTextColor(context.getResources().getColor(R.color.Blue));
			holder.select_image.setVisibility(View.VISIBLE);
		} else {
			holder.subjectText.setTextColor(context.getResources().getColor(R.color.Black));
			holder.select_image.setVisibility(View.GONE);
		}
		return convertView;
	}

	class Viewholder {
		private TextView subjectText;
		private ImageView select_image;
	}

}
