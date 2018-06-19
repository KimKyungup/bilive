package org.qtum.wallet.ui.fragment.fragment_scribble;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.qtum.wallet.R;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.NetworkStateReceiver;
import org.qtum.wallet.dataprovider.receivers.network_state_receiver.listeners.NetworkStateListener;
import org.qtum.wallet.model.writeblock.WriteBlock;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.fragment_main.MainFragment;
import org.qtum.wallet.ui.fragment.fragment_scribble_detail.ScribbleDetailFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ScribbleFragment extends BaseFragment implements IScribbleView, RecyclerViewScribbleAdapter.OnItemClickListener {

    private final int SCROLL_THRESHOLD_IN_DIP = 20;

    private IScribblePresenter mFragmentPresenter;

    @BindView(R.id.layoutMainWriteEditor)
    ConstraintLayout layoutMainWriteEditor;

    @BindView(R.id.imageViewWrite)
    ImageView imageViewWrite;

    @BindView(R.id.textViewWriteOnBlockchain)
    TextView textViewWriteOnBlockchain;

    @BindView(R.id.editTextEditor)
    EditText editTextEditor;

    @BindView(R.id.textViewBalanceETH)
    TextView textViewBalanceETH;

    @BindView(R.id.imageViewWriteComplete)
    ImageView imageViewWriteComplete;

    @BindView(R.id.textViewPostSelectorAllPost)
    TextView textViewPostSelectorAllPost;

    @BindView(R.id.textViewPostSelectorMyPost)
    TextView textViewPostSelectorMyPost;

    @BindView(R.id.swipeRefreshLayoutPostList)
    SwipeRefreshLayout swipeRefreshLayoutPostList;

    @BindView(R.id.recyclerViewPost)
    RecyclerView recyclerViewPost;

    private RecyclerViewScribbleAdapter postAdapter = null;

    // Write editor visible / invisible
    private boolean isWriteEditorExpanded = false;

    // Footer 숨기고 보이기 위해
    private int scrollThreshold;
    private int scrollMoved = 0;
    private boolean isBottomMenuVisible = true;

    // 추가 로딩
    private boolean mLoadingFlag = false;
    private int visibleItemCount;
    private int totalItemCount;
    private int pastVisibleItems;


    // Network state
    private NetworkStateReceiver mNetworkStateReceiver;
    private NetworkStateListener mNetworkStateListener;

    public static ScribbleFragment newInstance() {
        Bundle args = new Bundle();
        ScribbleFragment fragment = new ScribbleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new ScribblePresenterImpl(this, new ScribbleInteractorImpl(getContext()));
    }

    @Override
    protected IScribblePresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_scribble;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        scrollThreshold = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, SCROLL_THRESHOLD_IN_DIP, getContext().getResources().getDisplayMetrics());
        swipeRefreshLayoutPostList.setOnRefreshListener(onRefreshListener);

        recyclerViewPost.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPost.addOnScrollListener(onScrollListener);

        selectAllPost();
        ((MainFragment) getParentFragment()).hideTopMenu();
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
    public void onDestroyView() {
        super.onDestroyView();
        mNetworkStateReceiver.removeNetworkStateListener(mNetworkStateListener);
        setAdapterNull();
    }

    @Override
    public void startRefreshAnimation() {
        if (swipeRefreshLayoutPostList != null) {
            swipeRefreshLayoutPostList.setRefreshing(true);
        }
    }

    @Override
    public void setAdapterNull() {
        // Not needed
    }

    @Override
    public void updateWriteBlocks(List<WriteBlock> writeblocks) {
        postAdapter = new RecyclerViewScribbleAdapter(writeblocks);
        postAdapter.setOnItemClickListener(this);
        recyclerViewPost.setAdapter(postAdapter);
//        postAdapter.notifyDataSetChanged();
        stopRefreshRecyclerAnimation();
    }

    @Override
    public void stopRefreshRecyclerAnimation() {
        if (swipeRefreshLayoutPostList != null) {
            swipeRefreshLayoutPostList.setRefreshing(false);
        }
    }

    @Override
    public String getWriteText() {
        return editTextEditor.getText().toString();
    }

    @Override
    public void tost(String s) {
        Toast.makeText(getContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    public ScribbleInteractorImpl.SendTxCallBack getSendTransactionCallback() {
        return sendCallback;
    }

    @Override
    public void loadNewWrite() {
        mLoadingFlag = true;
        //mWriteAdapter.setLoadingFlag(true);
        postAdapter.notifyItemChanged(totalItemCount - 1);
    }

    @Override
    public void addHistory(int positionStart, int itemCount, List<WriteBlock> historyList) {
        mLoadingFlag = false;
        postAdapter.notifyItemRangeChanged(positionStart, itemCount);
    }

    @Override
    public void notifyConfirmHistory(final int notifyPosition) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                postAdapter.notifyItemChanged(notifyPosition);
            }
        });
    }

    private void showKeyboard()
    {
        editTextEditor.requestFocus();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(editTextEditor, InputMethodManager.SHOW_FORCED);
    }

    private void expandWriteEditor()
    {
        int initHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, getContext().getResources().getDisplayMetrics());
        int targetHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 228, getContext().getResources().getDisplayMetrics());
        ValueAnimator va = ValueAnimator.ofInt(initHeight, targetHeight);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                layoutMainWriteEditor.getLayoutParams().height = value.intValue();
                layoutMainWriteEditor.requestLayout();
            }
        });
        va.start();

        imageViewWrite.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_main_write_button_clockwise));
        textViewWriteOnBlockchain.setVisibility(View.INVISIBLE);
        showKeyboard();

        isWriteEditorExpanded = true;
    }

    private void reduceWriteEditor()
    {
        int initHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 38, getContext().getResources().getDisplayMetrics());
        int targetHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 228, getContext().getResources().getDisplayMetrics());
        ValueAnimator va = ValueAnimator.ofInt(targetHeight, initHeight);
        va.setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                layoutMainWriteEditor.getLayoutParams().height = value.intValue();
                layoutMainWriteEditor.requestLayout();
            }
        });
        va.start();

        imageViewWrite.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_main_write_button_counter_clockwise));
        textViewWriteOnBlockchain.setVisibility(View.VISIBLE);
        hideKeyBoard();

        isWriteEditorExpanded = false;

    }

    private void selectAllPost()
    {
        textViewPostSelectorAllPost.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.btn_color));
        textViewPostSelectorAllPost.setTextColor(ContextCompat.getColor(getContext(), R.color.post_selector_font_color));

        textViewPostSelectorMyPost.setBackgroundColor(Color.TRANSPARENT);
        textViewPostSelectorMyPost.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_normal));
    }

    private void selectMyPost()
    {
        textViewPostSelectorAllPost.setBackgroundColor(Color.TRANSPARENT);
        textViewPostSelectorAllPost.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color_normal));

        textViewPostSelectorMyPost.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.btn_color));
        textViewPostSelectorMyPost.setTextColor(ContextCompat.getColor(getContext(), R.color.post_selector_font_color));
    }

    private void openScribbleDetailFragment(int index) {
        ScribbleDetailFragment fragment = ScribbleDetailFragment.newInstance(getContext(), index);
        ((MainActivity)getActivity()).openFragment(fragment);
    }


    @OnClick({R.id.textViewWriteOnBlockchain, R.id.imageViewWrite, R.id.textViewPostSelectorAllPost, R.id.textViewPostSelectorMyPost, R.id.imageViewWriteComplete})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewWriteOnBlockchain: {
                if (!isWriteEditorExpanded) {
                    expandWriteEditor();
                }
                break;
            }
            case R.id.imageViewWrite: {
                if (isWriteEditorExpanded) {
                    reduceWriteEditor();
                }
                break;
            }
            case R.id.textViewPostSelectorAllPost: {
                Toast.makeText(getContext(), "[Debug Toast] ScribbleFragment=textViewPostSelectorAllPost", Toast.LENGTH_SHORT).show();
                getPresenter().onAllPostSelected();
                selectAllPost();
                break;
            }
            case R.id.textViewPostSelectorMyPost: {
                Toast.makeText(getContext(), "[Debug Toast] ScribbleFragment=textViewPostSelectorMyPost", Toast.LENGTH_SHORT).show();
                getPresenter().onMyPostSelected();
                selectMyPost();
                break;
            }
            case R.id.imageViewWriteComplete: {
                getPresenter().write();
                reduceWriteEditor();

                /* For test */
                Toast.makeText(getContext(), "[Debug Toast] ScribbleFragment=imageViewWriteComplete", Toast.LENGTH_SHORT).show();
                editTextEditor.setText("");
                /* For test */

                break;
            }
        }
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            getPresenter().onRefresh();
        }
    };

    @Override
    public void onItemClick(View view, int pos) {
        Toast.makeText(getContext(), "[Debug Toast] listViewPost=onItemClickListener(row=" + String.valueOf(pos) + ")", Toast.LENGTH_SHORT).show();
        openScribbleDetailFragment(pos);
    }

    RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());

            /* ??? 무슨 코드인지 모르겠음 */ // TODO - setEnabled()를 변경해야 할 필요한 이유가 있는가?
            if (!swipeRefreshLayoutPostList.isRefreshing())
                if (manager.findFirstCompletelyVisibleItemPosition() == 0)
                    swipeRefreshLayoutPostList.setEnabled(true);
                else
                    swipeRefreshLayoutPostList.setEnabled(false);

            /* 최하단으로 스크롤시 추가 Write 보이기 */
            if (dy > 0) {
                if (!mLoadingFlag) {
                    visibleItemCount = manager.getChildCount();
                    totalItemCount = manager.getItemCount();
                    pastVisibleItems = manager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisibleItems) >= totalItemCount - 1) {
                        getPresenter().onLastItem(totalItemCount - 1);
                    }
                }
            }

            /* 위아래 Scrolling시 하단 메뉴 보이기/숨기기 애니메이션 관련 코드 */
            if (scrollMoved > 0 && dy < 0) {
                scrollMoved = 0;
            }
            else if (scrollMoved < 0 && dy > 0) {
                scrollMoved = 0;
            }
            scrollMoved += dy;
            if (scrollMoved > scrollThreshold) {
                if (isBottomMenuVisible) {
                    ((MainFragment) getParentFragment()).hideBottomBar();
                    isBottomMenuVisible = false;
                }
            } else if (scrollMoved < (-scrollThreshold)) {
                if (!isBottomMenuVisible) {
                    ((MainFragment) getParentFragment()).showBottomBar();
                    isBottomMenuVisible = true;
                }
            }
        }
    };

    private ScribbleInteractorImpl.SendTxCallBack sendCallback = new ScribbleInteractorImpl.SendTxCallBack() {
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
