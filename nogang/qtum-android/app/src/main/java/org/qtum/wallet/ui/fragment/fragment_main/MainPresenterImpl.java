package org.qtum.wallet.ui.fragment.fragment_main;

import org.qtum.wallet.ui.base.base_fragment.BaseFragmentPresenterImpl;

public class MainPresenterImpl extends BaseFragmentPresenterImpl implements IMainPresenter {

    private IMainView mFragmentView;
    private IMainInteractor mFragmentInteractor;

    private MainAction action;

    public MainPresenterImpl(IMainView fragmentView, IMainInteractor interactor) {
        mFragmentView = fragmentView;
        mFragmentInteractor = interactor;
    }

    @Override
    public IMainView getView() {
        return mFragmentView;
    }

    @Override
    public void setAction(MainAction action) {
        this.action = action;
    }

    @Override
    public void onWriteMenuSelected() {
        getView().openMainWriteMenu();
    }

    @Override
    public void onMyWalletMenuSelected() {
        getView().openMyWalletMenu();
    }

    @Override
    public void onSettingMenuSelected() {
        getView().openSettingMenu();
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        switch (action) {
            case WRITE: {    // 작성하기
                getView().openMainWriteMenu();
                break;
            }
            case MY_WALLET: {  // 내지갑
                getView().openMyWalletMenu();
                break;
            }
            case SETTING: { // 설정
                getView().openSettingMenu();
                break;
            }
        }
    }
}
