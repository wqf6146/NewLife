package com.yhkj.yymall.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.log.ViseLog;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.util.StatusBarUtil;
import com.yhkj.yymall.BaseActivity;

import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.adapter.OrderDetailAdapter;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.GetServerBean;
import com.yhkj.yymall.bean.OrderDetailBean;
import com.yhkj.yymall.bean.OrderListBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.YiYaHeaderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


/**
 * Created by Administrator on 2017/7/4.
 */

public class    OrderDetailActivity extends BaseActivity implements YiYaHeaderView.OnRefreshLisiten {

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshView;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.aod_toolbar_bg)
    LinearLayout mToolBar;

    @Bind(R.id.vt_btn_left)
    ImageView mBtnBack;

    @Bind(R.id.vt_tv_title)
    TextView mTvTitle;

    @Bind(R.id.ao_progressbar)
    ProgressBar mProgressBar;

    private int mOrderId;
    private String mStatus;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail);
    }

    @Override
    protected void initView() {

//        StatusBarUtil.StatusBarLightMode(this);
        setStatusViewVisiable(false);
        mStatus = getIntent().getStringExtra("status");
        mOrderId = getIntent().getIntExtra("id",-1);
    }

    private int height = 0;// 滑动开始变色的高度
    private int overallXScroll = 0;
    private boolean mGraySet = false;
    @Override
    protected void bindEvent() {

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getInstance().finishActivity(OrderDetailActivity.this);
            }
        });
        mRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int scrollState) {}

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (height == 0){
                    height = mRecycleView.computeVerticalScrollRange() - mRecycleView.computeVerticalScrollExtent();
                }

                overallXScroll = overallXScroll + dy;// 累加y值 解决滑动一半y值为0
                if (overallXScroll <= 0) {   //设置标题的背景颜色
                    mToolBar.setBackgroundColor(Color.argb((int) 0, 0, 124, 209));
                } else if (overallXScroll > 0 && overallXScroll <= height) { //滑动距离小于banner图的高度时，设置背景和字体颜色颜色透明度渐变

//                    if (overallXScroll >= height/2){
//                        if (!mGraySet){
//                            mGraySet = true;
////                            mBtnBack.setImageResource(R.mipmap.ic_nor_gray_arrowleft);
////                            mTvTitle.setTextColor(getResources().getColor(R.color.grayfont));
//                        }
//                    }else{
//                        if (mGraySet){
//                            mGraySet = false;
////                            mBtnBack.setImageResource(R.mipmap.ic_nor_arrowleft);
////                            mTvTitle.setTextColor(getResources().getColor(R.color.white));
//                        }
//                    }

                    float scale = (float) overallXScroll / height;
                    float alpha = (255 * scale);
                    mToolBar.setBackgroundColor(Color.argb((int) alpha, 0, 124, 209));
                } else {
                    mToolBar.setBackgroundColor(Color.argb(255, 0, 124, 209));
                }
            }
        });
    }

    private String getOrderStatusString(OrderDetailBean.DataBean ordersBean){
        int orderStatus = ordersBean.getStatus();
        String status = "未知状态";
        switch (orderStatus){
            case 1:
                status = "等待付款";
                break;
            case 2:
                if (ordersBean.getType() == 3){
                    //拼团订单
                    if (ordersBean.getIsGroup() == 0)
                        status = "待成团";
                    else if (ordersBean.getIsGroup() == 1){
                        if (ordersBean.getDistributionStatus() == 0)
                            status = "待发货";
                        else if (ordersBean.getDistributionStatus() > 0 )
                            status = "待收货";
                    }
                }else {
                    if (ordersBean.getDistributionStatus() == 0)
                        status = "待发货";
                    else if (ordersBean.getDistributionStatus() > 0 )
                        status = "待收货";
                }
                break;
            case 3:
                status = "已取消";
                break;
            case 4:
                status = "已取消";
                break;
            case 5:
                if (ordersBean.getIsComment() == 0)
                    status = "待评价";
                else if (ordersBean.getIsComment() == 1)
                    status = "交易完成";
                break;
            case 6:
                status = "已退款";
                break;
            case 7:
                status = "已退款";
                break;
            case 8:
                status = "申请售后中";
                break;
        }
        return status;
    }


    @Override
    protected void initData() {

        mBtnBack.setImageResource(R.mipmap.login_back);
        mTvTitle.setText("订单详情");
        mTvTitle.setTextColor(getResources().getColor(R.color.white));
        initRefreshLayout();
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData(null);
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        YYMallApi.getService(this, new YYMallApi.ApiResult<GetServerBean.DataBean>(this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
            }

            @Override
            public void onNext(GetServerBean.DataBean dataBean) {
                mQqBean = dataBean;
            }

            @Override
            public void onStart() {

            }
        });
    }

    private GetServerBean.DataBean mQqBean;
    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        YYMallApi.getService(this, new YYMallApi.ApiResult<GetServerBean.DataBean>(this) {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
            }

            @Override
            public void onNext(GetServerBean.DataBean dataBean) {
                mQqBean = dataBean;
            }

            @Override
            public void onStart() {

            }
        });
        getData(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            setResult(RESULT_OK);
            mStatus = null;
            getData(null);
        }
    }

    private void getData(final RefreshLayout refreshlayout){
        if (mOrderId == -1)
            return;
        YYMallApi.getNewOrderDetail(this, String.valueOf(mOrderId), new YYMallApi.ApiResult<OrderDetailBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                mProgressBar.setVisibility(GONE);
                setNetWorkErrShow(VISIBLE);
                if (refreshlayout!=null)
                    refreshlayout.finishRefresh();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(final OrderDetailBean dataBean) {
                overallXScroll = 0;
                mProgressBar.setVisibility(GONE);
                if (refreshlayout!=null)
                    refreshlayout.finishRefresh();
                setNetWorkErrShow(GONE);
                if (dataBean.getCode() != 0){
                    ViseLog.e(dataBean.getMsg());
                    showToast(dataBean.getMsg());
                    return;
                }

                if (TextUtils.isEmpty(mStatus)){
                    mStatus = getOrderStatusString(dataBean.getData());
                }

                List<OrderDetailBean.DataBean> datas = new ArrayList<OrderDetailBean.DataBean>();
                datas.add(dataBean.getData());

                mRecycleView.setAdapter(new CommonAdapter<OrderDetailBean.DataBean>(OrderDetailActivity.this,R.layout.item_orderdetail,datas) {
                    @Override
                    protected void convert(ViewHolder holder, final OrderDetailBean.DataBean bean, int position) {
                        holder.setOnClickListener(R.id.iod_ll_lineserver, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //联系官方客服
                                if (CommonUtil.isQQClientAvailable(OrderDetailActivity.this) && mQqBean!=null && mQqBean.getInfo()!=null)
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mqqwpa://im/chat?chat_type=wpa&uin=" + mQqBean.getInfo().getQq() + "")));
                                else
                                    showToast("请先安装QQ");
                            }
                        });
                        if (bean.getTrace() == null){
                            holder.setVisible(R.id.iod_tv_logistictime,false);
                            holder.setVisible(R.id.iod_img_logisticright,false);
                        }else{
                            holder.setVisible(R.id.iod_tv_logistictime,true);
                            holder.setVisible(R.id.iod_img_logisticright,true);
                            holder.setText(R.id.iod_tv_logistictime,bean.getTrace().getAcceptTime());
                            holder.setText(R.id.iod_tv_logisticnew,bean.getTrace().getAcceptStation());
                            holder.setOnClickListener(R.id.iod_ll_logistic, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent  = new Intent(mContext,LogisticsdetailActivity.class);
                                    intent.putExtra("id",mOrderId);
                                    startActivity(intent);
                                }
                            });
                        }
                        holder.setText(R.id.iod_tv_orderstatus,mStatus);
                        holder.setText(R.id.iod_tv_username,bean.getAcceptName());
                        holder.setText(R.id.iod_tv_userphone,bean.getTelphone());
                        holder.setText(R.id.iod_tv_address,bean.getAddress());
                        holder.setText(R.id.iod_tv_merchantname,bean.getSellerName());

                        holder.setOnClickListener(R.id.io_tv_copy, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                // 将文本内容放到系统剪贴板里。
                                cm.setPrimaryClip(ClipData.newPlainText(null, bean.getOrderNo()));
                                showToast("复制成功");
                            }
                        });

                        if (bean.getYaya() == 0){
                            holder.setVisible(R.id.io_rl_useyaya,false);
                        }else{
                            holder.setText(R.id.iod_tv_useyaya,"- ¥" + bean.getYaya());
                        }

                        if (bean.getType() == 8){
                            //积分
                            holder.setVisible(R.id.io_rl_discount,false);
                            holder.setText(R.id.iod_tv_shopallprice,bean.getRealAmount() + "积分");
                            if (Double.parseDouble(bean.getFreight()) == 0.00){
                                //免运费 只显示积分
                                holder.setText(R.id.iod_tv_orderprice,bean.getRealAmount() + "积分");
                            }else{
                                holder.setText(R.id.iod_tv_orderprice,"¥" + bean.getFreight() + "+" + bean.getRealAmount() + "积分");
                            }
                        }else{
                            if (TextUtils.isEmpty(bean.getPromotions()) || Double.parseDouble(bean.getPromotions()) == 0.0d){
                                holder.setVisible(R.id.io_rl_discount,false);
                            }else{
                                holder.setText(R.id.iod_tv_shopdiscountprice,"- ¥" + bean.getPromotions());
                            }
                            holder.setText(R.id.iod_tv_shopallprice,"¥"+bean.getRealAmount());
                            holder.setText(R.id.iod_tv_orderprice,"¥"+bean.getPayableAmount());
                        }

                        if ( Double.parseDouble(bean.getFreight()) == 0.0d ){
                            holder.setVisible(R.id.io_rl_freight,false);
                        }else{
                            holder.setText(R.id.iod_tv_freightprice,"¥"+bean.getFreight());
                        }

                        holder.setText(R.id.iod_tv_ordernumb,String.format(getString(R.string.ordernumber),
                                bean.getOrderNo()));
                        holder.setText(R.id.iod_tv_ordertime,String.format(getString(R.string.ordertime),
                                bean.getCreateTime() == null ? "2017-6-29 10:20:00" : bean.getCreateTime() ));


                        NestFullListView nestFullListView = holder.getView(R.id.iod_listview);
                        nestFullListView.setAdapter(new NestFullListViewAdapter<OrderDetailBean.DataBean.GoodsesBean>(R.layout.item_order_entity,
                                bean.getGoodses()) {
                            @Override
                            public void onBind(int pos, final OrderDetailBean.DataBean.GoodsesBean goodsesBean, NestFullViewHolder holder) {
                                holder.setOnClickListener(R.id.ioe_ll_container, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                         if (goodsesBean.getType() == 0){
                                            //普通商品
                                            if (goodsesBean.getPanicBuyItemId() != 0){
                                                //限时抢购
                                                Intent intent = new Intent(mContext, TimeKillDetailActivity.class);
                                                intent.putExtra("id", String.valueOf(goodsesBean.getPanicBuyItemId()));
                                                startActivity(intent);
                                            }else{
                                                Intent intent = new Intent(mContext, CommodityDetailsActivity.class);
                                                intent.putExtra("goodsId", String.valueOf(goodsesBean.getGoodsId()));
                                                startActivity(intent);
                                            }
                                        }else if (goodsesBean.getType() == 1){
                                            //拼团商品
                                            Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                                            intent.putExtra("goodsId", String.valueOf(goodsesBean.getGoodsId()));
                                            startActivity(intent);
                                        }else if (goodsesBean.getType() == 2){
                                            //租赁商品
                                            Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                                            intent.putExtra("id", String.valueOf(goodsesBean.getGoodsId()));
                                            startActivity(intent);
                                        }else if (goodsesBean.getType() == 3){
                                            //折扣
                                            Intent intent = new Intent(mContext, DiscountDetailsActivity.class);
                                            intent.putExtra("goodsId", String.valueOf(goodsesBean.getGoodsId()));
                                            startActivity(intent);
                                        }else if (goodsesBean.getType() == 4){
                                            //积分
                                            Intent intent = new Intent(mContext, IntegralDetailActivity.class);
                                            intent.putExtra("id", String.valueOf(goodsesBean.getGoodsId()));
                                            startActivity(intent);
                                        }else if (goodsesBean.getType() == 6){
                                             //日常活动
                                             Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                                             intent.putExtra("goodsId", String.valueOf(goodsesBean.getGoodsId()));
                                             startActivity(intent);
                                         }
                                    }
                                });
                                // (bean.getStatus() ==2 || (bean.getStatus()==5 && bean.getIsComment() == 0) )&&
                                if (goodsesBean.getRefundmentStatus().equals("1")){
                                    holder.setVisible(R.id.ioe_tv_aftersale,true);
                                    holder.setOnClickListener(R.id.ioe_tv_aftersale, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            //售后
                                            Intent intent = new Intent(OrderDetailActivity.this,ApplyAfterMallActivity.class);
                                            intent.putExtra("orderGoodsId",goodsesBean.getId());
                                            startActivityForResult(intent,0);
                                        }
                                    });
                                }else{
                                    holder.getView(R.id.ioe_tv_aftersale).setVisibility(GONE);
                                }
                                Glide.with(OrderDetailActivity.this).load(goodsesBean.getGoodsImg()).into((ImageView) holder.getView(R.id.ioe_img_shopimg));
                                holder.setText(R.id.ioc_tv_shopname,goodsesBean.getGoodsName());
                                holder.setText(R.id.ioc_tv_shopdesc,goodsesBean.getGoodsSpec());

                                if (bean.getType() == 8){
                                    //积分
                                    holder.setText(R.id.ioc_tv_shopprice,String.valueOf(goodsesBean.getRealPrice()) + "积分");
                                }else{
                                    holder.setText(R.id.ioc_tv_shopprice,"¥"+goodsesBean.getRealPrice());
                                }

                                holder.setText(R.id.ioe_tv_count,"x" + goodsesBean.getGoodsNums());

                            }
                        });

                        //付款后显示申请售后
//                        if (bean.getStatus() ==2 || (bean.getStatus()==5 && bean.getIsComment() == 0)){
//                            holder.setVisible(R.id.iod_tv_aftersale,true);
//                        }
                        if (bean.getType() == 3){
                            //拼团订单
//                            holder.setVisible(R.id.iod_ll_tab,false);
                            if (bean.getIsGroup() == 1){
                                //已成团
                                if (bean.getDistributionStatus() == 1){
                                    //已发货
                                    holder.setVisible(R.id.iod_ll_tab,true);
                                    holder.setText(R.id.iod_tv_tab1,"查看物流");
                                    holder.setOnClickListener(R.id.iod_tv_tab1, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (bean.getTrace() == null){
                                                showToast("暂无物流信息");
                                                return;
                                            }
                                            Intent intent  = new Intent(mContext,LogisticsdetailActivity.class);
                                            intent.putExtra("id",mOrderId);
                                            startActivity(intent);
                                        }
                                    });
                                    holder.setText(R.id.iod_tv_tab2,"拼团详情");
                                    holder.setOnClickListener(R.id.iod_tv_tab2, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(OrderDetailActivity.this, MineGroupDetailActivity.class);
                                            intent.putExtra("groupid",bean.getGroupUserId());
                                            intent.putExtra("id", bean.getId());
                                            intent.putExtra("status",mStatus);
                                            if (bean.getGoodses()!=null && bean.getGoodses().size() > 0){
                                                intent.putExtra("img",bean.getGoodses().get(0).getGoodsImg());
                                                intent.putExtra("goodsid",bean.getGoodses().get(0).getGoodsId());
                                            }
                                            startActivity(intent);
                                        }
                                    });
                                    holder.setVisible(R.id.iod_tv_tab3,false);
                                }else{
                                    //待发货
                                    holder.setVisible(R.id.iod_ll_tab,true);
                                    holder.setText(R.id.iod_tv_tab1,"拼团详情");
                                    holder.setOnClickListener(R.id.iod_tv_tab1, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(OrderDetailActivity.this, MineGroupDetailActivity.class);
                                            intent.putExtra("groupid",bean.getGroupUserId());
                                            intent.putExtra("id", bean.getId());
                                            intent.putExtra("status",mStatus);
                                            if (bean.getGoodses()!=null && bean.getGoodses().size() > 0){
                                                intent.putExtra("img",bean.getGoodses().get(0).getGoodsImg());
                                                intent.putExtra("goodsid",bean.getGoodses().get(0).getGoodsId());
                                            }
                                            startActivity(intent);
                                        }
                                    });
                                    holder.getView(R.id.iod_tv_tab2).setVisibility(GONE);
                                    holder.getView(R.id.iod_tv_tab3).setVisibility(GONE);
                                }
                            }else{
                                //未成团
                                holder.setText(R.id.iod_tv_tab1,"拼团详情");
                                holder.setOnClickListener(R.id.iod_tv_tab1, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(OrderDetailActivity.this, MineGroupDetailActivity.class);
                                        intent.putExtra("groupid",bean.getGroupUserId());
                                        intent.putExtra("id", bean.getId());
                                        intent.putExtra("status",mStatus);
                                        if (bean.getGoodses()!=null && bean.getGoodses().size() > 0){
                                            intent.putExtra("img",bean.getGoodses().get(0).getGoodsImg());
                                            intent.putExtra("goodsid",bean.getGoodses().get(0).getGoodsId());
                                        }
                                        startActivity(intent);
                                    }
                                });
                                holder.getView(R.id.iod_tv_tab2).setVisibility(GONE);
                                holder.getView(R.id.iod_tv_tab3).setVisibility(GONE);
                            }
                        }else{

                            if (bean.getStatus() == 1 || bean.getStatus() == 4){
                                //待支付 已作废
                                holder.setVisible(R.id.iod_ll_tab,false);
                            }else if (bean.getStatus() == 2){
                                if (bean.getDistributionStatus() == 1){
                                    //待收货
                                    holder.setVisible(R.id.iod_ll_tab,true);
                                    holder.setText(R.id.iod_tv_tab1,"查看物流");

                                    holder.setOnClickListener(R.id.iod_tv_tab1, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (bean.getTrace() == null){
                                                showToast("暂无物流信息");
                                                return;
                                            }
                                            Intent intent  = new Intent(mContext,LogisticsdetailActivity.class);
                                            intent.putExtra("id",mOrderId);
                                            startActivity(intent);
                                        }
                                    });


                                    holder.getView(R.id.iod_tv_tab2).setVisibility(GONE);
                                    holder.getView(R.id.iod_tv_tab3).setVisibility(GONE);
                                }else{
                                    //待发货
                                    holder.setVisible(R.id.iod_ll_tab,false);
                                }
                            }else if (bean.getStatus() == 5){
//                                holder.setVisible(R.id.iod_tv_aftersale,true);
                                if (bean.getIsComment() == 0){
                                    //未评价
                                    holder.setVisible(R.id.iod_ll_tab,true);
                                    holder.setText(R.id.iod_tv_tab1,"查看物流");
                                    holder.setOnClickListener(R.id.iod_tv_tab1, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (bean.getTrace() == null){
                                                showToast("暂无物流信息");
                                                return;
                                            }
                                            Intent intent  = new Intent(mContext,LogisticsdetailActivity.class);
                                            intent.putExtra("id",mOrderId);
                                            startActivity(intent);
                                        }
                                    });
                                    holder.setText(R.id.iod_tv_tab2,"去评价");
                                    holder.setOnClickListener(R.id.iod_tv_tab2, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(mContext, ShopEvaluateActivity.class);
                                            intent.putExtra("orderid", String.valueOf(bean.getId()));
                                            startActivityForResult(intent,0);
                                        }
                                    });
                                    holder.getView(R.id.iod_tv_tab3).setVisibility(GONE);
                                }else{
                                    //已评价
                                    holder.setVisible(R.id.iod_ll_tab,true);
                                    holder.setText(R.id.iod_tv_tab1,"查看物流");
                                    holder.setOnClickListener(R.id.iod_tv_tab1, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (bean.getTrace() == null){
                                                showToast("暂无物流信息");
                                                return;
                                            }
                                            Intent intent  = new Intent(mContext,LogisticsdetailActivity.class);
                                            intent.putExtra("id",mOrderId);
                                            startActivity(intent);
                                        }
                                    });
                                    holder.getView(R.id.iod_tv_tab2).setVisibility(GONE);
                                    holder.getView(R.id.iod_tv_tab3).setVisibility(GONE);
                                }
                            }else{
                                holder.getView(R.id.iod_ll_tab).setVisibility(GONE);
                            }
                        }
                    }
                });
            }
        });
    }


    private Animation mAnimToolBarIn;
    private Animation mAnimToolBarOut;
    private Animation getAnimToolBarOut(){
        if (mAnimToolBarOut == null){
            mAnimToolBarOut = AnimationUtils.loadAnimation(this,R.anim.fade_out);
            mAnimToolBarOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mToolBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        return mAnimToolBarOut;
    }

    private Animation getAnimToolBarIn(){
        if (mAnimToolBarIn == null){
            mAnimToolBarIn = AnimationUtils.loadAnimation(this,R.anim.fade_in);
            mAnimToolBarIn.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mToolBar.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        return mAnimToolBarIn;
    }

    private void initRefreshLayout() {
        mRefreshView.setRefreshHeader(new YiYaHeaderView(this).setOnRefreshLisiten(this));
//        mRefreshView.setEnableOverScrollBounce(false);
        mRefreshView.setLoadmoreFinished(true);
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(refreshlayout);
            }
        });
    }

    private int mToolbarStatus = 0;
    @Override
    public void onStartPull() {
        if (mToolbarStatus == 0){
            mToolbarStatus = 1;
            mToolBar.startAnimation(getAnimToolBarOut());
        }
    }

    @Override
    public void onBackTop() {
        if (mToolbarStatus == 1){
            mToolbarStatus = 0;
            mToolBar.startAnimation(getAnimToolBarIn());
        }
    }
}
