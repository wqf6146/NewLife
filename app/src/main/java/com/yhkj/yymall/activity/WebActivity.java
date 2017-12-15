package com.yhkj.yymall.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.just.library.AgentWeb;
import com.just.library.AgentWebSettings;
import com.just.library.ChromeClientCallbackManager;
import com.just.library.DownLoadResultListener;
import com.just.library.IWebLayout;
import com.just.library.PermissionInterceptor;
import com.just.library.WebDefaultSettingsManager;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.MesBean;
import com.yhkj.yymall.config.JSInterface;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;

/**
 */
public class WebActivity extends BaseToolBarActivity {

    private AgentWeb mAgentWeb;
    private String mUrl;
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
//        buildAgentWeb();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
//        buildAgentWeb();
    }

    protected void buildAgentWeb(boolean goUrl) {
        mAgentWeb = AgentWeb.with(this)//
                .setAgentWebParent(getAgentWebParent(), new ViewGroup.LayoutParams(-1, -1))//
                .closeProgressBar()
                .setReceivedTitleCallback(getReceivedTitleCallback())
                .setWebChromeClient(getWebChromeClient())
                .setWebViewClient(getWebViewClient())
                .setWebView(getWebView())
                .setPermissionInterceptor(getPermissionInterceptor())
                .setWebLayout(getWebLayout())
                .addDownLoadResultListener(getDownLoadResultListener())
                .setAgentWebSettings(getAgentWebSettings())
                .setSecutityType(AgentWeb.SecurityType.strict)
                .createAgentWeb()//
                .ready()
                .go(goUrl ? mUrl = getUrl() : "");
    }

    @Override
    protected void initView() {
        super.initView();
        setToolBarColor(getResources().getColor(R.color.theme_bule));
    }

    @Override
    protected void onPause() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (mAgentWeb != null)
            mAgentWeb.uploadFileResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {
        if (mAgentWeb != null)
            mAgentWeb.getWebLifeCycle().onDestroy();
        super.onDestroy();
        AppManager.getInstance().finishActivity(this);
    }


    protected @Nullable
    DownLoadResultListener getDownLoadResultListener() {
        return null;
    }

    private @Nullable ChromeClientCallbackManager.ReceivedTitleCallback getReceivedTitleCallback() {
        return new ChromeClientCallbackManager.ReceivedTitleCallback() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
            }
        };
    }

    public @Nullable
    AgentWebSettings getAgentWebSettings() {
        return WebDefaultSettingsManager.getInstance();
    }


    protected @Nullable
    WebChromeClient getWebChromeClient() {
        return null;
    }


    protected @Nullable WebView getWebView() {
        return null;
    }

    protected  @Nullable
    IWebLayout getWebLayout() {
        return null;
    }
    protected PermissionInterceptor getPermissionInterceptor() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        AppManager.getInstance().addActivity(this);

        mTvTitle.setText(getIntent().getStringExtra("title"));

        initData();
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();

    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        setImgBackLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAgentWeb.getWebCreator().get().getUrl().equals(mUrl)) {
                    AppManager.getInstance().finishActivity(WebActivity.this);
                    return;
                }
                if (mAgentWeb.getWebCreator().get().canGoBack()){
                    mAgentWeb.getWebCreator().get().goBack();
                }else{
                    AppManager.getInstance().finishActivity(WebActivity.this);
                }
            }
        });
    }

    @Override
    protected void initData() {
        final String id = getIntent().getStringExtra("id");
        if (TextUtils.isEmpty(id)){
            initWeb(true);
        }else{
            YYMallApi.getMesById(this, id, new ApiCallback<MesBean.DataBean>() {
                @Override
                public void onError(ApiException e) {
                    super.onError(e);
                    showToast(e.getMessage());
                    setNetWorkErrShow(View.VISIBLE);
                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onNext(MesBean.DataBean mesBean) {
                    setNetWorkErrShow(View.GONE);
                    initWeb(false);
                    mTvTitle.setText(mesBean.getTitle());
                    //newMesIntent.putExtra(Constant.WEB_TAG.TAG, ApiService.SERVER_URL + id);
                    mAgentWeb.getLoader().loadUrl(mUrl = ApiService.SERVER_URL + id);
                }

                @Override
                public void onStart() {

                }
            });
        }
    }

    private void initWeb(boolean goUrl){
        buildAgentWeb(goUrl);
        mAgentWeb.getJsInterfaceHolder().addJavaObject("aosObject",new JSInterface(new JSInterface.JsCallBack() {
            @Override
            public void callBack(final String... args) {
                //线下活动分享
                if (args.length < 1)
                    return;
                new ShareAction(WebActivity.this)
                        .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE,
                                SHARE_MEDIA.QZONE)
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                String url = ApiService.OFFLINE_SHAREES_URL;

                                UMWeb web = new UMWeb(url);
                                web.setTitle(args[0]);
                                web.setDescription(args[1]);
                                web.setThumb( new UMImage(WebActivity.this, R.mipmap.ic_nor_whiteyiyiyaya));  //缩略图
                                new ShareAction(WebActivity.this).withMedia(web)
                                        .setPlatform(share_media)
                                        .setCallback(shareListener)
                                        .share();
                            }
                        })
                        .open();
            }
        }));
    }


    @NonNull
    protected ViewGroup getAgentWebParent() {
        return (ViewGroup) this.findViewById(R.id.container);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb != null && mAgentWeb.handleKeyEvent(keyCode, event)) {
            if (mAgentWeb.getWebCreator().get().getUrl().equals(mUrl))
                return super.onKeyDown(keyCode, event);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Nullable
    protected String getUrl() {
        return getIntent().getStringExtra(Constant.WEB_TAG.TAG) + "#" + YYApp.getInstance().getToken();
    }

    public static void loadUrl(Context mContext, String mUrl, String title) {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra(Constant.WEB_TAG.TAG, mUrl);
        intent.putExtra("title", title);
        mContext.startActivity(intent);
    }

    public static void loadUrl(Context mContext, String id) {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra("id", id);
        mContext.startActivity(intent);
    }

    @Nullable
    protected WebViewClient getWebViewClient() {
        return new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setNetWorkErrShow(View.GONE);
            }
        };
    }

    private ProgressDialog getProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(WebActivity.this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
//                    progressShow = false;
                }
            });
        }
        return mProgressDialog;
    }
    private ProgressDialog mProgressDialog;
    protected void showProgressDialog(String txt){
        if (mProgressDialog == null){
            mProgressDialog = getProgressDialog();
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(txt);
            mProgressDialog.show();
        }
    }
    protected void hideProgressDialog(){
        if (mProgressDialog!=null)
            mProgressDialog.dismiss();
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
            YYMallApi.OfflineActCallback(WebActivity.this, new ApiCallback<CommonBean>() {
                @Override
                public void onStart() {

                }

                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(ApiException e) {
                    Toast.makeText(WebActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNext(CommonBean commonBean) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDialog();
                        }
                    });
                    Toast.makeText(WebActivity.this,"分享成功",Toast.LENGTH_SHORT).show();
                    AppManager.getInstance().finishActivity(WebActivity.this);
                }
            });
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
            Toast.makeText(WebActivity.this,"分享失败",Toast.LENGTH_SHORT).show();
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
//            Toast.makeText(WebActivity.this,"取消分享",Toast.LENGTH_SHORT).show();
        }
    };
}
