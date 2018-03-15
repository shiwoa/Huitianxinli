package com.yizhilu.community.adapter;

import java.util.List;

import com.yizhilu.entity.SubjectEntity;
import com.yizhilu.huitianxinli.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//话题类型适配器
public class TopicTypeAdapter extends BaseAdapter {

	private Context context;
	private List<SubjectEntity> subjectList;
	private HolderView holderView = null;

	public TopicTypeAdapter(Context context, List<SubjectEntity> subjectList) {
		super();
		this.context = context;
		this.subjectList = subjectList;
	}

	@Override
	public int getCount() {
		return subjectList.size();
	}

	@Override
	public Object getItem(int position) {
		return subjectList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holderView = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_topic_type, parent, false);
			holderView.type = (TextView) convertView.findViewById(R.id.type);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		holderView.type.setText(subjectList.get(position).getSubjectName());
		return convertView;
	}

	class HolderView {
		private TextView type;
	}

}
