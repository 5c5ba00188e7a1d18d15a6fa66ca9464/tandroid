package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzht extends zzip implements zzkf {
    private static final zzht zzd;
    private int zze;
    private int zzf;
    private int zzg;
    private int zzh;
    private int zzi;
    private int zzj;
    private int zzk;
    private byte zzl = 2;

    static {
        zzht zzhtVar = new zzht();
        zzd = zzhtVar;
        zzit.zzD(zzht.class, zzhtVar);
    }

    private zzht() {
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return Byte.valueOf(this.zzl);
        }
        if (i2 == 2) {
            return new zzkp(zzd, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0000\u0000\u0001᠌\u0000\u0002᠌\u0001\u0003᠌\u0002\u0004᠌\u0003\u0005᠌\u0004\u0006᠌\u0005", new Object[]{"zze", "zzf", zzho.zza, "zzg", zzhn.zza, "zzh", zzhr.zza, "zzi", zzhs.zza, "zzj", zzhq.zza, "zzk", zzhp.zza});
        }
        if (i2 == 3) {
            return new zzht();
        }
        zzhj zzhjVar = null;
        if (i2 == 4) {
            return new zzhm(zzhjVar);
        }
        if (i2 == 5) {
            return zzd;
        }
        this.zzl = obj == null ? (byte) 0 : (byte) 1;
        return null;
    }
}
