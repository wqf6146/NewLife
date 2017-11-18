package com.yhkj.yymall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.OfferedDetailsPopAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.GrouponSubBean;
import com.yhkj.yymall.bean.NormsBean;
import com.yhkj.yymall.bean.OfferedDetailsBean;
import com.yhkj.yymall.bean.ShopSpecBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.popwindows.GroupCarPopupView;
import com.yhkj.yymall.view.popwindows.OfferedPopipView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static com.yhkj.yymall.http.api.ApiService.SHARE_SHOP_URL;

/**
 * Created by Administrator on 2017/7/31.
 */

public class OfferedDetailsActivity extends BaseToolBarActivity implements OfferedPopipView.OnShopCarResLisiten {

    @Bind(R.id.img_offereddetails_shop)
    ImageView img_offereddetails_shop;

    @Bind(R.id.tv_offereddetails_shopname)
    TextView tv_offereddetails_shopname;

    @Bind(R.id.tv_offereddetails_numb)
    TextView tv_offereddetails_numb;

    @Bind(R.id.tv_offereddetails_money)
    TextView tv_offereddetails_money;

    @Bind(R.id.tv_offereddetails_offere)
    TextView tv_offereddetails_offere;

    @Bind(R.id.tv_offereddetails_hor)
    TextView tv_offereddetails_hor;

    @Bind(R.id.tv_offereddetails_min)
    TextView tv_offereddetails_min;

    @Bind(R.id.ag_listview)
    NestFullListView mListView;

    @Bind(R.id.ao_tv_desc)
    TextView mTvDesc;

    private Intent intent;
    private String groupId;
    private int mCurSelectNumb = 1;
    private int time;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.arg1 != 0) {
                tv_offereddetails_hor.setText(formatTime(msg.arg1, 0));
                tv_offereddetails_min.setText(formatTime(msg.arg1, 1));
            } else {

            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offereddetails);
        setOnResumeRegisterBus(true);
    }

    @Override
    protected void initData() {
        setTvTitleText("拼团详情");
        setImgBackVisiable(View.VISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        intent = getIntent();
        groupId = intent.getStringExtra("groupId");
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

    private OfferedDetailsBean.DataBean mDataBean;
    private void getData() {
        YYMallApi.getGroupJoin(OfferedDetailsActivity.this, groupId, new YYMallApi.ApiResult<OfferedDetailsBean.DataBean>(OfferedDetailsActivity.this) {
            @Override
            public void onStart() {

            }

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
            public void onNext(final OfferedDetailsBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                mDataBean = dataBean;
                tv_offereddetails_shopname.setText(dataBean.getGoods().getName() + "");
                tv_offereddetails_numb.setText(dataBean.getAllowNum() + "人团");
                tv_offereddetails_money.setText("¥" + dataBean.getGoods().getPrice());
                time = dataBean.getTime();
                new Thread(sendable).start();

                mListView.setAdapter(new NestFullListViewAdapter<String>(R.layout.item_group_head, dataBean.getMemberImg()) {
                    @Override
                    public void onBind(int pos, String img, final NestFullViewHolder holder) {
                        if (pos == 0) {
                            holder.setVisible(R.id.igh_tv_master, true);
                        } else {
                            holder.setVisible(R.id.igh_tv_master, false);
                        }
                        Glide.with(OfferedDetailsActivity.this).load(img)
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

                Glide.with(OfferedDetailsActivity.this).load(dataBean.getGoods().getImg()).into(img_offereddetails_shop);

                int diffPeople =  dataBean.getAllowNum() -  dataBean.getMemberImg().size();
                mTvDesc.setText("等待成团，仅剩"+ diffPeople +"个名额");

                if (mDataBean.getIsJoin() > 0){
                    //已经参团
                    tv_offereddetails_offere.setText("邀请好友参团");
                    tv_offereddetails_offere.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new ShareAction(OfferedDetailsActivity.this)
                                    .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE,
                                            SHARE_MEDIA.QZONE)
                                    .setCallback(shareListener)
                                    .setShareboardclickCallback(new ShareBoardlistener() {
                                        @Override
                                        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                            String url = SHARE_SHOP_URL + "#" + mDataBean.getGoods().getId();
                                            if (share_media == SHARE_MEDIA.SINA) {
                                                UMImage image;
                                                if (!TextUtils.isEmpty(mDataBean.getGoods().getImg()))
                                                    image = new UMImage(OfferedDetailsActivity.this,mDataBean.getGoods().getImg());  //缩略图
                                                else
                                                    image = new UMImage(OfferedDetailsActivity.this, R.mipmap.ic_nor_srcpic);  //缩略图
                                                new ShareAction(OfferedDetailsActivity.this).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧" + url).withMedia(image)
                                                        .setCallback(shareListener).setPlatform(SHARE_MEDIA.SINA).share();
                                            } else {
                                                UMWeb web = new UMWeb(url);
                                                web.setTitle(mDataBean.getGoods().getName());//标题
                                                if (!TextUtils.isEmpty(mDataBean.getGoods().getImg()))
                                                    web.setThumb(new UMImage(OfferedDetailsActivity.this,mDataBean.getGoods().getImg()));  //缩略图
                                                else
                                                    web.setThumb(new UMImage(OfferedDetailsActivity.this, R.mipmap.ic_nor_srcpic));  //缩略图
                                                web.setDescription("我在YiYiYaYa发现了一个不错的商品，快来看看吧");//描述
                                                new ShareAction(OfferedDetailsActivity.this).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧").withMedia(web).
                                                        setCallback(shareListener).setPlatform(share_media).share();
                                            }
                                        }
                                    })
                                    .open();
                        }
                    });
                }else{
                    tv_offereddetails_offere.setText("一键参团");
                    tv_offereddetails_offere.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //type团-1 买-0
                            if (mDataBean == null) return;
                            OfferedPopipView carPopupView;
                            if (mSelectSpecHashMap == null)
                                carPopupView = new OfferedPopipView(OfferedDetailsActivity.this,OfferedDetailsActivity.this,mDataBean,1,groupId);
                            else
                                carPopupView = new OfferedPopipView(OfferedDetailsActivity.this,OfferedDetailsActivity.this,mDataBean,mSelectSpecHashMap,
                                        mSelectSpecPosHashMap,String.valueOf(mSelectNumb),1,groupId);
                            carPopupView.showPopupWindow();


//                            new OfferedDetailsActivity.OfferedDetailsPopupView(OfferedDetailsActivity.this,
//                                    dataBean, dataBean.getGoods().getId() + "").showPopupWindow();
                        }
                    });
                }
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
    public void onCanSelectRes(String commonCanBuy, String leaseCanBuy) {

    }

    private LinkedHashMap mSelectSpecHashMap;
    @Override
    public void onShopCarEntityRes(LinkedHashMap hashMap) {
        mSelectSpecHashMap = hashMap;
    }

    HashMap mSelectSpecPosHashMap;
    @Override
    public void onShopCarResPos(HashMap hashMap) {
        mSelectSpecPosHashMap = hashMap;
    }

    @Override
    public void onShopCarSelectString(String string) {

    }

    private int mSelectNumb;
    @Override
    public void onShopSpecRes(ShopSpecBean.DataBean specBean, int numb) {
        mSelectNumb = numb;
    }
    //
//    class OfferedDetailsPopupView extends BasePopupWindow implements View.OnClickListener {
//
//        private Context mContext;
//        private TextView vps_tv_shopprice, vps_tv_repertory, tv_popgroupondetails_dbuy, tv_popgroupondetails_tbuy;
//        private RecyclerView vps_rv;
//        private OfferedDetailsPopAdapter detailsPopAdapter;
//        private String goodsIds;
//        private OfferedDetailsBean.DataBean mDataBeans;
//        private ImageView vps_img_shop;
//        private ImageView bt_popdetails_close;
//        private SparseArray<Integer> mListSelectSpec = new SparseArray<>();
//        private String repertory;
//        private String shopId = "";
//        private ProgressBar vpg_progressbar;
//        private NormsBean.DataBean mSpecBean;
//        private OfferedDetailsPopAdapter.DetailsPopCall calls = new OfferedDetailsPopAdapter.DetailsPopCall() {
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
//        private void initView(){
//            vps_tv_shopprice = (TextView) findViewById(R.id.vps_tv_shopprice);
//            vps_rv = (RecyclerView) findViewById(R.id.vps_rv);
//            vps_img_shop = (ImageView) findViewById(R.id.vps_img_shop);
//            vps_tv_repertory = (TextView) findViewById(R.id.vps_tv_repertory);
//            bt_popdetails_close = (ImageView) findViewById(R.id.bt_popdetails_close);
//            tv_popgroupondetails_dbuy = (TextView) findViewById(R.id.tv_popgroupondetails_dbuy);
//            tv_popgroupondetails_tbuy = (TextView) findViewById(R.id.tv_popgroupondetails_tbuy);
//            vpg_progressbar = (ProgressBar)findViewById(R.id.vpg_progressbar);
//            tv_popgroupondetails_dbuy.setOnClickListener(this);
//            tv_popgroupondetails_tbuy.setOnClickListener(this);
//            bt_popdetails_close.setOnClickListener(this);
//            tv_popgroupondetails_dbuy.setVisibility(GONE);
//            tv_popgroupondetails_tbuy.setVisibility(View.VISIBLE);
//            tv_popgroupondetails_tbuy.setText(mDataBean.getAllowNum() + "人团");
//        }
//
//        public OfferedDetailsPopupView(final Activity context, OfferedDetailsBean.DataBean dataBeans, String goodsId) {
//            super(context);
//            this.mContext = context;
//            this.goodsIds = goodsId;
//            this.mDataBeans = dataBeans;
//            initView();
//            getSpec();
//        }
//        private void getSpec(){
//            HashMap hashMap = new HashMap<>();
//            List<OfferedDetailsBean.DataBean.GoodsBean.SpecBean> specBean = mDataBean.getGoods().getSpec();
//            if (mListSelectSpec == null || mListSelectSpec.size() == 0) {
//                for (int i = 0; i < specBean.size(); i++) {
//                    hashMap.put(specBean.get(i).getId() + "", specBean.get(i).getValue().get(0).getName() + "");
//                    mListSelectSpec.put(i,0);
//                }
//            } else {
//                for (int i = 0; i < specBean.size(); i++) {
//                    hashMap.put(specBean.get(i).getId() + "", specBean.get(i).getValue().get(mListSelectSpec.get(i)).getName() + "");
//                }
//            }
//
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
//                    vpg_progressbar.setVisibility(GONE);
//                    updateSpecInfo(dataBean);
//                }
//            });
//        }
//        private void updateSpecInfo(NormsBean.DataBean dataBean) {
//            vpg_progressbar.setVisibility(GONE);
//            mSpecBean = dataBean;
//            calculateCanBuyTag();
//            Glide.with(mContext).load(dataBean.getImg()).into(vps_img_shop);
//            vps_tv_repertory.setText("库存" + dataBean.getStoreNum() + "件");
//            repertory = dataBean.getStoreNum() + "";
//            vps_tv_shopprice.setText("¥" + mDataBean.getGoods().getPrice());
//            shopId = dataBean.getId() + "";
//            List<OfferedDetailsBean.DataBean.GoodsBean.SpecBean> list = mDataBeans.getGoods().getSpec();
//            detailsPopAdapter = new OfferedDetailsPopAdapter(mContext, list, mListSelectSpec, calls, mCurSelectNumb,mSpecBean.getLimitnum(),getCurMaxBuy());
//            vps_rv.setLayoutManager(new LinearLayoutManager(mContext));
//            vps_rv.setAdapter(detailsPopAdapter);
//        }
//
//        private String getCanSelectStr() {
//            if (!canBuy() && mSpecBean != null) {
//                if (mSpecBean.getStoreNum() == 0) {
//                    return  "暂无库存";
//                } else {
//                    return  "超过最大可团购数量";
//                }
//            }else{
//                return null;
//            }
//        }
//
//        private int mGroupCanMaxBuy;
//
//        //计算购买上限
//        private void calculateCanBuyTag(){
//            int limit = mSpecBean.getLimitnum();
//            int storeNumb = mSpecBean.getStoreNum();
//            int allowPayNumb = mSpecBean.getAllowMaxNum();
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
//            return mGroupCanMaxBuy;
//        }
//
//        /**
//         *
//         */
//        private boolean canBuy(){
//            return mCurSelectNumb <= mGroupCanMaxBuy;
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
//                case R.id.tv_popgroupondetails_tbuy:
//                    if (repertory.equals("0")) {
//                        showToast("库存不足，请选择其他规格。");
//                    } else {
//                        String str = getCanSelectStr();
//                        if (!TextUtils.isEmpty(str)){
//                            showToast(str);
//                            return;
//                        }
//                        YYMallApi.getGroupComfirm(OfferedDetailsActivity.this, shopId, groupId, mCurSelectNumb + "", new ApiCallback<GrouponSubBean.DataBean>() {
//                            @Override
//                            public void onStart() {
//
//                            }
//
//                            @Override
//                            public void onError(ApiException e) {
//                                showToast(e.getMessage());
//                            }
//
//                            @Override
//                            public void onCompleted() {
//
//                            }
//
//                            @Override
//                            public void onNext(GrouponSubBean.DataBean dataBean) {
//                                intent = new Intent(mContext, CheckOutActivity.class);
//                                intent.putExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.GROUPON);
//                                intent.putExtra("groupUserId", dataBean.getGroupUserId() + "");
//                                intent.putExtra("nums", mCurSelectNumb + "");
//                                intent.putExtra("productId", shopId);
//                                mContext.startActivity(intent);
//                            }
//                        });
//
//                        dismiss();
//                    }
//                    break;
//                default:
//                    break;
//
//            }
//        }
//    }

    public static String formatTime(int ms, int type) {

        int mi = 60;
        int hh = mi * 60;

        long hours = ms / hh;
        long minute = (ms - (hours * hh)) / mi;
        if (type == 0) {
            return hours + "";
        }
        return minute + "";
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


    @Override
    protected void onDestroy() {
        time = -2;
        super.onDestroy();
        UMShareAPI.get(this).release();
    }
}
