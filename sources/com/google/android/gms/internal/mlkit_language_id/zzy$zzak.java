package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzy$zzak extends zzeo implements zzgb {
    private static final zzy$zzak zzk;
    private static volatile zzgj zzl;
    private int zzc;
    private zzy$zzam zzd;
    private long zze;
    private int zzf;
    private long zzg;
    private int zzh;
    private long zzi;
    private zzeu zzj = zzeo.zzk();

    /* loaded from: classes.dex */
    public enum zza implements zzet {
        zza(0),
        zzb(1),
        zzc(2),
        zzd(3),
        zze(4),
        zzf(5),
        zzg(6),
        zzh(7),
        zzi(8),
        zzj(9),
        zzk(10),
        zzl(11),
        zzm(12);

        private static final zzes zzn = new zzbc();
        private final int zzo;

        zza(int i) {
            this.zzo = i;
        }

        public static zzev zzb() {
            return zzbb.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zza.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzo + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzo;
        }
    }

    /* loaded from: classes.dex */
    public static final class zzb extends zzeo.zzb implements zzgb {
        private zzb() {
            super(zzy$zzak.zzk);
        }

        /* synthetic */ zzb(zzx zzxVar) {
            this();
        }
    }

    static {
        zzy$zzak zzy_zzak = new zzy$zzak();
        zzk = zzy_zzak;
        zzeo.zza(zzy$zzak.class, zzy_zzak);
    }

    private zzy$zzak() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Type inference failed for: r4v19, types: [com.google.android.gms.internal.mlkit_language_id.zzgj, com.google.android.gms.internal.mlkit_language_id.zzeo$zza] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    public final Object zza(int i, Object obj, Object obj2) {
        zzx zzxVar = null;
        switch (zzx.zza[i - 1]) {
            case 1:
                return new zzy$zzak();
            case 2:
                return new zzb(zzxVar);
            case 3:
                return zzeo.zza(zzk, "\u0001\u0007\u0000\u0001\u0001\u0007\u0007\u0000\u0001\u0000\u0001ဉ\u0000\u0002ဃ\u0001\u0003ဌ\u0002\u0004ဃ\u0003\u0005ဌ\u0004\u0006ဂ\u0005\u0007\u0016", new Object[]{"zzc", "zzd", "zze", "zzf", zzai.zzb(), "zzg", "zzh", zza.zzb(), "zzi", "zzj"});
            case 4:
                return zzk;
            case 5:
                zzgj zzgjVar = zzl;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzy$zzak.class) {
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
