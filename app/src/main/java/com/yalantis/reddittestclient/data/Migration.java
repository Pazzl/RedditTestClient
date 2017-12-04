package com.yalantis.reddittestclient.data;

import io.realm.DynamicRealm;
import io.realm.RealmMigration;

/**
 * Created by ak on 04.12.17.
 */

public class Migration implements RealmMigration {

    public static final long CURRENT_REALM_VER = 0;

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {

    }
}
