package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.common.Privacy_Policy_activity;

import java.io.File;
import java.io.FileWriter;

public class OCRResultActivity extends AppCompatActivity {
    LinearLayout btnCopyOCR;
    LinearLayout btnSaveText;
    LinearLayout btnShareOCR;
    String folder_save_ocr = ("Ocr_Scan" + Long.toString(System.currentTimeMillis()));
    TextView scanText;
    Toolbar toolbar;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        setContentView((int) R.layout.activity_o_c_r_result);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.btnSaveText = (LinearLayout) findViewById(R.id.btnSaveText);
        this.btnShareOCR = (LinearLayout) findViewById(R.id.btnShareOCR);
        this.btnCopyOCR = (LinearLayout) findViewById(R.id.btnCopyOCR);
        this.scanText = (TextView) findViewById(R.id.scanText);
        Intent intent = getIntent();
        if (intent != null) {
            String stringExtra = intent.getStringExtra("textScanned");
            Log.e("HHHH", "Data  " + stringExtra);
            this.scanText.setText(stringExtra);
        }
        this.btnShareOCR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String charSequence = OCRResultActivity.this.scanText.getText().toString();
                Intent intent = new Intent("android.intent.action.SEND");
                intent.setType("text/plain");
                intent.putExtra("android.intent.extra.SUBJECT", "Subject Here");
                intent.putExtra("android.intent.extra.TEXT", charSequence);
                OCRResultActivity.this.startActivity(Intent.createChooser(intent, "Choose"));
            }
        });
        this.btnCopyOCR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                OCRResultActivity.checkCopyToClipboard(OCRResultActivity.this.getApplicationContext(), OCRResultActivity.this.scanText.getText().toString());
            }
        });
        this.btnSaveText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                final Dialog dialog = new Dialog(OCRResultActivity.this);
                dialog.setContentView(R.layout.dialog_save_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                final EditText editText = (EditText) dialog.findViewById(R.id.etFolderName);
                dialog.setCancelable(false);
                editText.setText("" + OCRResultActivity.this.folder_save_ocr);
                ((LinearLayout) dialog.findViewById(R.id.llOK)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        if (editText.getText().length() > 0) {
                            OCRResultActivity.this.folder_save_ocr = editText.getText().toString();
                            dialog.dismiss();
                            new saveTextFile().execute(new Void[0]);
                            return;
                        }
                        editText.setError("Enter Folder Name");
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
    }

    private class saveTextFile extends AsyncTask<Void, Void, Void> {
        File file;

        private saveTextFile() {
        }


        public void onPreExecute() {
            super.onPreExecute();
        }


        public Void doInBackground(Void... voidArr) {
            if (OCRResultActivity.this.scanText.getText() != null) {
                MainUtils.isFolderCreated();
                File file2 = new File(Environment.getExternalStorageDirectory(), "Document Scanner/OCR Files/" + OCRResultActivity.this.folder_save_ocr);
                if (!file2.exists()) {
                    file2.mkdir();
                }
                File externalStorageDirectory = Environment.getExternalStorageDirectory();
                File file3 = new File(externalStorageDirectory, "Document Scanner/OCR Files/" + OCRResultActivity.this.folder_save_ocr + "/" + ("OcrScan_" + Long.toString(System.currentTimeMillis())) + ".txt");
                this.file = file3;
                if (file3.exists()) {
                    this.file.delete();
                }
                try {
                    FileWriter fileWriter = new FileWriter(this.file);
                    fileWriter.append(OCRResultActivity.this.scanText.getText().toString());
                    fileWriter.flush();
                    fileWriter.close();
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

        /* access modifiers changed from: protected */
        public void onPostExecute(Void voidR) {
            Intent intent = new Intent(OCRResultActivity.this, TextFileViwerActivity.class);
            intent.putExtra("textFilePath", this.file.getPath());
            OCRResultActivity.this.startActivity(intent);
        }
    }

    public static void checkCopyToClipboard(Context context, String str) {
        ((ClipboardManager) context.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("text", str));
        Toast makeText = Toast.makeText(context, "Copy To Clipboard", 0);
        makeText.setGravity(17, 0, 0);
        makeText.show();
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
