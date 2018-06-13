package org.qtum.wallet.ui.fragment.fragment_input_fingerprint;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class InputFingerprintPresenterImpl extends BaseFragmentPresenterImpl implements IInputFingerprintPresenter {

    private IInputFingerprintView mFragmentView;
    private IInputFingerprintInteractor mFragmentInteractor;

    public InputFingerprintPresenterImpl(IInputFingerprintView fragmentView, IInputFingerprintInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IInputFingerprintView getView() {
        return mFragmentView;
    }

    @Override
    public void registerLater() {
        getView().openMainFragment();
    }

    @Override
    public void allowFingerPrint() {
        getView().openMainFragment();
    }
}
