package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.scanner.camera.phototopdf.papercamerascanner.Adapter.CreationAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.Model.CreationModel;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.common.Privacy_Policy_activity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Camera_Scanner_CreationListActivity extends AppCompatActivity {
    CreationAdapter adapter;
    int ads_const;
    ArrayList<CreationModel> creationModels = new ArrayList<>();
    String folderPath;
    RelativeLayout layout;
    RecyclerView rvcreationChild;
    SharedPreferences spref;
    Toolbar toolbar;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        setContentView((int) R.layout.activity_creation_list);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Intent intent = getIntent();
        if (intent != null) {
            this.folderPath = intent.getStringExtra("folderPath");
        }
        this.rvcreationChild = (RecyclerView) findViewById(R.id.rvcreationChild);
    }

    public void bindMyCreation(File file) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm a");
        File[] listFiles = file.listFiles();
        if (listFiles != null && listFiles.length > 0) {
            for (int i = 0; i < listFiles.length; i++) {
                if (listFiles[i].isDirectory()) {
                    bindMyCreation(listFiles[i]);
                } else {
                    String format = simpleDateFormat.format(new Date(listFiles[i].lastModified()));
                    String format2 = simpleDateFormat2.format(new Date(listFiles[i].lastModified()));
                    ArrayList<CreationModel> arrayList = this.creationModels;
                    String name = listFiles[i].getName();
                    String parent = listFiles[i].getParent();
                    String path = listFiles[i].getPath();
                    arrayList.add(new CreationModel(name, parent, path, format + " " + format2, listFiles[i].length()));
                }
            }
            this.rvcreationChild.setHasFixedSize(true);
            this.rvcreationChild.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            CreationAdapter creationAdapter = new CreationAdapter(this, this.creationModels);
            this.adapter = creationAdapter;
            this.rvcreationChild.setAdapter(creationAdapter);
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

           /* case R.id.contact :
                Intent intent2 = new Intent("android.intent.action.SEND");
                intent2.setType("message/rfc822");
                intent2.putExtra("android.intent.extra.EMAIL", new String[]{""});
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


    public void onResume() {
        super.onResume();
        this.creationModels.clear();
        bindMyCreation(new File(this.folderPath));
    }
}
