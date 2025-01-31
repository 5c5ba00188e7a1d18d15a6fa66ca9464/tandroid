package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zznz extends zzit implements zzkf {
    private static final zznz zzb;
    private int zzd;
    private zznx zze;
    private zznx zzf;

    static {
        zznz zznzVar = new zznz();
        zzb = zznzVar;
        zzit.zzD(zznz.class, zznzVar);
    }

    private zznz() {
    }

    public static zznz zzj(byte[] bArr) {
        return (zznz) zzit.zzu(zzb, bArr);
    }

    public final zznx zzf() {
        zznx zznxVar = this.zze;
        return zznxVar == null ? zznx.zzg() : zznxVar;
    }

    public final zznx zzg() {
        zznx zznxVar = this.zzf;
        return zznxVar == null ? zznx.zzg() : zznxVar;
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဉ\u0001", new Object[]{"zzd", "zze", "zzf"});
        }
        if (i2 == 3) {
            return new zznz();
        }
        zznv zznvVar = null;
        if (i2 == 4) {
            return new zzny(zznvVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }
}
