package com.yhkj.yymall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yhkj.yymall.R;

import java.util.List;

/**
 * Created by Administrator on 2017/7/5.
 */

public class ShopClassifyAdapter extends RecyclerView.Adapter<ShopClassifyAdapter.ShopClassifyViewHolder> {

    private Context mContext;

    public ShopClassifyAdapter(Context context){
        mContext = context;
    }

    @Override
    public void onBindViewHolder(ShopClassifyViewHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(ShopClassifyViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        holder.mImgItem.setImageResource(R.mipmap.text_car);
        holder.mTvName.setText("四轮推车");
    }

    @Override
    public ShopClassifyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopClassifyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_classify_shop,parent,false));
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class ShopClassifyViewHolder extends RecyclerView.ViewHolder{

        ImageView mImgItem;
        TextView mTvName;

        public ShopClassifyViewHolder(View view){
            super(view);
            mImgItem = (ImageView)view.findViewById(R.id.ics_img_shop);
            mTvName = (TextView)view.findViewById(R.id.ics_tv_shopname);
        }
    }
}
