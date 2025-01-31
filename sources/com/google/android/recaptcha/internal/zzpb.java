package com.google.android.recaptcha.internal;

/* loaded from: classes.dex */
public enum zzpb implements zziv {
    zza(0),
    zzb(1),
    zzc(2),
    zzd(3),
    zze(4),
    zzf(5),
    zzg(6),
    zzh(7),
    zzi(8),
    zzj(9),
    zzk(-1);

    private static final zziw zzl = new zziw() { // from class: com.google.android.recaptcha.internal.zzpa
    };
    private final int zzn;

    zzpb(int i) {
        this.zzn = i;
    }

    public static zzpb zzb(int i) {
        switch (i) {
            case 0:
                return zza;
            case 1:
                return zzb;
            case 2:
                return zzc;
            case 3:
                return zzd;
            case 4:
                return zze;
            case 5:
                return zzf;
            case 6:
                return zzg;
            case 7:
                return zzh;
            case 8:
                return zzi;
            case 9:
                return zzj;
            default:
                return null;
        }
    }

    @Override // java.lang.Enum
    public final String toString() {
        return Integer.toString(zza());
    }

    @Override // com.google.android.recaptcha.internal.zziv
    public final int zza() {
        if (this != zzk) {
            return this.zzn;
        }
        throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
    }
}
