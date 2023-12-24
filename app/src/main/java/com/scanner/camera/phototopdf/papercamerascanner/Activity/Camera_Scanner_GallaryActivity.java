package com.scanner.camera.phototopdf.papercamerascanner.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.scanner.camera.phototopdf.papercamerascanner.Adapter.FolderAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.Adapter.SelectedImageAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.Adapter.ViewPagerAdapter;
import com.scanner.camera.phototopdf.papercamerascanner.Fragments.FolderFragment;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.IConstant;
import com.scanner.camera.phototopdf.papercamerascanner.HelperClass.MainUtils;
import com.scanner.camera.phototopdf.papercamerascanner.Model.FolderModel;
import com.scanner.camera.phototopdf.papercamerascanner.OnClickInterface.OnItemClickListener;
import com.scanner.camera.phototopdf.papercamerascanner.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Camera_Scanner_GallaryActivity extends AppCompatActivity implements OnItemClickListener {
    public static List<String> mainImageModelList = new ArrayList();
    public static List<String> savedImageModelList = new ArrayList();
    public static SelectedImageAdapter selectedImageAdapter;
    public static TextView tvCount;
    int countEdges = 0;
    Dialog dialogLoading;

    public ArrayList<FolderModel> fileList = new ArrayList<>();
    FolderAdapter folderAdapter;
    String folder_save_book = ("Document Scanner/Book/Book" + Long.toString(System.currentTimeMillis()));
    String folder_save_business_id = ("Document Scanner/Business Card/Business_Card" + Long.toString(System.currentTimeMillis()));
    String folder_save_doc = ("Document Scanner/Document/Document" + Long.toString(System.currentTimeMillis()));
    String folder_save_id_card = ("Document Scanner/ID Card/ID_CARD" + Long.toString(System.currentTimeMillis()));
    String folder_save_ocr = ("Document Scanner/OCR Files/Ocr_Scan" + Long.toString(System.currentTimeMillis()));
    String folder_save_passport_doc = ("Document Scanner/Photo ID/Photo_Scan" + Long.toString(System.currentTimeMillis()));
    ImageView ivCloseGallary;
    ImageView ivDoneGallary;
    RecyclerView rvDirectoryTab;
    RecyclerView rvSelectedImage;
    String savedFolderName = "";
    ArrayList<String> videoList = new ArrayList<>();
    ViewPager vpGalleryList;


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_gallary);
        Window window = getWindow();
        window.addFlags(Integer.MIN_VALUE);

        window.setStatusBarColor(getResources().getColor(R.color.colorBlack));
        this.rvDirectoryTab = (RecyclerView) findViewById(R.id.rvDirectoryTab);
        this.rvSelectedImage = (RecyclerView) findViewById(R.id.rvSelectedImage);
        this.ivCloseGallary = (ImageView) findViewById(R.id.ivCloseGallary);
        this.ivDoneGallary = (ImageView) findViewById(R.id.ivDoneGallary);
        tvCount = (TextView) findViewById(R.id.tvCount);
        setFolderName();
        new loadGallaryImage().execute();
        for (int i = 0; i < this.fileList.size(); i++) {
            System.out.println(this.fileList.get(i).getName());
        }
        this.vpGalleryList = (ViewPager) findViewById(R.id.vpGalleryList);
        this.ivCloseGallary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Camera_Scanner_GallaryActivity.this.onBackPressed();
            }
        });
        this.ivDoneGallary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (Camera_Scanner_GallaryActivity.mainImageModelList.size() == 0) {
                    Toast makeText = Toast.makeText(Camera_Scanner_GallaryActivity.this.getApplicationContext(), "Please Select Image..!", 1);
                    makeText.setGravity(17, 0, 0);
                    makeText.show();
                    return;
                }
                new importImage().execute();
            }
        });
        this.vpGalleryList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int i) {
            }

            public void onPageScrolled(int i, float f, int i2) {
            }

            public void onPageSelected(int i) {
                Camera_Scanner_GallaryActivity.this.selectCurrentPageTab(i);
                Camera_Scanner_GallaryActivity.this.rvDirectoryTab.smoothScrollToPosition(i);
            }
        });
    }

    public void selectCurrentPageTab(int i) {
        for (int i2 = 0; i2 < this.fileList.size(); i2++) {
            if (i2 == i) {
                this.fileList.get(i2).setSelected(true);
            } else {
                this.fileList.get(i2).setSelected(false);
            }
        }
        this.folderAdapter.notifyDataSetChanged();
    }


    public void addTabs(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (int i = 0; i < this.fileList.size(); i++) {
            viewPagerAdapter.addFrag(new FolderFragment(this.fileList.get(i).getFilePath()), this.fileList.get(i).getName());
        }
        viewPager.setAdapter(viewPagerAdapter);
    }


    public void findVideos(File file, ArrayList<String> arrayList) {
        for (File file2 : file.listFiles()) {
            if (file2.isDirectory()) {
                if (!file2.getName().startsWith(".") && !file2.getName().equals("data") && !file2.getName().toLowerCase().equals("sent")) {
                    findVideos(file2, arrayList);
                }
            } else if (file2.getAbsolutePath().toLowerCase().contains(".png") || file2.getAbsolutePath().toLowerCase().contains(".jpg") || file2.getAbsolutePath().toLowerCase().contains(".jpeg")) {
                arrayList.add(file2.getAbsolutePath());
                if (!addSinglePath(file2.getParent())) {
                    String[] split = file2.getParent().split("/");
                    ArrayList<FolderModel> arrayList2 = this.fileList;
                    boolean z = true;
                    String str = split[split.length - 1];
                    String parent = file2.getParent();
                    if (this.fileList.size() != 0) {
                        z = false;
                    }
                    arrayList2.add(new FolderModel(str, parent, z));
                }
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class loadGallaryImage extends AsyncTask<Void, Void, Void> {
        private loadGallaryImage() {
        }


        public void onPreExecute() {
            super.onPreExecute();
            Camera_Scanner_GallaryActivity.this.showLoading("Loading Images");
        }


        public Void doInBackground(Void... voidArr) {
            Camera_Scanner_GallaryActivity.this.findVideos(new File("/storage/emulated/0"), Camera_Scanner_GallaryActivity.this.videoList);
            return null;
        }

        public void onPostExecute(Void voidR) {
            Camera_Scanner_GallaryActivity cameraScannerGallaryActivity = Camera_Scanner_GallaryActivity.this;
            cameraScannerGallaryActivity.addTabs(cameraScannerGallaryActivity.vpGalleryList);
            Camera_Scanner_GallaryActivity.this.dialogLoading.dismiss();
            Camera_Scanner_GallaryActivity cameraScannerGallaryActivity2 = Camera_Scanner_GallaryActivity.this;
            Camera_Scanner_GallaryActivity cameraScannerGallaryActivity3 = Camera_Scanner_GallaryActivity.this;
            cameraScannerGallaryActivity2.folderAdapter = new FolderAdapter(cameraScannerGallaryActivity3, cameraScannerGallaryActivity3.fileList, Camera_Scanner_GallaryActivity.this);
            Camera_Scanner_GallaryActivity.this.rvDirectoryTab.setLayoutManager(new LinearLayoutManager(Camera_Scanner_GallaryActivity.this.getApplicationContext(), 0, false));
            Camera_Scanner_GallaryActivity.this.rvDirectoryTab.setItemAnimator(new DefaultItemAnimator());
            Camera_Scanner_GallaryActivity.this.rvDirectoryTab.setAdapter(Camera_Scanner_GallaryActivity.this.folderAdapter);
            Camera_Scanner_GallaryActivity.selectedImageAdapter = new SelectedImageAdapter(Camera_Scanner_GallaryActivity.this, Camera_Scanner_GallaryActivity.mainImageModelList, Camera_Scanner_GallaryActivity.this);
            Camera_Scanner_GallaryActivity.this.rvSelectedImage.setLayoutManager(new LinearLayoutManager(Camera_Scanner_GallaryActivity.this.getApplicationContext(), 0, false));
            Camera_Scanner_GallaryActivity.this.rvSelectedImage.setItemAnimator(new DefaultItemAnimator());
            Camera_Scanner_GallaryActivity.this.rvSelectedImage.setAdapter(Camera_Scanner_GallaryActivity.selectedImageAdapter);
        }



    }

    public boolean addSinglePath(String str) {
        boolean z = false;
        for (int i = 0; i < this.fileList.size(); i++) {
            if (str.equals(this.fileList.get(i).getFilePath())) {
                z = true;
            }
        }
        return z;
    }

    public void OnClick(View view, int i, String str) {
        int id = view.getId();
        if (id == R.id.btnRemove) {
            FolderFragment.selectedOrNot(mainImageModelList.get(i), false);
        } else if (id == R.id.llView) {
            this.vpGalleryList.setCurrentItem(i);
            selectCurrentPageTab(i);
        }
    }

    private class importImage extends AsyncTask<Void, Void, Void> {
        private importImage() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            Camera_Scanner_GallaryActivity.this.showLoading("Importing Images");
        }


        public Void doInBackground(Void... voidArr) {
            MainUtils.isFolderCreated();
            String file = Environment.getExternalStorageDirectory().toString();
            File file2 = new File(file + "/" + Camera_Scanner_GallaryActivity.this.savedFolderName);
            file2.mkdirs();
            for (int i = 0; i < Camera_Scanner_GallaryActivity.mainImageModelList.size(); i++) {
                File file3 = new File(file2, "DocScan_"+ Long.toString(System.currentTimeMillis())  + i + ".png");
                if (file3.exists()) {
                    file3.delete();
                }
                try {
                    File file4 = new File(Camera_Scanner_GallaryActivity.mainImageModelList.get(i));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap decodeStream = BitmapFactory.decodeStream(new FileInputStream(file4), (Rect) null, options);
                    FileOutputStream fileOutputStream = new FileOutputStream(file3);
                    Camera_Scanner_GallaryActivity.this.createContrast(decodeStream, 50.0d).compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                    fileOutputStream.flush();
                    fileOutputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Camera_Scanner_GallaryActivity.savedImageModelList.add(file3.getPath());
            }
            return null;
        }


        public void onPostExecute(Void voidR) {
            Camera_Scanner_GallaryActivity.this.dialogLoading.dismiss();
            MainUtils.setScanType(Camera_Scanner_GallaryActivity.this, "Document");
            MainUtils.setFromEdit(Camera_Scanner_GallaryActivity.this, false);
            MainUtils.setFromGallaryEdge(Camera_Scanner_GallaryActivity.this, true);
            Camera_Scanner_GallaryActivity.mainImageModelList.clear();
            Intent intent = new Intent(Camera_Scanner_GallaryActivity.this, Camera_Scanner_EdgeDetectionActivity.class);
            intent.putExtra("imageUrl", Camera_Scanner_GallaryActivity.savedImageModelList.get(0));
            Camera_Scanner_GallaryActivity.this.startActivityForResult(intent, IConstant.RESULT_GALLARY_EDGE_OK);
        }
    }


    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == IConstant.RESULT_GALLARY_EDGE_OK) {
            int i3 = this.countEdges + 1;
            this.countEdges = i3;
            if (i3 == savedImageModelList.size()) {
                MainUtils.setFromGallaryEdge(this, false);
                Intent intent2 = new Intent(this, Camera_Scanner_BookScanPagesActivity.class);
                intent2.putExtra("folderName", this.savedFolderName);
                startActivity(intent2);
                finish();
                savedImageModelList.clear();
                return;
            }
            Intent intent3 = new Intent(this, Camera_Scanner_EdgeDetectionActivity.class);
            intent3.putExtra("imageUrl", savedImageModelList.get(this.countEdges));
            startActivityForResult(intent3, IConstant.RESULT_GALLARY_EDGE_OK);
        } else if (i2 == IConstant.RESULT_GALLARY_EDGE_CANCEL) {
            MainUtils.setFromGallaryEdge(this, false);
            Intent intent4 = new Intent(this, Camera_Scanner_BookScanPagesActivity.class);
            intent4.putExtra("folderName", this.savedFolderName);
            startActivity(intent4);
            finish();
            savedImageModelList.clear();
        }
    }

    public Bitmap createContrast(Bitmap bitmap, double d) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap createBitmap = Bitmap.createBitmap(width, height, bitmap.getConfig());
        double pow = Math.pow((d + 100.0d) / 100.0d, 2.0d);
        for (int i = 0; i < width; i++) {
            int i2 = 0;
            while (i2 < height) {
                int pixel = bitmap.getPixel(i, i2);
                int alpha = Color.alpha(pixel);
                int red = (int) (((((((double) Color.red(pixel)) / 255.0d) - 0.5d) * pow) + 0.5d) * 255.0d);
                int i3 = 255;
                if (red < 0) {
                    red = 0;
                } else if (red > 255) {
                    red = 255;
                }
                int i4 = width;
                int i5 = height;
                int red2 = (int) (((((((double) Color.red(pixel)) / 255.0d) - 0.5d) * pow) + 0.5d) * 255.0d);
                if (red2 < 0) {
                    red2 = 0;
                } else if (red2 > 255) {
                    red2 = 255;
                }
                int red3 = (int) (((((((double) Color.red(pixel)) / 255.0d) - 0.5d) * pow) + 0.5d) * 255.0d);
                if (red3 < 0) {
                    i3 = 0;
                } else if (red3 <= 255) {
                    i3 = red3;
                }
                createBitmap.setPixel(i, i2, Color.argb(alpha, red, red2, i3));
                i2++;
                width = i4;
                height = i5;
            }
            int i6 = width;
            int i7 = height;
        }
        return createBitmap;
    }

    private void setFolderName() {
        if (MainUtils.getSaveFolder(this).equals("Passport")) {
            File file = new File(Environment.getExternalStorageDirectory(), this.folder_save_passport_doc);
            if (!file.exists()) {
                file.mkdir();
            }
            this.savedFolderName = this.folder_save_passport_doc;
        } else if (MainUtils.getSaveFolder(this).equals("Book")) {
            File file2 = new File(Environment.getExternalStorageDirectory(), this.folder_save_book);
            if (!file2.exists()) {
                file2.mkdir();
            }
            this.savedFolderName = this.folder_save_book;
        } else if (MainUtils.getSaveFolder(this).equals("Document")) {
            File file3 = new File(Environment.getExternalStorageDirectory(), this.folder_save_doc);
            if (!file3.exists()) {
                file3.mkdir();
            }
            this.savedFolderName = this.folder_save_doc;
        } else if (MainUtils.getSaveFolder(this).equals("IDCard")) {
            File file4 = new File(Environment.getExternalStorageDirectory(), this.folder_save_id_card);
            if (!file4.exists()) {
                file4.mkdir();
            }
            this.savedFolderName = this.folder_save_id_card;
        } else if (MainUtils.getSaveFolder(this).equals("BusinessCard")) {
            File file5 = new File(Environment.getExternalStorageDirectory(), this.folder_save_business_id);
            if (!file5.exists()) {
                file5.mkdir();
            }
            this.savedFolderName = this.folder_save_business_id;
        } else if (MainUtils.getSaveFolder(this).equals("OCRText")) {
            File file6 = new File(Environment.getExternalStorageDirectory(), this.folder_save_ocr);
            if (!file6.exists()) {
                file6.mkdir();
            }
            this.savedFolderName = this.folder_save_ocr;
        }
    }


    public void showLoading(String str) {
        Dialog dialog = new Dialog(this);
        this.dialogLoading = dialog;
        dialog.setContentView(R.layout.loading_dialog);
        this.dialogLoading.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        this.dialogLoading.setCancelable(false);
        ((AppCompatTextView) this.dialogLoading.findViewById(R.id.tvLoadingText)).setText(str);
        this.dialogLoading.show();
    }

    public void onBackPressed() {
        super.onBackPressed();
        mainImageModelList.clear();
        finish();
    }
}
