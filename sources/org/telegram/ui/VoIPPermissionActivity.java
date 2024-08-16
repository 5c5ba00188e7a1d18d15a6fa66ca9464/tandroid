package org.telegram.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.util.ArrayList;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.voip.VoIPService;
import org.telegram.tgnet.TLRPC$PhoneCall;
import org.telegram.ui.Components.voip.VoIPHelper;
@TargetApi(23)
/* loaded from: classes4.dex */
public class VoIPPermissionActivity extends Activity {
    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        int checkSelfPermission;
        int checkSelfPermission2;
        TLRPC$PhoneCall tLRPC$PhoneCall;
        super.onCreate(bundle);
        VoIPService sharedInstance = VoIPService.getSharedInstance();
        boolean z = (sharedInstance == null || (tLRPC$PhoneCall = sharedInstance.privateCall) == null || !tLRPC$PhoneCall.video) ? false : true;
        ArrayList arrayList = new ArrayList();
        checkSelfPermission = checkSelfPermission("android.permission.RECORD_AUDIO");
        if (checkSelfPermission != 0) {
            arrayList.add("android.permission.RECORD_AUDIO");
        }
        if (z) {
            checkSelfPermission2 = checkSelfPermission("android.permission.CAMERA");
            if (checkSelfPermission2 != 0) {
                arrayList.add("android.permission.CAMERA");
            }
        }
        if (arrayList.isEmpty()) {
            return;
        }
        try {
            requestPermissions((String[]) arrayList.toArray(new String[0]), z ? 102 : 101);
        } catch (Exception e) {
            FileLog.e(e);
        }
    }

    @Override // android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        boolean shouldShowRequestPermissionRationale;
        if (i == 101 || i == 102) {
            boolean z = false;
            int i2 = 0;
            while (true) {
                if (i2 >= iArr.length) {
                    z = true;
                    break;
                } else if (iArr[i2] != 0) {
                    break;
                } else {
                    i2++;
                }
            }
            if (iArr.length > 0 && z) {
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().acceptIncomingCall();
                }
                finish();
                startActivity(new Intent(this, LaunchActivity.class).setAction("voip"));
                return;
            }
            shouldShowRequestPermissionRationale = shouldShowRequestPermissionRationale("android.permission.RECORD_AUDIO");
            if (!shouldShowRequestPermissionRationale) {
                if (VoIPService.getSharedInstance() != null) {
                    VoIPService.getSharedInstance().declineIncomingCall();
                }
                VoIPHelper.permissionDenied(this, new Runnable() { // from class: org.telegram.ui.VoIPPermissionActivity$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        VoIPPermissionActivity.this.finish();
                    }
                }, i);
                return;
            }
            finish();
        }
    }
}
