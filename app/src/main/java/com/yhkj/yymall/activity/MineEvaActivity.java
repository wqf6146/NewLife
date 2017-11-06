package com.yhkj.yymall.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
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
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.BannerItemBean;
import com.yhkj.yymall.bean.MineEvaBean;

import com.yhkj.yymall.bean.ShopDetailsBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.view.YiYaHeaderView;

import java.util.ArrayList;

import butterknife.Bind;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by Administrator on 2017/8/24.
 */

public class MineEvaActivity extends BaseToolBarActivity {

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
        initRefreshLayout();
    }

    @Override
    protected void bindEvent() {
        super.bindEvent();
    }

    @Override
    protected void initData() {
        setToolBarColor(getResources().getColor(R.color.theme_bule));
        setTvTitleText("我的评价");
    }

    @Override
    protected void onActivityLoadFinish() {
        super.onActivityLoadFinish();
        getData(null,null);
    }

    private void initRefreshLayout() {
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRefreshLayout.setRefreshHeader(new YiYaHeaderView(this));
        mRefreshLayout.setLoadmoreFinished(false);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = 1;
                refreshlayout.setLoadmoreFinished(false);
                getData(true,refreshlayout);
            }
        });
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                mPage++;
                getData(false,refreshlayout);
            }
        });
    }

    @Override
    protected void onReLoadClickLisiten() {
        super.onReLoadClickLisiten();
        getData(null,null);
    }

    private int mPage = 1;
    private CommonAdapter mAdapter;
    private HeaderAndFooterWrapper mHeadWarpperAdapter;
    private void getData(final Boolean refreshOrLoadmore,final RefreshLayout refreshlayout) {
        YYMallApi.getMineEvaList(this, mPage, new YYMallApi.ApiResult<MineEvaBean.DataBean>(MineEvaActivity.this) {
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
                    if (refreshOrLoadmore){
                        refreshlayout.finishRefresh();
                    }else{
                        refreshlayout.finishLoadmore();
                        mPage--;
                    }
                }
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(MineEvaBean.DataBean dataBean) {
                setNetWorkErrShow(GONE);
                if (refreshlayout!=null){
                    if (refreshOrLoadmore){
                        refreshlayout.finishRefresh();
                        mAdapter.setDatas(dataBean.getInfo());
                        mHeadWarpperAdapter.notifyDataSetChanged();
                    }else{
                        refreshlayout.finishLoadmore();
                        if (dataBean.getInfo() == null || dataBean.getInfo().size() == 0){
                            refreshlayout.setLoadmoreFinished(true);
                            mPage--;
                        }else{
                            mAdapter.addDatas(dataBean.getInfo());
                            mHeadWarpperAdapter.notifyDataSetChanged();
                        }
                    }
                    return;
                }

                if (dataBean.getInfo()==null || dataBean.getInfo().size()==0){
                    setNoDataView(R.mipmap.ic_nor_noeva,"暂无评价",buildEvaHeadView(dataBean));
                    return;
                }
                if (mAdapter == null){
                    mAdapter = new CommonAdapter<MineEvaBean.DataBean.InfoBean>(MineEvaActivity.this,R.layout.item_mineeva_entity,dataBean.getInfo()) {
                        @Override
                        protected void convert(ViewHolder holder, final MineEvaBean.DataBean.InfoBean bean, int position) {
                            final ImageView userImg = holder.getView(R.id.ic_img_avatar);
                            Glide.with(MineEvaActivity.this).load(bean.getIco()).asBitmap().centerCrop().placeholder(R.mipmap.ic_nor_srcheadimg).into(new BitmapImageViewTarget(userImg) {
                                @Override
                                protected void setResource(Bitmap resource) {
                                    RoundedBitmapDrawable circularBitmapDrawable =
                                            RoundedBitmapDrawableFactory.create(getResources(), resource);
                                    circularBitmapDrawable.setCircular(true);
                                    userImg.setImageDrawable(circularBitmapDrawable);
                                }
                            });
                            holder.setText(R.id.ic_tv_name,bean.getUsername());
                            holder.setText(R.id.ic_tv_timespec,bean.getTime() +" " + bean.getGoodsSpec());
                            holder.setText(R.id.ic_tv_content,bean.getContents());


                            if (bean.getImgs()!=null && bean.getImgs().size() > 0){
                                final ArrayList bannerItemBean = new ArrayList<>();
                                LinearLayout llImgContainer = holder.getView(R.id.ime_ll_piccontainer);
                                holder.setVisible(R.id.ime_ll_piccontainer,true);
                                for (int i=0;i<bean.getImgs().size();i++){
                                    final int pos = i;
                                    ImageView imgView = (ImageView) llImgContainer.getChildAt(i);
                                    bannerItemBean.add(new BannerItemBean(Constant.BANNER.IMG,null,bean.getImgs().get(i)));
                                    Glide.with(mContext).load(bean.getImgs().get(i)).placeholder(R.mipmap.ic_nor_srcpic).into(imgView);
                                    imgView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(MineEvaActivity.this,ShopBannerActivity.class);
                                            intent.putParcelableArrayListExtra(Constant.BANNER.ITEMBEAN,bannerItemBean);
                                            intent.putExtra(Constant.BANNER.POSITION,pos);
                                            startActivity(intent);
                                        }
                                    });
                                }
                            }else{
                                holder.setVisible(R.id.ime_ll_piccontainer,false);
                            }
                            if (TextUtils.isEmpty(bean.getReContents())){
                                holder.setVisible(R.id.ic_tv_reply,false);
                            }else{
                                holder.setText(R.id.ic_tv_reply,"掌柜回复：" + bean.getReContents());
                            }
                            holder.setOnClickListener(R.id.ioe_ll_container, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //// 0普通订单,1团购订单,2限时抢购,3拼团,4租赁
                                    if (bean.getGoodsType() == 0){
                                        if (bean.getPanicBuyItemId() != 0 ){
                                            //限时抢购
                                            Intent intent = new Intent(mContext, TimeKillDetailActivity.class);
                                            intent.putExtra("id", String.valueOf(bean.getPanicBuyItemId()));
                                            startActivity(intent);
                                            return;
                                        }
                                        //普通商品
                                        Intent intent = new Intent(mContext, CommodityDetailsActivity.class);
                                        intent.putExtra("goodsId", String.valueOf(bean.getGoodsId()));
                                        startActivity(intent);
                                    }else if (bean.getGoodsType() == 1){
                                        //拼团商品
                                        Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                                        intent.putExtra("goodsId", String.valueOf(bean.getGoodsId()));
                                        startActivity(intent);
                                    }else if (bean.getGoodsType() == 2){
                                        //租赁商品
                                        Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                                        intent.putExtra("id", String.valueOf(bean.getGoodsId()));
                                        startActivity(intent);
                                    }else if (bean.getGoodsType() == 3){
                                        //折扣
                                        Intent intent = new Intent(mContext, DiscountDetailsActivity.class);
                                        intent.putExtra("goodsId", String.valueOf(bean.getGoodsId()));
                                        startActivity(intent);
                                    }else if (bean.getGoodsType() == 4){
                                        //积分
                                        Intent intent = new Intent(mContext, IntegralDetailActivity.class);
                                        intent.putExtra("id", String.valueOf(bean.getGoodsId()));
                                        startActivity(intent);
                                    }else if (bean.getGoodsType() == 6){
                                        //积分
                                        Intent intent = new Intent(mContext, DailyDetailsActivity.class);
                                        intent.putExtra("goodsId", String.valueOf(bean.getGoodsId()));
                                        startActivity(intent);
                                    }
                                }
                            });
                            Glide.with(mContext).load(bean.getGoodsImg()).into((ImageView) holder.getView(R.id.ioe_img_shopimg));
                            holder.setText(R.id.ioc_tv_shopname,bean.getGoodsName());
                            holder.setText(R.id.ioc_tv_shopprice,"¥"  + bean.getRealPrice());
                            if (bean.getGoodsType() == 2) {
                                //租赁商品
                                holder.setText(R.id.ioc_tv_shopprice,"¥"  + bean.getRealPrice());
                                holder.setImageResource(R.id.ioe_img_tagshop,R.mipmap.ic_nor_tagfree);
                                holder.setVisible(R.id.ioe_img_tagshop,true);
                            }else if (bean.getGoodsType() == 1){
                                //拼团商品
                                holder.setText(R.id.ioc_tv_shopprice,"¥"  + bean.getRealPrice());
                                holder.setImageResource(R.id.ioe_img_tagshop,R.mipmap.ic_nor_taggroup);
                                holder.setVisible(R.id.ioe_img_tagshop,true);
                            }else if (bean.getGoodsType() == 3){
                                //折扣
                                holder.setText(R.id.ioc_tv_shopprice,"¥"  + bean.getRealPrice());
                                holder.setImageResource(R.id.ioe_img_tagshop,R.mipmap.ic_nor_tagdiscount);
                                holder.setVisible(R.id.ioe_img_tagshop,true);
                            }else if (bean.getGoodsType() == 4){
                                //积分
                                holder.setText(R.id.ioc_tv_shopprice,bean.getRealPrice() + "积分");
                                holder.setImageResource(R.id.ioe_img_tagshop,R.mipmap.ic_nor_tagintegral);
                                holder.setVisible(R.id.ioe_img_tagshop,true);
                            }else if (bean.getGoodsType() == 0 && bean.getPanicBuyItemId() != 0){
                                //限时抢购
                                holder.setText(R.id.ioc_tv_shopprice,"¥"  + bean.getRealPrice());
                                holder.setImageResource(R.id.ioe_img_tagshop,R.mipmap.ic_nor_tagtimekill);
                                holder.setVisible(R.id.ioe_img_tagshop,true);
                            }else{
                                holder.setText(R.id.ioc_tv_shopprice,"¥"  + bean.getRealPrice());
                            }
                            holder.setText(R.id.ioe_tv_status,bean.getPoint() == 1 ? "差评" :
                                    bean.getPoint() == 3 ? "中评" : "好评");
                        }
                    };
                    mHeadWarpperAdapter = new HeaderAndFooterWrapper(mAdapter);
                    mHeadWarpperAdapter.addHeaderView(buildEvaHeadView(dataBean));
                    mRecycleView.setAdapter(mHeadWarpperAdapter);
                }else{
                    mAdapter.setDatas(dataBean.getInfo());
                    mHeadWarpperAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private View buildEvaHeadView(MineEvaBean.DataBean dataBean){
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_mineeva_top,mRecycleView,false);
        final ImageView imgUser = (ImageView)view.findViewById(R.id.ic_img_avatar);
        final TextView tvUser = (TextView)view.findViewById(R.id.ic_tv_name);
        final TextView tvGoEva = (TextView)view.findViewById(R.id.imt_tv_goeva);
        Glide.with(MineEvaActivity.this).load(dataBean.getUserInfo().getIco()).asBitmap().centerCrop().placeholder(R.mipmap.ic_nor_srcheadimg).into(new BitmapImageViewTarget(imgUser) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imgUser.setImageDrawable(circularBitmapDrawable);
            }
        });
        tvUser.setText(dataBean.getUserInfo().getUsername());
        tvGoEva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //去评价
                Bundle bundle = new Bundle();
                bundle.putInt(Constant.TYPE_FRAGMENT_ORDER.TYPE, Constant.TYPE_FRAGMENT_ORDER.UNEVALUATE);
                startActivity(MineOrderActivity.class, bundle);
            }
        });
        return view;
    }
}
