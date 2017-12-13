package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.vise.log.ViseLog;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.SeckillFragmentAdapter;
import com.yhkj.yymall.adapter.TtimeLimitPagerAdapter;
import com.yhkj.yymall.bean.TimeKillClassifBean;
import com.yhkj.yymall.bean.TimeKillDateBean;
import com.yhkj.yymall.fragment.ShopListFragment;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.popwindows.ShopClassifyPopView;
import com.yhkj.yymall.view.viewpager.PagerAdapter;
import com.yhkj.yymall.view.viewpager.UltraViewPager;
import com.yhkj.yymall.view.viewpager.ViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/6/27.
 */

public class SeckillingActivity extends BaseToolBarActivity implements ShopClassifyPopView.OnClassifyUpdate{

    @Bind(R.id.ft_ultraviewViewpager)
    UltraViewPager mUltraView;

    @Bind(R.id.ft_viewpager)
    ViewPager mViewPager;

    private TtimeLimitPagerAdapter mHeadAdapter = null;

    @Override
    public void onClassifyUpdate(Integer id){
        updateFragment(id);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_timekill);
    }

    private List<TimeKillClassifBean.DataBean.CategorysBean> mClassifyBean;
    private TimeKillClassifBean.DataBean.CategorysBean mCurCateGorys;
    @Override
    protected void onActivityLoadFinish(){
        super.onActivityLoadFinish();
        YYMallApi.getTimeKillClassify(this, new YYMallApi.ApiResult<TimeKillClassifBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(TimeKillClassifBean.DataBean dataBean) {
                mClassifyBean = dataBean.getCategorys();
                mCurCateGorys = mClassifyBean.get(0);
            }
        });
        getData(null);
    }

    @Override
    protected void onReLoadClickLisiten(){
        super.onReLoadClickLisiten();
        YYMallApi.getTimeKillClassify(this, new YYMallApi.ApiResult<TimeKillClassifBean.DataBean>(this){
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(TimeKillClassifBean.DataBean dataBean){
                mClassifyBean = dataBean.getCategorys();
                mCurCateGorys = mClassifyBean.get(0);
            }
        });
        getData(null);
    }

    // param updatePaniclBuy ：要更新哪个fragment
    public void getData(final Integer paniclbuyId){
        YYMallApi.getTimeKillDate(this, new YYMallApi.ApiResult<TimeKillDateBean.DataBean>(this){
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                if (e.getCode() == 6011){
                    setImgRightVisiable(GONE);
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无活动");
                    return;
                }
                setNetWorkErrShow(View.VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(TimeKillDateBean.DataBean dataBean){
                setNetWorkErrShow(GONE);
                if (dataBean.getPaniclBuy() == null || dataBean.getPaniclBuy().size() == 0){
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无活动");
                    return;
                }
                setImgRightVisiable(VISIBLE);
                if (!compareData(dataBean)){
                    mDateBean = dataBean;
                    setData(dataBean,paniclbuyId);
                }else{
                    for (int i=0 ;i<mShopListAdapter.getCount(); i++){
                        ShopListFragment fragment = (ShopListFragment) mShopListAdapter.getFragments().get(mShopListAdapter.getItemId(i));
                        if (fragment!=null && fragment.isRefresh()){
                            fragment.startRefresh();
                        }
                    }
                }
            }
        });
    }

    private boolean compareData(TimeKillDateBean.DataBean dataBean) {
        if (mDateBean == null || mDateBean.getPaniclBuy() == null)
            return false;
        return mDateBean.getPaniclBuy().equals(dataBean.getPaniclBuy());
    }

    private TimeKillDateBean.DataBean mDateBean;
    private void setData(TimeKillDateBean.DataBean dataBean,final Integer paniclbuyId){
        mUltraView.disableAutoScroll();
        mUltraView.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        mHeadAdapter = new TtimeLimitPagerAdapter(this,dataBean.getPaniclBuy());
        mHeadAdapter.setOnItemClickLisiten(new TtimeLimitPagerAdapter.OnItemClickListen(){
            @Override
            public void onItemClick(int position) {
                mUltraView.setCurrentItem(position,true);
                mViewPager.setCurrentItem(position,true);
            }
        });
        mUltraView.setAdapter(mHeadAdapter);
        mUltraView.setMultiScreen(0.33f);
        mViewPager.setMultiScreen(0.33f);
        mUltraView.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                mViewPager.setCurrentItem(position,true);
//                mUltraView.setCurrentItem(position,true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        if (mShopListAdapter == null){
            mShopListAdapter = new SeckillFragmentAdapter(getSupportFragmentManager(), dataBean.getPaniclBuy())
                    .setOnUpdateFragmentListen(new SeckillFragmentAdapter.OnUpdateFragmentListen() {
                        @Override
                        public void updateFragment(ShopListFragment fragment,int paniclBuyId) {
                            if (fragment.isRefresh()){
                                fragment.startRefresh();
                            }
                        }
                    });
            mViewPager.setAdapter(mShopListAdapter);
            mViewPager.setFlolwViewPager(mUltraView.getViewPager());
            mViewPager.setOffscreenPageLimit(dataBean.getPaniclBuy().size());
        }else{
            mShopListAdapter.setPaniclBuyBean(dataBean.getPaniclBuy());
            mViewPager.setAdapter(mShopListAdapter);

            if (paniclbuyId == null){
                mUltraView.setCurrentItem(0);
                mViewPager.setCurrentItem(0);
            }else{
                boolean bJumb = false;
                for (int i=0;i<dataBean.getPaniclBuy().size();i++){
                    if (paniclbuyId == dataBean.getPaniclBuy().get(i).getId()){
                        mViewPager.setCurrentItem(i);
                        mUltraView.setCurrentItem(i);
                        bJumb = true;
                        break;
                    }
                }
                if (!bJumb){
                    mUltraView.setCurrentItem(0);
                    mViewPager.setCurrentItem(0);
                }
            }
        }

//        mUltraView.setCurrentItem(1,true);
//        mViewPager.setCurrentItem(1,true);
//        //推送跳转
//        if (!TextUtils.isEmpty(mShowIndex)){
//            int index = Integer.parseInt(mShowIndex);
////            mViewPager.setCurrentItem(1,true);
////            mUltraView.setCurrentItem(1,true);
//            mShowIndex = null;
//        }else{
////            mUltraView.setCurrentItem(0, false);
//        }
    }

    private void updateFragment(Integer category){
        for (int i=0 ;i<mShopListAdapter.getCount(); i++){
            ShopListFragment fragment = (ShopListFragment) mShopListAdapter.getFragments().get(mShopListAdapter.getItemId(i));
            fragment.updateClassify(category);
        }
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        setImgRightResource(R.mipmap.details_dian);
        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShopClassifyPopView(SeckillingActivity.this).setOnClassifyUpdate(SeckillingActivity.this).showPopupWindow(v);
            }
        });
    }

    private SeckillFragmentAdapter mShopListAdapter;


    //推送跳转
//    private String mShowIndex;
    @Override
    protected void initData() {
        setTvTitleText("限时抢购");
        setTitleWireVisiable(GONE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
//        mShowIndex = getIntent().getStringExtra("panicBuyId");
    }
}

