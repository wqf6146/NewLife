package com.yhkj.yymall.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import java.util.ArrayList;

public class NetStateReceiver extends BroadcastReceiver {

    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private final static String TAG = NetStateReceiver.class.getSimpleName();

    private static boolean isNetAvailable = false;
    private static ArrayList<NetChangeObserver> mNetChangeObservers = new ArrayList<NetChangeObserver>();
    private static BroadcastReceiver mBroadcastReceiver;
    private int mNetType;

    private static BroadcastReceiver getReceiver() {
        if (null == mBroadcastReceiver) {
            synchronized (NetStateReceiver.class) {
                if (null == mBroadcastReceiver) {
                    mBroadcastReceiver = new NetStateReceiver();
                }
            }
        }
        return mBroadcastReceiver;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mBroadcastReceiver = NetStateReceiver.this;
        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)) {
            if (!NetWorkUtils.isNetworkConnected(context)) {
                Log.e(this.getClass().getName(), "<--- network disconnected --->");
                isNetAvailable = false;
            } else {
                Log.e(this.getClass().getName(), "<--- network connected --->");
                isNetAvailable = true;
                mNetType = NetWorkUtils.getAPNType(context);
            }
            notifyObserver();
        }
    }

    /**
     * 注册
     *
     * @param mContext
     */
    public static void registerNetworkStateReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        mContext.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    /**
     * 清除
     *
     * @param mContext
     */
    public static void checkNetworkState(Context mContext) {
        Intent intent = new Intent();
        intent.setAction(ANDROID_NET_CHANGE_ACTION);
        mContext.sendBroadcast(intent);
    }

    /**
     * 反注册
     *
     * @param mContext
     */
    public static void unRegisterNetworkStateReceiver(Context mContext) {
        if (mBroadcastReceiver != null) {
            try {
                mContext.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {

            }
        }

    }

    public static boolean isNetworkAvailable() {
        return isNetAvailable;
    }

//    public static HttpUtil.NetType getAPNType() {
//        return mNetType;
//    }

    private void notifyObserver() {
        if (!mNetChangeObservers.isEmpty()) {
            int size = mNetChangeObservers.size();
            for (int i = 0; i < size; i++) {
                NetChangeObserver observer = mNetChangeObservers.get(i);
                if (observer != null) {
                    if (isNetworkAvailable()) {
                        observer.onNetConnected(mNetType);
                    } else {
                        observer.onNetDisConnect();
                    }
                }
            }
        }
    }

    /**
     * 添加网络监听
     *
     * @param observer
     */
    public static void registerObserver(NetChangeObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<NetChangeObserver>();
        }
        mNetChangeObservers.add(observer);
    }

    /**
     * 移除网络监听
     *
     * @param observer
     */
    public static void removeRegisterObserver(NetChangeObserver observer) {
        if (mNetChangeObservers != null) {
            if (mNetChangeObservers.contains(observer)) {
                mNetChangeObservers.remove(observer);
            }
        }
    }


    public interface NetChangeObserver {
        /**
         * 网络连接回调 type为网络类型
         */
        void onNetConnected(int type);
        /**
         * 没有网络
         */
        void onNetDisConnect();
    }
}