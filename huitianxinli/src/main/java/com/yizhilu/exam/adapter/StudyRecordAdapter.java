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
 * 作者：caibin
 * 时间：2016/6/1 19:45
 * 类说明：学习记录的适配器
 */
public class StudyRecordAdapter extends BaseAdapter {
    private Context context;  //上下文对象
    private List<PublicEntity> recordList;  //学习记录的集合
    private RecordInterface recordInterface;  //接口
    //设置接口的方法
    public void setRecordInterface(RecordInterface recordInterface){
        this.recordInterface = recordInterface;
    }
    public StudyRecordAdapter(Context context, List<PublicEntity> recordList) {
        this.context = context;
        this.recordList = recordList;
    }
    @Override
    public int getCount() {
        return recordList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_study_record,parent,false);
            holder.exam_title = (TextView) convertView.findViewById(R.id.exam_title);  //标题
            holder.exam_time = (TextView) convertView.findViewById(R.id.exam_time);  //时间
            holder.continue_study = (TextView) convertView.findViewById(R.id.continue_study);  //继续做题
            holder.look_parser = (TextView) convertView.findViewById(R.id.look_parser);  //查看解析
            holder.look_report = (TextView) convertView.findViewById(R.id.look_report);  //查看报告
            holder.not_finish = (TextView) convertView.findViewById(R.id.not_finish);  //为完成
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.exam_title.setText(recordList.get(position).getPaperName());
        holder.exam_time.setText(recordList.get(position).getAddTime());
        holder.continue_study.setText(R.string.continue_study);
        holder.look_parser.setText(R.string.look_parser);
        holder.look_report.setText(R.string.look_report);
        int status = recordList.get(position).getStatus();
        if(status == 0){
            holder.continue_study.setVisibility(View.GONE);
            holder.look_parser.setVisibility(View.VISIBLE);
            holder.look_report.setVisibility(View.VISIBLE);
            holder.not_finish.setVisibility(View.GONE);
        }else{
            holder.continue_study.setVisibility(View.VISIBLE);
            holder.not_finish.setVisibility(View.VISIBLE);
            holder.look_parser.setVisibility(View.GONE);
            holder.look_report.setVisibility(View.GONE);
        }
        holder.continue_study.setOnClickListener(new MyOnClick(position,"继续练习"));
        holder.look_parser.setOnClickListener(new MyOnClick(position,"查看解析"));
        holder.look_report.setOnClickListener(new MyOnClick(position,"查看报告"));
        return convertView;
    }

    class ViewHolder{
        //标题,时间,继续做题,查看解析,查看报告,未完成
        private TextView exam_title,exam_time,continue_study,look_parser,look_report,not_finish;
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
