package com.google.android.recaptcha.internal;

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
public final class zzjf {
    public static final zzjf zza;
    public static final zzjf zzb;
    public static final zzjf zzc;
    public static final zzjf zzd;
    public static final zzjf zze;
    public static final zzjf zzf;
    public static final zzjf zzg;
    public static final zzjf zzh;
    public static final zzjf zzi;
    public static final zzjf zzj;
    private static final /* synthetic */ zzjf[] zzk;
    private final Class zzl;
    private final Class zzm;
    private final Object zzn;

    static {
        zzjf zzjfVar = new zzjf("VOID", 0, Void.class, Void.class, null);
        zza = zzjfVar;
        Class cls = Integer.TYPE;
        zzjf zzjfVar2 = new zzjf("INT", 1, cls, Integer.class, 0);
        zzb = zzjfVar2;
        zzjf zzjfVar3 = new zzjf("LONG", 2, Long.TYPE, Long.class, 0L);
        zzc = zzjfVar3;
        zzjf zzjfVar4 = new zzjf("FLOAT", 3, Float.TYPE, Float.class, Float.valueOf(0.0f));
        zzd = zzjfVar4;
        zzjf zzjfVar5 = new zzjf("DOUBLE", 4, Double.TYPE, Double.class, Double.valueOf(0.0d));
        zze = zzjfVar5;
        zzjf zzjfVar6 = new zzjf("BOOLEAN", 5, Boolean.TYPE, Boolean.class, Boolean.FALSE);
        zzf = zzjfVar6;
        zzjf zzjfVar7 = new zzjf("STRING", 6, String.class, String.class, "");
        zzg = zzjfVar7;
        zzjf zzjfVar8 = new zzjf("BYTE_STRING", 7, zzgw.class, zzgw.class, zzgw.zzb);
        zzh = zzjfVar8;
        zzjf zzjfVar9 = new zzjf("ENUM", 8, cls, Integer.class, null);
        zzi = zzjfVar9;
        zzjf zzjfVar10 = new zzjf("MESSAGE", 9, Object.class, Object.class, null);
        zzj = zzjfVar10;
        zzk = new zzjf[]{zzjfVar, zzjfVar2, zzjfVar3, zzjfVar4, zzjfVar5, zzjfVar6, zzjfVar7, zzjfVar8, zzjfVar9, zzjfVar10};
    }

    private zzjf(String str, int i, Class cls, Class cls2, Object obj) {
        this.zzl = cls;
        this.zzm = cls2;
        this.zzn = obj;
    }

    public static zzjf[] values() {
        return (zzjf[]) zzk.clone();
    }

    public final Class zza() {
        return this.zzm;
    }
}
