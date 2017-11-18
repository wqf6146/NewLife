package com.yhkj.yymall.presenter.base;

import android.app.Activity;

import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.bean.OrderListBean;

/**
 * Created by Administrator on 2017/7/27.
 */

public abstract class  BaseOrderListPresenter {

    BaseOrderListView mBaseOrderListView;
    public BaseOrderListPresenter(BaseOrderListView baseOrderView){
        mBaseOrderListView = baseOrderView;
    }

    // 获取当前订单类别list

    /**
     *
     * @param ptype   （1为全部，2为非租赁，3为租赁）
     * @param status 订单状态（空为全部1为代付款 2为待收货，5待评价）
     * @param page
     * @param isComment 是否评论
     * @param isGroup 是否成团 (0成团 1)
     */
    public abstract <T> void getOrderList(boolean bshow,final Integer ptype, final Integer status, final Integer page,
                                             Integer isComment,Integer isGroup);

    public interface BaseOrderListView <T> {
        Activity getActivity();

        /**
         * 获取订单失败
         */
        void getOrderListInfoFaild(ApiException e);

        /**
         * 获取订单列表成功
         */
        void getOrderListInfoSuc(T dataBean);
    }
}
