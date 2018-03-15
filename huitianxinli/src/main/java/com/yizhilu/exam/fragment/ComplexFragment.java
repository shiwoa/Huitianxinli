package com.yizhilu.exam.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yizhilu.application.BaseFragment;
import com.yizhilu.exam.entity.PublicEntity;
import com.yizhilu.huitianxinli.R;

/**
 * 作者：caibin
 * 时间：2016/6/8 11:28
 * 类说明：材料题材料的fragment
 */
public class ComplexFragment extends BaseFragment {
    private View inflate;  //总布局
    private TextView complexName;   //材料题
    private PublicEntity publicEntity;  //材料的实体
    @Override
    public View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflate = inflater.inflate(R.layout.fragment_complex, container, false);
        //获取传过来的信息
        getIntentMessage();
        return inflate;
    }

    /**
     * @author bin
     * 时间: 2016/6/4 17:54
     * 方法说明:获取传过来的信息
     */
    public void getIntentMessage() {
        Bundle arguments = getArguments();  //获取传过来的信息
        publicEntity = (PublicEntity) arguments.getSerializable("entity");  //考试的总的实体
    }

    @Override
    public void initView() {
        complexName = (TextView) inflate.findViewById(R.id.complexName);  //材料题的材料
        complexName.setText(publicEntity.getComplexContent());  //设置材料题的材料
    }

    @Override
    public void addOnClick() {

    }
}
