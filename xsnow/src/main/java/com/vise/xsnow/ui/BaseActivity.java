package com.vise.xsnow.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vise.xsnow.event.BusFactory;
import com.vise.xsnow.manager.AppManager;

import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportActivity;

/**
 * @Description: Activity基类
 */
public abstract class BaseActivity extends SupportActivity {

    protected Context mContext;
    private boolean isOnResumeRegisterBus = false;
    private boolean isOnStartRegisterBus = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        AppManager.getInstance().addActivity(this);
        if (isOnResumeRegisterBus) {
            BusFactory.getBus().register(this);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        initView();
        bindEvent();
        initData();
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isOnStartRegisterBus) {
            BusFactory.getBus().register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
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
}
