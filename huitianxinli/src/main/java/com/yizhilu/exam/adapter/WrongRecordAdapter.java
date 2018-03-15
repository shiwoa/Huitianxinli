package com.yizhilu.exam.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yizhilu.exam.RecordInterface;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;

/**
 * 错题的适配器
 */
public class WrongRecordAdapter extends BaseAdapter {
    private Context context;  //上下文对象
    private List<PublicEntity> questionList;  //学习记录的集合
    private RecordInterface recordInterface;  //接口
    //设置接口的方法
    public void setRecordInterface(RecordInterface recordInterface){
        this.recordInterface = recordInterface;
    }
    public WrongRecordAdapter(Context context, List<PublicEntity> questionList) {
        this.context = context;
        this.questionList = questionList;
    }
    @Override
    public int getCount() {
        return questionList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_wrong_record,parent,false);
            holder.exam_title = (TextView) convertView.findViewById(R.id.exam_title);  //标题
            holder.exam_time = (TextView) convertView.findViewById(R.id.exam_time);  //时间
            holder.look_parser = (TextView) convertView.findViewById(R.id.look_parser);  //查看解析
            holder.look_report = (TextView) convertView.findViewById(R.id.look_report);  //删除错题
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.exam_title.setText(questionList.get(position).getQstContent());
        holder.exam_time.setText(questionList.get(position).getErrorQuestionAddTime());
        holder.look_parser.setOnClickListener(new MyOnClick(position,"查看解析"));
        holder.look_report.setOnClickListener(new MyOnClick(position,"删除错题"));
        return convertView;
    }

    class ViewHolder{
        //标题,时间,继续做题,查看解析,查看报告
        private TextView exam_title,exam_time,look_parser,look_report;
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
