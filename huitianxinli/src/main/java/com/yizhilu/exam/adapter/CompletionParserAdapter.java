package com.yizhilu.exam.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;

/**
 * 作者：caibin
 * 时间：2016/6/12 16:35
 * 类说明：填空题查看解析的适配器
 */
public class CompletionParserAdapter extends BaseAdapter {
    private Context context;
    private PublicEntity publicEntity;  //实体类
    private boolean isUser;  //是否是用户答案
    public CompletionParserAdapter(Context context, PublicEntity publicEntity, boolean isuser) {
        this.context = context;
        this.publicEntity = publicEntity;
        this.isUser = isuser;
    }
    @Override
    public int getCount() {
        if (isUser)
            return publicEntity.getUserFillList().size();
        return publicEntity.getFillList().size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_completion_parser,parent,false);
            holder.textView = (TextView) convertView.findViewById(R.id.completionContent);
            holder.imageView = (ImageView) convertView.findViewById(R.id.completionImage);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        if(isUser){  //用户答案
            String userAnswer = publicEntity.getUserFillList().get(position);
            holder.textView.setText((position+1)+"、 "+userAnswer);
            holder.textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            holder.textView.getPaint().setAntiAlias(true);//抗锯齿
            String rightAnswer = publicEntity.getFillList().get(position);
            if(rightAnswer.contains(",")){
                String[] string  = rightAnswer.split(",");
                for(int i=0;i<string.length;i++){
                    if(userAnswer.equals(string[i])){
                        holder.imageView.setBackgroundResource(R.mipmap.right_question);
                        break;
                    }
                    if(i == string.length-1){
                        holder.imageView.setBackgroundResource(R.mipmap.error_question);
                    }
                }
            }else{
                if(userAnswer.equals(rightAnswer)){
                    holder.imageView.setBackgroundResource(R.mipmap.right_question);
                }else{
                    holder.imageView.setBackgroundResource(R.mipmap.error_question);
                }
            }

        }else{  //解析的正确答案
            holder.textView.setText((position+1)+"、 "+publicEntity.getFillList().get(position));
        }
        return convertView;
    }

    class ViewHolder{
        private TextView textView;
        private ImageView imageView;
    }
}
