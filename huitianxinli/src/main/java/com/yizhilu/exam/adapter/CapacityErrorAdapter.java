package com.yizhilu.exam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yizhilu.exam.RecordInterface;
import com.yizhilu.huitianxinli.R;

/**
 * @author bin
 * 时间: 2016/6/2 20:51
 * 类说明: 智能错题练习列表的适配器
 */
public class CapacityErrorAdapter extends BaseAdapter {
	private Context context;  //上下文对象
	private int titles[],contents[],models[];  //错题智能练习的标题，内容，考试模式
	private RecordInterface recordInterface;  //接口
	//设置接口的方法
	public void setRecordInterface(RecordInterface recordInterface){
		this.recordInterface = recordInterface;
	}
	public CapacityErrorAdapter(Context context,int title[],int[] content,int[] model) {
		super();
		this.context = context;
		this.titles = title;
		this.contents = content;
		this.models = model;
	}

	@Override
	public int getCount() {
		return titles.length;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_capacity_error, parent,false);
			holder.phase_title = (TextView) convertView.findViewById(R.id.phase_title);
			holder.phase_content = (TextView) convertView.findViewById(R.id.phase_content);
			holder.exam_modle_text = (TextView) convertView.findViewById(R.id.exam_modle_text);
			holder.practice_modle_text = (TextView) convertView.findViewById(R.id.practice_modle_text);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.phase_title.setText(titles[position]);
		holder.phase_content.setText(contents[position]);
		holder.exam_modle_text.setText(models[0]);
		holder.practice_modle_text.setText(models[1]);
		//考试模式
		holder.exam_modle_text.setOnClickListener(new MyOnClick(position,"考试模式"));
		//练习模式
		holder.practice_modle_text.setOnClickListener(new MyOnClick(position,"练习模式"));
		return convertView;
	}

	class ViewHolder{
		private TextView phase_title,phase_content,exam_modle_text,practice_modle_text;
	}

	class MyOnClick implements View.OnClickListener {
		private int position;
		private String name;

		public MyOnClick(int position, String name) {
			this.position = position;
			this.name = name;
		}

		@Override
		public void onClick(View v) {
			recordInterface.myClick(position,name);
		}
	}
}
