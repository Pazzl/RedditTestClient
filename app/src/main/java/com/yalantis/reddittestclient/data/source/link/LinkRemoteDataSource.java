package com.yalantis.reddittestclient.data.source.link;

import android.support.annotation.Nullable;

import com.yalantis.reddittestclient.api.model.Listing;
import com.yalantis.reddittestclient.api.model.Thing;
import com.yalantis.reddittestclient.data.Link;
import com.yalantis.reddittestclient.data.source.base.BaseRemoteDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import static com.yalantis.reddittestclient.api.ApiSettings.LINKS_LIMIT;
import static com.yalantis.reddittestclient.api.ApiSettings.LINKS_MAX_TOP;

/**
 * Created by ak on 02.12.2017.
 */

public class LinkRemoteDataSource extends BaseRemoteDataSource implements LinkDataSource {

    //handle pagination
    //cause we fetched another data type from api, we have to handle it here
    private static String listingAfter;
    private static int listingLimit;
    private static int listingCount = 0;

    @Override
    public Single<List<Link>> getLinks(@Nullable String after) {
        if (after == null) {
            //we fetch in one request not greater then max
            listingLimit = LINKS_LIMIT > LINKS_MAX_TOP ? LINKS_MAX_TOP : LINKS_LIMIT;
            listingCount = 0;
        } else {
            listingLimit = LINKS_MAX_TOP - listingCount < listingLimit ? LINKS_MAX_TOP - listingCount : LINKS_LIMIT;
        }
        return redditService.getRedditTop(after, listingLimit)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<Thing<Listing>, SingleSource<List<Link>>>() {
                    @Override
                    public SingleSource<List<Link>> apply(Thing<Listing> listingThing) throws Exception {
                        List<Link> links = new ArrayList<>();
                        Listing listing = listingThing.getData();
                        listingAfter = listing.getAfter();
                        listingCount += listingLimit;
                        for (Thing<Link> linkThing : listing.getChildrens()) {
                            links.add(linkThing.getData());
                        }
                        return Single.just(links);
                    }
                });
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

    @Override
    public boolean isFetchedAllData() {
        return listingCount >= LINKS_MAX_TOP;
    }

    @Override
    public String getAfter() {
        return listingAfter;
    }

    @Override
    public void setAfter(String listingAfter) {
        LinkRemoteDataSource.listingAfter = listingAfter;
    }
}
