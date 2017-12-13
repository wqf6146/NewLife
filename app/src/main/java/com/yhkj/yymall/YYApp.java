package com.yhkj.yymall;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.umeng.socialize.PlatformConfig;
import com.yhkj.yymall.base.DbHelper;
import com.yhkj.yymall.base.HxHelper;
import com.yhkj.yymall.bean.UserConfig;

import cn.beecloud.BeeCloud;
import cn.jpush.android.api.JPushInterface;


/**
 * Created by Administrator on 2017/6/19.
 */

public class YYApp extends Application {
    private static YYApp mInstance;
    private static boolean isForeground = false;
    public static YYApp getInstance() {
        return mInstance;
    }

    private String mToken;
    private String mHotSearch;
    private String mPhone;
    private String mRegistrationId;

    public void setRegistrationId(String registrationId) {
        this.mRegistrationId = registrationId;
    }

    public String getRegistrationId() {
        return mRegistrationId;
    }

    public String getmHotSearch() {
        return mHotSearch;
    }

    public void setmHotSearch(String mHotSearch) {
        this.mHotSearch = mHotSearch;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getPhone() {
        if (mPhone == null) {
            UserConfig userConfig = DbHelper.getInstance().userConfigLongDBManager().load(1l);
            if (userConfig != null && userConfig.getState()) {
                mPhone = userConfig.getPhone();
            }
        }
        return mPhone == null ? "" : mPhone;
    }

    public int mIndexShow = -1;
    public void setIndexShow(int index){
        mIndexShow = index;
    }
    public int getIndexShow(){
        int res = mIndexShow;
        mIndexShow =-1;
        return res;
    }

    public String getToken() {
        if (mToken == null) {
            UserConfig userConfig = DbHelper.getInstance().userConfigLongDBManager().load(1l);
            if (userConfig != null && userConfig.getState()) {
                mToken = userConfig.getToken();
            }
        }
        return mToken;
    }

    public void setToken(String token) {
        this.mToken = token;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        initHotfix();
    }

    private void initHotfix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }

        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                //.setAesKey("0123456789123456")
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        String msg = new StringBuilder("").append("Mode:").append(mode)
                                .append(" Code:").append(code)
                                .append(" Info:").append(info)
                                .append(" HandlePatchVersion:").append(handlePatchVersion).toString();
                        Log.e("initHotfix",msg);
                    }
                }).initialize();
    }



    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        DbHelper.getInstance().init(mInstance);
        if (HxHelper.getInstance().init(mInstance)){
            HxHelper.getInstance().initUi();
        }
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
                JPushInterface.init(mInstance);     // 初始化 JPush
                mRegistrationId = JPushInterface.getRegistrationID(mInstance);

                /**
                 * 初始化BeeCloud账户
                 */
                BeeCloud.setAppIdAndSecret("d37ead18-00f5-4c53-96b6-1f7222b575dc", "5852fb24-e2ed-4a74-a44a-168a846e45b0");
                /**
                 * 开启测试模式,默认false，正式开始的时候直接注释就行
                 */
                //BeeCloud.setSandbox(true);


                /**
                 * 友盟三方登录分享的各平台appkey value 新浪多出一个url
                 * 初始化sdk
                 */
                PlatformConfig.setWeixin("wx1b4e339277365985", "0f4839759e9ef7a57ef8d0b4ffc1d259");
                PlatformConfig.setQQZone("1106319912", "znVjPvZAwQQrxXaB");
                PlatformConfig.setSinaWeibo("3789899951", "862a78fd0ff2dfad350a4fa02f9e9184", "http://www.yiyiyaya.cc");

//                Cockroach.install(new Cockroach.ExceptionHandler() {
//
//                    // handlerException内部建议手动try{  你的异常处理逻辑  }catch(Throwable e){ } ，以防handlerException内部再次抛出异常，导致循环调用handlerException
//
//                    @Override
//                    public void handlerException(final Thread thread, final Throwable throwable) {
//                        new Handler(Looper.getMainLooper()).post(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    //写本地
//                                    AECHFileWriter.getInstance(mInstance).writeEx2File(throwable);
//                                    //写服务器
//                                    Toast.makeText(mInstance, "Exception Happend\n" + thread + "\n" + throwable.toString(), Toast.LENGTH_SHORT).show();
//                                } catch(Throwable e){
//                                    Toast.makeText(mInstance, "Exception Happend\n" + thread + "\n" + e.toString(), Toast.LENGTH_SHORT).show();
//                                    ViseLog.e(e);
//                                }
//                            }
//                        });
//                    }
//                });
            }
        }).start();


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                isForeground = true;
            }

            @Override
            public void onActivityPaused(Activity activity) {
                isForeground = false;
            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
    public boolean isForeground() {
        return isForeground;
    }
}
