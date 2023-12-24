package com.scanner.camera.phototopdf.papercamerascanner.Filter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.util.List;

public class ThumbnailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "THUMBNAILS_ADAPTER";

    public static int lastPosition = -1;
    Context context;
    private List<ThumbnailItem> dataSet;

    public ThumbnailCallback thumbnailCallback;

    public ThumbnailsAdapter(List<ThumbnailItem> list, ThumbnailCallback thumbnailCallback2) {
        Log.v(TAG, "Thumbnails Adapter has " + list.size() + " items");
        this.dataSet = list;
        this.thumbnailCallback = thumbnailCallback2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Log.v(TAG, "On Create View Holder Called");

        View inflate = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_thumbnail_item, viewGroup, false);
        this.context = viewGroup.getContext();
        return new ThumbnailsViewHolder(inflate);
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int i) {
        final ThumbnailItem thumbnailItem = this.dataSet.get(i);
        Log.v(TAG, "On Bind View Called");
        ThumbnailsViewHolder thumbnailsViewHolder = (ThumbnailsViewHolder) viewHolder;
        thumbnailsViewHolder.thumbnail.setImageBitmap(thumbnailItem.image);
        if (this.dataSet.get(i).isSelected()) {
            thumbnailsViewHolder.llThumbSelected.setBackground(ContextCompat.getDrawable(this.context,R.drawable.bg_selected_thumb));
        } else {
            thumbnailsViewHolder.llThumbSelected.setBackground(ContextCompat.getDrawable(this.context, R.drawable.bg_unselected_thumb));
        }
        thumbnailsViewHolder.thumbnail.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ThumbnailsAdapter.lastPosition != i) {
                    ThumbnailsAdapter.this.thumbnailCallback.onThumbnailClick(thumbnailItem.filter, i);
                    int unused = ThumbnailsAdapter.lastPosition = i;
                }
            }
        });
    }

    private void setAnimation(View view, int i) {
        ViewHelper.setAlpha(view, 0.0f);
        ViewPropertyAnimator.animate(view).alpha(1.0f).setDuration(250).start();
        lastPosition = i;
    }

    public int getItemCount() {
        return this.dataSet.size();
    }

    public static class ThumbnailsViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout llThumbSelected;
        public ImageView thumbnail;

        public ThumbnailsViewHolder(View view) {
            super(view);
            this.thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            this.llThumbSelected = (LinearLayout) view.findViewById(R.id.llThumbSelected);
        }
    }
}
