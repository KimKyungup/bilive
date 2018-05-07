package org.qtum.wallet.ui.fragment.write_fragment;

import android.app.Activity;
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
import org.qtum.wallet.model.news.News;
import org.qtum.wallet.model.writeblock.WriteBlock;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.news_detail_fragment.NewsDetailFragment;
import org.qtum.wallet.ui.fragment.news_fragment.NewsInteractorImpl;
import org.qtum.wallet.ui.fragment.news_fragment.NewsPresenter;
import org.qtum.wallet.ui.fragment.news_fragment.NewsPresenterImpl;
import org.qtum.wallet.ui.fragment.news_fragment.NewsView;
import org.qtum.wallet.ui.fragment_factory.Factory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public abstract class WriteFragment extends BaseFragment implements WriteView {
    private WritePresenter mWriteFragmentPresenter;
    protected WriteAdapter mWriteAdapter;

    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.text_write)
    protected EditText mTextWrite;

    private NetworkStateReceiver mNetworkStateReceiver;
    private NetworkStateListener mNetworkStateListener;

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, WriteFragment.class);
        fragment.setArguments(args);
        return fragment;
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
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                    BaseFragment newsDetailFragment = NewsDetailFragment.newInstance(getContext(), getAdapterPosition());
                    openFragment(newsDetailFragment);
                }
            });
            ButterKnife.bind(this, itemView);
        }

        void bindWriteBlocks(WriteBlock writeBlock) {
            mTextViewTitle.setText(writeBlock.getmBlockHash());
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
}