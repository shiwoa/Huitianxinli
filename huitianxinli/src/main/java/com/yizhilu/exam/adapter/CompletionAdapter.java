package com.yizhilu.exam.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.exam.utils.StaticUtils;
import com.yizhilu.huitianxinli.R;

/**
 * 作者：caibin
 * 时间：2016/6/7 15:39
 * 类说明：填空题的适配器
 */
public class CompletionAdapter extends BaseAdapter {
    private Context context;
    private PublicEntity publicEntity;  //实体
    private int questionPosition;  //试题的下标
    private List<String> answerList; // 答案的集合
    private String userAnswer;  //用户填写的答案
    private StringBuffer buffer;  //拼接答案
    private EditText editText;  //当前输入的
    private int textPosition = -1;  //当前修改的填空的下标
    public CompletionAdapter(Context context, PublicEntity publicEntity, int position,List<String> completionList) {
        this.context = context;
        this.publicEntity = publicEntity;
        this.questionPosition = position;
        this.answerList = completionList;
        userAnswer = StaticUtils.getPositionAnswer(questionPosition-1);  //获取用户的答案
        Log.i("lala",userAnswer+"..."+(questionPosition-1));
        if(!TextUtils.isEmpty(userAnswer)){
            if (userAnswer.contains(",")) {
                String[] split = userAnswer.split(",");
                for (int i = 0; i < split.length; i++) {
                    answerList.set(i, split[i]);
                }
            }else{
                answerList.set(0, userAnswer);
            }
        }
    }
    @Override
    public int getCount() {
        return publicEntity.getFillList().size();
    }

    @Override
    public Object getItem(int position) {
        return publicEntity.getFillList().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_completion,parent,false);
            holder.editText = (EditText) convertView.findViewById(R.id.completion_edit);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        String answer = answerList.get(position);
        if(TextUtils.isEmpty(answerList.get(position))){
            holder.editText.setHint("第"+(position+1)+"题 请输入答案");
        }else{
            holder.editText.setText(answer);
        }
        holder.editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                EditText edit = (EditText) v.findViewById(R.id.completion_edit);
                if(!hasFocus){  //失去焦点
                    buffer = new StringBuffer();
                    answerList.set(position, edit.getText().toString());
                    for (int i = 0; i < answerList.size(); i++) {
                        String aa = answerList.get(i);
                        if (TextUtils.isEmpty(aa)) {
                            buffer.append(",");
                        } else {
                            buffer.append(aa + ",");
                        }
                    }
                    if (buffer.toString().contains(",")) {
                        buffer.deleteCharAt(buffer.length() - 1);
                    }
                    if (buffer.toString().contains(",")&&buffer.toString().length() == 1) {
                    	StaticUtils.setPositionAnswer(questionPosition-1, "");
					}else{
						StaticUtils.setPositionAnswer(questionPosition-1, buffer.toString());
					}
                    Log.i("lala",StaticUtils.getPositionAnswer(questionPosition-1)+"。。。"+(questionPosition-1));
                }else{
                    editText = edit;
                    textPosition = position;
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        private EditText editText;  //输入框
    }
    /**
     * @author bin
     * 时间: 2016/6/7 17:10
     * 方法说明:当填空界面不可见时调用 保存填空答案
     */
    public void setAnswer(){
        if(textPosition != -1){
            buffer = new StringBuffer();
            answerList.set(textPosition, editText.getText().toString());
            for (int i = 0; i < answerList.size(); i++) {
                String aa = answerList.get(i);
                if (aa.equals("")) {
                    buffer.append(" ,");
                } else {
                    buffer.append(aa + ",");
                }
            }
            if (buffer.toString().contains(",")) {
                buffer.deleteCharAt(buffer.length() - 1);
            }
            if (buffer.toString().contains(",")&&buffer.toString().length() == 1) {
            	StaticUtils.setPositionAnswer(questionPosition-1, "");
			}else{
				StaticUtils.setPositionAnswer(questionPosition-1, buffer.toString());
			}
//            StaticUtils.setPositionAnswer(questionPosition-1,buffer.toString());
        }
    }
}
