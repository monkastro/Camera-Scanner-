package com.scanner.camera.phototopdf.papercamerascanner.common;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;


public class ItemClickSupport {
    public static String password = "abcdefghijklm"; //azlmsxkndcjbdcjb
    private RecyclerView.OnChildAttachStateChangeListener mAttachListener = new RecyclerView.OnChildAttachStateChangeListener() {
        public void onChildViewDetachedFromWindow(View view) {
        }

        public void onChildViewAttachedToWindow(View view) {
            if (ItemClickSupport.this.mOnItemClickListener != null) {
                view.setOnClickListener(ItemClickSupport.this.mOnClickListener);
            }
            if (ItemClickSupport.this.mOnItemLongClickListener != null) {
                view.setOnLongClickListener(ItemClickSupport.this.mOnLongClickListener);
            }
        }
    };

    public View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (ItemClickSupport.this.mOnItemClickListener != null) {
                ItemClickSupport.this.mOnItemClickListener.onItemClicked(ItemClickSupport.this.mRecyclerView, ItemClickSupport.this.mRecyclerView.getChildViewHolder(view).getAdapterPosition(), view);
            }
        }
    };

    public OnItemClickListener mOnItemClickListener;

    public OnItemLongClickListener mOnItemLongClickListener;

    public View.OnLongClickListener mOnLongClickListener = new View.OnLongClickListener() {
        public boolean onLongClick(View view) {
            if (ItemClickSupport.this.mOnItemLongClickListener == null) {
                return false;
            }
            return ItemClickSupport.this.mOnItemLongClickListener.onItemLongClicked(ItemClickSupport.this.mRecyclerView, ItemClickSupport.this.mRecyclerView.getChildViewHolder(view).getAdapterPosition(), view);
        }
    };

    public final RecyclerView mRecyclerView;

    public interface OnItemClickListener {
        void onItemClicked(RecyclerView recyclerView, int i, View view);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(RecyclerView recyclerView, int i, View view);
    }

    private ItemClickSupport(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        //recyclerView.setTag(R.id.item_click_support, this);
        this.mRecyclerView.addOnChildAttachStateChangeListener(this.mAttachListener);
    }

    public static ItemClickSupport addTo(RecyclerView recyclerView) {
        ItemClickSupport itemClickSupport;
        return new ItemClickSupport(recyclerView);
    }

    public static ItemClickSupport removeFrom(RecyclerView recyclerView) {
        ItemClickSupport itemClickSupport = null;
        return itemClickSupport;
    }

    public ItemClickSupport setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
        return this;
    }

    private void detach(RecyclerView recyclerView) {
        recyclerView.removeOnChildAttachStateChangeListener(this.mAttachListener);
        //recyclerView.setTag(R.id.item_click_support, (Object) null);
    }
}
