package org.qtum.wallet.ui.fragment.fragment_scribble_detail;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

public interface IScribbleDetailView extends BaseFragmentView {

    void setBody(String body);
    void showShareMenu(String text);
}
