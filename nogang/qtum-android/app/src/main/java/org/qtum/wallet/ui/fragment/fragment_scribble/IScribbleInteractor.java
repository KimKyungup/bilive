package org.qtum.wallet.ui.fragment.fragment_scribble;

import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history_ether.TxListResponse;
import org.qtum.wallet.model.news.News;

import java.math.BigDecimal;
import java.util.List;

import rx.Observable;

public interface IScribbleInteractor {

    Observable<HistoryResponse> getHistoryResponseQtum(final String address, final int limit, final int offset);
    Observable<TxListResponse> getHistoryResponseEther(final String address, final int page, final int size);
    void unSubscribe();

    void qtumSendTx(String txHex, ScribbleInteractorImpl.SendTxCallBack callBack);

    String qtumCreateTransactionHash(String abiParams, String contractAddress, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, String fee);
    Observable<String> createEtherTransactionHash(final String text, final String contractAddress, final int gasLimit, final int gasPrice, final String fee);

    BigDecimal getFeePerKb();
    void getUnspentOutputs(String address, final ScribbleInteractorImpl.GetUnspentListCallBack callBack);
    //void getUnspentOutputs(WriteInteractorImpl.GetUnspentListCallBack callBack);
    List<News> getNewses();

    void setNewses(List<News> newses);

}
