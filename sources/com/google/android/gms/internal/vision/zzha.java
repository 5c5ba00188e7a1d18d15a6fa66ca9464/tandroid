package com.google.android.gms.internal.vision;
/* loaded from: classes.dex */
public enum zzha implements zzje {
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
    zzk(10),
    zzl(11),
    zzm(12),
    zzn(13);
    
    private static final zzjh zzo = new zzjh() { // from class: com.google.android.gms.internal.vision.zzhd
    };
    private final int zzp;

    zzha(int i) {
        this.zzp = i;
    }

    public static zzha zza(int i) {
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
            case 10:
                return zzk;
            case 11:
                return zzl;
            case 12:
                return zzm;
            case 13:
                return zzn;
            default:
                return null;
        }
    }

    public static zzjg zzb() {
        return zzhc.zza;
    }

    @Override // java.lang.Enum
    public final String toString() {
        return "<" + zzha.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzp + " name=" + name() + '>';
    }

    @Override // com.google.android.gms.internal.vision.zzje
    public final int zza() {
        return this.zzp;
    }
}
