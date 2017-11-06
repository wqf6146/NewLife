package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;

import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.vise.log.ViseLog;
import com.vise.xsnow.event.EventSubscribe;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.ShopItemPagerAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.event.MainTabSelectEvent;
import com.yhkj.yymall.fragment.OrderChildFragment;
import com.yhkj.yymall.fragment.OrderFragment;
import com.yhkj.yymall.view.popwindows.OrderMenuPopupView;

import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.Bind;

import static android.view.View.GONE;
import static com.yhkj.yymall.base.Constant.ACTIVITY_BUNDLE;

/**
 * Created by Administrator on 2017/7/3.
 */

public class MineOrderActivity extends BaseToolBarActivity {

    @Bind(R.id.amo_tablayout)
    TabLayout mTabLayout;

    @Bind(R.id.amo_viewpager)
    ViewPager mViewPager;

    private int mType = 1; //（1为全部，2为非租赁，3为租赁）
    private ShopItemPagerAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mineorder);
        setOnResumeRegisterBus(true);
    }


    @EventSubscribe
    public void tabSelect(MainTabSelectEvent tabSelectEvent){
        ViseLog.e(tabSelectEvent);
    }

    @Override
    protected void initView() {
        super.initView();
        setNetWorkErrShow(GONE);
        String[] strings = new String[]{
                "全部",
                "待付款",
                "待成团",
                "待收货",
                "待评价"
        };
        mAdapter = new ShopItemPagerAdapter(getSupportFragmentManager(),strings,
                OrderFragment.getInstance(Constant.TYPE_FRAGMENT_ORDER.ALL,mType),
                OrderFragment.getInstance(Constant.TYPE_FRAGMENT_ORDER.UNPAY,mType),
                OrderChildFragment.getInstance(Constant.TYPE_FRAGMENT_ORDER.UNGROUP,mType),
                OrderChildFragment.getInstance(Constant.TYPE_FRAGMENT_ORDER.UNTAKE,mType),
                OrderChildFragment.getInstance(Constant.TYPE_FRAGMENT_ORDER.UNEVALUATE,mType));

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(6);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new OrderMenuPopupView(MineOrderActivity.this).setOnItemClickLisiten(new OrderMenuPopupView.OnItemClickListen() {
                    @Override
                    public void OnItemClickLisiten(int type) {
                        mType = type;
                        mAdapter.updateType(mType);
                    }
                }).showPopupWindow(v);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void initData() {
        setTvTitleText("我的订单");
        setImgRightVisiable(View.VISIBLE);
        setImgRightResource(R.mipmap.ic_nor_screen);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));

        //      getIntent().getBundleExtra(ACTIVITY_BUNDLE).getInt(Constant.TYPE_FRAGMENT_ORDER.TYPE)

        mViewPager.setCurrentItem(getIntent().getBundleExtra(ACTIVITY_BUNDLE).getInt(Constant.TYPE_FRAGMENT_ORDER.TYPE));
    }
}
