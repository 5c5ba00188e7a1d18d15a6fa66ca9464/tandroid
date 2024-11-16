package com.google.android.gms.internal.mlkit_language_id;

import com.google.android.gms.internal.mlkit_language_id.zzeo;

/* loaded from: classes.dex */
public final class zzid$zza extends zzeo implements zzgb {
    private static final zzid$zza zzf;
    private static volatile zzgj zzg;
    private int zzc;
    private int zzd;
    private zzid$zzj zze;

    public static final class zza extends zzeo.zzb implements zzgb {
        private zza() {
            super(zzid$zza.zzf);
        }

        /* synthetic */ zza(zzic zzicVar) {
            this();
        }
    }

    public enum zzb implements zzet {
        zza(0),
        zzb(1);

        private static final zzes zzc = new zzie();
        private final int zzd;

        zzb(int i) {
            this.zzd = i;
        }

        public static zzev zzb() {
            return zzif.zza;
        }

        @Override // java.lang.Enum
        public final String toString() {
            return "<" + zzb.class.getName() + '@' + Integer.toHexString(System.identityHashCode(this)) + " number=" + this.zzd + " name=" + name() + '>';
        }

        @Override // com.google.android.gms.internal.mlkit_language_id.zzet
        public final int zza() {
            return this.zzd;
        }
    }

    static {
        zzid$zza zzid_zza = new zzid$zza();
        zzf = zzid_zza;
        zzeo.zza(zzid$zza.class, zzid_zza);
    }

    private zzid$zza() {
    }

    /* JADX WARN: Type inference failed for: r3v15, types: [com.google.android.gms.internal.mlkit_language_id.zzeo$zza, com.google.android.gms.internal.mlkit_language_id.zzgj] */
    @Override // com.google.android.gms.internal.mlkit_language_id.zzeo
    protected final Object zza(int i, Object obj, Object obj2) {
        zzic zzicVar = null;
        switch (zzic.zza[i - 1]) {
            case 1:
                return new zzid$zza();
            case 2:
                return new zza(zzicVar);
            case 3:
                return zzeo.zza(zzf, "\u0001\u0002\u0000\u0001\u0001\u0002\u0002\u0000\u0000\u0000\u0001ဌ\u0000\u0002ဉ\u0001", new Object[]{"zzc", "zzd", zzb.zzb(), "zze"});
            case 4:
                return zzf;
            case 5:
                zzgj zzgjVar = zzg;
                zzgj zzgjVar2 = zzgjVar;
                if (zzgjVar == null) {
                    synchronized (zzid$zza.class) {
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
