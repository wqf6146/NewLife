package com.yhkj.yymall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.xsnow.event.BusFactory;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.util.StatusBarUtil;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.base.DbHelper;

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
 * Created by Administrator on 2017/6/19.
 */

public abstract class BaseFragment extends SupportFragment {
    protected Context mContext;
    protected Resources mResources;
    protected LayoutInflater mInflater;
    protected View mRootView;
    private boolean isOnResumeRegisterBus = false;
    private boolean isOnStartRegisterBus = false;

    protected View mRlNoNetWork;
    protected ProgressBar mProgressBar;
//    protected TextView mTvReLoad;
    protected ViewGroup mLlNoNetWorkEntity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mContext = activity;
        this.mResources = mContext.getResources();
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_root,container,false);
        ((ViewGroup)mRootView.findViewById(R.id.ftr_fl_container)).addView(inflater.inflate(getLayoutID(), container, false));
        ButterKnife.bind(this,mRootView);
        initBaseUi();
        initView(mRootView);
        bindEvent();
        initData();
        return mRootView;
    }

    private void initBaseUi() {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onResume() {
        super.onResume();
        if (isOnResumeRegisterBus) {
            BusFactory.getBus().register(this);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isOnStartRegisterBus) {
            BusFactory.getBus().register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (isOnResumeRegisterBus) {
//            BusFactory.getBus().unregister(this);
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
//
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (isOnStartRegisterBus) {
            BusFactory.getBus().unregister(this);
        }
    }

    protected boolean isOnResumeRegisterBus() {
        return isOnResumeRegisterBus;
    }

    protected BaseFragment setOnResumeRegisterBus(boolean onResumeRegisterBus) {
        isOnResumeRegisterBus = onResumeRegisterBus;
        return this;
    }

    protected boolean isOnStartRegisterBus() {
        return isOnStartRegisterBus;
    }

    protected BaseFragment setOnStartRegisterBus(boolean onStartRegisterBus) {
        isOnStartRegisterBus = onStartRegisterBus;
        return this;
    }

    /**
     * 布局的LayoutID
     *
     * @return
     */
    protected abstract int getLayoutID();

    /**
     * 初始化子View
     */
    @CallSuper
    protected void initView(View contentView){
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

    /**
     * 绑定事件
     */
    @CallSuper
    protected void bindEvent(){
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

    /**
     * 初始化数据
     */
    protected abstract void initData();

    private ImageView mImgNoData;
    private TextView mTvNoDataTip;
    private TextView mTvNoDataBtn;

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

    protected void startActivity(Class<?> cls){
        Intent intent = new Intent();
        intent.setClass(_mActivity,cls);
        startActivity(intent);
    }
    protected void startActivity(Class<?> cls, Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(_mActivity,cls);
        intent.putExtra(ACTIVITY_BUNDLE,bundle);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls, int  type){
        Intent intent = new Intent();
        intent.setClass(_mActivity,cls);
        intent.putExtra(TOOLBAR_TYPE.TYPE,type);
        startActivity(intent);
    }

    private Toast mToast;
    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(_mActivity, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
}
