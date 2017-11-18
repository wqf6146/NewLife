package com.yhkj.yymall.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/6/26.
 */
@Entity
public class RecnetSearchBean {
    @Id
    Long id;
    String mText;
    public String getMText() {
        return this.mText;
    }
    public void setMText(String mText) {
        this.mText = mText;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    @Generated(hash = 868932781)
    public RecnetSearchBean(Long id, String mText) {
        this.id = id;
        this.mText = mText;
    }
    @Generated(hash = 1754093437)
    public RecnetSearchBean() {
    }
    public RecnetSearchBean(String text){
        mText = text;
    }
}
