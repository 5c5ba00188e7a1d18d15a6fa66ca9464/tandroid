package com.google.android.gms.internal.vision;

/* loaded from: classes.dex */
final class zzlw extends zzlu {
    zzlw() {
    }

    private static void zza(Object obj, zzlx zzlxVar) {
        ((zzjb) obj).zzb = zzlxVar;
    }

    @Override // com.google.android.gms.internal.vision.zzlu
    final /* synthetic */ Object zza() {
        return zzlx.zzb();
    }

    @Override // com.google.android.gms.internal.vision.zzlu
    final /* synthetic */ void zza(Object obj, int i, long j) {
        ((zzlx) obj).zza(i << 3, Long.valueOf(j));
    }

    @Override // com.google.android.gms.internal.vision.zzlu
    final /* synthetic */ void zza(Object obj, int i, zzht zzhtVar) {
        ((zzlx) obj).zza((i << 3) | 2, zzhtVar);
    }

    @Override // com.google.android.gms.internal.vision.zzlu
    final /* synthetic */ void zza(Object obj, zzmr zzmrVar) {
        ((zzlx) obj).zzb(zzmrVar);
    }

    @Override // com.google.android.gms.internal.vision.zzlu
    final /* bridge */ /* synthetic */ void zza(Object obj, Object obj2) {
        zza(obj, (zzlx) obj2);
    }

    @Override // com.google.android.gms.internal.vision.zzlu
    final /* synthetic */ Object zzb(Object obj) {
        return ((zzjb) obj).zzb;
    }

    @Override // com.google.android.gms.internal.vision.zzlu
    final /* synthetic */ void zzb(Object obj, zzmr zzmrVar) {
        ((zzlx) obj).zza(zzmrVar);
    }

    @Override // com.google.android.gms.internal.vision.zzlu
    final /* synthetic */ void zzb(Object obj, Object obj2) {
        zza(obj, (zzlx) obj2);
    }

    @Override // com.google.android.gms.internal.vision.zzlu
    final /* synthetic */ Object zzc(Object obj, Object obj2) {
        zzlx zzlxVar = (zzlx) obj;
        zzlx zzlxVar2 = (zzlx) obj2;
        return zzlxVar2.equals(zzlx.zza()) ? zzlxVar : zzlx.zza(zzlxVar, zzlxVar2);
    }

    @Override // com.google.android.gms.internal.vision.zzlu
    final void zzd(Object obj) {
        ((zzjb) obj).zzb.zzc();
    }

    @Override // com.google.android.gms.internal.vision.zzlu
    final /* synthetic */ int zze(Object obj) {
        return ((zzlx) obj).zzd();
    }

    @Override // com.google.android.gms.internal.vision.zzlu
    final /* synthetic */ int zzf(Object obj) {
        return ((zzlx) obj).zze();
    }
}
