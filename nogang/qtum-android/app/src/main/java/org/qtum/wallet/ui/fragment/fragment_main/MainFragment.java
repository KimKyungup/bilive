package org.qtum.wallet.ui.fragment.fragment_main;

import android.content.Context;
import android.os.Bundle;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;
import org.qtum.wallet.ui.fragment_factory.Factory;

public class MainFragment extends BaseFragment implements MainView {

    private MainPresenter mFragmentPresenter;

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, MainFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new MainPresenterImpl(this, new MainInteractorImpl(getContext()));
    }

    @Override
    protected MainPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

}
