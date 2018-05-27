package org.qtum.wallet.datastorage;

import android.content.Context;
import android.util.Log;

import com.google.common.collect.ImmutableList;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.qtum.wallet.utils.DictionaryWords;
import org.qtum.wallet.utils.CurrentNetParams;
import org.qtum.wallet.utils.EtherWallet;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class KeyStorage implements Serializable {

    private static KeyStorage sKeyStorage;
    private List<DeterministicKey> mDeterministicKeyList;
    private List<String> mAddressesList;
    private Wallet sQtumWallet = null;
    private int sCurrentKeyPosition = 0;
    private final int ADDRESSES_COUNT = 10;
    String walletAddress;

    public static KeyStorage getInstance() {
        if (sKeyStorage == null) {
            sKeyStorage = new KeyStorage();
        }
        return sKeyStorage;
    }

    private KeyStorage() {
    }

    public void setWallet(Wallet wallet) {
        this.sQtumWallet = wallet;
    }

    public void clearKeyStorage() {
        sKeyStorage = null;
    }

    public Observable<String> createWallet(final String seedString) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

                String passphrase = "";
                DeterministicSeed seed = null;
                try {
                    seed = new DeterministicSeed(seedString, null, passphrase, DeterministicHierarchy.BIP32_STANDARDISATION_TIME_SECS);

                } catch (UnreadableWalletException e) {
                    e.printStackTrace();
                }
                if (seed != null) {
                    sQtumWallet = Wallet.fromSeed(CurrentNetParams.getNetParams(), seed);
                    try {
                        File dir = new File("");
                        walletAddress = EtherWallet.generateNewWalletFile("1234", dir, true);
                    }
                    catch(Exception e){
                        Log.d("EtherWallet", e.toString());
                    }
                }
                getKeyList();
                subscriber.onNext(seedString);
            }
        });
    }

    public String getRandomSeed(){
        String mnemonicCode = "";
        for (int i = 0; i < 11; i++) {
            mnemonicCode += DictionaryWords.getRandomWord() + " ";
        }
        mnemonicCode += DictionaryWords.getRandomWord();
        return mnemonicCode;
    }

    public List<DeterministicKey> getKeyList() {
        if (mDeterministicKeyList == null || mDeterministicKeyList.isEmpty()) {
            mDeterministicKeyList = new ArrayList<>(ADDRESSES_COUNT);
            mAddressesList = new ArrayList<>();
            List<ChildNumber> pathParent = new ArrayList<>();
            pathParent.add(new ChildNumber(88, true));
            pathParent.add(new ChildNumber(0, true));
            for (int i = 0; i < ADDRESSES_COUNT; i++) {
                ImmutableList<ChildNumber> path = HDUtils.append(pathParent, new ChildNumber(i, true));
                DeterministicKey k = sQtumWallet.getActiveKeyChain().getKeyByPath(path, true);
                mDeterministicKeyList.add(k);
                mAddressesList.add(k.toAddress(CurrentNetParams.getNetParams()).toString());
            }
        }
        return mDeterministicKeyList;
    }

    public String getCurrentAddress() {
        return getKeyList().get(sCurrentKeyPosition).toAddress(CurrentNetParams.getNetParams()).toString();
    }

    public List<String> getAddresses() {
        return mAddressesList;
    }

    public DeterministicKey getCurrentKey() {
        return getKeyList().get(sCurrentKeyPosition);
    }

    public void setCurrentKeyPosition(int currentKeyPosition) {
        sCurrentKeyPosition = currentKeyPosition;
    }

    public int getCurrentKeyPosition() {
        return sCurrentKeyPosition;
    }


}