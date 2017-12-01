package com.yalantis.reddittestclient.data.source.base;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yalantis.reddittestclient.BuildConfig;
import com.yalantis.reddittestclient.api.ApiSettings;
import com.yalantis.reddittestclient.api.services.RedditService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ak on 01.12.17.
 */

public class BaseRemoteDataSource implements BaseDataSource {

    private static final int CONNECT_TIMEOUT = 10;
    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 30;

    private Retrofit retrofit;
    protected RedditService redditService;

    @Override
    public void init(Context context) {
        initRetorfit();
        initRedditService();
    }

    @Override
    public void destroy() {

    }

    private void initRetorfit() {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);

        if (BuildConfig.DEBUG) {
            okHttpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiSettings.BASE_URL)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private void initRedditService() {
        redditService = retrofit.create(RedditService.class);
    }
}
