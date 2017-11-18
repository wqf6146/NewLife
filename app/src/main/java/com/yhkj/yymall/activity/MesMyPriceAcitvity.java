package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

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
import com.yhkj.yymall.bean.MesMyPriceBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/8/16.
 */

public class MesMyPriceAcitvity extends BaseToolBarActivity {

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
        YYMallApi.getMessageMyPriceList(this, page, new YYMallApi.ApiResult<MesMyPriceBean.DataBean>(this) {
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
            public void onNext(MesMyPriceBean.DataBean dataBean) {
                setUiData(refreshOrLoadMore,refreshlayout,dataBean);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        getData(null,null);
    }

    private void setEntityData(MesMyPriceBean.DataBean dataBean){
        if (mAdapter == null){
            mAdapter = new CommonAdapter<MesMyPriceBean.DataBean.ListBean>(this,R.layout.item_mes_myprice,dataBean.getList()){
                @Override
                protected void convert(ViewHolder holder, final MesMyPriceBean.DataBean.ListBean listBean, final int position) {
                    //类型 1：余额2积分3押金条4丫丫5经验
                    switch (listBean.getType()){
                        case 1:
                            holder.setOnClickListener(R.id.imm_ll_container, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    listBean.setStatus(1);
                                    notifyItemChanged(position);
                                    startActivityForResult(new Intent(MesMyPriceAcitvity.this,MyPriceActivity.class),0);
                                }
                            });
                            holder.setText(R.id.imm_tv_title,"余额变动通知");
                            break;
                        case 2:
                            holder.setOnClickListener(R.id.imm_ll_container, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    listBean.setStatus(1);
                                    notifyItemChanged(position);
                                    startActivityForResult(new Intent(MesMyPriceAcitvity.this,MyIntegralActivity.class),0);
                                }
                            });
                            holder.setText(R.id.imm_tv_title,"积分变动通知");
                            break;
                        case 3:
                            holder.setOnClickListener(R.id.imm_ll_container, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    listBean.setStatus(1);
                                    notifyItemChanged(position);
                                    startActivityForResult(new Intent(MesMyPriceAcitvity.this,PledgeActivity.class),0);
                                }
                            });
                            holder.setText(R.id.imm_tv_title,"押金条变动通知");
                            break;
                        case 4:
                            holder.setOnClickListener(R.id.imm_ll_container, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    listBean.setStatus(1);
                                    notifyItemChanged(position);
                                    startActivityForResult(new Intent(MesMyPriceAcitvity.this,MyYaYaActivity.class),0);
                                }
                            });
                            holder.setText(R.id.imm_tv_title,"丫丫变动通知");
                            break;
                        case 5:
                            holder.setOnClickListener(R.id.imm_ll_container, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    listBean.setStatus(1);
                                    notifyItemChanged(position);
                                    startActivityForResult(new Intent(MesMyPriceAcitvity.this,MyLevelsActivity.class),0);
                                }
                            });
                            holder.setText(R.id.imm_tv_title,"经验变动通知");
                            break;
                    }
                    holder.setText(R.id.imm_tv_content,listBean.getIntro());
                    holder.setText(R.id.imm_tv_time,listBean.getTime());

                    int unreadcolor = getResources().getColor(R.color.grayfont);
                    int readcolor = getResources().getColor(R.color.grayfont_1_5);
                    if(listBean.getStatus() == 0){
                        //未读
                        holder.setTextColor(R.id.imm_tv_title,unreadcolor);
                        holder.setTextColor(R.id.imm_tv_content,unreadcolor);
                        holder.setTextColor(R.id.imm_tv_time,unreadcolor);
                    }else{
                        holder.setTextColor(R.id.imm_tv_title,readcolor);
                        holder.setTextColor(R.id.imm_tv_content,readcolor);
                        holder.setTextColor(R.id.imm_tv_time,readcolor);
                    }
                }
            };
            mRecycleView.setAdapter(mAdapter);
        }else{
            mAdapter.setDatas(dataBean.getList());
            mAdapter.notifyDataSetChanged();
        }
    }

    private CommonAdapter mAdapter;
    private void setUiData(Boolean refreshOrLoadMore,RefreshLayout refreshlayout,MesMyPriceBean.DataBean dataBean) {
        if (refreshlayout == null){
            //首次加载
            if (dataBean.getList() == null || dataBean.getList().size() == 0){
                setNoDataView(R.mipmap.ic_nor_orderbg,"暂无通知");
            }else{
                setNetWorkErrShow(View.GONE);
                setEntityData(dataBean);
            }
        }else{
            if (refreshOrLoadMore){
                //下拉刷新
                if (dataBean.getList() == null || dataBean.getList().size() == 0){
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无通知");
                }else {
                    refreshlayout.finishRefresh();
                    setEntityData(dataBean);
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
        setTvTitleText("我的资产");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
    }
}
