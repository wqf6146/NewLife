package com.yhkj.yymall.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.yhkj.yymall.R;
import com.yhkj.yymall.util.CommonUtil;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

/**
 * Created by Administrator on 2017/6/29.
 */

public class CountView extends View {

    private long mSecond = 0;

    private Paint mPaintBg,mPaintDigit;

    private int mBgColor,mDigColor;


    private RectF mHRect,mMRect,mSRect,mColonRect1,mColonRect2;

    private int mWidth,mHeight;

    private int mDefaultTextSize = Math.round(16 * getResources().getDisplayMetrics().scaledDensity);

    private int mPadItem = 5;

    private String[] mHMS;

    private Paint.FontMetricsInt mFontMetrics;

    private boolean mRun = false;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mSecond > 0){
                mSecond--;
                mHMS = CommonUtil.secToTime(mSecond);
                invalidate();
                sendEmptyMessageDelayed(0,1000);
            }else{
                mRun = false;
            }
        }
    };

    public CountView(Context context){
        this(context,null);
    }

    public CountView(Context context, @Nullable AttributeSet attrs){
        super(context,attrs);
        init();
    }


    public void start(Long mills){
        if (!mRun){
            mRun = true;
            mSecond = mills;
            invalidate();
            mHandler.sendEmptyMessageDelayed(0,1000);
        }
    }

    public void reset(Long mills){
        mHandler.removeMessages(0);
        mSecond = mills;
        invalidate();
        mHandler.sendEmptyMessageDelayed(0,1000);
    }

    private void init(){
        mPaintBg = new Paint();
        mPaintDigit = new Paint(Paint.ANTI_ALIAS_FLAG);

        mBgColor = getResources().getColor(R.color.colorAccent);
        mDigColor = getResources().getColor(R.color.white);

        mPaintBg.setColor(mBgColor);
        mPaintBg.setStrokeWidth(2);
        mPaintBg.setTextSize(mDefaultTextSize);
        mPaintBg.setTextAlign(Paint.Align.CENTER);

        mPaintDigit.setColor(mDigColor);
        mPaintDigit.setStrokeWidth(2);

        mPaintDigit.setTextSize(mDefaultTextSize);
        mPaintDigit.setTextAlign(Paint.Align.CENTER);
        mFontMetrics = mPaintDigit.getFontMetricsInt();

        mHRect = new RectF();
        mMRect = new RectF();
        mSRect = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
//        if (mSecond!=0){
//
//        }else {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        }
        int width = getTextMeasureWidth(getFormatHMStext(mSecond)) + mPadItem*6;
        setMeasuredDimension(width,width/3);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

//        mHMS = getFormatHMS(mSecond);
        mHMS = CommonUtil.secToTime(mSecond);
        mHRect.set(0,0,getTextMeasureWidth(mHMS[0])+2*mPadItem,mHeight);

        float mRectLeft = mHRect.right + getTextMeasureWidth(":");
        float mRectRight = mRectLeft + getTextMeasureWidth(mHMS[1]) + 2*mPadItem;
        mMRect.set(mRectLeft,0,mRectRight,mHeight);

        float sRectLeft = mMRect.right + getTextMeasureWidth(":");
        float sRectRight = sRectLeft + getTextMeasureWidth(mHMS[2]) + 2*mPadItem;
        mSRect.set(sRectLeft,0,sRectRight,mHeight);

        mColonRect1 = new RectF(Math.round(mHRect.right),0,Math.round(mMRect.left),mHeight);
        mColonRect2 = new RectF(Math.round(mMRect.right),0,Math.round(mSRect.left),mHeight);
    }

    /**
     * measure text
     * @param text
     * @return
     */
    private int getTextMeasureWidth(String text){
        TextPaint newPaint = new TextPaint();
        newPaint.setTextSize(mDefaultTextSize);
        return Math.round(newPaint.measureText(text));
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);

        if (mHMS !=null ){
            //draw背景
            canvas.drawRoundRect(mHRect,10,10,mPaintBg);
            ////draw HH
            drawTextInRectCenter(canvas,mHRect,mHMS[0],mPaintDigit);

            //draw ":"
            drawTextInRectCenter(canvas,mColonRect1,":",mPaintBg);
            //draw背景
            canvas.drawRoundRect(mMRect,10,10,mPaintBg);
            ////draw MM
            drawTextInRectCenter(canvas,mMRect,mHMS[1],mPaintDigit);

            //draw ":"
            drawTextInRectCenter(canvas,mColonRect2,":",mPaintBg);
            //draw背景
            canvas.drawRoundRect(mSRect,10,10,mPaintBg);
            ////draw SS
            drawTextInRectCenter(canvas,mSRect,mHMS[2],mPaintDigit);
        }

    }

    private void drawTextInRectCenter(Canvas canvas,RectF rect,String text,Paint paint){
        float baseline = (rect.bottom + rect.top - mFontMetrics.bottom - mFontMetrics.top) / 2;
        canvas.drawText(text, rect.centerX(), baseline, paint);
    }


    private String[] getFormatHMS(@NonNull Long mills){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        String hms = formatter.format(mills * 1000);
        return hms.split(":");
    }
    private String getFormatHMStext(@NonNull Long mills){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(mills * 1000);
    }

}
