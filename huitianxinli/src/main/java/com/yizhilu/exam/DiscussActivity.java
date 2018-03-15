package com.yizhilu.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;



import com.yizhilu.application.BaseActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ConstantUtils;

/**
 * @author bin
 *         时间: 2016/6/4 11:23
 *         类说明:讨论题的选项
 */
public class DiscussActivity extends BaseActivity {

    TextView diacussNum;  //标题

    RadioGroup radioGroup;

    TextView cancel;

    TextView startAnswer;

    RadioButton oneButton;

    RadioButton twoButton;
    private String num;  //题数
    private Intent intent;  //意图对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_discuss);
        diacussNum = (TextView) findViewById(R.id.diacuss_num);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        cancel = (TextView) findViewById(R.id.cancel);
        startAnswer = (TextView) findViewById(R.id.start_answer);
        oneButton = (RadioButton) findViewById(R.id.oneButton);
        twoButton = (RadioButton) findViewById(R.id.twoButton);

        //获取传过来的数据
        getIntentMessage();
        super.onCreate(savedInstanceState);
    }

    /**
     * @author bin
     * 时间: 2016/6/4 11:30
     * 方法说明:获取传过来的数据
     */
    public void getIntentMessage() {
        Intent intent = getIntent();
        num = intent.getStringExtra("num");
    }

    @Override
    public void initView() {
        intent = new Intent();  //意图对象
        diacussNum.setText("论述题自测-共(" + num + ")道");
    }

    @Override
    public void addOnClick() {
        cancel.setOnClickListener(this);  //取消
        startAnswer.setOnClickListener(this);  //开始做题
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.cancel:  //取消
                DiscussActivity.this.finish();
                break;
            case R.id.start_answer:  //开始做题
                int id = radioGroup.getCheckedRadioButtonId();  //获取选中的Id
                if (id == R.id.oneButton) {
                    num = "1";
                } else if (id == R.id.twoButton) {
                    num = "2";
                } else {
                    ConstantUtils.showMsg(DiscussActivity.this, "请选择题数");
                    return;
                }
                intent.setClass(DiscussActivity.this, BeginExamActivity.class);
                intent.putExtra("examName","论述题自测");
                intent.putExtra("num",num);  //题数
                startActivity(intent);
                DiscussActivity.this.finish();
                break;
        }
    }
}
