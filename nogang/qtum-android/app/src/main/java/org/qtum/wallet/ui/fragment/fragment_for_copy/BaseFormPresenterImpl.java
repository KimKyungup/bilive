package org.qtum.wallet.ui.fragment.fragment_for_copy;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class BaseFormPresenterImpl extends BaseFragmentPresenterImpl implements IBaseFormPresenter {

    private IBaseFormView mFragmentView;
    private IBaseFormInteractor mFragmentInteractor;

    public BaseFormPresenterImpl(IBaseFormView fragmentView, IBaseFormInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IBaseFormView getView() {
        return mFragmentView;
    }

}
