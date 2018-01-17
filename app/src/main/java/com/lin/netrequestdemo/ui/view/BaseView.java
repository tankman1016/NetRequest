package com.lin.netrequestdemo.ui.view;

import android.content.Context;

/**
 * 通用的一些东西
 */

public interface BaseView {
    /**
     * 显示正在加载view
     */
    void showLoading();
    /**
     * 显示正在加载view
     */
    void showLoading(String msg);
    /**
     * 关闭正在加载view
     */
    void hideLoading();
    /**
     * 显示提示
     * @param msg
     */
    void showToast(String msg);
    /**
     * 显示请求错误提示
     */
    void showErr();
    /**
     * 获取上下文
     * @return 上下文
     */
    Context getContext();
}
