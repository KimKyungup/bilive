package org.qtum.wallet.ui.fragment.fragment_input_fingerprint;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenter;
import org.qtum.wallet.ui.fragment.fragment_main.MainAction;
import org.qtum.wallet.ui.fragment.fragment_main.MainFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public class InputFingerprintFragment extends BaseFragment implements InputFingerprintView {

    private InputFingerprintPresenter mFragmentPresenter;

    @BindView(R.id.buttonRegisterLater)
    Button buttonRegisterLater;

    @BindView(R.id.buttonRegisterAllow)
    Button buttonRegisterAllow;

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, InputFingerprintFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new InputFingerprintPresenterImpl(this, new InputFingerprintInteractorImpl(getContext()));
    }

    @Override
    protected InputFingerprintPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_input_fingerprint;
    }

    @Override
    public void openMainFragment() {
        Fragment fragment = MainFragment.newInstance(getContext(), MainAction.WRITE);
        openRootFragment(fragment);
    }

    @OnClick({R.id.buttonRegisterLater, R.id.buttonRegisterAllow})
    void onClick(View view) {
        switch (view.getId()) {
            // 나중에
            case R.id.buttonRegisterLater: {
                getPresenter().registerLater();
                break;
            }
            // 허용
            case R.id.buttonRegisterAllow: {
                getPresenter().allowFingerPrint();
                break;
            }
        }
    }
}
