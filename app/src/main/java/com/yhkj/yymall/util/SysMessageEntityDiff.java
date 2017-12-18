package com.yhkj.yymall.util;

import android.support.v7.util.DiffUtil;

import com.yhkj.yymall.bean.MessageListBean;
import com.yhkj.yymall.bean.OrderChildBean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/4.
 */

public class SysMessageEntityDiff extends DiffUtil.Callback {

    private List<MessageListBean.DataBean.ListBean> mOldBean;
    private List<MessageListBean.DataBean.ListBean> mNewBean;

    public SysMessageEntityDiff(List<MessageListBean.DataBean.ListBean> newDatas, List<MessageListBean.DataBean.ListBean> oldDatas){
        mOldBean = oldDatas;
        mNewBean = newDatas;
    }

    @Override
    public int getNewListSize() {
        return mNewBean.size();
    }

    @Override
    public int getOldListSize() {
        return mOldBean.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        MessageListBean.DataBean.ListBean newBean = mNewBean.get(newItemPosition);
        MessageListBean.DataBean.ListBean oldBean = mOldBean.get(newItemPosition);
        return newBean.getId() == oldBean.getId()
                && newBean.getStatus().equals(oldBean.getStatus());
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
