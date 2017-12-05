package com.yalantis.reddittestclient.data.source.link;

import android.support.annotation.Nullable;

import com.yalantis.reddittestclient.data.Link;

import java.util.List;

import io.reactivex.Single;

/**
 * Created by ak on 01.12.17.
 */

interface LinkDataSource {

    Single<List<Link>> getLinks(@Nullable String after);

    void saveLinks(List<Link> links);

    void clearLinks();

    boolean isEmpty();
}
