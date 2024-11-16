package com.google.android.gms.internal.mlkit_vision_subject_segmentation;

import java.util.List;

/* loaded from: classes.dex */
final class zzau extends zzav {
    final transient int zza;
    final transient int zzb;
    final /* synthetic */ zzav zzc;

    zzau(zzav zzavVar, int i, int i2) {
        this.zzc = zzavVar;
        this.zza = i;
        this.zzb = i2;
    }

    @Override // java.util.List
    public final Object get(int i) {
        zzi.zza(i, this.zzb, "index");
        return this.zzc.get(i + this.zza);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzb;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzav, java.util.List
    public final /* bridge */ /* synthetic */ List subList(int i, int i2) {
        return subList(i, i2);
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzaq
    final int zzb() {
        return this.zzc.zzc() + this.zza + this.zzb;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzaq
    final int zzc() {
        return this.zzc.zzc() + this.zza;
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzaq
    final Object[] zze() {
        return this.zzc.zze();
    }

    @Override // com.google.android.gms.internal.mlkit_vision_subject_segmentation.zzav
    /* renamed from: zzf */
    public final zzav subList(int i, int i2) {
        zzi.zzc(i, i2, this.zzb);
        int i3 = this.zza;
        return this.zzc.subList(i + i3, i2 + i3);
    }
}
