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
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.BannerItemBean;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.GrouponSubBean;
import com.yhkj.yymall.bean.ShopDetailsBean;
import com.yhkj.yymall.bean.ShopSpecBean;
import com.yhkj.yymall.config.FullscreenHolder;
import com.yhkj.yymall.config.IWebPageView;
import com.yhkj.yymall.config.JSInterface;
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
import com.yhkj.yymall.view.popwindows.GroupCarPopupView;
import com.yhkj.yymall.view.popwindows.ShopArgsPopupView;
import com.yhkj.yymall.view.popwindows.ShopGiftPopupView;
import com.yhkj.yymall.view.popwindows.WebPopupView;
import com.yhkj.yymall.view.viewpager.PagerAdapter;
import com.yhkj.yymall.view.viewpager.UltraViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vise.utils.handler.HandlerUtil.runOnUiThread;
import static com.yhkj.yymall.http.api.ApiService.SHARE_SHOP_URL;

/**
 * Created by Administrator on 2017/7/25.
 */

public class GrouponDetailsActivity extends BaseToolBarActivity implements CommentFragment.OnFinishListen,IWebPageView,
        GroupCarPopupView.OnShopCarResLisiten{


    @Bind(R.id.ll_groupondetails_has)
    LinearLayout ll_groupondetails_has;

    @Bind(R.id.as_allflowlayouts)
    TagFlowLayout as_allflowlayouts;

    @Bind(R.id.yvp_groupondetails)
    UltraViewPager yvp_groupondetails;

    @Bind(R.id.tv_groupondetails_name)
    TextView tv_groupondetails_name;

    @Bind(R.id.tv_groupondetails_price)
    TextView tv_groupondetails_price;

    @Bind(R.id.tv_groupondetails_yprice)
    TextView tv_groupondetails_yprice;

    @Bind(R.id.tv_groupondetails_xiaol)
    TextView tv_groupondetails_xiaol;

    @Bind(R.id.tv_detailsline_point)
    TextView tv_groupondetails_point;

    @Bind(R.id.rl_detailsline_point)
    RelativeLayout rl_groupondetails_point;

    @Bind(R.id.acd_ll_getscore)
    LinearLayout mLlGetScore;


    @Bind(R.id.rl_detailsline_exp)
    RelativeLayout rl_groupondetails_exp;


    @Bind(R.id.tv_detailsline_exp)
    TextView tv_groupondetails_exp;

    @Bind(R.id.tv_groupondetails_pingj)
    TextView tv_groupondetails_pingj;

    @Bind(R.id.tv_detailsline_user)
    TextView tv_groupondetails_user;

    @Bind(R.id.img_detailline_useric)
    ImageView img_groupondetails_useric;

    @Bind(R.id.tv_detailsline_connet)
    TextView tv_groupondetails_connet;

    @Bind(R.id.tv_detailsline_spec)
    TextView tv_groupondetails_spec;

    @Bind(R.id.tv_groupondetails_standard)
    TextView tv_groupondetails_standard;

//    @Bind(R.id.web_groipondetails)
//    AdvancedWebView web_groipondetails;

    @Bind(R.id.web_groipondetails)
    WebView web_groipondetails;

    @Bind(R.id.videoContainer)
    FrameLayout mFlViewdeoContainer;

    @Bind(R.id.rl_groupondetails_standard)
    RelativeLayout rl_groupondetails_standard;

    @Bind(R.id.tv_groupondetails_gname)
    TextView tv_groupondetails_gname;

    @Bind(R.id.tv_groupondetails_gfrom)
    TextView tv_groupondetails_gfrom;

    @Bind(R.id.tv_groupondetails_gperson)
    TextView tv_groupondetails_gperson;

    @Bind(R.id.tv_groupondetails_buy)
    LinearLayout tv_groupondetails_buy;

    @Bind(R.id.img_groupondetails_heads)
    ImageView img_groupondetails_heads;

    @Bind(R.id.tv_groupondetails_tnumb)
    TextView tv_groupondetails_tnumb;

    @Bind(R.id.tv_groupondetails_dbuy)
    TextView tv_groupondetails_dbuy;

    @Bind(R.id.tv_groupondetails_tbuy)
    TextView tv_groupondetails_tbuy;

    @Bind(R.id.ald_tv_shopdesc)
    TextView mTvShopDesc;

    @Bind(R.id.tv_commodity_add)
    LinearLayout tv_commodity_add;

    @Bind(R.id.tv_groupondetails_offered)
    TextView tv_groupondetails_offered;

    @Bind(R.id.ll_groupondetails_kf)
    LinearLayout ll_groupondetails_kf;

    @Bind(R.id.agd_ll_collect)
    LinearLayout mLlCollect;

    @Bind(R.id.al_ll_comment)
    LinearLayout mLlComment;


    @Bind(R.id.ac_tv_nocomment)
    TextView mTvNoComment;

    @Bind(R.id.ac_ll_cmfirst)
    LinearLayout mLlCmfirst;


    @Bind(R.id.ald_rl_popargs)
    RelativeLayout mRlPopArgs;

    @Bind(R.id.ag_tv_yytip)
    TextView mTvYyTip;

    @Bind(R.id.ag_rl_grouprule)
    RelativeLayout mRlGroupRule;

    @Bind(R.id.ag_rl_groupxy)
    RelativeLayout mRlGroupXieYi;

    @Bind(R.id.ald_tv_storenone)
    TextView mTvStoreNone;

    @Bind(R.id.ag_tv_buystr)
    TextView mTvBuyStr;

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

    private MyWebChromeClient mWebChromeClient;

    private int time;
    private String mCurTimeStr;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 != 0) {
                String timestr = formatTime(msg.arg1);
                if (TextUtils.isEmpty(mCurTimeStr) || !mCurTimeStr.equals(timestr)) {
                    mCurTimeStr = timestr;
                    setTvTitleText("距离结束仅剩\n" + mCurTimeStr);
                }
            } else {
                setTvTitleText("距离结束仅剩\n00天00时00分");
            }
        }
    };


    private List<String> imgBannerList;
    private List<View> viewList;
    private Intent intent;
    private String goodsId;
    private Thread thread;
    private ApiCallback apiCallback = new ApiCallback<GrouponSubBean.DataBean>() {
        @Override
        public void onStart() {

        }

        @Override
        public void onError(ApiException e) {
            showToast(e.getMessage());
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onNext(GrouponSubBean.DataBean dataBean) {
            if (dataBean.getResult() != 0){
                //0 操作成功 1 操作失败 2 库存不足 3 拼团活动已结束 4 购买数量已达上限 5 本商品不可重复拼团
                switch (dataBean.getResult()){
                    case 1:
                        showToast("操作失败");
                        break;
                    case 2:
                        showToast("库存不足");
                        break;
                    case 3:
                        showToast("拼团活动已结束");
                        break;
                    case 4:
                        showToast("购买数量已达上限");
                        break;
                    case 5:
                        showToast("本商品不可重复拼团");
                        break;
                }
                return;
            }
            intent = new Intent(mContext, CheckOutActivity.class);
            intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.GROUPON);
            intent.putExtra("groupUserId", dataBean.getGroupUserId() + "");
            intent.putExtra("nums", mSelectNumb + "");
            intent.putExtra("productId", String.valueOf(mCurSpecBean.getId()));
            mContext.startActivity(intent);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        web_groipondetails.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        web_groipondetails.onPause();
        web_groipondetails.resumeTimers();
        // 设置为横屏
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }


    private void initWebView() {
//        mProgressBar.setVisibility(View.VISIBLE);
        if (web_groipondetails == null) return;
        WebSettings ws = web_groipondetails.getSettings();
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
        web_groipondetails.setInitialScale(1);
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
        web_groipondetails.setWebChromeClient(mWebChromeClient);
        // 与js交互
//        web_groipondetails.addJavascriptInterface(new JSInterface(this), "injectedObject");
        web_groipondetails.setWebViewClient(new MyWebViewClient(this));
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
        web_groipondetails.setVisibility(View.VISIBLE);
    }

    @Override
    public void hindWebView() {
        web_groipondetails.setVisibility(View.INVISIBLE);
    }

    @Override
    public void fullViewAddView(View view) {
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        mFlViewdeoContainer = new FullscreenHolder(GrouponDetailsActivity.this);
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
        web_groipondetails.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName(\"img\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                //  "objs[i].onclick=function(){alert(this.getAttribute(\"has_link\"));}" +
                "objs[i].onclick=function(){window.injectedObject.imageClick(this.getAttribute(\"src\"),this.getAttribute(\"has_link\"));}" +
                "}" +
                "})()");

        // 遍历所有的a节点,将节点里的属性传递过去(属性自定义,用于页面跳转)
        web_groipondetails.loadUrl("javascript:(function(){" +
                "var objs =document.getElementsByTagName(\"a\");" +
                "for(var i=0;i<objs.length;i++)" +
                "{" +
                "objs[i].onclick=function(){" +
                "window.injectedObject.textClick(this.getAttribute(\"mSelectSpecStr\"),this.getAttribute(\"item_pk\"));}" +
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
                ShopGiftPopupView shopGiftPopupView = new ShopGiftPopupView(GrouponDetailsActivity.this,mDataBean.getId());
                shopGiftPopupView.showPopupWindow();
            }
        });
        mRlGroupRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebPopupView webPopupView =  new WebPopupView(GrouponDetailsActivity.this,ApiService.YYWEB + Constant.WEB_TAG.GROUP);
                webPopupView.setTvTitle("拼团规则");
                webPopupView.showPopupWindow();
            }
        });
        rl_groupondetails_point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebPopupView webPopupView =  new WebPopupView(GrouponDetailsActivity.this,ApiService.YYWEB + Constant.WEB_TAG.GET_INT_BY_MALL);
                webPopupView.setTvTitle("购物得积分");
                webPopupView.showPopupWindow();
            }
        });
        rl_groupondetails_exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WebPopupView webPopupView =  new WebPopupView(GrouponDetailsActivity.this,ApiService.YYWEB + Constant.WEB_TAG.GET_EXP_BY_MALL);
                webPopupView.setTvTitle("购物得经验");
                webPopupView.showPopupWindow();
            }
        });
        mRlGroupXieYi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url;
                if (mDataBean.getSellerId() == 0){
                    url = ApiService.YYWEB + Constant.WEB_TAG.ZP_MALL_SELF;
                }else{
                    url = ApiService.YYWEB + Constant.WEB_TAG.ZP_MALL;
                }
                WebPopupView webPopupView = new WebPopupView(GrouponDetailsActivity.this, url);
                webPopupView.setTvTitle("基本服务");
                webPopupView.showPopupWindow();
            }
        });
        mRlPopArgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataBean !=null) {
                    ShopArgsPopupView shopArgsPopupView = new ShopArgsPopupView<ShopDetailsBean.DataBean.AttrBean>(GrouponDetailsActivity.this, mDataBean.getAttr()) {
                        @Override
                        protected void bind(ViewHolder holder, ShopDetailsBean.DataBean.AttrBean attrBean, int position) {
                            holder.setText(R.id.isa_tv_key,attrBean.getName());
                            holder.setText(R.id.isa_tv_value,attrBean.getValue());
                        }
                    };
                    shopArgsPopupView.showPopupWindow();
                }
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
                YYMallApi.addCollectShpp(GrouponDetailsActivity.this, new String[]{goodsId}, new YYMallApi.ApiResult<CommonBean>(GrouponDetailsActivity.this) {
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

        mLlComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDataBean == null) return;
                setTvTitleText("全部评价(" + mDataBean.getComment().getCount() + ")");
                setImgRightVisiable(View.INVISIBLE);

                loadRootFragment(R.id.ald_fl_comment, CommentFragment.getInstance(mDataBean.getId(),-1,null).setOnFinishListen(GrouponDetailsActivity.this));
            }
        });
    }

    @Override
    public void onFinish() {
        setTvTitleText("商品详情");
        setImgRightVisiable(View.VISIBLE);

    }

    private int mTagSelect;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupondetails);
//        setOnResumeRegisterBus(true);
    }

    private ShopDetailsBean.DataBean mDataBean;

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData();
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData();
    }

    @Override
    protected void initData() {
        setTvTitleText("距离结束仅剩\n00天00时00分");
        setImgBackVisiable(View.VISIBLE);
        setImgRightResource(R.mipmap.details_dian);
        setImgRightVisiable(View.VISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        ll_groupondetails_kf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                    showToast("请先登录");
                    startActivity(LoginActivity.class);
                    return;
                }
//                Intent intent = new Intent(getApplicationContext(), MyServiceActivity.class);
//                startActivity(intent);
                Intent intent = new Intent();
                intent.setClass(GrouponDetailsActivity.this, ChatLoginActivity.class);
                intent.putExtra(Constant.INTENT_CODE_IMG_SELECTED_KEY,Constant.INTENT_CODE_IMG_SHOP);
                intent.putExtra("shopid",String.valueOf(mDataBean.getId()));
                intent.putExtra("shoptype", String.valueOf(Constant.SHOP_TYPE.GROUP));
                intent.putExtra("shopname",mDataBean.getName());
                intent.putExtra("shopprice","¥"+mTwoPointDf.format(Double.parseDouble(mDataBean.getGroup().getPrice())));
                intent.putExtra("shopdesc",mDataBean.getDescription());
                intent.putExtra("shopimg",mDataBean.getPhoto().get(0));
                startActivity(intent);
            }
        });

        intent = getIntent();
        goodsId = intent.getStringExtra("goodsId");

        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DetailsMenuPopupView(GrouponDetailsActivity.this).setOnMenuClickLisiten(new DetailsMenuPopupView.OnMenuClickLisiten() {
                    @Override
                    public void onMenuClick(int pos) {
                        if (pos == 1){
                            //分享
//                            com.sina.weibo.sdk.auth.AuthInfo authInfo = new AuthInfo(this, Constants.APP_KEY,
//                                    Constants.REDIRECT_URL, Constants.SCOPE);
//                            com.sina.weibo.sdk.auth.sso.SsoHandler ssoHandler = new SsoHandler(LoginWeiboActivity.this,authInfo);
//                            ssoHandler.authorizeWeb(new AuthListener());  //AuthListener 是SDK的回调接口
                            new ShareAction(GrouponDetailsActivity.this)
                                    .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE,
                                            SHARE_MEDIA.QZONE)
                                    .setCallback(shareListener)
                                    .setShareboardclickCallback(new ShareBoardlistener() {
                                        @Override
                                        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                            String url = SHARE_SHOP_URL + "#" + mDataBean.getId();
                                            UMWeb web = new UMWeb(url);
                                            web.setTitle(mDataBean.getName());//标题
                                            if (mDataBean.getPhoto() !=null && mDataBean.getPhoto().size() > 0)
                                                web.setThumb( new UMImage(GrouponDetailsActivity.this, mDataBean.getPhoto().get(0)));  //缩略图
                                            else
                                                web.setThumb( new UMImage(GrouponDetailsActivity.this, R.mipmap.ic_nor_srcpic));  //缩略图
                                            web.setDescription("我在YiYiYaYa发现了一个不错的商品，快来看看吧");//描述
                                            new ShareAction(GrouponDetailsActivity.this).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧").withMedia(web).setCallback(shareListener).setPlatform(share_media).share();
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

    }


    @Override
    protected void initView() {
        super.initView();
        setTitleWireVisiable(GONE);
        yvp_groupondetails.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                WindowManager wm = (WindowManager) GrouponDetailsActivity.this
                        .getSystemService(Context.WINDOW_SERVICE);
                Point outSize = new Point();
                wm.getDefaultDisplay().getSize(outSize);
                yvp_groupondetails.getLayoutParams().height = outSize.x;
            }
        });
        yvp_groupondetails.initIndicator();
        yvp_groupondetails.getIndicator().setMargin(0,0,0,20);
        yvp_groupondetails.getIndicator().setOrientation(UltraViewPager.Orientation.HORIZONTAL);
        yvp_groupondetails.getIndicator().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        yvp_groupondetails.getIndicator().setFocusResId(0).setNormalResId(0);
        yvp_groupondetails.getIndicator().setFocusColor(getResources().getColor(R.color.theme_bule)).setNormalColor(getResources().getColor(R.color.halfgraybg))
                .setRadius((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
        yvp_groupondetails.getIndicator().build();
    }
    private String mTvSelectTip;
    public void getData(){
        YYMallApi.getShopInfo(GrouponDetailsActivity.this, goodsId,1, new YYMallApi.ApiResult<ShopDetailsBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                if (e.getCode() == 6005 || e.getCode() == 8001){
                    replaceCustomView(new GoodsGoneLayout(GrouponDetailsActivity.this).setLoadLisiten(new GoodsGoneLayout.OnLoadDoneLisiten() {
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
            public void onNext(final ShopDetailsBean.DataBean dataBean) {
                if (GrouponDetailsActivity.this.isFinishing())
                    return;
                if (dataBean.getStatus() == 0){
                    replaceCustomView(new GoodsGoneLayout(GrouponDetailsActivity.this).setLoadLisiten(new GoodsGoneLayout.OnLoadDoneLisiten() {
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
                //满减
                String curStr = mDataBean.getCut();
                if (TextUtils.isEmpty(curStr)){
                    mRlCut.setVisibility(GONE);
                }else{
                    mTvCut.setText(curStr);
                }
                storeVerify();
                saleVerify();
                imgBannerList = new ArrayList<>();
                viewList = new ArrayList<>();
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
                mTvYyTip.setText("(正价购买商品最多可用" + dataBean.getMaxYY() + "丫丫)");
                if (dataBean.getVideo().getImg() == null || dataBean.getVideo().getImg().equals("")) {

                } else {
                    imgBannerList.add(dataBean.getVideo().getImg());
                }
                for (int i = 0; i < dataBean.getPhoto().size(); i++) {
                    imgBannerList.add(dataBean.getPhoto().get(i));
                }
                buildBannerBean(dataBean);
                for (int i = 0; i < imgBannerList.size(); i++) {
                    viewList.add(LayoutInflater.from(GrouponDetailsActivity.this).inflate(R.layout.view_banner_item, null));
                }

                if (dataBean.getSellerId() == 0){
                    mTvSeller.setText("自营");
                }else{
                    mTvSeller.setText("商户");
                }

                if (!TextUtils.isEmpty(dataBean.getDescription()))
                    mTvShopDesc.setText(dataBean.getDescription());
                else
                    mTvShopDesc.setVisibility(GONE);

                GrouponDetailsActivity.PagerAdapters adapters = new GrouponDetailsActivity.PagerAdapters(imgBannerList, viewList);
                yvp_groupondetails.setAdapter(adapters);
                tv_groupondetails_name.setText(dataBean.getName() + "");
                tv_groupondetails_yprice.setText("原价" + new java.text.DecimalFormat("#0.00").format(Double.parseDouble(dataBean.getPrice())));
                tv_groupondetails_yprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                tv_groupondetails_price.setText("¥" + new java.text.DecimalFormat("#0.00").format(Double.parseDouble(dataBean.getGroup().getPrice())));
                tv_groupondetails_xiaol.setText(dataBean.getGroup().getSum() + "人已团");

//                if (dataBean.getPoint() == 0 && dataBean.getExp() == 0)
//                    mLlGetScore.setVisibility(GONE);
//                else{
//                    tv_groupondetails_point.setText("购物得" + dataBean.getPoint() + "积分");
//                    tv_groupondetails_exp.setText("购物得" + dataBean.getExp() + "经验");
//                }

                if (dataBean.getPoint() == 0){
                    rl_groupondetails_point.setVisibility(GONE);
                }else{
                    tv_groupondetails_point.setText("购物得" + dataBean.getPoint() + "积分");
                }
                if (dataBean.getExp() == 0){
                    rl_groupondetails_exp.setVisibility(GONE);
                }else{
                    tv_groupondetails_exp.setText("购物得" + dataBean.getExp() + "经验");
                }

                time = dataBean.getGroup().getTime();
                thread = new Thread(sendable);
                thread.start();

                if (dataBean.getComment().getList()!= null && dataBean.getComment().getList().size() != 0) {
                    mTvNoComment.setVisibility(GONE);
                    mLlCmfirst.setVisibility(VISIBLE);
                    tv_groupondetails_user.setText("" + dataBean.getComment().getList().get(0).getUser());
                    //Glide.with(mContext).load(lists.get(position)).into((ImageView) viewLists.get(position).findViewById(R.id.vb_img));
//                    Glide.with(getApplicationContext()).load(dataBean.getComment().getList().get(0).getUserico()).placeholder(R.mipmap.ic_nor_srcheadimg).into(img_groupondetails_useric);
                    Glide.with(GrouponDetailsActivity.this).load(dataBean.getComment().getList().get(0).getUserico()).asBitmap().centerCrop().placeholder(R.mipmap.ic_nor_srcheadimg)
                            .skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(new BitmapImageViewTarget(img_groupondetails_useric) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            img_groupondetails_useric.setImageDrawable(circularBitmapDrawable);
                        }
                    });
                    tv_groupondetails_connet.setText("" + dataBean.getComment().getList().get(0).getContents());
                    tv_groupondetails_spec.setText("" + dataBean.getComment().getList().get(0).getSpec());
                }else{
                    mTvNoComment.setVisibility(VISIBLE);
                    mLlCmfirst.setVisibility(GONE);
                }

                tv_groupondetails_pingj.setText("产品评价(" + dataBean.getComment().getCount() + ")");
                if (dataBean.getGroup() !=null && dataBean.getGroup().getList()!= null && dataBean.getGroup().getList().size() != 0) {
                    int numb = dataBean.getGroup().getList().get(0).getNumber();
                    if (numb == 0){
                        ll_groupondetails_has.setVisibility(GONE);
                    }else{
                        String str="仅差<font color='#007cd1'>" + numb + "</font>人成团";
                        tv_groupondetails_gperson.setText(Html.fromHtml(str));
                        ll_groupondetails_has.setVisibility(View.VISIBLE);
                        tv_groupondetails_gname.setText(dataBean.getGroup().getList().get(0).getUser() + "");
                        tv_groupondetails_gfrom.setText(dataBean.getGroup().getList().get(0).getFrom());

                        Glide.with(GrouponDetailsActivity.this).load(dataBean.getGroup().getList().get(0).getUserico())
                                .asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.mipmap.ic_nor_srcheadimg).into(new BitmapImageViewTarget(img_groupondetails_heads) {
                            @Override
                            protected void setResource(Bitmap resource) {
                                RoundedBitmapDrawable circularBitmapDrawable =
                                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                                circularBitmapDrawable.setCircular(true);
                                img_groupondetails_heads.setImageDrawable(circularBitmapDrawable);
                            }
                        });
                        ll_groupondetails_has.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //团列表
                                intent = new Intent(getApplicationContext(), GrouponAllActivity.class);
                                intent.putExtra("groupId", dataBean.getGroup().getId() + "");
                                startActivity(intent);
                            }
                        });

                        tv_groupondetails_offered.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //参团
                                if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                                    showToast("请先登录");
                                    startActivity(LoginActivity.class);
                                    return;
                                }
                                intent = new Intent(getApplicationContext(), OfferedDetailsActivity.class);
                                intent.putExtra("groupId", dataBean.getGroup().getList().get(0).getId() + "");
                                startActivity(intent);
                            }
                        });
                    }
                }
                tv_groupondetails_tnumb.setText(dataBean.getGroup().getAllowNum() + "人成团");
                mTvSelectTip = "请选择商品";
                for (int i = 0; i < dataBean.getSpec().size(); i++) {
                    if (i < dataBean.getSpec().size() - 1) {
                        mTvSelectTip += dataBean.getSpec().get(i).getName() + "，";
                    } else {
                        mTvSelectTip += dataBean.getSpec().get(i).getName();
                    }
                }
                tv_groupondetails_dbuy.setText("¥" + new java.text.DecimalFormat("#0.00").format(Double.parseDouble(dataBean.getPrice())));
                tv_groupondetails_tbuy.setText("¥" + new java.text.DecimalFormat("#0.00").format(Double.parseDouble(dataBean.getGroup().getPrice())));
                tv_groupondetails_standard.setText(mTvSelectTip);
                String[] mTestVal = new String[dataBean.getComment().getType().size()];
                for (int i = 0; i < dataBean.getComment().getType().size(); i++) {
                    mTestVal[i] = dataBean.getComment().getType().get(i).getName() + "(" + dataBean.getComment().getType().get(i).getCount() +")";
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
                        setTvTitleText(tv_groupondetails_pingj.getText().toString());
                        mTagSelect = position + 4;
                        setImgRightVisiable(View.INVISIBLE);
                        loadRootFragment(R.id.ald_fl_comment,CommentFragment.getInstance(mDataBean.getId(),mTagSelect,(Integer)view.getTag()).setOnFinishListen(GrouponDetailsActivity.this));
                        return true;
                    }
                });
                initWebView();
                web_groipondetails.loadUrl(ApiService.SHOP_DETAIL + "#" + dataBean.getId());
                Log.e("url",ApiService.SHOP_DETAIL + "#" + dataBean.getId());

                rl_groupondetails_standard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStoreNone  || mIsSale == 1) return;
                        if (TextUtils.isEmpty(YYApp.getInstance().getToken())) {
                            startActivity(LoginActivity.class);
                            showToast("请先登录");
                            return;
                        }
                        showSelectCarPop(null);
                    }
                });
                tv_groupondetails_buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStoreNone  || mIsSale == 1) return;

                        if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                            return;
                        }

                        if (mCurSpecBean == null || mSelectNumb == -1 ){
                            showSelectCarPop(0);
                            return;
                        }

                        if (mSelectNumb == 0){
                            showToast("该商品暂无库存");
                            return;
                        }

                        if (!TextUtils.isEmpty(mCommonCanBuy)){
                            showToast(mCommonCanBuy);
                            return;
                        }

                        Intent intent = new Intent(GrouponDetailsActivity.this, CheckOutActivity.class);
                        intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.COMMONSHOP);
                        intent.putExtra("productId", String.valueOf(mCurSpecBean.getId()));
                        intent.putExtra("nums", String.valueOf(mSelectNumb));
                        startActivity(intent);
                    }
                });
                tv_commodity_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mStoreNone || mIsSale ==1 ) return;
                        if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
                            showToast("请先登录");
                            startActivity(LoginActivity.class);
                            return;
                        }
                        if (mCurSpecBean == null || mSelectNumb == -1 ){
                            showSelectCarPop(1);
                            return;
                        }

                        if (mSelectNumb == 0){
                            showToast("该商品暂无库存");
                            return;
                        }

                        if (!TextUtils.isEmpty(mGroupCanBuyStr)){
                            showToast(mGroupCanBuyStr);
                            return;
                        }

                        YYMallApi.getGroupCreate(GrouponDetailsActivity.this, mDataBean.getGroup().getId(),
                                mCurSpecBean.getId(), mSelectNumb, apiCallback);
                    }
                });
            }
        });
    }
    private void showSelectCarPop(Integer type){
        //type团-1 买-0
        if (mDataBean == null) return;
        GroupCarPopupView carPopupView;
        if (mSelectSpecHashMap == null)
            carPopupView = new GroupCarPopupView(GrouponDetailsActivity.this,GrouponDetailsActivity.this,mDataBean,type);
        else
            carPopupView = new GroupCarPopupView(GrouponDetailsActivity.this,GrouponDetailsActivity.this,mDataBean,mSelectSpecHashMap,mSelectSpecPosHashMap,String.valueOf(mSelectNumb),type);
        carPopupView.showPopupWindow();
    }

    private LinkedHashMap mSelectSpecHashMap;
    private HashMap mSelectSpecPosHashMap;
    //    private String mProductId;
    private int mSelectNumb = -1;
    private String mGroupCanBuy;
    private String mCommonCanBuy;

    @Override
    public void onShopCarEntityRes(LinkedHashMap hashMap) {
        mSelectSpecHashMap = hashMap;
    }

    @Override
    public void onShopCarResPos(HashMap hashMap) {
        mSelectSpecPosHashMap = hashMap;
    }

    @Override
    public void onShopCarSelectString(String string) {
        if (TextUtils.isEmpty(string))
            tv_groupondetails_standard.setText(mTvSelectTip);
        else
            tv_groupondetails_standard.setText(string);
    }

    private ShopSpecBean.DataBean mCurSpecBean;
    private java.text.DecimalFormat mTwoPointDf =new java.text.DecimalFormat("#0.00");
    @Override
    public void onShopSpecRes(ShopSpecBean.DataBean specBean, int numb) {
        mCurSpecBean = specBean;
        mSelectNumb = numb;
        if (specBean != null)
            updateSpecPrice(specBean);
//        mTvCurMallPrice.setText("¥"+mTwoPointDf.format(specBean.getNormalPrice()));
//        mTvCurPlddgePrice.setText("¥"+mTwoPointDf.format(specBean.getPrice()));
//        mTvPayPrice.setText("¥"+mTwoPointDf.format(specBean.getNormalPrice()));
//        mTvLeasePrice.setText("¥"+mTwoPointDf.format(specBean.getPrice()));
    }

    @Override
    public void onCanSelectRes(String commonCanBuy, String groupCanBuy) {
        mCommonCanBuy = commonCanBuy;
        mGroupCanBuy = groupCanBuy;
    }

    private void updateSpecPrice(ShopSpecBean.DataBean specBean){
        tv_groupondetails_price.setText("¥" + mTwoPointDf.format(specBean.getPrice()));
        tv_groupondetails_tbuy.setText("¥" + mTwoPointDf.format(specBean.getPrice()));
        tv_groupondetails_dbuy.setText("¥" + mTwoPointDf.format(specBean.getNormalPrice()));
    }
    /**
     * 库存等限制性判断
     */
    private boolean mStoreNone = false;
    private void storeVerify(){
        if (mDataBean.getStoreNum() <= 0){
            mTvStoreNone.setVisibility(VISIBLE);
            mStoreNone = true;
            int halftp = getResources().getColor(R.color.half_transparency);
            mTvBuyStr.setTextColor(halftp);
            tv_groupondetails_tnumb.setTextColor(halftp);
            tv_groupondetails_dbuy.setTextColor(halftp);
            tv_groupondetails_tbuy.setTextColor(halftp);
            tv_groupondetails_standard.setTextColor(getResources().getColor(R.color.grayfont_1_5));
        }
    }
    private int mIsSale; //1为不可销售，0为可销售
    private void saleVerify(){
        mIsSale = mDataBean.getIsSale();
        if (mIsSale == 1){
            mTvStoreNone.setVisibility(VISIBLE);
            mTvStoreNone.setText("该商品暂不支持购买");
            int halftp = getResources().getColor(R.color.half_transparency);
            mTvBuyStr.setTextColor(halftp);
            tv_groupondetails_tnumb.setTextColor(halftp);
            tv_groupondetails_dbuy.setTextColor(halftp);
            tv_groupondetails_tbuy.setTextColor(halftp);
            tv_groupondetails_standard.setTextColor(getResources().getColor(R.color.grayfont_1_5));
        }
    }

    private String mCommonCanBuyStr;
    private String mGroupCanBuyStr;

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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showProgressDialog("正在加载，请稍后...");
                }
            });
        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgressDialog();
                }
            });
            showToast("分享成功");
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgressDialog();
                }
            });
            showToast("分享失败");
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    hideProgressDialog();
                }
            });
//            showToast("取消分享");

        }
    };

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
            Glide.with(mContext).load(lists.get(position)).into(imgview);
            container.addView(viewLists.get(position));
            viewLists.get(position).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(GrouponDetailsActivity.this, ShopBannerActivity.class);
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

//    class CommonShopDetailsPopupView extends BasePopupWindow implements View.OnClickListener {
//
//        private Context mContext;
//        private TextView vps_tv_shopprice, vps_tv_repertory, tv_popgroupondetails_dbuy, tv_popgroupondetails_tbuy;
//        private RecyclerView vps_rv;
//        private GrouponDetailsPopAdapter detailsPopAdapter;
//        private String goodsIds;
//        private ShopDetailsBean.DataBean sDataBeans;
//        private ImageView vps_img_shop;
//        private ImageView bt_popdetails_close;
//        private Boolean mBGroup;//是否是团购 true
//        private ProgressBar mProgressBar;
//        private int mGroupLimitNumb;
//        private NormsBean.DataBean mSpecBean;
//        private GrouponDetailsPopAdapter.DetailsPopCall calls = new GrouponDetailsPopAdapter.DetailsPopCall() {
//            @Override
//            public void send(SparseArray<Integer> sparseArray) {
//                mListSelectSpec = sparseArray;
//                getSpec();
//            }
//
//            @Override
//            public void send(int numbs) {
//                mCurSelectNumb = numbs;
//            }
//        };
//
//        private void getSpec(){
//            HashMap hashMap = new HashMap<>();
//            mSelectSpecStr = "";
//            if (mListSelectSpec == null || mListSelectSpec.size() == 0) {
//                for (int i = 0; i < sDataBeans.getSpec().size(); i++) {
//                    hashMap.put(sDataBeans.getSpec().get(i).getId() + "", sDataBeans.getSpec().get(i).getValue().get(0).getName() + "");
//                    mSelectSpecStr = mSelectSpecStr + sDataBeans.getSpec().get(i).getValue().get(0).getName() + ",";
//                    mListSelectSpec.put(i,0);
//                }
//            } else {
//                for (int i = 0; i < sDataBeans.getSpec().size(); i++) {
//                    hashMap.put(sDataBeans.getSpec().get(i).getId() + "", sDataBeans.getSpec().get(i).getValue().get(mListSelectSpec.get(i)).getName() + "");
//                    mSelectSpecStr = mSelectSpecStr + sDataBeans.getSpec().get(i).getValue().get(mListSelectSpec.get(i)).getName() + ",";
//                }
//            }
//
//            tv_groupondetails_standard.setText("已选择：" + mSelectSpecStr);
//            HashMap hashMaps = new HashMap();
//            hashMaps.put("spec", hashMap);
//            String jsonStr = new Gson().toJson(hashMaps);
//            YYMallApi.getShopSpec(mContext, goodsIds, jsonStr, true,new YYMallApi.ApiResult<NormsBean.DataBean>(mContext) {
//                @Override
//                public void onStart() {
//                }
//
//                @Override
//                public void onError(ApiException e) {
//                    super.onError(e);
//                    showToast(e.getMessage());
//                }
//
//                @Override
//                public void onCompleted() {
//
//                }
//
//                @Override
//                public void onNext(NormsBean.DataBean dataBean) {
//                    mProgressBar.setVisibility(GONE);
//                    updateSpecInfo(dataBean);
//                }
//            });
//        }
//
//        private void updateSpecInfo(NormsBean.DataBean dataBean){
//            mSpecBean = dataBean;
//            updateSpecPrice(dataBean);
//            calculateCanBuyTag();
//            Glide.with(mContext).load(dataBean.getImg()).into(vps_img_shop);
//            mStoreNumb = dataBean.getStoreNum();
//            vps_tv_repertory.setText("库存" + dataBean.getStoreNum() + "件");
//            mTvYyTip.setText("(正价购买商品最多可用" + dataBean.getYayaLimit() + "丫丫)");
//            vps_tv_shopprice.setText(mBGroup==null || mBGroup!=null&&mBGroup==true ? "¥" + mSpecBean.getPrice() :
//                    "¥" + new java.text.DecimalFormat("#0.00").format(mSpecBean.getNormalPrice()));
//            shopId = dataBean.getId() + "";
//            List<ShopDetailsBean.DataBean.SpecBean> sPecList = sDataBeans.getSpec();
//            mGroupLimitNumb = dataBean.getLimitnum();
//            detailsPopAdapter = new GrouponDetailsPopAdapter(mContext, sPecList, mListSelectSpec, calls, mCurSelectNumb,
//                    getCurMaxBuy(),mGroupLimitNumb);
//            vps_rv.setLayoutManager(new LinearLayoutManager(mContext));
//            vps_rv.setAdapter(detailsPopAdapter);
//        }
//
//        private void getCanSelectStr() {
//            if (mSpecBean==null) return;
//            if (mBGroup == null || !mBGroup) {
//                if (!canBuy(0)) {
//                    if (mSpecBean.getStoreNum() == 0) {
//                        mCommonCanBuyStr =  "暂无库存";
//                    } else {
//                        mCommonCanBuyStr = "超过最大可购买数量";
//                    }
//                }else{
//                    mCommonCanBuyStr =null;
//                }
//            } else {
//                if (!canBuy(1)) {
//                    if (mSpecBean.getStoreNum() == 0) {
//                        mGroupCanBuyStr = "暂无库存";
//                    } else {
//                        mGroupCanBuyStr = "超过最大可团购数量";
//                    }
//                }else{
//                    mGroupCanBuyStr =  null;
//                }
//            }
//        }
//        private int mCommonCanMaxBuy;
//        private int mGroupCanMaxBuy;
//        //计算购买上限
//        private void calculateCanBuyTag(){
//            int limit = mSpecBean.getLimitnum();
//            int storeNumb = mSpecBean.getStoreNum();
//            int allowPayNumb = mSpecBean.getAllowMaxNum();
//            mCommonCanMaxBuy = storeNumb;
//            if (limit == 0){
//                //不限购
//                mGroupCanMaxBuy = storeNumb;
//            }else{
//                //限购
//                mGroupCanMaxBuy = allowPayNumb > storeNumb ? storeNumb : allowPayNumb;
//            }
//        }
//
//        private int getCurMaxBuy(){
//            if (mBGroup == null || mBGroup == false){
//                //两者和普通
//                return mCommonCanMaxBuy;
//            }else {
//                return mGroupCanMaxBuy;
//            }
//        }
//
//        /**
//         *
//         * @param type
//         */
//        private boolean canBuy(int type){
//            //type  0-buy  1-lease
//            if (type == 0){
//                return mCurSelectNumb <= mCommonCanMaxBuy;
//            }else{
//                return mCurSelectNumb <= mGroupCanMaxBuy;
//            }
//        }
//
//        private void init(){
//            vps_tv_shopprice = (TextView) findViewById(R.id.vps_tv_shopprice);
//            vps_rv = (RecyclerView) findViewById(R.id.vps_rv);
//            vps_img_shop = (ImageView) findViewById(R.id.vps_img_shop);
//            vps_tv_repertory = (TextView) findViewById(R.id.vps_tv_repertory);
//            bt_popdetails_close = (ImageView) findViewById(R.id.bt_popdetails_close);
//            tv_popgroupondetails_dbuy = (TextView) findViewById(R.id.tv_popgroupondetails_dbuy);
//            tv_popgroupondetails_tbuy = (TextView) findViewById(R.id.tv_popgroupondetails_tbuy);
//            mProgressBar = (ProgressBar) findViewById(R.id.vpg_progressbar);
//            tv_popgroupondetails_dbuy.setOnClickListener(this);
//            tv_popgroupondetails_tbuy.setOnClickListener(this);
//            bt_popdetails_close.setOnClickListener(this);
//        }
//
//        public CommonShopDetailsPopupView(final Activity context, ShopDetailsBean.DataBean dataBeans, String goodsId,Boolean bGroup) {
//            super(context);
//            this.mContext = context;
//            this.goodsIds = goodsId;
//            this.sDataBeans = dataBeans;
//            this.mBGroup = bGroup;
//            init();
//            if (bGroup == null) {
//                tv_popgroupondetails_dbuy.setVisibility(View.VISIBLE);
//                tv_popgroupondetails_tbuy.setVisibility(View.VISIBLE);
//                tv_popgroupondetails_tbuy.setText(dataBeans.getGroup().getAllowNum() + "人团");
//            }else{
//                if (bGroup) {
//                    tv_popgroupondetails_dbuy.setVisibility(View.GONE);
//                    tv_popgroupondetails_tbuy.setVisibility(View.VISIBLE);
//                    tv_popgroupondetails_tbuy.setText(dataBeans.getGroup().getAllowNum() + "人团");
//                }else {
//                    //
//                    tv_popgroupondetails_dbuy.setVisibility(View.VISIBLE);
//                    tv_popgroupondetails_tbuy.setVisibility(View.GONE);
//                }
//            }
//
//            getSpec();
//        }
//
//        @Override
//        public void dismiss() {
//            super.dismiss();
//            getCanSelectStr();
//        }
//
//        @Override
//        protected Animation initShowAnimation() {
//            Animation animation = getTranslateAnimation(500 * 2, 0, 300);
//            return animation;
//        }
//
//        @Override
//        public View getClickToDismissView() {
//            return getPopupWindowView();
//        }
//
//        @Override
//        public View onCreatePopupView() {
//            return createPopupById(R.layout.view_pop_groupdetals);
//        }
//
//        @Override
//        public View initAnimaView() {
//            return findViewById(R.id.vps_rl_popview);
//        }
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.bt_popdetails_close:
//                    dismiss();
//                    break;
//                case R.id.tv_popgroupondetails_dbuy:
//                    //普通商品购买
//                    if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
//                        showToast("请先登录");
//                        startActivity(LoginActivity.class);
//                        return;
//                    }
//
//                    if (mStoreNumb == 0) {
//                        showToast("库存不足，请选择其他规格。");
//                    } else {
//                        if (!canBuy(0)){
//                            showToast("超过最大可购买数量");
//                            return;
//                        }
//
//                        intent = new Intent(mContext, CheckOutActivity.class);
//                        intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.COMMONSHOP);
//                        intent.putExtra("productId", shopId);
//                        intent.putExtra("nums", mCurSelectNumb + "");
//                        mContext.startActivity(intent);
//                        dismiss();
//                    }
//                    break;
//                case R.id.tv_popgroupondetails_tbuy:
//                    if (TextUtils.isEmpty(YYApp.getInstance().getToken())){
//                        showToast("请先登录");
//                        startActivity(LoginActivity.class);
//                        return;
//                    }
//
//                    if (!canBuy(1)){
//                        showToast("超过最大可团购数量");
//                        return;
//                    }
//
//                    if (mStoreNumb == 0) {
//                        showToast("库存不足，请选择其他规格。");
//                    } else {
//                        YYMallApi.getGroupCreate(GrouponDetailsActivity.this, id, shopId, mCurSelectNumb + "", apiCallback);
//                        dismiss();
//                    }
//                    break;
//                default:
//                    break;
//
//            }
//        }
//    }

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

    Runnable sendable = new Runnable() {
        @Override
        public void run() {
            while (-1 < time) {
                try {
                    Thread.sleep(1000);
                    Message message = new Message();
                    message.arg1 = time;
                    handler.sendMessage(message);
                    time--;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    public static String formatTime(int ms) {

        int mi = 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;

        String strDay = day < 10 ? "0" + day : "" + day; //天
        String strHour = hour < 10 ? "0" + hour : "" + hour;//小时
        String strMinute = minute < 10 ? "0" + minute : "" + minute;//分钟
        return strDay + "天" + strHour + "小时" + strMinute + "分钟";
    }

    @Override
    protected void onDestroy() {
        time = -2;
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
