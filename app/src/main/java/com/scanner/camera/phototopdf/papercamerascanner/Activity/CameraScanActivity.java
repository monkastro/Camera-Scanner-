package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.scanner.camera.phototopdf.papercamerascanner.CustomView.CameraPreview;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.BasicActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Objects;

public class CameraScanActivity extends Activity {
    public static final String LOG_TAG = "NiceCameraExample";
    Bitmap bitmapSingle;
    Bitmap[] bitmapTwo;
    ImageView bookMiddleLine;
    TextView bookWarning;
    LinearLayout btnCBook;
    LinearLayout btnCDocument;
    LinearLayout btnCIdCard;
    LinearLayout btnCPhoto;
    ImageView button_back;
    ImageView button_finish;
    ImageView button_flash;

    public CameraPreview camPreview;

    public Camera camera;
    private int cameraID;
    Dialog dialogLoading;
    String flashMode = "off";
    String folder_save_book = ("Document Scanner/Book/Book" + Long.toString(System.currentTimeMillis()));
    String folder_save_business_id = ("Document Scanner/Business Card/Business_Card" + Long.toString(System.currentTimeMillis()));
    String folder_save_doc = ("Document Scanner/Document/Document" + Long.toString(System.currentTimeMillis()));
    String folder_save_id_card = ("Document Scanner/ID Card/ID_Card" + Long.toString(System.currentTimeMillis()));
    String folder_save_ocr = ("Document Scanner/OCR Files/Ocr_Scan" + Long.toString(System.currentTimeMillis()));
    String folder_save_passport_doc = ("Document Scanner/Photo ID/Photo_Scan" + Long.toString(System.currentTimeMillis()));
    View lineBook;
    View lineDocument;
    View lineIdCard;
    View linePhoto;
    String savedFolderName = "";
    String scanOption = null;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.addFlags(Integer.MIN_VALUE);

        window.setStatusBarColor(getResources().getColor(R.color.colorBlack));
        setContentView(R.layout.activity_camera_scan);
        this.bookMiddleLine = (ImageView) findViewById(R.id.bookMiddleLine);
        this.bookWarning = (TextView) findViewById(R.id.bookWar);
        this.button_back = (ImageView) findViewById(R.id.button_back);
        this.btnCIdCard = (LinearLayout) findViewById(R.id.btnCIdCard);
        this.btnCBook = (LinearLayout) findViewById(R.id.btnCBook);
        this.btnCDocument = (LinearLayout) findViewById(R.id.btnCDocument);
        this.btnCPhoto = (LinearLayout) findViewById(R.id.btnCPhoto);
        this.linePhoto = findViewById(R.id.linePhoto);
        this.lineBook = findViewById(R.id.lineBook);
        this.lineDocument = findViewById(R.id.lineDocument);
        this.lineIdCard = findViewById(R.id.lineIdCard);
        selectedCameraType(MainUtils.getScanType(this));
        MainUtils.setFromEdit(this, false);
        if (setCameraInstance()) {
            this.camPreview = new CameraPreview(this, this.camera, this.cameraID);
        } else {
            finish();
        }
        ((RelativeLayout) findViewById(R.id.preview_layout)).addView(this.camPreview);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.camPreview.getLayoutParams();
        layoutParams.width = -1;
        layoutParams.height = -1;
        this.camPreview.setLayoutParams(layoutParams);
        this.button_finish = (ImageView) findViewById(R.id.button_finish);
        this.button_flash = (ImageView) findViewById(R.id.button_flash);
        MainUtils.isFolderCreated();
        setFolderName();
        this.button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CameraScanActivity.this.onBackPressed();
            }
        });
        ((ImageView) findViewById(R.id.button_capture)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CameraScanActivity.this.camera.takePicture((Camera.ShutterCallback) null, (Camera.PictureCallback) null, CameraScanActivity.this.camPreview);
                Handler handler = new Handler(Looper.getMainLooper());
                CameraScanActivity.this.showLoading();
                if (MainUtils.getScanType(CameraScanActivity.this).equals("Book")) {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            CameraScanActivity.this.bitmapTwo = CameraScanActivity.this.splitBitmap(CameraScanActivity.this.camPreview.getClickBitmap());
                            new saveImageMultiple().execute(new Void[0]);
                        }
                    }, 3000);
                } else {
                    handler.postDelayed(new Runnable() {
                        public void run() {
                            CameraScanActivity.this.bitmapSingle = CameraScanActivity.this.camPreview.getClickBitmap();
                            new saveImageSingle().execute(new Void[0]);
                        }
                    }, 3000);
                }
            }
        });
        this.button_finish.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (MainUtils.getScanType(CameraScanActivity.this).equals("Book") || MainUtils.getScanType(CameraScanActivity.this).equals("Document") || MainUtils.getScanType(CameraScanActivity.this).equals("Passport")) {
                    Intent intent = new Intent(CameraScanActivity.this, Camera_Scanner_BookScanPagesActivity.class);
                    intent.putExtra("folderName", CameraScanActivity.this.savedFolderName);
                    CameraScanActivity.this.startActivity(intent);
                } else if (MainUtils.getScanType(CameraScanActivity.this).equals("IDCard") || MainUtils.getScanType(CameraScanActivity.this).equals("BusinessCard")) {
                    Intent intent2 = new Intent(CameraScanActivity.this, IDCardPagesActivity.class);
                    intent2.putExtra("folderName", CameraScanActivity.this.savedFolderName);
                    MainUtils.setTakenIdCardSides(CameraScanActivity.this, 0);
                    MainUtils.setIdCardSides(CameraScanActivity.this, 1);
                    MainUtils.setCameraDialogVisible(CameraScanActivity.this, true);
                    CameraScanActivity.this.startActivity(intent2);
                }
                MainUtils.setTakenImagesCount(CameraScanActivity.this, 0);
                CameraScanActivity.this.finish();
            }
        });
        this.button_flash.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CameraScanActivity cameraScanActivity = CameraScanActivity.this;
                cameraScanActivity.flashModeSet(cameraScanActivity.flashMode);
                CameraScanActivity.this.camPreview.flashModeSet();
            }
        });
        this.btnCIdCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CameraScanActivity.this.selectedCameraType("IDCard");
            }
        });
        this.btnCDocument.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CameraScanActivity.this.selectedCameraType("Document");
            }
        });
        this.btnCBook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CameraScanActivity.this.selectedCameraType("Book");
            }
        });
        this.btnCPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CameraScanActivity.this.selectedCameraType("Passport");
            }
        });
        fixElementsPosition(getResources().getConfiguration().orientation);
    }


    public void flashModeSet(String string2) {
        int n;
        int n2 = string2.hashCode();
        if (n2 != 3551) {
            if (n2 != 109935) return;
            if (!string2.equals((Object)"off")) return;
            n = 0;
        } else {
            if (!string2.equals((Object)"on")) return;
            n = 1;
        }
        if (n != 0) {
            if (n != 1) {
                return;
            }
            this.flashMode = "off";
            this.button_flash.setImageResource(R.drawable.ic_camera_flash_off_icon);
            return;
        }
        this.flashMode = "on";
        this.button_flash.setImageResource(R.drawable.ic_camera_flash_on_icon);}

    private class saveImageMultiple extends AsyncTask<Void, Void, Void> {
        File file;

        private saveImageMultiple() {
        }


        public void onPreExecute() {
            super.onPreExecute();
        }


        public Void doInBackground(Void... voidArr) {
            if (CameraScanActivity.this.bitmapTwo != null) {
                String str = "BookScan_"+ Long.toString(System.currentTimeMillis());
                for (int i = 0; i < 2; i++) {
                    File file2 = new File(Environment.getExternalStorageDirectory(), CameraScanActivity.this.savedFolderName + "/" + str + i + ".png");
                    this.file = file2;
                    if (file2.exists()) {
                        this.file.delete();
                    }
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(this.file);
                        if (i == 0) {
                            CameraScanActivity.this.bitmapTwo[i].compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                        } else if (i == 1) {
                            CameraScanActivity.this.bitmapTwo[i].compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                            CameraScanActivity.this.bitmapTwo = null;
                        }
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                Log.e("HHHH", "Capture other side tooo");
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void voidR) {
            CameraScanActivity.this.dialogLoading.dismiss();
            MainUtils.setTakenImagesCount(CameraScanActivity.this, MainUtils.getTakenImagesCount(CameraScanActivity.this) + 2);
            CameraScanActivity.this.button_finish.setVisibility(0);
            CameraScanActivity.this.camPreview.clearClickBitmap();
        }
    }

    private class saveImageSingle extends AsyncTask<Void, Void, Void> {
        File file;

        private saveImageSingle() {
        }


        public void onPreExecute() {
            super.onPreExecute();
        }


        public Void doInBackground(Void... voidArr) {
            if (CameraScanActivity.this.bitmapSingle != null) {
                MainUtils.isFolderCreated();
                if (MainUtils.getSaveFolder(CameraScanActivity.this).equals("Passport")) {
                    File file2 = new File(Environment.getExternalStorageDirectory(), CameraScanActivity.this.savedFolderName);
                    if (!file2.exists()) {
                        file2.mkdir();
                    }
                    File externalStorageDirectory = Environment.getExternalStorageDirectory();
                    this.file = new File(externalStorageDirectory, CameraScanActivity.this.savedFolderName + "/" + ("PhotoID_"+ Long.toString(System.currentTimeMillis())  + ".png"));
                } else if (MainUtils.getSaveFolder(CameraScanActivity.this).equals("Book")) {
                    File file3 = new File(Environment.getExternalStorageDirectory(), CameraScanActivity.this.savedFolderName);
                    if (!file3.exists()) {
                        file3.mkdir();
                    }
                    File externalStorageDirectory2 = Environment.getExternalStorageDirectory();
                    this.file = new File(externalStorageDirectory2, CameraScanActivity.this.savedFolderName + "/" + ("BookScan_" ) + Long.toString(System.currentTimeMillis())+ ".png");
                } else if (MainUtils.getSaveFolder(CameraScanActivity.this).equals("Document")) {
                    File file4 = new File(Environment.getExternalStorageDirectory(), CameraScanActivity.this.savedFolderName);
                    if (!file4.exists()) {
                        file4.mkdir();
                    }
                    File externalStorageDirectory3 = Environment.getExternalStorageDirectory();
                    this.file = new File(externalStorageDirectory3, CameraScanActivity.this.savedFolderName + "/" + ("DocScan_"+ Long.toString(System.currentTimeMillis()) ) + ".png");
                } else if (MainUtils.getSaveFolder(CameraScanActivity.this).equals("IDCard")) {
                    File file5 = new File(Environment.getExternalStorageDirectory(), CameraScanActivity.this.savedFolderName);
                    if (!file5.exists()) {
                        file5.mkdir();
                    }
                    File externalStorageDirectory4 = Environment.getExternalStorageDirectory();
                    this.file = new File(externalStorageDirectory4, CameraScanActivity.this.savedFolderName + "/" + ("IDScan_"+ Long.toString(System.currentTimeMillis())) + ".png");
                } else if (MainUtils.getSaveFolder(CameraScanActivity.this).equals("BusinessCard")) {
                    File file6 = new File(Environment.getExternalStorageDirectory(), CameraScanActivity.this.savedFolderName);
                    if (!file6.exists()) {
                        file6.mkdir();
                    }
                    File externalStorageDirectory5 = Environment.getExternalStorageDirectory();
                    this.file = new File(externalStorageDirectory5, CameraScanActivity.this.savedFolderName + "/" + ("CardScan_"+ Long.toString(System.currentTimeMillis()) ) + ".png");
                } else if (MainUtils.getSaveFolder(CameraScanActivity.this).equals("OCRText")) {
                    File file7 = new File(Environment.getExternalStorageDirectory(), CameraScanActivity.this.savedFolderName);
                    if (!file7.exists()) {
                        file7.mkdir();
                    }
                    File externalStorageDirectory6 = Environment.getExternalStorageDirectory();
                    this.file = new File(externalStorageDirectory6, CameraScanActivity.this.savedFolderName + "/" + ("OcrScan_"+ Long.toString(System.currentTimeMillis()) ) + ".png");
                }
                if (this.file.exists()) {
                    this.file.delete();
                }
                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(this.file);
                    if (MainUtils.getScanType(CameraScanActivity.this).equals("Document")) {
                        CameraScanActivity.this.createContrast(CameraScanActivity.this.bitmapSingle, 50.0d).compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    } else {
                        CameraScanActivity.this.bitmapSingle.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    }
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return null;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            } else {
                Log.e("HHHH", "Data not found");
                return null;
            }
        }


        public void onPostExecute(Void voidR) {
            MainUtils.setTakenImagesCount(CameraScanActivity.this, MainUtils.getTakenImagesCount(CameraScanActivity.this) + 1);
            if (MainUtils.getScanType(CameraScanActivity.this).equals("Passport")) {
                MainUtils.setRotateFileName(CameraScanActivity.this, this.file.getPath());
                CameraScanActivity.this.dialogLoading.dismiss();
                CameraScanActivity.this.camPreview.clearClickBitmap();
                Intent intent = new Intent(CameraScanActivity.this, BasicActivity.class);
                intent.putExtra("imageUrl", Uri.fromFile(this.file).toString());
                CameraScanActivity.this.startActivity(intent);
                return;
            }
            CameraScanActivity.this.dialogLoading.dismiss();
            CameraScanActivity.this.camPreview.clearClickBitmap();
            Intent intent2 = new Intent(CameraScanActivity.this, Camera_Scanner_EdgeDetectionActivity.class);
            intent2.putExtra("imageUrl", this.file.getPath());
            CameraScanActivity.this.startActivity(intent2);
        }
    }

    public Bitmap createContrast(Bitmap bitmap, double d) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        double pow = Math.pow((d + 100.0d) / 100.0d, 2.0d);
        for (int i = 0; i < width; i++) {
            int i2 = 0;
            while (i2 < height) {
                int pixel = bitmap.getPixel(i, i2);
                int alpha = Color.alpha(pixel);
                int red = (int) (((((((double) Color.red(pixel)) / 255.0d) - 0.5d) * pow) + 0.5d) * 255.0d);
                int i3 = 255;
                if (red < 0) {
                    red = 0;
                } else if (red > 255) {
                    red = 255;
                }
                int i4 = width;
                int i5 = height;
                int red2 = (int) (((((((double) Color.red(pixel)) / 255.0d) - 0.5d) * pow) + 0.5d) * 255.0d);
                if (red2 < 0) {
                    red2 = 0;
                } else if (red2 > 255) {
                    red2 = 255;
                }
                int red3 = (int) (((((((double) Color.red(pixel)) / 255.0d) - 0.5d) * pow) + 0.5d) * 255.0d);
                if (red3 < 0) {
                    i3 = 0;
                } else if (red3 <= 255) {
                    i3 = red3;
                }
                createBitmap.setPixel(i, i2, Color.argb(alpha, red, red2, i3));
                i2++;
                width = i4;
                height = i5;
            }
            int i6 = width;
            int i7 = height;
        }
        return createBitmap;
    }

    public Bitmap[] splitBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.postRotate(270.0f);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight() / 2);
        Bitmap createBitmap2 = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2, bitmap.getWidth(), bitmap.getHeight() / 2);
        return new Bitmap[]{Bitmap.createBitmap(createBitmap, 0, 0, createBitmap.getWidth(), createBitmap.getHeight(), matrix, true), Bitmap.createBitmap(createBitmap2, 0, 0, createBitmap2.getWidth(), createBitmap2.getHeight(), matrix, true)};
    }

    private void openIDDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_id_card);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(0));
        dialog.setCancelable(false);
        ((LinearLayout) dialog.findViewById(R.id.btnSingleSide)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CameraScanActivity.this.scanOption = "SingleSide";
                dialog.dismiss();
                MainUtils.setCameraDialogVisible(CameraScanActivity.this, false);
                MainUtils.setIdCardSides(CameraScanActivity.this, 1);
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.btnDoubleSide)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CameraScanActivity.this.scanOption = "DoubleSide";
                dialog.dismiss();
                MainUtils.setCameraDialogVisible(CameraScanActivity.this, false);
                MainUtils.setIdCardSides(CameraScanActivity.this, 2);
            }
        });
        ((ImageView) dialog.findViewById(R.id.ivCloseDialog)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                MainUtils.setCameraDialogVisible(CameraScanActivity.this, true);
                MainUtils.setScanType(CameraScanActivity.this, "Document");
                CameraScanActivity cameraScanActivity = CameraScanActivity.this;
                cameraScanActivity.selectedCameraType(MainUtils.getScanType(cameraScanActivity));
            }
        });
        dialog.show();
    }


    public void setFolderName() {
        switch (MainUtils.getSaveFolder(this)) {
            case "Passport":
                File file = new File(Environment.getExternalStorageDirectory(), this.folder_save_passport_doc);
                if (!file.exists()) {
                    file.mkdir();
                }
                this.savedFolderName = this.folder_save_passport_doc;
                break;
            case "Book":
                File file2 = new File(Environment.getExternalStorageDirectory(), this.folder_save_book);
                if (!file2.exists()) {
                    file2.mkdir();
                }
                this.savedFolderName = this.folder_save_book;
                break;
            case "Document":
                File file3 = new File(Environment.getExternalStorageDirectory(), this.folder_save_doc);
                if (!file3.exists()) {
                    file3.mkdir();
                }
                this.savedFolderName = this.folder_save_doc;
                break;
            case "IDCard":
                File file4 = new File(Environment.getExternalStorageDirectory(), this.folder_save_id_card);
                if (!file4.exists()) {
                    file4.mkdir();
                }
                this.savedFolderName = this.folder_save_id_card;
                break;
            case "BusinessCard":
                File file5 = new File(Environment.getExternalStorageDirectory(), this.folder_save_business_id);
                if (!file5.exists()) {
                    file5.mkdir();
                }
                this.savedFolderName = this.folder_save_business_id;
                break;
            case "OCRText":
                File file6 = new File(Environment.getExternalStorageDirectory(), this.folder_save_ocr);
                if (!file6.exists()) {
                    file6.mkdir();
                }
                this.savedFolderName = this.folder_save_ocr;
                break;
        }
    }


    public void selectedCameraType(final String str) {
        if (MainUtils.getTakenImagesCount(this) > 0) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_camera_discard);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            dialog.setCancelable(false);
            ((LinearLayout) dialog.findViewById(R.id.btnCancelDialog)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            ((LinearLayout) dialog.findViewById(R.id.btnDelete)).setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    CameraScanActivity.this.deleteRecursive(new File(Environment.getExternalStorageDirectory(), CameraScanActivity.this.savedFolderName));
                    CameraScanActivity.this.setFolderName();
                    MainUtils.setTakenIdCardSides(CameraScanActivity.this, 0);
                    MainUtils.setIdCardSides(CameraScanActivity.this, 1);
                    MainUtils.setCameraDialogVisible(CameraScanActivity.this, true);
                    MainUtils.setTakenImagesCount(CameraScanActivity.this, 0);
                    CameraScanActivity.this.button_finish.setVisibility(8);
                    CameraScanActivity.this.selectedCameraType(str);
                    CameraScanActivity.this.bitmapSingle = null;
                    CameraScanActivity.this.bitmapTwo = null;
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (str.equals("IDCard")) {
            MainUtils.setScanType(this, "IDCard");
            this.lineIdCard.setBackgroundColor(ContextCompat.getColor(this, R.color.colorYellowMain));
            this.lineDocument.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            this.lineBook.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            this.linePhoto.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            this.bookMiddleLine.setVisibility(8);
            this.bookWarning.setVisibility(8);
            if (MainUtils.getCameraDialogVisible(this)) {
                openIDDialog();
            }
        } else if (str.equals("Document")) {
            MainUtils.setScanType(this, "Document");
            this.lineIdCard.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            this.lineDocument.setBackgroundColor(ContextCompat.getColor(this, R.color.colorYellowMain));
            this.lineBook.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            this.linePhoto.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            this.bookMiddleLine.setVisibility(8);
            this.bookWarning.setVisibility(8);
            MainUtils.setCameraDialogVisible(this, true);
        } else if (str.equals("Book")) {
            MainUtils.setScanType(this, "Book");
            this.lineIdCard.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            this.lineDocument.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            this.lineBook.setBackgroundColor(ContextCompat.getColor(this, R.color.colorYellowMain));
            this.linePhoto.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            this.bookMiddleLine.setVisibility(0);
            this.bookWarning.setVisibility(0);
            MainUtils.setCameraDialogVisible(this, true);
        } else if (str.equals("Passport")) {
            MainUtils.setScanType(this, "Passport");
            this.lineIdCard.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            this.lineDocument.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            this.lineBook.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlack));
            this.linePhoto.setBackgroundColor(ContextCompat.getColor(this, R.color.colorYellowMain));
            MainUtils.setFromEdit(this, false);
            this.bookMiddleLine.setVisibility(8);
            this.bookWarning.setVisibility(8);
            MainUtils.setCameraDialogVisible(this, true);
        }
    }

    public void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File deleteRecursive : file.listFiles()) {
                deleteRecursive(deleteRecursive);
            }
        }
        file.delete();
    }


    public void onResume() {
        super.onResume();
        if (setCameraInstance()) {
            if (MainUtils.getScanType(this).equals("IDCard") && MainUtils.getIdCardSides(this) == MainUtils.getTakenIdCardSides(this)) {
                Intent intent = new Intent(this, IDCardPagesActivity.class);
                if (MainUtils.getSaveFolder(this).equals("IDCard")) {
                    intent.putExtra("folderName", this.savedFolderName);
                } else {
                    intent.putExtra("folderName", this.savedFolderName);
                }
                MainUtils.setTakenIdCardSides(this, 0);
                MainUtils.setIdCardSides(this, 1);
                MainUtils.setCameraDialogVisible(this, true);
                startActivity(intent);
                finish();
            }
            if (MainUtils.getTakenImagesCount(this) > 0) {
                this.button_finish.setVisibility(0);
            } else {
                this.button_finish.setVisibility(8);
            }
        } else {
            Log.e(LOG_TAG, "onResume(): can't reconnect the camera");
            finish();
        }
    }


    public void onPause() {
        super.onPause();
        releaseCameraInstance();
    }


    public void onDestroy() {
        super.onDestroy();
        MainUtils.setCameraDialogVisible(this, true);
        MainUtils.setTakenImagesCount(this, 0);
        releaseCameraInstance();
    }

    public void onConfigurationChanged(@NotNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        fixElementsPosition(configuration.orientation);
    }

    private boolean setCameraInstance() {
        if (this.camera != null) {
            Log.i(LOG_TAG, "setCameraInstance(): camera is already set, nothing to do");
            return true;
        }
        int i = this.cameraID;
        if (i < 0) {
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            int i2 = 0;
            while (i2 < Camera.getNumberOfCameras()) {
                Camera.getCameraInfo(i2, cameraInfo);
                if (cameraInfo.facing == 0) {
                    try {
                        this.camera = Camera.open(i2);
                        this.cameraID = i2;
                        return true;
                    } catch (RuntimeException e) {
                        Log.e(LOG_TAG, "setCameraInstance(): trying to open camera #" + i2 + " but it's locked", e);
                    }
                } else {
                    i2++;
                }
            }
        } else {
            try {
                this.camera = Camera.open(i);
            } catch (RuntimeException e2) {
                Log.e(LOG_TAG, "setCameraInstance(): trying to re-open camera #" + this.cameraID + " but it's locked", e2);
            }
        }
        if (this.camera == null) {
            try {
                this.camera = Camera.open();
                this.cameraID = 0;
            } catch (RuntimeException e3) {
                Log.e(LOG_TAG, "setCameraInstance(): trying to open default camera but it's locked. The camera is not available for this app at the moment.", e3);
                return false;
            }
        }
        Log.i(LOG_TAG, "setCameraInstance(): successfully set camera #" + this.cameraID);
        return true;
    }

    private void releaseCameraInstance() {
        Camera camera2 = this.camera;
        if (camera2 != null) {
            try {
                camera2.stopPreview();
            } catch (Exception unused) {
                Log.i(LOG_TAG, "releaseCameraInstance(): tried to stop a non-existent preview, this is not an error");
            }
            this.camera.setPreviewCallback((Camera.PreviewCallback) null);
            this.camera.release();
            this.camera = null;
            this.cameraID = -1;
            Log.i(LOG_TAG, "releaseCameraInstance(): camera has been released.");
        }
    }

    private void fixElementsPosition(int i) {
        ImageView imageView = (ImageView) findViewById(R.id.button_capture);
    }

    public Camera getCamera() {
        return this.camera;
    }

    public int getCameraID() {
        return this.cameraID;
    }


    public void showLoading() {
        Dialog dialog = new Dialog(this);
        this.dialogLoading = dialog;
        dialog.setContentView(R.layout.loading_dialog);
        this.dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialogLoading.setCancelable(false);
        this.dialogLoading.show();
    }

    public void onBackPressed() {
        super.onBackPressed();
        releaseCameraInstance();
        MainUtils.setCameraDialogVisible(this, true);
        MainUtils.setTakenImagesCount(this, 0);
        finish();
    }
}
