package com.yhkj.yymall.presenter;

import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.bean.OrderChildBean;
import com.yhkj.yymall.http.YYMallApi;
import com.yhkj.yymall.presenter.base.BaseOrderListPresenter;

/**
 * Created by Administrator on 2017/7/27.
 */

public class BehindThreeOrderListPresenter extends BaseOrderListPresenter {

    BaseOrderListPresenter.BaseOrderListView mOrderSettleView;

    public BehindThreeOrderListPresenter(BaseOrderListPresenter.BaseOrderListView baseOrderListView){
        super(baseOrderListView);
        mOrderSettleView = baseOrderListView;
    }


    @Override
    public <T> void getOrderList(boolean bshow,Integer ptype, Integer status, Integer page, Integer isComment, Integer isGroup) {
        YYMallApi.getOrderThree(mOrderSettleView.getActivity(),status,ptype, page, isComment,isGroup,bshow,new YYMallApi.ApiResult<OrderChildBean.DataBean>(mOrderSettleView.getActivity()) {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                super.onError(e);
                mOrderSettleView.getOrderListInfoFaild(e);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(OrderChildBean.DataBean dataBean) {
                mOrderSettleView.getOrderListInfoSuc(dataBean);
            }
        });
    }
}
