package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.itextpdf.text.html.HtmlTags;
import com.scanner.camera.phototopdf.papercamerascanner.Adapter.ColorAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.BindEditing;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.Model.ColorModel;
import com.scanner.camera.phototopdf.papercamerascanner.OnClickInterface.OnItemClickListener;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import com.scanner.camera.phototopdf.papercamerascanner.common.Privacy_Policy_activity;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class IDCardPagesActivity extends AppCompatActivity implements OnItemClickListener {

    public ArrayList<String> ImagesArray = new ArrayList<>();
    Bitmap bitmapScreen;
    LinearLayout btnBackG;
    LinearLayout btnEdit;
    LinearLayout btnSave;
    LinearLayout btnShare;
    ColorAdapter colorAdapter;
    ArrayList<ColorModel> colorArrayList = new ArrayList<>();
    Dialog dialogLoading;
    DrawableSticker drawableSticker;
    ArrayList<DrawableSticker> drawableStickerArrayList = new ArrayList<>();
    File filePathOld;
    String foldername = "";

    public ArrayList<String> imagesarrayPDF = new ArrayList<>();
    boolean isStickerAvailable = false;
    ImageView ivCancelColor;
    ImageView ivOkColor;
    LinearLayout llColorTool;
    LinearLayout llIDCardBG;
    int oldText = R.color.colorBlack;
    int selectedImagePosition = 0;
    StickerView stickerViewIDCard;
    int text_Color;
    Toolbar toolbar;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_i_d_card_pages);
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.stickerViewIDCard = (StickerView) findViewById(R.id.stickerViewIDCard);
        this.btnShare = (LinearLayout) findViewById(R.id.btnShare);
        this.btnEdit = (LinearLayout) findViewById(R.id.btnEdit);
        this.btnBackG = (LinearLayout) findViewById(R.id.btnBackG);
        this.btnSave = (LinearLayout) findViewById(R.id.btnSave);
        this.llIDCardBG = (LinearLayout) findViewById(R.id.llIDCardBG);
        this.llColorTool = (LinearLayout) findViewById(R.id.llColorTool);
        this.ivCancelColor = (ImageView) findViewById(R.id.ivCancelColor);
        this.ivOkColor = (ImageView) findViewById(R.id.ivOkColor);
        bindColors();
        this.btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                IDCardPagesActivity.this.stickerViewIDCard.removeAllStickers();
                ArrayList arrayList = new ArrayList();
                arrayList.add(IDCardPagesActivity.this.ImagesArray.get(IDCardPagesActivity.this.selectedImagePosition));
                IDCardPagesActivity iDCardPagesActivity = IDCardPagesActivity.this;
                MainUtils.setRotateFileName(iDCardPagesActivity, (String) iDCardPagesActivity.ImagesArray.get(IDCardPagesActivity.this.selectedImagePosition));
                Intent intent = new Intent(IDCardPagesActivity.this, ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ImageList", arrayList);
                intent.putExtras(bundle);

                IDCardPagesActivity.this.startActivity(intent);
            }
        });
        this.btnBackG.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                IDCardPagesActivity.this.llColorTool.setVisibility(0);
                IDCardPagesActivity iDCardPagesActivity = IDCardPagesActivity.this;
                iDCardPagesActivity.oldText = iDCardPagesActivity.text_Color;
            }
        });
        this.ivOkColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                IDCardPagesActivity.this.llColorTool.setVisibility(8);
                IDCardPagesActivity.this.llIDCardBG.setBackgroundColor(IDCardPagesActivity.this.text_Color);
            }
        });
        this.ivCancelColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                IDCardPagesActivity.this.llColorTool.setVisibility(8);
                IDCardPagesActivity iDCardPagesActivity = IDCardPagesActivity.this;
                iDCardPagesActivity.text_Color = iDCardPagesActivity.oldText;
                IDCardPagesActivity.this.llIDCardBG.setBackgroundColor(IDCardPagesActivity.this.text_Color);
            }
        });
        this.btnShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (IDCardPagesActivity.this.ImagesArray.size() > 1) {
                    IDCardPagesActivity.this.shareDialog();
                    return;
                }
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.SUBJECT", "Here are some files.");
                intent.setType("*/*");
                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File((String) IDCardPagesActivity.this.ImagesArray.get(IDCardPagesActivity.this.selectedImagePosition))));
                intent.addFlags(1);
                IDCardPagesActivity.this.startActivity(Intent.createChooser(intent, "send"));
            }
        });
        this.btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                IDCardPagesActivity.this.stickerViewIDCard.remo();
                IDCardPagesActivity.this.llIDCardBG.setDrawingCacheEnabled(true);
                IDCardPagesActivity iDCardPagesActivity = IDCardPagesActivity.this;
                iDCardPagesActivity.bitmapScreen = Bitmap.createBitmap(iDCardPagesActivity.llIDCardBG.getDrawingCache());
                IDCardPagesActivity.this.llIDCardBG.setDrawingCacheEnabled(false);
                IDCardPagesActivity.this.llIDCardBG.performClick();
                Dialog dialog = new Dialog(IDCardPagesActivity.this);
                dialog.setContentView(R.layout.dialog_save_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                final EditText editText = (EditText) dialog.findViewById(R.id.etFolderName);
                dialog.setCancelable(false);
                final String name = new File((String) IDCardPagesActivity.this.ImagesArray.get(0)).getParentFile().getName();
                final String replace = IDCardPagesActivity.this.filePathOld.toString().replace(name, "");
                editText.setText("" + name);
                final EditText editText2 = editText;
                final Dialog dialog2 = dialog;
                ((LinearLayout) dialog.findViewById(R.id.llOK)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        File[] listFiles;
                        if (editText2.getText().length() <= 0) {
                            editText2.setError("Enter Folder Name");
                        } else if (name.equals(editText2.getText().toString())) {
                            dialog2.dismiss();
                            for (int i = 0; i < IDCardPagesActivity.this.ImagesArray.size(); i++) {
                                IDCardPagesActivity.this.imagesarrayPDF.add(IDCardPagesActivity.this.ImagesArray.get(i));
                            }
                            new saveEditedScreen().execute(new Void[0]);
                        } else {
                            File file = new File(replace + editText2.getText().toString());
                            if (IDCardPagesActivity.this.filePathOld.renameTo(file)) {
                                IDCardPagesActivity.this.foldername = file.getPath().replace(Environment.getExternalStorageDirectory().toString() + "/", "");
                                Log.e("HHHH", "HHHHHHHHHHHHHHH  " + IDCardPagesActivity.this.foldername);
                                IDCardPagesActivity.this.ImagesArray.clear();
                                String str = Environment.getExternalStorageDirectory().toString() + "/" + IDCardPagesActivity.this.foldername;
                                IDCardPagesActivity.this.filePathOld = new File(str);
                                if (new File(str).exists() && (listFiles = new File(str).listFiles()) != null && listFiles.length > 0) {
                                    for (File file2 : listFiles) {
                                        if (file2.getPath().contains(".png") || file2.getPath().contains(".jpg")) {
                                            IDCardPagesActivity.this.ImagesArray.add(file2.getPath());
                                            IDCardPagesActivity.this.imagesarrayPDF.add(file2.getPath());
                                        }
                                    }
                                }
                                dialog2.dismiss();
                                new saveEditedScreen().execute(new Void[0]);
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
        });
        this.stickerViewIDCard.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            public void onStickerDoubleTapped(Sticker sticker) {
            }

            public void onStickerDragFinished(Sticker sticker) {
            }

            public void onStickerFlipped(Sticker sticker) {
            }

            public void onStickerTouchedDown(Sticker sticker) {
            }

            public void onStickerZoomFinished(Sticker sticker) {
            }

            public void onStickerAdded(Sticker sticker) {
                if (sticker instanceof DrawableSticker) {
                    IDCardPagesActivity.this.isStickerAvailable = true;
                }
            }

            public void onStickerClicked(Sticker sticker) {
                if (sticker instanceof DrawableSticker) {
                    for (int i = 0; i < IDCardPagesActivity.this.drawableStickerArrayList.size(); i++) {
                        if (IDCardPagesActivity.this.drawableStickerArrayList.get(i).equals(sticker)) {
                            IDCardPagesActivity.this.selectedImagePosition = i;
                        }
                    }
                    IDCardPagesActivity.this.isStickerAvailable = true;
                }
            }

            public void onStickerDeleted(Sticker sticker) {
                if (sticker instanceof DrawableSticker) {
                    for (int i = 0; i < IDCardPagesActivity.this.drawableStickerArrayList.size(); i++) {
                        if (IDCardPagesActivity.this.drawableStickerArrayList.get(i).equals(sticker)) {
                            File file = new File((String) IDCardPagesActivity.this.ImagesArray.get(i));
                            if (file.exists()) {
                                file.delete();
                                IDCardPagesActivity.this.ImagesArray.remove(i);
                                IDCardPagesActivity.this.drawableStickerArrayList.remove(i);
                            }
                        }
                    }
                }
            }
        });
    }

    private void init() {
        File[] listFiles;
        Intent intent = getIntent();
        if (intent != null) {
            this.foldername = intent.getStringExtra("folderName");
            Log.e("HHHH", "HHHHHHHHHHHHHHH  " + this.foldername);
            String str = Environment.getExternalStorageDirectory().toString() + "/" + this.foldername;
            this.filePathOld = new File(str);
            if (new File(str).exists() && (listFiles = new File(str).listFiles()) != null && listFiles.length > 0) {
                for (File path : listFiles) {
                    this.ImagesArray.add(path.getPath());
                }
            }
        }
        for (int i = 0; i < this.ImagesArray.size(); i++) {
            File file = new File(this.ImagesArray.get(i));
            if (file.exists()) {
                Log.e("HHHH", "Image exists ");
                Bitmap decodeFile = BitmapFactory.decodeFile(file.getAbsolutePath());
                Log.e("HHHH", "Bitmap  " + decodeFile);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), decodeFile);
                DrawableSticker drawableSticker2 = new DrawableSticker(bitmapDrawable);
                this.drawableSticker = drawableSticker2;
                this.drawableStickerArrayList.add(drawableSticker2);
                this.drawableSticker.setDrawable((Drawable) bitmapDrawable);
                if (i == 0) {
                    this.stickerViewIDCard.addSticker(this.drawableSticker, 2);
                } else {
                    this.stickerViewIDCard.addSticker(this.drawableSticker, 16);
                }
                Log.e("HHHH", "drawableStickerArrayList Size  " + this.drawableStickerArrayList.size());
            }
        }
    }

    public void OnClick(View view, int i, String str) {
        if (((str.hashCode() == 94842723 && str.equals(HtmlTags.COLOR)) ? (char) 0 : 65535) == 0) {
            this.text_Color = Color.parseColor(this.colorArrayList.get(i).getColorCode());
            for (int i2 = 0; i2 < this.colorArrayList.size(); i2++) {
                if (i2 == i) {
                    this.colorArrayList.get(i).setSelected(true);
                } else {
                    this.colorArrayList.get(i2).setSelected(false);
                }
            }
            this.colorAdapter.notifyDataSetChanged();
        }
    }

    private class saveEditedScreen extends AsyncTask<Void, Void, Void> {
        private saveEditedScreen() {
        }


        public void onPreExecute() {
            super.onPreExecute();
            IDCardPagesActivity.this.showLoading();
        }


        public Void doInBackground(Void... voidArr) {
            String file = Environment.getExternalStorageDirectory().toString();
            File file2 = new File(file + "/" + IDCardPagesActivity.this.foldername);
            file2.mkdirs();
            File file3 = new File(file2, "IDCard_" + Long.toString(System.currentTimeMillis()) + "3.png");
            if (file3.exists()) {
                file3.delete();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file3);
                IDCardPagesActivity.this.bitmapScreen.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                IDCardPagesActivity.this.imagesarrayPDF.add(file3.getAbsolutePath());
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Void voidR) {
            IDCardPagesActivity.this.dialogLoading.dismiss();
            Intent intent = new Intent(IDCardPagesActivity.this, Camera_Scanner_DisplayIDCardResultActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("folderName", IDCardPagesActivity.this.foldername);
            intent.putExtras(bundle);

            IDCardPagesActivity.this.startActivity(intent);
            IDCardPagesActivity.this.finish();
        }
    }


    public void shareDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_share);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(true);
        ((LinearLayout) dialog.findViewById(R.id.btnSingle)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.SUBJECT", "Here are some files.");
                intent.setType("*/*");
                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File((String) IDCardPagesActivity.this.ImagesArray.get(IDCardPagesActivity.this.selectedImagePosition))));
                intent.addFlags(1);
                IDCardPagesActivity.this.startActivity(Intent.createChooser(intent, "send"));
                dialog.dismiss();
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.btnAllImage)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND_MULTIPLE");
                intent.putExtra("android.intent.extra.SUBJECT", "Here are some files.");
                intent.setType("*/*");
                ArrayList arrayList = new ArrayList();
                Iterator it = IDCardPagesActivity.this.ImagesArray.iterator();
                while (it.hasNext()) {
                    arrayList.add(Uri.fromFile(new File((String) it.next())));
                }
                intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
                IDCardPagesActivity.this.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void bindColors() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvColors);
        this.colorArrayList = BindEditing.bindTextColor();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 6));
        ColorAdapter colorAdapter2 = new ColorAdapter(this, this.colorArrayList, this, HtmlTags.COLOR);
        this.colorAdapter = colorAdapter2;
        recyclerView.setAdapter(colorAdapter2);
    }


    public void showLoading() {
        Dialog dialog = new Dialog(this);
        this.dialogLoading = dialog;
        dialog.setContentView(R.layout.loading_dialog);
        this.dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialogLoading.setCancelable(false);
        this.dialogLoading.show();
    }


    public void onResume() {
        super.onResume();
        if (this.stickerViewIDCard.getStickerCount() == 0) {
            this.ImagesArray.clear();
            this.drawableStickerArrayList.clear();
            init();
        }
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (this.llColorTool.getVisibility() == 0) {
            this.llColorTool.setVisibility(8);
        } else {
            this.ImagesArray.clear();
            this.stickerViewIDCard.removeAllStickers();
            this.drawableStickerArrayList.clear();
        }
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 16908332:
                onBackPressed();
                return true;

            /*case R.id.contact :
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("message/rfc822");
                intent2.putExtra("android.intent.extra.EMAIL", new String[]{"thepurpleclubinc@gmail.com"});
                intent2.putExtra("android.intent.extra.SUBJECT", "");
                intent2.putExtra("android.intent.extra.TEXT", "");
                try {
                    startActivity(Intent.createChooser(intent2, "Send mail..."));
                } catch (ActivityNotFoundException unused) {
                }
                return true;*/
            case R.id.privacy :
                Intent intent3 = new Intent(getApplicationContext(), Privacy_Policy_activity.class);

                startActivity(intent3);
                return true;
            case R.id.rate :
                if (isOnline()) {
                    Intent intent4 = new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName()));

                    intent4.addFlags(268435456);
                    startActivity(intent4);
                } else {
                    Toast makeText = Toast.makeText(getApplicationContext(), "No Internet Connection..", 0);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                }
                return true;
            case R.id.share :
                if (isOnline()) {
                    Intent intent5 = new Intent("android.intent.action.SEND");
                    intent5.setType("text/plain");
                    intent5.putExtra("android.intent.extra.TEXT", "Hi! I'm using A4 Paper Scanner  : Paper Scanner. Check it out:http://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(Intent.createChooser(intent5, "Share with Friends"));
                } else {
                    Toast makeText2 = Toast.makeText(getApplicationContext(), "No Internet Connection..", 0);
                    makeText2.setGravity(17, 0, 0);
                    makeText2.show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
