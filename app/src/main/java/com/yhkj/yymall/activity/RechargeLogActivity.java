package com.yhkj.yymall.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.RechargeListBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/2.
 */

public class RechargeLogActivity extends BaseToolBarActivity {

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
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(this));
        mRefreshLayout.setLoadmoreFinished(true);
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getData(true,refreshlayout);
            }
        });
//
//        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
//            @Override
//            public void onLoadmore(RefreshLayout refreshlayout) {
//                mCurPage++;
//                getData(false,refreshlayout);
//            }
//        });
    }


    private int mCurPage = 1;
    private CommonAdapter mAdapter;
    @Override
    protected void initData() {
        setTvTitleText("充值明细");
        setToolBarColor(getResources().getColor(R.color.theme_bule));

        getData(null,null);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null,null);
    }

    private void getData(final Boolean pullorloadmore, final RefreshLayout refreshlayout) {
        YYMallApi.getRechargeList(this, new YYMallApi.ApiResult<RechargeListBean.DataBean>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                setNetWorkErrShow(VISIBLE);
                ViseLog.e(e);
                showToast(e.getMessage());

                if (refreshlayout!=null){
                     if (pullorloadmore){
                         refreshlayout.finishRefresh();
                     }else{
                         refreshlayout.finishLoadmore();
                         mCurPage--;
                     }
                }

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(RechargeListBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                if (refreshlayout!=null){
                    if (pullorloadmore){
                        refreshlayout.finishRefresh();
                        mAdapter.setDatas(dataBean.getList());
                        mAdapter.notifyDataSetChanged();
                    }else{
                        refreshlayout.finishLoadmore();
                        if (dataBean.getList() == null || dataBean.getList().size() == 0){
                            refreshlayout.setLoadmoreFinished(true);
                        }
                        mAdapter.addDatas(dataBean.getList());
                        mAdapter.notifyDataSetChanged();
                    }
                    return;
                }

                if (dataBean.getList()==null || dataBean.getList().size()==0){
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无充值明细");
                    return;
                }

                mAdapter = new CommonAdapter<RechargeListBean.DataBean.ListBean>(RechargeLogActivity.this,R.layout.item_log,dataBean.getList()) {
                    @Override
                    protected void convert(ViewHolder holder, final RechargeListBean.DataBean.ListBean bean, int position) {
                        holder.setText(R.id.il_tv_title,"充值");
                        holder.setText(R.id.il_tv_time,bean.getTime());
                        holder.setText(R.id.il_tv_balance,bean.getStatus() == 1 ? "成功" : "失败");
                        holder.setText(R.id.il_tv_dyn,bean.getAccount() + "元");
                        holder.setVisible(R.id.il_tv_dyn,true);
                        holder.setOnClickListener(R.id.il_ll_container, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(RechargeLogActivity.this,RechargeDetailActivity.class);
                                intent.putExtra("id",bean.getId());
                                startActivity(intent);
                            }
                        });
                    }
                };
                mRecycleView.setAdapter(mAdapter);
            }
        });
    }
}
