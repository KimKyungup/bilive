package org.qtum.wallet.ui.fragment.fragment_scribble;

import android.content.Context;

import org.bitcoinj.script.Script;
import org.qtum.wallet.dataprovider.rest_api.ether.EthereumService;
import org.qtum.wallet.dataprovider.rest_api.qtum.QtumService;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.datastorage.QtumSettingSharedPreference;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.gson.SendRawTransactionRequest;
import org.qtum.wallet.model.gson.SendRawTransactionResponse;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history_ether.TxListResponse;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.utils.ContractBuilder;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

public class ScribbleInteractorImpl implements IScribbleInteractor {

    public interface SendTxCallBack {
        void onSuccess();
        void onError(String error);
    }

    public interface GetUnspentListCallBack {
        void onSuccess(List<UnspentOutput> unspentOutputs);
        void onError(String error);
    }

    private Context mContext;
    private SubscriptionList mSubscriptionList = new SubscriptionList();

    ScribbleInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public Observable<HistoryResponse> getHistoryResponseQtum(String address, int limit, int offset) {
        return QtumService.newInstance().getHistoryResponse(address, limit, offset);
    }

    @Override
    public Observable<TxListResponse> getHistoryResponseEther(String address, int page, int size) {
        return EthereumService.newInstance().getTransactionList(address, page, size);
    }

    @Override
    public void unSubscribe() {
        if (mSubscriptionList != null) {
            mSubscriptionList.clear();
        }
    }

    @Override
    public void qtumSendTx(String txHex, final SendTxCallBack callBack) {
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
    public String qtumCreateTransactionHash(String abiParams, String contractAddress, List<UnspentOutput> unspentOutputs, int gasLimit, int gasPrice, String fee) {
        ContractBuilder contractBuilder = new ContractBuilder();
        Script script = contractBuilder.createMethodScript(abiParams, gasLimit, gasPrice, contractAddress);
        return contractBuilder.createTransactionHash(script, unspentOutputs, gasLimit, gasPrice, getFeePerKb(), fee, "",mContext);
    }

    @Override
    public Observable<String> createEtherTransactionHash(final String text, final String contractAddress, int gasLimit, int gasPrice, String fee) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                HttpService httpService = new HttpService("https://rinkeby.infura.io/gGHwulfhVK8ouWn8aZMz");
                Web3j web3 = Web3jFactory.build(httpService);
                Credentials credentials = KeyStorage.getInstance().getCredentials();
                RawTransactionManager rawTxManager = new RawTransactionManager(web3, credentials);

                //BigInteger bigGasPrice = new BigInteger("0");
                //BigInteger bigGasLimit = new BigInteger("0");

                String result = "";
                try {
                    BigInteger bigGasLimit = BigInteger.valueOf(90000);
                    BigInteger bigGasPrice = web3.ethGasPrice().send().getGasPrice();
                    EthSendTransaction ethSendTransaction = rawTxManager.sendTransaction(bigGasPrice, bigGasLimit, contractAddress, text, BigInteger.ZERO);
                    result = ethSendTransaction.getTransactionHash();
                }
                catch (IOException e){

                }

                subscriber.onNext(result);
            }
        });
    }

    @Override
    public BigDecimal getFeePerKb() {
        QtumSettingSharedPreference qtumSettingSharedPreference = new QtumSettingSharedPreference();
        return new BigDecimal(qtumSettingSharedPreference.getFeePerKb(mContext));
    }

    @Override
    public void getUnspentOutputs(String address, final GetUnspentListCallBack callBack) {
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

    public void getUnspentOutputs(final GetUnspentListCallBack callBack) {
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
