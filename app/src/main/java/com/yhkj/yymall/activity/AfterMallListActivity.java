package com.yhkj.yymall.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.AfterMallListBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;
import com.yhkj.yymall.view.YiYaHeaderView;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/3.
 */

public class AfterMallListActivity extends BaseToolBarActivity {

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_refresh_recycleview);
    }

    @Override
    protected void initView() {
        super.initView();
        initRefreshLayout();
    }
    private void initRefreshLayout() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(this));
        mRefreshLayout.setLoadmoreFinished(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = 1;
                refreshlayout.setLoadmoreFinished(false);
                getData(true,refreshlayout);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPage++;
                getData(false,refreshlayout);
            }
        });
    }
    @Override
    protected void bindEvent() {
        super.bindEvent();
    }


    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null,null);
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData(null,null);
    }

    private int mPage = 1;
    private CommonAdapter mAdapter;
    private void getData(final Boolean refreshOrLoadmore,final RefreshLayout refreshlayout) {
        YYMallApi.getAfterMallList(this,mPage, new YYMallApi.ApiResult<AfterMallListBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
                if (refreshlayout!=null){
                    if (refreshOrLoadmore){
                        refreshlayout.finishRefresh();
                    }else{
                        refreshlayout.finishLoadmore();
                        mPage--;
                    }
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(final AfterMallListBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                if (refreshlayout!=null){
                    if (refreshOrLoadmore){
                        refreshlayout.finishRefresh();
                        mAdapter.setDatas(dataBean.getOrders());
                        mAdapter.notifyDataSetChanged();
                    }else{
                        refreshlayout.finishLoadmore();
                        if (dataBean.getOrders() == null || dataBean.getOrders().size() == 0){
                            refreshlayout.setLoadmoreFinished(true);
                            mPage--;
                        }else{
                            mAdapter.addDatas(dataBean.getOrders());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                    return;
                }

                if (dataBean.getOrders() == null || dataBean.getOrders().size() == 0){
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无售后订单");
                    return;
                }
                if (mAdapter == null){
                    mAdapter = new CommonAdapter<AfterMallListBean.DataBean.OrdersBean>(
                            AfterMallListActivity.this,R.layout.item_aftermall_shop,dataBean.getOrders()) {
                        @Override
                        protected void convert(ViewHolder holder, final AfterMallListBean.DataBean.OrdersBean ordersBean, int position) {
                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(AfterMallListActivity.this, RefundDetailActivity.class);
                                    intent.putExtra("id", String.valueOf(ordersBean.getId()));
                                    startActivity(intent);
                                }
                            });
                            holder.setText(R.id.ioc_tv_merchantname, ordersBean.getSellerName());
                            holder.setVisible(R.id.ias_tv_backprice, false);
                            if (ordersBean.getStatus() == 0) {
                                //申请中
                                holder.setText(R.id.ias_tv_topstatus, "处理中");
                                holder.setVisible(R.id.ias_tv_bottomstatus, false);
                            } else if (ordersBean.getStatus() == 1) {
                                //退款失败
                                holder.setText(R.id.ias_tv_topstatus, "交易关闭");
                                holder.setVisible(R.id.ias_tv_bottomstatus, true);
                                if (ordersBean.getType() == 0) {
                                    //退款
//                                    holder.setImageResource(R.id.ias_tv_bottomstatus, R.mipmap.ic_nor_tui);
                                    Drawable drawable= getResources().getDrawable(R.mipmap.ic_nor_tui);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    ((TextView)holder.getView(R.id.ias_tv_bottomstatus)).setCompoundDrawables(drawable,null,null,null);
                                    holder.setText(R.id.ias_tv_bottomstatus, "退货退款 退款失败");
                                } else {
                                    Drawable drawable= getResources().getDrawable(R.mipmap.ic_nor_redchange);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    ((TextView)holder.getView(R.id.ias_tv_bottomstatus)).setCompoundDrawables(drawable,null,null,null);
                                    holder.setText(R.id.ias_tv_bottomstatus, "换货 换货失败");
                                }
                            } else if (ordersBean.getStatus() == 2) {
                                //退款成功
                                holder.setText(R.id.ias_tv_topstatus, "交易关闭");
                                holder.setVisible(R.id.ias_tv_bottomstatus, true);
                                if (ordersBean.getType() == 0) {
                                    //退款
                                    holder.setText(R.id.ias_tv_bottomstatus, "退货退款 退款成功");
                                    Drawable drawable= getResources().getDrawable(R.mipmap.ic_nor_tui);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    ((TextView)holder.getView(R.id.ias_tv_bottomstatus)).setCompoundDrawables(drawable,null,null,null);
                                    holder.setVisible(R.id.ias_tv_backprice, true);
                                    if (ordersBean.getOrderType() == 8){
                                        //积分商品
                                        if (TextUtils.isEmpty(ordersBean.getAmount()))
                                            holder.setText(R.id.ias_tv_backprice, "退货金额：¥0.00+" + ordersBean.getIntegral() + "积分");
                                        else{
                                            Double price = Double.parseDouble(ordersBean.getAmount());
                                            if (price == 0.0d){
                                                holder.setText(R.id.ias_tv_backprice, "退货金额：¥0.00+" + ordersBean.getIntegral() + "积分");
                                            }else{
                                                holder.setText(R.id.ias_tv_backprice, "退货金额：¥" + ordersBean.getAmount() + "+" + ordersBean.getIntegral() + "积分");
                                            }
                                        }
                                    }else {
                                        if (ordersBean.getYaya()!=0)
                                            holder.setText(R.id.ias_tv_backprice, "退货金额：¥" + ordersBean.getAmount() + "+" + ordersBean.getYaya() + "丫丫");
                                        else{
                                            holder.setText(R.id.ias_tv_backprice, "退货金额：¥" + ordersBean.getAmount());
                                        }
                                    }
                                } else {
                                    holder.setText(R.id.ias_tv_bottomstatus, "换货 换货成功");
                                    Drawable drawable= getResources().getDrawable(R.mipmap.ic_nor_redchange);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    ((TextView)holder.getView(R.id.ias_tv_bottomstatus)).setCompoundDrawables(drawable,null,null,null);
                                }
                            }

                            ((NestFullListView) holder.getView(R.id.ias_listview)).setAdapter(new NestFullListViewAdapter<AfterMallListBean.DataBean.OrdersBean.OrderGoodsBean>(R.layout.item_aftermall_entry, ordersBean.getOrderGoods()) {
                                @Override
                                public void onBind(int pos, AfterMallListBean.DataBean.OrdersBean.OrderGoodsBean bean, NestFullViewHolder holder) {
                                    holder.setText(R.id.fn_tv_shopname_hor_2, bean.getGoodsName());
                                    Glide.with(AfterMallListActivity.this).load(bean.getGoodsImg()).into((ImageView) holder.getView(R.id.is_img_shop));
                                    holder.setText(R.id.ias_tv_desc, bean.getGoodsSpec());
                                    if (ordersBean.getOrderType() == 8){
                                        //积分商品
                                        holder.setText(R.id.fn_tv_shopprice_hor_2,bean.getIntegral() + "积分");
                                    }else {
                                        holder.setText(R.id.fn_tv_shopprice_hor_2, "¥" + bean.getGoodsPrice());
                                    }

                                    holder.setText(R.id.iae_tv_numb, "x" + bean.getGoodsNum());
                                }
                            });
                            ((NestFullListView) holder.getView(R.id.ias_listview)).setOnItemClickListener(new NestFullListView.OnItemClickListener() {
                                @Override
                                public void onItemClick(NestFullListView parent, View view, int position) {
                                    Intent intent = new Intent(AfterMallListActivity.this, RefundDetailActivity.class);
                                    intent.putExtra("id", String.valueOf(ordersBean.getId()));
                                    startActivity(intent);
                                }
                            });
                        }
                    };
                    mRecycleView.setAdapter(mAdapter);
                }else{
                    mAdapter.setDatas(dataBean.getOrders());
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void initData() {
        setTvTitleText("我的售后");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
    }
}
