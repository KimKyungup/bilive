package org.qtum.wallet.ui.fragment.fragment_scribble;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;

public interface IScribblePresenter extends BaseFragmentPresenter {

    void onAllPostSelected();
    void onMyPostSelected();
    void onWriteComplete();

}
