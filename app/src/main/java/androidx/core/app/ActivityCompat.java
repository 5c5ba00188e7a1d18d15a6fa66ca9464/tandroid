package androidx.core.app;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.core.content.ContextCompat;
import java.util.Arrays;
/* loaded from: classes.dex */
public class ActivityCompat extends ContextCompat {
    private static PermissionCompatDelegate sDelegate;

    /* loaded from: classes.dex */
    public interface OnRequestPermissionsResultCallback {
        void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults);
    }

    /* loaded from: classes.dex */
    public interface PermissionCompatDelegate {
        boolean onActivityResult(Activity activity, int requestCode, int resultCode, Intent data);

        boolean requestPermissions(Activity activity, String[] permissions, int requestCode);
    }

    /* loaded from: classes.dex */
    public interface RequestPermissionsRequestCodeValidator {
        void validateRequestPermissionsRequestCode(int requestCode);
    }

    public static PermissionCompatDelegate getPermissionCompatDelegate() {
        return sDelegate;
    }

    public static void startActivityForResult(Activity activity, Intent intent, int requestCode, Bundle options) {
        if (Build.VERSION.SDK_INT >= 16) {
            activity.startActivityForResult(intent, requestCode, options);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }

    public static void requestPermissions(final Activity activity, final String[] permissions, final int requestCode) {
        PermissionCompatDelegate permissionCompatDelegate = sDelegate;
        if (permissionCompatDelegate == null || !permissionCompatDelegate.requestPermissions(activity, permissions, requestCode)) {
            for (String str : permissions) {
                if (TextUtils.isEmpty(str)) {
                    throw new IllegalArgumentException("Permission request for permissions " + Arrays.toString(permissions) + " must not contain null or empty values");
                }
            }
            if (Build.VERSION.SDK_INT >= 23) {
                if (activity instanceof RequestPermissionsRequestCodeValidator) {
                    ((RequestPermissionsRequestCodeValidator) activity).validateRequestPermissionsRequestCode(requestCode);
                }
                activity.requestPermissions(permissions, requestCode);
            } else if (activity instanceof OnRequestPermissionsResultCallback) {
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: androidx.core.app.ActivityCompat.1
                    @Override // java.lang.Runnable
                    public void run() {
                        int[] iArr = new int[permissions.length];
                        PackageManager packageManager = activity.getPackageManager();
                        String packageName = activity.getPackageName();
                        int length = permissions.length;
                        for (int i = 0; i < length; i++) {
                            iArr[i] = packageManager.checkPermission(permissions[i], packageName);
                        }
                        ((OnRequestPermissionsResultCallback) activity).onRequestPermissionsResult(requestCode, permissions, iArr);
                    }
                });
            }
        }
    }
}
