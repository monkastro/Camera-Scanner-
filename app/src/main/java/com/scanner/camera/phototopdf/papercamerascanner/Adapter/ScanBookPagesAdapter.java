package com.scanner.camera.phototopdf.papercamerascanner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.util.ArrayList;

public class ScanBookPagesAdapter extends RecyclerView.Adapter<ScanBookPagesAdapter.ItemViewHolder> {
    private ArrayList<String> arrayList;
    private Context context;

    class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ItemViewHolder(View view) {
            super(view);
            this.image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public ScanBookPagesAdapter(Context context2, ArrayList<String> arrayList2) {
        this.context = context2;
        this.arrayList = arrayList2;
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.slidingimages_layout, viewGroup, false));
    }

    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        if (MainUtils.getScanType(this.context).equals("Book")) {
            itemViewHolder.image.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).load(this.arrayList.get(i)).diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true)).into(itemViewHolder.image);
    }

    public int getItemCount() {
        return this.arrayList.size();
    }
}
