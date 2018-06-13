package org.qtum.wallet.ui.fragment.fragment_my_wallet_send_coin;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public class MyWalletSendCoinFragment extends BaseFragment implements IMyWalletSendCoinView {

    private IMyWalletSendCoinPresenter mFragmentPresenter;

    @BindView(R.id.imageViewBackArrow)
    ImageView imageViewBackArrow;

    @BindView(R.id.linearLayouBtntRecentUsed)
    LinearLayout linearLayouBtntRecentUsed;

    @BindView(R.id.linearLayoutBtnCodeScan)
    LinearLayout linearLayoutBtnCodeScan;

    @BindView(R.id.buttonSend)
    Button buttonSend;

    public static MyWalletSendCoinFragment newInstance(Context context) {
        Bundle args = new Bundle();
        MyWalletSendCoinFragment fragment = (MyWalletSendCoinFragment) Factory.instantiateFragment(context, MyWalletSendCoinFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new MyWalletSendCoinPresenterImpl(this, new MyWalletSendCoinInteractorImpl(getContext()));
    }

    @Override
    protected IMyWalletSendCoinPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_wallet_send_coin;
    }

    @OnClick({R.id.imageViewBackArrow, R.id.linearLayouBtntRecentUsed, R.id.linearLayoutBtnCodeScan, R.id.buttonSend, })
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBackArrow: {
                getFragmentManager().popBackStack();
                break;
            }
            case R.id.linearLayouBtntRecentUsed: {
                Toast.makeText(getContext(), "[Debug Toast] MyWalletSendCoinFragment=linearLayouBtntRecentUsed", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.linearLayoutBtnCodeScan: {
                Toast.makeText(getContext(), "[Debug Toast] MyWalletSendCoinFragment=linearLayoutBtnCodeScan", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.buttonSend: {
                Toast.makeText(getContext(), "[Debug Toast] MyWalletSendCoinFragment=buttonSend", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
