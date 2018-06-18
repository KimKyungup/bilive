package org.qtum.wallet.ui.fragment.fragment_scribble_detail;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class ScribbleDetailPresenterImpl extends BaseFragmentPresenterImpl implements IScribbleDetailPresenter {

    private IScribbleDetailView mFragmentView;
    private IScribbleDetailInteractor mFragmentInteractor;

    private int targetIndex;

    public ScribbleDetailPresenterImpl(IScribbleDetailView fragmentView, IScribbleDetailInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IScribbleDetailView getView() {
        return mFragmentView;
    }

    public IScribbleDetailInteractor getInteractor() {
        return mFragmentInteractor;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        getView().setBody(getInteractor().getBody(targetIndex));

    }

    @Override
    public void setIndex(int index) {
        targetIndex = index;
    }

    @Override
    public void onBackButtonClicked() {

    }

    @Override
    public void onInfoButtonClicked() {

    }

    @Override
    public void onShareButtonClicked() {
        StringBuilder builder = new StringBuilder();
        builder.append(getInteractor().getTXHash(targetIndex));
        builder.append("\n\n");
        builder.append(getInteractor().getBlockTime(targetIndex));
        builder.append("\n\n");
        builder.append(getInteractor().getBody(targetIndex));
        getView().showShareMenu(builder.toString());
    }
}
