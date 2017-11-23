package com.yhkj.yymall.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.activity.LogisticsdetailActivity;
import com.yhkj.yymall.activity.MainActivity;
import com.yhkj.yymall.activity.OrderDetailActivity;
import com.yhkj.yymall.activity.ShopEvaluateActivity;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.CommonBean;
import com.yhkj.yymall.bean.OrderChildBean;
import com.yhkj.yymall.event.MainTabSelectEvent;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.presenter.AllOrUnParListPresenter;
import com.yhkj.yymall.presenter.BehindThreeOrderListPresenter;
import com.yhkj.yymall.presenter.base.BaseOrderListPresenter;
import com.yhkj.yymall.util.OrderEntityDiff;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.YiYaHeaderView;

import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;
import static com.vise.utils.handler.HandlerUtil.runOnUiThread;
import static com.yhkj.yymall.http.api.ApiService.SHARE_SHOP_URL;

/**
 * Created by Administrator on 2017/7/3.
 */

public class OrderChildFragment extends BaseFragment implements BaseOrderListPresenter.BaseOrderListView<OrderChildBean.DataBean> {

    private int mType;
    private int mParentType = 1; //（1为全部，2为非租赁，3为租赁）
    private int mIsComment,mIsGroup;

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshView;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;

    private CommonAdapter<OrderChildBean.DataBean.OrdersBean> mOrderListAdapter;
    private BaseOrderListPresenter mOrderPresenter;
    java.text.DecimalFormat mTwoPointDf =new java.text.DecimalFormat("#0.00");

    public static OrderChildFragment getInstance(int type, int parentType) {
        OrderChildFragment fragment = new OrderChildFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE,type);
        bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.PTYPE,parentType);
        fragment.setArguments(bundle);
        return fragment;
    }

    private int mCurPager = 1;
    private int mStatus;  // 接口状态（空为全部1为代付款）
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
        mCurPager = 1;
        mRefreshView.setLoadmoreFinished(false);
        getData(true);
    }

    @Override
    public void getOrderListInfoSuc(OrderChildBean.DataBean dataBean) {
        setNetWorkErrShow(GONE);
        if (mRefreshView.getState() == RefreshState.Loading){
            mRefreshView.finishLoadmore();
            if (mOrderListAdapter!=null && dataBean.getOrders().size() > 0) {
                int start = mOrderListAdapter.getDatas().size();
                mOrderListAdapter.addDatas(dataBean.getOrders());
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
//        if (mCurPager == 1)
//            mRecycleView.scrollToPosition(0);
    }

    @Override
    public void getOrderListInfoFaild(ApiException e) {
        showToast(e.getMessage());
        if (mRefreshView.getState() == RefreshState.Loading){
            mRefreshView.finishLoadmore();
        }else if (mRefreshView.getState() == RefreshState.Refreshing){
            mRefreshView.finishRefresh();
        }
        setNetWorkErrShow(View.VISIBLE);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
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
    protected void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(_mActivity));
    }

    @Override
    public void onSupportVisible() {
        mCurPager = 1;
        getData(false);
    }

    private String getOrderStatusString(OrderChildBean.DataBean.OrdersBean ordersBean){
        // 等待成团 | 等待发货 已发货 | 待评价
        int disStatus = ordersBean.getDistributionStatus();// 0-待发货 1发货 2部分发货
        int idGroup = ordersBean.getIsGroup(); // 0-待成团 1
        int isComment = ordersBean.getIsComment(); //0-待评价 1
        String status = "";
        switch (mType){
            case Constant.TYPE_FRAGMENT_ORDER.UNGROUP:
                if (idGroup == 0)
                    status = "待成团";
                else if (idGroup == 1)
                    status = "已成团";
                break;
            case Constant.TYPE_FRAGMENT_ORDER.UNTAKE:
                if (disStatus == 0)
                    status = "待发货";
                else if (disStatus == 1)
                    status = "待收货";
                else if (disStatus == 2)
                    status = "部分发货";
                break;
            case Constant.TYPE_FRAGMENT_ORDER.UNEVALUATE:
                if (isComment == 0)
                    status = "待评价";
                else if (isComment == 1)
                    status = "已评价";
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCurPager = 1;
        getData(false);
    }

    private void setOrderData(OrderChildBean.DataBean dataBean){
        if (dataBean == null || dataBean.getOrders() == null || dataBean.getOrders().size() == 0) {
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

    private void setAdapterData(OrderChildBean.DataBean dataBean){
        if (mOrderListAdapter == null){
            mOrderListAdapter = new CommonAdapter<OrderChildBean.DataBean.OrdersBean>(_mActivity,R.layout.item_order_full,dataBean.getOrders()) {
                @Override
                protected void convert(ViewHolder holder, final OrderChildBean.DataBean.OrdersBean dataBean, int position) {
                    if (mType == Constant.TYPE_FRAGMENT_ORDER.UNEVALUATE && dataBean.getIsComment() == 0) {
                        //待评价
                        holder.setText(R.id.ioc_tv_tab1, "查看物流");
                        holder.setText(R.id.ioc_tv_tab2, "    评价    ");
                        holder.setVisible(R.id.ioc_tv_tab2, true);
                        holder.setOnClickListener(R.id.ioc_tv_tab1, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), LogisticsdetailActivity.class);
                                intent.putExtra("id", dataBean.getId());
                                startActivityForResult(intent,0);
                            }
                        });
                        holder.setOnClickListener(R.id.ioc_tv_tab2, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(getContext(), ShopEvaluateActivity.class);
                                intent.putExtra("orderid", String.valueOf(dataBean.getId()));
                                startActivityForResult(intent,0);
                            }
                        });
                        holder.setVisible(R.id.ioc_tv_tab3, false);
                    }else if (mType == Constant.TYPE_FRAGMENT_ORDER.UNTAKE){
                        if ( dataBean.getDistributionStatus() == 1 ){
                            //已发货
                            holder.setText(R.id.ioc_tv_tab1, "查看物流");
                            holder.setText(R.id.ioc_tv_tab2, "确认收货");
                            holder.setVisible(R.id.ioc_tv_tab1, true);
                            holder.setVisible(R.id.ioc_tv_tab2, true);
                            holder.setOnClickListener(R.id.ioc_tv_tab1, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getContext(), LogisticsdetailActivity.class);
                                    intent.putExtra("id",dataBean.getId());
                                    startActivityForResult(intent,0);
                                }
                            });
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
                                            YYMallApi.verifyTakeGoods(_mActivity, String.valueOf(dataBean.getId()),new YYMallApi.ApiResult<CommonBean>(_mActivity) {
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
                                                    intent.putExtra("orderid", String.valueOf(dataBean.getId()));
                                                    startActivityForResult(intent,0);
                                                }
                                            });
                                        }
                                    });
                                    builder.show();

                                }
                            });
                        }else {
//                        holder.setText(R.id.ioc_tv_tab1, "查看物流");
//                        holder.setOnClickListener(R.id.ioc_tv_tab1, new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent = new Intent(getContext(), LogisticsdetailActivity.class);
//                                intent.putExtra("id", String.valueOf(dataBean.getId()));
//                                startActivityForResult(intent,0);
//                            }
//                        });
                            holder.setVisible(R.id.ioc_tv_tab1, false);
                            holder.setVisible(R.id.ioc_tv_tab2, false);
                        }
                        holder.setVisible(R.id.ioc_tv_tab3, false);
                    }else if (mType == Constant.TYPE_FRAGMENT_ORDER.UNGROUP){
                        //等待发货 0-待成团 1
                        if (dataBean.getIsGroup() == 0){
                            holder.setVisible(R.id.ioc_tv_tab1, true);
                            holder.setText(R.id.ioc_tv_tab1, "取消订单");
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
                                            YYMallApi.cancelOrder(_mActivity, dataBean.getOrderGroupId(),new YYMallApi.ApiResult<CommonBean>(_mActivity) {
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
//                            holder.setText(R.id.ioc_tv_tab2, "修改地址");
//                            holder.setVisible(R.id.ioc_tv_tab2, true);
//                            holder.setOnClickListener(R.id.ioc_tv_tab2, new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent intent = new Intent(_mActivity, EditPlacesActivity.class);
//                                    intent.putExtra(Constant.TYPE_PLACES.TYPE, Constant.TYPE_PLACES.ORDER);
//                                    intent.putExtra("orderGroupId",dataBean.getOrderGroupId());
//                                    startActivity(intent);
//                                }
//                            });
                            holder.setText(R.id.ioc_tv_tab3, "喊人参团");
                            holder.setVisible(R.id.ioc_tv_tab3, true);
                            holder.setOnClickListener(R.id.ioc_tv_tab3, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new ShareAction(_mActivity)
                                            .setDisplayList(SHARE_MEDIA.SINA,SHARE_MEDIA.QQ,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE,
                                                    SHARE_MEDIA.QZONE)
                                            .setShareboardclickCallback(new ShareBoardlistener() {
                                                @Override
                                                public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                                    String url = SHARE_SHOP_URL + "#" + dataBean.getGoodses().get(0).getGoodsId();
                                                    if (share_media == SHARE_MEDIA.SINA){
                                                        UMImage image;
                                                        if (dataBean.getGoodses() !=null && dataBean.getGoodses().size() > 0)
                                                            image = new UMImage(_mActivity, dataBean.getGoodses().get(0).getGoodsImg());  //缩略图
                                                        else
                                                            image = new UMImage(_mActivity, R.mipmap.ic_nor_srcpic);  //缩略图
                                                        new ShareAction(_mActivity).setPlatform(SHARE_MEDIA.SINA).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧"+url).withMedia(image).setCallback(shareListener).share();
                                                    }else{
                                                        UMWeb web = new UMWeb(url);
                                                        if (dataBean.getGoodses() !=null && dataBean.getGoodses().size() > 0) {
                                                            web.setTitle(dataBean.getGoodses().get(0).getGoodsName());//标题
                                                            web.setThumb(new UMImage(_mActivity, dataBean.getGoodses().get(0).getGoodsImg()));  //缩略图
                                                        }else
                                                            web.setThumb( new UMImage(_mActivity, R.mipmap.ic_nor_srcpic));  //缩略图
                                                        web.setDescription("我在YiYiYaYa发现了一个不错的商品，快来看看吧");//描述
                                                        new ShareAction(_mActivity).withText("我在YiYiYaYa发现了一个不错的商品，快来看看吧").setPlatform(share_media).withMedia(web).setCallback(shareListener).share();
                                                    }
                                                }
                                            })
                                            .open();
                                }
                            });
                        } else if (dataBean.getIsGroup() == 1){
                            //已成团
                        }else{

                        }
                    }

                    final String status = getOrderStatusString(dataBean);
                    holder.setText(R.id.ioc_tv_merchantname, dataBean.getSellerName());
                    holder.setText(R.id.ioc_tv_orderstatus, status);
                    NestFullListView nestFullListView = holder.getView(R.id.iof_listview);
                    nestFullListView.setTag(dataBean.getId());
                    nestFullListView.setAdapter(new NestFullListViewAdapter<OrderChildBean.DataBean.OrdersBean.GoodsesBean>(R.layout.item_order_entity,
                            dataBean.getGoodses()) {
                        @Override
                        public void onBind(int pos, final OrderChildBean.DataBean.OrdersBean.GoodsesBean goodsesBean, NestFullViewHolder holder) {
                            Glide.with(_mActivity).load(goodsesBean.getGoodsImg()).into((ImageView) holder.getView(R.id.ioe_img_shopimg));
                            holder.setText(R.id.ioc_tv_shopname, goodsesBean.getGoodsName());
                            holder.setText(R.id.ioc_tv_shopdesc, goodsesBean.getGoodsSpec());
                            if (dataBean.getType() == 8){
                                //积分
                                holder.setText(R.id.ioc_tv_shopprice,String.valueOf(goodsesBean.getRealPrice()) + "积分");
                            }else{
                                holder.setText(R.id.ioc_tv_shopprice, "¥" + mTwoPointDf.format(Double.parseDouble(goodsesBean.getRealPrice())));
                            }
                            holder.setText(R.id.ioe_tv_count, "x" + String.valueOf(goodsesBean.getGoodsNums()));
                            holder.setVisible(R.id.ioe_tv_count, true);
                            holder.setVisible(R.id.ioe_tv_pay, false);
                            holder.setVisible(R.id.ioe_tv_addcar, false);
                        }
                    });
                    nestFullListView.setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                        @Override
                        public void onItemClick(NestFullListView parent, View view, int position) {
                            Intent intent = new Intent(_mActivity, OrderDetailActivity.class);
                            intent.putExtra("id", dataBean.getId());
                            intent.putExtra("status",status);
                            startActivity(intent);
                        }
                    });

                    if (dataBean.getType() == 8){
                        //积分
                        holder.setText(R.id.ioac_tv_sumstrings,String.format(getString(R.string.order_integralall_sumstring),
                                String.valueOf(getTotalCount(dataBean.getGoodses())),
                                String.valueOf(dataBean.getOrderAmount())));
                    }else{
                        holder.setText(R.id.ioac_tv_sumstrings,String.format(getString(R.string.order_all_sumstring),
                                String.valueOf(getTotalCount(dataBean.getGoodses())),
                                String.valueOf(dataBean.getOrderAmount())));
                    }

//                    holder.setText(R.id.ioac_tv_sumstrings, String.format(getString(R.string.order_all_sumstring),
//                            String.valueOf(getTotalCount(dataBean.getGoodses())),
//                            String.valueOf(dataBean.getOrderAmount())));
                }

                @Override
                protected void convert(ViewHolder holder, OrderChildBean.DataBean.OrdersBean ordersBean, int position, List<Object> payloads) {
                    Log.e("tag",position+"");
                }
            };
            mRecycleView.setAdapter(mOrderListAdapter);
        }else{
//            List<OrderChildBean.DataBean.OrdersBean> oldData = mOrderListAdapter.getDatas();
//            DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new OrderEntityDiff(dataBean.getOrders(), oldData), true);
//            diffResult.dispatchUpdatesTo(mOrderListAdapter);
//            oldData.clear();
//            oldData.addAll(dataBean.getOrders());
            mOrderListAdapter.setDatas(dataBean.getOrders());
            mOrderListAdapter.notifyDataSetChanged();
        }
    }

    private int getTotalCount(List<OrderChildBean.DataBean.OrdersBean.GoodsesBean> goodsesBeanListg){
        int count = 0;
        for (OrderChildBean.DataBean.OrdersBean.GoodsesBean goodsesBean : goodsesBeanListg){
            count += goodsesBean.getGoodsNums();
        }
        return count;
    }
}
