package org.qtum.wallet.ui.fragment.language_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class LanguageFragment extends BaseFragment implements LanguageView, OnLanguageIntemClickListener {

    private LanguagePresenter mLanguageFragmentPresenter;
    protected LanguageAdapter mLanguageAdapter;
    protected List<Pair<String, String>> mLanguagesList;

    @BindView(R.id.recycler_view)
    protected
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_toolbar_language)
    TextView mTextViewToolBarLanguage;

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
        BaseFragment fragment = Factory.instantiateFragment(context, LanguageFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mLanguageFragmentPresenter = new LanguagePresenterImpl(this, new LanguageInteractorImpl(getContext()));
    }

    @Override
    protected LanguagePresenter getPresenter() {
        return mLanguageFragmentPresenter;
    }

    @Override
    public void initializeViews() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void resetText() {
        mTextViewToolBarLanguage.setText(R.string.language);
    }

    private int findLanguagePosition(String currentLanguage) {
        int pos = 0;
        for (Pair<String, String> lang : mLanguagesList) {
            if (lang.first.equals(currentLanguage)) {
                return pos;
            }
            pos++;
        }
        return pos;
    }

    @Override
    public void onLanguageIntemClick(int adapterPosition) {
        int oldPosition = findLanguagePosition(getPresenter().getCurrentLanguage());
        if (oldPosition != adapterPosition) {
            getPresenter().setCurrentLanguage(mLanguageAdapter.mLanguagesList.get(adapterPosition).first);
            mLanguageAdapter.notifyItemChanged(oldPosition);
            mLanguageAdapter.notifyItemChanged(adapterPosition);
        }
    }
}
