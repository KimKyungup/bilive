package org.qtum.wallet.ui.fragment.fragment_scribble;

import org.qtum.wallet.model.writeblock.WriteBlock;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

import java.util.List;

public interface IScribbleView extends BaseFragmentView {

    void startRefreshAnimation();

    void setAdapterNull();

    void updateWriteBlocks(List<WriteBlock> writeblocks);

    void stopRefreshRecyclerAnimation();

    String getWriteText();
    void tost(String s);
    ScribbleInteractorImpl.SendTxCallBack getSendTransactionCallback();

    public void loadNewWrite();
    public void addHistory(int positionStart, int itemCount, List<WriteBlock> historyList);

    public void notifyConfirmHistory(final int notifyPosition);
}
