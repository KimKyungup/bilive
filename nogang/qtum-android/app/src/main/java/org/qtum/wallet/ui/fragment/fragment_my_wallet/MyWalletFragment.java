package org.qtum.wallet.ui.fragment.fragment_my_wallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.fragment_main.MainFragment;
import org.qtum.wallet.ui.fragment.fragment_my_wallet_one_coin.MyWalletOneCoinFragment;
import org.qtum.wallet.ui.fragment.fragment_my_wallet_tbd.MyWalletTBDFragment;
import org.qtum.wallet.ui.fragment.fragment_my_wallet_total.MyWalletTotalFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class MyWalletFragment extends BaseFragment implements IMyWalletView {

    private IMyWalletPresenter mFragmentPresenter;

    private MyWalletFragmentPagerAdapter mFragmentPagerAdapter;

    private MyWalletTotalFragment myWalletTotalFragment = null;
    private MyWalletOneCoinFragment myWalletOneCoinFragment = null;
    private MyWalletTBDFragment myWalletTBDFragment = null;

    @BindView(R.id.viewPagerMyWallet)
    ViewPager viewPagerMyWallet;

    public static MyWalletFragment newInstance() {
        Bundle args = new Bundle();
        MyWalletFragment fragment = new MyWalletFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new MyWalletPresenterImpl(this, new MyWalletInteractorImpl(getContext()));
    }

    @Override
    protected IMyWalletPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_my_wallet;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        if (myWalletTotalFragment == null) {
            myWalletTotalFragment = MyWalletTotalFragment.newInstance(getContext());
        }

        if (myWalletOneCoinFragment == null) {
            myWalletOneCoinFragment = MyWalletOneCoinFragment.newInstance(getContext());
        }

        if (myWalletTBDFragment == null) {
            myWalletTBDFragment = MyWalletTBDFragment.newInstance(getContext());
        }

        // Init Pager Adapter
        mFragmentPagerAdapter = new MyWalletFragmentPagerAdapter(getChildFragmentManager());
        mFragmentPagerAdapter.addFragment(myWalletTotalFragment);
        mFragmentPagerAdapter.addFragment(myWalletOneCoinFragment);
        mFragmentPagerAdapter.addFragment(myWalletTBDFragment);

        // Assign Adapter
        viewPagerMyWallet.setAdapter(mFragmentPagerAdapter);
        viewPagerMyWallet.setOffscreenPageLimit(mFragmentPagerAdapter.getCount() - 1); // View Pager가 모든 항목을 미리 Load 해놓기 위함이며, 개수가 3개라 상관 없음

        // Page 변경 이벤트 등록
        viewPagerMyWallet.addOnPageChangeListener(onPageChangeListener);

        // 밑줄을 [전체]에 긋게 한다.
        ((MainFragment) getParentFragment()).showTopMenu();
        ((MainFragment) getParentFragment()).selectAllCoin();
    }

    public void setAllCoinPage() {
        viewPagerMyWallet.setCurrentItem(MyWalletChildFragment.MY_WALLET_ALL_COIN, true);
    }

    public void setAllETHPage() {
        viewPagerMyWallet.setCurrentItem(MyWalletChildFragment.MY_WALLET_ETH, true);
    }

    public void setAllQTUMPage() {
        viewPagerMyWallet.setCurrentItem(MyWalletChildFragment.MY_WALLET_QTUM, true);
    }

    private void updateTopMenu(int position) {

        switch (position) {
            case MyWalletChildFragment.MY_WALLET_ALL_COIN: {
                ((MainFragment) getParentFragment()).selectAllCoin();
                break;
            }
            case MyWalletChildFragment.MY_WALLET_ETH: {
                ((MainFragment) getParentFragment()).selectETH();
                break;
            }
            case MyWalletChildFragment.MY_WALLET_QTUM: {
                ((MainFragment) getParentFragment()).selectQTUM();
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
