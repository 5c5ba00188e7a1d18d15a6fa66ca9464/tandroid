package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
/* loaded from: classes.dex */
public final class zzy$zzal extends zzeo implements zzgb {
    private static final zzy$zzal zzl;
    private static volatile zzgj zzm;
    private int zzc;
    private int zzf;
    private int zzi;
    private long zzj;
    private boolean zzk;
    private String zzd = "";
    private String zze = "";
    private String zzg = "";
    private String zzh = "";

    /* loaded from: classes.dex */
    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzy$zzal.zzl);
        }

        /* synthetic */ zza(zzx zzxVar) {
            this();
        }
    }

    /* loaded from: classes.dex */
    public enum zzb implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3),
        zze(4),
        zzf(5),
        zzg(6);
        
        private static final zzes zzh = new zzbd();
        private final int zzi;

        zzb(int i) {
            this.zzi = i;
        }

        public static zzev zzb() {
            return zzbe.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzb.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzi + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzi;
        }
    }

    /* loaded from: classes.dex */
    public enum zzc implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3),
        zze(4),
        zzf(5);
        
        private static final zzes zzg = new zzbg();
        private final int zzh;

        zzc(int i) {
            this.zzh = i;
        }

        public static zzev zzb() {
            return zzbf.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzc.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzh + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzh;
        }
    }

    static {
        zzy$zzal zzy_zzal = new zzy$zzal();
        zzl = zzy_zzal;
        zzeo.zza(zzy$zzal.class, zzy_zzal);
    }

    private zzy$zzal() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r4v20, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzal();
            case 2:
                return new zza(null);
            case 3:
                return zzeo.zza(zzl, "\u0001\b\u0000\u0001\u0001\b\b\u0000\u0000\u0000\u0001ဈ\u0000\u0002ဈ\u0001\u0003ဌ\u0002\u0004ဈ\u0003\u0005ဈ\u0004\u0006ဌ\u0005\u0007ဃ\u0006\bဇ\u0007", new Object[]{"zzc", "zzd", "zze", "zzf", zzc.zzb(), "zzg", "zzh", "zzi", zzb.zzb(), "zzj", "zzk"});
            case 4:
                return zzl;
            case 5:
                zzgj zzgjVar = zzm;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzal.class) {
                        try {
                            zzgj zzgjVar3 = zzm;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzl);
                                zzm = zzaVar;
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
