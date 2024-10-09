package com.google.android.gms.internal.mlkit_language_id;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'zzb' uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:451)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByRegister(EnumVisitor.java:395)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:324)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:262)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes.dex */
public final class zzfb {
    public static final zzfb zza;
    public static final zzfb zzb;
    public static final zzfb zzc;
    public static final zzfb zzd;
    public static final zzfb zze;
    public static final zzfb zzf;
    public static final zzfb zzg;
    public static final zzfb zzh;
    public static final zzfb zzi;
    public static final zzfb zzj;
    private static final /* synthetic */ zzfb[] zzn;
    private final Class zzk;
    private final Class zzl;
    private final Object zzm;

    static {
        zzfb zzfbVar = new zzfb("VOID", 0, Void.class, Void.class, null);
        zza = zzfbVar;
        Class cls = Integer.TYPE;
        zzfb zzfbVar2 = new zzfb("INT", 1, cls, Integer.class, 0);
        zzb = zzfbVar2;
        zzfb zzfbVar3 = new zzfb("LONG", 2, Long.TYPE, Long.class, 0L);
        zzc = zzfbVar3;
        zzfb zzfbVar4 = new zzfb("FLOAT", 3, Float.TYPE, Float.class, Float.valueOf(0.0f));
        zzd = zzfbVar4;
        zzfb zzfbVar5 = new zzfb("DOUBLE", 4, Double.TYPE, Double.class, Double.valueOf(0.0d));
        zze = zzfbVar5;
        zzfb zzfbVar6 = new zzfb("BOOLEAN", 5, Boolean.TYPE, Boolean.class, Boolean.FALSE);
        zzf = zzfbVar6;
        zzfb zzfbVar7 = new zzfb("STRING", 6, String.class, String.class, "");
        zzg = zzfbVar7;
        zzfb zzfbVar8 = new zzfb("BYTE_STRING", 7, zzdn.class, zzdn.class, zzdn.zza);
        zzh = zzfbVar8;
        zzfb zzfbVar9 = new zzfb("ENUM", 8, cls, Integer.class, null);
        zzi = zzfbVar9;
        zzfb zzfbVar10 = new zzfb("MESSAGE", 9, Object.class, Object.class, null);
        zzj = zzfbVar10;
        zzn = new zzfb[]{zzfbVar, zzfbVar2, zzfbVar3, zzfbVar4, zzfbVar5, zzfbVar6, zzfbVar7, zzfbVar8, zzfbVar9, zzfbVar10};
    }

    private zzfb(String str, int i, Class cls, Class cls2, Object obj) {
        this.zzk = cls;
        this.zzl = cls2;
        this.zzm = obj;
    }

    public static zzfb[] values() {
        return (zzfb[]) zzn.clone();
    }

    public final Class zza() {
        return this.zzl;
    }
}
