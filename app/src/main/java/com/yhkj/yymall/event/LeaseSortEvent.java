package com.yhkj.yymall.event;

import com.vise.xsnow.event.IEvent;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/7/14.
 */

public class LeaseSortEvent implements IEvent {
    public int type;
    public String order;
    public String by;
    public String page;
    public String limit;
    public String brand;
    public Attr attr;

    public String getAttrKey() {
        return attr.key;
    }

    public String getAttrValue() {
        return attr.value;
    }



    public LeaseSortEvent(int type, String order, String by, String page, String limit, String brand, String key, String value){
        this.type = type;
        this.order = order;
        this.by = by;
        this.page = page;
        this.limit = limit;
        this.brand = brand;
        this.attr = new Attr(key,value);
    }
    class Attr {
        public String key;
        public String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public Attr(String key, String value){
           this.key = key;
            this.value = value;
        }
    }
}
