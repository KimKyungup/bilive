package com.blockchain.scribl.scribl;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class IntroduceActivity extends AppCompatActivity {

    private IntroduceFragmentPagerAdapter mIntroduceFragmentPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mIntroduceFragmentPagerAdapter = new IntroduceFragmentPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewPagerIntroduce);
        mViewPager.setAdapter(mIntroduceFragmentPagerAdapter);

        Intent intent = new Intent(IntroduceActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
            int targetLayoutResource = R.layout.fragment_intoduce_page_1;

            switch (sectionNumber) {
                case 1:
                    targetLayoutResource = R.layout.fragment_intoduce_page_1;
                    break;
                case 2:
                    targetLayoutResource = R.layout.fragment_intoduce_page_2;
                    break;
                case 3:
                    targetLayoutResource = R.layout.fragment_intoduce_page_3;
                    break;
            }

            View rootView = inflater.inflate(targetLayoutResource, container, false);

            return rootView;
        }
    }

    public class IntroduceFragmentPagerAdapter extends FragmentPagerAdapter {

        public IntroduceFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }
}
