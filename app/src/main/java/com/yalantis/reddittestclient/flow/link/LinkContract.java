package com.yalantis.reddittestclient.flow.link;

import com.yalantis.reddittestclient.base.BaseMVPView;
import com.yalantis.reddittestclient.base.BaseMvpPresenter;
import com.yalantis.reddittestclient.data.Link;

import java.util.List;

/**
 * Created by ak on 03.12.2017.
 */

class LinkContract {

    interface View extends BaseMVPView {

        void showLinks(List<Link> links, boolean clearPrev);

        void showPaginationProgress();

        void hidePaginationProgress();

    }

    interface Presenter extends BaseMvpPresenter<View> {

        void initLinks();

        void loadLinks(boolean loadLocalLinks);

        void loadNextLinks();

        void refreshLinks();

        void onLinkClicked(Link link);

    }
}
