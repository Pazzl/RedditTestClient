package com.yalantis.reddittestclient;

import android.app.Application;

import com.yalantis.reddittestclient.data.Migration;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ak on 02.12.2017.
 */

public class RedditApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .schemaVersion(Migration.CURRENT_REALM_VER)
                .migration(new Migration())
                .build();
        Realm.setDefaultConfiguration(realmConfig);
    }


}
