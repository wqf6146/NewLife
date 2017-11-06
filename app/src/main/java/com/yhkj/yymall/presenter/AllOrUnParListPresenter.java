package com.yhkj.yymall.presenter;

import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.bean.OrderListBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.presenter.base.BaseOrderListPresenter;

/**
 * Created by Administrator on 2017/7/27.
 */

public class AllOrUnParListPresenter extends BaseOrderListPresenter {

    BaseOrderListView mOrderListView;

    public AllOrUnParListPresenter(BaseOrderListView baseOrderListView){
        super(baseOrderListView);
        mOrderListView = baseOrderListView;
    }

    @Override
    public <T> void getOrderList(boolean bshow,Integer ptype, Integer status, Integer page, Integer isComment, Integer isGroup) {
        YYMallApi.getAllOrUnPayOrderList(mOrderListView.getActivity(),ptype,status, page,bshow,
                new YYMallApi.ApiResult<OrderListBean.DataBean>(mOrderListView.getActivity()) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                ViseLog.e(e);
                mOrderListView.getOrderListInfoFaild(e);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(OrderListBean.DataBean dataBean) {
                mOrderListView.getOrderListInfoSuc(dataBean);
            }
        });
    }
}
