package com.lin.netrequestdemo.ui.view;


import com.lin.netrequestdemo.data.entity.CatResult;

import java.util.List;

public interface MainView extends BaseView {
    void getNewsSuccess();

    void getCatsSuccess(List<CatResult.DataBean> dataList);
}
