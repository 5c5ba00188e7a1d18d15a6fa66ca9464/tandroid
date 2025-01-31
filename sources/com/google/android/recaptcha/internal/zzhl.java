package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzhl extends zzip implements zzkf {
    private static final zzhl zzd;
    private int zze;
    private boolean zzf;
    private zzht zzg;
    private boolean zzh;
    private byte zzj = 2;
    private zzjb zzi = zzko.zze();

    static {
        zzhl zzhlVar = new zzhl();
        zzd = zzhlVar;
        zzit.zzD(zzhl.class, zzhlVar);
    }

    private zzhl() {
    }

    public static zzhl zzg() {
        return zzd;
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return Byte.valueOf(this.zzj);
        }
        if (i2 == 2) {
            return new zzkp(zzd, "\u0001\u0004\u0000\u0001\u0001ϧ\u0004\u0000\u0001\u0002\u0001ဇ\u0000\u0002ᐉ\u0001\u0003ဇ\u0002ϧЛ", new Object[]{"zze", "zzf", "zzg", "zzh", "zzi", zzhx.class});
        }
        if (i2 == 3) {
            return new zzhl();
        }
        zzhj zzhjVar = null;
        if (i2 == 4) {
            return new zzhk(zzhjVar);
        }
        if (i2 == 5) {
            return zzd;
        }
        this.zzj = obj == null ? (byte) 0 : (byte) 1;
        return null;
    }
}
