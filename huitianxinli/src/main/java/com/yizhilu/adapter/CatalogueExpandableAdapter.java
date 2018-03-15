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

/**
 * @author bin 修改人: 时间:2015-10-23 上午9:54:06 类说明:子目录的适配器
 */
public class CatalogueExpandableAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<EntityCourse> courseKpoints; // 子目录的集合
	private EntityCourse entityCourse = null; // 当前选中的实体

	public CatalogueExpandableAdapter(Context context, List<EntityCourse> courseKpoints) {
		super();
		this.context = context;
		this.courseKpoints = courseKpoints;
	}

	public void setSelectEntity(EntityCourse entityCourse) {
		this.entityCourse = entityCourse;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return courseKpoints.get(groupPosition).getChildKpoints().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		ViewChild viewChild = null;
		if (convertView == null) {
			viewChild = new ViewChild();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_child, null);
			viewChild.chile_image = (ImageView) convertView.findViewById(R.id.chile_image);
			viewChild.child_name = (TextView) convertView.findViewById(R.id.child_name);
			viewChild.course_isfree = (TextView) convertView.findViewById(R.id.course_isfree); // 试听
			convertView.setTag(viewChild);
		} else {
			viewChild = (ViewChild) convertView.getTag();
		}
		EntityCourse courseEntity = courseKpoints.get(groupPosition).getChildKpoints().get(childPosition);
		viewChild.child_name.setText(courseEntity.getName());
		int isfree = courseEntity.getIsfree();
		if (isfree == 1) { // 试听
			viewChild.course_isfree.setVisibility(View.VISIBLE);
		} else { // 不是试听
			viewChild.course_isfree.setVisibility(View.GONE);
		}
		if (entityCourse != null) {
			if (courseEntity == entityCourse) {
				viewChild.chile_image.setBackgroundResource(R.drawable.section_selected);
				viewChild.child_name.setTextColor(context.getResources().getColor(R.color.Blue));
			} else {
				viewChild.chile_image.setBackgroundResource(R.drawable.section);
				viewChild.child_name.setTextColor(context.getResources().getColor(R.color.color_67));
			}
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return courseKpoints.get(groupPosition).getChildKpoints().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return courseKpoints.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return courseKpoints.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		ViewParent viewParent = null;
		if (convertView == null) {
			viewParent = new ViewParent();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_parent, null);
			viewParent.parent_headImage = (ImageView) convertView.findViewById(R.id.parent_headImage);
			viewParent.parent_name = (TextView) convertView.findViewById(R.id.parent_name);
			viewParent.child_number = (TextView) convertView.findViewById(R.id.child_number);
			viewParent.has_free = (TextView) convertView.findViewById(R.id.has_free);
			convertView.setTag(viewParent);
		} else {
			viewParent = (ViewParent) convertView.getTag();
		}
		EntityCourse courseEntity = courseKpoints.get(groupPosition);
		viewParent.parent_name.setText(courseEntity.getName());
		if(courseEntity.getAudtion()==1){
			viewParent.has_free.setText("试听");
		}
		
		List<EntityCourse> childKpoints = courseEntity.getChildKpoints();
		if (childKpoints != null && childKpoints.size() > 0) {
			viewParent.child_number.setText("共" + childKpoints.size() + "节");
		} else {
			viewParent.child_number.setText("共0节");
		}
		if (entityCourse != null) {
			if (courseEntity == entityCourse) {
				viewParent.parent_headImage.setBackgroundResource(R.drawable.xian_select);
				viewParent.parent_name.setTextColor(context.getResources().getColor(R.color.Blue));
				viewParent.child_number.setTextColor(context.getResources().getColor(R.color.Blue));
			} else {
				viewParent.parent_headImage.setBackgroundResource(R.drawable.xian);
				viewParent.parent_name.setTextColor(context.getResources().getColor(R.color.color_67));
				viewParent.child_number.setTextColor(context.getResources().getColor(R.color.Blue));
			}
		}
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

	class ViewParent {
		private TextView parent_name, child_number, has_free; // 父目录的title
		private ImageView parent_headImage;
	}

	class ViewChild {
		private ImageView chile_image;
		private TextView child_name, course_isfree;
	}
}
