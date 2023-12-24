package com.scanner.camera.phototopdf.papercamerascanner.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.scanner.camera.phototopdf.papercamerascanner.R;


public class Privacy_Policy_activity extends AppCompatActivity {
    private static final String TAG = "Main";
    CharSequence[] Titles = {"PRIVACY POLICY", "PERMISSION"};
    TabPagerAdapter adapter;
    Context context;
    ViewPager pager;
    private ProgressDialog progress;
    TabLayout tabs;
    Toolbar toolbar;
    int totaltabs = 2;
    private WebView webvw;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);

            window.setStatusBarColor(getResources().getColor(R.color.pp_color));
        }
        setContentView((int) R.layout.privacy_policy_activity);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolBar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.context = this;
        this.pager = (ViewPager) findViewById(R.id.viewpager);
        this.tabs = (TabLayout) findViewById(R.id.tabs);
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), this.Titles, this.totaltabs);
        this.adapter = tabPagerAdapter;
        this.pager.setAdapter(tabPagerAdapter);
        this.tabs.setupWithViewPager(this.pager);
    }

    public class TabPagerAdapter extends FragmentStatePagerAdapter {
        CharSequence[] Titles;
        int totaltabs;

        public TabPagerAdapter(FragmentManager fragmentManager, CharSequence[] charSequenceArr, int i) {
            super(fragmentManager);
            this.Titles = charSequenceArr;
            this.totaltabs = i;
        }

        public Fragment getItem(int i) {
            if (i == 0) {
                return new Privacy_Policy_Fragment();
            }
            if (i != 1) {
                return null;
            }
            return new Permission_Fragment();
        }

        public CharSequence getPageTitle(int i) {
            return this.Titles[i];
        }

        public int getCount() {
            return this.totaltabs;
        }
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
           // case R.id.change_consent:

              //  return true;
            /*case R.id.contact:
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
            case R.id.privacy:
                Intent intent3 = new Intent(getApplicationContext(), Privacy_Policy_activity.class);

                startActivity(intent3);
                return true;
            case R.id.rate:
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
            case R.id.share:
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
