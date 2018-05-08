package org.qtum.wallet.ui.fragment.write_fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.NetworkStateReceiver;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.listeners.NetworkStateListener;
import org.qtum.wallet.model.writeblock.WriteBlock;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WriteFragment extends BaseFragment implements WriteView {
    private WritePresenter mWriteFragmentPresenter;
    protected WriteAdapter mWriteAdapter;
    protected LinearLayoutManager mLinearLayoutManager;
    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.text_write)
    protected EditText mTextWrite;

    private NetworkStateReceiver mNetworkStateReceiver;
    private NetworkStateListener mNetworkStateListener;

    protected boolean mLoadingFlag = false;
    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisibleItems;

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, WriteFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_write;
    }

    @Override
    public void updateWriteBlocks(List<WriteBlock> writeBlocks) {
        mWriteAdapter = new WriteAdapter(writeBlocks, R.layout.item_write);
        mRecyclerView.setAdapter(mWriteAdapter);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public String getWriteText(){
        return mTextWrite.getText().toString();
    }

    @OnClick(R.id.bt_write)
    public void onWriteClick() {
        //Toast.makeText(getContext(),"eee",Toast.LENGTH_LONG).show();
        /*
        if (mSeekBar != null) {
            textViewChangeValue = true;
            double value = (mMinFee + (mSeekBar.getProgress() * step)) / 100000000.;
            seekBarChangeValue = true;
            mTextInputEditTextFee.setText(new DecimalFormat("#.########").format(value));
        }
        */
        hideKeyBoard();
        getPresenter().write();
        mTextWrite.setText("");
    }

    @Override
    protected void createPresenter() {
        mWriteFragmentPresenter = new WritePresenterImpl(this, new WriteInteractorImpl(getContext()));
    }

    @Override
    protected WritePresenter getPresenter() {
        return mWriteFragmentPresenter;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mNetworkStateReceiver = getMainActivity().getNetworkReceiver();
        mNetworkStateListener = new NetworkStateListener() {
            @Override
            public void onNetworkStateChanged(boolean networkConnectedFlag) {
                getPresenter().onNetworkStateChanged(networkConnectedFlag);
            }
        };
        mNetworkStateReceiver.addNetworkStateListener(mNetworkStateListener);
    }

    @Override
    public void initializeViews() {
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());
                if (!mSwipeRefreshLayout.isRefreshing())
                    if (manager.findFirstCompletelyVisibleItemPosition() == 0)
                        mSwipeRefreshLayout.setEnabled(true);
                    else
                        mSwipeRefreshLayout.setEnabled(false);
                if (dy > 0) {
                    if (!mLoadingFlag) {
                        visibleItemCount = mLinearLayoutManager.getChildCount();
                        totalItemCount = mLinearLayoutManager.getItemCount();
                        pastVisibleItems = mLinearLayoutManager.findFirstVisibleItemPosition();
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount - 1) {
                            getPresenter().onLastItem(totalItemCount - 1);
                            //Toast.makeText(getContext(),"last",Toast.LENGTH_LONG).show();
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

    }

    @Override
    public void setAdapterNull() {
        mWriteAdapter = null;
    }

    @Override
    public void startRefreshAnimation() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    class NewsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_description)
        TextView mTextViewDescription;
        @BindView(R.id.tv_title)
        TextView mTextViewTitle;
        @BindView(R.id.tv_date)
        TextView mTextViewDate;

        NewsHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Todo : Write 클릭시 세부 내용 보여주는 fragment 작성
                    //BaseFragment newsDetailFragment = NewsDetailFragment.newInstance(getContext(), getAdapterPosition());
                    //openFragment(newsDetailFragment);
                }
            });
            ButterKnife.bind(this, itemView);
        }

        void bindWriteBlocks(WriteBlock writeBlock) {
            mTextViewTitle.setText(writeBlock.getTXHash());
            mTextViewDate.setText(writeBlock.getBlockTime());
            mTextViewDescription.setText(writeBlock.getWrite());
        }
    }

    public class WriteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<WriteBlock> mWriteBlcokList;
        WriteBlock mWriteBlock;
        private @LayoutRes
        int mResId;

        public WriteAdapter(List<WriteBlock> writeBlockList, @LayoutRes int resId) {
            mWriteBlcokList = writeBlockList;
            mResId = resId;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(mResId, parent, false);
            return new NewsHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            mWriteBlock = mWriteBlcokList.get(position);
            ((NewsHolder) holder).bindWriteBlocks(mWriteBlock);

        }

        @Override
        public int getItemCount() {
            return mWriteBlcokList.size();
        }

        public void setLoadingFlag(boolean loadingFlag) {
            mLoadingFlag = loadingFlag;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mNetworkStateReceiver.removeNetworkStateListener(mNetworkStateListener);
        setAdapterNull();
    }

    @Override
    public void stopRefreshRecyclerAnimation() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void tost(String s){
        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
    }
    @Override
    public WriteInteractorImpl.SendTxCallBack getSendTransactionCallback() {
        return sendCallback;
    }

    private WriteInteractorImpl.SendTxCallBack sendCallback = new WriteInteractorImpl.SendTxCallBack() {
        @Override
        public void onSuccess() {
            setAlertDialog(org.qtum.wallet.R.string.payment_completed_successfully, "Ok", BaseFragment.PopUpType.confirm);
            getPresenter().loadAndUpdateWrite();
        }

        @Override
        public void onError(String error) {
            dismissProgressDialog();
            setAlertDialog(org.qtum.wallet.R.string.error, error, "Ok", BaseFragment.PopUpType.error);
        }
    };

    @Override
    public void loadNewWrite() {
        mLoadingFlag = true;
        //mWriteAdapter.setLoadingFlag(true);
        mWriteAdapter.notifyItemChanged(totalItemCount - 1);
    }

    @Override
    public void addHistory(int positionStart, int itemCount, List<WriteBlock> historyList) {
        //mWriteAdapter.setHistoryList(historyList);
        //mWriteAdapter.setLoadingFlag(false);
        mLoadingFlag = false;
        mWriteAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void notifyConfirmHistory(final int notifyPosition) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWriteAdapter.notifyItemChanged(notifyPosition);
            }
        });
    }
}
