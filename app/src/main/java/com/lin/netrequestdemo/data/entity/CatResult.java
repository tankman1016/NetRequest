package com.lin.netrequestdemo.data.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/1/13.
 */

public class CatResult {


    /**
     * code : 200
     * msg : 价格趋势分类
     * data : [{"catid":244,"catname":"猪价"},{"catid":247,"catname":"牛价"},{"catid":250,"catname":"羊价"},{"catid":253,"catname":"鸡价"},{"catid":256,"catname":"鸭价"}]
     */

    private String code;
    private String msg;
    private List<DataBean> data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * catid : 244
         * catname : 猪价
         */

        private int catid;
        private String catname;

        public int getCatid() {
            return catid;
        }

        public void setCatid(int catid) {
            this.catid = catid;
        }

        public String getCatname() {
            return catname;
        }

        public void setCatname(String catname) {
            this.catname = catname;
        }

    }
}
