package org.qtum.wallet.ui.fragment.fragment_main;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class MainPresenterImpl extends BaseFragmentPresenterImpl implements MainPresenter {

    private MainView mFragmentView;
    private MainInteractor mFragmentInteractor;

    public MainPresenterImpl(MainView fragmentView, MainInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public MainView getView() {
        return mFragmentView;
    }

}
