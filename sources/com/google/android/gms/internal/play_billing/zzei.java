package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
final class zzei extends zzeg {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzeg
    public final /* synthetic */ int zza(Object obj) {
        return ((zzeh) obj).zza();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzeg
    public final /* synthetic */ int zzb(Object obj) {
        return ((zzeh) obj).zzb();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzeg
    public final /* bridge */ /* synthetic */ Object zzc(Object obj) {
        zzcb zzcbVar = (zzcb) obj;
        zzeh zzehVar = zzcbVar.zzc;
        if (zzehVar != zzeh.zzc()) {
            return zzehVar;
        }
        zzeh zzf = zzeh.zzf();
        zzcbVar.zzc = zzf;
        return zzf;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzeg
    public final /* synthetic */ Object zzd(Object obj) {
        return ((zzcb) obj).zzc;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzeg
    public final /* bridge */ /* synthetic */ Object zze(Object obj, Object obj2) {
        if (zzeh.zzc().equals(obj2)) {
            return obj;
        }
        zzeh zzehVar = (zzeh) obj2;
        if (zzeh.zzc().equals(obj)) {
            return zzeh.zze((zzeh) obj, zzehVar);
        }
        ((zzeh) obj).zzd(zzehVar);
        return obj;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzeg
    public final /* bridge */ /* synthetic */ void zzf(Object obj, int i, long j) {
        ((zzeh) obj).zzj(i << 3, Long.valueOf(j));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzeg
    public final void zzg(Object obj) {
        ((zzcb) obj).zzc.zzh();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzeg
    public final /* synthetic */ void zzh(Object obj, Object obj2) {
        ((zzcb) obj).zzc = (zzeh) obj2;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzeg
    public final /* synthetic */ void zzi(Object obj, zzey zzeyVar) {
        ((zzeh) obj).zzk(zzeyVar);
    }
}
