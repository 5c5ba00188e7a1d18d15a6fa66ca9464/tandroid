package org.telegram.messenger.support.customtabsclient.shared;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import org.telegram.messenger.ApplicationLoader;
/* loaded from: classes3.dex */
public abstract class CustomTabsHelper {
    private static String sPackageNameToUse;

    /* JADX WARN: Code restructure failed: missing block: B:39:0x00ad, code lost:
        if (r6.contains("com.google.android.apps.chrome") != false) goto L37;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static String getPackageNameToUse(Context context) {
        String str;
        PackageManager packageManager;
        ApplicationInfo applicationInfo;
        String str2 = sPackageNameToUse;
        if (str2 != null) {
            return str2;
        }
        PackageManager packageManager2 = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("http://www.example.com"));
        ResolveInfo resolveActivity = packageManager2.resolveActivity(intent, 0);
        String str3 = resolveActivity != null ? resolveActivity.activityInfo.packageName : null;
        List<ResolveInfo> queryIntentActivities = packageManager2.queryIntentActivities(intent, 0);
        ArrayList arrayList = new ArrayList();
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            Intent intent2 = new Intent();
            intent2.setAction("android.support.customtabs.action.CustomTabsService");
            intent2.setPackage(resolveInfo.activityInfo.packageName);
            if (packageManager2.resolveService(intent2, 0) != null) {
                arrayList.add(resolveInfo.activityInfo.packageName);
            }
        }
        if (arrayList.isEmpty()) {
            sPackageNameToUse = null;
        } else {
            if (arrayList.size() == 1) {
                str = (String) arrayList.get(0);
            } else if (!TextUtils.isEmpty(str3) && !hasSpecializedHandlerIntents(context, intent) && arrayList.contains(str3)) {
                sPackageNameToUse = str3;
            } else if (arrayList.contains("com.android.chrome")) {
                sPackageNameToUse = "com.android.chrome";
            } else {
                str = "com.chrome.beta";
                if (!arrayList.contains("com.chrome.beta")) {
                    str = "com.chrome.dev";
                    if (!arrayList.contains("com.chrome.dev")) {
                        str = "com.google.android.apps.chrome";
                    }
                }
            }
            sPackageNameToUse = str;
        }
        try {
            if ("com.sec.android.app.sbrowser".equalsIgnoreCase(sPackageNameToUse) && (applicationInfo = (packageManager = ApplicationLoader.applicationContext.getPackageManager()).getApplicationInfo("com.android.chrome", 0)) != null && applicationInfo.enabled) {
                packageManager.getPackageInfo("com.android.chrome", 1);
                sPackageNameToUse = "com.android.chrome";
            }
        } catch (Throwable unused) {
        }
        return sPackageNameToUse;
    }

    private static boolean hasSpecializedHandlerIntents(Context context, Intent intent) {
        List<ResolveInfo> queryIntentActivities;
        try {
            queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 64);
        } catch (RuntimeException unused) {
            Log.e("CustomTabsHelper", "Runtime exception while getting specialized handlers");
        }
        if (queryIntentActivities != null && queryIntentActivities.size() != 0) {
            for (ResolveInfo resolveInfo : queryIntentActivities) {
                IntentFilter intentFilter = resolveInfo.filter;
                if (intentFilter != null && intentFilter.countDataAuthorities() != 0 && intentFilter.countDataPaths() != 0 && resolveInfo.activityInfo != null) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }
}
