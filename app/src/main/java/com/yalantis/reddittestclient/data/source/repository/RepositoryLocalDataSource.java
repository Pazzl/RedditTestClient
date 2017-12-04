package com.yalantis.reddittestclient.data.source.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.yalantis.reddittestclient.data.Link;
import com.yalantis.reddittestclient.data.LinkFields;
import com.yalantis.reddittestclient.data.source.base.BaseLocalDataSource;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by ak on 01.12.17.
 */

public class RepositoryLocalDataSource extends BaseLocalDataSource implements RepositoryDataSource {

    @Override
    public Single<List<Link>> getLinks(@Nullable String after) {
        return getCurrentRealm().where(Link.class)
                .findAllSortedAsync(LinkFields.RATING, Sort.DESCENDING)
                .asFlowable()
                .toObservable()
                .flatMap(new Function<RealmResults<Link>, ObservableSource<Link>>() {
                    @Override
                    public ObservableSource<Link> apply(RealmResults<Link> links) throws Exception {
                        //Get List<Link> and convert to Observable
                        return Observable.fromIterable(links.subList(0, links.size()));
                    }
                })
                .toList();
    }

    @Override
    public void saveLinks(final List<Link> links) {
        getCurrentRealm().executeTransaction(
                new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        realm.copyToRealmOrUpdate(links);
                    }
                });
    }

    @Override
    public void clearLinks() {
        getCurrentRealm().executeTransactionAsync(
                new Realm.Transaction() {
                    @Override
                    public void execute(@NonNull Realm realm) {
                        realm.delete(Link.class);
                    }
                }
        );
    }

    @Override
    public boolean isEmpty() {
        return getCurrentRealm().where(Link.class).count() > 0;
    }
}
