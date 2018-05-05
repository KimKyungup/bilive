package org.qtum.wallet.ui.fragment.transaction_fragment;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.Vin;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class TransactionPresenterImpl extends BaseFragmentPresenterImpl implements TransactionPresenter {

    private TransactionView mTransactionView;
    private TransactionInteractor mTransactionInteractor;

    public TransactionPresenterImpl(TransactionView view, TransactionInteractor interactor) {
        mTransactionView = view;
        mTransactionInteractor = interactor;
    }

    @Override
    public TransactionView getView() {
        return mTransactionView;
    }

    private TransactionInteractor getInteractor() {
        return mTransactionInteractor;
    }

    @Override
    public void openTransactionView(int position) {
        String dateString;
        History history = getInteractor().getHistory(position);
        if (history.getBlockTime() != null) {
            dateString = getInteractor().getFullDate(history.getBlockTime() * 1000L);
        } else {
            dateString = getInteractor().getUnconfirmedDate();
        }
        getView().setUpTransactionData(history.getChangeInBalance().toString(), history.getFee().toString(), dateString,
                history.getBlockHeight() > 0, history.getTransactionReceipt()!=null && history.getTransactionReceipt().getLog()!=null && !history.getTransactionReceipt().getLog().isEmpty());
    }
}
