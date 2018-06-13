package org.qtum.wallet.ui.fragment.fragment_my_wallet_tbd;

import android.content.Context;
import android.os.Bundle;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

public class MyWalletTBDFragment extends BaseFragment implements IMyWalletTBDView {

    private IMyWalletTBDPresenter mFragmentPresenter;

    public static MyWalletTBDFragment newInstance(Context context) {
        Bundle args = new Bundle();
        MyWalletTBDFragment fragment = (MyWalletTBDFragment) Factory.instantiateFragment(context, MyWalletTBDFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new MyWalletTBDPresenterImpl(this, new MyWalletTBDInteractorImpl(getContext()));
    }

    @Override
    protected IMyWalletTBDPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_wallet_tbd;
    }

}
