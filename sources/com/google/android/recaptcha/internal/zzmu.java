package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzmu extends zzit implements zzkf {
    private static final zzmu zzb;
    private String zzd = "";
    private String zze = "";
    private String zzf = "";
    private String zzg = "";
    private String zzh = "";
    private String zzi = "";

    static {
        zzmu zzmuVar = new zzmu();
        zzb = zzmuVar;
        zzit.zzD(zzmu.class, zzmuVar);
    }

    private zzmu() {
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\u0006\u0000\u0000\u0001\u0006\u0006\u0000\u0000\u0000\u0001Ȉ\u0002Ȉ\u0003Ȉ\u0004Ȉ\u0005Ȉ\u0006Ȉ", new Object[]{"zzd", "zze", "zzf", "zzg", "zzh", "zzi"});
        }
        if (i2 == 3) {
            return new zzmu();
        }
        zzms zzmsVar = null;
        if (i2 == 4) {
            return new zzmt(zzmsVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }
}
