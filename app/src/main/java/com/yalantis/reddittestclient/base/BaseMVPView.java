package com.yalantis.reddittestclient.base;

import android.content.Context;
import android.support.annotation.StringRes;

/**
 * Created by ak on 02.12.2017.
 */

public interface BaseMVPView {

    Context getContext();

    void showProgressBar();

    void hideProgressBar();

    void showErrorMessage(String errorString);

    void showErrorMessage(@StringRes int errorResId);

    void showInfoMessage(String infoString);

    void showInfoMessage(@StringRes int infoResId);
}
