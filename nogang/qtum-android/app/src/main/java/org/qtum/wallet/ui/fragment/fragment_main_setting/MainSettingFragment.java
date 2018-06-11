package org.qtum.wallet.ui.fragment.fragment_main_setting;

import android.os.Bundle;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;

public class MainSettingFragment extends BaseFragment implements MainSettingView {

    private MainSettingPresenter mFragmentPresenter;

    public static MainSettingFragment newInstance() {
        Bundle args = new Bundle();
        MainSettingFragment fragment = new MainSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new MainSettingPresenterImpl(this, new MainSettingInteractorImpl(getContext()));
    }

    @Override
    protected MainSettingPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_setting;
    }

}
