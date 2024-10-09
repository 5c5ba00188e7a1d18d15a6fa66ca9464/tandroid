package com.google.android.gms.internal.vision;

import java.util.List;
import java.util.Map;

/* loaded from: classes.dex */
final class zzil implements zzmr {
    private final zzii zza;

    private zzil(zzii zziiVar) {
        zzii zziiVar2 = (zzii) zzjf.zza((Object) zziiVar, "output");
        this.zza = zziiVar2;
        zziiVar2.zza = this;
    }

    public static zzil zza(zzii zziiVar) {
        zzil zzilVar = zziiVar.zza;
        return zzilVar != null ? zzilVar : new zzil(zziiVar);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final int zza() {
        return zzmq.zza;
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i) {
        this.zza.zza(i, 3);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i, double d) {
        this.zza.zza(i, d);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i, float f) {
        this.zza.zza(i, f);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i, int i2) {
        this.zza.zze(i, i2);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i, long j) {
        this.zza.zza(i, j);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i, zzht zzhtVar) {
        this.zza.zza(i, zzhtVar);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i, zzkf zzkfVar, Map map) {
        for (Map.Entry entry : map.entrySet()) {
            this.zza.zza(i, 2);
            this.zza.zzb(zzkc.zza(zzkfVar, entry.getKey(), entry.getValue()));
            zzkc.zza(this.zza, zzkfVar, entry.getKey(), entry.getValue());
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i, Object obj) {
        if (obj instanceof zzht) {
            this.zza.zzb(i, (zzht) obj);
        } else {
            this.zza.zza(i, (zzkk) obj);
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i, Object obj, zzlc zzlcVar) {
        this.zza.zza(i, (zzkk) obj, zzlcVar);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i, String str) {
        this.zza.zza(i, str);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i, List list) {
        int i2 = 0;
        if (!(list instanceof zzjv)) {
            while (i2 < list.size()) {
                this.zza.zza(i, (String) list.get(i2));
                i2++;
            }
            return;
        }
        zzjv zzjvVar = (zzjv) list;
        while (i2 < list.size()) {
            Object zzb = zzjvVar.zzb(i2);
            if (zzb instanceof String) {
                this.zza.zza(i, (String) zzb);
            } else {
                this.zza.zza(i, (zzht) zzb);
            }
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i, List list, zzlc zzlcVar) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            zza(i, list.get(i2), zzlcVar);
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzf(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zza(int i, boolean z) {
        this.zza.zza(i, z);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zzb(int i) {
        this.zza.zza(i, 4);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zzb(int i, int i2) {
        this.zza.zzb(i, i2);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zzb(int i, long j) {
        this.zza.zzc(i, j);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zzb(int i, Object obj, zzlc zzlcVar) {
        zzii zziiVar = this.zza;
        zziiVar.zza(i, 3);
        zzlcVar.zza(obj, (zzmr) zziiVar.zza);
        zziiVar.zza(i, 4);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zzb(int i, List list) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            this.zza.zza(i, (zzht) list.get(i2));
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zzb(int i, List list, zzlc zzlcVar) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            zzb(i, list.get(i2), zzlcVar);
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzi(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzd(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zzc(int i, int i2) {
        this.zza.zzb(i, i2);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zzc(int i, long j) {
        this.zza.zza(i, j);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzd(((Long) list.get(i4)).longValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zzd(int i, int i2) {
        this.zza.zze(i, i2);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zzd(int i, long j) {
        this.zza.zzc(i, j);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zze(((Long) list.get(i4)).longValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zze(int i, int i2) {
        this.zza.zzc(i, i2);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zze(int i, long j) {
        this.zza.zzb(i, j);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzg(((Long) list.get(i4)).longValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzc(((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
    public final void zzf(int i, int i2) {
        this.zza.zzd(i, i2);
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzb(((Float) list.get(i4)).floatValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Float) list.get(i2)).floatValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzb(((Double) list.get(i4)).doubleValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Double) list.get(i2)).doubleValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzk(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzb(((Boolean) list.get(i4)).booleanValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zza(((Boolean) list.get(i2)).booleanValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzg(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzb(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzj(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzd(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzh(((Long) list.get(i4)).longValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzc(((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzh(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzc(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.gms.internal.vision.zzmr
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
            i3 += zzii.zzf(((Long) list.get(i4)).longValue());
        }
        this.zza.zzb(i3);
        while (i2 < list.size()) {
            this.zza.zzb(((Long) list.get(i2)).longValue());
            i2++;
        }
    }
}
