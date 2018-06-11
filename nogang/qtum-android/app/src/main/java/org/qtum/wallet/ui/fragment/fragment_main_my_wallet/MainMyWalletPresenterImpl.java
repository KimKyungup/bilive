package org.qtum.wallet.ui.fragment.fragment_main_my_wallet;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class MainMyWalletPresenterImpl extends BaseFragmentPresenterImpl implements MainMyWalletPresenter {

    private MainMyWalletView mFragmentView;
    private MainMyWalletInteractor mFragmentInteractor;

    public MainMyWalletPresenterImpl(MainMyWalletView fragmentView, MainMyWalletInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public MainMyWalletView getView() {
        return mFragmentView;
    }

}
