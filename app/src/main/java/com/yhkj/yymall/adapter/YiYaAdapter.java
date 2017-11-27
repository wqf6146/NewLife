package com.yhkj.yymall.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yhkj.yymall.R;
import com.yhkj.yymall.bean.YiyaListBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/27.
 */

public class YiYaAdapter extends RecyclerView.Adapter<YiYaAdapter.ViewHolder> {

    List<YiyaListBean.DataBean.GoodsBean> mDatas;

    protected YiYaAdapter(List<YiyaListBean.DataBean.GoodsBean> goodsBean){
        mDatas = goodsBean;
    }

    @Override
    public int getItemCount() {
        return mDatas.size() + 1;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = null;
//        if (viewType == -1){
//            //head
//            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yiyamall_top,parent,false);
//        }else{
//            view = LayoutInflater.from()
//        }
//        return view;
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0){
            return -1;
        }
        return super.getItemViewType(position);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view){
            super(view);
        }
    }
}
