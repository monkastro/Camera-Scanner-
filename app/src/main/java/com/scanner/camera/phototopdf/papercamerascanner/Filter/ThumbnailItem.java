package com.scanner.camera.phototopdf.papercamerascanner.Filter;

import android.graphics.Bitmap;
import com.zomato.photofilters.imageprocessors.Filter;

public class ThumbnailItem {
    public Filter filter = new Filter();
    public Bitmap image = null;
    private boolean isSelected;

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
    }
}
