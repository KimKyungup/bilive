package org.qtum.wallet.ui.fragment.fragment_input_fingerprint;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class InputFingerprintPresenterImpl extends BaseFragmentPresenterImpl implements InputFingerprintPresenter {

    private InputFingerprintView mFragmentView;
    private InputFingerprintInteractor mFragmentInteractor;

    public InputFingerprintPresenterImpl(InputFingerprintView fragmentView, InputFingerprintInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public InputFingerprintView getView() {
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
