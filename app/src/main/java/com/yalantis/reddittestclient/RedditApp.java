package com.yalantis.reddittestclient;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by ak on 02.12.2017.
 */

public class RedditApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }

}
