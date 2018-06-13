package org.qtum.wallet.ui.fragment.fragment_setting;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class SettingPresenterImpl extends BaseFragmentPresenterImpl implements ISettingPresenter {

    private ISettingView mFragmentView;
    private ISettingInteractor mFragmentInteractor;

    public SettingPresenterImpl(ISettingView fragmentView, ISettingInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public ISettingView getView() {
        return mFragmentView;
    }

}
