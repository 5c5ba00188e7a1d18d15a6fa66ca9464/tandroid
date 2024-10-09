package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzk extends zzeo implements zzgb {
    private static final zzy$zzk zzi;
    private static volatile zzgj zzj;
    private int zzc;
    private zzy$zzaf zzd;
    private zzy$zzam zze;
    private long zzf;
    private float zzg;
    private zzy$zzae zzh;

    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzk.zzi);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    static {
        zzy$zzk zzy_zzk = new zzy$zzk();
        zzi = zzy_zzk;
        zzeo.zza(zzy$zzk.class, zzy_zzk);
    }

    private zzy$zzk() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzk();
            case 2:
                return new zza(zzxVar);
            case 3:
                return zzeo.zza(zzi, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဉ\u0001\u0003ဃ\u0002\u0004ခ\u0003\u0005ဉ\u0004", new Object[]{"zzc", "zzd", "zze", "zzf", "zzg", "zzh"});
            case 4:
                return zzi;
            case 5:
                zzgj zzgjVar = zzj;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzk.class) {
                        try {
                            zzgj zzgjVar3 = zzj;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzi);
                                zzj = zzaVar;
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
