package org.qtum.wallet.ui.fragment.fragment_my_wallet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class MyWalletFragmentPagerAdapter extends FragmentPagerAdapter {

    public static final int MY_WALLET_ALL_COIN = 0;
    public static final int MY_WALLET_ETH = 1;
    public static final int MY_WALLET_QTUM = 2;
    public static final int MY_WALLET_PAGE_MAX = 3;

    private ArrayList<Fragment> mListFragments = new ArrayList<Fragment>();

    public MyWalletFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if (position < mListFragments.size()) {
            return mListFragments.get(position);
        }
        else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return mListFragments.size();
    }

    public void addFragment(Fragment fragment) {
        if (fragment != null) {
            mListFragments.add(fragment);
        }
    }

    public void clearFragments() {
        mListFragments.clear();
    }
}