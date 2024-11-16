package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
final class zzcr extends zzct {
    /* synthetic */ zzcr(zzcq zzcqVar) {
        super(null);
    }

    @Override // com.google.android.gms.internal.play_billing.zzct
    final void zza(Object obj, long j) {
        ((zzcf) zzeq.zzf(obj, j)).zzb();
    }

    @Override // com.google.android.gms.internal.play_billing.zzct
    final void zzb(Object obj, Object obj2, long j) {
        zzcf zzcfVar = (zzcf) zzeq.zzf(obj, j);
        zzcf zzcfVar2 = (zzcf) zzeq.zzf(obj2, j);
        int size = zzcfVar.size();
        int size2 = zzcfVar2.size();
        if (size > 0 && size2 > 0) {
            if (!zzcfVar.zzc()) {
                zzcfVar = zzcfVar.zzd(size2 + size);
            }
            zzcfVar.addAll(zzcfVar2);
        }
        if (size > 0) {
            zzcfVar2 = zzcfVar;
        }
        zzeq.zzs(obj, j, zzcfVar2);
    }
}
