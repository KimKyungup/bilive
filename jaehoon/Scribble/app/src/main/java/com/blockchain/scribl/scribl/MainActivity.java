package com.blockchain.scribl.scribl;

import android.animation.ValueAnimator;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MainFragmentPagerAdapter mMainFragmentPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mMainFragmentPagerAdapter = new MainFragmentPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.viewPagerMain);
        mViewPager.setAdapter(mMainFragmentPagerAdapter);

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
            View rootView = inflater.inflate(R.layout.fragment_main_write, container, false);

            final ConstraintLayout layoutMainWriteEditor = (ConstraintLayout)rootView.findViewById(R.id.layoutMainWriteEditor);

            final TextView textViewWriteOnBlockchain = (TextView)rootView.findViewById(R.id.textViewWriteOnBlockchain);
            final ImageView imageViewPlus = (ImageView)rootView.findViewById(R.id.imageViewWrite);
            textViewWriteOnBlockchain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int targetHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 228, getContext().getResources().getDisplayMetrics());
                    layoutMainWriteEditor.getLayoutParams().height = targetHeight;
                    layoutMainWriteEditor.requestLayout();

                    imageViewPlus.setRotation((float) 45.0);
                    textViewWriteOnBlockchain.setVisibility(View.INVISIBLE);
                }
            });

            return rootView;
        }
    }

    public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

        public MainFragmentPagerAdapter(FragmentManager fm) {
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
            return 1;
        }
    }
}
