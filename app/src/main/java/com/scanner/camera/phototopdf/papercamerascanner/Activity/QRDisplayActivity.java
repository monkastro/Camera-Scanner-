package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.common.Privacy_Policy_activity;

import java.io.File;
import java.io.FileOutputStream;

public class QRDisplayActivity extends AppCompatActivity {
    LinearLayout btnSaveImage;
    LinearLayout btnSavePdf;
    LinearLayout btnShare;
    String imagePath = null;
    ImageView ivQRImage;
    Toolbar toolbar;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_q_r_display);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolBar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.ivQRImage = (ImageView) findViewById(R.id.ivQRImage);
        this.btnShare = (LinearLayout) findViewById(R.id.btnShare);
        this.btnSaveImage = (LinearLayout) findViewById(R.id.btnSaveImage);
        this.btnSavePdf = (LinearLayout) findViewById(R.id.btnSavePdf);
        Intent intent = getIntent();
        if (intent != null) {
            String stringExtra = intent.getStringExtra("imagePath");
            this.imagePath = stringExtra;
            this.ivQRImage.setImageURI(Uri.parse(stringExtra));
        }
        this.btnShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("image/*");
                intent.putExtra("android.intent.extra.STREAM", Uri.parse(QRDisplayActivity.this.imagePath));
                QRDisplayActivity.this.startActivity(Intent.createChooser(intent, "Share Image"));
            }
        });
        this.btnSaveImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Toast makeText = Toast.makeText(QRDisplayActivity.this.getApplicationContext(), "QR Code saved.", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }
        });
        this.btnSavePdf.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String file = Environment.getExternalStorageDirectory().toString();
                File file2 = new File(file + "/Document Scanner");
                file2.mkdirs();
                File file3 = new File(QRDisplayActivity.this.imagePath);
                try {
                    File file4 = new File(file2, "QRScan_" + Long.toString(System.currentTimeMillis()) + ".pdf");
                    Document document = new Document();
                    PdfWriter.getInstance(document, new FileOutputStream(file4));
                    document.open();
                    Image instance = Image.getInstance(file3.getPath());
                    instance.scalePercent(((((document.getPageSize().getWidth() - document.leftMargin()) - document.rightMargin()) - 0.0f) / instance.getWidth()) * 100.0f);
                    instance.setAlignment(5);
                    document.add(instance);
                    document.close();
                    Intent intent = new Intent(QRDisplayActivity.this, Camera_Scanner_PDFViewerActivity.class);
                    intent.putExtra("pdfFilePath", file4.getPath());
                    intent.addFlags(268435456);
                    QRDisplayActivity.this.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
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
