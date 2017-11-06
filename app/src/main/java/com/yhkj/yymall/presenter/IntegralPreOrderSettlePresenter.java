package com.yhkj.yymall.presenter;


import android.content.Intent;
import android.util.SparseArray;
import android.widget.Toast;

import com.vise.xsnow.manager.AppManager;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.YYApp;
import com.yhkj.yymall.activity.PayResultActivity;
import com.yhkj.yymall.activity.PaymentAcitivity;
import com.yhkj.yymall.base.Constant;
import com.yhkj.yymall.bean.CheckOutBean;
import com.yhkj.yymall.bean.CheckOutOkBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.presenter.base.BaseOrderSettlePresenter;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/21.
 * 积分订单逻辑
 */

public class IntegralPreOrderSettlePresenter extends BaseOrderSettlePresenter {

    BaseOrderSettleView mPreOrderView;

    public IntegralPreOrderSettlePresenter(BaseOrderSettleView preOrderView){
        super(preOrderView);
        mPreOrderView = preOrderView;
    }


    /**
     * 获取订单信息
     */
    public void getOrderInfo(Boolean bShow,String productId, String nums,String panicBuyId,
                             String groupUserId,Integer addressid,String discountId,String integralId,String dailyId){
        YYMallApi.getIntegralPreviewOrder(mPreOrderView.getActivity(),integralId, productId, nums,addressid,bShow,
                new YYMallApi.ApiResult<CheckOutBean.DataBean>(mPreOrderView.getActivity()) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                mPreOrderView.getPreOrderFail(e);
                Toast.makeText(mPreOrderView.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(final CheckOutBean.DataBean dataBean) {
                mPreOrderView.getOrderInfo(dataBean);
            }
        });
    }

    /**
     * 提交订单
     */
    public void submitOrder(String addressId, String useYaya, String productId,
                            String nums,String panicBuyId,String groupUserId,SparseArray delivery,String discountId,final String integralId,String dailyId){

        HashMap hashMap = new HashMap();
        if (delivery != null || delivery.size() > 0) {
            HashMap valueMap = new HashMap();
            for (int i=0; i<delivery.size();i++){
                valueMap.put(String.valueOf(delivery.keyAt(i)),String.valueOf(delivery.valueAt(i)));
            }
            hashMap.put("delivery", valueMap);
        }

        YYMallApi.getIntegralOrder(mPreOrderView.getActivity(), integralId,addressId,  productId, nums,hashMap,
                new YYMallApi.ApiResult<CheckOutOkBean.DataBean>(mPreOrderView.getActivity()) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                Toast.makeText(mPreOrderView.getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(CheckOutOkBean.DataBean dataBean) {
                if  (dataBean.getTotal() == 0.0d){
                    //免邮状态
                    mPreOrderView.submitOrderSuccess(dataBean);
                    Intent intent = new Intent(mPreOrderView.getActivity(), PayResultActivity.class);
                    intent.putExtra("integralId", dataBean.getIntegral());
                    intent.putExtra("orderNo", dataBean.getOrderNo() + "");
                    intent.putExtra("total", "-2");
                    mPreOrderView.getActivity().startActivity(intent);
                    AppManager.getInstance().finishActivity(mPreOrderView.getActivity());
                }else {
                    Intent intent = new Intent(mPreOrderView.getActivity(), PaymentAcitivity.class);
                    intent.putExtra("orderNo", dataBean.getOrderNo() + "");
                    intent.putExtra("total", dataBean.getTotal() + "");
                    mPreOrderView.getActivity().startActivity(intent);
                    AppManager.getInstance().finishActivity(mPreOrderView.getActivity());
                }
            }
        });
    }

    @Override
    public <T> void getOrderTwoList(Integer status, Integer page, ApiCallback<T> callback) {

    }

}
