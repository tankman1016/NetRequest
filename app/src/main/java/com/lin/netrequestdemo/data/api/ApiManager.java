package com.lin.netrequestdemo.data.api;

import com.lin.netrequestdemo.data.AppConstants;
import com.lin.netrequestdemo.util.AppLogUtil;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiManager {

    private Retrofit client;

    private ApiManager() {
        client = new Retrofit.Builder()
                .baseUrl(AppConstants.Base_Url_Test)
                .client(initClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static volatile AppApi INSTANCE;

    public static AppApi getInstance() {
        if (INSTANCE == null) {
            synchronized (ApiManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new ApiManager().getAppApi();
                }
            }
        }
        return INSTANCE;
    }

    private AppApi getAppApi() {
        return client.create(AppApi.class);
    }

    private static OkHttpClient initClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //声明日志类
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                AppLogUtil.printE(message);
            }
        });
        //设定日志级别
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //延时
        builder.addInterceptor(httpLoggingInterceptor)
                //设置参数加密
                //.addInterceptor(new EncryptParamInterceptor())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS);
        return builder.build();
    }

}
