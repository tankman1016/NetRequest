package com.lin.netrequestdemo.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.lin.netrequestdemo.ui.view.BaseView;


public class BaseActivity extends AppCompatActivity implements BaseView{
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

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
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
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
