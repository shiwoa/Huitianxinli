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



import com.alibaba.fastjson.JSONObject;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.exam.adapter.StudyRecordAdapter;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.ExamAddress;
import com.yizhilu.view.NoScrollListView;

/**
 * @author bin
 *         时间: 2016/5/26 13:59
 *         方法说明:学习记录的页面
 */
public class StudyRecordActivity extends BaseActivity implements RecordInterface {


    ImageView sideMenu;  //返回图片

    LinearLayout leftLayout;  //返回布局

    TextView title;

    NoScrollListView studyRecordList;  //学习记录的列表

    LinearLayout assessmentLayout;  //能力评估布局

    ImageView recordImage;  //考试记录图片

    TextView recordText;  //考试记录文字

    LinearLayout errorLayout;  //错题记录布局

    LinearLayout collectionLayout;  //收藏试题布局

    PullToRefreshScrollView refreshScrollView;  //下拉刷新和加载

    TextView nullText;  //无内容显示文字

    LinearLayout nullLayout;  //无内容显示布局
    private int userId, subjectId, page = 1;  //用户id,专业的Id,页数
    private ProgressDialog progressDialog;  //加载数据显示
    private List<PublicEntity> recordList;  //学习记录的集合
    private StudyRecordAdapter recordAdapter;  //学习记录的适配器
    private Intent intent;  //意图对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_study_record);
        sideMenu = (ImageView) findViewById(R.id.side_menu);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        title = (TextView) findViewById(R.id.title);
        assessmentLayout = (LinearLayout) findViewById(R.id.assessment_layout);
        recordImage = (ImageView) findViewById(R.id.record_image);
        recordText = (TextView) findViewById(R.id.record_text);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        collectionLayout = (LinearLayout) findViewById(R.id.collection_layout);
        refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView);
        nullText = (TextView) findViewById(R.id.null_text);
        nullLayout = (LinearLayout) findViewById(R.id.null_layout);
        studyRecordList = (NoScrollListView) findViewById(R.id.studyRecordList);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        intent = new Intent();  //意图对象
        recordList = new ArrayList<PublicEntity>();  //学习记录的集合
        progressDialog = new ProgressDialog(StudyRecordActivity.this); //加载数据显示的dialog
        userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);  //获取用户id
        subjectId = getSharedPreferences("subjectId", MODE_PRIVATE).getInt("subjectId", 0);  //获取专业id
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.exam_record);  //设置标题
        recordImage.setBackgroundResource(R.mipmap.recorded);
        recordText.setTextColor(getResources().getColor(R.color.color_main));
        refreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);  //设置加载模式
        //获取考试记录的方法
        getExamRecord(userId, subjectId, page);
    }

    @Override
    public void addOnClick() {
        leftLayout.setOnClickListener(this);  //返回的事件
        assessmentLayout.setOnClickListener(this);  //能力评估
        errorLayout.setOnClickListener(this);  //错题记录
        collectionLayout.setOnClickListener(this);  //收藏
        refreshScrollView.setOnRefreshListener(this);  //刷新的加载的类
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_layout:  //返回的布局
                this.finish();
                break;
            case R.id.assessment_layout: //能力评估
                intent.setClass(StudyRecordActivity.this, AbilityAssessActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.error_layout:  //错题记录
                intent.setClass(StudyRecordActivity.this, WrongRecordActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.collection_layout:
                intent.setClass(StudyRecordActivity.this, CollectRecordActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }
    /**
     * @author bin
     * 时间: 2016/6/1 18:18
     * 方法说明:获取学习记录的方法
     */
    /**
     * 联网获取练习历史的方法
     */
    private void getExamRecord(int userId, int subId, final int page) {
        RequestParams params = new RequestParams();
        params.put("cusId", userId);
        params.put("subId", subId);
        params.put("page.currentPage", page);
        Log.i("lala", ExamAddress.PRACTICEHISTORY_URL + "?" + params.toString());
        DemoApplication.getHttpClient().post(ExamAddress.PRACTICEHISTORY_URL, params,
                new TextHttpResponseHandler() {
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
                                PublicEntity publicEntity = JSONObject
                                        .parseObject(data, PublicEntity.class);
                                String message = publicEntity.getMessage();
                                if (publicEntity.isSuccess()) {
                                    if (page == 1) {
                                        recordList.clear();
                                    }
                                    PublicEntity entity = publicEntity.getEntity();
                                    if (entity == null) {
                                        refreshScrollView.setVisibility(View.GONE);
                                        nullLayout.setVisibility(View.VISIBLE);
                                        nullText.setText("无考试记录");
                                        return;
                                    }
                                    if (entity.getPage().getTotalPageSize() == page) {
                                        refreshScrollView
                                                .setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                                    }
                                    List<PublicEntity> paperRecordList = entity
                                            .getQueryPaperRecordList();
                                    if (paperRecordList != null
                                            && paperRecordList.size() > 0) {
                                        for (int i = 0; i < paperRecordList
                                                .size(); i++) {
                                            recordList.add(publicEntity
                                                    .getEntity()
                                                    .getQueryPaperRecordList()
                                                    .get(i));
                                        }
                                        if (recordList != null
                                                && recordList.size() > 0) {
                                            recordAdapter = new StudyRecordAdapter(StudyRecordActivity.this,
                                                    recordList);
                                            recordAdapter.setRecordInterface(StudyRecordActivity.this);
                                            studyRecordList.setAdapter(recordAdapter);
                                        }
                                    }
                                    if (recordList.size() <= 0) {
                                        refreshScrollView.setVisibility(View.GONE);
                                        nullLayout.setVisibility(View.VISIBLE);
                                        nullText.setText("无考试记录");
                                    } else {
                                        nullLayout.setVisibility(View.GONE);
                                        refreshScrollView.setVisibility(View.VISIBLE);
                                    }
                                    refreshScrollView.onRefreshComplete();
                                } else {
                                    ConstantUtils.showMsg(StudyRecordActivity.this,
                                            message);
                                    refreshScrollView.onRefreshComplete();
                                }
                            } catch (Exception e) {
                                refreshScrollView.onRefreshComplete();
                                refreshScrollView.setVisibility(View.GONE);
                                nullLayout.setVisibility(View.VISIBLE);
                                nullText.setText("无考试记录");
                            }
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, String arg2,
                                          Throwable arg3) {
                        ConstantUtils.exitProgressDialog(progressDialog);
//                        refreshScrollView.onRefreshComplete();
                    }
                });
    }

    @Override
    public void myClick(int position, String name) {
        if ("继续练习".equals(name)) {
            int type = recordList.get(position).getType();
            if (type != 2) {
                intent.setClass(StudyRecordActivity.this, BeginExamActivity.class);
                intent.putExtra("examName", "继续练习");
                intent.putExtra("continueId", recordList.get(position).getId());
                startActivity(intent);
            } else {
                intent.setClass(StudyRecordActivity.this, BeginExamPaperActivity.class);
                intent.putExtra("examName", "继续练习");
                intent.putExtra("continueId", recordList.get(position).getId());
                startActivity(intent);
            }
        } else if ("查看解析".equals(name)) {
            int type = recordList.get(position).getType();
            int id = recordList.get(position).getId();
            if (type != 2) {  //练习
                intent.setClass(StudyRecordActivity.this, LookParserActivity.class);
                intent.putExtra("id", id);  //试题的Id
                startActivity(intent);
            } else {  //考试
                intent.setClass(StudyRecordActivity.this, LookParserActivity.class);
                intent.putExtra("id", id);  //试题的Id
                startActivity(intent);
            }
        } else if ("查看报告".equals(name)) {
            Intent intent = new Intent(StudyRecordActivity.this, PracticeReportActivity.class);
            intent.putExtra("paperId", recordList.get(position).getId());
            startActivity(intent);
        }
    }

    /**
     * @author bin
     * 时间: 2016/6/7 20:22
     * 方法说明:下拉刷新
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        page = 1;
        refreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        //获取考试记录的方法
        getExamRecord(userId, subjectId, page);
    }

    /**
     * @author bin
     * 时间: 2016/6/7 20:22
     * 方法说明:上拉加载
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);
        page++;
        //获取考试记录的方法
        getExamRecord(userId, subjectId, page);
    }
}
