package com.yalantis.reddittestclient.api.services;

import android.support.annotation.Nullable;

import com.yalantis.reddittestclient.api.ApiSettings;
import com.yalantis.reddittestclient.api.model.Listing;
import com.yalantis.reddittestclient.api.model.Thing;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ak on 01.12.17.
 */

public interface RedditService {

    @GET(ApiSettings.PATH_TOP)
    Single<Thing<Listing>> getRedditTop(@Nullable @Query("after") String after, @Nullable @Query("limit") int limit);
}
