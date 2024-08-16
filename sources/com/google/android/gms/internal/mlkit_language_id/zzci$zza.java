package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final class zzci$zza extends zzeo<zzci$zza, zza> implements zzgb {
    private static final zzex<Integer, zzdd> zzd = new zzcj();
    private static final zzci$zza zze;
    private static volatile zzgj<zzci$zza> zzf;
    private zzeu zzc = zzeo.zzk();

    private zzci$zza() {
    }

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb<zzci$zza, zza> implements zzgb {
        private zza() {
            super(zzci$zza.zze);
        }

        /* synthetic */ zza(zzch zzchVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj<com.google.android.gms.internal.mlkit_language_id.zzci$zza>, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzch.zza[i - 1]) {
            case 1:
                return new zzci$zza();
            case 2:
                return new zza(null);
            case 3:
                return zzeo.zza(zze, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001e", new Object[]{"zzc", zzdd.zzb()});
            case 4:
                return zze;
            case 5:
                zzgj<zzci$zza> zzgjVar = zzf;
                zzgj<zzci$zza> zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzci$zza.class) {
                        try {
                            zzgj<zzci$zza> zzgjVar3 = zzf;
                            zzgj<zzci$zza> zzgjVar4 = zzgjVar3;
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

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.gms.internal.mlkit_language_id.zzcj, com.google.android.gms.internal.mlkit_language_id.zzex<java.lang.Integer, com.google.android.gms.internal.mlkit_language_id.zzdd>] */
    static {
        zzci$zza zzci_zza = new zzci$zza();
        zze = zzci_zza;
        zzeo.zza(zzci$zza.class, zzci_zza);
    }
}
