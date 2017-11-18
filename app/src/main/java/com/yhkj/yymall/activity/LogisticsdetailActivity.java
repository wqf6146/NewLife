package com.yhkj.yymall.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.CommonTabPagerAdapter;
import com.yhkj.yymall.adapter.ShopItemPagerAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.GetServerBean;
import com.yhkj.yymall.bean.LogisticsBean;
import com.yhkj.yymall.fragment.LogisticsdetailFragment;
import com.yhkj.yymall.fragment.OrderChildFragment;
import com.yhkj.yymall.fragment.OrderFragment;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;

import butterknife.Bind;
import me.yokeyword.fragmentation.SupportFragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/7/19.
 */

public class LogisticsdetailActivity extends BaseToolBarActivity {

    @Bind(R.id.ald_rl_bottom)
    RelativeLayout mRlServer;

    @Bind(R.id.ald_tablayout)
    TabLayout mTabLayout;

    @Bind(R.id.ald_vp_viewpager)
    ViewPager mViewPager;

    @Bind(R.id.al_rl_nodata)
    RelativeLayout mRlNodata;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logisticsdetail);
    }

    private int mOrderId;
    @Override
    protected void initView() {
        super.initView();
        mOrderId = getIntent().getIntExtra("id",-1);

    }
    private GetServerBean.DataBean mDataBean;

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        YYMallApi.getService(this, new YYMallApi.ApiResult<GetServerBean.DataBean>(this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
            }

            @Override
            public void onNext(GetServerBean.DataBean dataBean) {
                mDataBean = dataBean;
            }

            @Override
            public void onStart() {

            }
        });
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mRlServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //联系官方客服
//                if (CommonUtil.isQQClientAvailable(LogisticsdetailActivity.this) && mDataBean!=null && mDataBean.getInfo()!=null)
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + mDataBean.getInfo().getQq() + "")));
//                else
//                    showToast("请先安装QQ");
                Intent intent = new Intent();
                intent.setClass(LogisticsdetailActivity.this, ChatLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onReLoadClickLisiten() {
        if (mOrderId != -1) {
            super.onReLoadClickLisiten();
            getData();
        }
    }

    @Override
    protected void initData() {
        setTvTitleText("物流详情");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        if (mOrderId != -1)
            getData();
    }

    public void getData() {
        YYMallApi.getLogisticsDetail(this, String.valueOf(mOrderId), new YYMallApi.ApiResult<LogisticsBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(LogisticsBean dataBean) {
                setNetWorkErrShow(GONE);
                if (dataBean.getCode() != 0){
                    ViseLog.e(dataBean.getMsg());
                    showToast(dataBean.getMsg());
                    return;
                }
                if (dataBean == null || dataBean.getData() == null || dataBean.getData().size() == 0){
                    mRlNodata.setVisibility(View.VISIBLE);
                }else {
                    mRlNodata.setVisibility(GONE);
                    setUiData(dataBean);
//                    mLlLogData.setVisibility(View.VISIBLE);
//                    mLlLoginfo.setVisibility(View.VISIBLE);
//                    initUidata(dataBean.getData().get(0));
                }
            }
        });
    }

    private CommonTabPagerAdapter mAdapter;
    private void setUiData(LogisticsBean dataBean) {
        String[] strings = new String[dataBean.getData().size()];
        SupportFragment[] fragments = new SupportFragment[dataBean.getData().size()];
        for (int i=0;i<strings.length;i++){
            int tag = i+1;
            strings[i] = "包裹" + tag;
            fragments[i] = LogisticsdetailFragment.getInstance();
            Bundle bundles = fragments[i].getArguments();
            bundles.putParcelable("data",dataBean.getData().get(i));
        }
        mAdapter = new CommonTabPagerAdapter(getSupportFragmentManager(),strings, fragments);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(fragments.length);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
//        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        if (dataBean.getData().size() == 1)
            mTabLayout.setVisibility(GONE);
    }

}
