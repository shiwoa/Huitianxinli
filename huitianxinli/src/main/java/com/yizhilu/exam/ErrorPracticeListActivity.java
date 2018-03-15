package com.yizhilu.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;



import com.yizhilu.application.BaseActivity;
import com.yizhilu.exam.adapter.CapacityErrorAdapter;
import com.yizhilu.huitianxinli.R;

/**
 * @author bin
 *         时间: 2016/6/2 20:35
 *         类说明:智能错题练习列表
 */
public class ErrorPracticeListActivity extends BaseActivity implements RecordInterface{


    ImageView sideMenu;  //返回图片

    LinearLayout leftLayout;  //返回布局

    TextView title;  //标题

    ListView errorList;  //智能做题列表
    private int[] titles, contents, models;  //标题,内容,模式
    private CapacityErrorAdapter capacityErrorAdapter;  //智能错题列表的适配器
    private Intent intent;  //意图对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_error_practice_list);
        sideMenu = (ImageView) findViewById(R.id.side_menu);
        leftLayout = (LinearLayout) findViewById(R.id.left_layout);
        title = (TextView) findViewById(R.id.title);
        errorList = (ListView) findViewById(R.id.errorList);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        intent = new Intent();  //意图对象
        titles = new int[]{R.string.orderPractice, R.string.randomPractice, R.string.systemPractice};
        contents = new int[]{R.string.orderContent, R.string.randomContent, R.string.systemContent};
        models = new int[]{R.string.examModel, R.string.practiceModel};
        leftLayout.setVisibility(View.VISIBLE);  //返回的布局
        sideMenu.setVisibility(View.VISIBLE);  //返回的图片
        sideMenu.setBackgroundResource(R.mipmap.return_button);  //设置返回图片
        title.setText(R.string.error_practice);  //设置标题
        capacityErrorAdapter = new CapacityErrorAdapter(ErrorPracticeListActivity.this,titles,contents,models);
        capacityErrorAdapter.setRecordInterface(ErrorPracticeListActivity.this);  //绑定点击事件
        errorList.setAdapter(capacityErrorAdapter);
    }

    @Override
    public void addOnClick() {
        leftLayout.setOnClickListener(this);  //返回
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.left_layout:  //返回
                ErrorPracticeListActivity.this.finish();
                break;
        }
    }

    @Override
    public void myClick(int position, String name) {
        intent.setClass(ErrorPracticeListActivity.this,BeginExamActivity.class);
        intent.putExtra("examName","错题智能练习");
        if("考试模式".equals(name)){
            intent.putExtra("practiceType",1);  //1为：考试模式
        }else if("练习模式".equals(name)){
            intent.putExtra("practiceType",2);  //2为：练习模式
        }
        startActivity(intent);
    }
}
