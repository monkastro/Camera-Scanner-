package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.scanner.camera.phototopdf.papercamerascanner.Adapter.MyTabPageAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.Fragments.AllDocFragment;
import com.scanner.camera.phototopdf.papercamerascanner.Fragments.BookFragment;
import com.scanner.camera.phototopdf.papercamerascanner.Fragments.BusinessCardFragment;
import com.scanner.camera.phototopdf.papercamerascanner.Fragments.DocumentFragment;
import com.scanner.camera.phototopdf.papercamerascanner.Fragments.IDCardFragment;
import com.scanner.camera.phototopdf.papercamerascanner.Fragments.PhotoIdFragment;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.common.ExitActivity;
import com.scanner.camera.phototopdf.papercamerascanner.common.Privacy_Policy_activity;


import javax.crypto.spec.SecretKeySpec;


public class Camera_Scanner_MainActivity extends AppCompatActivity {
    public static final String ACTION_CLOSE = "ACTION_CLOSE";
    private static String INSTALL_PREF = "install_pref";
    private static final int PERMISSION_REQUEST_CODE = 1;
    private static SecretKeySpec secret;
    Activity activity;


    final Context context = this;
    private FirstReceiver firstReceiver;
    ImageView ivCameraBtn;
    ImageView ivGallaryBtn;
    RelativeLayout layout;
    Context mContext;

    private MyTabPageAdapter pageAdapter;
    String result = "";
    //SharedPreferences spref;
    public TabLayout tabLayout;
    Toolbar toolbar;
    private ViewPager viewPager;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().build());
        setContentView((int) R.layout.activity_main);

        this.activity = this;
        this.mContext = this;
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        IntentFilter intentFilter = new IntentFilter(ACTION_CLOSE);
        FirstReceiver firstReceiver2 = new FirstReceiver();
        this.firstReceiver = firstReceiver2;
        registerReceiver(firstReceiver2, intentFilter);


        this.ivGallaryBtn = (ImageView) findViewById(R.id.ivGallaryBtn);
        this.ivCameraBtn = (ImageView) findViewById(R.id.ivCameraBtn);
        this.viewPager = (ViewPager) findViewById(R.id.viewPager);
        this.tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        MyTabPageAdapter myTabPageAdapter = new MyTabPageAdapter(getSupportFragmentManager());
        this.pageAdapter = myTabPageAdapter;
        myTabPageAdapter.addFragments(new AllDocFragment(), "All Documents");
        this.pageAdapter.addFragments(new BookFragment(), "Book Scan");
        this.pageAdapter.addFragments(new DocumentFragment(), "Scan Document");
        this.pageAdapter.addFragments(new IDCardFragment(), "ID Card");
        this.pageAdapter.addFragments(new BusinessCardFragment(), "Business Card");
        this.pageAdapter.addFragments(new PhotoIdFragment(), "Photo ID");
        //this.pageAdapter.addFragments(new TextFilesFragment(), "OCR Scan");
        this.viewPager.setAdapter(this.pageAdapter);
        this.tabLayout.setupWithViewPager(this.viewPager);
        MainUtils.setCameraDialogVisible(this, true);
        MainUtils.setIdCardSides(this, 1);
        this.tabLayout.getTabAt(0).setIcon((int) R.drawable.ic_all_document_home_page_icon);
        this.tabLayout.getTabAt(1).setIcon((int) R.drawable.ic_book_scan__home_page_icon);
        this.tabLayout.getTabAt(2).setIcon((int) R.drawable.ic_scan_documnet_home_page_icon);
        this.tabLayout.getTabAt(3).setIcon((int) R.drawable.ic_id_card_home_page__icon);
        this.tabLayout.getTabAt(4).setIcon((int) R.drawable.ic_business_card_home_page_icon);
        this.tabLayout.getTabAt(5).setIcon((int) R.drawable.ic_photo_id_home_page_icon);
        //this.tabLayout.getTabAt(6).setIcon((int) R.drawable.ic_ocr_tab_icon);
        this.tabLayout.getTabAt(0).getIcon().setColorFilter(getResources().getColor(R.color.colorSelectedTab), PorterDuff.Mode.SRC_IN);
        this.tabLayout.addOnTabSelectedListener((TabLayout.OnTabSelectedListener) new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {
            }

            public void onTabSelected(TabLayout.Tab tab) {
                if (tab == Camera_Scanner_MainActivity.this.tabLayout.getTabAt(0)) {
                    Camera_Scanner_MainActivity.this.tabLayout.getTabAt(0).getIcon().setColorFilter(Camera_Scanner_MainActivity.this.getResources().getColor(R.color.colorSelectedTab), PorterDuff.Mode.SRC_IN);
                } else if (tab == Camera_Scanner_MainActivity.this.tabLayout.getTabAt(1)) {
                    Camera_Scanner_MainActivity.this.tabLayout.getTabAt(1).getIcon().setColorFilter(Camera_Scanner_MainActivity.this.getResources().getColor(R.color.colorSelectedTab), PorterDuff.Mode.SRC_IN);
                } else if (tab == Camera_Scanner_MainActivity.this.tabLayout.getTabAt(2)) {
                    Camera_Scanner_MainActivity.this.tabLayout.getTabAt(2).getIcon().setColorFilter(Camera_Scanner_MainActivity.this.getResources().getColor(R.color.colorSelectedTab), PorterDuff.Mode.SRC_IN);
                } else if (tab == Camera_Scanner_MainActivity.this.tabLayout.getTabAt(3)) {
                    Camera_Scanner_MainActivity.this.tabLayout.getTabAt(3).getIcon().setColorFilter(Camera_Scanner_MainActivity.this.getResources().getColor(R.color.colorSelectedTab), PorterDuff.Mode.SRC_IN);
                } else if (tab == Camera_Scanner_MainActivity.this.tabLayout.getTabAt(4)) {
                    Camera_Scanner_MainActivity.this.tabLayout.getTabAt(4).getIcon().setColorFilter(Camera_Scanner_MainActivity.this.getResources().getColor(R.color.colorSelectedTab), PorterDuff.Mode.SRC_IN);
                } else if (tab == Camera_Scanner_MainActivity.this.tabLayout.getTabAt(5)) {
                    Camera_Scanner_MainActivity.this.tabLayout.getTabAt(5).getIcon().setColorFilter(Camera_Scanner_MainActivity.this.getResources().getColor(R.color.colorSelectedTab), PorterDuff.Mode.SRC_IN);
                } /*else {
                    MainActivity.this.tabLayout.getTabAt(6).getIcon().setColorFilter(MainActivity.this.getResources().getColor(R.color.colorSelectedTab), PorterDuff.Mode.SRC_IN);
                }*/
            }

            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab == Camera_Scanner_MainActivity.this.tabLayout.getTabAt(0)) {
                    Camera_Scanner_MainActivity.this.tabLayout.getTabAt(0).getIcon().setColorFilter(Camera_Scanner_MainActivity.this.getResources().getColor(R.color.colorUnselectedTab), PorterDuff.Mode.SRC_IN);
                } else if (tab == Camera_Scanner_MainActivity.this.tabLayout.getTabAt(1)) {
                    Camera_Scanner_MainActivity.this.tabLayout.getTabAt(1).getIcon().setColorFilter(Camera_Scanner_MainActivity.this.getResources().getColor(R.color.colorUnselectedTab), PorterDuff.Mode.SRC_IN);
                } else if (tab == Camera_Scanner_MainActivity.this.tabLayout.getTabAt(2)) {
                    Camera_Scanner_MainActivity.this.tabLayout.getTabAt(2).getIcon().setColorFilter(Camera_Scanner_MainActivity.this.getResources().getColor(R.color.colorUnselectedTab), PorterDuff.Mode.SRC_IN);
                } else if (tab == Camera_Scanner_MainActivity.this.tabLayout.getTabAt(3)) {
                    Camera_Scanner_MainActivity.this.tabLayout.getTabAt(3).getIcon().setColorFilter(Camera_Scanner_MainActivity.this.getResources().getColor(R.color.colorUnselectedTab), PorterDuff.Mode.SRC_IN);
                } else if (tab == Camera_Scanner_MainActivity.this.tabLayout.getTabAt(4)) {
                    Camera_Scanner_MainActivity.this.tabLayout.getTabAt(4).getIcon().setColorFilter(Camera_Scanner_MainActivity.this.getResources().getColor(R.color.colorUnselectedTab), PorterDuff.Mode.SRC_IN);
                } else if (tab == Camera_Scanner_MainActivity.this.tabLayout.getTabAt(5)) {
                    Camera_Scanner_MainActivity.this.tabLayout.getTabAt(5).getIcon().setColorFilter(Camera_Scanner_MainActivity.this.getResources().getColor(R.color.colorUnselectedTab), PorterDuff.Mode.SRC_IN);
                } /*else {
                    MainActivity.this.tabLayout.getTabAt(6).getIcon().setColorFilter(MainActivity.this.getResources().getColor(R.color.colorUnselectedTab), PorterDuff.Mode.SRC_IN);
                }*/
            }
        });
        this.ivCameraBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Camera_Scanner_MainActivity cameraScannerMainActivity = Camera_Scanner_MainActivity.this;
                cameraScannerMainActivity.setSaveFolderMethod(cameraScannerMainActivity.tabLayout.getSelectedTabPosition());
                if (Camera_Scanner_MainActivity.this == null) {
                    Intent intent = new Intent(Camera_Scanner_MainActivity.this, CameraScanActivity.class);

                    Camera_Scanner_MainActivity.this.startActivity(intent);
                }else {
                    Intent intent2 = new Intent(Camera_Scanner_MainActivity.this, CameraScanActivity.class);

                    Camera_Scanner_MainActivity.this.startActivity(intent2);
                }
            }
        });
        this.ivGallaryBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Camera_Scanner_MainActivity cameraScannerMainActivity = Camera_Scanner_MainActivity.this;
                cameraScannerMainActivity.setSaveFolderMethod(cameraScannerMainActivity.tabLayout.getSelectedTabPosition());

                if (Camera_Scanner_MainActivity.this== null) {
                    Intent intent = new Intent(Camera_Scanner_MainActivity.this, Camera_Scanner_GallaryActivity.class);

                    Camera_Scanner_MainActivity.this.startActivity(intent);
                }  else {
                    Intent intent2 = new Intent(Camera_Scanner_MainActivity.this, Camera_Scanner_GallaryActivity.class);

                    Camera_Scanner_MainActivity.this.startActivity(intent2);
                }
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case 16908332:
                onBackPressed();
                break;
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
            case R.id.qrScanner :

                    Intent intent5 = new Intent(this, GenerateQRActivity.class);

                    startActivity(intent5);
                    break;

            case R.id.rate :
                if (isOnline()) {
                    Intent intent6 = new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName()));

                    intent6.addFlags(268435456);
                    startActivity(intent6);
                } else {
                    Toast makeText = Toast.makeText(getApplicationContext(), "No Internet Connection..", 0);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                }
                return true;
            case R.id.share :
                if (isOnline()) {
                    Intent intent7 = new Intent("android.intent.action.SEND");
                    intent7.setType("text/plain");
                    intent7.putExtra("android.intent.extra.TEXT", "Hi! I'm using A4 Paper Scanner  : Paper Scanner. Check it out:http://play.google.com/store/apps/details?id=" + getPackageName());
                    startActivity(Intent.createChooser(intent7, "Share with Friends"));
                } else {
                    Toast makeText2 = Toast.makeText(getApplicationContext(), "No Internet Connection..", 0);
                    makeText2.setGravity(17, 0, 0);
                    makeText2.show();
                }
                return true;
        }
        return true;
    }

    public void setSaveFolderMethod(int i) {
        if (i == 0 || i == 2) {
            MainUtils.setSaveFolder(this.context, "Document");
            MainUtils.setScanType(this, "Document");
        } else if (i == 1) {
            MainUtils.setSaveFolder(this.context, "Book");
            MainUtils.setScanType(this, "Book");
        } else if (i == 3) {
            MainUtils.setSaveFolder(this, "IDCard");
            MainUtils.setScanType(this, "IDCard");
        } else if (i == 4) {
            MainUtils.setSaveFolder(this, "BusinessCard");
            MainUtils.setScanType(this, "IDCard");
        } else if (i == 5) {
            MainUtils.setSaveFolder(this.context, "Passport");
            MainUtils.setScanType(this, "Passport");
        } /*else if (i == 6) {
            MainUtils.setSaveFolder(this.context, "OCRText");
            MainUtils.setScanType(this, "Document");
        }*/
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this.context, "android.permission.WRITE_EXTERNAL_STORAGE") == 0 || ContextCompat.checkSelfPermission(this.context, "android.permission.READ_EXTERNAL_STORAGE") == 0 || ContextCompat.checkSelfPermission(this.context, "android.permission.CAMERA") == 0;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this.activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 1);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        if (i == 1 && iArr.length > 0 && iArr[0] == 0 && iArr[1] == 0) {
            int i2 = iArr[2];
        }
    }


    public void onResume() {
        super.onResume();
        if (!checkPermission()) {
            requestPermission();
        }
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, ExitActivity.class);

        startActivity(intent);
    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.firstReceiver);
    }

    class FirstReceiver extends BroadcastReceiver {
        FirstReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Log.e("FirstReceiver", "FirstReceiver");
            if (intent.getAction().equals(Camera_Scanner_MainActivity.ACTION_CLOSE)) {
                Camera_Scanner_MainActivity.this.finish();
            }
        }
    }

    private boolean checkNewInstall() {
        Context context2 = this.mContext;
        SharedPreferences sharedPreferences = context2.getSharedPreferences(context2.getPackageName(), 0);
        boolean z = sharedPreferences.getBoolean(INSTALL_PREF, false);
        if (!z) {
            sharedPreferences.edit().putBoolean(INSTALL_PREF, true).commit();
        }
        return z;
    }


        public Void doInBackground(Void... voidArr) {

            return null;
        }


}
