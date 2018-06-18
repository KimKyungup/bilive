package org.qtum.wallet.ui.fragment.fragment_scribble_detail;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

public interface IScribbleDetailPresenter extends BaseFragmentPresenter {

    void setIndex(int index);

    void onBackButtonClicked();
    void onInfoButtonClicked();
    void onShareButtonClicked();
}
