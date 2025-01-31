package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzpq extends zzit implements zzkf {
    private static final zzpq zzb;
    private int zzd = 0;
    private Object zze;

    static {
        zzpq zzpqVar = new zzpq();
        zzb = zzpqVar;
        zzit.zzD(zzpq.class, zzpqVar);
    }

    private zzpq() {
    }

    public final long zzH() {
        if (this.zzd == 8) {
            return ((Long) this.zze).longValue();
        }
        return 0L;
    }

    public final zzgw zzI() {
        return this.zzd == 3 ? (zzgw) this.zze : zzgw.zzb;
    }

    public final String zzK() {
        return this.zzd == 4 ? (String) this.zze : "";
    }

    public final String zzL() {
        return this.zzd == 12 ? (String) this.zze : "";
    }

    public final boolean zzM() {
        if (this.zzd == 2) {
            return ((Boolean) this.zze).booleanValue();
        }
        return false;
    }

    public final boolean zzN() {
        return this.zzd == 1;
    }

    public final int zzO() {
        switch (this.zzd) {
            case 0:
                return 16;
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 5;
            case 6:
                return 6;
            case 7:
                return 7;
            case 8:
                return 8;
            case 9:
                return 9;
            case 10:
                return 10;
            case 11:
                return 11;
            case 12:
                return 12;
            case 13:
                return 13;
            case 14:
                return 14;
            case 15:
                return 15;
            default:
                return 0;
        }
    }

    public final double zzf() {
        if (this.zzd == 11) {
            return ((Double) this.zze).doubleValue();
        }
        return 0.0d;
    }

    public final float zzg() {
        if (this.zzd == 10) {
            return ((Float) this.zze).floatValue();
        }
        return 0.0f;
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\u000f\u0001\u0000\u0001\u000f\u000f\u0000\u0000\u0000\u0001>\u0000\u0002:\u0000\u0003=\u0000\u0004Ȼ\u0000\u0005B\u0000\u0006B\u0000\u0007>\u0000\bC\u0000\t6\u0000\n4\u0000\u000b3\u0000\fȻ\u0000\r:\u0000\u000e?\u0000\u000f?\u0000", new Object[]{"zze", "zzd"});
        }
        if (i2 == 3) {
            return new zzpq();
        }
        zzor zzorVar = null;
        if (i2 == 4) {
            return new zzpp(zzorVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }

    public final int zzi() {
        if (this.zzd == 1) {
            return ((Integer) this.zze).intValue();
        }
        return 0;
    }

    public final int zzj() {
        if (this.zzd == 5) {
            return ((Integer) this.zze).intValue();
        }
        return 0;
    }

    public final int zzk() {
        if (this.zzd == 6) {
            return ((Integer) this.zze).intValue();
        }
        return 0;
    }
}
