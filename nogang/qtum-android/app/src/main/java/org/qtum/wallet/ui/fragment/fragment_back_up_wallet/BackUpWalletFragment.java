package org.qtum.wallet.ui.fragment.fragment_back_up_wallet;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public class BackUpWalletFragment extends BaseFragment implements IBackUpWalletView {

    private IBackUpWalletPresenter mFragmentPresenter;

    @BindView(R.id.imageViewBackArrow)
    ImageView imageViewBackArrow;

    @BindView(R.id.textViewKeywords)
    TextView textViewKeywords;

    @BindView(R.id.buttonCopyToClipboard)
    Button buttonCopyToClipboard;

    public static BackUpWalletFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BackUpWalletFragment fragment = (BackUpWalletFragment) Factory.instantiateFragment(context, BackUpWalletFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new BackUpWalletPresenterImpl(this, new BackUpWalletInteractorImpl(getContext()));
    }

    @Override
    protected IBackUpWalletPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_back_up_wallet;
    }

    @OnClick({R.id.imageViewBackArrow, R.id.buttonCopyToClipboard,})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBackArrow: {
                getFragmentManager().popBackStack();
                break;
            }
            case R.id.buttonCopyToClipboard: {
                Toast.makeText(getContext(), getString(R.string.back_up_wallet_copy_to_clipboard_complete), Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
