package com.lin.netrequestdemo.presenter;

import android.content.Context;
import android.util.Log;

import com.lin.netrequestdemo.AppInit;
import com.lin.netrequestdemo.ui.view.BaseView;
import com.lin.netrequestdemo.util.NetWorkUtils;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Presenter中可共用的代码就是对View引用的方法了，值得注意的是，
 * 上面已经定义好了BaseView，
 * 所以我们希望Presenter中持有的View都是BaseView的子类，这里同样需要泛型来约束：
 * 链接view 这里采用弱引用 防止特殊情况下 aty 销魂 没有执行 onDestroy的情况
 */

public class BasePresenter<V extends BaseView> {
    protected Reference<V> mViewRef;
    /**
     * 处理RxJava订阅和取消订阅
     */
    public CompositeSubscription mCompositeSubscription;

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
    public void unsubscribe() {
        Log.v("Lin2", "取消订阅");
        if (mCompositeSubscription != null) {
            mCompositeSubscription.unsubscribe();
        }
    }

    /**
     * 绑定view，一般在初始化中调用该方法
     */
    public void attachView(V mvpView) {
        mViewRef = new WeakReference<V>(mvpView);
    }

    /**
     * 断开view，一般在onDestroy中调用
     */
    public void detachView() {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }

    /**
     * 是否与View建立连接
     * 每次调用业务请求的时候都要出先调用方法检查是否与View建立连接
     */
    public boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    /**
     * 获取连接的view
     */
    public V getView() {
        return mViewRef.get();
    }

    /**
     * 处理请求数据失败
     */
    public void handleFailed(String msg, boolean isShowErrorMsg) {
        if (isViewAttached()) {
            getView().hideLoading();
            if (isShowErrorMsg) {
                getView().showToast(msg);
            }
        }
    }

    public boolean isHaveNet() {

        if (!NetWorkUtils.isConnected(AppInit.getContextObject())) {
            getView().showToast("网络未连接");
            return false;
        }

        if (!NetWorkUtils.isAvailable(AppInit.getContextObject())) {
            getView().showToast("网络不可用");
            return false;
        }

        return true;
    }

}