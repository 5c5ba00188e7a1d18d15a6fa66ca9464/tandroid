package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;
import com.google.android.gms.internal.mlkit_language_id.zzy$zzae;

/* loaded from: classes.dex */
public final class zzy$zzag extends zzeo implements zzgb {
    private static final zzy$zzag zzk;
    private static volatile zzgj zzl;
    private int zzc;
    private long zzd;
    private int zze;
    private int zzf;
    private int zzg;
    private int zzh;
    private int zzi;
    private int zzj;

    public enum zza implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3),
        zze(4),
        zzf(5);

        private static final zzes zzg = new zzay();
        private final int zzh;

        zza(int i) {
            this.zzh = i;
        }

        public static zzev zzb() {
            return zzax.zza;
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
            super(zzy$zzag.zzk);
        }

        /* synthetic */ zzb(zzx zzxVar) {
            this();
        }
    }

    static {
        zzy$zzag zzy_zzag = new zzy$zzag();
        zzk = zzy_zzag;
        zzeo.zza(zzy$zzag.class, zzy_zzag);
    }

    private zzy$zzag() {
    }

    /* JADX WARN: Type inference failed for: r4v20, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzag();
            case 2:
                return new zzb(zzxVar);
            case 3:
                return zzeo.zza(zzk, "\u0001\u0007\u0000\u0001\u0001\u0007\u0007\u0000\u0000\u0000\u0001ဃ\u0000\u0002ဌ\u0001\u0003ဌ\u0002\u0004ဋ\u0003\u0005ဋ\u0004\u0006ဋ\u0005\u0007ဋ\u0006", new Object[]{"zzc", "zzd", "zze", zza.zzb(), "zzf", zzy$zzae.zzb.zzb(), "zzg", "zzh", "zzi", "zzj"});
            case 4:
                return zzk;
            case 5:
                zzgj zzgjVar = zzl;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzag.class) {
                        try {
                            zzgj zzgjVar3 = zzl;
                            zzgj zzgjVar4 = zzgjVar3;
                            if (zzgjVar3 == null) {
                                ?? zzaVar = new zzeo.zza(zzk);
                                zzl = zzaVar;
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
