package com.yhkj.yymall.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.just.library.BaseAgentWebActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.config.JSInterface;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CommonUtil;
import static android.os.Build.VERSION_CODES.KITKAT;
import static com.yhkj.yymall.http.api.ApiService.SHARE_CODE_URL;
import static com.yhkj.yymall.http.api.ApiService.SHARE_SHOP_URL;

/**
 */
public class WebActivity extends BaseAgentWebActivity {

    private TextView mTvTitle;
    private ImageView mImgBack;
    private View mDeadStatusView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        mDeadStatusView = findViewById(R.id.aw_view_statusbar);
        if (Build.VERSION.SDK_INT >= KITKAT)
            mDeadStatusView.getLayoutParams().height = CommonUtil.getStatusBarHeight(this);
        mImgBack = (ImageView)findViewById(R.id.aw_img_back);
        mTvTitle = (TextView)findViewById(R.id.aw_tv_title);

        mImgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mAgentWeb.back())
                    finish();
            }
        });
        mTvTitle.setText(getIntent().getStringExtra("title"));

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

//                                if (share_media == SHARE_MEDIA.SINA){
//                                    if (CommonUtil.isWeiboClientAvailable(WebActivity.this)){
//                                        UMImage image = new UMImage(WebActivity.this, R.mipmap.ic_nor_whiteyiyiyaya);  //缩略图
//                                        new ShareAction(WebActivity.this).setPlatform(SHARE_MEDIA.SINA).withText(args[1]+url)
//                                                .withMedia(image).setCallback(shareListener).share();
//                                    }else{
//                                        Toast.makeText(WebActivity.this,"请先安装新浪微博",Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }else{
//                                    UMWeb web = new UMWeb(url);
//                                    web.setTitle(args[0]);//标题
//                                    web.setThumb( new UMImage(WebActivity.this, R.mipmap.ic_nor_whiteyiyiyaya));  //缩略图
//                                    web.setDescription(args[1]);//描述
//                                    new ShareAction(WebActivity.this).withText(args[1]).setPlatform(share_media).withMedia(web).setCallback(shareListener).share();
//                                }
                            }
                        })
                        .open();
            }
        }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @NonNull
    @Override
    protected ViewGroup getAgentWebParent() {
        return (ViewGroup) this.findViewById(R.id.container);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mAgentWeb != null && mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void setTitle(WebView view, String title) {

    }

    @Override
    protected int getIndicatorColor() {
        return Color.parseColor("#00ffffff");
    }

    @Override
    protected int getIndicatorHeight() {
        return 3;
    }

    @Nullable
    @Override
    protected String getUrl() {
        return getIntent().getStringExtra(Constant.WEB_TAG.TAG) + "#" + YYApp.getInstance().getToken();
    }

    public static void loadUrl(Context mContext, String mUrl, String title) {
        Intent intent = new Intent(mContext, WebActivity.class);
        intent.putExtra(Constant.WEB_TAG.TAG, mUrl);
        intent.putExtra("title", title);
        mContext.startActivity(intent);
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
                    finish();
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
            Toast.makeText(WebActivity.this,"取消分享",Toast.LENGTH_SHORT).show();
        }
    };
}
