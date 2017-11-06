package com.yhkj.yymall.view.behavior;

import android.content.Context;
import android.hardware.SensorManager;
import android.support.design.BuildConfig;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.OverScroller;


import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;

import java.lang.ref.WeakReference;


public class LeaseHeaderPagerBehavior extends ViewOffsetBehavior {
    private static final String TAG = "UcNewsHeaderPager";
    public static final int STATE_OPENED = 0;
    public static final int STATE_CLOSED = 1;
    public static final int DURATION_SHORT = 300;
    public static final int DURATION_LONG = 600;

    private int mCurState = STATE_OPENED;
    private OnPagerStateListener mPagerStateListener;

    private OverScroller mOverScroller;
    private OverScroller mFlingScroller;

    private WeakReference<CoordinatorLayout> mParent;
    private WeakReference<View> mChild;
    private Context mContext;
    public void setPagerStateListener(OnPagerStateListener pagerStateListener) {
        mPagerStateListener = pagerStateListener;
    }

    public LeaseHeaderPagerBehavior() {
        init();
    }

    public LeaseHeaderPagerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        mPhysicalCoeff =  SensorManager.GRAVITY_EARTH // g (m/s^2)
                * 39.37f // inch/meter
                * context.getResources().getDisplayMetrics().density * 160.0f
                * 0.84f;
    }

    private void init() {
        mOverScroller = new OverScroller(YYApp.getInstance());
        mFlingScroller = new OverScroller(YYApp.getInstance());
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        mParent = new WeakReference<CoordinatorLayout>(parent);
        mChild = new WeakReference<View>(child);
    }

    private int mTopRvOffsetY;
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, View child, View
            directTargetChild, View target, int nestedScrollAxes) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "onStartNestedScroll: nestedScrollAxes=" + nestedScrollAxes);
        }

        boolean canScroll = canScroll(child, 0);

        //拦截垂直方向上的滚动事件且当前状态是打开的并且还可以继续向上收缩
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0 && canScroll &&
                !isClosed(child);
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, View child, View target,
                                    float velocityX, float velocityY) {
        // velocityY 上滑负 下拉正
        // consumed the flinging behavior until Closed
        if (mCurState == STATE_CLOSED)
            return false;
        boolean coumsed = !isClosed(child);
        if (target instanceof RecyclerView && target.getId() == R.id.fn_recyclerView_hot ) {
            RecyclerView rvTop = (RecyclerView)target;
            if (rvTop.computeVerticalScrollExtent() + rvTop.computeVerticalScrollOffset()
                    >= rvTop.computeVerticalScrollRange()){
                if (mParentFlingRunable != null) {
                    child.removeCallbacks(mParentFlingRunable);
                    mParentFlingRunable = null;
                }
                mParentFlingRunable = new ParentFlingRunnable(mParent.get(),mChild.get());
                mParentFlingRunable.fling(Math.round(velocityY));
            }
        } else if (target instanceof RecyclerView && target.getId() == R.id.fn_recycleview_content && mCurState == STATE_OPENED){
            if (mParentFlingRunable != null) {
                child.removeCallbacks(mParentFlingRunable);
                mParentFlingRunable = null;
            }
            mParentFlingRunable = new ParentFlingRunnable(mParent.get(),mChild.get());
            mParentFlingRunable.fling(Math.round(velocityY));
        }

        return coumsed;
    }

    private static final float INFLEXION = 0.35f; // Tension lines cross at (INFLEXION, 1)
    // Fling friction
    private static float mFlingFriction = ViewConfiguration.getScrollFriction();
    private static float mPhysicalCoeff;
    private static float DECELERATION_RATE = (float) (Math.log(0.78) / Math.log(0.9));

    private double getSplineDeceleration(int velocity) {
        return Math.log(INFLEXION * Math.abs(velocity) / (mFlingFriction * mPhysicalCoeff));
    }

    //通过初始速度获取最终滑动距离
    private double getSplineFlingDistance(int velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return mFlingFriction * mPhysicalCoeff * Math.exp(DECELERATION_RATE / decelMinusOne * l);
    }
    private int getSplineFlingDuration(int velocity) {
        final double l = getSplineDeceleration(velocity);
        final double decelMinusOne = DECELERATION_RATE - 1.0;
        return (int) (1000.0 * Math.exp(l / decelMinusOne));
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, View child, View target,
                                 float velocityX, float velocityY, boolean consumed) {
        Log.i(TAG, "onNestedFling: velocityY=" +velocityY);
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY,
                consumed);
    }

    private boolean isClosed(View child) {
        boolean isClosed = child.getTranslationY() == getHeaderOffsetRange();
        return isClosed;
    }

    public boolean isClosed() {
        return mCurState == STATE_CLOSED;
    }

    private void changeState(int newState) {
        if (mCurState != newState) {
            mCurState = newState;
            if (mCurState == STATE_OPENED) {
                if (mPagerStateListener != null) {
                    mPagerStateListener.onPagerOpened();
                }

            } else {
                if (mPagerStateListener != null) {
                    mPagerStateListener.onPagerClosed();
                }
            }
        }
    }

    // 表示 Header TransLationY 的值是否达到我们指定的阀值， headerOffsetRange，到达了，返回 false，
    // 否则，返回 true。注意 TransLationY 是负数。
    private boolean canScroll(View child, float pendingDy) {
        int pendingTranslationY = (int) (child.getTranslationY() - pendingDy);
        int headerOffsetRange = getHeaderOffsetRange();
        if (pendingTranslationY >= headerOffsetRange && pendingTranslationY <= 0) {
            return true;
        }
        return false;
    }


    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, final View child, MotionEvent ev) {

        boolean closed = isClosed();
        Log.i(TAG, "onInterceptTouchEvent: closed=" + closed);
        if (ev.getAction() == MotionEvent.ACTION_UP && !closed) {
            handleActionUp(parent,child);
        }

        return super.onInterceptTouchEvent(parent, child, ev);
    }
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, View child, View target,
                                  int dx, int dy, int[] consumed) {
        if (child.getTranslationY() >= 0 && mCurState == STATE_OPENED && target instanceof RecyclerView && target.getId() == R.id.fn_recyclerView_hot){
            if (dy >0){
                //上拉
                if (target.canScrollVertically(1)){
                    //还可以向下滚动
                    return;
                }
            }else{//下拉
                if (target.canScrollVertically(-1)){
                    //还可以向上滚动
                    return;
                }
            }
        }
        if (mOverScroller!=null && mOverScroller.computeScrollOffset()){
            return ;
        }
        //dy>0 scroll up;dy<0,scroll down
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed);
        float halfOfDis = dy;
        //    不能滑动了，直接给 Header 设置 终值，防止出错
        if (!canScroll(child, halfOfDis)) {
            child.setTranslationY(halfOfDis > 0 ? getHeaderOffsetRange() : 0);
        } else {
            child.setTranslationY(child.getTranslationY() - halfOfDis);
        }
        //consumed all scroll behavior after we started Nested Scrolling
        consumed[1] = dy;
    }

    //    需要注意的是  Header 我们是通过 setTranslationY 来移出屏幕的，所以这个值是负数
    private int mDynDis;
    public void setHeadViewHeight(int headViewHeight){
        mDynDis = headViewHeight;
    }
    private int getHeaderOffsetRange() {
        return -mDynDis;
    }

    private void handleActionUp(CoordinatorLayout parent, final View child) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "handleActionUp: ");
        }
        if (mFlingRunnable != null) {
            child.removeCallbacks(mFlingRunnable);
            mFlingRunnable = null;
        }
        /// 6.0f
        if (child.getTranslationY() <= getHeaderOffsetRange() + 200) {
            mFlingRunnable = new FlingRunnable(parent, child);
            mFlingRunnable.scrollToClosed(DURATION_SHORT);
        }

    }

    private void onFlingFinished(View layout) {
        changeState(isClosed(layout) ? STATE_CLOSED : STATE_OPENED);
    }

    public void openPager() {
        openPager(DURATION_LONG);
    }

    /**
     * @param duration open animation duration
     */
    public void openPager(int duration) {
        View child = mChild.get();
        CoordinatorLayout parent = mParent.get();
        if (isClosed() && child != null) {
            if (mParentFlingRunable !=null ){
                child.removeCallbacks(mFlingRunnable);
                mParentFlingRunable = null;
            }
            if (mFlingRunnable != null) {
                child.removeCallbacks(mFlingRunnable);
                mFlingRunnable = null;
            }
            mFlingRunnable = new FlingRunnable(parent, child);
            mFlingRunnable.scrollToOpen(duration);
        }
    }

    public void closePager() {
        closePager(DURATION_SHORT);
    }

    /**
     * @param duration close animation duration
     */
    public void closePager(int duration) {
        View child = mChild.get();
        CoordinatorLayout parent = mParent.get();
        if (!isClosed()) {
            if (mParentFlingRunable !=null ){
                child.removeCallbacks(mFlingRunnable);
                mParentFlingRunable = null;
            }
            if (mFlingRunnable != null) {
                child.removeCallbacks(mFlingRunnable);
                mFlingRunnable = null;
            }
            mFlingRunnable = new FlingRunnable(parent, child);
            mFlingRunnable.scrollToClosed(duration);
        }
    }

    private FlingRunnable mFlingRunnable;
    private ParentFlingRunnable mParentFlingRunable;

    /**
     * For animation , Why not use {@link android.view.ViewPropertyAnimator } to play animation
     * is of the
     * other {@link CoordinatorLayout.Behavior} that depend on this could not receiving the
     * correct result of
     * {@link View#getTranslationY()} after animation finished for whatever reason that i don't know
     */
    private class ParentFlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;

        ParentFlingRunnable(CoordinatorLayout parent, View layout) {
            mParent = parent;
            mLayout = layout;
        }

        public void fling(int velocityY){
            float curTranslationY =  ViewCompat.getTranslationY(mLayout);
//            mFlingScroller.fling(0, Math.round(ty),0,velocityY,0,0,-500,10000);
            int dis = velocityY > 0 ? -(int)getSplineFlingDistance(velocityY) : (int)getSplineFlingDistance(velocityY);
             if (dis < 0){
                //往上滑出了屏幕
                int tagY = getHeaderOffsetRange();
                dis = dis < tagY ? tagY : dis;

            }else{
                //下拉回到屏幕
                float maxBackTopDis = Math.abs(curTranslationY);
                dis = Math.round(dis > maxBackTopDis ? maxBackTopDis : dis);
            }
            mFlingScroller.startScroll(0, Math.round(curTranslationY - 0.1f), 0, dis,
                    getSplineFlingDuration(velocityY));
            start();
        }

        private void start() {
            if (mFlingScroller.computeScrollOffset()) {
                mParentFlingRunable = new ParentFlingRunnable(mParent, mLayout);
                ViewCompat.postOnAnimation(mLayout, mParentFlingRunable);
            }
        }

        @Override
        public void run() {
            if (mLayout != null && mFlingScroller != null) {
                if (mFlingScroller.computeScrollOffset()) {
                     if (BuildConfig.DEBUG) {
                        Log.d(TAG, "run: " + mFlingScroller.getCurrY());
                     }
                    if (mFlingScroller.getCurrY()<=getHeaderOffsetRange()){
                        closePager();
                    }else{
                        ViewCompat.setTranslationY(mLayout, mFlingScroller.getCurrY());
                        ViewCompat.postOnAnimation(mLayout, this);
                    }
                }
            }
        }
    }
    private class FlingRunnable implements Runnable {
        private final CoordinatorLayout mParent;
        private final View mLayout;

        FlingRunnable(CoordinatorLayout parent, View layout) {
            mParent = parent;
            mLayout = layout;
        }

        public void scrollToClosed(int duration) {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            float dy = getHeaderOffsetRange() - curTranslationY;
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "scrollToClosed:offest:" + getHeaderOffsetRange());
                Log.d(TAG, "scrollToClosed: cur0:" + curTranslationY + ",end0:" + dy);
                Log.d(TAG, "scrollToClosed: cur:" + Math.round(curTranslationY) + ",end:" + Math
                        .round(dy));
                Log.d(TAG, "scrollToClosed: cur1:" + (int) (curTranslationY) + ",end:" + (int) dy);
            }
            mOverScroller.startScroll(0, Math.round(curTranslationY - 0.1f), 0, Math.round(dy +
                    0.1f), duration);
            start();
        }

        public void scrollToOpen(int duration) {
            float curTranslationY = ViewCompat.getTranslationY(mLayout);
            mOverScroller.startScroll(0, (int) curTranslationY, 0, (int) -curTranslationY,
                    duration);
            mPagerStateListener.startPagerOpened();
            start();
        }

        private void start() {
            if (mOverScroller.computeScrollOffset()) {
                mFlingRunnable = new FlingRunnable(mParent, mLayout);
                ViewCompat.postOnAnimation(mLayout, mFlingRunnable);
            } else {
                onFlingFinished(mLayout);
            }
        }

        @Override
        public void run() {
            if (mLayout != null && mOverScroller != null) {
                if (mOverScroller.computeScrollOffset()) {
                    if (BuildConfig.DEBUG) {
                        Log.d(TAG, "run: " + mOverScroller.getCurrY());
                    }
                    ViewCompat.setTranslationY(mLayout, mOverScroller.getCurrY());
                    ViewCompat.postOnAnimation(mLayout, this);
                } else {
                    onFlingFinished(mLayout);
                }
            }
        }
    }

    /**
     * callback for HeaderPager 's state
     */
    public interface OnPagerStateListener {
        /**
         * do callback when pager closed
         */
        void onPagerClosed();

        /**
         * do callback when pager opened
         */
        void onPagerOpened();

        void startPagerOpened();
    }

}
