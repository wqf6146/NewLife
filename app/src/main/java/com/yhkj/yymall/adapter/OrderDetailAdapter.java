package com.yhkj.yymall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yhkj.yymall.R;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/4.
 */

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {

    Context mContext;

    public OrderDetailAdapter(Context context){
        mContext = context;
    }

    @Override
    public OrderDetailAdapter.OrderDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderDetailViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_orderdetail,parent,false));
    }

    @Override
    public void onBindViewHolder(OrderDetailAdapter.OrderDetailViewHolder holder, int position) {
        List<Object> objectList = new ArrayList<>();
        objectList.add("");
        objectList.add("");
        objectList.add("");
        holder.mListView.setAdapter((new NestFullListViewAdapter<Object>(R.layout.item_group_entity,objectList) {
            @Override
            public void onBind(int pos, Object o, NestFullViewHolder holder) {

            }
        }));
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class OrderDetailViewHolder extends RecyclerView.ViewHolder{

        NestFullListView mListView;

        public OrderDetailViewHolder(View view){
            super(view);
            mListView = (NestFullListView)itemView.findViewById(R.id.iod_listview);
        }
    }
}
