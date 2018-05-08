package org.qtum.wallet.ui.fragment.wallet_fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.NetworkStateReceiver;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.listeners.NetworkStateListener;
import org.qtum.wallet.dataprovider.services.update_service.UpdateService;
import org.qtum.wallet.dataprovider.services.update_service.listeners.BalanceChangeListener;
import org.qtum.wallet.dataprovider.services.update_service.listeners.TransactionListener;
import org.qtum.wallet.model.gson.history.History;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.fragment.qtum_cash_management_fragment.AddressListFragment;

import org.qtum.wallet.ui.fragment.send_fragment.SendFragment;

import org.qtum.wallet.ui.fragment_factory.Factory;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.utils.ClipboardUtils;
import org.qtum.wallet.utils.FontTextView;
import org.qtum.wallet.utils.ResizeWidthAnimation;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class WalletFragment extends BaseFragment implements WalletView, TransactionClickListener {

    private boolean OPEN_QR_CODE_FRAGMENT_FLAG = false;
    private static final int REQUEST_CAMERA = 3;
    private NetworkStateListener mNetworkStateListener;
    protected WalletPresenter mWalletFragmentPresenter;
    protected TransactionAdapter mTransactionAdapter;
    protected LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
    protected int visibleItemCount;
    protected int totalItemCount;
    protected int pastVisibleItems;
    protected boolean mLoadingFlag = false;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh)
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.app_bar)
    protected AppBarLayout mAppBarLayout;

    @BindView(R.id.bt_qr_code)
    protected ImageButton mButtonQrCode;

    @BindView(R.id.tv_wallet_name)
    protected TextView mTextViewWalletName;

    @BindView(R.id.fade_divider_root)
    RelativeLayout fadeDividerRoot;

    @BindView(R.id.tv_public_key)
    protected FontTextView publicKeyValue;

    @BindView(R.id.tv_balance)
    protected FontTextView balanceValue;

    @BindView(R.id.ll_balance)
    protected LinearLayout balanceLayout;

    @BindView(R.id.available_balance_title)
    protected FontTextView balanceTitle;

    @BindView(R.id.tv_unconfirmed_balance)
    protected FontTextView uncomfirmedBalanceValue;

    @BindView(R.id.unconfirmed_balance_title)
    protected FontTextView uncomfirmedBalanceTitle;

    @BindView(R.id.balance_view)
    protected FrameLayout balanceView;

    @BindView(R.id.toolbar_layout)
    protected CollapsingToolbarLayout collapsingToolbar;

    private NetworkStateReceiver mNetworkStateReceiver;
    private UpdateService mUpdateService;

    float headerPAdding = 0;
    float prevPercents = 1;

    @BindView(R.id.fade_divider)
    View fadeDivider;

    @BindView(R.id.page_indicator)
    public View pagerIndicator;

    @Override
    protected int getLayout() {
        return R.layout.fragment_wallet;
    }

    final DisplayMetrics dm = new DisplayMetrics();

    boolean expanded = false;

    public void doDividerExpand() {
        if (!expanded) {
            expanded = true;
            fadeDivider.clearAnimation();
            fadeDivider.setVisibility(View.VISIBLE);
            ResizeWidthAnimation anim = new ResizeWidthAnimation(fadeDivider, getResources().getDisplayMetrics().widthPixels);
            anim.setDuration(300);
            anim.setFillEnabled(true);
            anim.setFillAfter(true);
            fadeDivider.startAnimation(anim);
        }
    }

    public void doDividerCollapse() {
        if (expanded) {
            fadeDivider.clearAnimation();
            fadeDivider.setVisibility(View.INVISIBLE);
            ViewGroup.LayoutParams lp = fadeDivider.getLayoutParams();
            lp.width = 0;
            fadeDivider.setLayoutParams(lp);
            expanded = false;
        }
    }



    protected void animateText(float percents, View view, float fringe) {
        if (percents > fringe) {
            view.setScaleX(percents);
            view.setScaleY(percents);
        } else {
            view.setScaleX(fringe);
            view.setScaleY(fringe);
        }
    }



    @Override
    public void updateBalance(String balance, String unconfirmedBalance) {
        try {
            balanceValue.setText(String.format("%s QTUM", balance));
            if (unconfirmedBalance != null) {
                uncomfirmedBalanceValue.setVisibility(View.VISIBLE);
                uncomfirmedBalanceTitle.setVisibility(View.VISIBLE);
                uncomfirmedBalanceValue.setText(String.format("%s QTUM", unconfirmedBalance));
            } else {
                uncomfirmedBalanceValue.setVisibility(View.GONE);
                uncomfirmedBalanceTitle.setVisibility(View.GONE);
            }
        } catch (NullPointerException e) {
            Log.d("WalletFragment", "updateBalance: " + e.getMessage());
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getMainActivity().subscribeServiceConnectionChangeEvent(new MainActivity.OnServiceConnectionChangeListener() {
            @Override
            public void onServiceConnectionChange(boolean isConnecting) {
                if (isConnecting) {
                    mUpdateService = getMainActivity().getUpdateService();
                    mUpdateService.addTransactionListener(new TransactionListener() {
                        @Override
                        public void onNewHistory(History history) {
                            getPresenter().onNewHistory(history);
                        }

                        @Override
                        public boolean getVisibility() {
                            return getPresenter().getVisibility();
                        }
                    });
                    initBalanceListener();
                }
            }
        });

        getMainActivity().addPermissionResultListener(new MainActivity.PermissionsResultListener() {
            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                if (requestCode == REQUEST_CAMERA) {
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        OPEN_QR_CODE_FRAGMENT_FLAG = true;
                    }
                }
            }
        });
        mNetworkStateReceiver = getMainActivity().getNetworkReceiver();
        mNetworkStateListener = new NetworkStateListener() {
            @Override
            public void onNetworkStateChanged(boolean networkConnectedFlag) {
                getPresenter().onNetworkStateChanged(networkConnectedFlag);
            }
        };
        mNetworkStateReceiver.addNetworkStateListener(mNetworkStateListener);
    }

    public void initBalanceListener() {
        mUpdateService.addBalanceChangeListener(balanceListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPresenter().updateVisibility(true);
        if (mUpdateService != null) {
            mUpdateService.clearNotification();
        }
        if (OPEN_QR_CODE_FRAGMENT_FLAG) {
            openQrCodeFragment();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPresenter().updateVisibility(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mNetworkStateListener!=null) {
            mNetworkStateReceiver.removeNetworkStateListener(mNetworkStateListener);
        }
        if(mUpdateService!=null) {
            mUpdateService.removeTransactionListener();
            mUpdateService.removeBalanceChangeListener(balanceListener);
        }
        if(getMainActivity()!=null) {
            getMainActivity().removePermissionResultListener();
        }
        setAdapterNull();
    }

    @OnClick({R.id.ll_receive, R.id.iv_receive})
    public void onReceiveClick() {
        //BaseFragment receiveFragment = ReceiveFragment.newInstance(getContext(), null, null);
        //openFragmentForResult(receiveFragment);
    }

    @OnClick(R.id.iv_choose_address)
    public void onChooseAddressClick() {
        BaseFragment addressListFragment = AddressListFragment.newInstance(getContext());
        openFragment(addressListFragment);
    }

    @OnClick({R.id.bt_qr_code})
    public void onClick(View view) {
        if (getMainActivity().checkPermission(Manifest.permission.CAMERA)) {
            openQrCodeFragment();
        } else {
            getMainActivity().loadPermissions(Manifest.permission.CAMERA, REQUEST_CAMERA);
        }
    }

    private void openQrCodeFragment() {
        OPEN_QR_CODE_FRAGMENT_FLAG = false;
        SendFragment sendFragment = (SendFragment) SendFragment.newInstance(true, null, null, null, getContext());
        openRootFragment(sendFragment);
        getMainActivity().setRootFragment(sendFragment);
    }

    @OnLongClick(R.id.tv_public_key)
    public boolean onAddressLongClick() {
        ClipboardUtils.copyToClipBoard(getContext(), publicKeyValue.getText().toString(), new ClipboardUtils.CopyCallback() {
            @Override
            public void onCopyToClipBoard() {
                showToast(getString(R.string.copied));
            }
        });
        return true;
    }

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, WalletFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
    }

    protected float percents = 1;

    public int getTotalRange() {
        return mAppBarLayout.getTotalScrollRange();
    }

    @Override
    public void initializeViews() {
        super.initializeViews();
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (!mLoadingFlag) {
                        visibleItemCount = mLinearLayoutManager.getChildCount();
                        totalItemCount = mLinearLayoutManager.getItemCount();
                        pastVisibleItems = mLinearLayoutManager.findFirstVisibleItemPosition();
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount - 1) {
                            getPresenter().onLastItem(totalItemCount - 1);
                        }
                    }
                }
            }
        });
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorAccent));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPresenter().onRefresh();
            }
        });

        showBottomNavView(R.color.primary_text_color);

        headerPAdding = convertDpToPixel(16, getContext());

        uncomfirmedBalanceValue.setVisibility(View.GONE);
        uncomfirmedBalanceTitle.setVisibility(View.GONE);

        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //TODO : wallet 상의 리스트 올리거나 내렸을경우 상단 appbar ui 변경해주는 코드 작성
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

                    balanceView.setAlpha((percents > 0.5f) ? percents : 1 - percents);

                    if (percents == 0) {
                        doDividerExpand();
                    } else {
                        doDividerCollapse();
                    }

                    final float textPercent = (percents >= .5f) ? percents : .5f;
                    final float textPercent3f = (percents >= .3f) ? percents : .3f;

                    if (uncomfirmedBalanceTitle.getVisibility() == View.VISIBLE) {
                        animateText(percents, balanceLayout, .5f);
                        balanceLayout.setX(balanceView.getWidth() - (balanceView.getWidth() / 2 * percents + (balanceLayout.getWidth() * textPercent) / 2) - balanceLayout.getWidth() * (1 - textPercent) - headerPAdding * (1 - percents));
                        balanceLayout.setY(balanceView.getHeight() / 2 - balanceTitle.getHeight() * percents - balanceLayout.getHeight() * percents - balanceLayout.getHeight() * (1 - percents));

                        animateText(percents, balanceTitle, .7f);
                        balanceTitle.setX(balanceView.getWidth() / 2 * percents - (balanceTitle.getWidth() * textPercent3f) / 2 + headerPAdding * (1 - percents));
                        balanceTitle.setY(balanceView.getHeight() / 2 - balanceTitle.getHeight() * percents - balanceTitle.getHeight() * (1 - percents));

                        animateText(percents, uncomfirmedBalanceValue, .5f);
                        uncomfirmedBalanceValue.setX(balanceView.getWidth() - (balanceView.getWidth() / 2 * percents + (uncomfirmedBalanceValue.getWidth() * textPercent) / 2) - uncomfirmedBalanceValue.getWidth() * (1 - textPercent) - headerPAdding * (1 - percents));

                        animateText(percents, uncomfirmedBalanceTitle, .7f);
                        uncomfirmedBalanceTitle.setY(balanceView.getHeight() / 2 + uncomfirmedBalanceValue.getHeight() * percents - (uncomfirmedBalanceTitle.getHeight() * percents * (1 - percents)));
                        uncomfirmedBalanceTitle.setX(balanceView.getWidth() / 2 * percents - (uncomfirmedBalanceTitle.getWidth() * textPercent3f) / 2 + headerPAdding * (1 - percents));
                    } else {
                        animateText(percents, balanceTitle, .7f);
                        balanceTitle.setX(balanceView.getWidth() / 2 * percents - (balanceTitle.getWidth() * textPercent3f) / 2 + headerPAdding * (1 - percents));
                        balanceTitle.setY(balanceView.getHeight() / 2 + balanceTitle.getHeight() / 2 * percents - balanceTitle.getHeight() / 2 * (1 - percents));

                        animateText(percents, balanceLayout, .5f);
                        balanceLayout.setX(balanceView.getWidth() - (balanceView.getWidth() / 2 * percents + (balanceLayout.getWidth() * textPercent) / 2) - balanceLayout.getWidth() * (1 - textPercent) - headerPAdding * (1 - percents));
                        balanceLayout.setY(balanceView.getHeight() / 2 - balanceLayout.getHeight() * percents - balanceLayout.getHeight() / 2 * (1 - percents));
                    }
                    prevPercents = percents;
                }
            }

        });
    }

    @Override
    protected WalletPresenter getPresenter() {
        return mWalletFragmentPresenter;
    }

    @Override
    protected void createPresenter() {
        mWalletFragmentPresenter = new WalletPresenterImpl(this, new WalletInteractorImpl(getContext()));
    }

    @Override
    public void onTransactionClick(int adapterPosition) {
        getPresenter().openTransactionFragment(adapterPosition);
    }


    @Override
    public void updateHistory(List<History> historyList) {
        updateHistory(new TransactionAdapter(historyList, getAdapterListener()));
    }

    public void updateHistory(TransactionAdapter adapter) {
        mTransactionAdapter = adapter;
        mRecyclerView.setAdapter(mTransactionAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
        mLoadingFlag = false;
    }

    protected TransactionClickListener getAdapterListener() {
        return this;
    }

    @Override
    public void setAdapterNull() {
    }

    @Override
    public void updatePubKey(String pubKey) {
        publicKeyValue.setText(pubKey);
    }

    @Override
    public void startRefreshAnimation() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void stopRefreshRecyclerAnimation() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void addHistory(int positionStart, int itemCount, List<History> historyList) {
        mTransactionAdapter.setHistoryList(historyList);
        mTransactionAdapter.setLoadingFlag(false);
        mLoadingFlag = false;
        mTransactionAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void loadNewHistory() {
        mLoadingFlag = true;
        mTransactionAdapter.setLoadingFlag(true);
        mTransactionAdapter.notifyItemChanged(totalItemCount - 1);
    }

    @Override
    public void notifyNewHistory() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTransactionAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void notifyConfirmHistory(final int notifyPosition) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTransactionAdapter.notifyItemChanged(notifyPosition);
            }
        });
    }

    BalanceChangeListener balanceListener = new BalanceChangeListener() {
        @Override
        public void onChangeBalance(final BigDecimal unconfirmedBalance, final BigDecimal balance) {
            getMainActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String balanceString = balance.toString();
                    if (balanceString != null) {
                        String unconfirmedBalanceString = unconfirmedBalance.toString();
                        if (!TextUtils.isEmpty(unconfirmedBalanceString) && !unconfirmedBalanceString.equals("0")) {
                            updateBalance(balanceString, String.valueOf(unconfirmedBalance.floatValue()));
                        } else {
                            updateBalance(balanceString, null);
                        }
                    }
                }
            });
        }
    };

    @Override
    public void openTransactionsFragment(int position) {
        //TODO : transaction 기록을 눌렀을 때 표시되어야 하는 fragment 호출
        //Fragment fragment = TransactionFragment.newInstance(getContext(), position);
        //openFragment(fragment);
    }
}