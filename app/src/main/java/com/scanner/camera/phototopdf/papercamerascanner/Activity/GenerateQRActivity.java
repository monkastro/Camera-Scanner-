package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;


import com.google.android.gms.vision.barcode.Barcode;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.common.Privacy_Policy_activity;

import java.io.File;

public class GenerateQRActivity extends AppCompatActivity {

    public Barcode barcodeResult;
    Bitmap bitmap;
    LinearLayout btnGenerate;
    LinearLayout btnScanQRCode;
    EditText etContact;
    EditText etEmail;
    EditText etMessage;
    EditText etSubject;
    EditText etText;
    EditText etWebLink;
    EditText etWifiName;
    EditText etWifiPassword;
    boolean isShow = false;
    ImageView ivEye;
    RelativeLayout layout;
    LinearLayout qrContact;
    LinearLayout qrEmail;
    LinearLayout qrText;
    String[] qrTyp = {"Wifi", "Email", "WebLink", "Text", "Phone"};
    LinearLayout qrWebLink;
    LinearLayout qrWifi;
    RadioButton radioButton;
    RadioGroup securityRadioGroup;
    String selecterQRType = "";
    Spinner spQRType;
    SharedPreferences spref;
    Toolbar toolbar;
    String wifiType = "WPA/WPA2";




    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_generate_q_r);
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolBar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        this.etWifiName = (EditText) findViewById(R.id.etWifiName);
        this.etWifiPassword = (EditText) findViewById(R.id.etWifiPassword);
        this.etContact = (EditText) findViewById(R.id.etContact);
        this.etText = (EditText) findViewById(R.id.etText);
        this.etWebLink = (EditText) findViewById(R.id.etWebLink);
        this.etEmail = (EditText) findViewById(R.id.etEmail);
        this.etSubject = (EditText) findViewById(R.id.etSubject);
        this.etMessage = (EditText) findViewById(R.id.etMessage);
        this.spQRType = (Spinner) findViewById(R.id.spQRType);
        this.ivEye = (ImageView) findViewById(R.id.ivEye);
        this.qrEmail = (LinearLayout) findViewById(R.id.qrEmail);
        this.qrText = (LinearLayout) findViewById(R.id.qrText);
        this.qrWebLink = (LinearLayout) findViewById(R.id.qrWebLink);
        this.qrWifi = (LinearLayout) findViewById(R.id.qrWifi);
        this.qrContact = (LinearLayout) findViewById(R.id.qrContact);
        this.btnScanQRCode = (LinearLayout) findViewById(R.id.btnScanQRCode);
        this.securityRadioGroup = (RadioGroup) findViewById(R.id.securityRadioGroup);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, 17367048, this.qrTyp);
        arrayAdapter.setDropDownViewResource(17367049);
        this.spQRType.setAdapter(arrayAdapter);
        this.btnGenerate = (LinearLayout) findViewById(R.id.btnGenerate);
        this.spQRType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onNothingSelected(AdapterView<?> adapterView) {
            }

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
                GenerateQRActivity generateQRActivity = GenerateQRActivity.this;
                generateQRActivity.selecterQRType = generateQRActivity.qrTyp[i];
                GenerateQRActivity generateQRActivity2 = GenerateQRActivity.this;
                generateQRActivity2.updateView(generateQRActivity2.selecterQRType);
            }
        });
        this.ivEye.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (GenerateQRActivity.this.isShow) {
                    GenerateQRActivity.this.isShow = false;
                    GenerateQRActivity.this.ivEye.setImageDrawable(GenerateQRActivity.this.getResources().getDrawable(R.drawable.ic_password_hide_icon));
                    GenerateQRActivity.this.etWifiPassword.setTransformationMethod(new PasswordTransformationMethod());
                    return;
                }
                GenerateQRActivity.this.isShow = true;
                GenerateQRActivity.this.ivEye.setImageDrawable(GenerateQRActivity.this.getResources().getDrawable(R.drawable.ic_password_show_icon));
                GenerateQRActivity.this.etWifiPassword.setTransformationMethod((TransformationMethod) null);
            }
        });
        this.securityRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                GenerateQRActivity generateQRActivity = GenerateQRActivity.this;
                generateQRActivity.radioButton = (RadioButton) generateQRActivity.findViewById(i);
                GenerateQRActivity generateQRActivity2 = GenerateQRActivity.this;
                generateQRActivity2.wifiType = generateQRActivity2.radioButton.getText().toString();
            }
        });
        this.btnGenerate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder();
                if (GenerateQRActivity.this.selecterQRType.equals("Wifi")) {
                    if (GenerateQRActivity.this.etWifiName.getText().toString().length() > 0 || GenerateQRActivity.this.etWifiPassword.getText().toString().length() > 0) {
                        sb.append("WIFI:T:");
                        sb.append(GenerateQRActivity.this.wifiType);
                        sb.append(";S:");
                        sb.append(GenerateQRActivity.this.etWifiName.getText().toString());
                        sb.append(";P:");
                        sb.append(GenerateQRActivity.this.etWifiPassword.getText().toString());
                        sb.append(";");
                        GenerateQRActivity.this.generateQR(sb);
                        return;
                    }
                    if (GenerateQRActivity.this.etWifiName.getText().toString().length() == 0) {
                        GenerateQRActivity.this.etWifiName.setError("Please Enter Wifi Name");
                    }
                    if (GenerateQRActivity.this.etWifiPassword.getText().toString().length() == 0) {
                        GenerateQRActivity.this.etWifiPassword.setError("Please Enter Wifi Password");
                    }
                } else if (GenerateQRActivity.this.selecterQRType.equals("Email")) {
                    if (GenerateQRActivity.this.etEmail.getText().toString().length() > 0 || GenerateQRActivity.this.etSubject.getText().toString().length() > 0 || GenerateQRActivity.this.etMessage.getText().toString().length() > 0) {
                        String obj = (GenerateQRActivity.this.etSubject.getText().toString().equals("") || GenerateQRActivity.this.etSubject.getText().toString().length() == 0) ? " " : GenerateQRActivity.this.etSubject.getText().toString();
                        sb.append("MATMSG:TO:");
                        sb.append(GenerateQRActivity.this.etEmail.getText().toString());
                        sb.append(";SUB:");
                        sb.append(obj);
                        sb.append(";BODY:");
                        sb.append(GenerateQRActivity.this.etMessage.getText().toString());
                        sb.append(";");
                        GenerateQRActivity.this.generateQR(sb);
                        return;
                    }
                    if (GenerateQRActivity.this.etEmail.getText().toString().length() == 0) {
                        GenerateQRActivity.this.etEmail.setError("Please Enter Email");
                    }
                    if (GenerateQRActivity.this.etSubject.getText().toString().length() == 0) {
                        GenerateQRActivity.this.etSubject.setError("Please Enter Email Subject");
                    }
                    if (GenerateQRActivity.this.etMessage.getText().toString().length() == 0) {
                        GenerateQRActivity.this.etSubject.setError("Please Enter Email Message");
                    }
                } else if (GenerateQRActivity.this.selecterQRType.equals("WebLink")) {
                    if (GenerateQRActivity.this.etWebLink.getText().length() > 0) {
                        if (GenerateQRActivity.this.etWebLink.getText().toString().contains("http://") || GenerateQRActivity.this.etWebLink.getText().toString().contains("https:")) {
                            sb.append(GenerateQRActivity.this.etWebLink.getText().toString());
                        } else {
                            sb.append("http://");
                            sb.append(GenerateQRActivity.this.etWebLink.getText().toString());
                        }
                        GenerateQRActivity.this.generateQR(sb);
                        return;
                    }
                    GenerateQRActivity.this.etWebLink.setError("Please Enter WebLink");
                } else if (GenerateQRActivity.this.selecterQRType.equals("Text")) {
                    if (GenerateQRActivity.this.etText.getText().length() > 0) {
                        sb.append(GenerateQRActivity.this.etText.getText().toString());
                        GenerateQRActivity.this.generateQR(sb);
                        return;
                    }
                    GenerateQRActivity.this.etText.setError("Please Enter Text");
                } else if (!GenerateQRActivity.this.selecterQRType.equals("Phone")) {
                } else {
                    if (GenerateQRActivity.this.etContact.getText().length() > 0) {
                        sb.append("tel:");
                        sb.append(GenerateQRActivity.this.etContact.getText().toString());
                        GenerateQRActivity.this.generateQR(sb);
                        return;
                    }
                    GenerateQRActivity.this.etContact.setError("Please Enter Phone");
                }
            }
        });
        this.btnScanQRCode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                new MaterialBarcodeScannerBuilder().withActivity(GenerateQRActivity.this).withEnableAutoFocus(true).withBleepEnabled(true).withBackfacingCamera().withText("Scanning...").withCenterTracker(R.drawable.track, R.drawable.track).withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    public void onResult(Barcode barcode) {
                        Barcode unused = GenerateQRActivity.this.barcodeResult = barcode;
                        System.out.println(GenerateQRActivity.this.barcodeResult.rawValue);
                        Intent intent = new Intent(GenerateQRActivity.this, DisplayScanResultActivity.class);
                        intent.putExtra("scanText", GenerateQRActivity.this.barcodeResult.rawValue);
                        GenerateQRActivity.this.startActivity(intent);
                    }
                }).build().startScan();
            }
        });
    }


    public void generateQR(StringBuilder sb) {
        Display defaultDisplay = ((WindowManager) getSystemService("window")).getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        int i = point.x;
        int i2 = point.y;
        if (i >= i2) {
            i = i2;
        }
        String sb2 = sb.toString();
        MainUtils.isFolderCreated();
        String str = Environment.getExternalStorageDirectory().getPath() + "/Document Scanner/";
        String str2 = "QR_" + System.currentTimeMillis();
        File file = new File(new File(Environment.getExternalStorageDirectory().getPath() + "/Document Scanner"), str2);
        QRGEncoder qRGEncoder = new QRGEncoder(sb2, (Bundle) null, QRGContents.Type.TEXT, (i * 3) / 4);
        qRGEncoder.setColorBlack(ViewCompat.MEASURED_STATE_MASK);
        qRGEncoder.setColorWhite(-1);
        try {
            this.bitmap = qRGEncoder.getBitmap();
            if (ContextCompat.checkSelfPermission(getApplicationContext(), "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
                try {
                    if (new QRGSaver().save(str, str2, this.bitmap, QRGContents.ImageType.IMAGE_JPEG)) {
                        Intent intent = new Intent(this, QRDisplayActivity.class);
                        intent.putExtra("imagePath", file.getPath() + ".jpg");
                        startActivity(intent);
                        clearTexts();
                        return;
                    }
                    Toast makeText = Toast.makeText(getApplicationContext(), "QR not Generated.", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
            }
        } catch (Exception e2) {
            Log.v("HHHH", e2.toString());
        }
    }


    public void updateView(String str) {
        if (str.equals("Wifi")) {
            this.qrWifi.setVisibility(0);
            this.qrContact.setVisibility(8);
            this.qrEmail.setVisibility(8);
            this.qrWebLink.setVisibility(8);
            this.qrText.setVisibility(8);
        } else if (str.equals("Email")) {
            this.qrWifi.setVisibility(8);
            this.qrContact.setVisibility(8);
            this.qrEmail.setVisibility(0);
            this.qrWebLink.setVisibility(8);
            this.qrText.setVisibility(8);
        } else if (str.equals("WebLink")) {
            this.qrWifi.setVisibility(8);
            this.qrContact.setVisibility(8);
            this.qrEmail.setVisibility(8);
            this.qrWebLink.setVisibility(0);
            this.qrText.setVisibility(8);
        } else if (str.equals("Text")) {
            this.qrWifi.setVisibility(8);
            this.qrContact.setVisibility(8);
            this.qrEmail.setVisibility(8);
            this.qrWebLink.setVisibility(8);
            this.qrText.setVisibility(0);
        } else if (str.equals("Phone")) {
            this.qrWifi.setVisibility(8);
            this.qrContact.setVisibility(0);
            this.qrEmail.setVisibility(8);
            this.qrWebLink.setVisibility(8);
            this.qrText.setVisibility(8);
        }
    }

    private void clearTexts() {
        this.etWifiName.setText("");
        this.etWifiPassword.setText("");
        this.etContact.setText("");
        this.etWebLink.setText("");
        this.etText.setText("");
        this.etEmail.setText("");
        this.etSubject.setText("");
        this.etMessage.setText("");
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
