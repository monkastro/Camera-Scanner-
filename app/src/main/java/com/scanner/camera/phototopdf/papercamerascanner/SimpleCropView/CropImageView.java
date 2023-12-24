package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import androidx.exifinterface.media.ExifInterface;
import com.bumptech.glide.Registry;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.animation.SimpleValueAnimator;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.animation.SimpleValueAnimatorListener;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.animation.ValueAnimatorV14;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.animation.ValueAnimatorV8;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.callback.Callback;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.callback.CropCallback;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.callback.LoadCallback;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.callback.SaveCallback;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.util.Logger;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.util.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class CropImageView extends androidx.appcompat.widget.AppCompatImageView {
    private static final int DEBUG_TEXT_SIZE_IN_DP = 15;
    private static final int DEFAULT_ANIMATION_DURATION_MILLIS = 100;
    private static final float DEFAULT_INITIAL_FRAME_SCALE = 1.0f;
    private static final int FRAME_STROKE_WEIGHT_IN_DP = 1;
    private static final int GUIDE_STROKE_WEIGHT_IN_DP = 1;
    private static final int HANDLE_SIZE_IN_DP = 14;
    private static final int MIN_FRAME_SIZE_IN_DP = 50;
    private static final String TAG = CropImageView.class.getSimpleName();
    private static final int TRANSLUCENT_BLACK = -1157627904;
    private static final int TRANSLUCENT_WHITE = -1140850689;
    private static final int TRANSPARENT = 0;
    private static final int WHITE = -1;
    private final Interpolator DEFAULT_INTERPOLATOR;
    /* access modifiers changed from: private */
    public float mAngle;
    private int mAnimationDurationMillis;
    private SimpleValueAnimator mAnimator;
    private int mBackgroundColor;
    private PointF mCenter;
    private Bitmap.CompressFormat mCompressFormat;
    private int mCompressQuality;
    private CropMode mCropMode;
    private PointF mCustomRatio;
    private ExecutorService mExecutor;
    /* access modifiers changed from: private */
    public int mExifRotation;
    private int mFrameColor;
    /* access modifiers changed from: private */
    public RectF mFrameRect;
    private float mFrameStrokeWeight;
    private int mGuideColor;
    private ShowMode mGuideShowMode;
    private float mGuideStrokeWeight;
    private int mHandleColor;
    private ShowMode mHandleShowMode;
    private int mHandleSize;
    /* access modifiers changed from: private */
    public Handler mHandler;
    private RectF mImageRect;
    private float mImgHeight;
    private float mImgWidth;
    /* access modifiers changed from: private */
    public RectF mInitialFrameRect;
    private float mInitialFrameScale;
    private int mInputImageHeight;
    private int mInputImageWidth;
    private Interpolator mInterpolator;
    /* access modifiers changed from: private */
    public boolean mIsAnimating;
    private boolean mIsAnimationEnabled;
    private boolean mIsCropEnabled;
    /* access modifiers changed from: private */
    public AtomicBoolean mIsCropping;
    /* access modifiers changed from: private */
    public boolean mIsDebug;
    private boolean mIsEnabled;
    private boolean mIsHandleShadowEnabled;
    private boolean mIsInitialized;
    /* access modifiers changed from: private */
    public AtomicBoolean mIsLoading;
    /* access modifiers changed from: private */
    public boolean mIsRotating;
    /* access modifiers changed from: private */
    public AtomicBoolean mIsSaving;
    private float mLastX;
    private float mLastY;
    private Matrix mMatrix;
    private float mMinFrameSize;
    private int mOutputHeight;
    private int mOutputImageHeight;
    private int mOutputImageWidth;
    private int mOutputMaxHeight;
    private int mOutputMaxWidth;
    private int mOutputWidth;
    private int mOverlayColor;
    private Paint mPaintBitmap;
    private Paint mPaintDebug;
    private Paint mPaintFrame;
    private Paint mPaintTranslucent;
    private Uri mSaveUri;
    /* access modifiers changed from: private */
    public float mScale;
    private boolean mShowGuide;
    private boolean mShowHandle;
    /* access modifiers changed from: private */
    public Uri mSourceUri;
    private TouchArea mTouchArea;
    private int mTouchPadding;
    /* access modifiers changed from: private */
    public int mViewHeight;
    /* access modifiers changed from: private */
    public int mViewWidth;

    private enum TouchArea {
        OUT_OF_BOUNDS,
        CENTER,
        LEFT_TOP,
        RIGHT_TOP,
        LEFT_BOTTOM,
        RIGHT_BOTTOM
    }

    private float constrain(float f, float f2, float f3, float f4) {
        return (f < f2 || f > f3) ? f4 : f;
    }

    private float getRotatedHeight(float f, float f2, float f3) {
        return f % 180.0f == 0.0f ? f3 : f2;
    }

    private float getRotatedWidth(float f, float f2, float f3) {
        return f % 180.0f == 0.0f ? f2 : f3;
    }

    /* renamed from: sq */
    private float m87sq(float f) {
        return f * f;
    }

    public CropImageView(Context context) {
        this(context, (AttributeSet) null);
    }

    public CropImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CropImageView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mViewWidth = 0;
        this.mViewHeight = 0;
        this.mScale = 1.0f;
        this.mAngle = 0.0f;
        this.mImgWidth = 0.0f;
        this.mImgHeight = 0.0f;
        this.mIsInitialized = false;
        this.mMatrix = null;
        this.mCenter = new PointF();
        this.mIsRotating = false;
        this.mIsAnimating = false;
        this.mAnimator = null;
        DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
        this.DEFAULT_INTERPOLATOR = decelerateInterpolator;
        this.mInterpolator = decelerateInterpolator;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mSourceUri = null;
        this.mSaveUri = null;
        this.mExifRotation = 0;
        this.mOutputWidth = 0;
        this.mOutputHeight = 0;
        this.mIsDebug = false;
        this.mCompressFormat = Bitmap.CompressFormat.PNG;
        this.mCompressQuality = 100;
        this.mInputImageWidth = 0;
        this.mInputImageHeight = 0;
        this.mOutputImageWidth = 0;
        this.mOutputImageHeight = 0;
        this.mIsLoading = new AtomicBoolean(false);
        this.mIsCropping = new AtomicBoolean(false);
        this.mIsSaving = new AtomicBoolean(false);
        this.mTouchArea = TouchArea.OUT_OF_BOUNDS;
        this.mCropMode = CropMode.SQUARE;
        this.mGuideShowMode = ShowMode.SHOW_ALWAYS;
        this.mHandleShowMode = ShowMode.SHOW_ALWAYS;
        this.mTouchPadding = 0;
        this.mShowGuide = true;
        this.mShowHandle = true;
        this.mIsCropEnabled = true;
        this.mIsEnabled = true;
        this.mCustomRatio = new PointF(1.0f, 1.0f);
        this.mFrameStrokeWeight = 2.0f;
        this.mGuideStrokeWeight = 2.0f;
        this.mIsAnimationEnabled = true;
        this.mAnimationDurationMillis = 100;
        this.mIsHandleShadowEnabled = true;
        this.mExecutor = Executors.newSingleThreadExecutor();
        float density = getDensity();
        this.mHandleSize = (int) (14.0f * density);
        this.mMinFrameSize = 50.0f * density;
        float f = density * 1.0f;
        this.mFrameStrokeWeight = f;
        this.mGuideStrokeWeight = f;
        this.mPaintFrame = new Paint();
        this.mPaintTranslucent = new Paint();
        Paint paint = new Paint();
        this.mPaintBitmap = paint;
        paint.setFilterBitmap(true);
        Paint paint2 = new Paint();
        this.mPaintDebug = paint2;
        paint2.setAntiAlias(true);
        this.mPaintDebug.setStyle(Paint.Style.STROKE);
        this.mPaintDebug.setColor(-1);
        this.mPaintDebug.setTextSize(15.0f * density);
        this.mMatrix = new Matrix();
        this.mScale = 1.0f;
        this.mBackgroundColor = 0;
        this.mFrameColor = -1;
        this.mOverlayColor = TRANSLUCENT_BLACK;
        this.mHandleColor = -1;
        this.mGuideColor = TRANSLUCENT_WHITE;
        handleStyleable(context, attributeSet, i, density);
    }

    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mode = this.mCropMode;
        savedState.backgroundColor = this.mBackgroundColor;
        savedState.overlayColor = this.mOverlayColor;
        savedState.frameColor = this.mFrameColor;
        savedState.guideShowMode = this.mGuideShowMode;
        savedState.handleShowMode = this.mHandleShowMode;
        savedState.showGuide = this.mShowGuide;
        savedState.showHandle = this.mShowHandle;
        savedState.handleSize = this.mHandleSize;
        savedState.touchPadding = this.mTouchPadding;
        savedState.minFrameSize = this.mMinFrameSize;
        savedState.customRatioX = this.mCustomRatio.x;
        savedState.customRatioY = this.mCustomRatio.y;
        savedState.frameStrokeWeight = this.mFrameStrokeWeight;
        savedState.guideStrokeWeight = this.mGuideStrokeWeight;
        savedState.isCropEnabled = this.mIsCropEnabled;
        savedState.handleColor = this.mHandleColor;
        savedState.guideColor = this.mGuideColor;
        savedState.initialFrameScale = this.mInitialFrameScale;
        savedState.angle = this.mAngle;
        savedState.isAnimationEnabled = this.mIsAnimationEnabled;
        savedState.animationDuration = this.mAnimationDurationMillis;
        savedState.exifRotation = this.mExifRotation;
        savedState.sourceUri = this.mSourceUri;
        savedState.saveUri = this.mSaveUri;
        savedState.compressFormat = this.mCompressFormat;
        savedState.compressQuality = this.mCompressQuality;
        savedState.isDebug = this.mIsDebug;
        savedState.outputMaxWidth = this.mOutputMaxWidth;
        savedState.outputMaxHeight = this.mOutputMaxHeight;
        savedState.outputWidth = this.mOutputWidth;
        savedState.outputHeight = this.mOutputHeight;
        savedState.isHandleShadowEnabled = this.mIsHandleShadowEnabled;
        savedState.inputImageWidth = this.mInputImageWidth;
        savedState.inputImageHeight = this.mInputImageHeight;
        savedState.outputImageWidth = this.mOutputImageWidth;
        savedState.outputImageHeight = this.mOutputImageHeight;
        return savedState;
    }

    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        this.mCropMode = savedState.mode;
        this.mBackgroundColor = savedState.backgroundColor;
        this.mOverlayColor = savedState.overlayColor;
        this.mFrameColor = savedState.frameColor;
        this.mGuideShowMode = savedState.guideShowMode;
        this.mHandleShowMode = savedState.handleShowMode;
        this.mShowGuide = savedState.showGuide;
        this.mShowHandle = savedState.showHandle;
        this.mHandleSize = savedState.handleSize;
        this.mTouchPadding = savedState.touchPadding;
        this.mMinFrameSize = savedState.minFrameSize;
        this.mCustomRatio = new PointF(savedState.customRatioX, savedState.customRatioY);
        this.mFrameStrokeWeight = savedState.frameStrokeWeight;
        this.mGuideStrokeWeight = savedState.guideStrokeWeight;
        this.mIsCropEnabled = savedState.isCropEnabled;
        this.mHandleColor = savedState.handleColor;
        this.mGuideColor = savedState.guideColor;
        this.mInitialFrameScale = savedState.initialFrameScale;
        this.mAngle = savedState.angle;
        this.mIsAnimationEnabled = savedState.isAnimationEnabled;
        this.mAnimationDurationMillis = savedState.animationDuration;
        this.mExifRotation = savedState.exifRotation;
        this.mSourceUri = savedState.sourceUri;
        this.mSaveUri = savedState.saveUri;
        this.mCompressFormat = savedState.compressFormat;
        this.mCompressQuality = savedState.compressQuality;
        this.mIsDebug = savedState.isDebug;
        this.mOutputMaxWidth = savedState.outputMaxWidth;
        this.mOutputMaxHeight = savedState.outputMaxHeight;
        this.mOutputWidth = savedState.outputWidth;
        this.mOutputHeight = savedState.outputHeight;
        this.mIsHandleShadowEnabled = savedState.isHandleShadowEnabled;
        this.mInputImageWidth = savedState.inputImageWidth;
        this.mInputImageHeight = savedState.inputImageHeight;
        this.mOutputImageWidth = savedState.outputImageWidth;
        this.mOutputImageHeight = savedState.outputImageHeight;
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int size = MeasureSpec.getSize(i);
        int size2 = MeasureSpec.getSize(i2);
        setMeasuredDimension(size, size2);
        this.mViewWidth = (size - getPaddingLeft()) - getPaddingRight();
        this.mViewHeight = (size2 - getPaddingTop()) - getPaddingBottom();
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        if (getDrawable() != null) {
            setupLayout(this.mViewWidth, this.mViewHeight);
        }
    }

    public void onDraw(Canvas canvas) {
        canvas.drawColor(this.mBackgroundColor);
        if (this.mIsInitialized) {
            setMatrix();
            Bitmap bitmap = getBitmap();
            if (bitmap != null) {
                canvas.drawBitmap(bitmap, this.mMatrix, this.mPaintBitmap);
                drawCropFrame(canvas);
            }
            if (this.mIsDebug) {
                drawDebugInfo(canvas);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        this.mExecutor.shutdown();
        super.onDetachedFromWindow();
    }

    private void handleStyleable(Context context, AttributeSet attributeSet, int i, float f) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.scv_CropImageView, i, 0);
        this.mCropMode = CropMode.SQUARE;
        try {
            Drawable drawable = obtainStyledAttributes.getDrawable(14);
            if (drawable != null) {
                setImageDrawable(drawable);
            }
            CropMode[] values = CropMode.values();
            int length = values.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                CropMode cropMode = values[i2];
                if (obtainStyledAttributes.getInt(4, 3) == cropMode.getId()) {
                    this.mCropMode = cropMode;
                    break;
                }
                i2++;
            }
            this.mBackgroundColor = obtainStyledAttributes.getColor(2, 0);
            this.mOverlayColor = obtainStyledAttributes.getColor(17, TRANSLUCENT_BLACK);
            this.mFrameColor = obtainStyledAttributes.getColor(5, -1);
            this.mHandleColor = obtainStyledAttributes.getColor(10, -1);
            this.mGuideColor = obtainStyledAttributes.getColor(7, TRANSLUCENT_WHITE);
            ShowMode[] values2 = ShowMode.values();
            int length2 = values2.length;
            int i3 = 0;
            while (true) {
                if (i3 >= length2) {
                    break;
                }
                ShowMode showMode = values2[i3];
                if (obtainStyledAttributes.getInt(8, 1) == showMode.getId()) {
                    this.mGuideShowMode = showMode;
                    break;
                }
                i3++;
            }
            ShowMode[] values3 = ShowMode.values();
            int length3 = values3.length;
            int i4 = 0;
            while (true) {
                if (i4 >= length3) {
                    break;
                }
                ShowMode showMode2 = values3[i4];
                if (obtainStyledAttributes.getInt(12, 1) == showMode2.getId()) {
                    this.mHandleShowMode = showMode2;
                    break;
                }
                i4++;
            }
            setGuideShowMode(this.mGuideShowMode);
            setHandleShowMode(this.mHandleShowMode);
            this.mHandleSize = obtainStyledAttributes.getDimensionPixelSize(13, (int) (14.0f * f));
            this.mTouchPadding = obtainStyledAttributes.getDimensionPixelSize(18, 0);
            this.mMinFrameSize = (float) obtainStyledAttributes.getDimensionPixelSize(16, (int) (50.0f * f));
            int i5 = (int) (f * 1.0f);
            this.mFrameStrokeWeight = (float) obtainStyledAttributes.getDimensionPixelSize(6, i5);
            this.mGuideStrokeWeight = (float) obtainStyledAttributes.getDimensionPixelSize(9, i5);
            this.mIsCropEnabled = obtainStyledAttributes.getBoolean(3, true);
            this.mInitialFrameScale = constrain(obtainStyledAttributes.getFloat(15, 1.0f), 0.01f, 1.0f, 1.0f);
            this.mIsAnimationEnabled = obtainStyledAttributes.getBoolean(1, true);
            this.mAnimationDurationMillis = obtainStyledAttributes.getInt(0, 100);
            this.mIsHandleShadowEnabled = obtainStyledAttributes.getBoolean(11, true);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable th) {
            obtainStyledAttributes.recycle();
            throw th;
        }
        obtainStyledAttributes.recycle();
    }

    private void drawDebugInfo(Canvas canvas) {
        StringBuilder sb;
        Paint.FontMetrics fontMetrics = this.mPaintDebug.getFontMetrics();
        this.mPaintDebug.measureText(ExifInterface.LONGITUDE_WEST);
        float f = fontMetrics.descent;
        float f2 = fontMetrics.ascent;
        float f3 = this.mImageRect.left;
        getDensity();
        float f4 = this.mImageRect.top;
        getDensity();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("LOADED FROM: ");
        sb2.append(this.mSourceUri != null ? "Uri" : Registry.BUCKET_BITMAP);
        StringBuilder sb3 = new StringBuilder();
        if (this.mSourceUri == null) {
            sb3.append("INPUT_IMAGE_SIZE: ");
            sb3.append((int) this.mImgWidth);
            sb3.append("x");
            sb3.append((int) this.mImgHeight);
            sb = new StringBuilder();
        } else {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("INPUT_IMAGE_SIZE: ");
            sb4.append(this.mInputImageWidth);
            sb4.append("x");
            sb4.append(this.mInputImageHeight);
            sb = new StringBuilder();
        }
        sb.append("LOADED_IMAGE_SIZE: ");
        sb.append(getBitmap().getWidth());
        sb.append("x");
        sb.append(getBitmap().getHeight());
        StringBuilder sb5 = new StringBuilder();
        if (this.mOutputImageWidth > 0 && this.mOutputImageHeight > 0) {
            sb5.append("OUTPUT_IMAGE_SIZE: ");
            sb5.append(this.mOutputImageWidth);
            sb5.append("x");
            sb5.append(this.mOutputImageHeight);
            StringBuilder sb6 = new StringBuilder();
            sb6.append("EXIF ROTATION: ");
            sb6.append(this.mExifRotation);
            StringBuilder sb7 = new StringBuilder();
            sb7.append("CURRENT_ROTATION: ");
            sb7.append((int) this.mAngle);
        }
        StringBuilder sb8 = new StringBuilder();
        sb8.append("FRAME_RECT: ");
        sb8.append(this.mFrameRect.toString());
        StringBuilder sb9 = new StringBuilder();
        sb9.append("ACTUAL_CROP_RECT: ");
        sb9.append(getActualCropRect() != null ? getActualCropRect().toString() : "");
    }

    private void drawCropFrame(Canvas canvas) {
        if (this.mIsCropEnabled && !this.mIsRotating) {
            drawOverlay(canvas);
            drawFrame(canvas);
            if (this.mShowGuide) {
                drawGuidelines(canvas);
            }
            if (this.mShowHandle) {
                drawHandles(canvas);
            }
        }
    }

    private void drawOverlay(Canvas canvas) {
        this.mPaintTranslucent.setAntiAlias(true);
        this.mPaintTranslucent.setFilterBitmap(true);
        this.mPaintTranslucent.setColor(this.mOverlayColor);
        this.mPaintTranslucent.setStyle(Paint.Style.FILL);
        Path path = new Path();
        RectF rectF = new RectF((float) Math.floor((double) this.mImageRect.left), (float) Math.floor((double) this.mImageRect.top), (float) Math.ceil((double) this.mImageRect.right), (float) Math.ceil((double) this.mImageRect.bottom));
        if (this.mIsAnimating || !(this.mCropMode == CropMode.CIRCLE || this.mCropMode == CropMode.CIRCLE_SQUARE)) {
            path.addRect(rectF, Path.Direction.CW);
            path.addRect(this.mFrameRect, Path.Direction.CCW);
            canvas.drawPath(path, this.mPaintTranslucent);
            return;
        }
        path.addRect(rectF, Path.Direction.CW);
        PointF pointF = new PointF((this.mFrameRect.left + this.mFrameRect.right) / 2.0f, (this.mFrameRect.top + this.mFrameRect.bottom) / 2.0f);
        path.addCircle(pointF.x, pointF.y, (this.mFrameRect.right - this.mFrameRect.left) / 2.0f, Path.Direction.CCW);
        canvas.drawPath(path, this.mPaintTranslucent);
    }

    private void drawFrame(Canvas canvas) {
        this.mPaintFrame.setAntiAlias(true);
        this.mPaintFrame.setFilterBitmap(true);
        this.mPaintFrame.setStyle(Paint.Style.STROKE);
        this.mPaintFrame.setColor(this.mFrameColor);
        this.mPaintFrame.setStrokeWidth(this.mFrameStrokeWeight);
        canvas.drawRect(this.mFrameRect, this.mPaintFrame);
    }

    private void drawGuidelines(Canvas canvas) {
        this.mPaintFrame.setColor(this.mGuideColor);
        this.mPaintFrame.setStrokeWidth(this.mGuideStrokeWeight);
        float f = this.mFrameRect.left + ((this.mFrameRect.right - this.mFrameRect.left) / 3.0f);
        float f2 = this.mFrameRect.right - ((this.mFrameRect.right - this.mFrameRect.left) / 3.0f);
        float f3 = this.mFrameRect.top + ((this.mFrameRect.bottom - this.mFrameRect.top) / 3.0f);
        float f4 = this.mFrameRect.bottom - ((this.mFrameRect.bottom - this.mFrameRect.top) / 3.0f);
        canvas.drawLine(f, this.mFrameRect.top, f, this.mFrameRect.bottom, this.mPaintFrame);
        canvas.drawLine(f2, this.mFrameRect.top, f2, this.mFrameRect.bottom, this.mPaintFrame);
        canvas.drawLine(this.mFrameRect.left, f3, this.mFrameRect.right, f3, this.mPaintFrame);
        canvas.drawLine(this.mFrameRect.left, f4, this.mFrameRect.right, f4, this.mPaintFrame);
    }

    private void drawHandles(Canvas canvas) {
        if (this.mIsHandleShadowEnabled) {
            drawHandleShadows(canvas);
        }
        this.mPaintFrame.setStyle(Paint.Style.FILL);
        this.mPaintFrame.setColor(this.mHandleColor);
        canvas.drawCircle(this.mFrameRect.left, this.mFrameRect.top, (float) this.mHandleSize, this.mPaintFrame);
        canvas.drawCircle(this.mFrameRect.right, this.mFrameRect.top, (float) this.mHandleSize, this.mPaintFrame);
        canvas.drawCircle(this.mFrameRect.left, this.mFrameRect.bottom, (float) this.mHandleSize, this.mPaintFrame);
        canvas.drawCircle(this.mFrameRect.right, this.mFrameRect.bottom, (float) this.mHandleSize, this.mPaintFrame);
    }

    private void drawHandleShadows(Canvas canvas) {
        this.mPaintFrame.setStyle(Paint.Style.FILL);
        this.mPaintFrame.setColor(TRANSLUCENT_BLACK);
        RectF rectF = new RectF(this.mFrameRect);
        rectF.offset(0.0f, 1.0f);
        canvas.drawCircle(rectF.left, rectF.top, (float) this.mHandleSize, this.mPaintFrame);
        canvas.drawCircle(rectF.right, rectF.top, (float) this.mHandleSize, this.mPaintFrame);
        canvas.drawCircle(rectF.left, rectF.bottom, (float) this.mHandleSize, this.mPaintFrame);
        canvas.drawCircle(rectF.right, rectF.bottom, (float) this.mHandleSize, this.mPaintFrame);
    }

    /* access modifiers changed from: private */
    public void setMatrix() {
        this.mMatrix.reset();
        this.mMatrix.setTranslate(this.mCenter.x - (this.mImgWidth * 0.5f), this.mCenter.y - (this.mImgHeight * 0.5f));
        Matrix matrix = this.mMatrix;
        float f = this.mScale;
        matrix.postScale(f, f, this.mCenter.x, this.mCenter.y);
        this.mMatrix.postRotate(this.mAngle, this.mCenter.x, this.mCenter.y);
    }

    /* access modifiers changed from: private */
    public void setupLayout(int i, int i2) {
        if (i != 0 && i2 != 0) {
            setCenter(new PointF(((float) getPaddingLeft()) + (((float) i) * 0.5f), ((float) getPaddingTop()) + (((float) i2) * 0.5f)));
            setScale(calcScale(i, i2, this.mAngle));
            setMatrix();
            RectF calcImageRect = calcImageRect(new RectF(0.0f, 0.0f, this.mImgWidth, this.mImgHeight), this.mMatrix);
            this.mImageRect = calcImageRect;
            RectF rectF = this.mInitialFrameRect;
            if (rectF != null) {
                this.mFrameRect = applyInitialFrameRect(rectF);
            } else {
                this.mFrameRect = calcFrameRect(calcImageRect);
            }
            this.mIsInitialized = true;
            invalidate();
        }
    }

    private float calcScale(int i, int i2, float f) {
        this.mImgWidth = (float) getDrawable().getIntrinsicWidth();
        this.mImgHeight = (float) getDrawable().getIntrinsicHeight();
        if (this.mImgWidth <= 0.0f) {
            this.mImgWidth = (float) i;
        }
        if (this.mImgHeight <= 0.0f) {
            this.mImgHeight = (float) i2;
        }
        float f2 = (float) i;
        float f3 = (float) i2;
        float f4 = f2 / f3;
        float rotatedWidth = getRotatedWidth(f) / getRotatedHeight(f);
        if (rotatedWidth >= f4) {
            return f2 / getRotatedWidth(f);
        }
        if (rotatedWidth < f4) {
            return f3 / getRotatedHeight(f);
        }
        return 1.0f;
    }

    private RectF calcImageRect(RectF rectF, Matrix matrix) {
        RectF rectF2 = new RectF();
        matrix.mapRect(rectF2, rectF);
        return rectF2;
    }

    private RectF calcFrameRect(RectF rectF) {
        float ratioX = getRatioX(rectF.width());
        float ratioY = getRatioY(rectF.height());
        float width = rectF.width() / rectF.height();
        float f = ratioX / ratioY;
        float f2 = rectF.left;
        float f3 = rectF.top;
        float f4 = rectF.right;
        float f5 = rectF.bottom;
        if (f >= width) {
            f2 = rectF.left;
            f4 = rectF.right;
            float f6 = (rectF.top + rectF.bottom) * 0.5f;
            float width2 = (rectF.width() / f) * 0.5f;
            f3 = f6 - width2;
            f5 = f6 + width2;
        } else if (f < width) {
            f3 = rectF.top;
            f5 = rectF.bottom;
            float f7 = (rectF.left + rectF.right) * 0.5f;
            float height = rectF.height() * f * 0.5f;
            f4 = f7 + height;
            f2 = f7 - height;
        }
        float f8 = f4 - f2;
        float f9 = f5 - f3;
        float f10 = f2 + (f8 / 2.0f);
        float f11 = f3 + (f9 / 2.0f);
        float f12 = this.mInitialFrameScale;
        float f13 = (f8 * f12) / 2.0f;
        float f14 = (f9 * f12) / 2.0f;
        return new RectF(f10 - f13, f11 - f14, f10 + f13, f11 + f14);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mIsInitialized || !this.mIsCropEnabled || !this.mIsEnabled || this.mIsRotating || this.mIsAnimating || this.mIsLoading.get() || this.mIsCropping.get()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 0) {
            onDown(motionEvent);
            return true;
        } else if (action == 1) {
            getParent().requestDisallowInterceptTouchEvent(false);
            onUp(motionEvent);
            return true;
        } else if (action == 2) {
            onMove(motionEvent);
            if (this.mTouchArea != TouchArea.OUT_OF_BOUNDS) {
                getParent().requestDisallowInterceptTouchEvent(true);
            }
            return true;
        } else if (action != 3) {
            return false;
        } else {
            getParent().requestDisallowInterceptTouchEvent(false);
            onCancel();
            return true;
        }
    }

    private void onDown(MotionEvent motionEvent) {
        invalidate();
        this.mLastX = motionEvent.getX();
        this.mLastY = motionEvent.getY();
        checkTouchArea(motionEvent.getX(), motionEvent.getY());
    }

    private void onMove(MotionEvent motionEvent) {
        float x = motionEvent.getX() - this.mLastX;
        float y = motionEvent.getY() - this.mLastY;
        int i = this.mTouchArea.ordinal();
        if (i == 1) {
            moveFrame(x, y);
        } else if (i == 2) {
            moveHandleLT(x, y);
        } else if (i == 3) {
            moveHandleRT(x, y);
        } else if (i == 4) {
            moveHandleLB(x, y);
        } else if (i == 5) {
            moveHandleRB(x, y);
        }
        invalidate();
        this.mLastX = motionEvent.getX();
        this.mLastY = motionEvent.getY();
    }

    private void onUp(MotionEvent motionEvent) {
        if (this.mGuideShowMode == ShowMode.SHOW_ON_TOUCH) {
            this.mShowGuide = false;
        }
        if (this.mHandleShowMode == ShowMode.SHOW_ON_TOUCH) {
            this.mShowHandle = false;
        }
        this.mTouchArea = TouchArea.OUT_OF_BOUNDS;
        invalidate();
    }

    private void onCancel() {
        this.mTouchArea = TouchArea.OUT_OF_BOUNDS;
        invalidate();
    }

    private void checkTouchArea(float f, float f2) {
        if (isInsideCornerLeftTop(f, f2)) {
            this.mTouchArea = TouchArea.LEFT_TOP;
            if (this.mHandleShowMode == ShowMode.SHOW_ON_TOUCH) {
                this.mShowHandle = true;
            }
            if (this.mGuideShowMode == ShowMode.SHOW_ON_TOUCH) {
                this.mShowGuide = true;
            }
        } else if (isInsideCornerRightTop(f, f2)) {
            this.mTouchArea = TouchArea.RIGHT_TOP;
            if (this.mHandleShowMode == ShowMode.SHOW_ON_TOUCH) {
                this.mShowHandle = true;
            }
            if (this.mGuideShowMode == ShowMode.SHOW_ON_TOUCH) {
                this.mShowGuide = true;
            }
        } else if (isInsideCornerLeftBottom(f, f2)) {
            this.mTouchArea = TouchArea.LEFT_BOTTOM;
            if (this.mHandleShowMode == ShowMode.SHOW_ON_TOUCH) {
                this.mShowHandle = true;
            }
            if (this.mGuideShowMode == ShowMode.SHOW_ON_TOUCH) {
                this.mShowGuide = true;
            }
        } else if (isInsideCornerRightBottom(f, f2)) {
            this.mTouchArea = TouchArea.RIGHT_BOTTOM;
            if (this.mHandleShowMode == ShowMode.SHOW_ON_TOUCH) {
                this.mShowHandle = true;
            }
            if (this.mGuideShowMode == ShowMode.SHOW_ON_TOUCH) {
                this.mShowGuide = true;
            }
        } else if (isInsideFrame(f, f2)) {
            if (this.mGuideShowMode == ShowMode.SHOW_ON_TOUCH) {
                this.mShowGuide = true;
            }
            this.mTouchArea = TouchArea.CENTER;
        } else {
            this.mTouchArea = TouchArea.OUT_OF_BOUNDS;
        }
    }

    private boolean isInsideFrame(float f, float f2) {
        if (this.mFrameRect.left > f || this.mFrameRect.right < f || this.mFrameRect.top > f2 || this.mFrameRect.bottom < f2) {
            return false;
        }
        this.mTouchArea = TouchArea.CENTER;
        return true;
    }

    private boolean isInsideCornerLeftTop(float f, float f2) {
        float f3 = f - this.mFrameRect.left;
        float f4 = f2 - this.mFrameRect.top;
        return m87sq((float) (this.mHandleSize + this.mTouchPadding)) >= (f3 * f3) + (f4 * f4);
    }

    private boolean isInsideCornerRightTop(float f, float f2) {
        float f3 = f - this.mFrameRect.right;
        float f4 = f2 - this.mFrameRect.top;
        return m87sq((float) (this.mHandleSize + this.mTouchPadding)) >= (f3 * f3) + (f4 * f4);
    }

    private boolean isInsideCornerLeftBottom(float f, float f2) {
        float f3 = f - this.mFrameRect.left;
        float f4 = f2 - this.mFrameRect.bottom;
        return m87sq((float) (this.mHandleSize + this.mTouchPadding)) >= (f3 * f3) + (f4 * f4);
    }

    private boolean isInsideCornerRightBottom(float f, float f2) {
        float f3 = f - this.mFrameRect.right;
        float f4 = f2 - this.mFrameRect.bottom;
        return m87sq((float) (this.mHandleSize + this.mTouchPadding)) >= (f3 * f3) + (f4 * f4);
    }

    private void moveFrame(float f, float f2) {
        this.mFrameRect.left += f;
        this.mFrameRect.right += f;
        this.mFrameRect.top += f2;
        this.mFrameRect.bottom += f2;
        checkMoveBounds();
    }

    private void moveHandleLT(float f, float f2) {
        if (this.mCropMode == CropMode.FREE) {
            this.mFrameRect.left += f;
            this.mFrameRect.top += f2;
            if (isWidthTooSmall()) {
                this.mFrameRect.left -= this.mMinFrameSize - getFrameW();
            }
            if (isHeightTooSmall()) {
                this.mFrameRect.top -= this.mMinFrameSize - getFrameH();
            }
            checkScaleBounds();
            return;
        }
        float ratioY = (getRatioY() * f) / getRatioX();
        this.mFrameRect.left += f;
        this.mFrameRect.top += ratioY;
        if (isWidthTooSmall()) {
            float frameW = this.mMinFrameSize - getFrameW();
            this.mFrameRect.left -= frameW;
            this.mFrameRect.top -= (frameW * getRatioY()) / getRatioX();
        }
        if (isHeightTooSmall()) {
            float frameH = this.mMinFrameSize - getFrameH();
            this.mFrameRect.top -= frameH;
            this.mFrameRect.left -= (frameH * getRatioX()) / getRatioY();
        }
        if (!isInsideHorizontal(this.mFrameRect.left)) {
            float f3 = this.mImageRect.left - this.mFrameRect.left;
            this.mFrameRect.left += f3;
            this.mFrameRect.top += (f3 * getRatioY()) / getRatioX();
        }
        if (!isInsideVertical(this.mFrameRect.top)) {
            float f4 = this.mImageRect.top - this.mFrameRect.top;
            this.mFrameRect.top += f4;
            this.mFrameRect.left += (f4 * getRatioX()) / getRatioY();
        }
    }

    private void moveHandleRT(float f, float f2) {
        if (this.mCropMode == CropMode.FREE) {
            this.mFrameRect.right += f;
            this.mFrameRect.top += f2;
            if (isWidthTooSmall()) {
                this.mFrameRect.right += this.mMinFrameSize - getFrameW();
            }
            if (isHeightTooSmall()) {
                this.mFrameRect.top -= this.mMinFrameSize - getFrameH();
            }
            checkScaleBounds();
            return;
        }
        float ratioY = (getRatioY() * f) / getRatioX();
        this.mFrameRect.right += f;
        this.mFrameRect.top -= ratioY;
        if (isWidthTooSmall()) {
            float frameW = this.mMinFrameSize - getFrameW();
            this.mFrameRect.right += frameW;
            this.mFrameRect.top -= (frameW * getRatioY()) / getRatioX();
        }
        if (isHeightTooSmall()) {
            float frameH = this.mMinFrameSize - getFrameH();
            this.mFrameRect.top -= frameH;
            this.mFrameRect.right += (frameH * getRatioX()) / getRatioY();
        }
        if (!isInsideHorizontal(this.mFrameRect.right)) {
            float f3 = this.mFrameRect.right - this.mImageRect.right;
            this.mFrameRect.right -= f3;
            this.mFrameRect.top += (f3 * getRatioY()) / getRatioX();
        }
        if (!isInsideVertical(this.mFrameRect.top)) {
            float f4 = this.mImageRect.top - this.mFrameRect.top;
            this.mFrameRect.top += f4;
            this.mFrameRect.right -= (f4 * getRatioX()) / getRatioY();
        }
    }

    private void moveHandleLB(float f, float f2) {
        if (this.mCropMode == CropMode.FREE) {
            this.mFrameRect.left += f;
            this.mFrameRect.bottom += f2;
            if (isWidthTooSmall()) {
                this.mFrameRect.left -= this.mMinFrameSize - getFrameW();
            }
            if (isHeightTooSmall()) {
                this.mFrameRect.bottom += this.mMinFrameSize - getFrameH();
            }
            checkScaleBounds();
            return;
        }
        float ratioY = (getRatioY() * f) / getRatioX();
        this.mFrameRect.left += f;
        this.mFrameRect.bottom -= ratioY;
        if (isWidthTooSmall()) {
            float frameW = this.mMinFrameSize - getFrameW();
            this.mFrameRect.left -= frameW;
            this.mFrameRect.bottom += (frameW * getRatioY()) / getRatioX();
        }
        if (isHeightTooSmall()) {
            float frameH = this.mMinFrameSize - getFrameH();
            this.mFrameRect.bottom += frameH;
            this.mFrameRect.left -= (frameH * getRatioX()) / getRatioY();
        }
        if (!isInsideHorizontal(this.mFrameRect.left)) {
            float f3 = this.mImageRect.left - this.mFrameRect.left;
            this.mFrameRect.left += f3;
            this.mFrameRect.bottom -= (f3 * getRatioY()) / getRatioX();
        }
        if (!isInsideVertical(this.mFrameRect.bottom)) {
            float f4 = this.mFrameRect.bottom - this.mImageRect.bottom;
            this.mFrameRect.bottom -= f4;
            this.mFrameRect.left += (f4 * getRatioX()) / getRatioY();
        }
    }

    private void moveHandleRB(float f, float f2) {
        if (this.mCropMode == CropMode.FREE) {
            this.mFrameRect.right += f;
            this.mFrameRect.bottom += f2;
            if (isWidthTooSmall()) {
                this.mFrameRect.right += this.mMinFrameSize - getFrameW();
            }
            if (isHeightTooSmall()) {
                this.mFrameRect.bottom += this.mMinFrameSize - getFrameH();
            }
            checkScaleBounds();
            return;
        }
        float ratioY = (getRatioY() * f) / getRatioX();
        this.mFrameRect.right += f;
        this.mFrameRect.bottom += ratioY;
        if (isWidthTooSmall()) {
            float frameW = this.mMinFrameSize - getFrameW();
            this.mFrameRect.right += frameW;
            this.mFrameRect.bottom += (frameW * getRatioY()) / getRatioX();
        }
        if (isHeightTooSmall()) {
            float frameH = this.mMinFrameSize - getFrameH();
            this.mFrameRect.bottom += frameH;
            this.mFrameRect.right += (frameH * getRatioX()) / getRatioY();
        }
        if (!isInsideHorizontal(this.mFrameRect.right)) {
            float f3 = this.mFrameRect.right - this.mImageRect.right;
            this.mFrameRect.right -= f3;
            this.mFrameRect.bottom -= (f3 * getRatioY()) / getRatioX();
        }
        if (!isInsideVertical(this.mFrameRect.bottom)) {
            float f4 = this.mFrameRect.bottom - this.mImageRect.bottom;
            this.mFrameRect.bottom -= f4;
            this.mFrameRect.right -= (f4 * getRatioX()) / getRatioY();
        }
    }

    private void checkScaleBounds() {
        float f = this.mFrameRect.left - this.mImageRect.left;
        float f2 = this.mFrameRect.right - this.mImageRect.right;
        float f3 = this.mFrameRect.top - this.mImageRect.top;
        float f4 = this.mFrameRect.bottom - this.mImageRect.bottom;
        if (f < 0.0f) {
            this.mFrameRect.left -= f;
        }
        if (f2 > 0.0f) {
            this.mFrameRect.right -= f2;
        }
        if (f3 < 0.0f) {
            this.mFrameRect.top -= f3;
        }
        if (f4 > 0.0f) {
            this.mFrameRect.bottom -= f4;
        }
    }

    private void checkMoveBounds() {
        float f = this.mFrameRect.left - this.mImageRect.left;
        if (f < 0.0f) {
            this.mFrameRect.left -= f;
            this.mFrameRect.right -= f;
        }
        float f2 = this.mFrameRect.right - this.mImageRect.right;
        if (f2 > 0.0f) {
            this.mFrameRect.left -= f2;
            this.mFrameRect.right -= f2;
        }
        float f3 = this.mFrameRect.top - this.mImageRect.top;
        if (f3 < 0.0f) {
            this.mFrameRect.top -= f3;
            this.mFrameRect.bottom -= f3;
        }
        float f4 = this.mFrameRect.bottom - this.mImageRect.bottom;
        if (f4 > 0.0f) {
            this.mFrameRect.top -= f4;
            this.mFrameRect.bottom -= f4;
        }
    }

    private boolean isInsideHorizontal(float f) {
        return this.mImageRect.left <= f && this.mImageRect.right >= f;
    }

    private boolean isInsideVertical(float f) {
        return this.mImageRect.top <= f && this.mImageRect.bottom >= f;
    }

    private boolean isWidthTooSmall() {
        return getFrameW() < this.mMinFrameSize;
    }

    private boolean isHeightTooSmall() {
        return getFrameH() < this.mMinFrameSize;
    }

    private void recalculateFrameRect(int i) {
        if (this.mImageRect != null) {
            if (this.mIsAnimating) {
                getAnimator().cancelAnimation();
            }
            final RectF rectF = new RectF(this.mFrameRect);
            final RectF calcFrameRect = calcFrameRect(this.mImageRect);
            final float f = calcFrameRect.left - rectF.left;
            final float f2 = calcFrameRect.top - rectF.top;
            final float f3 = calcFrameRect.right - rectF.right;
            final float f4 = calcFrameRect.bottom - rectF.bottom;
            if (this.mIsAnimationEnabled) {
                SimpleValueAnimator animator = getAnimator();
                animator.addAnimatorListener(new SimpleValueAnimatorListener() {
                    public void onAnimationStarted() {
                        boolean unused = CropImageView.this.mIsAnimating = true;
                    }

                    public void onAnimationUpdated(float f) {
                        RectF unused = CropImageView.this.mFrameRect = new RectF(rectF.left + (f * f), rectF.top + (f2 * f), rectF.right + (f3 * f), rectF.bottom + (f4 * f));
                        CropImageView.this.invalidate();
                    }

                    public void onAnimationFinished() {
                        RectF unused = CropImageView.this.mFrameRect = calcFrameRect;
                        CropImageView.this.invalidate();
                        boolean unused2 = CropImageView.this.mIsAnimating = false;
                    }
                });
                animator.startAnimation((long) i);
                return;
            }
            this.mFrameRect = calcFrameRect(this.mImageRect);
            invalidate();
        }
    }

    private float getRatioX(float f) {
        switch (this.mCropMode.ordinal()) {
            case 1:
                return this.mImageRect.width();
            case 3:
                return 4.0f;
            case 4:
                return 3.0f;
            case 5:
                return 16.0f;
            case 6:
                return 9.0f;
            case 7:
            case 8:
            case 9:
                return 1.0f;
            case 10:
                return this.mCustomRatio.x;
            default:
                return f;
        }
    }

    private float getRatioY(float f) {
        switch (this.mCropMode.ordinal()) {
            case 1:
                return this.mImageRect.height();
            case 3:
                return 3.0f;
            case 4:
                return 4.0f;
            case 5:
                return 9.0f;
            case 6:
                return 16.0f;
            case 7:
            case 8:
            case 9:
                return 1.0f;
            case 10:
                return this.mCustomRatio.y;
            default:
                return f;
        }
    }

    private float getRatioX() {
        int i = this.mCropMode.ordinal();
        if (i == 1) {
            return this.mImageRect.width();
        }
        if (i == 10) {
            return this.mCustomRatio.x;
        }
        if (i == 3) {
            return 4.0f;
        }
        if (i == 4) {
            return 3.0f;
        }
        if (i != 5) {
            return i != 6 ? 1.0f : 9.0f;
        }
        return 16.0f;
    }

    private float getRatioY() {
        int i = this.mCropMode.ordinal();
        if (i == 1) {
            return this.mImageRect.height();
        }
        if (i == 10) {
            return this.mCustomRatio.y;
        }
        if (i == 3) {
            return 3.0f;
        }
        if (i == 4) {
            return 4.0f;
        }
        if (i != 5) {
            return i != 6 ? 1.0f : 16.0f;
        }
        return 9.0f;
    }

    private float getDensity() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) getContext().getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.density;
    }

    /* access modifiers changed from: private */
    public void postErrorOnMainThread(final Callback callback, final Throwable th) {
        if (callback != null) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                callback.onError(th);
            } else {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        callback.onError(th);
                    }
                });
            }
        }
    }

    private Bitmap getBitmap() {
        Drawable drawable = getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }
        return ((BitmapDrawable) drawable).getBitmap();
    }

    private float getRotatedWidth(float f) {
        return getRotatedWidth(f, this.mImgWidth, this.mImgHeight);
    }

    private float getRotatedHeight(float f) {
        return getRotatedHeight(f, this.mImgWidth, this.mImgHeight);
    }

    private Bitmap getRotatedBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.setRotate(this.mAngle, (float) (bitmap.getWidth() / 2), (float) (bitmap.getHeight() / 2));
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private SimpleValueAnimator getAnimator() {
        setupAnimatorIfNeeded();
        return this.mAnimator;
    }

    private void setupAnimatorIfNeeded() {
        if (this.mAnimator != null) {
            return;
        }
        if (Build.VERSION.SDK_INT < 14) {
            this.mAnimator = new ValueAnimatorV8(this.mInterpolator);
        } else {
            this.mAnimator = new ValueAnimatorV14(this.mInterpolator);
        }
    }

    private Bitmap getCroppedBitmapFromUri() throws IOException {
        InputStream inputStream = null;
        try {
            inputStream = getContext().getContentResolver().openInputStream(this.mSourceUri);
            BitmapRegionDecoder newInstance = BitmapRegionDecoder.newInstance(inputStream, false);
            int width = newInstance.getWidth();
            int height = newInstance.getHeight();
            Rect calcCropRect = calcCropRect(width, height);
            if (this.mAngle != 0.0f) {
                Matrix matrix = new Matrix();
                matrix.setRotate(-this.mAngle);
                RectF rectF = new RectF();
                matrix.mapRect(rectF, new RectF(calcCropRect));
                rectF.offset(rectF.left < 0.0f ? (float) width : 0.0f, rectF.top < 0.0f ? (float) height : 0.0f);
                calcCropRect = new Rect((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom);
            }
            Bitmap decodeRegion = newInstance.decodeRegion(calcCropRect, new BitmapFactory.Options());
            if (this.mAngle != 0.0f) {
                Bitmap rotatedBitmap = getRotatedBitmap(decodeRegion);
                if (!(decodeRegion == getBitmap() || decodeRegion == rotatedBitmap)) {
                    decodeRegion.recycle();
                }
                decodeRegion = rotatedBitmap;
            }
            return decodeRegion;
        } finally {
            Utils.closeQuietly(inputStream);
        }
    }

    private Rect calcCropRect(int i, int i2) {
        float f = (float) i;
        float f2 = (float) i2;
        float rotatedWidth = getRotatedWidth(this.mAngle, f, f2) / this.mImageRect.width();
        float f3 = this.mImageRect.left * rotatedWidth;
        float f4 = this.mImageRect.top * rotatedWidth;
        int round = Math.round((this.mFrameRect.left * rotatedWidth) - f3);
        int round2 = Math.round((this.mFrameRect.top * rotatedWidth) - f4);
        int round3 = Math.round((this.mFrameRect.right * rotatedWidth) - f3);
        int round4 = Math.round((this.mFrameRect.bottom * rotatedWidth) - f4);
        return new Rect(Math.max(round, 0), Math.max(round2, 0), Math.min(round3, Math.round(getRotatedWidth(this.mAngle, f, f2))), Math.min(round4, Math.round(getRotatedHeight(this.mAngle, f, f2))));
    }

    private Bitmap scaleBitmapIfNeeded(Bitmap bitmap) {
        int i;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float ratioX = getRatioX(this.mFrameRect.width()) / getRatioY(this.mFrameRect.height());
        int i2 = this.mOutputWidth;
        int i3 = 0;
        if (i2 > 0) {
            i3 = Math.round(((float) i2) / ratioX);
        } else {
            int i4 = this.mOutputHeight;
            if (i4 > 0) {
                i3 = i4;
                i2 = Math.round(((float) i4) * ratioX);
            } else {
                int i5 = this.mOutputMaxWidth;
                if (i5 <= 0 || (i = this.mOutputMaxHeight) <= 0 || (width <= i5 && height <= i)) {
                    i2 = 0;
                } else {
                    i2 = this.mOutputMaxWidth;
                    i3 = this.mOutputMaxHeight;
                    if (((float) i2) / ((float) i3) >= ratioX) {
                        i2 = Math.round(((float) i3) * ratioX);
                    } else {
                        i3 = Math.round(((float) i2) / ratioX);
                    }
                }
            }
        }
        if (i2 <= 0 || i3 <= 0) {
            return bitmap;
        }
        Bitmap scaledBitmap = Utils.getScaledBitmap(bitmap, i2, i3);
        if (!(bitmap == getBitmap() || bitmap == scaledBitmap)) {
            bitmap.recycle();
        }
        return scaledBitmap;
    }


    public Uri saveImage(Bitmap bitmap, Uri uri) throws Throwable {
        this.mSaveUri = uri;
        if (uri != null) {
            OutputStream outputStream = null;
            try {
                outputStream = getContext().getContentResolver().openOutputStream(uri);
                bitmap.compress(this.mCompressFormat, this.mCompressQuality, outputStream);
                Utils.copyExifInfo(getContext(), this.mSourceUri, uri, bitmap.getWidth(), bitmap.getHeight());
                Utils.updateGalleryInfo(getContext(), uri);
                return uri;
            } finally {
                Utils.closeQuietly(outputStream);
            }
        } else {
            throw new IllegalStateException("Save uri must not be null.");
        }
    }

    public Bitmap getImageBitmap() {
        return getBitmap();
    }

    public void setImageBitmap(Bitmap bitmap) {
        super.setImageBitmap(bitmap);
    }

    public void setImageResource(int i) {
        this.mIsInitialized = false;
        resetImageInfo();
        super.setImageResource(i);
        updateLayout();
    }

    public void setImageDrawable(Drawable drawable) {
        this.mIsInitialized = false;
        resetImageInfo();
        setImageDrawableInternal(drawable);
    }


    public void setImageDrawableInternal(Drawable drawable) {
        super.setImageDrawable(drawable);
        updateLayout();
    }

    public void setImageURI(Uri uri) {
        this.mIsInitialized = false;
        super.setImageURI(uri);
        updateLayout();
    }

    private void updateLayout() {
        if (getDrawable() != null) {
            setupLayout(this.mViewWidth, this.mViewHeight);
        }
    }

    private void resetImageInfo() {
        if (!this.mIsLoading.get()) {
            this.mSourceUri = null;
            this.mSaveUri = null;
            this.mInputImageWidth = 0;
            this.mInputImageHeight = 0;
            this.mOutputImageWidth = 0;
            this.mOutputImageHeight = 0;
            this.mAngle = (float) this.mExifRotation;
        }
    }

    public void startLoad(Uri uri, LoadCallback loadCallback) {
        loadAsync(uri, loadCallback);
    }

    public void loadAsync(Uri uri, LoadCallback loadCallback) {
        loadAsync(uri, false, (RectF) null, loadCallback);
    }

    public void loadAsync(Uri uri, boolean z, RectF rectF, LoadCallback loadCallback) {
        final Uri uri2 = uri;
        final RectF rectF2 = rectF;
        final boolean z2 = z;
        final LoadCallback loadCallback2 = loadCallback;
        this.mExecutor.submit(new Runnable() {
            public void run() {
                try {
                    CropImageView.this.mIsLoading.set(true);
                    Uri unused = CropImageView.this.mSourceUri = uri2;
                    RectF unused2 = CropImageView.this.mInitialFrameRect = rectF2;
                    if (z2) {
                        CropImageView.this.applyThumbnail(uri2);
                    }
                    final Bitmap access$600 = CropImageView.this.getImage(uri2);
                    CropImageView.this.mHandler.post(new Runnable() {
                        public void run() {
                            float unused = CropImageView.this.mAngle = (float) CropImageView.this.mExifRotation;
                            CropImageView.this.setImageDrawableInternal(new BitmapDrawable(CropImageView.this.getResources(), access$600));
                            if (loadCallback2 != null) {
                                loadCallback2.onSuccess();
                            }
                        }
                    });
                } catch (Exception e) {
                    CropImageView.this.postErrorOnMainThread(loadCallback2, e);
                } catch (Throwable th) {
                    CropImageView.this.mIsLoading.set(false);
                    try {
                        throw th;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                CropImageView.this.mIsLoading.set(false);
            }
        });
    }

    public Completable loadAsCompletable(Uri uri) {
        return loadAsCompletable(uri, false, (RectF) null);
    }

    public Completable loadAsCompletable(final Uri uri, final boolean z, final RectF rectF) {
        return Completable.create(new CompletableOnSubscribe() {
            public void subscribe(final CompletableEmitter completableEmitter) throws Exception {
                RectF unused = CropImageView.this.mInitialFrameRect = rectF;
                Uri unused2 = CropImageView.this.mSourceUri = uri;
                if (z) {
                    try {
                        CropImageView.this.applyThumbnail(uri);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                Bitmap bitmap = null;
                try {
                    bitmap = CropImageView.this.getImage(uri);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                Bitmap finalBitmap = bitmap;
                CropImageView.this.mHandler.post(new Runnable() {
                    public void run() {
                        float unused = CropImageView.this.mAngle = (float) CropImageView.this.mExifRotation;
                        CropImageView.this.setImageDrawableInternal(new BitmapDrawable(CropImageView.this.getResources(), finalBitmap));
                        completableEmitter.onComplete();
                    }
                });
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            public void accept(Disposable disposable) throws Exception {
                CropImageView.this.mIsLoading.set(true);
            }
        }).doFinally(new Action() {
            public void run() throws Exception {
                CropImageView.this.mIsLoading.set(false);
            }
        });
    }

    public LoadRequest load(Uri uri) {
        return new LoadRequest(this, uri);
    }


    public void applyThumbnail(Uri uri) throws Throwable {
        final Bitmap thumbnail = getThumbnail(uri);
        if (thumbnail != null) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    CropImageView cropImageView = CropImageView.this;
                    float unused = cropImageView.mAngle = (float) cropImageView.mExifRotation;
                    CropImageView.this.setImageDrawableInternal(new BitmapDrawable(CropImageView.this.getResources(), thumbnail));
                }
            });
        }
    }


    public Bitmap getImage(Uri uri) throws Throwable {
        if (uri != null) {
            this.mExifRotation = Utils.getExifOrientation(getContext(), this.mSourceUri);
            int maxSize = Utils.getMaxSize();
            int max = Math.max(this.mViewWidth, this.mViewHeight);
            if (max != 0) {
                maxSize = max;
            }
            Bitmap decodeSampledBitmapFromUri = Utils.decodeSampledBitmapFromUri(getContext(), this.mSourceUri, maxSize);
            this.mInputImageWidth = Utils.sInputImageWidth;
            this.mInputImageHeight = Utils.sInputImageHeight;
            return decodeSampledBitmapFromUri;
        }
        throw new IllegalStateException("Source Uri must not be null.");
    }

    private Bitmap getThumbnail(Uri uri) throws Throwable {
        if (uri != null) {
            this.mExifRotation = Utils.getExifOrientation(getContext(), this.mSourceUri);
            int max = (int) (((float) Math.max(this.mViewWidth, this.mViewHeight)) * 0.1f);
            if (max == 0) {
                return null;
            }
            Bitmap decodeSampledBitmapFromUri = Utils.decodeSampledBitmapFromUri(getContext(), this.mSourceUri, max);
            this.mInputImageWidth = Utils.sInputImageWidth;
            this.mInputImageHeight = Utils.sInputImageHeight;
            return decodeSampledBitmapFromUri;
        }
        throw new IllegalStateException("Source Uri must not be null.");
    }

    public void rotateImage(RotateDegrees rotateDegrees, int i) {
        if (this.mIsRotating) {
            getAnimator().cancelAnimation();
        }
        final float f = this.mAngle;
        final float value = f + ((float) rotateDegrees.getValue());
        final float f2 = value - f;
        final float f3 = this.mScale;
        final float calcScale = calcScale(this.mViewWidth, this.mViewHeight, value);
        if (this.mIsAnimationEnabled) {
            final float f4 = calcScale - f3;
            SimpleValueAnimator animator = getAnimator();
            animator.addAnimatorListener(new SimpleValueAnimatorListener() {
                public void onAnimationStarted() {
                    boolean unused = CropImageView.this.mIsRotating = true;
                }

                public void onAnimationUpdated(float f) {
                    float unused = CropImageView.this.mAngle = f + (f2 * f);
                    float unused2 = CropImageView.this.mScale = f3 + (f4 * f);
                    CropImageView.this.setMatrix();
                    CropImageView.this.invalidate();
                }

                public void onAnimationFinished() {
                    float unused = CropImageView.this.mAngle = value % 360.0f;
                    float unused2 = CropImageView.this.mScale = calcScale;
                    RectF unused3 = CropImageView.this.mInitialFrameRect = null;
                    CropImageView cropImageView = CropImageView.this;
                    cropImageView.setupLayout(cropImageView.mViewWidth, CropImageView.this.mViewHeight);
                    boolean unused4 = CropImageView.this.mIsRotating = false;
                }
            });
            animator.startAnimation((long) i);
            return;
        }
        this.mAngle = value % 360.0f;
        this.mScale = calcScale;
        setupLayout(this.mViewWidth, this.mViewHeight);
    }

    public void rotateImage(RotateDegrees rotateDegrees) {
        rotateImage(rotateDegrees, this.mAnimationDurationMillis);
    }

    public Bitmap getCroppedBitmap() {
        Bitmap bitmap = getBitmap();
        if (bitmap == null) {
            return null;
        }
        Bitmap rotatedBitmap = getRotatedBitmap(bitmap);
        Rect calcCropRect = calcCropRect(bitmap.getWidth(), bitmap.getHeight());
        Bitmap createBitmap = Bitmap.createBitmap(rotatedBitmap, calcCropRect.left, calcCropRect.top, calcCropRect.width(), calcCropRect.height(), (Matrix) null, false);
        if (!(rotatedBitmap == createBitmap || rotatedBitmap == bitmap)) {
            rotatedBitmap.recycle();
        }
        if (this.mCropMode != CropMode.CIRCLE) {
            return createBitmap;
        }
        Bitmap circularBitmap = getCircularBitmap(createBitmap);
        if (createBitmap != getBitmap()) {
            createBitmap.recycle();
        }
        return circularBitmap;
    }

    public Bitmap getCircularBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        Canvas canvas = new Canvas(createBitmap);
        int width = bitmap.getWidth() / 2;
        int height = bitmap.getHeight() / 2;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.drawCircle((float) width, (float) height, (float) Math.min(width, height), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return createBitmap;
    }

    public void startCrop(final Uri uri, final CropCallback cropCallback, final SaveCallback saveCallback) {
        this.mExecutor.submit(new Runnable() {
            public void run() {
                Bitmap bitmap = null;
                try {
                    CropImageView.this.mIsCropping.set(true);
                    bitmap = CropImageView.this.cropImage();
                    Bitmap finalBitmap = bitmap;
                    CropImageView.this.mHandler.post(new Runnable() {
                        public void run() {
                            if (cropCallback != null) {
                                cropCallback.onSuccess(finalBitmap);
                            }
                            if (CropImageView.this.mIsDebug) {
                                CropImageView.this.invalidate();
                            }
                        }
                    });
                    Uri unused = CropImageView.this.saveImage(bitmap, uri);
                    CropImageView.this.mHandler.post(new Runnable() {
                        public void run() {
                            if (saveCallback != null) {
                                saveCallback.onSuccess(uri);
                            }
                        }
                    });
                } catch (Exception e) {
                    if (bitmap == null) {
                        CropImageView.this.postErrorOnMainThread(cropCallback, e);
                    } else {
                        CropImageView.this.postErrorOnMainThread(saveCallback, e);
                    }
                } catch (Throwable th) {
                    CropImageView.this.mIsCropping.set(false);
                    try {
                        throw th;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                CropImageView.this.mIsCropping.set(false);
            }
        });
    }

    public void cropAsync(final Uri uri, final CropCallback cropCallback) {
        this.mExecutor.submit(new Runnable() {
            public void run() {
                try {
                    CropImageView.this.mIsCropping.set(true);
                    if (uri != null) {
                        Uri unused = CropImageView.this.mSourceUri = uri;
                    }
                    final Bitmap access$1900 = CropImageView.this.cropImage();
                    CropImageView.this.mHandler.post(new Runnable() {
                        public void run() {
                            if (cropCallback != null) {
                                cropCallback.onSuccess(access$1900);
                            }
                            if (CropImageView.this.mIsDebug) {
                                CropImageView.this.invalidate();
                            }
                        }
                    });
                } catch (Exception e) {
                    CropImageView.this.postErrorOnMainThread(cropCallback, e);
                } catch (Throwable th) {
                    CropImageView.this.mIsCropping.set(false);
                    throw th;
                }
                CropImageView.this.mIsCropping.set(false);
            }
        });
    }

    public void cropAsync(CropCallback cropCallback) {
        cropAsync((Uri) null, cropCallback);
    }

    public Single<Bitmap> cropAsSingle(final Uri uri) {
        return Single.fromCallable(new Callable<Bitmap>() {
            public Bitmap call() throws Exception {
                Uri uri = null;
                if (uri != null) {
                    Uri unused = CropImageView.this.mSourceUri = uri;
                }
                return CropImageView.this.cropImage();
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            public void accept(Disposable disposable) throws Exception {
                CropImageView.this.mIsCropping.set(true);
            }
        }).doFinally(new Action() {
            public void run() throws Exception {
                CropImageView.this.mIsCropping.set(false);
            }
        });
    }

    public Single<Bitmap> cropAsSingle() {
        return cropAsSingle((Uri) null);
    }

    public CropRequest crop(Uri uri) {
        return new CropRequest(this, uri);
    }

    public void saveAsync(final Uri uri, final Bitmap bitmap, final SaveCallback saveCallback) {
        this.mExecutor.submit(new Runnable() {
            public void run() {
                try {
                    CropImageView.this.mIsSaving.set(true);
                    Uri unused = CropImageView.this.saveImage(bitmap, uri);
                    CropImageView.this.mHandler.post(new Runnable() {
                        public void run() {
                            if (saveCallback != null) {
                                saveCallback.onSuccess(uri);
                            }
                        }
                    });
                } catch (Exception e) {
                    CropImageView.this.postErrorOnMainThread(saveCallback, e);
                } catch (Throwable th) {
                    CropImageView.this.mIsSaving.set(false);
                    try {
                        throw th;
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }
                CropImageView.this.mIsSaving.set(false);
            }
        });
    }

    public Single<Uri> saveAsSingle(final Bitmap bitmap, final Uri uri) {
        return Single.fromCallable(new Callable<Uri>() {
            public Uri call() throws Exception {
                try {
                    return CropImageView.this.saveImage(bitmap, uri);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                return null;
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            public void accept(Disposable disposable) throws Exception {
                CropImageView.this.mIsSaving.set(true);
            }
        }).doFinally(new Action() {
            public void run() throws Exception {
                CropImageView.this.mIsSaving.set(false);
            }
        });
    }

    public SaveRequest save(Bitmap bitmap) {
        return new SaveRequest(this, bitmap);
    }


    public Bitmap cropImage() throws IOException, IllegalStateException {
        Bitmap bitmap;
        if (this.mSourceUri == null) {
            bitmap = getCroppedBitmap();
        } else {
            bitmap = getCroppedBitmapFromUri();
            if (this.mCropMode == CropMode.CIRCLE) {
                Bitmap circularBitmap = getCircularBitmap(bitmap);
                if (bitmap != getBitmap()) {
                    bitmap.recycle();
                }
                bitmap = circularBitmap;
            }
        }
        Bitmap scaleBitmapIfNeeded = scaleBitmapIfNeeded(bitmap);
        this.mOutputImageWidth = scaleBitmapIfNeeded.getWidth();
        this.mOutputImageHeight = scaleBitmapIfNeeded.getHeight();
        return scaleBitmapIfNeeded;
    }

    public RectF getActualCropRect() {
        RectF rectF = this.mImageRect;
        if (rectF == null) {
            return null;
        }
        float f = rectF.left / this.mScale;
        float f2 = this.mImageRect.top / this.mScale;
        return new RectF(Math.max(0.0f, (this.mFrameRect.left / this.mScale) - f), Math.max(0.0f, (this.mFrameRect.top / this.mScale) - f2), Math.min(this.mImageRect.right / this.mScale, (this.mFrameRect.right / this.mScale) - f), Math.min(this.mImageRect.bottom / this.mScale, (this.mFrameRect.bottom / this.mScale) - f2));
    }

    private RectF applyInitialFrameRect(RectF rectF) {
        RectF rectF2 = new RectF();
        rectF2.set(rectF.left * this.mScale, rectF.top * this.mScale, rectF.right * this.mScale, rectF.bottom * this.mScale);
        rectF2.offset(this.mImageRect.left, this.mImageRect.top);
        rectF2.set(Math.max(this.mImageRect.left, rectF2.left), Math.max(this.mImageRect.top, rectF2.top), Math.min(this.mImageRect.right, rectF2.right), Math.min(this.mImageRect.bottom, rectF2.bottom));
        return rectF2;
    }

    public void setCropMode(CropMode cropMode, int i) {
        if (cropMode == CropMode.CUSTOM) {
            setCustomRatio(1, 1);
            return;
        }
        this.mCropMode = cropMode;
        recalculateFrameRect(i);
    }

    public void setCropMode(CropMode cropMode) {
        setCropMode(cropMode, this.mAnimationDurationMillis);
    }

    public void setCustomRatio(int i, int i2, int i3) {
        if (i != 0 && i2 != 0) {
            this.mCropMode = CropMode.CUSTOM;
            this.mCustomRatio = new PointF((float) i, (float) i2);
            recalculateFrameRect(i3);
        }
    }

    public void setCustomRatio(int i, int i2) {
        setCustomRatio(i, i2, this.mAnimationDurationMillis);
    }

    public void setOverlayColor(int i) {
        this.mOverlayColor = i;
        invalidate();
    }

    public void setFrameColor(int i) {
        this.mFrameColor = i;
        invalidate();
    }

    public void setHandleColor(int i) {
        this.mHandleColor = i;
        invalidate();
    }

    public void setGuideColor(int i) {
        this.mGuideColor = i;
        invalidate();
    }

    public void setBackgroundColor(int i) {
        this.mBackgroundColor = i;
        invalidate();
    }

    public void setMinFrameSizeInDp(int i) {
        this.mMinFrameSize = ((float) i) * getDensity();
    }

    public void setMinFrameSizeInPx(int i) {
        this.mMinFrameSize = (float) i;
    }

    public void setHandleSizeInDp(int i) {
        this.mHandleSize = (int) (((float) i) * getDensity());
    }

    public void setTouchPaddingInDp(int i) {
        this.mTouchPadding = (int) (((float) i) * getDensity());
    }




    public void setGuideShowMode(ShowMode showMode) {
        this.mGuideShowMode = showMode;
        int i = showMode.ordinal();
        if (i == 1) {
            this.mShowGuide = true;
        } else if (i == 2 || i == 3) {
            this.mShowGuide = false;
        }
        invalidate();
    }

    public void setHandleShowMode(ShowMode showMode) {
        this.mHandleShowMode = showMode;
        int i = showMode.ordinal();
        if (i == 1) {
            this.mShowHandle = true;
        } else if (i == 2 || i == 3) {
            this.mShowHandle = false;
        }
        invalidate();
    }

    public void setFrameStrokeWeightInDp(int i) {
        this.mFrameStrokeWeight = ((float) i) * getDensity();
        invalidate();
    }

    public void setGuideStrokeWeightInDp(int i) {
        this.mGuideStrokeWeight = ((float) i) * getDensity();
        invalidate();
    }

    public void setCropEnabled(boolean z) {
        this.mIsCropEnabled = z;
        invalidate();
    }

    public void setEnabled(boolean z) {
        super.setEnabled(z);
        this.mIsEnabled = z;
    }

    public void setInitialFrameScale(float f) {
        this.mInitialFrameScale = constrain(f, 0.01f, 1.0f, 1.0f);
    }

    public void setAnimationEnabled(boolean z) {
        this.mIsAnimationEnabled = z;
    }

    public void setAnimationDuration(int i) {
        this.mAnimationDurationMillis = i;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.mInterpolator = interpolator;
        this.mAnimator = null;
        setupAnimatorIfNeeded();
    }

    public void setDebug(boolean z) {
        this.mIsDebug = z;

        Logger.enabled = true;
        invalidate();
    }

    public void setLoggingEnabled(boolean z) {
        Logger.enabled = z;
    }

    public void setOutputWidth(int i) {
        this.mOutputWidth = i;
        this.mOutputHeight = 0;
    }

    public void setOutputHeight(int i) {
        this.mOutputHeight = i;
        this.mOutputWidth = 0;
    }

    public void setOutputMaxSize(int i, int i2) {
        this.mOutputMaxWidth = i;
        this.mOutputMaxHeight = i2;
    }

    public void setCompressFormat(Bitmap.CompressFormat compressFormat) {
        this.mCompressFormat = compressFormat;
    }

    public void setCompressQuality(int i) {
        this.mCompressQuality = i;
    }

    public void setHandleShadowEnabled(boolean z) {
        this.mIsHandleShadowEnabled = z;
    }

    public boolean isCropping() {
        return this.mIsCropping.get();
    }

    public Uri getSourceUri() {
        return this.mSourceUri;
    }

    public Uri getSaveUri() {
        return this.mSaveUri;
    }

    public boolean isSaving() {
        return this.mIsSaving.get();
    }

    private void setScale(float f) {
        this.mScale = f;
    }

    private void setCenter(PointF pointF) {
        this.mCenter = pointF;
    }

    private float getFrameW() {
        return this.mFrameRect.right - this.mFrameRect.left;
    }

    private float getFrameH() {
        return this.mFrameRect.bottom - this.mFrameRect.top;
    }

    public enum CropMode {
        FIT_IMAGE(0),
        RATIO_4_3(1),
        RATIO_3_4(2),
        SQUARE(3),
        RATIO_16_9(4),
        RATIO_9_16(5),
        FREE(6),
        CUSTOM(7),
        CIRCLE(8),
        CIRCLE_SQUARE(9);
        

        /* renamed from: ID */
        private final int f663ID;

        private CropMode(int i) {
            this.f663ID = i;
        }

        public int getId() {
            return this.f663ID;
        }
    }

    public enum ShowMode {
        SHOW_ALWAYS(1),
        SHOW_ON_TOUCH(2),
        NOT_SHOW(3);
        

        /* renamed from: ID */
        private final int f664ID;

        private ShowMode(int i) {
            this.f664ID = i;
        }

        public int getId() {
            return this.f664ID;
        }
    }

    public enum RotateDegrees {
        ROTATE_90D(90),
        ROTATE_180D(180),
        ROTATE_270D(TIFFConstants.TIFFTAG_IMAGEDESCRIPTION),
        ROTATE_M90D(-90),
        ROTATE_M180D(-180),
        ROTATE_M270D(-270);
        
        private final int VALUE;

        private RotateDegrees(int i) {
            this.VALUE = i;
        }

        public int getValue() {
            return this.VALUE;
        }
    }

    public static class SavedState extends BaseSavedState {
        public static final Creator CREATOR = new Creator() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        float angle;
        int animationDuration;
        int backgroundColor;
        Bitmap.CompressFormat compressFormat;
        int compressQuality;
        float customRatioX;
        float customRatioY;
        int exifRotation;
        int frameColor;
        float frameStrokeWeight;
        int guideColor;
        ShowMode guideShowMode;
        float guideStrokeWeight;
        int handleColor;
        ShowMode handleShowMode;
        int handleSize;
        float initialFrameScale;
        int inputImageHeight;
        int inputImageWidth;
        boolean isAnimationEnabled;
        boolean isCropEnabled;
        boolean isDebug;
        boolean isHandleShadowEnabled;
        float minFrameSize;
        CropMode mode;
        int outputHeight;
        int outputImageHeight;
        int outputImageWidth;
        int outputMaxHeight;
        int outputMaxWidth;
        int outputWidth;
        int overlayColor;
        Uri saveUri;
        boolean showGuide;
        boolean showHandle;
        Uri sourceUri;
        int touchPadding;

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mode = (CropMode) parcel.readSerializable();
            this.backgroundColor = parcel.readInt();
            this.overlayColor = parcel.readInt();
            this.frameColor = parcel.readInt();
            this.guideShowMode = (ShowMode) parcel.readSerializable();
            this.handleShowMode = (ShowMode) parcel.readSerializable();
            boolean z = true;
            this.showGuide = parcel.readInt() != 0;
            this.showHandle = parcel.readInt() != 0;
            this.handleSize = parcel.readInt();
            this.touchPadding = parcel.readInt();
            this.minFrameSize = parcel.readFloat();
            this.customRatioX = parcel.readFloat();
            this.customRatioY = parcel.readFloat();
            this.frameStrokeWeight = parcel.readFloat();
            this.guideStrokeWeight = parcel.readFloat();
            this.isCropEnabled = parcel.readInt() != 0;
            this.handleColor = parcel.readInt();
            this.guideColor = parcel.readInt();
            this.initialFrameScale = parcel.readFloat();
            this.angle = parcel.readFloat();
            this.isAnimationEnabled = parcel.readInt() != 0;
            this.animationDuration = parcel.readInt();
            this.exifRotation = parcel.readInt();
            this.sourceUri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
            this.saveUri = (Uri) parcel.readParcelable(Uri.class.getClassLoader());
            this.compressFormat = (Bitmap.CompressFormat) parcel.readSerializable();
            this.compressQuality = parcel.readInt();
            this.isDebug = parcel.readInt() != 0;
            this.outputMaxWidth = parcel.readInt();
            this.outputMaxHeight = parcel.readInt();
            this.outputWidth = parcel.readInt();
            this.outputHeight = parcel.readInt();
            this.isHandleShadowEnabled = parcel.readInt() == 0 ? false : z;
            this.inputImageWidth = parcel.readInt();
            this.inputImageHeight = parcel.readInt();
            this.outputImageWidth = parcel.readInt();
            this.outputImageHeight = parcel.readInt();
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeSerializable(this.mode);
            parcel.writeInt(this.backgroundColor);
            parcel.writeInt(this.overlayColor);
            parcel.writeInt(this.frameColor);
            parcel.writeSerializable(this.guideShowMode);
            parcel.writeSerializable(this.handleShowMode);
            parcel.writeInt(this.showGuide ? 1 : 0);
            parcel.writeInt(this.showHandle ? 1 : 0);
            parcel.writeInt(this.handleSize);
            parcel.writeInt(this.touchPadding);
            parcel.writeFloat(this.minFrameSize);
            parcel.writeFloat(this.customRatioX);
            parcel.writeFloat(this.customRatioY);
            parcel.writeFloat(this.frameStrokeWeight);
            parcel.writeFloat(this.guideStrokeWeight);
            parcel.writeInt(this.isCropEnabled ? 1 : 0);
            parcel.writeInt(this.handleColor);
            parcel.writeInt(this.guideColor);
            parcel.writeFloat(this.initialFrameScale);
            parcel.writeFloat(this.angle);
            parcel.writeInt(this.isAnimationEnabled ? 1 : 0);
            parcel.writeInt(this.animationDuration);
            parcel.writeInt(this.exifRotation);
            parcel.writeParcelable(this.sourceUri, i);
            parcel.writeParcelable(this.saveUri, i);
            parcel.writeSerializable(this.compressFormat);
            parcel.writeInt(this.compressQuality);
            parcel.writeInt(this.isDebug ? 1 : 0);
            parcel.writeInt(this.outputMaxWidth);
            parcel.writeInt(this.outputMaxHeight);
            parcel.writeInt(this.outputWidth);
            parcel.writeInt(this.outputHeight);
            parcel.writeInt(this.isHandleShadowEnabled ? 1 : 0);
            parcel.writeInt(this.inputImageWidth);
            parcel.writeInt(this.inputImageHeight);
            parcel.writeInt(this.outputImageWidth);
            parcel.writeInt(this.outputImageHeight);
        }
    }
}
