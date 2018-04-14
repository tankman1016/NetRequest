package com.lin.netrequestdemo.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.lin.netrequestdemo.AppInit;
import com.lin.netrequestdemo.util.NetWorkUtils;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class BaseActivity extends AppCompatActivity implements BaseView {

    private ProgressDialog mProgressDialog;
    /**
     * 处理RxJava订阅和取消订阅
     */
    public CompositeSubscription mCompositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

    }

    public boolean isHaveNet() {

        if (!NetWorkUtils.isConnected(AppInit.getContextObject())) {
            showToast("网络未连接");
            return false;
        }

        if (!NetWorkUtils.isAvailable(AppInit.getContextObject())) {
            showToast("网络不可用");
            return false;
        }
        return true;
    }

    /**
     * 添加订阅
     */
    public void addSubscription(Subscription subscription) {
        Log.v("Lin2", "添加订阅");
        if (mCompositeSubscription == null) {
            mCompositeSubscription = new CompositeSubscription();
        }
        mCompositeSubscription.add(subscription);
    }

    /**
     * 取消订阅
     */
    private void unsubscribe() {
        Log.v("Lin2", "取消订阅");
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unsubscribe();
    }

    @Override
    public void showLoading() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage("数据加载中");
            mProgressDialog.show();
        }
    }

    @Override
    public void showLoading(String msg) {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.setMessage(msg);
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErr() {
        Toast.makeText(this, "出错了..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getContext() {
        return BaseActivity.this;
    }

}
