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

public class ShopItemPagerAdapter extends FragmentPagerAdapter {

    SupportFragment[] mFragments;

    String[] mTitles;

    public ShopItemPagerAdapter(FragmentManager fm, String[] titles,SupportFragment ... fragments){
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }

    public void updateType(int type){
        for (int i=0;i<mFragments.length;i++){
            if (i<2)
                ((OrderFragment)mFragments[i]).updateType(type);
            else
                ((OrderChildFragment)mFragments[i]).updateType(type);
        }
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
