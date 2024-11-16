package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzap extends zzeo implements zzgb {
    private static final zzy$zzap zzj;
    private static volatile zzgj zzk;
    private int zzc;
    private zzy$zzaf zzd;
    private zzci$zzb zze;
    private zzy$zzae zzf;
    private zzy$zzac zzg;
    private int zzh;
    private int zzi;

    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzap.zzj);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    static {
        zzy$zzap zzy_zzap = new zzy$zzap();
        zzj = zzy_zzap;
        zzeo.zza(zzy$zzap.class, zzy_zzap);
    }

    private zzy$zzap() {
    }

    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzap();
            case 2:
                return new zza(zzxVar);
            case 3:
                return zzeo.zza(zzj, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဉ\u0001\u0003ဉ\u0002\u0004ဉ\u0003\u0005ဋ\u0004\u0006ဋ\u0005", new Object[]{"zzc", "zzd", "zze", "zzf", "zzg", "zzh", "zzi"});
            case 4:
                return zzj;
            case 5:
                zzgj zzgjVar = zzk;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzap.class) {
                        try {
                            zzgj zzgjVar3 = zzk;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzj);
                                zzk = zzaVar;
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
