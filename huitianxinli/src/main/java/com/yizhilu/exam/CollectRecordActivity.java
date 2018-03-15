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
import com.yizhilu.exam.adapter.CollectRecordAdapter;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.ExamAddress;
import com.yizhilu.view.NoScrollListView;

/**
 * @author bin
 *         时间: 2016/6/2 11:50
 *         类说明:收藏试题的类
 */
public class CollectRecordActivity extends BaseActivity implements RecordInterface {

    ImageView sideMenu;  //返回图片

    LinearLayout leftLayout;  //返回布局

    TextView title;

    LinearLayout assessmentLayout;  //能力评估布局

    LinearLayout errorLayout;  //错题记录布局

    LinearLayout recordLayout;  //学习记录布局

    ImageView collectionImage;  //收藏图片

    TextView collectionText;  //收藏文字

    TextView totalNumber;  //收藏题数

    NoScrollListView collectRecordList;  //收藏试题的列表

    PullToRefreshScrollView refreshScrollView;  //刷新

    TextView nullText;  //空布局显示的文字

    LinearLayout nullLayout;  //无内容显示
    private int userId, subjectId, page = 1;  //用户id,专业的Id,页数
    private ProgressDialog progressDialog;  //加载数据显示
    private Intent intent;  //意图对象
    private List<PublicEntity> collectList;  //收藏的集合
    private CollectRecordAdapter collectRecordAdapter;  //收藏的适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_collect_record);
        sideMenu = (ImageView) findViewById(R.id.side_menu);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        title = (TextView) findViewById(R.id.title);
        assessmentLayout = (LinearLayout) findViewById(R.id.assessment_layout);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        recordLayout = (LinearLayout) findViewById(R.id.record_layout);
        collectionImage = (ImageView) findViewById(R.id.collection_image);
        collectionText = (TextView) findViewById(R.id.collection_text);
        totalNumber = (TextView) findViewById(R.id.total_number);
        collectRecordList = (NoScrollListView) findViewById(R.id.collectRecordList);
        refreshScrollView = (PullToRefreshScrollView) findViewById(R.id.refreshScrollView);
        nullText = (TextView) findViewById(R.id.null_text);
        nullLayout = (LinearLayout) findViewById(R.id.null_layout);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        intent = new Intent();  //意图对象
        progressDialog = new ProgressDialog(CollectRecordActivity.this); //加载数据显示的dialog
        collectList = new ArrayList<PublicEntity>();  //收藏的集合
        userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);  //获取用户id
        subjectId = getSharedPreferences("subjectId", MODE_PRIVATE).getInt("subjectId", 0);  //获取专业id
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.collection);  //设置标题
        collectionImage.setBackgroundResource(R.mipmap.collected);
        collectionText.setTextColor(getResources().getColor(R.color.color_main));
        refreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);  //设置加载模式
        //联网获取收藏习题的方法
        getCollectExerciseData(userId, subjectId, page);
    }

    @Override
    public void addOnClick() {
        leftLayout.setOnClickListener(this);  //返回的事件
        assessmentLayout.setOnClickListener(this);  //能力评估
        recordLayout.setOnClickListener(this);  //学习记录
        errorLayout.setOnClickListener(this);  //错题记录
        refreshScrollView.setOnRefreshListener(this);  //刷新事件
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_layout:  //返回的布局
                this.finish();
                break;
            case R.id.assessment_layout: //能力评估
                intent.setClass(CollectRecordActivity.this, AbilityAssessActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.error_layout:  //错题记录
                intent.setClass(CollectRecordActivity.this, WrongRecordActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.record_layout:
                intent.setClass(CollectRecordActivity.this, StudyRecordActivity.class);
                startActivity(intent);
                this.finish();
                break;
        }
    }

    /**
     * 联网获取收藏习题的方法
     */
    private void getCollectExerciseData(int userId, int subId, final int pageSize) {
        RequestParams params = new RequestParams();
        params.put("cusId", userId);
        params.put("subId", subId);
        params.put("page.currentPage", pageSize);
        Log.i("lala", ExamAddress.COLLECTEXERCISE_URL + "?" + params.toString());
        DemoApplication.getHttpClient().post(ExamAddress.COLLECTEXERCISE_URL, params, new TextHttpResponseHandler() {
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
                        PublicEntity publicEntity = JSONObject.parseObject(data, PublicEntity.class);
                        String message = publicEntity.getMessage();
                        if (publicEntity.isSuccess()) {
                            totalNumber.setText(publicEntity.getEntity().getPage().getTotalResultSize() + "");
                            if (pageSize == 1) {
                                collectList.clear();
                            }
                            if (pageSize == publicEntity.getEntity()
                                    .getPage().getTotalPageSize()) {
                                refreshScrollView
                                        .setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                            }
                            if (data.contains("queryQuestionList")) {
                                for (int i = 0; i < publicEntity
                                        .getEntity().getQueryQuestionList()
                                        .size(); i++) {
                                    collectList.add(publicEntity
                                            .getEntity()
                                            .getQueryQuestionList().get(i));
                                }
                            }
                            if(collectList.size()<=0){
                                refreshScrollView.setVisibility(View.GONE);
                                nullLayout.setVisibility(View.VISIBLE);
                                nullText.setText("无收藏试题");
                            }else{refreshScrollView.setVisibility(View.VISIBLE);
                                nullLayout.setVisibility(View.GONE);
                            }
                            collectRecordAdapter = new CollectRecordAdapter(
                                    CollectRecordActivity.this, collectList);
                            collectRecordAdapter.setRecordInterface(CollectRecordActivity.this);  //绑定点击事件
                            collectRecordList.setAdapter(collectRecordAdapter);
                            refreshScrollView.onRefreshComplete();
                        } else {
                            ConstantUtils.showMsg(CollectRecordActivity.this, message);
                            refreshScrollView.onRefreshComplete();
                        }
                    } catch (Exception e) {
                        refreshScrollView.onRefreshComplete();
                    }
                }
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                ConstantUtils.exitProgressDialog(progressDialog);
                refreshScrollView.onRefreshComplete();

            }
        });
    }

    @Override
    public void myClick(int position, String name) {
        int id = collectList.get(position).getId();
        if ("查看解析".equals(name)) {
            intent.setClass(CollectRecordActivity.this, SingleLookParserActivity.class);
            intent.putExtra("id", id);  //试题的Id
            startActivity(intent);
        } else if ("取消收藏".equals(name)) {
            //取消收藏的方法
            cancelCollectTi(userId, subjectId, id);
        }
    }

    /**
     * @author bin
     * 时间: 2016/6/7 10:33
     * 方法说明:取消收藏的方法
     */
    private void cancelCollectTi(final int userId, int subId, int id) {
        RequestParams params = new RequestParams();
        params.put("cusId", userId);
        params.put("subId", subId);
        params.put("qstId", id);
        DemoApplication.getHttpClient().post(ExamAddress.CANCELCOLLECT_URL, params, new TextHttpResponseHandler() {
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
                        PublicEntity publicEntity = JSONObject.parseObject(data, PublicEntity.class);
                        String message = publicEntity.getMessage();
                        if (publicEntity.isSuccess()) {
                            page = 1;
                            collectList.clear();
//                            refreshScrollView.setMode(Mode.BOTH);
                            //联网获取收藏习题的方法
                            getCollectExerciseData(userId, subjectId, page);
                        } else {
                            ConstantUtils.showMsg(CollectRecordActivity.this, message);
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
     * 时间: 2016/6/7 20:58
     * 方法说明:下拉刷新
     */
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullDownToRefresh(refreshView);
        page = 1;
        refreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        //联网获取收藏习题的方法
        getCollectExerciseData(userId, subjectId, page);
    }

    /**
     * @author bin
     * 时间: 2016/6/7 20:58
     * 方法说明:上拉加载
     */
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        super.onPullUpToRefresh(refreshView);
        page++;
        //联网获取收藏习题的方法
        getCollectExerciseData(userId, subjectId, page);
    }
}
