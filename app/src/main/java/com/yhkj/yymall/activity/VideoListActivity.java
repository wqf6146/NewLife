package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.MultiItemTypeAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.adapter.recycleview.wrapper.HeaderAndFooterWrapper;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.UltraBannerPagerAdapter;
import com.yhkj.yymall.bean.ShopDetailsBean;
import com.yhkj.yymall.bean.VideoDescBean;
import com.yhkj.yymall.bean.VideoListBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.ItemOffsetDecoration;
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.viewpager.UltraViewPager;

import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/12/15.
 */

public class VideoListActivity extends BaseToolBarActivity {

    @Bind(R.id.av_refreshview)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.av_recycleview)
    RecyclerView mRecycleView;

    CommonAdapter mCommonAdapter;

    @Bind(R.id.av_ultraviewpager)
    UltraViewPager mUltraViewPager;

    @Bind(R.id.av_rl_nodata)
    RelativeLayout mRLNoData;

    @Bind(R.id.av_ll_nonetwork)
    LinearLayout mLlNoNetWork;

    @Bind(R.id.av_progress)
    ProgressBar mProgress;

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
        setTitleWireVisiable(GONE);
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
        mLlNoNetWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReLoadClickLisiten();
            }
        });
    }
    private List mBannerData;

    @Override
    protected void onReLoadClickLisiten() {
        setLoadViewShow(VISIBLE);
        getData(null);
    }

    //默认此状态
    protected void setLoadViewShow(int show){
        mRLNoData.setVisibility(show);
        mLlNoNetWork.setVisibility(GONE);
        mProgress.setVisibility(show);
    }

    @Override
    protected void initData() {
        mBannerData = (List)getIntent().getParcelableArrayListExtra("banner");
        super.setNetWorkErrShow(GONE);
        initBanner();
    }


    @Override
    protected void onResume() {
        super.onResume();
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
                if(bRefresh !=null && bRefresh){
                    mRefreshLayout.finishRefresh();
                }

                if (dataBean == null || dataBean.getList().size() == 0){
                    setNoDataView(R.mipmap.ic_nor_novideo,"暂无直播视频");
                    return;
                }

                setNetWorkErrShow(GONE);

                if(mCommonAdapter == null){
                    initUi(dataBean);
                }else{
                    mCommonAdapter.setDatas(dataBean.getList());
                    mCommonAdapter.notifyDataSetChanged();
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

    @Bind(R.id.av_img_nodata)
    ImageView mImgNoData;

    @Bind(R.id.av_tv_tip)
    TextView mTvTip;

    @Bind(R.id.av_tv_btn)
    TextView mTvNoDataBtn;

    protected void setNoDataView(@DrawableRes int mipmap, String tiptext){
        mLlNoNetWorkEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mProgress.setVisibility(GONE);
        mRLNoData.setVisibility(View.VISIBLE);
        mLlNoNetWork.setVisibility(View.VISIBLE);
        mImgNoData.setImageResource(mipmap);
        mTvNoDataBtn.setVisibility(GONE);
        mTvTip.setText(tiptext);
    }

    protected void setNetWorkErrShow(int visiable){
        mRLNoData.setVisibility(visiable);
        mLlNoNetWork.setVisibility(visiable);
        mProgress.setVisibility(GONE);
    }

    private void initBanner(){
        UltraBannerPagerAdapter ultraViewPagerAdapter = new UltraBannerPagerAdapter<VideoDescBean.DataBean.BannerBean>(this,
                mBannerData,true){
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
//        UltraViewPager ultraViewPager = (UltraViewPager)LayoutInflater.from(this).inflate(R.layout.view_banner_pager,mRecycleView,false);
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

    private void initUi(final VideoListBean.DataBean dataBean) {

        mCommonAdapter = new CommonAdapter<VideoListBean.DataBean.ListBean>(this,R.layout.item_video,dataBean.getList()) {
            @Override
            protected void convert(ViewHolder holder, final VideoListBean.DataBean.ListBean bean, int position) {
                final ImageView img = holder.getView(R.id.iv_img);
                img.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ViewGroup.LayoutParams lp = img.getLayoutParams();
                        lp.height = (int) (img.getWidth()/1.77);
                    }
                });
                Glide.with(VideoListActivity.this).load(bean.getImg()).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.mipmap.ic_nor_srcpic).into(img);
                holder.setText(R.id.iv_tv_title,bean.getTitle());
                holder.setText(R.id.iv_tv_content,bean.getSchool_name());
            }
        };
        mCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener<VideoListBean.DataBean.ListBean>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, VideoListBean.DataBean.ListBean bean, int position) {
                Intent intent = new Intent(mContext,VideoPlayActivity.class);
                intent.putExtra("pos",position);
                intent.putExtra("token",dataBean.getToken());
                intent.putParcelableArrayListExtra("list",dataBean.getList());
                mContext.startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

//        mWrapperAdapter = new HeaderAndFooterWrapper(mCommonAdapter);
//        mWrapperAdapter.addHeaderView(ultraViewPager);
        mRecycleView.setAdapter(mCommonAdapter);
    }
}
