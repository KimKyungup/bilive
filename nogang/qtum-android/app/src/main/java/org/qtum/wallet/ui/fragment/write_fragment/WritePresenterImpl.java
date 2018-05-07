package org.qtum.wallet.ui.fragment.write_fragment;

import android.util.Log;

import org.qtum.wallet.datastorage.HistoryList;
import org.qtum.wallet.datastorage.WriteList;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.model.writeblock.WriteBlock;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.utils.DateCalculator;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

public class WritePresenterImpl extends BaseFragmentPresenterImpl implements WritePresenter {

    private WriteView mWriteFragmentView;
    private WriteInteractor mWriteFragmentInteractor;
    private boolean mNetworkConnectedFlag = false;
    private String mWriteText;
    private SubscriptionList mSubscriptionList = new SubscriptionList();

    public WritePresenterImpl(WriteView writeFragmentView, WriteInteractor writeInteractor) {
        mWriteFragmentView = writeFragmentView;
        mWriteFragmentInteractor = writeInteractor;
    }

    public void write() {
        mWriteText = mWriteFragmentView.getWriteText();
        //createTx(Hex.encode(mWriteText.getBytes()).toString(), "qQajXaHWgZL9puf3z2h5KLsEapsCJvH91w", "qQajXaHWgZL9puf3z2h5KLsEapsCJvH91w", 2000000, 100000, "0.1");
        String s = mWriteText.toString();

        while(s.getBytes().length < 5){
            s += " ";
        }

        String text = hexToString(s);

        //createTx(text, "494048b0fed64a1acfc390038aa2ad54d27ab1de", "qQajXaHWgZL9puf3z2h5KLsEapsCJvH91w", 200000, 40, "0.1");
        //createTx(text, "494048b0fed64a1acfc111111111111111111111", "qQajXaHWgZL9puf3z2h5KLsEapsCJvH91w", 200000, 40, "0.01");
        createTx(text, "494048b0fed64a1acfc111111111111111111111", "", 200000, 40, "0.01");


    }

    public String hexToString(String s) {
        try {
            byte[] bytes = new String(s).getBytes("utf-8");
            StringBuilder stringBuilder = new StringBuilder();

            for (byte b : bytes) {
                stringBuilder.append(String.format("%02x", b & 0xff));
            }
            return stringBuilder.toString();
        }
        catch  (java.io.UnsupportedEncodingException e) {
            return "";
        }
    }

    public String hexToUTF8(String hex) {
        byte[] bytes;
        bytes = new BigInteger(hex, 16).toByteArray();
        return new String(bytes, Charset.forName("UTF-8"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getInteractor().unSubscribe();
    }

    private WriteInteractor getInteractor() {
        return mWriteFragmentInteractor;
    }

    @Override
    public WriteView getView() {
        return mWriteFragmentView;
    }

    @Override
    public void onRefresh() {
        if (mNetworkConnectedFlag) {
            loadAndUpdateWrite();
        } else {
            getView().setAlertDialog(org.qtum.wallet.R.string.no_internet_connection,
                    org.qtum.wallet.R.string.please_check_your_network_settings,
                    org.qtum.wallet.R.string.ok,
                    BaseFragment.PopUpType.error);
            getView().stopRefreshRecyclerAnimation();
        }
    }

    @Override
    public void onNetworkStateChanged(boolean networkConnectedFlag) {

        mNetworkConnectedFlag = networkConnectedFlag;
        if (networkConnectedFlag) {
            loadAndUpdateWrite();
        } else {
            //getView().updateWriteBlocks(getInteractor().getNewses());
            //NewsStorage.newInstance().setNewses(getInteractor().getNewses());
        }

    }

    private void createTx(final String abiParams, final String contractAddress, String senderAddress, final int gasLimit, final int gasPrice, final String fee) {

        getInteractor().getUnspentOutputs(senderAddress, new WriteInteractorImpl.GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutput> unspentOutputs) {
                String txHex = getInteractor().createTransactionHash(abiParams, contractAddress, unspentOutputs, gasLimit, gasPrice, fee);
                //getView().tost(txHex);
                getInteractor().sendTx(txHex, getView().getSendTransactionCallback());
            }

            @Override
            public void onError(String error) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(org.qtum.wallet.R.string.error, error, "Ok", BaseFragment.PopUpType.error);
            }
        });
    }

    @Override
    public void onLastItem(final int currentItemCount){

        getView().loadNewWrite();
        mSubscriptionList.add(getInteractor().getHistoryResponse("qQEhVZZFoW8Ao5K1sRf8Z81yvVsx5vVJvQ", 25, currentItemCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HistoryResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HistoryResponse historyResponse) {
                        ArrayList<WriteBlock> writeBlocks = getWriteBlockList(historyResponse);
                        WriteList.getInstance().addAll(writeBlocks);

                        getView().addHistory(currentItemCount, WriteList.getInstance().getWriteList().size() - currentItemCount + 1,
                                WriteList.getInstance().getWriteList());
                        //initTransactionReceipt(historyResponse.getItems());

                    }
                }));


    }
    @Override
    public void loadAndUpdateWrite() {
        getView().startRefreshAnimation();
        getInteractor().getHistoryResponse("qQEhVZZFoW8Ao5K1sRf8Z81yvVsx5vVJvQ",25, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HistoryResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HistoryResponse historyResponse) {

                        WriteList mWriteList = WriteList.getInstance();
                        ArrayList<WriteBlock> writeBlockList = new ArrayList<>();

                        Log.d("nogang", historyResponse.getItems().get(0).getTxHash());

                        writeBlockList = getWriteBlockList(historyResponse);
                        mWriteList.setWriteList(writeBlockList);
                        getView().updateWriteBlocks(mWriteList.getWriteList());
                    }
                });

    }

    private ArrayList<WriteBlock> getWriteBlockList(HistoryResponse historyResponse){
        ArrayList<WriteBlock> writeBlockList = new ArrayList<>();

        for (History history : historyResponse.getItems()) {
            for(Vout vout : history.getVout()){
                if (vout.getAddress().equals("qQEhVZZFoW8Ao5K1sRf8Z81yvVsx5vVJvQ")) {
                    String[] s = vout.getPubKey().split(" ");
                    Log.d("post", hexToUTF8(s[3]));
                    WriteBlock writeBlcok = new WriteBlock();
                    writeBlcok.setWrite(hexToUTF8(s[3]));
                    try{
                        writeBlcok.setBlockTime(DateCalculator.getShortDate(history.getBlockTime() * 1000L));
                        writeBlcok.setTXHash(history.getTxHash());
                    }catch(Exception e) {
                        writeBlcok.setBlockTime("unconfirmed");
                        writeBlcok.setTXHash("");
                    }
                    writeBlockList.add(writeBlcok);
                }
            }
        }

        return writeBlockList;
    }
}
