package org.qtum.wallet.ui.fragment.fragment_my_wallet_one_coin;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class MyWalletOneCoinPresenterImpl extends BaseFragmentPresenterImpl implements IMyWalletOneCoinPresenter {

    private IMyWalletOneCoinView mFragmentView;
    private IMyWalletOneCoinInteractor mFragmentInteractor;

    public MyWalletOneCoinPresenterImpl(IMyWalletOneCoinView fragmentView, IMyWalletOneCoinInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IMyWalletOneCoinView getView() {
        return mFragmentView;
    }

}
