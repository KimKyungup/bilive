package org.qtum.wallet.ui.fragment.templates_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.qtum.wallet.R;
import org.qtum.wallet.model.ContractTemplate;
import org.qtum.wallet.ui.fragment.set_your_token_fragment.SetYourTokenFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class TemplatesFragment extends BaseFragment implements TemplatesView, TemplateSelectListener {

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, TemplatesFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    private TemplatesPresenter presenter;

    @BindView(R.id.recycler_view)
    RecyclerView contractList;

    @OnClick(R.id.ibt_back)
    public void onClick() {
        getActivity().onBackPressed();
    }

    @Override
    protected void createPresenter() {
        presenter = new TemplatesPresenterImpl(this, new TemplatesInteractorImpl(getContext()));
    }

    @Override
    protected TemplatesPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        contractList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    protected void initializeRecyclerView(List<ContractTemplate> contractFullTemplateList, int resId) {
        contractList.setAdapter(new TemplatesRecyclerAdapter(contractFullTemplateList, this, resId));
    }

    @Override
    public void onSelectContract(ContractTemplate contractTemplate) {
        BaseFragment fragment = SetYourTokenFragment.newInstance(getContext(), contractTemplate.getUuid());
        openFragment(fragment);
    }
}
