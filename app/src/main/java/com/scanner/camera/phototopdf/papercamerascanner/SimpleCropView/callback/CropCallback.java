package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.callback;

import android.graphics.Bitmap;

public interface CropCallback extends Callback {
    void onSuccess(Bitmap bitmap);
}
