package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public final class zzno extends zzit implements zzkf {
    private static final zzno zzb;
    private int zzd;
    private zzib zze;
    private zzlj zzf;
    private zzib zzg;
    private zzlj zzh;

    static {
        zzno zznoVar = new zzno();
        zzb = zznoVar;
        zzit.zzD(zzno.class, zznoVar);
    }

    private zzno() {
    }

    static /* synthetic */ void zzH(zzno zznoVar, zzib zzibVar) {
        zzibVar.getClass();
        zznoVar.zzg = zzibVar;
        zznoVar.zzd |= 4;
    }

    public static zznn zzf() {
        return (zznn) zzb.zzp();
    }

    static /* synthetic */ void zzi(zzno zznoVar, zzib zzibVar) {
        zzibVar.getClass();
        zznoVar.zze = zzibVar;
        zznoVar.zzd |= 1;
    }

    static /* synthetic */ void zzj(zzno zznoVar, zzlj zzljVar) {
        zzljVar.getClass();
        zznoVar.zzh = zzljVar;
        zznoVar.zzd |= 8;
    }

    static /* synthetic */ void zzk(zzno zznoVar, zzlj zzljVar) {
        zzljVar.getClass();
        zznoVar.zzf = zzljVar;
        zznoVar.zzd |= 2;
    }

    @Override // com.google.android.recaptcha.internal.zzit
    protected final Object zzh(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzit.zzA(zzb, "\u0000\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဉ\u0001\u0003ဉ\u0002\u0004ဉ\u0003", new Object[]{"zzd", "zze", "zzf", "zzg", "zzh"});
        }
        if (i2 == 3) {
            return new zzno();
        }
        zznm zznmVar = null;
        if (i2 == 4) {
            return new zznn(zznmVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }
}
