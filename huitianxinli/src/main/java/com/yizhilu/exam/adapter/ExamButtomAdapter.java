package com.yizhilu.exam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.huitianxinli.R;

/**
 * 作者：caibin
 * 时间：2016/6/4 15:06
 * 类说明：考试底部按钮的适配器
 */
public class ExamButtomAdapter extends BaseAdapter {
    private int images[];
    private int type;
    private int texts[];
    private Context context;  //上下文对象
    public ExamButtomAdapter(Context context,int images[],int texts[],int type) {
        this.context = context;
        this.images = images;
        this.texts = texts;
        this.type = type;
    }

    @Override
    public int getCount() {
        return images.length;
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.item_exam_buttom, parent,false);
            holder.image = (ImageView) convertView
                    .findViewById(R.id.imageView);
            holder.text = (TextView) convertView
                    .findViewById(R.id.textView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.image.setBackgroundResource(images[position]);
        holder.text.setText(texts[position]);
        if(texts[position] == R.string.collected){
            holder.text.setTextColor(context.getResources().getColor(R.color.color_FF9704));
        }else{
            if (type == 1) {
                holder.text.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.text.setTextColor(context.getResources().getColor(R.color.color_33));
            }
        }
        return convertView;
    }

    class ViewHolder {
        private ImageView image;
        private TextView text;
    }
}
