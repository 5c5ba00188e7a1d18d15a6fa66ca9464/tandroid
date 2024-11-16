package com.google.android.gms.internal.mlkit_vision_label;

import java.util.List;

/* loaded from: classes.dex */
final class zzbd extends zzbe {
    final transient int zza;
    final transient int zzb;
    final /* synthetic */ zzbe zzc;

    zzbd(zzbe zzbeVar, int i, int i2) {
        this.zzc = zzbeVar;
        this.zza = i;
        this.zzb = i2;
    }

    @Override // java.util.List
    public final Object get(int i) {
        zzs.zza(i, this.zzb, "index");
        return this.zzc.get(i + this.zza);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzb;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbe, java.util.List
    public final /* bridge */ /* synthetic */ List subList(int i, int i2) {
        return subList(i, i2);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzaz
    final int zzb() {
        return this.zzc.zzc() + this.zza + this.zzb;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzaz
    final int zzc() {
        return this.zzc.zzc() + this.zza;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzaz
    final Object[] zze() {
        return this.zzc.zze();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_label.zzbe
    /* renamed from: zzf */
    public final zzbe subList(int i, int i2) {
        zzs.zzc(i, i2, this.zzb);
        zzbe zzbeVar = this.zzc;
        int i3 = this.zza;
        return zzbeVar.subList(i + i3, i2 + i3);
    }
}
