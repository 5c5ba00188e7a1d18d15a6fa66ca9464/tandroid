package com.google.android.gms.internal.vision;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes.dex */
public class zzml extends Enum<zzml> {
    public static final zzml zza;
    public static final zzml zzb;
    public static final zzml zzc;
    public static final zzml zzd;
    public static final zzml zze;
    public static final zzml zzf;
    public static final zzml zzg;
    public static final zzml zzh;
    public static final zzml zzi;
    public static final zzml zzj;
    public static final zzml zzk;
    public static final zzml zzl;
    public static final zzml zzm;
    public static final zzml zzn;
    public static final zzml zzo;
    public static final zzml zzp;
    public static final zzml zzq;
    public static final zzml zzr;
    private static final /* synthetic */ zzml[] zzu;
    private final zzmo zzs;
    private final int zzt;

    public static zzml[] values() {
        return (zzml[]) zzu.clone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public zzml(String str, int i, zzmo zzmoVar, int i2) {
        super(str, i);
        this.zzs = zzmoVar;
        this.zzt = i2;
    }

    public final zzmo zza() {
        return this.zzs;
    }

    static {
        zzml zzmlVar = new zzml("DOUBLE", 0, zzmo.DOUBLE, 1);
        zza = zzmlVar;
        zzml zzmlVar2 = new zzml("FLOAT", 1, zzmo.FLOAT, 5);
        zzb = zzmlVar2;
        zzmo zzmoVar = zzmo.LONG;
        zzml zzmlVar3 = new zzml("INT64", 2, zzmoVar, 0);
        zzc = zzmlVar3;
        zzml zzmlVar4 = new zzml("UINT64", 3, zzmoVar, 0);
        zzd = zzmlVar4;
        zzmo zzmoVar2 = zzmo.INT;
        zzml zzmlVar5 = new zzml("INT32", 4, zzmoVar2, 0);
        zze = zzmlVar5;
        zzml zzmlVar6 = new zzml("FIXED64", 5, zzmoVar, 1);
        zzf = zzmlVar6;
        zzml zzmlVar7 = new zzml("FIXED32", 6, zzmoVar2, 5);
        zzg = zzmlVar7;
        zzml zzmlVar8 = new zzml("BOOL", 7, zzmo.BOOLEAN, 0);
        zzh = zzmlVar8;
        zzmk zzmkVar = new zzmk("STRING", 8, zzmo.STRING, 2);
        zzi = zzmkVar;
        zzmo zzmoVar3 = zzmo.MESSAGE;
        zzmn zzmnVar = new zzmn("GROUP", 9, zzmoVar3, 3);
        zzj = zzmnVar;
        zzmm zzmmVar = new zzmm("MESSAGE", 10, zzmoVar3, 2);
        zzk = zzmmVar;
        zzmp zzmpVar = new zzmp("BYTES", 11, zzmo.BYTE_STRING, 2);
        zzl = zzmpVar;
        zzml zzmlVar9 = new zzml("UINT32", 12, zzmoVar2, 0);
        zzm = zzmlVar9;
        zzml zzmlVar10 = new zzml("ENUM", 13, zzmo.ENUM, 0);
        zzn = zzmlVar10;
        zzml zzmlVar11 = new zzml("SFIXED32", 14, zzmoVar2, 5);
        zzo = zzmlVar11;
        zzml zzmlVar12 = new zzml("SFIXED64", 15, zzmoVar, 1);
        zzp = zzmlVar12;
        zzml zzmlVar13 = new zzml("SINT32", 16, zzmoVar2, 0);
        zzq = zzmlVar13;
        zzml zzmlVar14 = new zzml("SINT64", 17, zzmoVar, 0);
        zzr = zzmlVar14;
        zzu = new zzml[]{zzmlVar, zzmlVar2, zzmlVar3, zzmlVar4, zzmlVar5, zzmlVar6, zzmlVar7, zzmlVar8, zzmkVar, zzmnVar, zzmmVar, zzmpVar, zzmlVar9, zzmlVar10, zzmlVar11, zzmlVar12, zzmlVar13, zzmlVar14};
    }
}
