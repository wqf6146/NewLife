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
    String token;

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

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Generated(hash = 926575067)
    public BaseConfig(Long id, int actBit, String token) {
        this.id = id;
        this.actBit = actBit;
        this.token = token;
    }

    @Generated(hash = 1736079804)
    public BaseConfig() {
    }
}
