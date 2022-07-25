package com.google.android.gms.internal.clearcut;

import java.io.IOException;
/* loaded from: classes.dex */
final class zzez extends zzex<zzey, zzey> {
    private static void zza(Object obj, zzey zzeyVar) {
        ((zzcg) obj).zzjp = zzeyVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzex
    public final /* synthetic */ void zza(zzey zzeyVar, int i, long j) {
        zzeyVar.zzb(i << 3, Long.valueOf(j));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzex
    public final /* synthetic */ void zza(zzey zzeyVar, int i, zzbb zzbbVar) {
        zzeyVar.zzb((i << 3) | 2, zzbbVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzex
    public final /* synthetic */ void zza(zzey zzeyVar, zzfr zzfrVar) throws IOException {
        zzeyVar.zzb(zzfrVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzex
    public final void zzc(Object obj) {
        ((zzcg) obj).zzjp.zzv();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzex
    public final /* synthetic */ void zzc(zzey zzeyVar, zzfr zzfrVar) throws IOException {
        zzeyVar.zza(zzfrVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzex
    public final /* synthetic */ zzey zzdz() {
        return zzey.zzeb();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzex
    public final /* synthetic */ void zze(Object obj, zzey zzeyVar) {
        zza(obj, zzeyVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzex
    public final /* synthetic */ void zzf(Object obj, zzey zzeyVar) {
        zza(obj, zzeyVar);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzex
    public final /* synthetic */ zzey zzg(zzey zzeyVar, zzey zzeyVar2) {
        zzey zzeyVar3 = zzeyVar;
        zzey zzeyVar4 = zzeyVar2;
        return zzeyVar4.equals(zzey.zzea()) ? zzeyVar3 : zzey.zza(zzeyVar3, zzeyVar4);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzex
    public final /* synthetic */ int zzm(zzey zzeyVar) {
        return zzeyVar.zzas();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzex
    public final /* synthetic */ zzey zzq(Object obj) {
        return ((zzcg) obj).zzjp;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.clearcut.zzex
    public final /* synthetic */ int zzr(zzey zzeyVar) {
        return zzeyVar.zzec();
    }
}
