package com.google.android.gms.internal.play_billing;

/* loaded from: classes.dex */
public final class zzfj extends zzcb implements zzdg {
    private static final zzfj zzb;
    private int zzd;
    private int zze;
    private int zzg;
    private String zzf = "";
    private String zzh = "";

    static {
        zzfj zzfjVar = new zzfj();
        zzb = zzfjVar;
        zzcb.zzp(zzfj.class, zzfjVar);
    }

    private zzfj() {
    }

    public static zzfh zzv() {
        return (zzfh) zzb.zzg();
    }

    static /* synthetic */ void zzx(zzfj zzfjVar, int i) {
        zzfjVar.zzd |= 1;
        zzfjVar.zze = i;
    }

    static /* synthetic */ void zzy(zzfj zzfjVar, String str) {
        str.getClass();
        zzfjVar.zzd |= 2;
        zzfjVar.zzf = str;
    }

    static /* synthetic */ void zzz(zzfj zzfjVar, int i) {
        zzfjVar.zzg = i - 1;
        zzfjVar.zzd |= 4;
    }

    @Override // com.google.android.gms.internal.play_billing.zzcb
    protected final Object zzu(int i, Object obj, Object obj2) {
        int i2 = i - 1;
        if (i2 == 0) {
            return (byte) 1;
        }
        if (i2 == 2) {
            return zzcb.zzm(zzb, "\u0001\u0004\u0000\u0001\u0001\u0005\u0004\u0000\u0000\u0000\u0001င\u0000\u0002ဈ\u0001\u0004᠌\u0002\u0005ဈ\u0003", new Object[]{"zzd", "zze", "zzf", "zzg", zzfi.zza, "zzh"});
        }
        if (i2 == 3) {
            return new zzfj();
        }
        zzfg zzfgVar = null;
        if (i2 == 4) {
            return new zzfh(zzfgVar);
        }
        if (i2 != 5) {
            return null;
        }
        return zzb;
    }
}
