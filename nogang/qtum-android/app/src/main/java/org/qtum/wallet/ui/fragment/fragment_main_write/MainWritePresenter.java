package org.qtum.wallet.ui.fragment.fragment_main_write;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

public interface MainWritePresenter extends BaseFragmentPresenter {

    void onAllPostSelected();
    void onMyPostSelected();
    void onWriteComplete();

}
