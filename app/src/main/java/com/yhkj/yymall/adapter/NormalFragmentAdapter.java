package com.yhkj.yymall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by Administrator on 2017/9/22.
 */

public class NormalFragmentAdapter extends FragmentPagerAdapter {

    private SupportFragment[] mFragment;

    public NormalFragmentAdapter(FragmentManager fm, SupportFragment ... fragments){
        super(fm);
        mFragment = fragments;
    }

    public void setData(SupportFragment ... fragments){
        mFragment = fragments;
    }

    @Override
    public long getItemId(int position) {
        return mFragment[position].hashCode();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment[position];
    }

    @Override
    public int getCount() {
        return mFragment.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
