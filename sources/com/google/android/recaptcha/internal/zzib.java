package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzib extends zzit implements zzkf {
    private static final zzib zzb;
    private long zzd;
    private int zze;

    static {
        zzib zzibVar = new zzib();
        zzb = zzibVar;
        zzit.zzD(zzib.class, zzibVar);
    }

    private zzib() {
    }

    public static zzia zzi() {
        return (zzia) zzb.zzp();
    }

    public final int zzf() {
        return this.zze;
    }

    public final long zzg() {
        return this.zzd;
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return new zzkp(zzb, "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001\u0002\u0002\u0004", new Object[]{"zzd", "zze"});
        }
        if (i2 == 3) {
            return new zzib();
        }
        zzhz zzhzVar = null;
        if (i2 == 4) {
            return new zzia(zzhzVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }
}
