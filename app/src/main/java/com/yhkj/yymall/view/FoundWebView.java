package com.yhkj.yymall.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Administrator on 2017/7/10.
 */

public class FoundWebView extends WebView {

    ScrollInterface web;

    /**
     * Constructs a new WebView with layout parameters.
     *
     * @param context a Context object used to access application assets
     * @param attrs   an AttributeSet passed to our parent
     */
    public FoundWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        web.onSChanged(l, t, oldl, oldt);

    }

    public void setOnCustomScroolChangeListener(ScrollInterface t) {
        this.web = t;
    }

    public interface ScrollInterface {
        public void onSChanged(int l, int t, int oldl, int oldt);
    }

    ;
}
