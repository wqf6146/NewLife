package com.yhkj.yymall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
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
import com.yhkj.yymall.bean.GoodsLikeBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.YiYaHeaderView;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;

/**
 * Created by Administrator on 2017/8/18.
 */

public class RecommenActivity extends BaseToolBarActivity {

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData(null,null);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_refresh_recycleview);
    }

    private GridLayoutManager mGridLayoutHelper;
    @Override
    protected void initView() {
        super.initView();
        mGridLayoutHelper = new GridLayoutManager(this,1);
        mRecycleView.setLayoutManager(mGridLayoutHelper);
        mRecycleView.addItemDecoration(new ItemOffsetDecoration(CommonUtil.dip2px(this,1)));
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(this));
        setImgRightResource(R.mipmap.ic_nor_whitelistopen);
    }

    public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

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
    private int mCurPage = 1;
    @Override
    protected void bindEvent() {
        super.bindEvent();
        setImgRightLisiten(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object object = mRecycleView.getTag();
                if ( object != null && (boolean)object == true){
                    // true - ver
                    mRecycleView.setTag(false);
                    GridLayoutManager gridLayoutManager = (GridLayoutManager)mRecycleView.getLayoutManager();
                    gridLayoutManager.setSpanCount(1);
                    setImgRightResource(R.mipmap.ic_nor_whitelistopen);
                }else{
                    mRecycleView.setTag(true);
                    GridLayoutManager gridLayoutManager = (GridLayoutManager)mRecycleView.getLayoutManager();
                    gridLayoutManager.setSpanCount(2);
                    setImgRightResource(R.mipmap.ic_nor_whitelistclose);
                }

                mRecycleView.getAdapter().notifyDataSetChanged();
            }
        });
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
    private CommonAdapter mShopListAdapter;


    private void getData(final Boolean refreshOrLoadmore,final RefreshLayout refreshlayout) {
        YYMallApi.getGoodsLike(this, mCurPage, false,new YYMallApi.ApiResult<GoodsLikeBean.DataBean>(this) {
            @Override
            public void onStart() {}

            @Override
            public void onError(ApiException e) {
                ViseLog.e(e);
                showToast(e.getMessage());
                setNetWorkErrShow(VISIBLE);
                if (refreshlayout!=null){
                    if (refreshOrLoadmore){
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
            public void onNext(GoodsLikeBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                if (refreshlayout!=null){
                    if (refreshOrLoadmore){
                        refreshlayout.finishRefresh();
                        mShopListAdapter.setDatas(dataBean.getList());
                        mShopListAdapter.notifyDataSetChanged();
                    }else{
                        refreshlayout.finishLoadmore();
                        if (dataBean.getList() == null || dataBean.getList().size() == 0){
                            refreshlayout.setLoadmoreFinished(true);
                        }
                        mShopListAdapter.addDatas(dataBean.getList());
                        mShopListAdapter.notifyDataSetChanged();
                    }
                    return;
                }

                if (dataBean.getList()==null || dataBean.getList().size()==0){
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无推荐商品");
                    return;
                }

                if (mShopListAdapter == null) {
                    mShopListAdapter = new CommonAdapter<GoodsLikeBean.DataBean.ListBean>(RecommenActivity.this, R.layout.item_shop, dataBean.getList()) {
                        @Override
                        protected void convert(ViewHolder holder, final GoodsLikeBean.DataBean.ListBean bean, int position) {
                            Object object = mRecycleView.getTag();
                            if (object != null && (boolean) object == true) {
                                holder.setVisible(R.id.is_ll_vert, true);
                                holder.setVisible(R.id.fn_ll_hor, false);
                                Glide.with(mContext).load(bean.getImg()).placeholder(R.mipmap.ic_nor_srcpic).into((ImageView) holder.getView(R.id.is_vert_img_shop));
                                holder.setText(R.id.is_vert_shop_name, bean.getName());

                                holder.setText(R.id.is_vert_shop_groupnumber, String.format(mContext.getString(R.string.salepeoplecount), String.valueOf(bean.getSale())));
                                if (bean.getType() == 2) {
                                    //租赁商品
                                    holder.setText(R.id.is_vert_shop_price, "¥" + String.valueOf(bean.getPrice()));
                                    ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                    layoutParams.removeRule(ALIGN_PARENT_LEFT);
                                    layoutParams.addRule(ALIGN_PARENT_RIGHT);
                                    tagShop.setImageResource(R.mipmap.ic_nor_tagfree);

                                    holder.setVisible(R.id.is_vert_img_tagshop,true);
                                }else if (bean.getType() == 1){
                                    //拼团商品
                                    holder.setText(R.id.is_vert_shop_price, "¥" + String.valueOf(bean.getPrice()));
                                    ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                    layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                                    layoutParams.addRule(ALIGN_PARENT_LEFT);
                                    tagShop.setImageResource(R.mipmap.ic_nor_taggroup);

                                    holder.setVisible(R.id.is_vert_img_tagshop,true);
                                }else if (bean.getType() == 3){
                                    //折扣
                                    holder.setText(R.id.is_vert_shop_price, "¥" + String.valueOf(bean.getPrice()));
                                    ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                    layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                                    layoutParams.addRule(ALIGN_PARENT_LEFT);
                                    tagShop.setImageResource(R.mipmap.ic_nor_tagdiscount);

                                    holder.setVisible(R.id.is_vert_img_tagshop,true);
                                }else if (bean.getType() == 4){
                                    //积分
                                    holder.setText(R.id.is_vert_shop_price, String.valueOf(bean.getPrice()) + "积分");
                                    ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                    layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                                    layoutParams.addRule(ALIGN_PARENT_LEFT);
                                    tagShop.setImageResource(R.mipmap.ic_nor_tagintegral);

                                    holder.setVisible(R.id.is_vert_img_tagshop,true);
                                }else if (bean.getType() == 0 && bean.getPanicBuyItemId() != 0){
                                    //限时抢购
                                    holder.setText(R.id.is_vert_shop_price, "¥" + String.valueOf(bean.getPrice()));
                                    ImageView tagShop = holder.getView(R.id.is_vert_img_tagshop);
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                    layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                                    layoutParams.addRule(ALIGN_PARENT_LEFT);
                                    tagShop.setImageResource(R.mipmap.ic_nor_tagtimekill);

                                    holder.setVisible(R.id.is_vert_img_tagshop,true);
                                }else{
                                    holder.setText(R.id.is_vert_shop_price, "¥" + String.valueOf(bean.getPrice()));
                                    holder.setVisible(R.id.is_vert_img_tagshop,false);
                                }
                            } else {
                                holder.setVisible(R.id.is_ll_vert, false);
                                holder.setVisible(R.id.fn_ll_hor, true);
                                Glide.with(mContext).load(bean.getImg()).placeholder(R.mipmap.ic_nor_srcpic).into((ImageView) holder.getView(R.id.is_img_shop));
                                holder.setText(R.id.is_hor_shop_name, bean.getName());
                                holder.setText(R.id.is_hor_groupnumber, String.format(mContext.getString(R.string.salepeoplecount), String.valueOf(bean.getSale())));
                                if (bean.getType()== 2){
                                    holder.setText(R.id.is_hor_groupnumber, String.format(mContext.getString(R.string.getpeoplecount), String.valueOf(bean.getSale())));
                                }else{
                                    holder.setText(R.id.is_hor_groupnumber, String.format(mContext.getString(R.string.salepeoplecount), String.valueOf(bean.getSale())));
                                }
                                if (bean.getType() == 2) {
                                    //租赁商品
                                    holder.setText(R.id.is_hor_shop_price, "¥" + String.valueOf(bean.getPrice()));
                                    ImageView tagShop = holder.getView(R.id.is_img_tagshop);
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                    layoutParams.removeRule(ALIGN_PARENT_LEFT);
                                    layoutParams.addRule(ALIGN_PARENT_RIGHT);
                                    tagShop.setImageResource(R.mipmap.ic_nor_tagfree);


                                    holder.setVisible(R.id.is_img_tagshop,true);
                                }else if (bean.getType() == 1){
                                    //拼团商品
                                    holder.setText(R.id.is_hor_shop_price, "¥" + String.valueOf(bean.getPrice()));
                                    ImageView tagShop = holder.getView(R.id.is_img_tagshop);
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                    layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                                    layoutParams.addRule(ALIGN_PARENT_LEFT);
                                    tagShop.setImageResource(R.mipmap.ic_nor_taggroup);

                                    holder.setVisible(R.id.is_img_tagshop,true);
                                }else if (bean.getType() == 3){
                                    //折扣
                                    holder.setText(R.id.is_hor_shop_price, "¥" + String.valueOf(bean.getPrice()));
                                    ImageView tagShop = holder.getView(R.id.is_img_tagshop);
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                    layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                                    layoutParams.addRule(ALIGN_PARENT_LEFT);
                                    tagShop.setImageResource(R.mipmap.ic_nor_tagdiscount);

                                    holder.setVisible(R.id.is_img_tagshop,true);
                                }else if (bean.getType() == 4){
                                    //积分
                                    holder.setText(R.id.is_hor_shop_price,String.valueOf(bean.getPrice()) + "积分");
                                    ImageView tagShop = holder.getView(R.id.is_img_tagshop);
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                    layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                                    layoutParams.addRule(ALIGN_PARENT_LEFT);
                                    tagShop.setImageResource(R.mipmap.ic_nor_tagintegral);

                                    holder.setVisible(R.id.is_img_tagshop,true);
                                }else if (bean.getType() == 0 && bean.getPanicBuyItemId() != 0){
                                    //限时抢购
                                    holder.setText(R.id.is_hor_shop_price, "¥" + String.valueOf(bean.getPrice()));
                                    ImageView tagShop = holder.getView(R.id.is_img_tagshop);
                                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)tagShop.getLayoutParams();
                                    layoutParams.removeRule(ALIGN_PARENT_RIGHT);
                                    layoutParams.addRule(ALIGN_PARENT_LEFT);
                                    tagShop.setImageResource(R.mipmap.ic_nor_tagtimekill);

                                    holder.setVisible(R.id.is_img_tagshop,true);
                                }else{
                                    holder.setText(R.id.is_hor_shop_price, "¥" + String.valueOf(bean.getPrice()));
                                    holder.setVisible(R.id.is_img_tagshop,false);
                                }
                            }

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (bean.getType() == 2) {
                                        //租赁商品
                                        Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                                        intent.putExtra("id", String.valueOf(bean.getId()));
                                        startActivity(intent);
                                    } else if (bean.getType() == 0) {
                                        //普通商品
                                        if (bean.getPanicBuyItemId()!=0){
                                            //限时抢购
                                            Intent intent = new Intent(mContext, TimeKillDetailActivity.class);
                                            intent.putExtra("id", String.valueOf(bean.getPanicBuyItemId()));
                                            startActivity(intent);
                                        }else{
                                            Intent intent = new Intent(mContext, CommodityDetailsActivity.class);
                                            intent.putExtra("goodsId", String.valueOf(bean.getId()));
                                            startActivity(intent);
                                        }
                                    } else if (bean.getType() == 1) {
                                        //拼团商品
                                        Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                                        intent.putExtra("goodsId", String.valueOf(bean.getId()));
                                        startActivity(intent);
                                    }else if (bean.getType() == 3){
                                        //折扣
                                        Intent intent = new Intent(mContext, DiscountDetailsActivity.class);
                                        intent.putExtra("goodsId", bean.getId() + "");
                                        mContext.startActivity(intent);
                                    }else if (bean.getType() == 4){
                                        //积分
                                        Intent intent = new Intent(mContext, IntegralDetailActivity.class);
                                        intent.putExtra("id", bean.getId() + "");
                                        mContext.startActivity(intent);
                                    }else if (bean.getType() == 6){
                                        //日常活动
                                        Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                                        intent.putExtra("goodsId", bean.getId() + "");
                                        mContext.startActivity(intent);
                                    }
                                }
                            });

                        }
                    };
                    mRecycleView.setAdapter(mShopListAdapter);
                }else{
                    mShopListAdapter.setDatas(dataBean.getList());
                    mShopListAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null,null);
    }

    @Override
    protected void initData() {
        setTvTitleText("推荐商品");
        setToolBarColor(getResources().getColor(R.color.theme_bule));
    }
}
