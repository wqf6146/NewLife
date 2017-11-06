package com.yhkj.yymall;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.CheckResult;
import android.support.annotation.ColorInt;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.trello.rxlifecycle.LifecycleProvider;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.vise.xsnow.event.BusFactory;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.activity.LoginActivity;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.util.CommonUtil;

import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import static android.view.View.GONE;
import static com.vise.xsnow.net.mode.ApiCode.Response.ACCESS_TOKEN_ERROR;
import static com.vise.xsnow.net.mode.ApiCode.Response.ACCESS_TOKEN_EXPIRED;
import static com.vise.xsnow.net.mode.ApiCode.Response.ACCESS_TOKEN_FAILD;
import static com.yhkj.yymall.base.Constant.ACTIVITY_BUNDLE;

/**
 * Created by Administrator on 2017/6/19.
 */

public abstract class BaseActivity extends me.yokeyword.fragmentation.SupportActivity implements LifecycleProvider<ActivityEvent> {

    protected Context mContext;
    private boolean isOnResumeRegisterBus = false;
    private boolean isOnStartRegisterBus = false;


    private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();


    @Override
    @NonNull
    @CheckResult
    public final Observable<ActivityEvent> lifecycle() {
        return lifecycleSubject.asObservable();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull ActivityEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(ActivityEvent.CREATE);
        mContext = this;
        AppManager.getInstance().addActivity(this);
        if (isOnResumeRegisterBus) {
            BusFactory.getBus().register(this);
        }

        /**
         * 设置透明主题
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            StatusBarUtil.transparencyBar(this);
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }

    private ViewGroup mRootView;
    private View mContentView;
    private boolean mOriginDataStatus = false;
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        mRootView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.activity_root, null);
        if (mOriginDataStatus){
            super.setContentView(mRootView);
            initBaseUi();
//            onGetOriginData();
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

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        ButterKnife.bind(this);
        initView();
        bindEvent();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        lifecycleSubject.onNext(ActivityEvent.RESUME);
    }

    @CallSuper
    @Override
    protected void onStart() {
        super.onStart();
        lifecycleSubject.onNext(ActivityEvent.START);
        if (isOnStartRegisterBus) {
            BusFactory.getBus().register(this);
        }
    }

    @CallSuper
    @Override
    protected void onPause() {
        super.onPause();
        lifecycleSubject.onNext(ActivityEvent.PAUSE);
    }

    @CallSuper
    @Override
    protected void onStop() {
        lifecycleSubject.onNext(ActivityEvent.STOP);
        super.onStop();
    }

    @CallSuper
    @Override
    protected void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(ActivityEvent.DESTROY);
        if (isOnStartRegisterBus) {
            BusFactory.getBus().unregister(this);
        }
        AppManager.getInstance().removeActivity(this);
    }

    protected boolean isOnResumeRegisterBus() {
        return isOnResumeRegisterBus;
    }

    protected BaseActivity setOnResumeRegisterBus(boolean onResumeRegisterBus) {
        isOnResumeRegisterBus = onResumeRegisterBus;
        return this;
    }

    protected boolean isOnStartRegisterBus() {
        return isOnStartRegisterBus;
    }

    protected BaseActivity setOnStartRegisterBus(boolean onStartRegisterBus) {
        isOnStartRegisterBus = onStartRegisterBus;
        return this;
    }

    /**
     * 初始化子View
     */
    protected abstract void initView();

    /**
     * 绑定事件
     */
    protected abstract void bindEvent();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化基础UI 一般是不需要网络获取的
     */
    private View mViewLine;
    private View mDeadStatusView;
    protected RelativeLayout mRlNoNetwork;
    protected ViewGroup mLlNoNetWorkEntity;
    @CallSuper
    protected void initBaseUi(){
        mViewLine = mRootView.findViewById(R.id.view_btx);
        mDeadStatusView = findViewById(R.id.ftr_dead_statusbg);
        mRlNoNetwork = (RelativeLayout)findViewById(R.id.ftr_rl_nodata);
        mLlNoNetWorkEntity = (ViewGroup)mRootView.findViewById(R.id.ftr_ll_nonetwork);
        mRlNoNetwork.setVisibility(GONE);

        mDeadStatusView.getLayoutParams().height = CommonUtil.getStatusBarHeight(this);

        mLlNoNetWorkEntity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReLoadClickLisiten();
            }
        });

    }
    //重新加载
    protected void onReLoadClickLisiten() {}

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

    //设置无网络View
    protected void setNetWorkErrShow(int show){
        mRlNoNetwork.setVisibility(show);
        mLlNoNetWorkEntity.setVisibility(show);
    }


    protected void startActivity(Class<?> cls, Bundle bundle){
        Intent intent = new Intent();
        intent.setClass(this,cls);
        intent.putExtra(ACTIVITY_BUNDLE,bundle);
        startActivity(intent);
    }

    /**
     * 跳转activity
     *
     * @param cls
     */

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        startActivity(intent);
    }

    protected void setStatusViewVisiable(boolean visiable){
        if (mDeadStatusView == null)return;
        mDeadStatusView.setVisibility(visiable ? View.VISIBLE : GONE);
    }

    protected boolean isNull(Object object) {
        return object == null;
    }

    Toast mToast;
    public void showToast(String text) {
        if (mToast == null) {
            mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }


    private boolean mInitData = true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && mInitData){
            mInitData = false;
            onActivityLoadFinish();
        }
    }

    protected void onActivityLoadFinish(){}

    protected void setStatusColor(@ColorInt int color) {
        mDeadStatusView.setBackgroundColor(color);
    }
    /**
     * 关闭软键盘
     */

    protected void closeKeyboard() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
