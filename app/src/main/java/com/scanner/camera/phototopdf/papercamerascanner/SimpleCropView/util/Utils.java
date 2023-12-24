package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.opengl.GLES10;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.itextpdf.text.Annotation;
import com.itextpdf.text.pdf.codec.TIFFConstants;
import com.itextpdf.text.pdf.codec.wmf.MetaDo;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Utils {
    private static final int SIZE_DEFAULT = 2048;
    private static final int SIZE_LIMIT = 4096;
    private static final String TAG = Utils.class.getSimpleName();
    public static int sInputImageHeight = 0;
    public static int sInputImageWidth = 0;

    public static int getRotateDegreeFromOrientation(int i) {
        if (i == 3) {
            return 180;
        }
        if (i == 6) {
            return 90;
        }
        if (i != 8) {
            return 0;
        }
        return TIFFConstants.TIFFTAG_IMAGEDESCRIPTION;
    }

    public static void copyExifInfo(Context context, Uri uri, Uri uri2, int i, int i2) throws Throwable {
        if (uri != null && uri2 != null) {
            try {
                File fileFromUri = getFileFromUri(context, uri);
                File fileFromUri2 = getFileFromUri(context, uri2);
                if (fileFromUri == null) {
                    return;
                }
                if (fileFromUri2 != null) {
                    String absolutePath = fileFromUri.getAbsolutePath();
                    String absolutePath2 = fileFromUri2.getAbsolutePath();
                    ExifInterface exifInterface = new ExifInterface(absolutePath);
                    ArrayList<String> arrayList = new ArrayList<>();
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_DATETIME);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_FLASH);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_FOCAL_LENGTH);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_GPS_ALTITUDE);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_GPS_ALTITUDE_REF);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_GPS_DATESTAMP);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_GPS_LATITUDE);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_GPS_LATITUDE_REF);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_GPS_LONGITUDE);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_GPS_LONGITUDE_REF);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_GPS_PROCESSING_METHOD);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_GPS_TIMESTAMP);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_MAKE);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_MODEL);
                    arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_WHITE_BALANCE);
                    if (Build.VERSION.SDK_INT >= 11) {
                        arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_EXPOSURE_TIME);
                        arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_F_NUMBER);
                        arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_ISO_SPEED_RATINGS);
                    }
                    if (Build.VERSION.SDK_INT >= 23) {
                        arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_DATETIME_DIGITIZED);
                        arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_SUBSEC_TIME);
                        arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_SUBSEC_TIME_DIGITIZED);
                        arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_SUBSEC_TIME_ORIGINAL);
                    }
                    if (Build.VERSION.SDK_INT >= 24) {
                        arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_F_NUMBER);
                        arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_ISO_SPEED_RATINGS);
                        arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_SUBSEC_TIME_DIGITIZED);
                        arrayList.add(androidx.exifinterface.media.ExifInterface.TAG_SUBSEC_TIME_ORIGINAL);
                    }
                    ExifInterface exifInterface2 = new ExifInterface(absolutePath2);
                    for (String str : arrayList) {
                        String attribute = exifInterface.getAttribute(str);
                        if (!TextUtils.isEmpty(attribute)) {
                            exifInterface2.setAttribute(str, attribute);
                        }
                    }
                    exifInterface2.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_IMAGE_WIDTH, String.valueOf(i));
                    exifInterface2.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_IMAGE_LENGTH, String.valueOf(i2));
                    exifInterface2.setAttribute(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, String.valueOf(0));
                    exifInterface2.saveAttributes();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getExifRotation(File file) {
        if (file == null) {
            return 0;
        }
        try {
            return getRotateDegreeFromOrientation(new ExifInterface(file.getAbsolutePath()).getAttributeInt(androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION, 0));
        } catch (IOException e) {
            Logger.m89e("An error occurred while getting the exif data: " + e.getMessage(), e);
            return 0;
        }
    }

    public static int getExifRotation(Context context, Uri uri) {
        Cursor cursor = null;
        try {
            Cursor query = context.getContentResolver().query(uri, new String[]{"orientation"}, (String) null, (String[]) null, (String) null);
            if (query != null) {
                if (query.moveToFirst()) {
                    int i = query.getInt(0);
                    if (query != null) {
                        query.close();
                    }
                    return i;
                }
            }
            if (query != null) {
                query.close();
            }
            return 0;
        } catch (RuntimeException unused) {
            if (cursor != null) {
                cursor.close();
            }
            return 0;
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    public static int getExifOrientation(Context context, Uri uri) throws Throwable {
        if (uri.getAuthority().toLowerCase().endsWith("media")) {
            return getExifRotation(context, uri);
        }
        return getExifRotation(getFileFromUri(context, uri));
    }

    public static Matrix getMatrixFromExifOrientation(int i) {
        Matrix matrix = new Matrix();
        switch (i) {
            case 2:
                matrix.postScale(-1.0f, 1.0f);
                break;
            case 3:
                matrix.postRotate(180.0f);
                break;
            case 4:
                matrix.postScale(1.0f, -1.0f);
                break;
            case 5:
                matrix.postRotate(90.0f);
                matrix.postScale(1.0f, -1.0f);
                break;
            case 6:
                matrix.postRotate(90.0f);
                break;
            case 7:
                matrix.postRotate(-90.0f);
                matrix.postScale(1.0f, -1.0f);
                break;
            case 8:
                matrix.postRotate(-90.0f);
                break;
        }
        return matrix;
    }

    public static int getExifOrientationFromAngle(int i) {
        int i2 = i % 360;
        if (i2 == 90) {
            return 6;
        }
        if (i2 != 180) {
            return i2 != 270 ? 1 : 8;
        }
        return 3;
    }

    public static Uri ensureUriPermission(Context context, Intent intent) {
        Uri data = intent.getData();
        if (Build.VERSION.SDK_INT >= 19) {
            context.getContentResolver().takePersistableUriPermission(data, intent.getFlags() & 1);
        }
        return data;
    }

    /**
     * Get image file from uri
     *
     * @param context The context
     * @param uri The Uri of the image
     * @return Image file
     */
    @TargetApi(Build.VERSION_CODES.KITKAT) public static File getFileFromUri(final Context context,
                                                                             final Uri uri) throws Throwable {
        String filePath = null;
        final boolean isKitkat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitkat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                // String "id" may not represent a valid Long type data, it may equals to
                // something like "raw:/storage/emulated/0/Download/some_file" instead.
                // Doing a check before passing the "id" to Long.valueOf(String) would be much safer.
                if (RawDocumentsHelper.isRawDocId(id)) {
                    filePath = RawDocumentsHelper.getAbsoluteFilePath(id);
                } else {
                    final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    filePath = getDataColumn(context, contentUri, null, null);
                }
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };
                filePath = getDataColumn(context, contentUri, selection, selectionArgs);
            } else if (isGoogleDriveDocument(uri)) {
                return getGoogleDriveFile(context, uri);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            if (isGooglePhotosUri(uri)) {
                filePath = uri.getLastPathSegment();
            } else {
                filePath = getDataColumn(context, uri, null, null);
            }
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (filePath != null) {
            return new File(filePath);
        }
        return null;
    }


    public static class RawDocumentsHelper {
        public static final String RAW_PREFIX = "raw:";

        public static boolean isRawDocId(String str) {
            return str != null && str.startsWith(RAW_PREFIX);
        }

        public static String getDocIdForFile(File file) {
            return RAW_PREFIX + file.getAbsolutePath();
        }

        public static String getAbsoluteFilePath(String str) {
            return str.substring(4);
        }
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {
        Cursor cursor = null;
        final String[] projection = {
                MediaStore.MediaColumns.DATA, MediaStore.MediaColumns.DISPLAY_NAME
        };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int columnIndex =
                        (uri.toString().startsWith("content://com.google.android.gallery3d"))
                                ? cursor.getColumnIndex(MediaStore.MediaColumns.DISPLAY_NAME)
                                : cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
                if (columnIndex != -1) {
                    return cursor.getString(columnIndex);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static boolean isGoogleDriveDocument(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority());
    }

    private static File getGoogleDriveFile(Context context, Uri uri) throws Throwable {
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream;
        Throwable th;
        if (uri == null) {
            return null;
        }
        String absolutePath = new File(context.getCacheDir(), "tmp").getAbsolutePath();
        try {
            ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
            if (openFileDescriptor == null) {
                closeQuietly((Closeable) null);
                closeQuietly((Closeable) null);
                return null;
            }
            fileInputStream = new FileInputStream(openFileDescriptor.getFileDescriptor());
            try {
                fileOutputStream = new FileOutputStream(absolutePath);
            } catch (IOException unused) {
                fileOutputStream = null;
                closeQuietly(fileInputStream);
                closeQuietly(fileOutputStream);
                return null;
            } catch (Throwable th2) {
                th = th2;
                fileOutputStream = null;
                closeQuietly(fileInputStream);
                closeQuietly(fileOutputStream);
                throw th;
            }
            try {
                byte[] bArr = new byte[4096];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read != -1) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        File file = new File(absolutePath);
                        closeQuietly(fileInputStream);
                        closeQuietly(fileOutputStream);
                        return file;
                    }
                }
            } catch (IOException unused2) {
                closeQuietly(fileInputStream);
                closeQuietly(fileOutputStream);
                return null;
            } catch (Throwable th3) {
                th = th3;
                closeQuietly(fileInputStream);
                closeQuietly(fileOutputStream);
                throw th;
            }
        } catch (IOException unused3) {
            fileOutputStream = null;
            fileInputStream = null;
            closeQuietly(fileInputStream);
            closeQuietly(fileOutputStream);
            return null;
        } catch (Throwable th4) {
            fileInputStream = null;
            th = th4;
            fileOutputStream = null;
            closeQuietly(fileInputStream);
            closeQuietly(fileOutputStream);
            throw th;
        }
    }


    public static Bitmap decodeSampledBitmapFromUri(Context context, Uri sourceUri, int requestSize) {
        InputStream stream = null;
        Bitmap bitmap = null;
        try {
            stream = context.getContentResolver().openInputStream(sourceUri);
            if (stream != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = Utils.calculateInSampleSize(context, sourceUri, requestSize);
                options.inJustDecodeBounds = false;
                bitmap = BitmapFactory.decodeStream(stream, null, options);
            }
        } catch (java.io.FileNotFoundException e) {
            //Logger.e(e.getMessage());
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                //Logger.e(e.getMessage());
            }
        }
        return bitmap;
    }
    public static int calculateInSampleSize(Context context, Uri sourceUri, int requestSize) {
        InputStream is = null;
        // check image size
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try {
            is = context.getContentResolver().openInputStream(sourceUri);
            BitmapFactory.decodeStream(is, null, options);
        } catch (java.io.FileNotFoundException ignored) {
        } finally {
            closeQuietly(is);
        }
        int inSampleSize = 1;
        sInputImageWidth = options.outWidth;
        sInputImageHeight = options.outHeight;
        while (options.outWidth / inSampleSize > requestSize
                || options.outHeight / inSampleSize > requestSize) {
            inSampleSize *= 2;
        }
        return inSampleSize;
    }

    public static Bitmap getScaledBitmapForHeight(Bitmap bitmap, int i) {
        return getScaledBitmap(bitmap, Math.round(((float) i) * (((float) bitmap.getWidth()) / ((float) bitmap.getHeight()))), i);
    }

    public static Bitmap getScaledBitmapForWidth(Bitmap bitmap, int i) {
        return getScaledBitmap(bitmap, i, Math.round(((float) i) / (((float) bitmap.getWidth()) / ((float) bitmap.getHeight()))));
    }

    public static Bitmap getScaledBitmap(Bitmap bitmap, int i, int i2) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) i) / ((float) width), ((float) i2) / ((float) height));
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static int getMaxSize() {
        int[] iArr = new int[1];
        GLES10.glGetIntegerv(MetaDo.META_SETDIBTODEV, iArr, 0);
        if (iArr[0] > 0) {
            return Math.min(iArr[0], 4096);
        }
        return 2048;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static void updateGalleryInfo(Context context, Uri uri) throws Throwable {
        if (Annotation.CONTENT.equals(uri.getScheme())) {
            ContentValues contentValues = new ContentValues();
            File fileFromUri = getFileFromUri(context, uri);
            if (fileFromUri != null && fileFromUri.exists()) {
                contentValues.put("_size", Long.valueOf(fileFromUri.length()));
            }
            context.getContentResolver().update(uri, contentValues, (String) null, (String[]) null);
        }
    }
}
