package org.qtum.wallet.ui.fragment.wallet_main_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.ViewGroup;

import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;

import org.qtum.wallet.ui.fragment.wallet_fragment.WalletFragment;

import butterknife.BindView;

public abstract class WalletMainFragment extends BaseFragment implements WalletMainView {

    private WalletFragment mWalletFragment;

    public static WalletMainFragment newInstance(Context context) {
        Bundle args = new Bundle();
        WalletMainFragment fragment = (WalletMainFragment) Factory.instantiateFragment(context, WalletMainFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    private WalletMainPresenter mWalletMainPresenter;

    @BindView(org.qtum.wallet.R.id.view_pager)
    protected
    ViewPager pager;

    @Override
    protected void createPresenter() {
        mWalletMainPresenter = new WalletMainPresenterImpl(this, new WalletMainInteractorImpl(getContext()));
    }

    @Override
    protected WalletMainPresenter getPresenter() {
        return mWalletMainPresenter;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        pager.setAdapter(new FragmentAdapter(getFragmentManager()));
        showBottomNavView(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMainActivity().setIconChecked(1);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public class FragmentAdapter extends FragmentStatePagerAdapter {

        int NUM_ITEMS = 1;

        private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        public WalletFragment getWalletFragment() {
            return (WalletFragment) registeredFragments.get(0);
        }

        public FragmentAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            //Todo : viewPager 구현
            switch (position) {
                case 0:
                    mWalletFragment = (WalletFragment) WalletFragment.newInstance(getContext());
                    return mWalletFragment;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWalletFragment != null) {
            mWalletFragment.dismiss();
        }
    }
}
