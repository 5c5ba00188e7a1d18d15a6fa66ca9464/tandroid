package com.google.android.gms.location;

import com.google.android.gms.common.internal.Preconditions;

/* loaded from: classes.dex */
public abstract class zzae {
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
        if (i == 100) {
            return "HIGH_ACCURACY";
        }
        if (i == 102) {
            return "BALANCED_POWER_ACCURACY";
        }
        if (i == 104) {
            return "LOW_POWER";
        }
        if (i == 105) {
            return "PASSIVE";
        }
        throw new IllegalArgumentException();
    }
}
