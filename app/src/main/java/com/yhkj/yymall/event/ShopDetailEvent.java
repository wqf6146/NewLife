package com.yhkj.yymall.event;

import com.vise.xsnow.event.IEvent;

/**
 * Created by Administrator on 2017/7/10.
 */

public class ShopDetailEvent implements IEvent {
    private int mType;
    private int sType;
    public ShopDetailEvent(int type ,int types){
        mType = type;
        sType = types;
    }

    public int getmType() {
        return mType;
    }
    public int getsType() {
        return sType;
    }

}
