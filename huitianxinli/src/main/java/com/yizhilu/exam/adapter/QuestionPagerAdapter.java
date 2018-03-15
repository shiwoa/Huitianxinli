package com.yizhilu.exam.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 作者：caibin
 * 时间：2016/6/4 18:08
 * 类说明：试题的适配器
 */
public class QuestionPagerAdapter extends FragmentPagerAdapter{
    private List<Fragment> fragments;  //试题的集合

    public QuestionPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int arg0) {
        return fragments.get(arg0);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
