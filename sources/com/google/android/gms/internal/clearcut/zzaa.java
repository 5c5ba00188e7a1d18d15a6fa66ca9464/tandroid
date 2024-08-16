package com.google.android.gms.internal.clearcut;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.UserManager;
/* loaded from: classes.dex */
public class zzaa {
    private static volatile UserManager zzdc;
    private static volatile boolean zzdd = !zzf();

    private zzaa() {
    }

    public static boolean zze(Context context) {
        return zzf() && !zzf(context);
    }

    private static boolean zzf() {
        return Build.VERSION.SDK_INT >= 24;
    }

    @TargetApi(24)
    private static boolean zzf(Context context) {
        Object systemService;
        boolean z = zzdd;
        if (!z) {
            UserManager userManager = zzdc;
            if (userManager == null) {
                synchronized (zzaa.class) {
                    try {
                        userManager = zzdc;
                        if (userManager == null) {
                            systemService = context.getSystemService(UserManager.class);
                            UserManager userManager2 = (UserManager) systemService;
                            zzdc = userManager2;
                            if (userManager2 == null) {
                                zzdd = true;
                                return true;
                            }
                            userManager = userManager2;
                        }
                    } finally {
                    }
                }
            }
            z = userManager.isUserUnlocked();
            zzdd = z;
            if (z) {
                zzdc = null;
            }
        }
        return z;
    }
}
