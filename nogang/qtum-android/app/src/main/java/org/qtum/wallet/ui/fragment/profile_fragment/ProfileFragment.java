package org.qtum.wallet.ui.fragment.profile_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.datastorage.listeners.LanguageChangeListener;

import org.qtum.wallet.ui.fragment.pin_fragment.PinFragment;

import org.qtum.wallet.ui.fragment.start_page_fragment.StartPageFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;

import butterknife.BindView;

import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.AUTHENTICATION_FOR_PASSPHRASE;
import static org.qtum.wallet.ui.fragment.pin_fragment.PinAction.CHANGING;

public class ProfileFragment extends BaseFragment implements ProfileView, OnSettingClickListener {

    protected PrefAdapter adapter;

    @BindView(org.qtum.wallet.R.id.pref_list)
    protected RecyclerView prefList;

    @BindView(R.id.tv_toolbar_profile)
    TextView mTextViewToolBar;

    protected ProfilePresenter mProfileFragmentPresenter;
    protected DividerItemDecoration dividerItemDecoration;
    private UpdateService mUpdateService;
    private LanguageChangeListener mLanguageChangeListener = new LanguageChangeListener() {
        @Override
        public void onLanguageChange() {
            resetText();
        }
    };

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, ProfileFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.lyt_profile_preference;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getMainActivity().setIconChecked(2);
        getPresenter().setupLanguageChangeListener(mLanguageChangeListener);
    }

    @Override
    protected void createPresenter() {
        mProfileFragmentPresenter = new ProfilePresenterImpl(this, new ProfileInteractorImpl(getContext()));
    }

    @Override
    protected ProfilePresenter getPresenter() {
        return mProfileFragmentPresenter;
    }

    @Override
    public void onSettingClick(int key) {
        BaseFragment fragment = null;
        switch (key) {
            case org.qtum.wallet.R.string.change_pin:
                fragment = PinFragment.newInstance(CHANGING, getContext());
                break;
            case org.qtum.wallet.R.string.wallet_backup:
                fragment = PinFragment.newInstance(AUTHENTICATION_FOR_PASSPHRASE, getContext());
                break;
            case org.qtum.wallet.R.string.log_out:
                setAlertDialog(getString(R.string.warning), getString(R.string.you_are_about_to_exit_your_account_all_account_data_will_be_erased_from_the_device_please_make_sure_you_have_saved_backups_of_your_passphrase_and_required_contracts), "Cancel", "Logout", PopUpType.error, new AlertDialogCallBack() {
                    @Override
                    public void onButtonClick() {
                    }

                    @Override
                    public void onButton2Click() {
                        onLogout();
                    }
                });
                break;
            default:
                break;
        }

        if (fragment != null) {
            openFragment(fragment);
        }
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        prefList.setLayoutManager(new LinearLayoutManager(getContext()));
        dividerItemDecoration = new DividerItemDecoration(getContext(), R.drawable.color_primary_divider, R.drawable.section_setting_divider, getPresenter().getSettingsData());
        showBottomNavView(R.color.colorPrimary);
        adapter = new PrefAdapter(getPresenter().getSettingsData(), this, R.layout.lyt_profile_pref_list_item);
        prefList.addItemDecoration(dividerItemDecoration);
        prefList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getPresenter().removeLanguageListener(mLanguageChangeListener);
    }

    @Override
    public void onSwitchChange(int key, boolean isChecked) {
        switch (key) {
            case org.qtum.wallet.R.string.touch_id:
                getPresenter().onTouchIdSwitched(isChecked);
                break;
            default:
                break;
        }
    }

    public void onLogout() {
        getPresenter().clearWallet();
        getMainActivity().onLogout();
        mUpdateService = getMainActivity().getUpdateService();
        mUpdateService.stopMonitoring();
        BaseFragment startPageFragment = StartPageFragment.newInstance(false, getContext());
        getMainActivity().openRootFragment(startPageFragment);
    }

    @Override
    public void startDialogFragmentForResult() {
        LogOutDialogFragment logOutDialogFragment = new LogOutDialogFragment();
        logOutDialogFragment.setTargetFragment(this, 200);
        logOutDialogFragment.show(getFragmentManager(), LogOutDialogFragment.class.getCanonicalName());
    }

    @Override
    public boolean checkAvailabilityTouchId() {
        return getMainActivity().checkAvailabilityTouchId();
    }

    @Override
    public void resetText() {
        adapter.notifyDataSetChanged();
        mTextViewToolBar.setText(R.string.setting);
    }
}
