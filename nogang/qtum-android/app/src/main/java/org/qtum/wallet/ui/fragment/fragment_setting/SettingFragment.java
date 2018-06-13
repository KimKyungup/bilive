package org.qtum.wallet.ui.fragment.fragment_setting;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.fragment_back_up_wallet.BackUpWalletFragment;
import org.qtum.wallet.ui.fragment.fragment_import_wallet.ImportWalletFragment;
import org.qtum.wallet.ui.fragment.fragment_main.MainFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingFragment extends BaseFragment implements ISettingView {

    private ISettingPresenter mFragmentPresenter;

    @BindView(R.id.frameLayoutBtnBackUpWallet)
    FrameLayout frameLayoutBtnBackUpWallet;

    @BindView(R.id.frameLayoutBtnImportWallet)
    FrameLayout frameLayoutBtnImportWallet;

    @BindView(R.id.frameLayoutBtnRefresh)
    FrameLayout frameLayoutBtnRefresh;

    @BindView(R.id.frameLayoutBtnChangePassword)
    FrameLayout frameLayoutBtnChangePassword;

    @BindView(R.id.frameLayoutBtnFingerprint)
    FrameLayout frameLayoutBtnFingerprint;

    @BindView(R.id.frameLayoutBtnLogout)
    FrameLayout frameLayoutBtnLogout;

    @BindView(R.id.imageViewFingerprintOnOff)
    ImageView imageViewFingerprintOnOff;

    private boolean fingerprintState = false;

    public static SettingFragment newInstance() {
        Bundle args = new Bundle();
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new SettingPresenterImpl(this, new SettingInteractorImpl(getContext()));
    }

    @Override
    protected ISettingPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_setting;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        ((MainFragment) getParentFragment()).hideTopMenu();
    }

    private void setFingerprintState(boolean state) {
        fingerprintState = state;

        if (state) {
            imageViewFingerprintOnOff.setImageResource(R.drawable.tumbler_on);
        }
        else {
            imageViewFingerprintOnOff.setImageResource(R.drawable.tumbler_off);
        }
    }

    private void toggleFingerprintState() {
        if (fingerprintState) {
            fingerprintState = false;
            imageViewFingerprintOnOff.setImageResource(R.drawable.tumbler_off);
        }
        else {
            fingerprintState = true;
            imageViewFingerprintOnOff.setImageResource(R.drawable.tumbler_on);
        }
    }

    private void openBackUpWalletFragment() {
        BackUpWalletFragment fragment = BackUpWalletFragment.newInstance(getContext());
        ((MainActivity)getActivity()).openFragment(fragment);
    }

    private void openImportWalletFragment() {
        ImportWalletFragment fragment = ImportWalletFragment.newInstance(getContext());
        ((MainActivity)getActivity()).openFragment(fragment);
    }

    @OnClick({R.id.frameLayoutBtnBackUpWallet, R.id.frameLayoutBtnImportWallet, R.id.frameLayoutBtnRefresh, R.id.frameLayoutBtnChangePassword, R.id.frameLayoutBtnFingerprint, R.id.frameLayoutBtnLogout, R.id.imageViewFingerprintOnOff})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.frameLayoutBtnBackUpWallet: {
                Toast.makeText(getContext(), "[Debug Toast] frameLayoutBtnBackUpWallet", Toast.LENGTH_SHORT).show();
                openBackUpWalletFragment();
                break;
            }
            case R.id.frameLayoutBtnImportWallet: {
                Toast.makeText(getContext(), "[Debug Toast] frameLayoutBtnImportWallet", Toast.LENGTH_SHORT).show();
                openImportWalletFragment();
                break;
            }
            case R.id.frameLayoutBtnRefresh: {
                Toast.makeText(getContext(), getString(R.string.setting_refresh_complete), Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.frameLayoutBtnChangePassword: {
                Toast.makeText(getContext(), "[Debug Toast] frameLayoutBtnChangePassword", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.frameLayoutBtnFingerprint: {
                Toast.makeText(getContext(), "[Debug Toast] frameLayoutBtnFingerprint", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.frameLayoutBtnLogout: {
                Toast.makeText(getContext(), "[Debug Toast] frameLayoutBtnLogout", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.imageViewFingerprintOnOff: {
                Toast.makeText(getContext(), "[Debug Toast] imageViewFingerprintOnOff", Toast.LENGTH_SHORT).show();
                toggleFingerprintState();
                break;
            }
        }
    }
}
