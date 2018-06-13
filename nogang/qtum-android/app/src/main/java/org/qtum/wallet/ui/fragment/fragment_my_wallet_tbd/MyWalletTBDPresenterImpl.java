package org.qtum.wallet.ui.fragment.fragment_my_wallet_tbd;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class MyWalletTBDPresenterImpl extends BaseFragmentPresenterImpl implements IMyWalletTBDPresenter {

    private IMyWalletTBDView mFragmentView;
    private IMyWalletTBDInteractor mFragmentInteractor;

    public MyWalletTBDPresenterImpl(IMyWalletTBDView fragmentView, IMyWalletTBDInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IMyWalletTBDView getView() {
        return mFragmentView;
    }

}
