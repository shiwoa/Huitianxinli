package com.yizhilu.exam.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.StringUtil;

/**
 * @author 杨财宾
 * 查看解析答案的适配器
 *
 */
public class ParserAnswerAdapter extends BaseAdapter{
	private List<PublicEntity> answerList;  //试题答案的集合
	private Context context;  //上下文对象
	public ParserAnswerAdapter(Context context, List<PublicEntity> answerList) {
		super();
		this.context = context;
		this.answerList = answerList;
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
		PublicEntity publicEntity = answerList.get(position);
		//获取我的答案
		String userAnswer = StringUtil.isFieldEmpty(publicEntity.getUserAnswer());
		if (TextUtils.isEmpty(userAnswer)) {
			holder.layout.setBackgroundResource(R.mipmap.answered_bg);
			holder.answer_text.setTextColor(context.getResources().getColor(R.color.white));
		} else {
			//获取试题是否正确的值
			int qstRecordstatus = publicEntity.getQstRecordstatus();
			if (qstRecordstatus == 0) {  //对
				holder.layout.setBackgroundResource(R.mipmap.answer_bg);
				holder.answer_text.setTextColor(context.getResources().getColor(R.color.color_cacccf));
			} else {  //错
				holder.layout.setBackgroundResource(R.mipmap.answered_bg);
				holder.answer_text.setTextColor(context.getResources().getColor(R.color.white));
			}
		}
		return convertView;
	}

	class ViewHolder{
		private RelativeLayout layout;
		private TextView answer_text;
		private ImageView answer_dian;
	}
}
