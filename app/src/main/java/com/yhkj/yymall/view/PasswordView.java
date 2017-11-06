package com.yhkj.yymall.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import com.yhkj.yymall.R;


/**
 * Created by Administrator on 2017/7/13.
 */
public class PasswordView extends AppCompatEditText {

    private int passwordCount;

    private int strokeColor;

    private Paint mCirclePaint;

    private Paint mPaint;

    private int symbolColor;

    private float mRadius;

    private float inputBoxStroke;

    private StringBuffer mText;

    public PasswordView(Context context) {
        super(context);
    }

    public PasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.inputBox);
        passwordCount = ta.getInteger(R.styleable.inputBox_passwordCount, 6);
        strokeColor = ta.getColor(R.styleable.inputBox_stokeColor, Color.GRAY);
        symbolColor = ta.getColor(R.styleable.inputBox_symbolColor, Color.BLACK);
        mRadius = ta.getDimension(R.styleable.inputBox_symbolRadius, 12);
        inputBoxStroke = ta.getDimension(R.styleable.inputBox_inputBoxStroke, 1f);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setStroke((int) inputBoxStroke, strokeColor);
        gd.setCornerRadius(8);
        setBackgroundDrawable(gd);
        ta.recycle();
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setColor(strokeColor);
            mPaint.setStrokeWidth(inputBoxStroke);
        }
        if (mCirclePaint == null) {
            mCirclePaint = new Paint();
            mCirclePaint.setColor(symbolColor);
            mCirclePaint.setStyle(Paint.Style.FILL);
        }
    }

    public PasswordView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int singleWidth = getMeasuredWidth() / passwordCount;
        int height = getMeasuredHeight();
        for (int i = 1; i < passwordCount; i++) {
            canvas.drawLine(singleWidth * i, 0, singleWidth * i, height, mPaint);
        }
        if (mText != null) {
            int textSize = mText.length() > passwordCount ? passwordCount : mText.length();
            for (int i = 1; i <= textSize; i++) {
                canvas.drawCircle(singleWidth * i - singleWidth / 2, height / 2, mRadius, mCirclePaint);
            }
        }
    }

    public int getPasswordCount() {
        return passwordCount;
    }
    public void setPasswordCount(int passwordCount) {
        this.passwordCount = passwordCount;
    }
    public void setPassword(CharSequence text) {
        mText = new StringBuffer(text);
        if (text.length() > passwordCount) {
            mText.delete(mText.length() - 1, mText.length());
            return;
        }
        postInvalidate();
    }

    public void clearPassword() {
        if (mText != null) {
            mText.delete(0, mText.length());
        }
    }

    public CharSequence getPassword() {
        return mText;
    }
}
