package org.qtum.wallet.ui.fragment.fragment_import_wallet;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public class ImportWalletFragment extends BaseFragment implements IImportWalletView {

    private IImportWalletPresenter mFragmentPresenter;

    @BindView(R.id.imageViewBackArrow)
    ImageView imageViewBackArrow;

    @BindView(R.id.editTextKeywords)
    EditText editTextKeywords;

    @BindView(R.id.buttonImport)
    Button buttonImport;

    public static ImportWalletFragment newInstance(Context context) {
        Bundle args = new Bundle();
        ImportWalletFragment fragment = (ImportWalletFragment) Factory.instantiateFragment(context, ImportWalletFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new ImportWalletPresenterImpl(this, new ImportWalletInteractorImpl(getContext()));
    }

    @Override
    protected IImportWalletPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_import_wallet;
    }

    @OnClick({R.id.imageViewBackArrow, R.id.buttonImport,})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBackArrow: {
                getFragmentManager().popBackStack();
                break;
            }
            case R.id.buttonImport: {
                Toast.makeText(getContext(), "[Debug Toast] ImportWalletFragment=buttonImport", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
