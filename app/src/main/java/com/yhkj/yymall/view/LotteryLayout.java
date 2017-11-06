package com.yhkj.yymall.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vise.log.ViseLog;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.LottoryInfoBean;

/**
 * Created by Administrator on 2017/7/1.
 */

public class LotteryLayout extends FrameLayout {

    private Context mContext;

    LinearLayout[] mLotteryItemView = new LinearLayout[8];
    ImageView[] mImgs = new ImageView[8];
    TextView[] mTvs = new TextView[8];
    View[] mViews = new View[8];
    LinearLayout mLlStartBtn;

    TextView mTvDrawText;
    LinearLayout mLlBg;

    public LotteryLayout(Context context){
        super(context,null);
    }

    public LotteryLayout(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        mContext = context;
        init();
    }

    public void onDestroy(){
        System.gc();
        Log.e("onDestroy","lottery");
        mLotteryItemView = null;
        mImgs = null;
        mTvs = null;
        mViews = null;
        isMarqueeRunning = false;
        isGameRunning = false;
    }

    private void init() {
        inflate(mContext, R.layout.view_lotttery_layout,this);
        initView();
    }

    private int mCurBgRes = R.mipmap.ic_nor_lottrylight;
    private OnLotteryLisiten mOnLotteryLisiten;

    public void setLotteryLisiten(OnLotteryLisiten onlotteryLisiten){
        mOnLotteryLisiten = onlotteryLisiten;
    }

    public interface OnLotteryLisiten {
        void onStartLottery();
        void onEndLottery();
    }

    private void initView() {
        mLotteryItemView[0] = (LinearLayout) findViewById(R.id.vll_ll_item_1);
        mLotteryItemView[1] = (LinearLayout) findViewById(R.id.vll_ll_item_2);
        mLotteryItemView[2] = (LinearLayout) findViewById(R.id.vll_ll_item_3);
        mLotteryItemView[3] = (LinearLayout) findViewById(R.id.vll_ll_item_4);
        mLotteryItemView[4] = (LinearLayout) findViewById(R.id.vll_ll_item_5);
        mLotteryItemView[5] = (LinearLayout) findViewById(R.id.vll_ll_item_6);
        mLotteryItemView[6] = (LinearLayout) findViewById(R.id.vll_ll_item_7);
        mLotteryItemView[7] = (LinearLayout) findViewById(R.id.vll_ll_item_8);

        mImgs[0] = (ImageView) findViewById(R.id.vll_img_1);
        mImgs[1] = (ImageView) findViewById(R.id.vll_img_2);
        mImgs[2] = (ImageView) findViewById(R.id.vll_img_3);
        mImgs[3] = (ImageView) findViewById(R.id.vll_img_4);
        mImgs[4] = (ImageView) findViewById(R.id.vll_img_5);
        mImgs[5] = (ImageView) findViewById(R.id.vll_img_6);
        mImgs[6] = (ImageView) findViewById(R.id.vll_img_7);
        mImgs[7] = (ImageView) findViewById(R.id.vll_img_8);

        mTvs[0] = (TextView) findViewById(R.id.vll_tv_1);
        mTvs[1] = (TextView) findViewById(R.id.vll_tv_2);
        mTvs[2] = (TextView) findViewById(R.id.vll_tv_3);
        mTvs[3] = (TextView) findViewById(R.id.vll_tv_4);
        mTvs[4] = (TextView) findViewById(R.id.vll_tv_5);
        mTvs[5] = (TextView) findViewById(R.id.vll_tv_6);
        mTvs[6] = (TextView) findViewById(R.id.vll_tv_7);
        mTvs[7] = (TextView) findViewById(R.id.vll_tv_8);

        mViews[0] = (View)findViewById(R.id.vll_gray_1);
        mViews[1] = (View)findViewById(R.id.vll_gray_2);
        mViews[2] = (View)findViewById(R.id.vll_gray_3);
        mViews[3] = (View)findViewById(R.id.vll_gray_4);
        mViews[4] = (View)findViewById(R.id.vll_gray_5);
        mViews[5] = (View)findViewById(R.id.vll_gray_6);
        mViews[6] = (View)findViewById(R.id.vll_gray_7);
        mViews[7] = (View)findViewById(R.id.vll_gray_8);

        mTvDrawText = (TextView)findViewById(R.id.vll_tv_drawtext);
        mLlStartBtn = (LinearLayout)findViewById(R.id.vll_ll_start);
        mLlBg = (LinearLayout)findViewById(R.id.vll_ll_bg);
        mLlStartBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnLotteryLisiten!=null){
                    mOnLotteryLisiten.onStartLottery();
                }
            }
        });

        isMarqueeRunning = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isMarqueeRunning) {
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    post(new Runnable() {
                        @Override
                        public void run() {
                            if (mLlBg != null) {
                                if (mCurBgRes == R.mipmap.ic_nor_lottrylight) {
                                    mCurBgRes = R.mipmap.ic_nor_lottrylight_2;
                                    mLlBg.setBackgroundResource(mCurBgRes);
                                } else {
                                    mCurBgRes = R.mipmap.ic_nor_lottrylight;
                                    mLlBg.setBackgroundResource(mCurBgRes);
                                }
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private int currentIndex = 0;
    private int currentTotal = 0;
    private int stayIndex = 0;
    private boolean isMarqueeRunning = false;
    private boolean isGameRunning = false;
    private boolean isTryToStop = false;
    private static final int DEFAULT_SPEED = 150;
    private static final int MIN_SPEED = 50;
    private int currentSpeed = DEFAULT_SPEED;
    private long getInterruptTime() {
        currentTotal++;
        if (isTryToStop) {
            currentSpeed += 10;
            if (currentSpeed > DEFAULT_SPEED) {
                currentSpeed = DEFAULT_SPEED;
            }
        } else {
            if (currentTotal / mViews.length > 0) {
                currentSpeed -= 10;
            }
            if (currentSpeed < MIN_SPEED) {
                currentSpeed = MIN_SPEED;
            }
        }
        return currentSpeed;
    }

    private void tryToStop() {
        isTryToStop = true;
    }
    private class mLotteryThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (isGameRunning) {
                try {
                    Thread.sleep(getInterruptTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                post(new Runnable() {
                    @Override
                    public void run() {

                        if (!isGameRunning){
                            return;
                        }

                        int preIndex = currentIndex;
                        currentIndex++;

                        if (currentIndex >= mViews.length) {
                            currentIndex = 0;
                        }

                        mViews[preIndex].setVisibility(VISIBLE);
                        mViews[currentIndex].setVisibility(INVISIBLE);
                        if (isTryToStop && currentSpeed == DEFAULT_SPEED && stayIndex == currentIndex) {
                            isGameRunning = false;
                            mOnLotteryLisiten.onEndLottery();
                        }
                    }
                });
            }
        }
    }

    public void startRunLottery(final int tag){
        if (tag > 7){
            throw new IllegalArgumentException("tag >=0 && tag <= 7");
        }
        isTryToStop = false;
        isGameRunning = true;
        stayIndex = tag;
        for (int i=0; i<mViews.length;i++)
            mViews[i].setVisibility(VISIBLE);
        new mLotteryThread().start();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                tryToStop();
            }
        },3000);
    }

    public boolean getStatus(){
        return isGameRunning;
    }

    private LottoryInfoBean.DataBean mDataBean;
    public void setInfo(LottoryInfoBean.DataBean dataBean){
        mDataBean = dataBean;
        mTvDrawText.setText(mDataBean.getRollinfo().getTitle());
        for (int i=0; i<mDataBean.getPrizeinfo().size();i++) {
            LottoryInfoBean.DataBean.PrizeinfoBean bean = mDataBean.getPrizeinfo().get(i);
            Glide.with(getContext()).load(bean.getImg()).into(mImgs[i]);
            mTvs[i].setText(bean.getTitle());
        }
    }
}
