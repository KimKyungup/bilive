package org.qtum.wallet.ui.fragment.fragment_scribble_detail;

import android.content.Context;
import android.util.Log;

import org.qtum.wallet.datastorage.WriteList;
import org.qtum.wallet.model.writeblock.WriteBlock;

import java.util.ArrayList;

public class ScribbleDetailInteractorImpl implements IScribbleDetailInteractor {

    private Context mContext;

    ScribbleDetailInteractorImpl(Context context) {
        mContext = context;
    }

    @Override
    public String getBody(int index) {
        return WriteList.getInstance().getWriteList().get(index).getWrite();
    }

    @Override
    public String getTXHash(int index) {
        return WriteList.getInstance().getWriteList().get(index).getTXHash();
    }

    @Override
    public String getBlockTime(int index) {
        return WriteList.getInstance().getWriteList().get(index).getBlockTime();
    }
}
