package org.qtum.wallet.ui.fragment.write_fragment;

import org.qtum.wallet.model.news.News;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface WriteView extends BaseFragmentView {
    void startRefreshAnimation();

    void setAdapterNull();

    void updateNews(List<News> newses);

    void stopRefreshRecyclerAnimation();

    String getWriteText();
    void tost(String s);
    WriteInteractorImpl.SendTxCallBack getSendTransactionCallback();
}