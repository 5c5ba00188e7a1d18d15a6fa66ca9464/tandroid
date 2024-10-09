package com.google.android.gms.internal.vision;

import androidx.activity.result.ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
final class zzkq implements zzlc {
    private final zzkk zza;
    private final zzlu zzb;
    private final boolean zzc;
    private final zziq zzd;

    private zzkq(zzlu zzluVar, zziq zziqVar, zzkk zzkkVar) {
        this.zzb = zzluVar;
        this.zzc = zziqVar.zza(zzkkVar);
        this.zzd = zziqVar;
        this.zza = zzkkVar;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zzkq zza(zzlu zzluVar, zziq zziqVar, zzkk zzkkVar) {
        return new zzkq(zzluVar, zziqVar, zzkkVar);
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final int zza(Object obj) {
        int hashCode = this.zzb.zzb(obj).hashCode();
        return this.zzc ? (hashCode * 53) + this.zzd.zza(obj).hashCode() : hashCode;
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final Object zza() {
        return this.zza.zzq().zze();
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zza(Object obj, zzmr zzmrVar) {
        Iterator zzd = this.zzd.zza(obj).zzd();
        if (zzd.hasNext()) {
            ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(((Map.Entry) zzd.next()).getKey());
            throw null;
        }
        zzlu zzluVar = this.zzb;
        zzluVar.zzb(zzluVar.zzb(obj), zzmrVar);
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zza(Object obj, byte[] bArr, int i, int i2, zzhn zzhnVar) {
        zzjb zzjbVar = (zzjb) obj;
        if (zzjbVar.zzb == zzlx.zza()) {
            zzjbVar.zzb = zzlx.zzb();
        }
        ActivityResultRegistry$$ExternalSyntheticThrowCCEIfNotNull0.m(obj);
        throw null;
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final boolean zza(Object obj, Object obj2) {
        if (!this.zzb.zzb(obj).equals(this.zzb.zzb(obj2))) {
            return false;
        }
        if (this.zzc) {
            return this.zzd.zza(obj).equals(this.zzd.zza(obj2));
        }
        return true;
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final int zzb(Object obj) {
        zzlu zzluVar = this.zzb;
        int zze = zzluVar.zze(zzluVar.zzb(obj));
        return this.zzc ? zze + this.zzd.zza(obj).zzg() : zze;
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zzb(Object obj, Object obj2) {
        zzle.zza(this.zzb, obj, obj2);
        if (this.zzc) {
            zzle.zza(this.zzd, obj, obj2);
        }
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final void zzc(Object obj) {
        this.zzb.zzd(obj);
        this.zzd.zzc(obj);
    }

    @Override // com.google.android.gms.internal.vision.zzlc
    public final boolean zzd(Object obj) {
        return this.zzd.zza(obj).zzf();
    }
}
