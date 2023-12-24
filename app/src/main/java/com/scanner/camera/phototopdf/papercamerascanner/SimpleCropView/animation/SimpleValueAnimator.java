package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.animation;

public interface SimpleValueAnimator {
    void addAnimatorListener(SimpleValueAnimatorListener simpleValueAnimatorListener);

    void cancelAnimation();

    boolean isAnimationStarted();

    void startAnimation(long j);
}
