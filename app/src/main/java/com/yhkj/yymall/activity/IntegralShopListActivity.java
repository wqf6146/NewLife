package com.yhkj.yymall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.ShopListBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.popwindows.ShopClassifyPopView;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/9/25.
 */

public class IntegralShopListActivity extends BaseToolBarActivity implements ShopClassifyPopView.OnClassifyUpdate {

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshView;

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
        setTitleWireVisiable(GONE);
        initRefreshLayout();
        setImgRightResource(R.mipmap.details_dian);
        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShopClassifyPopView(IntegralShopListActivity.this).setOnClassifyUpdate(IntegralShopListActivity.this).showPopupWindow(v);
            }
        });
    }
    Integer mCategoryId = null;
    @Override
    public void onClassifyUpdate(Integer categoryid) {
        if (categoryid == null || categoryid == 0)
            mCategoryId = null;
        else
            mCategoryId = categoryid;
        mCurPage = 1;
        mRefreshView.setLoadmoreFinished(false);
        getData(null,null,true);
    }
    private void initRefreshLayout() {
        mRefreshView.setRefreshHeader(new YiYaHeaderView(this));
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCurPage = 1;
                refreshlayout.setLoadmoreFinished(false);
                getData(true,refreshlayout,false);
            }
        });
        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mCurPage++;
                getData(false,refreshlayout,false);
            }
        });
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData(null,null,false);
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null,null,false);
    }

    private int mCurPage = 1;
    private void getData(final Boolean pullOrLoadMore, final RefreshLayout refreshlayout,boolean bShow) {
        YYMallApi.getIntegralShopList(this, mCurPage,mCategoryId,bShow, new ApiCallback<ShopListBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
                if (refreshlayout!=null && pullOrLoadMore!=null){
                    if (pullOrLoadMore){
                        refreshlayout.finishRefresh();
                    }else{
                        refreshlayout.finishLoadmore();
                    }
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ShopListBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                if (refreshlayout == null){
                    if (dataBean.getList()!=null && dataBean.getList().size() == 0) {
                        setNoDataView(R.mipmap.ic_nor_orderbg,"暂无积分商品");
                    }else{
                        setData(dataBean);
                    }
                }else if (pullOrLoadMore!=null){
                    if (pullOrLoadMore){
                        refreshlayout.finishRefresh();
                        if (dataBean.getList()!=null && dataBean.getList().size() == 0) {
                            setNoDataView(R.mipmap.ic_nor_orderbg,"暂无积分商品");
                        }else{
                            setData(dataBean);
                        }
                    }else{
                        refreshlayout.finishLoadmore();
                        if (dataBean.getList()!=null && dataBean.getList().size() == 0){
                            mCurPage--;
                            refreshlayout.setLoadmoreFinished(true);
                        }else {
                            mAdapter.addDatas(dataBean.getList());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private CommonAdapter mAdapter;
    private void setData(ShopListBean.DataBean dataBean) {
        if (mAdapter == null){
            mRecycleView.setLayoutManager(new GridLayoutManager(this,2));
            mRecycleView.addItemDecoration(new ItemOffsetDecoration(CommonUtil.dip2px(this,1)));
            mAdapter = new CommonAdapter<ShopListBean.DataBean.ListBean>(this,R.layout.item_integralshop,dataBean.getList()) {
                @Override
                protected void convert(ViewHolder holder, final ShopListBean.DataBean.ListBean bean, int position) {
                    Glide.with(IntegralShopListActivity.this).load(bean.getImg()).into((ImageView) holder.getView(R.id.ii_img_shopimg));
                    holder.setImageResource(R.id.ii_img_tag,R.mipmap.ic_nor_tagintegral);
                    holder.setText(R.id.ii_tv_shopname,bean.getName());
                    holder.setText(R.id.ii_tv_shopsale,"已售" + bean.getSale() + "件");
                    holder.setText(R.id.ii_tv_shopintegral,String.valueOf(Math.round(bean.getPrice())));
                    holder.setOnClickListener(R.id.ii_ll_container, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(IntegralShopListActivity.this,IntegralDetailActivity.class);
                            intent.putExtra("id",bean.getGoodsId() + "");
                            startActivity(intent);
                        }
                    });
                }
            };
            mRecycleView.setAdapter(mAdapter);
        }else{
            mAdapter.setDatas(dataBean.getList());
            mAdapter.notifyDataSetChanged();
        }
    }
    class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

        private int mItemOffset;

        public ItemOffsetDecoration(int itemOffset) {
            mItemOffset = itemOffset;
        }

        public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
            this(context.getResources().getDimensionPixelSize(itemOffsetId));
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
        }
    }
    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void initData() {
        setTvTitleText("积分商城");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
    }
}
