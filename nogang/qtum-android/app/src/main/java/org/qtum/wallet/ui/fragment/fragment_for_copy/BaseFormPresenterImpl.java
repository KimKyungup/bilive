package org.qtum.wallet.ui.fragment.fragment_for_copy;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class BaseFormPresenterImpl extends BaseFragmentPresenterImpl implements BaseFormPresenter {

    private BaseFormView mFragmentView;
    private BaseFormInteractor mFragmentInteractor;

    public BaseFormPresenterImpl(BaseFormView fragmentView, BaseFormInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public BaseFormView getView() {
        return mFragmentView;
    }

}
