package org.qtum.wallet.ui.wave_visualizer;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import java.util.ArrayList;
import java.util.List;

public class WaveHelper {
    private WaveView mWaveView;

    private AnimatorSet mAnimatorSet;

    public WaveHelper(WaveView waveView) {
        mWaveView = waveView;
        initAnimation();
    }

    public void start() {
        mWaveView.setShowWave(true);
        if (mAnimatorSet != null) {
            mAnimatorSet.start();
        }
    }

    private void initAnimation() {
        List<Animator> animators = new ArrayList<>();

        ObjectAnimator waveWidthAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveLengthRatio", 1f, .8f);
        waveWidthAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveWidthAnim.setRepeatMode(ValueAnimator.REVERSE);
        waveWidthAnim.setDuration(5000);
        waveWidthAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveWidthAnim);

        ObjectAnimator waveShiftAnim = ObjectAnimator.ofFloat(
                mWaveView, "waveShiftRatio", 0f, 1f);
        waveShiftAnim.setRepeatCount(ValueAnimator.INFINITE);
        waveShiftAnim.setDuration(10000);
        waveShiftAnim.setInterpolator(new LinearInterpolator());
        animators.add(waveShiftAnim);

        ObjectAnimator waterLevelAnim = ObjectAnimator.ofFloat(
                mWaveView, "waterLevelRatio", .4f, .5f);
        waterLevelAnim.setDuration(10000);
        waterLevelAnim.setInterpolator(new DecelerateInterpolator());
        waterLevelAnim.setRepeatCount(ValueAnimator.INFINITE);
        waterLevelAnim.setRepeatMode(ValueAnimator.REVERSE);
        animators.add(waterLevelAnim);

        ObjectAnimator amplitudeAnim = ObjectAnimator.ofFloat(
                mWaveView, "amplitudeRatio", 0.05f, 0.12f);
        amplitudeAnim.setRepeatCount(ValueAnimator.INFINITE);
        amplitudeAnim.setRepeatMode(ValueAnimator.REVERSE);
        amplitudeAnim.setDuration(10000);
        amplitudeAnim.setInterpolator(new LinearInterpolator());
        animators.add(amplitudeAnim);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.playTogether(animators);
    }

    public void cancel() {
        if (mAnimatorSet != null) {
            mAnimatorSet.end();
        }
    }
}
