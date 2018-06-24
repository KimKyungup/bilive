package org.qtum.wallet.ui.fragment.fragment_introduce;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import org.qtum.wallet.R;
import org.qtum.wallet.datastorage.ScribbleSharedPreference;
import org.qtum.wallet.ui.base.base_fragment.BaseFragment;
import org.qtum.wallet.ui.fragment.fragment_input_fingerprint.InputFingerprintFragment;
import org.qtum.wallet.ui.fragment.fragment_input_password.InputPasswordAction;
import org.qtum.wallet.ui.fragment.fragment_input_password.InputPasswordFragment;
import org.qtum.wallet.ui.fragment_factory.Factory;

import butterknife.BindView;
import butterknife.OnClick;

public class IntroduceFragment extends BaseFragment implements IIntroduceView {

    private IIntroducePresenter mFragmentPresenter;

    private IntroduceFragmentPagerAdapter mFragmentPagerAdapter;

    @BindView(R.id.viewPagerIntroduce)
    ViewPager viewPagerIntroduce;

    @BindView(R.id.imageViewPageIndicator_1)
    ImageView imageViewPageIndicator_1;

    @BindView(R.id.imageViewPageIndicator_2)
    ImageView imageViewPageIndicator_2;

    @BindView(R.id.imageViewPageIndicator_3)
    ImageView imageViewPageIndicator_3;

    @BindView(R.id.buttonIntroduceStart)
    Button buttonIntroduceStart;

    // Introduce Page가 어디서 어디로 Swipe 되었는지 확인하기 위한 변수
    private int prevPage = 0;

    public static BaseFragment newInstance(Context context) {
        Bundle args = new Bundle();
        BaseFragment fragment = Factory.instantiateFragment(context, IntroduceFragment.class);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void createPresenter() {
        mFragmentPresenter = new IntroducePresenterImpl(this, new IntroduceInteractorImpl(getContext()));
    }

    @Override
    protected IIntroducePresenter getPresenter() {
        return mFragmentPresenter;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_introduce;
    }

    @Override
    public void initializeViews() {
        super.initializeViews();

        // Init Pager Adapter
        mFragmentPagerAdapter = new IntroduceFragmentPagerAdapter(getChildFragmentManager());

        // Assign Adapter
        viewPagerIntroduce.setAdapter(mFragmentPagerAdapter);

        // Page 변경 이벤트 등록
        viewPagerIntroduce.addOnPageChangeListener(onPageChangeListener);

    }

    private void updateDotState(int position)
    {
        switch (position) {
            case IntroduceChildFragment.INTRODUCE_PAGE_1: {
                imageViewPageIndicator_1.setImageResource(R.drawable.intro_dot_selected);
                imageViewPageIndicator_2.setImageResource(R.drawable.intro_dot_s);
                imageViewPageIndicator_3.setImageResource(R.drawable.intro_dot_s);
                break;
            }
            case IntroduceChildFragment.INTRODUCE_PAGE_2: {
                imageViewPageIndicator_1.setImageResource(R.drawable.intro_dot_s);
                imageViewPageIndicator_2.setImageResource(R.drawable.intro_dot_selected);
                imageViewPageIndicator_3.setImageResource(R.drawable.intro_dot_s);
                break;
            }
            case IntroduceChildFragment.INTRODUCE_PAGE_3: {
                imageViewPageIndicator_1.setImageResource(R.drawable.intro_dot_s);
                imageViewPageIndicator_2.setImageResource(R.drawable.intro_dot_s);
                imageViewPageIndicator_3.setImageResource(R.drawable.intro_dot_selected);
                break;
            }
        }
    }

    private void handleStartButton(int position)
    {
        if (prevPage == IntroduceChildFragment.INTRODUCE_PAGE_MAX - 2 && position == IntroduceChildFragment.INTRODUCE_PAGE_MAX - 1) {
            showStartButton();
        }
        else if (prevPage == IntroduceChildFragment.INTRODUCE_PAGE_MAX - 1 && position == IntroduceChildFragment.INTRODUCE_PAGE_MAX - 2) {
            hideStartButton();
        }
        prevPage = position;
    }

    // Introduce Page 변경
    private void setPage(int position)
    {
        if (position < IntroduceChildFragment.INTRODUCE_PAGE_MAX - 1) {
            viewPagerIntroduce.setCurrentItem(position + 1, true);
        }
    }

    // [시작하기] 버튼 나타내기 (+애니메이션)
    private void showStartButton()
    {
        buttonIntroduceStart.setVisibility(View.VISIBLE);
        buttonIntroduceStart.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.introduce_start_button_enter));
    }

    // [시작하기] 버튼 숨기기 (+애니메이션)
    private void hideStartButton()
    {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.introduce_start_button_exit);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Do nothing
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 애니메이션이 종료되면 Button을 숨긴다.
                buttonIntroduceStart.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        buttonIntroduceStart.startAnimation(animation);
    }

    private void openInputPasswordFragment()
    {
        Fragment fragment;
        if ( ScribbleSharedPreference.getInstance().getKeyGeneratedInstance(getContext()) == true ) {
            if (ScribbleSharedPreference.getInstance().getTouchIdEnable(getContext()) == true) {
                fragment = InputFingerprintFragment.newInstance(getContext());
            }
            else {
                fragment = InputPasswordFragment.newInstance(getContext(), InputPasswordAction.AUTHENTICATION);
            }
        }
        else {
            fragment = InputPasswordFragment.newInstance(getContext(), InputPasswordAction.REGISTER);
        }
        openRootFragment(fragment);
    }

    @OnClick({R.id.textViewIntroduceSkip, R.id.textViewIntroduceNext, R.id.buttonIntroduceStart})
    void onClick(View view) {
        switch (view.getId()) {
            // 건너뛰기, 시작하기
            case R.id.textViewIntroduceSkip:
            case R.id.buttonIntroduceStart: {
                openInputPasswordFragment();
                break;
            }
            // 다음
            case R.id.textViewIntroduceNext: {
                int position = viewPagerIntroduce.getCurrentItem();
                setPage(position);
                break;
            }
        }
    }

    // View Pager 관련 이벤트 리스너
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Do nothing
        }

        @Override
        public void onPageSelected(int position) {
            updateDotState(position);
            handleStartButton(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            // Do nothing
        }
    };
}
