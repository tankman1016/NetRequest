package com.lin.netrequestdemo.data.api;

import com.lin.netrequestdemo.data.BaseResponse;
import com.lin.netrequestdemo.data.entity.CatBean;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 *
 */

public interface MallApi {

    /**
     * 获取分类数据
     */
    @GET("global_api.php")
    Observable<BaseResponse<List<CatBean>>> getCat(@QueryMap() Map<String, String> map);

    /**
     * 获取新闻数据
     */
    @FormUrlEncoded
    @POST("article_api.php")
    Observable<String> getNews(@FieldMap Map<String, String> map);


}
