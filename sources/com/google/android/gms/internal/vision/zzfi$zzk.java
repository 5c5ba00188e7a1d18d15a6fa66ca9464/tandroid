package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;

/* loaded from: classes.dex */
public final class zzfi$zzk extends zzjb implements zzkm {
    private static final zzfi$zzk zzj;
    private static volatile zzkx zzk;
    private int zzc;
    private long zze;
    private zzfi$zza zzf;
    private zzfi$zzg zzh;
    private zzfi$zzb zzi;
    private String zzd = "";
    private String zzg = "";

    public static final class zza extends zzjb.zzb implements zzkm {
        private zza() {
            super(zzfi$zzk.zzj);
        }

        /* synthetic */ zza(zzfk zzfkVar) {
            this();
        }
    }

    static {
        zzfi$zzk zzfi_zzk = new zzfi$zzk();
        zzj = zzfi_zzk;
        zzjb.zza(zzfi$zzk.class, zzfi_zzk);
    }

    private zzfi$zzk() {
    }

    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.vision.zzjb$zza, com.google.android.gms.internal.vision.zzkx] */
    @Override // com.google.android.gms.internal.vision.zzjb
    protected final Object zza(int i, Object obj, Object obj2) {
        zzfk zzfkVar = null;
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zzk();
            case 2:
                return new zza(zzfkVar);
            case 3:
                return zzjb.zza(zzj, "\u0001\u0006\u0000\u0001\u0001\u0011\u0006\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဂ\u0001\u0003ဉ\u0002\u0006ဈ\u0003\u0010ဉ\u0004\u0011ဉ\u0005", new Object[]{"zzc", "zzd", "zze", "zzf", "zzg", "zzh", "zzi"});
            case 4:
                return zzj;
            case 5:
                zzkx zzkxVar = zzk;
                zzkx zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zzk.class) {
                        try {
                            zzkx zzkxVar3 = zzk;
                            zzkx zzkxVar4 = zzkxVar3;
                            if (zzkxVar3 == null) {
                                ?? zzaVar = new zzjb.zza(zzj);
                                zzk = zzaVar;
                                zzkxVar4 = zzaVar;
                            }
                        } finally {
                        }
                    }
                }
                return zzkxVar2;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
