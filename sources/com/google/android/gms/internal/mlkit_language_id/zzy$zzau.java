package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
import com.google.android.gms.internal.mlkit_language_id.zzy$zzaf;

/* loaded from: classes.dex */
public final class zzy$zzau extends zzeo implements zzgb {
    private static final zzy$zzau zzh;
    private static volatile zzgj zzi;
    private int zzc;
    private zzy$zzaf zzd;
    private zzy$zzai zze;
    private zzc zzf;
    private zzd zzg;

    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzau.zzh);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }

        public final zza zza(zzy$zzaf.zza zzaVar) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzau) this.zza).zza((zzy$zzaf) ((zzeo) zzaVar.zzg()));
            return this;
        }

        public final zza zza(zzy$zzai zzy_zzai) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzau) this.zza).zza(zzy_zzai);
            return this;
        }

        public final zza zza(zzc zzcVar) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzau) this.zza).zza(zzcVar);
            return this;
        }

        public final zza zza(zzd zzdVar) {
            if (this.zzb) {
                zzc();
                this.zzb = false;
            }
            ((zzy$zzau) this.zza).zza(zzdVar);
            return this;
        }
    }

    /* loaded from: classes.dex */
    public static final class zzb extends zzeo implements zzgb {
        private static final zzb zzf;
        private static volatile zzgj zzg;
        private int zzc;
        private float zzd;
        private String zze = "";

        /* loaded from: classes.dex */
        public static final class zza extends zzeo.zzb implements zzgb {
            private zza() {
                super(zzb.zzf);
            }

            /* synthetic */ zza(zzx zzxVar) {
                this();
            }

            public final zza zza(String str) {
                if (this.zzb) {
                    zzc();
                    this.zzb = false;
                }
                ((zzb) this.zza).zza(str);
                return this;
            }
        }

        static {
            zzb zzbVar = new zzb();
            zzf = zzbVar;
            zzeo.zza(zzb.class, zzbVar);
        }

        private zzb() {
        }

        public static zza zza() {
            return (zza) zzf.zzh();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void zza(String str) {
            str.getClass();
            this.zzc |= 2;
            this.zze = str;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
        @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
        public final Object zza(int i, Object obj, Object obj2) {
            zzx zzxVar = null;
            switch (zzx.zza[i - 1]) {
                case 1:
                    return new zzb();
                case 2:
                    return new zza(zzxVar);
                case 3:
                    return zzeo.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ခ\u0000\u0002ဈ\u0001", new Object[]{"zzc", "zzd", "zze"});
                case 4:
                    return zzf;
                case 5:
                    zzgj zzgjVar = zzg;
                    zzgj zzgjVar2 = zzgjVar;
                    if (zzgjVar == null) {
                        synchronized (zzb.class) {
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

    /* loaded from: classes.dex */
    public static final class zzc extends zzeo implements zzgb {
        private static final zzc zze;
        private static volatile zzgj zzf;
        private int zzc;
        private zzb zzd;

        /* loaded from: classes.dex */
        public static final class zza extends zzeo.zzb implements zzgb {
            private zza() {
                super(zzc.zze);
            }

            /* synthetic */ zza(zzx zzxVar) {
                this();
            }

            public final zza zza(zzb.zza zzaVar) {
                if (this.zzb) {
                    zzc();
                    this.zzb = false;
                }
                ((zzc) this.zza).zza((zzb) ((zzeo) zzaVar.zzg()));
                return this;
            }
        }

        static {
            zzc zzcVar = new zzc();
            zze = zzcVar;
            zzeo.zza(zzc.class, zzcVar);
        }

        private zzc() {
        }

        public static zza zza() {
            return (zza) zze.zzh();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public final void zza(zzb zzbVar) {
            zzbVar.getClass();
            this.zzd = zzbVar;
            this.zzc |= 1;
        }

        public static zzc zzb() {
            return zze;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
        @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
        public final Object zza(int i, Object obj, Object obj2) {
            zzx zzxVar = null;
            switch (zzx.zza[i - 1]) {
                case 1:
                    return new zzc();
                case 2:
                    return new zza(zzxVar);
                case 3:
                    return zzeo.zza(zze, "\u0001\u0001\u0000\u0001\u0001\u0001\u0001\u0000\u0000\u0000\u0001ဉ\u0000", new Object[]{"zzc", "zzd"});
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

    /* loaded from: classes.dex */
    public static final class zzd extends zzeo implements zzgb {
        private static final zzd zzd;
        private static volatile zzgj zze;
        private zzew zzc = zzeo.zzl();

        /* loaded from: classes.dex */
        public static final class zza extends zzeo.zzb implements zzgb {
            private zza() {
                super(zzd.zzd);
            }

            /* synthetic */ zza(zzx zzxVar) {
                this();
            }
        }

        static {
            zzd zzdVar = new zzd();
            zzd = zzdVar;
            zzeo.zza(zzd.class, zzdVar);
        }

        private zzd() {
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
        @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
        public final Object zza(int i, Object obj, Object obj2) {
            zzx zzxVar = null;
            switch (zzx.zza[i - 1]) {
                case 1:
                    return new zzd();
                case 2:
                    return new zza(zzxVar);
                case 3:
                    return zzeo.zza(zzd, "\u0001\u0001\u0000\u0000\u0001\u0001\u0001\u0000\u0001\u0000\u0001\u001b", new Object[]{"zzc", zzb.class});
                case 4:
                    return zzd;
                case 5:
                    zzgj zzgjVar = zze;
                    zzgj zzgjVar2 = zzgjVar;
                    if (zzgjVar == null) {
                        synchronized (zzd.class) {
                            try {
                                zzgj zzgjVar3 = zze;
                                zzgj zzgjVar4 = zzgjVar3;
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
    }

    static {
        zzy$zzau zzy_zzau = new zzy$zzau();
        zzh = zzy_zzau;
        zzeo.zza(zzy$zzau.class, zzy_zzau);
    }

    private zzy$zzau() {
    }

    public static zza zza() {
        return (zza) zzh.zzh();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(zzy$zzaf zzy_zzaf) {
        zzy_zzaf.getClass();
        this.zzd = zzy_zzaf;
        this.zzc |= 1;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(zzy$zzai zzy_zzai) {
        zzy_zzai.getClass();
        this.zze = zzy_zzai;
        this.zzc |= 2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(zzc zzcVar) {
        zzcVar.getClass();
        this.zzf = zzcVar;
        this.zzc |= 4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void zza(zzd zzdVar) {
        zzdVar.getClass();
        this.zzg = zzdVar;
        this.zzc |= 8;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r2v14, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzau();
            case 2:
                return new zza(zzxVar);
            case 3:
                return zzeo.zza(zzh, "\u0001\u0004\u0000\u0001\u0001\u0004\u0004\u0000\u0000\u0000\u0001ဉ\u0000\u0002ဉ\u0001\u0003ဉ\u0002\u0004ဉ\u0003", new Object[]{"zzc", "zzd", "zze", "zzf", "zzg"});
            case 4:
                return zzh;
            case 5:
                zzgj zzgjVar = zzi;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzau.class) {
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
