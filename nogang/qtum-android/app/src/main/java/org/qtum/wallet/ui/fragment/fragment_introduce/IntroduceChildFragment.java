package org.qtum.wallet.ui.fragment.fragment_introduce;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.R;

public class IntroduceChildFragment extends Fragment {

    public static final int INTRODUCE_PAGE_1 = 0;
    public static final int INTRODUCE_PAGE_2 = 1;
    public static final int INTRODUCE_PAGE_3 = 2;
    public static final int INTRODUCE_PAGE_MAX = 3;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "SECTION_NUMBER";

    public IntroduceChildFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static IntroduceChildFragment newInstance(int sectionNumber) {
        IntroduceChildFragment fragment = new IntroduceChildFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        int targetLayoutResource = R.layout.fragment_introduce_page_1;

        switch (sectionNumber) {
            case INTRODUCE_PAGE_1:
                targetLayoutResource = R.layout.fragment_introduce_page_1;
                break;
            case INTRODUCE_PAGE_2:
                targetLayoutResource = R.layout.fragment_introduce_page_2;
                break;
            case INTRODUCE_PAGE_3:
                targetLayoutResource = R.layout.fragment_introduce_page_3;
                break;
        }

        View rootView = inflater.inflate(targetLayoutResource, container, false);

        return rootView;
    }
}

