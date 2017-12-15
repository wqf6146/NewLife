package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.adapter.recycleview.wrapper.HeaderAndFooterWrapper;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.UltraBannerPagerAdapter;
import com.yhkj.yymall.bean.VideoDescBean;
import com.yhkj.yymall.bean.VideoListBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.ItemOffsetDecoration;
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.viewpager.UltraViewPager;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/12/15.
 */

public class VideoListActivity extends BaseToolBarActivity {

    @Bind(R.id.av_refreshview)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.av_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.av_tv_nextstep)
    TextView mTvNextStep;

    HeaderAndFooterWrapper mWrapperAdapter;
    CommonAdapter mCommonAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videolist);
    }

    @Override
    protected void initView() {
        super.initView();
        setTvTitleText("视频监控列表");
        setToolBarColor(getResources().getColor(R.color.theme_bule));

        mRecycleView.setLayoutManager(new GridLayoutManager(this,2));
        mRecycleView.addItemDecoration(new ItemOffsetDecoration(CommonUtil.dip2px(this,1)));
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(this));
        mRefreshLayout.setLoadmoreFinished(true);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(true);
            }
        });
    }

    private List mBannerData;
    @Override
    protected void initData() {
        mBannerData = (List)getIntent().getParcelableArrayListExtra("banner");
        getData(null);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null);
    }

    private void getData(final Boolean bRefresh) {
        YYMallApi.getVideoList(this, new ApiCallback<VideoListBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(VideoListBean.DataBean dataBean) {
                setNetWorkErrShow(View.GONE);

                if (bRefresh !=null && bRefresh){
                    mRefreshLayout.finishRefresh();
                }

                if (mCommonAdapter == null)
                    initUi(mBannerData,dataBean);
                else {
                    mCommonAdapter.setDatas(dataBean.getList());
                    mWrapperAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
            }
        });
    }

    private void initUi(final List<VideoDescBean.DataBean.BannerBean> bannerData, VideoListBean.DataBean dataBean) {
        UltraBannerPagerAdapter ultraViewPagerAdapter = new UltraBannerPagerAdapter<VideoDescBean.DataBean.BannerBean>(this,
                bannerData,true){
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
        UltraViewPager ultraViewPager = (UltraViewPager)LayoutInflater.from(this).inflate(R.layout.view_banner_pager,mRecycleView,false);
        ultraViewPager.getLayoutParams().height = (int) Math.round(metric.widthPixels / 2.42);
        ultraViewPager.setAdapter(ultraViewPagerAdapter);
        ultraViewPager.setAutoScroll(2000);
        ultraViewPager.initIndicator();
        ultraViewPager.getIndicator().setMargin(0,0,0,20);
        ultraViewPager.getIndicator().setOrientation(UltraViewPager.Orientation.HORIZONTAL);
        ultraViewPager.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        ultraViewPager.getIndicator().setFocusResId(0).setNormalResId(0);
        ultraViewPager.getIndicator().setFocusColor(mContext.getResources().getColor(R.color.theme_bule)).setNormalColor(mContext.getResources().getColor(R.color.halfgraybg))
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, mContext.getResources().getDisplayMetrics()));
        ultraViewPager.getIndicator().build();

        mCommonAdapter = new CommonAdapter<VideoListBean.DataBean.ListBean>(this,R.layout.item_video,dataBean.getList()) {
            @Override
            protected void convert(ViewHolder holder, VideoListBean.DataBean.ListBean bean, int position) {
                Glide.with(VideoListActivity.this).load(bean.getImg()).placeholder(R.mipmap.ic_nor_srcpic).into((ImageView)holder.getView(R.id.iv_img));
                holder.setText(R.id.iv_tv_title,bean.getTitle());
                holder.setText(R.id.iv_tv_content,bean.getSchool_name());
            }
        };

        mWrapperAdapter = new HeaderAndFooterWrapper(mCommonAdapter);
        mWrapperAdapter.addHeaderView(ultraViewPager);
        mRecycleView.setAdapter(mWrapperAdapter);
    }
}
