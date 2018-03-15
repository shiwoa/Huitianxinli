package com.yizhilu.exam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.yizhilu.exam.entity.ListEntity;
import com.yizhilu.huitianxinli.R;

/**
 * 作者：caibin
 * 时间：2016/6/12 11:07
 * 类说明：考点练习情况的适配器
 */
public class PracticeConditionAdapter extends BaseExpandableListAdapter {
    private Context context;  //上下文对象

    private ListEntity listEntity;  //实体类

    public PracticeConditionAdapter(Context context, ListEntity listEntity) {
        this.context = context;
        this.listEntity = listEntity;
    }
    @Override
    public int getGroupCount() {
        return listEntity.getEntity().size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listEntity.getEntity().get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listEntity.getEntity().get(groupPosition).getExamPointSon().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_practice_report,parent,false);
            holder.reportName = (TextView) convertView.findViewById(R.id.reportName);
            holder.questionsNumber = (TextView) convertView.findViewById(R.id.questionsNumber);
            holder.rightNumber = (TextView) convertView.findViewById(R.id.rightNumber);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.reportName.setText(listEntity.getEntity().get(groupPosition).getName());  //专业名
        holder.questionsNumber.setText(listEntity.getEntity().get(groupPosition).getQstCount()+"");  //试题数量
        holder.rightNumber.setText(listEntity.getEntity().get(groupPosition).getCusRightQstNum()+"");  //正确数量
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class ViewHolder{
        private TextView reportName,questionsNumber,rightNumber;
    }
}
