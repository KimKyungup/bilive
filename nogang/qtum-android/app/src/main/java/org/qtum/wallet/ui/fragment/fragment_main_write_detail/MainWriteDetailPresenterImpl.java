package org.qtum.wallet.ui.fragment.fragment_main_write_detail;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class MainWriteDetailPresenterImpl extends BaseFragmentPresenterImpl implements MainWriteDetailPresenter {

    private MainWriteDetailView mFragmentView;
    private MainWriteDetailInteractor mFragmentInteractor;

    public MainWriteDetailPresenterImpl(MainWriteDetailView fragmentView, MainWriteDetailInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public MainWriteDetailView getView() {
        return mFragmentView;
    }

}
