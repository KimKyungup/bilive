package org.qtum.wallet.ui.fragment.fragment_setting;

public interface ISettingInteractor {

    boolean IsAvailableFingerprint();
    void setTouchIdEnable(boolean state);
    boolean getTouchIdEnable();
}
