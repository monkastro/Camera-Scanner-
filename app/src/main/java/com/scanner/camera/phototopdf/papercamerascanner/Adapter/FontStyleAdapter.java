package com.scanner.camera.phototopdf.papercamerascanner.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.scanner.camera.phototopdf.papercamerascanner.Model.FontStyleModel;
import com.scanner.camera.phototopdf.papercamerascanner.OnClickInterface.OnItemClickListener;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.util.ArrayList;

public class FontStyleAdapter extends RecyclerView.Adapter<FontStyleAdapter.ItemViewHolder> {
    private ArrayList<FontStyleModel> arrayList;
    String bindType;
    private Context context;

    public OnItemClickListener itemListener;

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTextStyle;

        public ItemViewHolder(View view) {
            super(view);
            TextView textView = (TextView) view.findViewById(R.id.tvTextStyle);
            this.tvTextStyle = textView;
            textView.setOnClickListener(this);
        }

        public void onClick(View view) {
            FontStyleAdapter.this.itemListener.OnClick(view, getLayoutPosition(), FontStyleAdapter.this.bindType);
        }
    }

    public FontStyleAdapter(Context context2, ArrayList<FontStyleModel> arrayList2, OnItemClickListener onItemClickListener, String str) {
        this.context = context2;
        this.arrayList = arrayList2;
        this.itemListener = onItemClickListener;
        this.bindType = str;
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.font_style_item_layout, viewGroup, false));
    }

    public void onBindViewHolder(ItemViewHolder itemViewHolder, int i) {
        Typeface font = ResourcesCompat.getFont(this.context, this.arrayList.get(i).getTypeFace());
        String fontName = this.arrayList.get(i).getFontName();
        itemViewHolder.tvTextStyle.setTypeface(font);
        itemViewHolder.tvTextStyle.setText(fontName.toUpperCase());
        if (this.arrayList.get(i).isSelected()) {
            itemViewHolder.tvTextStyle.setTextColor(this.context.getResources().getColor(R.color.selectedButtonColor));
        } else {
            itemViewHolder.tvTextStyle.setTextColor(this.context.getResources().getColor(R.color.unselectedButtonColor));
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }
}
