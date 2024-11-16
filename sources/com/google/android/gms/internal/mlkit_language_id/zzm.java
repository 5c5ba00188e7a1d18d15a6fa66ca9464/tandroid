package com.google.android.gms.internal.mlkit_language_id;

import java.util.List;

/* loaded from: classes.dex */
final class zzm extends zzk {
    private final transient int zza;
    private final transient int zzb;
    private final /* synthetic */ zzk zzc;

    zzm(zzk zzkVar, int i, int i2) {
        this.zzc = zzkVar;
        this.zza = i;
        this.zzb = i2;
    }

    @Override // java.util.List
    public final Object get(int i) {
        zzg.zza(i, this.zzb);
        return this.zzc.get(i + this.zza);
    }

    @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
    public final int size() {
        return this.zzb;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzk, java.util.List
    public final /* synthetic */ List subList(int i, int i2) {
        return subList(i, i2);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzk
    /* renamed from: zza */
    public final zzk subList(int i, int i2) {
        zzg.zza(i, i2, this.zzb);
        zzk zzkVar = this.zzc;
        int i3 = this.zza;
        return (zzk) zzkVar.subList(i + i3, i2 + i3);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzl
    final Object[] zzb() {
        return this.zzc.zzb();
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzl
    final int zzc() {
        return this.zzc.zzc() + this.zza;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzl
    final int zzd() {
        return this.zzc.zzc() + this.zza + this.zzb;
    }
}
