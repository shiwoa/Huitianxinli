package com.yizhilu.exam;

import java.math.BigDecimal;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.alibaba.fastjson.JSON;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.exam.adapter.PracticeConditionAdapter;
import com.yizhilu.exam.entity.ListEntity;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ExamAddress;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.view.NoScrollExpandableListView;

//练习报告
public class PracticeReportActivity extends BaseActivity {

    ImageView sideMenu;  //返回图片

    LinearLayout leftLayout;  //返回布局

    TextView title;  //标题

    TextView reportType;  //练习类型

    TextView submitTime;  //交卷时间

    TextView rightNumber;  //正确题数

    TextView zongNumber;  //总题数

    TextView answerTime;  //答题用时

    TextView averageTime;  //平均用时

    TextView correctRate;  //正确率

    NoScrollExpandableListView reportListView;  //知识点练习情况的列表

    TextView lookParser;  //查看解析
    private int paperId;  //做完题的试卷或练习的Id
    private int userId, subjectId;//用户id,专业id
    private ProgressDialog progressDialog;  //加载数据显示
    private int paperType;  //试卷类型
    private Intent intent;  //意图对象
    private PublicEntity paperRecord;  //报告记录的实体
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_practice_report);
        sideMenu = (ImageView) findViewById(R.id.side_menu);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        title = (TextView) findViewById(R.id.title);
        reportType = (TextView) findViewById(R.id.reportType);
        submitTime = (TextView) findViewById(R.id.submitTime);
        rightNumber = (TextView) findViewById(R.id.right_number);
        zongNumber = (TextView) findViewById(R.id.zongNumber);
        answerTime = (TextView) findViewById(R.id.answer_time);
        averageTime = (TextView) findViewById(R.id.average_time);
        correctRate = (TextView) findViewById(R.id.correct_rate);
        reportListView = (NoScrollExpandableListView) findViewById(R.id.reportListView);
        lookParser = (TextView) findViewById(R.id.lookParser);

        //获取传过来的信息
        getIntentMessage();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        intent = new Intent();  //意图对象
        progressDialog = new ProgressDialog(PracticeReportActivity.this);  //加载数据显示
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.practice_report);  //设置标题
        //联网获取报告的方法
        getHttpReportData(paperId);
        //联网获取每个考点的练习情况
        getHttpPracticeCondition(userId, subjectId, paperId);
    }

    /**
     * 获取传过来的参数
     */
    private void getIntentMessage() {
        Intent intent = getIntent();
        paperId = intent.getIntExtra("paperId", 0);
        userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);  //获取用户id
        subjectId = getSharedPreferences("subjectId", MODE_PRIVATE).getInt("subjectId", 0);  //获取专业id
    }

    @Override
    public void addOnClick() {
        leftLayout.setOnClickListener(this);  //返回
        lookParser.setOnClickListener(this);  //查看解析
    }

    /**
     * @author bin
     * 时间: 2016/6/12 9:21
     * 方法说明:联网获取练习报告的方法
     */
    private void getHttpReportData(int paperId) {
        RequestParams params = new RequestParams();
        params.put("id", paperId);
        params.put("cusId", userId);
        params.put("subId", subjectId);
        Log.i("lala", ExamAddress.PRACTICEREPORT_URL + "?" + params.toString());
        DemoApplication.getHttpClient().post(ExamAddress.PRACTICEREPORT_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                ConstantUtils.showProgressDialog(progressDialog);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String data) {
                ConstantUtils.exitProgressDialog(progressDialog);
                if (!TextUtils.isEmpty(data)) {
                    try {
                        PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
                        String message = publicEntity.getMessage();
                        if (publicEntity.isSuccess()) {
                            paperRecord = publicEntity.getEntity().getPaperRecord();
                            paperType = paperRecord.getType();
                            reportType.setText(paperRecord.getPaperName());  //试卷类型
                            submitTime.setText(paperRecord.getAddTime() + "");  //交卷时间
                            rightNumber.setText(paperRecord.getCorrectNum() + "");  //答对题数
                            zongNumber.setText("/" + paperRecord.getQstCount() + "道");  //总题数
                            correctRate.setText((int) (paperRecord.getAccuracy() * 100) + "");  //正确率
                            answerTime.setText(paperRecord.getTestTime() + "");  //答题用时
                            double aa = (double) paperRecord.getTestTime() / paperRecord.getQstCount();
                            BigDecimal b = new BigDecimal(aa);
                            double f1 = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                            averageTime.setText(f1 + "");  //平均用时
                        } else {
                            ConstantUtils.showMsg(PracticeReportActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                ConstantUtils.exitProgressDialog(progressDialog);
            }
        });
    }

    /**
     * @author bin
     * 时间: 2016/6/12 11:01
     * 方法说明:联网获取每个考点的练习情况
     */
    private void getHttpPracticeCondition(int userId, int subId, int paperRecordId) {
        RequestParams params = new RequestParams();
        params.put("cusId", userId);
        params.put("subId", subId);
        params.put("paperRecordId", paperRecordId);
        Log.i("lala", ExamAddress.PRACTICECONDITION_URL + "?" + params.toString());
        DemoApplication.getHttpClient().post(ExamAddress.PRACTICECONDITION_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                ConstantUtils.showProgressDialog(progressDialog);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String data) {
                ConstantUtils.exitProgressDialog(progressDialog);
                if (!TextUtils.isEmpty(data)) {
                    try {
                        ListEntity listEntity = JSON.parseObject(data, ListEntity.class);
                        PracticeConditionAdapter adapter = new PracticeConditionAdapter(PracticeReportActivity.this, listEntity);
                        reportListView.setAdapter(adapter);
                    } catch (Exception e) {
                    }
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                ConstantUtils.exitProgressDialog(progressDialog);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.left_layout:  //返回
                PracticeReportActivity.this.finish();
                break;
            case R.id.lookParser:  //查看解析
                intent.setClass(PracticeReportActivity.this,LookParserActivity.class);
                intent.putExtra("id",paperRecord.getId());
                startActivity(intent);
                PracticeReportActivity.this.finish();
                break;
        }
    }
}
