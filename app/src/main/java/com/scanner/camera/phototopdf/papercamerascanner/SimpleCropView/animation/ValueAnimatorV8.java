package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.animation;

import android.os.SystemClock;
import android.view.animation.Interpolator;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ValueAnimatorV8 implements SimpleValueAnimator {
    private static final int DEFAULT_ANIMATION_DURATION = 150;
    private static final int FRAME_RATE = 30;
    private static final int UPDATE_SPAN = Math.round(33.333332f);

    public SimpleValueAnimatorListener animatorListener = new SimpleValueAnimatorListener() {
        public void onAnimationFinished() {
        }

        public void onAnimationStarted() {
        }

        public void onAnimationUpdated(float f) {
        }
    };
    long duration;
    boolean isAnimationStarted = false;

    public Interpolator mInterpolator;
    private final Runnable runnable = new Runnable() {
        public void run() {
            long uptimeMillis = SystemClock.uptimeMillis() - ValueAnimatorV8.this.start;
            if (uptimeMillis > ValueAnimatorV8.this.duration) {
                ValueAnimatorV8.this.isAnimationStarted = false;
                ValueAnimatorV8.this.animatorListener.onAnimationFinished();
                ValueAnimatorV8.this.service.shutdown();
                return;
            }
            ValueAnimatorV8.this.animatorListener.onAnimationUpdated(Math.min(ValueAnimatorV8.this.mInterpolator.getInterpolation(((float) uptimeMillis) / ((float) ValueAnimatorV8.this.duration)), 1.0f));
        }
    };
    ScheduledExecutorService service;
    long start;

    public ValueAnimatorV8(Interpolator interpolator) {
        this.mInterpolator = interpolator;
    }

    public void startAnimation(long j) {
        if (j >= 0) {
            this.duration = j;
        } else {
            this.duration = 150;
        }
        this.isAnimationStarted = true;
        this.animatorListener.onAnimationStarted();
        this.start = SystemClock.uptimeMillis();
        ScheduledExecutorService newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        this.service = newSingleThreadScheduledExecutor;
        newSingleThreadScheduledExecutor.scheduleAtFixedRate(this.runnable, 0, (long) UPDATE_SPAN, TimeUnit.MILLISECONDS);
    }

    public void cancelAnimation() {
        this.isAnimationStarted = false;
        this.service.shutdown();
        this.animatorListener.onAnimationFinished();
    }

    public boolean isAnimationStarted() {
        return this.isAnimationStarted;
    }

    public void addAnimatorListener(SimpleValueAnimatorListener simpleValueAnimatorListener) {
        if (simpleValueAnimatorListener != null) {
            this.animatorListener = simpleValueAnimatorListener;
        }
    }
}
