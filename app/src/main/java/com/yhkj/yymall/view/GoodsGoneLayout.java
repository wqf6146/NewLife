package com.yhkj.yymall.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.R;
import com.yhkj.yymall.adapter.GoodsLikeAdapter;
import com.yhkj.yymall.bean.GoodsLikeBean;
import com.yhkj.yymall.http.YYMallApi;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/9/7.
 */

public class GoodsGoneLayout extends FrameLayout {

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshView;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;

    public GoodsGoneLayout(Context context){
        this(context,null);
    }

    public GoodsGoneLayout(Context context, AttributeSet attributeSet){
        super(context,attributeSet);

        init();
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_refresh_recycleview,this);
        ButterKnife.bind(this,view);

        initData();
    }

    private void initData() {
        initRefreshLayout();
        getData();
    }

    private void initGoodsList(GoodsLikeBean.DataBean dataBean) {
        if (mAdapter == null) {
            final VirtualLayoutManager layoutManager = new VirtualLayoutManager(getContext());
            mRecycleView.setLayoutManager(layoutManager);
            final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
            mRecycleView.setRecycledViewPool(viewPool);
            viewPool.setMaxRecycledViews(0, 10);
            mAdapter = new GoodsLikeAdapter(getContext(), layoutManager,R.layout.item_goodsgone, dataBean){
                @Override
                public void initHeadView(GoodsLikeAdapter.CommonHolder holder, int position) {

                }
            };
            mRecycleView.setAdapter(mAdapter);
        } else {
            mAdapter.addItemList(dataBean);
        }
    }

    private int mCurPage = 1;
    private GoodsLikeAdapter mAdapter;
    private void initRefreshLayout() {
        mRefreshView.setEnableRefresh(false);
        mRefreshView.setEnableAutoLoadmore(true);
        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(final RefreshLayout refreshlayout) {
                mCurPage++;
                YYMallApi.getGoodsLike(getContext(), mCurPage, false, new ApiCallback<GoodsLikeBean.DataBean>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onError(ApiException e) {
                        refreshlayout.finishLoadmore();
                        Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(GoodsLikeBean.DataBean dataBean) {
                        if (dataBean.getList() != null && dataBean.getList().size() > 0) {
                            refreshlayout.finishLoadmore();
                            mAdapter.addItemList(dataBean);
                        } else {
                            refreshlayout.finishLoadmore();
                            refreshlayout.setLoadmoreFinished(true);
                        }
                    }
                });
            }
        });
    }

    private void getData() {
        YYMallApi.getGoodsLike(getContext(), mCurPage, false, new ApiCallback<GoodsLikeBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                if (!mShowDone && mLoadLisiten!=null)
                    mLoadLisiten.onLoadFaild();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(GoodsLikeBean.DataBean dataBean) {
                if (!mShowDone && mLoadLisiten!=null)
                    mLoadLisiten.onLoadSuccess();
                mShowDone = true;
                initGoodsList(dataBean);
            }
        });
    }

    private boolean mShowDone = false;
    private OnLoadDoneLisiten mLoadLisiten;

    public GoodsGoneLayout setLoadLisiten(OnLoadDoneLisiten loadLisiten) {
        this.mLoadLisiten = loadLisiten;
        return this;
    }

    public interface OnLoadDoneLisiten {
        void onLoadSuccess();
        void onLoadFaild();
    }

}
