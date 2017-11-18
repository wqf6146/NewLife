package com.yhkj.yymall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.yhkj.yymall.fragment.OrderChildFragment;
import com.yhkj.yymall.fragment.OrderFragment;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Administrator on 2017/7/3.
 */

public class CommonTabPagerAdapter extends FragmentPagerAdapter {

    SupportFragment[] mFragments;

    String[] mTitles;

    public CommonTabPagerAdapter(FragmentManager fm, String[] titles, SupportFragment ... fragments){
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }
}
