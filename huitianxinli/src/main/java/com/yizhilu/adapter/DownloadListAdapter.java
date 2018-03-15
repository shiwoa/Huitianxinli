package com.yizhilu.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.entity.EntityCourse;
import com.yizhilu.huitianxinli.R;

public class DownloadListAdapter extends BaseExpandableListAdapter {
	private Context context;  //上下文对象
	private List<EntityCourse> datas;
	private LayoutInflater inflater;
	private List<EntityCourse> downloads; // 需要下载的课程信息
	public DownloadListAdapter(Context context, List<EntityCourse> datas,List<EntityCourse> downloads) {
		this.context = context;
		this.datas = datas;
		inflater = LayoutInflater.from(context);
		this.downloads = downloads;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return datas.get(groupPosition).getChildKpoints().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder childHolder = null;
		if(convertView == null){
			childHolder = new ChildHolder();
			convertView = inflater.inflate(R.layout.item_download_child, null);
			childHolder.chile_image = (ImageView) convertView.findViewById(R.id.chile_image);
			childHolder.childName = (TextView) convertView
					.findViewById(R.id.download_child_text);
			childHolder.course_isfree = (TextView) convertView.findViewById(R.id.course_isfree);
			convertView.setTag(childHolder);
		}else{
			childHolder = (ChildHolder) convertView.getTag();
		}
		EntityCourse courseEntity = datas.get(groupPosition).getChildKpoints().get(childPosition);
		childHolder.childName.setText(courseEntity.getName());
		int isfree = courseEntity.getIsfree();
		if(isfree == 1){  //试听
			childHolder.course_isfree.setVisibility(View.VISIBLE);
		}else{  //不是试听
			childHolder.course_isfree.setVisibility(View.GONE);
		}
		if(downloads.contains(datas.get(groupPosition).getChildKpoints().get(childPosition))){
			childHolder.chile_image.setBackgroundResource(R.drawable.section_selected);
			childHolder.childName.setTextColor(context.getResources().getColor(R.color.Blue));
		}else{
			childHolder.chile_image.setBackgroundResource(R.drawable.section);
			childHolder.childName.setTextColor(context.getResources().getColor(R.color.text_color_normal));
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return datas.get(groupPosition).getChildKpoints().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return datas.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return datas.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ParentHolder parentHolder = null;
		if(convertView == null){
			parentHolder = new ParentHolder();
			convertView = inflater.inflate(R.layout.item_download_group, null);
			parentHolder.parentName = (TextView) convertView.findViewById(R.id.download_group_text);
			parentHolder.child_number = (TextView) convertView.findViewById(R.id.child_number);
			parentHolder.headImage = (ImageView) convertView
					.findViewById(R.id.download_group_image);
			convertView.setTag(parentHolder);
		}else{
			parentHolder = (ParentHolder) convertView.getTag();
		}
		parentHolder.parentName.setText(datas.get(groupPosition).getName());
		List<EntityCourse> childKpoints = datas.get(groupPosition).getChildKpoints();
		if(childKpoints!=null&&childKpoints.size()>0){
			parentHolder.child_number.setText("共"+childKpoints.size()+"节");
		}else{
			parentHolder.child_number.setText("共0节");
		}
//		if (datas.get(groupPosition).getChildKpoints().size() < 1) {
//			parentHolder.headImage.setVisibility(View.GONE);
//		} else {
//			parentHolder.headImage.setVisibility(View.VISIBLE);
//		}
//		if (isExpanded) {
//			parentHolder.headImage.setImageResource(R.drawable.group_down);
//		} else {
//			parentHolder.headImage.setImageResource(R.drawable.group_up);
//		}
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	class ParentHolder{
		private TextView parentName,child_number;
		private ImageView headImage;
	}
	class ChildHolder{
		private TextView childName,course_isfree;
		private ImageView chile_image;
	}
}
