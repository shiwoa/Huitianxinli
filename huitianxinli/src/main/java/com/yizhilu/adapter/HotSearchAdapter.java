package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yizhilu.entity.EntityCourse;
import com.yizhilu.huitianxinli.R;

public class HotSearchAdapter extends BaseAdapter {
	private Context context;
	private List<EntityCourse> courseList;
	
	public HotSearchAdapter(Context context,List<EntityCourse> courseList) {
		super();
		this.context = context;
		this.courseList = courseList;
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
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_search, null);
			holder.titleText = (TextView) convertView.findViewById(R.id.tv_hotsearch);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.titleText.setText(courseList.get(position).getCourseName());
		return convertView;
	}

	class ViewHolder{
		private TextView titleText;
	}
}
