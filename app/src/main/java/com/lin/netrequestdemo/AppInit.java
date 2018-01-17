package com.lin.netrequestdemo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;


public class AppInit extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContextObject() {
        return context;
    }
}
