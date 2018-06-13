package org.qtum.wallet.ui.fragment.fragment_back_up_wallet;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class BackUpWalletPresenterImpl extends BaseFragmentPresenterImpl implements IBackUpWalletPresenter {

    private IBackUpWalletView mFragmentView;
    private IBackUpWalletInteractor mFragmentInteractor;

    public BackUpWalletPresenterImpl(IBackUpWalletView fragmentView, IBackUpWalletInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IBackUpWalletView getView() {
        return mFragmentView;
    }

}
