package com.google.android.gms.internal.clearcut;
/* JADX WARN: Init of enum zzky can be incorrect */
/* JADX WARN: Init of enum zzlf can be incorrect */
/* loaded from: classes.dex */
public enum zzcq {
    zzkx(Void.class, Void.class, null),
    zzky(r1, Integer.class, 0),
    zzkz(Long.TYPE, Long.class, 0L),
    zzla(Float.TYPE, Float.class, Float.valueOf(0.0f)),
    zzlb(Double.TYPE, Double.class, Double.valueOf(0.0d)),
    zzlc(Boolean.TYPE, Boolean.class, Boolean.FALSE),
    zzld(String.class, String.class, ""),
    zzle(zzbb.class, zzbb.class, zzbb.zzfi),
    zzlf(r1, Integer.class, null),
    zzlg(Object.class, Object.class, null);
    
    private final Class<?> zzli;
    private final Object zzlj;

    static {
        Class cls = Integer.TYPE;
    }

    zzcq(Class cls, Class cls2, Object obj) {
        this.zzli = cls2;
        this.zzlj = obj;
    }

    public final Class<?> zzbq() {
        return this.zzli;
    }
}
