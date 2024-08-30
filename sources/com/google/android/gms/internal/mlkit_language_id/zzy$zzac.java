package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
/* loaded from: classes.dex */
public final class zzy$zzac extends zzeo implements zzgb {
    private static final zzy$zzac zzj;
    private static volatile zzgj zzk;
    private int zzc;
    private int zzd;
    private int zze;
    private int zzf;
    private int zzg;
    private boolean zzh;
    private float zzi;

    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzac.zzj);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    /* loaded from: classes.dex */
    public enum zzb implements zzet {
        zza(0),
        zzb(1),
        zzc(2);
        
        private static final zzes zzd = new zzan();
        private final int zze;

        zzb(int i) {
            this.zze = i;
        }

        public static zzev zzb() {
            return zzao.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzb.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zze + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zze;
        }
    }

    /* loaded from: classes.dex */
    public enum zzc implements zzet {
        zza(0),
        zzb(1),
        zzc(2);
        
        private static final zzes zzd = new zzaq();
        private final int zze;

        zzc(int i) {
            this.zze = i;
        }

        public static zzev zzb() {
            return zzap.zza;
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

    /* loaded from: classes.dex */
    public enum zzd implements zzet {
        zza(0),
        zzb(1),
        zzc(2);
        
        private static final zzes zzd = new zzar();
        private final int zze;

        zzd(int i) {
            this.zze = i;
        }

        public static zzev zzb() {
            return zzas.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzd.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zze + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zze;
        }
    }

    /* loaded from: classes.dex */
    public enum zze implements zzet {
        zza(0),
        zzb(1),
        zzc(2);
        
        private static final zzes zzd = new zzau();
        private final int zze;

        zze(int i) {
            this.zze = i;
        }

        public static zzev zzb() {
            return zzat.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zze.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zze + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zze;
        }
    }

    static {
        zzy$zzac zzy_zzac = new zzy$zzac();
        zzj = zzy_zzac;
        zzeo.zza(zzy$zzac.class, zzy_zzac);
    }

    private zzy$zzac() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r6v22, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzac();
            case 2:
                return new zza(null);
            case 3:
                return zzeo.zza(zzj, "\u0001\u0006\u0000\u0001\u0001\u0006\u0006\u0000\u0000\u0000\u0001ဌ\u0000\u0002ဌ\u0001\u0003ဌ\u0002\u0004ဌ\u0003\u0005ဇ\u0004\u0006ခ\u0005", new Object[]{"zzc", "zzd", zzd.zzb(), "zze", zzb.zzb(), "zzf", zze.zzb(), "zzg", zzc.zzb(), "zzh", "zzi"});
            case 4:
                return zzj;
            case 5:
                zzgj zzgjVar = zzk;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzac.class) {
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
