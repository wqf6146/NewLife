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

    @Generated(hash = 1099987791)
    public UserConfig(Long id, String token, Boolean state) {
        this.id = id;
        this.token = token;
        this.state = state;
    }

    @Generated(hash = 523434660)
    public UserConfig() {
    }
}
