package org.qtum.wallet.ui.fragment.write_fragment;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.model.writeblock.WriteBlock;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface WriteView extends BaseFragmentView {
    void startRefreshAnimation();

    void setAdapterNull();

    void updateWriteBlocks(List<WriteBlock> writeblocks);

    void stopRefreshRecyclerAnimation();

    String getWriteText();
    void tost(String s);
    WriteInteractorImpl.SendTxCallBack getSendTransactionCallback();

    public void loadNewWrite();
    public void addHistory(int positionStart, int itemCount, List<WriteBlock> historyList);

    public void notifyConfirmHistory(final int notifyPosition);

}