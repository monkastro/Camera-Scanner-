package com.scanner.camera.phototopdf.papercamerascanner.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.scanner.camera.phototopdf.papercamerascanner.Model.ColorModel;
import com.scanner.camera.phototopdf.papercamerascanner.OnClickInterface.OnItemClickListener;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.util.ArrayList;

public class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.ItemViewHolder> {
    private ArrayList<ColorModel> arrayList;
    String bindType;
    private Context context;

    public OnItemClickListener itemListener;

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cvTextColor;
        ImageView ivSelectedColor;
        LinearLayout llColorRound;

        public ItemViewHolder(View view) {
            super(view);
            this.cvTextColor = (CardView) view.findViewById(R.id.textColor);
            this.ivSelectedColor = (ImageView) view.findViewById(R.id.ivSelectedColor);
            this.llColorRound = (LinearLayout) view.findViewById(R.id.llColorRound);
            this.cvTextColor.setOnClickListener(this);
        }

        public void onClick(View view) {
            ColorAdapter.this.itemListener.OnClick(view, getLayoutPosition(), ColorAdapter.this.bindType);
        }
    }

    public ColorAdapter(Context context2, ArrayList<ColorModel> arrayList2, OnItemClickListener onItemClickListener, String str) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.itemListener = onItemClickListener;
        this.bindType = str;
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.color_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.cvTextColor.setCardBackgroundColor(Color.parseColor(this.arrayList.get(i).getColorCode()));
        if (!this.arrayList.get(i).isSelected()) {
            itemViewHolder.ivSelectedColor.setVisibility(8);
        } else if (this.arrayList.get(i).getColorCode().equals("#ffffff")) {
            itemViewHolder.ivSelectedColor.setVisibility(0);
            itemViewHolder.ivSelectedColor.setColorFilter(this.context.getResources().getColor(R.color.colorBlack));
            itemViewHolder.llColorRound.setBackground(ContextCompat.getDrawable(this.context, R.drawable.round_selected_color));
        } else {
            itemViewHolder.ivSelectedColor.setVisibility(0);
            itemViewHolder.llColorRound.setBackground(ContextCompat.getDrawable(this.context, R.drawable.round_selected_color));
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }
}
