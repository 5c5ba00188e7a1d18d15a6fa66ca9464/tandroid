package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzmx extends zzit implements zzkf {
    private static final zzmx zzb;
    private String zzd = "";
    private String zze = "";

    static {
        zzmx zzmxVar = new zzmx();
        zzb = zzmxVar;
        zzit.zzD(zzmx.class, zzmxVar);
    }

    private zzmx() {
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\u0002\u0000\u0000\u0001\u0002\u0002\u0000\u0000\u0000\u0001Ȉ\u0002Ȉ", new Object[]{"zzd", "zze"});
        }
        if (i2 == 3) {
            return new zzmx();
        }
        zzmv zzmvVar = null;
        if (i2 == 4) {
            return new zzmw(zzmvVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }
}
