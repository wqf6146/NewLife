package com.yhkj.yymall.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/7/12.
 */

@Entity
public class UserConfig {
    @Id
    Long id;
    String token;
    Boolean state;
    String phone;
    int actBit;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setActBit(int actBit) {
        this.actBit = actBit;
    }

    public int getActBit() {
        return actBit;
    }

    @Generated(hash = 1285892582)
    public UserConfig(Long id, String token, Boolean state, String phone, int actBit) {
        this.id = id;
        this.token = token;
        this.state = state;
        this.phone = phone;
        this.actBit = actBit;
    }

    @Generated(hash = 523434660)
    public UserConfig() {
    }
}
