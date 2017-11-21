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
    String headIco;
    

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

    public String getHeadIco() {
        return this.headIco;
    }

    public void setHeadIco(String headIco) {
        this.headIco = headIco;
    }

    @Generated(hash = 1715376617)
    public UserConfig(Long id, String token, Boolean state, String phone,
            String headIco) {
        this.id = id;
        this.token = token;
        this.state = state;
        this.phone = phone;
        this.headIco = headIco;
    }

    @Generated(hash = 523434660)
    public UserConfig() {
    }
}
