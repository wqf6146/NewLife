package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.animation.Animation;

import com.yhkj.yymall.BaseActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.ShopBannerAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.BannerItemBean;
import com.yhkj.yymall.fragment.ShopBannerDetailFragment;
import com.yhkj.yymall.view.pageindicatorview.PageIndicatorView;

import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/6/20.
 */

public class ShopBannerActivity extends BaseActivity {

    @Bind(R.id.as_viewpager)
    ViewPager mViewPager;

    @Bind(R.id.as_pageIndicatorView)
    PageIndicatorView mIndicatorView;

    private int mPos;

    private List<BannerItemBean> mData;
    private Fragment[] mFragments;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopbanner);
    }

    @Override
    public void onBackPressedSupport() {
        finish();
        overridePendingTransition(Animation.INFINITE, Animation.INFINITE);
    }

    @Override
    protected void initView() {
        setNetWorkErrShow(GONE);
        setStatusViewVisiable(false);
    }

    @Override
    protected void bindEvent() {
    }

    @Override
    protected void initData() {
        mData = getIntent().getParcelableArrayListExtra(Constant.BANNER.ITEMBEAN);
        mPos = getIntent().getIntExtra(Constant.BANNER.POSITION,0);
        buildFragment();
        ShopBannerAdapter adapter = new ShopBannerAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(mFragments.length);
        mViewPager.setCurrentItem(mPos);
    }

    public void buildFragment(){
        mFragments = new Fragment[mData.size()];
        for (int i=0;i<mData.size();i++){
            BannerItemBean bean = mData.get(i);
            mFragments[i] = ShopBannerDetailFragment.getInstance();
            Bundle bundles = mFragments[i].getArguments();
            if (bean.type == Constant.BANNER.VIDEO) {
                bundles.putInt(Constant.BANNER.BANNER_TYPE, Constant.BANNER.VIDEO);
                bundles.putString(Constant.BANNER.URL_VIDEO,bean.url);
                bundles.putString(Constant.BANNER.URL_IMG,bean.img);
            }else if (bean.type == Constant.BANNER.FILE) {
                bundles.putInt(Constant.BANNER.BANNER_TYPE, Constant.BANNER.FILE);
                bundles.putSerializable(Constant.BANNER.URL_FILE,bean.file);
            }else {
                bundles.putInt(Constant.BANNER.BANNER_TYPE, Constant.BANNER.IMG);
                bundles.putString(Constant.BANNER.URL_IMG,bean.img);
            }
        }
    }
}

