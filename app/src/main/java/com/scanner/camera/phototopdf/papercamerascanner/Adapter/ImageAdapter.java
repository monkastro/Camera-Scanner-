package com.scanner.camera.phototopdf.papercamerascanner.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.scanner.camera.phototopdf.papercamerascanner.Model.ImageModel;
import com.scanner.camera.phototopdf.papercamerascanner.OnClickInterface.OnItemClickListener;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.io.File;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.AlbumViewHolder> {
    private Context context;
    private List<ImageModel> list;
    OnItemClickListener onItemClickListener;

    public ImageAdapter(Context context2, List<ImageModel> list2, OnItemClickListener onItemClickListener2) {
        this.list = list2;
        this.context = context2;
        this.onItemClickListener = onItemClickListener2;
    }

    public AlbumViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AlbumViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image, (ViewGroup) null, false));
    }

    public void onBindViewHolder(AlbumViewHolder albumViewHolder, int i) {
        if (new File(this.list.get(i).getFilePath()).exists()) {
            ((RequestBuilder) Glide.with(this.context).load(Uri.fromFile(new File(this.list.get(i).getFilePath()))).thumbnail(0.5f).placeholder((int) R.drawable.select_gallery_default_icon)).into(albumViewHolder.ivGalleryImage);
        }
        if (this.list.get(i).isSelected()) {
            albumViewHolder.llSelectedView.setVisibility(0);
        } else {
            albumViewHolder.llSelectedView.setVisibility(8);
        }
    }

    public int getItemCount() {
        return this.list.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivGalleryImage;
        RelativeLayout llSelectedView;

        AlbumViewHolder(View view) {
            super(view);
            ImageView imageView = (ImageView) view.findViewById(R.id.ivGalleryImage);
            this.ivGalleryImage = imageView;
            imageView.setOnClickListener(this);
            this.llSelectedView = (RelativeLayout) view.findViewById(R.id.llSelectedView);
        }

        public void onClick(View view) {
            ImageAdapter.this.onItemClickListener.OnClick(view, getLayoutPosition(), "");
        }
    }
}
