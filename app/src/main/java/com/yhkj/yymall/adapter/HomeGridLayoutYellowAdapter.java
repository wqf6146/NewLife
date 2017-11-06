package com.yhkj.yymall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.bumptech.glide.Glide;
import com.yhkj.yymall.R;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.HomeRecommBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */

public class HomeGridLayoutYellowAdapter extends DelegateAdapter.Adapter<HomeGridLayoutYellowAdapter.HomeGroupPayHolder> {

    private LayoutHelper mLayoutHelper;
    private Context mContext;

    private HomeRecommBean.DataBean.CategorysBean mDatas;

    public void setDatas(HomeRecommBean.DataBean.CategorysBean datas){
        mDatas = datas;
    }

    public HomeGridLayoutYellowAdapter(Context context, LayoutHelper layoutHelper,HomeRecommBean.DataBean.CategorysBean bean){
        mContext = context;
        mLayoutHelper = layoutHelper;
        mDatas = bean;
    }

    @Override
    public void onBindViewHolder(HomeGridLayoutYellowAdapter.HomeGroupPayHolder holder, int position) {
        if (mDatas != null){
            holder.mTvName.setText(mDatas.getName());
            holder.mTvGroupSize.setText(String.format(mContext.getString(R.string.groupsize),String.valueOf(mDatas.getGroupPurchaseNum())));
            List<HomeRecommBean.DataBean.CategorysBean.GoodsBean> goodsBeen = mDatas.getGoods();
            HomeRecommBean.DataBean.CategorysBean.GoodsBean bean = goodsBeen.get(0);
            Glide.with(mContext).load(bean.getImg()).placeholder(R.mipmap.text_car).into(holder.mImgShop1);
            holder.mTvShopName1.setText(bean.getName());
            holder.mTvPrice1.setText(String.valueOf(bean.getPrice()));
//            holder.mTvMarkPrice1.setText(bean.getMarketPrice());
//            holder.mTvMarkPrice1.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mTvSaleTotal1.setText(String.format(mContext.getString(R.string.sale),String.valueOf(bean.getSale())));

            HomeRecommBean.DataBean.CategorysBean.GoodsBean bean1 = goodsBeen.get(1);
            Glide.with(mContext).load(bean1.getImg()).placeholder(R.mipmap.text_shopcar).into(holder.mImgShop2);
            holder.mTvShopName2.setText(bean1.getName());
            holder.mTvPrice2.setText(String.valueOf(bean1.getPrice()));
//            holder.mTvMarkPrice2.setText(bean1.getMarketPrice());
//            holder.mTvMarkPrice2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.mTvSaleTotal2.setText(String.format(mContext.getString(R.string.sale),String.valueOf(bean1.getSale())));
        }
    }

    @Override
    public void onBindViewHolder(HomeGridLayoutYellowAdapter.HomeGroupPayHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

    }

    @Override
    public HomeGridLayoutYellowAdapter.HomeGroupPayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HomeGridLayoutYellowAdapter.HomeGroupPayHolder(LayoutInflater.from(mContext).inflate(R.layout.item_home_grid_yellow, parent, false));
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return mLayoutHelper;
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return Constant.TYPE_HOME.ACTIVITY_YELLOW;
    }


    static class HomeGroupPayHolder extends RecyclerView.ViewHolder {
        TextView mTvName;
        TextView mTvGroupSize;
        ImageView mImgShop1;
        TextView mTvShopName1;
        TextView mTvPrice1;
//        TextView mTvMarkPrice1;
        TextView mTvSaleTotal1;
        TextView mTvGoPay1;

        ImageView mImgShop2;
        TextView mTvShopName2;
        TextView mTvPrice2;
//        TextView mTvMarkPrice2;
        TextView mTvSaleTotal2;
        TextView mTvGoPay2;

        public HomeGroupPayHolder(View itemView) {
            super(itemView);
            mTvName = (TextView)itemView.findViewById(R.id.ihgy_tv_name);
            mTvGroupSize = (TextView)itemView.findViewById(R.id.ihgy_tv_groupsize);
            mImgShop1 = (ImageView)itemView.findViewById(R.id.ihg_img_shop_1);
            mTvShopName1 = (TextView)itemView.findViewById(R.id.ihg_tv_shopname_1);
            mTvPrice1 = (TextView)itemView.findViewById(R.id.ihg_tv_price_1);
//            mTvMarkPrice1 = (TextView)itemView.findViewById(R.id.ihg_tv_marketPrice_1);
            mTvSaleTotal1  = (TextView)itemView.findViewById(R.id.ihg_tv_sale_1);
            mTvGoPay1 = (TextView)itemView.findViewById(R.id.ihg_tv_gopay_1);

            mImgShop2 = (ImageView)itemView.findViewById(R.id.ihg_img_shop_2);
            mTvShopName2 = (TextView)itemView.findViewById(R.id.ihg_tv_shopname_2);
            mTvPrice2 = (TextView)itemView.findViewById(R.id.ihg_tv_price_2);
//            mTvMarkPrice2 = (TextView)itemView.findViewById(R.id.ihg_tv_marketPrice_2);
            mTvSaleTotal2  = (TextView)itemView.findViewById(R.id.ihg_tv_sale_2);
            mTvGoPay2 = (TextView)itemView.findViewById(R.id.ihg_tv_gopay_2);
        }
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }
}
