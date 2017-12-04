package com.yalantis.reddittestclient.base;

import android.support.annotation.NonNull;

import com.yalantis.reddittestclient.manager.SharedPreferencesManager;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by ak on 02.12.2017.
 */

public abstract class BaseMvpPresenterImpl<V extends BaseMVPView> implements BaseMvpPresenter<V> {

    protected V view;
    protected SharedPreferencesManager preferencesManager;
    private CompositeDisposable disposableList = new CompositeDisposable();

    @Override
    public void attachView(V view) {
        this.view = view;
        preferencesManager = SharedPreferencesManager.getInstance(view.getContext().getApplicationContext());
    }

    protected void addDisposable(@NonNull Disposable disposable) {
        disposableList.add(disposable);
    }

    @Override
    public void detachView() {
        disposableList.clear();
        preferencesManager.setUpdateTime(System.currentTimeMillis());
        this.view = null;
    }
}
