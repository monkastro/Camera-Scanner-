package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import androidx.fragment.app.DialogFragment;

import com.scanner.camera.phototopdf.papercamerascanner.R;


public class ProgressDialogFragment extends DialogFragment {
    public static final String TAG = ProgressDialogFragment.class.getSimpleName();

    public static ProgressDialogFragment getInstance() {
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        progressDialogFragment.setArguments(new Bundle());
        return progressDialogFragment;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_progress_dialog, (ViewGroup) null, false);
        ((ProgressBar) inflate.findViewById(R.id.progress)).getIndeterminateDrawable().setColorFilter(getContext().getResources().getColor(R.color.colorYellowMain), PorterDuff.Mode.SRC_IN);
        return inflate;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.getWindow().requestFeature(1);
        onCreateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        onCreateDialog.setCancelable(false);
        onCreateDialog.setCanceledOnTouchOutside(false);
        onCreateDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return i == 4 || i == 84;
            }
        });
        return onCreateDialog;
    }
}
