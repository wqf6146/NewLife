package com.yhkj.yymall.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yhkj.yymall.R;
import com.yhkj.yymall.activity.OrderDetailActivity;
import com.yhkj.yymall.view.NestFullListView.NestFullListView;
import com.yhkj.yymall.view.NestFullListView.NestFullListViewAdapter;
import com.yhkj.yymall.view.NestFullListView.NestFullViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/3.
 */

public class OrderRecycleAdapter extends RecyclerView.Adapter<OrderRecycleAdapter.ViewHolder> {

    Context mContext;

    public OrderRecycleAdapter(Context context){
        mContext = context;
    }

    @Override
    public OrderRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrderRecycleAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order_child,parent,false));
    }

    @Override
    public void onBindViewHolder(OrderRecycleAdapter.ViewHolder holder, int position) {

        List<Object> objectList = new ArrayList<>();
        objectList.add("");
        objectList.add("");
        objectList.add("");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(mContext, OrderDetailActivity.class);
                mContext.startActivity(intent);
            }
        });
        ((NestFullListView)holder.itemView.findViewById(R.id.ioc_shoplistview)).setAdapter(new NestFullListViewAdapter<Object>(R.layout.item_group_entity,objectList) {
            @Override
            public void onBind(int pos, Object o, final NestFullViewHolder holder) {
                holder.getConvertView().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return 10;
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
