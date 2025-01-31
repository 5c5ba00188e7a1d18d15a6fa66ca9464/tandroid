package com.google.android.recaptcha.internal;

/* JADX WARN: Enum visitor error
jadx.core.utils.exceptions.JadxRuntimeException: Init of enum field 'zzc' uses external variables
	at jadx.core.dex.visitors.EnumVisitor.createEnumFieldByConstructor(EnumVisitor.java:451)
	at jadx.core.dex.visitors.EnumVisitor.processEnumFieldByRegister(EnumVisitor.java:395)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromFilledArray(EnumVisitor.java:324)
	at jadx.core.dex.visitors.EnumVisitor.extractEnumFieldsFromInsn(EnumVisitor.java:262)
	at jadx.core.dex.visitors.EnumVisitor.convertToEnum(EnumVisitor.java:151)
	at jadx.core.dex.visitors.EnumVisitor.visit(EnumVisitor.java:100)
 */
/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* loaded from: classes.dex */
public final class zzmb {
    public static final zzmb zza;
    public static final zzmb zzb;
    public static final zzmb zzc;
    public static final zzmb zzd;
    public static final zzmb zze;
    public static final zzmb zzf;
    public static final zzmb zzg;
    public static final zzmb zzh;
    public static final zzmb zzi;
    public static final zzmb zzj;
    public static final zzmb zzk;
    public static final zzmb zzl;
    public static final zzmb zzm;
    public static final zzmb zzn;
    public static final zzmb zzo;
    public static final zzmb zzp;
    public static final zzmb zzq;
    public static final zzmb zzr;
    private static final /* synthetic */ zzmb[] zzs;
    private final zzmc zzt;

    static {
        zzmb zzmbVar = new zzmb("DOUBLE", 0, zzmc.zzd, 1);
        zza = zzmbVar;
        zzmb zzmbVar2 = new zzmb("FLOAT", 1, zzmc.zzc, 5);
        zzb = zzmbVar2;
        zzmc zzmcVar = zzmc.zzb;
        zzmb zzmbVar3 = new zzmb("INT64", 2, zzmcVar, 0);
        zzc = zzmbVar3;
        zzmb zzmbVar4 = new zzmb("UINT64", 3, zzmcVar, 0);
        zzd = zzmbVar4;
        zzmc zzmcVar2 = zzmc.zza;
        zzmb zzmbVar5 = new zzmb("INT32", 4, zzmcVar2, 0);
        zze = zzmbVar5;
        zzmb zzmbVar6 = new zzmb("FIXED64", 5, zzmcVar, 1);
        zzf = zzmbVar6;
        zzmb zzmbVar7 = new zzmb("FIXED32", 6, zzmcVar2, 5);
        zzg = zzmbVar7;
        zzmb zzmbVar8 = new zzmb("BOOL", 7, zzmc.zze, 0);
        zzh = zzmbVar8;
        zzmb zzmbVar9 = new zzmb("STRING", 8, zzmc.zzf, 2);
        zzi = zzmbVar9;
        zzmc zzmcVar3 = zzmc.zzi;
        zzmb zzmbVar10 = new zzmb("GROUP", 9, zzmcVar3, 3);
        zzj = zzmbVar10;
        zzmb zzmbVar11 = new zzmb("MESSAGE", 10, zzmcVar3, 2);
        zzk = zzmbVar11;
        zzmb zzmbVar12 = new zzmb("BYTES", 11, zzmc.zzg, 2);
        zzl = zzmbVar12;
        zzmb zzmbVar13 = new zzmb("UINT32", 12, zzmcVar2, 0);
        zzm = zzmbVar13;
        zzmb zzmbVar14 = new zzmb("ENUM", 13, zzmc.zzh, 0);
        zzn = zzmbVar14;
        zzmb zzmbVar15 = new zzmb("SFIXED32", 14, zzmcVar2, 5);
        zzo = zzmbVar15;
        zzmb zzmbVar16 = new zzmb("SFIXED64", 15, zzmcVar, 1);
        zzp = zzmbVar16;
        zzmb zzmbVar17 = new zzmb("SINT32", 16, zzmcVar2, 0);
        zzq = zzmbVar17;
        zzmb zzmbVar18 = new zzmb("SINT64", 17, zzmcVar, 0);
        zzr = zzmbVar18;
        zzs = new zzmb[]{zzmbVar, zzmbVar2, zzmbVar3, zzmbVar4, zzmbVar5, zzmbVar6, zzmbVar7, zzmbVar8, zzmbVar9, zzmbVar10, zzmbVar11, zzmbVar12, zzmbVar13, zzmbVar14, zzmbVar15, zzmbVar16, zzmbVar17, zzmbVar18};
    }

    private zzmb(String str, int i, zzmc zzmcVar, int i2) {
        this.zzt = zzmcVar;
    }

    public static zzmb[] values() {
        return (zzmb[]) zzs.clone();
    }

    public final zzmc zza() {
        return this.zzt;
    }
}
