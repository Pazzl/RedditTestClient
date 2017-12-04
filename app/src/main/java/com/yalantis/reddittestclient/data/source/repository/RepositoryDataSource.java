package com.yalantis.reddittestclient.data.source.repository;

import android.support.annotation.Nullable;

import com.yalantis.reddittestclient.data.Link;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by ak on 01.12.17.
 */

interface RepositoryDataSource {

    Single<List<Link>> getSingleLinks(@Nullable String after);

    Observable<List<Link>> getObservableLinks(@Nullable String after);

    void saveLinks(List<Link> links);

    void clearLinks();

    boolean isEmpty();
}
