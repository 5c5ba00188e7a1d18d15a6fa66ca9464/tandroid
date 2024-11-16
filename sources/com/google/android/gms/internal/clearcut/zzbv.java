package com.google.android.gms.internal.clearcut;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.Map;

/* loaded from: classes.dex */
final class zzbv extends zzbu {
    zzbv() {
    }

    @Override // com.google.android.gms.internal.clearcut.zzbu
    final int zza(Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        throw null;
    }

    @Override // com.google.android.gms.internal.clearcut.zzbu
    final zzby zza(Object obj) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }

    @Override // com.google.android.gms.internal.clearcut.zzbu
    final void zza(zzfr zzfrVar, Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        int[] iArr = zzbw.zzgq;
        throw null;
    }

    @Override // com.google.android.gms.internal.clearcut.zzbu
    final void zza(Object obj, zzby zzbyVar) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }

    @Override // com.google.android.gms.internal.clearcut.zzbu
    final zzby zzb(Object obj) {
        zzby zza = zza(obj);
        if (!zza.isImmutable()) {
            return zza;
        }
        zzby zzbyVar = (zzby) zza.clone();
        zza(obj, zzbyVar);
        return zzbyVar;
    }

    @Override // com.google.android.gms.internal.clearcut.zzbu
    final void zzc(Object obj) {
        zza(obj).zzv();
    }

    @Override // com.google.android.gms.internal.clearcut.zzbu
    final boolean zze(zzdo zzdoVar) {
        return false;
    }
}
