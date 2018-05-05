package org.qtum.wallet.ui.fragment.write_fragment;

import android.content.Context;

import org.bitcoinj.script.Script;
import org.qtum.wallet.dataprovider.rest_api.medium.MediumService;
import org.qtum.wallet.dataprovider.rest_api.qtum.QtumService;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.QtumSettingSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.gson.SendRawTransactionRequest;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.model.news.RssFeed;
import org.qtum.wallet.ui.fragment.news_fragment.NewsInteractor;
import org.qtum.wallet.ui.fragment.send_fragment.SendInteractorImpl;
import org.qtum.wallet.utils.ContractBuilder;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

public class WriteInteractorImpl implements WriteInteractor {

    private Context mContext;
    private SubscriptionList mSubscriptionList = new SubscriptionList();
    private final String MEDIUM_QTUM_CHANEL = "@qtum";

    public interface SendTxCallBack {
        void onSuccess();

        void onError(String error);
    }

    public WriteInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public Observable<RssFeed> getMediumRssFeed() {
        return MediumService.getInstance().getRssFeed(MEDIUM_QTUM_CHANEL);
    }

    @Override
    public Observable<HistoryResponse> getHistoryResponse(final String address, final int limit, final int offset) {
        return QtumService.newInstance().getHistoryResponse(address, limit, offset);
    }

    @Override
    public void unSubscribe() {
        mSubscriptionList.clear();
    }

    @Override
    public void sendTx(String txHex, final WriteInteractorImpl.SendTxCallBack callBack) {
        QtumService.newInstance().sendRawTransaction(new SendRawTransactionRequest(txHex, 1))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SendRawTransactionResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(SendRawTransactionResponse sendRawTransactionResponse) {
                        callBack.onSuccess();
                    }
                });
    }
    @Override
    public BigDecimal getFeePerKb() {
        QtumSettingSharedPreference qtumSettingSharedPreference = new QtumSettingSharedPreference();
        return new BigDecimal(qtumSettingSharedPreference.getFeePerKb(mContext));
    }
    @Override
    public String createTransactionHash(String abiParams, String contractAddress, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, String fee) {
        ContractBuilder contractBuilder = new ContractBuilder();
        Script script = contractBuilder.createMethodScript(abiParams, gasLimit, gasPrice, contractAddress);
        return contractBuilder.createTransactionHash(script, unspentOutputs, gasLimit, gasPrice, getFeePerKb(), fee, "",mContext);
    }

    @Override
    public void getUnspentOutputs(String address, final WriteInteractorImpl.GetUnspentListCallBack callBack) {
        if (address.equals("")) {
            getUnspentOutputs(callBack);
            return;
        }
        QtumService.newInstance().getUnspentOutputs(address)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UnspentOutput>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<UnspentOutput> unspentOutputs) {

                        for (Iterator<UnspentOutput> iterator = unspentOutputs.iterator(); iterator.hasNext(); ) {
                            UnspentOutput unspentOutput = iterator.next();
                            if (!unspentOutput.isOutputAvailableToPay()) {
                                iterator.remove();
                            }
                        }
                        Collections.sort(unspentOutputs, new Comparator<UnspentOutput>() {
                            @Override
                            public int compare(UnspentOutput unspentOutput, UnspentOutput t1) {
                                return unspentOutput.getAmount().doubleValue() < t1.getAmount().doubleValue() ? 1 : unspentOutput.getAmount().doubleValue() > t1.getAmount().doubleValue() ? -1 : 0;
                            }
                        });
                        callBack.onSuccess(unspentOutputs);
                    }
                });
    }

    public void getUnspentOutputs(final WriteInteractorImpl.GetUnspentListCallBack callBack) {
        QtumService.newInstance().getUnspentOutputsForSeveralAddresses(KeyStorage.getInstance().getAddresses())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<UnspentOutput>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError("Get Unspent Outputs " + e.getMessage());
                    }

                    @Override
                    public void onNext(List<UnspentOutput> unspentOutputs) {

                        for (Iterator<UnspentOutput> iterator = unspentOutputs.iterator(); iterator.hasNext(); ) {
                            UnspentOutput unspentOutput = iterator.next();
                            if (!unspentOutput.isOutputAvailableToPay()/* || unspentOutput.getConfirmations()==0*/) {
                                iterator.remove();
                            }
                        }
                        Collections.sort(unspentOutputs, new Comparator<UnspentOutput>() {
                            @Override
                            public int compare(UnspentOutput unspentOutput, UnspentOutput t1) {
                                return unspentOutput.getAmount().doubleValue() < t1.getAmount().doubleValue() ? 1 : unspentOutput.getAmount().doubleValue() > t1.getAmount().doubleValue() ? -1 : 0;
                            }
                        });
                        callBack.onSuccess(unspentOutputs);
                    }
                });
    }

    public interface GetUnspentListCallBack {
        void onSuccess(List<UnspentOutput> unspentOutputs);

        void onError(String error);
    }

    @Override
    public List<News> getNewses() {
        TinyDB tinyDB = new TinyDB(new WeakReference<>(mContext).get());
        return tinyDB.getNewsList();
    }

    @Override
    public void setNewses(List<News> newses) {
        TinyDB tinyDB = new TinyDB(new WeakReference<>(mContext).get());
        tinyDB.putNewsList(newses);
    }

}
