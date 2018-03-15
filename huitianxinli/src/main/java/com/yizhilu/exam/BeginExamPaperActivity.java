package com.yizhilu.exam;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.Header;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.exam.adapter.AnswerAdapter;
import com.yizhilu.exam.adapter.ExamButtomAdapter;
import com.yizhilu.exam.adapter.QuestionPagerAdapter;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.exam.fragment.ChoiceQuestionFragment;
import com.yizhilu.exam.fragment.ComplexFragment;
import com.yizhilu.exam.utils.StaticUtils;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.ExamAddress;
import com.yizhilu.utils.StringUtil;

/**
 * @author bin
 *         时间: 2016/6/3 16:51
 *         类说明:考试的类
 */
public class BeginExamPaperActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    ViewPager viewPager;  //试题滑动的控件

    GridView gridViewOne;   //收藏等布局

    GridView gridViewTwo;  //暂停等布局

    LinearLayout gridViewLayout;  //收藏等的布局

    ImageView sideMenu;  //返回图片

    LinearLayout leftLayout;  //返回布局

    TextView title;  //标题

    TextView examType;  //考试类型

    TextView currentNumber;  //当前题数

    TextView allNumber; //总题数

    TextView timeText;  //时间

    LinearLayout timeLayout;  //时间布局

    TextView typeNumber;  //当前类型题

    TextView typeAllnumber;  //不同类型总题数

    TextView bracketText;  //右边括号
    private String examName;  //考点的类型,知识点练习的类型
    private int userId, subjectId;  //用户Id,专业Id
    private ProgressDialog progressDialog;  //加载数据显示
    private int[] imageOne, imageTwo, textOne, textTwo;  //图片和文字的数组
    private ExamButtomAdapter adapterOne, adapterTwo;  //底部导航适配器
    private PublicEntity publicEntity;  //试题的实体类
    private ComplexFragment complexFragment;  //材料题的材料的fragment
    private ChoiceQuestionFragment choiceQuestionFragment;  //选择判断等
    private List<String> answerList;//答案集合
    private int examPositionZ = 0;  //试题总的下标
    private List<Boolean> collectList, markers; // 收藏的集合,标记的集合
    private List<Fragment> fragments;  //存放试题fragment的集合
    private QuestionPagerAdapter questionPagerAdapter;  //试题的适配器
    private BroadCast broadCast;  //广播的对象
    private int questionPosition, questionId;  //当前试题的下标,当前试题的Id
    private Intent intent;  //意图对象
    private int continueId;  //继续练习的Id
    private int paperId;  //试卷的Id
    private List<PublicEntity> entityList;  //实体的总集合
    private boolean isNext, isPause,isPauseing, timeExit,exit;  //是否是下次再做,是否是暂停(控制倒计时),暂停控制对话框,时间到退出,点返回退出
    private Dialog submitDialog;  //提交试卷和下次再做的弹框
    private TextView confirmButton, cancleButton, submitTitle;  //交卷时 确定,和取消按钮,dialog标题
    private int type, testTime, paperRecordId, paPerId;  //试卷的类型,测试时间,试卷记录Id,试卷Id,提交试卷用
    private Dialog answerSheetDialog;  //显示答题卡得dialog
    private GridView sheet_gridView;  //答题卡
    private AnswerAdapter answerAdapter;  //答题卡的适配器
    private TextView cancel_sheet, start_answer;  //取消和我要交卷
    private Timer timer;  //定时器
    private int mm, ss = 59,timePosition = 3;  //分,秒,时间到提交诗卷的倒计时
    private LinearLayout exit_layoutOne,exit_layoutTwo;  //返回退出布局(交卷布局),时间到退出
    private TextView title_text,time_exit, time_config;  //退出和暂停标题,时间到弹窗显示的内容
    private int paperType; //标记论述题 4为论述题
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_begin_exam);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        gridViewOne = (GridView) findViewById(R.id.gridView_one);
        gridViewTwo = (GridView) findViewById(R.id.gridView_two);
        gridViewLayout = (LinearLayout) findViewById(R.id.gridView_Layout);
        sideMenu = (ImageView) findViewById(R.id.side_menu);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        title = (TextView) findViewById(R.id.title);
        examType = (TextView) findViewById(R.id.exam_type);
        currentNumber = (TextView) findViewById(R.id.currentNumber);
        allNumber = (TextView) findViewById(R.id.allNumber);
        timeText = (TextView) findViewById(R.id.time_text);
        timeLayout = (LinearLayout) findViewById(R.id.time_layout);
        typeNumber = (TextView) findViewById(R.id.type_number);
        typeAllnumber = (TextView) findViewById(R.id.typeAllnumber);
        bracketText = (TextView) findViewById(R.id.bracket_text);

        //获取传过来的信息
        getIntentMessage();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (broadCast == null) {
            broadCast = new BroadCast();
            IntentFilter filter = new IntentFilter();
            filter.addAction("exam");
            registerReceiver(broadCast, filter);
        }
    }

    /**
     * @author bin
     * 时间: 2016/6/4 13:32
     * 方法说明:获取传过来的信息
     */
    public void getIntentMessage() {
        userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);  //获取用户Id
        subjectId = getSharedPreferences("subjectId", MODE_PRIVATE).getInt("subjectId", 0);  //专业Id
        Intent intent = getIntent();
        examName = intent.getStringExtra("examName");
        if ("阶段测试和真题练习".equals(examName)) {
            paperId = intent.getIntExtra("paperId", 0);  //获取考试的Id
        } else if ("继续练习".equals(examName)) {
            continueId = intent.getIntExtra("continueId", 0);  //继续练习的Id
        }

        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
//        title.setText(R.string.exam_record);  //设置标题
    }

    @Override
    public void initView() {
        timer = new Timer();  //定时器
        intent = new Intent();  //意图对象
        answerList = new ArrayList<String>();  //答案的集合
        collectList = new ArrayList<Boolean>();  //收藏的集合
        markers = new ArrayList<Boolean>(); // 标记的集合
        fragments = new ArrayList<Fragment>();  //存放试题fragment的集合
        entityList = new ArrayList<PublicEntity>();  //实体的集合
        progressDialog = new ProgressDialog(BeginExamPaperActivity.this);  //加载数据显示
        imageOne = new int[]{R.mipmap.collect_exam_bg, R.mipmap.marker_bg,
                R.mipmap.error_correction, R.mipmap.next_work}; // 收藏，标记，纠错，下次再做
        imageTwo = new int[]{R.mipmap.pause_bg, R.mipmap.answer_sheet,
                R.mipmap.send_paper, R.mipmap.more_exam_bg}; // 暂停，答题卡，交卷，更多
        textOne = new int[]{R.string.collect, R.string.marker, R.string.correction_error, R.string.next_work};
        textTwo = new int[]{R.string.pause_text, R.string.answer_sheet, R.string.send_paper, R.string.more_text};
        adapterOne = new ExamButtomAdapter(BeginExamPaperActivity.this, imageOne, textOne, 1);
        gridViewOne.setAdapter(adapterOne);
        adapterTwo = new ExamButtomAdapter(BeginExamPaperActivity.this, imageTwo, textTwo, 2);
        gridViewTwo.setAdapter(adapterTwo);
        if ("阶段测试和真题练习".equals(examName)) {
            type = 2;
            //获取阶段测试和真题练习的方法
            getPhaseTestData(userId, paperId);
        } else if ("继续练习".equals(examName)) {
            //获取继续联系的方法
            getContinuePractice(userId, subjectId, continueId);
        }
    }

    @Override
    public void addOnClick() {
        leftLayout.setOnClickListener(this);  //返回
        viewPager.setOnPageChangeListener(this);  //切换试题的事件
        gridViewOne.setOnItemClickListener(this);  //收藏等布局
        gridViewTwo.setOnItemClickListener(this);  //暂停等布局
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_layout:  //返回
            	if (publicEntity == null || publicEntity.getEntity() == null){
                    BeginExamPaperActivity.this.finish();
                    return;
                }
            	List<PublicEntity> paperMiddleList = publicEntity.getEntity().getPaperMiddleList();
                if(paperMiddleList == null || paperMiddleList.size()<=0){
                    BeginExamPaperActivity.this.finish();
                    return;
                }
                isNext = true;
                exit = true;
                //显示退出的dialog
                showCommitDialog("确定要退出吗?");
                break;
            case R.id.dialogbtnsure:  //确定
                    if (answerSheetDialog != null && answerSheetDialog.isShowing()) {
                        answerSheetDialog.dismiss();
                    }
                    if (submitDialog != null && submitDialog.isShowing()) {
                        submitDialog.dismiss();
                    }
                    if (exit) {  //下次再做
                        // 下次再做的方法
//                        submitExamPaper();
                    	this.finish();
                    } else {  //交卷
                        // 提交试卷的方法
                        submitExamPaper();
                    }
                break;
            case R.id.dialogbtncancle:  //取消
                isNext = false;
                exit = false;
                submitDialog.dismiss();
                break;
            case R.id.cancel_sheet:  //答题卡取消
                answerSheetDialog.dismiss();
                break;
            case R.id.start_answer:  //提交试卷
                isNext = false;
                answerSheetDialog.dismiss();
                showCommitDialog("确定交卷吗?");
                break;
            case R.id.time_config:  //时间到交卷的确定
            	submitDialog.dismiss();
                if(isPauseing){
                    isPause = false;
                    isPauseing = false;
                    textTwo[0] = R.string.pause_text;
                    imageTwo[0] = R.mipmap.pause_bg;
                    adapterTwo.notifyDataSetChanged();
                }else{
                    //isPauseing为true  是为了避免这时正好倒计时3秒时间也到了  就会提交两次
                    isPauseing = true;
                    // 提交试卷的方法
                    submitExamPaper();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        switch (parent.getId()) {
            case R.id.gridView_one:  //收藏等布局
                switch (position) {
                    case 0:  //收藏
                        questionId = entityList.get(questionPosition).getQstId();
                        if (collectList.get(questionPosition)) {  //已收藏
                            //取消收藏
                            isCancelCollect(userId, subjectId, questionId, true);
                        } else {
                            //收藏
                            isCancelCollect(userId, subjectId, questionId, false);
                        }
                        break;
                    case 1:  //标记
                        if (markers.get(questionPosition)) {  //已标记
                            textOne[1] = R.string.marker;
                            markers.set(questionPosition, false);
                            adapterOne.notifyDataSetChanged();
                        } else {  //未标记
                            textOne[1] = R.string.markered;
                            markers.set(questionPosition, true);
                            adapterOne.notifyDataSetChanged();
                        }
                        break;
                    case 2:  //纠错
                        questionId = entityList.get(questionPosition).getId();
                        intent.setClass(BeginExamPaperActivity.this, CorrectErrorActivity.class);
                        intent.putExtra("questionId", questionId);  //试题Id
                        startActivity(intent);
                        gridViewLayout.setVisibility(View.GONE);
                        break;
                    case 3:  //下次再做
                        isNext = true;
                        showCommitDialog("是否下次再做?");
                        break;
                }
                break;
            case R.id.gridView_two:  //暂停等布局
                switch (position) {
                    case 0:  //暂停
                        if (isPause) {
                            isPause = false;
                            isPauseing = false;
                            textTwo[0] = R.string.pause_text;
                            imageTwo[0] = R.mipmap.pause_bg;
                        } else {
                            isPause = true;
                            isPauseing = true;
                            textTwo[0] = R.string.start_text;
                            imageTwo[0] = R.mipmap.start_bg;
                            //显示退出的dialog
                            showCommitDialog("是否开始做题 ?");
                        }
                        adapterTwo.notifyDataSetChanged();
                        break;
                    case 1:  //答题卡
                        //显示答题卡
                        showAnswerSheet();
                        break;
                    case 2:  //交卷
                        isNext = false;
                        showCommitDialog("确定交卷吗?");
                        break;
                    case 3:  //更多
                        if (gridViewLayout.getVisibility() == View.VISIBLE) {
                            gridViewLayout.setVisibility(View.GONE);
                        } else {
                            gridViewLayout.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                break;
            case R.id.sheet_gridView:  //答题卡
                answerSheetDialog.dismiss();
                viewPager.setCurrentItem(position);
                break;
        }
    }

    /**
     * @author bin
     * 时间: 2016/6/8 14:58
     * 方法说明:提交试卷和下次在做的弹框
     */
    public void showCommitDialog(String title) {
        sendBroadcast(new Intent("completion"));  //发送到做题的fragment
        if (submitDialog != null) {
            submitTitle.setText(title);  //设置标题
            submitDialog.show();
            if (timeExit||isPauseing) {
                exit_layoutOne.setVisibility(View.GONE);
                exit_layoutTwo.setVisibility(View.VISIBLE);
                if(timeExit){
                    title_text.setText("已到交卷时间！");
                    time_exit.setVisibility(View.VISIBLE);
                }else{
                    title_text.setText(title);
                    time_exit.setVisibility(View.GONE);
                }
            } else {
                exit_layoutOne.setVisibility(View.VISIBLE);
                exit_layoutTwo.setVisibility(View.GONE);
            }
        } else {
            View view = getLayoutInflater().inflate(R.layout.exam_dialog_show, null);
            WindowManager manager = (WindowManager) getSystemService(
                    Context.WINDOW_SERVICE);
            @SuppressWarnings("deprecation")
            int width = manager.getDefaultDisplay().getWidth();
            int scree = (width / 3) * 2;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.width = scree;
            view.setLayoutParams(layoutParams);
            submitDialog = new Dialog(this, R.style.custom_dialog);
            submitDialog.setContentView(view);
            submitDialog.setCancelable(false);
            submitDialog.show();
            exit_layoutOne = (LinearLayout) view.findViewById(R.id.exit_layoutOne);
            exit_layoutTwo = (LinearLayout) view.findViewById(R.id.exit_layoutTwo);
            title_text = (TextView) view.findViewById(R.id.title_text);
            time_exit = (TextView) view.findViewById(R.id.time_exit);
            time_config = (TextView) view.findViewById(R.id.time_config);
            if (timeExit||isPauseing) {
                exit_layoutOne.setVisibility(View.GONE);
                exit_layoutTwo.setVisibility(View.VISIBLE);
                if(timeExit){
                    title_text.setText("已到交卷时间！");
                    time_exit.setVisibility(View.VISIBLE);
                }else{
                    title_text.setText(title);
                    time_exit.setVisibility(View.GONE);
                }
            } else {
                exit_layoutOne.setVisibility(View.VISIBLE);
                exit_layoutTwo.setVisibility(View.GONE);
            }            time_config.setOnClickListener(this);  //时间到的退出
            submitTitle = (TextView) view.findViewById(R.id.texttitles);
            submitTitle.setText(title);  //设置标题
            confirmButton = (TextView) view.findViewById(R.id.dialogbtnsure);
            confirmButton.setText("确定");
            confirmButton.setOnClickListener(BeginExamPaperActivity.this);  //确定
            cancleButton = (TextView) view.findViewById(R.id.dialogbtncancle);
            cancleButton.setText("取消");
            cancleButton.setOnClickListener(BeginExamPaperActivity.this);  //取消
        }
    }

    /**
     * @author bin
     * 时间: 2016/6/24 19:13
     * 方法说明:显示答题卡的方法
     */
    public void showAnswerSheet() {
        if (answerSheetDialog != null) {
            answerSheetDialog.show();
            List<String> answerList = StaticUtils.getAnswerList();
            answerAdapter = new AnswerAdapter(BeginExamPaperActivity.this, answerList, markers);
            sheet_gridView.setAdapter(answerAdapter);
        } else {
            View view = getLayoutInflater().inflate(R.layout.dialog_answer_sheet, null);
            answerSheetDialog = new Dialog(this, R.style.custom_dialog);
            answerSheetDialog.getWindow().setGravity(Gravity.TOP);
            answerSheetDialog.setContentView(view);
            answerSheetDialog.setCancelable(false);
            answerSheetDialog.show();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = answerSheetDialog.getWindow().getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            answerSheetDialog.getWindow().setAttributes(lp);
            cancel_sheet = (TextView) view.findViewById(R.id.cancel_sheet);  //取消
            start_answer = (TextView) view.findViewById(R.id.start_answer);  //我要交卷
            sheet_gridView = (GridView) view.findViewById(R.id.sheet_gridView);  //答题卡
            cancel_sheet.setOnClickListener(BeginExamPaperActivity.this);
            start_answer.setOnClickListener(BeginExamPaperActivity.this);
            sheet_gridView.setOnItemClickListener(BeginExamPaperActivity.this);
            List<String> answerList = StaticUtils.getAnswerList();
            answerAdapter = new AnswerAdapter(BeginExamPaperActivity.this, answerList, markers);
            sheet_gridView.setAdapter(answerAdapter);
        }
    }

    /**
     * @author bin
     * 时间: 2016/6/8 10:32
     * 方法说明:获取阶段测试和真题练习的方法
     */
    private void getPhaseTestData(int userId, int paperId) {
        RequestParams params = new RequestParams();
        params.put("cusId", userId);
        params.put("paperId", paperId);
        Log.i("lala", ExamAddress.PHASETEST_EXAMURL + "?" + params.toString());
        DemoApplication.getHttpClient().post(ExamAddress.PHASETEST_EXAMURL, params,
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
                                publicEntity = JSONObject.parseObject(data, PublicEntity.class);
                                String msg = publicEntity.getEntity().getMsg();
                                paperType = publicEntity.getEntity().getPaperType();
                                if ("is_ok".equals(msg)) {
                                    //解析数据的方法
                                    parserJsonMessage();
                                } else {
                                    ConstantUtils.showMsg(BeginExamPaperActivity.this, msg);
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
     * @author bin
     * 时间: 2016/6/7 21:34
     * 方法说明:联网获取继续练习的方法
     */
    private void getContinuePractice(int userId, int subId, int id) {
        RequestParams params = new RequestParams();
        params.put("cusId", userId);
        params.put("subId", subId);
        params.put("id", id);
        Log.i("lala", ExamAddress.LOOKPARSERORCONTINUEPRACTICE + "?" + params.toString());
        DemoApplication.getHttpClient().post(ExamAddress.LOOKPARSERORCONTINUEPRACTICE, params,
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
                                publicEntity = JSON.parseObject(data, PublicEntity.class);
                                String message = publicEntity.getMessage();
                                paperType = publicEntity.getEntity().getPaperRecord().getType();
                                if (publicEntity.isSuccess()) {
                                    //解析数据的方法
                                    parserJsonMessage();
                                } else {
                                    ConstantUtils.showMsg(BeginExamPaperActivity.this,
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
     * @author bin
     * 时间: 2016/6/7 14:56
     * 方法说明:取消收藏和收藏的接口
     */
    private void isCancelCollect(int userId, int subId, int qstId, boolean isCancel) {
        RequestParams params = new RequestParams();
        params.put("cusId", userId);
        params.put("subId", subId);
        params.put("qstId", qstId);
        if (isCancel) {   //取消收藏
        	Log.i("lala",ExamAddress.CANCELCOLLECT_URL+params+"----添加收藏---");
            DemoApplication.getHttpClient().post(ExamAddress.CANCELCOLLECT_URL, params,
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
                                    PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
                                    String message = publicEntity.getMessage();
                                    if (publicEntity.isSuccess()) {
                                        collectList.set(questionPosition, false);
                                        imageOne[0] = R.mipmap.collect_exam_bg;
                                        textOne[0] = R.string.collect;
                                        adapterOne.notifyDataSetChanged();
                                    } else {
                                        ConstantUtils.showMsg(
                                                BeginExamPaperActivity.this, message);
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
        } else {  //收藏
            DemoApplication.getHttpClient().post(ExamAddress.COLLECT_URL, params,
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
                                    PublicEntity publicEntity = JSON.parseObject(data, PublicEntity.class);
                                    String message = publicEntity.getMessage();
                                    if (publicEntity.isSuccess()) {
                                        collectList.set(questionPosition, true);
                                        imageOne[0] = R.mipmap.collected_exam_bg;
                                        textOne[0] = R.string.collected;
                                        adapterOne.notifyDataSetChanged();
                                    } else {
                                        ConstantUtils.showMsg(
                                                BeginExamPaperActivity.this, message);
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
    }

    /**
     * @author bin
     * 时间: 2016/6/6 15:44
     * 方法说明:解析数据的方法
     */
    public void parserJsonMessage() {
    	title.setText(publicEntity.getEntity().getPaper().getName());
        List<PublicEntity> paperMiddleList = publicEntity.getEntity().getPaperMiddleList();
        if (paperMiddleList == null || paperMiddleList.size() <= 0)
            return;
        for (int i = 0; i < paperMiddleList.size(); i++) {
            PublicEntity paperEntity = paperMiddleList.get(i);
            int type = paperEntity.getType();  //获取试题的类型
            if (type != 4) {   //不是材料题
                List<PublicEntity> qstMiddleList = paperEntity.getQstMiddleList();
                if (qstMiddleList == null || qstMiddleList.size() <= 0) {
                    continue;
                } else {
                    for (int j = 0; j < qstMiddleList.size(); j++) {
                        PublicEntity middleEntity = qstMiddleList.get(j);
                        int favoritesId = middleEntity.getFavoritesId();  //获取是否收藏的类型
                        //0:为未收藏
                        if (favoritesId == 0) {
                            collectList.add(false);
                        } else {
                            collectList.add(true);
                        }
                        markers.add(false);  //添加标记
                        examPositionZ++;  //试题下标加
                        /**
                         *  setQuestionPosition()  设置某种类型当前题数
                         *  setQuestionZong()  设置某种类型总题数
                         *  因为查看解析上面有当前题型是什么题型  总共多少题  当前是第几题
                         *  接口返回不了这些信息  只能我们本地来记录  所以我在实体中加了两个字段
                         *  因为这是试卷类型 paperMiddleList这个集合下面每个实体是一种题型和练习不一样
                         */
                        middleEntity.setQuestionPosition(j + 1);
                        middleEntity.setQuestionZong(qstMiddleList.size());
                        entityList.add(middleEntity);  //添加实体
                        int qstType = middleEntity.getQstType();
                        if (qstType == 1 || qstType == 2 || qstType == 3) {   //单选，多选，判断
                            answerList.add(StringUtil.isFieldEmpty(middleEntity.getUserAnswer()));  //添加空的答案
                        } else if (qstType == 6) {  //论述题
                            answerList.add(StringUtil.isFieldEmpty(middleEntity.getUserAnswer()));  //添加空的答案
                        } else if (qstType == 7) {  //填空题
                            List<String> userFillList = middleEntity.getUserFillList();
                            if (userFillList == null || userFillList.size() <= 0) {
                                answerList.add("");  //添加空的答案
                            } else {
                                StringBuffer buffer = new StringBuffer();
                                for (int k = 0; k < userFillList.size(); k++) {
                                    buffer.append(userFillList.get(k) + ",");
                                }
                                if (buffer.toString().contains(",")) {
                                    buffer.deleteCharAt(buffer.length() - 1);
                                }
                                answerList.add(StringUtil.isFieldEmpty(buffer.toString()));  //添加已做的答案
                            }
                        }
                        choiceQuestionFragment = new ChoiceQuestionFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("entity", middleEntity);
                        //                                        bundle.putInt("typePosition",i);
                        bundle.putInt("examPosition", j);
                        bundle.putInt("zongPosition", examPositionZ);
                        choiceQuestionFragment.setArguments(bundle);
                        fragments.add(choiceQuestionFragment);  //fragment的集合
                    }
                }
            } else {  //是材料题
                List<PublicEntity> complexList = paperEntity.getComplexList();//获取材料题的集合
                if (complexList == null || complexList.size() <= 0) {
                    continue;
                } else {
                    for (int j = 0; j < complexList.size(); j++) {
                        PublicEntity complexEntity = complexList.get(j);  //得到材料题的材料
                        collectList.add(false);  //添加收藏
                        markers.add(false);  //添加标记
                        examPositionZ++;  //试题下标加
                        answerList.add("我是材料题,可以略过");  //添加答案
                        complexEntity.setQstType(paperEntity.getType());  //把父类型赋值给材料类型
                        entityList.add(complexEntity);  //添加实体
                        //添加材料题的fragment
                        complexFragment = new ComplexFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("entity", complexEntity);
                        complexFragment.setArguments(bundle);
                        fragments.add(complexFragment);  //fragment的集合
                        //得到材料下试题的集合
                        List<PublicEntity> queryQstMiddleList = complexEntity.getQueryQstMiddleList();
                        if (queryQstMiddleList != null && queryQstMiddleList.size() > 0) {
                            for (int k = 0; k < queryQstMiddleList.size(); k++) {
                                PublicEntity qstmiddleEntity = queryQstMiddleList.get(k);
                                int favoritesId = qstmiddleEntity.getFavoritesId();
                                //0:为未收藏
                                if (favoritesId == 0) {
                                    collectList.add(false);
                                } else {
                                    collectList.add(true);
                                }
                                markers.add(false);  //添加标记
                                examPositionZ++;  //试题下标加
                                /**
                                 * 原理同上
                                 * 这里说一下setComplexType()这个字段，这个字段也是我自己加的
                                 * 原因是：材料题和其他类型的题返回的数据类型是不一样的，材料题下面还有单选，多选等题型
                                 * 所以要添加一下标识，是不是材料题下面的题型
                                 */
                                qstmiddleEntity.setQuestionZong(queryQstMiddleList.size());
                                qstmiddleEntity.setQuestionPosition(k + 1);
                                qstmiddleEntity.setComplexType("complex");  //标识是材料下面的题
                                entityList.add(qstmiddleEntity);  //添加实体
                                int qstType = qstmiddleEntity.getQstType();
                                if (qstType == 0) {
                                    qstType = qstmiddleEntity.getQuestionType();
                                }
                                if (qstType == 1 || qstType == 2 || qstType == 3) {   //单选，多选，判断
                                    answerList.add(StringUtil.isFieldEmpty(qstmiddleEntity.getUserAnswer()));  //添加空的答案
                                } else if (qstType == 6) {  //论述题
                                    answerList.add(StringUtil.isFieldEmpty(qstmiddleEntity.getUserAnswer()));  //添加空的答案
                                } else if (qstType == 7) {
                                    List<String> userFillList = qstmiddleEntity.getUserFillList();
                                    if (userFillList == null || userFillList.size() <= 0) {
                                        answerList.add("");  //添加空的答案
                                    } else {
                                        StringBuffer buffer = new StringBuffer();
                                        for (int z = 0; z < userFillList.size(); z++) {
                                            buffer.append(userFillList.get(z) + ",");
                                        }
                                        if (buffer.toString().contains(",")) {
                                            buffer.deleteCharAt(buffer.length() - 1);
                                        }
                                        answerList.add(StringUtil.isFieldEmpty(buffer.toString()));  //添加已做的答案
                                    }
                                }
                                choiceQuestionFragment = new ChoiceQuestionFragment();
                                Bundle cBundle = new Bundle();
                                cBundle.putSerializable("entity", qstmiddleEntity);
//                                        bundle.putInt("typePosition",i);
                                cBundle.putInt("examPosition", k);
                                cBundle.putInt("zongPosition", examPositionZ);
                                choiceQuestionFragment.setArguments(cBundle);
                                fragments.add(choiceQuestionFragment);  //fragment的集合
                            }
                        }
                    }
                }
            }
        }
        int replyTime = publicEntity.getEntity().getPaper().getReplyTime();
        mm = replyTime - 1;
        timeLayout.setVisibility(View.VISIBLE);
        timeText.setText(mm + ":" + ss);  //设置时间
        startTimer();  //开启倒计时
        StaticUtils.setAnswerList(answerList);  //设置初始答案
        if (collectList.get(0)) {
            imageOne[0] = R.mipmap.collected_exam_bg;
            textOne[0] = R.string.collected;
            adapterOne.notifyDataSetChanged();
        }
        int qstType = paperMiddleList.get(0).getType();
        setExamType(qstType);  //设置当前题类型
        currentNumber.setText("1");  //当前题
        allNumber.setText("/" + fragments.size() + "题");  //总题数
        questionPagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(questionPagerAdapter);
    }

    /**
     * @author bin
     * 时间: 2016/6/7 20:07
     * 方法说明:设置题的类型
     */
    public void setExamType(int qstType) {
        boolean isComplex;
        if("complex".equals(StringUtil.isFieldEmpty(entityList.get(questionPosition).getComplexType()))){
            isComplex = true;
        }else{
            isComplex = false;
        }
        switch (qstType) {
            case 1:  //单选
                if(isComplex){
                    examType.setText("材料分析题-单选题 (");
                }else{
                    examType.setText("单选题 (");
                }
                break;
            case 2:  //多选
                if(isComplex){
                    examType.setText("材料分析题-多选题 (");
                }else{
                    examType.setText("多选题 (");
                }
                break;
            case 3:  //判断
                if(isComplex){
                    examType.setText("材料分析题-判断题 (");
                }else{
                    examType.setText("判断题 (");
                }
                break;
            case 4:  //材料题
                examType.setText("材料分析题 (");
                break;
            case 6:  //论述题
                if(isComplex){
                    examType.setText("材料分析题-论述题 (");
                }else{
                    examType.setText("论述题 (");
                }
                break;
            case 7:  //填空题
                if(isComplex){
                    examType.setText("材料分析题-填空题 (");
                }else{
                    examType.setText("填空题 (");
                }
                break;
        }
        String answer = StaticUtils.getAnswerList().get(questionPosition);
        if("我是材料题,可以略过".equals(answer)){
            examType.setText("材料分析题 (材料)");
            typeNumber.setVisibility(View.GONE);
            typeAllnumber.setVisibility(View.GONE);
            bracketText.setVisibility(View.GONE);
        }else{
            typeNumber.setVisibility(View.VISIBLE);
            typeAllnumber.setVisibility(View.VISIBLE);
            bracketText.setVisibility(View.VISIBLE);
            typeNumber.setText(entityList.get(questionPosition).getQuestionPosition()+"");
            typeAllnumber.setText("/"+entityList.get(questionPosition).getQuestionZong()+"");
            bracketText.setText(")");
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * @author bin
     * 时间: 2016/6/7 13:00
     * 方法说明:当viewPager切换完成后调用
     */
    @Override
    public void onPageSelected(int position) {
        questionPosition = position;
        if (collectList.get(position)) {
        	imageOne[0] = R.mipmap.collected_exam_bg;
            textOne[0] = R.string.collected;
            adapterOne.notifyDataSetChanged();
        } else {
        	imageOne[0] = R.mipmap.collect_exam_bg;
            textOne[0] = R.string.collect;
            adapterOne.notifyDataSetChanged();
        }
        int qstType = entityList.get(questionPosition).getType();
        if (qstType == 0) {
            qstType = entityList.get(questionPosition).getQstType();
            if(qstType == 0){
                qstType = entityList.get(questionPosition).getQuestionType();
            }
        }
        setExamType(qstType);  //设置当前题类型
        currentNumber.setText((questionPosition + 1) + "");
        if (markers.get(questionPosition)) {
            textOne[1] = R.string.markered;
            adapterOne.notifyDataSetChanged();
        } else {
            textOne[1] = R.string.marker;
            adapterOne.notifyDataSetChanged();
        }

    }

    /**
     * @author bin
     * 时间: 2016/6/8 16:49
     * 方法说明:提交试卷的方法
     */
    private void submitExamPaper() {
        testTime = publicEntity.getEntity().getPaper().getReplyTime();
        if ("继续练习".equals(examName)) {
        	type = publicEntity.getEntity().getPaperRecord().getType();
            paperRecordId = publicEntity.getEntity().getPaperRecord().getId();
        } else {
            paperRecordId = publicEntity.getEntity().getPaperRecordId();
        }
        paPerId = publicEntity.getEntity().getPaper().getId();
        subjectId = publicEntity.getEntity().getPaper().getSubjectId();
        Log.i("lala", examType + ",,," + type + "...." + paPerId + "..." + paperRecordId);
        RequestParams params = new RequestParams();
        params.put("paperRecord.type", type);
        params.put("paperRecord.replyTime", testTime);
        if (isNext) {
            params.put("optype", 1); // 提交试卷是0,下次再做为1
        } else {  
            params.put("optype", 0); // 提交试卷是0,下次再做为1
        }
        params.put("paperRecord.cusId", userId);  //用户Id
        int replyTime = publicEntity.getEntity().getPaper().getReplyTime();
        Log.i("lala", replyTime+"-----------时间---------------"+mm+"..."+ss);
        if(mm <= 0){
            if(ss<=0){
                params.put("paperRecord.testTime", replyTime*60);  //做题的时间
            }else{
                params.put("paperRecord.testTime", replyTime*60-ss);  //做题的时间
            }
        }else{
            params.put("paperRecord.testTime", (replyTime-mm-1)*60+(60-ss));  //做题的时间
        }
        params.put("paperRecord.subjectId", subjectId);  //专业Id
        params.put("paperRecord.epId", paPerId);
        params.put("paperRecord.id", paperRecordId);
        List<String> answer = StaticUtils.getAnswerList(); // 获取试题的答案
        try {
            // 循环添加每个试题的答案的
            submitQuestion(params, answer);
        } catch (Exception e) {
        }
        Log.i("lala", ExamAddress.SUBMIT_EXAMPAPER_URL + "?" + params.toString());
        DemoApplication.getHttpClient().post(ExamAddress.SUBMIT_EXAMPAPER_URL, params,
                new TextHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ConstantUtils.showProgressDialog(progressDialog);
                    }

                    @Override
                    public void onSuccess(int arg0, Header[] arg1, String data) {
                        Log.i("lala", data);
                        ConstantUtils.exitProgressDialog(progressDialog);
                        if (!TextUtils.isEmpty(data)) {
                            try {
                                org.json.JSONObject object = new org.json.JSONObject(
                                        data);
                                String message = object.getString("message");
                                if (object.getBoolean("success")) {
                                    int entity = object.getInt("entity");
                                    if (isNext) {  //提交试卷
                                        BeginExamPaperActivity.this.finish();
                                        return;
                                    }
                                    if (paperType == 4) {
                                    	intent.setClass(BeginExamPaperActivity.this,LookParserActivity.class);
                                        intent.putExtra("id",entity);
									}else{
	                                    intent.setClass(BeginExamPaperActivity.this, PracticeReportActivity.class);
	                                    intent.putExtra("paperId", entity);
									}
                                    startActivity(intent);
                                    BeginExamPaperActivity.this.finish();
                                } else {
                                    ConstantUtils.showMsg(
                                            BeginExamPaperActivity.this, message);
                                }
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, String arg2,
                                          Throwable arg3) {
                        ConstantUtils.exitProgressDialog(progressDialog);
                        Log.i("lala", "错误了。。。。。。。");
                    }
                });
    }

    /**
     * @author bin
     * 时间: 2016/6/8 17:42
     * 方法说明:循环提交每道试题的答案
     */
    private void submitQuestion(RequestParams params, List<String> answer) {
        int position = -1;  //提交试题试题的下标(不包括材料题)
        int answerPosition = -1;  //获取试题答案的下标
        List<PublicEntity> paperMiddleList = publicEntity.getEntity().getPaperMiddleList();
        for (int i = 0; i < paperMiddleList.size(); i++) {
            int type = paperMiddleList.get(i).getType();
            if (type != 4) {  //不是材料题
                List<PublicEntity> qstMiddleList = paperMiddleList.get(i).getQstMiddleList();
                for (int j = 0; j < qstMiddleList.size(); j++) {
                    position++;
                    answerPosition++;
                    params.put("record[" + position + "].qstType", qstMiddleList.get(j).getQstType());
                    params.put("record[" + position + "].pointId", qstMiddleList.get(j).getPointId());
                    params.put("record[" + position + "].qstIdsLite", qstMiddleList.get(j).getQstId());
                    if (!TextUtils.isEmpty(qstMiddleList.get(j).getIsAsr())) { // 论述题自测没有正确答案这个字段
                        params.put("record[" + position + "].answerLite", qstMiddleList.get(j)
                                .getIsAsr());
                    } else {
                        params.put("record[" + position + "].answerLite", "");
                    }
                    params.put("record[" + position + "].score", paperMiddleList.get(i).getScore());
                    params.put("record[" + position + "].paperMiddle", qstMiddleList.get(j).getPaperMiddleId());
                    Log.i("lala", answer.get(position));
                    params.put("record[" + position + "].userAnswer", answer.get(position));
                }
            } else {  //是材料题
                List<PublicEntity> complexList = paperMiddleList.get(i).getComplexList();
                for (int j = 0; j < complexList.size(); j++) {
                	answerPosition++;
                    List<PublicEntity> queryQstMiddleList = complexList.get(j).getQueryQstMiddleList();
                    for (int k = 0; k < queryQstMiddleList.size(); k++) {
                        position++;
                        answerPosition++;
                        params.put("record[" + position + "].qstType", queryQstMiddleList.get(k).getQuestionType());
                        params.put("record[" + position + "].pointId", queryQstMiddleList.get(k)
                                .getPointId());
                        params.put("record[" + position + "].qstIdsLite", queryQstMiddleList
                                .get(k).getQstId());
                        if (!TextUtils.isEmpty(queryQstMiddleList.get(k).getIsAsr())) { // 论述题自测没有正确答案这个字段
                            params.put("record[" + position + "].answerLite", queryQstMiddleList.get(k).getIsAsr());
                        } else {
                            params.put("record[" + position + "].answerLite", "");
                        }
                        params.put("record[" + position + "].score", paperMiddleList.get(i).getScore());
                        params.put("record[" + position + "].paperMiddle",
                                queryQstMiddleList.get(k).getPaperMiddleId());
                        params.put("record[" + position + "].userAnswer",
                                answer.get(answerPosition));
                    }
                }
            }
        }
    }

    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    /**
     * @author bin
     * 时间: 2016/6/27 16:12
     * 方法说明:开启定时器
     */
    public void startTimer() {
        timer.schedule(task, 0, 1000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int what = msg.what;
            switch (what) {
                case 1:  //倒计时
                    if (!isPause) {
                        if (ss <= 0) {
                            if (mm > 0) {
                                mm -= 1;
                                ss = 59;
                                timeText.setText(mm + ":" + ss);
                            } else {
                                isPause = true;
                                timeExit = true;
                                //显示退出的dialog
                                showCommitDialog("即将退出(3)");
                                //开启时间到提示交卷的方法
                                startTimeSubmit();
                            }
                        } else {
                            ss -= 1;
                            if (ss >= 10) {
                                timeText.setText(mm + ":" + ss);
                            } else {
                                timeText.setText(mm + ":0" + ss);
                            }
                        }
                    }
                    break;
            }
        }
    };
    /**
     * @author bin
     * 时间: 2016/6/30 18:01
     * 方法说明:开启时间到提示交卷的方法
     */
    public void startTimeSubmit(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                    	if(isPauseing){
                            return;
                        }
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timePosition -=1;
                                if(timePosition==0){
                                    isNext = false;
                                    timeExit = false;
                                    submitDialog.dismiss();
                                    //提交试卷
                                    submitExamPaper();
                                }else{
                                    time_exit.setText("即将退出("+timePosition+")");
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
    	isNext = true;
    	exit = true;
        //显示退出的dialog
        showCommitDialog("确定要退出吗?");
    }

    /**
     * @author bin
     *         时间: 2016/6/4 20:32
     *         类说明:切换试题的广播
     */
    class BroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("exam".equals(action)) {
                int position = intent.getIntExtra("position", 0);
                viewPager.setCurrentItem((position));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadCast != null) {
            unregisterReceiver(broadCast);
        }
        if (timer != null) {
            timer.cancel();
        }
    }
}
