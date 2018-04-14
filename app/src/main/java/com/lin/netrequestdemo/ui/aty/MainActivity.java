package com.lin.netrequestdemo.ui.aty;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;

import com.lin.netrequestdemo.R;
import com.lin.netrequestdemo.data.RxNet;
import com.lin.netrequestdemo.data.RxNetCallBack;
import com.lin.netrequestdemo.data.api.ApiManager;
import com.lin.netrequestdemo.data.entity.CatBean;
import com.lin.netrequestdemo.ui.BaseActivity;

import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isHaveNet()) {
            showLoading();
            Map<String, String> map = new ArrayMap<>();
            map.put("action", "pricetrend");
            addSubscription(RxNet.request(ApiManager.getInstance().getCat(map), new RxNetCallBack<List<CatBean>>() {
                @Override
                public void onSuccess(List<CatBean> data) {
                    hideLoading();
                    showToast("获取列表成功" + data.get(0).toString());
                }

                @Override
                public void onFailure(String msg) {
                    hideLoading();
                    showToast(msg);
                }
            }));
        }
    }

}
