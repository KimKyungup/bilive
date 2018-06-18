package org.qtum.wallet.ui.fragment.fragment_my_wallet_total;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;

public class MyWalletTotalFragment extends BaseFragment implements IMyWalletTotalView, RecyclerViewMyWalletAdapter.OnItemClickListener{

    private IMyWalletTotalPresenter mFragmentPresenter;

    @BindView(R.id.swipeRefreshLayoutMyWallet)
    SwipeRefreshLayout swipeRefreshLayoutMyWallet;

    @BindView(R.id.recyclerViewMyWallet)
    RecyclerView recyclerViewMyWallet;

    private RecyclerViewMyWalletAdapter myWalletAdapter = null;

    public static MyWalletTotalFragment newInstance(Context context) {
        Bundle args = new Bundle();
        MyWalletTotalFragment fragment = (MyWalletTotalFragment) Factory.instantiateFragment(context, MyWalletTotalFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new MyWalletTotalPresenterImpl(this, new MyWalletTotalInteractorImpl(getContext()));
    }

    @Override
    protected IMyWalletTotalPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_wallet_total;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        swipeRefreshLayoutMyWallet.setOnRefreshListener(onRefreshListener);

        myWalletAdapter = new RecyclerViewMyWalletAdapter(getContext());
        myWalletAdapter.setOnItemClickListener(this);

        recyclerViewMyWallet.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewMyWallet.setAdapter(myWalletAdapter);

        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_SEND, "이더리움 보냄", "2018년 4월 27일, 오전 10시 17분", "-35ETH");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_RECV, "퀀텀 받음", "2018년 4월 27일, 오전 10시 17분", "+35QTUM");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "퀀텀으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035QTUM");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "이더리움으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035ETH");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_SEND, "이더리움 보냄", "2018년 4월 27일, 오전 10시 17분", "-35ETH");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_RECV, "퀀텀 받음", "2018년 4월 27일, 오전 10시 17분", "+35QTUM");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "퀀텀으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035QTUM");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "이더리움으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035ETH");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_SEND, "이더리움 보냄", "2018년 4월 27일, 오전 10시 17분", "-35ETH");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_RECV, "퀀텀 받음", "2018년 4월 27일, 오전 10시 17분", "+35QTUM");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "퀀텀으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035QTUM");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "이더리움으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035ETH");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_SEND, "이더리움 보냄", "2018년 4월 27일, 오전 10시 17분", "-35ETH");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_RECV, "퀀텀 받음", "2018년 4월 27일, 오전 10시 17분", "+35QTUM");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "퀀텀으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035QTUM");
        myWalletAdapter.addItem(MyWalletHistoryItem.TYPE_WRITE, "이더리움으로 글 기록", "2018년 4월 27일, 오전 10시 17분", "-0.035ETH");
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Handler testHandler = new Handler();
            testHandler.postDelayed(testRunnable, 1000);
        }
    };

    @Override
    public void onItemClick(View view, int pos) {
        Toast.makeText(getContext(), "[Debug Toast] MyWalletTotalFragment=onItemClickListener(row=" + String.valueOf(pos) + ")", Toast.LENGTH_SHORT).show();
    }

    Runnable testRunnable = new Runnable() {
        @Override
        public void run() {
            swipeRefreshLayoutMyWallet.setRefreshing(false);
        }
    };

}
