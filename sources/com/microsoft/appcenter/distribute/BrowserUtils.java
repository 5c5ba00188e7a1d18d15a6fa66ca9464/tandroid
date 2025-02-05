package com.microsoft.appcenter.distribute;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import com.microsoft.appcenter.utils.AppCenterLog;
import java.net.URI;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes.dex */
abstract class BrowserUtils {
    static String appendUri(String str, String str2) {
        URI uri = new URI(str);
        String query = uri.getQuery();
        if (query != null) {
            str2 = query + "&" + str2;
        }
        return new URI(uri.getScheme(), uri.getAuthority(), uri.getPath(), str2, uri.getFragment()).toString();
    }

    static void openBrowser(String str, Activity activity) {
        try {
            openBrowserWithoutIntentChooser(str, activity);
        } catch (SecurityException e) {
            AppCenterLog.warn("AppCenterDistribute", "Browser could not be opened by trying to avoid intent chooser, starting implicit intent instead.", e);
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
        }
    }

    private static void openBrowserWithoutIntentChooser(String str, Activity activity) {
        String str2;
        String str3;
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
        List<ResolveInfo> queryIntentActivities = activity.getPackageManager().queryIntentActivities(intent, 0);
        if (queryIntentActivities.isEmpty()) {
            AppCenterLog.error("AppCenterDistribute", "No browser found on device, abort login.");
            return;
        }
        ResolveInfo resolveActivity = activity.getPackageManager().resolveActivity(intent, 65536);
        String str4 = null;
        if (resolveActivity != null) {
            ActivityInfo activityInfo = resolveActivity.activityInfo;
            str3 = activityInfo.packageName;
            str2 = activityInfo.name;
            AppCenterLog.debug("AppCenterDistribute", "Default browser seems to be " + str3 + "/" + str2);
        } else {
            str2 = null;
            str3 = null;
        }
        Iterator<ResolveInfo> it = queryIntentActivities.iterator();
        while (true) {
            if (!it.hasNext()) {
                str2 = null;
                break;
            }
            ActivityInfo activityInfo2 = it.next().activityInfo;
            if (activityInfo2.packageName.equals(str3) && activityInfo2.name.equals(str2)) {
                AppCenterLog.debug("AppCenterDistribute", "And its not the picker.");
                str4 = str3;
                break;
            }
        }
        if (resolveActivity != null && str4 == null) {
            AppCenterLog.debug("AppCenterDistribute", "Default browser is actually a picker...");
        }
        if (str4 == null) {
            AppCenterLog.debug("AppCenterDistribute", "Picking first browser in list.");
            ActivityInfo activityInfo3 = queryIntentActivities.iterator().next().activityInfo;
            str4 = activityInfo3.packageName;
            str2 = activityInfo3.name;
        }
        AppCenterLog.debug("AppCenterDistribute", "Launch browser=" + str4 + "/" + str2);
        intent.setClassName(str4, str2);
        activity.startActivity(intent);
    }
}
