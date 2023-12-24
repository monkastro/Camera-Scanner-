package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.scanner.camera.phototopdf.papercamerascanner.R;

public class Camera_Scanner_Splash extends AppCompatActivity {
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashactivity);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Camera_Scanner_Splash.this, Camera_Scanner_MainActivity.class);
                Camera_Scanner_Splash.this.startActivity(mainIntent);
                Camera_Scanner_Splash.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}