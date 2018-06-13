package org.qtum.wallet.ui.fragment.fragment_scribble;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class ScribblePresenterImpl extends BaseFragmentPresenterImpl implements IScribblePresenter {

    private IScribbleView mFragmentView;
    private IScribbleInteractor mFragmentInteractor;

    public ScribblePresenterImpl(IScribbleView fragmentView, IScribbleInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IScribbleView getView() {
        return mFragmentView;
    }

    @Override
    public void onAllPostSelected() {

    }

    @Override
    public void onMyPostSelected() {

    }

    @Override
    public void onWriteComplete() {

    }
}
