package com.yalantis.reddittestclient.base;

/**
 * Created by Ameron on 02.12.2017.
 */

public interface BaseMvpPresenter<V extends BaseMVPView> {

    void attachView(V view);

    void detachView();
}
