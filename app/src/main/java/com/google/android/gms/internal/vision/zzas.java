package com.google.android.gms.internal.vision;

import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.os.UserManager;
import android.util.Log;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes3.dex */
public class zzas {
    private static UserManager zza;
    private static volatile boolean zzb = !zza();
    private static boolean zzc = false;

    private zzas() {
    }

    public static boolean zza() {
        return Build.VERSION.SDK_INT >= 24;
    }

    public static boolean zza(Context context) {
        return !zza() || zzc(context);
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0033, code lost:
        r4 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0042, code lost:
        if (r4 == false) goto L21;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x0044, code lost:
        com.google.android.gms.internal.vision.zzas.zza = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x0046, code lost:
        return r4;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private static boolean zzb(Context context) {
        boolean z = true;
        int i = 1;
        while (true) {
            boolean z2 = false;
            if (i > 2) {
                break;
            }
            if (zza == null) {
                zza = (UserManager) context.getSystemService(UserManager.class);
            }
            UserManager userManager = zza;
            if (userManager == null) {
                return true;
            }
            try {
                if (userManager.isUserUnlocked()) {
                    break;
                } else if (userManager.isUserRunning(Process.myUserHandle())) {
                    z = false;
                }
            } catch (NullPointerException e) {
                Log.w("DirectBootUtils", "Failed to check if user is unlocked.", e);
                zza = null;
                i++;
            }
        }
    }

    private static boolean zzc(Context context) {
        if (zzb) {
            return true;
        }
        synchronized (zzas.class) {
            if (zzb) {
                return true;
            }
            boolean zzb2 = zzb(context);
            if (zzb2) {
                zzb = zzb2;
            }
            return zzb2;
        }
    }
}
