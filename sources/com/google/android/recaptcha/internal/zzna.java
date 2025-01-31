package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzna extends zzit implements zzkf {
    private static final zzna zzb;
    private int zzd;
    private int zzf;
    private int zzh;
    private int zzj;
    private String zze = "";
    private String zzg = "";
    private String zzi = "";

    static {
        zzna zznaVar = new zzna();
        zzb = zznaVar;
        zzit.zzD(zzna.class, zznaVar);
    }

    private zzna() {
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\u0007\u0000\u0000\u0001\u0007\u0007\u0000\u0000\u0000\u0001\u0004\u0002Ȉ\u0003\u0004\u0004Ȉ\u0005\u0004\u0006Ȉ\u0007\u0004", new Object[]{"zzd", "zze", "zzf", "zzg", "zzh", "zzi", "zzj"});
        }
        if (i2 == 3) {
            return new zzna();
        }
        zzmy zzmyVar = null;
        if (i2 == 4) {
            return new zzmz(zzmyVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }
}
