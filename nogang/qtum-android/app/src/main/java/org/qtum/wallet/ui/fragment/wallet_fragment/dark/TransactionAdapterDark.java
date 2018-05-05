package org.qtum.wallet.ui.fragment.wallet_fragment.dark;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.ui.fragment.wallet_fragment.ProgressBarHolder;
import org.qtum.wallet.ui.fragment.wallet_fragment.TransactionAdapter;
import org.qtum.wallet.ui.fragment.wallet_fragment.TransactionClickListener;

import java.util.List;

public class TransactionAdapterDark extends TransactionAdapter {

    public TransactionAdapterDark(List<History> historyList, TransactionClickListener listener) {
        super(historyList, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_TRANSACTION) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(org.qtum.wallet.R.layout.item_transaction, parent, false);
            return new TransactionHolderDark(view, listener);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(org.qtum.wallet.R.layout.item_progress_bar, parent, false);
            return new ProgressBarHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ProgressBarHolder) {
            ((ProgressBarHolder) holder).bindProgressBar(mLoadingFlag);
        } else {
            mHistory = mHistoryList.get(position);
            ((TransactionHolderDark) holder).bindTransactionData(mHistory);
        }
    }
}