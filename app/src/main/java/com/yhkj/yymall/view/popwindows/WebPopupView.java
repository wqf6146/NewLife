package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.animation.Animation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.config.IWebPageView;
import com.yhkj.yymall.config.MyWebChromeClient;
import com.yhkj.yymall.config.MyWebViewClient;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 */
public class WebPopupView<T> extends BasePopupWindow implements IWebPageView {

    @Bind(R.id.vps_webview)
    WebView mWebView;

    @Bind(R.id.vps_btn_copy)
    TextView mTvCopy;

    @Bind(R.id.vps_tv_title)
    TextView mTvTitle;

    @Bind(R.id.vpw_fl_wait)
    FrameLayout mFrameWait;

    @Bind(R.id.vpw_progressBar)
    ProgressBar mProgressBar;

    private MyWebChromeClient mWebChromeClient;

    public WebPopupView(Activity context,String url) {
        super(context);
        init(url);
    }

    @Override
    protected Animation initShowAnimation() {
        Animation animation = getTranslateAnimation(500 * 2, 0, 300);
        return animation;
    }

    @Override
    protected Animation initExitAnimation() {
        Animation animation = getTranslateAnimation(0, 500 * 2, 300);
        return animation;
    }
    @Override
    public Activity getActivity() {
        return getContext();
    }
    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        View view = createPopupById(R.layout.view_pop_webview);
        ButterKnife.bind(this,view);
        return view;
    }

    private void init(String url) {
        mTvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissWithOutAnima();
            }
        });
//        mWebView.setListener(getContext(), this);
        initWebView();
        mWebView.loadUrl(url);
    }


    private void initWebView() {
//        mProgressBar.setVisibility(View.VISIBLE);
        if (mWebView == null) return;
        WebSettings ws = mWebView.getSettings();
        // 网页内容的宽度是否可大于WebView控件的宽度
        ws.setLoadWithOverviewMode(false);
        // 保存表单数据
        ws.setSaveFormData(true);
        // 是否应该支持使用其屏幕缩放控件和手势缩放
        ws.setSupportZoom(true);
        ws.setBuiltInZoomControls(true);
        ws.setDisplayZoomControls(false);
        // 启动应用缓存
        ws.setAppCacheEnabled(true);
        // 设置缓存模式
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setDefaultZoom  api19被弃用
        // 设置此属性，可任意比例缩放。
        ws.setUseWideViewPort(true);
        // 缩放比例 1
        mWebView.setInitialScale(1);
        // 告诉WebView启用JavaScript执行。默认的是false。
        ws.setJavaScriptEnabled(true);
        //  页面加载好以后，再放开图片
        ws.setBlockNetworkImage(false);
        // 使用localStorage则必须打开
        ws.setDomStorageEnabled(true);
        // 排版适应屏幕
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // WebView是否支持多个窗口。
        ws.setSupportMultipleWindows(true);

        // webview从5.0开始默认不允许混合模式,https中不能加载http资源,需要设置开启。
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ws.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /** 设置字体默认缩放大小(改变网页字体大小,setTextSize  api14被弃用)*/
        ws.setTextZoom(100);

        mWebChromeClient = new MyWebChromeClient(this);
        mWebView.setWebChromeClient(mWebChromeClient);
        // 与js交互
//        mWebView.addJavascriptInterface(new JSInterface(this), "injectedObject");
        mWebView.setWebViewClient(new MyWebViewClient(this));
    }
    @Override
    public void hindProgressBar() {
//        mProgressBar.setVisibility(View.GONE);
//        setNetWorkErrShow(GONE);
        mProgressBar.setVisibility(GONE);
        mFrameWait.setVisibility(GONE);
    }

    @Override
    public void startProgress() {
//        startProgress90();
    }

    @Override
    public void showWebView() {
        mWebView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindWebView() {
        mWebView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void fullViewAddView(View view) {
//        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
//        mFlViewdeoContainer = new FullscreenHolder(LeaseDetailActivity.this);
//        mFlViewdeoContainer.addView(view);
//        decor.addView(mFlViewdeoContainer);
    }
    @Override
    public void showVideoFullView() {
//        mFlViewdeoContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindVideoFullView() {
//        mFlViewdeoContainer.setVisibility(View.GONE);
    }

    @Override
    public void progressChanged(int newProgress) {

    }
    @Override
    public void addImageClickListener() {
        // 这段js函数的功能就是，遍历所有的img节点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
        // 如要点击一张图片在弹出的页面查看所有的图片集合,则获取的值应该是个图片数组
        mWebView.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                //  "objs[i].onclick=function(){alert(this.getAttribute(\"has_link\"));}" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
                "}" +
                "})()");

        // 遍历所有的a节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
        mWebView.loadUrl("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"a\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){" +
                "window.injectedObject.textClick(this.getAttribute(\"type\"),this.getAttribute(\"item_pk\"));}" +
                "}" +
                "})()");
    }
    @Override
    public FrameLayout getVideoFullView() {
        return null;
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        mWebChromeClient.onHideCustomView();
    }
    @Override
    public void onDismiss() {
        super.onDismiss();
        mWebView.destroy();
    }

    public void setTvTitle(String title){
        mTvTitle.setText(title);
    }

    @Override
    public View initAnimaView() {
        return findViewById(R.id.vps_rl_popview);
    }
}
