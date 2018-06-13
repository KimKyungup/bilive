package org.qtum.wallet.ui.fragment.fragment_scribble_detail;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public class ScribbleDetailFragment extends BaseFragment implements IScribbleDetailView {

    private IScribbleDetailPresenter mFragmentPresenter;

    @BindView(R.id.imageViewBackArrow)
    ImageView imageViewBackArrow;

    @BindView(R.id.imageViewShare)
    ImageView imageViewShare;

    @BindView(R.id.imageViewInfo)
    ImageView imageViewInfo;

    public static ScribbleDetailFragment newInstance(Context context) {
        Bundle args = new Bundle();
        ScribbleDetailFragment fragment = (ScribbleDetailFragment) Factory.instantiateFragment(context, ScribbleDetailFragment.class);
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

    @OnClick({R.id.imageViewBackArrow, R.id.imageViewShare, R.id.imageViewInfo, })
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBackArrow: {
                getFragmentManager().popBackStack();
                break;
            }
            case R.id.imageViewShare: {
                Toast.makeText(getContext(), "[Debug Toast] ScribbleDetailFragment=imageViewShare", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.imageViewInfo: {
                Toast.makeText(getContext(), "[Debug Toast] ScribbleDetailFragment=imageViewInfo", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }
}
