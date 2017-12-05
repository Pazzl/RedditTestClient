package com.yalantis.reddittestclient.data.source.base;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by ak on 01.12.17.
 */

public class BaseLocalDataSource implements BaseDataSource {

    private static final ThreadLocal<Realm> REALM_THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public void init(Context context) {

    }

    @Override
    public void destroy() {

    }

    protected Realm getCurrentRealm() {
        Realm realm = REALM_THREAD_LOCAL.get();
        if (realm == null || realm.isClosed()) {
            try {
                realm = Realm.getDefaultInstance();
            }
            catch (RealmMigrationNeededException exception) {
                Realm.deleteRealm(new RealmConfiguration.Builder().build());
                realm = Realm.getDefaultInstance();
            }
            REALM_THREAD_LOCAL.set(realm);
        }
        return realm;
    }

    public void closeCurrentRealm() {
        Realm realm = REALM_THREAD_LOCAL.get();
        if (realm != null && !realm.isClosed() && !realm.isInTransaction()) {
            realm.close();
        }
        REALM_THREAD_LOCAL.set(null);
    }

}
