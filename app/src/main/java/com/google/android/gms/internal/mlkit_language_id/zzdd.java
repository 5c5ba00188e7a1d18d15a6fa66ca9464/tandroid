package com.google.android.gms.internal.mlkit_language_id;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public enum zzdd implements zzet {
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
    zzn(13),
    zzo(14),
    zzp(16);
    
    private final int zzr;

    @Override // com.google.android.gms.internal.mlkit_language_id.zzet
    public final int zza() {
        return this.zzr;
    }

    public static zzev zzb() {
        return zzdf.zza;
    }

    @Override // java.lang.Enum
    public final String toString() {
        return "<" + zzdd.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzr + " name=" + name() + '>';
    }

    zzdd(int i) {
        this.zzr = i;
    }

    static {
        new Object() { // from class: com.google.android.gms.internal.mlkit_language_id.zzdc
        };
    }
}
