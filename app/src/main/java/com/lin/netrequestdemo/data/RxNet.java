package com.lin.netrequestdemo.data;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class RxNet {
    /**
     * 统一处理单个请求
     *
     * @param observable
     * @param callBack
     * @return
     */
    public static <T> Subscription request(Observable<BaseResponse<T>> observable, final RxNetCallBack<T> callBack) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .onErrorReturn(new Func1<Throwable, BaseResponse<T>>() {
                    @Override
                    public BaseResponse<T> call(Throwable throwable) {
                        callBack.onFailure(ExceptionHandle.handleException(throwable));
                        return null;
                    }
                })
                .subscribe(new Subscriber<BaseResponse<T>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseResponse<T> baseResponse) {
                        if (baseResponse.getCode().equals("200")) {
                            callBack.onSuccess(baseResponse.getData());
                        } else {
                            callBack.onFailure(baseResponse.getMsg());
                        }
                    }
                });

    }
}
