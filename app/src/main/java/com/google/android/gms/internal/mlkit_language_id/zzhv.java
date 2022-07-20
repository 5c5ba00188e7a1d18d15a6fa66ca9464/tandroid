package com.google.android.gms.internal.mlkit_language_id;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public class zzhv extends Enum<zzhv> {
    public static final zzhv zza;
    public static final zzhv zzb;
    public static final zzhv zzc;
    public static final zzhv zzd;
    public static final zzhv zze;
    public static final zzhv zzf;
    public static final zzhv zzg;
    public static final zzhv zzh;
    public static final zzhv zzi;
    public static final zzhv zzj;
    public static final zzhv zzk;
    public static final zzhv zzl;
    public static final zzhv zzm;
    public static final zzhv zzn;
    public static final zzhv zzo;
    public static final zzhv zzp;
    public static final zzhv zzq;
    public static final zzhv zzr;
    private static final /* synthetic */ zzhv[] zzu;
    private final zzhy zzs;
    private final int zzt;

    public static zzhv[] values() {
        return (zzhv[]) zzu.clone();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public zzhv(String str, int i, zzhy zzhyVar, int i2) {
        super(str, i);
        this.zzs = zzhyVar;
        this.zzt = i2;
    }

    public final zzhy zza() {
        return this.zzs;
    }

    static {
        zzhv zzhvVar = new zzhv("DOUBLE", 0, zzhy.DOUBLE, 1);
        zza = zzhvVar;
        zzhv zzhvVar2 = new zzhv("FLOAT", 1, zzhy.FLOAT, 5);
        zzb = zzhvVar2;
        zzhy zzhyVar = zzhy.LONG;
        zzhv zzhvVar3 = new zzhv("INT64", 2, zzhyVar, 0);
        zzc = zzhvVar3;
        zzhv zzhvVar4 = new zzhv("UINT64", 3, zzhyVar, 0);
        zzd = zzhvVar4;
        zzhy zzhyVar2 = zzhy.INT;
        zzhv zzhvVar5 = new zzhv("INT32", 4, zzhyVar2, 0);
        zze = zzhvVar5;
        zzhv zzhvVar6 = new zzhv("FIXED64", 5, zzhyVar, 1);
        zzf = zzhvVar6;
        zzhv zzhvVar7 = new zzhv("FIXED32", 6, zzhyVar2, 5);
        zzg = zzhvVar7;
        zzhv zzhvVar8 = new zzhv("BOOL", 7, zzhy.BOOLEAN, 0);
        zzh = zzhvVar8;
        zzhu zzhuVar = new zzhu("STRING", 8, zzhy.STRING, 2);
        zzi = zzhuVar;
        zzhy zzhyVar3 = zzhy.MESSAGE;
        zzhx zzhxVar = new zzhx("GROUP", 9, zzhyVar3, 3);
        zzj = zzhxVar;
        zzhw zzhwVar = new zzhw("MESSAGE", 10, zzhyVar3, 2);
        zzk = zzhwVar;
        zzhz zzhzVar = new zzhz("BYTES", 11, zzhy.BYTE_STRING, 2);
        zzl = zzhzVar;
        zzhv zzhvVar9 = new zzhv("UINT32", 12, zzhyVar2, 0);
        zzm = zzhvVar9;
        zzhv zzhvVar10 = new zzhv("ENUM", 13, zzhy.ENUM, 0);
        zzn = zzhvVar10;
        zzhv zzhvVar11 = new zzhv("SFIXED32", 14, zzhyVar2, 5);
        zzo = zzhvVar11;
        zzhv zzhvVar12 = new zzhv("SFIXED64", 15, zzhyVar, 1);
        zzp = zzhvVar12;
        zzhv zzhvVar13 = new zzhv("SINT32", 16, zzhyVar2, 0);
        zzq = zzhvVar13;
        zzhv zzhvVar14 = new zzhv("SINT64", 17, zzhyVar, 0);
        zzr = zzhvVar14;
        zzu = new zzhv[]{zzhvVar, zzhvVar2, zzhvVar3, zzhvVar4, zzhvVar5, zzhvVar6, zzhvVar7, zzhvVar8, zzhuVar, zzhxVar, zzhwVar, zzhzVar, zzhvVar9, zzhvVar10, zzhvVar11, zzhvVar12, zzhvVar13, zzhvVar14};
    }
}
