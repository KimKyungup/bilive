package org.qtum.wallet.ui.fragment.fragment_main_my_wallet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.qtum.wallet.R;

import butterknife.BindView;

public class MainMyWalletChildFragment extends Fragment {

    public static final int MY_WALLET_ALL_COIN = 0;
    public static final int MY_WALLET_ETH = 1;
    public static final int MY_WALLET_QTUM = 2;
    public static final int MY_WALLET_PAGE_MAX = 3;

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "SECTION_NUMBER";

    public MainMyWalletChildFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainMyWalletChildFragment newInstance(int sectionNumber) {
        MainMyWalletChildFragment fragment = new MainMyWalletChildFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        int targetLayoutResource = R.layout.fragment_main_my_wallet_all;

        switch (sectionNumber) {
            case MY_WALLET_ALL_COIN:
                targetLayoutResource = R.layout.fragment_main_my_wallet_all;
                break;
            case MY_WALLET_ETH:
                targetLayoutResource = R.layout.fragment_main_my_wallet_coin;
                break;
            case MY_WALLET_QTUM:
                targetLayoutResource = R.layout.fragment_main_my_wallet_tbd;
                break;
        }

        View rootView = inflater.inflate(targetLayoutResource, container, false);

        return rootView;
    }
}

