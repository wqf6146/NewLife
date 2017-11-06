package com.yhkj.yymall.presenter.base;

import android.app.Activity;
import android.util.SparseArray;

import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.bean.CheckOutBean;
import com.yhkj.yymall.bean.CheckOutOkBean;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/21.
 * 预览于结算
 */

public abstract class BaseOrderSettlePresenter {

    BaseOrderSettleView mBaseOrderView;
    public BaseOrderSettlePresenter(BaseOrderSettleView baseOrderView){
        mBaseOrderView = baseOrderView;
    }

    /**
     * 提交订单
     */
    public abstract void submitOrder(String addressId, String useYaya, String productId, String nums,
                                     String panicBuyId, String groupUserId, SparseArray delivery,String discountId,String integralId,String dailyId);

    /**
     * 获取订单信息
     */
    public abstract void getOrderInfo(Boolean bShow,String productId,
                                      String nums,String panicBuyId,String groupUserId,Integer addressid,String discountId,String integralId,String dailyId);

    public abstract <T> void getOrderTwoList(final Integer status, final Integer page,ApiCallback<T> callback);

    public interface BaseOrderSettleView {
        Activity getActivity();
        /**
         * 提交订单成功
         */
        void submitOrderSuccess(CheckOutOkBean.DataBean bean);
        /**
         * 提交订单失败
         */
        void submitOrderFaild();

        /**
         * 获取订单信息成功
         */
        void getOrderInfo(CheckOutBean.DataBean dataBean);

        void getPreOrderFail(ApiException e);

    }
}
