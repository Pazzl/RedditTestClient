package com.yalantis.reddittestclient.data.source.repository;

import android.content.Context;
import android.util.Log;

import com.yalantis.reddittestclient.BuildConfig;
import com.yalantis.reddittestclient.data.Link;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

/**
 * Created by ak on 02.12.2017.
 */

public class LinksRepository {

    private RepositoryLocalDataSource localDataSource;
    private RepositoryRemoteDataSource remoteDataSource;

    public LinksRepository(Context context) {

        localDataSource = new RepositoryLocalDataSource();
        remoteDataSource = new RepositoryRemoteDataSource();

        localDataSource.init(context);
        remoteDataSource.init(context);
    }

    public Observable<List<Link>> getLinks(boolean loadFromLocalRepository) {
        if (BuildConfig.DEBUG) {
            Log.d("debug", "localDS empty -> " + localDataSource.isEmpty());
            Log.d("debug", "load from local -> " + localDataSource.isEmpty());
            Log.d("debug", "after -> " + remoteDataSource.getAfter());
        }


        if (!localDataSource.isEmpty() && loadFromLocalRepository && remoteDataSource.getAfter() == null) {
            if (BuildConfig.DEBUG) {
                Log.d("debug", "loading from local storage");
            }
            return localDataSource.getLinks(null).concatWith(getRemoteLinks(null)).toObservable();
        } else {
            Log.d("debug", getClass().getName() + " after = " + remoteDataSource.getAfter());

            return getRemoteLinks(remoteDataSource.getAfter()).toObservable();
        }
    }

    public Single<List<Link>> refreshLinks() {
        remoteDataSource.setAfter(null);
        return getRemoteLinks(null);
    }

    private Single<List<Link>> getRemoteLinks(final String after) {
        Log.d("debug", getClass().getName() + " after = " + after);

        return remoteDataSource.getLinks(after)
                .doOnSuccess(new Consumer<List<Link>>() {
                    @Override
                    public void accept(List<Link> links) throws Exception {
                        if (after == null) {
                            clearLinks();
                        }
                        saveLinks(links);
                    }
                });
    }

    private void saveLinks(List<Link> links) {
        localDataSource.saveLinks(links);
    }

    public void clearLinks() {
        localDataSource.clearLinks();
    }
}
