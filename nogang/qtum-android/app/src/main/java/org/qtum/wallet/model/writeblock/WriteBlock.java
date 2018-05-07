package org.qtum.wallet.model.writeblock;

public class WriteBlock {
    String mWrite = "";
    String mBlockTime = "";
    String mTXHash ="";

    public String getTXHash() {
        return mTXHash;
    }

    public void setTXHash(String mTXHash) {
        this.mTXHash = mTXHash;
    }

    public String getWrite() {
        return mWrite;
    }
    public void setWrite(String write) {
        mWrite =  write;
    }
    public String getBlockTime() {
        return mBlockTime;
    }
    public void setBlockTime(String blockTime) {
        mBlockTime = blockTime;
    }
}
