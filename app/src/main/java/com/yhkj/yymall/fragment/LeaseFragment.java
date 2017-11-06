package com.yhkj.yymall.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.log.ViseLog;
import com.vise.xsnow.event.EventSubscribe;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.util.luban.Luban;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.activity.MessageActivity;
import com.yhkj.yymall.activity.NewMessageActivity;
import com.yhkj.yymall.activity.ScanActivity;
import com.yhkj.yymall.activity.SearchActivity;
import com.yhkj.yymall.activity.ShopEvaluateActivity;
import com.yhkj.yymall.adapter.LeaseAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.LeaseClassifyBean;
import com.yhkj.yymall.bean.LeaseHotBean;
import com.yhkj.yymall.bean.ShopSelectBean;
import com.yhkj.yymall.bean.UnReadBean;
import com.yhkj.yymall.event.LeaseSortEvent;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.YiYaHeaderView;


import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/6/19.
 */

public class LeaseFragment extends BaseFragment {
    @Bind(R.id.fy_toolbar)
    LinearLayout mLlToolbar;

    @Bind(R.id.vt_btn_left)
    ImageView mImgLeft;

    @Bind(R.id.vt_tv_search)
    TextView mTvSearch;

    @Bind(R.id.fl_view_status)
    View mViewStatus;

    @Bind(R.id.vt_btn_right)
    ImageView mImgRight;

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;

    private LeaseAdapter mAdapter;

    private String mOrder = null,mBy = null,mPage = "1",mLimit = "10",mBrand = null,mAttr = null;

    private Drawable[] mDrawable;

    public static LeaseFragment getInstance() {
        LeaseFragment fragment = new LeaseFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        setOnResumeRegisterBus(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_lease;
    }

    private int mToolBarHeight = 0;
    private int mCondiTabHeight = 0;
    LinearLayout.LayoutParams mToolBarParam;
    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        setNetWorkErrShow(GONE);
        mDrawable = mTvSearch.getCompoundDrawables();
        initRefreshLayout();
        mLlToolbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mToolBarHeight == 0)
                    mToolBarHeight = mLlToolbar.getHeight();
                mToolBarParam = (LinearLayout.LayoutParams)mLlToolbar.getLayoutParams();
            }
        });
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getLeaseSelectListData(false);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mImgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int status = mAdapter.getStatusSpanc() == Constant.SHOPITEM_ORI.HOR ? Constant.SHOPITEM_ORI.VER :Constant.SHOPITEM_ORI.HOR;
                if (status == Constant.SHOPITEM_ORI.HOR){
                    mCondiTabHeight += CommonUtil.dip2px(_mActivity,100);
                    mImgRight.setImageResource(R.mipmap.ic_nor_whitelistopen);
                }else{
                    mCondiTabHeight -= CommonUtil.dip2px(_mActivity,100);
                    mImgRight.setImageResource(R.mipmap.ic_nor_whitelistclose);
                }
                mAdapter.changeShowStatus(status);
            }
        });
        mTvSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SearchActivity.class, Constant.TOOLBAR_TYPE.SEARCH_EDIT);
            }
        });
        mTvSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mDrawable.length < 2 || mDrawable[2] == null) {
                    return false;
                }
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }
                if (event.getX() > mTvSearch.getWidth() - mDrawable[2].getBounds().width()) {

                    startActivity(ScanActivity.class);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!TextUtils.isEmpty(YYApp.getInstance().getToken())){
            YYMallApi.getUnReadMesTag(_mActivity, new YYMallApi.ApiResult<UnReadBean.DataBean>(_mActivity) {
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
        }
    }

    @Override
    protected void initData() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mViewStatus.setVisibility(GONE);
        }
        mLlToolbar.setBackgroundColor(getResources().getColor(R.color.theme_bule));
        mImgLeft.setImageResource(R.mipmap.ic_nor_message);
        mImgRight.setImageResource(R.mipmap.ic_nor_whitelistclose);
        initListData();
        getLeaseAllData(null);
        mImgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                    Intent intent = new Intent(mContext, NewMessageActivity.class);
                    startActivity(intent);
                } else {
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                }
            }
        });
//      mRefreshLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//          @Override
//          public void onGlobalLayout() {
//              mRefreshLayout.getRefreshFooter().setPrimaryColors(R.color.theme_bule1);
//          }
//      });
    }

    private int mDisY;
    private void initListData() {
        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(_mActivity);
        mRecycleView.setLayoutManager(layoutManager);
        final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
        mRecycleView.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 10);

        mAdapter = new LeaseAdapter(_mActivity,layoutManager);
        mRecycleView.setAdapter(mAdapter);

        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mDisY+=dy;
                if (mCondiTabHeight == 0){
                    mCondiTabHeight = mAdapter.getStickyShopTabLayoutHelp().getLayoutRegion().bottom;
                }else{
                    if (mToolBarStatus == 0  && dy > 0 && mDisY > mCondiTabHeight) {
//                        mAnimBooleanList.add(true);
//                        debounceToolBarAnim();
                        getShopToolbarAnimOut();
                    }else if (mToolBarStatus == 1 && dy < 0) {
//                        mAnimBooleanList.add(false);
//                        debounceToolBarAnim();
                        getShopToolbarAnimIn();
                    }
                }
            }
        });
    }

//    List<Boolean> mAnimBooleanList = new ArrayList<>();
//    private void debounceToolBarAnim(){
//
//        Observable.from(mAnimBooleanList)
//                .debounce(1, TimeUnit.SECONDS)
//                .subscribe(new Action1<Boolean>() {
//                    @Override
//                    public void call(Boolean outOrin) {
//                        if (outOrin)
//                            getShopToolbarAnimOut();
//                        else
//                            getShopToolbarAnimIn();
//                        Log.e("info",new Gson().toJson(mAnimBooleanList));
//                    }
//                });
//    }

    private int mToolBarStatus = 0; //0-展开 1-隐藏
    private ValueAnimator mAnimOut,mAnimIn;
    private void getShopToolbarAnimOut(){
        if (mAnimOut == null){
            mAnimOut = ValueAnimator.ofInt(0,mToolBarHeight);
            mAnimOut.setDuration(300);
            mAnimOut.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mToolBarParam.height  = mToolBarHeight - (Integer) animation.getAnimatedValue();
                    mLlToolbar.setLayoutParams(mToolBarParam);
                }
            });
            mAnimOut.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mToolBarStatus = 1;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mAnimOut.start();
        }else if (!mAnimOut.isRunning()){
            mAnimOut.start();
        }
    }
    private void getShopToolbarAnimIn(){
        if (mAnimIn == null){
            mAnimIn = ValueAnimator.ofInt(0,mToolBarHeight);
            mAnimIn.setDuration(300);
            mAnimIn.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mToolBarParam.height = (Integer) animation.getAnimatedValue();
                    mLlToolbar.setLayoutParams(mToolBarParam);
                }
            });
            mAnimIn.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mToolBarStatus = 0;
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            mAnimIn.start();
        }else if (!mAnimIn.isRunning()){
            mAnimIn.start();
        }
    }

    private void initRefreshLayout() {
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(_mActivity));
        mRefreshLayout.setEnableLoadmore(true);
        mRefreshLayout.setEnableOverScrollBounce(false);

        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mDisY = 0;
                mPage = "1";
                mRefreshLayout.setLoadmoreFinished(false);
                getLeaseAllData(refreshlayout);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {

                final String page = String.valueOf(Integer.valueOf(mPage) + 1);
                YYMallApi.getLeaseSelectList(_mActivity, mOrder, mBy, page, mLimit, mBrand, mAttr,false, new ApiCallback<ShopSelectBean.DataBean>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        refreshlayout.finishLoadmore();
                        ViseLog.e(e);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(ShopSelectBean.DataBean dataBean) {
                        if (dataBean.getList() !=null && dataBean.getList().size() > 0){
                            refreshlayout.finishLoadmore();
                            mPage = page;
                            mAdapter.addLeaseSelList(dataBean);
                        }else{
                            refreshlayout.finishLoadmore();
                            refreshlayout.setLoadmoreFinished(true);
                        }
                    }
                });
            }
        });
    }


    HashMap mAttrHashMap = new LinkedHashMap();
    HashMap mAttrValueHashMap = new LinkedHashMap();
    @EventSubscribe
    public void refreshLeaseData(final LeaseSortEvent leaseSelectEvent){
        mRefreshLayout.setLoadmoreFinished(false);
        mPage = "1";
        if (leaseSelectEvent.type == Constant.TYPE_SELECT.BRAND){
            //品牌
            if (leaseSelectEvent.brand == null || leaseSelectEvent.brand.equals("全部"))
                mBrand = null;
            else
                mBrand = leaseSelectEvent.brand;
        }else if (leaseSelectEvent.type == Constant.TYPE_SELECT.ATTR){
            Gson gson = new Gson();
            if (leaseSelectEvent.getAttrValue().equals("全部")) {
                Iterator iterator = mAttrValueHashMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    if (leaseSelectEvent.getAttrKey().equals(key)) {
                        iterator.remove();
                        mAttrValueHashMap.remove(key);
                    }
                }
            }else{
                mAttrValueHashMap.put(leaseSelectEvent.getAttrKey(), leaseSelectEvent.getAttrValue());
            }
            mAttrHashMap.put("attrs",mAttrValueHashMap);
            mAttr = gson.toJson(mAttrHashMap);
        }else{
            mOrder = leaseSelectEvent.order;
            mBy = leaseSelectEvent.by;
            mLimit = String.valueOf(leaseSelectEvent.limit);
        }
        YYMallApi.getLeaseSelectList(_mActivity, mOrder, mBy, mPage, mLimit, mBrand, mAttr,true, new ApiCallback<ShopSelectBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                if (mToolBarStatus == 1)
                    getShopToolbarAnimIn();
                setNetWorkErrShow(VISIBLE);
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ShopSelectBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                mAdapter.setLeaseSelList(dataBean);
            }
        });
    }

    private void getLeaseAllData(final RefreshLayout xRefreshView){
        YYMallApi.getHotLease(_mActivity, new ApiCallback<LeaseHotBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                if (xRefreshView!=null) xRefreshView.finishRefresh();
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(LeaseHotBean.DataBean dataBean) {
                if (xRefreshView!=null) xRefreshView.finishRefresh();
                mAdapter.setLeaseHotInfo(dataBean.getHotlist());
            }
        });
        getLeaseClassify();
        getLeaseSelectListData(false);

    }

    private void getLeaseClassify() {
        YYMallApi.getLeaseClassify(_mActivity, false, new ApiCallback<LeaseClassifyBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(LeaseClassifyBean.DataBean dataBean) {
                mAdapter.setLeaseTabInfoBean(dataBean);
            }
        });
    }

    private void getLeaseSelectListData(boolean bLoadView){
        YYMallApi.getLeaseSelectList(_mActivity, mOrder, mBy, mPage, mLimit, mBrand, mAttr, bLoadView,new ApiCallback<ShopSelectBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
               if (mToolBarStatus == 1)
                    getShopToolbarAnimIn();
                setNetWorkErrShow(VISIBLE);
                ViseLog.e(e);
                showToast(e.getMessage());
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ShopSelectBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                mAdapter.setLeaseSelList(dataBean);
            }
        });
    }
}
