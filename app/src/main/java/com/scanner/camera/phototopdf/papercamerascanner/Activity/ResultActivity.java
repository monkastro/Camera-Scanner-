package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.itextpdf.text.html.HtmlTags;
import com.scanner.camera.phototopdf.papercamerascanner.Adapter.ColorAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.Adapter.FontStyleAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.Adapter.StickerAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.CustomView.CanvasView;
import com.scanner.camera.phototopdf.papercamerascanner.Filter.ThumbnailCallback;
import com.scanner.camera.phototopdf.papercamerascanner.Filter.ThumbnailItem;
import com.scanner.camera.phototopdf.papercamerascanner.Filter.ThumbnailsAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.Filter.ThumbnailsManager;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.BindEditing;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.IConstant;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.Model.ColorModel;
import com.scanner.camera.phototopdf.papercamerascanner.Model.DataModel;
import com.scanner.camera.phototopdf.papercamerascanner.Model.FontStyleModel;
import com.scanner.camera.phototopdf.papercamerascanner.OnClickInterface.OnItemClickListener;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.BasicActivity;

import com.scanner.camera.phototopdf.papercamerascanner.common.Privacy_Policy_activity;
import com.xiaopo.flying.sticker.DrawableSticker;
import com.xiaopo.flying.sticker.Sticker;
import com.xiaopo.flying.sticker.StickerView;
import com.xiaopo.flying.sticker.TextSticker;
import com.zomato.photofilters.imageprocessors.Filter;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ResultActivity extends AppCompatActivity implements OnItemClickListener, ThumbnailCallback {
    static final  boolean Disabled = false;
    ThumbnailsAdapter adapter;
    ArrayList<DataModel> arrayList2 = new ArrayList<>();
    Bitmap bitmap;
    int brightnessValue = 0;
    int brushColor = R.color.colorDefault;
    float brushSizeValue = 15.0f;
    LinearLayout btnAddBrightness;
    LinearLayout btnAddColor;
    LinearLayout btnAddContrast;
    LinearLayout btnAddFont;
    LinearLayout btnAddText;
    LinearLayout btnAddTextS;
    LinearLayout btnAddWatermark;
    LinearLayout btnAdjust;
    LinearLayout btnDBrush;
    LinearLayout btnDBrushSize;
    LinearLayout btnDColor;
    LinearLayout btnDColorPicker;
    LinearLayout btnDEraser;
    LinearLayout btnDoodle;
    LinearLayout btnEdgeDetect;
    LinearLayout btnEraserTool;
    LinearLayout btnFilter;
    LinearLayout btnHBrush;
    LinearLayout btnHBrushSize;
    LinearLayout btnHColor;
    LinearLayout btnHEraser;
    LinearLayout btnHighlight;
    LinearLayout btnOCR;
    LinearLayout btnSignature;
    LinearLayout btnSticker;
    LinearLayout btnTools;
    LinearLayout buttonDone;
    LinearLayout buttonRotateRight;
    CanvasView canvasViewDoodle;
    CanvasView canvasViewHighlight;
    ColorAdapter colorAdapter;
    ArrayList<ColorModel> colorArrayList = new ArrayList<>();
    final Context context = this;
    float contrastValue = 1.0f;
    Dialog dialogLoading;
    BottomSheetDialog dialogTools;
    DrawableSticker drawableSticker;
    Typeface fontFamily = null;
    FontStyleAdapter fontStyleAdapter;
    ArrayList<FontStyleModel> fontStyleArrayList = new ArrayList<>();
    LinearLayout horiAdjust;
    LinearLayout horiDoodle;
    LinearLayout horiHighlight;
    Bitmap imageDefaultBitmap;
    ArrayList<String> imageList;
    ArrayList<String> imageListEdited = new ArrayList<>();
    Boolean isAdjustContrast = false;
    boolean isColorPickerEnable = false;
    Boolean isColorToolForText = false;
    Boolean isDoodleEdit = false;
    Boolean isDoodleFirst = true;
    boolean isEdgeDetectEnable = false;
    Boolean isHighlightEdit = false;
    Boolean isHighlightFirst = true;
    Boolean isMagicEraser = false;
    boolean isRotateEnable = false;
    boolean isSavePdf = false;
    Boolean isStickerAdded = false;
    boolean isStickerAvailable = false;
    ImageView ivCancelAddText;
    ImageView ivCancelAdjust;
    ImageView ivCancelColor;
    ImageView ivCancelContrast;
    ImageView ivCancelDoodle;
    ImageView ivCancelFilter;
    ImageView ivCancelFont;
    ImageView ivCancelHighlight;
    ImageView ivCancelSize;
    ImageView ivCancelSticker;
    ImageView ivCancelTools;
    ImageView ivCroppedResult;
    ImageView ivDBrush;
    ImageView ivDBrushSize;
    ImageView ivDColor;
    ImageView ivDColorPicker;
    ImageView ivDEraser;
    ImageView ivHBrush;
    ImageView ivHBrushSize;
    ImageView ivHColor;
    ImageView ivHEraser;
    ImageView ivOkAddText;
    ImageView ivOkAdjustment;
    ImageView ivOkColor;
    ImageView ivOkContrast;
    ImageView ivOkDoodle;
    ImageView ivOkFilter;
    ImageView ivOkFont;
    ImageView ivOkHighlight;
    ImageView ivOkSize;
    ImageView ivOkSticker;
    LinearLayout llAddText;
    LinearLayout llBrushSize;
    LinearLayout llColorTool;
    LinearLayout llContrast;
    LinearLayout llFilterTool;
    LinearLayout llFontTool;
    LinearLayout llMainMenu;
    LinearLayout llStickers;
    File myDir;
    int oldBrushColor = 0;
    int oldBrushSize = 0;
    Typeface oldFontFamily = null;
    int oldText = R.color.colorBlack;
    int onTouchBrushColor;
    Bitmap originalBitmap;
    RelativeLayout rel_save_bmp;
    String root = "";
    RecyclerView rvColors;
    RecyclerView rvFilters;
    RecyclerView rvFontFamily;
    RecyclerView rvStickers;
    SeekBar sbBrightness;
    SeekBar sbBrushSize;
    SeekBar sbContrast;
    String scanTextResult = "";
    Boolean selectedToolDoodle = false;
    Boolean shouldAllowBack = false;
    String stickerText;
    StickerView stickerView;
    int text_Color = 0;
    List<ThumbnailItem> thumbs;
    Toolbar toolbar;
    TextView tvAddText;
    TextView tvAdjust;
    TextView tvDBrush;
    TextView tvDBrushSize;
    TextView tvDColor;
    TextView tvDColorPicker;
    TextView tvDEraser;
    TextView tvHBrush;
    TextView tvHBrushSize;
    TextView tvHColor;
    TextView tvHEraser;
    Bitmap updatedBitmapDoodle;
    Bitmap updatedBitmapHighlight;

    static {
        System.loadLibrary("NativeImageProcessor");
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_result);
        this.root = Environment.getExternalStorageDirectory().toString();
        File file = new File(this.root + "/Document Scanner");
        this.myDir = file;
        file.mkdirs();
        Toolbar toolbar2 = (Toolbar) findViewById(R.id.toolbar);
        this.toolbar = toolbar2;
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        this.btnTools = (LinearLayout) findViewById(R.id.btnTools);
        //this.btnFilter = (LinearLayout) findViewById(R.id.btnFilter);
        this.btnEdgeDetect = (LinearLayout) findViewById(R.id.btnEdgeDetect);
        this.buttonDone = (LinearLayout) findViewById(R.id.buttonDone);
        this.llMainMenu = (LinearLayout) findViewById(R.id.llMainMenu);
        this.llColorTool = (LinearLayout) findViewById(R.id.llColorTool);
        this.llFilterTool = (LinearLayout) findViewById(R.id.llFilterTool);
        this.llAddText = (LinearLayout) findViewById(R.id.llAddText);
        this.llFontTool = (LinearLayout) findViewById(R.id.llFontTool);
        this.llBrushSize = (LinearLayout) findViewById(R.id.llBrushSize);
        this.llContrast = (LinearLayout) findViewById(R.id.llContrast);
        this.llStickers = (LinearLayout) findViewById(R.id.llStickers);
        this.ivCancelColor = (ImageView) findViewById(R.id.ivCancelColor);
        this.ivCancelFilter = (ImageView) findViewById(R.id.ivCancelFilter);
        this.ivCancelAddText = (ImageView) findViewById(R.id.ivCancelAddText);
        this.ivCancelFont = (ImageView) findViewById(R.id.ivCancelFont);
        this.ivCancelSize = (ImageView) findViewById(R.id.ivCancelSize);
        this.ivCancelAdjust = (ImageView) findViewById(R.id.ivCancelAdjust);
        this.ivCancelContrast = (ImageView) findViewById(R.id.ivCancelContrast);
        this.ivCancelSticker = (ImageView) findViewById(R.id.ivCancelSticker);
        this.ivOkColor = (ImageView) findViewById(R.id.ivOkColor);
        this.ivOkFilter = (ImageView) findViewById(R.id.ivOkFilter);
        this.ivOkAddText = (ImageView) findViewById(R.id.ivOkAddText);
        this.ivOkFont = (ImageView) findViewById(R.id.ivOkFont);
        this.ivOkSize = (ImageView) findViewById(R.id.ivOkSize);
        this.ivOkAdjustment = (ImageView) findViewById(R.id.ivOkAdjustment);
        this.ivOkContrast = (ImageView) findViewById(R.id.ivOkContrast);
        this.ivOkSticker = (ImageView) findViewById(R.id.ivOkSticker);
        this.btnAddFont = (LinearLayout) findViewById(R.id.btnAddFont);
        this.btnAddColor = (LinearLayout) findViewById(R.id.btnAddColor);
        this.btnAddTextS = (LinearLayout) findViewById(R.id.btnAddTextS);
        this.tvAddText = (TextView) findViewById(R.id.tvAddText);
        this.tvAdjust = (TextView) findViewById(R.id.tvAdjust);
        this.btnAddBrightness = (LinearLayout) findViewById(R.id.btnAddBrightness);
        this.btnAddContrast = (LinearLayout) findViewById(R.id.btnAddContrast);
        this.canvasViewDoodle = (CanvasView) findViewById(R.id.canvasViewDoodle);
        this.canvasViewHighlight = (CanvasView) findViewById(R.id.canvasViewHighlight);
        this.ivCroppedResult = (ImageView) findViewById(R.id.ivCroppedResult);
        this.sbBrightness = (SeekBar) findViewById(R.id.sbBrightness);
        this.sbContrast = (SeekBar) findViewById(R.id.sbContrast);
        this.rvFontFamily = (RecyclerView) findViewById(R.id.rvFontFamily);
        this.rvColors = (RecyclerView) findViewById(R.id.rvColors);
        this.rvFilters = (RecyclerView) findViewById(R.id.rvFilters);
        this.rvStickers = (RecyclerView) findViewById(R.id.rvStickers);
        this.rel_save_bmp = (RelativeLayout) findViewById(R.id.rel_save_bmp);
        this.horiAdjust = (LinearLayout) findViewById(R.id.llAdjustment);
        this.stickerView = (StickerView) findViewById(R.id.stickerView);
        bindToolsView();
        initDoodleTools();
        ArrayList<String> arrayList = (ArrayList) getIntent().getExtras().getSerializable("ImageList");
        this.imageList = arrayList;
        if ((arrayList.size() == 1 && (MainUtils.getScanType(this).equals("Book") || MainUtils.getScanType(this).equals("Document"))) || MainUtils.getScanType(this).equals("IDCard") || MainUtils.getScanType(this).equals("BusinessCard") || MainUtils.getScanType(this).equals("Passport")) {
            Log.e("SSS", "File Path " + this.imageList.get(0));
            if (this.imageList.get(0).contains("content://")) {
                ((RequestBuilder) ((RequestBuilder) Glide.with((FragmentActivity) this).load(this.imageList.get(0)).diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true)).into(this.ivCroppedResult);
            } else {
                ((RequestBuilder) ((RequestBuilder) Glide.with((FragmentActivity) this).load(new File(this.imageList.get(0))).diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true)).into(this.ivCroppedResult);
            }
        }
        bindViews();
        this.btnTools.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.dialogTools.show();
            }
        });
        this.ivCancelTools.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.dialogTools.dismiss();
            }
        });
        this.btnAddColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ResultActivity.this.isStickerAvailable) {
                    ResultActivity.this.llAddText.setVisibility(8);
                    ResultActivity.this.llColorTool.setVisibility(0);
                    ResultActivity resultActivity = ResultActivity.this;
                    resultActivity.oldText = resultActivity.text_Color;
                    ResultActivity.this.isColorToolForText = true;
                    return;
                }
                Toast makeText = Toast.makeText(ResultActivity.this.getApplicationContext(), "Add Text by Using ADD TEXT..!", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }
        });
        this.ivOkColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llColorTool.setVisibility(8);
                if (ResultActivity.this.isColorToolForText.booleanValue()) {
                    ResultActivity.this.llAddText.setVisibility(0);
                } else if (ResultActivity.this.selectedToolDoodle.booleanValue()) {
                    ResultActivity.this.horiDoodle.setVisibility(0);
                    ResultActivity resultActivity = ResultActivity.this;
                    resultActivity.brushColor = resultActivity.text_Color;
                    ResultActivity.this.canvasViewDoodle.setMode(CanvasView.Mode.DRAW);
                    ResultActivity.this.canvasViewDoodle.setDrawer(CanvasView.Drawer.PEN);
                    ResultActivity.this.canvasViewDoodle.setPaintStrokeColor(ResultActivity.this.brushColor);
                    ResultActivity.this.manageSelectedToolColor(R.id.btnDBrush);
                } else {
                    ResultActivity.this.horiHighlight.setVisibility(0);
                    ResultActivity resultActivity2 = ResultActivity.this;
                    resultActivity2.brushColor = resultActivity2.text_Color;
                    ResultActivity.this.canvasViewHighlight.setMode(CanvasView.Mode.DRAW);
                    ResultActivity.this.canvasViewHighlight.setDrawer(CanvasView.Drawer.PEN);
                    ResultActivity.this.canvasViewHighlight.setPaintStrokeColor(ResultActivity.this.brushColor);
                    ResultActivity.this.canvasViewHighlight.setOpacity(128);
                    ResultActivity.this.manageSelectedToolColor(R.id.btnHBrush);
                }
            }
        });
        this.ivCancelColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llColorTool.setVisibility(8);
                if (ResultActivity.this.isColorToolForText.booleanValue()) {
                    ResultActivity.this.llAddText.setVisibility(0);
                    ResultActivity resultActivity = ResultActivity.this;
                    resultActivity.text_Color = resultActivity.oldText;
                    ResultActivity resultActivity2 = ResultActivity.this;
                    resultActivity2.updateSticker(resultActivity2.stickerText, ResultActivity.this.fontFamily, ResultActivity.this.text_Color);
                } else if (ResultActivity.this.selectedToolDoodle.booleanValue()) {
                    ResultActivity.this.horiDoodle.setVisibility(0);
                } else {
                    ResultActivity.this.horiHighlight.setVisibility(0);
                }
            }
        });

        // disable button filter
        /*this.btnFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.ivCroppedResult.invalidate();
                ResultActivity.this.originalBitmap = ((BitmapDrawable) ResultActivity.this.ivCroppedResult.getDrawable()).getBitmap();
                ResultActivity.this.initUIWidgets();
                ResultActivity.this.llFilterTool.setVisibility(0);
                ResultActivity.this.llMainMenu.setVisibility(8);
            }
        });*/
        this.ivOkFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llFilterTool.setVisibility(8);
                ResultActivity.this.llMainMenu.setVisibility(0);
            }
        });
        this.ivCancelFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llFilterTool.setVisibility(8);
                ResultActivity.this.llMainMenu.setVisibility(0);
                ResultActivity.this.ivCroppedResult.setImageBitmap(ResultActivity.this.originalBitmap);
            }
        });
        this.btnAddFont.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ResultActivity.this.isStickerAvailable) {
                    ResultActivity.this.llAddText.setVisibility(8);
                    ResultActivity.this.llFontTool.setVisibility(0);
                    ResultActivity resultActivity = ResultActivity.this;
                    resultActivity.oldFontFamily = resultActivity.fontFamily;
                    return;
                }
                Toast makeText = Toast.makeText(ResultActivity.this.getApplicationContext(), "Add Text by Using ADD TEXT..!", 1);
                makeText.setGravity(17, 0, 0);
                makeText.show();
            }
        });
        this.ivOkFont.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llFontTool.setVisibility(8);
                ResultActivity.this.llAddText.setVisibility(0);
            }
        });
        this.ivCancelFont.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llAddText.setVisibility(0);
                ResultActivity.this.llFontTool.setVisibility(8);
                ResultActivity resultActivity = ResultActivity.this;
                resultActivity.fontFamily = resultActivity.oldFontFamily;
                ResultActivity resultActivity2 = ResultActivity.this;
                resultActivity2.updateSticker(resultActivity2.stickerText, ResultActivity.this.fontFamily, ResultActivity.this.text_Color);
            }
        });
        this.btnAddText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.dialogTools.dismiss();
                ResultActivity.this.llMainMenu.setVisibility(8);
                ResultActivity.this.llAddText.setVisibility(0);
                ResultActivity.this.tvAddText.setText("Add Text");
            }
        });
        this.btnAddTextS.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.addSimpleTextDialog();
            }
        });
        this.ivOkAddText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llAddText.setVisibility(8);
                ResultActivity.this.llMainMenu.setVisibility(0);
            }
        });
        this.ivCancelAddText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llAddText.setVisibility(8);
                ResultActivity.this.llMainMenu.setVisibility(0);
            }
        });
        this.btnAddWatermark.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.dialogTools.dismiss();
                ResultActivity.this.llMainMenu.setVisibility(8);
                ResultActivity.this.llAddText.setVisibility(0);
                ResultActivity.this.tvAddText.setText("Watermark");
            }
        });
        this.btnDoodle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.dialogTools.dismiss();
                ResultActivity.this.llMainMenu.setVisibility(8);
                if (ResultActivity.this.isDoodleFirst.booleanValue()) {
                    ResultActivity resultActivity = ResultActivity.this;
                    resultActivity.imageDefaultBitmap = MainUtils.getImageBitmap(Camera_Scanner_EdgeDetectionActivity.class, resultActivity.ivCroppedResult);
                    ResultActivity.this.isDoodleFirst = false;
                }
                ResultActivity.this.addDoodle();
                ResultActivity.this.selectedToolDoodle = true;
            }
        });
        this.btnDBrush.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.canvasViewDoodle.setMode(CanvasView.Mode.DRAW);
                ResultActivity.this.canvasViewDoodle.setDrawer(CanvasView.Drawer.PEN);
                ResultActivity.this.canvasViewDoodle.setPaintStrokeColor(ResultActivity.this.brushColor);
                ResultActivity.this.manageSelectedToolColor(R.id.btnDBrush);
                ResultActivity resultActivity = ResultActivity.this;
                resultActivity.oldBrushSize = (int) resultActivity.brushSizeValue;
            }
        });
        this.btnDEraser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.canvasViewDoodle.setLayerType(2, (Paint) null);
                ResultActivity.this.canvasViewDoodle.setMode(CanvasView.Mode.ERASER);
                ResultActivity.this.manageSelectedToolColor(R.id.btnDEraser);
            }
        });
        this.btnDBrushSize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.horiDoodle.setVisibility(8);
                ResultActivity.this.llBrushSize.setVisibility(0);
                ResultActivity.this.manageSelectedToolColor(R.id.btnDBrushSize);
            }
        });
        this.btnDColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.horiDoodle.setVisibility(8);
                ResultActivity.this.llColorTool.setVisibility(0);
                ResultActivity resultActivity = ResultActivity.this;
                resultActivity.oldBrushColor = resultActivity.brushColor;
                ResultActivity.this.isColorToolForText = false;
                ResultActivity.this.manageSelectedToolColor(R.id.btnDColor);
            }
        });
        this.btnDColorPicker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.canvasViewDoodle.setVisibility(8);
                ResultActivity.this.isColorPickerEnable = true;
                ResultActivity.this.manageSelectedToolColor(R.id.btnDColorPicker);
            }
        });
        this.ivOkDoodle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ResultActivity.this.horiDoodle.getVisibility() == 0) {
                    ResultActivity.this.horiDoodle.setVisibility(8);
                }
                if (ResultActivity.this.stickerView.getStickerCount() > 0) {
                    ResultActivity.this.stickerView.remo();
                }
                ResultActivity resultActivity = ResultActivity.this;
                resultActivity.updatedBitmapDoodle = resultActivity.getBitmapView(resultActivity.rel_save_bmp);
                ResultActivity.this.isDoodleEdit = true;
                ResultActivity.this.canvasViewDoodle.setMode(CanvasView.Mode.STOP_DRAWING);
                ResultActivity.this.llMainMenu.setVisibility(0);
            }
        });
        this.ivCancelDoodle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ResultActivity.this.horiDoodle.getVisibility() == 0) {
                    ResultActivity.this.horiDoodle.setVisibility(8);
                    ResultActivity.this.canvasViewDoodle.setVisibility(8);
                    while (ResultActivity.this.canvasViewDoodle.undo()) {
                        ResultActivity.this.canvasViewDoodle.undo();
                    }
                }
                ResultActivity.this.llMainMenu.setVisibility(0);
            }
        });
        this.ivCancelSize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llBrushSize.setVisibility(8);
                if (ResultActivity.this.selectedToolDoodle.booleanValue()) {
                    ResultActivity.this.horiDoodle.setVisibility(0);
                    ResultActivity.this.canvasViewDoodle.setPaintStrokeColor(ResultActivity.this.oldBrushSize);
                    if (ResultActivity.this.canvasViewDoodle.getMode().equals(CanvasView.Mode.DRAW)) {
                        ResultActivity.this.manageSelectedToolColor(R.id.btnDBrush);
                    } else {
                        ResultActivity.this.manageSelectedToolColor(R.id.btnDEraser);
                    }
                } else {
                    ResultActivity.this.horiHighlight.setVisibility(0);
                    ResultActivity.this.canvasViewHighlight.setPaintStrokeColor(ResultActivity.this.oldBrushSize);
                    if (ResultActivity.this.canvasViewHighlight.getMode().equals(CanvasView.Mode.DRAW)) {
                        ResultActivity.this.manageSelectedToolColor(R.id.btnHBrush);
                    } else {
                        ResultActivity.this.manageSelectedToolColor(R.id.btnHEraser);
                    }
                }
            }
        });
        this.ivOkSize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llBrushSize.setVisibility(8);
                if (ResultActivity.this.selectedToolDoodle.booleanValue()) {
                    ResultActivity.this.horiDoodle.setVisibility(0);
                    if (ResultActivity.this.canvasViewDoodle.getMode().equals(CanvasView.Mode.DRAW)) {
                        ResultActivity.this.manageSelectedToolColor(R.id.btnDBrush);
                    } else {
                        ResultActivity.this.manageSelectedToolColor(R.id.btnDEraser);
                    }
                } else {
                    ResultActivity.this.horiHighlight.setVisibility(0);
                    if (ResultActivity.this.canvasViewHighlight.getMode().equals(CanvasView.Mode.DRAW)) {
                        ResultActivity.this.manageSelectedToolColor(R.id.btnHBrush);
                    } else {
                        ResultActivity.this.manageSelectedToolColor(R.id.btnHEraser);
                    }
                }
            }
        });
        this.ivCroppedResult.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (ResultActivity.this.isColorPickerEnable) {
                    ImageView imageView = (ImageView) view;
                    Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    Matrix matrix = new Matrix();
                    imageView.getImageMatrix().invert(matrix);
                    float[] fArr = {motionEvent.getX(), motionEvent.getY()};
                    matrix.mapPoints(fArr);
                    int pixel = bitmap.getPixel((int) fArr[0], (int) fArr[1]);
                    int red = Color.red(pixel);
                    int green = Color.green(pixel);
                    int blue = Color.blue(pixel);
                    int alpha = Color.alpha(pixel);
                    ResultActivity.this.onTouchBrushColor = Color.argb(alpha, red, green, blue);
                    if (ResultActivity.this.isColorPickerEnable) {
                        ResultActivity resultActivity = ResultActivity.this;
                        resultActivity.brushColor = resultActivity.onTouchBrushColor;
                        ResultActivity.this.canvasViewDoodle.setMode(CanvasView.Mode.DRAW);
                        ResultActivity.this.canvasViewDoodle.setPaintStrokeColor(ResultActivity.this.brushColor);
                        ResultActivity.this.canvasViewDoodle.setVisibility(0);
                        ResultActivity.this.isColorPickerEnable = false;
                        ResultActivity.this.manageSelectedToolColor(R.id.btnDBrush);
                    }
                }
                return false;
            }
        });
        this.btnHighlight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.dialogTools.dismiss();
                ResultActivity.this.llMainMenu.setVisibility(8);
                if (ResultActivity.this.isHighlightFirst.booleanValue()) {
                    ResultActivity resultActivity = ResultActivity.this;
                    resultActivity.imageDefaultBitmap = MainUtils.getImageBitmap(Camera_Scanner_EdgeDetectionActivity.class, resultActivity.ivCroppedResult);
                    ResultActivity.this.isHighlightFirst = false;
                }
                ResultActivity.this.addHighlight();
                ResultActivity.this.selectedToolDoodle = false;
            }
        });
        this.btnHBrush.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.canvasViewHighlight.setMode(CanvasView.Mode.DRAW);
                ResultActivity.this.canvasViewHighlight.setDrawer(CanvasView.Drawer.PEN);
                ResultActivity.this.canvasViewHighlight.setPaintStrokeColor(ResultActivity.this.brushColor);
                ResultActivity.this.manageSelectedToolColor(R.id.btnHBrush);
            }
        });
        this.btnHEraser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.canvasViewHighlight.setLayerType(2, (Paint) null);
                ResultActivity.this.canvasViewHighlight.setMode(CanvasView.Mode.ERASER);
                ResultActivity.this.manageSelectedToolColor(R.id.btnHEraser);
            }
        });
        this.btnHBrushSize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.horiHighlight.setVisibility(8);
                ResultActivity.this.llBrushSize.setVisibility(0);
                ResultActivity.this.manageSelectedToolColor(R.id.btnHBrushSize);
                ResultActivity resultActivity = ResultActivity.this;
                resultActivity.oldBrushSize = (int) resultActivity.brushSizeValue;
            }
        });
        this.btnHColor.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Log.e("HHHH", "COLOR TOUCH " + ResultActivity.this.isColorPickerEnable);
                ResultActivity.this.horiHighlight.setVisibility(8);
                ResultActivity.this.llColorTool.setVisibility(0);
                ResultActivity resultActivity = ResultActivity.this;
                resultActivity.oldBrushColor = resultActivity.brushColor;
                ResultActivity.this.isColorToolForText = false;
                ResultActivity.this.manageSelectedToolColor(R.id.btnHColor);
            }
        });
        this.ivOkHighlight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ResultActivity.this.horiHighlight.getVisibility() == 0) {
                    ResultActivity.this.horiHighlight.setVisibility(8);
                }
                if (ResultActivity.this.stickerView.getStickerCount() > 0) {
                    ResultActivity.this.stickerView.remo();
                }
                ResultActivity resultActivity = ResultActivity.this;
                resultActivity.updatedBitmapHighlight = resultActivity.getBitmapView(resultActivity.rel_save_bmp);
                ResultActivity.this.isHighlightEdit = true;
                ResultActivity.this.canvasViewHighlight.setMode(CanvasView.Mode.STOP_DRAWING);
                ResultActivity.this.llMainMenu.setVisibility(0);
            }
        });
        this.ivCancelHighlight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ResultActivity.this.horiHighlight.getVisibility() == 0) {
                    ResultActivity.this.horiHighlight.setVisibility(8);
                    ResultActivity.this.canvasViewHighlight.setVisibility(8);
                    while (ResultActivity.this.canvasViewHighlight.undo()) {
                        ResultActivity.this.canvasViewHighlight.undo();
                    }
                }
                ResultActivity.this.llMainMenu.setVisibility(0);
            }
        });
        this.btnEraserTool.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.dialogTools.dismiss();
                ResultActivity.this.isMagicEraser = true;
                ResultActivity.this.ivCroppedResult.invalidate();
                ResultActivity.this.bitmap = ((BitmapDrawable) ResultActivity.this.ivCroppedResult.getDrawable()).getBitmap();
                new saveEditedBook().execute(new Void[0]);
            }
        });
        this.sbBrushSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                ResultActivity.this.brushSizeValue = (float) i;
                ResultActivity.this.canvasViewDoodle.setPaintStrokeWidth(ResultActivity.this.brushSizeValue);
                ResultActivity.this.canvasViewHighlight.setPaintStrokeWidth(ResultActivity.this.brushSizeValue);
            }
        });
        this.btnAdjust.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.dialogTools.dismiss();
                ResultActivity.this.llMainMenu.setVisibility(8);
                ResultActivity.this.horiAdjust.setVisibility(0);
                ResultActivity.this.ivCroppedResult.invalidate();
                ResultActivity.this.originalBitmap = ((BitmapDrawable) ResultActivity.this.ivCroppedResult.getDrawable()).getBitmap();
            }
        });
        this.ivCancelAdjust.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.horiAdjust.setVisibility(8);
                ResultActivity.this.ivCroppedResult.setImageBitmap(ResultActivity.this.originalBitmap);
                ResultActivity.this.llMainMenu.setVisibility(0);
            }
        });
        this.ivOkAdjustment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.horiAdjust.setVisibility(8);
                ResultActivity.this.llMainMenu.setVisibility(0);
            }
        });
        this.btnAddBrightness.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.isAdjustContrast = false;
                ResultActivity.this.horiAdjust.setVisibility(8);
                ResultActivity.this.llContrast.setVisibility(0);
                ResultActivity.this.tvAdjust.setText("Brightness");
                ResultActivity.this.sbBrightness.setVisibility(0);
                ResultActivity.this.sbContrast.setVisibility(8);
                ResultActivity.this.ivCroppedResult.invalidate();
                ResultActivity.this.originalBitmap = ((BitmapDrawable) ResultActivity.this.ivCroppedResult.getDrawable()).getBitmap();
            }
        });
        this.btnAddContrast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.isAdjustContrast = true;
                ResultActivity.this.horiAdjust.setVisibility(8);
                ResultActivity.this.llContrast.setVisibility(0);
                ResultActivity.this.tvAdjust.setText(ExifInterface.TAG_CONTRAST);
                ResultActivity.this.sbContrast.setVisibility(0);
                ResultActivity.this.sbBrightness.setVisibility(8);
                ResultActivity.this.ivCroppedResult.invalidate();
                ResultActivity.this.originalBitmap = ((BitmapDrawable) ResultActivity.this.ivCroppedResult.getDrawable()).getBitmap();
            }
        });
        this.ivCancelContrast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llContrast.setVisibility(8);
                ResultActivity.this.ivCroppedResult.setImageBitmap(ResultActivity.this.originalBitmap);
                ResultActivity.this.horiAdjust.setVisibility(0);
            }
        });
        this.ivOkContrast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llContrast.setVisibility(8);
                ResultActivity.this.horiAdjust.setVisibility(0);
            }
        });
        this.buttonRotateRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.dialogTools.dismiss();
                ResultActivity.this.isRotateEnable = true;
                ResultActivity.this.stickerView.remo();
                MainUtils.setFromEdit(ResultActivity.this, true);
                ResultActivity.this.rel_save_bmp.setDrawingCacheEnabled(true);
                ResultActivity resultActivity = ResultActivity.this;
                resultActivity.bitmap = Bitmap.createBitmap(resultActivity.rel_save_bmp.getDrawingCache());
                ResultActivity.this.rel_save_bmp.setDrawingCacheEnabled(false);
                ResultActivity.this.rel_save_bmp.performClick();
                new saveEditedBook().execute();
            }
        });
        this.btnEdgeDetect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.isEdgeDetectEnable = true;
                MainUtils.setFromEdit(ResultActivity.this, true);
                ResultActivity.this.stickerView.remo();
                ResultActivity.this.rel_save_bmp.setDrawingCacheEnabled(true);
                ResultActivity resultActivity = ResultActivity.this;
                resultActivity.bitmap = Bitmap.createBitmap(resultActivity.rel_save_bmp.getDrawingCache());
                ResultActivity.this.rel_save_bmp.setDrawingCacheEnabled(false);
                ResultActivity.this.rel_save_bmp.performClick();
                new saveEditedBook().execute(new Void[0]);
            }
        });
        this.buttonDone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.stickerView.remo();
                ResultActivity.this.rel_save_bmp.setDrawingCacheEnabled(true);
                ResultActivity resultActivity = ResultActivity.this;
                resultActivity.bitmap = Bitmap.createBitmap(resultActivity.rel_save_bmp.getDrawingCache());
                ResultActivity.this.rel_save_bmp.setDrawingCacheEnabled(false);
                ResultActivity.this.rel_save_bmp.performClick();
                if ((MainUtils.getScanType(ResultActivity.this).equals("Book") || MainUtils.getScanType(ResultActivity.this).equals("Document") || MainUtils.getScanType(ResultActivity.this).equals("IDCard") || MainUtils.getScanType(ResultActivity.this).equals("BusinessCard") || MainUtils.getScanType(ResultActivity.this).equals("Passport")) && ResultActivity.this.imageList.size() == 1) {
                    Log.e("HHHH", "File Path " + ResultActivity.this.imageList.get(0));
                    new saveEditedBook().execute(new Void[0]);
                }
            }
        });
       /* this.btnOCR.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.dialogTools.dismiss();
                ResultActivity.this.rel_save_bmp.setDrawingCacheEnabled(true);
                ResultActivity resultActivity = ResultActivity.this;
                resultActivity.bitmap = Bitmap.createBitmap(resultActivity.rel_save_bmp.getDrawingCache());
                ResultActivity.this.rel_save_bmp.setDrawingCacheEnabled(false);
                ResultActivity.this.rel_save_bmp.performClick();
                final StringBuilder sb = new StringBuilder();
                FirebaseVisionCloudTextRecognizerOptions build = new FirebaseVisionCloudTextRecognizerOptions.Builder().setLanguageHints(Arrays.asList(new String[]{"en", "hi", "gu", "pt", "af", "fil", "it", "mr", "ne", "sa"})).setModelType(2).build();
                FirebaseVision.getInstance().getCloudTextRecognizer(build).processImage(FirebaseVisionImage.fromBitmap(ResultActivity.this.bitmap)).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                    public void onSuccess(FirebaseVisionText firebaseVisionText) {
                        if (firebaseVisionText.getTextBlocks().size() == 0) {
                            sb.append("No text Found");
                            return;
                        }
                        for (FirebaseVisionText.TextBlock text : firebaseVisionText.getTextBlocks()) {
                            sb.append(text.getText());
                        }
                        if (sb.toString() != null) {
                            Intent intent = new Intent(ResultActivity.this, OCRResultActivity.class);
                            intent.putExtra("textScanned", sb.toString());
                            ResultActivity.this.startActivity(intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    public void onFailure(Exception exc) {
                        Log.e("HHHH", "" + exc.getMessage());
                        exc.printStackTrace();
                    }
                });
            }
        });*/
        this.btnSignature.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.dialogTools.dismiss();
                ResultActivity.this.startActivityForResult(new Intent(ResultActivity.this, DrawSignatureActivity.class), IConstant.RESULT_SIGNATURE);
            }
        });
        this.btnSticker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.isStickerAdded = false;
                ResultActivity.this.dialogTools.dismiss();
                ResultActivity.this.llMainMenu.setVisibility(8);
                ResultActivity.this.llStickers.setVisibility(0);
            }
        });
        this.ivCancelSticker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //ResultActivity.this.stickerView.removeCurrentSticker();
                ResultActivity.this.llStickers.setVisibility(8);
                ResultActivity.this.llMainMenu.setVisibility(0);
            }
        });
        this.ivOkSticker.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ResultActivity.this.llStickers.setVisibility(8);
                ResultActivity.this.llMainMenu.setVisibility(0);
            }
        });
        this.stickerView.setOnStickerOperationListener(new StickerView.OnStickerOperationListener() {
            public void onStickerDoubleTapped(Sticker sticker) {
            }

            public void onStickerDragFinished(Sticker sticker) {
            }

            public void onStickerFlipped(Sticker sticker) {
            }

            public void onStickerTouchedDown(Sticker sticker) {
            }

            public void onStickerZoomFinished(Sticker sticker) {
            }

            public void onStickerAdded(Sticker sticker) {
                if (sticker instanceof TextSticker) {
                    ResultActivity.this.stickerText = ((TextSticker) sticker).getText();
                    ResultActivity.this.isStickerAvailable = true;
                }
            }

            public void onStickerClicked(Sticker sticker) {
                if (sticker instanceof TextSticker) {
                    ResultActivity.this.stickerText = ((TextSticker) sticker).getText();
                    ResultActivity.this.isStickerAvailable = true;
                }
            }

            public void onStickerDeleted(Sticker sticker) {
                ResultActivity.this.isStickerAvailable = false;
            }
        });
        this.sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                ResultActivity.this.brightnessValue = i;
                ResultActivity.this.ivCroppedResult.setImageBitmap(ResultActivity.changeBitmapContrastBrightness(ResultActivity.this.originalBitmap, ResultActivity.this.contrastValue, (float) ResultActivity.this.brightnessValue));
            }
        });
        this.sbContrast.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                ResultActivity.this.contrastValue = (float) (i / 10);
                ResultActivity.this.ivCroppedResult.setImageBitmap(ResultActivity.changeBitmapContrastBrightness(ResultActivity.this.originalBitmap, ResultActivity.this.contrastValue, (float) ResultActivity.this.brightnessValue));
            }
        });
    }

    public Bitmap getBitmapView(View view) {
        Bitmap bitmap2;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap createBitmap = Bitmap.createBitmap(view.getDrawingCache());
        if (createBitmap.getWidth() > createBitmap.getHeight()) {
            bitmap2 = Bitmap.createScaledBitmap(createBitmap, 1080, (int) Math.abs(((float) 1080) * (((float) createBitmap.getWidth()) / ((float) createBitmap.getHeight()))), true);
        } else {
            bitmap2 = Bitmap.createScaledBitmap(createBitmap, (int) Math.abs(((float) 1080) * (((float) createBitmap.getWidth()) / ((float) createBitmap.getHeight()))), 1080, true);
        }
        view.setDrawingCacheEnabled(false);
        view.destroyDrawingCache();
        return bitmap2;
    }

    public static Bitmap changeBitmapContrastBrightness(Bitmap bitmap2, float f, float f2) {
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{f, 0.0f, 0.0f, 0.0f, f2, 0.0f, f, 0.0f, 0.0f, f2, 0.0f, 0.0f, f, 0.0f, f2, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f});
        Bitmap createBitmap = Bitmap.createBitmap(bitmap2.getWidth(), bitmap2.getHeight(), bitmap2.getConfig());
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
        return createBitmap;
    }

    private void bindToolsView() {
        View inflate = getLayoutInflater().inflate(R.layout.bottom_sheet_tools, (ViewGroup) null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.AppBottomSheetDialogTheme);
        this.dialogTools = bottomSheetDialog;
        bottomSheetDialog.setContentView(inflate);
        this.dialogTools.setCancelable(false);
        this.dialogTools.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.ivCancelTools = (ImageView) this.dialogTools.findViewById(R.id.ivCancelTools);
        this.btnAddText = (LinearLayout) this.dialogTools.findViewById(R.id.btnAddText);
        this.btnAdjust = (LinearLayout) this.dialogTools.findViewById(R.id.btnAdjust);
        this.btnSticker = (LinearLayout) this.dialogTools.findViewById(R.id.btnSticker);
        this.btnDoodle = (LinearLayout) this.dialogTools.findViewById(R.id.btnDoodle);
        this.btnEraserTool = (LinearLayout) this.dialogTools.findViewById(R.id.btnEraserTool);
        this.btnHighlight = (LinearLayout) this.dialogTools.findViewById(R.id.btnHighLight);
        this.btnSignature = (LinearLayout) this.dialogTools.findViewById(R.id.btnSignature);
       // this.btnOCR = (LinearLayout) this.dialogTools.findViewById(R.id.btnOCR);
        this.buttonRotateRight = (LinearLayout) this.dialogTools.findViewById(R.id.buttonRotateRight);
        this.btnAddWatermark = (LinearLayout) this.dialogTools.findViewById(R.id.btnAddWatermark);
    }

    private void bindViews() {
        this.fontStyleArrayList = BindEditing.bindFontStyle();
        this.rvFontFamily.setHasFixedSize(true);
        this.rvFontFamily.setLayoutManager(new GridLayoutManager(this.context, 3));
        FontStyleAdapter fontStyleAdapter2 = new FontStyleAdapter(this, this.fontStyleArrayList, this, "fontStyle");
        this.fontStyleAdapter = fontStyleAdapter2;
        this.rvFontFamily.setAdapter(fontStyleAdapter2);
        this.colorArrayList = BindEditing.bindTextColor();
        this.rvColors.setHasFixedSize(true);
        this.rvColors.setLayoutManager(new GridLayoutManager(this.context, 6));
        ColorAdapter colorAdapter2 = new ColorAdapter(this, this.colorArrayList, this, HtmlTags.COLOR);
        this.colorAdapter = colorAdapter2;
        this.rvColors.setAdapter(colorAdapter2);
        this.rvStickers.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(0);
        this.rvStickers.setLayoutManager(linearLayoutManager);
        loadStickers();
    }

    public void loadStickers() {
        try {
            String[] list = getResources().getAssets().list("sticker");
            if (list != null) {
                int length = list.length;
                for (int i = 0; i < length; i++) {
                    ArrayList<DataModel> arrayList = this.arrayList2;
                    arrayList.add(new DataModel("sticker/" + list[i]));
                }
                this.rvStickers.setAdapter(new StickerAdapter(this.arrayList2, this, this, "Sticker"));
            }
        } catch (IOException unused) {
        }
    }

    private void initDoodleTools() {
        this.ivHBrush = (ImageView) findViewById(R.id.ivHBrush);
        this.tvHBrush = (TextView) findViewById(R.id.tvHBrush);
        this.ivHEraser = (ImageView) findViewById(R.id.ivHEraser);
        this.tvHEraser = (TextView) findViewById(R.id.tvHEraser);
        this.ivHBrushSize = (ImageView) findViewById(R.id.ivHBrushSize);
        this.tvHBrushSize = (TextView) findViewById(R.id.tvHBrushSize);
        this.ivHColor = (ImageView) findViewById(R.id.ivHColor);
        this.tvHColor = (TextView) findViewById(R.id.tvHColor);
        this.ivDBrush = (ImageView) findViewById(R.id.ivDBrush);
        this.tvDBrush = (TextView) findViewById(R.id.tvDBrush);
        this.ivDEraser = (ImageView) findViewById(R.id.ivDEraser);
        this.tvDEraser = (TextView) findViewById(R.id.tvDEraser);
        this.ivDBrushSize = (ImageView) findViewById(R.id.ivDBrushSize);
        this.tvDBrushSize = (TextView) findViewById(R.id.tvDBrushSize);
        this.ivDColor = (ImageView) findViewById(R.id.ivDColor);
        this.tvDColor = (TextView) findViewById(R.id.tvDColor);
        this.ivDColorPicker = (ImageView) findViewById(R.id.ivDColorPicker);
        this.tvDColorPicker = (TextView) findViewById(R.id.tvDColorPicker);
        this.horiDoodle = (LinearLayout) findViewById(R.id.horiDoodle);
        this.ivCancelDoodle = (ImageView) findViewById(R.id.ivCancelDoodle);
        this.ivOkDoodle = (ImageView) findViewById(R.id.ivOkDoodle);
        this.btnDBrush = (LinearLayout) findViewById(R.id.btnDBrush);
        this.btnDColorPicker = (LinearLayout) findViewById(R.id.btnDColorPicker);
        this.btnDColor = (LinearLayout) findViewById(R.id.btnDColor);
        this.btnDBrushSize = (LinearLayout) findViewById(R.id.btnDBrushSize);
        this.btnDEraser = (LinearLayout) findViewById(R.id.btnDEraser);
        this.sbBrushSize = (SeekBar) findViewById(R.id.sbBrushSize);
        this.horiHighlight = (LinearLayout) findViewById(R.id.horiHighlight);
        this.ivCancelHighlight = (ImageView) findViewById(R.id.ivCancelHighlight);
        this.ivOkHighlight = (ImageView) findViewById(R.id.ivOkHighlight);
        this.btnHBrush = (LinearLayout) findViewById(R.id.btnHBrush);
        this.btnHColor = (LinearLayout) findViewById(R.id.btnHColor);
        this.btnHBrushSize = (LinearLayout) findViewById(R.id.btnHBrushSize);
        this.btnHEraser = (LinearLayout) findViewById(R.id.btnHEraser);
    }

    private class saveEditedBook extends AsyncTask<Void, Void, Void> {
        File file;

        private saveEditedBook() {
        }

        public void onPreExecute() {
            super.onPreExecute();
            ResultActivity.this.showLoading();
        }


        public Void doInBackground(Void... voidArr) {
            File file2 = new File(MainUtils.getRotateFileName(ResultActivity.this));
            this.file = file2;
            if (file2.exists()) {
                this.file.delete();
            }
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(this.file);
                ResultActivity.this.bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }


        public void onPostExecute(Void voidR) {
            if (ResultActivity.this.isRotateEnable) {
                ResultActivity.this.dialogLoading.dismiss();
                Intent intent = new Intent(ResultActivity.this, BasicActivity.class);
                intent.putExtra("imageUrl", Uri.fromFile(this.file).toString());
                ResultActivity.this.startActivity(intent);
                ResultActivity.this.finish();
                ResultActivity.this.isRotateEnable = false;
                Log.e("SSSS", "Path Converted : " + Uri.fromFile(this.file).toString());
            } else if (ResultActivity.this.isEdgeDetectEnable) {
                MainUtils.setFromGallaryEdge(ResultActivity.this, false);
                ResultActivity.this.dialogLoading.dismiss();
                Intent intent2 = new Intent(ResultActivity.this, Camera_Scanner_EdgeDetectionActivity.class);
                intent2.putExtra("imageUrl", this.file.getPath());
                ResultActivity.this.startActivity(intent2);
                ResultActivity.this.finish();
                ResultActivity.this.isEdgeDetectEnable = false;
                Log.e("SSSS", "Path Converted : " + Uri.fromFile(this.file).toString());
            } else if (ResultActivity.this.isMagicEraser.booleanValue()) {
                ResultActivity.this.dialogLoading.dismiss();
                Intent intent3 = new Intent(ResultActivity.this, MagicalEraserActivity.class);
                intent3.putExtra("processMagic", this.file.toString());
                ResultActivity.this.startActivityForResult(intent3, IConstant.RESULT_MAGIC);
                ResultActivity.this.isMagicEraser = false;
            } else {
                ResultActivity.this.dialogLoading.dismiss();
                ResultActivity.this.deleteEdgeDetectors();
                ResultActivity.this.finish();
            }
        }
    }


    public void showLoading() {
        Dialog dialog = new Dialog(this);
        this.dialogLoading = dialog;
        dialog.setContentView(R.layout.loading_dialog);
        this.dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialogLoading.setCancelable(false);
        this.dialogLoading.show();
    }


    public void OnClick(View r5, int n, String string) {
        int n2;
        System.out.println(string);
        int n3 = string.hashCode();
        if (n3 != -1550943582) {
            if (n3 != -225599203) {
                if (n3 != 94842723) return;
                if (!string.equals((Object)"color")) return;
                n2 = 1;
            } else {
                if (!string.equals((Object)"Sticker")) return;
                n2 = 2;
            }
        } else {
            if (!string.equals((Object)"fontStyle")) return;
            n2 = 0;
        }
        if (n2 != 0) {
            if (n2 != 1) {
                DrawableSticker drawableSticker;
                if (n2 != 2) {
                    return;
                }
                this.isStickerAdded = true;
                Bitmap bitmap = MainUtils.getBitmapFromAsset(((DataModel)this.arrayList2.get(n)).getDirName(), (Context)this);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(this.getResources(), bitmap);
                this.drawableSticker = drawableSticker = new DrawableSticker((Drawable)bitmapDrawable);
                drawableSticker.setDrawable((Drawable)bitmapDrawable);
                if (this.isStickerAdded.booleanValue()) {
                    StickerView stickerView = this.stickerView;
                    stickerView.remove(stickerView.getCurrentSticker());
                    this.stickerView.addSticker(this.drawableSticker);
                    return;
                }
                this.stickerView.addSticker(this.drawableSticker);
                return;
            }
            this.text_Color = Color.parseColor((String)((ColorModel)this.colorArrayList.get(n)).getColorCode());
            for (int i = 0; i < this.colorArrayList.size(); ++i) {
                if (i == n) {
                    ((ColorModel)this.colorArrayList.get(n)).setSelected(true);
                    continue;
                }
                ((ColorModel)this.colorArrayList.get(i)).setSelected(false);
            }
            this.colorAdapter.notifyDataSetChanged();
            this.updateSticker(this.stickerText, this.fontFamily, this.text_Color);
            return;
        }
        this.fontFamily = ResourcesCompat.getFont((Context)this.context, (int)((FontStyleModel)this.fontStyleArrayList.get(n)).getTypeFace());
        for (int i = 0; i < this.fontStyleArrayList.size(); ++i) {
            if (i == n) {
                ((FontStyleModel)this.fontStyleArrayList.get(n)).setSelected(true);
                continue;
            }
            ((FontStyleModel)this.fontStyleArrayList.get(i)).setSelected(false);
        }
        this.fontStyleAdapter.notifyDataSetChanged();
        this.updateSticker(this.stickerText, this.fontFamily, this.text_Color);
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == IConstant.RESULT_SIGNATURE) {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), (Bitmap) intent.getParcelableExtra("resultSignature"));
            DrawableSticker drawableSticker2 = new DrawableSticker(bitmapDrawable);
            this.drawableSticker = drawableSticker2;
            drawableSticker2.setDrawable((Drawable) bitmapDrawable);
            this.stickerView.addSticker(this.drawableSticker);
        } else if (i2 == IConstant.RESULT_MAGIC) {
            ((RequestBuilder) ((RequestBuilder) Glide.with((FragmentActivity) this).load(new File(MainUtils.getRotateFileName(this))).diskCacheStrategy(DiskCacheStrategy.NONE)).skipMemoryCache(true)).into(this.ivCroppedResult);
        }
    }

    public void updateSticker(String str, Typeface typeface, int i) {
        TextSticker textSticker = new TextSticker(this);
        textSticker.setText(str);
        textSticker.setMaxTextSize(30.0f);
        textSticker.resizeText();
        if (typeface == null && i == 0) {
            this.stickerView.addSticker(textSticker);
        } else if (typeface != null && i != 0) {
            textSticker.setTypeface(typeface);
            textSticker.setTextColor(i);
            this.stickerView.replace(textSticker);
        } else if (i != 0) {
            textSticker.setTextColor(i);
            this.stickerView.replace(textSticker);
        } else {
            textSticker.setTypeface(typeface);
            this.stickerView.replace(textSticker);
        }
    }


    public void addSimpleTextDialog() {
        final Dialog dialog = new Dialog(this.context);
        dialog.setContentView(R.layout.dialog_add_text);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        final EditText editText = (EditText) dialog.findViewById(R.id.etInputText);
        dialog.setCancelable(false);
        ((LinearLayout) dialog.findViewById(R.id.btnDAddText)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                TextSticker textSticker = new TextSticker(ResultActivity.this.context);
                textSticker.setText(editText.getText().toString());
                textSticker.setMaxTextSize(30.0f);
                textSticker.resizeText();
                ResultActivity.this.stickerView.addSticker(textSticker);
            }
        });
        ((LinearLayout) dialog.findViewById(R.id.btnCancelDialog)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    public void addDoodle() {
        if (this.horiDoodle.getVisibility() == 0) {
            this.canvasViewDoodle.setVisibility(8);
            this.horiDoodle.setVisibility(8);
            return;
        }
        if (this.isHighlightEdit.booleanValue()) {
            this.ivCroppedResult.setImageBitmap(this.updatedBitmapHighlight);
        } else {
            this.ivCroppedResult.setImageBitmap(this.imageDefaultBitmap);
        }
        this.canvasViewDoodle.setVisibility(0);
        this.canvasViewHighlight.setVisibility(8);
        this.horiDoodle.setVisibility(0);
        this.canvasViewDoodle.setMode(CanvasView.Mode.DRAW);
        this.canvasViewDoodle.setDrawer(CanvasView.Drawer.PEN);
        this.canvasViewDoodle.setPaintStrokeColor(this.brushColor);
        this.canvasViewDoodle.setPaintStrokeWidth(this.brushSizeValue);
        manageSelectedToolColor(R.id.btnDBrush);
        Log.e("SSSS", "GetImage Size  W  " + MainUtils.getImageBitmap(Camera_Scanner_EdgeDetectionActivity.class, this.ivCroppedResult).getWidth() + " H  " + MainUtils.getImageBitmap(Camera_Scanner_EdgeDetectionActivity.class, this.ivCroppedResult).getHeight());
    }


    public void addHighlight() {
        if (this.horiHighlight.getVisibility() == 0) {
            this.canvasViewHighlight.setVisibility(8);
            this.horiHighlight.setVisibility(8);
            return;
        }
        if (this.isDoodleEdit.booleanValue()) {
            this.ivCroppedResult.setImageBitmap(this.updatedBitmapDoodle);
        } else {
            this.ivCroppedResult.setImageBitmap(this.imageDefaultBitmap);
        }
        this.canvasViewHighlight.setVisibility(0);
        this.canvasViewDoodle.setVisibility(8);
        this.horiHighlight.setVisibility(0);
        this.canvasViewHighlight.setMode(CanvasView.Mode.DRAW);
        this.canvasViewHighlight.setDrawer(CanvasView.Drawer.PEN);
        this.canvasViewHighlight.setPaintStrokeColor(this.brushColor);
        this.canvasViewHighlight.setPaintStrokeWidth(this.brushSizeValue);
        this.canvasViewHighlight.setOpacity(128);
        manageSelectedToolColor(R.id.btnHBrush);
    }

    public void manageSelectedToolColor(int i) {
        if (i == R.id.btnHBrush) {
            this.ivHBrush.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.selectedButtonColor));
            this.tvHBrush.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.ivHBrushSize.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvHBrushSize.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivHEraser.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvHEraser.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivHColor.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvHColor.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.btnHBrushSize) {
            this.ivHBrush.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvHBrush.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivHBrushSize.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.selectedButtonColor));
            this.tvHBrushSize.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.ivHEraser.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvHEraser.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivHColor.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvHColor.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.btnHEraser) {
            this.ivHBrush.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvHBrush.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivHBrushSize.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvHBrushSize.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivHEraser.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.selectedButtonColor));
            this.tvHEraser.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.ivHColor.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvHColor.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.btnHColor) {
            this.ivHBrush.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvHBrush.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivHBrushSize.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvHBrushSize.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivHEraser.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvHEraser.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivHColor.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.selectedButtonColor));
            this.tvHColor.setTextColor(getResources().getColor(R.color.selectedButtonColor));
        } else if (i == R.id.btnDBrush) {
            this.ivDBrush.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.selectedButtonColor));
            this.tvDBrush.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.ivDBrushSize.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDBrushSize.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDEraser.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDEraser.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDColor.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDColor.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDColorPicker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDColorPicker.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.btnDBrushSize) {
            this.ivDBrush.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDBrush.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDBrushSize.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.selectedButtonColor));
            this.tvDBrushSize.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.ivDEraser.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDEraser.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDColor.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDColor.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDColorPicker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDColorPicker.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.btnDEraser) {
            this.ivDBrush.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDBrush.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDBrushSize.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDBrushSize.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDEraser.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.selectedButtonColor));
            this.tvDEraser.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.ivDColor.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDColor.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDColorPicker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDColorPicker.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.btnDColor) {
            this.ivDBrush.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDBrush.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDBrushSize.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDBrushSize.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDEraser.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDEraser.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDColor.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.selectedButtonColor));
            this.tvDColor.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.ivDColorPicker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDColorPicker.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.btnDColorPicker) {
            this.ivDBrush.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDBrush.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDBrushSize.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDBrushSize.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDEraser.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDEraser.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDColor.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.unselectedButtonColor));
            this.tvDColor.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivDColorPicker.setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.selectedButtonColor));
            this.tvDColorPicker.setTextColor(getResources().getColor(R.color.selectedButtonColor));
        }
    }

    private void closeEditingTools() {
        if (this.llColorTool.getVisibility() == 0) {
            this.llColorTool.setVisibility(8);
            if (this.isColorToolForText.booleanValue()) {
                this.llAddText.setVisibility(0);
                int i = this.oldText;
                this.text_Color = i;
                updateSticker(this.stickerText, this.fontFamily, i);
            } else if (this.selectedToolDoodle.booleanValue()) {
                this.horiDoodle.setVisibility(0);
            } else {
                this.horiHighlight.setVisibility(0);
            }
        } else if (this.llFilterTool.getVisibility() == 0) {
            this.llFilterTool.setVisibility(8);
            this.llMainMenu.setVisibility(0);
            this.ivCroppedResult.setImageBitmap(this.originalBitmap);
        } else if (this.llAddText.getVisibility() == 0) {
            this.llAddText.setVisibility(8);
            this.llMainMenu.setVisibility(0);
        } else if (this.llFontTool.getVisibility() == 0) {
            this.llAddText.setVisibility(0);
            this.llFontTool.setVisibility(8);
            Typeface typeface = this.oldFontFamily;
            this.fontFamily = typeface;
            updateSticker(this.stickerText, typeface, this.text_Color);
        } else if (this.llBrushSize.getVisibility() == 0) {
            this.llBrushSize.setVisibility(8);
            if (this.selectedToolDoodle.booleanValue()) {
                this.horiDoodle.setVisibility(0);
                this.canvasViewDoodle.setPaintStrokeColor(this.oldBrushSize);
                if (this.canvasViewDoodle.getMode().equals(CanvasView.Mode.DRAW)) {
                    manageSelectedToolColor(R.id.btnDBrush);
                } else {
                    manageSelectedToolColor(R.id.btnDEraser);
                }
            } else {
                this.horiHighlight.setVisibility(0);
                this.canvasViewHighlight.setPaintStrokeColor(this.oldBrushSize);
                if (this.canvasViewHighlight.getMode().equals(CanvasView.Mode.DRAW)) {
                    manageSelectedToolColor(R.id.btnHBrush);
                } else {
                    manageSelectedToolColor(R.id.btnHEraser);
                }
            }
        } else if (this.llContrast.getVisibility() == 0) {
            this.llContrast.setVisibility(8);
            this.ivCroppedResult.setImageBitmap(this.originalBitmap);
            this.horiAdjust.setVisibility(0);
        } else if (this.llStickers.getVisibility() == 0) {
            //this.stickerView.removeCurrentSticker();
            this.llStickers.setVisibility(8);
            this.llMainMenu.setVisibility(0);
        } else if (this.horiAdjust.getVisibility() == 0) {
            this.horiAdjust.setVisibility(8);
            this.ivCroppedResult.setImageBitmap(this.originalBitmap);
            this.llMainMenu.setVisibility(0);
        } else if (this.horiDoodle.getVisibility() == 0) {
            this.horiDoodle.setVisibility(8);
            this.canvasViewDoodle.setVisibility(8);
            while (this.canvasViewDoodle.undo()) {
                this.canvasViewDoodle.undo();
            }
            this.llMainMenu.setVisibility(0);
        } else if (this.horiHighlight.getVisibility() == 0) {
            this.horiHighlight.setVisibility(8);
            this.canvasViewHighlight.setVisibility(8);
            while (this.canvasViewHighlight.undo()) {
                this.canvasViewHighlight.undo();
            }
            this.llMainMenu.setVisibility(0);
        } else if (this.llMainMenu.getVisibility() == 0) {
            this.shouldAllowBack = true;
        }
    }


    public void initUIWidgets() {
        initHorizontalList();
    }

    private void initHorizontalList() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(0);
        linearLayoutManager.scrollToPosition(0);
        this.rvFilters.setLayoutManager(linearLayoutManager);
        this.rvFilters.setHasFixedSize(true);
        bindDataToAdapter();
    }

    private void bindDataToAdapter() {
        final Application application = getApplication();
        new Handler().post(new Runnable() {
            public void run() {
                Bitmap createScaledBitmap = Bitmap.createScaledBitmap(ResultActivity.this.originalBitmap, 640, 640, false);
                ThumbnailsManager.clearThumbs();
               /* for (Filter filter : FilterPack.getFilterPack(ResultActivity.this.getApplicationContext())) {
                    ThumbnailItem thumbnailItem = new ThumbnailItem();
                    thumbnailItem.image = createScaledBitmap;
                    thumbnailItem.filter = filter;
                    ThumbnailsManager.addThumb(thumbnailItem);
                }*/
                ResultActivity.this.thumbs = ThumbnailsManager.processThumbs(application);
                ResultActivity.this.adapter = new ThumbnailsAdapter(ResultActivity.this.thumbs, ResultActivity.this);
                ResultActivity.this.rvFilters.setAdapter(ResultActivity.this.adapter);
                ResultActivity.this.adapter.notifyDataSetChanged();
            }
        });
    }

    public void onThumbnailClick(Filter filter, int i) {
        ImageView imageView = this.ivCroppedResult;
        Bitmap bitmap2 = this.originalBitmap;
        imageView.setImageBitmap(filter.processFilter(Bitmap.createScaledBitmap(bitmap2, bitmap2.getWidth(), this.originalBitmap.getHeight(), false)));
        for (int i2 = 0; i2 < this.thumbs.size(); i2++) {
            if (i2 == i) {
                this.thumbs.get(i).setSelected(true);
            } else {
                this.thumbs.get(i2).setSelected(false);
            }
        }
        this.adapter.notifyDataSetChanged();
    }


    public void deleteEdgeDetectors() {
        deleteRecursive(new File(Environment.getExternalStorageDirectory(), "Document Scanner/Edge Detect"));
    }

    public void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File deleteRecursive : file.listFiles()) {
                deleteRecursive(deleteRecursive);
            }
        }
        file.delete();
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void onBackPressed() {
        closeEditingTools();
        if (this.shouldAllowBack.booleanValue()) {
            super.onBackPressed();
            finish();
        }
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
