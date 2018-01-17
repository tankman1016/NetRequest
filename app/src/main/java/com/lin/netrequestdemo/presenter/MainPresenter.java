package com.lin.netrequestdemo.presenter;

import com.lin.netrequestdemo.data.RxNet;
import com.lin.netrequestdemo.data.RxNetCallBack;
import com.lin.netrequestdemo.data.api.ApiManager;
import com.lin.netrequestdemo.data.entity.CatBean;
import com.lin.netrequestdemo.ui.view.MainView;

import java.util.List;
import java.util.Map;

/**
 *
 */

public class MainPresenter extends BasePresenter<MainView> {

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
}
