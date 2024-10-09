package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzay extends zzeo implements zzgb {
    private static final zzy$zzay zzi;
    private static volatile zzgj zzj;
    private int zzc;
    private zzy$zzaw zzd;
    private int zze;
    private long zzf;
    private long zzg;
    private zzew zzh = zzeo.zzl();

    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzay.zzi);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    static {
        zzy$zzay zzy_zzay = new zzy$zzay();
        zzi = zzy_zzay;
        zzeo.zza(zzy$zzay.class, zzy_zzay);
    }

    private zzy$zzay() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v18, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzay();
            case 2:
                return new zza(zzxVar);
            case 3:
                return zzeo.zza(zzi, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0001\u0000\u0001ဉ\u0000\u0002ဌ\u0001\u0003ဃ\u0002\u0004ဃ\u0003\u0005\u001b", new Object[]{"zzc", "zzd", "zze", zzai.zzb(), "zzf", "zzg", "zzh", zzy$zzbf.class});
            case 4:
                return zzi;
            case 5:
                zzgj zzgjVar = zzj;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzay.class) {
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
