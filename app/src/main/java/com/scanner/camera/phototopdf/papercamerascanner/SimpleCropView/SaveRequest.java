package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView;

import android.graphics.Bitmap;
import android.net.Uri;

import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.callback.SaveCallback;

import io.reactivex.Single;


public class SaveRequest {
    private Bitmap.CompressFormat compressFormat;
    private int compressQuality = -1;
    private CropImageView cropImageView;
    private Bitmap image;

    public SaveRequest(CropImageView cropImageView2, Bitmap bitmap) {
        this.cropImageView = cropImageView2;
        this.image = bitmap;
    }

    public SaveRequest compressFormat(Bitmap.CompressFormat compressFormat2) {
        this.compressFormat = compressFormat2;
        return this;
    }

    public SaveRequest compressQuality(int i) {
        this.compressQuality = i;
        return this;
    }

    private void build() {
        Bitmap.CompressFormat compressFormat2 = this.compressFormat;
        if (compressFormat2 != null) {
            this.cropImageView.setCompressFormat(compressFormat2);
        }
        int i = this.compressQuality;
        if (i >= 0) {
            this.cropImageView.setCompressQuality(i);
        }
    }

    public void execute(Uri uri, SaveCallback saveCallback) {
        build();
        this.cropImageView.saveAsync(uri, this.image, saveCallback);
    }

    public Single<Uri> executeAsSingle(Uri uri) {
        build();
        return this.cropImageView.saveAsSingle(this.image, uri);
    }
}
