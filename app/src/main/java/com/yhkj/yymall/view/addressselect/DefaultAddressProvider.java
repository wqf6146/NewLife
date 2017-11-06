package com.yhkj.yymall.view.addressselect;

import android.content.Context;

import com.vise.log.ViseLog;
import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.yhkj.yymall.bean.PlacesBean;
import com.yhkj.yymall.http.YYMallApi;

import java.util.ArrayList;

public class DefaultAddressProvider implements AddressProvider {
    Context mContext;
    public DefaultAddressProvider(Context context) {
        mContext = context;
    }

    @Override
    public void provideProvinces(final AddressReceiver<PlacesBean.DataBean> addressReceiver) {
        YYMallApi.getPlaces(mContext, 0, new ApiCallback<PlacesBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                ViseLog.e(e);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(PlacesBean dataBean) {
                addressReceiver.send(new ArrayList<>(dataBean.getData()));
            }
        });

    }

    @Override
    public void provideCitiesWith(int provinceId, final AddressReceiver<PlacesBean.DataBean> addressReceiver) {

        YYMallApi.getPlaces(mContext, provinceId, new ApiCallback<PlacesBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                ViseLog.e(e);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(PlacesBean dataBean) {
                addressReceiver.send(new ArrayList<>(dataBean.getData()));
            }
        });
    }

    @Override
    public void provideCountiesWith(int cityId, final AddressReceiver<PlacesBean.DataBean> addressReceiver) {

        YYMallApi.getPlaces(mContext, cityId, new ApiCallback<PlacesBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                ViseLog.e(e);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(PlacesBean dataBean) {
                addressReceiver.send(new ArrayList<>(dataBean.getData()));
            }
        });
    }

    @Override
    public void provideStreetsWith(int countyId, final AddressReceiver<PlacesBean.DataBean> addressReceiver) {


        YYMallApi.getPlaces(mContext, countyId, new ApiCallback<PlacesBean>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onError(ApiException e) {
                ViseLog.e(e);
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onNext(PlacesBean dataBean) {
                addressReceiver.send(new ArrayList<>(dataBean.getData()));
            }
        });
    }
}
