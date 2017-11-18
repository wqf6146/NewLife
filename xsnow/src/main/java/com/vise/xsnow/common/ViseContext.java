package com.vise.xsnow.common;

import android.content.Context;

import com.vise.log.ViseLog;
import com.vise.log.inner.DefaultTree;

/**
 * @Description: 全局初始化配置
 */
public class ViseContext {
    private static ViseContext instance;
    private Context context;

    private ViseContext() {
    }

    public static ViseContext getInstance() {
        if (instance == null) {
            synchronized (ViseContext.class) {
                if (instance == null) {
                    instance = new ViseContext();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        initLog();
    }

    public Context getContext() {
        return context;
    }

    private void initLog() {
        ViseLog.getLogConfig().configAllowLog(true).configShowBorders(true);
        ViseLog.plant(new DefaultTree());
    }
}
