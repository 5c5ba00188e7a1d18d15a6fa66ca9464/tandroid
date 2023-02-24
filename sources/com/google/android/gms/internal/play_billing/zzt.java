package com.google.android.gms.internal.play_billing;

import java.util.List;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: com.android.billingclient:billing@@5.1.0 */
/* loaded from: classes.dex */
public final class zzt extends zzu {
    final transient int zza;
    final transient int zzb;
    final /* synthetic */ zzu zzc;

    /* JADX INFO: Access modifiers changed from: package-private */
    public zzt(zzu zzuVar, int i, int i2) {
        this.zzc = zzuVar;
        this.zza = i;
        this.zzb = i2;
    }

    @Override // java.util.List
    public final Object get(int i) {
        zzm.zza(i, this.zzb, "index");
        return this.zzc.get(i + this.zza);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzb;
    }

    @Override // com.google.android.gms.internal.play_billing.zzu, java.util.List
    public final /* bridge */ /* synthetic */ List subList(int i, int i2) {
        return subList(i, i2);
    }

    @Override // com.google.android.gms.internal.play_billing.zzr
    final int zzb() {
        return this.zzc.zzc() + this.zza + this.zzb;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzr
    public final int zzc() {
        return this.zzc.zzc() + this.zza;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzr
    public final boolean zzf() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.android.gms.internal.play_billing.zzr
    public final Object[] zzg() {
        return this.zzc.zzg();
    }

    @Override // com.google.android.gms.internal.play_billing.zzu
    public final zzu zzh(int i, int i2) {
        zzm.zzd(i, i2, this.zzb);
        zzu zzuVar = this.zzc;
        int i3 = this.zza;
        return zzuVar.subList(i + i3, i2 + i3);
    }
}
