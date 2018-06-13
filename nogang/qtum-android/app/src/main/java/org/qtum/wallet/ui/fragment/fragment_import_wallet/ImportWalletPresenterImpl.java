package org.qtum.wallet.ui.fragment.fragment_import_wallet;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class ImportWalletPresenterImpl extends BaseFragmentPresenterImpl implements IImportWalletPresenter {

    private IImportWalletView mFragmentView;
    private IImportWalletInteractor mFragmentInteractor;

    public ImportWalletPresenterImpl(IImportWalletView fragmentView, IImportWalletInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IImportWalletView getView() {
        return mFragmentView;
    }

}
