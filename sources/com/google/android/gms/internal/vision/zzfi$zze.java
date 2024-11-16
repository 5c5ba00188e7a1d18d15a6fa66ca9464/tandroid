package com.google.android.gms.internal.vision;

import com.google.android.gms.internal.vision.zzjb;

/* loaded from: classes.dex */
public final class zzfi$zze extends zzjb implements zzkm {
    private static final zzfi$zze zzl;
    private static volatile zzkx zzm;
    private int zzc;
    private boolean zze;
    private int zzf;
    private long zzg;
    private long zzh;
    private long zzi;
    private boolean zzk;
    private String zzd = "";
    private String zzj = "";

    public static final class zza extends zzjb.zzb implements zzkm {
        private zza() {
            super(zzfi$zze.zzl);
        }

        /* synthetic */ zza(zzfk zzfkVar) {
            this();
        }
    }

    public enum zzb implements zzje {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3);

        private static final zzjh zze = new zzfm();
        private final int zzf;

        zzb(int i) {
            this.zzf = i;
        }

        public static zzb zza(int i) {
            if (i == 0) {
                return zza;
            }
            if (i == 1) {
                return zzb;
            }
            if (i == 2) {
                return zzc;
            }
            if (i != 3) {
                return null;
            }
            return zzd;
        }

        public static zzjg zzb() {
            return zzfn.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzb.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzf + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.vision.zzje
        public final int zza() {
            return this.zzf;
        }
    }

    static {
        zzfi$zze zzfi_zze = new zzfi$zze();
        zzl = zzfi_zze;
        zzjb.zza(zzfi$zze.class, zzfi_zze);
    }

    private zzfi$zze() {
    }

    /* JADX WARN: Type inference failed for: r3v19, types: [com.google.android.gms.internal.vision.zzjb$zza, com.google.android.gms.internal.vision.zzkx] */
    @Override // com.google.android.gms.internal.vision.zzjb
    protected final Object zza(int i, Object obj, Object obj2) {
        zzfk zzfkVar = null;
        switch (zzfk.zza[i - 1]) {
            case 1:
                return new zzfi$zze();
            case 2:
                return new zza(zzfkVar);
            case 3:
                return zzjb.zza(zzl, "\u0001\b\u0000\u0001\u0001\b\b\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဇ\u0001\u0003ဌ\u0002\u0004ဂ\u0003\u0005ဂ\u0004\u0006ဂ\u0005\u0007ဈ\u0006\bဇ\u0007", new Object[]{"zzc", "zzd", "zze", "zzf", zzb.zzb(), "zzg", "zzh", "zzi", "zzj", "zzk"});
            case 4:
                return zzl;
            case 5:
                zzkx zzkxVar = zzm;
                zzkx zzkxVar2 = zzkxVar;
                if (zzkxVar == null) {
                    synchronized (zzfi$zze.class) {
                        try {
                            zzkx zzkxVar3 = zzm;
                            zzkx zzkxVar4 = zzkxVar3;
                            if (zzkxVar3 == null) {
                                ?? zzaVar = new zzjb.zza(zzl);
                                zzm = zzaVar;
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
