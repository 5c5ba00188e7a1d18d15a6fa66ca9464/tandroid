package com.google.android.recaptcha.internal;

import java.io.InputStream;

/* loaded from: classes.dex */
public final class zzoz extends zzit implements zzkf {
    private static final zzoz zzb;
    private int zzd;
    private int zze;
    private int zzf;

    static {
        zzoz zzozVar = new zzoz();
        zzb = zzozVar;
        zzit.zzD(zzoz.class, zzozVar);
    }

    private zzoz() {
    }

    public static zzoz zzg(InputStream inputStream) {
        return (zzoz) zzit.zzt(zzb, inputStream);
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဌ\u0000\u0002ဌ\u0001", new Object[]{"zzd", "zze", "zzf"});
        }
        if (i2 == 3) {
            return new zzoz();
        }
        zzor zzorVar = null;
        if (i2 == 4) {
            return new zzoy(zzorVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }

    public final zzpb zzi() {
        zzpb zzb2 = zzpb.zzb(this.zzf);
        return zzb2 == null ? zzpb.zzk : zzb2;
    }
}
