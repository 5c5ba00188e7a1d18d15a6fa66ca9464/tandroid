package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public abstract class zzhc {
    public static final /* synthetic */ int zzd = 0;
    private static volatile int zze = 100;
    int zza;
    final int zzb = zze;
    zzhd zzc;

    /* synthetic */ zzhc(zzhb zzhbVar) {
    }

    public static int zzF(int i) {
        return (i >>> 1) ^ (-(i & 1));
    }

    public static long zzG(long j) {
        return (j >>> 1) ^ (-(1 & j));
    }

    static zzhc zzH(byte[] bArr, int i, int i2, boolean z) {
        zzgy zzgyVar = new zzgy(bArr, 0, 0, false, null);
        try {
            zzgyVar.zze(0);
            return zzgyVar;
        } catch (zzje e) {
            throw new IllegalArgumentException(e);
        }
    }

    public abstract void zzA(int i);

    public abstract boolean zzC();

    public abstract boolean zzD();

    public abstract boolean zzE(int i);

    public abstract double zzb();

    public abstract float zzc();

    public abstract int zzd();

    public abstract int zze(int i);

    public abstract int zzf();

    public abstract int zzg();

    public abstract int zzh();

    public abstract int zzk();

    public abstract int zzl();

    public abstract int zzm();

    public abstract int zzn();

    public abstract long zzo();

    public abstract long zzp();

    public abstract long zzt();

    public abstract long zzu();

    public abstract long zzv();

    public abstract zzgw zzw();

    public abstract String zzx();

    public abstract String zzy();

    public abstract void zzz(int i);
}
