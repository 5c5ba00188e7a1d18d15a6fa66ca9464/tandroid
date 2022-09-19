package com.google.android.gms.internal.mlkit_language_id;
/* JADX WARN: Init of enum zzb can be incorrect */
/* JADX WARN: Init of enum zzi can be incorrect */
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public enum zzfb {
    zza(Void.class, Void.class, null),
    zzb(r1, Integer.class, 0),
    zzc(Long.TYPE, Long.class, 0L),
    zzd(Float.TYPE, Float.class, Float.valueOf(0.0f)),
    zze(Double.TYPE, Double.class, Double.valueOf(0.0d)),
    zzf(Boolean.TYPE, Boolean.class, Boolean.FALSE),
    zzg(String.class, String.class, ""),
    zzh(zzdn.class, zzdn.class, zzdn.zza),
    zzi(r1, Integer.class, null),
    zzj(Object.class, Object.class, null);
    
    private final Class<?> zzl;
    private final Object zzm;

    zzfb(Class cls, Class cls2, Object obj) {
        this.zzl = cls2;
        this.zzm = obj;
    }

    public final Class<?> zza() {
        return this.zzl;
    }

    static {
        Class cls = Integer.TYPE;
    }
}
