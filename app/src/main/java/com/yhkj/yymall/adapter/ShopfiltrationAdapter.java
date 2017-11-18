package com.yhkj.yymall.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yhkj.yymall.R;

/**
 * Created by Administrator on 2017/7/6.
 */

public class ShopfiltrationAdapter extends RecyclerView.Adapter<ShopfiltrationAdapter.ViewHolder> {

    private Context mContext;

    public ShopfiltrationAdapter(Context context){
        mContext = context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        ((TextView)holder.itemView.findViewById(R.id.ift_tv))
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShopfiltrationAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_fillder_tv,parent,false));
    }

    @Override
    public int getItemCount() {
        return 12;
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View view){
            super(view);
        }
    }
}
