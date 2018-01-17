package com.lin.netrequestdemo.data;


public class BaseResponse<T> {

    private String code;
    private String msg;
    private T data;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
