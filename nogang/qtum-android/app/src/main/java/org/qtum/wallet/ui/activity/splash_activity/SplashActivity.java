package org.qtum.wallet.ui.activity.splash_activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;

import android.view.ViewTreeObserver;

import org.qtum.wallet.R;
import org.qtum.wallet.ui.base.base_activity.BaseActivity;
import org.qtum.wallet.ui.activity.main_activity.MainActivity;
import org.qtum.wallet.utils.AppIntent;

import butterknife.BindView;

public class SplashActivity extends BaseActivity implements SplashActivityView {
    private SplashActivityPresenter presenter;
    private static final int LAYOUT = R.layout.lyt_splash; //Todo: 처음 앱 기동시 표시할 레이아웃 작성

    @BindView(R.id.ic_app_logo)
    AppCompatImageView appLogo;

    private int appLogoHeight = 0;

    @Override
    public void initializeViews() {
        recolorStatusBar(R.color.background);

        // Todo : Splash 화면이 로딩된 뒤 DoTransaction(에니메이션 코드) 을 수행 하기 위한 코드 appLogo 의 사이즈가 0이라면 에니메이션 수행하지 않고
        // 로딩 된뒤 수행하도록 하기 위한것 응용하여 작성

        if (appLogo.getHeight() == 0) {
            appLogo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    appLogo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    appLogoHeight = (appLogoHeight == 0) ? appLogo.getHeight() : appLogoHeight;
                    DoTransition();
                }
            });
        } else {
            appLogoHeight = (appLogoHeight == 0) ? appLogo.getHeight() : appLogoHeight;
            DoTransition();
        }

        //앱 실행 후 2초 뒤에 MainActivity 수행하도록함.
        startHandler = new Handler();
        startHandler.postDelayed(startRunnable, 2000);

    }

    private void DoTransition() {
        //Todo : 에니메이션 코드 작성
    }

    @Override
    protected void updateTheme() {
    }

    Handler startHandler;
    Runnable startRunnable = new Runnable() {
        @Override
        public void run() {
            startApp();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        bindView();
    }

    @Override
    protected void onDestroy() {
        if (startHandler != null) {
            startHandler.removeCallbacks(startRunnable);
        }
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //Todo : 에니메이션 실행 코드 작성
    }

    public void recolorStatusBar(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), color));
        }
    }

    @Override
    protected void createPresenter() {
        presenter = new SplashActivityPresenterImpl(this, new SplashActivityInteractorImpl(getContext()));
    }

    @Override
    protected SplashActivityPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void startApp() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction(AppIntent.USER_START_APP);
        startActivity(intent);
    }
}