package com.yhkj.yymall.fragment;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.util.StatusBarUtil;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.activity.NewMessageActivity;
import com.yhkj.yymall.activity.ScanActivity;
import com.yhkj.yymall.activity.SearchActivity;
import com.yhkj.yymall.activity.ShopClassifyActivity;
import com.yhkj.yymall.activity.WebActivity;
import com.yhkj.yymall.adapter.NewHomeAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.bean.BannerBean;
import com.yhkj.yymall.bean.BaseConfig;
import com.yhkj.yymall.bean.HomeActBean;
import com.yhkj.yymall.bean.HomeRecommBean;
import com.yhkj.yymall.bean.OfflineBean;
import com.yhkj.yymall.bean.UnReadBean;
import com.yhkj.yymall.bean.ValidBean;
import com.yhkj.yymall.config.LocalActUltils;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.popwindows.FullScreenPopupView;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/6/19.
 */

public class HomeFragment extends BaseFragment implements YiYaHeaderView.OnRefreshLisiten {

    @Bind(R.id.fh_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.fh_refreshlayout)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.vt_tv_search)
    TextView mTvSerach;

    @Bind(R.id.vt_btn_right)
    ImageView mBtnRight;

    @Bind(R.id.fh_view_status)
    View mViewStatus;

    @Bind(R.id.fh_ll_topbar)
    LinearLayout mLlTopBar;

    @Bind(R.id.vt_btn_left)
    ImageView mImgLeft;

    @Bind(R.id.fh_img_offlineimg)
    GifImageView mImgOffline;

    private Animation mAnimToolBarIn;
    private Animation mAnimToolBarOut;

    private NewHomeAdapter mHomeAdapter;

    private Drawable[] mDrawable;

    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_home;
    }

    private int height = 640;// 滑动开始变色的高度
    private int overallXScroll = 0;
    private boolean mGraySet = false;

    @Override
    protected void initView(final View contentView) {
        super.initView(contentView);
        setNetWorkErrShow(GONE);
        mDrawable = mTvSerach.getCompoundDrawables();
        initRefreshLayout();
        mImgLeft.setImageResource(R.mipmap.ic_nor_message);
        mBtnRight.setImageResource(R.mipmap.ic_nor_classily);


        mImgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (YYApp.getInstance().getToken() != null ){
                    Intent intent = new Intent(mContext, NewMessageActivity.class);
                    startActivity(intent);
                } else {
                    showToast("请先登录");
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

//        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                //007cd1
//                overallXScroll = overallXScroll + dy;// 累加y值 解决滑动一半y值为0
//                if (overallXScroll <= 0) {   //设置标题的背景颜色
//                    mLlTopBar.setBackgroundColor(Color.argb((int) 0, 0, 124, 209));
//                } else if (overallXScroll > 0 && overallXScroll <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变
//                    float scale = (float) overallXScroll / height;
//                    float alpha = (255 * scale);
//                    if (overallXScroll >= height / 2) {
//                        if (!mGraySet) {
//                            mGraySet = true;
//                            mImgLeft.setImageResource(R.mipmap.ic_nor_gray_message);
//                            mBtnRight.setImageResource(R.mipmap.ic_nor_grayclassify);
//                        }
//                    } else {
//                        if (mGraySet) {
//                            mGraySet = false;
//                            mImgLeft.setImageResource(R.mipmap.ic_nor_message);
//                            mBtnRight.setImageResource(R.mipmap.ic_nor_classily);
//                        }
//                    }
//                    mLlTopBar.setBackgroundColor(Color.argb((int) alpha, 0, 124, 209));
//                } else {
//                    mLlTopBar.setBackgroundColor(Color.argb(255, 0, 124, 209));
//                }
//            }
//        });

//        RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
//        mRecycleView.setRecycledViewPool(viewPool);
//        viewPool.setMaxRecycledViews(5, 5);
        mRecycleView.setLayoutManager(new LinearLayoutManager(_mActivity));
//        mHomeAdapter = new HomeAdapter(_mActivity, layoutManager, false);
        mHomeAdapter = new NewHomeAdapter(_mActivity);
        mRecycleView.setAdapter(mHomeAdapter);

        getData(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void aotuRefresh(){
        if (mRefreshLayout!=null){
            mRefreshLayout.autoRefresh();
        }
    }

    private void getData(final boolean isPullDown) {
        YYMallApi.getBanner(_mActivity, new ApiCallback<BannerBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                ViseLog.e(e);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(BannerBean.DataBean dataBean) {
                mHomeAdapter.setBannerData(dataBean);
            }
        });

        YYMallApi.getHomeAct(_mActivity, new ApiCallback<HomeActBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                ViseLog.e(e);
                showToast(e.getMessage());
                if (isPullDown) mRefreshLayout.finishRefresh();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(HomeActBean.DataBean dataBean) {
                if (isPullDown) mRefreshLayout.finishRefresh();
                String hotString = dataBean.getKeyWords();
                if (!TextUtils.isEmpty(hotString)){
                    YYApp.getInstance().setmHotSearch(hotString);
                    mTvSerach.setHint(hotString);
                }
                mHomeAdapter.setHomeActData(dataBean);
            }
        });

        YYMallApi.getHomeRecomm(_mActivity, new ApiCallback<HomeRecommBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(HomeRecommBean.DataBean dataBean) {
                mHomeAdapter.setHomeRecommendData(dataBean);
            }
        });
    }


    private void initRefreshLayout() {
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(_mActivity).setOnRefreshLisiten(this));
//        mRefreshLayout.setEnableLoadmore(false);
        mRefreshLayout.setLoadmoreFinished(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(true);
            }
        });
//        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                refreshlayout.finishLoadmore(2000);
//            }
//        });
    }

    private int mToolbarStatus = 0;

    @Override
    public void onStartPull() {
//        if (mToolbarStatus == 0) {
//            mToolbarStatus = 1;
//            mLlTopBar.startAnimation(getAnimToolBarOut());
//        }
    }

    @Override
    public void onBackTop() {
//        if (mToolbarStatus == 1) {
//            mToolbarStatus = 0;
//            mLlTopBar.startAnimation(getAnimToolBarIn());
//        }
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mImgOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    startActivity(new Intent(_mActivity,LoginActivity.class));
                    showToast("请先登录");
                    return;
                }

                YYMallApi.getOfflineActIsValid(_mActivity,mOfflineBean.getId(),new YYMallApi.ApiResult<ValidBean.DataBean>(_mActivity){
                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                    }

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                    }

                    @Override
                    public void onNext(ValidBean.DataBean dataBean) {
                        super.onNext(dataBean);
                        if (dataBean.getIsValid() == 1) {
                            if (mAdPopupView != null && !mAdPopupView.isShowing()){
                                mAdPopupView.showPopupWindow();
                                mImgOffline.setVisibility(GONE);
                            }else{
                                new OfflineTask().execute("2");
                            }
//                            WebActivity.loadUrl(_mActivity,String.valueOf(mImgOffline.getTag()),"报名资料");
                        }else{
                            showToast("活动已失效");
                            mImgOffline.setVisibility(GONE);
                        }
                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                });

            }
        });
        mTvSerach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SearchActivity.class, Constant.TOOLBAR_TYPE.SEARCH_EDIT);
            }
        });
        mTvSerach.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mDrawable.length < 2 || mDrawable[2] == null) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > mTvSerach.getWidth() - mDrawable[2].getBounds().width()) {

                    startActivity(ScanActivity.class);
                    return true;
                }
                return false;
            }
        });
        mBtnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ShopClassifyActivity.class, Constant.TOOLBAR_TYPE.SEARCH_TV);
            }
        });
    }

    @Override
    protected void initData() {
        mImgLeft.setImageResource(R.mipmap.ic_nor_message);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mViewStatus.setVisibility(GONE);
        }
    }

    private boolean mLightStatus = false;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        //设置深色主题
        if (!mLightStatus) {
            mLightStatus = true;
            StatusBarUtil.StatusBarLightMode(_mActivity);
        }
        if (!TextUtils.isEmpty(YYApp.getInstance().getToken())){
            YYMallApi.getUnReadMesTag(_mActivity,new YYMallApi.ApiResult<UnReadBean.DataBean>(_mActivity) {
                @Override
                public void onStart() {

                }

                @Override
                public void onError(ApiException e) {
                    super.onError(e);
                    showToast(e.getMessage());
                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(UnReadBean.DataBean dataBean) {
                    if (dataBean.getStatus()==1){
                        mImgLeft.setImageResource(R.mipmap.ic_nor_unmessage);
                    }else{
                        mImgLeft.setImageResource(R.mipmap.ic_nor_message);
                    }
                }
            });
        }else{
            mImgLeft.setImageResource(R.mipmap.ic_nor_message);
        }

        getOfflineAct();
    }

    @Override
    public boolean onBackPressedSupport() {
        if (mAdPopupView!= null && mAdPopupView.isShowing()){
            mAdPopupView.dismissWithOutAnima();
            return true;
        }
        return super.onBackPressedSupport();
    }

    private OfflineBean.DataBean mOfflineBean;
    private void getOfflineAct(){
        YYMallApi.getOfflineAct(_mActivity, new ApiCallback<OfflineBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(final OfflineBean.DataBean dataBean) {
                mOfflineBean = dataBean;
                if (dataBean.getHasJoin() == 0){
                    //还没参加过
                    if (checkActArrive(LocalActUltils.ACT_OFFLINE)) {
//                        活动已经推送到
                        if (mImgOffline.getVisibility() != VISIBLE
                            && ( mAdPopupView == null || (mAdPopupView !=null && !mAdPopupView.isShowing()) ) ){
                            mImgOffline.setTag(dataBean.getFloatX().getLink());
                            new OfflineTask().execute("1");
                        }
                    }else{
                        mImgOffline.setTag(dataBean.getPop().getLink());
                        new OfflineTask().execute("2");
    //                //第一次 显示广告图
                        List<BaseConfig> baseConfigs = DbHelper.getInstance().baseConfigLongDBManager().loadAll();
                        if (baseConfigs!=null && baseConfigs.size() > 0){
                            BaseConfig baseConfig = baseConfigs.get(0);
                            baseConfig.setActBit(LocalActUltils.setActBit(baseConfig.getActBit(),LocalActUltils.ACT_OFFLINE));
                            baseConfig.setToken(YYApp.getInstance().getToken());
                            DbHelper.getInstance().baseConfigLongDBManager().deleteAll();
                            DbHelper.getInstance().baseConfigLongDBManager().insert(baseConfig);
                        }else{
                            BaseConfig baseConfig = new BaseConfig();
                            baseConfig.setActBit(LocalActUltils.setActBit(baseConfig.getActBit(),LocalActUltils.ACT_OFFLINE));
                            baseConfig.setToken(YYApp.getInstance().getToken());
                            DbHelper.getInstance().baseConfigLongDBManager().insert(baseConfig);
                        }
                    }
                }else{
                    mImgOffline.setVisibility(GONE);
                }

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
//                showToast(e.getMessage());
                mImgOffline.setVisibility(GONE);
            }
        });
    }

    /**
     * 检查活动是否到达
     * @param act
     */
    private boolean checkActArrive(int act){
        List<BaseConfig> baseConfigs = DbHelper.getInstance().baseConfigLongDBManager().loadAll();
        if(baseConfigs!=null && baseConfigs.size() > 0){
            BaseConfig baseConfig = baseConfigs.get(0);
            if(!TextUtils.isEmpty(baseConfig.getToken()) && baseConfig.getToken().equals(YYApp.getInstance().getToken())){
                int actBit = baseConfig.getActBit();
                return LocalActUltils.compareActBitVal(actBit,act);
            }
            return LocalActUltils.compareActBitVal(baseConfig.getActBit(),act);
        }else{
            BaseConfig baseConfig = new BaseConfig();
            baseConfig.setActBit(LocalActUltils.setActBit(0,LocalActUltils.ACT_OFFLINE));
            DbHelper.getInstance().baseConfigLongDBManager().insert(baseConfig);
        }
        return false;
    }

    private Animation getAnimToolBarOut() {
        if (mAnimToolBarOut == null) {
            mAnimToolBarOut = AnimationUtils.loadAnimation(_mActivity, R.anim.fade_out);
            mAnimToolBarOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLlTopBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        return mAnimToolBarOut;
    }

    private Animation getAnimToolBarIn() {
        if (mAnimToolBarIn == null) {
            mAnimToolBarIn = AnimationUtils.loadAnimation(_mActivity, R.anim.fade_in);
            mAnimToolBarIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mLlTopBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        return mAnimToolBarIn;
    }


    public class OfflineTask extends AsyncTask<String, Void, File> {

        private Boolean mSmallOrBig;
        public OfflineTask() {

        }

        @Override
        protected File doInBackground(String... params) {
            mSmallOrBig = params[0].equals("1");
            try {
                return Glide
                        .with(_mActivity)
                        .load(mSmallOrBig ? mOfflineBean.getFloatX().getImg() : mOfflineBean.getPop().getImg())
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get();
            } catch (Exception ex) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(File result) {
            if (result == null) {
                return;
            }
            mImgOffline.setVisibility(GONE);
            if (mSmallOrBig){
                boolean bGif = mOfflineBean.getFloatX().getImg().endsWith("gif");
                if (bGif){
                    try{
                        GifDrawable gifDrawable = new GifDrawable(result);
                        mImgOffline.setImageDrawable(gifDrawable);
                        mImgOffline.setVisibility(View.VISIBLE);
                        gifDrawable.setLoopCount(0);
                        gifDrawable.start();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    mImgOffline.setImageBitmap(BitmapFactory.decodeFile(result.getAbsolutePath()));
                    mImgOffline.setVisibility(View.VISIBLE);
                }

            }else{
                if ( (mAdPopupView ==null || !mAdPopupView.isShowing()) && mImgOffline.getVisibility()!= View.VISIBLE ){
                    mAdPopupView = new FullScreenPopupView(_mActivity,result,mOfflineBean.getFloatX().getLink()){
                        @Override
                        public void dismissWithOutAnima() {
//                            new OfflineTask().execute("1");
                            mImgOffline.setTag(mOfflineBean.getFloatX().getLink());
                            mImgOffline.setVisibility(VISIBLE);
                            super.dismissWithOutAnima();
                        }
                    };
                    mAdPopupView.setDismissWhenTouchOuside(false);
                    mAdPopupView.showPopupWindow();
                }
            }
        }
    }

    private FullScreenPopupView mAdPopupView;
//
}
