package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.scanlibrary.PolygonView;
import com.scanlibrary.ScanActivity;

import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.IConstant;

import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Camera_Scanner_EdgeDetectionActivity extends AppCompatActivity {
    LinearLayout btnOk;
    LinearLayout btnReset;
    LinearLayout btnRetake;
    LinearLayout btnRotate;
    float degreeAngle = 90.0f;
    Dialog dialogLoading;
    ImageView ivBack;
    ImageView ivRetake;

    public Bitmap original;

    public PolygonView polygonView;
    String selectedImageUrl = null;
    private FrameLayout sourceFrame;
    ImageView sourceImageView;
    TextView tvRetake;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_edge_detection);
        this.ivBack = (ImageView) findViewById(R.id.ivBack);
        this.sourceImageView = (ImageView) findViewById(R.id.ivEdgeDetect);
        this.btnReset = (LinearLayout) findViewById(R.id.btnReset);
        this.btnOk = (LinearLayout) findViewById(R.id.btnOk);
        this.btnRotate = (LinearLayout) findViewById(R.id.btnRotate);
        this.btnRetake = (LinearLayout) findViewById(R.id.btnRetake);
        this.tvRetake = (TextView) findViewById(R.id.tvRetake);
        this.ivRetake = (ImageView) findViewById(R.id.ivRetake);
        this.sourceFrame = (FrameLayout) findViewById(R.id.sourceFrame);
        this.polygonView = (PolygonView) findViewById(R.id.polygonView);
        if (MainUtils.getFromGallaryEdge(this)) {
            this.tvRetake.setText("Cancel");
            this.ivRetake.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_menu_cancel_icon));
        }
        this.sourceFrame.post(new Runnable() {
            public void run() {
                Camera_Scanner_EdgeDetectionActivity cameraScannerEdgeDetectionActivity = Camera_Scanner_EdgeDetectionActivity.this;
                Bitmap unused = cameraScannerEdgeDetectionActivity.original = cameraScannerEdgeDetectionActivity.getBitmap();
                if (Camera_Scanner_EdgeDetectionActivity.this.original != null) {
                    Camera_Scanner_EdgeDetectionActivity cameraScannerEdgeDetectionActivity2 = Camera_Scanner_EdgeDetectionActivity.this;
                    cameraScannerEdgeDetectionActivity2.setBitmap(cameraScannerEdgeDetectionActivity2.original);
                }
            }
        });
        this.ivBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Camera_Scanner_EdgeDetectionActivity.this.onBackPressed();
            }
        });
        this.btnOk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Map<Integer, PointF> points = Camera_Scanner_EdgeDetectionActivity.this.polygonView.getPoints();
                if (Camera_Scanner_EdgeDetectionActivity.this.isScanPointsValid(points)) {
                    new ScanAsyncTask(points).execute(new Void[0]);
                    return;
                }
                Toast makeText = Toast.makeText(Camera_Scanner_EdgeDetectionActivity.this, "Scan Points Invalid", 0);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }
        });
        this.btnRetake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (MainUtils.getFromGallaryEdge(Camera_Scanner_EdgeDetectionActivity.this)) {
                    Camera_Scanner_EdgeDetectionActivity.this.setResult(IConstant.RESULT_GALLARY_EDGE_CANCEL, new Intent());
                    Camera_Scanner_EdgeDetectionActivity.this.finish();
                    return;
                }
                File file = new File(Camera_Scanner_EdgeDetectionActivity.this.selectedImageUrl);
                if (file.exists()) {
                    file.delete();
                }
                MainUtils.setTakenImagesCount(Camera_Scanner_EdgeDetectionActivity.this, MainUtils.getTakenImagesCount(Camera_Scanner_EdgeDetectionActivity.this) - 1);
                Camera_Scanner_EdgeDetectionActivity.this.finish();
            }
        });
        this.btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Camera_Scanner_EdgeDetectionActivity.this.setBitmap(MainUtils.getImageBitmap(Camera_Scanner_EdgeDetectionActivity.class, Camera_Scanner_EdgeDetectionActivity.this.sourceImageView));
            }
        });
        this.btnRotate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Bitmap bitmap;
                Bitmap imageBitmap ;
                imageBitmap = MainUtils.getImageBitmap(Camera_Scanner_EdgeDetectionActivity.class,sourceImageView);
                 imageBitmap = MainUtils.getImageBitmap(Camera_Scanner_EdgeDetectionActivity.class, Camera_Scanner_EdgeDetectionActivity.this.sourceImageView);
                if (Camera_Scanner_EdgeDetectionActivity.this.degreeAngle == 90.0f) {
                    Camera_Scanner_EdgeDetectionActivity cameraScannerEdgeDetectionActivity = Camera_Scanner_EdgeDetectionActivity.this;
                    bitmap = cameraScannerEdgeDetectionActivity.rotateBitmap(imageBitmap, cameraScannerEdgeDetectionActivity.degreeAngle);
                    Camera_Scanner_EdgeDetectionActivity.this.degreeAngle = 180.0f;
                } else if (Camera_Scanner_EdgeDetectionActivity.this.degreeAngle == 180.0f) {
                    Camera_Scanner_EdgeDetectionActivity cameraScannerEdgeDetectionActivity2 = Camera_Scanner_EdgeDetectionActivity.this;
                    bitmap = cameraScannerEdgeDetectionActivity2.rotateBitmap(imageBitmap, cameraScannerEdgeDetectionActivity2.degreeAngle);
                    Camera_Scanner_EdgeDetectionActivity.this.degreeAngle = 270.0f;
                } else if (Camera_Scanner_EdgeDetectionActivity.this.degreeAngle == 270.0f) {
                    Camera_Scanner_EdgeDetectionActivity cameraScannerEdgeDetectionActivity3 = Camera_Scanner_EdgeDetectionActivity.this;
                    bitmap = cameraScannerEdgeDetectionActivity3.rotateBitmap(imageBitmap, cameraScannerEdgeDetectionActivity3.degreeAngle);
                    Camera_Scanner_EdgeDetectionActivity.this.degreeAngle = 360.0f;
                } else if (Camera_Scanner_EdgeDetectionActivity.this.degreeAngle == 360.0f) {
                    Camera_Scanner_EdgeDetectionActivity cameraScannerEdgeDetectionActivity4 = Camera_Scanner_EdgeDetectionActivity.this;
                    bitmap = cameraScannerEdgeDetectionActivity4.rotateBitmap(imageBitmap, cameraScannerEdgeDetectionActivity4.degreeAngle);
                    Camera_Scanner_EdgeDetectionActivity.this.degreeAngle = 90.0f;
                } else {
                    bitmap = null;
                }
                Camera_Scanner_EdgeDetectionActivity.this.sourceImageView.setImageBitmap(bitmap);
                Bitmap unused = Camera_Scanner_EdgeDetectionActivity.this.original = bitmap;
            }
        });
    }

    public Bitmap rotateBitmap(Bitmap bitmap, float f) {
        Matrix matrix = new Matrix();
        matrix.postRotate(f);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public boolean isScanPointsValid(Map<Integer, PointF> map) {
        return map.size() == 4;
    }

    public Bitmap getBitmap() {
        Intent intent = getIntent();
        if (intent != null) {
            this.selectedImageUrl = intent.getStringExtra("imageUrl");
            Log.e("HHHH", "Image Url : " + this.selectedImageUrl);
        }
        try {
            File file = new File(this.selectedImageUrl);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            return BitmapFactory.decodeStream(new FileInputStream(file), (Rect) null, options);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setBitmap(Bitmap bitmap) {
        this.sourceImageView.setImageBitmap(scaledBitmap(bitmap, this.sourceFrame.getWidth(), this.sourceFrame.getHeight()));
        Bitmap bitmap2 = ((BitmapDrawable) this.sourceImageView.getDrawable()).getBitmap();
        this.polygonView.setPoints(getEdgePoints(bitmap2));
        this.polygonView.setVisibility(0);
        int dimension = ((int) getResources().getDimension(R.dimen.scanPadding)) * 2;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(bitmap2.getWidth() + dimension, bitmap2.getHeight() + dimension);
        layoutParams.gravity = 17;
        this.polygonView.setLayoutParams(layoutParams);
    }

    private Map<Integer, PointF> getEdgePoints(Bitmap bitmap) {
        return orderedValidEdgePoints(bitmap, getContourEdgePoints(bitmap));
    }

    private List<PointF> getContourEdgePoints(Bitmap bitmap) {
        float[] points = ScanActivity.getPoints(bitmap);
        float f = points[0];
        float f2 = points[1];
        float f3 = points[2];
        float f4 = points[3];
        float f5 = points[4];
        float f6 = points[5];
        float f7 = points[6];
        float f8 = points[7];
        ArrayList arrayList = new ArrayList();
        arrayList.add(new PointF(f, f5));
        arrayList.add(new PointF(f2, f6));
        arrayList.add(new PointF(f3, f7));
        arrayList.add(new PointF(f4, f8));
        return arrayList;
    }

    private Map<Integer, PointF> getOutlinePoints(Bitmap bitmap) {
        HashMap hashMap = new HashMap();
        hashMap.put(0, new PointF(0.0f, 0.0f));
        hashMap.put(1, new PointF((float) bitmap.getWidth(), 0.0f));
        hashMap.put(2, new PointF(0.0f, (float) bitmap.getHeight()));
        hashMap.put(3, new PointF((float) bitmap.getWidth(), (float) bitmap.getHeight()));
        return hashMap;
    }

    private Map<Integer, PointF> orderedValidEdgePoints(Bitmap bitmap, List<PointF> list) {
        Map<Integer, PointF> orderedPoints = this.polygonView.getOrderedPoints(list);
        return !this.polygonView.isValidShape(orderedPoints) ? getOutlinePoints(bitmap) : orderedPoints;
    }

    private Bitmap scaledBitmap(Bitmap bitmap, int i, int i2) {
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0.0f, 0.0f, (float) bitmap.getWidth(), (float) bitmap.getHeight()), new RectF(0.0f, 0.0f, (float) i, (float) i2), Matrix.ScaleToFit.CENTER);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }


    public Bitmap getScannedBitmap(Bitmap bitmap, Map<Integer, PointF> map) {
        bitmap.getWidth();
        bitmap.getHeight();
        float width = ((float) bitmap.getWidth()) / ((float) this.sourceImageView.getWidth());
        float height = ((float) bitmap.getHeight()) / ((float) this.sourceImageView.getHeight());
        float f = map.get(0).x * width;
        float f2 = map.get(1).x * width;
        float f3 = map.get(2).x * width;
        float f4 = map.get(3).x * width;
        float f5 = map.get(0).y * height;
        float f6 = map.get(1).y * height;
        float f7 = map.get(2).y * height;
        float f8 = map.get(3).y * height;
        Log.d("", "POints(" + f + "," + f5 + ")(" + f2 + "," + f6 + ")(" + f3 + "," + f7 + ")(" + f4 + "," + f8 + ")");
        return ScanActivity.getScannedBitmap(bitmap, f, f5, f2, f6, f3, f7, f4, f8);
    }

    private class ScanAsyncTask extends AsyncTask<Void, Void, Void> {
        File file;
        private Map<Integer, PointF> points;

        public ScanAsyncTask(Map<Integer, PointF> map) {
            this.points = map;
        }


        public void onPreExecute() {
            super.onPreExecute();
            Camera_Scanner_EdgeDetectionActivity.this.showLoading();
        }


        public Void doInBackground(Void... voidArr) {
            Camera_Scanner_EdgeDetectionActivity cameraScannerEdgeDetectionActivity = Camera_Scanner_EdgeDetectionActivity.this;
            Bitmap access$500 = cameraScannerEdgeDetectionActivity.getScannedBitmap(cameraScannerEdgeDetectionActivity.original, this.points);
            MainUtils.isFolderCreated();
            File file2 = new File(Environment.getExternalStorageDirectory(), "Document Scanner/Edge Detect");
            if (!file2.exists()) {
                file2.mkdir();
            } else if (!file2.isDirectory()) {
                file2.mkdir();
            }
            String str = "Scan_" + Long.toString(System.currentTimeMillis());
            if (MainUtils.getFromEdit(Camera_Scanner_EdgeDetectionActivity.this)) {
                this.file = new File(Environment.getExternalStorageDirectory(), "Document Scanner/Edge Detect" + "/" + str + ".png");
            } else {
                this.file = new File(Camera_Scanner_EdgeDetectionActivity.this.selectedImageUrl);
            }
            if (this.file.exists()) {
                this.file.delete();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(this.file);
                access$500.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Void voidR) {
            super.onPostExecute(voidR);
            if (MainUtils.getFromEdit(Camera_Scanner_EdgeDetectionActivity.this)) {
                Camera_Scanner_EdgeDetectionActivity.this.finish();
                ArrayList arrayList = new ArrayList();
                arrayList.add(this.file.getPath());
                Intent intent = new Intent(Camera_Scanner_EdgeDetectionActivity.this, ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ImageList", arrayList);
                intent.putExtras(bundle);
                Camera_Scanner_EdgeDetectionActivity.this.startActivity(intent);
            } else if (MainUtils.getFromGallaryEdge(Camera_Scanner_EdgeDetectionActivity.this)) {
                Camera_Scanner_EdgeDetectionActivity.this.setResult(IConstant.RESULT_GALLARY_EDGE_OK, new Intent());
                Camera_Scanner_EdgeDetectionActivity.this.finish();
            } else {
                if (MainUtils.getScanType(Camera_Scanner_EdgeDetectionActivity.this).equals("IDCard")) {
                    MainUtils.setTakenIdCardSides(Camera_Scanner_EdgeDetectionActivity.this, MainUtils.getTakenIdCardSides(Camera_Scanner_EdgeDetectionActivity.this) + 1);
                }
                Camera_Scanner_EdgeDetectionActivity.this.finish();
            }
            Camera_Scanner_EdgeDetectionActivity.this.dialogLoading.dismiss();
        }
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
        finish();
    }
}
