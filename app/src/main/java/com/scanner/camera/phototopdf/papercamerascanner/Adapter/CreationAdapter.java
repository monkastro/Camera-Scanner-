package com.scanner.camera.phototopdf.papercamerascanner.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionCloudTextRecognizerOptions;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.scanner.camera.phototopdf.papercamerascanner.Activity.Camera_Scanner_BookScanPagesActivity;
import com.scanner.camera.phototopdf.papercamerascanner.Activity.OCRResultActivity;
import com.scanner.camera.phototopdf.papercamerascanner.Activity.Camera_Scanner_PDFViewerActivity;
import com.scanner.camera.phototopdf.papercamerascanner.Activity.ResultActivity;
import com.scanner.camera.phototopdf.papercamerascanner.Activity.TextFileViwerActivity;

import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.Model.CreationModel;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

public class CreationAdapter extends RecyclerView.Adapter<CreationAdapter.ItemViewHolder> {

    public ArrayList<CreationModel> arrayList;
    Bitmap bitmap = null;

    public Context context;
    Dialog dialogLoading;

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivIcon;

        public LinearLayout ivItemMenu;

        public LinearLayout llItemClick;

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

    public CreationAdapter(Context context2, ArrayList<CreationModel> arrayList2) {
        this.context = context2;
        this.arrayList = arrayList2;
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.my_creation_item, viewGroup, false));
    }

    public void onBindViewHolder(final ItemViewHolder itemViewHolder, final int i) {
        if (this.arrayList.get(i).getFileName().contains(".jpg") || this.arrayList.get(i).getFileName().contains(".png")) {
            if (new File(this.arrayList.get(i).getFilePath()).exists()) {
                ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).load(new File(this.arrayList.get(i).getFilePath())).diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true)).into(itemViewHolder.ivIcon);
            }
        } else if (this.arrayList.get(i).getFileName().contains(".pdf")) {
            ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).load(Integer.valueOf(R.drawable.pdf_list_icon)).diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true)).into(itemViewHolder.ivIcon);
        } else if (this.arrayList.get(i).getFileName().contains(".txt")) {
            ((RequestBuilder) ((RequestBuilder) Glide.with(this.context).load(Integer.valueOf(R.drawable.text_list_icon)).diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true)).into(itemViewHolder.ivIcon);
        }
        itemViewHolder.tvFileName.setText(this.arrayList.get(i).getFileName());
        itemViewHolder.tvDateTime.setText(this.arrayList.get(i).getFileDateTime());
        itemViewHolder.tvFileSize.setText(MainUtils.getStorageSize(this.arrayList.get(i).getFileSize()));
        itemViewHolder.llItemClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (((CreationModel) CreationAdapter.this.arrayList.get(i)).getFileName().contains(".jpg") || ((CreationModel) CreationAdapter.this.arrayList.get(i)).getFileName().contains(".png")) {
                    String replace = ((CreationModel) CreationAdapter.this.arrayList.get(i)).getFileFolder().replace(Environment.getExternalStorageDirectory().toString() + "/", "");
                    Intent intent = new Intent(CreationAdapter.this.context, Camera_Scanner_BookScanPagesActivity.class);
                    intent.putExtra("folderName", replace);
                    intent.addFlags(268435456);
                    CreationAdapter.this.context.startActivity(intent);
                } else if (((CreationModel) CreationAdapter.this.arrayList.get(i)).getFileName().contains(".pdf")) {
                    Intent intent2 = new Intent(CreationAdapter.this.context, Camera_Scanner_PDFViewerActivity.class);
                    intent2.putExtra("pdfFilePath", ((CreationModel) CreationAdapter.this.arrayList.get(i)).getFilePath());
                    intent2.addFlags(268435456);
                    CreationAdapter.this.context.startActivity(intent2);
                } else if (((CreationModel) CreationAdapter.this.arrayList.get(i)).getFileName().contains(".txt")) {
                    Intent intent3 = new Intent(CreationAdapter.this.context, TextFileViwerActivity.class);
                    intent3.putExtra("textFilePath", ((CreationModel) CreationAdapter.this.arrayList.get(i)).getFilePath());
                    intent3.addFlags(268435456);
                    CreationAdapter.this.context.startActivity(intent3);
                }
            }
        });
        itemViewHolder.ivItemMenu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(CreationAdapter.this.context, itemViewHolder.ivItemMenu);
                if (((CreationModel) CreationAdapter.this.arrayList.get(i)).getFileName().contains(".pdf") || ((CreationModel) CreationAdapter.this.arrayList.get(i)).getFileName().contains(".txt")) {
                    popupMenu.inflate(R.menu.menu_item_click_for_pdf);
                } else {
                    popupMenu.inflate(R.menu.menu_item_click);
                }
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        if (itemId != R.id.mshare) {
                            switch (itemId) {
                                case R.id.mDelete :
                                    CreationAdapter.this.deleteItems(new File(((CreationModel) CreationAdapter.this.arrayList.get(i)).getFilePath()), i);
                                    return false;
                                case R.id.mEdit :
                                    CreationAdapter.this.redirectToEditPage(i);
                                    return false;
                                /*case R.id.mOCR :
                                    CreationAdapter.this.ocrImage(new File(((CreationModel) CreationAdapter.this.arrayList.get(i)).getFilePath()));
                                    return false;*/
                                case R.id.mRename :
                                    CreationAdapter.this.renameFolder(new File(((CreationModel) CreationAdapter.this.arrayList.get(i)).getFilePath()), i);
                                    return false;
                                case R.id.mView :
                                    if (((CreationModel) CreationAdapter.this.arrayList.get(i)).getFileName().contains(".pdf")) {
                                        Intent intent = new Intent(CreationAdapter.this.context, Camera_Scanner_PDFViewerActivity.class);
                                        intent.putExtra("pdfFilePath", ((CreationModel) CreationAdapter.this.arrayList.get(i)).getFilePath());
                                        intent.addFlags(268435456);
                                        CreationAdapter.this.context.startActivity(intent);
                                        return false;
                                    } else if (!((CreationModel) CreationAdapter.this.arrayList.get(i)).getFileName().contains(".txt")) {
                                        return false;
                                    } else {
                                        Intent intent2 = new Intent(CreationAdapter.this.context, TextFileViwerActivity.class);
                                        intent2.putExtra("textFilePath", ((CreationModel) CreationAdapter.this.arrayList.get(i)).getFilePath());
                                        intent2.addFlags(268435456);
                                        CreationAdapter.this.context.startActivity(intent2);
                                        return false;
                                    }
                                default:
                                    return false;
                            }
                        } else {
                            CreationAdapter.this.shareItems(new File(((CreationModel) CreationAdapter.this.arrayList.get(i)).getFilePath()));
                            return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }


    public void redirectToEditPage(int i) {
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(this.arrayList.get(i).getFilePath());
        MainUtils.setRotateFileName(this.context, this.arrayList.get(i).getFilePath());
        Intent intent = new Intent(this.context, ResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ImageList", arrayList2);
        intent.putExtras(bundle);
        intent.addFlags(268435456);
        this.context.startActivity(intent);
    }

    public void renameFolder(File file, int i) {
        final int i2 = i;
        Dialog dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.dialog_save_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        final EditText editText = (EditText) dialog.findViewById(R.id.etFolderName);
        dialog.setCancelable(false);
        ((TextView) dialog.findViewById(R.id.tvRename)).setText("Enter File Name");
        ((ImageView) dialog.findViewById(R.id.ivRename)).setImageDrawable(ContextCompat.getDrawable(this.context, R.drawable.ic_rename_icon));
        final String fileName = this.arrayList.get(i2).getFileName();
        String filePath = this.arrayList.get(i2).getFilePath();
        final String substring = filePath.substring(filePath.lastIndexOf("."));
        final String replace = file.toString().replace(fileName, "");
        editText.setText("" + fileName.replace(substring, ""));
        final EditText editText2 = editText;
        final Dialog dialog2 = dialog;
        final File file2 = file;
        ((LinearLayout) dialog.findViewById(R.id.llOK)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (editText2.getText().length() <= 0) {
                    editText2.setError("Enter File Name");
                } else if (fileName.equals(editText2.getText().toString())) {
                    dialog2.dismiss();
                } else {
                    File file = new File(replace + editText2.getText().toString() + substring);
                    if (file2.renameTo(file)) {
                        ((CreationModel) CreationAdapter.this.arrayList.get(i2)).setFilePath(file.getPath());
                        ((CreationModel) CreationAdapter.this.arrayList.get(i2)).setFileName(editText2.getText().toString() + substring);
                        CreationAdapter.this.notifyDataSetChanged();
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


    public void deleteItems(File file, int i) {
        if (file.exists()) {
            file.delete();
        }
        this.arrayList.remove(i);
        notifyDataSetChanged();
    }

    public void ocrImage(File file) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            this.bitmap = BitmapFactory.decodeStream(new FileInputStream(file), (Rect) null, options);
            final StringBuilder sb = new StringBuilder();
            FirebaseVisionCloudTextRecognizerOptions build = new FirebaseVisionCloudTextRecognizerOptions.Builder().setLanguageHints(Arrays.asList(new String[]{"en", "hi", "gu", "pt", "af", "fil", "it", "mr", "ne", "sa"})).setModelType(2).build();
            FirebaseVision.getInstance().getCloudTextRecognizer(build).processImage(FirebaseVisionImage.fromBitmap(this.bitmap)).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                public void onSuccess(FirebaseVisionText firebaseVisionText) {
                    if (firebaseVisionText.getTextBlocks().size() == 0) {
                        sb.append("No text Found");
                        return;
                    }
                    for (FirebaseVisionText.TextBlock text : firebaseVisionText.getTextBlocks()) {
                        sb.append(text.getText());
                    }
                    if (sb.toString() != null) {
                        Intent intent = new Intent(CreationAdapter.this.context, OCRResultActivity.class);
                        intent.putExtra("textScanned", sb.toString());
                        intent.addFlags(268435456);
                        CreationAdapter.this.context.startActivity(intent);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                public void onFailure(Exception exc) {
                    Log.e("HHHH", "" + exc.getMessage());
                    exc.printStackTrace();
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void shareItems(File file) {
        if (file.exists()) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.SUBJECT", "Here are some files.");
            intent.setType("*/*");
            intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(file));
            intent.addFlags(1);
            this.context.startActivity(Intent.createChooser(intent, "send"));
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private void showLoading() {
        Dialog dialog = new Dialog(this.context);
        this.dialogLoading = dialog;
        dialog.setContentView(R.layout.loading_dialog);
        this.dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialogLoading.setCancelable(false);
        this.dialogLoading.show();
    }
}
