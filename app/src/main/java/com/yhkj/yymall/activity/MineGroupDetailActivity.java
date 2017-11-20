package com.yhkj.yymall.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.zxing.common.detector.MathUtils;
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
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.GroupDetailBean;
import com.yhkj.yymall.event.MainTabSelectEvent;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.popwindows.DetailsMenuPopupView;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.TimeZone;

import butterknife.Bind;

import static android.view.View.GONE;
import static com.vise.utils.handler.HandlerUtil.runOnUiThread;
import static com.yhkj.yymall.http.api.ApiService.SHARE_SHOP_URL;

/**
 * Created by Administrator on 2017/8/7.
 */

public class MineGroupDetailActivity extends BaseToolBarActivity {

    private int mGroupId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGroupId = getIntent().getIntExtra("groupid", 0);
        setOpenOriginDataStatus(true);
        setContentView(R.layout.activity_groupdetailwait);
    }

    private GroupDetailBean.DataBean mDataBean;
    private int mType;
    NestFullListView mListView;
    TextView mTvShopName;
    TextView mTvShopTime;

    @Override
    protected void onGetOriginData() {
        super.onGetOriginData();
        YYMallApi.getGroupDetail(this, mGroupId, new YYMallApi.ApiResult<GroupDetailBean.DataBean>(MineGroupDetailActivity.this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(GroupDetailBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                //0正在拼团 1拼团完成 2拼团失败
                mDataBean = dataBean;
                if (dataBean.getStatus() == 0) {
                    mType = Constant.GROUP_STATUS.WAIT;
                    setUserContentView(R.layout.activity_groupdetailwait);
                } else if (dataBean.getStatus() == 1) {
                    mType = Constant.GROUP_STATUS.SUCCESS;
                    setUserContentView(R.layout.activity_groupsuc);
                } else {
                    mType = Constant.GROUP_STATUS.FAILD;
                    setUserContentView(R.layout.activity_groupdetailfaild);
                }
            }
        });
    }

    private void getData() {
        YYMallApi.getGroupDetail(this, mGroupId, new YYMallApi.ApiResult<GroupDetailBean.DataBean>(MineGroupDetailActivity.this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(GroupDetailBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                initUi();
            }
        });
    }


    private void initUi() {
        if (mType == Constant.GROUP_STATUS.FAILD) {

        } else {
//            Collections.reverse(mDataBean.getHeadIco());
            if (mType == Constant.GROUP_STATUS.WAIT) {
                int hor = mDataBean.getTime() / 60 / 60;
                int mis = (mDataBean.getTime() - (hor * 60 * 60)) / 60;
                mTvHour.setText(String.valueOf(hor));
                mTvMills.setText(String.valueOf(mis));
            }
            mTvShopName.setText(mDataBean.getGoodsName());
            mTvShopTime.setText(mDataBean.getCreateTime());
            mListView.setAdapter(new NestFullListViewAdapter<String>(R.layout.item_group_head, mDataBean.getHeadIco()) {
                @Override
                public void onBind(int pos, String img, final NestFullViewHolder holder) {
                    if (pos == 0) {
                        holder.setVisible(R.id.igh_tv_master, true);
                    } else {
                        holder.setVisible(R.id.igh_tv_master, false);
                    }
                    Glide.with(MineGroupDetailActivity.this).load(img)
                            .asBitmap().centerCrop().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.mipmap.ic_nor_srcheadimg).into(new BitmapImageViewTarget((ImageView) holder.getView(R.id.igh_img_head)) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            ((ImageView) holder.getView(R.id.igh_img_head)).setImageDrawable(circularBitmapDrawable);
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        if (mOriginDataStatus) {
            onGetOriginData();
        } else {
            getData();
        }
    }

    private TextView mTvGoHome, mTvHour, mTvMills, mTvInvaiteFriends, mTvCheckOrder;

    private int mGoodsId;
    private String mStringImg;
    @Override
    protected void initBaseUi() {
        super.initBaseUi();
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTvTitleText("拼团详情");
    }

    private int mOrderId;
    private String mStatus;

    @Override
    protected void initView() {
        super.initView();
        mStatus = getIntent().getStringExtra("status");
        mOrderId = getIntent().getIntExtra("id", -1);
        mGoodsId = getIntent().getIntExtra("goodsid",-1);
        mStringImg = getIntent().getStringExtra("img");
        if (mType == Constant.GROUP_STATUS.FAILD) {
            mTvGoHome = (TextView) findViewById(R.id.ag_tv_gohome);
        } else if (mType == Constant.GROUP_STATUS.WAIT) {
            mListView = (NestFullListView)findViewById(R.id.ag_listview);
            mTvShopName = (TextView)findViewById(R.id.ag_tv_shopname);
            mTvShopTime = (TextView)findViewById(R.id.ag_tv_shoptime);
            mTvHour = (TextView) findViewById(R.id.ag_tv_hour);
            mTvMills = (TextView) findViewById(R.id.ag_tv_mills);
            mTvInvaiteFriends = (TextView) findViewById(R.id.ag_tv_invitefriends);
        } else {
            mListView = (NestFullListView)findViewById(R.id.ag_listview);
            mTvShopName = (TextView)findViewById(R.id.ag_tv_shopname);
            mTvShopTime = (TextView)findViewById(R.id.ag_tv_shoptime);
            mTvCheckOrder = (TextView) findViewById(R.id.ag_tv_checkorder);
            mTvGoHome = (TextView) findViewById(R.id.ag_tv_backhome);
        }
    }

    @Override
    protected void initData() {
        initUi();
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
            showToast("取消分享");

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UMShareAPI.get(this).release();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();

        if (mType == Constant.GROUP_STATUS.FAILD) {
            mTvGoHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YYApp.getInstance().setIndexShow(0);
                    startActivity(MainActivity.class);
                    AppManager.getInstance().finishExceptActivity(MainActivity.class);
                }
            });
        } else if (mType == Constant.GROUP_STATUS.WAIT) {
            mTvInvaiteFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //邀请好友
                    new ShareAction(MineGroupDetailActivity.this)
                            .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                                    SHARE_MEDIA.QZONE)
                            .setCallback(shareListener)
                            .setShareboardclickCallback(new ShareBoardlistener() {
                                @Override
                                public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                    String url = SHARE_SHOP_URL + "#" + mGoodsId;
                                    UMWeb web = new UMWeb(url);
                                    web.setTitle(mDataBean.getGoodsName());//标题
                                    if (!TextUtils.isEmpty(mStringImg))
                                        web.setThumb(new UMImage(MineGroupDetailActivity.this,mStringImg));  //缩略图
                                    else
                                        web.setThumb(new UMImage(MineGroupDetailActivity.this, R.mipmap.ic_nor_srcpic));  //缩略图
                                    web.setDescription("我在YiYiYaYa发现了一个不错的商品，快来看看吧");//描述
                                    new ShareAction(MineGroupDetailActivity.this).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧").withMedia(web).
                                            setCallback(shareListener).setPlatform(share_media).share();
                                }
                            })
                            .open();
                }
            });
        } else {
            mTvCheckOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //查看订单
                    Intent intent = new Intent(MineGroupDetailActivity.this, OrderDetailActivity.class);
                    intent.putExtra("id", mOrderId);
                    startActivity(intent);
                }
            });
            mTvGoHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(MainActivity.class);
                    AppManager.getInstance().finishActivity(MineGroupDetailActivity.this);
                    BusFactory.getBus().post(new MainTabSelectEvent(0));
                }
            });
        }
    }
}

