package org.qtum.wallet.utils.migration_manager;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class Contract94 implements Serializable {

    @SerializedName("contractAddress")
    protected String mContractAddress;
    @SerializedName("uiid")
    protected String mUiid;
    @SerializedName("contractName")
    protected String mContractName;
    @SerializedName("hasBeenCreated")
    protected Boolean mHasBeenCreated;
    @SerializedName("date")
    protected String mDate;
    @SerializedName("senderAddress")
    protected String mSenderAddress;
    @SerializedName("isSubscribe")
    protected boolean mIsSubscribe;

    public Contract94(String contractAddress, String uiid, Boolean hasBeenCreated, String date, String senderAddress, String contractName){
        this.mContractAddress = contractAddress;
        this.mUiid = uiid;
        this.mHasBeenCreated = hasBeenCreated;
        this.mContractName = contractName;
        this.mDate = date;
        this.mSenderAddress = senderAddress;
        this.mIsSubscribe = false;
    }

    public String getContractAddress() {
        return mContractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.mContractAddress = contractAddress;
    }

    public String getUiid() {
        return mUiid;
    }

    public void setUiid(String uiid) {
        this.mUiid = uiid;
    }

    public Boolean isHasBeenCreated() {
        return mHasBeenCreated;
    }

    public void setHasBeenCreated(Boolean hasBeenCreated) {
        this.mHasBeenCreated = hasBeenCreated;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public String getContractName() {
        return mContractName;
    }

    public void setContractName(String contractName) {
        this.mContractName = contractName;
    }

    public String getSenderAddress() {
        return mSenderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.mSenderAddress = senderAddress;
    }

    public boolean isSubscribe() {
        return mIsSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        mIsSubscribe = subscribe;
    }
}
