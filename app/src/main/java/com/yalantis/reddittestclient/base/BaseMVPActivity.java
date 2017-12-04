package com.yalantis.reddittestclient.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.yalantis.reddittestclient.R;

/**
 * Created by Ameron on 02.12.2017.
 */

public abstract class BaseMVPActivity<T extends BaseMvpPresenter> extends AppCompatActivity implements BaseMVPView {

    protected T basePresenter;

    protected View rootView;
    protected ContentLoadingProgressBar contentLoadingProgressBar;

    protected abstract int getLayoutResId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        rootView = findViewById(android.R.id.content);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showErrorMessage(int errorResId) {
        showErrorMessage(getString(errorResId));
    }

    @Override
    public void showErrorMessage(String errorString) {
        if (rootView != null) {
            final Snackbar snackbar;
            snackbar = Snackbar.make(rootView, errorString, Snackbar.LENGTH_INDEFINITE);
            snackbar.setActionTextColor(Color.RED)
                    .setAction(R.string.err_action_text, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackbar.dismiss();
                        }
                    });
            snackbar.show();
        }
    }

    @Override
    public void showInfoMessage(int infoResId) {
        showInfoMessage(getString(infoResId));
    }

    @Override
    public void showInfoMessage(String infoString) {
        if (rootView != null) {
            Snackbar.make(rootView, infoString, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showProgressBar() {
        if (contentLoadingProgressBar != null && !contentLoadingProgressBar.isShown()) {
//            contentLoadingProgressBar.show();
            contentLoadingProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (contentLoadingProgressBar != null && contentLoadingProgressBar.isShown()) {
            contentLoadingProgressBar.hide();
        }
    }
}
