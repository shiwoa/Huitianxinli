package com.yizhilu.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;



import com.yizhilu.application.BaseActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ConstantUtils;

/**
 * @author bin
 *         时间: 2016/6/4 11:38
 *         类说明:组卷模考的选择的类
 */
public class AssemblyExamActivity extends BaseActivity {


    RadioGroup radioGroup;  //难度的组

    TextView cancel;  //取消

    TextView startAnswer;  //开始做题

    CheckBox cb1;  //知识点练习

    CheckBox cb2;  //阶段测试

    CheckBox cb3;  //真题练习
    private int level;  //难易程度
    private StringBuffer sourceBuffer;  //试卷的来源
    private Intent intent;  //意图对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_assembly_exam);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        cancel = (TextView) findViewById(R.id.cancel);
        startAnswer = (TextView) findViewById(R.id.start_answer);
        cb1 = (CheckBox) findViewById(R.id.cb1);
        cb2 = (CheckBox) findViewById(R.id.cb2);
        cb3 = (CheckBox) findViewById(R.id.cb3);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        intent = new Intent();  //意图对象
    }

    @Override
    public void addOnClick() {
        cancel.setOnClickListener(this);  //取消
        startAnswer.setOnClickListener(this);  //开始做题
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.cancel:  //取消
                AssemblyExamActivity.this.finish();
                break;
            case R.id.start_answer:  //开始做题
                sourceBuffer = new StringBuffer();  //试卷的来源
                int id = radioGroup.getCheckedRadioButtonId();
                if (id == R.id.rb1) {
                    level = 1;
                } else if (id == R.id.rb2) {
                    level = 2;
                } else if (id == R.id.rb3) {
                    level = 3;
                } else {
                    ConstantUtils.showMsg(AssemblyExamActivity.this, "请选择难度");
                    return;
                }
                if (cb1.isChecked()) {
                    sourceBuffer.append("'theSpecialTest',"); // 知识点练习
                }
                if (cb2.isChecked()) {
                    sourceBuffer.append("'examSprint',"); // 阶段测试
                }
                if (cb3.isChecked()) {
                    sourceBuffer.append("'zhenTi',"); // 真题练习
                }
                if (!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked()) {
                    ConstantUtils.showMsg(AssemblyExamActivity.this, "请选择试卷来源");
                    return;
                }
                intent.setClass(AssemblyExamActivity.this, BeginExamActivity.class);
                intent.putExtra("examName","组卷模考");
                intent.putExtra("level",level);  //难易程度
                intent.putExtra("source",sourceBuffer.toString());  //来源
                startActivity(intent);
                AssemblyExamActivity.this.finish();
                break;
        }
    }
}
