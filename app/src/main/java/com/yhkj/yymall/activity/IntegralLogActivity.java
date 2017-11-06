package com.yhkj.yymall.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.yhkj.yymall.bean.IntLogBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/2.
 */

public class IntegralLogActivity extends BaseToolBarActivity {

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
        //(1:购买商品，2:签到，3:抽奖获得，4:抽奖消费，5:兑换代金券，6评论，7完善信息8，退款 9,后台操作10,积分兑换11,积分兑换退款）
        String res = "";
        switch (type){
            case 1:
                res = "购买商品";
                break;
            case 2:
                res = "签到";
                break;
            case 3:
                res = "抽奖获得";
                break;
            case 4:
                res = "抽奖消费";
                break;
            case 5:
                res = "兑换代金券";
                break;
            case 6:
                res = "6评论";
                break;
            case 7:
                res = "完善信息";
                break;
            case 8:
                res = "退款";
                break;
            case 9:
                res = "后台操作";
                break;
            case 10:
                res = "积分兑换";
                break;
            case 11:
                res = "积分兑换退款";
                break;
        }
        return res;
    }

    private int mCurPage = 1;
    private CommonAdapter mAdapter;
    @Override
    protected void initData() {
        setTvTitleText("积分明细");
        setToolBarColor(getResources().getColor(R.color.theme_bule));

        getData(null,null);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null,null);
    }


    private void getData(final Boolean pullorloadmore,final RefreshLayout refreshlayout) {
        YYMallApi.getInterLog(this, mCurPage, new YYMallApi.ApiResult<IntLogBean.DataBean>(this) {
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
            public void onNext(IntLogBean.DataBean dataBean) {
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
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无积分明细");
                    return;
                }

                mAdapter = new CommonAdapter<IntLogBean.DataBean.ListBean>(IntegralLogActivity.this,R.layout.item_log,dataBean.getList()) {
                    @Override
                    protected void convert(ViewHolder holder, IntLogBean.DataBean.ListBean bean, int position) {
                        holder.setText(R.id.il_tv_title,getTypeString(bean.getType()));
                        holder.setText(R.id.il_tv_time,bean.getDatetime());
                        holder.setText(R.id.il_tv_balance,bean.getIntro());
//                        holder.setText(R.id.il_tv_dyn,String.valueOf(bean.getValue()));
                    }
                };
                mRecycleView.setAdapter(mAdapter);
            }
        });
    }
}
