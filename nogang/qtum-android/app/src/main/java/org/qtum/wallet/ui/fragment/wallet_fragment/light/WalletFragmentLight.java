package org.qtum.wallet.ui.fragment.wallet_fragment.light;

import android.support.design.widget.AppBarLayout;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.qtum.wallet.R;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.ui.fragment.wallet_fragment.WalletFragment;
import org.qtum.wallet.ui.wave_visualizer.WaveHelper;
import org.qtum.wallet.ui.wave_visualizer.WaveView;

import java.util.List;

import butterknife.BindView;

public class WalletFragmentLight extends WalletFragment {

    @BindView(R.id.app_bar_placeholder)
    View appbarPlaceholder;

    @BindView(R.id.not_confirmed_balance_view)
    View notConfirmedBalancePlaceholder;

    @BindView(R.id.tv_placeholder_balance_value)
    TextView placeHolderBalance;

    @BindView(R.id.tv_placeholder_not_confirmed_balance_value)
    TextView placeHolderBalanceNotConfirmed;

    @BindView(R.id.balance_view)
    FrameLayout waveContainer;

    @BindView(R.id.gradient_iv)
    ImageView mGrIv;

    @BindView(R.id.wave_view)
    WaveView waveView;

    private WaveHelper mWaveHelper;

    @Override
    protected int getLayout() {
        return R.layout.fragment_wallet_light;
    }

    @BindView(R.id.iv_choose_address)
    ImageView mIvChooseAddr;

    @Override
    public void initializeViews() {
        super.initializeViews();
        updateBalance("0", "0");
        showBottomNavView(R.color.title_color_light);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (mAppBarLayout != null) {
                    if (mSwipeRefreshLayout != null) {
                        if (!mSwipeRefreshLayout.isActivated()) {
                            if (verticalOffset == 0) {
                                mSwipeRefreshLayout.setEnabled(true);
                            } else {
                                mSwipeRefreshLayout.setEnabled(false);
                            }
                        }
                    }
                    percents = (((getTotalRange() - Math.abs(verticalOffset)) * 1.0f) / getTotalRange());
                    float testPercents = percents - (1 - percents);
                    float testP2 = (percents >= .8f) ? 0 : (1 - percents) - percents;
                    balanceView.setAlpha(testPercents);
                    mButtonQrCode.setAlpha(testPercents);
                    mTextViewWalletName.setAlpha(testPercents);
                    mGrIv.setAlpha(testPercents);
                    mIvChooseAddr.setAlpha(testPercents);
                    appbarPlaceholder.setAlpha(testP2);
                }
            }
        });
        appbarPlaceholder.setVisibility(View.VISIBLE);
        waveView.setShapeType(WaveView.ShapeType.SQUARE);
        mWaveHelper = new WaveHelper(waveView);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        mWaveHelper.start();
    }

    @Override
    public void onPause() {
        mWaveHelper.cancel();
        super.onPause();
    }

    @Override
    public void updateHistory(List<History> historyList) {
        super.updateHistory(new TransactionAdapterLight(historyList, getAdapterListener()));
    }

    @Override
    public void updateBalance(String balance, String unconfirmedBalance) {
        try {
            balanceValue.setText(getSpannedBalance(String.format("%s QTUM", balance)));
            placeHolderBalance.setText(getSpannedBalance(String.format("%s QTUM", balance)));
            if (unconfirmedBalance != null) {
                notConfirmedBalancePlaceholder.setVisibility(View.VISIBLE);
                uncomfirmedBalanceValue.setVisibility(View.VISIBLE);
                uncomfirmedBalanceTitle.setVisibility(View.VISIBLE);
                uncomfirmedBalanceValue.setText(getSpannedBalance(String.format("%s QTUM", unconfirmedBalance)));
                placeHolderBalanceNotConfirmed.setText(getSpannedBalance(String.format("%s QTUM", unconfirmedBalance)));
            } else {
                notConfirmedBalancePlaceholder.setVisibility(View.GONE);
                uncomfirmedBalanceValue.setVisibility(View.GONE);
                uncomfirmedBalanceTitle.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            Log.d("WalletFragmentLight", "updateBalance: " + e.getMessage());
        }
    }

    private SpannableString getSpannedBalance(String balance) {
        SpannableString span = new SpannableString(balance);
        if (balance.length() > 4) {
            span.setSpan(new RelativeSizeSpan(.6f), balance.length() - 4, balance.length(), 0);
        }
        return span;
    }
}
