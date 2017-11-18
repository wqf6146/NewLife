package com.yhkj.yymall.adapter;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.yhkj.yymall.activity.ShopListActivity;
import com.yhkj.yymall.bean.TimeKillDateBean;
import com.yhkj.yymall.fragment.ShopListFragment;
import com.yhkj.yymall.view.viewpager.PagerAdapter;
import com.yhkj.yymall.view.viewpager.UpdatableFragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2017/6/27.
 */

public class SeckillFragmentAdapter extends UpdatableFragmentPagerAdapter {

    private List<TimeKillDateBean.DataBean.PaniclBuyBean> mPaniclBuyBean;

    public SeckillFragmentAdapter(FragmentManager fm, List<TimeKillDateBean.DataBean.PaniclBuyBean> paniclBuyBean) {
        super(fm);
        mPaniclBuyBean = paniclBuyBean;
    }

    public void setPaniclBuyBean(List<TimeKillDateBean.DataBean.PaniclBuyBean> paniclBuyBean) {
        this.mPaniclBuyBean = paniclBuyBean;
    }

    @Override
    public Fragment getItem(int position) {
        ShopListFragment newFragment = ShopListFragment.getInstance();
        Bundle bundle = newFragment.getArguments();
        bundle.putString("paniclBuyId", String.valueOf(mPaniclBuyBean.get(position).getId()));
        return newFragment;
    }

    @Override
    public int getCount() {
        return mPaniclBuyBean.size();
    }

    @Override
    public long getItemId(int position) {
        return mPaniclBuyBean.get(position).getId();
    }

    @Override
    public int getItemPosition(Object object) {
        ShopListFragment fragment = (ShopListFragment) object;
        int id = Integer.parseInt(fragment.getPaniclBuyId());
        for (int i = 0; i < mPaniclBuyBean.size(); i++) {
            if (mPaniclBuyBean.get(i).getId() == id) {
                if (mOnUpdateFragmentListen!=null)
                    mOnUpdateFragmentListen.updateFragment(fragment,id);
                return i;
            }
        }
        return POSITION_NONE;
    }

    private OnUpdateFragmentListen mOnUpdateFragmentListen;

    public SeckillFragmentAdapter setOnUpdateFragmentListen(OnUpdateFragmentListen onUpdateFragmentListen) {
        this.mOnUpdateFragmentListen = onUpdateFragmentListen;
        return this;
    }

    public interface OnUpdateFragmentListen{
        void updateFragment(ShopListFragment fragment,int paniclBuyId);
    }
}
