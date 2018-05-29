package org.qtum.wallet.ui.fragment.create_wallet_input_fragment;

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
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.model.news.RssFeed;
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

public class CreateWalletInteractorImpl implements CreateWalletInteractor {

    private Context mContext;

    public CreateWalletInteractorImpl(Context context) {
        mContext = context;
    }
}
