package com.yhkj.yymall.event;

import com.vise.xsnow.event.IEvent;

/**
 * Created by Administrator on 2017/7/18.
 */

public class MainTabSelectEvent implements IEvent {
    public int tab;
    public MainTabSelectEvent(int tab){
        this.tab = tab;
    }
}
