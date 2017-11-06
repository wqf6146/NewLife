package com.yhkj.yymall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.adapter.DetailsPopAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.BannerItemBean;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.NormsBean;
import com.yhkj.yymall.bean.ShopDetailsBean;
import com.yhkj.yymall.config.FullscreenHolder;
import com.yhkj.yymall.config.IWebPageView;
import com.yhkj.yymall.config.ImageClickInterface;
import com.yhkj.yymall.config.MyWebChromeClient;
import com.yhkj.yymall.config.MyWebViewClient;
import com.yhkj.yymall.fragment.CommentFragment;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.GoodsGoneLayout;
import com.yhkj.yymall.view.flowlayout.FlowLayout;
import com.yhkj.yymall.view.flowlayout.TagAdapter;
import com.yhkj.yymall.view.flowlayout.TagFlowLayout;
import com.yhkj.yymall.view.nestpager.Page;
import com.yhkj.yymall.view.popwindows.DetailsMenuPopupView;
import com.yhkj.yymall.view.popwindows.ShopArgsPopupView;
import com.yhkj.yymall.view.popwindows.ShopGiftPopupView;
import com.yhkj.yymall.view.popwindows.WebPopupView;
import com.yhkj.yymall.view.viewpager.PagerAdapter;
import com.yhkj.yymall.view.viewpager.UltraViewPager;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.yhkj.yymall.http.api.ApiService.SHARE_SHOP_URL;

/**
 * Created by Administrator on 2017/7/8.
 */

public class CommodityDetailsActivity extends BaseToolBarActivity implements CommentFragment.OnFinishListen,IWebPageView {

    @Bind(R.id.as_allflowlayouts)
    TagFlowLayout as_allflowlayouts;

    @Bind(R.id.yvp_detailsline)
    UltraViewPager yvp_detailsline;

    @Bind(R.id.tv_detailsline_name)
    TextView tv_detailsline_name;

    @Bind(R.id.tv_detailsline_price)
    TextView tv_detailsline_price;

    @Bind(R.id.tv_detailsline_yprice)
    TextView tv_detailsline_yprice;

    @Bind(R.id.tv_detailsline_xiaol)
    TextView tv_detailsline_xiaol;

    @Bind(R.id.rl_detailsline_point)
    RelativeLayout rl_detailsline_point;

    @Bind(R.id.tv_detailsline_point)
    TextView tv_detailsline_point;

    @Bind(R.id.rl_detailsline_exp)
    RelativeLayout rl_detailsline_exp;

    @Bind(R.id.tv_detailsline_exp)
    TextView tv_detailsline_exp;

    @Bind(R.id.tv_detailsline_pingj)
    TextView tv_detailsline_pingj;

    @Bind(R.id.tv_detailsline_user)
    TextView tv_detailsline_user;

    @Bind(R.id.img_detailline_useric)
    ImageView img_detailline_useric;

    @Bind(R.id.tv_detailsline_connet)
    TextView tv_detailsline_connet;

    @Bind(R.id.tv_detailsline_spec)
    TextView tv_detailsline_spec;

    @Bind(R.id.tv_detailsline_standard)
    TextView tv_detailsline_standard;

    //    @Bind(R.id.web_commoditydetails)
//    AdvancedWebView web_commoditydetails;
    @Bind(R.id.web_commoditydetails)
    WebView web_commoditydetails;

    @Bind(R.id.videoContainer)
    FrameLayout mFlViewdeoContainer;

    @Bind(R.id.tv_commodity_addshopcar)
    TextView tv_commodity_addshopcar;

    @Bind(R.id.rl_detailsline_standard)
    RelativeLayout rl_detailsline_standard;

    @Bind(R.id.tv_commodity_buy)
    TextView tv_commodity_buy;

    @Bind(R.id.ac_ll_collect)
    LinearLayout mLlCollect;

    @Bind(R.id.ll_commoditydetails_kf)
    LinearLayout ll_commoditydetails_kf;

    @Bind(R.id.ald_rl_popargs)
    RelativeLayout mRlPopArgs;

    @Bind(R.id.al_ll_comment)
    LinearLayout mLlComment;


    @Bind(R.id.ald_tv_shopdesc)
    TextView mTvShopDesc;

    @Bind(R.id.ac_tv_nocomment)
    TextView mTvNoComment;

    @Bind(R.id.ac_ll_cmfirst)
    LinearLayout mLlCmfirst;

    @Bind(R.id.acd_ll_getscore)
    LinearLayout mLlGetScore;

    @Bind(R.id.acd_rl_baseserver)
    RelativeLayout mRlBaseServer;

    @Bind(R.id.ac_tv_yytip)
    TextView mTvYyTip;

    @Bind(R.id.ald_tv_storenone)
    TextView mTvStoreNone;

    @Bind(R.id.atd_tv_seller)
    TextView mTvSeller;


    @Bind(R.id.acd_ll_getgiftz)
    LinearLayout mLlGetGif;

    @Bind(R.id.ald_tv_giftdesc)
    TextView mTvGiftDesc;

    @Bind(R.id.common_tv_cut)
    TextView mTvCut;

    @Bind(R.id.common_rl_cut)
    RelativeLayout mRlCut;

    @Bind(R.id.ac_img_checkarrow)
    ImageView mImgCheckArrow;

    @Bind(R.id.ac_tv_checkdetail)
    TextView mTvCheckDetail;

    @Bind(R.id.pageOne)
    Page mPageOne;

    @Bind(R.id.pageTwo)
    Page mPageTwo;

    java.text.DecimalFormat mTwoPointDf =new java.text.DecimalFormat("#0.00");
    private List<String> imgBannerList;
    private List<View> viewList;
    private Intent intent;
    private String goodsId;
    private String type = "";
    private SparseArray<Integer> mCurSpecSpArr = new SparseArray<>();
    private int mCurSelectNumb = 1;
    private String shopId = "";
    private String mStoreNumb = null;

    private MyWebChromeClient mWebChromeClient;

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
                ShopGiftPopupView shopGiftPopupView = new ShopGiftPopupView(CommodityDetailsActivity.this,mDataBean.getId());
                shopGiftPopupView.showPopupWindow();
            }
        });
        setImgBackLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurState == 1) {
                    setTvTitleText("商品详情");
                    setImgRightVisiable(View.VISIBLE);
                    mCurState = 0;
                    pop();
                } else
                    AppManager.getInstance().finishActivity(CommodityDetailsActivity.this);
            }
        });
        rl_detailsline_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebPopupView webPopupView = new WebPopupView(CommodityDetailsActivity.this, ApiService.YYWEB + Constant.WEB_TAG.GET_INT_BY_MALL);
                webPopupView.setTvTitle("购物得积分");
                webPopupView.showPopupWindow();
            }
        });
        rl_detailsline_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebPopupView webPopupView = new WebPopupView(CommodityDetailsActivity.this, ApiService.YYWEB + Constant.WEB_TAG.GET_EXP_BY_MALL);
                webPopupView.setTvTitle("购物得经验");
                webPopupView.showPopupWindow();
            }
        });
        mRlBaseServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if (mDataBean.getSellerId() == 0){
                    url = ApiService.YYWEB + Constant.WEB_TAG.ZP_MALL_SELF;
                }else{
                    url = ApiService.YYWEB + Constant.WEB_TAG.ZP_MALL;
                }
                WebPopupView webPopupView = new WebPopupView(CommodityDetailsActivity.this, url);
                webPopupView.setTvTitle("基本服务");
                webPopupView.showPopupWindow();
            }
        });
        mLlCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                    return;
                }
                if (mDataBean == null) return;
                YYMallApi.addCollectShpp(CommodityDetailsActivity.this, new String[]{goodsId}, new YYMallApi.ApiResult<CommonBean>(CommodityDetailsActivity.this) {
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

        mRlPopArgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataBean != null) {
                    ShopArgsPopupView shopArgsPopupView = new ShopArgsPopupView<ShopDetailsBean.DataBean.AttrBean>(CommodityDetailsActivity.this, mDataBean.getAttr()) {
                        @Override
                        protected void bind(ViewHolder holder, ShopDetailsBean.DataBean.AttrBean attrBean, int position) {
                            holder.setText(R.id.isa_tv_key, attrBean.getName());
                            holder.setText(R.id.isa_tv_value, attrBean.getValue());
                        }
                    };
                    shopArgsPopupView.showPopupWindow();
                }
            }
        });

        mLlComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataBean == null) return;
                setTvTitleText("全部评价(" + mDataBean.getComment().getCount() + ")");
                setImgRightVisiable(View.INVISIBLE);
                mCurState = 1;
                loadRootFragment(R.id.ald_fl_comment, CommentFragment.getInstance(mDataBean.getId(), -1, null).setOnFinishListen(CommodityDetailsActivity.this));
            }
        });
    }

    @Override
    public void onFinish() {
        setTvTitleText("商品详情");
        setImgRightVisiable(View.VISIBLE);
        if (as_allflowlayouts != null)
            as_allflowlayouts.getAdapter().clear();
        mCurState = 0;
    }

    private int mCurState = 0; //0详情 1评论

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commoditydetails);
        setOnResumeRegisterBus(true);
    }

    private ShopDetailsBean.DataBean mDataBean;

    @Override
    protected void initView() {
        super.initView();
        setTitleWireVisiable(GONE);
        yvp_detailsline.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                WindowManager wm = (WindowManager) CommodityDetailsActivity.this
                        .getSystemService(Context.WINDOW_SERVICE);
                Point outSize = new Point();
                wm.getDefaultDisplay().getSize(outSize);
                yvp_detailsline.getLayoutParams().height = outSize.x;
            }
        });
        yvp_detailsline.initIndicator();
        yvp_detailsline.getIndicator().setMargin(0,0,0,20);
        yvp_detailsline.getIndicator().setOrientation(UltraViewPager.Orientation.HORIZONTAL);
        yvp_detailsline.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        yvp_detailsline.getIndicator().setFocusResId(0).setNormalResId(0);
        yvp_detailsline.getIndicator().setFocusColor(getResources().getColor(R.color.theme_bule)).setNormalColor(getResources().getColor(R.color.halfgraybg))
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        yvp_detailsline.getIndicator().build();
    }

    @Override
    protected void initData() {
        setTvTitleText("商品详情");
        setImgBackVisiable(View.VISIBLE);
        setImgRightResource(R.mipmap.details_dian);
        setImgRightVisiable(View.VISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        ll_commoditydetails_kf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), MyServiceActivity.class);
                startActivity(intent);
            }
        });

        intent = getIntent();
        goodsId = intent.getStringExtra("goodsId");

        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DetailsMenuPopupView(CommodityDetailsActivity.this).setOnMenuClickLisiten(new DetailsMenuPopupView.OnMenuClickLisiten() {
                    @Override
                    public void onMenuClick(int pos) {
                        if (pos == 1) {
//                            showShareDialog();

                            new ShareAction(CommodityDetailsActivity.this)
                                    .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                                            SHARE_MEDIA.QZONE)
                                    .setCallback(shareListener)
                                    .setShareboardclickCallback(new ShareBoardlistener() {
                                        @Override
                                        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                            String url = SHARE_SHOP_URL + "#" + mDataBean.getId();
                                            if (share_media == SHARE_MEDIA.SINA) {
                                                if (CommonUtil.isWeiboClientAvailable(CommodityDetailsActivity.this)){
                                                    UMImage image;
                                                    if (mDataBean.getPhoto() !=null && mDataBean.getPhoto().size() > 0)
                                                        image = new UMImage(CommodityDetailsActivity.this, mDataBean.getPhoto().get(0));  //缩略图
                                                    else
                                                        image = new UMImage(CommodityDetailsActivity.this, R.mipmap.ic_nor_srcpic);  //缩略图
                                                    new ShareAction(CommodityDetailsActivity.this).setPlatform(SHARE_MEDIA.SINA).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧："+mDataBean.getName()+url).withMedia(image).setCallback(shareListener).share();
                                                }else{
                                                    showToast("请先安装新浪微博");
                                                }
                                            } else {
                                                UMWeb web = new UMWeb(url);
                                                web.setTitle(mDataBean.getName());//标题
                                                if (mDataBean.getPhoto() != null && mDataBean.getPhoto().size() > 0)
                                                    web.setThumb(new UMImage(CommodityDetailsActivity.this, mDataBean.getPhoto().get(0)));  //缩略图
                                                else
                                                    web.setThumb(new UMImage(CommodityDetailsActivity.this, R.mipmap.ic_nor_srcpic));  //缩略图
                                                web.setDescription("我在YiYiYaYa发现了一个不错的商品，快来看看吧");//描述
                                                new ShareAction(CommodityDetailsActivity.this).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧").withMedia(web).
                                                        setCallback(shareListener).setPlatform(share_media).share();
                                            }
                                        }
                                    })
                                    .open();
                        } else if (pos == 2) {
                            //消息
                            if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                                showToast("请先登录");
                                startActivity(LoginActivity.class);
                                return;
                            }
                            startActivity(NewMessageActivity.class);
                        } else if (pos == 3) {
                            //反馈
                            if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
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
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        web_commoditydetails.onResume();
        web_commoditydetails.resumeTimers();
        // 设置为横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        web_commoditydetails.onPause();
    }

    @Override
    public Activity getActivity() {
        return this;
    }

    private void initWebView() {
//        mProgressBar.setVisibility(View.VISIBLE);

        WebSettings ws = web_commoditydetails.getSettings();
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
        web_commoditydetails.setInitialScale(1);
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
        web_commoditydetails.setWebChromeClient(mWebChromeClient);
        // 与js交互
        web_commoditydetails.addJavascriptInterface(new ImageClickInterface(this), "injectedObject");
        web_commoditydetails.setWebViewClient(new MyWebViewClient(this));
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
        web_commoditydetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindWebView() {
        web_commoditydetails.setVisibility(View.INVISIBLE);
    }

    @Override
    public void fullViewAddView(View view) {
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        mFlViewdeoContainer = new FullscreenHolder(CommodityDetailsActivity.this);
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
        web_commoditydetails.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                //  "objs[i].onclick=function(){alert(this.getAttribute(\"has_link\"));}" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
                "}" +
                "})()");

        // 遍历所有的a节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
        web_commoditydetails.loadUrl("javascript:(function(){" +
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
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    @Override
    public void onBackPressedSupport() {
        if (mWebChromeClient !=null && mWebChromeClient.inCustomView()) {
            hideCustomView();
        }else{
            super.onBackPressedSupport();
        }
    }
    private int mTagSelect;
    private void getData() {
        YYMallApi.getShopInfo(CommodityDetailsActivity.this, goodsId, 0,new YYMallApi.ApiResult<ShopDetailsBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                if (e.getCode() == 6005 || e.getCode() == 8001) {
//                    setNoDataView(R.mipmap.ic_nor_orderbg, "商品已失效,逛逛特卖吧", "逛逛特卖");
//                    setNoDataBtnLisiten(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            YYApp.getInstance().setIndexShow(0);
//                            startActivity(MainActivity.class);
//                            AppManager.getInstance().finishExceptActivity(MainActivity.class);
//                        }
//                    });
                    replaceCustomView(new GoodsGoneLayout(CommodityDetailsActivity.this).setLoadLisiten(new GoodsGoneLayout.OnLoadDoneLisiten() {
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
                } else {
                    setNetWorkErrShow(VISIBLE);
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(final ShopDetailsBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                mDataBean = dataBean;
                mIsSale = dataBean.getIsSale();
                storeVerify();
                saleVerify();
                imgBannerList = new ArrayList<>();
                viewList = new ArrayList<>();

                //满减
                String curStr = mDataBean.getCut();
                if (TextUtils.isEmpty(curStr)){
                    mRlCut.setVisibility(GONE);
                }else{
                    mTvCut.setText(curStr);
                }

                if (dataBean.getIsGift() == 1){
                    //有赠品
                    mLlGetGif.setVisibility(VISIBLE);
                    if (dataBean.getIsActivityGift() == 0){
                        mTvGiftDesc.setText("正价购买赠赠品（赠完为止）");
                    }else{
                        mTvGiftDesc.setText("购买即赠赠品（赠完为止）");
                    }
                }else{
                    mLlGetGif.setVisibility(GONE);
                }

                if (dataBean.getVideo().getImg() == null || dataBean.getVideo().getImg().equals("")) {

                } else {
                    imgBannerList.add(dataBean.getVideo().getImg());
                }
                for (int i = 0; i < dataBean.getPhoto().size(); i++) {
                    imgBannerList.add(dataBean.getPhoto().get(i));
                }
                buildBannerBean(dataBean);
                mTvYyTip.setText("(正价购买商品最多可用" + dataBean.getMaxYY() + "丫丫)");
                for (int i = 0; i < imgBannerList.size(); i++) {
                    viewList.add(LayoutInflater.from(CommodityDetailsActivity.this).inflate(R.layout.view_banner_item, null));
                }
                PagerAdapters adapters = new PagerAdapters(imgBannerList, viewList);
                yvp_detailsline.setAdapter(adapters);
                tv_detailsline_name.setText(dataBean.getName() + "");
                tv_detailsline_yprice.setText("原价" + mTwoPointDf.format(Double.parseDouble(dataBean.getMarketPrice())));
                tv_detailsline_yprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_detailsline_price.setText("¥" + mTwoPointDf.format(Double.parseDouble(dataBean.getPrice())));
                tv_detailsline_xiaol.setText("已售" + dataBean.getSale() + "件");

                if (dataBean.getSellerId() == 0){
                    mTvSeller.setText("自营");
                }else{
                    mTvSeller.setText("商户");
                }

                if (!TextUtils.isEmpty(dataBean.getDescription()))
                    mTvShopDesc.setText(dataBean.getDescription());
                else
                    mTvShopDesc.setVisibility(GONE);

//                if (dataBean.getPoint() == 0 && dataBean.getExp() == 0)
//                    mLlGetScore.setVisibility(GONE);
//                else {
//                    tv_detailsline_point.setText("购物得" + dataBean.getPoint() + "积分");
//                    tv_detailsline_exp.setText("购物得" + dataBean.getExp() + "经验");
//                }
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


                if (dataBean.getComment().getList() != null && dataBean.getComment().getList().size() != 0) {
                    mTvNoComment.setVisibility(GONE);
                    mLlCmfirst.setVisibility(VISIBLE);
                    tv_detailsline_user.setText("" + dataBean.getComment().getList().get(0).getUser());
                    //Glide.with(mContext).load(lists.get(position)).into((ImageView) viewLists.get(position).findViewById(R.id.vb_img));
//                    Glide.with(CommodityDetailsActivity.this).load(dataBean.getComment().getList().get(0).getUserico()).placeholder(R.mipmap.ic_nor_srcheadimg).into(img_detailline_useric);
                    Glide.with(CommodityDetailsActivity.this).load(dataBean.getComment().getList().get(0).getUserico()).asBitmap().centerCrop().placeholder(R.mipmap.ic_nor_srcheadimg)
                            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new BitmapImageViewTarget(img_detailline_useric) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            img_detailline_useric.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    tv_detailsline_connet.setText("" + dataBean.getComment().getList().get(0).getContents());
                    tv_detailsline_spec.setText("" + dataBean.getComment().getList().get(0).getSpec());
                } else {
                    mLlCmfirst.setVisibility(GONE);
                    mTvNoComment.setVisibility(VISIBLE);
                }
                tv_detailsline_pingj.setText("产品评价(" + dataBean.getComment().getCount() + ")");

                String guige = "请选择商品";
                for (int i = 0; i < dataBean.getSpec().size(); i++) {
                    if (i < dataBean.getSpec().size() - 1) {
                        guige = guige + dataBean.getSpec().get(i).getName() + "，";
                    } else {
                        guige = guige + dataBean.getSpec().get(i).getName();
                    }
                }
                tv_detailsline_standard.setText(guige);
                String[] mTestVal = new String[dataBean.getComment().getType().size()];
                for (int i = 0; i < dataBean.getComment().getType().size(); i++) {
                    mTestVal[i] = dataBean.getComment().getType().get(i).getName() + "(" + dataBean.getComment().getType().get(i).getCount() + ")";
                }
                as_allflowlayouts.setAdapter(new TagAdapter<String>(mTestVal) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        TextView tv = (TextView) LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_flow_tv,
                                as_allflowlayouts, false);
                        tv.setText(s);
                        return tv;
                    }
                });
                as_allflowlayouts.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        if (mDataBean == null) return false;
                        setTvTitleText(tv_detailsline_pingj.getText().toString());
                        mTagSelect = position + 4;
                        setImgRightVisiable(View.INVISIBLE);
                        mCurState = 1;
                        loadRootFragment(R.id.ald_fl_comment, CommentFragment.getInstance(mDataBean.getId(), mTagSelect, (Integer) view.getTag()).setOnFinishListen(CommodityDetailsActivity.this));
                        return true;
                    }
                });
                as_allflowlayouts.setMaxSelectCount(1);
//                web_commoditydetails.loadUrl("https://github.com/ysnows");
                initWebView();
                web_commoditydetails.loadUrl(ApiService.SHOP_DETAIL + "#" + dataBean.getId());

                tv_commodity_addshopcar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStoreNone || mIsSale ==1 ) return;
                        if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                            startActivity(LoginActivity.class);
                            showToast("请先登录");
                            return;
                        }
                        new CommonShopDetailsPopupView(CommodityDetailsActivity.this, dataBean, goodsId,true).showPopupWindow();
                    }
                });
                rl_detailsline_standard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStoreNone || mIsSale ==1 ) return;
                        if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                            startActivity(LoginActivity.class);
                            showToast("请先登录");
                            return;
                        }
                        new CommonShopDetailsPopupView(CommodityDetailsActivity.this, dataBean, goodsId,null).showPopupWindow();
                    }
                });

                tv_commodity_buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ( mStoreNone || mIsSale ==1) return;
                        if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                            startActivity(LoginActivity.class);
                            showToast("请先登录");
                            return;
                        }
                        if (mCurSpecSpArr.size() == 0) {
                            new CommonShopDetailsPopupView(CommodityDetailsActivity.this, dataBean, goodsId,false).showPopupWindow();
                            return;
                        }
                        if (mStoreNumb == null) return;
                        if ( mStoreNumb.equals("0")) {
                            showToast("库存不足，请选择其他规格。");
                        } else {
                            intent = new Intent(mContext, CheckOutActivity.class);
                            intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.COMMONSHOP);
                            intent.putExtra("productId", shopId);
                            intent.putExtra("nums", mCurSelectNumb + "");
                            mContext.startActivity(intent);
                        }
                    }
                });

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
            tv_commodity_addshopcar.setTextColor(getResources().getColor(R.color.half_transparency));
            tv_commodity_buy.setTextColor(getResources().getColor(R.color.half_transparency));
            tv_detailsline_standard.setTextColor(getResources().getColor(R.color.grayfont_1_5));
        }
    }
    private int mIsSale; //1为不可销售，0为可销售
    private void saleVerify(){
        if (mIsSale == 1){
            mTvStoreNone.setVisibility(VISIBLE);
            mTvStoreNone.setText("该商品暂不支持购买");
            tv_commodity_addshopcar.setTextColor(getResources().getColor(R.color.half_transparency));
            tv_commodity_buy.setTextColor(getResources().getColor(R.color.half_transparency));
            tv_detailsline_standard.setTextColor(getResources().getColor(R.color.grayfont_1_5));
        }
    }

    class PagerAdapters extends PagerAdapter {
        List<String> lists = new ArrayList<>();
        List<View> viewLists = new ArrayList<>();

        /**
         * Return the number of views available.
         */
        PagerAdapters(List<String> list, List<View> viewList) {
            this.lists = list;
            this.viewLists = viewList;
        }

        @Override
        public int getCount() {
            return viewLists.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {

            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            ImageView imgview = (ImageView) viewLists.get(position).findViewById(R.id.vb_img);
            imgview.setScaleType(ImageView.ScaleType.FIT_XY);
            Glide.with(mContext).load(lists.get(position)).placeholder(R.mipmap.ic_nor_srcpic).into(imgview);
            container.addView(viewLists.get(position));
            viewLists.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(CommodityDetailsActivity.this, ShopBannerActivity.class);
                    intent.putParcelableArrayListExtra(Constant.BANNER.ITEMBEAN, mBannerItemBean);
                    intent.putExtra(Constant.BANNER.POSITION, position);
                    startActivity(intent);
                }
            });
            return viewLists.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem(container, position, object);
            container.removeView(viewLists.get(position));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
//        web_commoditydetails.onDestroy();
        UMShareAPI.get(this).release();
        mFlViewdeoContainer.removeAllViews();
        if (web_commoditydetails != null) {
            ViewGroup parent = (ViewGroup) web_commoditydetails.getParent();
            if (parent != null) {
                parent.removeView(web_commoditydetails);
            }
            web_commoditydetails.removeAllViews();
            web_commoditydetails.loadUrl("about:blank");
            web_commoditydetails.stopLoading();
            web_commoditydetails.setWebChromeClient(null);
            web_commoditydetails.setWebViewClient(null);
            web_commoditydetails.destroy();
            web_commoditydetails = null;
        }
    }


    class CommonShopDetailsPopupView extends BasePopupWindow implements View.OnClickListener {

        private Context mContext;
        private TextView vps_tv_shopprice, vps_tv_repertory, tv_popshopdetails_addshopcar, tv_popshopdetails_buy;
        private RecyclerView vps_rv;
        private DetailsPopAdapter detailsPopAdapter;
        private String goodsIds;
        private HashMap<String, String> hashMap;
        private ShopDetailsBean.DataBean sdataBeans;
        private ImageView vps_img_shop;
        private ImageView bt_popdetails_close;
        private ProgressBar mProgress;
        private NormsBean.DataBean mSpecBean;
        private DetailsPopAdapter.DetailsPopCall calls = new DetailsPopAdapter.DetailsPopCall() {
            @Override
            public void send(SparseArray<Integer> list) {
                hashMap = new HashMap<>();
//                mCurSelectNumb = 1;
                mCurSpecSpArr = list;
                type = "";
                for (int i = 0; i < sdataBeans.getSpec().size(); i++) {
                    hashMap.put(sdataBeans.getSpec().get(i).getId() + "", sdataBeans.getSpec().get(i).getValue().get(mCurSpecSpArr.get(i)).getName() + "");
                    type = type + sdataBeans.getSpec().get(i).getValue().get(mCurSpecSpArr.get(i)).getName() + ",";
                }
                tv_detailsline_standard.setText("已选择：" + type);
                HashMap hashMaps = new HashMap();
                hashMaps.put("spec", hashMap);
                String jsonStr = new Gson().toJson(hashMaps);
                YYMallApi.getShopSpec(mContext, goodsIds, jsonStr, true, new YYMallApi.ApiResult<NormsBean.DataBean>(mContext) {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onError(ApiException e) {
                        super.onError(e);
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(NormsBean.DataBean dataBean) {
                        mSpecBean = dataBean;
                        Glide.with(mContext).load(dataBean.getImg()).into(vps_img_shop);
                        mStoreNumb = dataBean.getStoreNum() + "";
                        vps_tv_repertory.setText("库存" + dataBean.getStoreNum() + "件");
                        vps_tv_shopprice.setText("¥" + mTwoPointDf.format(dataBean.getPrice()));
                        tv_detailsline_price.setText("¥" + mTwoPointDf.format(dataBean.getPrice()));
                        mTvYyTip.setText("(正价购买商品最多可用" + dataBean.getYayaLimit() + "丫丫)");
                        shopId = dataBean.getId() + "";
                        List<ShopDetailsBean.DataBean.SpecBean> list = sdataBeans.getSpec();
                        detailsPopAdapter = new DetailsPopAdapter(mContext, list, mCurSpecSpArr, calls, mCurSelectNumb, dataBean.getStoreNum(),dataBean.getAllowMaxNum(),dataBean.getLimitnum());
                        vps_rv.setLayoutManager(new LinearLayoutManager(mContext));
                        vps_rv.setAdapter(detailsPopAdapter);
                    }
                });
            }

            @Override
            public void send(int numbs) {
                mCurSelectNumb = numbs;
            }
        };

        public CommonShopDetailsPopupView(final Activity context, ShopDetailsBean.DataBean dataBeans, String goodsId,final Boolean leftOrRight) {
            super(context);
            this.mContext = context;
            this.goodsIds = goodsId;
            this.sdataBeans = dataBeans;

            mProgress = (ProgressBar) findViewById(R.id.view_progress);
            vps_tv_shopprice = (TextView) findViewById(R.id.vps_tv_shopprice);
            vps_rv = (RecyclerView) findViewById(R.id.vps_rv);
            vps_img_shop = (ImageView) findViewById(R.id.vps_img_shop);
            vps_tv_repertory = (TextView) findViewById(R.id.vps_tv_repertory);
            bt_popdetails_close = (ImageView) findViewById(R.id.bt_popdetails_close);
            tv_popshopdetails_addshopcar = (TextView) findViewById(R.id.tv_popshopdetails_addshopcar);
            tv_popshopdetails_buy = (TextView) findViewById(R.id.tv_popshopdetails_buy);
            bt_popdetails_close.setOnClickListener(this);
            tv_popshopdetails_addshopcar.setOnClickListener(this);
            tv_popshopdetails_buy.setOnClickListener(this);



            hashMap = new HashMap<>();
            type = "";
            if (mCurSpecSpArr.size() < 0 || mCurSpecSpArr.size() == 0) {
                for (int i = 0; i < sdataBeans.getSpec().size(); i++) {
                    hashMap.put(sdataBeans.getSpec().get(i).getId() + "", sdataBeans.getSpec().get(i).getValue().get(0).getName() + "");
                    mCurSpecSpArr.put(i,0);
                    type = type + sdataBeans.getSpec().get(i).getValue().get(0).getName() + ",";
                }
            } else {
                for (int i = 0; i < sdataBeans.getSpec().size(); i++) {
                    hashMap.put(sdataBeans.getSpec().get(i).getId() + "", sdataBeans.getSpec().get(i).getValue().get(mCurSpecSpArr.get(i)).getName() + "");
                    type = type + sdataBeans.getSpec().get(i).getValue().get(mCurSpecSpArr.get(i)).getName() + ",";
                }
            }

            tv_detailsline_standard.setText("已选择：" + type);
            HashMap hashMaps = new HashMap();
            hashMaps.put("spec", hashMap);
            String jsonStr = new Gson().toJson(hashMaps);
            YYMallApi.getShopSpec(mContext, goodsIds, jsonStr,false,  new YYMallApi.ApiResult<NormsBean.DataBean>(mContext) {
                @Override
                public void onStart() {
                }

                @Override
                public void onError(ApiException e) {
                    super.onError(e);
                    showToast(e.getMessage());
                    mProgress.setVisibility(GONE);
                }

                @Override
                public void onCompleted() {
                }

                @Override
                public void onNext(NormsBean.DataBean dataBean) {
                    mProgress.setVisibility(GONE);
                    mSpecBean = dataBean;
                    if (leftOrRight == null){
                        //none
                        tv_popshopdetails_addshopcar.setVisibility(VISIBLE);
                        tv_popshopdetails_buy.setVisibility(VISIBLE);
                    }else if (leftOrRight){
                        //left
                        tv_popshopdetails_addshopcar.setVisibility(VISIBLE);
                    }else {
                        //right
                        tv_popshopdetails_buy.setVisibility(VISIBLE);
                    }

                    Glide.with(mContext).load(dataBean.getImg()).into(vps_img_shop);
                    vps_tv_repertory.setText("库存" + dataBean.getStoreNum() + "件");
                    mStoreNumb = dataBean.getStoreNum() + "";
                    vps_tv_shopprice.setText("¥" + mTwoPointDf.format(dataBean.getPrice()));
                    tv_detailsline_price.setText("¥" + mTwoPointDf.format(dataBean.getPrice()));
                    mTvYyTip.setText("(正价购买商品最多可用" + dataBean.getYayaLimit() + "丫丫)");
                    shopId = dataBean.getId() + "";
                    List<ShopDetailsBean.DataBean.SpecBean> list = sdataBeans.getSpec();
                    detailsPopAdapter = new DetailsPopAdapter(mContext, list, mCurSpecSpArr, calls, mCurSelectNumb, dataBean.getStoreNum(),dataBean.getAllowMaxNum(),dataBean.getLimitnum());
                    vps_rv.setLayoutManager(new LinearLayoutManager(mContext));
                    vps_rv.setAdapter(detailsPopAdapter);
                }
            });
        }

        @Override
        protected Animation initShowAnimation() {
            Animation animation = getTranslateAnimation(500 * 2, 0, 300);
            return animation;
        }

        @Override
        public View getClickToDismissView() {
            return getPopupWindowView();
        }

        @Override
        public View onCreatePopupView() {
            return createPopupById(R.layout.view_pop_shopdetails);
        }

        @Override
        public View initAnimaView() {
            return findViewById(R.id.vps_rl_popview);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bt_popdetails_close:
                    dismiss();
                    break;
                case R.id.tv_popshopdetails_addshopcar:
                    if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                        showToast("请先登录");
                        startActivity(LoginActivity.class);
                        return;
                    }
                    if (!TextUtils.isEmpty(mStoreNumb) && mStoreNumb.equals("0")) {
                        showToast("库存不足，请选择其他规格。");
                    } else {
                        YYMallApi.getAddCar(mContext, shopId, mCurSelectNumb, new YYMallApi.ApiResult<CommonBean>(getContext()) {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onError(ApiException e) {
                                super.onError(e);
                                showToast(e.getMessage());
                            }

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onNext(CommonBean commonBean) {
                                showToast("加入购物车成功");
//                                YYApp.getInstance().setUiUpdateTag(R.layout.fragment_shopcar, true);
                                dismiss();
                            }
                        });
                    }
                    break;

                case R.id.tv_popshopdetails_buy:
                    if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                        showToast("请先登录");
                        startActivity(LoginActivity.class);
                        return;
                    }
                    if (!TextUtils.isEmpty(mStoreNumb) && mStoreNumb.equals("0")) {
                        showToast("库存不足，请选择其他规格。");
                    } else {
                        if (mSpecBean.getLimitnum() !=0 && mCurSelectNumb > mSpecBean.getAllowMaxNum()){
                            showToast("超过最大可购买数量");
                            return;
                        }
                        intent = new Intent(mContext, CheckOutActivity.class);
                        intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.COMMONSHOP);
                        intent.putExtra("productId", shopId);
                        intent.putExtra("nums", mCurSelectNumb + "");
                        mContext.startActivity(intent);
                        dismiss();
                    }
                    break;

                default:
                    break;

            }
        }
    }

    ArrayList<BannerItemBean> mBannerItemBean;

    public void buildBannerBean(ShopDetailsBean.DataBean dataBean) {
        mBannerItemBean = new ArrayList<>();
        if (dataBean.getVideo() != null && !TextUtils.isEmpty(dataBean.getVideo().getUrl()) && !TextUtils.isEmpty(dataBean.getVideo().getImg())) {
            mBannerItemBean.add(new BannerItemBean(Constant.BANNER.VIDEO, dataBean.getVideo().getUrl(), dataBean.getVideo().getImg()));
        }
        for (int i = 0; i < dataBean.getPhoto().size(); i++) {
            mBannerItemBean.add(new BannerItemBean(Constant.BANNER.IMG, null, dataBean.getPhoto().get(i)));
        }
    }
}
