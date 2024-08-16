package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes.dex */
public final class zzfi$zzm extends zzjb<zzfi$zzm, zza> implements zzkm {
    private static final zzfi$zzm zzf;
    private static volatile zzkx<zzfi$zzm> zzg;
    private int zzc;
    private int zzd;
    private int zze;

    private zzfi$zzm() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.vision.zzjb$zza, com.google.android.gms.internal.vision.zzkx<com.google.android.gms.internal.vision.zzfi$zzm>] */
    @Override // com.google.android.gms.internal.vision.zzjb
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zzm();
            case 2:
                return new zza(null);
            case 3:
                return zzjb.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001င\u0000\u0002င\u0001", new Object[]{"zzc", "zzd", "zze"});
            case 4:
                return zzf;
            case 5:
                zzkx<zzfi$zzm> zzkxVar = zzg;
                zzkx<zzfi$zzm> zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zzm.class) {
                        try {
                            zzkx<zzfi$zzm> zzkxVar3 = zzg;
                            zzkx<zzfi$zzm> zzkxVar4 = zzkxVar3;
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

    /* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
    /* loaded from: classes.dex */
    public static final class zza extends zzjb.zzb<zzfi$zzm, zza> implements zzkm {
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
}
