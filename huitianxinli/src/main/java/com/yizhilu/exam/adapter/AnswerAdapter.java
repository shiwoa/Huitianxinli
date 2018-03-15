package com.yizhilu.exam.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yizhilu.huitianxinli.R;

/**
 * @author 杨财宾
 * 答案的适配器
 *
 */
public class AnswerAdapter extends BaseAdapter{
	private List<String> answerList;  //试题答案的集合
	private Context context;  //上下文对象
	private List<Boolean> markers;
	public AnswerAdapter(Context context,List<String> answerList,List<Boolean> markers) {
		super();
		this.context = context;
		this.answerList = answerList;
		this.markers = markers;
	}

	@Override
	public int getCount() {
		return answerList.size();
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
			convertView = LayoutInflater.from(context).inflate(R.layout.item_answer, parent,false);
			holder.layout = (RelativeLayout) convertView.findViewById(R.id.layout);  //总布局
			holder.answer_text = (TextView) convertView.findViewById(R.id.answer_text);
			holder.answer_dian = (ImageView) convertView.findViewById(R.id.answer_dian);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.answer_text.setText((position+1)+"");
		if(answerList.get(position).equals("")){
			holder.layout.setBackgroundResource(R.mipmap.answer_bg);
			holder.answer_text.setTextColor(context.getResources().getColor(R.color.color_cacccf));
		}else{
			if(answerList.get(position).equals("我是材料题,可以略过")){
				holder.layout.setBackgroundResource(R.mipmap.answer_bg);
				holder.answer_text.setText("材料");
				holder.answer_text.setTextColor(context.getResources().getColor(R.color.color_cacccf));
			}else{
				holder.layout.setBackgroundResource(R.mipmap.answered_bg);
				holder.answer_text.setTextColor(context.getResources().getColor(R.color.white));
			}
		}
		if(markers.get(position)){
			holder.answer_dian.setVisibility(View.VISIBLE);
		}else{
			holder.answer_dian.setVisibility(View.GONE);
		}
		return convertView;
	}

	class ViewHolder{
		private RelativeLayout layout;
		private TextView answer_text;
		private ImageView answer_dian;
	}
}
