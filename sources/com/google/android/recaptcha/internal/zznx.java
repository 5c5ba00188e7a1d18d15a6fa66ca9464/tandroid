package com.google.android.recaptcha.internal;

import java.util.List;

/* loaded from: classes.dex */
public final class zznx extends zzit implements zzkf {
    private static final zznx zzb;
    private zzjb zzd = zzit.zzx();

    static {
        zznx zznxVar = new zznx();
        zzb = zznxVar;
        zzit.zzD(zznx.class, zznxVar);
    }

    private zznx() {
    }

    public static zznx zzg() {
        return zzb;
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001Ț", new Object[]{"zzd"});
        }
        if (i2 == 3) {
            return new zznx();
        }
        zznv zznvVar = null;
        if (i2 == 4) {
            return new zznw(zznvVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }

    public final List zzi() {
        return this.zzd;
    }
}
