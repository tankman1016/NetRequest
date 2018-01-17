package com.lin.netrequestdemo.ui.aty;

import android.os.Bundle;
import android.support.v4.util.ArrayMap;

import com.lin.netrequestdemo.R;
import com.lin.netrequestdemo.data.entity.CatResult;
import com.lin.netrequestdemo.presenter.MainPresenter;
import com.lin.netrequestdemo.ui.BaseActivity;
import com.lin.netrequestdemo.ui.view.MainView;

import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity implements MainView {
    MainPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = new MainPresenter();
        presenter.attachView(this);
        Map<String, String> map = new ArrayMap<>();
        map.put("action", "pricetrend");
        presenter.getCatByLinNet(map);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();
        presenter.detachView();
    }

    @Override
    public void getNewsSuccess() {

    }

    @Override
    public void getCatsSuccess(List<CatResult.DataBean> dataList) {
        //在这里处理list 不用管怎么获取的 —_— mvp

    }
}
