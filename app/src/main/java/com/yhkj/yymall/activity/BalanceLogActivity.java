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
import com.yhkj.yymall.bean.BalanceLogBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/2.
 */

public class BalanceLogActivity extends BaseToolBarActivity {

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
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCurPage = 1;
                refreshlayout.setLoadmoreFinished(false);
                getData(true,refreshlayout);
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mCurPage++;
                getData(false,refreshlayout);
            }
        });
    }

    private String getTypeString(int type){
        String res = "";
        switch (type){
            case 0:
                res = "未知";
                break;
            case 1:
                res = "充值";
                break;
            case 2:
                res = "提现";
                break;
            case 3:
                res = "支付";
                break;
            case 4:
                res = "退款";
                break;
        }
        return res;
    }

    private int mCurPage = 1;
    private CommonAdapter mAdapter;
    @Override
    protected void initData() {
        setTvTitleText("余额明细");
        setToolBarColor(getResources().getColor(R.color.theme_bule));

        getData(null,null);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null,null);
    }

    private void getData(final Boolean pullorloadmore, final RefreshLayout refreshlayout) {
        YYMallApi.getBalanceLog(this, mCurPage, new YYMallApi.ApiResult<BalanceLogBean.DataBean>(this) {
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
            public void onNext(BalanceLogBean.DataBean dataBean) {
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
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无余额明细");
                    return;
                }

                mAdapter = new CommonAdapter<BalanceLogBean.DataBean.ListBean>(BalanceLogActivity.this,R.layout.item_log,dataBean.getList()) {
                    @Override
                    protected void convert(ViewHolder holder, final BalanceLogBean.DataBean.ListBean bean, int position) {
                        holder.setText(R.id.il_tv_title,getTypeString(bean.getType()));
                        holder.setText(R.id.il_tv_time,bean.getDatetime());
                        holder.setText(R.id.il_tv_balance,"余额：" + bean.getValue_log());
                        holder.setText(R.id.il_tv_dyn,String.valueOf(bean.getValue()));
                        holder.setVisible(R.id.il_tv_dyn,true);
                        holder.setOnClickListener(R.id.il_ll_container, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(BalanceLogActivity.this,PayListDetailActivity.class);
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
