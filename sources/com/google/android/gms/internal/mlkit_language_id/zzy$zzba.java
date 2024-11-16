package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzba extends zzeo implements zzgb {
    private static final zzy$zzba zzg;
    private static volatile zzgj zzh;
    private int zzc;
    private int zzd;
    private int zze;
    private int zzf;

    public enum zza implements zzet {
        zza(0),
        zzb(1),
        zzc(2);

        private static final zzes zzd = new zzbt();
        private final int zze;

        zza(int i) {
            this.zze = i;
        }

        public static zzev zzb() {
            return zzbs.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zze + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zze;
        }
    }

    public static final class zzb extends zzeo.zzb implements zzgb {
        private zzb() {
            super(zzy$zzba.zzg);
        }

        /* synthetic */ zzb(zzx zzxVar) {
            this();
        }
    }

    public enum zzc implements zzet {
        zza(0),
        zzb(1),
        zzc(2);

        private static final zzes zzd = new zzbu();
        private final int zze;

        zzc(int i) {
            this.zze = i;
        }

        public static zzev zzb() {
            return zzbv.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzc.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zze + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zze;
        }
    }

    static {
        zzy$zzba zzy_zzba = new zzy$zzba();
        zzg = zzy_zzba;
        zzeo.zza(zzy$zzba.class, zzy_zzba);
    }

    private zzy$zzba() {
    }

    /* JADX WARN: Type inference failed for: r5v18, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzba();
            case 2:
                return new zzb(zzxVar);
            case 3:
                return zzeo.zza(zzg, "\u0001\u0003\u0000\u0001\u0001\u0003\u0003\u0000\u0000\u0000\u0001ဌ\u0000\u0002ဌ\u0001\u0003ဌ\u0002", new Object[]{"zzc", "zzd", zza.zzb(), "zze", zzc.zzb(), "zzf", zzc.zzb()});
            case 4:
                return zzg;
            case 5:
                zzgj zzgjVar = zzh;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzba.class) {
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
