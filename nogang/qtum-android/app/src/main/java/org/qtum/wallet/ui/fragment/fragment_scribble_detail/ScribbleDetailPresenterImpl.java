package org.qtum.wallet.ui.fragment.fragment_scribble_detail;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class ScribbleDetailPresenterImpl extends BaseFragmentPresenterImpl implements IScribbleDetailPresenter {

    private IScribbleDetailView mFragmentView;
    private IScribbleDetailInteractor mFragmentInteractor;

    public ScribbleDetailPresenterImpl(IScribbleDetailView fragmentView, IScribbleDetailInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IScribbleDetailView getView() {
        return mFragmentView;
    }

}
