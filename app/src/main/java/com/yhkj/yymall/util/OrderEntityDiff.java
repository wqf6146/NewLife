package com.yhkj.yymall.util;

import android.support.v7.util.DiffUtil;

import com.yhkj.yymall.bean.OrderChildBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class OrderEntityDiff extends DiffUtil.Callback {

    private List<OrderChildBean.DataBean.OrdersBean> mOldOrderBean;
    private List<OrderChildBean.DataBean.OrdersBean> mNewOrderBean;

    public OrderEntityDiff(List<OrderChildBean.DataBean.OrdersBean> newDatas, List<OrderChildBean.DataBean.OrdersBean> oldDatas){
        mOldOrderBean = oldDatas;
        mNewOrderBean = newDatas;
    }

    @Override
    public int getNewListSize() {
        return mNewOrderBean.size();
    }

    @Override
    public int getOldListSize() {
        return mOldOrderBean.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewOrderBean.get(newItemPosition).equals(mOldOrderBean.get(oldItemPosition));
    }

//    @Nullable
//    @Override
//    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
//        OrderChildBean.DataBean.OrdersBean oldBean = mOldOrderBean.get(oldItemPosition);
//        OrderChildBean.DataBean.OrdersBean newBean = mNewOrderBean.get(newItemPosition);
//
//        Bundle payLoad = new Bundle();
//
//        if (oldBean.)
//
//        if (payLoad.size() == 0){
//            return null;
//        }
//        return payLoad;
//    }
}
