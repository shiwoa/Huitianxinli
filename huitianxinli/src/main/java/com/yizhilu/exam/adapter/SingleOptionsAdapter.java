package com.yizhilu.exam.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.exam.utils.HtmlImageGetter;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.StringUtil;

/**
 * 作者：caibin
 * 时间：2016/6/7 9:31
 * 类说明：单个试题查看解析选项的适配器
 */
public class SingleOptionsAdapter extends BaseAdapter {
    private Context context;
    private PublicEntity questionEntity;

    public SingleOptionsAdapter(Context context, PublicEntity questionEntity) {
        this.context = context;
        this.questionEntity = questionEntity;
    }

    @Override
    public int getCount() {
        return questionEntity.getOptions().size();
    }

    @Override
    public Object getItem(int position) {
        return questionEntity.getOptions().get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_select_options,parent,false);
            holder.optionImage = (TextView) convertView.findViewById(R.id.optionImage);
            holder.optionContent = (TextView) convertView.findViewById(R.id.optionContent);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        int qstType = questionEntity.getQstType();
        if(qstType == 0){
        	qstType = questionEntity.getQuestionType();
        }
        List<PublicEntity> options = questionEntity.getOptions();
        String userAnswer = StringUtil.isFieldEmpty(questionEntity.getUserAnswer());
        holder.optionImage.setText(options.get(position).getOptOrder());
//        holder.optionContent.setText(options.get(position).getOptContent());
        
        String optContent = options.get(position).getOptContent();
        holder.optionContent.setMovementMethod(ScrollingMovementMethod.getInstance());// 设置可滚动  
        holder.optionContent.setMovementMethod(LinkMovementMethod.getInstance());//设置超链接可以打开网页
        Drawable defaultDrawable = context.getResources().getDrawable(R.drawable.sprite);
        holder.optionContent.setText(Html.fromHtml(optContent, new HtmlImageGetter(context,holder.optionContent, "/esun_msg", defaultDrawable), null));  //试题题目
//        if (optContent.contains("\r") || optContent.contains("\n")) {
//			holder.optionContent.setText(Html.fromHtml(optContent));
//		}else{
//			holder.optionContent.setText(optContent);
//		}
        
        if(qstType == 1 || qstType == 3){  //单选和判断
            if(userAnswer.equals(options.get(position).getOptOrder())){
                holder.optionImage.setBackgroundResource(R.mipmap.single_selected);
                holder.optionImage.setTextColor(context.getResources().getColor(R.color.white));
            }else{
                holder.optionImage.setBackgroundResource(R.mipmap.single_select);
                holder.optionImage.setTextColor(context.getResources().getColor(R.color.color_main));
            }
        }else if(qstType == 2){  //多选题
            if(userAnswer.contains(options.get(position).getOptOrder())){
                holder.optionImage.setBackgroundResource(R.mipmap.double_selected);
                holder.optionImage.setTextColor(context.getResources().getColor(R.color.white));
            }else{
                holder.optionImage.setBackgroundResource(R.mipmap.double_select);
                holder.optionImage.setTextColor(context.getResources().getColor(R.color.color_main));
            }
        }
        return convertView;
    }

    class ViewHolder{
        private TextView optionImage,optionContent;
    }
}
