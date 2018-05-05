package org.qtum.wallet.ui.fragment.wallet_fragment;

import com.google.gson.JsonSyntaxException;

import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history.TransactionReceipt;
import org.qtum.wallet.model.gson.history.Vin;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

import java.math.BigDecimal;
import java.util.List;

import io.realm.Realm;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

public class WalletPresenterImpl extends BaseFragmentPresenterImpl implements WalletPresenter {
    private WalletInteractor mWalletFragmentInteractor;
    private WalletView mWalletView;
    private boolean mVisibility = false;
    private boolean mNetworkConnectedFlag = false;
    private SubscriptionList mSubscriptionList = new SubscriptionList();

    private final int ONE_PAGE_COUNT = 25;

    public WalletPresenterImpl(WalletView walletView, WalletInteractor interactor) {
        mWalletView = walletView;
        mWalletFragmentInteractor = interactor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        String pubKey = getInteractor().getAddress();
        getView().updatePubKey(pubKey);
        getView().updateHistory(getInteractor().getHistoryList());
    }

    @Override
    public WalletView getView() {
        return mWalletView;
    }

    private WalletInteractor getInteractor() {
        return mWalletFragmentInteractor;
    }

    @Override
    public void onRefresh() {
        if (mNetworkConnectedFlag) {
            loadAndUpdateData();
        } else {
            getView().setAlertDialog(org.qtum.wallet.R.string.no_internet_connection,
                    org.qtum.wallet.R.string.please_check_your_network_settings,
                    org.qtum.wallet.R.string.ok,
                    BaseFragment.PopUpType.error);
            getView().stopRefreshRecyclerAnimation();
        }
    }

    @Override
    public void openTransactionFragment(int position) {
        getView().openTransactionsFragment(position);
    }

    @Override
    public void onLastItem(final int currentItemCount) {
        if (getInteractor().getHistoryList().size() != getInteractor().getTotalHistoryItem()) {
            getView().loadNewHistory();
            mSubscriptionList.add(getInteractor().getHistoryList(ONE_PAGE_COUNT, currentItemCount)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<HistoryResponse>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(HistoryResponse historyResponse) {
                            for (History history : historyResponse.getItems()) {
                                calculateChangeInBalance(history, getInteractor().getAddresses());
                            }
                            HistoryList.getInstance().getHistoryList().addAll(historyResponse.getItems());
                            getView().addHistory(currentItemCount, getInteractor().getHistoryList().size() - currentItemCount + 1,
                                    getInteractor().getHistoryList());
                            initTransactionReceipt(historyResponse.getItems());
                        }
                    }));

        }
    }

    private void calculateChangeInBalance(History history, List<String> addresses) {
        BigDecimal totalVin = new BigDecimal("0.0");
        BigDecimal totalVout = new BigDecimal("0.0");
        BigDecimal totalOwnVin = new BigDecimal("0.0");
        BigDecimal totalOwnVout = new BigDecimal("0.0");

        boolean isOwnVin = false;

        for (Vin vin : history.getVin()) {
            for (String address : addresses) {
                if (vin.getAddress().equals(address)) {
                    isOwnVin = true;
                    vin.setOwnAddress(true);
                    totalOwnVin = totalOwnVin.add(vin.getValue());
                }
            }
            totalVin = totalVin.add(vin.getValue());
        }

        for (Vout vout : history.getVout()) {
            for (String address : addresses) {
                if (vout.getAddress().equals(address)) {
                    vout.setOwnAddress(true);
                    totalOwnVout = totalOwnVout.add(vout.getValue());
                }
            }
            totalVout = totalVout.add(vout.getValue());
        }

        history.setFee(totalVin.subtract(totalVout));
        if (isOwnVin) {
            history.setChangeInBalance(totalOwnVout.subtract(totalOwnVin).add(history.getFee()));
        } else {
            history.setChangeInBalance(totalOwnVout.subtract(totalOwnVin));
        }
    }


    private void loadAndUpdateData() {
        getView().startRefreshAnimation();
        mSubscriptionList.add(getInteractor().getHistoryList(ONE_PAGE_COUNT, 0).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HistoryResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().setAlertDialog(org.qtum.wallet.R.string.no_internet_connection,
                                org.qtum.wallet.R.string.please_check_your_network_settings,
                                org.qtum.wallet.R.string.ok,
                                BaseFragment.PopUpType.error);
                        getView().stopRefreshRecyclerAnimation();
                    }

                    @Override
                    public void onNext(final HistoryResponse historyResponse) {
                        for (History history : historyResponse.getItems()) {
                            calculateChangeInBalance(history, getInteractor().getAddresses());
                        }
                        HistoryList.getInstance().setHistoryList(historyResponse.getItems());
                        HistoryList.getInstance().setTotalItem(historyResponse.getTotalItems());
                        initTransactionReceipt(historyResponse.getItems());
                        getView().updateHistory(getInteractor().getHistoryList());
                    }
                }));
    }

    private void initTransactionReceipt(final List<History> histories) {
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                List<History> historiesDB = realm.copyFromRealm(realm.where(History.class).findAll());
                for (final History history : histories) {
                    if (history.getTransactionReceipt() == null) {
                        boolean success = false;
                        for (History historyDB : historiesDB) {
                            if (history.getTxHash().equals(historyDB.getTxHash())) {
                                history.setTransactionReceipt(historyDB.getTransactionReceipt());
                                success = true;
                                break;
                            }
                        }
                        if (!success) {
                            getInteractor().getTransactionReceipt(history.getTxHash())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Subscriber<List<TransactionReceipt>>() {
                                        @Override
                                        public void onCompleted() {

                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }

                                        @Override
                                        public void onNext(final List<TransactionReceipt> transactionReceipt) {
                                            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                                                @Override
                                                public void execute(Realm realm) {
                                                    if(transactionReceipt.size()>0) {
                                                        history.setTransactionReceipt(transactionReceipt.get(0));
                                                        realm.insertOrUpdate(history);
                                                    } else {
                                                        history.setReceiptUpdated(true);
                                                        realm.insertOrUpdate(history);
                                                    }
                                                    getView().notifyConfirmHistory(histories.indexOf(history));
                                                }
                                            });
                                        }
                                    });
                        }

                    }
                }
            }
        });
    }

    @Override
    public void onNetworkStateChanged(boolean networkConnectedFlag) {
        mNetworkConnectedFlag = networkConnectedFlag;
        if (networkConnectedFlag) {
            loadAndUpdateData();
        }
    }

    @Override
    public void onNewHistory(History history) {
        if (history.getBlockTime() != null) {
            calculateChangeInBalance(history, getInteractor().getAddresses());
            Integer notifyPosition = getInteractor().setHistory(history);
            if (notifyPosition == null) {
                getView().notifyNewHistory();
            } else {
                getView().notifyConfirmHistory(notifyPosition);
            }
        } else {
            calculateChangeInBalance(history, getInteractor().getAddresses());
            getInteractor().addToHistoryList(history);
            getView().notifyNewHistory();
        }
    }

    @Override
    public boolean getVisibility() {
        return mVisibility;
    }

    @Override
    public void updateVisibility(boolean value) {
        this.mVisibility = value;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mSubscriptionList != null) {
            mSubscriptionList.clear();
        }
    }

    public void setNetworkConnectedFlag(boolean mNetworkConnectedFlag) {
        this.mNetworkConnectedFlag = mNetworkConnectedFlag;
    }
}