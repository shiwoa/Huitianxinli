package com.yizhilu.exam;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.exam.adapter.WrongRecordAdapter;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.exam.entity.StringEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.ExamAddress;
import com.yizhilu.view.NoScrollListView;

/**
 * 错题记录
 */
public class WrongRecordActivity extends BaseActivity implements RecordInterface {



    ImageView sideMenu;

    LinearLayout leftLayout;

    TextView title;

    TextView totalNumber;

    NoScrollListView wrongRecordList;

    LinearLayout assessmentLayout;

    LinearLayout recordLayout;

    ImageView errorImage;

    TextView errorText;

    LinearLayout collectionLayout;

    PullToRefreshScrollView refreshScrollView;
    //刷新的布局

    TextView nullText;
    //无内容显示文字

    LinearLayout nullLayout;
    //无内容显示布局
    private ProgressDialog progressDialog;  //加载数据显示
    private int userId, subjectId, page = 1;  //用户id,专业的Id,页数
    private List<PublicEntity> questionList; // 错误习题的集合
    private WrongRecordAdapter adapter;//错题适配器
    private Intent intent;  //意图对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_wrong_record);
        sideMenu = (ImageView) findViewById(R.id.side_menu);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        title = (TextView) findViewById(R.id.title);
        totalNumber = (TextView) findViewById(R.id.total_number);
        wrongRecordList = (NoScrollListView) findViewById(R.id.wrongRecordList);
        assessmentLayout = (LinearLayout) findViewById(R.id.assessment_layout);
        recordLayout = (LinearLayout) findViewById(R.id.record_layout);
        errorImage = (ImageView) findViewById(R.id.error_image);
        errorText = (TextView) findViewById(R.id.error_text);
        collectionLayout = (LinearLayout) findViewById(R.id.collection_layout);
        refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView);
        nullText = (TextView) findViewById(R.id.null_text);
        nullLayout = (LinearLayout) findViewById(R.id.null_layout);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        intent = new Intent();  //意图对象
        questionList = new ArrayList<PublicEntity>();  //错误习题的集合
        progressDialog = new ProgressDialog(WrongRecordActivity.this); //加载数据显示的dialog
        userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);  //获取用户id
        subjectId = getSharedPreferences("subjectId", MODE_PRIVATE).getInt("subjectId", 0);  //获取专业id
        sideMenu.setBackgroundResource(R.mipmap.return_button);
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        title.setText(R.string.wrong_record);  //设置标题
        errorImage.setBackgroundResource(R.mipmap.error_no);
        errorText.setTextColor(getResources().getColor(R.color.color_main));
        refreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);  //设置加载模式
        // 联网获取错误习题的方法
        getErrorExerciseData(userId, subjectId, page);
    }

    @Override
    public void addOnClick() {
        leftLayout.setOnClickListener(this);
        assessmentLayout.setOnClickListener(this);//能力评估
        collectionLayout.setOnClickListener(this);  //收藏
        recordLayout.setOnClickListener(this);//学习记录
        refreshScrollView.setOnRefreshListener(this);  //刷新
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_layout://返回
                this.finish();
                break;
            case R.id.assessment_layout: //能力评估
                intent.setClass(WrongRecordActivity.this, AbilityAssessActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.record_layout:  //学习记录
                intent.setClass(WrongRecordActivity.this, StudyRecordActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.collection_layout://收藏
                intent.setClass(WrongRecordActivity.this, CollectRecordActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }

    /**
     * 联网获取错误习题的方法
     */
    private void getErrorExerciseData(int userId, int subId, final int pageSize) {
        RequestParams params = new RequestParams();
        params.put("cusId", userId);
        params.put("subId", subId);
        params.put("page.currentPage", pageSize);
        Log.i("lala", ExamAddress.ERROREXERCISE_URL + "?" + params.toString());
        DemoApplication.getHttpClient().post(ExamAddress.ERROREXERCISE_URL, params, new TextHttpResponseHandler() {

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
                if (!TextUtils.isEmpty(data)) {
                    try {
                        PublicEntity publicEntity = JSONObject.parseObject(data, PublicEntity.class);
                        String message = publicEntity.getMessage();
                        if (publicEntity.isSuccess()) {
                            totalNumber.setText(publicEntity.getEntity().getPage().getTotalResultSize() + "");
                            if (pageSize == 1) {
                                questionList.clear();
                            }
                            if (pageSize == publicEntity.getEntity().getPage().getTotalPageSize()) {
                                refreshScrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }
                            List<PublicEntity> paperRecordList = publicEntity.getEntity().getQueryQuestionList();
                            if (paperRecordList != null && paperRecordList.size() > 0) {
                                for (int i = 0; i < paperRecordList.size(); i++) {
                                    questionList.add(paperRecordList.get(i));
                                }
                            }
                            if(questionList.size()<=0){
                                refreshScrollView.setVisibility(View.GONE);
                                nullLayout.setVisibility(View.VISIBLE);
                                nullText.setText("无错误试题");
                            }else{
                                refreshScrollView.setVisibility(View.VISIBLE);
                                nullLayout.setVisibility(View.GONE);
                            }
                            adapter = new WrongRecordAdapter(WrongRecordActivity.this, questionList);
                            adapter.setRecordInterface(WrongRecordActivity.this);
                            wrongRecordList.setAdapter(adapter);
                            refreshScrollView.onRefreshComplete();
                        } else {
                            ConstantUtils.showMsg(WrongRecordActivity.this, message);
                            refreshScrollView.onRefreshComplete();
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    //
    @Override
    public void myClick(int position, String name) {
        if ("删除错题".equals(name)) {
            int errorQuestionId = questionList.get(position).getErrorQuestionId();
            //移除错题的方法
            removeErrorTi(errorQuestionId);
        } else if ("查看解析".equals(name)) {
            int id = questionList.get(position).getId();
            intent.setClass(WrongRecordActivity.this, SingleLookParserActivity.class);
            intent.putExtra("id", id);  //试题的Id
            startActivity(intent);
        }
    }


    /**
     * 移除错题的方法
     */
    private void removeErrorTi(int id) {
        ConstantUtils.showProgressDialog(progressDialog);
        RequestParams params = new RequestParams();
        params.put("ids", id);
        Log.i("lala", ExamAddress.REMOVEERRORTI_URL + "?" + params);
        DemoApplication.getHttpClient().post(ExamAddress.REMOVEERRORTI_URL, params, new TextHttpResponseHandler() {
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
                        StringEntity stringEntity = JSON.parseObject(data, StringEntity.class);
                        if (stringEntity.isSuccess()) {
                            page = 1;
                            questionList.clear();
                            // 联网获取错误习题的方法
                            getErrorExerciseData(userId, subjectId, page);
                        } else {
                            ConstantUtils.showMsg(WrongRecordActivity.this, stringEntity.getMessage());
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
     * 时间: 2016/6/7 20:54
     * 方法说明：下拉刷新
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);
        page++;
        // 联网获取错误习题的方法
        getErrorExerciseData(userId, subjectId, page);
    }

    /**
     * @author bin
     * 时间: 2016/6/7 20:54
     * 方法说明:上拉加载
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        page = 1;
        refreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        // 联网获取错误习题的方法
        getErrorExerciseData(userId, subjectId, page);
    }
}
