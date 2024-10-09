package com.google.android.gms.internal.mlkit_language_id;

/* loaded from: classes.dex */
public abstract class zzdh implements zzfy {
    protected abstract zzdh zza(zzde zzdeVar);

    @Override // com.google.android.gms.internal.mlkit_language_id.zzfy
    public final /* synthetic */ zzfy zza(zzfz zzfzVar) {
        if (zzn().getClass().isInstance(zzfzVar)) {
            return zza((zzde) zzfzVar);
        }
        throw new IllegalArgumentException("mergeFrom(MessageLite) can only merge messages of the same type.");
    }
}
