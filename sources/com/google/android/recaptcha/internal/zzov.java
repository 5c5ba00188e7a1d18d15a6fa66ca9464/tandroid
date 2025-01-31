package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzov extends zzit implements zzkf {
    private static final zzov zzb;
    private String zzd = "";
    private String zze = "";

    static {
        zzov zzovVar = new zzov();
        zzb = zzovVar;
        zzit.zzD(zzov.class, zzovVar);
    }

    private zzov() {
    }

    public static zzou zzf() {
        return (zzou) zzb.zzp();
    }

    static /* synthetic */ void zzi(zzov zzovVar, String str) {
        str.getClass();
        zzovVar.zzd = str;
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
            return new zzov();
        }
        zzor zzorVar = null;
        if (i2 == 4) {
            return new zzou(zzorVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }
}
