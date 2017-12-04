package com.yalantis.reddittestclient.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by ak on 02.12.2017.
 */

public class SharedPreferencesManager {

    private static final String SP_NAME = "shPrefs";
    private static final String UPDATE_TIME = "update_time";

    private static SharedPreferencesManager _self;
    private SharedPreferences sharedPreferences;

    private SharedPreferencesManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static SharedPreferencesManager getInstance(Context context) {

        if (_self == null) {
            _self = new SharedPreferencesManager(context);
        }

        return _self;
    }

    public long getUpdateTime() {
        return sharedPreferences.getLong(UPDATE_TIME, 0);
    }

    public void setUpdateTime(long updateTime) {
        sharedPreferences.edit().putLong(UPDATE_TIME, updateTime).apply();
    }
}
