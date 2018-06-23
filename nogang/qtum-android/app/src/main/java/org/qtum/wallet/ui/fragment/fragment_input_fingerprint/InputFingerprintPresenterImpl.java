package org.qtum.wallet.ui.fragment.fragment_input_fingerprint;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class InputFingerprintPresenterImpl extends BaseFragmentPresenterImpl implements IInputFingerprintPresenter {

    private final String SCRIBBLE_PASSWD_KEY = "Scribble_Fingerprint";

    private IInputFingerprintView mFragmentView;
    private IInputFingerprintInteractor mFragmentInteractor;


    private FingerprintManager mFingerprintManager;
    private FingerprintManager.CryptoObject mCryptoObject;

    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private Cipher cipher;
    private IInputFingerprintHelper fpHelper;

    public InputFingerprintPresenterImpl(IInputFingerprintView fragmentView, IInputFingerprintInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
        mFingerprintManager = mFragmentInteractor.getFingerprintManager();

        generateKey();
        cipherInit();

        mCryptoObject = new FingerprintManager.CryptoObject(cipher);
        fpHelper = new IInputFingerprintHelper(getInteractor().getContext(), this);
    }

    @Override
    public IInputFingerprintView getView() {
        return mFragmentView;
    }

    public IInputFingerprintInteractor getInteractor() {
        return mFragmentInteractor;
    }


    @Override
    public void registerLater() {
        getView().openMainFragment();
    }

    @Override
    public void allowFingerPrint() {
        fpHelper.startAuth(mFingerprintManager, mCryptoObject);
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void generateKey() {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");
        } catch (NoSuchAlgorithmException |
                NoSuchProviderException e) {
            throw new RuntimeException(
                    "Failed to get KeyGenerator instance", e);
        }

        try {
            keyStore.load(null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }

        try {
            keyGenerator.init(new
                    KeyGenParameterSpec.Builder(SCRIBBLE_PASSWD_KEY,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (InvalidAlgorithmParameterException e) {
            throw new RuntimeException(e);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean cipherInit() {
        try {
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (java.security.cert.CertificateException e) {
            e.printStackTrace();
        }

        try {
            SecretKey key = (SecretKey) keyStore.getKey(SCRIBBLE_PASSWD_KEY,null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | UnrecoverableKeyException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }
}
