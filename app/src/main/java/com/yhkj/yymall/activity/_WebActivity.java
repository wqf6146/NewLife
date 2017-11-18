package com.yhkj.yymall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yhkj.yymall.R;

import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.config.IWebPageView;
import com.yhkj.yymall.config.ImageClickInterface;
import com.yhkj.yymall.config.MyWebChromeClient;
import com.yhkj.yymall.config.MyWebViewClient;
import com.yhkj.yymall.util.CommonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.os.Build.VERSION_CODES.KITKAT;


/**
 * Created by Administrator on 2017/8/17.
 */

//public class _WebActivity extends AppCompatActivity implements IWebPageView {

//    @Bind(R.id.aw_webview)
//    WebView webView;
//
//    @Bind(R.id.aw_img_back)
//    ImageView mImgBack;
//    @Bind(R.id.aw_tv_title)
//    TextView mTvTitle;
//
//    @Bind(R.id.aw_view_statusbar)
//    View mViewStatusBar;
//
////    @Bind(R.id.aw_progressbar)
////    ProgressBar mProgressBar;
//
//    private MyWebChromeClient mWebChromeClient;
//    // 网页链接
//    private String mUrl,mTitle;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getIntentData();
//        setContentView(R.layout.activity_web);
//        ButterKnife.bind(this);
//        initWebView();
//        webView.loadUrl(mUrl);
//    }
//
//    private void getIntentData() {
//        if (getIntent() != null) {
//            mUrl = getIntent().getStringExtra(Constant.WEB_TAG.TAG);
//            mTitle = getIntent().getStringExtra("title");
//        }
//    }
//
//    private void initWebView() {
//        if (Build.VERSION.SDK_INT >= KITKAT)
//            mViewStatusBar.getLayoutParams().height = CommonUtil.getStatusBarHeight(this);
//        mTvTitle.setText(mTitle);
//        mImgBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                doBack();
//            }
//        });
//        WebSettings ws = webView.getSettings();
//        // 网页内容的宽度是否可大于WebView控件的宽度
//        ws.setLoadWithOverviewMode(false);
//        // 保存表单数据
//        ws.setSaveFormData(true);
//        // 是否应该支持使用其屏幕缩放控件和手势缩放
//        ws.setSupportZoom(true);
//        ws.setBuiltInZoomControls(true);
//        ws.setDisplayZoomControls(false);
//        // 启动应用缓存
//        ws.setAppCacheEnabled(true);
//        // 设置缓存模式
//        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
//        // setDefaultZoom  api19被弃用
//        // 设置此属性，可任意比例缩放。
//        ws.setUseWideViewPort(true);
//        // 缩放比例 1
//        webView.setInitialScale(1);
//        // 告诉WebView启用JavaScript执行。默认的是false。
//        ws.setJavaScriptEnabled(true);
//        //  页面加载好以后，再放开图片
//        ws.setBlockNetworkImage(false);
//        // 使用localStorage则必须打开
//        ws.setDomStorageEnabled(true);
//        // 排版适应屏幕
//        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        // WebView是否支持多个窗口。
//        ws.setSupportMultipleWindows(true);
//
//        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
//        ws.setTextZoom(100);
//
//        mWebChromeClient = new MyWebChromeClient(this);
//        webView.setWebChromeClient(mWebChromeClient);
//        // 与js交互
//        webView.addJavascriptInterface(new ImageClickInterface(this), "aosObject");
//        webView.setWebViewClient(new MyWebViewClient(this));
//    }
//
//    @Override
//    public Activity getActivity() {
//        return this;
//    }
//
//
//    @Override
//    public void hindProgressBar() {
//        mProgressBar.setVisibility(View.GONE);
//    }
//
//    @Override
//    public void startProgress() {
////        startProgress90();
//    }
//
//    @Override
//    public void showWebView() {
//        webView.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void hindWebView() {
//        webView.setVisibility(View.INVISIBLE);
//    }
//
//    @Override
//    public void fullViewAddView(View view) {
////        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
////        videoFullView = new FullscreenHolder(this);
////        videoFullView.addView(view);
////        decor.addView(videoFullView);
//    }
//
//    @Override
//    public void showVideoFullView() {
////        videoFullView.setVisibility(View.VISIBLE);
//    }
//
//    @Override
//    public void hindVideoFullView() {
////        videoFullView.setVisibility(View.GONE);
//    }
//    //
//    @Override
//    public void progressChanged(int newProgress) {
////        if (mProgress90) {
////            int progress = newProgress * 100;
////            if (progress > 900) {
////                mProgressBar.setProgress(progress);
////                if (progress == 1000) {
////                    mProgressBar.setVisibility(View.GONE);
////                }
////            }
////        }
//    }
//
//    @Override
//    public void addImageClickListener() {
//        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
//        // 如要点击一张图片在弹出的页面查看所有的图片集合,则获取的值应该是个图片数组
//        webView.loadUrl("javascript:(function(){" +
//                "var objs = document.getElementsByTagName(\"img\");" +
//                "for(var i=0;i<objs.length;i++)" +
//                "{" +
//                //  "objs[i].onclick=function(){alert(this.getAttribute(\"has_link\"));}" +
//                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
//                "}" +
//                "})()");
//
//        // 遍历所有的a节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
//        webView.loadUrl("javascript:(function(){" +
//                "var objs =document.getElementsByTagName(\"a\");" +
//                "for(var i=0;i<objs.length;i++)" +
//                "{" +
//                "objs[i].onclick=function(){" +
//                "window.injectedObject.textClick(this.getAttribute(\"type\"),this.getAttribute(\"item_pk\"));}" +
//                "}" +
//                "})()");
//    }
//
//    /**
//     * 进度条 假装加载到90%
//     */
////    public void startProgress90() {
////        for (int i = 0; i < 900; i++) {
////            final int progress = i + 1;
////            mProgressBar.postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    mProgressBar.setProgress(progress);
////                    if (progress == 900) {
////                        mProgress90 = true;
////                        if (mPageFinish) {
////                            startProgress90to100();
////                        }
////                    }
////                }
////            }, (i + 1) * 2);
////        }
////    }
//
//    /**
//     * 进度条 加载到100%
//     */
////    public void startProgress90to100() {
////        for (int i = 900; i <= 1000; i++) {
////            final int progress = i + 1;
////            mProgressBar.postDelayed(new Runnable() {
////                @Override
////                public void run() {
////                    mProgressBar.setProgress(progress);
////                    if (progress == 1000) {
////                        mProgressBar.setVisibility(View.GONE);
////                    }
////                }
////            }, (i + 1) * 2);
////        }
////    }
//
//
//    public FrameLayout getVideoFullView() {
//        return null;
//    }
//
//    /**
//     * 全屏时按返加键执行退出全屏方法
//     */
//    public void hideCustomView() {
//        mWebChromeClient.onHideCustomView();
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//    }
//
//    /**
//     * 上传图片之后的回调
//     */
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE) {
//            mWebChromeClient.mUploadMessage(intent, resultCode);
//        } else if (requestCode == MyWebChromeClient.FILECHOOSER_RESULTCODE_FOR_ANDROID_5) {
//            mWebChromeClient.mUploadMessageForAndroid5(intent, resultCode);
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            return doBack();
//        }
//        return false;
//    }
//
//    private boolean doBack(){
//        //全屏播放退出全屏
//        if (mWebChromeClient.inCustomView()) {
//            hideCustomView();
//            return true;
//
//            //返回网页上一页
//        } else if (webView.canGoBack()) {
//            webView.goBack();
//            return true;
//
//            //退出网页
//        } else {
//            webView.loadUrl("about:blank");
//            finish();
//        }
//        return false;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        webView.onPause();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        webView.onResume();
//        // 支付宝网页版在打开文章详情之后,无法点击按钮下一步
//        webView.resumeTimers();
//        // 设置为横屏
//        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        videoFullView.removeAllViews();
//        if (webView != null) {
//            ViewGroup parent = (ViewGroup) webView.getParent();
//            if (parent != null) {
//                parent.removeView(webView);
//            }
//            webView.removeAllViews();
//            webView.loadUrl("about:blank");
//            webView.stopLoading();
//            webView.setWebChromeClient(null);
//            webView.setWebViewClient(null);
//            webView.destroy();
//            webView = null;
//        }
//    }
//
//    /**
//     * 打开网页:
//     *
//     * @param mContext 上下文
//     * @param mUrl      要加载的网页url
//     */
//    public static void loadUrl(Context mContext, String mUrl, String title) {
//        Intent intent = new Intent(mContext, WebActivity.class);
//        intent.putExtra(Constant.WEB_TAG.TAG, mUrl);
//        intent.putExtra("title", title);
//        mContext.startActivity(intent);
//    }
//}