package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;

/* loaded from: classes.dex */
public final class zzfi$zzg extends zzjb implements zzkm {
    private static final zzfi$zzg zzj;
    private static volatile zzkx zzk;
    private int zzc;
    private int zzd;
    private int zze;
    private int zzf;
    private boolean zzg;
    private boolean zzh;
    private float zzi;

    public static final class zza extends zzjb.zzb implements zzkm {
        private zza() {
            super(zzfi$zzg.zzj);
        }

        /* synthetic */ zza(zzfk zzfkVar) {
            this();
        }
    }

    public enum zzb implements zzje {
        zzc(0),
        zza(1),
        zzb(2);

        private static final zzjh zzd = new zzfq();
        private final int zze;

        zzb(int i) {
            this.zze = i;
        }

        public static zzb zza(int i) {
            if (i == 0) {
                return zzc;
            }
            if (i == 1) {
                return zza;
            }
            if (i != 2) {
                return null;
            }
            return zzb;
        }

        public static zzjg zzb() {
            return zzfr.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzb.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zze + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.vision.zzje
        public final int zza() {
            return this.zze;
        }
    }

    public enum zzc implements zzje {
        zzd(0),
        zza(1),
        zzb(2),
        zzc(3);

        private static final zzjh zze = new zzft();
        private final int zzf;

        zzc(int i) {
            this.zzf = i;
        }

        public static zzc zza(int i) {
            if (i == 0) {
                return zzd;
            }
            if (i == 1) {
                return zza;
            }
            if (i == 2) {
                return zzb;
            }
            if (i != 3) {
                return null;
            }
            return zzc;
        }

        public static zzjg zzb() {
            return zzfs.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzc.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzf + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.vision.zzje
        public final int zza() {
            return this.zzf;
        }
    }

    public enum zzd implements zzje {
        zzd(0),
        zza(1),
        zzb(2),
        zzc(3);

        private static final zzjh zze = new zzfu();
        private final int zzf;

        zzd(int i) {
            this.zzf = i;
        }

        public static zzd zza(int i) {
            if (i == 0) {
                return zzd;
            }
            if (i == 1) {
                return zza;
            }
            if (i == 2) {
                return zzb;
            }
            if (i != 3) {
                return null;
            }
            return zzc;
        }

        public static zzjg zzb() {
            return zzfv.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzd.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzf + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.vision.zzje
        public final int zza() {
            return this.zzf;
        }
    }

    static {
        zzfi$zzg zzfi_zzg = new zzfi$zzg();
        zzj = zzfi_zzg;
        zzjb.zza(zzfi$zzg.class, zzfi_zzg);
    }

    private zzfi$zzg() {
    }

    /* JADX WARN: Type inference failed for: r5v21, types: [com.google.android.gms.internal.vision.zzjb$zza, com.google.android.gms.internal.vision.zzkx] */
    @Override // com.google.android.gms.internal.vision.zzjb
    protected final Object zza(int i, Object obj, Object obj2) {
        zzfk zzfkVar = null;
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zzg();
            case 2:
                return new zza(zzfkVar);
            case 3:
                return zzjb.zza(zzj, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0000\u0000\u0001ဌ\u0000\u0002ဌ\u0001\u0003ဌ\u0002\u0004ဇ\u0003\u0005ဇ\u0004\u0006ခ\u0005", new Object[]{"zzc", "zzd", zzd.zzb(), "zze", zzc.zzb(), "zzf", zzb.zzb(), "zzg", "zzh", "zzi"});
            case 4:
                return zzj;
            case 5:
                zzkx zzkxVar = zzk;
                zzkx zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zzg.class) {
                        try {
                            zzkx zzkxVar3 = zzk;
                            zzkx zzkxVar4 = zzkxVar3;
                            if (zzkxVar3 == null) {
                                ?? zzaVar = new zzjb.zza(zzj);
                                zzk = zzaVar;
                                zzkxVar4 = zzaVar;
                            }
                        } finally {
                        }
                    }
                }
                return zzkxVar2;
            case 6:
                return (byte) 1;
            case 7:
                return null;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
