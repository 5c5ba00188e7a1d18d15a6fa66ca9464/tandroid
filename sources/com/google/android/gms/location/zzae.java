package com.google.android.gms.location;

import com.google.android.gms.common.internal.Preconditions;
/* compiled from: com.google.android.gms:play-services-location@@21.0.1 */
/* loaded from: classes.dex */
public final class zzae {
    public static int zza(int i) {
        boolean z;
        if (i != 100 && i != 102 && i != 104) {
            if (i != 105) {
                z = false;
                Preconditions.checkArgument(z, "priority %d must be a Priority.PRIORITY_* constant", Integer.valueOf(i));
                return i;
            }
            i = 105;
        }
        z = true;
        Preconditions.checkArgument(z, "priority %d must be a Priority.PRIORITY_* constant", Integer.valueOf(i));
        return i;
    }

    public static String zzb(int i) {
        if (i != 100) {
            if (i == 102) {
                return "BALANCED_POWER_ACCURACY";
            }
            if (i == 104) {
                return "LOW_POWER";
            }
            if (i != 105) {
                throw new IllegalArgumentException();
            }
            return "PASSIVE";
        }
        return "HIGH_ACCURACY";
    }
}
