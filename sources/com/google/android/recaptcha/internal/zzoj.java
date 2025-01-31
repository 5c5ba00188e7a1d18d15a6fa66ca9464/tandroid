package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzoj extends zzit implements zzkf {
    private static final zzoj zzb;
    private int zzd;
    private String zze = "";
    private String zzf = "";
    private String zzg = "";
    private String zzh = "";
    private String zzi = "";
    private String zzj = "";
    private String zzk = "";

    static {
        zzoj zzojVar = new zzoj();
        zzb = zzojVar;
        zzit.zzD(zzoj.class, zzojVar);
    }

    private zzoj() {
    }

    static /* synthetic */ void zzH(zzoj zzojVar, String str) {
        str.getClass();
        zzojVar.zzd |= 32;
        zzojVar.zzj = str;
    }

    static /* synthetic */ void zzI(zzoj zzojVar, String str) {
        str.getClass();
        zzojVar.zzd |= 64;
        zzojVar.zzk = str;
    }

    static /* synthetic */ void zzJ(zzoj zzojVar, String str) {
        str.getClass();
        zzojVar.zzd |= 2;
        zzojVar.zzf = str;
    }

    static /* synthetic */ void zzK(zzoj zzojVar, String str) {
        str.getClass();
        zzojVar.zzd |= 4;
        zzojVar.zzg = str;
    }

    public static zzoi zzf() {
        return (zzoi) zzb.zzp();
    }

    static /* synthetic */ void zzi(zzoj zzojVar, String str) {
        str.getClass();
        zzojVar.zzd |= 1;
        zzojVar.zze = str;
    }

    static /* synthetic */ void zzj(zzoj zzojVar, String str) {
        str.getClass();
        zzojVar.zzd |= 8;
        zzojVar.zzh = str;
    }

    static /* synthetic */ void zzk(zzoj zzojVar, String str) {
        str.getClass();
        zzojVar.zzd |= 16;
        zzojVar.zzi = str;
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\u0007\u0000\u0001\u0001\u0007\u0007\u0000\u0000\u0000\u0001ለ\u0000\u0002ለ\u0001\u0003ለ\u0002\u0004ለ\u0003\u0005ለ\u0004\u0006ለ\u0005\u0007ለ\u0006", new Object[]{"zzd", "zze", "zzf", "zzg", "zzh", "zzi", "zzj", "zzk"});
        }
        if (i2 == 3) {
            return new zzoj();
        }
        zzoh zzohVar = null;
        if (i2 == 4) {
            return new zzoi(zzohVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }
}
