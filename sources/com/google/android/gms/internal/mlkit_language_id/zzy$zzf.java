package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzf extends zzeo implements zzgb {
    private static final zzy$zzf zzg;
    private static volatile zzgj zzh;
    private int zzc;
    private zzb zzd;
    private int zze;
    private zzy$zzab zzf;

    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzf.zzg);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    /* loaded from: classes.dex */
    public static final class zzb extends zzeo implements zzgb {
        private static final zzb zzi;
        private static volatile zzgj zzj;
        private int zzc;
        private int zzd;
        private boolean zze;
        private boolean zzf;
        private zzy$zzae zzg;
        private zzy$zzaw zzh;

        /* loaded from: classes.dex */
        public static final class zza extends zzeo.zzb implements zzgb {
            private zza() {
                super(zzb.zzi);
            }

            /* synthetic */ zza(zzx zzxVar) {
                this();
            }
        }

        static {
            zzb zzbVar = new zzb();
            zzi = zzbVar;
            zzeo.zza(zzb.class, zzbVar);
        }

        private zzb() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Type inference failed for: r3v18, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
        @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
        public final Object zza(int i, Object obj, Object obj2) {
            zzx zzxVar = null;
            switch (zzx.zza[i - 1]) {
                case 1:
                    return new zzb();
                case 2:
                    return new zza(zzxVar);
                case 3:
                    return zzeo.zza(zzi, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0000\u0000\u0001ဌ\u0000\u0002ဇ\u0001\u0003ဇ\u0002\u0004ဉ\u0003\u0005ဉ\u0004", new Object[]{"zzc", "zzd", zzai.zzb(), "zze", "zzf", "zzg", "zzh"});
                case 4:
                    return zzi;
                case 5:
                    zzgj zzgjVar = zzj;
                    zzgj zzgjVar2 = zzgjVar;
                    if (zzgjVar == null) {
                        synchronized (zzb.class) {
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

    static {
        zzy$zzf zzy_zzf = new zzy$zzf();
        zzg = zzy_zzf;
        zzeo.zza(zzy$zzf.class, zzy_zzf);
    }

    private zzy$zzf() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzf();
            case 2:
                return new zza(zzxVar);
            case 3:
                return zzeo.zza(zzg, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဋ\u0001\u0003ဉ\u0002", new Object[]{"zzc", "zzd", "zze", "zzf"});
            case 4:
                return zzg;
            case 5:
                zzgj zzgjVar = zzh;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzf.class) {
                        try {
                            zzgj zzgjVar3 = zzh;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzg);
                                zzh = zzaVar;
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
