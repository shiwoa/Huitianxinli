package com.yizhilu.exam.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.exam.utils.HtmlImageGetter;
import com.yizhilu.exam.utils.StaticUtils;
import com.yizhilu.huitianxinli.R;

/**
 * 作者：caibin
 * 时间：2016/6/4 18:30
 * 类说明：选项的适配器
 */
public class OptionsAdapter extends BaseAdapter {
    private Context context;
    private PublicEntity publicEntity;  //实体
    private String optionAnswer;  //选项的答案
    private int questionPosition;  //试题的下标
    public OptionsAdapter(Context context, PublicEntity publicEntity,int position) {
        this.context = context;
        this.publicEntity = publicEntity;
        this.questionPosition = position;
    }

    @Override
    public int getCount() {
        return publicEntity.getOptions().size();
    }

    @Override
    public Object getItem(int position) {
        return publicEntity.getOptions().get(position);
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
        int qstType = publicEntity.getQstType();
        if(qstType == 0){
            qstType = publicEntity.getQuestionType();
        }
        List<PublicEntity> options = publicEntity.getOptions();
        holder.optionImage.setText(options.get(position).getOptOrder());
//        holder.optionContent.setText(options.get(position).getOptContent().trim());
        String optContent = options.get(position).getOptContent();
        Drawable defaultDrawable = context.getResources().getDrawable(R.drawable.sprite);
        holder.optionContent.setText(Html.fromHtml(optContent, new HtmlImageGetter(context,holder.optionContent, "/esun_msg", defaultDrawable), null));  //试题题目
//		holder.optionContent.setText(Html.fromHtml(optContent));
        optionAnswer = StaticUtils.getPositionAnswer(questionPosition-1);  //获取选中的答案
        if(qstType == 1 || qstType == 3){  //单选和判断
            if(optionAnswer.equals(options.get(position).getOptOrder())){
                holder.optionImage.setBackgroundResource(R.mipmap.single_selected);
                holder.optionImage.setTextColor(context.getResources().getColor(R.color.white));
            }else{
                holder.optionImage.setBackgroundResource(R.mipmap.single_select);
                holder.optionImage.setTextColor(context.getResources().getColor(R.color.color_main));
            }
        }else if(qstType == 2){  //多选题
            if(optionAnswer.contains(options.get(position).getOptOrder())){
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
