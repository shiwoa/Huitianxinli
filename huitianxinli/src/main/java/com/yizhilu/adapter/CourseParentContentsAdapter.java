package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yizhilu.entity.EntityCourse;
import com.yizhilu.huitianxinli.R;

/**
 * @author bin
 * 修改人:
 * 时间:2015-10-23 上午9:06:30
 * 类说明:课程父目录的适配器
 */
public class CourseParentContentsAdapter extends BaseAdapter {
	private Context context;
	private List<EntityCourse> packageList;  //课程父目录的集合
	private int parentPosition;
	public CourseParentContentsAdapter(Context context,
			List<EntityCourse> packageList) {
		super();
		this.context = context;
		this.packageList = packageList;
	}

	@Override
	public int getCount() {
		return packageList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setParentPosition(int position){
		this.parentPosition = position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_parent_course, null);
			holder.parentLayout = (LinearLayout) convertView.findViewById(R.id.parentLayout);
			holder.parentImage = (ImageView) convertView.findViewById(R.id.parentImage);
			holder.catalogueName = (TextView) convertView.findViewById(R.id.catalogueName);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.catalogueName.setText(packageList.get(position).getName());
		if(parentPosition == position){
			holder.parentLayout.setBackgroundResource(R.drawable.chapter_frame_selected);
			holder.parentImage.setBackgroundResource(R.drawable.iconfont_wodekecheng_select);
			holder.catalogueName.setTextColor(context.getResources().getColor(R.color.Blue));
		}else{
			holder.parentLayout.setBackgroundResource(R.drawable.chapter_frame);
			holder.parentImage.setBackgroundResource(R.drawable.iconfont_wodekecheng);
			holder.catalogueName.setTextColor(context.getResources().getColor(R.color.color_9a));
		}
		return convertView;
	}

	class ViewHolder{
		private LinearLayout parentLayout;  //父目录的总布局
		private ImageView parentImage;  //父目录的图标
		private TextView catalogueName;  //目录的名字
	}
}
