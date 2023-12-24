package com.scanner.camera.phototopdf.papercamerascanner.Adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.scanner.camera.phototopdf.papercamerascanner.OnClickInterface.OnItemClickListener;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.io.File;
import java.util.List;

public class SelectedImageAdapter extends RecyclerView.Adapter<SelectedImageAdapter.AlbumViewHolder> {
    private Context context;
    private List<String> list;
    OnItemClickListener onItemClickListener;

    public SelectedImageAdapter(Context context2, List<String> list2, OnItemClickListener onItemClickListener2) {
        this.list = list2;
        this.context = context2;
        this.onItemClickListener = onItemClickListener2;
    }

    public AlbumViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AlbumViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_image_selected, (ViewGroup) null, false));
    }

    public void onBindViewHolder(AlbumViewHolder albumViewHolder, int i) {
        if (new File(this.list.get(i)).exists()) {
            Glide.with(this.context).load(Uri.fromFile(new File(this.list.get(i)))).thumbnail(0.5f).into(albumViewHolder.ivSelectedImage);
        }
    }

    public int getItemCount() {
        return this.list.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView btnRemove;
        ImageView ivSelectedImage;

        AlbumViewHolder(View view) {
            super(view);
            this.ivSelectedImage = (ImageView) view.findViewById(R.id.ivSelectedImage);
            ImageView imageView = (ImageView) view.findViewById(R.id.btnRemove);
            this.btnRemove = imageView;
            imageView.setOnClickListener(this);
        }

        public void onClick(View view) {
            SelectedImageAdapter.this.onItemClickListener.OnClick(view, getLayoutPosition(), "");
        }
    }
}
