package com.yizhilu.adapter;

import java.util.List;

import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.entity.EntityPublic;
import com.yizhilu.huitianxinli.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HorizontalListViewAdapter extends BaseAdapter {
	private Context context;
	private List<EntityPublic> list;
	private int number=0;
	
	public HorizontalListViewAdapter(Context context, List<EntityPublic> list) {
		this.context=context;
		this.list=list;
	}

	@Override
	public int getCount() {
		return list.size();
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
		viewHodler hodler=null;
		if(convertView==null){
			hodler = new viewHodler();
			convertView = LayoutInflater.from(context).inflate(R.layout.item_horizontal_listview, null);
			hodler.consultation_name=(TextView) convertView.findViewById(R.id.consultation_name);
			convertView.setTag(hodler);
		}else{
			hodler=(viewHodler) convertView.getTag();
		}
		if(number==position){
			hodler.consultation_name.setTextColor(context.getResources().getColor(R.color.Blue));
		}else{
			hodler.consultation_name.setTextColor(context.getResources().getColor(R.color.tabText));
		}
		hodler.consultation_name.setText(list.get(position).getName());
		return convertView;
	}

	class viewHodler {
		TextView consultation_name;
	}
	public void setPosition(int number){
		this.number=number;
	}
}
