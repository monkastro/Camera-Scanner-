package com.scanner.camera.phototopdf.papercamerascanner.HelperClass;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import com.itextpdf.text.Annotation;
import com.scanner.camera.phototopdf.papercamerascanner.Activity.Camera_Scanner_EdgeDetectionActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;

public class MainUtils {
    public static void setScanType(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences("ScanType", 0).edit();
        edit.putString("ScanType", str);
        edit.apply();
    }

    public static String getScanType(Context context) {
        return context.getSharedPreferences("ScanType", 0).getString("ScanType", "Book");
    }

    public static void setSaveFolder(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences("SaveFolder", 0).edit();
        edit.putString("SaveFolder", str);
        edit.apply();
    }

    public static String getSaveFolder(Context context) {
        return context.getSharedPreferences("SaveFolder", 0).getString("SaveFolder", "Document");
    }

    public static void setCameraDialogVisible(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences("CameraDialog", 0).edit();
        edit.putBoolean("CameraDialog", z);
        edit.apply();
    }

    public static boolean getCameraDialogVisible(Context context) {
        return context.getSharedPreferences("CameraDialog", 0).getBoolean("CameraDialog", true);
    }

    public static void setFromEdit(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences("FromEdit", 0).edit();
        edit.putBoolean("FromEdit", z);
        edit.apply();
    }

    public static boolean getFromEdit(Context context) {
        return context.getSharedPreferences("FromEdit", 0).getBoolean("FromEdit", false);
    }

    public static void setFromGallaryEdge(Context context, boolean z) {
        SharedPreferences.Editor edit = context.getSharedPreferences("GallaryEdge", 0).edit();
        edit.putBoolean("GallaryEdge", z);
        edit.apply();
    }

    public static boolean getFromGallaryEdge(Context context) {
        return context.getSharedPreferences("GallaryEdge", 0).getBoolean("GallaryEdge", false);
    }

    public static void setIdCardSides(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences("IdCardSides", 0).edit();
        edit.putInt("IdCardSides", i);
        edit.apply();
    }

    public static int getIdCardSides(Context context) {
        return context.getSharedPreferences("IdCardSides", 0).getInt("IdCardSides", 1);
    }

    public static void setTakenIdCardSides(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences("TakenIdCardSides", 0).edit();
        edit.putInt("TakenIdCardSides", i);
        edit.apply();
    }

    public static int getTakenIdCardSides(Context context) {
        return context.getSharedPreferences("TakenIdCardSides", 0).getInt("TakenIdCardSides", 0);
    }

    public static void setTakenImagesCount(Context context, int i) {
        SharedPreferences.Editor edit = context.getSharedPreferences("TakenImagesCount", 0).edit();
        edit.putInt("TakenImagesCount", i);
        edit.apply();
    }

    public static int getTakenImagesCount(Context context) {
        return context.getSharedPreferences("TakenImagesCount", 0).getInt("TakenImagesCount", 0);
    }

    public static void setRotateFileName(Context context, String str) {
        SharedPreferences.Editor edit = context.getSharedPreferences("RotateFileName", 0).edit();
        edit.putString("RotateFileName", str);
        edit.apply();
    }

    public static String getRotateFileName(Context context) {
        return context.getSharedPreferences("RotateFileName", 0).getString("RotateFileName", (String) null);
    }

    public static Bitmap getBitmapFromAsset(String str, Context context) {
        InputStream inputStream;
        try {
            inputStream = context.getAssets().open(str);
        } catch (IOException e) {
            e.printStackTrace();
            inputStream = null;
        }
        return BitmapFactory.decodeStream(inputStream);
    }

    public static Bitmap getImageBitmap(Class<Camera_Scanner_EdgeDetectionActivity> edgeDetectionActivity, ImageView imageView) {
        imageView.invalidate();
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public static boolean isFolderCreated() {
        File file = new File(Environment.getExternalStorageDirectory(), "Document Scanner");
        if (!file.exists() && !file.mkdirs()) {
            Log.d("App", "failed to create directory");
        }
        File file2 = new File(Environment.getExternalStorageDirectory(), "Document Scanner/Book");
        if (!file2.exists() && !file2.mkdirs()) {
            Log.d("App", "failed to create directory");
        }
        File file3 = new File(Environment.getExternalStorageDirectory(), "Document Scanner/Document");
        if (!file3.exists() && !file3.mkdirs()) {
            Log.d("App", "failed to create directory");
        }
        File file4 = new File(Environment.getExternalStorageDirectory(), "Document Scanner/ID Card");
        if (!file4.exists() && !file4.mkdirs()) {
            Log.d("App", "failed to create directory");
        }
        File file5 = new File(Environment.getExternalStorageDirectory(), "Document Scanner/Edge Detect");
        if (!file5.exists() && !file5.mkdirs()) {
            Log.d("App", "failed to create directory");
        }
        File file6 = new File(Environment.getExternalStorageDirectory(), "Document Scanner/Business Card");
        if (!file6.exists() && !file6.mkdirs()) {
            Log.d("App", "failed to create directory");
        }
        File file7 = new File(Environment.getExternalStorageDirectory(), "Document Scanner/Photo ID");
        if (!file7.exists() && !file7.mkdirs()) {
            Log.d("App", "failed to create directory");
        }
        File file8 = new File(Environment.getExternalStorageDirectory(), "Document Scanner/OCR Files");
        if (file8.exists() || file8.mkdirs()) {
            return true;
        }
        Log.d("App", "failed to create directory");
        return true;
    }

    public static String getStorageSize(long j) {
        if (j <= 0) {
            return "0";
        }
        double d = (double) j;
        int log10 = (int) (Math.log10(d) / Math.log10(1024.0d));
        return new DecimalFormat("#,##0.#").format(d / Math.pow(1024.0d, (double) log10)) + " " + new String[]{"B", "KB", "MB", "GB", "TB"}[log10];
    }

    public static String getPath(Context context, Uri uri) {
        Uri uri2 = null;
        if (!(Build.VERSION.SDK_INT >= 19) || !DocumentsContract.isDocumentUri(context, uri)) {
            if (Annotation.CONTENT.equalsIgnoreCase(uri.getScheme())) {
                return getDataColumn(context, uri, (String) null, (String[]) null);
            }
            if (Annotation.FILE.equalsIgnoreCase(uri.getScheme())) {
                return uri.getPath();
            }
        } else if (isExternalStorageDocument(uri)) {
            String[] split = DocumentsContract.getDocumentId(uri).split(":");
            if ("primary".equalsIgnoreCase(split[0])) {
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
        } else if (isDownloadsDocument(uri)) {
            return getDataColumn(context, ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(DocumentsContract.getDocumentId(uri)).longValue()), (String) null, (String[]) null);
        } else if (isMediaDocument(uri)) {
            String[] split2 = DocumentsContract.getDocumentId(uri).split(":");
            String str = split2[0];
            if ("image".equals(str)) {
                uri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            } else if ("video".equals(str)) {
                uri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            } else if ("audio".equals(str)) {
                uri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            }
            return getDataColumn(context, uri2, "_id=?", new String[]{split2[1]});
        }
        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {

                    DatabaseUtils.dumpCursor(cursor);

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;}

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
