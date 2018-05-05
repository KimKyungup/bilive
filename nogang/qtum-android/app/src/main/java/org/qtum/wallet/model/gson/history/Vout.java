package org.qtum.wallet.model.gson.history;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class Vout extends RealmObject implements TransactionInfo {

    @SerializedName("value")
    @Expose
    @Ignore
    private BigDecimal value;
    @SerializedName("address")
    @Expose
    @PrimaryKey
    private String address;
    @SerializedName("scriptPubKey")
    @Expose
    @Ignore
    private String scriptPubKey;

    private boolean isOwnAddress = false;

    /**
     * Constructor for unit testing
     */
    public Vout() {
    }

    /**
     * Constructor for unit testing
     */
    public Vout(String address) {
        this.address = address;
    }

    public boolean isOwnAddress() {
        return isOwnAddress;
    }

    public void setOwnAddress(boolean ownAddress) {
        isOwnAddress = ownAddress;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPubKey() {
        return scriptPubKey;
    }

    public void setPubKey(String scriptPubKey) {
        this.scriptPubKey = scriptPubKey;
    }
}
