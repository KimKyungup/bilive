package org.qtum.wallet.ui.fragment.write_fragment;

import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.model.news.RssFeed;
import org.qtum.wallet.ui.fragment.send_fragment.SendInteractorImpl;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;

public interface WriteInteractor {

    Observable<RssFeed> getMediumRssFeed();
    Observable<HistoryResponse> getHistoryResponse(final String address, final int limit, final int offset);
     void unSubscribe();

    void sendTx(String txHex, WriteInteractorImpl.SendTxCallBack callBack);

    String createTransactionHash(String abiParams, String contractAddress, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, String fee);

    BigDecimal getFeePerKb();
    void getUnspentOutputs(String address, final WriteInteractorImpl.GetUnspentListCallBack callBack);
    //void getUnspentOutputs(WriteInteractorImpl.GetUnspentListCallBack callBack);
    List<News> getNewses();

    void setNewses(List<News> newses);
}
