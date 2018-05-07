package com.scribble.scribble.ui.activity.main_activity;

import android.content.Context;

//import com.scribble.scribble.dataprovider.rest_api.qtum.QtumService;
//import com.scribble.scribble.datastorage.HistoryList;
//import com.scribble.scribble.datastorage.KeyStorage;
//import com.scribble.scribble.datastorage.QtumSettingSharedPreference;
//import com.scribble.scribble.datastorage.QtumSharedPreference;
//import com.scribble.scribble.datastorage.listeners.LanguageChangeListener;
//import com.scribble.scribble.model.gson.DGPInfo;
//import com.scribble.scribble.model.gson.FeePerKb;

import java.math.BigDecimal;

import rx.Observable;

class MainActivityInteractorImpl implements MainActivityInteractor {

    private Context mContext;

//    private boolean isDGPInfoLoaded = false;
//    private boolean isFeePerkbLoaded = false;

    MainActivityInteractorImpl(Context context) {
        mContext = context;
    }

//    @Override
//    public boolean getKeyGeneratedInstance() {
//        return QtumSharedPreference.getInstance().getKeyGeneratedInstance(mContext);
//    }
//
//    @Override
//    public void clearStatic() {
//        KeyStorage.getInstance().clearKeyStorage();
//        HistoryList.getInstance().clearHistoryList();
//    }
//
//    @Override
//    public Observable<DGPInfo> getDGPInfo() {
//        return QtumService.newInstance().getDGPInfo();
//    }
//
//    @Override
//    public boolean isDGPInfoLoaded() {
//        return isDGPInfoLoaded;
//    }
//
//    @Override
//    public boolean isFeePerkbLoaded() {
//        return isFeePerkbLoaded;
//    }
//
//    @Override
//    public void addLanguageChangeListener(LanguageChangeListener languageChangeListener) {
//        QtumSharedPreference.getInstance().addLanguageListener(languageChangeListener);
//    }
//
//    @Override
//    public void removeLanguageChangeListener(LanguageChangeListener languageChangeListener) {
//        QtumSharedPreference.getInstance().removeLanguageListener(languageChangeListener);
//    }
//
//    @Override
//    public Observable<FeePerKb> getFeePerKb() {
//        return QtumService.newInstance().getEstimateFeePerKb(2);
//    }
//
//    @Override
//    public void setDGPInfo(DGPInfo dgpInfo) {
//        isDGPInfoLoaded = true;
//        QtumSharedPreference.getInstance().setMinGasPrice(mContext, dgpInfo.getMingasprice());
//    }
//
//    @Override
//    public void setFeePerKb(FeePerKb feePerKb) {
//        isFeePerkbLoaded = true;
//        QtumSettingSharedPreference qtumSettingSharedPreference = new QtumSettingSharedPreference();
//        qtumSettingSharedPreference.setFeePerKb(mContext, feePerKb.getFeePerKb().setScale(5, BigDecimal.ROUND_HALF_DOWN).toPlainString());
//    }
}
