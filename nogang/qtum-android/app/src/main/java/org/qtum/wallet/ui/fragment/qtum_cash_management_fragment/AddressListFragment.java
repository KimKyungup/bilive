package org.qtum.wallet.ui.fragment.qtum_cash_management_fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import org.jsoup.helper.StringUtil;
import org.qtum.wallet.R;
import org.qtum.wallet.model.AddressWithBalance;
import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.FontTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddressListFragment extends BaseFragment implements AddressListView, OnAddressClickListener {

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;

    protected AlertDialog mTransferDialog;
    protected boolean showTransferDialog = false;

    AddressListPresenter mAddressListPresenter;
    protected AddressesWithBalanceAdapter mAddressesWithBalanceAdapter;

    @OnClick({R.id.ibt_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ibt_back:
                getActivity().onBackPressed();
                break;
        }
    }

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, AddressListFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_address_list;
    }

    @Override
    public void updateAddressList(List<AddressWithBalance> deterministicKeyWithBalance) {
        mAddressesWithBalanceAdapter = new AddressesWithBalanceAdapter(deterministicKeyWithBalance, this, R.layout.item_address);
        mRecyclerView.setAdapter(mAddressesWithBalanceAdapter);
    }

    @Override
    public void onItemClick(AddressWithBalance deterministicKeyWithBalance) {
        List<AddressWithBalance> deterministicKeyWithBalances = new ArrayList<>(getPresenter().getAddressWithBalanceList());
        deterministicKeyWithBalances.remove(deterministicKeyWithBalance);
        showTransferDialogFragment(deterministicKeyWithBalance, deterministicKeyWithBalances);
    }

    protected void showTransferDialogFragment(final AddressWithBalance keyWithBalanceTo, List<AddressWithBalance> keyWithBalanceList) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_transfer_balance_fragment, null);

        final TextInputEditText mEditTextAmount = (TextInputEditText) view.findViewById(R.id.et_amount);
        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner_transfer);
        FontTextView mEditTextAddressTo = (FontTextView) view.findViewById(R.id.tv_address_to);

        mEditTextAddressTo.setText(keyWithBalanceTo.getAddress());

        AddressesWithBalanceSpinnerAdapter spinnerAdapter = new AddressesWithBalanceSpinnerAdapter(getContext(), keyWithBalanceList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getPresenter().setKeyWithBalanceFrom((AddressWithBalance) spinner.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        view.findViewById(R.id.bt_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTransferDialog.dismiss();
            }
        });

        view.findViewById(R.id.bt_transfer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setProgressDialog();
                transfer(keyWithBalanceTo, getPresenter().getKeyWithBalanceFrom(), mEditTextAmount.getText().toString());
            }
        });

        mTransferDialog = new AlertDialog
                .Builder(getContext())
                .setView(view)
                .create();

        if (mTransferDialog.getWindow() != null) {
            mTransferDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        mTransferDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showTransferDialog = false;
            }
        });

        mTransferDialog.setCanceledOnTouchOutside(false);
        showTransferDialog = true;
        mTransferDialog.show();
    }
    @Override
    protected void createPresenter() {
        mAddressListPresenter = new AddressListPresenterImpl(this, new AddressListInteractorImpl(getContext()));
    }

    @Override
    protected AddressListPresenter getPresenter() {
        return mAddressListPresenter;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mTransferDialog != null) {
            mTransferDialog.dismiss();
        }
    }

    @Override
    public void initializeViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getMainActivity().addAuthenticationListener(new MainActivity.AuthenticationListener() {
            @Override
            public void onAuthenticate() {
                if (showTransferDialog) {
                    mTransferDialog.show();
                }
            }
        });
    }

    public void transfer(AddressWithBalance keyWithBalanceTo, AddressWithBalance keyWithBalanceFrom, String amountString) {
        if (!isValidFloat(amountString)) {
            setAlertDialog(getString(R.string.error),
                    getString(R.string.enter_valid_amount_value),
                    getString(R.string.ok),
                    BaseFragment.PopUpType.error);
            return;
        }

        if (Float.valueOf(amountString) <= 0) {
            setAlertDialog(getString(R.string.error),
                    getString(R.string.transaction_amount_cant_be_zero),
                    getString(R.string.ok),
                    BaseFragment.PopUpType.error);
            return;
        }

        getMainActivity().setIconChecked(1);
        Fragment fragment = SendFragment.newInstance(keyWithBalanceFrom.getAddress(), keyWithBalanceTo.getAddress(), amountString, "", getContext());
        getMainActivity().setRootFragment(fragment);
        openRootFragment(fragment);
    }

    private boolean isValidFloat(String value) {
        return !TextUtils.isEmpty(value) && !(value.length() == 1 && (value.charAt(0) == '.' || value.charAt(0) == ','));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getMainActivity().removeAuthenticationListener();
    }
}
