package com.lin.netrequestdemo.util;

import android.util.Log;

public class AppLogUtil {

    private static boolean Is_Test_Log = true;

    private AppLogUtil() {

    }

    public static void printV(String msg) {
        if (Is_Test_Log) {
            Log.v("Lin2", msg);
        }
    }

    public static void printV(String tag, String msg) {
        if (Is_Test_Log) {
            Log.v(tag, msg);
        }
    }

    public static void printE(String msg) {
        if (Is_Test_Log) {
            Log.e("Lin2", msg);
        }
    }

    public static void printE(String tag, String msg) {
        if (Is_Test_Log) {
            Log.e(tag, msg);
        }
    }
}
