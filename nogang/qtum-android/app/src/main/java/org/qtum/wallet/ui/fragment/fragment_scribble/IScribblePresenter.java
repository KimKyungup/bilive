package org.qtum.wallet.ui.fragment.fragment_scribble;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

public interface IScribblePresenter extends BaseFragmentPresenter {

    void onAllPostSelected();
    void onMyPostSelected();

    void onRefresh();

    void onNetworkStateChanged(boolean networkConnectedFlag);

    void write();
    void loadAndUpdateWrite();
    void onLastItem(final int currentItemCount);
}
