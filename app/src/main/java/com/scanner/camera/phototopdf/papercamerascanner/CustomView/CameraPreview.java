package com.scanner.camera.phototopdf.papercamerascanner.CustomView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.scanner.camera.phototopdf.papercamerascanner.Activity.CameraScanActivity;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback, Camera.PictureCallback {
    private final float ASPECT_RATIO_H = 2.0f;
    private final float ASPECT_RATIO_W = 4.0f;
    private final int PICTURE_MAX_WIDTH = 1280;
    private final int PREVIEW_MAX_WIDTH = 640;
    Camera camera;
    public Bitmap clickBitmap;
    Camera.Parameters parameters;

    public byte[] previewBuffer;
    private SurfaceHolder surfaceHolder;


    public void processFrame(byte[] bArr, Camera camera2) {
    }

    public void surfaceDestroyed(SurfaceHolder surfaceHolder2) {
    }

    public CameraPreview(Context context, Camera camera2, int i) {
        super(context);
        SurfaceHolder holder = getHolder();
        this.surfaceHolder = holder;
        holder.addCallback(this);
        if (Build.VERSION.SDK_INT < 11) {
            this.surfaceHolder.setType(3);
        }
    }

    public void surfaceCreated(SurfaceHolder surfaceHolder2) {
        setupCamera();
        startCameraPreview(surfaceHolder2);
    }

    public void surfaceChanged(SurfaceHolder surfaceHolder2, int i, int i2, int i3) {
        if (this.surfaceHolder.getSurface() == null) {



            Log.e(CameraScanActivity.LOG_TAG, "surfaceChanged(): surfaceHolder is null, nothing to do.");
            return;
        }
        stopCameraPreview();
        updateCameraDisplayOrientation();
        startCameraPreview(surfaceHolder2);
    }


    public void onMeasure(int i, int i2) {
        int size = MeasureSpec.getSize(i2);
        int size2 = MeasureSpec.getSize(i);
        setMeasuredDimension(size2, size);
        Log.i(CameraScanActivity.LOG_TAG, "onMeasure(): set surface dimension to " + size2 + "x" + size);
    }

    private void setupCamera() {
        Camera camera2 = ((CameraScanActivity) getContext()).getCamera();
        this.camera = camera2;
        if (camera2 == null) {
            Log.e(CameraScanActivity.LOG_TAG, "setupCamera(): warning, camera is null");
            return;
        }
        Camera.Parameters parameters2 = camera2.getParameters();
        this.parameters = parameters2;
        Camera.Size bestSize = getBestSize(parameters2.getSupportedPreviewSizes(), 640);
        Camera.Size bestSize2 = getBestSize(this.parameters.getSupportedPictureSizes(), 1280);
        this.parameters.setPreviewSize(bestSize.width, bestSize.height);
        this.parameters.setPictureSize(bestSize2.width, bestSize2.height);
        this.parameters.setFocusMode("continuous-picture");
        this.parameters.setPreviewFormat(17);
        this.parameters.setPictureFormat(256);
        try {
            this.parameters.setFlashMode("off");
        } catch (NoSuchMethodError e) {
            Log.e(CameraScanActivity.LOG_TAG, "setupCamera(): this camera ignored some unsupported settings.", e);
        }
        this.camera.setParameters(this.parameters);
        int i = this.camera.getParameters().getPreviewSize().width;
        int i2 = this.camera.getParameters().getPreviewSize().height;
        int i3 = this.camera.getParameters().getPictureSize().width;
        int i4 = this.camera.getParameters().getPictureSize().height;
        Log.d(CameraScanActivity.LOG_TAG, "setupCamera(): settings applied:\n\tpreview size: " + i + "x" + i2 + "\n\tpicture size: " + i3 + "x" + i4);
        try {
            this.previewBuffer = new byte[(((i * i2) * ImageFormat.getBitsPerPixel(this.camera.getParameters().getPreviewFormat())) / 8)];
            setCameraCallback();
        } catch (IOException e2) {
            Log.e(CameraScanActivity.LOG_TAG, "setupCamera(): error setting camera callback.", e2);
        }
    }

    private void setCameraCallback() throws IOException {
        Camera camera2 = ((CameraScanActivity) getContext()).getCamera();
        camera2.addCallbackBuffer(this.previewBuffer);
        camera2.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
            public void onPreviewFrame(byte[] bArr, Camera camera) {
                CameraPreview cameraPreview = CameraPreview.this;
                cameraPreview.processFrame(cameraPreview.previewBuffer, camera);
                camera.addCallbackBuffer(CameraPreview.this.previewBuffer);
            }
        });
    }

    private Camera.Size getBestSize(List<Camera.Size> list, int i) {
        Iterator<Camera.Size> it = list.iterator();
        Camera.Size size = null;
        while (true) {
            boolean z = false;
            if (!it.hasNext()) {
                break;
            }
            Camera.Size next = it.next();
            boolean z2 = ((float) next.width) / 4.0f == ((float) next.height) / 2.0f;
            boolean z3 = size == null || next.width > size.width;
            if (next.width <= i) {
                z = true;
            }
            if (z2 && z && z3) {
                size = next;
            }
        }
        if (size == null) {
            size = list.get(0);
            Log.e(CameraScanActivity.LOG_TAG, "determineBestSize(): can't find a good size. Setting to the very first...");
        }
        Log.i(CameraScanActivity.LOG_TAG, "determineBestSize(): bestSize is " + size.width + "x" + size.height);
        return size;
    }

    private synchronized void startCameraPreview(SurfaceHolder surfaceHolder2) {
        Camera camera2 = ((CameraScanActivity) getContext()).getCamera();
        try {
            camera2.setPreviewDisplay(surfaceHolder2);
            camera2.startPreview();
        } catch (Exception e) {
            Log.e(CameraScanActivity.LOG_TAG, "startCameraPreview(): error starting camera preview", e);
        }
        return;
    }

    private synchronized void stopCameraPreview() {
        try {
            ((CameraScanActivity) getContext()).getCamera().stopPreview();
        } catch (Exception unused) {
            Log.i(CameraScanActivity.LOG_TAG, "stopCameraPreview(): tried to stop a non-running preview, this is not an error");
        }
        return;
    }

    private void updateCameraDisplayOrientation() {
        int i;
        CameraScanActivity cameraScanActivity = (CameraScanActivity) getContext();
        Camera camera2 = cameraScanActivity.getCamera();
        int cameraID = cameraScanActivity.getCameraID();
        if (camera2 == null) {
            Log.e(CameraScanActivity.LOG_TAG, "updateCameraDisplayOrientation(): warning, camera is null");
            return;
        }
        int rotation = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRotation();
        int i2 = 0;
        if (rotation != 0) {
            if (rotation == 1) {
                i2 = 90;
            } else if (rotation == 2) {
                i2 = 180;
            } else if (rotation == 3) {
                i2 = TIFFConstants.TIFFTAG_IMAGEDESCRIPTION;
            }
        }
        if (Build.VERSION.SDK_INT >= 9) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            Camera.getCameraInfo(cameraID, cameraInfo);
            if (cameraInfo.facing == 1) {
                i = (360 - ((cameraInfo.orientation + i2) % 360)) % 360;
            } else {
                i = ((cameraInfo.orientation - i2) + 360) % 360;
            }
        } else {
            i = Math.abs(i2 - 90);
        }
        camera2.setDisplayOrientation(i);
    }

    public void onPictureTaken(byte[] bArr, Camera camera2) {
        Log.i(CameraScanActivity.LOG_TAG, "onPictureTaken(): raw image is " + bArr.length + " bytes long");
        stopCameraPreview();
        Bitmap decodeByteArray = BitmapFactory.decodeByteArray(bArr, 0, bArr.length);
        int rotation = ((Activity) getContext()).getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == 0 || rotation == 2) {
            Matrix matrix = new Matrix();
            matrix.postRotate(90.0f);
            decodeByteArray = Bitmap.createBitmap(decodeByteArray, 0, 0, decodeByteArray.getWidth(), decodeByteArray.getHeight(), matrix, true);
        }
        this.clickBitmap = decodeByteArray;
        startCameraPreview(this.surfaceHolder);
    }

    public Bitmap getClickBitmap() {
        return this.clickBitmap;
    }

    public void clearClickBitmap() {
        this.clickBitmap = null;
    }


    public void flashModeSet() {
        int n;
        String string2 = this.parameters.getFlashMode();
        Log.e((String)"000000000000", (String)string2);
        int n2 = string2.hashCode();
        if (n2 != 3551) {
            if (n2 != 109935) return;
            if (!string2.equals((Object)"off")) return;
            n = 0;
        } else {
            if (!string2.equals((Object)"on")) return;
            n = 1;
        }
        if (n != 0) {
            if (n != 1) {
                return;
            }
            this.parameters.setFlashMode("off");
            this.camera.setParameters(this.parameters);
            return;
        }
        this.parameters.setFlashMode("on");
        this.camera.setParameters(this.parameters);}
}
