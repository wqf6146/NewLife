package com.yhkj.yymall.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.popwindows.ShopClassifyPopView;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/9/25.
 */

public class OffPriceShopListActivity extends BaseToolBarActivity implements ShopClassifyPopView.OnClassifyUpdate{

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
                new ShopClassifyPopView(OffPriceShopListActivity.this).setOnClassifyUpdate(OffPriceShopListActivity.this).showPopupWindow(v);
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
        YYMallApi.getDiscoutnShopList(this, mCurPage,mCategoryId, bShow,new ApiCallback<ShopListBean.DataBean>() {
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
                        setNoDataView(R.mipmap.ic_nor_orderbg,"暂无折扣商品");
                    }else{
                        setData(dataBean);
                    }
                }else if (pullOrLoadMore!=null){
                    if (pullOrLoadMore){
                        refreshlayout.finishRefresh();
                        if (dataBean.getList()!=null && dataBean.getList().size() == 0) {
                            setNoDataView(R.mipmap.ic_nor_orderbg,"暂无折扣商品");
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
            mRecycleView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new CommonAdapter<ShopListBean.DataBean.ListBean>(this,R.layout.item_tagshop,dataBean.getList()) {
                @Override
                protected void convert(ViewHolder holder, final ShopListBean.DataBean.ListBean bean, int position) {
                    Glide.with(OffPriceShopListActivity.this).load(bean.getImg()).into((ImageView) holder.getView(R.id.is_img_shop));
                    holder.setImageResource(R.id.it_img_tag,R.mipmap.ic_nor_tagdiscount);
                    holder.setText(R.id.it_tv_shopname,bean.getName());
                    holder.setText(R.id.it_tv_tag,bean.getDiscount() + "折");
                    holder.setText(R.id.it_tv_sales,bean.getSale()+"人已抢");
                    holder.setText(R.id.it_tv_shopprice,"¥" + new java.text.DecimalFormat("#0.00").format(bean.getPrice()));
                    holder.setText(R.id.it_tv_shopsrcprice,"¥" + new java.text.DecimalFormat("#0.00").format(Double.parseDouble(bean.getMarketPrice())));
                    ((TextView)holder.getView(R.id.it_tv_shopsrcprice)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.setOnClickListener(R.id.it_ll_container, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(OffPriceShopListActivity.this,DiscountDetailsActivity.class);
                            intent.putExtra("goodsId",bean.getGoodsId() + "");
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

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void initData() {
        setTvTitleText("折扣促销");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
    }
}
