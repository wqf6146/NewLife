package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.MultiItemTypeAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.MesLogitBean;
import com.yhkj.yymall.bean.MesMyPriceBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/8/16.
 */

public class MesMyLogitAcitvity extends BaseToolBarActivity {

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_refresh_recycleview);
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData(null,null);
    }

    private void getData(final Boolean refreshOrLoadMore,final RefreshLayout refreshlayout) {
        YYMallApi.getMessageLogtisList(this, page, new YYMallApi.ApiResult<MesLogitBean.DataBean>(this) {
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
            public void onNext(MesLogitBean.DataBean dataBean) {
                setUiData(refreshOrLoadMore,refreshlayout,dataBean);

            }
        });
    }

    private void setEntityDadta(MesLogitBean.DataBean dataBean){
        if (mAdapter == null){
            mAdapter = new CommonAdapter<MesLogitBean.DataBean.ListBean>(this,R.layout.item_mes_logist,dataBean.getList()){
                @Override
                protected void convert(ViewHolder holder, final MesLogitBean.DataBean.ListBean listBean, int position) {
                    holder.setOnClickListener(R.id.iml_ll_container, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext,OrderDetailActivity.class);
                            intent.putExtra("id",listBean.getOrderId());
                            startActivity(intent);
                        }
                    });
                    //2=》发货通知 3-》收货通知
                    switch (listBean.getType()){
                        case 2:
                            holder.setText(R.id.iml_tv_status,"订单已发货");
                            break;
                        case 3:
                            holder.setText(R.id.iml_tv_status,"订单已完成");
                            break;
                    }

                    holder.setText(R.id.iml_tv_time,listBean.getTime());
                    Glide.with(MesMyLogitAcitvity.this).load(listBean.getImg()).into((ImageView)holder.getView(R.id.iml_img_shop));
                    holder.setText(R.id.iml_tv_shopname,listBean.getName());
                    holder.setText(R.id.iml_tv_logit,listBean.getFreightCompany() + "运单号：");
                    holder.setText(R.id.iml_tv_logitnumb,listBean.getDeliveryCode());
                }
            };
            mRecycleView.setAdapter(mAdapter);
        }else{
            mAdapter.setDatas(dataBean.getList());
        }
    }

    private CommonAdapter mAdapter;
    private void setUiData(Boolean refreshOrLoadMore,RefreshLayout refreshlayout,MesLogitBean.DataBean dataBean) {
        if (refreshlayout == null){
            //首次加载
            if (dataBean.getList() == null || dataBean.getList().size() == 0){
                setNoDataView(R.mipmap.ic_nor_orderbg,"暂无通知");
            }else{
                setNetWorkErrShow(View.GONE);
                setEntityDadta(dataBean);
            }
        }else{
            if (refreshOrLoadMore){
                //下拉刷新
                if (dataBean.getList() == null || dataBean.getList().size() == 0){
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无通知");
                }else {
                    setNetWorkErrShow(View.GONE);
                    refreshlayout.finishRefresh();
                    setEntityDadta(dataBean);
                }
            }else{
                //上啦加载
                if (dataBean.getList() !=null && dataBean.getList().size() > 0){
                    //有数据
                    refreshlayout.finishLoadmore();
                    mAdapter.addDatas(dataBean.getList());
                    mAdapter.notifyDataSetChanged();
                }else{
                    page--;
                    refreshlayout.finishLoadmore();
                    refreshlayout.setLoadmoreFinished(true);
                }
            }
        }

    }
    private int page = 1;
    private void initRefreshLayout() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(this));
        mRefreshLayout.setEnableOverScrollBounce(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mRefreshLayout.setLoadmoreFinished(false);
                page = 1;
                getData(true,refreshlayout);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                page++;
                getData(false,refreshlayout);
            }
        });
    }


    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null,null);
    }

    @Override
    protected void initView() {
        super.initView();
        initRefreshLayout();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void initData() {
        setTvTitleText("物流通知");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
    }
}
