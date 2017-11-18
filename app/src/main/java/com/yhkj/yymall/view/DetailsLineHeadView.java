package com.yhkj.yymall.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshKernel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.yhkj.yymall.R;

/**
 * Created by Administrator on 2017/7/15.
 */

public class DetailsLineHeadView extends RelativeLayout implements RefreshHeader {


    public DetailsLineHeadView(Context context) {
        super(context);
        this.initView(context, null, 0);
    }

    public DetailsLineHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs, 0);
    }

    public DetailsLineHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {

    }
    @Override
    public void onInitialized(RefreshKernel kernel, int height, int extendHeight) {
    }

    @Override
    public void onPullingDown(float percent, int offset, int headHeight, int extendHeight) {
    }

    @Override
    public void onReleasing(float percent, int offset, int headHeight, int extendHeight) {

    }

    @Override
    public void onStartAnimator(RefreshLayout layout, int headHeight, int extendHeight) {
//        mProgressDrawable.start();
    }

    @Override
    public int onFinish(RefreshLayout layout, boolean success) {
        return 0;//延迟500毫秒之后再弹回
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @NonNull
    public View getView() {
        return this;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return null;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
    }
}