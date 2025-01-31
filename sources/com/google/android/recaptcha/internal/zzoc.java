package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzoc extends zzit implements zzkf {
    private static final zzoc zzb;
    private int zzd;
    private String zze = "";
    private String zzf = "";
    private String zzg = "";
    private String zzh = "";
    private String zzi = "";
    private String zzj = "";
    private String zzk = "";
    private String zzl = "";

    static {
        zzoc zzocVar = new zzoc();
        zzb = zzocVar;
        zzit.zzD(zzoc.class, zzocVar);
    }

    private zzoc() {
    }

    static /* synthetic */ void zzL(zzoc zzocVar, String str) {
        str.getClass();
        zzocVar.zzd |= 8;
        zzocVar.zzh = str;
    }

    static /* synthetic */ void zzM(zzoc zzocVar, String str) {
        str.getClass();
        zzocVar.zzd |= 16;
        zzocVar.zzi = str;
    }

    static /* synthetic */ void zzN(zzoc zzocVar, String str) {
        str.getClass();
        zzocVar.zzd |= 32;
        zzocVar.zzj = str;
    }

    static /* synthetic */ void zzO(zzoc zzocVar, String str) {
        str.getClass();
        zzocVar.zzd |= 64;
        zzocVar.zzk = str;
    }

    static /* synthetic */ void zzP(zzoc zzocVar, String str) {
        str.getClass();
        zzocVar.zzd |= 128;
        zzocVar.zzl = str;
    }

    static /* synthetic */ void zzQ(zzoc zzocVar, String str) {
        str.getClass();
        zzocVar.zzd |= 2;
        zzocVar.zzf = str;
    }

    static /* synthetic */ void zzR(zzoc zzocVar, String str) {
        str.getClass();
        zzocVar.zzd |= 4;
        zzocVar.zzg = str;
    }

    public static zzob zzf() {
        return (zzob) zzb.zzp();
    }

    public final String zzH() {
        return this.zzf;
    }

    public final String zzI() {
        return this.zzh;
    }

    public final String zzJ() {
        return this.zzk;
    }

    public final String zzK() {
        return this.zzj;
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\b\u0000\u0001\u0001\b\b\u0000\u0000\u0000\u0001ለ\u0000\u0002ለ\u0001\u0003ለ\u0002\u0004ለ\u0003\u0005ለ\u0004\u0006ለ\u0005\u0007ለ\u0006\bለ\u0007", new Object[]{"zzd", "zze", "zzf", "zzg", "zzh", "zzi", "zzj", "zzk", "zzl"});
        }
        if (i2 == 3) {
            return new zzoc();
        }
        zzoa zzoaVar = null;
        if (i2 == 4) {
            return new zzob(zzoaVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }

    public final String zzi() {
        return this.zzi;
    }

    public final String zzj() {
        return this.zzl;
    }

    public final String zzk() {
        return this.zzg;
    }
}
