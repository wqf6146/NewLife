package com.yhkj.yymall.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.GrouponListBeans;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.http.api.ApiService;
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.popwindows.ShopClassifyPopView;

import java.util.List;

import butterknife.Bind;

import static android.view.View.GONE;

/**
 * Created by Administrator on 2017/8/1.
 */

public class GrouponListActivity extends BaseToolBarActivity implements ShopClassifyPopView.OnClassifyUpdate {

    @Bind(R.id.rv_grouponlist)
    RecyclerView rv_grouponlist;

    @Bind(R.id.fsc_refreshlayout)
    SmartRefreshLayout fsc_refreshlayout;


    private int mCurPager = 1;
    private GrouponGridAdapeter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grouponlist);
        setOnResumeRegisterBus(true);
    }

    @Override
    protected void initView() {
        super.initView();
        setImgRightResource(R.mipmap.details_dian);
        initRefreshView();
        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ShopClassifyPopView(GrouponListActivity.this).setOnClassifyUpdate(GrouponListActivity.this).showPopupWindow(v);
            }
        });
    }

    private void initRefreshView() {
        fsc_refreshlayout.setRefreshHeader(new YiYaHeaderView(this));
        fsc_refreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                fsc_refreshlayout.setLoadmoreFinished(false);
                mCurPager = 1;
                getData(true,refreshlayout,false);
            }
        });
        fsc_refreshlayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mCurPager++;
                getData(false,refreshlayout,false);
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
        mCurPager = 1;
        fsc_refreshlayout.setLoadmoreFinished(false);
        getData(null,null,true);
    }

    @Override
    protected void initData() {
        setTvTitleText("团购好货");
        setImgBackVisiable(View.VISIBLE);
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setStatusColor(getResources().getColor(R.color.theme_bule));
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

    private void getData(final Boolean pullOrLoadMore, final RefreshLayout refreshlayout, boolean bShow) {

        YYMallApi.getGroupList(GrouponListActivity.this, ApiService.GROUP_LISTS, mCurPager,mCategoryId, bShow,new YYMallApi.ApiResult<GrouponListBeans.DataBean>(GrouponListActivity.this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                showToast(e.getMessage());
                setNetWorkErrShow(View.VISIBLE);
                if (refreshlayout != null && pullOrLoadMore != null) {
                    if (pullOrLoadMore) {
                        refreshlayout.finishRefresh();
                    } else {
                        refreshlayout.finishLoadmore();
                    }
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(GrouponListBeans.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                if (refreshlayout == null){
                    if (dataBean.getList()!=null && dataBean.getList().size() == 0) {
                        setNoDataView(R.mipmap.ic_nor_orderbg,"暂无团购商品");
                    }else{
                        setData(dataBean);
                    }
                }else if (pullOrLoadMore!=null){
                    if (pullOrLoadMore){
                        refreshlayout.finishRefresh();
                        if (dataBean.getList()!=null && dataBean.getList().size() == 0) {
                            setNoDataView(R.mipmap.ic_nor_orderbg,"暂无团购商品");
                        }else{
                            setData(dataBean);
                        }
                    }else{
                        refreshlayout.finishLoadmore();
                        if (dataBean.getList()!=null && dataBean.getList().size() == 0){
                            mCurPager--;
                            refreshlayout.setLoadmoreFinished(true);
                        }else {
                            mAdapter.addData(dataBean.getList());
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }
        });
    }

    private void setData(GrouponListBeans.DataBean dataBean){
        if (mAdapter==null){
            final GridLayoutManager layoutManager = new GridLayoutManager(GrouponListActivity.this, 2);
            rv_grouponlist.setLayoutManager(layoutManager);
            final RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
            rv_grouponlist.setRecycledViewPool(viewPool);
            viewPool.setMaxRecycledViews(0, 20);
            GridLayoutHelper gridLayoutHelper = new GridLayoutHelper(2);
            gridLayoutHelper.setHGap(10);
            gridLayoutHelper.setVGap(10);
            mAdapter = new GrouponGridAdapeter(gridLayoutHelper, R.layout.item_grouponlist, dataBean);
            rv_grouponlist.setAdapter(mAdapter);
        }else{
            mAdapter.setData(dataBean.getList());
        }
    }

    class GrouponGridAdapeter extends DelegateAdapter.Adapter<GrouponGridViewHolder> {
        private GridLayoutHelper gridLayoutHelper;
        private int mCount = 0;
        private int mLayoutId;
        private List<GrouponListBeans.DataBean.ListBean> mDatas;
        public GrouponGridAdapeter(GridLayoutHelper gridLayoutHelper, int layoutId, GrouponListBeans.DataBean dataBeans) {
            this.gridLayoutHelper = gridLayoutHelper;
            mLayoutId = layoutId;
            mDatas = dataBeans.getList();
        }

        private void setData(List<GrouponListBeans.DataBean.ListBean> beanList){
            mDatas = beanList;
            notifyDataSetChanged();
        }

        private void addData(List<GrouponListBeans.DataBean.ListBean> beanList){
            int start = beanList.size();
            mDatas.addAll(beanList);
            notifyItemRangeChanged(start,mDatas.size());
//            notifyDataSetChanged();
        }

        @Override
        public LayoutHelper onCreateLayoutHelper() {
            return gridLayoutHelper;
        }

        @Override
        public GrouponGridViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            return new GrouponGridViewHolder(LayoutInflater.from(mContext).inflate(mLayoutId, parent, false));
        }

        @Override
        public void onBindViewHolder(GrouponListActivity.GrouponGridViewHolder holder, final int position) {
            final GrouponListBeans.DataBean.ListBean bean = mDatas.get(position);
            holder.ihg_tv_shopname_1.setText(bean.getName() + "");
            holder.ihg_tv_price_1.setText("¥" + bean.getPrice() + "");
            holder.ihg_tv_marketPrice_1.setText("¥" + bean.getMarketPrice() + "");
            holder.ihg_tv_marketPrice_1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.ihg_tv_sale_1.setText("已售" + bean.getSale() + "件");
            Glide.with(mContext).load(bean.getImg() + "").into(holder.ihg_img_shop_1);
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                    intent.putExtra("goodsId", bean.getGoodsId() + "");
                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

    }

    static class GrouponGridViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView ihg_tv_price_1, ihg_tv_shopname_1, ihg_tv_marketPrice_1, ihg_tv_sale_1;
        ImageView ihg_img_shop_1;

        public GrouponGridViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ihg_tv_price_1 = (TextView) view.findViewById(R.id.ihg_tv_price_1);
            ihg_tv_shopname_1 = (TextView) view.findViewById(R.id.ihg_tv_shopname_1);
            ihg_tv_marketPrice_1 = (TextView) view.findViewById(R.id.ihg_tv_marketPrice_1);
            ihg_tv_sale_1 = (TextView) view.findViewById(R.id.ihg_tv_sale_1);
            ihg_img_shop_1 = (ImageView) view.findViewById(R.id.ihg_img_shop_1);
        }

        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }
}
