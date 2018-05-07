package org.qtum.wallet.datastorage;

import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.writeblock.WriteBlock;

import java.util.ArrayList;
import java.util.List;

public class WriteList {
    private static WriteList sWriteList;

    private ArrayList<WriteBlock> mWriteList;
    private int mTotalItem = 0;

    private WriteList() {
        mWriteList = new ArrayList<>();
    }

    public static WriteList getInstance() {
        if (sWriteList == null) {
            sWriteList = new WriteList();
        }
        return sWriteList;
    }

    public void clearHistoryList() {
        sWriteList = null;
    }

    public ArrayList<WriteBlock> getWriteList() {
        return mWriteList;
    }

    public void setWriteList(ArrayList<WriteBlock> historyList) {
        mWriteList = historyList;
        mTotalItem = historyList.size();
    }

    public int getTotalItem() {
        return mTotalItem;
    }

    public void setTotalItem(int totalItem) {
        this.mTotalItem = totalItem;
    }

    public void add(WriteBlock wrteBlock) {
        mWriteList.add(wrteBlock);
        mTotalItem++;
    }

    public void addAll(ArrayList<WriteBlock> writeBlockArrayList){
        mWriteList.addAll(writeBlockArrayList);
        mTotalItem += writeBlockArrayList.size();
    }
}