package org.qtum.wallet.ui.fragment.fragment_scribble;

import android.util.Log;

import org.qtum.wallet.datastorage.WriteList;
import org.qtum.wallet.model.gson.UnspentOutput;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.model.gson.history.HistoryResponse;
import org.qtum.wallet.model.gson.history.Vout;
import org.qtum.wallet.model.gson.history_ether.Transaction;
import org.qtum.wallet.model.gson.history_ether.TxListResponse;
import org.qtum.wallet.model.writeblock.WriteBlock;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.utils.DateCalculator;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.internal.util.SubscriptionList;
import rx.schedulers.Schedulers;

public class ScribblePresenterImpl extends BaseFragmentPresenterImpl implements IScribblePresenter {

    private IScribbleView mFragmentView;
    private IScribbleInteractor mFragmentInteractor;
    private boolean mNetworkConnectedFlag = false;
    private String mWriteText;
    private SubscriptionList mSubscriptionList = new SubscriptionList();

    public ScribblePresenterImpl(IScribbleView fragmentView, IScribbleInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getInteractor().unSubscribe();
    }

    @Override
    public IScribbleView getView() {
        return mFragmentView;
    }

    private IScribbleInteractor getInteractor() {
        return mFragmentInteractor;
    }

    @Override
    public void onAllPostSelected() {

    }

    @Override
    public void onMyPostSelected() {

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

    @Override
    public void write() {
        mWriteText = getView().getWriteText();
        String s = mWriteText.toString();

        while(s.getBytes().length < 5){
            s += " ";
        }

        String text = hexToString(s);
        if (false) {
            qtumCreateTx(text, "494048b0fed64a1acfc111111111111111111111", "", 200000, 40, "0.01");
        }
        else {
            EtherCreateTx(text, "0xAf44747484436cc65327794cD1B12f085bea618a", "", 200000, 40, "0.01");
        }
    }

    @Override
    public void loadAndUpdateWrite() {
        getView().startRefreshAnimation();

        if(false) {
            loadAndWriteQtuem();
        } else {
            loadAndWriteEther();
        }
    }

    @Override
    public void onLastItem(int currentItemCount) {
        getView().loadNewWrite();
        if (false){
            mSubscriptionList.add(getQtumHistoryResponse(currentItemCount));
        } else {
            mSubscriptionList.add(getEtherHistoryResponse(currentItemCount));
        }
    }

    private void EtherCreateTx(final String text, final String contractAddress, String senderAddress, final int gasLimit, final int gasPrice, final String fee) {
        getInteractor().createEtherTransactionHash(text, contractAddress, gasLimit, gasPrice, fee)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().dismissProgressDialog();
                        getView().setAlertDialog(org.qtum.wallet.R.string.error, e.toString(), "Ok", BaseFragment.PopUpType.error);
                    }

                    @Override
                    public void onNext(String result) {
                        Log.d("result", result);
                        if (!result.equals("")) {
                            getView().getSendTransactionCallback().onSuccess();
                        }
                    }
                });
    }

    private void qtumCreateTx(final String abiParams, final String contractAddress, String senderAddress, final int gasLimit, final int gasPrice, final String fee) {

        getInteractor().getUnspentOutputs(senderAddress, new ScribbleInteractorImpl.GetUnspentListCallBack() {
            @Override
            public void onSuccess(List<UnspentOutput> unspentOutputs) {
                String txHex = getInteractor().qtumCreateTransactionHash(abiParams, contractAddress, unspentOutputs, gasLimit, gasPrice, fee);
                //getView().tost(txHex);
                getInteractor().qtumSendTx(txHex, getView().getSendTransactionCallback());
            }

            @Override
            public void onError(String error) {
                getView().dismissProgressDialog();
                getView().setAlertDialog(org.qtum.wallet.R.string.error, error, "Ok", BaseFragment.PopUpType.error);
            }
        });
    }

    private Subscription getEtherHistoryResponse(final int currentItemCount){
        return getInteractor().getHistoryResponseEther("0xAf44747484436cc65327794cD1B12f085bea618a", 2, currentItemCount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TxListResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TxListResponse historyResponse) {
                        ArrayList<WriteBlock> writeBlocks = getWriteBlockList(historyResponse);
                        WriteList.getInstance().addAll(writeBlocks);

                        getView().addHistory(currentItemCount, WriteList.getInstance().getWriteList().size() - currentItemCount + 1,
                                WriteList.getInstance().getWriteList());
                        //initTransactionReceipt(historyResponse.getItems());

                    }
                });
    }

    private Subscription getQtumHistoryResponse(final int currentItemCount){
        return getInteractor().getHistoryResponseQtum("qQEhVZZFoW8Ao5K1sRf8Z81yvVsx5vVJvQ", 25, currentItemCount)
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
                });
    }

    private void loadAndWriteQtuem() {
        getInteractor().getHistoryResponseQtum("qQEhVZZFoW8Ao5K1sRf8Z81yvVsx5vVJvQ",25, 0)
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

    private void loadAndWriteEther() {
        getInteractor().getHistoryResponseEther("0xAf44747484436cc65327794cD1B12f085bea618a",1, 10)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TxListResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TxListResponse historyResponse) {

                        WriteList mWriteList = WriteList.getInstance();
                        ArrayList<WriteBlock> writeBlockList = new ArrayList<>();

                        Log.d("ehter log", historyResponse.getTxList().get(0).getHash());
                        //Log.d("nogang", historyResponse.getItems().get(0).getTxHash());

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

    private ArrayList<WriteBlock> getWriteBlockList(TxListResponse  historyResponse){
        ArrayList<WriteBlock> writeBlockList = new ArrayList<>();

        for (Transaction history : historyResponse.getTxList()) {

            if (history.getTo().equals("0xaf44747484436cc65327794cd1b12f085bea618a")) {
                String s = history.getInput();
                if(s.equals("0x")) continue;

                s = s.substring(2);
                Log.d("post", hexToUTF8(s));
                WriteBlock writeBlcok = new WriteBlock();
                writeBlcok.setWrite(hexToUTF8(s));
                try{
                    writeBlcok.setBlockTime(DateCalculator.getShortDate(history.getTimeStamp() * 1000L));
                    writeBlcok.setTXHash(history.getHash());
                }catch(Exception e) {
                    writeBlcok.setBlockTime("unconfirmed");
                    writeBlcok.setTXHash("");
                }
                writeBlockList.add(writeBlcok);
            }

        }

        return writeBlockList;
    }

    private String hexToString(String s) {
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

    private String hexToUTF8(String hex) {
        byte[] bytes;
        bytes = new BigInteger(hex, 16).toByteArray();
        return new String(bytes, Charset.forName("UTF-8"));
    }
}
