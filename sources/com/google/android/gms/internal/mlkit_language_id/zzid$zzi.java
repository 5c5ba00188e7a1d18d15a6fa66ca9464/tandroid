package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
/* compiled from: com.google.mlkit:language-id@@16.1.1 */
/* loaded from: classes.dex */
public final class zzid$zzi extends zzeo<zzid$zzi, zza> implements zzgb {
    private static final zzid$zzi zzd;
    private static volatile zzgj<zzid$zzi> zze;
    private zzew<zzb> zzc = zzeo.zzl();

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public static final class zzb extends zzeo<zzb, zza> implements zzgb {
        private static final zzb zzh;
        private static volatile zzgj<zzb> zzi;
        private int zzc;
        private int zze;
        private long zzg;
        private String zzd = "";
        private String zzf = "";

        private zzb() {
        }

        /* compiled from: com.google.mlkit:language-id@@16.1.1 */
        /* loaded from: classes.dex */
        public static final class zza extends zzeo.zzb<zzb, zza> implements zzgb {
            private zza() {
                super(zzb.zzh);
            }

            /* synthetic */ zza(zzic zzicVar) {
                this();
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj<com.google.android.gms.internal.mlkit_language_id.zzid$zzi$zzb>, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
        @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
        public final Object zza(int i, Object obj, Object obj2) {
            switch (zzic.zza[i - 1]) {
                case 1:
                    return new zzb();
                case 2:
                    return new zza(null);
                case 3:
                    return zzeo.zza(zzh, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001ဈ\u0000\u0002င\u0001\u0003ဈ\u0002\u0004ဂ\u0003", new Object[]{"zzc", "zzd", "zze", "zzf", "zzg"});
                case 4:
                    return zzh;
                case 5:
                    zzgj<zzb> zzgjVar = zzi;
                    zzgj<zzb> zzgjVar2 = zzgjVar;
                    if (zzgjVar == null) {
                        synchronized (zzb.class) {
                            try {
                                zzgj<zzb> zzgjVar3 = zzi;
                                zzgj<zzb> zzgjVar4 = zzgjVar3;
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

        static {
            zzb zzbVar = new zzb();
            zzh = zzbVar;
            zzeo.zza(zzb.class, zzbVar);
        }
    }

    private zzid$zzi() {
    }

    /* compiled from: com.google.mlkit:language-id@@16.1.1 */
    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb<zzid$zzi, zza> implements zzgb {
        private zza() {
            super(zzid$zzi.zzd);
        }

        /* synthetic */ zza(zzic zzicVar) {
            this();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj<com.google.android.gms.internal.mlkit_language_id.zzid$zzi>, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzic.zza[i - 1]) {
            case 1:
                return new zzid$zzi();
            case 2:
                return new zza(null);
            case 3:
                return zzeo.zza(zzd, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001b", new Object[]{"zzc", zzb.class});
            case 4:
                return zzd;
            case 5:
                zzgj<zzid$zzi> zzgjVar = zze;
                zzgj<zzid$zzi> zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzid$zzi.class) {
                        try {
                            zzgj<zzid$zzi> zzgjVar3 = zze;
                            zzgj<zzid$zzi> zzgjVar4 = zzgjVar3;
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
        zzid$zzi zzid_zzi = new zzid$zzi();
        zzd = zzid_zzi;
        zzeo.zza(zzid$zzi.class, zzid_zzi);
    }
}
