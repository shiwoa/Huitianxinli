package com.yizhilu.exam.adapter;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yizhilu.exam.BeginExamPaperActivity;
import com.yizhilu.exam.RecordInterface;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ConstantUtils;

/**
 * 阶段测试适配器
 */
public class StageCommentAdapter extends BaseAdapter{
    private List<PublicEntity> paperList;  //阶段测试实体
    private Context context;  //上下文对象
    private RecordInterface recordInterface;  //点击接口对象
    private int type;
    public StageCommentAdapter(Context context, List<PublicEntity> paperList,int type) {
        this.context = context;
        this.paperList = paperList;
        this.type=type;
    }
    /**
     * @author bin
     * 时间: 2016/6/8 10:10
     * 方法说明:接口绑定事件
     */
    public void setRecordInterface(RecordInterface recordInterface){
        this.recordInterface = recordInterface;
    }
    @Override
    public int getCount() {
        return paperList.size();
    }

    @Override
    public Object getItem(int position) {
        return paperList.get(position);
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_stage_comment,parent,false);
            holder.name_text = (TextView) convertView.findViewById(R.id.name_text);
            holder.difficult_to = (TextView) convertView.findViewById(R.id.difficult_to);
            holder.intoExam = (TextView) convertView.findViewById(R.id.intoExam);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.difficult_to_image = (ImageView) convertView.findViewById(R.id.difficult_to_image);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name_text.setText(paperList.get(position).getName());
        holder.time.setText(paperList.get(position).getReplyTime()+"");
        if(paperList.get(position).getLevel() == 1){
            holder.difficult_to.setText("简单");
            holder.difficult_to_image.setBackgroundResource(R.mipmap.stars);
        }else if(paperList.get(position).getLevel() == 2){
            holder.difficult_to.setText("中等");
            holder.difficult_to_image.setBackgroundResource(R.mipmap.stars2);
        }else if(paperList.get(position).getLevel() == 3){
            holder.difficult_to.setText("困难");
            holder.difficult_to_image.setBackgroundResource(R.mipmap.stars3);
        }
    	  holder.intoExam.setOnClickListener(new MyOnClick(position,"进入考试"));
        return convertView;
    }

    class ViewHolder{
        //标题,难易度,时间,进入
        private TextView name_text,difficult_to,time,intoExam;
        //难易度图片
        private ImageView difficult_to_image;
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
