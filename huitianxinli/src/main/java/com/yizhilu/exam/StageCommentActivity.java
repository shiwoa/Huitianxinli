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
import android.widget.ListView;
import android.widget.TextView;



import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.exam.adapter.StageCommentAdapter;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.ExamAddress;

/**
 * @author bin
 *         时间: 2016/5/31 14:25
 *         类说明:阶段测评的类
 */
public class StageCommentActivity extends BaseActivity implements RecordInterface{


    LinearLayout leftLayout;
    //返回的布局

    TextView title;
    //标题

    ListView commentListView;
    //测试列表

    ImageView sideMenu;

    LinearLayout nullLayout;

    private int userId, subjectId,examType,page = 1, pageSize = 10; //用户Id,专业id,试卷类型,页数,每页显示的条目数
    private ProgressDialog progressDialog;  //加载数据显示的dialog
    private String examName;  //试卷类型名称
    private List<PublicEntity> paperList;
    private StageCommentAdapter stageCommentAdapter;  //阶段测试的适配器
    private Intent intent;  //意图对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_stage_comment);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        title = (TextView) findViewById(R.id.title);
        commentListView = (ListView) findViewById(R.id.comment_listView);
        sideMenu = (ImageView) findViewById(R.id.side_menu);
        nullLayout = (LinearLayout) findViewById(R.id.null_layout);

        //获取传过来的信息
        getIntentMessage();
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取传过来的信息
     */
    private void getIntentMessage() {
        Intent intent = getIntent();
        examType = intent.getIntExtra("examType",0);  //2：阶段测试  3：真题练习
        examName = intent.getStringExtra("examName");  //获取试卷的名称
    }

    /**
     * @author ming
     * 时间：2016/5/31 18:00
     * 方法声明：初始化
     */

    @Override
    public void initView() {
        intent = new Intent();  //意图对象
        paperList = new ArrayList<PublicEntity>();
        userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);  //获取用户id
        subjectId = getSharedPreferences("subjectId", MODE_PRIVATE).getInt("subjectId", 0);  //获取专业id
        progressDialog = new ProgressDialog(StageCommentActivity.this); //加载数据显示的dialog
        leftLayout.setVisibility(View.VISIBLE); //返回的按钮
        if ("阶段测试".equals(examName)) {
            title.setText(R.string.comment_title);  //设置标题
        } else if ("真题练习".equals(examName)) {
            title.setText(R.string.zhenti_practice);  //设置标题
        }
        sideMenu.setBackgroundResource(R.mipmap.return_button);
        //联网获取阶段测试的数据
        getPhaseTestData(userId, subjectId, examType, page, pageSize, examName);
    }

    /**
     * @author ming
     * 时间：2016/5/31 18:01
     * 方法声明：添加点击
     */
    @Override
    public void addOnClick() {
        leftLayout.setOnClickListener(this); // 返回按钮
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_layout: //返回按钮
                this.finish();
                break;

        }
    }

    /**
     * 联网获取阶段测试和真题练习的数据的数据
     */
    private void getPhaseTestData(int userId, int subId, final int type, final int page, int pageSize, String title) {
        RequestParams params = new RequestParams();
        params.put("cusId", userId);
        params.put("subId", subId);
        params.put("type", type);
        params.put("page.currentPage", page);
        params.put("page.pageSize", pageSize);
        params.put("title", title);
        Log.i("lala", ExamAddress.PHASETESTLIST_URL + "?" + params.toString());
        DemoApplication.getHttpClient().post(ExamAddress.PHASETESTLIST_URL, params, new TextHttpResponseHandler() {
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
                            List<PublicEntity> list = publicEntity.getEntity().getPaperList();
                            if (list == null || list.size() <= 0) {
                                nullLayout.setVisibility(View.VISIBLE);
                                return;
                            }
                            if (publicEntity.getEntity().getPaperList() != null && publicEntity.getEntity().getPaperList().size() > 0) {
                                for (int i = 0; i < publicEntity.getEntity().getPaperList().size(); i++) {
                                    paperList.add(list.get(i));
                                }
                            }
                            stageCommentAdapter = new StageCommentAdapter(StageCommentActivity.this, paperList, type);
                            stageCommentAdapter.setRecordInterface(StageCommentActivity.this);
                            commentListView.setAdapter(stageCommentAdapter);
//                          refreshScrollView.onRefreshComplete();
                        } else {
                            ConstantUtils.showMsg(StageCommentActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    /**
     * @author bin
     * 时间: 2016/6/8 10:13
     * 方法说明:item中进入考试的点击事件
     */
    @Override
    public void myClick(int position, String name) {
        if("进入考试".equals(name)){
        	int testNumber = paperList.get(position).getTestNumber();
        	int status = paperList.get(position).getStatus();
        	if(testNumber == 1 && status == 0){
        		ConstantUtils.showMsg(StageCommentActivity.this, "此试卷只允许做一次,您已经做过一次了!");
        		return;
        	}
            int id = paperList.get(position).getId();
            intent.setClass(StageCommentActivity.this,BeginExamPaperActivity.class);
            intent.putExtra("examName","阶段测试和真题练习");  //试卷类型
            intent.putExtra("paperId",id);  //试卷的Id
            startActivity(intent);
        }
    }
}
