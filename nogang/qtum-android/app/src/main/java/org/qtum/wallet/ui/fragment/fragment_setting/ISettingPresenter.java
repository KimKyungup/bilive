package org.qtum.wallet.ui.fragment.fragment_setting;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

public interface ISettingPresenter extends BaseFragmentPresenter {

    boolean loadSettingValue();
    void toggleFingerprintState();

    void setFingerprintEnable();
}
