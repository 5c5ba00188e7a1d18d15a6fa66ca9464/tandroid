package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzci$zzb extends zzeo implements zzgb {
    private static final zzci$zzb zzj;
    private static volatile zzgj zzk;
    private int zzc;
    private int zzd;
    private int zze;
    private int zzf;
    private boolean zzg;
    private boolean zzh;
    private float zzi;

    public enum zza implements zzet {
        zza(0),
        zzb(1),
        zzc(2);

        private static final zzes zzd = new zzcl();
        private final int zze;

        zza(int i) {
            this.zze = i;
        }

        public static zzev zzb() {
            return zzck.zza;
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
            super(zzci$zzb.zzj);
        }

        /* synthetic */ zzb(zzch zzchVar) {
            this();
        }
    }

    public enum zzc implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3);

        private static final zzes zze = new zzcm();
        private final int zzf;

        zzc(int i) {
            this.zzf = i;
        }

        public static zzev zzb() {
            return zzcn.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzc.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzf + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzf;
        }
    }

    public enum zzd implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3);

        private static final zzes zze = new zzcp();
        private final int zzf;

        zzd(int i) {
            this.zzf = i;
        }

        public static zzev zzb() {
            return zzco.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzd.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzf + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzf;
        }
    }

    static {
        zzci$zzb zzci_zzb = new zzci$zzb();
        zzj = zzci_zzb;
        zzeo.zza(zzci$zzb.class, zzci_zzb);
    }

    private zzci$zzb() {
    }

    /* JADX WARN: Type inference failed for: r5v21, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzch zzchVar = null;
        switch (zzch.zza[i - 1]) {
            case 1:
                return new zzci$zzb();
            case 2:
                return new zzb(zzchVar);
            case 3:
                return zzeo.zza(zzj, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0000\u0000\u0001ဌ\u0000\u0002ဌ\u0001\u0003ဌ\u0002\u0004ဇ\u0003\u0005ဇ\u0004\u0006ခ\u0005", new Object[]{"zzc", "zzd", zzd.zzb(), "zze", zzc.zzb(), "zzf", zza.zzb(), "zzg", "zzh", "zzi"});
            case 4:
                return zzj;
            case 5:
                zzgj zzgjVar = zzk;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzci$zzb.class) {
                        try {
                            zzgj zzgjVar3 = zzk;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzj);
                                zzk = zzaVar;
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
