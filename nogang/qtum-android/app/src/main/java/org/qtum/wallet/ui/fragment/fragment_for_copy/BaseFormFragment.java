package org.qtum.wallet.ui.fragment.fragment_for_copy;

import android.content.Context;
import android.os.Bundle;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;
import org.qtum.wallet.ui.fragment_factory.Factory;

public class BaseFormFragment extends BaseFragment implements BaseFormView {

    private BaseFormPresenter mFragmentPresenter;

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, BaseFormFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new BaseFormPresenterImpl(this, new BaseFormInteractorImpl(getContext()));
    }

    @Override
    protected BaseFormPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_input_fingerprint;
    }

}
