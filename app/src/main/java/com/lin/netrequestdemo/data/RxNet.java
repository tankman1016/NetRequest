package com.lin.netrequestdemo.data;


import com.lin.netrequestdemo.util.AppLogUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class RxNet {

    /**
     * 一般请求，返回数据带有body
     */

    public static <T> Disposable request(Observable<BaseResponse<T>> observable, final RxNetCallBack<T> callBack) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, BaseResponse<T>>() {
                    @Override
                    public BaseResponse<T> apply(Throwable throwable) {

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
                        AppLogUtil.printE("请求错误:" + throwable.getMessage());
                    }
                });
    }

    /**
     * 返回数据没有body
     */

    public static Disposable requestWithoutBody(Observable<BaseResponse> observable,
                                                final RxNetCallBack<String> callBack) {
        return observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, BaseResponse>() {
                    @Override
                    public BaseResponse apply(Throwable throwable) {
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
                        AppLogUtil.printE("请求错误:" + throwable.getMessage());
                    }
                });

    }

    /**
     * 请求返回错误请求码
     * -1表示请求失败
     */

    public static <T> Disposable requestForCode(Observable<BaseResponse<T>> observable, final RxNetCallBackForCode<T> callBack) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Function<Throwable, BaseResponse<T>>() {
                    @Override
                    public BaseResponse<T> apply(Throwable throwable) {
                        // -1表示请求失败
                        callBack.onFailure("-1", ExceptionHandle.handleException(throwable));
                        return null;
                    }
                })
                .subscribe(new Consumer<BaseResponse<T>>() {
                    @Override
                    public void accept(BaseResponse<T> tBaseResponse) {
                        if (tBaseResponse.getCode().equals("200")) {
                            callBack.onSuccess(tBaseResponse.getData());
                        } else {
                            callBack.onFailure(tBaseResponse.getCode(), tBaseResponse.getMsg());
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        AppLogUtil.printE("请求错误:" + throwable.getMessage());
                    }
                });
    }

    /**
     * 下载和上传
     */

}
