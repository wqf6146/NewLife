package com.yhkj.yymall.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.BackPladgeActivity;
import com.yhkj.yymall.activity.PladgeDetailActivity;
import com.yhkj.yymall.activity.RentPladgeActivity;
import com.yhkj.yymall.bean.RentRecordBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class EfficacyAdapter extends RecyclerView.Adapter<EfficacyAdapter.ViewHolder> {

    Activity mActivity;

    private List<RentRecordBean.DataBean.RentalsBean> mDataBean;
    private List<RentRecordBean.DataBean.RentalsBean.GoodsesBean> mDatas;
    public EfficacyAdapter(Activity context, List<RentRecordBean.DataBean.RentalsBean> datas){
        mActivity = context;
        mDataBean = datas;
    }

    @Override
    public EfficacyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new EfficacyAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rentshop,parent,false));
        else
            return new EfficacyAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rent_tab,parent,false));
    }

    @Override
    public void onBindViewHolder(EfficacyAdapter.ViewHolder holder, int position) {

        if (position > mDatas.size() - 1){
            holder.itemView.findViewById(R.id.irt_ll_turnpladge).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //转换押金
                    mActivity.startActivity(new Intent(mActivity, RentPladgeActivity.class));
                }
            });
            holder.itemView.findViewById(R.id.irt_ll_backpladge).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    返回押金
                    mActivity.startActivity(new Intent(mActivity,BackPladgeActivity.class));
                }
            });
        }else{
            final RentRecordBean.DataBean.RentalsBean.GoodsesBean bean = mDatas.get(position);
            holder.itemView.findViewById(R.id.irs_lostimg).setVisibility(View.GONE);
            holder.itemView.findViewById(R.id.ir_tv_yy).setVisibility(View.INVISIBLE);
            holder.itemView.findViewById(R.id.irs_img_arrow).setVisibility(View.VISIBLE);
            ImageView imgShop = (ImageView)holder.itemView.findViewById(R.id.is_img_shop);
            TextView tvName = (TextView)holder.itemView.findViewById(R.id.fn_tv_shopname_hor_2);
            TextView tvPrice = (TextView)holder.itemView.findViewById(R.id.fn_tv_shopprice_hor_2);
            TextView tvNumb = (TextView) holder.itemView.findViewById(R.id.is_shop_rentNumb);
            Glide.with(mActivity).load(bean.getGoodsImg()).into(imgShop);
            tvName.setText(bean.getGoodsName());
            tvPrice.setText("¥" + String.valueOf(bean.getRealPrice())+ "x" +bean.getGoodsNums());
//            tvNumb.setText("(共" + bean.getGoodsNums() + "件)");
            holder.itemView.findViewById(R.id.fn_ll_hor_2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //租赁详情
                    Intent intent = new Intent(mActivity, PladgeDetailActivity.class);
                    intent.putExtra("id",String.valueOf(bean.getId()));
                    intent.putExtra("numb",String.valueOf(bean.getGoodsNums()));
                    mActivity.startActivity(intent);

                }
            });
        }
    }

    private int mCount = 0;
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
        return mCount + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= mCount - 1){
            return 0;
        }else{
            return 1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View view){
            super(view);
        }
    }
}
