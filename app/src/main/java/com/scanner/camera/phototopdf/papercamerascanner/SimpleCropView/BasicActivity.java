package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.scanner.camera.phototopdf.papercamerascanner.Activity.ResultActivity;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.util.ArrayList;

public class BasicActivity extends AppCompatActivity {
    private static final String TAG = BasicActivity.class.getSimpleName();
    public static String selectedImageUrl;
    ImageView ivBack;
    ImageView ivSave;

    public static Intent createIntent(Activity activity) {
        return new Intent(activity, BasicActivity.class);
    }


    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_basic);
        selectedImageUrl = getIntent().getStringExtra("imageUrl");
        if (bundle == null) {
            getSupportFragmentManager().beginTransaction().add((int) R.id.container, (Fragment) BasicFragment.newInstance()).commit();
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    public void startResultActivity(Uri uri) {
        if (!isFinishing()) {
            if (MainUtils.getFromEdit(this)) {
                finish();
                ArrayList arrayList = new ArrayList();
                arrayList.add(uri.toString());
                Intent intent = new Intent(this, ResultActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("ImageList", arrayList);
                intent.putExtras(bundle);
                startActivity(intent);
                return;
            }
            finish();
        }
    }
}
