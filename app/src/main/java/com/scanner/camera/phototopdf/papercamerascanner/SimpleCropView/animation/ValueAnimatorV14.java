package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.animation;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.animation.Interpolator;

public class ValueAnimatorV14 implements SimpleValueAnimator, Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {
    private static final int DEFAULT_ANIMATION_DURATION = 150;
    private ValueAnimator animator;
    private SimpleValueAnimatorListener animatorListener = new SimpleValueAnimatorListener() {
        public void onAnimationFinished() {
        }

        public void onAnimationStarted() {
        }

        public void onAnimationUpdated(float f) {
        }
    };

    public void onAnimationRepeat(Animator animator2) {
    }

    public ValueAnimatorV14(Interpolator interpolator) {
        ValueAnimator ofFloat = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.animator = ofFloat;
        ofFloat.addListener(this);
        this.animator.addUpdateListener(this);
        this.animator.setInterpolator(interpolator);
    }

    public void startAnimation(long j) {
        if (j >= 0) {
            this.animator.setDuration(j);
        } else {
            this.animator.setDuration(150);
        }
        this.animator.start();
    }

    public void cancelAnimation() {
        this.animator.cancel();
    }

    public boolean isAnimationStarted() {
        return this.animator.isStarted();
    }

    public void addAnimatorListener(SimpleValueAnimatorListener simpleValueAnimatorListener) {
        if (simpleValueAnimatorListener != null) {
            this.animatorListener = simpleValueAnimatorListener;
        }
    }

    public void onAnimationStart(Animator animator2) {
        this.animatorListener.onAnimationStarted();
    }

    public void onAnimationEnd(Animator animator2) {
        this.animatorListener.onAnimationFinished();
    }

    public void onAnimationCancel(Animator animator2) {
        this.animatorListener.onAnimationFinished();
    }

    public void onAnimationUpdate(ValueAnimator valueAnimator) {
        this.animatorListener.onAnimationUpdated(valueAnimator.getAnimatedFraction());
    }
}
