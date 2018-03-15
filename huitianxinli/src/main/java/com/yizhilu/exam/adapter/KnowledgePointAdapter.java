package com.yizhilu.exam.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;

/**
 * @author bin
 * 时间: 2016/6/4 10:31
 * 方法说明:
 */
public class KnowledgePointAdapter extends BaseExpandableListAdapter {
	private Context context;  //上下文对象
	private List<PublicEntity> pointList;  //专业的集合
	private int positionGroup = -1;
	private int positionChild = -1;
	public KnowledgePointAdapter(Context context, List<PublicEntity> pointList) {
		super();
		this.context = context;
		this.pointList = pointList;
	}

	public void setGroupPosition(int position){
		this.positionGroup = position;
	}

	public void setChildPosition(int position){
		this.positionChild = position;
	}
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return pointList.get(groupPosition).getExamPointSon().get(childPosition);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_child_point, null);
			holder.point_name = (TextView) convertView.findViewById(R.id.point_name);  //专业名
			holder.select_image = (ImageView) convertView.findViewById(R.id.select_image);
			holder.lineOne = convertView.findViewById(R.id.lineOne);
			holder.lineTwo = convertView.findViewById(R.id.lineTwo);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		List<PublicEntity> subjectList = pointList.get(groupPosition).getExamPointSon();
		if(childPosition == subjectList.size()-1){
			holder.lineOne.setVisibility(View.GONE);
			holder.lineTwo.setVisibility(View.VISIBLE);
		}else{
			holder.lineOne.setVisibility(View.VISIBLE);
			holder.lineTwo.setVisibility(View.GONE);
		}
		holder.point_name.setText(subjectList.get(childPosition).getName());
		if(groupPosition == positionGroup && childPosition == positionChild){
			holder.select_image.setBackgroundResource(R.mipmap.red_point);
		}else{
			holder.select_image.setBackgroundResource(R.mipmap.white_point);
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return pointList.get(groupPosition).getExamPointSon().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return pointList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return pointList.size();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_group_point, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);  //展开图标
			holder.point_name = (TextView) convertView.findViewById(R.id.point_name);  //专业名
			holder.select_image = (ImageView) convertView.findViewById(R.id.select_image);  //选中的图标
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		List<PublicEntity> examPointSon = pointList.get(groupPosition).getExamPointSon();
		if(examPointSon == null || examPointSon.size()<=0){
			holder.imageView.setVisibility(View.GONE);
			holder.select_image.setVisibility(View.VISIBLE);
			if(groupPosition == positionGroup){
				holder.select_image.setBackgroundResource(R.mipmap.red_point);
			}else{
				holder.select_image.setBackgroundResource(R.mipmap.white_point);
			}
		}else{
			holder.imageView.setVisibility(View.VISIBLE);
			holder.select_image.setVisibility(View.GONE);
			if(isExpanded){
				holder.imageView.setBackgroundResource(R.mipmap.point_open);
			}else{
				holder.imageView.setBackgroundResource(R.mipmap.point_close);
			}
		}
		holder.point_name.setText(pointList.get(groupPosition).getName());
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
		private TextView point_name;  //专业的名称
		private ImageView imageView,select_image;  //展开的图标,选中的图标
		private View lineOne,lineTwo;  //线
	}
}
