package org.qtum.wallet.ui.fragment.fragment_input_password;

import android.os.Handler;

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

    private String mCurrentInputPasswd;
    private String mPreviousInputPasswd;
    private String PasswdHash;
    private String mSavedPasswd;

    public InputPasswordPresenterImpl(IInputPasswordView fragmentView, IInputPasswordInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;

        InitializeInputPasswdBlock();
    }

    @Override
    public IInputPasswordView getView() {
        return mFragmentView;
    }

    private IInputPasswordInteractor getInteractor() {
        return mFragmentInteractor;
    }

    @Override
    public void onNumberKeyTouched(int num) {
        if (nInputNumCount >= PASSWORD_LENGTH) {
            return;
        }

        mCurrentInputPasswd += num;
        nInputNumCount++;
        mFragmentView.setInputNumberPinImage(nInputNumCount);

        /** For UI Test code **/
        if (nInputNumCount == PASSWORD_LENGTH) {
            switch (testMode) {
                case 0:
                    getView().setInputNumberPinImage(0);
                    getView().setDescriptionMessage(R.string.password_retry_guide_description);

                    nInputNumCount = 0;
                    mPreviousInputPasswd = mCurrentInputPasswd;
                    mCurrentInputPasswd = "";

                    testMode = 1;
                    break;
                case 1:
                    if (mCurrentInputPasswd.equals(mPreviousInputPasswd)) {
                        PasswdHash = getInteractor().generateSHA256String(mCurrentInputPasswd);
                        getInteractor().savePassword(PasswdHash);
                        getInteractor().setKeyGeneratedInstance(true);

                        getView().setInputNumberPinImage(0);
                        getView().setDescriptionMessage(R.string.password_input_guide_description);

                        InitializeInputPasswdBlock();

                        testMode = 2;
                    } else {
                        getView().setInputNumberPinImage(0);
                        getView().setKeypadWrongImage();
                        getView().setDescriptionMessage(R.string.password_wrong_guide_description);

                        InitializeInputPasswdBlock();

                        testMode = 0;
                    }

                    break;
                case 2:

                    getView().setDescriptionMessage(R.string.password_wrong_guide_description);

                    PasswdHash = getInteractor().generateSHA256String(mCurrentInputPasswd);
                    mSavedPasswd = getInteractor().getPassword();

                    if (mSavedPasswd.isEmpty() == true) {
                        getView().setDescriptionMessage(R.string.password_register_guide_description);

                        InitializeInputPasswdBlock();

                        testMode = 0;
                    } else {
                        if (PasswdHash.equals(getInteractor().getPassword())) {
                            getView().setDescriptionMessage(R.string.password_correct_guide_description);

                            InitializeInputPasswdBlock();

                            testMode = 3;

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getView().openInputFingerprintFragment();
                                }
                            }, 2000);
                        } else {
                            getView().setInputNumberPinImage(0);
                            getView().setDescriptionMessage(R.string.password_wrong_guide_description);

                            InitializeInputPasswdBlock();

                            testMode = 2;
                        }
                    }
                    break;
                case 3:
                    break;
            }
        }
    }

    @Override
    public void onDelKeyTouched() {
        if (nInputNumCount <= 0) {
            return;
        }

        nInputNumCount--;
        mCurrentInputPasswd = mCurrentInputPasswd.substring(0, mCurrentInputPasswd.length()-1);
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
                testMode = 0;
                break;
            }
            case AUTHENTICATION: {  // 인증모드
                getView().setDescriptionMessage(R.string.password_input_guide_description);
                testMode = 2;
                break;
            }
        }
    }

    private void InitializeInputPasswdBlock() {
        nInputNumCount = 0;
        mPreviousInputPasswd = "";
        mCurrentInputPasswd = "";
        PasswdHash = "";
        mSavedPasswd = "";
    }
}
