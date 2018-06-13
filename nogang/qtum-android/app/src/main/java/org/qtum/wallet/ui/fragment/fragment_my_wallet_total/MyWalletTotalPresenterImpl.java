package org.qtum.wallet.ui.fragment.fragment_my_wallet_total;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class MyWalletTotalPresenterImpl extends BaseFragmentPresenterImpl implements IMyWalletTotalPresenter {

    private IMyWalletTotalView mFragmentView;
    private IMyWalletTotalInteractor mFragmentInteractor;

    public MyWalletTotalPresenterImpl(IMyWalletTotalView fragmentView, IMyWalletTotalInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IMyWalletTotalView getView() {
        return mFragmentView;
    }

}
