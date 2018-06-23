package org.qtum.wallet.ui.fragment.fragment_input_password;

import android.content.Context;

import org.qtum.wallet.utils.CryptoUtilsCompat;
import org.qtum.wallet.utils.crypto.KeyStoreHelper;

import org.qtum.wallet.datastorage.ScribbleSharedPreference;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class InputPasswordInteractorImpl implements IInputPasswordInteractor {

    private final String SCRIBBLE_PASSWD_KEY = "Scribble_dApp";

    private Context mContext;

    InputPasswordInteractorImpl(Context context) {
        mContext = context;
        try {
            KeyStoreHelper.createKeys(mContext, SCRIBBLE_PASSWD_KEY);
        } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getPassword() {
        String encryptedHash = ScribbleSharedPreference.getInstance().getDigitPassword(mContext);
        if ( encryptedHash.isEmpty() )
        {
            return encryptedHash;
        }
        else
        {
            return KeyStoreHelper.decrypt(SCRIBBLE_PASSWD_KEY, encryptedHash);
        }
    }

    @Override
    public void savePassword(String password) {
        String encryptedHash = KeyStoreHelper.encrypt(SCRIBBLE_PASSWD_KEY, password);
        ScribbleSharedPreference.getInstance().saveDigitPassword(mContext, encryptedHash);
    }


    @Override
    public String generateSHA256String(String pin) {
        return CryptoUtilsCompat.generateSHA256String(pin);
    }

    @Override
    public void setKeyGeneratedInstance(boolean isKeyGenerated) {
        ScribbleSharedPreference.getInstance().setKeyGeneratedInstance(mContext, isKeyGenerated);
    }
}
