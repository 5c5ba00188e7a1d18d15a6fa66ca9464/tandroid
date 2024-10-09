package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzj extends zzeo implements zzgb {
    private static final zzy$zzj zze;
    private static volatile zzgj zzf;
    private int zzc;
    private int zzd;

    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzj.zze);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    static {
        zzy$zzj zzy_zzj = new zzy$zzj();
        zze = zzy_zzj;
        zzeo.zza(zzy$zzj.class, zzy_zzj);
    }

    private zzy$zzj() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzj();
            case 2:
                return new zza(zzxVar);
            case 3:
                return zzeo.zza(zze, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0001ဌ\u0000", new Object[]{"zzc", "zzd", zzai.zzb()});
            case 4:
                return zze;
            case 5:
                zzgj zzgjVar = zzf;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzj.class) {
                        try {
                            zzgj zzgjVar3 = zzf;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zze);
                                zzf = zzaVar;
                                zzgjVar4 = zzaVar;
                            }
                        } finally {
                        }
                    }
                }
                return zzgjVar2;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
