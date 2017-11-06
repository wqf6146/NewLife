package com.yhkj.yymall.bean;

/**
 * Created by Administrator on 2017/7/3.
 */

public class DataBean {
    public static final int PARENT_ITEM = 0;//父布局
    public static final int CHILD_ITEM = 1;//子布局
    public static final int CLEAR = 2;//清空失效商品
    public static final int FAILURE = 3;//失效商品

    private int type;// 显示类型
    private String parentLeftTxt;
    private String childLeftTxt;

    public String getParentLeftTxt() {
        return parentLeftTxt;
    }

    public void setParentLeftTxt(String parentLeftTxt) {
        this.parentLeftTxt = parentLeftTxt;
    }

    public String getChildLeftTxt() {
        return childLeftTxt;
    }

    public void setChildLeftTxt(String childLeftTxt) {
        this.childLeftTxt = childLeftTxt;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
