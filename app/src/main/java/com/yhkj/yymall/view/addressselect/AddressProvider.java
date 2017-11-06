package com.yhkj.yymall.view.addressselect;

import com.yhkj.yymall.bean.PlacesBean;

import java.util.List;


public interface AddressProvider {
    void provideProvinces(AddressReceiver<PlacesBean.DataBean> addressReceiver);
    void provideCitiesWith(int provinceId, AddressReceiver<PlacesBean.DataBean> addressReceiver);
    void provideCountiesWith(int cityId, AddressReceiver<PlacesBean.DataBean> addressReceiver);
    void provideStreetsWith(int countyId, AddressReceiver<PlacesBean.DataBean> addressReceiver);

    interface AddressReceiver<T> {
        void send(List<T> data);
    }
}