package com.yhkj.yymall.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/11/14.
 */

@Entity
public class BaseConfig {
    @Id
    Long id;

    int actBit;

    public int getActBit() {
        return this.actBit;
    }

    public void setActBit(int actBit) {
        this.actBit = actBit;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Generated(hash = 1431119375)
    public BaseConfig(Long id, int actBit) {
        this.id = id;
        this.actBit = actBit;
    }

    @Generated(hash = 1736079804)
    public BaseConfig() {
    }
}
