package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzml extends zzit implements zzkf {
    private static final zzml zzb;
    private int zzd;
    private zzib zze;
    private int zzf;

    static {
        zzml zzmlVar = new zzml();
        zzb = zzmlVar;
        zzit.zzD(zzml.class, zzmlVar);
    }

    private zzml() {
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဉ\u0000\u0002\u0004", new Object[]{"zzd", "zze", "zzf"});
        }
        if (i2 == 3) {
            return new zzml();
        }
        zzmj zzmjVar = null;
        if (i2 == 4) {
            return new zzmk(zzmjVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }
}
