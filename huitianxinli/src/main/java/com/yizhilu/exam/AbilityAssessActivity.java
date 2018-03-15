package com.yizhilu.exam;

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
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.ExamAddress;

/**
 * @author bin
 *         时间: 2016/5/26 13:41
 *         类说明:能力评估的类
 */
public class AbilityAssessActivity extends BaseActivity {

    ImageView sideMenu;//返回箭头

    LinearLayout leftLayout;//返回

    TextView title;//标题

    ImageView setImage;

    LinearLayout rightLayout;

    TextView leftText;

    TextView rightLayoutTv;

    TextView ranking;//排名

    TextView totalNumber;//总题数

    TextView rightNumber;//答对题数

    TextView wrongNumber;//答错题数

    TextView averageScoreNumber;//平均分

    ImageView assessmentImage;//能力评估图片

    LinearLayout recordLayout;//考试记录

    LinearLayout errorLayout;//错题记录

    LinearLayout collectionLayout;//收藏记录

    TextView assessmentText;//能力评估字
    private int userId, subId;  //用户Id,专业Id
    private ProgressDialog progressDialog;
    private Intent intent;  //意图对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_ability_assess);
        sideMenu = (ImageView)findViewById(R.id.side_menu);
        leftLayout = (LinearLayout)findViewById(R.id.left_layout);
        title = (TextView)findViewById(R.id.title);
        setImage = (ImageView) findViewById(R.id.set_image);
        rightLayout = (LinearLayout) findViewById(R.id.right_layout);
        leftText = (TextView) findViewById(R.id.left_text);
        rightLayoutTv = (TextView) findViewById(R.id.right_layout_tv);
        ranking= (TextView)findViewById(R.id.ranking);//排
        totalNumber = (TextView)findViewById(R.id.total_number);
        rightNumber = (TextView)findViewById(R.id.right_number);
        wrongNumber = (TextView)findViewById(R.id.wrong_number);
        averageScoreNumber= (TextView)findViewById(R.id.average_score_number);
        assessmentImage = (ImageView)findViewById(R.id.assessment_image);
        recordLayout = (LinearLayout)findViewById(R.id.record_layout);
        errorLayout= (LinearLayout)findViewById(R.id.error_layout);
        collectionLayout = (LinearLayout)findViewById(R.id.collection_layout);
        assessmentText = (TextView)findViewById(R.id.assessment_text);

        super.onCreate(savedInstanceState);
    }

    /**
     * 初始化控件的方法
     */
    @Override
    public void initView() {
        intent = new Intent();
        assessmentImage.setBackgroundResource(R.mipmap.assessment_no);
        assessmentText.setTextColor(getResources().getColor(R.color.color_main));
        subId = getSharedPreferences("subjectId", MODE_PRIVATE).getInt(
                "subjectId", 0);  //获取专业的Id
        userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);  //获取用户id
        progressDialog = new ProgressDialog(AbilityAssessActivity.this);
        title.setText(getResources().getText(R.string.capability_assessment));
        sideMenu.setBackgroundResource(R.mipmap.return_button);
        leftLayout.setVisibility(View.VISIBLE);
        leftText.setVisibility(View.GONE);
        // 获取能力评估的方法
        getAbilityAssessData(userId, subId);
    }

    /**
     * 添加点击事件的方法
     */
    @Override
    public void addOnClick() {
        leftLayout.setOnClickListener(this);
        recordLayout.setOnClickListener(this);
        errorLayout.setOnClickListener(this);
        collectionLayout.setOnClickListener(this);
    }


    /**
     * 获取能力评估的方法
     */
    private void getAbilityAssessData(int userId, int subId) {
        RequestParams params = new RequestParams();
        params.put("cusId", userId);
        params.put("subId", subId);
        Log.i("lala", ExamAddress.ABILITYASSESS_URL + "?" + params);
        DemoApplication.getHttpClient().post(ExamAddress.ABILITYASSESS_URL, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                ConstantUtils.showProgressDialog(progressDialog);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ConstantUtils.exitProgressDialog(progressDialog);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String data) {
                ConstantUtils.exitProgressDialog(progressDialog);
                try {
                    if (!TextUtils.isEmpty(data)) {
                        PublicEntity publicEntity = JSON.parseObject(data,
                                PublicEntity.class);
                        String message = publicEntity.getMessage();
                        if (publicEntity.isSuccess()) {
                            PublicEntity entity = publicEntity.getEntity();
                            ranking.setText(entity.getAverageScoreRanking() + ""); //排名
                            totalNumber.setText(entity.getQstNum() + ""); // 做题总数
                            rightNumber.setText(entity.getRightQstNum() + "");  //做对题数
                            wrongNumber.setText(entity.getErrorQstNum() + ""); // 答题量
                            averageScoreNumber.setText(entity.getAverageScore() + "");  //平均分
                        } else {
                            ConstantUtils.showMsg(
                                    AbilityAssessActivity.this, message);
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_layout://返回
                this.finish();
                break;
            case R.id.record_layout://学习记录
                intent.setClass(this, StudyRecordActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.error_layout:  //错题记录
                intent.setClass(AbilityAssessActivity.this,WrongRecordActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.collection_layout://收藏
                intent.setClass(AbilityAssessActivity.this,CollectRecordActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }
}
