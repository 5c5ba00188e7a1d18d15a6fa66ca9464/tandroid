package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzpk extends zzit implements zzkf {
    private static final zzpk zzb;
    private int zzd = 0;
    private Object zze;

    static {
        zzpk zzpkVar = new zzpk();
        zzb = zzpkVar;
        zzit.zzD(zzpk.class, zzpkVar);
    }

    private zzpk() {
    }

    static /* synthetic */ void zzH(zzpk zzpkVar, double d) {
        zzpkVar.zzd = 10;
        zzpkVar.zze = Double.valueOf(d);
    }

    static /* synthetic */ void zzI(zzpk zzpkVar, String str) {
        str.getClass();
        zzpkVar.zzd = 11;
        zzpkVar.zze = str;
    }

    static /* synthetic */ void zzJ(zzpk zzpkVar, boolean z) {
        zzpkVar.zzd = 1;
        zzpkVar.zze = Boolean.valueOf(z);
    }

    static /* synthetic */ void zzK(zzpk zzpkVar, zzgw zzgwVar) {
        zzpkVar.zzd = 2;
        zzpkVar.zze = zzgwVar;
    }

    static /* synthetic */ void zzL(zzpk zzpkVar, String str) {
        str.getClass();
        zzpkVar.zzd = 3;
        zzpkVar.zze = str;
    }

    static /* synthetic */ void zzM(zzpk zzpkVar, int i) {
        zzpkVar.zzd = 4;
        zzpkVar.zze = Integer.valueOf(i);
    }

    public static zzpj zzf() {
        return (zzpj) zzb.zzp();
    }

    static /* synthetic */ void zzi(zzpk zzpkVar, int i) {
        zzpkVar.zzd = 5;
        zzpkVar.zze = Integer.valueOf(i);
    }

    static /* synthetic */ void zzj(zzpk zzpkVar, long j) {
        zzpkVar.zzd = 7;
        zzpkVar.zze = Long.valueOf(j);
    }

    static /* synthetic */ void zzk(zzpk zzpkVar, float f) {
        zzpkVar.zzd = 9;
        zzpkVar.zze = Float.valueOf(f);
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\u000b\u0001\u0000\u0001\u000b\u000b\u0000\u0000\u0000\u0001:\u0000\u0002=\u0000\u0003Ȼ\u0000\u0004B\u0000\u0005B\u0000\u0006>\u0000\u0007C\u0000\b6\u0000\t4\u0000\n3\u0000\u000bȻ\u0000", new Object[]{"zze", "zzd"});
        }
        if (i2 == 3) {
            return new zzpk();
        }
        zzor zzorVar = null;
        if (i2 == 4) {
            return new zzpj(zzorVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }
}
