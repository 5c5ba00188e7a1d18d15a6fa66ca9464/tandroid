package com.google.android.gms.internal.mlkit_language_id;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.Iterator;
import java.util.Map;
/* loaded from: classes.dex */
final class zzgc implements zzgp {
    private final zzfz zza;
    private final zzhh zzb;
    private final boolean zzc;
    private final zzee zzd;

    private zzgc(zzhh zzhhVar, zzee zzeeVar, zzfz zzfzVar) {
        this.zzb = zzhhVar;
        this.zzc = zzeeVar.zza(zzfzVar);
        this.zzd = zzeeVar;
        this.zza = zzfzVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzgc zza(zzhh zzhhVar, zzee zzeeVar, zzfz zzfzVar) {
        return new zzgc(zzhhVar, zzeeVar, zzfzVar);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final int zza(Object obj) {
        int hashCode = this.zzb.zza(obj).hashCode();
        return this.zzc ? (hashCode * 53) + this.zzd.zza(obj).hashCode() : hashCode;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final void zza(Object obj, zzib zzibVar) {
        Iterator zzd = this.zzd.zza(obj).zzd();
        if (zzd.hasNext()) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(((Map.Entry) zzd.next()).getKey());
            throw null;
        }
        zzhh zzhhVar = this.zzb;
        zzhhVar.zzb(zzhhVar.zza(obj), zzibVar);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final boolean zza(Object obj, Object obj2) {
        if (this.zzb.zza(obj).equals(this.zzb.zza(obj2))) {
            if (this.zzc) {
                return this.zzd.zza(obj).equals(this.zzd.zza(obj2));
            }
            return true;
        }
        return false;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final void zzb(Object obj) {
        this.zzb.zzb(obj);
        this.zzd.zzc(obj);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final void zzb(Object obj, Object obj2) {
        zzgr.zza(this.zzb, obj, obj2);
        if (this.zzc) {
            zzgr.zza(this.zzd, obj, obj2);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final boolean zzc(Object obj) {
        return this.zzd.zza(obj).zzf();
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzgp
    public final int zzd(Object obj) {
        zzhh zzhhVar = this.zzb;
        int zzc = zzhhVar.zzc(zzhhVar.zza(obj));
        return this.zzc ? zzc + this.zzd.zza(obj).zzg() : zzc;
    }
}
