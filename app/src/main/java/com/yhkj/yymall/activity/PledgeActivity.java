package com.yhkj.yymall.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.CommonPagerAdapter;
import com.yhkj.yymall.fragment.EfficacyFragment;
import com.yhkj.yymall.fragment.LevelUpFragment;
import com.yhkj.yymall.fragment.PrivilegeLevelFragment;

import java.lang.reflect.Field;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/31.
 */

public class PledgeActivity  extends BaseToolBarActivity {

    @Bind(R.id.ap_tablayout)
    TabLayout mTabLayout;

    @Bind(R.id.ap_viewpager)
    ViewPager mViewPager;

    String[] mTitles = new String[]{
        "未失效押金", "失效押金",
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pledge);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }


    @Override
    protected void initData() {
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        setTvTitleText("我的押金");
        CommonPagerAdapter adapter = new CommonPagerAdapter(getSupportFragmentManager(),mTitles,
                EfficacyFragment.getInstance(0), EfficacyFragment.getInstance(1));
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

        mTabLayout.post(new Runnable() {
            @Override
            public void run() {
                try {
                    setIndicator(mTabLayout,50,50);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
