package com.yhkj.yymall.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.yhkj.yymall.BaseFragment;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.SeckillingActivity;
import com.yhkj.yymall.activity.TimeKillDetailActivity;
import com.yhkj.yymall.bean.TimeKillShopBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;

import butterknife.Bind;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/6/27.
 * 限时抢购商品列表
 */

public class ShopListFragment extends BaseFragment {

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshView;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;

    private String mCategoryId; //分类id
    private String mPaniclBuyId; //场次id
    private int mPage = 1;
    public static ShopListFragment getInstance() {
        ShopListFragment fragment = new ShopListFragment();
        fragment.setArguments(new Bundle());
        return fragment;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.view_refresh_recycleview;
    }

    public String getPaniclBuyId(){
        return mPaniclBuyId;
    }

    public void setPaniclBuyId(String paniclBuyId){
        mPaniclBuyId = paniclBuyId;
        getData(null,null);
    }

    public boolean isRefresh(){
        return mRefreshView.getState() == RefreshState.Refreshing;
    }

    @Override
    protected void initView(View contentView) {
        super.initView(contentView);
        initRefreshLayout();

        mCategoryId  = getArguments().getString("categoryId");
        mPaniclBuyId  = getArguments().getString("paniclBuyId");
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void initData() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(_mActivity));
        getData(null,null);
    }

    public void updateClassify(Integer categoryid){
        if (categoryid == null || categoryid == 0)
            mCategoryId = null;
        else
            mCategoryId = String.valueOf(categoryid);
        mPage = 1;
        mRefreshView.setLoadmoreFinished(false);
        getData(null,null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null,null);
    }

    public void startRefresh(){
        mPage = 1;
        mRefreshView.setLoadmoreFinished(false);
        getData(true,mRefreshView);
    }

    private void getData(final Boolean refreshOrLoadmore, final RefreshLayout refreshlayout) {
        YYMallApi.getTimeKillShopList(_mActivity, mCategoryId, mPaniclBuyId, String.valueOf(mPage),
                new YYMallApi.ApiResult<TimeKillShopBean.DataBean>(_mActivity) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                showToast(e.getMessage());
                if (e.getCode() == 6011){
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无活动");
//                    ((SeckillingActivity)_mActivity).getData();
                }else{
                    setNetWorkErrShow(VISIBLE);
                }
                if (refreshlayout!=null){
                    if (refreshOrLoadmore)
                        refreshlayout.finishRefresh();
                    else {
                        refreshlayout.finishLoadmore();
                        mPage--;
                    }
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(TimeKillShopBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                if (refreshlayout != null){
                    if (refreshOrLoadmore)
                        refreshlayout.finishRefresh();
                    else {
                        refreshlayout.finishLoadmore();
                        if (dataBean == null || dataBean.getList() == null || dataBean.getList().size() == 0){
                            refreshlayout.setLoadmoreFinished(true);
                            mPage--;
                        }else{
                            addData(dataBean);
                        }
                        return;
                    }
                }
                setData(dataBean);
            }
        });
    }

    private void addData(TimeKillShopBean.DataBean dataBean) {
        if (mAdapter != null){
            mAdapter.addDatas(dataBean.getList());
            mAdapter.notifyDataSetChanged();
        }
    }

    private CommonAdapter mAdapter;
    private void setData(TimeKillShopBean.DataBean dataBean) {
        if (mAdapter == null){
            mAdapter = new CommonAdapter<TimeKillShopBean.DataBean.ListBean>(_mActivity,R.layout.item_timekill_shop,dataBean.getList()){
                @Override
                protected void convert(ViewHolder holder, final TimeKillShopBean.DataBean.ListBean bean, int position){
                    Glide.with(_mActivity).load(bean.getPic()).placeholder(R.mipmap.ic_nor_srcpic).into((ImageView)holder.getView(R.id.is_img_shop));
                    holder.setText(R.id.fn_tv_shopname_hor_2,bean.getName());
                    holder.setText(R.id.its_tv_sales,"已售" + bean.getSale() + "件");
                    holder.setText(R.id.its_tv_storenumb,"仅剩" + bean.getStoreNum() + "件");
                    holder.setText(R.id.fn_tv_shopprice_hor_2, "¥" + bean.getActivePrice());
                    holder.setText(R.id.is_shop_grayprice, "¥" + bean.getMarketPrice());
                    ((TextView)holder.getView(R.id.is_shop_grayprice)).getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    if (bean.getStatus() == 2){
                        holder.setBackgroundRes(R.id.is_shop_buy,R.drawable.tv_bg_circle_gray_1);
                        holder.setText(R.id.is_shop_buy,"即将开始");
                        holder.setOnClickListener(R.id.fn_ll_hor_2, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, TimeKillDetailActivity.class);
                                intent.putExtra("id",String.valueOf(bean.getPaniclBuyItemId()));
                                intent.putExtra("status",-1);
                                startActivity(intent);
                            }
                        });
                    }else{
                        holder.setBackgroundRes(R.id.is_shop_buy,R.drawable.tv_bg_circle_deepblue);
                        holder.setText(R.id.is_shop_buy,"立即抢购");
                        holder.setOnClickListener(R.id.fn_ll_hor_2, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(mContext, TimeKillDetailActivity.class);
                                intent.putExtra("id",String.valueOf(bean.getPaniclBuyItemId()));
                                startActivity(intent);
                            }
                        });
                    }
                    MaterialProgressBar progressBar = holder.getView(R.id.its_progressbar);
                    progressBar.setProgress(Math.round((float)bean.getSale() / (bean.getStoreNum() + bean.getSale()) * 100));
                }
            };
            mRecycleView.setAdapter(mAdapter);
        }else{
            mAdapter.setDatas(dataBean.getList());
            mAdapter.notifyDataSetChanged();
        }
    }

    private void initRefreshLayout() {
        mRefreshView.setRefreshHeader(new YiYaHeaderView(_mActivity));
        mRefreshView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                ((SeckillingActivity)_mActivity).getData(Integer.parseInt(mPaniclBuyId));
            }
        });
        mRefreshView.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPage++;
                getData(false,refreshlayout);
            }
        });
    }
}
