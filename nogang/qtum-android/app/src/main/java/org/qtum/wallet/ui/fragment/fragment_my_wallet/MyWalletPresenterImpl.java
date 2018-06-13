package org.qtum.wallet.ui.fragment.fragment_my_wallet;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class MyWalletPresenterImpl extends BaseFragmentPresenterImpl implements IMyWalletPresenter {

    private IMyWalletView mFragmentView;
    private IMyWalletInteractor mFragmentInteractor;

    public MyWalletPresenterImpl(IMyWalletView fragmentView, IMyWalletInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IMyWalletView getView() {
        return mFragmentView;
    }

}
