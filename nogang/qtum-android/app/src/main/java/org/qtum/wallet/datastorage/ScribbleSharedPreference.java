package org.qtum.wallet.datastorage;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ScribbleSharedPreference {
    private static ScribbleSharedPreference sInstance = null;

    private final String SCRIBBLE_IS_KEY_GENERATED = "scribble_is_key_exist";
    private final String SCRIBBLE_DATA_STORAGE = "scribble_data_storage";
    private final String SCRIBBLE_DIGIT_PASSWORD = "scribble_wallet_digit_password";
    private final String TOUCH_ID_ENABLE = "scribble_touch_enable";

    
    private ScribbleSharedPreference() {
    }

    public static ScribbleSharedPreference getInstance() {
        if (sInstance == null) {
            sInstance = new ScribbleSharedPreference();
        }
        return sInstance;
    }

    public void saveDigitPassword(Context context, String password) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SCRIBBLE_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(SCRIBBLE_DIGIT_PASSWORD, password);
        mEditor.apply();
    }

    public String getDigitPassword(Context context) {
        return context.getSharedPreferences(SCRIBBLE_DATA_STORAGE, Context.MODE_PRIVATE).getString(SCRIBBLE_DIGIT_PASSWORD, "");
    }

    public void saveTouchIdEnable(Context context, boolean isEnable) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SCRIBBLE_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(TOUCH_ID_ENABLE, isEnable);
        mEditor.apply();
    }

    public boolean getTouchIdEnable(Context context) {
        return context.getSharedPreferences(SCRIBBLE_DATA_STORAGE, Context.MODE_PRIVATE).getBoolean(TOUCH_ID_ENABLE, false);
    }

    public void setKeyGeneratedInstance(Context context, boolean isKeyGenerated) {
        SharedPreferences mSharedPreferences = context.getSharedPreferences(SCRIBBLE_DATA_STORAGE, Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean(SCRIBBLE_IS_KEY_GENERATED, isKeyGenerated);
        mEditor.apply();
    }

    public boolean getKeyGeneratedInstance(Context context) {
        return context.getSharedPreferences(SCRIBBLE_DATA_STORAGE, Context.MODE_PRIVATE).getBoolean(SCRIBBLE_IS_KEY_GENERATED, false);
    }
}