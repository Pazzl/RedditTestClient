package com.yalantis.reddittestclient.data.source.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.yalantis.reddittestclient.BuildConfig;
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

public class RepositoryLocalDataSource extends BaseLocalDataSource implements RepositoryDataSource {

    @Override
    public Single<List<Link>> getSingleLinks(@Nullable String after) {
        if (BuildConfig.DEBUG) {
            Log.d("debug", "get saved links ");
        }

//        return getCurrentRealm().where(Link.class)
//                .findAllSortedAsync(LinkFields.RATING, Sort.DESCENDING)
//                .asFlowable()
//                .flatMap(new Function<RealmResults<Link>, Publisher<Link>>() {
//                    @Override
//                    public Publisher<Link> apply(RealmResults<Link> links) throws Exception {
//                        return Flowable.fromIterable(links.size() > 0 ? links : new ArrayList<Link>());
//                    }
//                })
//                .toObservable()
//                .toList();

//        return getCurrentRealm().where(Link.class)
//                .findAllSortedAsync(LinkFields.RATING, Sort.DESCENDING)
//                .asFlowable()
//                .filter(new Predicate<RealmResults<Link>>() {
//                    @Override
//                    public boolean test(RealmResults<Link> links) throws Exception {
//                        Log.d("debug", "links size: " + links.size());
//                        return links.isLoaded();
//                    }
//                })
//                .toObservable()
//                .flatMap(new Function<RealmResults<Link>, ObservableSource<Link>>() {
//                    @Override
//                    public ObservableSource<Link> apply(RealmResults<Link> links) throws Exception {
//                        //Get List<Link> and convert to Observable
//                        return Observable.fromIterable(links.subList(0, links.size()));
//                    }
//                })
//                .toList();

//        return getCurrentRealm().where(Link.class)
//                .findAllSortedAsync(LinkFields.RATING, Sort.DESCENDING)
//                .get(0)
//                .<Link>asFlowable()
//                .toList();

        return Single.just((List<Link>) getCurrentRealm().where(Link.class).findAllSorted(LinkFields.RATING, Sort.DESCENDING));

    }

    @Override
    public Observable<List<Link>> getObservableLinks(@Nullable String after) {
        return getCurrentRealm().where(Link.class)
//                .findAllSortedAsync(LinkFields.RATING, Sort.DESCENDING)
                .findAllAsync()
                .asFlowable()
                .filter(new Predicate<RealmResults<Link>>() {
                    @Override
                    public boolean test(RealmResults<Link> links) throws Exception {
                        if (BuildConfig.DEBUG) {
                            Log.d("debug", "links size: " + links.size());
                        }
                        return links.isLoaded();
                    }
                })
                .toObservable()
                .flatMap(new Function<RealmResults<Link>, ObservableSource<List<Link>>>() {
                    @Override
                    public ObservableSource<List<Link>> apply(RealmResults<Link> links) throws Exception {
                        return Observable.just(links.subList(0, links.size() > 0 ? links.size() - 1 : 0));
                    }
                });

    }

    @Override
    public void saveLinks(final List<Link> links) {
        if (BuildConfig.DEBUG) {
            Log.d("debug", "save links ");
        }
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
        if (BuildConfig.DEBUG) {
            Log.d("debug", "clear links ");
        }
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
