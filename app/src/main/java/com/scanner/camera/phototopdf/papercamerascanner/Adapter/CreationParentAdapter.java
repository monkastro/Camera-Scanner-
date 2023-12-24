package com.scanner.camera.phototopdf.papercamerascanner.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.Model.CreationParentModel;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class CreationParentAdapter extends RecyclerView.Adapter<CreationParentAdapter.ItemViewHolder> {

    public ArrayList<CreationParentModel> arrayList;

    public Context context;

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivIcon;

        public LinearLayout ivItemMenu;
        private LinearLayout llItemClick;

        public TextView tvDateTime;

        public TextView tvFileName;

        public TextView tvFileSize;

        public ItemViewHolder(View view) {
            super(view);
            this.ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
            this.ivItemMenu = (LinearLayout) view.findViewById(R.id.ivItemMenu);
            this.llItemClick = (LinearLayout) view.findViewById(R.id.llItemClick);
            this.tvFileName = (TextView) view.findViewById(R.id.tvFileName);
            this.tvFileSize = (TextView) view.findViewById(R.id.tvFileSize);
            this.tvDateTime = (TextView) view.findViewById(R.id.tvDateTime);
        }
    }

    public CreationParentAdapter(Context context2, ArrayList<CreationParentModel> arrayList2) {
        this.context = context2;
        this.arrayList = arrayList2;
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_creation_folder_item, viewGroup, false));
    }

    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int i) {
        ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).load(Integer.valueOf(R.drawable.ic_folder_icon)).diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true)).into(itemViewHolder.ivIcon);
        itemViewHolder.tvFileName.setText(this.arrayList.get(i).getFileFolder());
        itemViewHolder.tvDateTime.setText(this.arrayList.get(i).getFileDateTime());
        itemViewHolder.tvFileSize.setText(MainUtils.getStorageSize(this.arrayList.get(i).getFileSize()));
        itemViewHolder.ivItemMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(CreationParentAdapter.this.context, itemViewHolder.ivItemMenu);
                popupMenu.inflate(R.menu.menu_folder_item_click);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.mainDelete :
                                CreationParentAdapter.this.deleteItems(new File(((CreationParentModel) CreationParentAdapter.this.arrayList.get(i)).getFolderPath()), i);
                                return false;
                            case R.id.mainRename :
                                CreationParentAdapter.this.renameFolder(new File(((CreationParentModel) CreationParentAdapter.this.arrayList.get(i)).getFolderPath()), i);
                                return false;
                            case R.id.mainshare :
                                CreationParentAdapter.this.share(((CreationParentModel) CreationParentAdapter.this.arrayList.get(i)).getFolderPath());
                                return false;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }


    private void deleteItems(File file, int i) {
        deleteRecursive(file);
        this.arrayList.remove(i);
        notifyDataSetChanged();
    }

    public void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File deleteRecursive : file.listFiles()) {
                deleteRecursive(deleteRecursive);
            }
        }
        file.delete();
    }

    public void renameFolder(File file, int i) {
        Dialog dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.dialog_save_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        final EditText editText = (EditText) dialog.findViewById(R.id.etFolderName);
        dialog.setCancelable(false);
        final String fileFolder = this.arrayList.get(i).getFileFolder();
        final String replace = file.toString().replace(fileFolder, "");
        editText.setText("" + fileFolder);
        final EditText editText2 = editText;
        final Dialog dialog2 = dialog;
        final File file2 = file;
        final int i2 = i;
        ((LinearLayout) dialog.findViewById(R.id.llOK)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (editText2.getText().length() <= 0) {
                    editText2.setError("Enter Folder Name");
                } else if (fileFolder.equals(editText2.getText().toString())) {
                    dialog2.dismiss();
                } else {
                    File file = new File(replace + editText2.getText().toString());
                    if (file2.renameTo(file)) {
                        ((CreationParentModel) CreationParentAdapter.this.arrayList.get(i2)).setFolderPath(file.getPath());
                        ((CreationParentModel) CreationParentAdapter.this.arrayList.get(i2)).setFileFolder(editText2.getText().toString());
                        CreationParentAdapter.this.notifyDataSetChanged();
                        dialog2.dismiss();
                    }
                }
            }
        });
        ((ImageView) dialog.findViewById(R.id.ivCancelName)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                editText.setText("");
            }
        });
        dialog.show();
    }


    private void share(String str) {
        File[] listFiles;
        ArrayList arrayList2 = new ArrayList();
        if (new File(str).exists() && (listFiles = new File(str).listFiles()) != null && listFiles.length > 0) {
            for (File path : listFiles) {
                arrayList2.add(path.getPath());
            }
        }
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.putExtra("android.intent.extra.SUBJECT", "Here are some files.");
        intent.setType("*/*");
        ArrayList arrayList3 = new ArrayList();
        Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            arrayList3.add(Uri.fromFile(new File((String) it.next())));
        }
        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList3);
        this.context.startActivity(intent);
    }

    public int getItemCount() {
        return this.arrayList.size();
    }
}
