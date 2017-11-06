package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.ShopItemPagerAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.fragment.GroupFragment;
import com.yhkj.yymall.fragment.OrderFragment;
import com.yhkj.yymall.view.popwindows.OrderMenuPopupView;

import butterknife.Bind;

import static android.view.View.GONE;
import static com.yhkj.yymall.base.Constant.ACTIVITY_BUNDLE;

/**
 * Created by Administrator on 2017/7/3.
 */
public class MineGroupActivity extends BaseToolBarActivity {

    @Bind(R.id.amo_tablayout)
    TabLayout mTabLayout;

    @Bind(R.id.amo_viewpager)
    ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineorder);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
        String[] strings = new String[]{
                "全部",
                "等待成团",
                "拼团成功",
                "拼团失败",
        };

        ShopItemPagerAdapter adapter = new ShopItemPagerAdapter(getSupportFragmentManager(),strings,
                GroupFragment.getInstance(Constant.TYPE_FRAGMENT_GROUP.ALL),
                GroupFragment.getInstance(Constant.TYPE_FRAGMENT_GROUP.UNGROUP),
                GroupFragment.getInstance(Constant.TYPE_FRAGMENT_GROUP.GROUPSUCSS),
                GroupFragment.getInstance(Constant.TYPE_FRAGMENT_GROUP.GROUPFAILD));

        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);

    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    @Override
    protected void initData() {
        setTvTitleText("我的拼团");
        setImgRightVisiable(View.VISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
    }
}
