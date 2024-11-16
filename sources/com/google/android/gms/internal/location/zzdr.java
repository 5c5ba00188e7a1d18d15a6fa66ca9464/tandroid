package com.google.android.gms.internal.location;

import java.util.List;

/* loaded from: classes.dex */
final class zzdr extends zzds {
    final transient int zza;
    final transient int zzb;
    final /* synthetic */ zzds zzc;

    zzdr(zzds zzdsVar, int i, int i2) {
        this.zzc = zzdsVar;
        this.zza = i;
        this.zzb = i2;
    }

    @Override // java.util.List
    public final Object get(int i) {
        zzdm.zza(i, this.zzb, "index");
        return this.zzc.get(i + this.zza);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzb;
    }

    @Override // com.google.android.gms.internal.location.zzds, java.util.List
    public final /* bridge */ /* synthetic */ List subList(int i, int i2) {
        return subList(i, i2);
    }

    @Override // com.google.android.gms.internal.location.zzdp
    final int zzb() {
        return this.zzc.zzc() + this.zza + this.zzb;
    }

    @Override // com.google.android.gms.internal.location.zzdp
    final int zzc() {
        return this.zzc.zzc() + this.zza;
    }

    @Override // com.google.android.gms.internal.location.zzdp
    final boolean zzf() {
        return true;
    }

    @Override // com.google.android.gms.internal.location.zzdp
    final Object[] zzg() {
        return this.zzc.zzg();
    }

    @Override // com.google.android.gms.internal.location.zzds
    /* renamed from: zzh */
    public final zzds subList(int i, int i2) {
        zzdm.zzc(i, i2, this.zzb);
        zzds zzdsVar = this.zzc;
        int i3 = this.zza;
        return zzdsVar.subList(i + i3, i2 + i3);
    }
}
