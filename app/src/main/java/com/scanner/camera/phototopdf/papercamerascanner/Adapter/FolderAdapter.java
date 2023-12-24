package com.scanner.camera.phototopdf.papercamerascanner.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.scanner.camera.phototopdf.papercamerascanner.Model.FolderModel;
import com.scanner.camera.phototopdf.papercamerascanner.OnClickInterface.OnItemClickListener;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.util.List;

public class FolderAdapter extends RecyclerView.Adapter<FolderAdapter.AlbumViewHolder> {
    private Context context;
    private List<FolderModel> list;
    OnItemClickListener onItemClickListener;

    public FolderAdapter(Context context2, List<FolderModel> list2, OnItemClickListener onItemClickListener2) {
        this.list = list2;
        this.context = context2;
        this.onItemClickListener = onItemClickListener2;
    }

    public AlbumViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new AlbumViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_folder, (ViewGroup) null, false));
    }

    public void onBindViewHolder(AlbumViewHolder albumViewHolder, int i) {
        albumViewHolder.tvFolderName.setText(this.list.get(i).getName());
        if (this.list.get(i).isSelected()) {
            albumViewHolder.tvFolderName.setTextColor(this.context.getResources().getColor(R.color.colorYellowMain));
        } else {
            albumViewHolder.tvFolderName.setTextColor(this.context.getResources().getColor(R.color.colorTextGrey));
        }
    }

    public int getItemCount() {
        return this.list.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout llView;
        TextView tvFolderName;

        AlbumViewHolder(View view) {
            super(view);
            this.tvFolderName = (TextView) view.findViewById(R.id.tvFolderName);
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.llView);
            this.llView = linearLayout;
            linearLayout.setOnClickListener(this);
        }

        public void onClick(View view) {
            FolderAdapter.this.onItemClickListener.OnClick(view, getLayoutPosition(), "");
        }
    }
}
