package com.yizhilu.exam;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.ExamAddress;

/**
 * @author bin
 *         时间: 2016/6/7 13:41
 *         方法说明:纠错的类
 */
public class CorrectErrorActivity extends BaseActivity {


    ImageView sideMenu;  //返回按钮

    LinearLayout leftLayout;  //返回布局

    TextView title;   //标题

    TextView rightLayoutTv;  //提交

    LinearLayout rightLayout;  //提交的布局

    EditText correctEidt;  //编辑框
    private int qstId;  //试题的Id
    private ProgressDialog progressDialog;  //加载数据显示
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_correct_error);
        sideMenu = (ImageView) findViewById(R.id.side_menu);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        title = (TextView) findViewById(R.id.title);
        rightLayoutTv = (TextView) findViewById(R.id.right_layout_tv);
        rightLayout = (LinearLayout) findViewById(R.id.right_layout);
        correctEidt = (EditText) findViewById(R.id.correct_eidt);

        //获取传过来的信息
        getIntentMessage();
        super.onCreate(savedInstanceState);
    }
    /**
     * @author bin
     * 时间: 2016/6/7 14:16
     * 方法说明:获取传过来的信息
     */
    private void getIntentMessage() {
        Intent intent = getIntent();
        qstId = intent.getIntExtra("questionId", 0);
    }

    @Override
    public void initView() {
        progressDialog = new ProgressDialog(CorrectErrorActivity.this);  //加载数据显示
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.correct_error);  //设置标题
        rightLayout.setVisibility(View.VISIBLE);  //提交布局
        rightLayoutTv.setVisibility(View.VISIBLE);
        rightLayoutTv.setText(R.string.submit);  //提交
    }

    @Override
    public void addOnClick() {
        leftLayout.setOnClickListener(this);  //返回
        rightLayout.setOnClickListener(this);  //提交
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.left_layout:  //返回
                CorrectErrorActivity.this.finish();
                break;
            case R.id.right_layout:  //提交
                String correct = correctEidt.getText().toString();
                if(TextUtils.isEmpty(correct)){
                    ConstantUtils.showMsg(CorrectErrorActivity.this,"请输入纠错内容");
                }else{
                    //提交纠错的方法
                    submitCorrection(correct);
                }
                break;
        }
    }

    /**
     * @author bin
     * 时间: 2016/6/7 14:18
     * 方法说明:提交纠错的方法
     */
    private void submitCorrection(String string) {
        RequestParams params = new RequestParams();
        params.put("paperId", 0);
        params.put("qstId", qstId);
        params.put("content", string);
        Log.i("lala", qstId+"..."+string);
        DemoApplication.getHttpClient().post(ExamAddress.SUBMITCORRECTION_URL, params , new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                ConstantUtils.showProgressDialog(progressDialog);
            }
            @Override
            public void onSuccess(int arg0, Header[] arg1, String data) {
                ConstantUtils.exitProgressDialog(progressDialog);
                Log.i("lala", data);
                if(!TextUtils.isEmpty(data)){
                    try {
                        JSONObject object = new JSONObject(data);
                        String message = object.getString("message");
                        if(object.getBoolean("success")){
                            correctEidt.setText("");
                            ConstantUtils.showMsg(CorrectErrorActivity.this, "纠错提交成功");
                            CorrectErrorActivity.this.finish();
                        }else{
                            ConstantUtils.showMsg(CorrectErrorActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                ConstantUtils.exitProgressDialog(progressDialog);
                Log.i("lala", ".........");
            }
        });
    }
}
