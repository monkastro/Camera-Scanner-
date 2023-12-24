package com.scanner.camera.phototopdf.papercamerascanner.SimpleCropView;

import androidx.fragment.app.Fragment;
import java.lang.ref.WeakReference;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.PermissionUtils;

final class BasicFragmentPermissionsDispatcher {
    /* access modifiers changed from: private */
    public static final String[] PERMISSION_CROPIMAGE = {"android.permission.WRITE_EXTERNAL_STORAGE"};
    /* access modifiers changed from: private */
    public static final String[] PERMISSION_PICKIMAGE = {"android.permission.READ_EXTERNAL_STORAGE"};
    private static final int REQUEST_CROPIMAGE = 0;
    private static final int REQUEST_PICKIMAGE = 1;

    private BasicFragmentPermissionsDispatcher() {
    }

    static void pickImageWithPermissionCheck(BasicFragment basicFragment) {
        if (PermissionUtils.hasSelfPermissions(basicFragment.requireActivity(), PERMISSION_PICKIMAGE)) {
            basicFragment.pickImage();
        } else if (PermissionUtils.shouldShowRequestPermissionRationale((Fragment) basicFragment, PERMISSION_PICKIMAGE)) {
            basicFragment.showRationaleForPick(new BasicFragmentPickImagePermissionRequest(basicFragment));
        } else {
            basicFragment.requestPermissions(PERMISSION_PICKIMAGE, 1);
        }
    }

    static void cropImageWithPermissionCheck(BasicFragment basicFragment) {
        if (PermissionUtils.hasSelfPermissions(basicFragment.requireActivity(), PERMISSION_CROPIMAGE)) {
            basicFragment.cropImage();
        } else if (PermissionUtils.shouldShowRequestPermissionRationale((Fragment) basicFragment, PERMISSION_CROPIMAGE)) {
            basicFragment.showRationaleForCrop(new BasicFragmentCropImagePermissionRequest(basicFragment));
        } else {
            basicFragment.requestPermissions(PERMISSION_CROPIMAGE, 0);
        }
    }

    static void onRequestPermissionsResult(BasicFragment basicFragment, int i, int[] iArr) {
        if (i != 0) {
            if (i == 1 && PermissionUtils.verifyPermissions(iArr)) {
                basicFragment.pickImage();
            }
        } else if (PermissionUtils.verifyPermissions(iArr)) {
            basicFragment.cropImage();
        }
    }

    private static final class BasicFragmentPickImagePermissionRequest implements PermissionRequest {
        private final WeakReference<BasicFragment> weakTarget;

        public void cancel() {
        }

        private BasicFragmentPickImagePermissionRequest(BasicFragment basicFragment) {
            this.weakTarget = new WeakReference<>(basicFragment);
        }

        public void proceed() {
            BasicFragment basicFragment = (BasicFragment) this.weakTarget.get();
            if (basicFragment != null) {
                basicFragment.requestPermissions(BasicFragmentPermissionsDispatcher.PERMISSION_PICKIMAGE, 1);
            }
        }
    }

    private static final class BasicFragmentCropImagePermissionRequest implements PermissionRequest {
        private final WeakReference<BasicFragment> weakTarget;

        public void cancel() {
        }

        private BasicFragmentCropImagePermissionRequest(BasicFragment basicFragment) {
            this.weakTarget = new WeakReference<>(basicFragment);
        }

        public void proceed() {
            BasicFragment basicFragment = (BasicFragment) this.weakTarget.get();
            if (basicFragment != null) {
                basicFragment.requestPermissions(BasicFragmentPermissionsDispatcher.PERMISSION_CROPIMAGE, 0);
            }
        }
    }
}
