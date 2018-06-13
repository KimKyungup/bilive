package org.qtum.wallet.ui.fragment.fragment_my_wallet_total;

public class MyWalletHistoryItem {

    public static final int TYPE_NONE = 0;
    public static final int TYPE_SEND = 1;
    public static final int TYPE_RECV = 2;
    public static final int TYPE_WRITE = 3;

    public int type;
    public String summary;
    public String date;
    public String value;

    public MyWalletHistoryItem() {
        type = 0;
        summary = "";
        date = "";
        value = "";
    }

    public MyWalletHistoryItem(int type, String summary, String date, String value){
        this.type = type;
        this.summary = summary;
        this.date = date;
        this.value = value;
    }
}

