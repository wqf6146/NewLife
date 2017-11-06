package com.yhkj.yymall.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vise.utils.assist.Check;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.AddressListBean;
import com.yhkj.yymall.bean.CheckOutBean;
import com.yhkj.yymall.bean.CheckOutOkBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.presenter.DailyPreOrderSettlePresenter;
import com.yhkj.yymall.presenter.DiscountPreOrderSettlePresenter;
import com.yhkj.yymall.presenter.GrouponPreOrderSettlePresenter;
import com.yhkj.yymall.presenter.IntegralPreOrderSettlePresenter;
import com.yhkj.yymall.presenter.base.BaseOrderSettlePresenter;
import com.yhkj.yymall.presenter.CommonPreOrderSettlePresenter;
import com.yhkj.yymall.presenter.LeasePreOrderSettlePresenter;
import com.yhkj.yymall.presenter.ShopCarPreOrderSettlePresenter;
import com.yhkj.yymall.presenter.TimekillPreOrderSettlePresenter;
import com.yhkj.yymall.presenter.DrawLotteSettlePresenter;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.SwitchView;
import com.yhkj.yymall.view.popwindows.FreightPopupView;
import com.yhkj.yymall.view.popwindows.PayYYPopup;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/7/4.
 */

public class CheckOutActivity extends BaseToolBarActivity implements BaseOrderSettlePresenter.BaseOrderSettleView {

    @Bind(R.id.ioc_checkout)
    NestFullListView ioc_checkout;

    @Bind(R.id.tv_chekout_yyfk)
    TextView tv_chekout_yyfk;

    @Bind(R.id.tv_chekout_person)
    TextView tv_chekout_person;

    @Bind(R.id.tv_chekout_adress)
    TextView tv_chekout_adress;

//    @Bind(R.id.tv_checkout_peisong)
//    TextView tv_checkout_peisong;

    //商品总价
//    @Bind(R.id.tv_checkout_allmoney)
//    TextView mTvGoodsPrice;

    @Bind(R.id.tv_checkout_amount)
    TextView tv_checkout_amount;

    //应付总价
    @Bind(R.id.tv_checkout_money)
    TextView mTvAllPayPrice;

    @Bind(R.id.ac_tv_preferential)
    TextView mTvPreferential;

    @Bind(R.id.img_chekout_xz)
    ImageView mImgCheckOutXieYi;

    @Bind(R.id.rl_checkout_adress)
    RelativeLayout rl_checkout_adress;

    @Bind(R.id.tv_checkout_submit)
    TextView tv_checkout_submit;

    @Bind(R.id.sv_checkout)
    SwitchView sv_checkout;

    @Bind(R.id.ac_ll_yayapay)
    LinearLayout mLlYayaPay;

    @Bind(R.id.ac_rl_xieyi)
    RelativeLayout mRlXieyi;

    @Bind(R.id.ac_tv_discount)
    TextView mTvDiscount;

    @Bind(R.id.ac_tv_cashtoyy)
    TextView mTvCashToYy;

    @Bind(R.id.ac_tv_cut)
    TextView mTvCut;

    private BaseOrderSettlePresenter mOrderPresenter;


    @Override
    public void submitOrderSuccess(CheckOutOkBean.DataBean bean) {

    }

    @Override
    public void submitOrderFaild() {

    }

    /**
     * 地址配送字符串 为空时可正常创建订单
     */
    private String mFreightStatusStr = "";
    private void getFreightStatusString(int code){
//        状态0=>正常 1=>不可达 2=>请选择收货地址 3=>商家没有配置配送方式
        switch (code){
            case 0:
                mFreightStatusStr = "";
            case 1:
                mFreightStatusStr = "该收货地址暂不支持配送，请重新选择收货地址";
                mFreightStatusStr = "该宝贝不支持销售到您选择的国家或区域";
                break;
            case 2:
                mFreightStatusStr = "请选择收货地址";
                break;
            case 3:
                mFreightStatusStr = "商家没有配置配送方式";
                break;
        }
    }

    SparseArray<Integer> mFreightIdArray;
    SparseArray<Double> mFreightPriceArray;
//    SparseArray<Double> mPromotionPriceArray;
//    SparseArray<Double> mMinusPriceArray; //货到付款的价格要减去
    private CheckOutBean.DataBean mDataBean;
    @Override
    public void getOrderInfo(final CheckOutBean.DataBean dataBean) {
        setNetWorkErrShow(GONE);
        mDataBean = dataBean;
        if (mDataBean.getMyYaya() == 0 || mDataBean.getYayaLimit() == 0){
            mLlYayaPay.setVisibility(GONE);
        }
        mFreightIdArray = new SparseArray<>();
        mFreightPriceArray = new SparseArray<>();
//        mPromotionPriceArray = new SparseArray<>();
//        mMinusPriceArray = new SparseArray<>();
        if (TextUtils.isEmpty(dataBean.getCut()))
            mTvCut.setVisibility(GONE);
        else
            mTvCut.setText(dataBean.getCut());
        tv_checkout_amount.setText(dataBean.getGoodsCount() + "");
        tv_chekout_yyfk.setText("剩余" + dataBean.getMyYaya() + "个，最多可用" + dataBean.getYayaLimit() + "个");
        sv_checkout.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                int maxYy;
                if (dataBean.getYayaLimit() > dataBean.getMyYaya()) {
                    maxYy = dataBean.getMyYaya();
                } else if (dataBean.getYayaLimit() < dataBean.getMyYaya()) {
                    maxYy = dataBean.getYayaLimit();
                } else {
                    maxYy = dataBean.getMyYaya();
                }
                PayYYPopup payYYPopup = new PayYYPopup(CheckOutActivity.this, yYcall, maxYy,dataBean.getMyYaya());
                payYYPopup.showPopupWindow();
                view.setOpened(false);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.setOpened(false);
                useYaya = 0;
                updateTotalPrice();
            }
        });

        ioc_checkout.setAdapter(new NestFullListViewAdapter<CheckOutBean.DataBean.SellersBean>(R.layout.item_chekout, dataBean.getSellers()) {
            @Override
            public void onBind(final int pos, final CheckOutBean.DataBean.SellersBean bean, final NestFullViewHolder pHolder) {
                if (pos == 0)
                    pHolder.getView(R.id.ic_view_topline).setVisibility(GONE);
                pHolder.setText(R.id.ic_tv_storename,bean.getName());
                if (bean.getCode() == 0 && bean.getFreight() != null && bean.getFreight().size() > 0){
                    pHolder.getView(R.id.ic_ll_freight).setVisibility(View.VISIBLE);
                    pHolder.getView(R.id.ic_ll_total).setVisibility(View.VISIBLE);
                    pHolder.setText(R.id.ic_tv_freightdesc,bean.getFreight().get(0).getName());
                    mFreightIdArray.put(bean.getId(),bean.getFreight().get(0).getDeliveryId());
                    mFreightPriceArray.put(bean.getId(),Double.parseDouble(bean.getFreight().get(0).getFreightPrice()));
                    pHolder.getView(R.id.ic_ll_freight).setTag(null);
                    if (bean.getFreight().size() > 1 ){
                        pHolder.setOnClickListener(R.id.ic_ll_freight, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //选择运费
                                FreightPopupView freightPopupView = new FreightPopupView<CheckOutBean.DataBean.SellersBean.FreightBean>(CheckOutActivity.this,bean.getFreight()) {
                                    @Override
                                    protected void bind(final ViewHolder holder, final CheckOutBean.DataBean.SellersBean.FreightBean freightBean, final int position) {
                                        holder.setText(R.id.if_tv_freight,freightBean.getName());
                                        Object tag = pHolder.getView(R.id.ic_ll_freight).getTag();
                                        if(tag!=null){
                                            if ((Integer)tag == position)
                                                holder.setImageResource(R.id.if_img_select,R.mipmap.ic_nor_bluenike);
                                            else
                                                holder.setImageResource(R.id.if_img_select,R.mipmap.ic_nor_graycicle);
                                        }else{
                                            if (position == 0)
                                                holder.setImageResource(R.id.if_img_select,R.mipmap.ic_nor_bluenike);
                                        }

                                        holder.setOnClickListener(R.id.if_rl_container, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mFreightBean = freightBean;
                                                pHolder.getView(R.id.ic_ll_freight).setTag(position);
                                                getAdapter().notifyDataSetChanged();
                                            }
                                        });
                                    }

                                    @Override
                                    protected void onSelectRes(CheckOutBean.DataBean.SellersBean.FreightBean freightBean) {
                                        if (freightBean == null) return;
                                        pHolder.setText(R.id.ic_tv_freightdesc,freightBean.getName());
                                        mFreightIdArray.put(bean.getId(),freightBean.getDeliveryId());

                                        mFreightPriceArray.put(bean.getId(),Double.parseDouble(freightBean.getFreightPrice()));
//                                        if (freightBean.getType() == 1){
//                                            //货到付款
////                                            mPromotionPriceArray.put(bean.getId(),bean.getPromotion());
//                                            Double discount = dataBean.getDiscount() * 0.1;
////                                            mMinusPriceArray.put(bean.getId(),bean.getSellerPrice() * discount);
//                                            if (mFreightPriceArray.get(bean.getId()) != null)
//                                                mFreightPriceArray.remove(bean.getId());
//                                        }else{
//
////                                            if (mPromotionPriceArray.get(bean.getId()) != null)
////                                                mPromotionPriceArray.remove(bean.getId());
////                                            if (mMinusPriceArray.get(bean.getId()) != null)
////                                                mMinusPriceArray.remove(bean.getId());
//                                        }
                                        if (mType == Constant.PREORDER_TYPE.INTEGRAL){
                                            pHolder.setText(R.id.ic_tv_totalprice,"¥" +
                                                    new java.text.DecimalFormat("#0.00").format( Double.parseDouble(freightBean.getFreightPrice()) ) + " + "+ (int)bean.getSellerPrice() + "积分");
                                        }else{
                                            Double goodsPrice = bean.getSellerPrice() + Double.parseDouble(freightBean.getFreightPrice());
                                            pHolder.setText(R.id.ic_tv_totalprice,"¥" +  new java.text.DecimalFormat("#0.00").format(goodsPrice));
                                        }
                                        updateTotalPrice();
                                    }
                                };
                                freightPopupView.showPopupWindow();
                            }
                        });
                    }else{
                        pHolder.getView(R.id.ic_img_freightarrow).setVisibility(GONE);
                    }
                    int goodsNumb = 0;
                    for (int i=0; i<bean.getGoodses().size(); i++){
                        CheckOutBean.DataBean.SellersBean.GoodsesBean goodsesBean = bean.getGoodses().get(i);
                        goodsNumb += Integer.parseInt(goodsesBean.getNums());
                    }
                    pHolder.setText(R.id.ic_tv_goodsnumb,"共" +goodsNumb+  "件商品 小计：");

                    if (mType == Constant.PREORDER_TYPE.INTEGRAL){
                        pHolder.setText(R.id.ic_tv_totalprice,"¥" +
                                new java.text.DecimalFormat("#0.00").format( Double.parseDouble(bean.getFreight().get(0).getFreightPrice()) ) + " + "+ (int)bean.getSellerPrice() + "积分");
                    }else{
                        Double goodsPrice = bean.getSellerPrice() + Double.parseDouble(bean.getFreight().get(0).getFreightPrice());
                        pHolder.setText(R.id.ic_tv_totalprice,"¥" + new java.text.DecimalFormat("#0.00").format( goodsPrice));
                    }
                }else{
                    //
                    getFreightStatusString(bean.getCode());
                    pHolder.setText(R.id.ic_tv_freightdesc,mFreightStatusStr);
                    pHolder.setText(R.id.ic_tv_storename,"失效宝贝");
                    pHolder.getView(R.id.ic_ll_freight).setVisibility(GONE);
                    pHolder.getView(R.id.ic_ll_total).setVisibility(GONE);
                    tv_checkout_submit.setBackgroundColor(getResources().getColor(R.color.grayline));
                }
//
                NestFullListView goodsListView = pHolder.getView(R.id.ic_listview_goods);
                goodsListView.setAdapter(new NestFullListViewAdapter<CheckOutBean.DataBean.SellersBean.GoodsesBean>(R.layout.item_check_goods,bean.getGoodses()) {
                    @Override
                    public void onBind(int pos, CheckOutBean.DataBean.SellersBean.GoodsesBean goodsesBean, NestFullViewHolder holder) {
                        Glide.with(CheckOutActivity.this).load(goodsesBean.getImg()).into((ImageView)holder.getView(R.id.icg_img_shopimg));
                        holder.setText(R.id.icg_tv_shopname,goodsesBean.getName());
                        if (bean.getCode() > 0){
                            holder.setText(R.id.icg_tv_shopdesc,mFreightStatusStr);
                            holder.setTextColor(R.id.icg_tv_shopname,getResources().getColor(R.color.grayfont_1_5));
                        }else{
                            holder.setText(R.id.icg_tv_shopdesc,goodsesBean.getSpec());
                        }

                        if (mType == Constant.PREORDER_TYPE.INTEGRAL){
                            holder.setText(R.id.icg_tv_shopprice,goodsesBean.getPrice() + "积分");
                        }else{
                            holder.setText(R.id.icg_tv_shopprice,"¥"+goodsesBean.getSell_price());
                        }
                        holder.setText(R.id.icg_tv_shopnumbs,"x"+goodsesBean.getNums());

                        if (goodsesBean.getGift()!=null && goodsesBean.getGift().size() > 0){
                            NestFullListView giftListView = holder.getView(R.id.icg_nestlistview);
                            giftListView.setAdapter(new NestFullListViewAdapter<CheckOutBean.DataBean.SellersBean.GoodsesBean.GiftBean>(R.layout.item_tv,goodsesBean.getGift()) {
                                @Override
                                public void onBind(int pos, CheckOutBean.DataBean.SellersBean.GoodsesBean.GiftBean giftBean, NestFullViewHolder holder) {
                                    holder.setText(R.id.it_tv_left,giftBean.getName());
                                    holder.setText(R.id.it_tv_right,"x" + giftBean.getNums());
                                    holder.getView(R.id.it_img_arrow).setVisibility(GONE);
                                }
                            });
                        }else{
                            holder.getView(R.id.icg_ll_gift).setVisibility(GONE);
                        }
                    }
                });

            }
        });
        mGoodsPrice = dataBean.getTotal();
        mTotalPrice = dataBean.getTotal();

        if (Double.parseDouble(dataBean.getPreferential()) == 0.0d){
            mTvPreferential.setVisibility(GONE);
        }else {
            mTvPreferential.setText("为您节省" + dataBean.getPreferential() + "元");
        }

        if (dataBean.getDiscount() == 10.0 || dataBean.getDiscount() == 0){
            mTvDiscount.setVisibility(GONE);
        }else{
            mTvDiscount.setText(dataBean.getGrade() + "等级会员购物享" + dataBean.getDiscount() + "折优惠");
        }

//        mTvGoodsPrice.setText("¥" + new java.text.DecimalFormat("#.00").format( dataBean.getTotal()));
        updateTotalPrice();
    }

    private void updateTotalPrice(){
        Double price = 0d;
        for (int i=0; i<mFreightPriceArray.size();i++){
            price += mFreightPriceArray.valueAt(i);
        }

//        for (int i=0; i<mPromotionPriceArray.size();i++){
//            price += mPromotionPriceArray.valueAt(i);
//        }
//
//        for (int i=0; i<mMinusPriceArray.size();i++){
//            price -= mMinusPriceArray.valueAt(i);
//        }

        if (mType != Constant.PREORDER_TYPE.INTEGRAL){
            price += mDataBean.getTotal();
            mTotalPrice = price - useYaya;
            mTvAllPayPrice.setText("¥" + new java.text.DecimalFormat("#0.00").format(mTotalPrice));
        }else{
            mTvAllPayPrice.setText("¥" + new java.text.DecimalFormat("#0.00").format(price));
        }
    }
    @Override
    public Activity getActivity() {
        return this;
    }

    private int mType;

    private String adressId, productId, nums, mPanicBuyId, groupUserId,discountId,integralId,dailyId;
    private AddressListBean.DataBean mAddressBean;
    private ApiCallback apiCallback = new YYMallApi.ApiResult<AddressListBean.DataBean >(CheckOutActivity.this) {
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
        public void onNext(final AddressListBean.DataBean dataBean) {
            int addresslist = dataBean.getList().size();
            mAddressBean = dataBean;
            if (addresslist == 0) {
                tv_chekout_adress.setVisibility(GONE);
                tv_chekout_person.setText("填写收货地址");
            } else {
                adressId = dataBean.getList().get(0).getId() + "";
                tv_chekout_adress.setVisibility(View.VISIBLE);
                tv_chekout_person.setText("收货人：" + dataBean.getList().get(0).getAccept_name() + " " + dataBean.getList().get(0).getMobile());
                tv_chekout_adress.setText(dataBean.getList().get(0).getProvince() + dataBean.getList().get(0).getCity() + dataBean.getList().get(0).getArea() + dataBean.getList().get(0).getStreet() + dataBean.getList().get(0).getAddress() + "");
            }
        }

    };

    private double mTotalPrice,mGoodsPrice; //mGoodsPrice-商品总额 mTotalPrice-总价
    private int useYaya = 0;
    private PayYYPopup.YYcall yYcall = new PayYYPopup.YYcall() {
        @Override
        public void send(int numb) {
//            mTvGoodsPrice.setText("¥" + new java.text.DecimalFormat("#.00").format( (mGoodsPrice - numb) ));
//            mTvAllPayPrice.setText("¥" + new java.text.DecimalFormat("#.00").format( (mTotalPrice - numb) ));
            if (numb != 0){
                sv_checkout.setOpened(true);
                useYaya = numb;
                updateTotalPrice();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
    }

    @Override
    public void getPreOrderFail(ApiException e) {
        setNetWorkErrShow(View.VISIBLE);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        mOrderPresenter.getOrderInfo(false,productId, nums, mPanicBuyId, groupUserId, TextUtils.isEmpty(adressId) ? null :
                Integer.parseInt(adressId),discountId,integralId,dailyId);
    }

    private void getOrderPresent() {
        mType = getIntent().getIntExtra(Constant.PREORDER_TYPE.TYPE, Constant.PREORDER_TYPE.COMMONSHOP);
        switch (mType) {
            case Constant.PREORDER_TYPE.COMMONSHOP:
                mOrderPresenter = new CommonPreOrderSettlePresenter(this);
                break;
            case Constant.PREORDER_TYPE.LEASESHOP:
                mOrderPresenter = new LeasePreOrderSettlePresenter(this);
                break;
            case Constant.PREORDER_TYPE.SHOPCAR:
                mOrderPresenter = new ShopCarPreOrderSettlePresenter(this);
                break;
            case Constant.PREORDER_TYPE.TIMEKILL:
                mOrderPresenter = new TimekillPreOrderSettlePresenter(this);
                break;
            case Constant.PREORDER_TYPE.GROUPON:
                mOrderPresenter = new GrouponPreOrderSettlePresenter(this);
                break;
            case Constant.PREORDER_TYPE.LOTTER:
                mOrderPresenter = new DrawLotteSettlePresenter(this);
                break;
            case Constant.PREORDER_TYPE.DISCOUNT:
                discountId = getIntent().getStringExtra("discountId");
                mOrderPresenter = new DiscountPreOrderSettlePresenter(this);
                break;
            case Constant.PREORDER_TYPE.INTEGRAL:
                integralId = getIntent().getStringExtra("integralId");
                mOrderPresenter = new IntegralPreOrderSettlePresenter(this);
                break;
            case Constant.PREORDER_TYPE.DAILY:
                dailyId = getIntent().getStringExtra("dailyId");
                mOrderPresenter = new DailyPreOrderSettlePresenter(this);
                break;
        }
    }

    @Override
    protected void initView() {
        super.initView();
        getOrderPresent();
        if (mType == Constant.PREORDER_TYPE.LOTTER || mType == Constant.PREORDER_TYPE.INTEGRAL ||  mType == Constant.PREORDER_TYPE.DISCOUNT ||
            mType == Constant.PREORDER_TYPE.LEASESHOP || mType == Constant.PREORDER_TYPE.TIMEKILL || mType == Constant.PREORDER_TYPE.GROUPON ||
                mType == Constant.PREORDER_TYPE.DAILY){
            mLlYayaPay.setVisibility(GONE);
        }
        if (mType == Constant.PREORDER_TYPE.LEASESHOP){
            mRlXieyi.setVisibility(View.VISIBLE);
            tv_checkout_submit.setBackgroundColor(getResources().getColor(R.color.grayline));
        }
//        else{
//            mImgCheckOutXieYi.setImageResource(R.mipmap.ic_nor_bluenike);
//        }
    }

    @Override
    public void onBackPressedSupport() {
        if (  mType ==  Constant.PREORDER_TYPE.LOTTER ){
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("取消确认");
            builder.setMessage("确定要放弃领取该商品吗？");
            builder.setPositiveButton("取消", null);
            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    AppManager.getInstance().finishActivity(CheckOutActivity.this);
                }
            });
            builder.show();
        }else{
            super.onBackPressedSupport();
        }
    }

    boolean mComfimXieYi = false;
    @Override
    protected void bindEvent() {
        super.bindEvent();
        mImgCheckOutXieYi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mComfimXieYi){
                    mComfimXieYi = false;
                    mImgCheckOutXieYi.setImageResource(R.mipmap.ic_nor_graycicle);
                    tv_checkout_submit.setBackgroundColor(getResources().getColor(R.color.grayline));
                }else{
                    mComfimXieYi = true;
                    mImgCheckOutXieYi.setImageResource(R.mipmap.ic_nor_bluenike);
                    tv_checkout_submit.setBackgroundColor(getResources().getColor(R.color.theme_bule));
                }
            }
        });
        mTvCashToYy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RentPladgeActivity.class);
//                Intent intent = new Intent(CheckOutActivity.this,WebActivity.class);
//                intent.putExtra("title","押金置换丫丫");
//                intent.putExtra(Constant.WEB_TAG.TAG, ApiService.YYWEB + Constant.WEB_TAG.YAJINZHIHUANYAYA);
//                startActivity(intent);
            }
        });
        mRlXieyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutActivity.this,WebActivity.class);
                intent.putExtra("title","租赁协议");
                intent.putExtra(Constant.WEB_TAG.TAG,ApiService.YYWEB + Constant.WEB_TAG.LEASE_XIYI);
                startActivity(intent);
            }
        });
        setImgBackLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (  mType == Constant.PREORDER_TYPE.LOTTER ){
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("取消确认");
                    builder.setMessage("确定要放弃领取该商品吗？");
                    builder.setPositiveButton("取消", null);
                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            AppManager.getInstance().finishActivity(CheckOutActivity.this);
                        }
                    });
                    builder.show();
                }else{
                    AppManager.getInstance().finishActivity(CheckOutActivity.this);
                }
            }
        });
        tv_checkout_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mType == Constant.PREORDER_TYPE.LEASESHOP && !mComfimXieYi){
                    showToast("请确认租赁协议");
                    return;
                }

                if (!TextUtils.isEmpty(mFreightStatusStr)){
                    showToast(mFreightStatusStr);
                    return;
                }
                if (adressId != null) {
                    mOrderPresenter.submitOrder(adressId, useYaya + "", productId, nums, mPanicBuyId, groupUserId,mFreightIdArray,discountId,integralId,dailyId);
                } else {
                    showToast("请添加收货地址");
                }
            }
        });
        rl_checkout_adress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckOutActivity.this, AddressManagerActivity.class);
                intent.putExtra(Constant.TYPE_PLACES.TYPE, Constant.TYPE_PLACES.SELECT);
                startActivityForResult(intent, Constant.TYPE_RELUST.CODE);
            }
        });
    }

    @Override
    protected void initData() {
        setTvTitleText("结算");
        setImgBackVisiable(View.VISIBLE);
        setImgRightVisiable(View.INVISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
        setTitleWireVisiable(GONE);
        productId = getIntent().getStringExtra("productId");
        nums = getIntent().getStringExtra("nums");
        groupUserId = getIntent().getStringExtra("groupUserId");
        mPanicBuyId = getIntent().getStringExtra("panicBuyId");
        mOrderPresenter.getOrderInfo(false,productId, nums, mPanicBuyId, groupUserId,null,discountId,integralId,dailyId);
        YYMallApi.getAddressList(CheckOutActivity.this, apiCallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.TYPE_RELUST.CODE && resultCode != RESULT_CANCELED) {
            adressId = resultCode + "";
            YYMallApi.getAddressList(CheckOutActivity.this, new YYMallApi.ApiResult<AddressListBean.DataBean>(CheckOutActivity.this) {
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
                public void onNext(AddressListBean.DataBean dataBean) {
                    dataBean.getList().size();
                    for (int i = 0; i < dataBean.getList().size(); i++) {
                        String id = dataBean.getList().get(i).getId();
                        if (id.equals(adressId)) {
                            tv_chekout_adress.setVisibility(View.VISIBLE);
                            tv_chekout_person.setText("收货人：" + dataBean.getList().get(i).getAccept_name() + " " + dataBean.getList().get(i).getMobile());
                            tv_chekout_adress.setText(dataBean.getList().get(i).getProvince() + dataBean.getList().get(i).getCity() + dataBean.getList().get(i).getArea() + dataBean.getList().get(i).getStreet() + dataBean.getList().get(i).getAddress() + "");
                        }
                    }
                }
            });
            mFreightStatusStr = "";
            tv_checkout_submit.setBackgroundColor(getResources().getColor(R.color.theme_bule));
            mOrderPresenter.getOrderInfo(true,productId, nums, mPanicBuyId, groupUserId,resultCode,discountId,integralId,dailyId);
        }
    }

}
