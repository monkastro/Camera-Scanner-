package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.scanner.camera.phototopdf.papercamerascanner.CustomView.CanvasView;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.IConstant;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.common.Privacy_Policy_activity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class MagicalEraserActivity extends AppCompatActivity {
    float brushSizeValue = 15.0f;
    LinearLayout btnESize;
    LinearLayout btnMagicEraser;
    LinearLayout btnUndo;
    CanvasView canvasViewErase;
    Dialog dialogLoading;
    ImageView ivCancelMEraser;
    ImageView ivCancelSize;
    ImageView ivMEraser;
    ImageView ivOkMEraser;
    ImageView ivOkSize;
    ImageView ivUndo;
    LinearLayout llBrushSize;
    int oldBrush = 0;
    SeekBar sbBrushSize;
    Toolbar toolbar;
    TextView tvMEraser;
    TextView tvUndo;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_magical_eraser);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.canvasViewErase = (CanvasView) findViewById(R.id.canvasViewErase);
        this.sbBrushSize = (SeekBar) findViewById(R.id.sbBrushSize);
        this.ivCancelMEraser = (ImageView) findViewById(R.id.ivCancelMEraser);
        this.ivOkMEraser = (ImageView) findViewById(R.id.ivOkMEraser);
        this.ivCancelSize = (ImageView) findViewById(R.id.ivCancelSize);
        this.ivOkSize = (ImageView) findViewById(R.id.ivOkSize);
        this.ivUndo = (ImageView) findViewById(R.id.ivUndo);
        this.ivMEraser = (ImageView) findViewById(R.id.ivMEraser);
        this.tvUndo = (TextView) findViewById(R.id.tvUndo);
        this.tvMEraser = (TextView) findViewById(R.id.tvMEraser);
        this.llBrushSize = (LinearLayout) findViewById(R.id.llBrushSize);
        this.btnMagicEraser = (LinearLayout) findViewById(R.id.btnMagicEraser);
        this.btnUndo = (LinearLayout) findViewById(R.id.btnUndo);
        this.btnESize = (LinearLayout) findViewById(R.id.btnESize);
        Intent intent = getIntent();
        if (intent != null) {
            String stringExtra = intent.getStringExtra("processMagic");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(new File(stringExtra)), (Rect) null, options);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            this.canvasViewErase.drawBitmap(bitmap);
            this.canvasViewErase.setMode(CanvasView.Mode.DRAW);
            this.canvasViewErase.setDrawer(CanvasView.Drawer.PEN);
            this.canvasViewErase.setPaintStrokeColor(-1);
            this.canvasViewErase.setPaintStrokeWidth(this.brushSizeValue);
            manageSelectedIcon(R.id.btnMagicEraser);
        }
        this.btnMagicEraser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MagicalEraserActivity.this.canvasViewErase.setMode(CanvasView.Mode.DRAW);
                MagicalEraserActivity.this.canvasViewErase.setDrawer(CanvasView.Drawer.PEN);
                MagicalEraserActivity.this.canvasViewErase.setPaintStrokeWidth(MagicalEraserActivity.this.brushSizeValue);
                MagicalEraserActivity.this.manageSelectedIcon(R.id.btnMagicEraser);
            }
        });
        this.ivOkMEraser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new saveImage().execute(new Void[0]);
            }
        });
        this.ivCancelMEraser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MagicalEraserActivity.this.onBackPressed();
            }
        });
        this.btnUndo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MagicalEraserActivity.this.canvasViewErase.setMode(CanvasView.Mode.STOP_DRAWING);
                MagicalEraserActivity.this.canvasViewErase.undo();
                MagicalEraserActivity.this.manageSelectedIcon(R.id.btnUndo);
            }
        });
        this.btnESize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MagicalEraserActivity.this.llBrushSize.setVisibility(0);
                MagicalEraserActivity magicalEraserActivity = MagicalEraserActivity.this;
                magicalEraserActivity.oldBrush = (int) magicalEraserActivity.brushSizeValue;
            }
        });
        this.ivCancelSize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MagicalEraserActivity.this.llBrushSize.setVisibility(8);
                MagicalEraserActivity magicalEraserActivity = MagicalEraserActivity.this;
                magicalEraserActivity.brushSizeValue = (float) magicalEraserActivity.oldBrush;
                MagicalEraserActivity.this.canvasViewErase.setPaintStrokeWidth(MagicalEraserActivity.this.brushSizeValue);
                MagicalEraserActivity.this.manageSelectedIcon(R.id.btnMagicEraser);
            }
        });
        this.ivOkSize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                MagicalEraserActivity.this.llBrushSize.setVisibility(8);
                MagicalEraserActivity.this.manageSelectedIcon(R.id.btnMagicEraser);
            }
        });
        this.sbBrushSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                MagicalEraserActivity.this.brushSizeValue = (float) i;
                MagicalEraserActivity.this.canvasViewErase.setPaintStrokeWidth(MagicalEraserActivity.this.brushSizeValue);
            }
        });
        this.canvasViewErase.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Bitmap bitmap = MagicalEraserActivity.this.canvasViewErase.getBitmap();
                Matrix matrix = new Matrix();
                ((CanvasView) view).getMatrix().invert(matrix);
                float[] fArr = {motionEvent.getX(), motionEvent.getY()};
                matrix.mapPoints(fArr);
                int pixel = bitmap.getPixel((int) fArr[0], (int) fArr[1]);
                MagicalEraserActivity.this.canvasViewErase.setPaintStrokeColor(Color.argb(Color.alpha(pixel), Color.red(pixel), Color.green(pixel), Color.blue(pixel)));
                return false;
            }
        });
    }


    public void manageSelectedIcon(int i) {
        if (i == R.id.btnUndo) {
            this.ivUndo.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.selectedButtonColor));
            this.tvUndo.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.ivMEraser.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvMEraser.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.btnMagicEraser) {
            this.ivUndo.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvUndo.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivMEraser.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.selectedButtonColor));
            this.tvMEraser.setTextColor(getResources().getColor(R.color.selectedButtonColor));
        }
    }

    private class saveImage extends AsyncTask<Void, Void, Void> {
        File file;

        private saveImage() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            MagicalEraserActivity.this.showLoading();
        }


        public Void doInBackground(Void... voidArr) {
            File file2 = new File(MainUtils.getRotateFileName(MagicalEraserActivity.this));
            this.file = file2;
            if (file2.exists()) {
                this.file.delete();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(this.file);
                MagicalEraserActivity.this.canvasViewErase.getBitmap().compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Void voidR) {
            MagicalEraserActivity.this.dialogLoading.dismiss();
            MagicalEraserActivity.this.setResult(IConstant.RESULT_MAGIC, new Intent());
            MagicalEraserActivity.this.finish();
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

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        super.onBackPressed();
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
