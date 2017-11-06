package com.yhkj.yymall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.xsnow.event.BusFactory;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.util.StatusBarUtil;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.util.CommonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vise.xsnow.net.mode.ApiCode.Response.ACCESS_TOKEN_ERROR;
import static com.vise.xsnow.net.mode.ApiCode.Response.ACCESS_TOKEN_EXPIRED;
import static com.vise.xsnow.net.mode.ApiCode.Response.ACCESS_TOKEN_FAILD;
import static com.yhkj.yymall.base.Constant.ACTIVITY_BUNDLE;
import static com.yhkj.yymall.base.Constant.TOOLBAR_TYPE;


/**
 * Created by Administrator on 2017/6/24.
 */

public abstract class BaseToolBarFragment extends SupportFragment {
    protected Context mContext;
    protected Resources mResources;
    protected LayoutInflater mInflater;
    protected ViewGroup mRootView;
    private boolean isOnResumeRegisterBus = false;
    private boolean isOnStartRegisterBus = false;
    private Toast mToast;
    private TextView vt_tv_right;

    @Bind(R.id.ftr_rl_toolbar)
    public RelativeLayout mRlToolbar;

    @Bind(R.id.vt_btn_left)
    public ImageView mImgBack;

    @Bind(R.id.vt_tv_title)
    public TextView mTvTitle;

    @Bind(R.id.vt_img_right)
    public ImageView mImgRight;

    @Bind(R.id.ftr_dead_statusbg)
    public View mDeadStatusView;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
        this.mResources = mContext.getResources();
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState){
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_toolbar_root, container, false);
        View mContentView = inflater.inflate(getLayoutID(), mRootView, false);
        ((ViewGroup) mRootView.findViewById(R.id.ftr_fl_container)).addView(mContentView);
        vt_tv_right = (TextView) mRootView.findViewById(R.id.vt_tv_right);
        ButterKnife.bind(this, mRootView);
        initBaseUi();
        initView(mRootView);
        bindEvent();
        initData();
        return mRootView;
    }
    protected View mRlNoNetWork;
    protected ProgressBar mProgressBar;
    protected ViewGroup mLlNoNetWorkEntity;

    private ImageView mImgNoData;
    private TextView mTvNoDataTip;
    private TextView mTvNoDataBtn;
    private void initBaseUi() {
        mRlNoNetWork = mRootView.findViewById(R.id.ftr_rl_nodata);
        mLlNoNetWorkEntity = (ViewGroup)mRootView.findViewById(R.id.ftr_ll_nonetwork);
        mProgressBar = (ProgressBar)mRootView.findViewById(R.id.fr_progressbar);
//        mTvReLoad = (TextView)mRootView.findViewById(R.id.vrr_tv_btn);
//        mRlNoNetWork.setVisibility(GONE);
        mImgNoData = (ImageView)mRootView.findViewById(R.id.vrr_img_nodata);
        mTvNoDataTip = (TextView) mRootView.findViewById(R.id.vrr_tv_tip);
        mTvNoDataBtn = (TextView) mRootView.findViewById(R.id.vrr_tv_btn);
        mLlNoNetWorkEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReLoadClickLisiten();
            }
        });
    }



    protected void onReLoadClickLisiten(){
        setLoadViewShow(VISIBLE);
    }

    @Override
    public void onResume(){
        super.onResume();
        if (isOnResumeRegisterBus){
            BusFactory.getBus().register(this);
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        if (isOnStartRegisterBus){
            BusFactory.getBus().register(this);
        }
    }

    @Override
    public void onPause(){
        super.onPause();
        if (isOnResumeRegisterBus){
            BusFactory.getBus().unregister(this);
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (isOnStartRegisterBus){
            BusFactory.getBus().unregister(this);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    protected boolean isOnResumeRegisterBus(){
        return isOnResumeRegisterBus;
    }

    protected BaseToolBarFragment setOnResumeRegisterBus(boolean onResumeRegisterBus) {
        isOnResumeRegisterBus = onResumeRegisterBus;
        return this;
    }

    protected boolean isOnStartRegisterBus(){
        return isOnStartRegisterBus;
    }

    protected BaseToolBarFragment setOnStartRegisterBus(boolean onStartRegisterBus){
        isOnStartRegisterBus = onStartRegisterBus;
        return this;
    }

    /**
     * 布局的LayoutID
     *
     * @return
     */
    abstract protected int getLayoutID();


    abstract protected void initData();

    @CallSuper
    protected void initView(View contentView){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            mDeadStatusView.setVisibility(GONE);
        }
        mDeadStatusView.getLayoutParams().height = CommonUtil.getStatusBarHeight(_mActivity);
        setToolBarColor(getResources().getColor(R.color.white));
        setStatusColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onSupportVisible(){
        super.onSupportVisible();
    }

    @CallSuper
    protected void bindEvent(){
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        mLlNoNetWorkEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReLoadClickLisiten();
            }
        });
    }
    protected void setNetWorkErrShow(int show){
        mProgressBar.setVisibility(GONE);
        mRlNoNetWork.setVisibility(show);
        mLlNoNetWorkEntity.setVisibility(show);
    }

    //默认此状态
    protected void setLoadViewShow(int show){
        mRlNoNetWork.setVisibility(show);
        mLlNoNetWorkEntity.setVisibility(GONE);
        mProgressBar.setVisibility(show);
    }

    protected void setNoDataView(@DrawableRes int mipmap, String tiptext, String btntext){
        mProgressBar.setVisibility(GONE);
        mRlNoNetWork.setVisibility(View.VISIBLE);
        mLlNoNetWorkEntity.setVisibility(View.VISIBLE);
        mImgNoData.setImageResource(mipmap);
        mTvNoDataBtn.setText(btntext);
        mTvNoDataTip.setText(tiptext);
    }
    protected void setNoDataBtnLisiten(View.OnClickListener onClickListener){
        mLlNoNetWorkEntity.setOnClickListener(null);
        mTvNoDataBtn.setOnClickListener(onClickListener);
    }

    protected void setNoDataView(@DrawableRes int mipmap,String tiptext){
        mProgressBar.setVisibility(GONE);
        mLlNoNetWorkEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mRlNoNetWork.setVisibility(View.VISIBLE);
        mLlNoNetWorkEntity.setVisibility(View.VISIBLE);
        mImgNoData.setImageResource(mipmap);
        mTvNoDataBtn.setVisibility(GONE);
        mTvNoDataTip.setText(tiptext);
    }
    protected void setImgBackLisiten(View.OnClickListener onClickListener){
        mImgBack.setOnClickListener(onClickListener);
    }

    protected void setImgBackResource(@DrawableRes int resId) {
        mImgBack.setImageResource(resId);
    }

    protected void setImgBackVisiable(int visiable) {
        mImgBack.setVisibility(visiable);
    }


    protected void setTvTitleLisiten(View.OnClickListener onClickListener) {
        mTvTitle.setOnClickListener(onClickListener);
    }

    protected void setTvTitleText(String text) {
        mTvTitle.setText(text);
    }

    protected void setTitleTvRightText(String text) {
        vt_tv_right.setText(text);
        vt_tv_right.setVisibility(View.VISIBLE);
    }

    protected void setTitleTvRightLisiten(View.OnClickListener onClickListener) {
        vt_tv_right.setOnClickListener(onClickListener);
    }

    protected void setTvTitleColor(@ColorInt int color) {
        mTvTitle.setBackgroundColor(color);
    }

    protected void setTvTitleVisiable(int visibility) {
        mTvTitle.setVisibility(visibility);
    }


    protected void setImgRightLisiten(View.OnClickListener onClickListener) {
        mImgRight.setOnClickListener(onClickListener);
    }

    protected void setImgRightResource(@DrawableRes int resId) {
        mImgRight.setImageResource(resId);
    }

    protected void setImgRightVisiable(int visiable) {
        mImgRight.setVisibility(visiable);
    }

    protected void setToolBarColor(@ColorInt int color) {
        mRlToolbar.setBackgroundColor(color);
    }

    protected void setToolBarVisiable(@ColorInt int visibility) {
        mRlToolbar.setVisibility(visibility);
    }

    protected void setStatusColor(@ColorInt int color) {
        mDeadStatusView.setBackgroundColor(color);
    }

    protected void setStatusVisiable(@ColorInt int visibility) {
        mDeadStatusView.setVisibility(visibility);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(_mActivity, cls);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, int type) {
        Intent intent = new Intent();
        intent.setClass(_mActivity, cls);
        intent.putExtra(TOOLBAR_TYPE.TYPE, type);
        startActivity(intent);
    }

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(_mActivity, cls);
        intent.putExtra(ACTIVITY_BUNDLE, bundle);
        startActivity(intent);
    }
}
