package com.vise.xsnow.net.func;

import com.vise.xsnow.net.exception.ApiException;

import rx.Observable;
import rx.functions.Func1;

/**
 * @Description: Throwable转Observable<T>
 */
public class ApiErrFunc<T> implements Func1<Throwable, Observable<T>> {
    @Override
    public Observable<T> call(Throwable throwable) {
        return Observable.error(ApiException.handleException(throwable));
    }
}
