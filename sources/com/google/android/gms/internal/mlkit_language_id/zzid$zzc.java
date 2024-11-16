package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzid$zzc extends zzeo implements zzgb {
    private static final zzid$zzc zzm;
    private static volatile zzgj zzn;
    private int zzc;
    private int zzd;
    private int zze;
    private zzid$zza zzh;
    private zzd zzi;
    private int zzj;
    private int zzl;
    private String zzf = "";
    private String zzg = "";
    private zzew zzk = zzeo.zzl();

    public enum zza implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3),
        zze(4),
        zzf(5);

        private static final zzes zzg = new zzig();
        private final int zzh;

        zza(int i) {
            this.zzh = i;
        }

        public static zzev zzb() {
            return zzih.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzh + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzh;
        }
    }

    public static final class zzb extends zzeo.zzb implements zzgb {
        private zzb() {
            super(zzid$zzc.zzm);
        }

        /* synthetic */ zzb(zzic zzicVar) {
            this();
        }
    }

    public static final class zzc extends zzeo implements zzgb {
        private static final zzc zzf;
        private static volatile zzgj zzg;
        private int zzc;
        private int zzd;
        private int zze;

        public static final class zza extends zzeo.zzb implements zzgb {
            private zza() {
                super(zzc.zzf);
            }

            /* synthetic */ zza(zzic zzicVar) {
                this();
            }
        }

        static {
            zzc zzcVar = new zzc();
            zzf = zzcVar;
            zzeo.zza(zzc.class, zzcVar);
        }

        private zzc() {
        }

        /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
        @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
        protected final Object zza(int i, Object obj, Object obj2) {
            zzic zzicVar = null;
            switch (zzic.zza[i - 1]) {
                case 1:
                    return new zzc();
                case 2:
                    return new zza(zzicVar);
                case 3:
                    return zzeo.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001င\u0000\u0002င\u0001", new Object[]{"zzc", "zzd", "zze"});
                case 4:
                    return zzf;
                case 5:
                    zzgj zzgjVar = zzg;
                    zzgj zzgjVar2 = zzgjVar;
                    if (zzgjVar == null) {
                        synchronized (zzc.class) {
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

    public static final class zzd extends zzeo implements zzgb {
        private static final zzd zzg;
        private static volatile zzgj zzh;
        private int zzc;
        private zzc zzd;
        private zzc zze;
        private boolean zzf;

        public static final class zza extends zzeo.zzb implements zzgb {
            private zza() {
                super(zzd.zzg);
            }

            /* synthetic */ zza(zzic zzicVar) {
                this();
            }
        }

        static {
            zzd zzdVar = new zzd();
            zzg = zzdVar;
            zzeo.zza(zzd.class, zzdVar);
        }

        private zzd() {
        }

        /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
        @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
        protected final Object zza(int i, Object obj, Object obj2) {
            zzic zzicVar = null;
            switch (zzic.zza[i - 1]) {
                case 1:
                    return new zzd();
                case 2:
                    return new zza(zzicVar);
                case 3:
                    return zzeo.zza(zzg, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဉ\u0001\u0003ဇ\u0002", new Object[]{"zzc", "zzd", "zze", "zzf"});
                case 4:
                    return zzg;
                case 5:
                    zzgj zzgjVar = zzh;
                    zzgj zzgjVar2 = zzgjVar;
                    if (zzgjVar == null) {
                        synchronized (zzd.class) {
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

    public enum zze implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3),
        zze(4),
        zzf(5);

        private static final zzes zzg = new zzii();
        private final int zzh;

        zze(int i) {
            this.zzh = i;
        }

        public static zzev zzb() {
            return zzik.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zze.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzh + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzh;
        }
    }

    static {
        zzid$zzc zzid_zzc = new zzid$zzc();
        zzm = zzid_zzc;
        zzeo.zza(zzid$zzc.class, zzid_zzc);
    }

    private zzid$zzc() {
    }

    /* JADX WARN: Type inference failed for: r4v24, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzic zzicVar = null;
        switch (zzic.zza[i - 1]) {
            case 1:
                return new zzid$zzc();
            case 2:
                return new zzb(zzicVar);
            case 3:
                return zzeo.zza(zzm, "\u0001\t\u0000\u0001\u0001\t\t\u0000\u0001\u0000\u0001ဌ\u0000\u0002ဌ\u0001\u0003ဈ\u0002\u0004ဈ\u0003\u0005ဉ\u0004\u0006ဉ\u0005\u0007င\u0006\b\u001b\tင\u0007", new Object[]{"zzc", "zzd", zza.zzb(), "zze", zze.zzb(), "zzf", "zzg", "zzh", "zzi", "zzj", "zzk", zzid$zzg.class, "zzl"});
            case 4:
                return zzm;
            case 5:
                zzgj zzgjVar = zzn;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzid$zzc.class) {
                        try {
                            zzgj zzgjVar3 = zzn;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzm);
                                zzn = zzaVar;
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
