package com.yizhilu.exam.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.exam.entity.ListEntity;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;

/**
 * 专业的适配器
 */
public class SubAdapter extends BaseExpandableListAdapter {
	private Context context;  //上下文对象
	private ListEntity subAllEntity;  //专业的集合
	public SubAdapter(Context context,ListEntity entity) {
		super();
		this.context = context;
		this.subAllEntity = entity;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return subAllEntity.getEntity().get(groupPosition).getSubjectList().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_child_sub, null);
			holder.sub_name = (TextView) convertView.findViewById(R.id.sub_name);  //专业名
			holder.lineOne = convertView.findViewById(R.id.lineOne);
			holder.lineTwo = convertView.findViewById(R.id.lineTwo);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		List<PublicEntity> subjectList = subAllEntity.getEntity().get(groupPosition).getSubjectList();
		if(childPosition == subjectList.size()-1){
			holder.lineOne.setVisibility(View.GONE);
			holder.lineTwo.setVisibility(View.VISIBLE);
		}else{
			holder.lineOne.setVisibility(View.VISIBLE);
			holder.lineTwo.setVisibility(View.GONE);
		}
		holder.sub_name.setText(subjectList.get(childPosition).getSubjectName());
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return subAllEntity.getEntity().get(groupPosition).getSubjectList().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return subAllEntity.getEntity().get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		if(subAllEntity.getEntity()!=null && subAllEntity.getEntity().size()>0){
			return subAllEntity.getEntity().size();
		}
		return 0;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_sub, null);
			holder.sub_name = (TextView) convertView.findViewById(R.id.sub_name);  //专业名
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);  //展开图标
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if(isExpanded){
			holder.imageView.setBackgroundResource(R.mipmap.open);
		}else{
			holder.imageView.setBackgroundResource(R.mipmap.close);
		}
		holder.sub_name.setText(subAllEntity.getEntity().get(groupPosition).getProfessionalName());
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	class ViewHolder{
		private TextView sub_name;  //专业的名称
		private ImageView imageView;  //展开的图标
		private View lineOne,lineTwo;  //线
	}
}
