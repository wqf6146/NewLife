package com.yhkj.yymall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yhkj.yymall.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/12.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.CollectViewHolder> {

    private Context mContext;

    public CollectAdapter(Context context){
        mContext = context;
    }

    private boolean mSelectStatus = false;
    public void setSelectStatus(boolean selectStatus){
        mSelectStatus = selectStatus;
        notifyDataSetChanged();
    }

    @Override
    public CollectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CollectViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_group_entity,parent,false));
    }

    @Override
    public void onBindViewHolder(CollectViewHolder holder, int position) {
        if (mSelectStatus)
            holder.mImgSelect.setVisibility(View.VISIBLE);
        else
            holder.mImgSelect.setVisibility(View.GONE);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class CollectViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.ioe_img_select)
        ImageView mImgSelect;

        @Bind(R.id.ioe_img_shopimg)
        ImageView mImgShop;

        @Bind(R.id.ioe_tv_addcar)
        TextView mTvAddCar;

        @Bind(R.id.ioe_tv_pay)
        TextView mTvPay;

        @Bind(R.id.ioc_tv_shopname)
        TextView mTvShopName;

        @Bind(R.id.ioc_tv_shopprice)
        TextView mTvShopPrice;

        @Bind(R.id.ioe_tv_count)
        TextView mTvShopCount;

        public CollectViewHolder(View view){
            super(view);
            ButterKnife.bind(this,view);
        }
    }
}
