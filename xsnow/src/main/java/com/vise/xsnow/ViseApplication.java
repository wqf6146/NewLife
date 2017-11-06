package com.vise.xsnow;

import android.app.Application;

import com.vise.xsnow.common.ViseContext;

/**
 * @Description: Application基类
 */
public class ViseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ViseContext.getInstance().init(this);
    }
}
