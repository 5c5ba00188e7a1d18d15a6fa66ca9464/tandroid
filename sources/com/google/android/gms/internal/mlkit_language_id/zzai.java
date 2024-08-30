package com.google.android.gms.internal.mlkit_language_id;
/* loaded from: classes.dex */
public enum zzai implements zzet {
    zza(0),
    zzc(1),
    zzd(2),
    zze(3),
    zzf(4),
    zzg(6),
    zzh(7),
    zzi(8),
    zzj(9),
    zzk(5),
    zzl(100),
    zzm(101),
    zzn(102),
    zzo(103),
    zzp(104),
    zzq(105),
    zzr(106),
    zzs(107),
    zzt(108),
    zzu(109),
    zzv(110),
    zzw(111),
    zzx(112),
    zzy(113),
    zzz(114),
    zzaa(115),
    zzab(116),
    zzb(9999);
    
    private static final zzes zzac = new zzes() { // from class: com.google.android.gms.internal.mlkit_language_id.zzah
    };
    private final int zzad;

    zzai(int i) {
        this.zzad = i;
    }

    public static zzev zzb() {
        return zzak.zza;
    }

    @Override // java.lang.Enum
    public final String toString() {
        return "<" + zzai.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzad + " name=" + name() + '>';
    }

    @Override // com.google.android.gms.internal.mlkit_language_id.zzet
    public final int zza() {
        return this.zzad;
    }
}
