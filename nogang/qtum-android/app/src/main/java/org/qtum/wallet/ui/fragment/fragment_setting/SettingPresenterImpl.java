package org.qtum.wallet.ui.fragment.fragment_setting;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class SettingPresenterImpl extends BaseFragmentPresenterImpl implements ISettingPresenter {

    private ISettingView mFragmentView;
    private ISettingInteractor mFragmentInteractor;
    private boolean mTouchState;

    public SettingPresenterImpl(ISettingView fragmentView, ISettingInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public ISettingView getView() {
        return mFragmentView;
    }

    public boolean loadSettingValue() {
        mTouchState = getInteractor().getTouchIdEnable();
        getView().setFingerprintState(mTouchState);

        return true;
    }

    public ISettingInteractor getInteractor() {
        return mFragmentInteractor;
    }

    public void toggleFingerprintState() {

        if ( getInteractor().IsAvailableFingerprint() == true )
        {
            if (mTouchState) {
                mTouchState = false;
            }
            else {
                mTouchState = true;
            }
            getView().setFingerprintState(mTouchState);
            getInteractor().setTouchIdEnable(mTouchState);
        }
    }
    public void setFingerprintEnable() {
        getView().openInputFingerprintFragment();
    }
}
