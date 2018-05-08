package org.qtum.wallet.ui.fragment.wallet_main_fragment;

import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WalletMainPresenterImpl extends BaseFragmentPresenterImpl implements WalletMainPresenter {

    private WalletMainInteractor mWalletMainFragmentInteractor;
    private WalletMainView mWalletMainView;

    public WalletMainPresenterImpl(WalletMainView view, WalletMainInteractor interactor) {
        mWalletMainView = view;
        mWalletMainFragmentInteractor = interactor;
    }

    @Override
    public WalletMainView getView() {
        return mWalletMainView;
    }

    private WalletMainInteractor getInteractor() {
        return mWalletMainFragmentInteractor;
    }
}