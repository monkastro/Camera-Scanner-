package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.itextpdf.text.html.HtmlTags;
import com.kyanogen.signatureview.SignatureView;
import com.scanner.camera.phototopdf.papercamerascanner.Adapter.ColorAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.BindEditing;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.IConstant;
import com.scanner.camera.phototopdf.papercamerascanner.Model.ColorModel;
import com.scanner.camera.phototopdf.papercamerascanner.OnClickInterface.OnItemClickListener;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.util.ArrayList;

public class DrawSignatureActivity extends AppCompatActivity implements OnItemClickListener {
    LinearLayout btnReset;
    LinearLayout buttonDone;
    ColorAdapter colorAdapter;
    ArrayList<ColorModel> colorArrayList = new ArrayList<>();
    RecyclerView rvColors;
    SignatureView signature_view;
    int text_Color = 0;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_draw_signature);
        this.signature_view = (SignatureView) findViewById(R.id.signature_view);
        this.btnReset = (LinearLayout) findViewById(R.id.btnReset);
        this.buttonDone = (LinearLayout) findViewById(R.id.buttonDoneSignature);
        this.rvColors = (RecyclerView) findViewById(R.id.rvColors);
        bindViews();
        this.buttonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (!DrawSignatureActivity.this.signature_view.isBitmapEmpty()) {
                    DrawSignatureActivity.this.signature_view.setDrawingCacheEnabled(true);
                    DrawSignatureActivity.this.signature_view.buildDrawingCache();
                    Bitmap createBitmap = Bitmap.createBitmap(DrawSignatureActivity.this.signature_view.getDrawingCache());
                    DrawSignatureActivity.this.signature_view.setDrawingCacheEnabled(false);
                    Bitmap scaleDownBitmap = DrawSignatureActivity.scaleDownBitmap(createBitmap, 120, DrawSignatureActivity.this);
                    Log.e("HHHH", "Bitmap " + scaleDownBitmap);
                    Intent intent = new Intent();
                    intent.putExtra("resultSignature", scaleDownBitmap);

                    DrawSignatureActivity.this.setResult(IConstant.RESULT_SIGNATURE, intent);
                    DrawSignatureActivity.this.finish();
                    return;
                }
                Toast makeText = Toast.makeText(DrawSignatureActivity.this, "Please draw Your Signature...!", 0);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }
        });
        this.btnReset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DrawSignatureActivity.this.signature_view.clearCanvas();
            }
        });
    }

    private void bindViews() {
        this.colorArrayList = BindEditing.bindTextColor();
        this.rvColors.setHasFixedSize(true);
        this.rvColors.setLayoutManager(new GridLayoutManager(this, 6));
        ColorAdapter colorAdapter2 = new ColorAdapter(this, this.colorArrayList, this, HtmlTags.COLOR);
        this.colorAdapter = colorAdapter2;
        this.rvColors.setAdapter(colorAdapter2);
    }

    public static Bitmap scaleDownBitmap(Bitmap bitmap, int i, Context context) {
        int i2 = (int) (((float) i) * context.getResources().getDisplayMetrics().density);
        return Bitmap.createScaledBitmap(bitmap, (int) (((double) (bitmap.getWidth() * i2)) / ((double) bitmap.getHeight())), i2, true);
    }

    public void OnClick(View view, int i, String str) {
        System.out.println(str);
        if (((str.hashCode() == 94842723 && str.equals(HtmlTags.COLOR)) ? (char) 0 : 65535) == 0) {
            this.text_Color = Color.parseColor(this.colorArrayList.get(i).getColorCode());
            for (int i2 = 0; i2 < this.colorArrayList.size(); i2++) {
                if (i2 == i) {
                    this.colorArrayList.get(i).setSelected(true);
                } else {
                    this.colorArrayList.get(i2).setSelected(false);
                }
            }
            this.colorAdapter.notifyDataSetChanged();
            this.signature_view.setPenColor(this.text_Color);
        }
    }
}
