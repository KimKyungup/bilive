package org.qtum.wallet.ui.fragment.fragment_main_my_wallet_send;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.fragment_main.IMainChildFragmentManager;
import org.qtum.wallet.ui.fragment_factory.Factory;

public class MainMyWalletSendFragment extends BaseFragment implements MainMyWalletSendView {

    private MainMyWalletSendPresenter mFragmentPresenter;
//    private IMainChildFragmentManager mainChildFragmentManager;
//
//    @BindView(R.id.linearLayoutButtonSend)
//    LinearLayout linearLayoutButtonSend;
//
//    @BindView(R.id.linearLayoutButtonReceive)
//    LinearLayout linearLayoutButtonReceive;
//
//    @BindView(R.id.linearLayoutButtonCodeScan)
//    LinearLayout linearLayoutButtonCodeScan;

    public static MainMyWalletSendFragment newInstance(Context context) {
        Bundle args = new Bundle();
        MainMyWalletSendFragment fragment = (MainMyWalletSendFragment) Factory.instantiateFragment(context, MainMyWalletSendFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new MainMyWalletSendPresenterImpl(this, new MainMyWalletSendInteractorImpl(getContext()));
    }

    @Override
    protected MainMyWalletSendPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_my_wallet_send;
    }

//    public void setMainChildFragmentManager(IMainChildFragmentManager mainChildFragmentManager) {
//        this.mainChildFragmentManager = mainChildFragmentManager;
//    }
//
//    @OnClick({R.id.linearLayoutButtonSend, R.id.linearLayoutButtonReceive, R.id.linearLayoutButtonCodeScan, })
//    void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.linearLayoutButtonSend: {
//                mainChildFragmentManager.openMainMyWalletSendFragment();
//                break;
//            }
//            case R.id.linearLayoutButtonReceive: {
//                break;
//            }
//            case R.id.linearLayoutButtonCodeScan: {
//                break;
//            }
//        }
//    }
}
