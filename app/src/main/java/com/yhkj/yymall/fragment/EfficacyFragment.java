package com.yhkj.yymall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.log.ViseLog;
import com.vise.xsnow.event.BusFactory;
import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.CommodityDetailsActivity;
import com.yhkj.yymall.activity.MainActivity;
import com.yhkj.yymall.adapter.EfficacyAdapter;
import com.yhkj.yymall.adapter.LoseEfficacyAdapter;
import com.yhkj.yymall.bean.RentRecordBean;
import com.yhkj.yymall.event.MainTabSelectEvent;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;

import butterknife.Bind;

/**
 * Created by Administrator on 2017/7/31.
 */

public class EfficacyFragment extends BaseFragment {

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;

    private String mCurPage = "1";
    private int mStatus; //0-未失效 1-失效


    public static EfficacyFragment getInstance(int status) {
        EfficacyFragment fragment = new EfficacyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("status",status);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.view_refresh_recycleview;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        mStatus = getArguments().getInt("status");
        initRefreshLayout();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        updateData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        updateData();
    }

    public void updateData(){
        YYMallApi.getRentRecord(_mActivity, String.valueOf(mStatus), mCurPage, new YYMallApi.ApiResult<RentRecordBean.DataBean>(_mActivity) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(RentRecordBean.DataBean dataBean) {
                setNetWorkErrShow(View.GONE);
                if (dataBean.getRentals() == null  || dataBean.getRentals().size() == 0){
                    setNetWorkErrShow(View.VISIBLE);
                    setNoDataView(R.mipmap.ic_nor_orderbg,"您暂无押金","逛逛热门租赁");
                    setNoDataBtnLisiten(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            YYApp.getInstance().setIndexShow(1);
                            startActivity(MainActivity.class);
                            AppManager.getInstance().finishExceptActivity(MainActivity.class);
                        }
                    });
                    return;
                }
                if (mStatus == 0)
                    mRecycleView.setAdapter(new EfficacyAdapter(_mActivity,dataBean.getRentals()));
                else
                    mRecycleView.setAdapter(new LoseEfficacyAdapter(_mActivity,dataBean.getRentals()));
            }
        });
    }

    @Override
    protected void initData() {

    }


    private void initRefreshLayout() {
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(_mActivity));
        mRefreshLayout.setLoadmoreFinished(true);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh();
            }
        });

        mRecycleView.setLayoutManager(new LinearLayoutManager(_mActivity));
    }

}
