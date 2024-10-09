package com.google.android.gms.internal.mlkit_language_id;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
final class zzed implements zzib {
    private final zzea zza;

    private zzed(zzea zzeaVar) {
        zzea zzeaVar2 = (zzea) zzeq.zza((Object) zzeaVar, "output");
        this.zza = zzeaVar2;
        zzeaVar2.zza = this;
    }

    public static zzed zza(zzea zzeaVar) {
        zzed zzedVar = zzeaVar.zza;
        return zzedVar != null ? zzedVar : new zzed(zzeaVar);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final int zza() {
        return zzia.zza;
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i) {
        this.zza.zza(i, 3);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, double d) {
        this.zza.zza(i, d);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, float f) {
        this.zza.zza(i, f);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, int i2) {
        this.zza.zze(i, i2);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, long j) {
        this.zza.zza(i, j);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, zzdn zzdnVar) {
        this.zza.zza(i, zzdnVar);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, zzfq zzfqVar, Map map) {
        Iterator it = map.entrySet().iterator();
        if (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            this.zza.zza(i, 2);
            entry.getKey();
            entry.getValue();
            throw null;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, Object obj) {
        if (obj instanceof zzdn) {
            this.zza.zzb(i, (zzdn) obj);
        } else {
            this.zza.zza(i, (zzfz) obj);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, Object obj, zzgp zzgpVar) {
        this.zza.zza(i, (zzfz) obj, zzgpVar);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, String str) {
        this.zza.zza(i, str);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, List list) {
        int i2 = 0;
        if (!(list instanceof zzfg)) {
            while (i2 < list.size()) {
                this.zza.zza(i, (String) list.get(i2));
                i2++;
            }
            return;
        }
        zzfg zzfgVar = (zzfg) list;
        while (i2 < list.size()) {
            Object zza = zzfgVar.zza(i2);
            if (zza instanceof String) {
                this.zza.zza(i, (String) zza);
            } else {
                this.zza.zza(i, (zzdn) zza);
            }
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, List list, zzgp zzgpVar) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            zza(i, list.get(i2), zzgpVar);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzb(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzf(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zza(int i, boolean z) {
        this.zza.zza(i, z);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzb(int i) {
        this.zza.zza(i, 4);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzb(int i, int i2) {
        this.zza.zzb(i, i2);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzb(int i, long j) {
        this.zza.zzc(i, j);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzb(int i, Object obj, zzgp zzgpVar) {
        zzea zzeaVar = this.zza;
        zzeaVar.zza(i, 3);
        zzgpVar.zza(obj, (zzib) zzeaVar.zza);
        zzeaVar.zza(i, 4);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzb(int i, List list) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            this.zza.zza(i, (zzdn) list.get(i2));
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzb(int i, List list, zzgp zzgpVar) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            zzb(i, list.get(i2), zzgpVar);
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzb(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zze(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzi(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzd(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzc(int i, int i2) {
        this.zza.zzb(i, i2);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzc(int i, long j) {
        this.zza.zza(i, j);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzc(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zza(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzd(((Long) list.get(i4)).longValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzd(int i, int i2) {
        this.zza.zze(i, i2);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzd(int i, long j) {
        this.zza.zzc(i, j);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzd(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zza(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zze(((Long) list.get(i4)).longValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zze(int i, int i2) {
        this.zza.zzc(i, i2);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zze(int i, long j) {
        this.zza.zzb(i, j);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zze(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzc(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzg(((Long) list.get(i4)).longValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzc(((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzf(int i, int i2) {
        this.zza.zzd(i, i2);
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzf(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zza(i, ((Float) list.get(i2)).floatValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzb(((Float) list.get(i4)).floatValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Float) list.get(i2)).floatValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzg(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zza(i, ((Double) list.get(i2)).doubleValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzb(((Double) list.get(i4)).doubleValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Double) list.get(i2)).doubleValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzh(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzb(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzk(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzi(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zza(i, ((Boolean) list.get(i2)).booleanValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzb(((Boolean) list.get(i4)).booleanValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Boolean) list.get(i2)).booleanValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzj(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzc(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzg(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzb(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzk(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zze(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzj(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzd(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzl(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzc(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzh(((Long) list.get(i4)).longValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzc(((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzm(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzd(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzh(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzc(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzib
    public final void zzn(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzb(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        this.zza.zza(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzea.zzf(((Long) list.get(i4)).longValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzb(((Long) list.get(i2)).longValue());
            i2++;
        }
    }
}
