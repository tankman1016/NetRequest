package com.lin.netrequestdemo.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 需要 ACCESS_NETWORK_STATE 权限
 */
public class NetWorkUtils {

    /**
     * 打开网络设置界面
     */
    public static void openWirelessSettings(Context context) {
        context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
    }
    /**
     * 获取活动网络信息
     */
    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }
    /**
     * 判断网络是否可用
     */
    public static boolean isAvailable(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isAvailable();
    }
    /**
     * 判断网络是否连接
     */
    public static boolean isConnected(Context context) {
        NetworkInfo info = getActiveNetworkInfo(context);
        return info != null && info.isConnected();
    }
    /**
     * 判断wifi是否连接状态
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm != null && cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
    }
}
