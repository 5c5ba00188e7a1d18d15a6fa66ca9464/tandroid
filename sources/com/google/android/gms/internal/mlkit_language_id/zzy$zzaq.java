package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzaq extends zzeo implements zzgb {
    private static final zzy$zzaq zzf;
    private static volatile zzgj zzg;
    private int zzc;
    private zzy$zzat zzd;
    private int zze;

    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzaq.zzf);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    static {
        zzy$zzaq zzy_zzaq = new zzy$zzaq();
        zzf = zzy_zzaq;
        zzeo.zza(zzy$zzaq.class, zzy_zzaq);
    }

    private zzy$zzaq() {
    }

    /* JADX WARN: Type inference failed for: r3v14, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzaq();
            case 2:
                return new zza(zzxVar);
            case 3:
                return zzeo.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဌ\u0001", new Object[]{"zzc", "zzd", "zze", zzai.zzb()});
            case 4:
                return zzf;
            case 5:
                zzgj zzgjVar = zzg;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzaq.class) {
                        try {
                            zzgj zzgjVar3 = zzg;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzf);
                                zzg = zzaVar;
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
