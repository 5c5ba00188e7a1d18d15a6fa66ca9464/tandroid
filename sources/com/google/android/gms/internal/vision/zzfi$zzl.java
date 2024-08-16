package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;
/* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
/* loaded from: classes.dex */
public final class zzfi$zzl extends zzjb<zzfi$zzl, zza> implements zzkm {
    private static final zzfi$zzl zzf;
    private static volatile zzkx<zzfi$zzl> zzg;
    private int zzc;
    private long zzd;
    private long zze;

    private zzfi$zzl() {
    }

    /* compiled from: com.google.android.gms:play-services-vision-common@@19.1.3 */
    /* loaded from: classes.dex */
    public static final class zza extends zzjb.zzb<zzfi$zzl, zza> implements zzkm {
        private zza() {
            super(zzfi$zzl.zzf);
        }

        /* synthetic */ zza(zzfk zzfkVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.vision.zzjb$zza, com.google.android.gms.internal.vision.zzkx<com.google.android.gms.internal.vision.zzfi$zzl>] */
    @Override // com.google.android.gms.internal.vision.zzjb
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zzl();
            case 2:
                return new zza(null);
            case 3:
                return zzjb.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဂ\u0000\u0002ဂ\u0001", new Object[]{"zzc", "zzd", "zze"});
            case 4:
                return zzf;
            case 5:
                zzkx<zzfi$zzl> zzkxVar = zzg;
                zzkx<zzfi$zzl> zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zzl.class) {
                        try {
                            zzkx<zzfi$zzl> zzkxVar3 = zzg;
                            zzkx<zzfi$zzl> zzkxVar4 = zzkxVar3;
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

    static {
        zzfi$zzl zzfi_zzl = new zzfi$zzl();
        zzf = zzfi_zzl;
        zzjb.zza(zzfi$zzl.class, zzfi_zzl);
    }
}
