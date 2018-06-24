package org.qtum.wallet.ui.fragment.fragment_input_fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;

public interface IInputFingerprintInteractor {

    Context getContext();
    FingerprintManager getFingerprintManager();

    boolean IsEnabledTouchLogin();
    void SetTouchLoginActivate();
}
