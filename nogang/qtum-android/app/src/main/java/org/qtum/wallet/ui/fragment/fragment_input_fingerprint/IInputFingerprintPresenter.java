package org.qtum.wallet.ui.fragment.fragment_input_fingerprint;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

public interface IInputFingerprintPresenter extends BaseFragmentPresenter {

    // 다음에 하기
    void registerLater();

    // 지문 잠금 해제 사용
    void allowFingerPrint();
}
