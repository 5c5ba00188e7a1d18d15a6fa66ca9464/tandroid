package com.google.android.gms.location;

import com.google.android.gms.common.internal.Preconditions;
/* loaded from: classes.dex */
public abstract class zzo {
    public static int zza(int i) {
        boolean z;
        if (i != 0 && i != 1) {
            if (i != 2) {
                z = false;
                Preconditions.checkArgument(z, "granularity %d must be a Granularity.GRANULARITY_* constant", Integer.valueOf(i));
                return i;
            }
            i = 2;
        }
        z = true;
        Preconditions.checkArgument(z, "granularity %d must be a Granularity.GRANULARITY_* constant", Integer.valueOf(i));
        return i;
    }

    public static String zzb(int i) {
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    return "GRANULARITY_FINE";
                }
                throw new IllegalArgumentException();
            }
            return "GRANULARITY_COARSE";
        }
        return "GRANULARITY_PERMISSION_LEVEL";
    }
}
