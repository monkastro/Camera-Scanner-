package com.scanner.camera.phototopdf.papercamerascanner.common;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.scanner.camera.phototopdf.papercamerascanner.Activity.Camera_Scanner_MainActivity;
import com.scanner.camera.phototopdf.papercamerascanner.R;


public class ExitActivity extends AppCompatActivity {

    LinearLayout btnNo;
    LinearLayout btnYes;
    RelativeLayout layout;
    LinearLayout lin_rate;

    public void onBackPressed() {
    }




    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(Integer.MIN_VALUE);

            window.setStatusBarColor(getResources().getColor(R.color.start_color));
        }
        setContentView((int) R.layout.adview_layout_exit1);

        this.btnYes = (LinearLayout) findViewById(R.id.btnyes);
        this.btnNo = (LinearLayout) findViewById(R.id.btnno);
        this.lin_rate = (LinearLayout) findViewById(R.id.lin_rate);
        this.btnYes.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ExitActivity.this.sendBroadcast(new Intent(Camera_Scanner_MainActivity.ACTION_CLOSE));
                ExitActivity.this.finish();
            }
        });
        this.btnNo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ExitActivity.this.finish();
            }
        });
        this.lin_rate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ExitActivity.this.isOnline()) {
                    Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + ExitActivity.this.getPackageName()));

                    intent.addFlags(268435456);
                    ExitActivity.this.startActivity(intent);
                    return;
                }
                Toast makeText = Toast.makeText(ExitActivity.this.getApplicationContext(), "No Internet Connection..", 0);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }
        });
    }

    public boolean isOnline() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
}
