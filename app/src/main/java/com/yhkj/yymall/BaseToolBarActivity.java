package com.yhkj.yymall;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vise.log.ViseLog;
import com.vise.xsnow.event.BusFactory;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.util.StatusBarUtil;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.util.CommonUtil;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

import static android.os.Build.VERSION_CODES.KITKAT;
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

public abstract class BaseToolBarActivity extends SupportActivity {
    protected Context mContext;
    protected Class<?> mClassThis;
    private boolean isOnResumeRegisterBus = false;
    private boolean isOnStartRegisterBus = false;
    protected View mContentView, view_btx;
    private ViewGroup mRootView;
    protected ViewGroup mRlToolbar;
    protected ImageView mImgBack;
    protected ImageView mImgRight;
    private Toast mToast;
    private int mToolBarType = 0; //0-普通 1-searchTvToolbar 2-searchEditToolbar
    protected TextView mTvTitle;
    protected TextView mTvSearch;
    protected TextView mTvRight;
    protected EditText mEditSearch;
    View mDeadStatusView;

    protected RelativeLayout mRlNoNetwork;
    protected ViewGroup mLlNoNetWorkEntity;
    protected Drawable[] mRightDrawable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mToolBarType = getIntent().getIntExtra(TOOLBAR_TYPE.TYPE, TOOLBAR_TYPE.COMMON);
        mContext = this;
        mClassThis = this.getClass();
        AppManager.getInstance().addActivity(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        if (mToolBarType == TOOLBAR_TYPE.COMMON)
            mRootView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_toolbar_root, null);
        else if (mToolBarType == TOOLBAR_TYPE.SEARCH_TV)
            mRootView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_search_tv_toolbar_root, null);
        else
            mRootView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_search_edit_toolbar_root, null);

        if (mOriginDataStatus){
            super.setContentView(mRootView);
            initBaseUi();
            onGetOriginData();
        }else{
            mContentView = LayoutInflater.from(this).inflate(layoutResID, null);
            ((ViewGroup) mRootView.findViewById(R.id.ftr_fl_container)).addView(mContentView);
            super.setContentView(mRootView);
            ButterKnife.bind(this, mRootView);
            initBaseUi();
            initView();
            bindEvent();
            initData();
        }
    }

    protected boolean mOriginDataStatus = false;
    protected void setOpenOriginDataStatus(boolean status){
        mOriginDataStatus = status;
    }

    //请求第一次网络数据再设置用户的view
    protected void onGetOriginData(){

    }

    private ProgressBar mProgressBar;
    /**
     * 初始化基础UI 一般是不需要网络获取的
     */
    @CallSuper
    protected void initBaseUi(){
        view_btx = mRootView.findViewById(R.id.view_btx);
        mImgBack = (ImageView) findViewById(R.id.vt_btn_left);
        mRlToolbar = (ViewGroup) findViewById(R.id.ftr_rl_toolbar);
        mImgRight = (ImageView) findViewById(R.id.vt_img_right);
        mTvRight = (TextView) findViewById(R.id.vt_tv_right);
        mDeadStatusView = findViewById(R.id.ftr_dead_statusbg);
        mRlNoNetwork = (RelativeLayout)findViewById(R.id.ftr_rl_nodata);
        mLlNoNetWorkEntity = (ViewGroup)mRootView.findViewById(R.id.ftr_ll_nonetwork);
//        mRlNoNetwork.setVisibility(GONE);
        mImgNoData = (ImageView)mRootView.findViewById(R.id.vrr_img_nodata);
        mLlNoDataTab = (LinearLayout) mRootView.findViewById(R.id.atr_ll_nodatatab);
        mTvNoDataTip = (TextView) findViewById(R.id.vrr_tv_tip);
        mTvNoDataBtn = (TextView) findViewById(R.id.vrr_tv_btn);
        mProgressBar = (ProgressBar)findViewById(R.id.atr_progrssbar);
        if (Build.VERSION.SDK_INT >= KITKAT)
            mDeadStatusView.getLayoutParams().height = CommonUtil.getStatusBarHeight(this);
        if (mToolBarType == TOOLBAR_TYPE.COMMON) {
            mTvTitle = (TextView) findViewById(R.id.vt_tv_title);
        } else if (mToolBarType == TOOLBAR_TYPE.SEARCH_TV) {
            mTvSearch = (TextView) findViewById(R.id.vt_tv_search);
            mImgRight = (ImageView) findViewById(R.id.vt_btn_right);
            mRightDrawable = mTvSearch.getCompoundDrawables();
        } else {
            mEditSearch = (EditText) findViewById(R.id.vt_edit_search);
        }

        mLlNoNetWorkEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReLoadClickLisiten();
            }
        });

    }

    //获取数据成功显示内容
    protected void setUserContentView(@LayoutRes int layoutResID){
        mOriginDataStatus = false;
        mContentView = LayoutInflater.from(this).inflate(layoutResID, null);
        ((ViewGroup) mRootView.findViewById(R.id.ftr_fl_container)).addView(mContentView);
        ButterKnife.bind(this, mRootView);
        initView();
        bindEvent();
        initData();
    }

    @Override
    public void setContentView(View view) {
        if (mToolBarType == TOOLBAR_TYPE.COMMON)
            mRootView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_toolbar_root, null);
        else if (mToolBarType == TOOLBAR_TYPE.SEARCH_TV)
            mRootView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_search_tv_toolbar_root, null);
        else
            mRootView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_search_edit_toolbar_root, null);

        mContentView = view;
        ((ViewGroup) mRootView.findViewById(R.id.ftr_fl_container)).addView(mContentView);
        super.setContentView(mRootView);
        ButterKnife.bind(this, mRootView);
        initView();
        bindEvent();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isOnResumeRegisterBus) {
            BusFactory.getBus().register(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isOnStartRegisterBus) {
            BusFactory.getBus().register(this);
        }
    }


    private boolean mInitData = true;


    @Override
    protected void onPause() {
        super.onPause();
        closeKeyboard();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isOnStartRegisterBus) {
            BusFactory.getBus().unregister(this);
        }
        if (mProgressDialog!=null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
            mProgressBar = null;
        }
        AppManager.getInstance().removeActivity(this);
    }

    protected boolean isOnResumeRegisterBus() {
        return isOnResumeRegisterBus;
    }

    protected BaseToolBarActivity setOnResumeRegisterBus(boolean onResumeRegisterBus) {
        isOnResumeRegisterBus = onResumeRegisterBus;
        return this;
    }

    protected boolean isOnStartRegisterBus() {
        return isOnStartRegisterBus;
    }

    protected BaseToolBarActivity setOnStartRegisterBus(boolean onStartRegisterBus) {
        isOnStartRegisterBus = onStartRegisterBus;
        return this;
    }

    /**
     * 初始化子View
     */
    @CallSuper
    protected void initView() {

    }

    /**
     * 绑定事件
     */
    @CallSuper
    protected void bindEvent() {
        mLlNoNetWorkEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReLoadClickLisiten();
            }
        });
        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishActivity(mClassThis);
            }
        });
    }

    //无数据点击事件
    protected void onReLoadClickLisiten() {
        setLoadViewShow(VISIBLE);
    }

    @Override
    public void onBackPressedSupport() {
        AppManager.getInstance().finishActivity(this);
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    protected void setNetWorkErrShow(int show){
        mProgressBar.setVisibility(GONE);
        mRlNoNetwork.setVisibility(show);
        mLlNoNetWorkEntity.setVisibility(show);
    }
    //默认此状态
    protected void setLoadViewShow(int show){
        mRlNoNetwork.setVisibility(show);
        mLlNoNetWorkEntity.setVisibility(GONE);
        mProgressBar.setVisibility(show);
    }

    /**
     * 设置无数据View
     */
    private ImageView mImgNoData;
    private TextView mTvNoDataTip;
    private TextView mTvNoDataBtn;
    private LinearLayout mLlNoDataTab;
    protected void setNoDataView(@DrawableRes int mipmap,String tiptext,String btntext){
        mProgressBar.setVisibility(GONE);
        mRlNoNetwork.setVisibility(View.VISIBLE);
        mLlNoNetWorkEntity.setVisibility(View.VISIBLE);
        mImgNoData.setImageResource(mipmap);
        mTvNoDataBtn.setText(btntext);
        mTvNoDataTip.setText(tiptext);
    }

    protected void setProgressBarShow(int show){
        mRlNoNetwork.setVisibility(show);
        mProgressBar.setVisibility(show);
    }

    protected void replaceCustomView(View view){
        mRootView.findViewById(R.id.ftr_fl_container).setVisibility(GONE);
        ((ViewGroup) mRootView.findViewById(R.id.ftr_fl_customview)).addView(view);
        mRootView.findViewById(R.id.ftr_fl_customview).setVisibility(VISIBLE);
    }

    protected void setNoDataView(@DrawableRes int mipmap,String tiptext){
        mLlNoNetWorkEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mProgressBar.setVisibility(GONE);
        mRlNoNetwork.setVisibility(View.VISIBLE);
        mLlNoNetWorkEntity.setVisibility(View.VISIBLE);
        mImgNoData.setImageResource(mipmap);
        mTvNoDataBtn.setVisibility(GONE);
        mTvNoDataTip.setText(tiptext);
    }
    protected void setNoDataView(@DrawableRes int mipmap,String tiptext,View view){
        mLlNoNetWorkEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mProgressBar.setVisibility(GONE);
        mRlNoNetwork.setVisibility(View.VISIBLE);
        mLlNoNetWorkEntity.setVisibility(View.VISIBLE);
        mImgNoData.setImageResource(mipmap);
        mTvNoDataBtn.setVisibility(GONE);
        mTvNoDataTip.setText(tiptext);
        mLlNoDataTab.addView(view,0);
    }

    protected void setNoDataBtnLisiten(View.OnClickListener onClickListener){
        mLlNoNetWorkEntity.setOnClickListener(null);
        mTvNoDataBtn.setOnClickListener(onClickListener);
    }

    protected void setImgBackLisiten(View.OnClickListener onClickListener) {
        if (isNull(mImgBack)) return;
        mImgBack.setOnClickListener(onClickListener);
    }

    protected void setImgBackResource(@DrawableRes int resId) {
        if (isNull(mImgBack)) return;
        mImgBack.setVisibility(View.VISIBLE);
        mImgBack.setImageResource(resId);
    }

    protected void setImgBackVisiable(int visiable) {
        if (isNull(mImgBack)) return;
        mImgBack.setVisibility(visiable);
    }

    protected void setImgRightLayoutParam(RelativeLayout.LayoutParams layoutParam){
        mImgRight.setLayoutParams(layoutParam);
    }

    protected void setTitleWireVisiable(int visiable) {
        if (isNull(view_btx)) return;
        view_btx.setVisibility(visiable);
    }


    protected void setTvTitleLisiten(View.OnClickListener onClickListener) {
        if (isNull(mTvTitle)) return;
        mTvTitle.setOnClickListener(onClickListener);
    }

    protected void setTvTitleText(String text) {
        if (isNull(mTvTitle)) return;
        mTvTitle.setText(text);
    }

    protected void setTvTitleColor(@ColorInt int color) {
        if (isNull(mTvTitle)) return;
        mTvTitle.setTextColor(color);
    }

    protected void setTvTitleVisiable(int visibility) {
        if (isNull(mTvTitle)) return;
        mTvTitle.setVisibility(visibility);
    }

    protected void setStatusColor(@ColorInt int color) {
        mDeadStatusView.setBackgroundColor(color);
    }
    protected void setStatusVisiable(int visibility) {
        if (mDeadStatusView!=null)
            mDeadStatusView.setVisibility(visibility);
    }
    protected void setImgRightLisiten(View.OnClickListener onClickListener) {
        if (isNull(mImgRight)) return;
        mImgRight.setOnClickListener(onClickListener);
    }

    protected void setImgRightResource(@DrawableRes int resId) {
        if (isNull(mImgRight)) return;
        mImgRight.setVisibility(View.VISIBLE);
        mImgRight.setImageResource(resId);
    }

    protected void setTvRightText(String text){
        if (isNull(mTvRight)) return;
        mTvRight.setVisibility(View.VISIBLE);
        mTvRight.setText(text);
    }

    protected void setTvRightTextColor(@ColorInt int color){
        if (isNull(mTvRight)) return;
        mTvRight.setTextColor(color);
    }


    protected void setTvRightLisiten(View.OnClickListener onClickListener) {
        if (isNull(mTvRight)) return;
        mTvRight.setOnClickListener(onClickListener);
    }

    protected void setImgRightVisiable(int visiable) {
        if (isNull(mImgRight)) return;
        mImgRight.setVisibility(visiable);
//        if (isNull(mTvRight)) return;
//        mTvRight.setVisibility(View.INVISIBLE);
    }
    protected void setTvRightVisiable(int visiable){
        if (isNull(mTvRight)) return;
        mTvRight.setVisibility(visiable);
//        if (isNull(mTvRight)) return;
//        mTvRight.setVisibility(visiable);
    }

    protected void setToolbarVisiable(boolean visiable){
        if (isNull(mRlToolbar)) return;
        mRlToolbar.setVisibility(visiable ? VISIBLE : GONE);
    }

    protected void setToolBarColor(@ColorInt int color) {
        if (isNull(mRlToolbar)) return;
        mRlToolbar.setBackgroundColor(color);
        if (isNull(mDeadStatusView)) return;
        mDeadStatusView.setBackgroundColor(color);
    }

    /**
     * 跳转activity
     *
     * @param cls
     */
    protected void startActivity(Class<?> cls, int type) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        intent.putExtra(TOOLBAR_TYPE.TYPE, type);
        startActivity(intent);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }

    protected boolean isNull(Object object) {
        return object == null;
    }

    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }
    protected void startActivity(Class<?> cls, Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(this,cls);
        intent.putExtra(ACTIVITY_BUNDLE,bundle);
        startActivity(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mInitData){
            mInitData = false;
            onActivityLoadFinish();
        }
    }

    protected void onActivityLoadFinish(){}
    private ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(BaseToolBarActivity.this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
//                    progressShow = false;
                }
            });
        }
        return mProgressDialog;
    }
    private ProgressDialog mProgressDialog;
    protected void showProgressDialog(String txt){
        if (mProgressDialog == null){
            mProgressDialog = getProgressDialog();
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(txt);
            mProgressDialog.show();
        }
    }
    protected void hideProgressDialog(){
        if (mProgressDialog!=null)
            mProgressDialog.dismiss();
    }

    /**
     * 关闭软键盘
     */

    private void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
