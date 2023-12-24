package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.scanner.camera.phototopdf.papercamerascanner.Activity.Camera_Scanner_MainActivity;
import com.scanner.camera.phototopdf.papercamerascanner.R;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.callback.CropCallback;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.callback.LoadCallback;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.callback.SaveCallback;
import com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView.util.Logger;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import permissions.dispatcher.PermissionRequest;

public class BasicFragment extends Fragment {
    private static final String KEY_FRAME_RECT = "FrameRect";
    private static final String KEY_SOURCE_URI = "SourceUri";
    private static final String PROGRESS_DIALOG = "ProgressDialog";
    private static final int REQUEST_PICK_IMAGE = 10011;
    private static final int REQUEST_SAF_PICK_IMAGE = 10012;
    private static final String TAG = BasicFragment.class.getSimpleName();
    private final View.OnClickListener btnListener = new View.OnClickListener() {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.button16_9:
                    BasicFragment.this.mCropView.setCropMode(CropImageView.CropMode.RATIO_16_9);
                    BasicFragment.this.manageSelectedIcon(R.id.button16_9);
                    return;
                case R.id.button1_1:
                    BasicFragment.this.mCropView.setCropMode(CropImageView.CropMode.SQUARE);
                    BasicFragment.this.manageSelectedIcon(R.id.button1_1);
                    return;
                case R.id.button3_4:
                    BasicFragment.this.mCropView.setCropMode(CropImageView.CropMode.RATIO_3_4);
                    BasicFragment.this.manageSelectedIcon(R.id.button3_4);
                    return;
                case R.id.button4_3:
                    BasicFragment.this.mCropView.setCropMode(CropImageView.CropMode.RATIO_4_3);
                    BasicFragment.this.manageSelectedIcon(R.id.button4_3);
                    return;
                case R.id.button9_16:
                    BasicFragment.this.mCropView.setCropMode(CropImageView.CropMode.RATIO_9_16);
                    BasicFragment.this.manageSelectedIcon(R.id.button9_16);
                    return;
                case R.id.buttonCircle:
                    BasicFragment.this.mCropView.setCropMode(CropImageView.CropMode.CIRCLE);
                    BasicFragment.this.manageSelectedIcon(R.id.buttonCircle);
                    return;
                case R.id.buttonCustom:
                    BasicFragment.this.mCropView.setCustomRatio(7, 5);
                    BasicFragment.this.manageSelectedIcon(R.id.buttonCustom);
                    return;
                case R.id.buttonDone:
                    BasicFragmentPermissionsDispatcher.cropImageWithPermissionCheck(BasicFragment.this);
                    return;
                case R.id.buttonFitImage:
                    BasicFragment.this.mCropView.setCropMode(CropImageView.CropMode.FIT_IMAGE);
                    BasicFragment.this.manageSelectedIcon(R.id.buttonFitImage);
                    return;
                case R.id.buttonFree:
                    BasicFragment.this.mCropView.setCropMode(CropImageView.CropMode.FREE);
                    BasicFragment.this.manageSelectedIcon(R.id.buttonFree);
                    return;
                case R.id.buttonRotateRight:
                    BasicFragment.this.mCropView.rotateImage(CropImageView.RotateDegrees.ROTATE_90D);
                    BasicFragment.this.manageSelectedIcon(R.id.buttonRotateRight);
                    return;
                default:
                    return;
            }
        }
    };
    private ImageView iv16_9;
    private ImageView iv1_1;
    private ImageView iv3_4;
    private ImageView iv4_3;
    private ImageView iv9_16;
    private ImageView ivCircle;
    private ImageView ivCustom;
    private ImageView ivFitImage;
    private ImageView ivFree;
    private ImageView ivRotateImage;
    /* access modifiers changed from: private */
    public Bitmap.CompressFormat mCompressFormat = Bitmap.CompressFormat.JPEG;
    private final CropCallback mCropCallback = new CropCallback() {
        public void onError(Throwable th) {
        }

        public void onSuccess(Bitmap bitmap) {
            BasicFragment.this.mCropView.save(bitmap).compressFormat(BasicFragment.this.mCompressFormat).execute(BasicFragment.this.createSaveUri(), BasicFragment.this.mSaveCallback);
        }
    };
    /* access modifiers changed from: private */
    public CropImageView mCropView;
    private RectF mFrameRect = null;
    private final LoadCallback mLoadCallback = new LoadCallback() {
        public void onError(Throwable th) {
        }

        public void onSuccess() {
        }
    };
    /* access modifiers changed from: private */
    public final SaveCallback mSaveCallback = new SaveCallback() {
        public void onSuccess(Uri uri) {
            BasicFragment.this.dismissProgress();
            ((BasicActivity) BasicFragment.this.requireActivity()).startResultActivity(uri);
        }

        public void onError(Throwable th) {
            BasicFragment.this.dismissProgress();
        }
    };
    private Uri mSourceUri = null;
    private TextView tv16_9;
    private TextView tv1_1;
    private TextView tv3_4;
    private TextView tv4_3;
    private TextView tv9_16;
    private TextView tvCircle;
    private TextView tvCustom;
    private TextView tvFixImage;
    private TextView tvFreeImage;
    private TextView tvRotateImage;

    public static BasicFragment newInstance() {
        BasicFragment basicFragment = new BasicFragment();
        basicFragment.setArguments(new Bundle());
        return basicFragment;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.fragment_basic, (ViewGroup) null, false);
    }

    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        bindViews(view);
        manageSelectedIcon(R.id.buttonFitImage);
        this.mCropView.setDebug(true);
        if (bundle != null) {
            this.mFrameRect = (RectF) bundle.getParcelable(KEY_FRAME_RECT);
            this.mSourceUri = (Uri) bundle.getParcelable(KEY_SOURCE_URI);
        }
        this.mCropView.load(Uri.parse(BasicActivity.selectedImageUrl)).initialFrameRect(this.mFrameRect).useThumbnail(true).execute(this.mLoadCallback);
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable(KEY_FRAME_RECT, this.mCropView.getActualCropRect());
        bundle.putParcelable(KEY_SOURCE_URI, this.mCropView.getSourceUri());
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        BasicFragmentPermissionsDispatcher.onRequestPermissionsResult(this, i, iArr);
    }

    private void bindViews(View view) {
        this.mCropView = (CropImageView) view.findViewById(R.id.cropImageView);
        view.findViewById(R.id.buttonDone).setOnClickListener(this.btnListener);
        view.findViewById(R.id.ivBack).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                BasicFragment.this.startActivity(new Intent(BasicFragment.this.getActivity(), Camera_Scanner_MainActivity.class));
            }
        });
        this.tv1_1 = (TextView) view.findViewById(R.id.tv1_1);
        this.tv3_4 = (TextView) view.findViewById(R.id.tv3_4);
        this.tv4_3 = (TextView) view.findViewById(R.id.tv4_3);
        this.tv9_16 = (TextView) view.findViewById(R.id.tv9_16);
        this.tv16_9 = (TextView) view.findViewById(R.id.tv16_9);
        this.tvCustom = (TextView) view.findViewById(R.id.tvCustom);
        this.tvCircle = (TextView) view.findViewById(R.id.tvCircle);
        this.iv1_1 = (ImageView) view.findViewById(R.id.iv1_1);
        this.iv3_4 = (ImageView) view.findViewById(R.id.iv3_4);
        this.iv4_3 = (ImageView) view.findViewById(R.id.iv4_3);
        this.iv9_16 = (ImageView) view.findViewById(R.id.iv9_16);
        this.iv16_9 = (ImageView) view.findViewById(R.id.iv16_9);
        this.ivCustom = (ImageView) view.findViewById(R.id.ivCustom);
        this.ivCircle = (ImageView) view.findViewById(R.id.ivCircle);
        this.ivFitImage = (ImageView) view.findViewById(R.id.ivFixImage);
        this.ivFree = (ImageView) view.findViewById(R.id.ivFreeImage);
        this.ivRotateImage = (ImageView) view.findViewById(R.id.ivRotateImage);
        this.tvFreeImage = (TextView) view.findViewById(R.id.tvFreeImage);
        this.tvFixImage = (TextView) view.findViewById(R.id.tvFixImage);
        this.tvRotateImage = (TextView) view.findViewById(R.id.tvRotateImage);
        view.findViewById(R.id.buttonFitImage).setOnClickListener(this.btnListener);
        view.findViewById(R.id.button1_1).setOnClickListener(this.btnListener);
        view.findViewById(R.id.button3_4).setOnClickListener(this.btnListener);
        view.findViewById(R.id.button4_3).setOnClickListener(this.btnListener);
        view.findViewById(R.id.button9_16).setOnClickListener(this.btnListener);
        view.findViewById(R.id.button16_9).setOnClickListener(this.btnListener);
        view.findViewById(R.id.buttonFree).setOnClickListener(this.btnListener);
        view.findViewById(R.id.buttonRotateRight).setOnClickListener(this.btnListener);
        view.findViewById(R.id.buttonCustom).setOnClickListener(this.btnListener);
        view.findViewById(R.id.buttonCircle).setOnClickListener(this.btnListener);
    }

    public void pickImage() {
        if (Build.VERSION.SDK_INT < 19) {
            startActivityForResult(new Intent("android.intent.action.GET_CONTENT").setType("image/*"), REQUEST_PICK_IMAGE);
            return;
        }
        Intent intent = new Intent("android.intent.action.OPEN_DOCUMENT");
        intent.addCategory("android.intent.category.OPENABLE");
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SAF_PICK_IMAGE);
    }

    public void cropImage() {
        showProgress();
        this.mCropView.crop(this.mSourceUri).execute(this.mCropCallback);
    }

    public void showRationaleForPick(PermissionRequest permissionRequest) {
        showRationaleDialog(R.string.permission_pick_rationale, permissionRequest);
    }

    public void showRationaleForCrop(PermissionRequest permissionRequest) {
        showRationaleDialog(R.string.permission_crop_rationale, permissionRequest);
    }

    public void showProgress() {
        getFragmentManager().beginTransaction().add((Fragment) ProgressDialogFragment.getInstance(), PROGRESS_DIALOG).commitAllowingStateLoss();
    }

    public void dismissProgress() {
        FragmentManager fragmentManager;
        ProgressDialogFragment progressDialogFragment;
        if (isResumed() && (fragmentManager = getFragmentManager()) != null && (progressDialogFragment = (ProgressDialogFragment) fragmentManager.findFragmentByTag(PROGRESS_DIALOG)) != null) {
            getFragmentManager().beginTransaction().remove(progressDialogFragment).commitAllowingStateLoss();
        }
    }

    public Uri createSaveUri() {
        return createNewUri(getContext(), this.mCompressFormat);
    }

    public static String getDirPath() {
        File file;
        File externalStorageDirectory = Environment.getExternalStorageDirectory();
        if (externalStorageDirectory.canWrite()) {
            file = new File(externalStorageDirectory.getPath() + "/TextOnPhoto/tempPhoto");
        } else {
            file = null;
        }
        if (file != null) {
            if (!file.exists()) {
                file.mkdirs();
            }
            if (file.canWrite()) {
                return file.getPath();
            }
        }
        return "";
    }

    public static Uri getUriFromDrawableResId(Context context, int i) {
        return Uri.parse("android.resource" + "://" + context.getResources().getResourcePackageName(i) + "/" + context.getResources().getResourceTypeName(i) + "/" + context.getResources().getResourceEntryName(i));
    }

    public static Uri createNewUri(Context context, Bitmap.CompressFormat compressFormat) {
        long currentTimeMillis = System.currentTimeMillis();
        String format = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date(currentTimeMillis));
        String str = "TextOnPhoto_" + format + "." + getMimeType(compressFormat);
        String str2 = getDirPath() + "/" + str;
        File file = new File(str2);
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", format);
        contentValues.put("_display_name", str);
        contentValues.put("mime_type", "image/" + getMimeType(compressFormat));
        contentValues.put("_data", str2);
        long j = currentTimeMillis / 1000;
        contentValues.put("date_added", Long.valueOf(j));
        contentValues.put("date_modified", Long.valueOf(j));
        if (file.exists()) {
            contentValues.put("_size", Long.valueOf(file.length()));
        }
        Uri insert = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Logger.m90i("SaveUri = " + insert);
        return insert;
    }



    public static String getMimeType(Bitmap.CompressFormat compressFormat) {
        switch (compressFormat) {
            case JPEG:
                return "jpeg";
            case PNG:
                return "png";
        }
        return "png";}

    public static Uri createTempUri(Context context) {
        return Uri.fromFile(new File(context.getCacheDir(), "cropped"));
    }

    private void showRationaleDialog(int i, final PermissionRequest permissionRequest) {
        new AlertDialog.Builder(getActivity()).setPositiveButton((int) R.string.button_allow, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                permissionRequest.proceed();
            }
        }).setNegativeButton((int) R.string.button_deny, (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                permissionRequest.cancel();
            }
        }).setCancelable(false).setMessage(i).show();
    }

    /* access modifiers changed from: package-private */
    public void manageSelectedIcon(int i) {
        if (i == R.id.buttonFitImage) {
            this.ivFitImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.selectedButtonColor));
            this.tvFixImage.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.ivFree.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvFreeImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivRotateImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvRotateImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv1_1.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv3_4.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv4_3.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv9_16.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv16_9.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCustom.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCircle.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.iv1_1.setImageDrawable(getResources().getDrawable(R.drawable.a1_1_icon));
            this.iv4_3.setImageDrawable(getResources().getDrawable(R.drawable.a4_3_icon));
            this.iv3_4.setImageDrawable(getResources().getDrawable(R.drawable.a3_4_icon));
            this.iv9_16.setImageDrawable(getResources().getDrawable(R.drawable.a9_6_icon));
            this.iv16_9.setImageDrawable(getResources().getDrawable(R.drawable.a16_9_icon));
            this.ivCustom.setImageDrawable(getResources().getDrawable(R.drawable.a7_5_icon));
            this.ivCircle.setImageDrawable(getResources().getDrawable(R.drawable.circle_icon));
        } else if (i == R.id.buttonFree) {
            this.ivFree.setColorFilter(ContextCompat.getColor(getContext(), R.color.selectedButtonColor));
            this.tvFreeImage.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.ivFitImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvFixImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivRotateImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvRotateImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv1_1.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv3_4.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv4_3.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv9_16.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv16_9.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCustom.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCircle.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.iv1_1.setImageDrawable(getResources().getDrawable(R.drawable.a1_1_icon));
            this.iv4_3.setImageDrawable(getResources().getDrawable(R.drawable.a4_3_icon));
            this.iv3_4.setImageDrawable(getResources().getDrawable(R.drawable.a3_4_icon));
            this.iv9_16.setImageDrawable(getResources().getDrawable(R.drawable.a9_6_icon));
            this.iv16_9.setImageDrawable(getResources().getDrawable(R.drawable.a16_9_icon));
            this.ivCustom.setImageDrawable(getResources().getDrawable(R.drawable.a7_5_icon));
            this.ivCircle.setImageDrawable(getResources().getDrawable(R.drawable.circle_icon));
        } else if (i == R.id.buttonRotateRight) {
            this.ivRotateImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.selectedButtonColor));
            this.tvRotateImage.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.ivFree.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvFreeImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivFitImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvFixImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv1_1.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv3_4.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv4_3.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv9_16.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv16_9.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCustom.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCircle.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.iv1_1.setImageDrawable(getResources().getDrawable(R.drawable.a1_1_icon));
            this.iv4_3.setImageDrawable(getResources().getDrawable(R.drawable.a4_3_icon));
            this.iv3_4.setImageDrawable(getResources().getDrawable(R.drawable.a3_4_icon));
            this.iv9_16.setImageDrawable(getResources().getDrawable(R.drawable.a9_6_icon));
            this.iv16_9.setImageDrawable(getResources().getDrawable(R.drawable.a16_9_icon));
            this.ivCustom.setImageDrawable(getResources().getDrawable(R.drawable.a7_5_icon));
            this.ivCircle.setImageDrawable(getResources().getDrawable(R.drawable.circle_icon));
        } else if (i == R.id.button1_1) {
            this.tv1_1.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.tv3_4.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv4_3.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv9_16.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv16_9.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCustom.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCircle.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.iv1_1.setImageDrawable(getResources().getDrawable(R.drawable.as1_1_icon));
            this.iv4_3.setImageDrawable(getResources().getDrawable(R.drawable.a4_3_icon));
            this.iv3_4.setImageDrawable(getResources().getDrawable(R.drawable.a3_4_icon));
            this.iv9_16.setImageDrawable(getResources().getDrawable(R.drawable.a9_6_icon));
            this.iv16_9.setImageDrawable(getResources().getDrawable(R.drawable.a16_9_icon));
            this.ivCustom.setImageDrawable(getResources().getDrawable(R.drawable.a7_5_icon));
            this.ivCircle.setImageDrawable(getResources().getDrawable(R.drawable.circle_icon));
            this.ivFitImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.ivFree.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvFreeImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvFixImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivRotateImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvRotateImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.button3_4) {
            this.tv3_4.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.tv1_1.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv4_3.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv9_16.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv16_9.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCustom.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCircle.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.iv1_1.setImageDrawable(getResources().getDrawable(R.drawable.a1_1_icon));
            this.iv3_4.setImageDrawable(getResources().getDrawable(R.drawable.as3_4_icon));
            this.iv4_3.setImageDrawable(getResources().getDrawable(R.drawable.a4_3_icon));
            this.iv9_16.setImageDrawable(getResources().getDrawable(R.drawable.a9_6_icon));
            this.iv16_9.setImageDrawable(getResources().getDrawable(R.drawable.a16_9_icon));
            this.ivCustom.setImageDrawable(getResources().getDrawable(R.drawable.a7_5_icon));
            this.ivCircle.setImageDrawable(getResources().getDrawable(R.drawable.circle_icon));
            this.ivFitImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.ivFree.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvFreeImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvFixImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivRotateImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvRotateImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.button4_3) {
            this.tv4_3.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.tv1_1.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv3_4.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv9_16.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv16_9.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCustom.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCircle.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.iv1_1.setImageDrawable(getResources().getDrawable(R.drawable.a1_1_icon));
            this.iv3_4.setImageDrawable(getResources().getDrawable(R.drawable.a3_4_icon));
            this.iv4_3.setImageDrawable(getResources().getDrawable(R.drawable.as4_3_icon));
            this.iv9_16.setImageDrawable(getResources().getDrawable(R.drawable.a9_6_icon));
            this.iv16_9.setImageDrawable(getResources().getDrawable(R.drawable.a16_9_icon));
            this.ivCustom.setImageDrawable(getResources().getDrawable(R.drawable.a7_5_icon));
            this.ivCircle.setImageDrawable(getResources().getDrawable(R.drawable.circle_icon));
            this.ivFitImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.ivFree.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvFreeImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvFixImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivRotateImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvRotateImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.button9_16) {
            this.tv9_16.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.tv1_1.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv3_4.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv4_3.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv16_9.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCustom.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCircle.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.iv1_1.setImageDrawable(getResources().getDrawable(R.drawable.a1_1_icon));
            this.iv4_3.setImageDrawable(getResources().getDrawable(R.drawable.a4_3_icon));
            this.iv3_4.setImageDrawable(getResources().getDrawable(R.drawable.a3_4_icon));
            this.iv9_16.setImageDrawable(getResources().getDrawable(R.drawable.as9_6_icon));
            this.iv16_9.setImageDrawable(getResources().getDrawable(R.drawable.a16_9_icon));
            this.ivCustom.setImageDrawable(getResources().getDrawable(R.drawable.a7_5_icon));
            this.ivCircle.setImageDrawable(getResources().getDrawable(R.drawable.circle_icon));
            this.ivFitImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.ivFree.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvFreeImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvFixImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivRotateImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvRotateImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.button16_9) {
            this.tv16_9.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.tv1_1.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv3_4.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv4_3.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv9_16.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCustom.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCircle.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.iv1_1.setImageDrawable(getResources().getDrawable(R.drawable.a1_1_icon));
            this.iv4_3.setImageDrawable(getResources().getDrawable(R.drawable.a4_3_icon));
            this.iv3_4.setImageDrawable(getResources().getDrawable(R.drawable.a3_4_icon));
            this.iv9_16.setImageDrawable(getResources().getDrawable(R.drawable.a9_6_icon));
            this.iv16_9.setImageDrawable(getResources().getDrawable(R.drawable.as16_9_icon));
            this.ivCustom.setImageDrawable(getResources().getDrawable(R.drawable.a7_5_icon));
            this.ivCircle.setImageDrawable(getResources().getDrawable(R.drawable.circle_icon));
            this.ivFitImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.ivFree.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvFreeImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvFixImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivRotateImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvRotateImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.buttonCustom) {
            this.tvCustom.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.tv1_1.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv3_4.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv4_3.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv9_16.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv16_9.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCircle.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.iv1_1.setImageDrawable(getResources().getDrawable(R.drawable.a1_1_icon));
            this.iv4_3.setImageDrawable(getResources().getDrawable(R.drawable.a4_3_icon));
            this.iv3_4.setImageDrawable(getResources().getDrawable(R.drawable.a3_4_icon));
            this.iv9_16.setImageDrawable(getResources().getDrawable(R.drawable.a9_6_icon));
            this.iv16_9.setImageDrawable(getResources().getDrawable(R.drawable.a16_9_icon));
            this.ivCustom.setImageDrawable(getResources().getDrawable(R.drawable.as7_5_icon));
            this.ivCircle.setImageDrawable(getResources().getDrawable(R.drawable.circle_icon));
            this.ivFitImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.ivFree.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvFreeImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvFixImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivRotateImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvRotateImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        } else if (i == R.id.buttonCircle) {
            this.tvCircle.setTextColor(getResources().getColor(R.color.selectedButtonColor));
            this.tv1_1.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv3_4.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv4_3.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv9_16.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tv16_9.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvCustom.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.iv1_1.setImageDrawable(getResources().getDrawable(R.drawable.a1_1_icon));
            this.iv4_3.setImageDrawable(getResources().getDrawable(R.drawable.a4_3_icon));
            this.iv3_4.setImageDrawable(getResources().getDrawable(R.drawable.a3_4_icon));
            this.iv9_16.setImageDrawable(getResources().getDrawable(R.drawable.a9_6_icon));
            this.iv16_9.setImageDrawable(getResources().getDrawable(R.drawable.a16_9_icon));
            this.ivCustom.setImageDrawable(getResources().getDrawable(R.drawable.a7_5_icon));
            this.ivCircle.setImageDrawable(getResources().getDrawable(R.drawable.circle_icon_s));
            this.ivFitImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.ivFree.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvFreeImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.tvFixImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
            this.ivRotateImage.setColorFilter(ContextCompat.getColor(getContext(), R.color.unselectedButtonColor));
            this.tvRotateImage.setTextColor(getResources().getColor(R.color.unselectedButtonColor));
        }
    }
}
