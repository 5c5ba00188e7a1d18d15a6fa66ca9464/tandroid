package com.microsoft.appcenter.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/* loaded from: classes.dex */
public abstract class AppNameHelper {
    public static String getAppName(Context context) {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        int i = applicationInfo.labelRes;
        return i == 0 ? String.valueOf(applicationInfo.nonLocalizedLabel) : context.getString(i);
    }
}
