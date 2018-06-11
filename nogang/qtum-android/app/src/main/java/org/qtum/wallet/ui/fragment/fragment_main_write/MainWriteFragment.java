package org.qtum.wallet.ui.fragment.fragment_main_write;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.fragment_main.IMainBottomBarManager;
import org.qtum.wallet.ui.fragment.fragment_main.IMainChildFragmentManager;

import butterknife.BindView;
import butterknife.OnClick;

public class MainWriteFragment extends BaseFragment implements MainWriteView {

    private MainWritePresenter mFragmentPresenter;
    private IMainBottomBarManager mainBottomBarManager;
    private IMainChildFragmentManager mainChildFragmentManager;

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

    @BindView(R.id.listViewPost)
    ListView listViewPost;

    private ListViewPostAdapter mListViewPostAdapter = null;

    private boolean isWriteEditorExpanded = false;

    private int scrollThreshold;
    private int prevScrollPosition = 0;
    private boolean isBottomMenuVisible = true;

    public static MainWriteFragment newInstance() {
        Bundle args = new Bundle();
        MainWriteFragment fragment = new MainWriteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new MainWritePresenterImpl(this, new MainWriteInteractorImpl(getContext()));
    }

    @Override
    protected MainWritePresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_write;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initializeViews() {
        super.initializeViews();

        scrollThreshold = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getContext().getResources().getDisplayMetrics());
        swipeRefreshLayoutPostList.setOnRefreshListener(onRefreshListener);

        mListViewPostAdapter = new ListViewPostAdapter(getContext());
        listViewPost.setAdapter(mListViewPostAdapter);
        listViewPost.setOnItemClickListener(onItemClickListener);
        listViewPost.setOnTouchListener(onTouchListener);

        String testStr = "존경하는 남과 북의 국민 여러분, 해외 동포 여러분. 김정은 위원장과 나는 평화를 바라는 8천만 겨레의 염원으로 역사적인 만남을 갖고 귀중한 합의를 이루었습니다. 한반도에 더 이상 전쟁은 없을 것이며 새로운 평화의 시대가 열리고 있음을 함께 선언하였습니다. 긴 세월 동안 분단의 아픔과 서러움 속에서도 끝내 극복할 수 있다고 믿었기에 우리는 이 자리에 설 수 있었습니다. #판문점선언\n" +
                "\n" +
                "오늘 김정은 위원장과 나는 완전한 비핵화를 통해 핵 없는 한반도를 실현하는 것이 우리의 공동 목표라는 것을 확인했습니다.\n" +
                "\n" +
                "북측이 먼저 취한 핵 동결 조치들은 대단히 중대한 의미를 가지고 있습니다. 한반도의 완전한 비핵화를 위한 소중한 출발이 될 것입니다. 앞으로 완전한 비핵화를 위해 남과 북이 더욱 긴밀히 협력해 나갈 것을 분명히 밝힙니다.\n" +
                "\n" +
                "우리는 또한 종전선언과 평화협정을 통해 한반도의 불안정한 정전 체제를 종식시키고 항구적이고 공고한 평화체제를 구축해나가기로 합의했습니다. 한반도를 둘러싼 국제 질서를 근본적으로 바꿀 수 있는 매우 중요한 합의입니다. \n" +
                "\n" +
                "#판문점 #문재인 #김정은 #넘어가 #한 #두자 #세글자 #네글자아 #글_양옆10";
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");
        mListViewPostAdapter.addItem(testStr, "2018년 4월 27일, 오전 10시 17분 | 0.004225 QTUM | 내 글");

        selectAllPost();

    }

    public void setMainBottomBarManager(IMainBottomBarManager mainBottomBarManager) {
        this.mainBottomBarManager = mainBottomBarManager;
    }

    public void setMainChildFragmentManager(IMainChildFragmentManager mainChildFragmentManager) {
        this.mainChildFragmentManager = mainChildFragmentManager;
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
                getPresenter().onAllPostSelected();
                selectAllPost();
                break;
            }
            case R.id.textViewPostSelectorMyPost: {
                getPresenter().onMyPostSelected();
                selectMyPost();
                break;
            }
            case R.id.imageViewWriteComplete: {
                getPresenter().onWriteComplete();
                reduceWriteEditor();

                /* For test */
                Toast.makeText(getContext(), "성공!! (for Test Toast)", Toast.LENGTH_SHORT).show();
                editTextEditor.setText("");
                /* For test */

                break;
            }
        }
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            Handler testHandler = new Handler();
            testHandler.postDelayed(testRunnable, 1000);
        }
    };

    ListView.OnItemClickListener onItemClickListener = new ListView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //Toast.makeText(getContext(), String.valueOf(i + 1) + "번째 선택", Toast.LENGTH_SHORT).show();
            mainChildFragmentManager.openMainWriteDetailFragment();
        }
    };

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    prevScrollPosition = (int) motionEvent.getY();
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    int mScrollY = (int) motionEvent.getY();

                    if (isBottomMenuVisible) {
                        if (prevScrollPosition - scrollThreshold > mScrollY) {
                            mainBottomBarManager.hideBottomBar();
                            isBottomMenuVisible = false;
                        }
                        else if (prevScrollPosition < mScrollY) {
                            prevScrollPosition = mScrollY;
                        }
                    }
                    else {
                        if (prevScrollPosition + scrollThreshold < mScrollY) {
                            mainBottomBarManager.showBottomBar();
                            isBottomMenuVisible = true;
                        }
                        else if (prevScrollPosition > mScrollY) {
                            prevScrollPosition = mScrollY;
                        }
                    }
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    prevScrollPosition = (int) motionEvent.getY();
                    break;
                }
            }
            return false;
        }
    };

    Runnable testRunnable = new Runnable() {
        @Override
        public void run() {
            swipeRefreshLayoutPostList.setRefreshing(false);
        }
    };


}
