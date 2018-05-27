package org.qtum.wallet.ui.fragment.introduce_fragment;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class CreateWalletPresenterImpl extends BaseFragmentPresenterImpl implements CreateWalletPresenter {

    private CreateWalletView mWriteFragmentView;
    private CreateWalletInteractor mWriteFragmentInteractor;

    public CreateWalletPresenterImpl(CreateWalletView writeFragmentView, CreateWalletInteractor writeInteractor) {
        mWriteFragmentView = writeFragmentView;
        mWriteFragmentInteractor = writeInteractor;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public CreateWalletInteractor getInteractor() {
        return mWriteFragmentInteractor;
    }

    @Override
    public CreateWalletView getView() {
        return mWriteFragmentView;
    }

}
