package org.qtum.wallet.ui.fragment.fragment_main_my_wallet;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MainMyWalletFragmentPagerAdapter extends FragmentPagerAdapter {

    public MainMyWalletFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return MainMyWalletChildFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return MainMyWalletChildFragment.MY_WALLET_PAGE_MAX;
    }
}