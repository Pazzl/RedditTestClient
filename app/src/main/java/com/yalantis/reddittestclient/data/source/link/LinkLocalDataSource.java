package com.yalantis.reddittestclient.data.source.link;

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
import io.reactivex.functions.Predicate;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by ak on 01.12.17.
 */

public class LinkLocalDataSource extends BaseLocalDataSource implements LinkDataSource {

    @Override
    public Single<List<Link>> getLinks(@Nullable String after) {
        return getCurrentRealm().where(Link.class)
                .findAllSortedAsync(LinkFields.RATING, Sort.DESCENDING)
                .asFlowable()
                .toObservable()
                // filter only loaded results
                .filter(new Predicate<RealmResults<Link>>() {
                    @Override
                    public boolean test(RealmResults<Link> links) throws Exception {
                        return links.isLoaded();
                    }
                })
                //we need only one portion of data
                .take(1)
                .switchMap(new Function<RealmResults<Link>, ObservableSource<Link>>() {
                    @Override
                    public ObservableSource<Link> apply(RealmResults<Link> links) throws Exception {
                        return Observable.fromIterable(links).take(links.size());
                    }
                })
                .toList();
    }

    @Override
    public void saveLinks(final List<Link> links) {
        getCurrentRealm().executeTransactionAsync(
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
        return getCurrentRealm().where(Link.class).count() == 0;
    }
}
