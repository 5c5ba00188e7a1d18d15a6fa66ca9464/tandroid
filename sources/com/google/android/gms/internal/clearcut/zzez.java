package com.google.android.gms.internal.clearcut;

/* loaded from: classes.dex */
final class zzez extends zzex {
    zzez() {
    }

    private static void zza(Object obj, zzey zzeyVar) {
        ((zzcg) obj).zzjp = zzeyVar;
    }

    @Override // com.google.android.gms.internal.clearcut.zzex
    final /* synthetic */ void zza(Object obj, int i, long j) {
        ((zzey) obj).zzb(i << 3, Long.valueOf(j));
    }

    @Override // com.google.android.gms.internal.clearcut.zzex
    final /* synthetic */ void zza(Object obj, int i, zzbb zzbbVar) {
        ((zzey) obj).zzb((i << 3) | 2, zzbbVar);
    }

    @Override // com.google.android.gms.internal.clearcut.zzex
    final /* synthetic */ void zza(Object obj, zzfr zzfrVar) {
        ((zzey) obj).zzb(zzfrVar);
    }

    @Override // com.google.android.gms.internal.clearcut.zzex
    final void zzc(Object obj) {
        ((zzcg) obj).zzjp.zzv();
    }

    @Override // com.google.android.gms.internal.clearcut.zzex
    final /* synthetic */ void zzc(Object obj, zzfr zzfrVar) {
        ((zzey) obj).zza(zzfrVar);
    }

    @Override // com.google.android.gms.internal.clearcut.zzex
    final /* synthetic */ Object zzdz() {
        return zzey.zzeb();
    }

    @Override // com.google.android.gms.internal.clearcut.zzex
    final /* synthetic */ void zze(Object obj, Object obj2) {
        zza(obj, (zzey) obj2);
    }

    @Override // com.google.android.gms.internal.clearcut.zzex
    final /* synthetic */ void zzf(Object obj, Object obj2) {
        zza(obj, (zzey) obj2);
    }

    @Override // com.google.android.gms.internal.clearcut.zzex
    final /* synthetic */ Object zzg(Object obj, Object obj2) {
        zzey zzeyVar = (zzey) obj;
        zzey zzeyVar2 = (zzey) obj2;
        return zzeyVar2.equals(zzey.zzea()) ? zzeyVar : zzey.zza(zzeyVar, zzeyVar2);
    }

    @Override // com.google.android.gms.internal.clearcut.zzex
    final /* synthetic */ int zzm(Object obj) {
        return ((zzey) obj).zzas();
    }

    @Override // com.google.android.gms.internal.clearcut.zzex
    final /* synthetic */ Object zzq(Object obj) {
        return ((zzcg) obj).zzjp;
    }

    @Override // com.google.android.gms.internal.clearcut.zzex
    final /* synthetic */ int zzr(Object obj) {
        return ((zzey) obj).zzec();
    }
}
