package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import com.google.android.gms.common.GooglePlayServicesUtilLight;

/* loaded from: classes.dex */
public abstract class DeviceProperties {
    private static Boolean zzd;
    private static Boolean zze;
    private static Boolean zzg;
    private static Boolean zzi;

    public static boolean isAuto(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (zzi == null) {
            boolean z = false;
            if (PlatformVersion.isAtLeastO() && packageManager.hasSystemFeature("android.hardware.type.automotive")) {
                z = true;
            }
            zzi = Boolean.valueOf(z);
        }
        return zzi.booleanValue();
    }

    public static boolean isUserBuild() {
        int i = GooglePlayServicesUtilLight.GOOGLE_PLAY_SERVICES_VERSION_CODE;
        return "user".equals(Build.TYPE);
    }

    public static boolean isWearable(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (zzd == null) {
            boolean z = false;
            if (PlatformVersion.isAtLeastKitKatWatch() && packageManager.hasSystemFeature("android.hardware.type.watch")) {
                z = true;
            }
            zzd = Boolean.valueOf(z);
        }
        return zzd.booleanValue();
    }

    public static boolean isWearableWithoutPlayStore(Context context) {
        if (isWearable(context) && !PlatformVersion.isAtLeastN()) {
            return true;
        }
        if (zza(context)) {
            return !PlatformVersion.isAtLeastO() || PlatformVersion.isAtLeastR();
        }
        return false;
    }

    public static boolean zza(Context context) {
        if (zze == null) {
            boolean z = false;
            if (PlatformVersion.isAtLeastLollipop() && context.getPackageManager().hasSystemFeature("cn.google")) {
                z = true;
            }
            zze = Boolean.valueOf(z);
        }
        return zze.booleanValue();
    }

    public static boolean zzb(Context context) {
        if (zzg == null) {
            boolean z = true;
            if (!context.getPackageManager().hasSystemFeature("android.hardware.type.iot") && !context.getPackageManager().hasSystemFeature("android.hardware.type.embedded")) {
                z = false;
            }
            zzg = Boolean.valueOf(z);
        }
        return zzg.booleanValue();
    }
}
