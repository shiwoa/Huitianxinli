package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.entity.EntityCourse;
import com.yizhilu.huitianxinli.R;

/**
 * @author bin
 * 修改人:
 * 时间:2015-10-21 上午11:14:06
 * 类说明:教师的适配器
 */
public class CourseTeacherAdapter extends BaseAdapter {

	private Context context;
	private List<EntityCourse> subjectList;  //专业的集合
	private int index = 0;  //選中的下標
	public CourseTeacherAdapter(Context context,List<EntityCourse> subjectList) {
		super();
		this.context = context;
		this.subjectList = subjectList;
	}

	@Override
	public int getCount() {
		return subjectList.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void setPostion(int position){
		this.index = position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Viewholder holder = null;
		if(convertView == null){
			holder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_subject, null);
			holder.subjectText = (TextView) convertView.findViewById(R.id.subject_text);
			holder.select_image = (ImageView) convertView.findViewById(R.id.select_image);
			convertView.setTag(holder);
		}else{
			holder = (Viewholder) convertView.getTag();
		}
		if(position == 0){
			holder.subjectText.setText("全部");
		}else{
			holder.subjectText.setText(subjectList.get(position-1).getName());
		}
		if(index == position){
			holder.subjectText.setTextColor(context.getResources().getColor(R.color.Blue));
			holder.select_image.setVisibility(View.VISIBLE);
		}else{
			holder.subjectText.setTextColor(context.getResources().getColor(R.color.Black));
			holder.select_image.setVisibility(View.GONE);
		}
		return convertView;
	}

	class Viewholder{
		private TextView subjectText;
		private ImageView select_image;
	}

}
