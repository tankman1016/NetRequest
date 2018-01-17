package com.lin.netrequestdemo.data;


public interface RxNetCallBack<T> {
    /**
     * 数据请求成功
     *
     * @param data 请求到的数据
     */
    void onSuccess(T data);

    /**
     * 数据请求失败
     */
    void onFailure(String msg);
}
