package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
public final class zzfw extends zzcb implements zzdg {
    private static final zzfw zzb;
    private int zzd;
    private int zze;

    static {
        zzfw zzfwVar = new zzfw();
        zzb = zzfwVar;
        zzcb.zzp(zzfw.class, zzfwVar);
    }

    private zzfw() {
    }

    @Override // com.google.android.gms.internal.play_billing.zzcb
    protected final Object zzu(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzcb.zzm(zzb, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0001᠌\u0000", new Object[]{"zzd", "zze", zzfv.zza});
        }
        if (i2 == 3) {
            return new zzfw();
        }
        zzft zzftVar = null;
        if (i2 == 4) {
            return new zzfu(zzftVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }
}
