package com.yhkj.yymall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.PladgeDetailActivity;
import com.yhkj.yymall.bean.RentRecordBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class LoseEfficacyAdapter extends RecyclerView.Adapter<LoseEfficacyAdapter.ViewHolder> {

    Context mContext;

    private List<RentRecordBean.DataBean.RentalsBean> mDataBean;
    private List<RentRecordBean.DataBean.RentalsBean.GoodsesBean> mDatas;
    public LoseEfficacyAdapter(Context context, List<RentRecordBean.DataBean.RentalsBean> datas){
        mContext = context;
        mDataBean = datas;
    }

    @Override
    public LoseEfficacyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LoseEfficacyAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rentshop,parent,false));
    }

    @Override
    public void onBindViewHolder(LoseEfficacyAdapter.ViewHolder holder, int position) {
        final RentRecordBean.DataBean.RentalsBean.GoodsesBean bean = mDatas.get(position);
        holder.itemView.findViewById(R.id.irs_lostimg).setVisibility(View.VISIBLE);
        holder.itemView.findViewById(R.id.irs_img_arrow).setVisibility(View.GONE);
        holder.itemView.findViewById(R.id.ir_tv_yy).setVisibility(View.INVISIBLE);
        ImageView imgShop = (ImageView)holder.itemView.findViewById(R.id.is_img_shop);
        TextView tvName = (TextView)holder.itemView.findViewById(R.id.fn_tv_shopname_hor_2);
        TextView tvPrice = (TextView)holder.itemView.findViewById(R.id.fn_tv_shopprice_hor_2);
        TextView tvNumb = (TextView) holder.itemView.findViewById(R.id.is_shop_rentNumb);
        Glide.with(mContext).load(bean.getGoodsImg()).into(imgShop);
        tvPrice.setText("¥"  + String.valueOf(bean.getRealPrice()));
        tvName.setText(bean.getGoodsName());
        tvNumb.setText("(共" + bean.getGoodsNums() + "件)");
        holder.itemView.findViewById(R.id.fn_ll_hor_2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //租赁详情
                //租赁详情
                Intent intent = new Intent(mContext, PladgeDetailActivity.class);
                intent.putExtra("id",String.valueOf(bean.getId()));
                intent.putExtra("type","lose");
                mContext.startActivity(intent);
            }
        });
    }


    private int mCount = -1;

    @Override
    public int getItemCount() {

        if (mDataBean.size() == 0)
            return 0;

        mCount = 0;
        mDatas = new ArrayList<>();
        for (int i=0; i <mDataBean.size(); i++){
            for (int j = 0;j <mDataBean.get(i).getGoodses().size(); j++){
                mCount++;
                mDatas.add(mDataBean.get(i).getGoodses().get(j));
            }
        }
        return mCount;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View view){
            super(view);
        }
    }
}
