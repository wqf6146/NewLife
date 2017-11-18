package com.yhkj.yymall.view.popwindows;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.adapter.recycleview.CommonAdapter;
import com.vise.xsnow.ui.adapter.recycleview.base.ViewHolder;
import com.vise.xsnow.ui.adapter.recycleview.wrapper.HeaderAndFooterWrapper;
import com.vise.xsnow.ui.basepopup.BasePopupWindow;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.CommodityDetailsActivity;
import com.yhkj.yymall.activity.DiscountDetailsActivity;
import com.yhkj.yymall.activity.GrouponDetailsActivity;
import com.yhkj.yymall.activity.IntegralDetailActivity;
import com.yhkj.yymall.activity.LeaseDetailActivity;
import com.yhkj.yymall.bean.ShopGiftBean;
import com.yhkj.yymall.http.YYMallApi;


import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.GONE;

/**
 */
public class ShopGiftPopupView extends BasePopupWindow {

    @Bind(R.id.vps_recycleview)
    RecyclerView mRecycleView;

    @Bind(R.id.vps_btn_copy)
    TextView mTvCopy;

    @Bind(R.id.vps_tv_title)
    TextView mTvTitle;

    @Bind(R.id.vps_fl_loading)
    FrameLayout mFlLoading;

    @Bind(R.id.vps_fl_err)
    FrameLayout mFlErr;

    @Bind(R.id.vps_ll_reload)
    LinearLayout mLlReload;

    private int mGoodsId;
    public ShopGiftPopupView(Activity context,int goodsId) {
        super(context);
        mGoodsId = goodsId;
        initBase();
    }

    private void initBase() {
        mLlReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFlErr.setVisibility(GONE);
                mFlLoading.setVisibility(View.VISIBLE);
                getData();
            }
        });
        getData();
    }


    @Override
    protected Animation initShowAnimation() {
        Animation animation = getTranslateAnimation(500 * 2, 0, 300);
        return animation;
    }

    @Override
    protected Animation initExitAnimation() {
        Animation animation = getTranslateAnimation(0, 500 * 2, 300);
        return animation;
    }

    @Override
    public View getClickToDismissView() {
        return getPopupWindowView();
    }

    @Override
    public View onCreatePopupView() {
        View view = createPopupById(R.layout.view_pop_shopgift);
        ButterKnife.bind(this,view);
        return view;
    }

    private void getData(){
        YYMallApi.getShopGift(getContext(), mGoodsId, new YYMallApi.ApiResult<ShopGiftBean.DataBean>(getContext()) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                showToast(e.getMessage());
                mFlLoading.setVisibility(GONE);
                mFlErr.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(ShopGiftBean.DataBean dataBean) {
                mFlLoading.setVisibility(GONE);
                mFlErr.setVisibility(GONE);
                init(dataBean);
            }
        });
    }

    private void init(ShopGiftBean.DataBean dataBean) {

        mTvCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        CommonAdapter commonAdapter = new CommonAdapter<ShopGiftBean.DataBean.InfoBean>(getContext(),R.layout.item_shopgift,dataBean.getInfo()) {
            @Override
            protected void convert(ViewHolder holder, final ShopGiftBean.DataBean.InfoBean bean, int position) {
                Glide.with(getContext()).load(bean.getImg()).into((ImageView) holder.getView(R.id.is_img_shop));
                holder.setText(R.id.is_tv_shopname,bean.getName());
                holder.setText(R.id.is_tv_shopprice,"价值" + bean.getPrice() + "元");
                holder.setOnClickListener(R.id.is_ll_container, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (bean.getType() == 2) {
                            //租赁商品
                            Intent intent = new Intent(mContext, LeaseDetailActivity.class);
                            intent.putExtra("id", String.valueOf(bean.getGoodsId()));
                            getContext().startActivity(intent);
                        }else if (bean.getType() == 0){
                            //普通商品
                            Intent intent = new Intent(mContext, CommodityDetailsActivity.class);
                            intent.putExtra("goodsId", String.valueOf(bean.getGoodsId()));
                            getContext().startActivity(intent);
                        }else if (bean.getType() == 1){
                            //拼团商品
                            Intent intent = new Intent(mContext, GrouponDetailsActivity.class);
                            intent.putExtra("goodsId", String.valueOf(bean.getGoodsId()));
                            getContext().startActivity(intent);
                        }else if (bean.getType() == 3){
                            //折扣
                            Intent intent = new Intent(mContext, DiscountDetailsActivity.class);
                            intent.putExtra("goodsId", String.valueOf(bean.getGoodsId()));
                            getContext().startActivity(intent);
                        }else if (bean.getType() == 4){
                            //积分
                            Intent intent = new Intent(mContext, IntegralDetailActivity.class);
                            intent.putExtra("id", String.valueOf(bean.getGoodsId()));
                            getContext().startActivity(intent);
                        }
                    }
                });
            }
        };
        HeaderAndFooterWrapper headerAndFooterWrapper = new HeaderAndFooterWrapper(commonAdapter);
        headerAndFooterWrapper.addHeaderView(LayoutInflater.from(getContext()).inflate(R.layout.item_shopgifthead,mRecycleView,false));
        mRecycleView.setAdapter(headerAndFooterWrapper);
    }

    public void setTvTitle(String title){
        mTvTitle.setText(title);
    }

//    protected abstract void bind(ViewHolder holder, T t, int position);
    @Override
    public View initAnimaView() {
        return findViewById(R.id.vps_rl_popview);
    }
}
