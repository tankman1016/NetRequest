package com.lin.netrequestdemo.data.entity;

/**
 * 分类
 */

public class CatBean {

    private int catid;
    private String catname;

    public int getCatid() {
        return catid;
    }

    public String getCatname() {
        return catname;
    }

    @Override
    public String toString() {
        return catid + catname;
    }
}
