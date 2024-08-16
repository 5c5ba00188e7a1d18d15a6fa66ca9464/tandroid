package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final class zzid$zzg extends zzeo<zzid$zzg, zza> implements zzgb {
    private static final zzid$zzg zzd;
    private static volatile zzgj<zzid$zzg> zze;
    private zzeu zzc = zzeo.zzk();

    private zzid$zzg() {
    }

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb<zzid$zzg, zza> implements zzgb {
        private zza() {
            super(zzid$zzg.zzd);
        }

        /* synthetic */ zza(zzic zzicVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r1v13, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj<com.google.android.gms.internal.mlkit_language_id.zzid$zzg>] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzic.zza[i - 1]) {
            case 1:
                return new zzid$zzg();
            case 2:
                return new zza(null);
            case 3:
                return zzeo.zza(zzd, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u0016", new Object[]{"zzc"});
            case 4:
                return zzd;
            case 5:
                zzgj<zzid$zzg> zzgjVar = zze;
                zzgj<zzid$zzg> zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzid$zzg.class) {
                        try {
                            zzgj<zzid$zzg> zzgjVar3 = zze;
                            zzgj<zzid$zzg> zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzd);
                                zze = zzaVar;
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

    static {
        zzid$zzg zzid_zzg = new zzid$zzg();
        zzd = zzid_zzg;
        zzeo.zza(zzid$zzg.class, zzid_zzg);
    }
}
