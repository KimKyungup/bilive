package org.qtum.wallet.ui.fragment.fragment_introduce;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentView;

public class IntroducePresenterImpl extends BaseFragmentPresenterImpl implements IntroducePresenter {

    private IntroduceView mFragmentView;
    private IntroduceInteractor mFragmentInteractor;

    public IntroducePresenterImpl(IntroduceView fragmentView, IntroduceInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IntroduceView getView() {
        return mFragmentView;
    }

}
