package org.qtum.wallet.ui.fragment.write_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;


public interface WritePresenter extends BaseFragmentPresenter {
    void onRefresh();

    void onNetworkStateChanged(boolean networkConnectedFlag);

    void write();
    void loadAndUpdateWrite();
}
