package com.yalantis.reddittestclient.flow.link;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.yalantis.reddittestclient.R;
import com.yalantis.reddittestclient.base.BaseMVPActivity;
import com.yalantis.reddittestclient.data.Link;

import java.util.List;

/**
 * Created by ak on 03.12.2017.
 */

public class LinkActivity extends BaseMVPActivity implements LinkContract.View {

    private static final int REDDIT_FETCH_LIMIT = 25;
    private static final int REDDIT_PAGINATION_MARGIN = 5;
    private static final String ARG_LOAD_LOCAL_LINKS = "arg_load_local";

    private LinkPresenter linkPresenter;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getApplication().onCreate();
        linkPresenter = new LinkPresenter();
        linkPresenter.attachView(this);

        contentLoadingProgressBar = findViewById(R.id.links_progressbar);
        recyclerView = findViewById(R.id.links_recyclerview);
        setupRecyclerView();
        refreshLayout = findViewById(R.id.links_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                linkPresenter.refreshLinks();
            }
        });

        if (savedInstanceState != null && savedInstanceState.getBoolean(ARG_LOAD_LOCAL_LINKS)) {
            //handle screen orientation change. load from Realm first
            linkPresenter.loadLinks(true);
        } else {
            linkPresenter.initLinks();
        }
    }

    @Override
    public void showLinks(List<Link> links, boolean clearPrev) {
        LinkAdapter adapter = (LinkAdapter) recyclerView.getAdapter();
        adapter.setLinks(links, clearPrev);
    }

    @Override
    public void showPaginationProgress() {
        ((LinkAdapter) recyclerView.getAdapter()).enableProgress(true);
    }

    @Override
    public void hidePaginationProgress() {
        ((LinkAdapter) recyclerView.getAdapter()).enableProgress(false);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_links;
    }

    @Override
    public void showProgressBar() {
        if (!refreshLayout.isRefreshing()) {
            super.showProgressBar();
            recyclerView.setEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        linkPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public void hideProgressBar() {
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        } else {
            super.hideProgressBar();
            recyclerView.setEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //handle screen orientation change
        outState.putBoolean(ARG_LOAD_LOCAL_LINKS, true);
        super.onSaveInstanceState(outState);
    }

    private void setupRecyclerView() {
        LinkAdapter adapter = new LinkAdapter(new LinkAdapter.LinkClickListener() {
            @Override
            public void onLinkClick(Link link) {
                linkPresenter.onLinkClicked(link);
            }
        });

        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                if (wm != null) {
                    Display display = wm.getDefaultDisplay();
                    DisplayMetrics metrics = new DisplayMetrics();
                    display.getMetrics(metrics);
                    return metrics.heightPixels;
                }
                return super.getExtraLayoutSpace(state);
            }
        };
        layoutManager.setInitialPrefetchItemCount(REDDIT_FETCH_LIMIT);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(REDDIT_FETCH_LIMIT);
        recyclerView.setDrawingCacheEnabled(true);
        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleListCount = recyclerView.getLayoutManager().getChildCount();
                int totalListCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleListPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (!refreshLayout.isRefreshing()) {
                    if (visibleListCount + firstVisibleListPosition >= totalListCount - REDDIT_PAGINATION_MARGIN
                            && firstVisibleListPosition >= 0 && totalListCount >= REDDIT_FETCH_LIMIT) {
                        linkPresenter.loadNextLinks();
                    }
                }
            }
        });
    }
}
