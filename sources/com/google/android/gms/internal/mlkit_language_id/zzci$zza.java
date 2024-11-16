package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzci$zza extends zzeo implements zzgb {
    private static final zzex zzd = new zzcj();
    private static final zzci$zza zze;
    private static volatile zzgj zzf;
    private zzeu zzc = zzeo.zzk();

    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzci$zza.zze);
        }

        /* synthetic */ zza(zzch zzchVar) {
            this();
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.gms.internal.mlkit_language_id.zzcj, com.google.android.gms.internal.mlkit_language_id.zzex] */
    static {
        zzci$zza zzci_zza = new zzci$zza();
        zze = zzci_zza;
        zzeo.zza(zzci$zza.class, zzci_zza);
    }

    private zzci$zza() {
    }

    /* JADX WARN: Type inference failed for: r3v14, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzch zzchVar = null;
        switch (zzch.zza[i - 1]) {
            case 1:
                return new zzci$zza();
            case 2:
                return new zza(zzchVar);
            case 3:
                return zzeo.zza(zze, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001e", new Object[]{"zzc", zzdd.zzb()});
            case 4:
                return zze;
            case 5:
                zzgj zzgjVar = zzf;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzci$zza.class) {
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
