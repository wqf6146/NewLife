package com.vise.xsnow.net.subscriber;

import android.app.Activity;
import android.content.Context;

import com.vise.xsnow.net.callback.ApiCallback;
import com.vise.xsnow.net.exception.ApiException;
import com.vise.xsnow.ui.basepopup.LodingPopup;

/**
 * @Description: 包含回调的订阅者，如果订阅这个，上层在不使用订阅者的情况下可获得回调
 */
public class ApiCallbackSubscriber<T> extends ApiSubscriber<T> {

    protected ApiCallback<T> callBack;

    //注入加载对话框

    private LodingPopup mLoadPop;
    private boolean bShowLoadView;
    public ApiCallbackSubscriber(Context context, boolean loadview, ApiCallback<T> callBack) {
        super(context);
        if (callBack == null) {
            throw new NullPointerException("this callback is null!");
        }
        this.callBack = callBack;
        bShowLoadView = loadview;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (bShowLoadView){
            if (mLoadPop == null)
                mLoadPop = new LodingPopup((Activity)contextWeakReference.get());
            mLoadPop.showPopupWindow();
        }

        callBack.onStart();
    }

    @Override
    public void onError(ApiException e) {
        if (bShowLoadView){
            if (mLoadPop != null)
                mLoadPop.dismiss();
        }
        callBack.onError(e);
    }

    @Override
    public void onCompleted() {
        callBack.onCompleted();
    }

    @Override
    public void onNext(T t) {
        if (bShowLoadView){
            if (mLoadPop != null)
                mLoadPop.dismiss();
        }
        callBack.onNext(t);
    }
}
