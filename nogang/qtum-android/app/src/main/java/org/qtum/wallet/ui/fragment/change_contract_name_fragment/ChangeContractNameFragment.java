package org.qtum.wallet.ui.fragment.change_contract_name_fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.my_contracts_fragment.MyContractsFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public abstract  class ChangeContractNameFragment extends BaseFragment implements ChangeContractNameView{

    ChangeContractNamePresenter mChangeContractNamePresenter;
    private static final String CURRENT_CONTRACT_NAME = "current_contract_name";
    private static final String POSITION = "position";

    @BindView(R.id.til_contract_name)
    TextInputLayout mTextInputLayoutContractName;
    @BindView(R.id.et_contract_name)
    EditText mEditTextContractName;

    @OnClick({R.id.ibt_back, R.id.bt_change})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
            case R.id.bt_change:
                String newContractName = mEditTextContractName.getText().toString();
                if(!newContractName.equals(getArguments().getString(CURRENT_CONTRACT_NAME))){
                    ((MyContractsFragment)getTargetFragment()).onRenameContract(getArguments().getInt(POSITION),newContractName);
                }
                getActivity().onBackPressed();
                break;
        }
    }

    public static BaseFragment newInstance(Context context, String currentContractName, int position) {

        Bundle args = new Bundle();

        BaseFragment fragment = Factory.instantiateFragment(context, ChangeContractNameFragment.class);
        args.putInt(POSITION, position);
        args.putString(CURRENT_CONTRACT_NAME, currentContractName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mTextInputLayoutContractName.setHint(getArguments().getString(CURRENT_CONTRACT_NAME));
    }

    @Override
    protected void createPresenter() {
        mChangeContractNamePresenter = new ChangeContractNamePresenterImpl(this, new ChangeContractNameInteractorImpl(getContext()));
    }

    @Override
    protected ChangeContractNamePresenter getPresenter() {
        return mChangeContractNamePresenter;
    }

    @Override
    public void setSoftMode() {
        super.setSoftMode();
        getMainActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }
}
