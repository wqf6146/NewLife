package com.yhkj.yymall.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
import com.vise.xsnow.ui.adapter.recycleview.wrapper.HeaderAndFooterWrapper;
import com.yhkj.yymall.BaseToolBarActivity;
import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.DailyHeadBean;
import com.yhkj.yymall.bean.DailyListGoodsBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.util.CommonUtil;
import com.yhkj.yymall.view.YiYaHeaderView;
import com.yhkj.yymall.view.popwindows.ShopClassifyPopView;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/18.
 */

public class DailyActActivity extends BaseToolBarActivity {

    @Bind(R.id.vr_refreshview)
    SmartRefreshLayout mRefreshLayout;

    @Bind(R.id.vr_recycleview)
    RecyclerView mRecycleView;

    DailyHeadBean.DataBean mDailyHeadBean;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_refresh_recycleview);
    }

    private GridLayoutManager mGridLayoutHelper;
    private HeaderAndFooterWrapper mHheaderAndFooterWrapper;
    @Override
    protected void initView() {
        super.initView();
        mGridLayoutHelper = new GridLayoutManager(this,2);
        mRecycleView.setLayoutManager(mGridLayoutHelper);
        mRecycleView.addItemDecoration(new ItemOffsetDecoration(CommonUtil.dip2px(this,1)));
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(this));
        setImgRightResource(R.mipmap.ic_nor_3point);
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
                new ShopClassifyPopView(DailyActActivity.this).setOnClassifyUpdate(new ShopClassifyPopView.OnClassifyUpdate() {
                    @Override
                    public void onClassifyUpdate(Integer id) {
                        mCategoryId = id;
                        mCurPage = 1;
                        getData(null,null,true);
                    }
                }).showPopupWindow(v);
            }
        });
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mCurPage = 1;
                refreshlayout.setLoadmoreFinished(false);
                getData(true,refreshlayout,false);
            }
        });

        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mCurPage++;
                getData(false,refreshlayout,false);
            }
        });
    }
    private CommonAdapter mShopListAdapter;

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getHeadData();
    }

    private Integer mDailyActiveId,mCategoryId;

    private void getHeadData(){
        YYMallApi.getDailyHead(this, new ApiCallback<DailyHeadBean.DataBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(DailyHeadBean.DataBean dataBean) {
                mDailyHeadBean = dataBean;
                mDailyActiveId = mDailyHeadBean.getDailyActive().getId();
                setTvTitleText(mDailyHeadBean.getDailyActive().getName());
                getData(null,null,false);
            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                setNetWorkErrShow(VISIBLE);
                ViseLog.e(e);
                setImgRightVisiable(INVISIBLE);
                if (e.getCode() == 6011 || e.getCode() == 6012 || e.getCode() == 8001) {
                    setNoDataView(R.mipmap.ic_nor_orderbg,"暂无活动商品");
                }
            }
        });
    }

    private void getData(final Boolean refreshOrLoadmore,final RefreshLayout refreshlayout,boolean bShow) {
        YYMallApi.getDailyList(this, mDailyActiveId,mCategoryId,mCurPage, bShow,new YYMallApi.ApiResult<DailyListGoodsBean.DataBean>(this) {
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
            public void onNext(DailyListGoodsBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                if (refreshlayout!=null){
                    if (refreshOrLoadmore){
                        refreshlayout.finishRefresh();
                        mShopListAdapter.setDatas(dataBean.getList());
                        mHheaderAndFooterWrapper.notifyDataSetChanged();
                    }else{
                        refreshlayout.finishLoadmore();
                        if (dataBean.getList() == null || dataBean.getList().size() == 0){
                            refreshlayout.setLoadmoreFinished(true);
                        }
                        mShopListAdapter.addDatas(dataBean.getList());
                        mHheaderAndFooterWrapper.notifyDataSetChanged();
                    }
                    return;
                }

                if (dataBean.getList()==null || dataBean.getList().size()==0){
                    setNoDataView(R.mipmap.ic_nor_orderbg,"该分类暂无活动商品");
                    return;
                }

                if (mShopListAdapter == null) {
                    mShopListAdapter = new CommonAdapter<DailyListGoodsBean.DataBean.ListBean>(DailyActActivity.this, R.layout.item_shop, dataBean.getList()) {
                        @Override
                        protected void convert(ViewHolder holder, final DailyListGoodsBean.DataBean.ListBean bean, int position) {
                            holder.setVisible(R.id.is_ll_vert, true);
                            holder.setVisible(R.id.fn_ll_hor, false);
                            Glide.with(mContext).load(bean.getPic()).placeholder(R.mipmap.ic_nor_srcpic).into((ImageView) holder.getView(R.id.is_vert_img_shop));
                            holder.setText(R.id.is_vert_shop_name, bean.getName());
                            holder.setText(R.id.is_vert_shop_price, "¥" + String.valueOf(bean.getActivePrice()));
                            holder.setText(R.id.is_vert_shop_groupnumber, String.format(mContext.getString(R.string.salepeoplecount), String.valueOf(bean.getSale())));

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                                    intent.putExtra("goodsId", String.valueOf(bean.getGoods_id()));
                                    startActivity(intent);
                                }
                            });

                        }
                    };
                    mHheaderAndFooterWrapper = new HeaderAndFooterWrapper(mShopListAdapter);
                    ImageView img = new ImageView(DailyActActivity.this);
                    img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                    FrameLayout frameLayout = (FrameLayout)LayoutInflater.from(DailyActActivity.this).inflate(R.layout.item_img,mRecycleView,false);
//                    ImageView img = (ImageView) frameLayout.findViewById(R.id.ii_img);

                    WindowManager wm = (WindowManager) DailyActActivity.this
                            .getSystemService(Context.WINDOW_SERVICE);
                    Point outSize = new Point();
                    wm.getDefaultDisplay().getSize(outSize);
                    img.getLayoutParams().height = outSize.x/3;

                    Glide.with(DailyActActivity.this).load(mDailyHeadBean.getDailyActive().getActive_img()).into(img);
                    mHheaderAndFooterWrapper.addHeaderView(img);
                    mRecycleView.setAdapter(mHheaderAndFooterWrapper);
                }else{
                    mShopListAdapter.setDatas(dataBean.getList());
                    mHheaderAndFooterWrapper.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getHeadData();
    }

    @Override
    protected void initData() {
        setToolBarColor(getResources().getColor(R.color.theme_bule));
    }
}
