package com.google.android.gms.internal.clearcut;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.Map;

/* loaded from: classes.dex */
final class zzbv extends zzbu {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzbu
    public final int zza(Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzbu
    public final zzby zza(Object obj) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzbu
    public final void zza(zzfr zzfrVar, Map.Entry entry) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(entry.getKey());
        int[] iArr = zzbw.zzgq;
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzbu
    public final void zza(Object obj, zzby zzbyVar) {
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzbu
    public final zzby zzb(Object obj) {
        zzby zza = zza(obj);
        if (!zza.isImmutable()) {
            return zza;
        }
        zzby zzbyVar = (zzby) zza.clone();
        zza(obj, zzbyVar);
        return zzbyVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzbu
    public final void zzc(Object obj) {
        zza(obj).zzv();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzbu
    public final boolean zze(zzdo zzdoVar) {
        return false;
    }
}
