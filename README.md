# NetRequestDemo
# 统一网络请求

 
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
# Presenter   

    public void getCatByLinNet(Map<String, String> map) {
        if (!isViewAttached()) {
            return;
        }
        if (!isHaveNet()) {
            return;
        }
        getView().showLoading();
        addSubscription(RxNet.request(ApiManager.getClient().getCat(map), new RxNetCallBack<List<CatBean>>() {
            @Override
            public void onSuccess(List<CatBean> data) {
                if (isViewAttached()) {
                    getView().hideLoading();
                    getView().showToast("获取列表成功");
                }

            }

            @Override
            public void onFailure(String msg) {
                handleFailed(msg, true);
            }
        }));
    }


    
    
    
    
    
