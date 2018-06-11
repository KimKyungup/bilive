package org.qtum.wallet.ui.fragment.fragment_main;

import android.content.Context;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.fragment_main_my_wallet.MainMyWalletFragment;
import org.qtum.wallet.ui.fragment.fragment_main_my_wallet_send.MainMyWalletSendFragment;
import org.qtum.wallet.ui.fragment.fragment_main_setting.MainSettingFragment;
import org.qtum.wallet.ui.fragment.fragment_main_write.MainWriteFragment;
import org.qtum.wallet.ui.fragment.fragment_main_write_detail.MainWriteDetailFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public class MainFragment extends BaseFragment implements MainView, IMainBottomBarManager, IMainTopBarManager, IMainChildFragmentManager {

    private final static String ACTION = "action";

    private MainPresenter mFragmentPresenter;

    private MainWriteFragment mMainWriteFragment = null;
    private MainMyWalletFragment mMainMyWalletFragment = null;
    private MainSettingFragment mMainSettingFragment = null;

    @BindView(R.id.frameLayoutBottomBarWrite)
    FrameLayout frameLayoutBottomBarWrite;

    @BindView(R.id.frameLayoutBottomBarMyWallet)
    FrameLayout frameLayoutBottomBarMyWallet;

    @BindView(R.id.frameLayoutBottomBarSetting)
    FrameLayout frameLayoutBottomBarSetting;

    @BindView(R.id.imageViewBottomBarWrite)
    ImageView imageViewBottomBarWrite;

    @BindView(R.id.imageViewBottomBarMyWallet)
    ImageView imageViewBottomBarMyWallet;

    @BindView(R.id.imageViewBottomBarSetting)
    ImageView imageViewBottomBarSetting;

    @BindView(R.id.textViewBottomBarWrite)
    TextView textViewBottomBarWrite;

    @BindView(R.id.textViewBottomBarMyWallet)
    TextView textViewBottomBarMyWallet;

    @BindView(R.id.textViewBottomBarSetting)
    TextView textViewBottomBarSetting;

    @BindView(R.id.layoutMainBottomBar)
    ConstraintLayout layoutMainBottomBar;

    // Top Bar, Top Menu
    @BindView(R.id.textViewTopbarAllCoins)
    TextView textViewTopbarAllCoins;

    @BindView(R.id.textViewTopbarAbbrivationETH)
    TextView textViewTopbarAbbrivationETH;

    @BindView(R.id.textViewTopbarAbbrivationQTUM)
    TextView textViewTopbarAbbrivationQTUM;

    @BindView(R.id.viewTopbarUnderlineAllCoins)
    View viewTopbarUnderlineAllCoins;

    @BindView(R.id.viewTopbarUnderlineETH)
    View viewTopbarUnderlineETH;

    @BindView(R.id.viewTopbarUnderlineQTUM)
    View viewTopbarUnderlineQTUM;


    public static BaseFragment newInstance(Context context, MainAction action) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, MainFragment.class);
        args.putSerializable(ACTION, action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new MainPresenterImpl(this, new MainInteractorImpl(getContext()));
    }

    @Override
    protected MainPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        // 어떤 Action으로 Fragment를 실행할지 결정
        getPresenter().setAction((MainAction) getArguments().getSerializable(ACTION));

    }

     @Override
    public void openMainWriteMenu() {
         writeMenuSelect();
         openMainWriteFragment();
         hideTopMenu();
    }

    @Override
    public void openMyWalletMenu() {
        myWalletMenuSelect();
        openMyWalletFragment();
        showTopMenu();
    }

    @Override
    public void openSettingMenu() {
        settingMenuSelect();
        openSettingFragment();
        hideTopMenu();

        // Only for test
        openMainMyWalletSendFragment();
    }

    @Override
    public void openMainWriteDetailFragment() {
        MainWriteDetailFragment fragment = MainWriteDetailFragment.newInstance(getContext());
        openFragment(fragment);
    }

    @Override
    public void openMainMyWalletSendFragment() {
        MainMyWalletSendFragment fragment = MainMyWalletSendFragment.newInstance(getContext());
        openFragment(fragment);
    }

    @Override
    public void showBottomBar() {
        layoutMainBottomBar.setVisibility(View.VISIBLE);
        layoutMainBottomBar.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.introduce_start_button_enter));
    }

    @Override
    public void selectAllCoin() {
        viewTopbarUnderlineAllCoins.setVisibility(View.VISIBLE);
        viewTopbarUnderlineETH.setVisibility(View.INVISIBLE);
        viewTopbarUnderlineQTUM.setVisibility(View.INVISIBLE);
    }

    @Override
    public void selectETH() {
        viewTopbarUnderlineAllCoins.setVisibility(View.INVISIBLE);
        viewTopbarUnderlineETH.setVisibility(View.VISIBLE);
        viewTopbarUnderlineQTUM.setVisibility(View.INVISIBLE);
    }

    @Override
    public void selectQTUM() {
        viewTopbarUnderlineAllCoins.setVisibility(View.INVISIBLE);
        viewTopbarUnderlineETH.setVisibility(View.INVISIBLE);
        viewTopbarUnderlineQTUM.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomBar() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.introduce_start_button_exit);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 애니메이션이 종료되면 Button을 숨긴다.
                layoutMainBottomBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        layoutMainBottomBar.startAnimation(animation);
    }

    private void openMainWriteFragment() {
        if (mMainWriteFragment == null) {
            mMainWriteFragment = MainWriteFragment.newInstance();
            mMainWriteFragment.setMainBottomBarManager(this);
            mMainWriteFragment.setMainChildFragmentManager(this);
        }

        getChildFragmentManager()
                .beginTransaction()
                .replace(org.qtum.wallet.R.id.fragment_container_main, mMainWriteFragment, mMainWriteFragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    private void openMyWalletFragment() {
        if (mMainMyWalletFragment == null) {
            mMainMyWalletFragment = MainMyWalletFragment.newInstance();
            mMainMyWalletFragment.setMainTopBarManager(this);
            mMainMyWalletFragment.setMainChildFragmentManager(this);
        }

        getChildFragmentManager()
                .beginTransaction()
                .replace(org.qtum.wallet.R.id.fragment_container_main, mMainMyWalletFragment, mMainMyWalletFragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    private void openSettingFragment() {
        if (mMainSettingFragment == null) {
            mMainSettingFragment = MainSettingFragment.newInstance();
        }

        getChildFragmentManager()
                .beginTransaction()
                .replace(org.qtum.wallet.R.id.fragment_container_main, mMainSettingFragment, mMainSettingFragment.getClass().getCanonicalName())
                .addToBackStack(null)
                .commit();
    }

    private void writeMenuSelect()
    {
        imageViewBottomBarWrite.setImageResource(R.drawable.btbar_write_selected);
        imageViewBottomBarMyWallet.setImageResource(R.drawable.btbar_wallet);
        imageViewBottomBarSetting.setImageResource(R.drawable.btbar_setting);

        textViewBottomBarWrite.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_normal));
        textViewBottomBarMyWallet.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_disabled));
        textViewBottomBarSetting.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_disabled));

    }

    private void myWalletMenuSelect()
    {
        imageViewBottomBarWrite.setImageResource(R.drawable.btbar_write);
        imageViewBottomBarMyWallet.setImageResource(R.drawable.btbar_wallet_selected);
        imageViewBottomBarSetting.setImageResource(R.drawable.btbar_setting);

        textViewBottomBarWrite.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_disabled));
        textViewBottomBarMyWallet.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_normal));
        textViewBottomBarSetting.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_disabled));
    }

    private void settingMenuSelect()
    {
        imageViewBottomBarWrite.setImageResource(R.drawable.btbar_write);
        imageViewBottomBarMyWallet.setImageResource(R.drawable.btbar_wallet);
        imageViewBottomBarSetting.setImageResource(R.drawable.btbar_setting_selected);

        textViewBottomBarWrite.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_disabled));
        textViewBottomBarMyWallet.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_disabled));
        textViewBottomBarSetting.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_normal));
    }

    private void showTopMenu()
    {
        textViewTopbarAllCoins.setVisibility(View.VISIBLE);
        textViewTopbarAbbrivationETH.setVisibility(View.VISIBLE);
        textViewTopbarAbbrivationQTUM.setVisibility(View.VISIBLE);
        viewTopbarUnderlineAllCoins.setVisibility(View.INVISIBLE);
        viewTopbarUnderlineETH.setVisibility(View.INVISIBLE);
        viewTopbarUnderlineQTUM.setVisibility(View.INVISIBLE);
    }

    private void hideTopMenu()
    {
        textViewTopbarAllCoins.setVisibility(View.INVISIBLE);
        textViewTopbarAbbrivationETH.setVisibility(View.INVISIBLE);
        textViewTopbarAbbrivationQTUM.setVisibility(View.INVISIBLE);
        viewTopbarUnderlineAllCoins.setVisibility(View.INVISIBLE);
        viewTopbarUnderlineETH.setVisibility(View.INVISIBLE);
        viewTopbarUnderlineQTUM.setVisibility(View.INVISIBLE);
    }

    @OnClick({R.id.frameLayoutBottomBarWrite, R.id.frameLayoutBottomBarMyWallet, R.id.frameLayoutBottomBarSetting, R.id.textViewTopbarAllCoins, R.id.textViewTopbarAbbrivationETH, R.id.textViewTopbarAbbrivationQTUM,})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.frameLayoutBottomBarWrite: {
                getPresenter().onWriteMenuSelected();
                break;
            }
            case R.id.frameLayoutBottomBarMyWallet: {
                getPresenter().onMyWalletMenuSelected();
                break;
            }
            case R.id.frameLayoutBottomBarSetting: {
                getPresenter().onSettingMenuSelected();
                break;
            }
            case R.id.textViewTopbarAllCoins: {
                mMainMyWalletFragment.setAllCoinPage();
                break;
            }
            case R.id.textViewTopbarAbbrivationETH: {
                mMainMyWalletFragment.setAllETHPage();
                break;
            }
            case R.id.textViewTopbarAbbrivationQTUM: {
                mMainMyWalletFragment.setAllQTUMPage();
                break;
            }
        }
    }
}
