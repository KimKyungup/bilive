package org.qtum.wallet.ui.fragment.fragment_my_wallet_send_coin;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class MyWalletSendCoinPresenterImpl extends BaseFragmentPresenterImpl implements IMyWalletSendCoinPresenter {

    private IMyWalletSendCoinView mFragmentView;
    private IMyWalletSendCoinInteractor mFragmentInteractor;

    public MyWalletSendCoinPresenterImpl(IMyWalletSendCoinView fragmentView, IMyWalletSendCoinInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IMyWalletSendCoinView getView() {
        return mFragmentView;
    }

}
