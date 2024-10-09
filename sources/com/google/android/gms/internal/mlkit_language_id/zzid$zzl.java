package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzid$zzl extends zzeo.zzc implements zzgb {
    private static final zzid$zzl zzf;
    private static volatile zzgj zzg;
    private byte zze = 2;
    private zzew zzd = zzeo.zzl();

    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzd implements zzgb {
        private zza() {
            super(zzid$zzl.zzf);
        }

        /* synthetic */ zza(zzic zzicVar) {
            this();
        }
    }

    /* loaded from: classes.dex */
    public static final class zzb extends zzeo implements zzgb {
        private static final zzb zzg;
        private static volatile zzgj zzh;
        private int zzc;
        private int zzd;
        private int zze;
        private zzew zzf = zzeo.zzl();

        /* loaded from: classes.dex */
        public static final class zza extends zzeo.zzb implements zzgb {
            private zza() {
                super(zzb.zzg);
            }

            /* synthetic */ zza(zzic zzicVar) {
                this();
            }
        }

        static {
            zzb zzbVar = new zzb();
            zzg = zzbVar;
            zzeo.zza(zzb.class, zzbVar);
        }

        private zzb() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
        @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
        public final Object zza(int i, Object obj, Object obj2) {
            zzic zzicVar = null;
            switch (zzic.zza[i - 1]) {
                case 1:
                    return new zzb();
                case 2:
                    return new zza(zzicVar);
                case 3:
                    return zzeo.zza(zzg, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0001\u0000\u0001င\u0000\u0002င\u0001\u0003\u001b", new Object[]{"zzc", "zzd", "zze", "zzf", zzid$zzk.class});
                case 4:
                    return zzg;
                case 5:
                    zzgj zzgjVar = zzh;
                    zzgj zzgjVar2 = zzgjVar;
                    if (zzgjVar == null) {
                        synchronized (zzb.class) {
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

    static {
        zzid$zzl zzid_zzl = new zzid$zzl();
        zzf = zzid_zzl;
        zzeo.zza(zzid$zzl.class, zzid_zzl);
    }

    private zzid$zzl() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r3v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        zzic zzicVar = null;
        switch (zzic.zza[i - 1]) {
            case 1:
                return new zzid$zzl();
            case 2:
                return new zza(zzicVar);
            case 3:
                return zzeo.zza(zzf, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001b", new Object[]{"zzd", zzb.class});
            case 4:
                return zzf;
            case 5:
                zzgj zzgjVar = zzg;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzid$zzl.class) {
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
                return Byte.valueOf(this.zze);
            case 7:
                this.zze = (byte) (obj != null ? 1 : 0);
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
