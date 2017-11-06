package com.yhkj.yymall.view.addressselect;

import com.yhkj.yymall.bean.PlacesBean;

public interface OnAddressSelectedListener {
    void onAddressSelected(PlacesBean.DataBean province, PlacesBean.DataBean city, PlacesBean.DataBean county, PlacesBean.DataBean street);
}
