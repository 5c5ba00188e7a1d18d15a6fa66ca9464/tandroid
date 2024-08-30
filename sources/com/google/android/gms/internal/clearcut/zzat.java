package com.google.android.gms.internal.clearcut;
/* loaded from: classes.dex */
public abstract class zzat implements zzdp {
    protected abstract zzat zza(zzas zzasVar);

    @Override // com.google.android.gms.internal.clearcut.zzdp
    public final /* synthetic */ zzdp zza(zzdo zzdoVar) {
        if (zzbe().getClass().isInstance(zzdoVar)) {
            return zza((zzas) zzdoVar);
        }
        throw new IllegalArgumentException("mergeFrom(MessageLite) can only merge messages of the same type.");
    }
}
