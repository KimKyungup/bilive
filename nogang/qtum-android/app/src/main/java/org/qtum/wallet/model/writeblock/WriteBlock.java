package org.qtum.wallet.model.writeblock;

public class WriteBlock {
    String mWrite = "";
    String mBlockTime = "";
    String mBlockHash ="";

    public String getmBlockHash() {
        return mBlockHash;
    }

    public void setmBlockHash(String mBlockHash) {
        this.mBlockHash = mBlockHash;
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
