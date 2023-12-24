package com.scanner.camera.phototopdf.papercamerascanner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.Model.DataModel;
import com.scanner.camera.phototopdf.papercamerascanner.OnClickInterface.OnItemClickListener;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.util.ArrayList;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.MyViewHolder> {
    String bindType;
    public ArrayList<DataModel> dataSet;

    public OnItemClickListener itemListener;
    public Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewIcon;

        public MyViewHolder(View view) {
            super(view);
            ImageView imageView = (ImageView) view.findViewById(R.id.imgPIPFramePreview);
            this.imageViewIcon = imageView;
            imageView.setOnClickListener(this);
        }

        public void onClick(View view) {
            StickerAdapter.this.itemListener.OnClick(view, getLayoutPosition(), StickerAdapter.this.bindType);
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_editor_abc_card_row, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        ImageView imageView = myViewHolder.imageViewIcon;
        imageView.setTag("" + i);
        myViewHolder.imageViewIcon.setImageBitmap(MainUtils.getBitmapFromAsset(this.dataSet.get(i).getDirName(), this.mContext));
    }

    public StickerAdapter(ArrayList<DataModel> arrayList, Context context, OnItemClickListener onItemClickListener, String str) {
        this.dataSet = arrayList;
        this.mContext = context;
        this.itemListener = onItemClickListener;
        this.bindType = str;
    }

    public int getItemCount() {
        return this.dataSet.size();
    }
}
