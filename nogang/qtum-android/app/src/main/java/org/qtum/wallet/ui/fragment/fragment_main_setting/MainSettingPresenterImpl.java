package org.qtum.wallet.ui.fragment.fragment_main_setting;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class MainSettingPresenterImpl extends BaseFragmentPresenterImpl implements MainSettingPresenter {

    private MainSettingView mFragmentView;
    private MainSettingInteractor mFragmentInteractor;

    public MainSettingPresenterImpl(MainSettingView fragmentView, MainSettingInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public MainSettingView getView() {
        return mFragmentView;
    }

}
