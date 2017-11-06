package com.vise.xsnow.net.callback;

import com.vise.xsnow.net.exception.ApiException;

/**
 * @Description: API操作回调
 */


public abstract class ApiCallback<T> {
    public abstract void onStart();

    public void onError(ApiException e){

    }

    public abstract void onCompleted();

    public abstract void onNext(T t);
}
