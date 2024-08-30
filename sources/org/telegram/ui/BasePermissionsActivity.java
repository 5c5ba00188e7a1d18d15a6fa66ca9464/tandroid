package org.telegram.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import androidx.fragment.app.FragmentActivity;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.ContactsController;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.ImageLoader;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.messenger.camera.CameraController;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.Theme;
/* loaded from: classes4.dex */
public abstract class BasePermissionsActivity extends FragmentActivity {
    protected int currentAccount = -1;

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$createPermissionErrorAlert$0(DialogInterface dialogInterface, int i) {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.parse("package:" + ApplicationLoader.applicationContext.getPackageName()));
            startActivity(intent);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    private void showPermissionErrorAlert(int i, String str) {
        createPermissionErrorAlert(i, str).show();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x009f, code lost:
        if (r2 == false) goto L56;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public boolean checkPermissionsResult(int i, String[] strArr, int[] iArr) {
        int i2;
        int i3;
        if (iArr == null) {
            iArr = new int[0];
        }
        if (strArr == null) {
            strArr = new String[0];
        }
        boolean z = iArr.length > 0 && iArr[0] == 0;
        if (i == 104) {
            if (z) {
                GroupCallActivity groupCallActivity = GroupCallActivity.groupCallInstance;
                if (groupCallActivity != null) {
                    groupCallActivity.enableCamera();
                }
            } else {
                i2 = R.raw.permission_request_camera;
                i3 = R.string.VoipNeedCameraPermission;
                showPermissionErrorAlert(i2, LocaleController.getString(i3));
            }
        } else if (i == 4 || i == 151) {
            if (z) {
                ImageLoader.getInstance().checkMediaPaths();
            } else {
                showPermissionErrorAlert(R.raw.permission_request_folder, LocaleController.getString(i == 151 ? R.string.PermissionNoStorageAvatar : R.string.PermissionStorageWithHint));
            }
        } else if (i != 5) {
            if (i == 3 || i == 150) {
                int min = Math.min(strArr.length, iArr.length);
                boolean z2 = true;
                boolean z3 = true;
                for (int i4 = 0; i4 < min; i4++) {
                    if ("android.permission.RECORD_AUDIO".equals(strArr[i4])) {
                        z2 = iArr[i4] == 0;
                    } else if ("android.permission.CAMERA".equals(strArr[i4])) {
                        z3 = iArr[i4] == 0;
                    }
                }
                if (i == 150 && !(z2 && z3)) {
                    i2 = R.raw.permission_request_camera;
                    i3 = R.string.PermissionNoCameraMicVideo;
                } else if (!z2) {
                    i2 = R.raw.permission_request_microphone;
                    i3 = R.string.PermissionNoAudioWithHint;
                } else if (z3) {
                    if (SharedConfig.inappCamera) {
                        CameraController.getInstance().initCamera(null);
                    }
                    return false;
                }
                showPermissionErrorAlert(i2, LocaleController.getString(i3));
            } else if (i != 18 && i != 19 && i != 20 && i != 22) {
                if (i == 2) {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(z ? NotificationCenter.locationPermissionGranted : NotificationCenter.locationPermissionDenied, new Object[0]);
                } else if (i == 211) {
                    NotificationCenter.getGlobalInstance().lambda$postNotificationNameOnUIThread$1(z ? NotificationCenter.locationPermissionGranted : NotificationCenter.locationPermissionDenied, 1);
                }
            }
            i2 = R.raw.permission_request_camera;
            i3 = R.string.PermissionNoCameraWithHint;
            showPermissionErrorAlert(i2, LocaleController.getString(i3));
        } else if (!z) {
            showPermissionErrorAlert(R.raw.permission_request_contacts, LocaleController.getString(R.string.PermissionNoContactsSharing));
            return false;
        } else {
            ContactsController.getInstance(this.currentAccount).forceImportContacts();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AlertDialog createPermissionErrorAlert(int i, String str) {
        return new AlertDialog.Builder(this).setTopAnimation(i, 72, false, Theme.getColor(Theme.key_dialogTopBackground)).setMessage(AndroidUtilities.replaceTags(str)).setPositiveButton(LocaleController.getString(R.string.PermissionOpenSettings), new DialogInterface.OnClickListener() { // from class: org.telegram.ui.BasePermissionsActivity$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                BasePermissionsActivity.this.lambda$createPermissionErrorAlert$0(dialogInterface, i2);
            }
        }).setNegativeButton(LocaleController.getString(R.string.ContactsPermissionAlertNotNow), null).create();
    }
}
