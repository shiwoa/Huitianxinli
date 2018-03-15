package com.yizhilu.exam.fragment;

import org.apache.http.Header;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.loopj.android.http.TextHttpResponseHandler;
import com.yizhilu.application.BaseFragment;
import com.yizhilu.application.DemoApplication;
import com.yizhilu.exam.adapter.SubAdapter;
import com.yizhilu.exam.entity.ListEntity;
import com.yizhilu.huitianxinli.R;
import com.yizhilu.utils.ConstantUtils;
import com.yizhilu.utils.ExamAddress;

/**
 * @author bin
 *         时间: 2016/5/31 12:50
 *         类说明:侧滑菜单的类
 */
public class ExamSlidingMenuFragment extends BaseFragment {
    private ExpandableListView subList;//二级目录
    private View inflate;  //侧滑菜单的总布局
    private ProgressDialog progressDialog;  //加载数据显示的dialog
    private ListEntity listEntity;  //专业的集合
    private Intent intent;  //意图对象
    private TextView nullText;
    private LinearLayout nullLayout;  //空布局
    
    @Override
    public View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_exam_sliding_menu, container, false);
        return inflate;
    }

    @Override
    public void initView() {
        progressDialog = new ProgressDialog(getActivity());  //加载数据的dialog
        subList = (ExpandableListView) inflate.findViewById(R.id.sub_list);
        nullLayout  = (LinearLayout) inflate.findViewById(R.id.null_layout);  //空布局
        nullText = (TextView) inflate.findViewById(R.id.null_text);  //无内容显示的文字
        //联网获取专业的列表
        getSubData();
    }

    @Override
    public void addOnClick() {
        subList.setOnGroupClickListener(this);  //父目录点击事件
        subList.setOnChildClickListener(this);  //子目录点击事件
        nullLayout.setOnClickListener(this);  //空布局的点击事件
    }

    /**
     * 联网获取专业的列表
     */
    private void getSubData() {

        DemoApplication.getHttpClient().get(ExamAddress.SUBLIST_URL, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                ConstantUtils.showProgressDialog(progressDialog);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                ConstantUtils.exitProgressDialog(progressDialog);
                nullLayout.setVisibility(View.VISIBLE);
                nullText.setText("无目录节点,点击刷新");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String data) {
                ConstantUtils.exitProgressDialog(progressDialog);
                nullLayout.setVisibility(View.GONE);
                try {
                    if (!TextUtils.isEmpty(data)) {
                        listEntity = JSON.parseObject(data, ListEntity.class);
                        if (ExamSlidingMenuFragment.this.listEntity.isSuccess()) {
                            SubAdapter adapter = new SubAdapter(getActivity(), ExamSlidingMenuFragment.this.listEntity);
                            subList.setAdapter(adapter);
                        } else {
                            ConstantUtils.showMsg(getActivity(), ExamSlidingMenuFragment.this.listEntity.getMessage());
                        }
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        return super.onGroupClick(parent, v, groupPosition, id);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        int subjectId = listEntity.getEntity().get(groupPosition).getSubjectList().get(childPosition).getSubjectId();
        String subjectName = listEntity.getEntity().get(groupPosition).getSubjectList().get(childPosition).getSubjectName();
        getActivity().getSharedPreferences("subjectId",getActivity().MODE_PRIVATE).edit().putInt("subjectId",subjectId).commit();
        getActivity().getSharedPreferences("subjectName",getActivity().MODE_PRIVATE).edit().putInt("subjectName",subjectId).commit();
        intent = new Intent("slidingClose");  //发向首页,目的:关闭侧滑菜单
        intent.putExtra("subName",subjectName);  //专业的名字
        getActivity().sendBroadcast(intent);
        return false;
    }
    @Override
    public void onClick(View v) {
    	super.onClick(v);
    	switch (v.getId()) {
		case R.id.null_layout://空布局
			getSubData();
			break;

		default:
			break;
		}
    }
}
