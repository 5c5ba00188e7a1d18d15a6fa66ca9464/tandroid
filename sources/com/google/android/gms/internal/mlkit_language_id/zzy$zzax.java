package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzax extends zzeo implements zzgb {
    private static final zzy$zzax zzh;
    private static volatile zzgj zzi;
    private int zzc;
    private zzy$zzaf zzd;
    private zzy$zzae zze;
    private zzy$zzaw zzf;
    private zzew zzg = zzeo.zzl();

    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzax.zzh);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    static {
        zzy$zzax zzy_zzax = new zzy$zzax();
        zzh = zzy_zzax;
        zzeo.zza(zzy$zzax.class, zzy_zzax);
    }

    private zzy$zzax() {
    }

    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzax();
            case 2:
                return new zza(zzxVar);
            case 3:
                return zzeo.zza(zzh, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0001\u0000\u0001ဉ\u0000\u0002ဉ\u0001\u0003ဉ\u0002\u0004\u001b", new Object[]{"zzc", "zzd", "zze", "zzf", "zzg", zzy$zzan.class});
            case 4:
                return zzh;
            case 5:
                zzgj zzgjVar = zzi;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzax.class) {
                        try {
                            zzgj zzgjVar3 = zzi;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzh);
                                zzi = zzaVar;
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
