package org.qtum.wallet.ui.fragment.fragment_introduce;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IntroduceFragmentPagerAdapter extends FragmentPagerAdapter {

    public IntroduceFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return IntroduceChildFragment.newInstance(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return IntroduceChildFragment.INTRODUCE_PAGE_MAX;
    }
}