package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
final class zzbm {
    private final Object zza;
    private final int zzb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzbm(Object obj, int i) {
        this.zza = obj;
        this.zzb = i;
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof zzbm)) {
            return false;
        }
        zzbm zzbmVar = (zzbm) obj;
        return this.zza == zzbmVar.zza && this.zzb == zzbmVar.zzb;
    }

    public final int hashCode() {
        return (System.identityHashCode(this.zza) * 65535) + this.zzb;
    }
}
