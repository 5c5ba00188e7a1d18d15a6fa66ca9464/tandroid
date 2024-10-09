package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;

/* loaded from: classes.dex */
public final class zzfi$zzb extends zzjb implements zzkm {
    private static final zzji zzd = new zzfl();
    private static final zzfi$zzb zze;
    private static volatile zzkx zzf;
    private zzjj zzc = zzjb.zzn();

    /* loaded from: classes.dex */
    public static final class zza extends zzjb.zzb implements zzkm {
        private zza() {
            super(zzfi$zzb.zze);
        }

        /* synthetic */ zza(zzfk zzfkVar) {
            this();
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.gms.internal.vision.zzji, com.google.android.gms.internal.vision.zzfl] */
    static {
        zzfi$zzb zzfi_zzb = new zzfi$zzb();
        zze = zzfi_zzb;
        zzjb.zza(zzfi$zzb.class, zzfi_zzb);
    }

    private zzfi$zzb() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v14, types: [com.google.android.gms.internal.vision.zzkx, com.google.android.gms.internal.vision.zzjb$zza] */
    @Override // com.google.android.gms.internal.vision.zzjb
    public final Object zza(int i, Object obj, Object obj2) {
        zzfk zzfkVar = null;
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zzb();
            case 2:
                return new zza(zzfkVar);
            case 3:
                return zzjb.zza(zze, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001e", new Object[]{"zzc", zzgz.zzb()});
            case 4:
                return zze;
            case 5:
                zzkx zzkxVar = zzf;
                zzkx zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zzb.class) {
                        try {
                            zzkx zzkxVar3 = zzf;
                            zzkx zzkxVar4 = zzkxVar3;
                            if (zzkxVar3 == null) {
                                ?? zzaVar = new zzjb.zza(zze);
                                zzf = zzaVar;
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
