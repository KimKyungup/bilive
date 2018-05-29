package org.qtum.wallet.ui.fragment.create_wallet_input_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateWalletFragment extends BaseFragment implements CreateWalletView {
    private CreateWalletPresenter mCreateWalletPresenter;
    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, CreateWalletFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_write;
    }


    @Override
    protected void createPresenter() {
        mCreateWalletPresenter = new CreateWalletPresenterImpl(this, new CreateWalletInteractorImpl(getContext()));
    }

    @Override
    protected CreateWalletPresenter getPresenter() {
        return mCreateWalletPresenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void initializeViews() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
