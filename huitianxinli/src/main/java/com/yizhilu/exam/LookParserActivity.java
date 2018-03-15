package com.yizhilu.exam;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseActivity;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.exam.adapter.ExamButtomAdapter;
import com.yizhilu.exam.adapter.ParserAnswerAdapter;
import com.yizhilu.exam.adapter.QuestionPagerAdapter;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.exam.fragment.LookParserFragment;
import com.yizhilu.exam.utils.StaticUtils;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.Address;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.ExamAddress;
import com.yizhilu.utils.StringUtil;

/**
 * @author bin
 *         时间: 2016/6/12 18:09
 *         类说明: 查看解析的类
 */
public class LookParserActivity extends BaseActivity implements ViewPager.OnPageChangeListener {

    ImageView sideMenu;

    LinearLayout leftLayout;

    TextView title;

    GridView gridView;

    TextView examType;

    TextView currentNumber;

    TextView allNumber;

    ViewPager viewPager;

    ViewPager errorViewPager;
    //查看错题解析

    TextView typeNumber;

    TextView typeAllnumber;

    TextView bracketText;
    private int[] imageGrid, textGrid;  //图片和文字的数组
    private int userId, subjectId, id;  //用户Id,专业Id,试卷id
    private ProgressDialog progressDialog;  //加载数据显示
    private PublicEntity publicEntity;  //解析的实体类
    private List<PublicEntity> queryQuestionList;  //全部试题的集合
    private List<PublicEntity> errorQuestionList;  //错误试题的集合
    private List<PublicEntity> paperMiddleList;  //试卷解析的实体
    private List<Boolean> collectList;  //收藏的集合
    private int examPositionZ = 0, questionPosition;  //试题总的下标,当前试题的下标
    private LookParserFragment lookParserFragment;  //查看解析的faragment
    private List<Fragment> fragments;  //查看解析类的集合
    private QuestionPagerAdapter questionPagerAdapter;  //试题的适配器
    private List<PublicEntity> entityList;  //解析实体的总集合
    private ExamButtomAdapter adapterGrid;  //底部导航适配器
    private int type, questionId;  //是练习还是试卷,当前试题的Id
    private List<String> noteList;  //笔记的集合
    private Intent intent;  //意图对象
    private Dialog answerSheetDialog;  //显示答题卡得dialog
    private ParserAnswerAdapter answerAdapter;  //答题卡的适配器
    private GridView sheet_gridView;  //答题卡
    private TextView cancel_sheet, start_answer;  //取消和我要交卷
    private int danZong, danPosition, duoZong, duoPosition, panZong, panPosition, tianZong, tianPosition, lunZong, lunPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_look_parser);
        sideMenu = (ImageView) findViewById(R.id.side_menu);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        title = (TextView) findViewById(R.id.title);
        gridView = (GridView) findViewById(R.id.gridView);
        examType = (TextView) findViewById(R.id.exam_type);
        currentNumber = (TextView) findViewById(R.id.currentNumber);
        allNumber = (TextView) findViewById(R.id.allNumber);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        errorViewPager = (ViewPager) findViewById(R.id.errorViewPager);
        typeNumber = (TextView) findViewById(R.id.type_number);
        typeAllnumber = (TextView) findViewById(R.id.typeAllnumber);
        bracketText = (TextView) findViewById(R.id.bracket_text);

        //获取传过来的信息
        getIntentMessage();
        super.onCreate(savedInstanceState);
    }

    /**
     * @author bin
     * 时间: 2016/6/13 9:42
     * 方法说明:获取传过来的信息
     */
    public void getIntentMessage() {
        userId = getSharedPreferences("userId", MODE_PRIVATE).getInt("userId", 0);
        subjectId = getSharedPreferences("subjectId", MODE_PRIVATE).getInt("subjectId", 0);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
    }

    @Override
    public void initView() {
        intent = new Intent();  //意图对象
        progressDialog = new ProgressDialog(LookParserActivity.this);  //联网获取数据
        noteList = new ArrayList<String>();  //笔记的集合
        entityList = new ArrayList<PublicEntity>();  //解析实体的集合
        collectList = new ArrayList<Boolean>();  //收藏的集合
        fragments = new ArrayList<Fragment>();  //存放查看解析的集合
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.look_parser);  //设置标题
        imageGrid = new int[]{R.mipmap.error_topic, R.mipmap.answer_sheet, R.mipmap.look_error, R.mipmap.parser_collect}; // 收藏，标记，纠错，下次再做
        textGrid = new int[]{R.string.topic_error, R.string.answer_sheet, R.string.look_error, R.string.collect};
        adapterGrid = new ExamButtomAdapter(LookParserActivity.this, imageGrid, textGrid, 2);
        gridView.setAdapter(adapterGrid);
        // 联网获取解析的方法
        getLookPaserData(userId, subjectId, id);
    }

    @Override
    public void addOnClick() {
        leftLayout.setOnClickListener(this);  //返回
        viewPager.setOnPageChangeListener(this);  //切换调用
        errorViewPager.setOnPageChangeListener(this);  //错误习题切换
        gridView.setOnItemClickListener(this);  //底部导航点击事件
    }

    /**
     * 联网获取解析的方法
     */
    private void getLookPaserData(int userId, int subId, int id) {
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
                                if (publicEntity.isSuccess()) {
                                    PublicEntity paperRecord = publicEntity.getEntity().getPaperRecord();
                                    type = paperRecord.getType();
                                    if (type != 2) {  //练习
                                        //解析数据的方法
                                        parserJsonMessage();
                                    } else {  //试卷
                                        //解析试卷数据的方法
                                        parserPaperJsonMessage();
                                    }
                                } else {
                                    ConstantUtils.showMsg(LookParserActivity.this, message);
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
     * 时间: 2016/6/6 15:44
     * 方法说明:解析练习数据的方法
     */
    public void parserJsonMessage() {
        queryQuestionList = publicEntity.getEntity().getQueryQuestionList();
        if (queryQuestionList == null || queryQuestionList.size() <= 0) {
//            nullLayout.setVisibility(View.VISIBLE);
//            nullLayout.setBackgroundResource(R.color.color_f1);
//            nullText.setText("无试题内容");
//            gridViewTwo.setVisibility(View.GONE);
            return;
        }
        //获取不同试题类型总题数和当前题个数的方法
        getQuestionNumber(queryQuestionList, 0, false, null);
        for (int i = 0; i < queryQuestionList.size(); i++) {
            PublicEntity queryQuestionEntity = queryQuestionList.get(i);
            int favoritesId = queryQuestionEntity.getFavoritesId();
            if (favoritesId == 0) {
                collectList.add(false);
            } else {
                collectList.add(true);
            }
            int qstType = queryQuestionEntity.getQstType();
            //获取不同试题类型总题数和当前题个数的方法
            getQuestionNumber(queryQuestionList, qstType, true, queryQuestionEntity);
            entityList.add(queryQuestionEntity);
            noteList.add(StringUtil.isFieldEmpty(queryQuestionEntity.getNoteContent()));  //添加笔记的集合
            examPositionZ++;  //试题下标加
            lookParserFragment = new LookParserFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("entity", queryQuestionEntity);
//                                        bundle.putInt("typePosition",i);
            bundle.putInt("examPosition", i);
            bundle.putInt("zongPosition", examPositionZ);
            lookParserFragment.setArguments(bundle);
            fragments.add(lookParserFragment);  //fragment的集合
        }
        if (collectList.get(0)) {
            textGrid[3] = R.string.collected;
            imageGrid[3] = R.mipmap.parser_collected;
            adapterGrid.notifyDataSetChanged();
        }
        StaticUtils.setNoteList(noteList);  //设置笔记的集合
        int qstType = queryQuestionList.get(0).getQstType();
        setExamType(qstType);  //设置当前题类型
        currentNumber.setText("1");  //当前题
        allNumber.setText("/" + fragments.size() + "题");  //总题数
        questionPagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(questionPagerAdapter);
    }

    /**
     * @author bin
     * 时间: 2016/6/6 15:44
     * 方法说明:解析试卷数据的方法
     */
    public void parserPaperJsonMessage() {
        paperMiddleList = publicEntity.getEntity().getPaperMiddleList();
        if (paperMiddleList == null || paperMiddleList.size() <= 0) {
            return;
        }
        for (int i = 0; i < paperMiddleList.size(); i++) {
            int type = paperMiddleList.get(i).getType();
            if (type != 4) {
                List<PublicEntity> qstMiddleList = paperMiddleList.get(i).getQstMiddleList();
                if (qstMiddleList == null || qstMiddleList.size() <= 0) {
                    continue;
                }
                for (int j = 0; j < qstMiddleList.size(); j++) {
                    PublicEntity middleEntity = qstMiddleList.get(j);
                    int favoritesId = middleEntity.getFavoritesId();
                    if (favoritesId == 0) {
                        collectList.add(false);
                    } else {
                        collectList.add(true);
                    }
                    /**
                     *  setQuestionPosition()  设置某种类型当前题数
                     *  setQuestionZong()  设置某种类型总题数
                     *  因为查看解析上面有当前题型是什么题型  总共多少题  当前是第几题
                     *  接口返回不了这些信息  只能我们本地来记录  所以我在实体中加了两个字段
                     *  因为这是试卷类型 paperMiddleList这个集合下面每个实体是一种题型和练习不一样
                     */
                    middleEntity.setQuestionPosition(j + 1);  //设置某种类型当前题数
                    middleEntity.setQuestionZong(qstMiddleList.size());  //设置某种类型总题数
                    entityList.add(middleEntity);
                    noteList.add(StringUtil.isFieldEmpty(middleEntity.getNoteContent()));  //添加笔记的集合
                    examPositionZ++;  //试题下标加
                    lookParserFragment = new LookParserFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("entity", middleEntity);
//                                        bundle.putInt("typePosition",i);
                    bundle.putInt("examType", 2);  //试卷类型为2
                    bundle.putInt("examPosition", j);
                    bundle.putInt("zongPosition", examPositionZ);
                    lookParserFragment.setArguments(bundle);
                    fragments.add(lookParserFragment);  //fragment的集合
                }
            } else {
                List<PublicEntity> complexList = paperMiddleList.get(i).getComplexList();
                if (complexList == null || complexList.size() <= 0) {
                    continue;
                }
                for (int j = 0; j < complexList.size(); j++) {
                    //得到材料的实体
                    PublicEntity complexEntity = complexList.get(j);
                    List<PublicEntity> queryQstMiddleList = complexEntity.getQueryQstMiddleList();
                    if (queryQstMiddleList == null || queryQstMiddleList.size() <= 0) {
                        continue;
                    }
                    for (int k = 0; k < queryQstMiddleList.size(); k++) {
                        PublicEntity middleEntity = queryQstMiddleList.get(k);
                        int favoritesId = middleEntity.getFavoritesId();
                        if (favoritesId == 0) {
                            collectList.add(false);
                        } else {
                            collectList.add(true);
                        }
                        /**
                         * 原理同上
                         * 这里说一下setComplexType()这个字段，这个字段也是我自己加的
                         * 原因是：材料题和其他类型的题返回的数据类型是不一样的，材料题下面还有单选，多选等题型
                         * 所以要添加一下标识，是不是材料题下面的题型
                         */
                        middleEntity.setComplexContent(complexEntity.getComplexContent());
                        middleEntity.setQuestionPosition(k + 1);  //设置某种类型当前题数
                        middleEntity.setQuestionZong(queryQstMiddleList.size());  //设置某种类型总题数
                        middleEntity.setComplexType("complex");  //标识是材料下面的题
                        entityList.add(middleEntity);
                        noteList.add(StringUtil.isFieldEmpty(middleEntity.getNoteContent()));  //添加笔记的集合
                        examPositionZ++;  //试题下标加
                        lookParserFragment = new LookParserFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("entity", middleEntity);
//                                        bundle.putInt("typePosition",i);
                        bundle.putInt("examPosition", j);
                        bundle.putInt("zongPosition", examPositionZ);
                        lookParserFragment.setArguments(bundle);
                        fragments.add(lookParserFragment);  //fragment的集合
                    }
                }
            }
        }
        if (collectList.get(0)) {
            textGrid[3] = R.string.collected;
            imageGrid[3] = R.mipmap.parser_collected;
            adapterGrid.notifyDataSetChanged();
        }
        StaticUtils.setNoteList(noteList);  //设置笔记的集合
        int qstType = entityList.get(0).getQstType();
        if(qstType == 0){
        	qstType = entityList.get(0).getQuestionType();
        }
        setExamType(qstType);  //设置当前题类型
        currentNumber.setText("1");  //当前题
        allNumber.setText("/" + fragments.size() + "题");  //总题数
        questionPagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(questionPagerAdapter);
    }

    /**
     * @author bin
     * 时间: 2016/6/6 15:44
     * 方法说明:仅查看错题的方法
     */
    public void parserErrorJsonMessage() {
        //获取不同试题类型总题数和当前题个数的方法
        getQuestionNumber(errorQuestionList, 0, false, null);
        for (int i = 0; i < errorQuestionList.size(); i++) {
            PublicEntity errorEntity = errorQuestionList.get(i);
            int favoritesId = errorEntity.getFavoritesId();
            if (favoritesId == 0) {
                collectList.add(false);
            } else {
                collectList.add(true);
            }
            int qstType = errorEntity.getQstType();
            if(qstType == 0){
            	qstType = errorEntity.getQuestionType();
            }
            //获取不同试题类型总题数和当前题个数的方法
            getQuestionNumber(errorQuestionList, qstType, true, errorEntity);
            entityList.add(errorEntity);
            noteList.add(StringUtil.isFieldEmpty(errorEntity.getNoteContent()));  //添加笔记的集合
            examPositionZ++;  //试题下标加
            lookParserFragment = new LookParserFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("entity", errorEntity);
//                                        bundle.putInt("typePosition",i);
            bundle.putInt("examPosition", i);
            bundle.putInt("zongPosition", examPositionZ);
            lookParserFragment.setArguments(bundle);
            fragments.add(lookParserFragment);  //fragment的集合
        }
        if (collectList.get(0)) {
            textGrid[3] = R.string.collected;
            imageGrid[3] = R.mipmap.parser_collected;
            adapterGrid.notifyDataSetChanged();
        }
        StaticUtils.setNoteList(noteList);  //设置笔记的集合
        int qstType = errorQuestionList.get(0).getQstType();
        if(qstType == 0){
        	qstType = errorQuestionList.get(0).getQuestionType();
        }
        setExamType(qstType);  //设置当前题类型
        currentNumber.setText("1");  //当前题
        allNumber.setText("/" + fragments.size() + "题");  //总题数
        questionPagerAdapter = new QuestionPagerAdapter(getSupportFragmentManager(), fragments);
        errorViewPager.setAdapter(questionPagerAdapter);
    }

    /**
     * @author bin
     * 时间: 2016/6/28 20:44
     * 方法说明:获取不同试题类型总题数个数的方法
     */
    public void getQuestionNumber(List<PublicEntity> queryQuestionList, int typeQst, boolean isPosition, PublicEntity entity) {
        if (isPosition) {
            switch (typeQst) {
                case 1:  //单
                    danPosition += 1;
                    entity.setQuestionZong(danZong);
                    entity.setQuestionPosition(danPosition);
                    break;
                case 2:  //多
                    duoPosition += 1;
                    entity.setQuestionZong(duoZong);
                    entity.setQuestionPosition(duoPosition);
                    break;
                case 3:  //判
                    panPosition += 1;
                    entity.setQuestionZong(panZong);
                    entity.setQuestionPosition(panPosition);
                    break;
                case 6:  //论述
                    lunPosition += 1;
                    entity.setQuestionZong(lunZong);
                    entity.setQuestionPosition(lunPosition);
                    break;
                case 7:  //填
                    tianPosition += 1;
                    entity.setQuestionZong(tianZong);
                    entity.setQuestionPosition(tianPosition);
                    break;
            }
        } else {
            for (PublicEntity queryEntity : queryQuestionList) {
                int qstType = queryEntity.getQstType();
                if(qstType == 0){
                	qstType = queryEntity.getQuestionType();
                }
                switch (qstType) {
                    case 1:  //单
                        danZong += 1;
                        break;
                    case 2:  //多
                        duoZong += 1;
                        break;
                    case 3:  //判
                        panZong += 1;
                        break;
                    case 6:  //论述
                        lunZong += 1;
                        break;
                    case 7:  //填
                        tianZong += 1;
                        break;
                }
            }
        }

    }
    /**
     * @author bin
     * 时间: 2016/6/7 20:07
     * 方法说明:设置题的类型
     */
    public void setExamType(int qstType) {
        boolean isComplex;
        if ("complex".equals(StringUtil.isFieldEmpty(entityList.get(questionPosition).getComplexType()))) {
            isComplex = true;
        } else {
            isComplex = false;
        }
        switch (qstType) {
            case 1:  //单选
                if (isComplex) {
                    examType.setText("材料分析题-单选题 (");
                } else {
                    examType.setText("单选题 (");
                }
                break;
            case 2:  //多选
                if (isComplex) {
                    examType.setText("材料分析题-多选题 (");
                } else {
                    examType.setText("多选题 (");
                }
                break;
            case 3:  //判断
                if (isComplex) {
                    examType.setText("材料分析题-判断题 (");
                } else {
                    examType.setText("判断题 (");
                }
                break;
            case 6:  //论述题
                if (isComplex) {
                    examType.setText("材料分析题-论述题 (");
                } else {
                    examType.setText("论述题 (");
                }
                break;
            case 7:  //填空题
                if (isComplex) {
                    examType.setText("材料分析题-填空题 (");
                } else {
                    examType.setText("填空题 (");
                }
                break;
        }
        typeNumber.setVisibility(View.VISIBLE);
        typeAllnumber.setVisibility(View.VISIBLE);
        bracketText.setVisibility(View.VISIBLE);
        typeNumber.setText(entityList.get(questionPosition).getQuestionPosition() + "");
        typeAllnumber.setText("/" + entityList.get(questionPosition).getQuestionZong() + "");
        bracketText.setText(")");
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_layout:  //返回
                LookParserActivity.this.finish();
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        questionPosition = position;
        if (collectList.get(position)) {
            textGrid[3] = R.string.collected;
            imageGrid[3] = R.mipmap.parser_collected;
            adapterGrid.notifyDataSetChanged();
        } else {
            textGrid[3] = R.string.collect;
            imageGrid[3] = R.mipmap.parser_collect;
            adapterGrid.notifyDataSetChanged();
        }
        int qstType = entityList.get(questionPosition).getQstType();  //获取试题类型
        if(qstType == 0){
        	qstType = entityList.get(questionPosition).getQuestionType();
        }
        setExamType(qstType);  //设置当前题类型
        currentNumber.setText((questionPosition + 1) + "");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);
        switch (parent.getId()) {
            case R.id.gridView:  //底部导航
                switch (position) {
                    case 0:  //题目纠错
                        int questionId;
                        if (type != 2) {
                            questionId = entityList.get(questionPosition).getId();
                        } else {
                            questionId = entityList.get(questionPosition).getQstId();
                        }
                        intent.setClass(LookParserActivity.this, CorrectErrorActivity.class);
                        intent.putExtra("questionId", questionId);  //试题Id
                        startActivity(intent);
                        break;
                    case 1:  //答题卡
                        //显示答题卡
                        showAnswerSheet();
                        break;
                    case 2:  //查看错题
                        if (textGrid[2] == R.string.look_error) {
                            errorQuestionList = publicEntity.getEntity().getErrorQuestionList();
                            if (errorQuestionList == null || errorQuestionList.size() <= 0) {
                                ConstantUtils.showMsg(LookParserActivity.this, "无错误试题");
                                return;
                            }
                            collectList.clear();
                            entityList.clear();
                            fragments.clear();
                            noteList.clear();
                            textGrid[2] = R.string.look_all_parser;
                            adapterGrid.notifyDataSetChanged();
                            viewPager.setVisibility(View.GONE);
                            errorViewPager.setVisibility(View.VISIBLE);
                            examPositionZ = 0;
                            //试题类型和类型题数的标识
                            setTypePosition();
                            //查看错题
                            parserErrorJsonMessage();
                        } else {
                            collectList.clear();
                            entityList.clear();
                            fragments.clear();
                            noteList.clear();
                            textGrid[2] = R.string.look_error;
                            adapterGrid.notifyDataSetChanged();
                            viewPager.setVisibility(View.VISIBLE);
                            errorViewPager.setVisibility(View.GONE);
                            examPositionZ = 0;
                            //试题类型和类型题数的标识
                            setTypePosition();
                            if (type != 2) {  //练习
                                //解析数据的方法
                                parserJsonMessage();
                            } else {  //试卷
                                //解析试卷数据的方法
                                parserPaperJsonMessage();
                            }
                        }
                        break;
                    case 3:  //收藏
                        /**
                         * 查看解析分三种类型，练习，考试，错题   错题和练习返回的数据是一样的，
                         * 试卷是qstId，获取qstid为0
                         * 说明是练习和错题了
                         */
                        questionId = entityList.get(questionPosition).getQstId();
                        if (questionId == 0) {
                            questionId = entityList.get(questionPosition).getId();
                        }
                        if (collectList.get(questionPosition)) {  //已收藏
                            //取消收藏
                            isCancelCollect(userId, subjectId, questionId, true);
                        } else {
                            //收藏
                            isCancelCollect(userId, subjectId, questionId, false);
                        }
                        break;
                }
                break;
            case R.id.sheet_gridView:  //答题卡点击事件
                if (textGrid[2] == R.string.look_error) {
                    viewPager.setCurrentItem(position);
                } else {
                    errorViewPager.setCurrentItem(position);
                }
                answerSheetDialog.dismiss();
                break;
        }
    }
    /**
     * @author bin
     * 时间: 2016/6/30 11:34
     * 方法说明:重置试题类型和个数的方法
     */
    public void setTypePosition(){
        danZong = 0;
        danPosition = 0;
        duoZong = 0;
        duoPosition = 0;
        panZong = 0;
        panPosition = 0;
        tianZong = 0;
        tianPosition = 0;
        lunZong = 0;
        lunPosition = 0;
        //当前题的下标
        questionPosition = 0;
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
                                        textGrid[3] = R.string.collect;
                                        imageGrid[3] = R.mipmap.parser_collect;
                                        adapterGrid.notifyDataSetChanged();
                                    } else {
                                        ConstantUtils.showMsg(
                                                LookParserActivity.this, message);
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
                                        textGrid[3] = R.string.collected;
                                        imageGrid[3] = R.mipmap.parser_collected;
                                        adapterGrid.notifyDataSetChanged();
                                    } else {
                                        ConstantUtils.showMsg(
                                                LookParserActivity.this, message);
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
     * 时间: 2016/6/24 19:13
     * 方法说明:显示答题卡的方法
     */
    public void showAnswerSheet() {
        if (answerSheetDialog != null) {
            answerSheetDialog.show();
            answerAdapter = new ParserAnswerAdapter(LookParserActivity.this, entityList);
            sheet_gridView.setAdapter(answerAdapter);
        } else {
            View view = getLayoutInflater().inflate(R.layout.dialog_answer_sheet, null);
            answerSheetDialog = new Dialog(this, R.style.custom_dialog);
            answerSheetDialog.getWindow().setGravity(Gravity.TOP);
            answerSheetDialog.setContentView(view);
            answerSheetDialog.setCancelable(true);
            answerSheetDialog.show();
            WindowManager windowManager = getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = answerSheetDialog.getWindow().getAttributes();
            lp.width = (int) (display.getWidth()); //设置宽度
            answerSheetDialog.getWindow().setAttributes(lp);
            cancel_sheet = (TextView) view.findViewById(R.id.cancel_sheet);  //取消
            start_answer = (TextView) view.findViewById(R.id.start_answer);  //我要交卷
            sheet_gridView = (GridView) view.findViewById(R.id.sheet_gridView);  //答题卡
            cancel_sheet.setOnClickListener(LookParserActivity.this);
            start_answer.setOnClickListener(LookParserActivity.this);
            cancel_sheet.setVisibility(View.GONE);
            start_answer.setVisibility(View.GONE);
            sheet_gridView.setOnItemClickListener(LookParserActivity.this);
            answerAdapter = new ParserAnswerAdapter(LookParserActivity.this, entityList);
            sheet_gridView.setAdapter(answerAdapter);
        }
    }
}
