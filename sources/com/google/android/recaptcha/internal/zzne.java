package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public enum zzne implements zziv {
    zza(0),
    zzb(1),
    zzc(2),
    zzd(18),
    zze(19),
    zzf(3),
    zzg(4),
    zzh(20),
    zzi(21),
    zzj(22),
    zzk(23),
    zzl(24),
    zzm(5),
    zzn(6),
    zzo(7),
    zzp(25),
    zzq(26),
    zzr(27),
    zzs(8),
    zzt(9),
    zzu(10),
    zzv(11),
    zzw(12),
    zzx(13),
    zzy(14),
    zzz(15),
    zzA(16),
    zzB(17),
    zzC(-1);

    private static final zziw zzD = new zziw() { // from class: com.google.android.recaptcha.internal.zznd
    };
    private final int zzF;

    zzne(int i) {
        this.zzF = i;
    }

    @Override // java.lang.Enum
    public final String toString() {
        return Integer.toString(zza());
    }

    @Override // com.google.android.recaptcha.internal.zziv
    public final int zza() {
        if (this != zzC) {
            return this.zzF;
        }
        throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
    }
}
