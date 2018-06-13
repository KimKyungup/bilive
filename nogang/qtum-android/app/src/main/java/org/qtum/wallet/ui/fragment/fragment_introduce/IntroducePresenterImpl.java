package org.qtum.wallet.ui.fragment.fragment_introduce;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class IntroducePresenterImpl extends BaseFragmentPresenterImpl implements IIntroducePresenter {

    private IIntroduceView mFragmentView;
    private IIntroduceInteractor mFragmentInteractor;

    public IntroducePresenterImpl(IIntroduceView fragmentView, IIntroduceInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IIntroduceView getView() {
        return mFragmentView;
    }

}
