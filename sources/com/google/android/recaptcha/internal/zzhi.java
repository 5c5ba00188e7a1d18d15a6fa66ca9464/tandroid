package com.google.android.recaptcha.internal;

import java.util.List;

/* loaded from: classes.dex */
final class zzhi implements zzmd {
    private final zzhh zza;

    private zzhi(zzhh zzhhVar) {
        byte[] bArr = zzjc.zzd;
        this.zza = zzhhVar;
        zzhhVar.zza = this;
    }

    public static zzhi zza(zzhh zzhhVar) {
        zzhi zzhiVar = zzhhVar.zza;
        return zzhiVar != null ? zzhiVar : new zzhi(zzhhVar);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzA(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzh(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            ((Long) list.get(i4)).longValue();
            i3 += 8;
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            this.zza.zzi(((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzB(int i, int i2) {
        this.zza.zzp(i, (i2 >> 31) ^ (i2 + i2));
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzC(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                zzhh zzhhVar = this.zza;
                int intValue = ((Integer) list.get(i2)).intValue();
                zzhhVar.zzp(i, (intValue >> 31) ^ (intValue + intValue));
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            int intValue2 = ((Integer) list.get(i4)).intValue();
            i3 += zzhh.zzy((intValue2 >> 31) ^ (intValue2 + intValue2));
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            zzhh zzhhVar2 = this.zza;
            int intValue3 = ((Integer) list.get(i2)).intValue();
            zzhhVar2.zzq((intValue3 >> 31) ^ (intValue3 + intValue3));
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzD(int i, long j) {
        this.zza.zzr(i, (j >> 63) ^ (j + j));
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzE(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                zzhh zzhhVar = this.zza;
                long longValue = ((Long) list.get(i2)).longValue();
                zzhhVar.zzr(i, (longValue >> 63) ^ (longValue + longValue));
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            long longValue2 = ((Long) list.get(i4)).longValue();
            i3 += zzhh.zzz((longValue2 >> 63) ^ (longValue2 + longValue2));
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            zzhh zzhhVar2 = this.zza;
            long longValue3 = ((Long) list.get(i2)).longValue();
            zzhhVar2.zzs((longValue3 >> 63) ^ (longValue3 + longValue3));
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    @Deprecated
    public final void zzF(int i) {
        this.zza.zzo(i, 3);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzG(int i, String str) {
        this.zza.zzm(i, str);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzH(int i, List list) {
        int i2 = 0;
        if (!(list instanceof zzjm)) {
            while (i2 < list.size()) {
                this.zza.zzm(i, (String) list.get(i2));
                i2++;
            }
            return;
        }
        zzjm zzjmVar = (zzjm) list;
        while (i2 < list.size()) {
            Object zzf = zzjmVar.zzf(i2);
            if (zzf instanceof String) {
                this.zza.zzm(i, (String) zzf);
            } else {
                this.zza.zze(i, (zzgw) zzf);
            }
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzI(int i, int i2) {
        this.zza.zzp(i, i2);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzJ(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzp(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzhh.zzy(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            this.zza.zzq(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzK(int i, long j) {
        this.zza.zzr(i, j);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzL(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzr(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzhh.zzz(((Long) list.get(i4)).longValue());
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            this.zza.zzs(((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzb(int i, boolean z) {
        this.zza.zzd(i, z);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzc(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzd(i, ((Boolean) list.get(i2)).booleanValue());
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            ((Boolean) list.get(i4)).booleanValue();
            i3++;
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            this.zza.zzb(((Boolean) list.get(i2)).booleanValue() ? (byte) 1 : (byte) 0);
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzd(int i, zzgw zzgwVar) {
        this.zza.zze(i, zzgwVar);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zze(int i, List list) {
        for (int i2 = 0; i2 < list.size(); i2++) {
            this.zza.zze(i, (zzgw) list.get(i2));
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzf(int i, double d) {
        this.zza.zzh(i, Double.doubleToRawLongBits(d));
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzg(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzh(i, Double.doubleToRawLongBits(((Double) list.get(i2)).doubleValue()));
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            ((Double) list.get(i4)).doubleValue();
            i3 += 8;
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            this.zza.zzi(Double.doubleToRawLongBits(((Double) list.get(i2)).doubleValue()));
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    @Deprecated
    public final void zzh(int i) {
        this.zza.zzo(i, 4);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzi(int i, int i2) {
        this.zza.zzj(i, i2);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzj(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzj(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzhh.zzu(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            this.zza.zzk(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzk(int i, int i2) {
        this.zza.zzf(i, i2);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzl(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzf(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            ((Integer) list.get(i4)).intValue();
            i3 += 4;
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            this.zza.zzg(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzm(int i, long j) {
        this.zza.zzh(i, j);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzn(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzh(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            ((Long) list.get(i4)).longValue();
            i3 += 8;
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            this.zza.zzi(((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzo(int i, float f) {
        this.zza.zzf(i, Float.floatToRawIntBits(f));
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzp(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzf(i, Float.floatToRawIntBits(((Float) list.get(i2)).floatValue()));
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            ((Float) list.get(i4)).floatValue();
            i3 += 4;
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            this.zza.zzg(Float.floatToRawIntBits(((Float) list.get(i2)).floatValue()));
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzq(int i, Object obj, zzkr zzkrVar) {
        zzhh zzhhVar = this.zza;
        zzhhVar.zzo(i, 3);
        zzkrVar.zzj((zzke) obj, zzhhVar.zza);
        zzhhVar.zzo(i, 4);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzr(int i, int i2) {
        this.zza.zzj(i, i2);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzs(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzj(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzhh.zzu(((Integer) list.get(i4)).intValue());
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            this.zza.zzk(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzt(int i, long j) {
        this.zza.zzr(i, j);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzu(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzr(i, ((Long) list.get(i2)).longValue());
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            i3 += zzhh.zzz(((Long) list.get(i4)).longValue());
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            this.zza.zzs(((Long) list.get(i2)).longValue());
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzv(int i, Object obj, zzkr zzkrVar) {
        zzke zzkeVar = (zzke) obj;
        zzhe zzheVar = (zzhe) this.zza;
        zzheVar.zzq((i << 3) | 2);
        zzheVar.zzq(((zzgf) zzkeVar).zza(zzkrVar));
        zzkrVar.zzj(zzkeVar, zzheVar.zza);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzw(int i, Object obj) {
        if (obj instanceof zzgw) {
            zzhe zzheVar = (zzhe) this.zza;
            zzheVar.zzq(11);
            zzheVar.zzp(2, i);
            zzheVar.zze(3, (zzgw) obj);
            zzheVar.zzq(12);
            return;
        }
        zzhh zzhhVar = this.zza;
        zzke zzkeVar = (zzke) obj;
        zzhe zzheVar2 = (zzhe) zzhhVar;
        zzheVar2.zzq(11);
        zzheVar2.zzp(2, i);
        zzheVar2.zzq(26);
        zzheVar2.zzq(zzkeVar.zzn());
        zzkeVar.zze(zzhhVar);
        zzheVar2.zzq(12);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzx(int i, int i2) {
        this.zza.zzf(i, i2);
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzy(int i, List list, boolean z) {
        int i2 = 0;
        if (!z) {
            while (i2 < list.size()) {
                this.zza.zzf(i, ((Integer) list.get(i2)).intValue());
                i2++;
            }
            return;
        }
        this.zza.zzo(i, 2);
        int i3 = 0;
        for (int i4 = 0; i4 < list.size(); i4++) {
            ((Integer) list.get(i4)).intValue();
            i3 += 4;
        }
        this.zza.zzq(i3);
        while (i2 < list.size()) {
            this.zza.zzg(((Integer) list.get(i2)).intValue());
            i2++;
        }
    }

    @Override // com.google.android.recaptcha.internal.zzmd
    public final void zzz(int i, long j) {
        this.zza.zzh(i, j);
    }
}
