package org.qtum.wallet.ui.fragment.fragment_main_write;

import android.widget.Toast;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class MainWritePresenterImpl extends BaseFragmentPresenterImpl implements MainWritePresenter {

    private MainWriteView mFragmentView;
    private MainWriteInteractor mFragmentInteractor;

    public MainWritePresenterImpl(MainWriteView fragmentView, MainWriteInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public MainWriteView getView() {
        return mFragmentView;
    }

    @Override
    public void onAllPostSelected() {

    }

    @Override
    public void onMyPostSelected() {

    }

    @Override
    public void onWriteComplete() {

    }
}
