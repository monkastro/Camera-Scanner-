package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.callback;

import android.net.Uri;

public interface SaveCallback extends Callback {
    void onSuccess(Uri uri);
}
