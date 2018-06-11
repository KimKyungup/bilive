package org.qtum.wallet.ui.fragment.fragment_main_write_detail;

import android.content.Context;
import android.os.Bundle;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

public class MainWriteDetailFragment extends BaseFragment implements MainWriteDetailView {

    private MainWriteDetailPresenter mFragmentPresenter;

    public static MainWriteDetailFragment newInstance(Context context) {
        Bundle args = new Bundle();
        MainWriteDetailFragment fragment = (MainWriteDetailFragment) Factory.instantiateFragment(context, MainWriteDetailFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new MainWriteDetailPresenterImpl(this, new MainWriteDetailInteractorImpl(getContext()));
    }

    @Override
    protected MainWriteDetailPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_write_detail;
    }

}
