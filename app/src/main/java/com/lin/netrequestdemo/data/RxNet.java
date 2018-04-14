package com.lin.netrequestdemo.data;


import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxNet {
    /**
     * 统一处理单个请求
     *
     * @param observable
     * @param callBack
     * @param <T>
     */
    public static <T> Disposable request(Observable<BaseResponse<T>> observable, final RxNetCallBack<T> callBack) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, BaseResponse<T>>() {
                    @Override
                    public BaseResponse<T> apply(Throwable throwable) {
                        Log.v("LinNetError", throwable.getMessage());
                        callBack.onFailure(ExceptionHandle.handleException(throwable));
                        return null;
                    }
                })
                .subscribe(new Consumer<BaseResponse<T>>() {
                    @Override
                    public void accept(BaseResponse<T> tBaseResponse) {
                        if (tBaseResponse.getCode().equals("200")) {
                            callBack.onSuccess(tBaseResponse.getData());

                        } else {
                            callBack.onFailure(tBaseResponse.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.v("LinNetError", "单个请求的错误" + throwable.getMessage());
                    }
                });
    }

    /**
     * 统一处理单个请求
     * 返回数据没有body
     */
    public static Disposable requestWithoutBody(Observable<BaseResponse> observable,
                                                final RxNetCallBack<String> callBack) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, BaseResponse>() {
                    @Override
                    public BaseResponse apply(Throwable throwable) {
                        Log.v("LinNetError", throwable.getMessage());
                        callBack.onFailure(ExceptionHandle.handleException(throwable));
                        return null;
                    }
                })
                .subscribe(new Consumer<BaseResponse>() {
                    @Override
                    public void accept(BaseResponse baseResponse) {
                        if (baseResponse.getCode().equals("200")) {
                            callBack.onSuccess(baseResponse.getMsg());
                        } else {
                            callBack.onFailure(baseResponse.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.v("LinNetError", "单个请求的错误:没有body" + throwable.getMessage());
                    }
                });

    }
}
