package com.microsoft.appcenter.distribute;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import com.microsoft.appcenter.utils.AppCenterLog;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class InstallerUtils {
    private static final Set LOCAL_STORES;
    private static Boolean sInstalledFromAppStore;

    static {
        HashSet hashSet = new HashSet();
        LOCAL_STORES = hashSet;
        hashSet.add("adb");
        hashSet.add("com.android.packageinstaller");
        hashSet.add("com.google.android.packageinstaller");
        hashSet.add("com.android.managedprovisioning");
        hashSet.add("com.miui.packageinstaller");
        hashSet.add("com.samsung.android.packageinstaller");
        hashSet.add("pc");
        hashSet.add("com.google.android.apps.nbu.files");
        hashSet.add("org.mozilla.firefox");
        hashSet.add("com.android.chrome");
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Intent getInstallIntent(Uri uri) {
        Intent intent = new Intent("android.intent.action.INSTALL_PACKAGE");
        intent.setData(uri);
        intent.addFlags(1);
        intent.addFlags(268435456);
        intent.addFlags(536870912);
        return intent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static synchronized boolean isInstalledFromAppStore(String str, Context context) {
        boolean booleanValue;
        synchronized (InstallerUtils.class) {
            try {
                if (sInstalledFromAppStore == null) {
                    String installerPackageName = context.getPackageManager().getInstallerPackageName(context.getPackageName());
                    AppCenterLog.debug(str, "InstallerPackageName=" + installerPackageName);
                    sInstalledFromAppStore = Boolean.valueOf((installerPackageName == null || LOCAL_STORES.contains(installerPackageName)) ? false : true);
                }
                booleanValue = sInstalledFromAppStore.booleanValue();
            } catch (Throwable th) {
                throw th;
            }
        }
        return booleanValue;
    }

    public static boolean isUnknownSourcesEnabled(Context context) {
        boolean canRequestPackageInstalls;
        int i = Build.VERSION.SDK_INT;
        if (i < 26) {
            ContentResolver contentResolver = context.getContentResolver();
            return "1".equals(i < 21 ? Settings.Global.getString(contentResolver, "install_non_market_apps") : Settings.Secure.getString(contentResolver, "install_non_market_apps"));
        }
        if (context.getApplicationInfo().targetSdkVersion >= 26) {
            canRequestPackageInstalls = context.getPackageManager().canRequestPackageInstalls();
            if (!canRequestPackageInstalls) {
                return false;
            }
        }
        return true;
    }
}
