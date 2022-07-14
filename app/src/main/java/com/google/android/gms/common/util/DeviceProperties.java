package com.google.android.gms.common.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
/* compiled from: com.google.android.gms:play-services-basement@@17.5.0 */
/* loaded from: classes3.dex */
public final class DeviceProperties {
    private static Boolean zza;
    private static Boolean zzb;
    private static Boolean zzc;
    private static Boolean zzd;
    private static Boolean zze;
    private static Boolean zzf;
    private static Boolean zzg;
    private static Boolean zzh;

    private DeviceProperties() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x003d, code lost:
        if (com.google.android.gms.common.util.DeviceProperties.zzb.booleanValue() != false) goto L23;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public static boolean isTablet(Resources resources) {
        boolean z = false;
        if (resources == null) {
            return false;
        }
        if (zza == null) {
            if (!((resources.getConfiguration().screenLayout & 15) > 3)) {
                if (zzb == null) {
                    Configuration configuration = resources.getConfiguration();
                    zzb = Boolean.valueOf((configuration.screenLayout & 15) <= 3 && configuration.smallestScreenWidthDp >= 600);
                }
            }
            z = true;
            zza = Boolean.valueOf(z);
        }
        return zza.booleanValue();
    }

    public static boolean isWearable(Context context) {
        return isWearable(context.getPackageManager());
    }

    public static boolean isWearable(PackageManager packageManager) {
        if (zzc == null) {
            zzc = Boolean.valueOf(PlatformVersion.isAtLeastKitKatWatch() && packageManager.hasSystemFeature("android.hardware.type.watch"));
        }
        return zzc.booleanValue();
    }

    public static boolean isWearableWithoutPlayStore(Context context) {
        if (isWearable(context)) {
            if (!PlatformVersion.isAtLeastN()) {
                return true;
            }
            return zzb(context) && !PlatformVersion.isAtLeastO();
        }
        return false;
    }

    public static boolean isSidewinder(Context context) {
        return zzb(context);
    }

    private static boolean zzb(Context context) {
        if (zzd == null) {
            zzd = Boolean.valueOf(PlatformVersion.isAtLeastLollipop() && context.getPackageManager().hasSystemFeature("cn.google"));
        }
        return zzd.booleanValue();
    }

    public static boolean isLatchsky(Context context) {
        if (zze == null) {
            PackageManager packageManager = context.getPackageManager();
            zze = Boolean.valueOf(packageManager.hasSystemFeature("com.google.android.feature.services_updater") && packageManager.hasSystemFeature("cn.google.services"));
        }
        return zze.booleanValue();
    }

    public static boolean zza(Context context) {
        if (zzf == null) {
            zzf = Boolean.valueOf(context.getPackageManager().hasSystemFeature("android.hardware.type.iot") || context.getPackageManager().hasSystemFeature("android.hardware.type.embedded"));
        }
        return zzf.booleanValue();
    }

    @Deprecated
    public static boolean isFeaturePhone(Context context) {
        return false;
    }

    public static boolean isAuto(Context context) {
        return isAuto(context.getPackageManager());
    }

    public static boolean isAuto(PackageManager packageManager) {
        if (zzg == null) {
            zzg = Boolean.valueOf(PlatformVersion.isAtLeastO() && packageManager.hasSystemFeature("android.hardware.type.automotive"));
        }
        return zzg.booleanValue();
    }

    public static boolean isTv(Context context) {
        return isTv(context.getPackageManager());
    }

    public static boolean isTv(PackageManager packageManager) {
        if (zzh == null) {
            zzh = Boolean.valueOf(packageManager.hasSystemFeature("com.google.android.tv") || packageManager.hasSystemFeature("android.hardware.type.television") || packageManager.hasSystemFeature("android.software.leanback"));
        }
        return zzh.booleanValue();
    }

    public static boolean isUserBuild() {
        return "user".equals(Build.TYPE);
    }
}
