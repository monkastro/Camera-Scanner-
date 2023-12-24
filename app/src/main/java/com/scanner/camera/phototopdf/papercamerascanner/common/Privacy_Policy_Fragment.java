package com.scanner.camera.phototopdf.papercamerascanner.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.fragment.app.Fragment;

import com.scanner.camera.phototopdf.papercamerascanner.R;


public class Privacy_Policy_Fragment extends Fragment {
    private static final String TAG = "policy";

    public ProgressDialog progress;
    private WebView webvw;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.privacy_fragment, viewGroup, false);
        WebView webView = (WebView) inflate.findViewById(R.id.privacy_webview);
        this.webvw = webView;
        webView.getSettings().setJavaScriptEnabled(true);
        this.webvw.setScrollBarStyle(33554432);
        final AlertDialog create = new AlertDialog.Builder(getActivity()).create();
        this.progress = ProgressDialog.show(getActivity(), "Please Wait...", "Loading...");
        this.webvw.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                Log.i(Privacy_Policy_Fragment.TAG, "Processing webview url click...");
                webView.loadUrl(str);
                return true;
            }

            public void onPageFinished(WebView webView, String str) {
                Log.i(Privacy_Policy_Fragment.TAG, "Finished loading URL: " + str);
                if (Privacy_Policy_Fragment.this.progress.isShowing()) {
                    Privacy_Policy_Fragment.this.progress.dismiss();
                }
            }

            public void onReceivedError(WebView webView, int i, String str, String str2) {
                Log.e(Privacy_Policy_Fragment.TAG, "Error: " + str);
                create.setTitle("Error");
                create.setMessage(str);
                create.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                create.show();
            }
        });
        this.webvw.loadUrl("https://bharatstudiospolicy.blogspot.com/2020/12/a4-paper-scanner-camera-document-scanner.html");
        return inflate;
    }
}
