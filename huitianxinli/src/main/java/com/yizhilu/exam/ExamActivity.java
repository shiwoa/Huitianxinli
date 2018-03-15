package com.yizhilu.exam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;



import com.alibaba.fastjson.JSON;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.slidingmenu.lib.SlidingMenu;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.community.GroupMainActivity;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.exam.fragment.ExamSlidingMenuFragment;
import com.yizhilu.huitianxinli.LoginActivity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.ExamAddress;

/**
 * @author bin
 *         时间: 2016/5/26 9:05
 *         类说明: 主页的类
 */
public class ExamActivity extends BaseActivity {

    ImageView sideMenu;//科目

    ListView classification;//考试类型

    ImageView setImage;//设置

    LinearLayout assessment;//能力评估

    LinearLayout record;//考试记录

    LinearLayout leftLayout;

    LinearLayout rightLayout;

    TextView shequ_text;  //社区

    LinearLayout school_layout;  //网校布局

    TextView school_text;  //网校

    TextView leftText;//科目
//    @InjectView(R.id.title)
//    TextView title;  //标题
    LinearLayout errorLayout;
    LinearLayout collectionLayout;

    TextView lastTimeText;//为做完的题
    
    LinearLayout head_exal;
    private int[] images;//考试类型图片数组
    private String[] names;//考试类型名字数组
    private String[] contents;//考试类型内容数组
    private String[] detaileds;//考试类型详情数组
    private List<Map<String, Object>> myList;//考试类型集合
    private Intent intent;  //意图对象
    private SlidingMenu slidingMenu;
    private MyBroadCast broadCast;  //广播的对象
    private int userId, subjectId;  //用户Id,专业的Id
    private ProgressDialog progressDialog;  //加载数据显示
    private PublicEntity notFinishPaper;  //未做完试卷的实体
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_exam);
        sideMenu= (ImageView)findViewById(R.id.side_menu);//科
        classification = (ListView)findViewById(R.id.classification);
        setImage= (ImageView)findViewById(R.id.set_image);
        assessment = (LinearLayout)findViewById(R.id.assessment_layout);
        record = (LinearLayout)findViewById(R.id.record_layout);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        rightLayout = (LinearLayout) findViewById(R.id.right_layout);
        shequ_text = (TextView) findViewById(R.id.right_layout_tv);
        school_layout = (LinearLayout) findViewById(R.id.school_layout);
        school_text = (TextView) findViewById(R.id.school_text);
        leftText= (TextView)findViewById(R.id.left_text);
        lastTimeText = (TextView)findViewById(R.id.last_time_text);
        head_exal = (LinearLayout)findViewById(R.id.head_exal);
        collectionLayout = (LinearLayout) findViewById(R.id.collection_layout);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);  //获取用户id
        subjectId = getSharedPreferences("subjectId", MODE_PRIVATE).getInt("subjectId", 0);  //获取专业的Id
        if (broadCast == null) {
            broadCast = new MyBroadCast();
            IntentFilter filter = new IntentFilter();
            filter.addAction("slidingClose"); // 选中专业的动作
            registerReceiver(broadCast, filter);
        }
        if (subjectId != 0) {
            // 获取未做完的试题
            getHttpUnFinishedData(userId, subjectId);
        }

    }

    /**
     * 联网获取未完成的试题
     */
    public void getHttpUnFinishedData(int userId, int subjectId) {
        RequestParams params = new RequestParams();
        params.put("cusId", userId);
        params.put("subId", subjectId);
        Log.i("lala",ExamAddress.UNFINISHED_URL+"?"+params);
        DemoApplication.getHttpClient().post(ExamAddress.UNFINISHED_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String data) {
                if (!TextUtils.isEmpty(data)) {
                    try {
                        PublicEntity publicEntity = JSON.parseObject(
                                data, PublicEntity.class);
                        String message = publicEntity.getMessage();
                        if (publicEntity.isSuccess()) {
                            PublicEntity entity = publicEntity.getEntity();
                            if (entity != null && entity.getRecentlyNotFinishPaper()!= null) {
                                notFinishPaper = entity.getRecentlyNotFinishPaper();
                                lastTimeText.setText(entity.getRecentlyNotFinishPaper().getPaperName());
                            } else {
                                lastTimeText.setText("");
                            }
                        } else {
                            ConstantUtils.showMsg(ExamActivity.this, message);
                        }
                    } catch (Exception e) {
                    }
                }
            }

        });
    }



    /**
     * 初始化数据的方法
     */
    @Override
    public void initView() {
        progressDialog = new ProgressDialog(ExamActivity.this); //加载数据显示的dialog
        intent = new Intent();
        //初始化侧滑菜单
        initSlidingMenu();
        //初始化数据
        intDate();

    }


    /**
     * 添加点击事件的方法
     */
    @Override
    public void addOnClick() {
        lastTimeText.setOnClickListener(this);  //未做完试题
        leftLayout.setOnClickListener(this);  //侧滑菜单的布局
        rightLayout.setOnClickListener(this);  //设置的布局
        classification.setOnItemClickListener(this);//选择做题类型
        record.setOnClickListener(this);//学习记录
        assessment.setOnClickListener(this);//能力评估
        errorLayout.setOnClickListener(this);//错题记录
        collectionLayout.setOnClickListener(this);  //收藏试题布局
        school_layout.setOnClickListener(this);  //网校
    }

    /**
     * 初始化listview数据
     */
    private void intDate() {
        sideMenu.setBackgroundResource(R.mipmap.top_menu);//显示右侧
        leftLayout.setVisibility(View.VISIBLE);  //显示侧滑菜单
        leftText.setVisibility(View.VISIBLE);//显示科目
        rightLayout.setVisibility(View.VISIBLE);  //显示设置布局
        shequ_text.setVisibility(View.VISIBLE);
        shequ_text.setText("社区");
        school_layout.setVisibility(View.VISIBLE);
        school_text.setText("网校");
        head_exal.setVisibility(View.VISIBLE);
        images = new int[]{R.mipmap.knowledge, R.mipmap.stage,
                R.mipmap.zhenti, R.mipmap.intelligence, R.mipmap.discuss, R.mipmap.test_assembly};
        names = new String[]{"知识点练习", "阶段测试", "真题练习", "智能错题练习", "讨论题练习", "组卷练习"};
        contents = new String[]{"专业讲师，运营人员倾心打造高品质", "针对阶段学习情况，详细到考点", "覆盖历年所有真题，感受历年", "历史错题，针对性测评", "案例分析与讨论习题，详细到考点", "专业团队，科学组卷，强力打造"};
        detaileds = new String[]{"知识考点强化", "增强学习体验", "真题的", "打破疑难点", "快速击破智慧学习", "精品模考"};
        myList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image", images[i]);
            map.put("name", names[i]);
            map.put("content", contents[i]);
            map.put("detailed", detaileds[i]);
            myList.add(map);
        }
        SimpleAdapter madapter = new SimpleAdapter(this, myList,
                R.layout.item_main_type, new String[]{"image", "name", "content", "detailed"},
                new int[]{R.id.type_image, R.id.name_text, R.id.content_text, R.id.detailed_text});
        classification.setAdapter(madapter);
    }

    /**
     * @author bin
     * 时间: 2016/5/31 17:32
     * 方法说明:初始化侧滑菜单方法
     */
    public void initSlidingMenu() {
        slidingMenu = new SlidingMenu(getApplicationContext());
        slidingMenu.setMode(SlidingMenu.LEFT);
        slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        // 设置压缩的效果
        slidingMenu.setBehindScrollScale(0.35f);
        // 设置滑动菜单拉开后的距离
        slidingMenu.setBehindOffsetRes(R.dimen.menu_offset_with);
        //设置阴影宽度
        slidingMenu.setShadowWidth(getWindowManager().getDefaultDisplay().getWidth() / 50);
        //设置阴影的图片
        slidingMenu.setShadowDrawable(R.drawable.shadow);
        // 设置是否开启渐变效果
        slidingMenu.setFadeEnabled(true);
        // 设置渐变想过的值 范围(0.0-1.0f);
        slidingMenu.setFadeDegree(0.3f);
        slidingMenu.setBehindWidth((int) (ExamActivity.this.getWindowManager()
                .getDefaultDisplay().getHeight() / 2.3));
        slidingMenu.setMenu(R.layout.sliding_layout);
        slidingMenu.attachToActivity(ExamActivity.this,
                SlidingMenu.SLIDING_CONTENT);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction();
        transaction.replace(R.id.sliding_layout,
                new ExamSlidingMenuFragment());
        transaction.commit();
    } 

    /**
     * listview点击事件
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        switch (parent.getId()) {
            case R.id.classification: //首页分类
                switch (position) {
                    case 0:  //知识点练习
                        if (userId == 0) {
                            intent.setClass(this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            if (subjectId == 0) {
                            	slidingMenu.toggle(true);
                            } else {
                                //联网获取知识点的方法
                                getKnowledgePoint(userId,subjectId);
                            }
                        }
                        break;
                    case 1:  //阶段测试
                        if (userId == 0) {
                            intent.setClass(this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                        	if (subjectId == 0) {
                               	slidingMenu.toggle(true);
                               } else {
                            	   intent.setClass(ExamActivity.this, StageCommentActivity.class);
                            	   intent.putExtra("examType",2);
                            	   intent.putExtra("examName", "阶段测试"); // 阶段测试
                            	   startActivity(intent);
                               }
                        }
                        break;
                    case 2:  //真题练习
                        if (userId == 0) {
                            intent.setClass(this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                        	if(subjectId == 0){
                        		slidingMenu.toggle(true);
                        	}else{
                        		intent.setClass(ExamActivity.this, StageCommentActivity.class);
                        		intent.putExtra("examType",3);
                        		intent.putExtra("examName", "真题练习"); // 真题
                        		startActivity(intent);
                        	}
                        }
                        break;
                    case 3:  //智能错题练习
                        if (userId == 0) {
                            intent.setClass(ExamActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                        	if(subjectId == 0){
                        		slidingMenu.toggle(true);
                        	}else{
	                            intent.setClass(ExamActivity.this, ErrorPracticeListActivity.class);
	                            startActivity(intent);
                            }
                        }
                        break;
                    case 4:  //讨论题练习
                        if (userId == 0) {
                            intent.setClass(ExamActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            if (subjectId == 0) {
                            	slidingMenu.toggle(true);
                            } else {
                                // 联网获取论述题自测的题数
                                getDiscussNum(userId,subjectId);
                            }
                        }
                        break;
                    case 5:  //组卷练习
                        if (userId == 0) {
                            intent.setClass(ExamActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            if (subjectId == 0) {
                            	slidingMenu.toggle(true);
                            } else {
                                // 弹出组卷的对话框
                                intent.setClass(ExamActivity.this,AssemblyExamActivity.class);
                                startActivity(intent);
                            }
                        }
                        break;
                }
                break;
        }

    }

    /**
     * @author bin
     * 时间: 2016/6/4 10:20
     * 方法说明:获取知识点练习的数据
     */
    private void getKnowledgePoint(int userId,int subId) {
        ConstantUtils.showProgressDialog(progressDialog);
        RequestParams params = new RequestParams();
        params.put("cusId", userId);  //用户Id
        params.put("subId", subId);  //专业Id
        Log.i("lala", ExamAddress.KNOWLEDGEPOINT_URL+"?"+params.toString());
        DemoApplication.getHttpClient().post(ExamAddress.KNOWLEDGEPOINT_URL, params , new TextHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                ConstantUtils.showProgressDialog(progressDialog);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String data) {
                ConstantUtils.exitProgressDialog(progressDialog);
                if(!TextUtils.isEmpty(data)){
                    try {
                        PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
                        String message = publicEntity.getMessage();
                        if(publicEntity.isSuccess()){
                            intent.setClass(ExamActivity.this,KnowledgePointActivity.class);
                            intent.putExtra("publicEntity",publicEntity);
                            startActivity(intent);
                        }else {
                            ConstantUtils.showMsg(ExamActivity.this, message);
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
     * @author 杨财宾 时间:2015-9-1 上午9:11:13 方法说明:练完获取论述题自测的题数
     */
    private void getDiscussNum(int userId,int subId) {
        ConstantUtils.showProgressDialog(progressDialog);
        RequestParams params = new RequestParams();
        params.put("cusId", userId);
        params.put("subId", subId);
        Log.i("lala",ExamAddress.DIACUSSTESTNUM_URL+"?"+params.toString());
        DemoApplication.getHttpClient().post(ExamAddress.DIACUSSTESTNUM_URL, params,
                new TextHttpResponseHandler() {

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, String data) {
                        ConstantUtils.exitProgressDialog(progressDialog);
                        if (!TextUtils.isEmpty(data)) {
                            try {
                                JSONObject object = new JSONObject(data);
                                String message = object.getString("message");
                                boolean success = object.getBoolean("success");
                                if (success) {
                                    String num = object.getString("entity");
                                    intent.setClass(ExamActivity.this,DiscussActivity.class);
                                    intent.putExtra("num",num);
                                    startActivity(intent);
                                } else {
                                    ConstantUtils.showMsg(ExamActivity.this,
                                            message);
                                }
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, String arg2,
                                          Throwable arg3) {
                        ConstantUtils.exitProgressDialog(progressDialog);
                    }
                });
    }
    /**
     * 点击事件
     */
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.last_time_text:  //未做完试题
                if(notFinishPaper!=null){
                    int type = notFinishPaper.getType();
                    if(type!=2){
                        intent.setClass(ExamActivity.this,BeginExamActivity.class);
                    }else{
                        intent.setClass(ExamActivity.this,BeginExamPaperActivity.class);
                    }
                    intent.putExtra("examName","继续练习");
                    intent.putExtra("continueId",notFinishPaper.getId());
                    startActivity(intent);
                }
                break;
            case R.id.right_layout://社区
                intent.setClass(this, GroupMainActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.school_layout:
            	this.finish();
            	break;
            case R.id.left_layout://科目
                if (userId == 0) {
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    slidingMenu.toggle(true);
                }
                break;
            case R.id.record_layout://学习记录
                if (userId == 0) {
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                	if (subjectId == 0) {
                		slidingMenu.toggle(true);
					}else{
						intent.setClass(this, StudyRecordActivity.class);
						startActivity(intent);
					}
                }
                break;
            case R.id.assessment_layout://能力评估error_layout
                if (userId == 0) {
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                	if (subjectId == 0) {
                		slidingMenu.toggle(true);
					}else{
						intent.setClass(this, AbilityAssessActivity.class);
						startActivity(intent);
					}
                }
                break;
            case R.id.error_layout://错题记录
                if (userId == 0) {
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                	if (subjectId == 0) {
                		slidingMenu.toggle(true);
					}else{
						intent.setClass(this, WrongRecordActivity.class);
						startActivity(intent);
					}
                }
                break;
            case R.id.collection_layout:  //收藏试题
                if (userId == 0) {
                    intent.setClass(this, LoginActivity.class);
                    startActivity(intent);
                } else {
                	if (subjectId == 0) {
                		slidingMenu.toggle(true);
					}else{
						intent.setClass(this, CollectRecordActivity.class);
						startActivity(intent);
					}
                }
                break;

        }
    }

    /**
     * @author bin
     *         时间: 2016/6/1 15:43
     *         类说明:广播的类
     */
    class MyBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("slidingClose".equals(action)) { // 选完专业的操作
                String subjectName = intent.getStringExtra("subName");
                leftText.setText(subjectName); // 设置选中的专业的名字
                slidingMenu.toggle(false);
                subjectId = getSharedPreferences("subjectId", MODE_PRIVATE).getInt(
                        "subjectId", 0);  //获取专业的Id
                // 获取未做完的试题
                getHttpUnFinishedData(userId, subjectId);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (broadCast != null) {
            unregisterReceiver(broadCast);
        }
        super.onDestroy();
    }
}
