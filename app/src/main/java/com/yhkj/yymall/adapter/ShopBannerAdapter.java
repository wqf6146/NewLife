package com.yhkj.yymall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ShopBannerAdapter extends FragmentPagerAdapter {

    Fragment[] mFragment;

    public ShopBannerAdapter(FragmentManager fm,Fragment ... fragment){
        super(fm);
        mFragment = fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment[position];
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getCount() {
        return mFragment.length;
    }


}
