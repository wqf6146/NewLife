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

public class YiYaHeaderView extends RelativeLayout implements RefreshHeader {

    public static String REFRESH_HEADER_PULLDOWN = "下拉可以刷新";
    public static String REFRESH_HEADER_REFRESHING = "正在刷新";
    public static String REFRESH_HEADER_RELEASE = "释放立即刷新";
    public static String REFRESH_HEADER_FINISH = "刷新完成";
    public static String REFRESH_HEADER_FAILED = "刷新失败";

    private TextView mHeaderText;
    private SpinnerStyle mSpinnerStyle = SpinnerStyle.Translate;

    private OnRefreshLisiten mOnRefreshLisiten;

    public YiYaHeaderView(Context context) {
        super(context);
        this.initView(context, null, 0);
    }

    public YiYaHeaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context, attrs, 0);
    }

    public YiYaHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initView(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    private View mRootView;
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        mRootView = LayoutInflater.from(context).inflate(R.layout.view_refresh_header_normal,this);
        mHeaderText = (TextView) mRootView.findViewById(R.id.tv_normal_refresh_header_status);
        if (isInEditMode()) {
            mHeaderText.setText(REFRESH_HEADER_REFRESHING);
        }
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
        if (success) {
            mHeaderText.setText(REFRESH_HEADER_FINISH);
        } else {
            mHeaderText.setText(REFRESH_HEADER_FAILED);
        }
        return 500;//延迟500毫秒之后再弹回
    }

    @Override
    public void setPrimaryColors(int... colors) {
        if (colors.length > 1) {
            setBackgroundColor(colors[0]);
            mHeaderText.setTextColor(colors[1]);
        } else if (colors.length > 0) {
            setBackgroundColor(colors[0]);
            if (colors[0] == 0xffffffff) {;
                mHeaderText.setTextColor(0xff666666);
            } else {
                mHeaderText.setTextColor(0xffffffff);
            }
        }
    }

    @NonNull
    public View getView() {
        return mRootView;
    }

    @Override
    public SpinnerStyle getSpinnerStyle() {
        return mSpinnerStyle;
    }

    @Override
    public void onStateChanged(RefreshLayout refreshLayout, RefreshState oldState, RefreshState newState) {
        switch (newState) {
            case None:
                if (mOnRefreshLisiten!=null)
                    mOnRefreshLisiten.onBackTop();
                break;
            case PullDownToRefresh:
                if (mOnRefreshLisiten!=null)
                    mOnRefreshLisiten.onStartPull();
                mHeaderText.setText(REFRESH_HEADER_PULLDOWN);
                break;
            case Refreshing:
                mHeaderText.setText(REFRESH_HEADER_REFRESHING);
                break;
            case ReleaseToRefresh:
                mHeaderText.setText(REFRESH_HEADER_RELEASE);
                replaceRefreshLayoutBackground(refreshLayout);
                break;
        }
    }
    private Runnable restoreRunable;
    private void restoreRefreshLayoutBackground() {
        if (restoreRunable != null) {
            restoreRunable.run();
            restoreRunable = null;
        }
    }

    private void replaceRefreshLayoutBackground(final RefreshLayout refreshLayout) {
    }

    public interface OnRefreshLisiten{
        void onStartPull();
        void onBackTop();
    }

    public YiYaHeaderView setOnRefreshLisiten(OnRefreshLisiten onRefreshLisiten ){
        mOnRefreshLisiten = onRefreshLisiten;
        return this;
    }


}