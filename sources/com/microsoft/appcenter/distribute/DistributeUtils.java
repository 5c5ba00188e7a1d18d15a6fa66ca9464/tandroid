package com.microsoft.appcenter.distribute;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import com.microsoft.appcenter.utils.AppCenterLog;
import com.microsoft.appcenter.utils.DeviceInfoHelper;
import com.microsoft.appcenter.utils.HashUtils;
import com.microsoft.appcenter.utils.IdHelper;
import com.microsoft.appcenter.utils.NetworkStateHelper;
import com.microsoft.appcenter.utils.storage.SharedPreferencesManager;
import java.util.UUID;
import org.json.JSONException;

/* loaded from: classes.dex */
abstract class DistributeUtils {
    static String computeReleaseHash(PackageInfo packageInfo) {
        return HashUtils.sha256(packageInfo.packageName + ":" + packageInfo.versionName + ":" + DeviceInfoHelper.getVersionCode(packageInfo));
    }

    static int getNotificationId() {
        return Distribute.class.getName().hashCode();
    }

    static int getStoredDownloadState() {
        return SharedPreferencesManager.getInt("Distribute.download_state", 0);
    }

    static ReleaseDetails loadCachedReleaseDetails() {
        String string = SharedPreferencesManager.getString("Distribute.release_details");
        if (string == null) {
            return null;
        }
        try {
            return ReleaseDetails.parse(string);
        } catch (JSONException e) {
            AppCenterLog.error("AppCenterDistribute", "Invalid release details in cache.", e);
            SharedPreferencesManager.remove("Distribute.release_details");
            return null;
        }
    }

    static void updateSetupUsingBrowser(Activity activity, String str, String str2, PackageInfo packageInfo) {
        if (!NetworkStateHelper.getSharedInstance(activity).isNetworkConnected()) {
            AppCenterLog.info("AppCenterDistribute", "Postpone enabling in app updates via browser as network is disconnected.");
            Distribute.getInstance().completeWorkflow();
            return;
        }
        String computeReleaseHash = computeReleaseHash(packageInfo);
        String uuid = UUID.randomUUID().toString();
        String str3 = (((((((str + String.format("/apps/%s/private-update-setup/", str2)) + "?release_hash=" + computeReleaseHash) + "&redirect_id=" + activity.getPackageName()) + "&redirect_scheme=appcenter") + "&request_id=" + uuid) + "&platform=Android") + "&enable_failure_redirect=true") + "&install_id=" + IdHelper.getInstallId().toString();
        AppCenterLog.debug("AppCenterDistribute", "No token, need to open browser to url=" + str3);
        SharedPreferencesManager.putString("Distribute.request_id", uuid);
        BrowserUtils.openBrowser(str3, activity);
    }

    static void updateSetupUsingTesterApp(Activity activity, PackageInfo packageInfo) {
        String computeReleaseHash = computeReleaseHash(packageInfo);
        String uuid = UUID.randomUUID().toString();
        String str = (((("ms-actesterapp://update-setup?release_hash=" + computeReleaseHash) + "&redirect_id=" + activity.getPackageName()) + "&redirect_scheme=appcenter") + "&request_id=" + uuid) + "&platform=Android";
        AppCenterLog.debug("AppCenterDistribute", "No token, need to open tester app to url=" + str);
        SharedPreferencesManager.putString("Distribute.request_id", uuid);
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        intent.addFlags(268435456);
        activity.startActivity(intent);
    }
}
