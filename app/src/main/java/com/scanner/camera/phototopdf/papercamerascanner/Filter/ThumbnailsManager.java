package com.scanner.camera.phototopdf.papercamerascanner.Filter;

import android.content.Context;
import android.graphics.Bitmap;

import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.util.ArrayList;
import java.util.List;

public final class ThumbnailsManager {
    private static List<ThumbnailItem> filterThumbs = new ArrayList(10);
    private static List<ThumbnailItem> processedThumbs = new ArrayList(10);

    private ThumbnailsManager() {
    }

    public static void addThumb(ThumbnailItem thumbnailItem) {
        filterThumbs.add(thumbnailItem);
    }

    public static List<ThumbnailItem> processThumbs(Context context) {
        for (ThumbnailItem next : filterThumbs) {
            int dimension = (int) context.getResources().getDimension(R.dimen.thumbnail_size);
            next.image = Bitmap.createScaledBitmap(next.image, dimension, dimension, false);
            next.image = next.filter.processFilter(next.image);
            processedThumbs.add(next);
        }
        return processedThumbs;
    }

    public static void clearThumbs() {
        filterThumbs = new ArrayList();
        processedThumbs = new ArrayList();
    }
}
