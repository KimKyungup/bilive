package org.qtum.wallet.dataprovider.services.update_service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.qtum.wallet.QtumApplication;
import org.qtum.wallet.dataprovider.firebase.FirebaseSharedPreferences;
import org.qtum.wallet.dataprovider.firebase.listeners.FireBaseTokenRefreshListener;
import org.qtum.wallet.dataprovider.rest_api.qtum.QtumService;
import org.qtum.wallet.dataprovider.services.update_service.listeners.BalanceChangeListener;
import org.qtum.wallet.datastorage.QStoreStorage;
import org.qtum.wallet.datastorage.TinyDB;
import org.qtum.wallet.model.contract.ContractCreationStatus;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history.Vin;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.model.gson.qstore.PurchaseItem;
import org.qtum.wallet.model.gson.token_balance.Balance;
import org.qtum.wallet.model.gson.token_balance.TokenBalance;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.utils.BoughtContractBuilder;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.services.update_service.listeners.ContractPurchaseListener;
import org.qtum.wallet.dataprovider.services.update_service.listeners.TokenListener;
import org.qtum.wallet.dataprovider.services.update_service.listeners.TransactionListener;
import org.qtum.wallet.model.contract.Contract;
import org.qtum.wallet.model.contract.Token;
import org.qtum.wallet.dataprovider.services.update_service.listeners.TokenBalanceChangeListener;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.qstore.ContractPurchase;
import org.qtum.wallet.datastorage.KeyStorage;
import org.qtum.wallet.utils.ContractBuilder;
import org.qtum.wallet.utils.CurrentNetParams;
import org.qtum.wallet.utils.DateCalculator;
import org.qtum.wallet.utils.QtumIntent;

import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static org.qtum.wallet.WearListCallListenerService.ADDRESS;
import static org.qtum.wallet.WearListCallListenerService.BALANCE;
import static org.qtum.wallet.WearListCallListenerService.CURR_TIME_MILLS;
import static org.qtum.wallet.WearListCallListenerService.ITEMS;
import static org.qtum.wallet.WearListCallListenerService.UNC_BALANCE;

public class UpdateService extends Service implements GoogleApiClient.ConnectionCallbacks {

    private final static String TAG = "UPDATE_SERVICE";
    private final int DEFAULT_NOTIFICATION_ID = 101;
    private NotificationManager notificationManager;
    private TransactionListener mTransactionListener = null;
    private List<BalanceChangeListener> mBalanceChangeListeners = new ArrayList<>();
    private HashMap<String, List<TokenBalanceChangeListener>> mStringTokenBalanceChangeListenerHashMap = new HashMap<>();
    private HashMap<String, TokenBalance> mAllTokenBalanceList = new HashMap<>();
    private TokenListener mTokenListener;
    private ContractPurchaseListener mContractPurchaseListener;
    private boolean monitoringFlag = false;
    private Notification notification;
    private Socket socket;
    private int totalTransaction = 0;
    private JSONArray mAddresses;
    private BigDecimal unconfirmedBalance;
    private BigDecimal balance;
    private GoogleApiClient mApiClient;
    private List<String> addresses;

    public String getBalance(){
        return balance != null? balance.toString() : null;
    }

    public String getUnconfirmedBalance(){
        return unconfirmedBalance != null? unconfirmedBalance.toString() : null;
    }

    private String mFirebasePrevToken;
    private String mFirebaseCurrentToken;

    private UpdateBinder mUpdateBinder = new UpdateBinder();
    String[] firebaseTokens;

    private static UpdateService instance;

    public UpdateService getInstance(){
        return instance;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void initGoogleApiClient() {
        if(mApiClient == null) {
            mApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Wearable.API)
                    .addConnectionCallbacks(this)
                    .build();
            mApiClient.connect();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initGoogleApiClient();

        try {
            SSLContext mySSLContext = SSLContext.getInstance("TLS");
            HostnameVerifier myHostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {

                }

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }};
            mySSLContext.init(null, trustAllCerts, null);
            IO.Options opts = new IO.Options();
            opts.sslContext = mySSLContext;
            opts.hostnameVerifier = myHostnameVerifier;

            socket = IO.socket(CurrentNetParams.getUrl(), opts);
        } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }

        checkConfirmContract();
        checkPurchaseContract();

        firebaseTokens = FirebaseSharedPreferences.getInstance().getFirebaseTokens(getApplicationContext());
        mFirebasePrevToken = firebaseTokens[0];
        mFirebaseCurrentToken = firebaseTokens[1];

        FirebaseSharedPreferences.getInstance().addFirebaseTokenRefreshListener(new FireBaseTokenRefreshListener() {
            @Override
            public void onRefresh(String prevToken, String currentToken) {
                mFirebasePrevToken = prevToken;
                mFirebaseCurrentToken = currentToken;
                subscribeSocket();
            }
        });

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

                subscribeSocket();

            }
        }).on("balance_changed", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                try {
                    JSONObject data = (JSONObject) args[0];

                    try {
                        unconfirmedBalance = (new BigDecimal(data.getString("unconfirmedBalance"))).divide(new BigDecimal("100000000"), MathContext.DECIMAL128);
                        balance = (new BigDecimal(data.getString("balance"))).divide(new BigDecimal("100000000"), MathContext.DECIMAL128);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (BalanceChangeListener balanceChangeListener : mBalanceChangeListeners) {
                        balanceChangeListener.onChangeBalance(unconfirmedBalance, balance);
                    }
                    onBalanceChange();
                } catch (ClassCastException e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }).on("new_transaction", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new Gson();
                JSONObject data = (JSONObject) args[0];
                History history = gson.fromJson(data.toString(), History.class);

                if (history.getBlockTime() != null) {

                    ContractCreationStatus contractCreationStatus;
                    if(history.getContractHasBeenCreated() != null && history.getContractHasBeenCreated()){
                        contractCreationStatus = ContractCreationStatus.Created;
                    } else {
                        contractCreationStatus = ContractCreationStatus.Failed;
                    }
                    String txHash = history.getTxHash();
                    String contractAddress = ContractBuilder.generateContractAddress(txHash);

                    TinyDB tinyDB = new TinyDB(getApplicationContext());

                    boolean done = false;

                    List<Contract> contractList = tinyDB.getContractListWithoutToken();
                    for (Contract contract : contractList) {
                        if (contract.getContractAddress() != null && contract.getContractAddress().equals(contractAddress)) {
                            contract.setCreationStatus(contractCreationStatus);
                            contract.setDate(DateCalculator.getDateInFormat(history.getBlockTime() * 1000L));
                            done = true;
                            ArrayList<String> unconfirmedContractTxHashList = tinyDB.getUnconfirmedContractTxHasList();
                            unconfirmedContractTxHashList.remove(history.getTxHash());
                            tinyDB.putUnconfirmedContractTxHashList(unconfirmedContractTxHashList);
                            break;
                        }
                    }
                    tinyDB.putContractListWithoutToken(contractList);

                    if (!done) {
                        List<Token> tokenList = tinyDB.getTokenList();
                        for (Token token : tokenList) {
                            if (token.getContractAddress() != null && token.getContractAddress().equals(contractAddress)) {
                                token.setCreationStatus(contractCreationStatus);
                                token.setDate(DateCalculator.getDateInFormat(history.getBlockTime() * 1000L));
                                ArrayList<String> unconfirmedContractTxHashList = tinyDB.getUnconfirmedContractTxHasList();
                                unconfirmedContractTxHashList.remove(history.getTxHash());
                                tinyDB.putUnconfirmedContractTxHashList(unconfirmedContractTxHashList);
                                break;
                            }
                        }
                        tinyDB.putTokenList(tokenList);
                        if (mTokenListener != null) {
                            mTokenListener.newToken();
                        }
                    }

                    subscribeTokenBalanceChange(contractAddress, mFirebasePrevToken, mFirebaseCurrentToken);
                }
                if (mTransactionListener != null) {
                    mTransactionListener.onNewHistory(history);
                    if (!mTransactionListener.getVisibility()) {
                        if (history.getBlockTime() != null) {
                            totalTransaction++;
                            sendNotification(getString(R.string.new_confirmed_transaction), totalTransaction + " " + getString(R.string.new_confirmed_transaction),
                                    getString(R.string.touch_to_open_transaction_history), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                        }
                    }
                } else {
                    if (history.getBlockTime() != null) {
                        totalTransaction++;
                        sendNotification(getString(R.string.new_confirmed_transaction), totalTransaction + " " + getString(R.string.new_confirmed_transaction),
                                getString(R.string.touch_to_open_transaction_history), RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                    }
                }

            }
        }).on("token_balance_change", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new Gson();
                JSONObject data = (JSONObject) args[0];
                TokenBalance tokenBalance = gson.fromJson(data.toString(), TokenBalance.class);
                updateTokenBalance(tokenBalance);

            }
        }).on("contract_purchase", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Gson gson = new Gson();
                JSONObject data = (JSONObject) args[0];
                final ContractPurchase objectData = gson.fromJson(data.toString(), ContractPurchase.class);

                BoughtContractBuilder boughtContractBuilder = new BoughtContractBuilder();
                boughtContractBuilder.build(getApplicationContext(), objectData, new BoughtContractBuilder.ContractBuilderListener() {
                    @Override
                    public void onBuildSuccess() {
                        QStoreStorage.getInstance(getApplicationContext()).setPurchaseItemBuyStatus(objectData.getContractId(), PurchaseItem.PAID_STATUS);
                    }
                });

                if (mContractPurchaseListener != null) {
                    mContractPurchaseListener.onContractPurchased(objectData);
                }

            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d("socket", "disconnect");
            }
        });

        notificationManager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
    }

    //WEAR

    private void onBalanceChange() {
        QtumService.newInstance().getHistoryListForSeveralAddresses(addresses, 10, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<HistoryResponse>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onNext(HistoryResponse historyResponse) {
                        Gson gson = new Gson();
                        List<History> items = historyResponse.getItems();
                        for (History item : items) {
                            calculateChangeInBalance(item, addresses);
                        }
                        String s = gson.toJson(items);
                        Log.d(TAG, "onNext: HISTORY ITEMS = " + s);
                        sendData(s, balance.toString(), unconfirmedBalance.toString(), QtumApplication.instance.getWearableMessagingProvider().getAddress());
                    }
                });
    }

    private void sendData(String items, String balance, String uncBalance, String address) {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/data");
        putDataMapReq.getDataMap().putLong(CURR_TIME_MILLS, System.currentTimeMillis());
        putDataMapReq.getDataMap().putString(ITEMS, items);
        putDataMapReq.getDataMap().putString(BALANCE, balance);
        putDataMapReq.getDataMap().putString(UNC_BALANCE, uncBalance);
        putDataMapReq.getDataMap().putString(ADDRESS, address);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest().setUrgent();
        DataApi.DataItemResult await = Wearable.DataApi.putDataItem(mApiClient, putDataReq).await();
    }

    private void calculateChangeInBalance(History history, List<String> addresses) {
        BigDecimal changeInBalance = calculateVout(history, addresses).subtract(calculateVin(history, addresses));
        history.setChangeInBalance(changeInBalance);
    }

    private BigDecimal calculateVin(History history, List<String> addresses) {
        BigDecimal totalVin = new BigDecimal("0.0");
        boolean equals = false;
        for (Vin vin : history.getVin()) {
            for (String address : addresses) {
                if (vin.getAddress().equals(address)) {
                    vin.setOwnAddress(true);
                    equals = true;
                }
            }
        }
        if (equals) {
            totalVin = history.getAmount();
        }
        return totalVin;
    }

    private BigDecimal calculateVout(History history, List<String> addresses) {
        BigDecimal totalVout = new BigDecimal("0.0");
        for (Vout vout : history.getVout()) {
            for (String address : addresses) {
                if (vout.getAddress().equals(address)) {
                    vout.setOwnAddress(true);
                    totalVout = totalVout.add(vout.getValue());
                }
            }
        }
        return totalVout;
    }

    //WEAR


    private void updateTokenBalance(TokenBalance tokenBalance) {
        TokenBalance tokenBalanceFromList = mAllTokenBalanceList.get(tokenBalance.getContractAddress());
        if (tokenBalanceFromList != null) {
            for(Balance balance : tokenBalance.getBalances()) {
                for (Balance balanceFromList : tokenBalanceFromList.getBalances()) {
                    if(balance.getAddress().equals(balanceFromList.getAddress())){
                        balanceFromList.setBalance(balance.getBalance());
                    }
                }
            }
        } else {
            mAllTokenBalanceList.put(tokenBalance.getContractAddress(), tokenBalance);
        }

        List<TokenBalanceChangeListener> tokenBalanceChangeListeners = mStringTokenBalanceChangeListenerHashMap.get(tokenBalance.getContractAddress());
        if(tokenBalanceChangeListeners!=null) {
            for (TokenBalanceChangeListener tokenBalanceChangeListener : tokenBalanceChangeListeners) {
                tokenBalanceChangeListener.onBalanceChange(mAllTokenBalanceList.get(tokenBalance.getContractAddress()));
            }
        }
    }

    private void checkPurchaseContract() {
        for (final PurchaseItem purchaseItem : QStoreStorage.getInstance(getApplicationContext()).getNonPayedContracts()) {
            QtumService.newInstance()
                    .isPaidByRequestId(purchaseItem.getContractId(), purchaseItem.getRequestId())
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<ContractPurchase>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(ContractPurchase contractPurchase) {
                            if (contractPurchase.getPayedAt() != null) {
                                QStoreStorage.getInstance(getApplicationContext()).setPurchaseItemBuyStatus(purchaseItem.getContractId(), PurchaseItem.PAID_STATUS);

                                BoughtContractBuilder boughtContractBuilder = new BoughtContractBuilder();
                                boughtContractBuilder.build(getApplicationContext(), contractPurchase, new BoughtContractBuilder.ContractBuilderListener() {
                                    @Override
                                    public void onBuildSuccess() {
                                        int i = 0;
                                    }
                                });

                                if (mContractPurchaseListener != null) {
                                    mContractPurchaseListener.onContractPurchased(contractPurchase);
                                }
                            }
                        }
                    });
        }

    }

    private void checkConfirmContract() {
        final TinyDB tinyDB = new TinyDB(getApplicationContext());
        final ArrayList<String> unconfirmedContractTxHashList = tinyDB.getUnconfirmedContractTxHasList();
        for (final String unconfirmedContractTxHash : unconfirmedContractTxHashList) {
            QtumService.newInstance()
                    .getTransaction(unconfirmedContractTxHash)
                    .subscribeOn(Schedulers.io())
                    .subscribe(new Subscriber<History>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                        }

                        @Override
                        public void onNext(History history) {
                            if (history.getBlockTime() != null) {

                                String contractAddress = ContractBuilder.generateContractAddress(unconfirmedContractTxHash);

                                ContractCreationStatus contractCreationStatus;
                                if(history.getContractHasBeenCreated() != null && history.getContractHasBeenCreated()){
                                    contractCreationStatus = ContractCreationStatus.Created;
                                } else {
                                    contractCreationStatus = ContractCreationStatus.Failed;
                                }

                                boolean done = false;

                                List<Contract> contractList = tinyDB.getContractListWithoutToken();
                                for (Contract contract : contractList) {
                                    if (contract.getContractAddress() != null && contract.getContractAddress().equals(contractAddress)) {
                                        contract.setCreationStatus(contractCreationStatus);
                                        contract.setDate(DateCalculator.getDateInFormat(history.getBlockTime() * 1000L));
                                        done = true;
                                        unconfirmedContractTxHashList.remove(history.getTxHash());
                                        tinyDB.putUnconfirmedContractTxHashList(unconfirmedContractTxHashList);
                                        break;
                                    }
                                }
                                tinyDB.putContractListWithoutToken(contractList);

                                if (!done) {
                                    List<Token> tokenList = tinyDB.getTokenList();
                                    for (Token token : tokenList) {
                                        if (token.getContractAddress() != null && token.getContractAddress().equals(contractAddress)) {
                                            token.setCreationStatus(contractCreationStatus);
                                            token.setDate(DateCalculator.getDateInFormat(history.getBlockTime() * 1000L));
                                            unconfirmedContractTxHashList.remove(history.getTxHash());
                                            tinyDB.putUnconfirmedContractTxHashList(unconfirmedContractTxHashList);
                                            break;
                                        }
                                    }
                                    tinyDB.putTokenList(tokenList);
                                    if (mTokenListener != null) {
                                        mTokenListener.newToken();
                                    }
                                }

                                subscribeTokenBalanceChange(contractAddress, mFirebasePrevToken, mFirebaseCurrentToken);
                            }
                        }
                    });
        }
    }

    private void subscribeSocket() {
        subscribeBalanceChange(mFirebasePrevToken, mFirebaseCurrentToken);
        for (Contract contract : (new TinyDB(getApplicationContext())).getContractList()) {
            subscribeTokenBalanceChange(contract.getContractAddress(), mFirebasePrevToken, mFirebaseCurrentToken);
        }
        subscribeStoreContracts();
    }

    private void subscribeStoreContracts() {
        for (PurchaseItem purchaseItem : QStoreStorage.getInstance(getApplicationContext()).getNonPayedContracts()) {
            socket.emit("subscribe", "contract_purchase", purchaseItem.getRequestId());
        }
    }

    public void subscribeStoreContract(String id) {
        socket.emit("subscribe", "contract_purchase", id);
    }

    public void subscribeTokenBalanceChange(String tokenAddress) {
        subscribeTokenBalanceChange(tokenAddress, mFirebasePrevToken, mFirebaseCurrentToken);
    }

    private void subscribeTokenBalanceChange(String tokenAddress, String prevToken, String currentToken) {
        JSONObject jsonObject = new JSONObject();
        JSONObject jsonObjectToken = new JSONObject();
        try {
            jsonObject.put("contract_address", tokenAddress);
            jsonObject.put("addresses", mAddresses);

            jsonObjectToken.put("notificationToken", currentToken);
            jsonObjectToken.put("prevToken", prevToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("subscribe", "token_balance_change", jsonObject, jsonObjectToken);
    }

    private void subscribeBalanceChange(String prevToken, String currentToken) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("notificationToken", currentToken);
            jsonObject.put("prevToken", prevToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("subscribe", "balance_subscribe", mAddresses, jsonObject);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    public void startMonitoring() {
        if (!monitoringFlag) {
            mAddresses = new JSONArray();
            for (String address : getPublicAddresses()) {
                mAddresses.put(address);
            }
            socket.connect();
            monitoringFlag = true;
        }
    }

    private List<String> getPublicAddresses(){
        List<String> addresses = KeyStorage.getInstance().getAddresses();
        TinyDB tinyDB = new TinyDB(getApplicationContext());
        if(addresses == null){
            addresses = tinyDB.getPublicAddresses();
        } else {
            tinyDB.savePublicAddresses(addresses);
        }

        this.addresses = addresses;

        return addresses;
    }

    public void stopMonitoring() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("notificationToken", mFirebaseCurrentToken);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("unsubscribe", "token_balance_change", null, obj);
        socket.emit("unsubscribe", "balance_subscribe", null, obj);
        socket.disconnect();
        monitoringFlag = false;
        mAddresses = null;
        notificationManager.cancel(DEFAULT_NOTIFICATION_ID);
    }

    private void sendNotification(String Ticker, String Title, String Text, Uri sound) {

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(QtumIntent.OPEN_FROM_NOTIFICATION);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setTicker(Ticker)
                .setContentTitle(Title)
                .setContentText(Text)
                .setWhen(System.currentTimeMillis())
                .setSound(sound);

        if (android.os.Build.VERSION.SDK_INT <= 21) {
            builder.setSmallIcon(R.mipmap.ic_launcher);
        } else {
            builder.setSmallIcon(R.drawable.logo);
        }
        notification = builder.build();

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(DEFAULT_NOTIFICATION_ID, notification);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mUpdateBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    public void addTransactionListener(TransactionListener updateServiceListener) {
        mTransactionListener = updateServiceListener;
    }

    public void removeTransactionListener() {
        mTransactionListener = null;
    }

    public void addBalanceChangeListener(BalanceChangeListener balanceChangeListener) {
        mBalanceChangeListeners.add(balanceChangeListener);
        if (balance != null) {
            balanceChangeListener.onChangeBalance(unconfirmedBalance, balance);
        }
    }

    public void addTokenListener(TokenListener tokenListener) {
        mTokenListener = tokenListener;
    }

    public void removeTokenListener() {
        mTokenListener = null;
    }

    public void setContractPurchaseListener(ContractPurchaseListener contractPurchaseListener) {
        this.mContractPurchaseListener = contractPurchaseListener;
    }

    public void removeContractPurchaseListener() {
        this.mContractPurchaseListener = null;
    }

    public void addTokenBalanceChangeListener(String address, TokenBalanceChangeListener tokenBalanceChangeListener) {
        if(mStringTokenBalanceChangeListenerHashMap.get(address)==null){
            mStringTokenBalanceChangeListenerHashMap.put(address, new ArrayList<TokenBalanceChangeListener>());
        }
        mStringTokenBalanceChangeListenerHashMap.get(address).add(tokenBalanceChangeListener);
        TokenBalance tokenBalance = mAllTokenBalanceList.get(address);
        if (tokenBalance != null) {
            tokenBalanceChangeListener.onBalanceChange(tokenBalance);
        }
    }

    public TokenBalance getTokenBalance(String address) {
        return mAllTokenBalanceList.get(address);
    }

    public void removeTokenBalanceChangeListener(String address, TokenBalanceChangeListener tokenBalanceChangeListener) {
        mStringTokenBalanceChangeListenerHashMap.get(address).remove(tokenBalanceChangeListener);
    }

    public void removeBalanceChangeListener(BalanceChangeListener balanceChangeListener) {
        mBalanceChangeListeners.remove(balanceChangeListener);
    }

    public void clearNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
        totalTransaction = 0;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Wearable.MessageApi.sendMessage(mApiClient, "/app_started", "/app_started", null);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public class UpdateBinder extends Binder {
        public UpdateService getService() {
            return UpdateService.this;
        }
    }
}