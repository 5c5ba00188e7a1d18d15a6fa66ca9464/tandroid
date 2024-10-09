package com.google.android.gms.internal.icing;

/* loaded from: classes.dex */
public abstract class zzbp {
    public static Object zza(Object obj) {
        if (obj != null) {
            return obj;
        }
        StringBuilder sb = new StringBuilder(29);
        sb.append((CharSequence) "expected a non-null reference", 0, 29);
        throw new zzbq(sb.toString());
    }
}
