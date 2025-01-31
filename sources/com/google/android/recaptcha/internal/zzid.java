package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
final class zzid {
    private final Object zza;
    private final int zzb;

    zzid(Object obj, int i) {
        this.zza = obj;
        this.zzb = i;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzid)) {
            return false;
        }
        zzid zzidVar = (zzid) obj;
        return this.zza == zzidVar.zza && this.zzb == zzidVar.zzb;
    }

    public final int hashCode() {
        return (System.identityHashCode(this.zza) * 65535) + this.zzb;
    }
}
