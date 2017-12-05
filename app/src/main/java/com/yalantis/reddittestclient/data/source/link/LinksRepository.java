package com.yalantis.reddittestclient.data.source.link;

import android.content.Context;

import com.yalantis.reddittestclient.data.Link;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by ak on 02.12.2017.
 */

public class LinksRepository {

    private LinkLocalDataSource localDataSource;
    private LinkRemoteDataSource remoteDataSource;

    public LinksRepository(Context context) {

        localDataSource = new LinkLocalDataSource();
        remoteDataSource = new LinkRemoteDataSource();

        localDataSource.init(context);
        remoteDataSource.init(context);
    }

    public Observable<List<Link>> getLinks(boolean loadFromLocalRepository) {
        if (!localDataSource.isEmpty() && loadFromLocalRepository) {
            return localDataSource.getLinks(null).concatWith(getRemoteLinks(null)).toObservable();
        } else {
            return getRemoteLinks(remoteDataSource.getAfter()).toObservable();
        }
    }

    public Single<List<Link>> refreshLinks() {
        remoteDataSource.setAfter(null);
        return getRemoteLinks(null);
    }

    private Single<List<Link>> getRemoteLinks(final String after) {
        return remoteDataSource.getLinks(after)
                .observeOn(AndroidSchedulers.mainThread())
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

    private void clearLinks() {
        localDataSource.clearLinks();
    }

    public void clear() {
        localDataSource.closeCurrentRealm();
    }
}
