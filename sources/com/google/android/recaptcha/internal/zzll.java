package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
abstract class zzll {
    zzll() {
    }

    abstract int zza(Object obj);

    abstract int zzb(Object obj);

    abstract Object zzc(Object obj);

    abstract Object zzd(Object obj);

    abstract Object zze(Object obj, Object obj2);

    abstract Object zzf();

    abstract Object zzg(Object obj);

    abstract void zzh(Object obj, int i, int i2);

    abstract void zzi(Object obj, int i, long j);

    abstract void zzj(Object obj, int i, Object obj2);

    abstract void zzk(Object obj, int i, zzgw zzgwVar);

    abstract void zzl(Object obj, int i, long j);

    abstract void zzm(Object obj);

    abstract void zzn(Object obj, Object obj2);

    abstract void zzo(Object obj, Object obj2);

    abstract void zzp(Object obj, zzmd zzmdVar);

    abstract void zzq(Object obj, zzmd zzmdVar);

    final boolean zzr(Object obj, zzkq zzkqVar) {
        int zzd = zzkqVar.zzd();
        int i = zzd >>> 3;
        int i2 = zzd & 7;
        if (i2 == 0) {
            zzl(obj, i, zzkqVar.zzl());
            return true;
        }
        if (i2 == 1) {
            zzi(obj, i, zzkqVar.zzk());
            return true;
        }
        if (i2 == 2) {
            zzk(obj, i, zzkqVar.zzp());
            return true;
        }
        if (i2 != 3) {
            if (i2 == 4) {
                return false;
            }
            if (i2 != 5) {
                throw zzje.zza();
            }
            zzh(obj, i, zzkqVar.zzf());
            return true;
        }
        Object zzf = zzf();
        int i3 = i << 3;
        while (zzkqVar.zzc() != Integer.MAX_VALUE && zzr(zzf, zzkqVar)) {
        }
        if ((4 | i3) != zzkqVar.zzd()) {
            throw zzje.zzb();
        }
        zzg(zzf);
        zzj(obj, i, zzf);
        return true;
    }

    abstract boolean zzs(zzkq zzkqVar);
}
