package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.common.Privacy_Policy_activity;

import java.io.File;
import java.io.FileOutputStream;

public class DisplayScanResultActivity extends AppCompatActivity {
    Bitmap bitmap;
    LinearLayout btnSaveImage;
    LinearLayout btnSavePdf;
    LinearLayout btnShare;
    Dialog dialogLoading;
    String imagePath;
    boolean isSavePdf = false;
    boolean isSaved = false;
    boolean isShare = false;
    ImageView ivScanResult;
    File myDir;
    String root = "";
    String scanType = "QRCode";
    Toolbar toolbar;
    TextView tvScanText;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_display_scan_result);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolBar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.root = Environment.getExternalStorageDirectory().toString();
        File file = new File(this.root + "/Document Scanner");
        this.myDir = file;
        file.mkdirs();
        this.tvScanText = (TextView) findViewById(R.id.tvScanText);
        this.ivScanResult = (ImageView) findViewById(R.id.ivScanResult);
        this.btnShare = (LinearLayout) findViewById(R.id.btnShare);
        this.btnSaveImage = (LinearLayout) findViewById(R.id.btnSaveImage);
        this.btnSavePdf = (LinearLayout) findViewById(R.id.btnSavePdf);
        Intent intent = getIntent();
        if (intent != null) {
            String stringExtra = intent.getStringExtra("scanText");
            generateBarCode(stringExtra);
            TextView textView = this.tvScanText;
            textView.setText("" + stringExtra);
        }
        this.btnSaveImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new saveImage().execute(new Void[0]);
            }
        });
        this.btnSavePdf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DisplayScanResultActivity.this.isSavePdf = true;
                new saveImage().execute(new Void[0]);
            }
        });
        this.btnShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DisplayScanResultActivity.this.isShare = true;
                if (!DisplayScanResultActivity.this.isSaved || DisplayScanResultActivity.this.imagePath == null) {
                    new saveImage().execute(new Void[0]);
                    return;
                }
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.STREAM", Uri.parse(DisplayScanResultActivity.this.imagePath));
                DisplayScanResultActivity.this.startActivity(Intent.createChooser(intent, "Share Image"));
            }
        });
    }

    private class saveImage extends AsyncTask<Void, Void, Void> {
        File file;

        private saveImage() {
        }


        public void onPreExecute() {
            super.onPreExecute();
            DisplayScanResultActivity.this.showLoading();
        }


        public Void doInBackground(Void... voidArr) {
            File file2 = new File(DisplayScanResultActivity.this.myDir, "DocScan_"+ Long.toString(System.currentTimeMillis())  + ".png");
            this.file = file2;
            if (file2.exists()) {
                this.file.delete();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(this.file);
                DisplayScanResultActivity.this.bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Void voidR) {
            DisplayScanResultActivity.this.imagePath = this.file.getPath();
            if (DisplayScanResultActivity.this.isSavePdf) {
                new savePDF().execute(new Void[0]);
            } else if (DisplayScanResultActivity.this.isShare) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.STREAM", Uri.parse(DisplayScanResultActivity.this.imagePath));
                DisplayScanResultActivity.this.startActivity(Intent.createChooser(intent, "Share Image"));
                DisplayScanResultActivity.this.isShare = false;
                DisplayScanResultActivity.this.dialogLoading.dismiss();
            } else {
                DisplayScanResultActivity.this.isSaved = true;
                DisplayScanResultActivity.this.dialogLoading.dismiss();
                Toast makeText = Toast.makeText(DisplayScanResultActivity.this.getApplicationContext(), "QR Code saved.", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }
        }
    }

    private class savePDF extends AsyncTask<Void, Void, Void> {
        File file2;

        private savePDF() {
        }


        public void onPreExecute() {
            super.onPreExecute();
        }


        public Void doInBackground(Void... voidArr) {
            try {
                this.file2 = new File(DisplayScanResultActivity.this.myDir, "QRScan_"+ Long.toString(System.currentTimeMillis()) + ".pdf");
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(this.file2));
                document.open();
                Image instance = Image.getInstance(DisplayScanResultActivity.this.imagePath);
                instance.scalePercent(((((document.getPageSize().getWidth() - document.leftMargin()) - document.rightMargin()) - 0.0f) / instance.getWidth()) * 100.0f);
                instance.setAlignment(5);
                document.add(instance);
                document.close();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Void voidR) {
            File file = new File(DisplayScanResultActivity.this.imagePath);
            if (file.exists()) {
                file.delete();
            }
            DisplayScanResultActivity.this.isSavePdf = false;
            DisplayScanResultActivity.this.dialogLoading.dismiss();
            Intent intent = new Intent(DisplayScanResultActivity.this, Camera_Scanner_PDFViewerActivity.class);
            intent.putExtra("pdfFilePath", this.file2.getPath());
            intent.addFlags(268435456);
            DisplayScanResultActivity.this.startActivity(intent);
        }
    }

    /* access modifiers changed from: private */
    public void showLoading() {
        Dialog dialog = new Dialog(this);
        this.dialogLoading = dialog;
        dialog.setContentView(R.layout.loading_dialog);
        this.dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialogLoading.setCancelable(false);
        this.dialogLoading.show();
    }

    public void generateBarCode(String str) {
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BarcodeFormat barcodeFormat = "Barcode".equals(this.scanType) ? BarcodeFormat.CODE_128 : BarcodeFormat.QR_CODE;
            int i = 150;
            int i2 = "Barcode".equals(this.scanType) ? ItemTouchHelper.Callback.DEFAULT_SWIPE_ANIMATION_DURATION : 150;
            if ("Barcode".equals(this.scanType)) {
                i = 200;
            }
            Bitmap createBitmap = new BarcodeEncoder().createBitmap(multiFormatWriter.encode(str, barcodeFormat, i2, i));
            this.bitmap = createBitmap;
            this.ivScanResult.setImageBitmap(createBitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
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
