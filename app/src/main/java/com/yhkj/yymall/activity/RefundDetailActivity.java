package com.yhkj.yymall.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.RefundBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/8/15.
 */

public class RefundDetailActivity extends BaseToolBarActivity {

    @Bind(R.id.ar_tv_refundstatus)
    TextView mTvRefundStatus;

    @Bind(R.id.ar_tv_refundtime)
    TextView mTvRefundTime;

    @Bind(R.id.ar_tv_orderprice)
    TextView mTvOrderPrice;

    @Bind(R.id.ar_tv_refundprice)
    TextView mTvRefundPrice;

    @Bind(R.id.ar_rl_refundprice)
    RelativeLayout mRlRefundprice;

    @Bind(R.id.il_view_line_bottom_1)
    View mViewLine_1;

    @Bind(R.id.il_view_line_bottom_2)
    View mViewLine_2;



    @Bind(R.id.aa_tv_applyprogress_2)
    TextView mTvApplyPregress_2;


    @Bind(R.id.il_tv_time_1)
    TextView mTvTime_1;

    @Bind(R.id.il_tv_time_2)
    TextView mTvTime_2;

    @Bind(R.id.il_tv_time_3)
    TextView mTvTime_3;

//    @Bind(R.id.aa_tv_acount)
//    TextView mTvAcount;

//    @Bind(R.id.aa_tv_createtime)
//    TextView mTvCreateTime;

//    @Bind(R.id.aa_tv_applyno)
//    TextView mTvApplyNo;

//    @Bind(R.id.aa_ll_progress_2)
//    LinearLayout mLlProgress_2;

    @Bind(R.id.aa_ll_progress_3)
    LinearLayout mLlProgress_3;

    @Bind(R.id.aa_rl_progress)
    RelativeLayout mRlProgress;

    @Bind(R.id.ar_listview)
    NestFullListView mNestListView;


    @Bind(R.id.ar_tv_orderno)
    TextView mTvOrderNo;


    @Bind(R.id.ar_ll_lineserver)
    LinearLayout mLlLineServer;

    @Bind(R.id.ar_ll_phoneserver)
    LinearLayout mLlPhoneServer;

    //----------
    @Bind(R.id.ar_tv_servicemoney)
    TextView mTvServiceMoney;

    @Bind(R.id.ar_tv_serviceinfo)
    TextView mTvServiceInfo;


    @Bind(R.id.ar_tv_refundreson)
    TextView mTvRefundReson;

    @Bind(R.id.aa_tv_applyprogress_1)
    TextView mTvApplyPregress_1;

    @Bind(R.id.ar_tv_refundnores)
    TextView mTvRefundNoReson;


    @Bind(R.id.aa_tv_applyprogress_3)
    TextView mTvApplyPregress_3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refunddetail);
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

    private void setTypeInfo(int type){
        if (type == 0){
            //退货
            setTvTitleText("退货详情");
            mTvServiceMoney.setText("退货金额");
            mTvServiceInfo.setText("退货信息");
            mTvApplyPregress_1.setText("买家申请退货");
            mTvRefundNoReson.setText("退货驳回原因");
            mTvApplyPregress_3.setText("退货成功");
        }else {
            //换货
            setTvTitleText("换货详情");
            mTvServiceMoney.setVisibility(GONE);
            mTvServiceInfo.setText("换货信息");
            mTvApplyPregress_1.setText("买家申请换货");
            mTvRefundNoReson.setText("换货驳回原因");
            mTvApplyPregress_3.setText("换货成功");
        }

    }

    private void getData() {
        YYMallApi.getRefundDetail(this, getIntent().getStringExtra("id"), new YYMallApi.ApiResult<RefundBean.DataBean>(this) {
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
            public void onNext(RefundBean.DataBean dataBean) {
                setNetWorkErrShow(View.GONE);
                setUiData(dataBean);
            }
        });
    }

    private void setUiData(final RefundBean.DataBean dataBean) {
        setTypeInfo(dataBean.getType());
        mTvOrderNo.setText(dataBean.getOrderNo());
        if (dataBean.getOrderType() == 8){
            //积分
            if (Double.parseDouble(dataBean.getOrderAmount()) == 0){
                mTvOrderPrice.setText("¥0.00+" + dataBean.getIntegral() + "积分");
            }else{
                mTvOrderPrice.setText("¥" + dataBean.getOrderAmount() +"+"+ dataBean.getIntegral() + "积分");
            }
        }else{
            if (dataBean.getRefYaya() == 0){
                mTvOrderPrice.setText("¥" + dataBean.getOrderAmount());
            }else{
                mTvOrderPrice.setText("¥" + dataBean.getOrderAmount() +"+"+ dataBean.getYaya() + "丫丫");
            }
        }
        mTvRefundTime.setText(dataBean.getTime());
        if (dataBean.getType() == 0){
            //退货
            mTvRefundReson.setText("退货原因："+dataBean.getContent());
        }else{
            mTvRefundReson.setText("换货原因："+dataBean.getContent());
        }

        //售后状态,0:申请退款 1:退款失败 2:退款成功
        mRlProgress.setVisibility(View.VISIBLE);
        mLlLineServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CommonUtil.isQQClientAvailable(RefundDetailActivity.this))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + dataBean.getQq())));
                else
                    showToast("请先安装QQ");
            }
        });
        mLlPhoneServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + dataBean.getPhone());
                intent3.setData(data);
                startActivity(intent3);
            }
        });
        if (dataBean.getStatus() == 0){
            mTvRefundStatus.setText("商家处理中");
            mLlProgress_3.setVisibility(GONE);
            mViewLine_2.setVisibility(GONE);
            mTvTime_1.setText(dataBean.getTime());
            mTvTime_2.setText(dataBean.getTime());
        }else if (dataBean.getStatus() == 1){
            if (dataBean.getType() == 0){
                //退货
                mTvRefundStatus.setText("退货失败");
                mTvApplyPregress_3.setText("退货失败");
                mTvRefundNoReson.setText("退货驳回原因：" + dataBean.getDisposeIdea());
            }else{
                mTvRefundStatus.setText("换货失败");
                mTvApplyPregress_3.setText("换货失败");
                mTvRefundNoReson.setText("换货驳回原因：" + dataBean.getDisposeIdea());
            }
            mTvRefundNoReson.setVisibility(View.VISIBLE);
            mTvTime_1.setText(dataBean.getTime());
            mTvTime_2.setText(dataBean.getTime());
            mTvTime_3.setText(dataBean.getDisposeTime());
        }else if (dataBean.getStatus() == 2){
            if (dataBean.getType() == 0){
                //退货
                mTvRefundStatus.setText("退货成功");
                mTvApplyPregress_3.setText("退货成功");
                mRlRefundprice.setVisibility(View.VISIBLE);
                if (dataBean.getOrderType() == 8){
                    if (dataBean.getIntegral() != 0){
                        mTvRefundPrice.setText("¥" + dataBean.getAmount() + "+" + dataBean.getIntegral() + "积分");
                    }else{
                        mTvRefundPrice.setText("¥" + dataBean.getAmount());
                    }
                }else{
                    if (dataBean.getRefYaya() != 0){
                        mTvRefundPrice.setText("¥" + dataBean.getAmount() +"+"+ dataBean.getRefYaya() + "丫丫");
                    }else{
                        mTvRefundPrice.setText("¥" + dataBean.getAmount());
                    }
                }
            }else{
                mTvRefundStatus.setText("换货成功");
                mTvApplyPregress_3.setText("换货成功");
            }
            mTvTime_1.setText(dataBean.getTime());
            mTvTime_2.setText(dataBean.getTime());
            mTvTime_3.setText(dataBean.getDisposeTime());
        }

        mNestListView.setAdapter(new NestFullListViewAdapter<RefundBean.DataBean.OrderGoodsBean>(R.layout.item_order_entity,dataBean.getOrderGoods()) {
            @Override
            public void onBind(int pos, final RefundBean.DataBean.OrderGoodsBean bean, NestFullViewHolder holder) {
                Glide.with(RefundDetailActivity.this).load(bean.getGoodsImg()).into((ImageView) holder.getView(R.id.ioe_img_shopimg));
                holder.setText(R.id.ioc_tv_shopname,bean.getGoodsName());
                if (dataBean.getOrderType() == 8){
                    holder.setText(R.id.ioc_tv_shopprice,String.valueOf(bean.getIntegral()) +"积分");
                }else{
                    holder.setText(R.id.ioc_tv_shopprice,"¥"+bean.getGoodsPrice());
                }
                holder.setText(R.id.ioc_tv_shopdesc,bean.getGoodsSpec());
                holder.setText(R.id.ioe_tv_count,"x"+String.valueOf(bean.getGoodsNum()));
                holder.setOnClickListener(R.id.ioe_ll_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getGoodsType() == 0){
                            //普通商品
                            if (bean.getPanicBuyItemId() != 0){
                                //限时抢购
                                Intent intent = new Intent(mContext, TimeKillDetailActivity.class);
                                intent.putExtra("id", String.valueOf(bean.getPanicBuyItemId()));
                                startActivity(intent);
                            }else{
                                Intent intent = new Intent(mContext, CommodityDetailsActivity.class);
                                intent.putExtra("goodsId", String.valueOf(bean.getGoodsId()));
                                startActivity(intent);
                            }
                        }else if (bean.getGoodsType() == 1){
                            //拼团商品
                            Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                            intent.putExtra("goodsId", String.valueOf(bean.getGoodsId()));
                            startActivity(intent);
                        }else if (bean.getGoodsType() == 2){
                            //租赁商品
                            Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                            intent.putExtra("id", String.valueOf(bean.getGoodsId()));
                            startActivity(intent);
                        }else if (bean.getGoodsType() == 3){
                            //折扣
                            Intent intent = new Intent(mContext, DiscountDetailsActivity.class);
                            intent.putExtra("goodsId", String.valueOf(bean.getGoodsId()));
                            startActivity(intent);
                        }else if (bean.getGoodsType() == 4){
                            //积分
                            Intent intent = new Intent(mContext, IntegralDetailActivity.class);
                            intent.putExtra("id", String.valueOf(bean.getGoodsId()));
                            startActivity(intent);
                        }else if (bean.getGoodsType() == 6){
                            //日常活动
                            Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                            intent.putExtra("goodsId", String.valueOf(bean.getGoodsId()));
                            startActivity(intent);
                        }
                    }
                });

            }
        });
    }

    @Override
    protected void initView() {
        super.initView();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void initData() {
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTitleWireVisiable(GONE);
    }
}
