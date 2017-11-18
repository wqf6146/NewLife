package com.yhkj.yymall.config;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;


/**
 */
public class MyWebViewClient extends WebViewClient {

    private IWebPageView mIWebPageView;
    private Activity mActivity;

    public MyWebViewClient(IWebPageView mIWebPageView) {
        this.mIWebPageView = mIWebPageView;
        mActivity = mIWebPageView.getActivity();

    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // 优酷视频跳转浏览器播放
        if (url.startsWith("http://v.youku.com/")) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addCategory("android.intent.category.BROWSABLE");
            Uri content_url = Uri.parse(url);
            intent.setData(content_url);
            mActivity.startActivity(intent);
            return true;

            // 电话、短信、邮箱
        } else if (url.startsWith(WebView.SCHEME_TEL) || url.startsWith("sms:") || url.startsWith(WebView.SCHEME_MAILTO)) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                mActivity.startActivity(intent);
            } catch (ActivityNotFoundException ignored) {
            }
            return true;
        }
        mIWebPageView.startProgress();
        view.loadUrl(url);
        return false;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
//        if (mActivity.mProgress90) {
//            mIWebPageView.hindProgressBar();
//        } else {
//            mActivity.mPageFinish = true;
//        }
//        if (!CheckNetwork.isNetworkConnected(mActivity)) {
//            mIWebPageView.hindProgressBar();
//        }
        // html加载完成之后，添加监听图片的点击js函数
        mIWebPageView.hindProgressBar();
        mIWebPageView.addImageClickListener();
        super.onPageFinished(view, url);
    }

    // 视频全屏播放按返回页面被放大的问题
    @Override
    public void onScaleChanged(WebView view, float oldScale, float newScale) {
        super.onScaleChanged(view, oldScale, newScale);
        if (newScale - oldScale > 7) {
            view.setInitialScale((int) (oldScale / newScale * 100)); //异常放大，缩回去。
        }
    }
}
