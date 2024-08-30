package com.google.android.gms.internal.flags;

import android.os.Parcel;
/* loaded from: classes.dex */
public abstract class zzc {
    private static final ClassLoader zzd = zzc.class.getClassLoader();

    public static void writeBoolean(Parcel parcel, boolean z) {
        parcel.writeInt(z ? 1 : 0);
    }

    public static boolean zza(Parcel parcel) {
        return parcel.readInt() != 0;
    }
}
