package org.qtum.wallet.ui.fragment.fragment_main_my_wallet;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.fragment_main.IMainChildFragmentManager;
import org.qtum.wallet.ui.fragment.fragment_main.IMainTopBarManager;

import butterknife.BindView;

public class MainMyWalletFragment extends BaseFragment implements MainMyWalletView {

    private MainMyWalletPresenter mFragmentPresenter;
    private IMainTopBarManager mainTopBarManager;
    private IMainChildFragmentManager mainChildFragmentManager;

    private MainMyWalletFragmentPagerAdapter mFragmentPagerAdapter;

    @BindView(R.id.viewPagerMyWallet)
    ViewPager viewPagerMyWallet;

    public static MainMyWalletFragment newInstance() {
        Bundle args = new Bundle();
        MainMyWalletFragment fragment = new MainMyWalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new MainMyWalletPresenterImpl(this, new MainMyWalletInteractorImpl(getContext()));
    }

    @Override
    protected MainMyWalletPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_my_wallet;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        // Init Pager Adapter
        mFragmentPagerAdapter = new MainMyWalletFragmentPagerAdapter(getChildFragmentManager());

        // Assign Adapter
        viewPagerMyWallet.setAdapter(mFragmentPagerAdapter);

        // Page 변경 이벤트 등록
        viewPagerMyWallet.addOnPageChangeListener(onPageChangeListener);

        // 밑줄을 [전체]에 긋게 한다.
        mainTopBarManager.selectAllCoin();

    }

    public void setMainTopBarManager(IMainTopBarManager mainTopBarManager) {
        this.mainTopBarManager = mainTopBarManager;
    }

    public void setMainChildFragmentManager(IMainChildFragmentManager mainChildFragmentManager) {
        this.mainChildFragmentManager = mainChildFragmentManager;
    }

    public void setAllCoinPage() {
        viewPagerMyWallet.setCurrentItem(MainMyWalletChildFragment.MY_WALLET_ALL_COIN, true);
    }

    public void setAllETHPage() {
        viewPagerMyWallet.setCurrentItem(MainMyWalletChildFragment.MY_WALLET_ETH, true);
    }

    public void setAllQTUMPage() {
        viewPagerMyWallet.setCurrentItem(MainMyWalletChildFragment.MY_WALLET_QTUM, true);
    }

    private void updateTopMenu(int position) {

        switch (position) {
            case MainMyWalletChildFragment.MY_WALLET_ALL_COIN: {
                mainTopBarManager.selectAllCoin();
                break;
            }
            case MainMyWalletChildFragment.MY_WALLET_ETH: {
                mainTopBarManager.selectETH();
                break;
            }
            case MainMyWalletChildFragment.MY_WALLET_QTUM: {
                mainTopBarManager.selectQTUM();
                break;
            }
        }
    }

    // View Pager 관련 이벤트 리스너
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Do nothing
        }

        @Override
        public void onPageSelected(int position) {
            updateTopMenu(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Do nothing
        }
    };


}
