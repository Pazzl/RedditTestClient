package com.yalantis.reddittestclient.flow.link;

import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;

import com.yalantis.reddittestclient.R;
import com.yalantis.reddittestclient.base.BaseMvpPresenterImpl;
import com.yalantis.reddittestclient.data.Link;
import com.yalantis.reddittestclient.data.source.link.LinksRepository;

import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * Created by ak on 03.12.2017.
 */

public class LinkPresenter extends BaseMvpPresenterImpl<LinkContract.View> implements LinkContract.Presenter {

    //time-to-live cached data
    private static final long CACHE_VALID = 10 * 60 * 1000L;

    private LinksRepository linksRepository;
    private boolean isFetchingInProgress = false;

    @Override
    public void attachView(LinkContract.View view) {
        super.attachView(view);
        linksRepository = new LinksRepository(view.getContext());
    }

    @Override
    public void initLinks() {
        long curTime = System.currentTimeMillis();
        boolean loadLocalLinks = curTime - preferencesManager.getUpdateTime() <= CACHE_VALID;
        loadLinks(loadLocalLinks);
    }

    @Override
    public void detachView() {
        linksRepository.clear();
        super.detachView();
    }

    @Override
    public void loadLinks(final boolean loadLocalLinks) {
        if (!isFetchingInProgress) {
            isFetchingInProgress = true;
            view.showProgressBar();
            addDisposable(linksRepository.getLinks(loadLocalLinks)
                    .subscribe(new Consumer<List<Link>>() {
                        @Override
                        public void accept(List<Link> links) throws Exception {
                            view.hideProgressBar();
                            if (links.size() > 0) {
                                view.showLinks(links, true);
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            isFetchingInProgress = false;
                            view.hideProgressBar();
                            throwable.printStackTrace();
                            view.showErrorMessage(throwable.getMessage());
                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {
                            isFetchingInProgress = false;
                        }
                    }));
        }
    }

    @Override
    public void loadNextLinks() {
        if (!isFetchingInProgress && !linksRepository.isFetchedMaxLinks()) {
            isFetchingInProgress = true;
            view.showPaginationProgress();
            addDisposable(linksRepository.getLinks(false)
                    .subscribe(new Consumer<List<Link>>() {
                        @Override
                        public void accept(List<Link> links) throws Exception {
                            view.hidePaginationProgress();
                            view.showLinks(links, false);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            isFetchingInProgress = false;
                            view.hidePaginationProgress();
                            throwable.printStackTrace();
                            view.showErrorMessage(throwable.getMessage());
                        }
                    }, new Action() {
                        @Override
                        public void run() throws Exception {
                            isFetchingInProgress = false;
                        }
                    }));
        }
    }

    @Override
    public void refreshLinks() {
        if (!isFetchingInProgress) {
            view.showProgressBar();
            addDisposable(linksRepository.refreshLinks()
                    .subscribe(new Consumer<List<Link>>() {
                        @Override
                        public void accept(List<Link> links) throws Exception {
                            isFetchingInProgress = false;
                            view.hideProgressBar();
                            view.showLinks(links, true);
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            isFetchingInProgress = false;
                            view.hideProgressBar();
                            throwable.printStackTrace();
                            view.showErrorMessage(throwable.getMessage());
                        }
                    }));
        }
    }

    @Override
    public void onLinkClicked(Link link) {
        String url = link.getUrl();
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setToolbarColor(ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark));
        builder.setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.launchUrl(view.getContext(), Uri.parse(url));
    }
}
