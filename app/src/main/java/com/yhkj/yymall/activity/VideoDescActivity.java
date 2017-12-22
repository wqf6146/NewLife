package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.UltraBannerPagerAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.VideoDescBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.viewpager.UltraViewPager;
import com.yhkj.yymall.view.viewpager.UltraViewPagerAdapter;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/12/14.
 */

public class VideoDescActivity extends BaseToolBarActivity {

    @Bind(R.id.av_ultraviewpager)
    UltraViewPager mUltraViewPager;

    @Bind(R.id.av_tv_desc)
    TextView mTvDesc;

    @Bind(R.id.av_tv_nextstep)
    TextView mTvNextStep;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videodesc);
    }

    @Override
    protected void initView() {
        super.initView();
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTitleWireVisiable(View.GONE);
        setTvTitleText("视频监控说明");
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mTvNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VideoDescActivity.this,VideoListActivity.class);
                intent.putParcelableArrayListExtra("banner",mDataBean.getBanner());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    private VideoDescBean.DataBean mDataBean;
    private void getData() {
        YYMallApi.getVideoDesc(this, new ApiCallback<VideoDescBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(VideoDescBean.DataBean dataBean) {
                setNetWorkErrShow(View.GONE);
                mDataBean = dataBean;
                initUi(dataBean);
                mTvDesc.setText(Html.fromHtml(dataBean.getExplain()));
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                setNetWorkErrShow(View.VISIBLE);
            }
        });
    }

    private void initUi(VideoDescBean.DataBean dataBean) {
        UltraBannerPagerAdapter ultraViewPagerAdapter = new UltraBannerPagerAdapter<VideoDescBean.DataBean.BannerBean>(this,
                dataBean.getBanner(),true){
            @Override
            protected void bind(ViewGroup container, VideoDescBean.DataBean.BannerBean bannerBean, int position) {
                final ImageView imageView = (ImageView) container.findViewById(R.id.vb_img);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(mContext).load(bannerBean.getSrc()).placeholder(R.drawable.ic_nor_nobanner).into(imageView);
//                container.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(IntegralDetailActivity.this,ShopBannerActivity.class);
//                        intent.putParcelableArrayListExtra(Constant.BANNER.ITEMBEAN,mBannerItemBean);
//                        intent.putExtra(Constant.BANNER.POSITION,position);
//                        startActivity(intent);
//                    }
//                });
            }
        };
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mUltraViewPager.getLayoutParams().height = (int) Math.round(metric.widthPixels / 2.42);
        mUltraViewPager.setAdapter(ultraViewPagerAdapter);
        mUltraViewPager.setAutoScroll(2000);
        mUltraViewPager.initIndicator();
        mUltraViewPager.getIndicator().setMargin(0,0,0,20);
        mUltraViewPager.getIndicator().setOrientation(UltraViewPager.Orientation.HORIZONTAL);
        mUltraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        mUltraViewPager.getIndicator().setFocusResId(0).setNormalResId(0);
        mUltraViewPager.getIndicator().setFocusColor(mContext.getResources().getColor(R.color.theme_bule)).setNormalColor(mContext.getResources().getColor(R.color.halfgraybg))
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mContext.getResources().getDisplayMetrics()));
        mUltraViewPager.getIndicator().build();
    }
}
