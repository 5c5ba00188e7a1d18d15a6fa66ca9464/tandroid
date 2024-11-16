package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzbc extends zzeo implements zzgb {
    private static final zzy$zzbc zzi;
    private static volatile zzgj zzj;
    private int zzc;
    private zzy$zzaf zzd;
    private zzew zze = zzeo.zzl();
    private int zzf;
    private int zzg;
    private int zzh;

    public enum zza implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3),
        zze(101),
        zzf(1001),
        zzg(1002);

        private static final zzes zzh = new zzbx();
        private final int zzi;

        zza(int i) {
            this.zzi = i;
        }

        public static zzev zzb() {
            return zzbw.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzi + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzi;
        }
    }

    public static final class zzb extends zzeo.zzb implements zzgb {
        private zzb() {
            super(zzy$zzbc.zzi);
        }

        /* synthetic */ zzb(zzx zzxVar) {
            this();
        }
    }

    public static final class zzc extends zzeo implements zzgb {
        private static final zzc zze;
        private static volatile zzgj zzf;
        private int zzc;
        private float zzd;

        public static final class zza extends zzeo.zzb implements zzgb {
            private zza() {
                super(zzc.zze);
            }

            /* synthetic */ zza(zzx zzxVar) {
                this();
            }
        }

        static {
            zzc zzcVar = new zzc();
            zze = zzcVar;
            zzeo.zza(zzc.class, zzcVar);
        }

        private zzc() {
        }

        /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
        @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
        protected final Object zza(int i, Object obj, Object obj2) {
            zzx zzxVar = null;
            switch (zzx.zza[i - 1]) {
                case 1:
                    return new zzc();
                case 2:
                    return new zza(zzxVar);
                case 3:
                    return zzeo.zza(zze, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0001ခ\u0000", new Object[]{"zzc", "zzd"});
                case 4:
                    return zze;
                case 5:
                    zzgj zzgjVar = zzf;
                    zzgj zzgjVar2 = zzgjVar;
                    if (zzgjVar == null) {
                        synchronized (zzc.class) {
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

    static {
        zzy$zzbc zzy_zzbc = new zzy$zzbc();
        zzi = zzy_zzbc;
        zzeo.zza(zzy$zzbc.class, zzy_zzbc);
    }

    private zzy$zzbc() {
    }

    /* JADX WARN: Type inference failed for: r3v16, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzbc();
            case 2:
                return new zzb(zzxVar);
            case 3:
                return zzeo.zza(zzi, "\u0001\u0005\u0000\u0001\u0001\u0005\u0005\u0000\u0001\u0000\u0001ဉ\u0000\u0002\u001b\u0003ဌ\u0001\u0004င\u0002\u0005င\u0003", new Object[]{"zzc", "zzd", "zze", zzc.class, "zzf", zza.zzb(), "zzg", "zzh"});
            case 4:
                return zzi;
            case 5:
                zzgj zzgjVar = zzj;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzbc.class) {
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
