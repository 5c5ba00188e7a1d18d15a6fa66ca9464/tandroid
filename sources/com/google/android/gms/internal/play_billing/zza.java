package com.google.android.gms.internal.play_billing;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public enum zza {
    zza(-999),
    zzb(-3),
    zzc(-2),
    zzd(-1),
    zze(0),
    zzf(1),
    zzg(2),
    zzh(3),
    zzi(4),
    zzj(5),
    zzk(6),
    zzl(7),
    zzm(8),
    zzn(11),
    zzo(12);
    
    private static final zzx zzp;
    private final int zzr;

    static {
        zza[] values;
        zzw zzwVar = new zzw();
        for (zza zzaVar : values()) {
            zzwVar.zza(Integer.valueOf(zzaVar.zzr), zzaVar);
        }
        zzp = zzwVar.zzb();
    }

    zza(int i) {
        this.zzr = i;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static zza zza(int i) {
        zzx zzxVar = zzp;
        Integer valueOf = Integer.valueOf(i);
        return !zzxVar.containsKey(valueOf) ? zza : (zza) zzxVar.get(valueOf);
    }
}
