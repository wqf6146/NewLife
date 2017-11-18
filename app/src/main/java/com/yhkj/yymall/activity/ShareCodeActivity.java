package com.yhkj.yymall.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.yhkj.yymall.BaseActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.InviteCodeBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.util.CommonUtil;

import butterknife.Bind;

import static com.yhkj.yymall.http.api.ApiService.SHARE_CODE_URL;
import static com.yhkj.yymall.http.api.ApiService.SHARE_SHOP_URL;

/**
 * Created by Administrator on 2017/8/9.
 */

public class ShareCodeActivity extends BaseActivity {

    @Bind(R.id.ai_img_leftbtn)
    ImageView mImgLeftBtn;

    @Bind(R.id.ai_tv_integral)
    TextView mTvIntegral;

    @Bind(R.id.ai_tv_share)
    TextView mTvShare;

    @Bind(R.id.ai_tv_yourcode)
    TextView mTvYourCode;

    @Bind(R.id.ai_tv_checkrule)
    TextView mTvCheckRule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitefriend);
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        if(Build.VERSION.SDK_INT>=23){
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS,Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(ShareCodeActivity.this,mPermissionList,123);
        }
    }

    @Override
    protected void initView() {
        setStatusViewVisiable(false);
    }

    @Override
    protected void bindEvent() {
        mTvCheckRule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareCodeActivity.this,WebActivity.class);
                intent.putExtra("title","邀请好友规则");
                intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.YAOQINGGUIZE);
                startActivity(intent);
            }
        });
        mImgLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishActivity(ShareCodeActivity.this);
            }
        });
        mTvShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //分享
                if (TextUtils.isEmpty(mCode)) return;

                new ShareAction(ShareCodeActivity.this)
                        .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE,
                                SHARE_MEDIA.QZONE)
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                String url = SHARE_CODE_URL + "#" + mCode;
                                if (share_media == SHARE_MEDIA.SINA){
                                    if (CommonUtil.isWeiboClientAvailable(ShareCodeActivity.this)){
                                        UMImage image = new UMImage(ShareCodeActivity.this, R.mipmap.ic_nor_whiteyiyiyaya);  //缩略图
                                        new ShareAction(ShareCodeActivity.this).setPlatform(SHARE_MEDIA.SINA).withText("YiYiYaYa厂家直销各种儿童用品，玩具、童车、童装、家居等，高品质低价格，更有安全座椅0元领用，快来看看吧！"+url)
                                                .withMedia(image).setCallback(shareListener).share();
                                    }else{
                                        showToast("请先安装新浪微博");
                                    }

                                }else{
                                    UMWeb web = new UMWeb(url);
                                    web.setTitle("YiYiYaYa,厂家直销专用app,更高质量，更低价格，宝宝开心，妈妈放心，我们更安心");//标题
                                    web.setThumb( new UMImage(ShareCodeActivity.this, R.mipmap.ic_nor_whiteyiyiyaya));  //缩略图
                                    web.setDescription("YiYiYaYa厂家直销各种儿童用品，玩具、童车、童装、家居等，高品质低价格，更有安全座椅0元领用，快来看看吧！");//描述
                                    new ShareAction(ShareCodeActivity.this).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧").setPlatform(share_media).withMedia(web).setCallback(shareListener).share();
                                }
                            }
                        })
                        .open();
            }
        });
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
//            if (UMShareAPI.get(ShareCodeActivity.this).isInstall(ShareCodeActivity.this,platform)){
                showToast(t.getMessage());
//            }else{
//                showToast("分享失败");
//            }
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
        UMShareAPI.get(this).release();
    }

    @Override
    protected void initData() {
        YYMallApi.shareInvite(this, new YYMallApi.ApiResult<InviteCodeBean.DataBean>(mContext) {
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
            public void onNext(InviteCodeBean.DataBean dataBean) {
                setUi(dataBean);
            }

        });
    }

    private String mCode;

    private void setUi(InviteCodeBean.DataBean dataBean) {
        mCode = dataBean.getShare_code();
        mTvIntegral.setText(dataBean.getNub() + "丫丫");
        mTvYourCode.setText("您的邀请码：" + mCode);
    }
}
