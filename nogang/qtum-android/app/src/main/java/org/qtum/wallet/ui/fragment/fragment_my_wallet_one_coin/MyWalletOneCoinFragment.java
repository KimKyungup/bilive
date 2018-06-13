package org.qtum.wallet.ui.fragment.fragment_my_wallet_one_coin;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.fragment_my_wallet_send_coin.MyWalletSendCoinFragment;
import org.qtum.wallet.ui.fragment.fragment_my_wallet_total.ListViewMyWalletAdapter;
import org.qtum.wallet.ui.fragment.fragment_my_wallet_total.MyWalletHistoryItem;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public class MyWalletOneCoinFragment extends BaseFragment implements IMyWalletOneCoinView {

    private IMyWalletOneCoinPresenter mFragmentPresenter;

    @BindView(R.id.linearLayoutButtonSend)
    LinearLayout linearLayoutButtonSend;

    @BindView(R.id.linearLayoutButtonReceive)
    LinearLayout linearLayoutButtonReceive;

    @BindView(R.id.linearLayoutButtonCodeScan)
    LinearLayout linearLayoutButtonCodeScan;

    @BindView(R.id.swipeRefreshLayoutMyWallet)
    SwipeRefreshLayout swipeRefreshLayoutMyWallet;

    @BindView(R.id.listViewMyWallet)
    ListView listViewMyWallet;

    private ListViewMyWalletAdapter listViewMyWalletAdapter = null;

    public static MyWalletOneCoinFragment newInstance(Context context) {
        Bundle args = new Bundle();
        MyWalletOneCoinFragment fragment = (MyWalletOneCoinFragment) Factory.instantiateFragment(context, MyWalletOneCoinFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new MyWalletOneCoinPresenterImpl(this, new MyWalletOneCoinInteractorImpl(getContext()));
    }

    @Override
    protected IMyWalletOneCoinPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_wallet_one_coin;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        swipeRefreshLayoutMyWallet.setOnRefreshListener(onRefreshListener);

        listViewMyWalletAdapter = new ListViewMyWalletAdapter(getContext());
        listViewMyWallet.setAdapter(listViewMyWalletAdapter);
        listViewMyWallet.setOnItemClickListener(onItemClickListener);

        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_SEND, "이더리움 보냄", "2018년 4월 27일, 오전 10시 17분", "-35ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_RECV, "이더리움 받음", "2018년 4월 27일, 오전 10시 17분", "+35ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "이더리움으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "이더리움으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_SEND, "이더리움 보냄", "2018년 4월 27일, 오전 10시 17분", "-35ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_RECV, "이더리움 받음", "2018년 4월 27일, 오전 10시 17분", "+35ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "이더리움으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "이더리움으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_SEND, "이더리움 보냄", "2018년 4월 27일, 오전 10시 17분", "-35ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_RECV, "이더리움 받음", "2018년 4월 27일, 오전 10시 17분", "+35ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "이더리움으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "이더리움으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_SEND, "이더리움 보냄", "2018년 4월 27일, 오전 10시 17분", "-35ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_RECV, "이더리움 받음", "2018년 4월 27일, 오전 10시 17분", "+35ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "이더리움으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035ETH");
        listViewMyWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "이더리움으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035ETH");
    }

    private void openMyWalletSendCoinFragment() {
        MyWalletSendCoinFragment fragment = MyWalletSendCoinFragment.newInstance(getContext());
        ((MainActivity)getActivity()).openFragment(fragment);
    }

    @OnClick({R.id.linearLayoutButtonSend, R.id.linearLayoutButtonReceive, R.id.linearLayoutButtonCodeScan, })
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayoutButtonSend: {
                Toast.makeText(getContext(), "[Debug Toast] linearLayoutButtonSend", Toast.LENGTH_SHORT).show();
                openMyWalletSendCoinFragment();
                break;
            }
            case R.id.linearLayoutButtonReceive: {
                Toast.makeText(getContext(), "[Debug Toast] linearLayoutButtonReceive", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.linearLayoutButtonCodeScan: {
                Toast.makeText(getContext(), "[Debug Toast] linearLayoutButtonCodeScan", Toast.LENGTH_SHORT).show();
                break;
            }

        }
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Handler testHandler = new Handler();
            testHandler.postDelayed(testRunnable, 1000);
        }
    };

    ListView.OnItemClickListener onItemClickListener = new ListView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Toast.makeText(getContext(), "[Debug Toast] MyWalletOneCoinFragment=onItemClickListener", Toast.LENGTH_SHORT).show();
        }
    };

    Runnable testRunnable = new Runnable() {
        @Override
        public void run() {
            swipeRefreshLayoutMyWallet.setRefreshing(false);
        }
    };

}
