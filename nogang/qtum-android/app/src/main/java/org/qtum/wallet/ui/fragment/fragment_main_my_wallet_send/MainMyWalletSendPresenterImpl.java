package org.qtum.wallet.ui.fragment.fragment_main_my_wallet_send;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class MainMyWalletSendPresenterImpl extends BaseFragmentPresenterImpl implements MainMyWalletSendPresenter {

    private MainMyWalletSendView mFragmentView;
    private MainMyWalletSendInteractor mFragmentInteractor;

    public MainMyWalletSendPresenterImpl(MainMyWalletSendView fragmentView, MainMyWalletSendInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public MainMyWalletSendView getView() {
        return mFragmentView;
    }

}
