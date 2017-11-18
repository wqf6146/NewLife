package com.yhkj.yymall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.vise.log.ViseLog;
import com.vise.xsnow.event.BusFactory;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.adapter.UltraBannerPagerAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.BannerItemBean;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.ShopDetailsBean;
import com.yhkj.yymall.bean.ShopSpecBean;
import com.yhkj.yymall.bean.TimeKillDetailBean;
import com.yhkj.yymall.config.FullscreenHolder;
import com.yhkj.yymall.config.IWebPageView;
import com.yhkj.yymall.config.ImageClickInterface;
import com.yhkj.yymall.config.MyWebChromeClient;
import com.yhkj.yymall.config.MyWebViewClient;
import com.yhkj.yymall.event.MainTabSelectEvent;
import com.yhkj.yymall.fragment.CommentFragment;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.util.TagUtils;
import com.yhkj.yymall.view.GoodsGoneLayout;
import com.yhkj.yymall.view.flowlayout.FlowLayout;
import com.yhkj.yymall.view.flowlayout.TagAdapter;
import com.yhkj.yymall.view.flowlayout.TagFlowLayout;
import com.yhkj.yymall.view.nestpager.Page;
import com.yhkj.yymall.view.popwindows.DetailsMenuPopupView;
import com.yhkj.yymall.view.popwindows.ShopArgsPopupView;
import com.yhkj.yymall.view.popwindows.ShopGiftPopupView;
import com.yhkj.yymall.view.popwindows.TimeKilShopCarPopupView;
import com.yhkj.yymall.view.popwindows.WebPopupView;
import com.yhkj.yymall.view.viewpager.UltraViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import butterknife.Bind;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.yhkj.yymall.http.api.ApiService.SHARE_SHOP_URL;

/**
 * Created by Administrator on 2017/7/14.
 */

public class TimeKillDetailActivity extends BaseToolBarActivity  implements TimeKilShopCarPopupView.OnShopCarResLisiten,CommentFragment.OnFinishListen,IWebPageView {

    @Bind(R.id.ald_banner)
    UltraViewPager mBanner;

//    @Bind(R.id.webview)
//    AdvancedWebView mWebView;

    @Bind(R.id.webview)
    WebView mWebView;

    @Bind(R.id.videoContainer)
    FrameLayout mFlViewdeoContainer;


    @Bind(R.id.ald_rl_popcar)
    RelativeLayout mRlPopCar;

    @Bind(R.id.ald_rl_popargs)
    RelativeLayout mRlPopArgs;

    @Bind(R.id.ald_tv_shopname)
    TextView mTvShopName;

    @Bind(R.id.ald_tv_commentsize)
    TextView mTvCommentSize;

    @Bind(R.id.ald_img_avatar)
    ImageView mImgAvatar;

    @Bind(R.id.ald_tv_commonname)
    TextView mTvCommonName;

    @Bind(R.id.ald_tv_commondesc)
    TextView mTvCommonDesc;

    @Bind(R.id.ald_tv_spec)
    TextView mTvCommonSpec;

    @Bind(R.id.ald_flowlayout)
    TagFlowLayout mFlowLayout;

    @Bind(R.id.ald_tv_selectstring)
    TextView mTvSelectString;

    @Bind(R.id.ald_ll_leaseprice)
    LinearLayout mLlLeasePrice;

    @Bind(R.id.atd_tv_buystr)
    TextView mTvBuyStr;

    @Bind(R.id.ald_ll_payprice)
    LinearLayout mLlPayPrice;

    @Bind(R.id.atkd_tv_day)
    TextView mTvDay;

    @Bind(R.id.atkd_tv_hour)
    TextView mTvHour;

    @Bind(R.id.atkd_tv_min)
    TextView mTvMin;

    @Bind(R.id.atkd_progressbar)
    MaterialProgressBar mProgressBar;

    @Bind(R.id.atkd_tv_sales)
    TextView mTvSales;

    @Bind(R.id.atkd_tv_storenumb)
    TextView mTvStoreNumb;

    @Bind(R.id.atkd_tv_price)
    TextView mTvPrice;

    @Bind(R.id.atkd_tv_srcprice)
    TextView mTvSrcPrice;

    @Bind(R.id.at_ll_collect)
    LinearLayout mLlCollect;

    @Bind(R.id.atjd_ll_common)
    LinearLayout mLlCommon;

    TimeKillDetailBean.DataBean mDataBean;

    @Bind(R.id.acd_ll_getscore)
    LinearLayout mLlGetScore;

    @Bind(R.id.rl_detailsline_point)
    RelativeLayout rl_detailsline_point;

    @Bind(R.id.ac_tv_nocomment)
    TextView mTvNoComment;

    @Bind(R.id.ac_ll_cmfirst)
    LinearLayout mLlCmfirst;

    @Bind(R.id.tv_detailsline_point)
    TextView tv_detailsline_point;

    @Bind(R.id.rl_detailsline_exp)
    RelativeLayout rl_detailsline_exp;

    @Bind(R.id.at_ll_server)
    LinearLayout mLlServer;

    @Bind(R.id.tv_detailsline_exp)
    TextView tv_detailsline_exp;

    @Bind(R.id.ald_tv_shopdesc)
    TextView mTvShopDesc;

    @Bind(R.id.atd_ll_baseserver)
    LinearLayout mLlBaseServer;

    @Bind(R.id.ald_tv_storenone)
    TextView mTvStoreNone;

    @Bind(R.id.atd_tv_seller)
    TextView mTvSeller;

    private MyWebChromeClient mWebChromeClient;

    @Bind(R.id.acd_ll_getgiftz)
    LinearLayout mLlGetGif;

    @Bind(R.id.ald_tv_giftdesc)
    TextView mTvGiftDesc;

    @Bind(R.id.ac_img_checkarrow)
    ImageView mImgCheckArrow;

    @Bind(R.id.ac_tv_checkdetail)
    TextView mTvCheckDetail;

    @Bind(R.id.pageOne)
    Page mPageOne;

    @Bind(R.id.pageTwo)
    Page mPageTwo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timekilldetail);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        mWebView.resumeTimers();
        // 设置为横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mWebView.onPause();
    }

    @Override
    public void onFinish() {
        setTvTitleText("商品详情");
        setImgRightVisiable(View.VISIBLE);
        mCurState = 0;
    }


    private void initWebView() {
//        mProgressBar.setVisibility(View.VISIBLE);

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
        mWebView.addJavascriptInterface(new ImageClickInterface(this), "injectedObject");
        mWebView.setWebViewClient(new MyWebViewClient(this));
    }
    @Override
    public void hindProgressBar() {
//        mProgressBar.setVisibility(View.GONE);
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
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        mFlViewdeoContainer = new FullscreenHolder(TimeKillDetailActivity.this);
        mFlViewdeoContainer.addView(view);
        decor.addView(mFlViewdeoContainer);
    }
    @Override
    public void showVideoFullView() {
        mFlViewdeoContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindVideoFullView() {
        mFlViewdeoContainer.setVisibility(View.GONE);
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
        return mFlViewdeoContainer;
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        mWebChromeClient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    @Override
    public void onBackPressedSupport() {
        if (mWebChromeClient !=null && mWebChromeClient.inCustomView()) {
            hideCustomView();
        }else{
            super.onBackPressedSupport();
        }
    }
    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitleWireVisiable(GONE);
        mBanner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                WindowManager wm = (WindowManager) TimeKillDetailActivity.this
                        .getSystemService(Context.WINDOW_SERVICE);
                Point outSize = new Point();
                wm.getDefaultDisplay().getSize(outSize);
                mBanner.getLayoutParams().height = outSize.x;
            }
        });
        mBanner.initIndicator();
        mBanner.getIndicator().setMargin(0,0,0,20);
        mBanner.getIndicator().setOrientation(UltraViewPager.Orientation.HORIZONTAL);
        mBanner.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        mBanner.getIndicator().setFocusResId(0).setNormalResId(0);
        mBanner.getIndicator().setFocusColor(getResources().getColor(R.color.theme_bule)).setNormalColor(getResources().getColor(R.color.halfgraybg))
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        mBanner.getIndicator().build();
    }

    private void showSelectCarPop(Integer type){
        if (mDataBean == null) return;
        TimeKilShopCarPopupView carPopupView;
        if (mSelectSpecHashMap == null)
            carPopupView = new TimeKilShopCarPopupView(TimeKillDetailActivity.this,TimeKillDetailActivity.this,mDataBean,type);
        else
            carPopupView = new TimeKilShopCarPopupView(TimeKillDetailActivity.this,TimeKillDetailActivity.this,mDataBean,mSelectSpecHashMap,mSelectSpecPosHashMap,mSelectNumb,type);
        carPopupView.showPopupWindow();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        mWebView.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            showToast("分享成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            showToast("分享失败");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            showToast("取消分享");

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mWebView.onDestroy();
        UMShareAPI.get(this).release();
        mFlViewdeoContainer.removeAllViews();
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.removeAllViews();
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }


    @Override
    protected void bindEvent() {
        super.bindEvent();

        mPageOne.setOnPageStatusChange(new Page.OnPageStatusChange() {
            @Override
            public void onPageStatusChange(int status) {
                if (status == 1){
                    //显示pageOne
                    mImgCheckArrow.setImageResource(R.mipmap.ic_nor_pullarrow);
                    mTvCheckDetail.setText("上拉查看图文详情");
                }
            }
        });
        mPageTwo.setOnPageStatusChange(new Page.OnPageStatusChange() {
            @Override
            public void onPageStatusChange(int status) {
                if (status == 1){
                    //显示pageTwo
                    mImgCheckArrow.setImageResource(R.mipmap.ic_nor_pusharrow);
                    mTvCheckDetail.setText("下拉返回商品详情");
                }
            }
        });
        mLlGetGif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopGiftPopupView shopGiftPopupView = new ShopGiftPopupView(TimeKillDetailActivity.this,mDataBean.getGoodsId());
                shopGiftPopupView.showPopupWindow();
            }
        });

        mLlServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                    return;
                }
                startActivity(MyServiceActivity.class);
            }
        });
        rl_detailsline_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebPopupView webPopupView =  new WebPopupView(TimeKillDetailActivity.this,ApiService.YYWEB + Constant.WEB_TAG.GET_INT_BY_MALL);
                webPopupView.setTvTitle("购物得积分");
                webPopupView.showPopupWindow();
            }
        });
        rl_detailsline_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebPopupView webPopupView =  new WebPopupView(TimeKillDetailActivity.this,ApiService.YYWEB + Constant.WEB_TAG.GET_EXP_BY_MALL);
                webPopupView.setTvTitle("购物得经验");
                webPopupView.showPopupWindow();
            }
        });
        mLlBaseServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if (mDataBean.getSellerId() == 0){
                    url = ApiService.YYWEB + Constant.WEB_TAG.ZP_MALL_SELF;
                }else{
                    url = ApiService.YYWEB + Constant.WEB_TAG.ZP_MALL;
                }
                WebPopupView webPopupView = new WebPopupView(TimeKillDetailActivity.this, url);
                webPopupView.setTvTitle("基本服务");
                webPopupView.showPopupWindow();
            }
        });
        setImgBackLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( mCurState == 1) {
                    setTvTitleText("商品详情");
                    setImgRightVisiable(View.VISIBLE);
                    mCurState = 0;
                    pop();
                }else
                    AppManager.getInstance().finishActivity(TimeKillDetailActivity.this);
            }
        });

        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DetailsMenuPopupView(TimeKillDetailActivity.this).setOnMenuClickLisiten(new DetailsMenuPopupView.OnMenuClickLisiten() {
                    @Override
                    public void onMenuClick(int pos) {
                        if (pos == 1){
//                            showShareDialog();
                            new ShareAction(TimeKillDetailActivity.this)
                                    .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE,
                                            SHARE_MEDIA.QZONE)
                                    .setCallback(shareListener)
                                    .setShareboardclickCallback(new ShareBoardlistener() {
                                        @Override
                                        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                            String url = SHARE_SHOP_URL + "#" + mDataBean.getGoodsId();
                                            if (share_media == SHARE_MEDIA.SINA){
                                                if (CommonUtil.isWeiboClientAvailable(TimeKillDetailActivity.this)) {
                                                    UMImage image;
                                                    if (mDataBean.getPhoto() !=null && mDataBean.getPhoto().size() > 0)
                                                        image = new UMImage(TimeKillDetailActivity.this, mDataBean.getPhoto().get(0));  //缩略图
                                                    else
                                                        image = new UMImage(TimeKillDetailActivity.this, R.mipmap.ic_nor_srcpic);  //缩略图
                                                    new ShareAction(TimeKillDetailActivity.this).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧："+mDataBean.getName()+url).withMedia(image).setCallback(shareListener).share();
                                                }else{
                                                    showToast("请先安装新浪微博");
                                                }
                                            }else{
                                                UMWeb web = new UMWeb(url);
                                                web.setTitle(mDataBean.getName());//标题
                                                if (mDataBean.getPhoto() !=null && mDataBean.getPhoto().size() > 0)
                                                    web.setThumb( new UMImage(TimeKillDetailActivity.this, mDataBean.getPhoto().get(0)));  //缩略图
                                                else
                                                    web.setThumb( new UMImage(TimeKillDetailActivity.this, R.mipmap.ic_nor_srcpic));  //缩略图
                                                web.setDescription("我在YiYiYaYa发现了一个不错的商品，快来看看吧");//描述
                                                new ShareAction(TimeKillDetailActivity.this).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧").withMedia(web).setPlatform(share_media).setCallback(shareListener).share();
                                            }
                                        }
                                    })
                                    .open();
                        }else if (pos == 2){
                            //消息
                            if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                                showToast("请先登录");
                                startActivity(LoginActivity.class);
                                return;
                            }
                            startActivity(NewMessageActivity.class);
                        }else if (pos ==3){
                            //反馈
                            if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                                showToast("请先登录");
                                startActivity(LoginActivity.class);
                                return;
                            }
                            startActivity(AdvanceActivity.class);
                        }
                    }
                }).showPopupWindow(v);
            }
        });

        mLlCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                    return;
                }
                if (mDataBean == null) return;
                YYMallApi.addCollectShpp(TimeKillDetailActivity.this, new String[]{
                        String.valueOf(mDataBean.getGoodsId())
                }, new YYMallApi.ApiResult<CommonBean>(TimeKillDetailActivity.this) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        ViseLog.e(e);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        showToast("收藏成功");
                    }
                });
            }
        });
        mLlCommon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataBean == null) return;
                setTvTitleText(mTitleString);
                setImgRightVisiable(View.INVISIBLE);
                mCurState = 1;
                loadRootFragment(R.id.ald_fl_comment,CommentFragment.getInstance(mDataBean.getGoodsId(),-1,null).setOnFinishListen(TimeKillDetailActivity.this));
            }
        });
        mLlPayPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                    return;
                }

                if (mCurSpecBean == null || TextUtils.isEmpty(mSelectNumb)){
                    showSelectCarPop(1);
                    return;
                }
                Intent intent = new Intent(TimeKillDetailActivity.this, CheckOutActivity.class);
                intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.TIMEKILL);
                intent.putExtra("productId", String.valueOf(mCurSpecBean.getId()));
                intent.putExtra("nums", mSelectNumb);
                intent.putExtra("panicBuyId", mPanicBuyId);
                startActivity(intent);
            }
        });
        mLlLeasePrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStoreNone || mIsSale == 1) return;
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                    return;
                }

                if (mCurSpecBean == null || TextUtils.isEmpty(mSelectNumb)){
                    showSelectCarPop(1);
                    return;
                }

                if (!TextUtils.isEmpty(mCommonCanBuy)){
                    showToast(mCommonCanBuy);
                    return;
                }

                Intent intent = new Intent(TimeKillDetailActivity.this, CheckOutActivity.class);
                intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.TIMEKILL);
                intent.putExtra("productId", String.valueOf(mCurSpecBean.getId()));
                intent.putExtra("nums", mSelectNumb);
                intent.putExtra("panicBuyId", mPanicBuyId);
                startActivity(intent);
            }
        });
        mRlPopCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mStoreNone || mIsSale == 1) return;
                showSelectCarPop(1);
            }
        });
        mRlPopArgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataBean !=null) {
                    ShopArgsPopupView shopArgsPopupView = new ShopArgsPopupView<TimeKillDetailBean.DataBean.AttrBean>(TimeKillDetailActivity.this, mDataBean.getAttr()) {
                        @Override
                        protected void bind(ViewHolder holder, TimeKillDetailBean.DataBean.AttrBean attrBean, int position) {
                            holder.setText(R.id.isa_tv_key,attrBean.getName());
                            holder.setText(R.id.isa_tv_value,attrBean.getValue());
                        }
                    };
                    shopArgsPopupView.showPopupWindow();
                }
            }
        });
    }

    private LinkedHashMap mSelectSpecHashMap;
    private HashMap mSelectSpecPosHashMap;
//    private String mProductId;
    private String mSelectNumb;
    private String mPanicBuyId;

    @Override
    public void onShopCarEntityRes(LinkedHashMap hashMap) {
        mSelectSpecHashMap = hashMap;
    }


    @Override
    public void onShopCarResPos(HashMap hashMap) {
        mSelectSpecPosHashMap = hashMap;
    }

    private String mTvSelectTip;
    @Override
    public void onShopCarSelectString(String string) {
        if (TextUtils.isEmpty(string))
            mTvSelectString.setText(mTvSelectTip);
        else
            mTvSelectString.setText(string);
    }

    private ShopSpecBean.DataBean mCurSpecBean;
    @Override
    public void onShopSpecRes(ShopSpecBean.DataBean specBean, String numb) {
        mCurSpecBean = specBean;
        mSelectNumb = numb;
        if (specBean == null)
            return;
        mTvPrice.setText("¥" + String.valueOf(specBean.getPrice()));
    }

    private String mCommonCanBuy;
    @Override
    public void onCanSelectRes(String commonCanBuy) {
        mCommonCanBuy = commonCanBuy;
    }

    @Override
    protected void initData() {
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setImgRightResource(R.mipmap.ic_nor_3point);
        setTvTitleText("商品详情");
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    private void getData(){
        YYMallApi.getTimeKillDetail(this, getIntent().getStringExtra("id"), new YYMallApi.ApiResult<TimeKillDetailBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                if (e.getCode() == 6011 || e.getCode() == 6012 || e.getCode() == 8001){
//                    setNoDataView(R.mipmap.ic_nor_orderbg,e.getMessage());
//                    setNoDataBtnLisiten(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            YYApp.getInstance().setIndexShow(0);
//                            startActivity(MainActivity.class);
//                            AppManager.getInstance().finishExceptActivity(MainActivity.class);
//                        }
//                    });
                    replaceCustomView(new GoodsGoneLayout(TimeKillDetailActivity.this).setLoadLisiten(new GoodsGoneLayout.OnLoadDoneLisiten() {
                        @Override
                        public void onLoadSuccess() {
                            setImgRightVisiable(GONE);
                            setNetWorkErrShow(GONE);
                        }

                        @Override
                        public void onLoadFaild() {
                            setNetWorkErrShow(VISIBLE);
                        }
                    }));
                }else{
                    setNetWorkErrShow(VISIBLE);
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(TimeKillDetailBean.DataBean dataBean) {
                if (dataBean.getStatus() == 1){
                    replaceCustomView(new GoodsGoneLayout(TimeKillDetailActivity.this).setLoadLisiten(new GoodsGoneLayout.OnLoadDoneLisiten() {
                        @Override
                        public void onLoadSuccess() {
                            setImgRightVisiable(GONE);
                            setNetWorkErrShow(GONE);
                        }

                        @Override
                        public void onLoadFaild() {
                            setNetWorkErrShow(VISIBLE);
                        }
                    }));
                    return;
                }
                setNetWorkErrShow(GONE);
                mDataBean = dataBean;
                storeVerify();
                saleVerify();
                mPanicBuyId = String.valueOf(dataBean.getPanicId());
                setDetailData(dataBean);
            }
        });
    }

    /**
     * 库存判断
     */
    private boolean mStoreNone = false;
    private void storeVerify(){
        if (mDataBean.getStoreNum() <= 0){
            mTvStoreNone.setVisibility(VISIBLE);
            mStoreNone = true;
            mTvBuyStr.setTextColor(getResources().getColor(R.color.half_transparency));
            mTvSelectString.setTextColor(getResources().getColor(R.color.grayfont_1_5));
        }
    }
    private int mIsSale; //1为不可销售，0为可销售
    private void saleVerify(){
        mIsSale = mDataBean.getIsSale();
        if (mIsSale == 1){
            mTvStoreNone.setVisibility(VISIBLE);
            mTvStoreNone.setText("该商品暂不支持购买");
            mTvBuyStr.setTextColor(getResources().getColor(R.color.half_transparency));
            mTvSelectString.setTextColor(getResources().getColor(R.color.grayfont_1_5));
        }
    }
    private String mTitleString;
    public void setDetailData(final TimeKillDetailBean.DataBean dataBean){
        buildBannerBean(dataBean);
        //
        mProgressBar.setProgress(Math.round((float)dataBean.getSale() / (dataBean.getStoreNum() + dataBean.getSale()) * 100));
        mTvSales.setText("已售" + String.valueOf(dataBean.getSale()) + "件");
        mTvStoreNumb.setText("仅剩" + String.valueOf(dataBean.getStoreNum()) + "件");
        mTvDay.setText(String.valueOf(dataBean.getTime().getDay()));
        mTvHour.setText(String.valueOf(dataBean.getTime().getHour()));
        mTvMin.setText(String.valueOf(dataBean.getTime().getMin()));

        if (dataBean.getIsGift() == 1 && dataBean.getIsActivityGift() != 0){
            //有赠品
            mLlGetGif.setVisibility(VISIBLE);
            mTvGiftDesc.setText("购买即赠赠品（赠完为止）");
        }else{
            mLlGetGif.setVisibility(GONE);
        }

//        if (dataBean.getPoint() == 0 && dataBean.getExp() == 0)
//            mLlGetScore.setVisibility(GONE);
//        else{
//            tv_detailsline_point.setText("购物得" + dataBean.getPoint() + "积分");
//            tv_detailsline_exp.setText("购物得" + dataBean.getExp() + "经验");
//        }
        if (dataBean.getPoint() == 0){
            rl_detailsline_point.setVisibility(GONE);
        }else{
            tv_detailsline_point.setText("购物得" + dataBean.getPoint() + "积分");
        }
        if (dataBean.getExp() == 0){
            rl_detailsline_exp.setVisibility(GONE);
        }else{
            tv_detailsline_exp.setText("购物得" + dataBean.getExp() + "经验");
        }
        if (dataBean.getSellerId() == 0){
            mTvSeller.setText("自营");
        }else{
            mTvSeller.setText("商户");
        }

        //
        mTvPrice.setText("¥" + String.valueOf(dataBean.getPrice()));
        mTvSrcPrice.setText(String.valueOf("原价¥" + dataBean.getMarketPrice()));
        mTvSrcPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        //商品名字
        mTvShopName.setText(dataBean.getName());
        if (!TextUtils.isEmpty(dataBean.getDescription()))
            mTvShopDesc.setText(dataBean.getDescription());
        else
            mTvShopDesc.setVisibility(GONE);
        //轮播图
        UltraBannerPagerAdapter adapters = new UltraBannerPagerAdapter<BannerItemBean>(this,mBannerItemBean,false){
            @Override
            protected void bind(ViewGroup container, BannerItemBean itemBean, final int position) {
                final ImageView imageView = (ImageView) container.findViewById(R.id.vb_img);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                Glide.with(mContext).load(itemBean.img).placeholder(R.mipmap.ic_nor_srcpic).into(imageView);
                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TimeKillDetailActivity.this,ShopBannerActivity.class);
                        intent.putParcelableArrayListExtra(Constant.BANNER.ITEMBEAN,mBannerItemBean);
                        intent.putExtra(Constant.BANNER.POSITION,position);
                        startActivity(intent);
                    }
                });
            }
        };
        mBanner.setAdapter(adapters);
        initWebView();
        mWebView.loadUrl(ApiService.SHOP_DETAIL + "#" + dataBean.getGoodsId());
        Log.e("url",ApiService.SHOP_DETAIL + "#" + dataBean.getGoodsId());

        mTvSelectTip = "请选择商品";
        for (int i = 0; i < dataBean.getSpec().size(); i++) {
            if (i < dataBean.getSpec().size() - 1) {
                mTvSelectTip += dataBean.getSpec().get(i).getName() + "，";
            } else {
                mTvSelectTip += dataBean.getSpec().get(i).getName();
            }
        }
        mTvSelectString.setText(mTvSelectTip);


        if (dataBean.getComment()==null || dataBean.getComment().getList()==null || dataBean.getComment().getList().size() == 0) {
            mLlCmfirst.setVisibility(GONE);
            mTvNoComment.setVisibility(VISIBLE);
        }else{
            mLlCmfirst.setVisibility(VISIBLE);
            mTvNoComment.setVisibility(GONE);
            mTvCommentSize.setText(String.format(getString(R.string.shopcommentsize),String.valueOf(dataBean.getCommentCount())));
            TimeKillDetailBean.DataBean.CommentBean.ListBean listBean = dataBean.getComment().getList().get(0);
//            Glide.with(this).load(listBean.getUserico()).placeholder(R.mipmap.ic_nor_srcheadimg).into(mImgAvatar);
            Glide.with(this).load(listBean.getUserico()).asBitmap().centerCrop().placeholder(R.mipmap.ic_nor_srcheadimg)
                    .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new BitmapImageViewTarget(mImgAvatar) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mImgAvatar.setImageDrawable(circularBitmapDrawable);
                }
            });
            mTvCommonName.setText(listBean.getUser() == null ? "test" : listBean.getUser());
            mTvCommonDesc.setText(listBean.getContents());
            mTvCommonSpec.setText(listBean.getSpec());
        }

        //商品评价
        mTitleString = "产品评价(" + String.valueOf(dataBean.getCommentCount()) + ")";
        mFlowLayout.setAdapter(new TagAdapter<TimeKillDetailBean.DataBean.CommentBean.TypeBean>(dataBean.getComment().getType()) {
            @Override
            public View getView(FlowLayout parent, final int position, TimeKillDetailBean.DataBean.CommentBean.TypeBean bean) {
                final TextView tv = (TextView) LayoutInflater.from(TimeKillDetailActivity.this).inflate(R.layout.item_flow_tv,
                        mFlowLayout, false);
                tv.setText(bean.getName() + "(" + bean.getCount() + ")");
                return tv;
            }
        });
        mFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                if (mDataBean == null) return false;
                setTvTitleText(mTitleString);
                mTagSelect = position + 4;
                setImgRightVisiable(View.INVISIBLE);
                mCurState = 1;
                loadRootFragment(R.id.ald_fl_comment,CommentFragment.getInstance(mDataBean.getGoodsId(),mTagSelect,(Integer)view.getTag()).setOnFinishListen(TimeKillDetailActivity.this));
                return true;
            }
        });
        mFlowLayout.setMaxSelectCount(1);
    }



    private int mTagSelect;
    private int mCurState = 0; //0详情 1评论
    ArrayList<BannerItemBean> mBannerItemBean;
    public void buildBannerBean(TimeKillDetailBean.DataBean dataBean) {
        mBannerItemBean = new ArrayList<>();
        if (dataBean.getVideo() != null && !TextUtils.isEmpty(dataBean.getVideo().getUrl()) && !TextUtils.isEmpty(dataBean.getVideo().getImg())) {
            mBannerItemBean.add(new BannerItemBean(Constant.BANNER.VIDEO, dataBean.getVideo().getUrl(), dataBean.getVideo().getImg()));
        }
        for (int i = 0; i < dataBean.getPhoto().size(); i++) {
            mBannerItemBean.add(new BannerItemBean(Constant.BANNER.IMG, null, dataBean.getPhoto().get(i)));
        }
    }
}
