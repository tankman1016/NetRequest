package com.lin.netrequestdemo.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lin.netrequestdemo.data.RxDisposeManager;

import io.reactivex.disposables.Disposable;


@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity implements BaseView {

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

    }

    //添加当前类名(lin.frameapp.xxx)的dispose
    public void addDispose(Disposable disposable) {
        RxDisposeManager.get().add(getClass().getName(), disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除dispose
        RxDisposeManager.get().cancel(getClass().getName());
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
