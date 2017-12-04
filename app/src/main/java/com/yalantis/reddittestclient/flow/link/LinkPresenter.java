package com.yalantis.reddittestclient.flow.link;

import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.yalantis.reddittestclient.BuildConfig;
import com.yalantis.reddittestclient.R;
import com.yalantis.reddittestclient.base.BaseMvpPresenterImpl;
import com.yalantis.reddittestclient.data.Link;
import com.yalantis.reddittestclient.data.source.repository.LinksRepository;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Ameron on 03.12.2017.
 */

public class LinkPresenter extends BaseMvpPresenterImpl<LinkContract.View> implements LinkContract.Presenter {

    private static final long CACHE_VALID = 10 * 60 * 1000L;

    private LinksRepository linksRepository;
    private String after;
    private boolean loadLocalLinks;

    @Override
    public void attachView(LinkContract.View view) {
        super.attachView(view);
        linksRepository = new LinksRepository(view.getContext());
    }

    @Override
    public void initLinks() {
        long curTime = System.currentTimeMillis();
        loadLocalLinks = curTime - preferencesManager.getUpdateTime() <= CACHE_VALID;
        if (BuildConfig.DEBUG) {
            Log.d("debug", "initLinks with local -> " + loadLocalLinks);
        }

        loadLinks(loadLocalLinks);
    }

    @Override
    public void loadLinks(final boolean loadLocalLinks) {
        view.showProgressBar();
        addDisposable(linksRepository.getLinks(loadLocalLinks)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Link>>() {
                    @Override
                    public void accept(List<Link> links) throws Exception {
                        view.hideProgressBar();
                        view.showLinks(links, true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.hideProgressBar();
                        throwable.printStackTrace();
                        view.showErrorMessage(throwable.toString());
                    }
                }));
    }

    @Override
    public void loadNextLinks() {
//        view.showProgressBar();
        addDisposable(linksRepository.getLinks(false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Link>>() {
                    @Override
                    public void accept(List<Link> links) throws Exception {
//                        view.hideProgressBar();
                        view.showLinks(links, false);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
//                        view.hideProgressBar();
                        throwable.printStackTrace();
                        view.showErrorMessage(throwable.toString());
                    }
                }));
    }

    @Override
    public void refreshLinks() {
        view.showProgressBar();
        addDisposable(linksRepository.refreshLinks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Link>>() {
                    @Override
                    public void accept(List<Link> links) throws Exception {
                        view.hideProgressBar();
                        view.showLinks(links, true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        view.hideProgressBar();
                        throwable.printStackTrace();
                        view.showErrorMessage(throwable.toString());
                    }
                }));
    }

    @Override
    public void onLinkClicked(Link link) {
        String url = link.getUrl();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(view.getContext(), Uri.parse(url));
    }
}
