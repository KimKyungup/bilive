package org.qtum.wallet.ui.fragment.fragment_input_password;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class InputPasswordPresenterImpl extends BaseFragmentPresenterImpl implements InputPasswordPresenter {

    private final static int PASSWORD_LENGTH = 6;

    private InputPasswordView mFragmentView;
    private InputPasswordInteractor mFragmentInteractor;

    private InputPasswordAction action;

    private int nInputNumCount = 0;

    public InputPasswordPresenterImpl(InputPasswordView fragmentView, InputPasswordInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public InputPasswordView getView() {
        return mFragmentView;
    }

    @Override
    public void onNumberKeyTouched(int num) {
        if (nInputNumCount >= PASSWORD_LENGTH) {
            return;
        }

        nInputNumCount++;
        mFragmentView.setInputNumberPinImage(nInputNumCount);

        /** Test code **/
        if (nInputNumCount == PASSWORD_LENGTH) {
            // Test
            getView().openInputFingerprintFragment();
        }


        // TODO : Password를 저장하는 Logic 추가 필요

    }

    @Override
    public void onDelKeyTouched() {
        if (nInputNumCount <= 0) {
            return;
        }

        nInputNumCount--;
        mFragmentView.setInputNumberPinImage(nInputNumCount);
    }

    @Override
    public void setAction(InputPasswordAction action) {
        this.action = action;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        switch (action) {
            case REGISTER: {    // 등록모드
                getView().setDescriptionMessage(R.string.password_setting_guide_description);
                break;
            }
            case AUTHENTICATION: {  // 인증모드
                getView().setDescriptionMessage(R.string.password_input_guide_description);
                break;
            }
        }
    }
}
