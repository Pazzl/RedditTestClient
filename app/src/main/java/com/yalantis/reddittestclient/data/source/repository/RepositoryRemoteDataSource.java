package com.yalantis.reddittestclient.data.source.repository;

import android.support.annotation.Nullable;

import com.yalantis.reddittestclient.api.model.Listing;
import com.yalantis.reddittestclient.api.model.Thing;
import com.yalantis.reddittestclient.data.Link;
import com.yalantis.reddittestclient.data.source.base.BaseRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by ak on 02.12.2017.
 */

public class RepositoryRemoteDataSource extends BaseRemoteDataSource implements RepositoryDataSource {

    private static String listingAfter;

    @Override
    public Single<List<Link>> getSingleLinks(@Nullable String after) {
        return redditService.getRedditTop(after)
                .flatMap(new Function<Thing<Listing>, SingleSource<List<Link>>>() {
                    @Override
                    public SingleSource<List<Link>> apply(Thing<Listing> listingThing) throws Exception {
                        List<Link> links = new ArrayList<>();
                        Listing listing = listingThing.getData();
                        listingAfter = listing.getAfter();
                        for (Thing<Link> linkThing : listing.getChildrens()) {
                            links.add(linkThing.getData());
                        }
                        return Single.just(links);
                    }
                })
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<List<Link>> getObservableLinks(@Nullable String after) {
        return null;
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

    String getAfter() {
        return listingAfter;
    }

    void setAfter(String listingAfter) {
        RepositoryRemoteDataSource.listingAfter = listingAfter;
    }
}
