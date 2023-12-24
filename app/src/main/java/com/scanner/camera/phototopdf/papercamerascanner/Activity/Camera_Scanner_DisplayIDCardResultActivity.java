package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import app.diesel.discrete_scrollview_indicator.DiscreteScrollViewIndicator;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.scanner.camera.phototopdf.papercamerascanner.Adapter.ScanBookPagesAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.common.Privacy_Policy_activity;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class Camera_Scanner_DisplayIDCardResultActivity extends AppCompatActivity {

    public ArrayList<String> ImagesArray = new ArrayList<>();
    LinearLayout btnDelete;
    LinearLayout btnEdit;
    LinearLayout btnSave;
    LinearLayout btnShare;
    Dialog dialogLoading;
    String foldername = "";
    DiscreteScrollViewIndicator indicator;
    ScanBookPagesAdapter scanBookPagesAdapter;
    DiscreteScrollView scrollView;
    Toolbar toolbar;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_display_i_d_card_result);
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.scrollView = (DiscreteScrollView) findViewById(R.id.picker);
        this.btnShare = (LinearLayout) findViewById(R.id.btnShare);
        this.btnEdit = (LinearLayout) findViewById(R.id.btnEdit);
        this.btnDelete = (LinearLayout) findViewById(R.id.btnDelete);
        this.btnSave = (LinearLayout) findViewById(R.id.btnSave);
        this.indicator = (DiscreteScrollViewIndicator) findViewById(R.id.indicator);
        init();
        this.btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArrayList arrayList = new ArrayList();
                arrayList.add(Camera_Scanner_DisplayIDCardResultActivity.this.ImagesArray.get(Camera_Scanner_DisplayIDCardResultActivity.this.scrollView.getCurrentItem()));
                Camera_Scanner_DisplayIDCardResultActivity cameraScannerDisplayIDCardResultActivity = Camera_Scanner_DisplayIDCardResultActivity.this;
                MainUtils.setRotateFileName(cameraScannerDisplayIDCardResultActivity, (String) cameraScannerDisplayIDCardResultActivity.ImagesArray.get(Camera_Scanner_DisplayIDCardResultActivity.this.scrollView.getCurrentItem()));
                Intent intent = new Intent(Camera_Scanner_DisplayIDCardResultActivity.this, ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ImageList", arrayList);
                intent.putExtras(bundle);

                Camera_Scanner_DisplayIDCardResultActivity.this.startActivity(intent);
            }
        });
        this.btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                File file = new File((String) Camera_Scanner_DisplayIDCardResultActivity.this.ImagesArray.get(Camera_Scanner_DisplayIDCardResultActivity.this.scrollView.getCurrentItem()));
                if (file.exists()) {
                    file.delete();
                    Camera_Scanner_DisplayIDCardResultActivity.this.ImagesArray.remove(Camera_Scanner_DisplayIDCardResultActivity.this.scrollView.getCurrentItem());
                    Camera_Scanner_DisplayIDCardResultActivity.this.scanBookPagesAdapter.notifyDataSetChanged();
                    if (Camera_Scanner_DisplayIDCardResultActivity.this.ImagesArray.size() == 0) {
                        Camera_Scanner_DisplayIDCardResultActivity.this.finish();
                    }
                }
            }
        });
        this.btnShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Camera_Scanner_DisplayIDCardResultActivity.this.ImagesArray.size() > 1) {
                    Camera_Scanner_DisplayIDCardResultActivity.this.shareDialog();
                    return;
                }
                Intent intent = new Intent();
                intent.setAction("android.intent.action.SEND");
                intent.putExtra("android.intent.extra.SUBJECT", "Here are some files.");
                intent.setType("*/*");
                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File((String) Camera_Scanner_DisplayIDCardResultActivity.this.ImagesArray.get(Camera_Scanner_DisplayIDCardResultActivity.this.scrollView.getCurrentItem()))));
                intent.addFlags(1);
                Camera_Scanner_DisplayIDCardResultActivity.this.startActivity(Intent.createChooser(intent, "send"));
            }
        });
        this.btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new savePDF().execute(new Void[0]);
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
            if (new File(str).exists() && (listFiles = new File(str).listFiles()) != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.getPath().contains(".png") || file.getPath().contains(".jpg")) {
                        this.ImagesArray.add(file.getPath());
                    }
                }
            }
        }
        ScanBookPagesAdapter scanBookPagesAdapter2 = new ScanBookPagesAdapter(this, this.ImagesArray);
        this.scanBookPagesAdapter = scanBookPagesAdapter2;
        this.scrollView.setAdapter(scanBookPagesAdapter2);
        this.indicator.setDiscreteScrolView(this.scrollView);
    }

    private class savePDF extends AsyncTask<Void, Void, Void> {
        File file2;

        private savePDF() {
            this.file2 = null;
        }


        public void onPreExecute() {
            super.onPreExecute();
            Camera_Scanner_DisplayIDCardResultActivity.this.showLoading();
        }


        public Void doInBackground(Void... voidArr) {
            try {
                String file = Environment.getExternalStorageDirectory().toString();
                File file3 = new File(file + "/" + Camera_Scanner_DisplayIDCardResultActivity.this.foldername);
                file3.mkdirs();
                this.file2 = new File(file3, "IdCard_"+ Long.toString(System.currentTimeMillis())  + ".pdf");
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(this.file2));
                document.open();
                for (int i = 0; i < Camera_Scanner_DisplayIDCardResultActivity.this.ImagesArray.size(); i++) {
                    Image instance = Image.getInstance((String) Camera_Scanner_DisplayIDCardResultActivity.this.ImagesArray.get(i));
                    instance.scalePercent(((((document.getPageSize().getWidth() - document.leftMargin()) - document.rightMargin()) - 0.0f) / instance.getWidth()) * 100.0f);
                    instance.setAlignment(5);
                    document.add(instance);
                }
                document.close();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Void voidR) {
            Camera_Scanner_DisplayIDCardResultActivity.this.dialogLoading.dismiss();
            Intent intent = new Intent(Camera_Scanner_DisplayIDCardResultActivity.this, Camera_Scanner_PDFViewerActivity.class);
            intent.putExtra("pdfFilePath", this.file2.getPath());
            Camera_Scanner_DisplayIDCardResultActivity.this.startActivity(intent);
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
                intent.putExtra("android.intent.extra.STREAM", Uri.fromFile(new File((String) Camera_Scanner_DisplayIDCardResultActivity.this.ImagesArray.get(Camera_Scanner_DisplayIDCardResultActivity.this.scrollView.getCurrentItem()))));
                intent.addFlags(1);
                Camera_Scanner_DisplayIDCardResultActivity.this.startActivity(Intent.createChooser(intent, "send"));
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
                Iterator it = Camera_Scanner_DisplayIDCardResultActivity.this.ImagesArray.iterator();
                while (it.hasNext()) {
                    arrayList.add(Uri.fromFile(new File((String) it.next())));
                }
                intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
                Camera_Scanner_DisplayIDCardResultActivity.this.startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.show();
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
        this.ImagesArray.clear();
        init();
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
