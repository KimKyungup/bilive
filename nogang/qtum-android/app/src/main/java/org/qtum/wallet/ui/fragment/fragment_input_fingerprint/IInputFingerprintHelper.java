
package org.qtum.wallet.ui.fragment.fragment_input_fingerprint;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.widget.Toast;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

import javax.security.auth.callback.Callback;

@TargetApi(Build.VERSION_CODES.M)
public class IInputFingerprintHelper extends FingerprintManager.AuthenticationCallback {

    private static final long ERROR_TIMEOUT_MILLIS = 1600;
    private static final long SUCCESS_DELAY_MILLIS = 1300;

    private Context mContext;

    private CancellationSignal mCancellationSignal;

    private boolean mSelfCancelled;

    private IInputFingerprintPresenter mPresenter;


    public IInputFingerprintHelper(Context context, IInputFingerprintPresenter presenter) {
        mContext = context;
        mPresenter = presenter;
    }

    public IInputFingerprintPresenter getPresenter()
    {
        return mPresenter;
    }

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cancellationSignal = new CancellationSignal();
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId,
                                      CharSequence errString) {
        Toast.makeText(mContext,
                "Authentication error\n" + errString,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId,
                                     CharSequence helpString) {
        Toast.makeText(mContext,
                "Authentication help\n" + helpString,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(mContext,
                "등록되지 않은 지문입니다." ,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        Toast.makeText(mContext,
                "지문인식 성공!" ,
                Toast.LENGTH_LONG).show();

        getPresenter().registerLater();
    }
}