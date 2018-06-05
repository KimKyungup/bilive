package org.qtum.wallet.ui.fragment.fragment_input_password;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

public interface InputPasswordPresenter extends BaseFragmentPresenter {

    void onNumberKeyTouched(int num);
    void onDelKeyTouched();

    void setAction(InputPasswordAction action);
}
