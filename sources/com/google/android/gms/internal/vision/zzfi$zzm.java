package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;

/* loaded from: classes.dex */
public final class zzfi$zzm extends zzjb implements zzkm {
    private static final zzfi$zzm zzf;
    private static volatile zzkx zzg;
    private int zzc;
    private int zzd;
    private int zze;

    /* loaded from: classes.dex */
    public static final class zza extends zzjb.zzb implements zzkm {
        private zza() {
            super(zzfi$zzm.zzf);
        }

        /* synthetic */ zza(zzfk zzfkVar) {
            this();
        }
    }

    static {
        zzfi$zzm zzfi_zzm = new zzfi$zzm();
        zzf = zzfi_zzm;
        zzjb.zza(zzfi$zzm.class, zzfi_zzm);
    }

    private zzfi$zzm() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.vision.zzkx, com.google.android.gms.internal.vision.zzjb$zza] */
    @Override // com.google.android.gms.internal.vision.zzjb
    public final Object zza(int i, Object obj, Object obj2) {
        zzfk zzfkVar = null;
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zzm();
            case 2:
                return new zza(zzfkVar);
            case 3:
                return zzjb.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001င\u0000\u0002င\u0001", new Object[]{"zzc", "zzd", "zze"});
            case 4:
                return zzf;
            case 5:
                zzkx zzkxVar = zzg;
                zzkx zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zzm.class) {
                        try {
                            zzkx zzkxVar3 = zzg;
                            zzkx zzkxVar4 = zzkxVar3;
                            if (zzkxVar3 == null) {
                                ?? zzaVar = new zzjb.zza(zzf);
                                zzg = zzaVar;
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
