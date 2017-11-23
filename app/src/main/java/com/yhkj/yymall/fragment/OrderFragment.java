package com.yhkj.yymall.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.socialize.ShareAction;
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
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.CommodityDetailsActivity;
import com.yhkj.yymall.activity.EditPlacesActivity;
import com.yhkj.yymall.activity.LogisticsdetailActivity;
import com.yhkj.yymall.activity.MainActivity;
import com.yhkj.yymall.activity.OrderDetailActivity;
import com.yhkj.yymall.activity.PaymentAcitivity;
import com.yhkj.yymall.activity.ShopEvaluateActivity;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.OrderListBean;
import com.yhkj.yymall.event.MainTabSelectEvent;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.presenter.AllOrUnParListPresenter;
import com.yhkj.yymall.presenter.BehindThreeOrderListPresenter;
import com.yhkj.yymall.presenter.base.BaseOrderListPresenter;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.YiYaHeaderView;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.vise.utils.handler.HandlerUtil.runOnUiThread;
import static com.yhkj.yymall.http.api.ApiService.SHARE_SHOP_URL;

/**
 * Created by Administrator on 2017/7/3.
 */

public class OrderFragment extends BaseFragment implements BaseOrderListPresenter.BaseOrderListView<OrderListBean.DataBean> {

    private int mType;
    private int mParentType = 1; //（1为全部，2为非租赁，3为租赁）
    private int mIsComment,mIsGroup;

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshView;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;
    java.text.DecimalFormat mTwoPointDf =new java.text.DecimalFormat("#0.00");
    private CommonAdapter<OrderListBean.DataBean.OrderGroupBean> mOrderListAdapter;

    private BaseOrderListPresenter mOrderPresenter;

    public static OrderFragment getInstance(int type,int parentType) {
        OrderFragment fragment = new OrderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE,type);
        bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.PTYPE,parentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int mCurPager = 1;
    private int mStatus;  // 接口状态（空为全部1为代付款）
    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.view_refresh_recycleview;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mType = getArguments().getInt(Constant.TYPE_FRAGMENT_ORDER.TYPE);
        mParentType = getArguments().getInt(Constant.TYPE_FRAGMENT_ORDER.PTYPE);
        initPresenter();
        initRefreshLayout();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }


    public void updateType(int type){
        mParentType = type;
        mCurPager = 0;
        mRefreshView.setLoadmoreFinished(false);
        getData(true);

    }

    @Override
    public void getOrderListInfoSuc(OrderListBean.DataBean dataBean) {
        setNetWorkErrShow(GONE);
        if (mRefreshView.getState() == RefreshState.Loading){
            mRefreshView.finishLoadmore();
            if (mOrderListAdapter!=null && dataBean.getOrderGroup().size() > 0) {
                int start = mOrderListAdapter.getDatas().size();
                mOrderListAdapter.addDatas(dataBean.getOrderGroup());
                mOrderListAdapter.notifyItemChanged(start, mOrderListAdapter.getDatas().size());
            }else{
                mCurPager--;
                mRefreshView.setLoadmoreFinished(true);
            }
        }else if (mRefreshView.getState() == RefreshState.Refreshing){
            mRefreshView.finishRefresh();
            mRefreshView.setLoadmoreFinished(false);
            setOrderData(dataBean);
        }else{
            setOrderData(dataBean);
        }
    }



    @Override
    public void getOrderListInfoFaild(ApiException e) {
        showToast(e.getMessage());
        if (mRefreshView.getState() == RefreshState.Loading){
            mRefreshView.finishLoadmore();
        }else if (mRefreshView.getState() == RefreshState.Refreshing){
            mRefreshView.finishRefresh();
        }
        setNetWorkErrShow(VISIBLE);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(false);
    }

    private void initPresenter(){
        if (mType == Constant.TYPE_FRAGMENT_ORDER.ALL){
            mStatus = 0;
            mOrderPresenter = new AllOrUnParListPresenter(this);
        }else if (mType == Constant.TYPE_FRAGMENT_ORDER.UNPAY){
            mStatus = 1;
            mOrderPresenter = new AllOrUnParListPresenter(this);
        }else if (mType == Constant.TYPE_FRAGMENT_ORDER.UNGROUP){
            mStatus = 2;
            mIsComment = 0;
            mIsGroup = 0;
            mOrderPresenter = new BehindThreeOrderListPresenter(this);
        }else if (mType == Constant.TYPE_FRAGMENT_ORDER.UNTAKE){
            mStatus = 2;
            mIsComment = 0;
            mIsGroup = 1;
            mOrderPresenter = new BehindThreeOrderListPresenter(this);
        }else{
            //待评价
            mStatus = 5;
            mIsComment = 0;
            mIsGroup = 1;
            mOrderPresenter = new BehindThreeOrderListPresenter(this);
        }
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getData(false);
    }

    @Override
    protected void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(_mActivity));
    }

    private String getOrderStatusString(OrderListBean.DataBean.OrderGroupBean groupBean,
                                        OrderListBean.DataBean.OrderGroupBean.OrdersBean ordersBean){
        int groupStatus = groupBean.getStatus();
        int orderStatus = ordersBean.getStatus();
        String status = "未知状态";
        switch (orderStatus){
            case 1:
                status = "待付款";
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
                status = "部分退款";
                break;
            case 8:
                status = "申请售后中";
                break;
        }
        return status;
    }

    private void initRefreshLayout() {
        mRefreshView.setRefreshHeader(new YiYaHeaderView(_mActivity));
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCurPager = 1;
                getData(false);
            }
        });
        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mCurPager++;
                getData(false);
            }
        });
    }

    private void getData(boolean bshow){
        mOrderPresenter.getOrderList(bshow,mParentType,mStatus,mCurPager,mIsComment,mIsGroup);
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

    private void setOrderData(OrderListBean.DataBean dataBean){
        if (dataBean == null || dataBean.getOrderGroup() == null || dataBean.getOrderGroup().size() == 0) {
            setNoDataView(R.mipmap.ic_nor_orderbg,"您暂无订单","逛逛特卖");
            setNoDataBtnLisiten(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    YYApp.getInstance().setIndexShow(0);
                    startActivity(MainActivity.class);
                    AppManager.getInstance().finishExceptActivity(MainActivity.class);
                }
            });
        }else {
            setAdapterData(dataBean);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
            getData(false);
    }

    private void setAdapterData(OrderListBean.DataBean dataBean){
        mOrderListAdapter = new CommonAdapter<OrderListBean.DataBean.OrderGroupBean>(_mActivity,R.layout.item_order_all_coat,dataBean.getOrderGroup()) {
            @Override
            protected void convert(ViewHolder holder, final OrderListBean.DataBean.OrderGroupBean dataBean, int position) {
                NestFullListView nestFullListView = holder.getView(R.id.ioc_shoplistview);
                nestFullListView.setTag(dataBean.getId());

                nestFullListView.setAdapter(new NestFullListViewAdapter<OrderListBean.DataBean.OrderGroupBean.OrdersBean>(R.layout.item_order_all,
                        dataBean.getOrders() ) {
                    @Override
                    public void onBind(int pos, final OrderListBean.DataBean.OrderGroupBean.OrdersBean ordersBean, NestFullViewHolder holder) {

                        final String status = getOrderStatusString(dataBean,ordersBean);
                        holder.setText(R.id.ioc_tv_merchantname,ordersBean.getSellerName());
                        holder.setText(R.id.ioc_tv_orderstatus,status); //订单类型状态

                        if (dataBean.getStatus() != 1 ){
                            if (pos != dataBean.getOrders().size()-1 && dataBean.getOrders().size() > 1){
                                holder.setVisible(R.id.ioa_view_line,true);
                            }
                            holder.setVisible(R.id.ioa_ll_countstring,true);

                            if (ordersBean.getType() == 8){
                                //积分
                                holder.setText(R.id.ioa_tv_countstring,String.format(getString(R.string.order_integralall_sumstring),
                                        String.valueOf(ordersBean.getTotalNums()),
                                        String.valueOf(ordersBean.getTotalPrice())));
                            }else{
                                holder.setText(R.id.ioa_tv_countstring,String.format(getString(R.string.order_all_sumstring),
                                        String.valueOf(ordersBean.getTotalNums()),
                                        mTwoPointDf.format(ordersBean.getTotalPrice())));
                            }

//                            holder.setText(R.id.ioa_tv_countstring,String.format(getString(R.string.order_all_sumstring),
//                                    String.valueOf(ordersBean.getTotalNums()),
//                                    String.valueOf(ordersBean.getTotalPrice())));
                        }else{
//                            holder.setVisible(R.id.ioa_view_line,false);
                            holder.getView(R.id.ioa_view_line).setVisibility(GONE);
                            holder.getView(R.id.ioa_ll_countstring).setVisibility(GONE);
                        }

                        if (ordersBean.getStatus() == 1 || ordersBean.getStatus() == 4){
                            //待支付 已作废
//                            holder.setVisible(R.id.ioa_ll_tab,false);
                            holder.getView(R.id.ioa_ll_tab).setVisibility(GONE);
                        }else if (ordersBean.getStatus() == 2){

                            // 0普通订单,1团购订单,2限时抢购,3拼团,4租赁 7折扣 8积分
                            if (ordersBean.getType() == 3){
                                //拼团
                                if ( ordersBean.getIsGroup() == 1){
                                    //已成团

                                    if (ordersBean.getDistributionStatus() == 1){
                                        //已发货
                                        holder.setVisible(R.id.ioa_ll_tab,true);
                                        holder.setText(R.id.ioc_tv_tab1,"查看物流");
                                        holder.setOnClickListener(R.id.ioc_tv_tab1, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent =  new Intent(_mActivity,LogisticsdetailActivity.class);
                                                intent.putExtra("id",ordersBean.getId());
                                                startActivity(intent);
                                            }
                                        });
                                        holder.setText(R.id.ioc_tv_tab2,"确认收货");
                                        holder.setOnClickListener(R.id.ioc_tv_tab2, new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
                                                builder.setTitle("收货确认");
                                                builder.setMessage("是否确定收到货品？");
                                                builder.setPositiveButton("取消", null);
                                                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        YYMallApi.verifyTakeGoods(_mActivity, String.valueOf(ordersBean.getId()), new YYMallApi.ApiResult<CommonBean>(_mActivity) {
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
                                                                showToast("收货成功");
                                                                mCurPager = 1;
                                                                getData(true);
                                                                Intent intent = new Intent(getContext(), ShopEvaluateActivity.class);
                                                                intent.putExtra("orderid", String.valueOf(ordersBean.getId()));
                                                                startActivityForResult(intent,0);
                                                            }
                                                        });
                                                    }
                                                });
                                                builder.show();

                                            }
                                        });
                                        holder.getView(R.id.ioc_tv_tab3).setVisibility(GONE);;
                                    }else if (ordersBean.getDistributionStatus() == 0){
                                        //待发货
                                        holder.setVisible(R.id.ioa_ll_tab,false);
                                    }
                                }else {
                                    //待成团
                                    holder.getView(R.id.ioa_ll_tab).setVisibility(VISIBLE);
                                    holder.setText(R.id.ioc_tv_tab1,"取消订单");
                                    holder.setOnClickListener(R.id.ioc_tv_tab1, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
                                            builder.setTitle("取消确认");
                                            builder.setMessage("是否确定取消该订单？");
                                            builder.setPositiveButton("取消", null);
                                            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    YYMallApi.cancelOrder(_mActivity, dataBean.getId(), new YYMallApi.ApiResult<CommonBean>(_mActivity) {
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
                                                            getData(true);
                                                            showToast("取消成功");
                                                        }
                                                    });
                                                }
                                            });
                                            builder.show();
                                        }
                                    });
//                                    holder.setText(R.id.ioc_tv_tab2,"修改地址");
//                                    holder.setOnClickListener(R.id.ioc_tv_tab2, new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Intent intent = new Intent(_mActivity, EditPlacesActivity.class);
//                                            intent.putExtra(Constant.TYPE_PLACES.TYPE, Constant.TYPE_PLACES.ORDER);
//                                            intent.putExtra("orderGroupId",dataBean.getId());
//                                            startActivityForResult(intent,0);
//                                        }
//                                    });
                                    holder.setText(R.id.ioc_tv_tab2, "喊人参团");
                                    holder.setVisible(R.id.ioc_tv_tab2, true);
                                    holder.getView(R.id.ioc_tv_tab3).setVisibility(GONE);
                                    holder.setOnClickListener(R.id.ioc_tv_tab2, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new ShareAction(_mActivity)
                                                    .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE,
                                                            SHARE_MEDIA.QZONE)
                                                    .setShareboardclickCallback(new ShareBoardlistener() {
                                                        @Override
                                                        public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                                            String url = SHARE_SHOP_URL + "#" + ordersBean.getGoodses().get(0).getGoodsId();
                                                            UMWeb web = new UMWeb(url);
                                                            if (ordersBean.getGoodses() !=null && ordersBean.getGoodses().size() > 0) {
                                                                web.setTitle(ordersBean.getGoodses().get(0).getGoodsName());//标题
                                                                web.setThumb(new UMImage(_mActivity, ordersBean.getGoodses().get(0).getGoodsImg()));  //缩略图
                                                            }else
                                                                web.setThumb( new UMImage(_mActivity, R.mipmap.ic_nor_srcpic));  //缩略图
                                                            web.setDescription("我在YiYiYaYa发现了一个不错的商品，快来看看吧");//描述
                                                            new ShareAction(_mActivity).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧").setPlatform(share_media).withMedia(web).setCallback(shareListener).share();
                                                        }
                                                    })
                                                    .open();
                                        }
                                    });
                                }
                            }else{
                                if (ordersBean.getDistributionStatus() == 1 ){
                                    //已发货
                                    holder.setVisible(R.id.ioa_ll_tab,true);
                                    holder.setText(R.id.ioc_tv_tab1,"查看物流");
                                    holder.setOnClickListener(R.id.ioc_tv_tab1, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent =  new Intent(_mActivity,LogisticsdetailActivity.class);
                                            intent.putExtra("id",ordersBean.getId());
                                            startActivity(intent);
                                        }
                                    });
                                    holder.setText(R.id.ioc_tv_tab2,"确认收货");
                                    holder.setOnClickListener(R.id.ioc_tv_tab2, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
                                            builder.setTitle("收货确认");
                                            builder.setMessage("是否确定收到货品？");
                                            builder.setPositiveButton("取消", null);
                                            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    YYMallApi.verifyTakeGoods(_mActivity, String.valueOf(ordersBean.getId()), new YYMallApi.ApiResult<CommonBean>(_mActivity) {
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
                                                            showToast("收货成功");
                                                            mCurPager = 1;
                                                            getData(true);
                                                            Intent intent = new Intent(getContext(), ShopEvaluateActivity.class);
                                                            intent.putExtra("orderid", String.valueOf(ordersBean.getId()));
                                                            startActivityForResult(intent,0);
                                                        }
                                                    });
                                                }
                                            });
                                            builder.show();

                                        }
                                    });
                                    holder.getView(R.id.ioc_tv_tab3).setVisibility(GONE);
                                }
                            }




                        }else if (ordersBean.getStatus() == 5){
                            if (ordersBean.getIsComment() == 0){
                                //未评价
                                holder.setVisible(R.id.ioa_ll_tab,true);
                                holder.setText(R.id.ioc_tv_tab1,"查看物流");
                                holder.setOnClickListener(R.id.ioc_tv_tab1, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent =  new Intent(_mActivity,LogisticsdetailActivity.class);
                                        intent.putExtra("id",ordersBean.getId());
                                        startActivity(intent);
                                    }
                                });
                                holder.setText(R.id.ioc_tv_tab2,"    评价    ");
                                holder.setOnClickListener(R.id.ioc_tv_tab2, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getContext(), ShopEvaluateActivity.class);
                                        intent.putExtra("orderid", String.valueOf(ordersBean.getId()));
                                        startActivityForResult(intent,0);
                                    }
                                });
                                holder.getView(R.id.ioc_tv_tab3).setVisibility(GONE);
                            }else{
                                //已评价
                                holder.setVisible(R.id.ioa_ll_tab,true);
                                holder.setText(R.id.ioc_tv_tab1,"查看物流");
                                holder.setOnClickListener(R.id.ioc_tv_tab1, new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent =  new Intent(_mActivity,LogisticsdetailActivity.class);
                                        intent.putExtra("id",ordersBean.getId());
                                        startActivity(intent);
                                    }
                                });
                                holder.getView(R.id.ioc_tv_tab2).setVisibility(GONE);
                                holder.getView(R.id.ioc_tv_tab3).setVisibility(GONE);
                            }
                        }else{
                            holder.getView(R.id.ioa_ll_tab).setVisibility(GONE);
                        }

                        NestFullListView itemListView = holder.getView(R.id.ioa_shopitemlistview);
                        itemListView.setAdapter(new NestFullListViewAdapter<OrderListBean.DataBean.OrderGroupBean.OrdersBean.GoodsesBean>(R.layout.item_order_entity,
                                ordersBean.getGoodses()) {
                            @Override
                            public void onBind(int pos, OrderListBean.DataBean.OrderGroupBean.OrdersBean.GoodsesBean goodsesBean, NestFullViewHolder holder) {
                                Glide.with(_mActivity).load(goodsesBean.getGoodsImg()).into((ImageView) holder.getView(R.id.ioe_img_shopimg));
                                holder.setText(R.id.ioc_tv_shopname,goodsesBean.getGoodsName());
                                holder.setText(R.id.ioc_tv_shopdesc,goodsesBean.getGoodsSpec());
                                if (ordersBean.getType() == 8){
                                    //积分
                                    holder.setText(R.id.ioc_tv_shopprice,String.valueOf(goodsesBean.getRealPrice()) + "积分");
                                }else{
                                    holder.setText(R.id.ioc_tv_shopprice,"¥" + mTwoPointDf.format(Double.parseDouble(goodsesBean.getRealPrice())));
                                }
                                holder.setText(R.id.ioe_tv_count,"x" + String.valueOf(goodsesBean.getGoodsNums()));
                                holder.setVisible(R.id.ioe_tv_count,true);
                                holder.setVisible(R.id.ioe_tv_pay,false);
                                holder.setVisible(R.id.ioe_tv_addcar,false);

                            }
                        });
                        itemListView.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                            @Override
                            public void onItemClick(NestFullListView parent, View view, int position) {
                                Intent intent = new Intent(mContext,OrderDetailActivity.class);
                                intent.putExtra("id",ordersBean.getId());
                                intent.putExtra("status",status);
                                startActivityForResult(intent,0);
                            }
                        });
                    }
                });

//                if (dataBean.getStatus() != 1 && dataBean.getOrders().size() > 1 ){
//
//                }else{
//
//
//                }

                if (dataBean.getStatus() == 1){
                    //等待付款
                    holder.setVisible(R.id.ioac_ll_total,true);
                    holder.setText(R.id.ioac_tv_sumstrings,String.format(getString(R.string.order_all_sumstring),
                            String.valueOf(dataBean.getGroupNums()),
                            mTwoPointDf.format(dataBean.getGroupPrice())));

                    holder.setVisible(R.id.ioac_ll_tab,true);
                    holder.setText(R.id.ioac_tv_tab1,"取消订单");
                    holder.setOnClickListener(R.id.ioac_tv_tab1, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(_mActivity);
                            builder.setTitle("取消确认");
                            builder.setMessage("是否确定取消该订单？");
                            builder.setPositiveButton("取消", null);
                            builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    YYMallApi.cancelOrder(_mActivity, dataBean.getId(), new YYMallApi.ApiResult<CommonBean>(_mActivity) {
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
                                            getData(true);
                                            showToast("取消成功");
                                        }
                                    });
                                }
                            });
                            builder.show();
                        }
                    });
                    holder.setText(R.id.ioac_tv_tab3,"修改地址");
                    holder.setOnClickListener(R.id.ioac_tv_tab3, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(_mActivity, EditPlacesActivity.class);
                            intent.putExtra(Constant.TYPE_PLACES.TYPE, Constant.TYPE_PLACES.ORDER);
                            intent.putExtra("orderGroupId",dataBean.getId());
                            startActivityForResult(intent,0);
                        }
                    });
                    holder.setText(R.id.ioac_tv_tab2,"付款"+getFormatHMStext(dataBean.getEndTime()));
                    holder.setBackgroundRes(R.id.ioac_tv_tab2,R.drawable.stroke_square_red);
                    holder.setTextColor(R.id.ioac_tv_tab2,getResources().getColor(R.color.redfont));
                    holder.setOnClickListener(R.id.ioac_tv_tab2, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(_mActivity, PaymentAcitivity.class);
                            intent.putExtra("orderNo",String.valueOf(dataBean.getOrderGroupNo()));
                            intent.putExtra("total", String.valueOf(dataBean.getPayPrice()));
                            startActivity(intent);
                        }
                    });
                }else {
                    holder.setVisible(R.id.ioac_ll_total,false);
                    holder.setVisible(R.id.ioac_ll_tab,false);
                }

            }
        };
        mRecycleView.setAdapter(mOrderListAdapter);
    }

    private String getFormatHMStext(@NonNull int mills){
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        return formatter.format(mills * 1000);
    }
}
