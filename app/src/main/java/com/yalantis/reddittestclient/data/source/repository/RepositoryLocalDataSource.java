package com.yalantis.reddittestclient.data.source.repository;

import android.support.annotation.Nullable;

import com.yalantis.reddittestclient.data.Link;
import com.yalantis.reddittestclient.data.LinkFields;
import com.yalantis.reddittestclient.data.source.base.BaseLocalDataSource;

import java.util.List;

import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.RealmResults;
import io.realm.Sort;
import io.realm.rx.CollectionChange;

/**
 * Created by ak on 01.12.17.
 */

public class RepositoryLocalDataSource extends BaseLocalDataSource implements RepositoryDataSource {

    @Override
    public Single<List<Link>> getLinks(@Nullable String after) {
        return getCurrentRealm().where(Link.class).findAllSorted(LinkFields.RATING, Sort.DESCENDING)
                .asChangesetObservable()
                .observeOn(Schedulers.io())
                .flatMap(new Function<CollectionChange<RealmResults<Link>>, ObservableSource<Link>>() {
                    @Override
                    public ObservableSource<Link> apply(CollectionChange<RealmResults<Link>> realmResults) throws Exception {

                        return null;
                    }
                }).toSortedList();
    }

    @Override
    public void saveLinks(List<Link> links) {

    }

    @Override
    public void clearLinks() {

    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
