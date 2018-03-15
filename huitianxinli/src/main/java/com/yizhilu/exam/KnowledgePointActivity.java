package com.yizhilu.exam;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;



import com.yizhilu.application.BaseActivity;
import com.yizhilu.exam.adapter.KnowledgePointAdapter;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ConstantUtils;

/**
 * @author bin
 *         时间: 2016/6/1 11:23
 *         类说明:知识点练习页
 */
public class KnowledgePointActivity extends BaseActivity {

    ExpandableListView pointListView;  //知识点的列表

    TextView fifteenText;  //来15道

    TextView orderText;  //顺序

    ImageView backImage;  //返回
    private PublicEntity publicEntity;  //实体类
    private KnowledgePointAdapter pointAdapter;  //知识点列表的适配器
    private List<PublicEntity> pointList;  //知识点集合
    private int pointiId;  //知识点Id
    private Intent intent;  //意图对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_knowledge_point);
        pointListView = (ExpandableListView) findViewById(R.id.pointListView);
        fifteenText = (TextView) findViewById(R.id.fifteen_text);
        orderText = (TextView) findViewById(R.id.order_text);
        backImage = (ImageView) findViewById(R.id.back_image);

        //获取传过来的信息
        getIntentMessage();
        super.onCreate(savedInstanceState);
    }

    /**
     * @author bin
     * 时间: 2016/6/4 10:23
     * 方法说明:获取传过来的信息
     */
    public void getIntentMessage() {
        Intent intent = getIntent();
        publicEntity = (PublicEntity) intent.getSerializableExtra("publicEntity");  //传过来的实体类
    }

    @Override
    public void initView() {
        intent = new Intent();  //意图对象
        pointList = publicEntity.getEntity().getPointList();
        pointAdapter = new KnowledgePointAdapter(KnowledgePointActivity.this, pointList);
        pointListView.setAdapter(pointAdapter);
    }

    @Override
    public void addOnClick() {
        backImage.setOnClickListener(this);  //返回
        pointListView.setOnGroupClickListener(this);  //父的点击事件
        pointListView.setOnChildClickListener(this);  //子的点击事件
        fifteenText.setOnClickListener(this);  //来15道
        orderText.setOnClickListener(this);  //顺序
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        List<PublicEntity> examPointSon = pointList.get(groupPosition).getExamPointSon();
        if (examPointSon == null || examPointSon.size() <= 0) {
            pointiId = pointList.get(groupPosition).getId();  //知识点Id
            pointAdapter.setGroupPosition(groupPosition);
            pointAdapter.notifyDataSetChanged();
            return false;
        }
        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        pointiId = pointList.get(groupPosition).getExamPointSon().get(childPosition).getId();
        pointAdapter.setGroupPosition(groupPosition);
        pointAdapter.setChildPosition(childPosition);
        pointAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.back_image:  //返回
                KnowledgePointActivity.this.finish();
                break;
            case R.id.fifteen_text:  //来15道
                if(pointiId == 0){
                    ConstantUtils.showMsg(KnowledgePointActivity.this,"请选择知识点");
                }else{
                    intent.setClass(KnowledgePointActivity.this, BeginExamActivity.class);
                    intent.putExtra("examName","知识点练习");
                    intent.putExtra("type","15");
                    intent.putExtra("pointId",pointiId);
                    startActivity(intent);
                    KnowledgePointActivity.this.finish();
                }
                break;
            case R.id.order_text:  //顺序
                if(pointiId == 0){
                    ConstantUtils.showMsg(KnowledgePointActivity.this,"请选择知识点");
                }else{
                    intent.setClass(KnowledgePointActivity.this, BeginExamActivity.class);
                    intent.putExtra("examName","知识点练习");
                    intent.putExtra("type","100");
                    intent.putExtra("pointId",pointiId);
                    startActivity(intent);
                    KnowledgePointActivity.this.finish();
                }
                break;
        }
    }
}
