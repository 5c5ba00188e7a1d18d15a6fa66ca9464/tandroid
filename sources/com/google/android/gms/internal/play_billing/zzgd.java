package com.google.android.gms.internal.play_billing;
/* loaded from: classes.dex */
public final class zzgd extends zzcb implements zzdg {
    private static final zzgd zzb;
    private int zzd;
    private int zze;

    static {
        zzgd zzgdVar = new zzgd();
        zzb = zzgdVar;
        zzcb.zzp(zzgd.class, zzgdVar);
    }

    private zzgd() {
    }

    public static zzgd zzw() {
        return zzb;
    }

    @Override // com.google.android.gms.internal.play_billing.zzcb
    protected final Object zzu(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 != 0) {
            if (i2 != 2) {
                if (i2 != 3) {
                    if (i2 != 4) {
                        if (i2 != 5) {
                            return null;
                        }
                        return zzb;
                    }
                    return new zzgb(null);
                }
                return new zzgd();
            }
            return zzcb.zzm(zzb, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0001᠌\u0000", new Object[]{"zzd", "zze", zzgc.zza});
        }
        return (byte) 1;
    }
}
