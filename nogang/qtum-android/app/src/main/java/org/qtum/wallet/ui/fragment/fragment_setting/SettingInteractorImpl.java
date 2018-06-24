package org.qtum.wallet.ui.fragment.fragment_setting;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.widget.Toast;

import org.qtum.wallet.datastorage.ScribbleSharedPreference;

public class SettingInteractorImpl implements ISettingInteractor {

    private Context mContext;

    SettingInteractorImpl(Context context) {
        mContext = context;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean IsAvailableFingerprint()
    {
        if (!((FingerprintManager)mContext.getSystemService(Context.FINGERPRINT_SERVICE)).hasEnrolledFingerprints()) {
            // This happens when no fingerprints are registered.
            Toast.makeText(mContext, "지문 등록을 하고나서 사용이 가능합니다.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void setTouchIdEnable(boolean state)
    {
        ScribbleSharedPreference.getInstance().saveTouchIdEnable(mContext, state);
    }

    public boolean getTouchIdEnable()
    {
        return ScribbleSharedPreference.getInstance().getTouchIdEnable(mContext);
    }
}
