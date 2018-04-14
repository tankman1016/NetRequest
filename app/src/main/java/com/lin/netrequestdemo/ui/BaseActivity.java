package com.lin.netrequestdemo.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.lin.netrequestdemo.AppInit;
import com.lin.netrequestdemo.util.NetWorkUtils;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


public class BaseActivity extends AppCompatActivity implements BaseView {

    private ProgressDialog mProgressDialog;
    /**
     * 处理RxJava订阅和取消订阅
     */
    private CompositeDisposable mCompositeDisposable;

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

    public void addCompositeDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();
        }
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
    public Context getContext() {
        return BaseActivity.this;
    }

}
