package org.qtum.wallet.ui.fragment.fragment_scribble_detail;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public class ScribbleDetailFragment extends BaseFragment implements IScribbleDetailView {

    private IScribbleDetailPresenter mFragmentPresenter;
    private static final String INDEX = "index";

    @BindView(R.id.imageViewBackArrow)
    ImageView imageViewBackArrow;

    @BindView(R.id.imageViewShare)
    ImageView imageViewShare;

    @BindView(R.id.imageViewInfo)
    ImageView imageViewInfo;

    @BindView(R.id.textViewBody)
    TextView textViewBody;

    public static ScribbleDetailFragment newInstance(Context context, int index) {
        Bundle args = new Bundle();
        ScribbleDetailFragment fragment = (ScribbleDetailFragment) Factory.instantiateFragment(context, ScribbleDetailFragment.class);
        args.putInt(INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new ScribbleDetailPresenterImpl(this, new ScribbleDetailInteractorImpl(getContext()));
    }

    @Override
    protected IScribbleDetailPresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_scribble_detail;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        getPresenter().setIndex((int) getArguments().getInt(INDEX));
    }

    @Override
    public void setBody(String body) {
        if (textViewBody != null) {
            textViewBody.setText(body);
        }
    }

    @Override
    public void showShareMenu(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        getMainActivity().startActivity(Intent.createChooser(shareIntent, getResources().getString(R.string.share)));
    }

    @OnClick({R.id.imageViewBackArrow, R.id.imageViewShare, R.id.imageViewInfo, })
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBackArrow: {
                getPresenter().onBackButtonClicked();
                getFragmentManager().popBackStack();
                break;
            }
            case R.id.imageViewInfo: {
                getPresenter().onInfoButtonClicked();
                Toast.makeText(getContext(), "[Debug Toast] ScribbleDetailFragment=imageViewInfo", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.imageViewShare: {
                getPresenter().onShareButtonClicked();
                Toast.makeText(getContext(), "[Debug Toast] ScribbleDetailFragment=imageViewShare", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
