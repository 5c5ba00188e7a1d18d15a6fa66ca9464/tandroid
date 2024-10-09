package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;

/* loaded from: classes.dex */
public final class zzfi$zzc extends zzjb implements zzkm {
    private static final zzfi$zzc zzg;
    private static volatile zzkx zzh;
    private int zzc;
    private int zzd;
    private int zze;
    private String zzf = "";

    /* loaded from: classes.dex */
    public static final class zza extends zzjb.zzb implements zzkm {
        private zza() {
            super(zzfi$zzc.zzg);
        }

        /* synthetic */ zza(zzfk zzfkVar) {
            this();
        }
    }

    static {
        zzfi$zzc zzfi_zzc = new zzfi$zzc();
        zzg = zzfi_zzc;
        zzjb.zza(zzfi$zzc.class, zzfi_zzc);
    }

    private zzfi$zzc() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r4v17, types: [com.google.android.gms.internal.vision.zzkx, com.google.android.gms.internal.vision.zzjb$zza] */
    @Override // com.google.android.gms.internal.vision.zzjb
    public final Object zza(int i, Object obj, Object obj2) {
        zzfk zzfkVar = null;
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zzc();
            case 2:
                return new zza(zzfkVar);
            case 3:
                return zzjb.zza(zzg, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0000\u0000\u0001ဌ\u0000\u0002ဌ\u0001\u0003ဈ\u0002", new Object[]{"zzc", "zzd", zzgz.zzb(), "zze", zzha.zzb(), "zzf"});
            case 4:
                return zzg;
            case 5:
                zzkx zzkxVar = zzh;
                zzkx zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zzc.class) {
                        try {
                            zzkx zzkxVar3 = zzh;
                            zzkx zzkxVar4 = zzkxVar3;
                            if (zzkxVar3 == null) {
                                ?? zzaVar = new zzjb.zza(zzg);
                                zzh = zzaVar;
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
