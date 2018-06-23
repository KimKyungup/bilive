package org.qtum.wallet.ui.fragment.fragment_input_fingerprint;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;

public class InputFingerprintInteractorImpl implements IInputFingerprintInteractor {

    private Context mContext;
    private FingerprintManager mFingerprintManager;

    InputFingerprintInteractorImpl(Context context) {
        mContext = context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mFingerprintManager = (FingerprintManager) mContext.getSystemService(Context.FINGERPRINT_SERVICE);
        }
        else{
            mFingerprintManager = null;
        }
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public FingerprintManager getFingerprintManager() {
        return mFingerprintManager;
    }
}
