package com.yalantis.reddittestclient.data.source.base;

import android.content.Context;

/**
 * Created by ak on 01.12.17.
 */

public interface BaseDataSource {

    void init(Context context);
    void destroy();
}
