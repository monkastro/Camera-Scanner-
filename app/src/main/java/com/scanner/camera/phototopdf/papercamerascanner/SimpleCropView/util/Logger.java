package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.util;

import android.util.Log;

public class Logger {
    private static final String TAG = "SimpleCropView";
    public static boolean enabled = false;

    public static void m88e(String str) {
        if (enabled) {
            Log.e(TAG, str);
        }
    }


    public static void m89e(String str, Throwable th) {
        if (enabled) {
            Log.e(TAG, str, th);
        }
    }


    public static void m90i(String str) {
        if (enabled) {
            Log.i(TAG, str);
        }
    }


    public static void m91i(String str, Throwable th) {
        if (enabled) {
            Log.i(TAG, str, th);
        }
    }

}
