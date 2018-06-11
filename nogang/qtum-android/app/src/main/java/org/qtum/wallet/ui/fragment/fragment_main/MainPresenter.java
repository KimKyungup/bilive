package org.qtum.wallet.ui.fragment.fragment_main;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

public interface MainPresenter extends BaseFragmentPresenter {

    void onWriteMenuSelected();
    void onMyWalletMenuSelected();
    void onSettingMenuSelected();

    void setAction(MainAction action);
}
