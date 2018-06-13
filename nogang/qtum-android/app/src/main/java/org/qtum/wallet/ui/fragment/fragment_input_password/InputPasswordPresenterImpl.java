package org.qtum.wallet.ui.fragment.fragment_input_password;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class InputPasswordPresenterImpl extends BaseFragmentPresenterImpl implements IInputPasswordPresenter {

    private final static int PASSWORD_LENGTH = 6;

    private IInputPasswordView mFragmentView;
    private IInputPasswordInteractor mFragmentInteractor;

    private InputPasswordAction action;

    private int nInputNumCount = 0;

    /* For UI Demo */
    private int testMode = 0;

    public InputPasswordPresenterImpl(IInputPasswordView fragmentView, IInputPasswordInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IInputPasswordView getView() {
        return mFragmentView;
    }

    @Override
    public void onNumberKeyTouched(int num) {
        if (nInputNumCount >= PASSWORD_LENGTH) {
            return;
        }

        nInputNumCount++;
        mFragmentView.setInputNumberPinImage(nInputNumCount);

        /** For UI Test code **/
        if (nInputNumCount == PASSWORD_LENGTH) {
            switch (testMode) {
                case 0:
                    getView().setInputNumberPinImage(0);
                    getView().setDescriptionMessage(R.string.password_retry_guide_description);
                    nInputNumCount = 0;
                    break;
                case 1:
                    getView().setInputNumberPinImage(0);
                    getView().setDescriptionMessage(R.string.password_input_guide_description);
                    nInputNumCount = 0;
                    break;
                case 2:
                    getView().setKeypadWrongImage();
                    getView().setDescriptionMessage(R.string.password_wrong_guide_description);
                    nInputNumCount = 5;
                    break;
                case 3:
                    getView().openInputFingerprintFragment();
                    break;
            }
            testMode++;
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
