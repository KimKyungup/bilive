package org.qtum.wallet.ui.fragment.write_fragment;

import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history_ether.TxListResponse;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.model.news.RssFeed;
import org.qtum.wallet.ui.fragment.send_fragment.SendInteractorImpl;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;

public interface WriteInteractor {


    Observable<HistoryResponse> getHistoryResponseQtum(final String address, final int limit, final int offset);
    Observable<TxListResponse> getHistoryResponseEther(final String address, final int page, final int size);
    void unSubscribe();

    void qtumSendTx(String txHex, WriteInteractorImpl.SendTxCallBack callBack);

    String qtumCreateTransactionHash(String abiParams, String contractAddress, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, String fee);
    Observable<String> createEtherTransactionHash(final String text, final String contractAddress, final int gasLimit, final int gasPrice, final String fee);

    BigDecimal getFeePerKb();
    void getUnspentOutputs(String address, final WriteInteractorImpl.GetUnspentListCallBack callBack);
    //void getUnspentOutputs(WriteInteractorImpl.GetUnspentListCallBack callBack);
    List<News> getNewses();

    void setNewses(List<News> newses);
}
