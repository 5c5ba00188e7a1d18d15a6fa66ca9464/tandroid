package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;

/* loaded from: classes.dex */
public final class zzfi$zzn extends zzjb implements zzkm {
    private static final zzfi$zzn zzh;
    private static volatile zzkx zzi;
    private int zzc;
    private zzfi$zzd zzd;
    private int zze;
    private zzfi$zzh zzf;
    private zzfi$zzc zzg;

    public static final class zza extends zzjb.zzb implements zzkm {
        private zza() {
            super(zzfi$zzn.zzh);
        }

        /* synthetic */ zza(zzfk zzfkVar) {
            this();
        }
    }

    static {
        zzfi$zzn zzfi_zzn = new zzfi$zzn();
        zzh = zzfi_zzn;
        zzjb.zza(zzfi$zzn.class, zzfi_zzn);
    }

    private zzfi$zzn() {
    }

    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.vision.zzjb$zza, com.google.android.gms.internal.vision.zzkx] */
    @Override // com.google.android.gms.internal.vision.zzjb
    protected final Object zza(int i, Object obj, Object obj2) {
        zzfk zzfkVar = null;
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zzn();
            case 2:
                return new zza(zzfkVar);
            case 3:
                return zzjb.zza(zzh, "\u0001\u0004\u0000\u0001\u0001\u0011\u0004\u0000\u0000\u0000\u0001ဉ\u0000\u0002င\u0001\u0010ဉ\u0002\u0011ဉ\u0003", new Object[]{"zzc", "zzd", "zze", "zzf", "zzg"});
            case 4:
                return zzh;
            case 5:
                zzkx zzkxVar = zzi;
                zzkx zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zzn.class) {
                        try {
                            zzkx zzkxVar3 = zzi;
                            zzkx zzkxVar4 = zzkxVar3;
                            if (zzkxVar3 == null) {
                                ?? zzaVar = new zzjb.zza(zzh);
                                zzi = zzaVar;
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
