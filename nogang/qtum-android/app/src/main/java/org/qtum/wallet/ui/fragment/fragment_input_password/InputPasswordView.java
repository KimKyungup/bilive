package org.qtum.wallet.ui.fragment.fragment_input_password;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

public interface InputPasswordView extends BaseFragmentView {

    // Input Pin image의 개수를 변경 (0 ~ 6)
    void setInputNumberPinImage(int count);

    // Guide text 변경
    void setDescriptionMessage(int strResourceID);

    // Keypad를 기본 이미지로 변경
    void setKeypadNormalImage();

    // Keypad를 잘못된 입력 이미지로 변경
    void setKeypadWrongImage();

    void openInputFingerprintFragment();
}
